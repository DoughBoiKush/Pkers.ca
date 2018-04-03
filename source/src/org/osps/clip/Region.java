package org.osps.clip;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.osps.cache.RSObjectDefinition;
import org.osps.model.content.teleport.Position;
import org.osps.model.npcs.NPC;
import org.osps.model.players.Player;
import org.osps.util.Misc;
import org.osps.world.GameObject;
import org.osps.world.WorldObject;

import com.google.common.io.ByteStreams;

public class Region {

	/**
	 * An array of {@link WorldObject} objects that will be added after the maps
	 * have been loaded.
	 */
	private static final WorldObject[] EXISTANT_OBJECTS = { 
			
			new WorldObject(25808, 2729, 3494, 0, 0),
			new WorldObject(25808, 2728, 3494, 0, 0), 
			new WorldObject(25808, 2727, 3494, 0, 0),
			new WorldObject(25808, 2724, 3494, 0, 0), 
			new WorldObject(25808, 2722, 3494, 0, 0),
			new WorldObject(25808, 2721, 3494, 0, 0), 
			new WorldObject(6552, 3088, 3511, 0, 0),
			
	};

	/**
	 * A map containing each region as the key, and a Collection of real world
	 * objects as the value.
	 */
	private static HashMap<Integer, ArrayList<WorldObject>> worldObjects = new HashMap<>();

	/**
	 * Determines if an object is real or not. If the Collection of regions and
	 * real objects contains the properties passed in the parameters then the
	 * object will be determined real
	 * 
	 * @param id
	 *            the id of the object
	 * @param x
	 *            the x coordinate of the object
	 * @param y
	 *            the y coordinate of the object
	 * @param height
	 *            the height of the object
	 * @return
	 */
	public static boolean isWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return true;
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (regionObjects == null) {
			return true;
		}
		Optional<WorldObject> exists = regionObjects.stream()
				.filter(object -> object.id == id && object.x == x && object.y == y && object.height == height)
				.findFirst();
		return exists.isPresent();
	}

	public static Optional<WorldObject> getWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return Optional.empty();
		}
		Collection<WorldObject> regionObjects = worldObjects.get(region.id);
		if (regionObjects == null) {
			return Optional.empty();
		}
		Optional<WorldObject> exists = regionObjects.stream()
				.filter(object -> object.id == id && object.x == x && object.y == y && object.height == height)
				.findFirst();
		return exists;
	}

	/**
	 * Adds a {@link WorldObject} to the {@link worldObjects} map based on the
	 * x, y, height, and identification of the object.
	 * 
	 * @param id
	 *            the id of the object
	 * @param x
	 *            the x position of the object
	 * @param y
	 *            the y position of the object
	 * @param height
	 *            the height of the object
	 */
	public static void addWorldObject(int id, int x, int y, int height, int face) {
		Region region = getRegion(x, y);
		if (region == null) {
			return;
		}
		int regionId = region.id;
		if (worldObjects.containsKey(regionId)) {
			if (objectExists(regionId, id, x, y, height)) {
				return;
			}
			worldObjects.get(regionId).add(new WorldObject(id, x, y, height, face));
		} else {
			ArrayList<WorldObject> object = new ArrayList<>(1);
			object.add(new WorldObject(id, x, y, height, face));
			worldObjects.put(regionId, object);
		}
	}

	public static void removeWorldObject(int id, int x, int y, int height) {
		Region region = getRegion(x, y);
		if (region == null) {
			return;
		}
		int regionId = region.id;
		if (worldObjects.containsKey(regionId)) {
			List<WorldObject> objects = worldObjects.get(regionId);
			for (WorldObject object : objects) {
				if (object == null) {
					continue;
				}
				if (object.getId() == id && object.getX() == x && object.getY() == y && object.getHeight() == height) {
					objects.remove(object);
					if (objects.isEmpty()) {
						worldObjects.remove(regionId);
					}
				}
			}
		}
	}

	/**
	 * A convenience method for lamda expressions
	 * 
	 * @param object
	 *            the world object being added
	 */
	private static void addWorldObject(WorldObject object) {
		addWorldObject(object.getId(), object.getX(), object.getY(), object.getHeight(), object.getFace());
	}

	/**
	 * Determines if an object exists in a region
	 * 
	 * @param region
	 *            the region
	 * @param id
	 *            the object id
	 * @param x`
	 *            the object x pos
	 * @param y
	 *            the object y pos
	 * @param height
	 *            the object z pos
	 * @return true if the object exists in the region, otherwise false
	 */
	private static boolean objectExists(int region, int id, int x, int y, int height) {
		List<WorldObject> objects = worldObjects.get(region);
		for (WorldObject object : objects) {
			if (object == null) {
				continue;
			}
			if (object.getId() == id && object.getX() == x && object.getY() == y && object.getHeight() == height) {
				return true;
			}
		}
		return false;
	}

	private void addProjectileClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (shootable[height] == null) {
			shootable[height] = new int[64][64];
		}
		shootable[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	private void removeProjectileClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (shootable[height] == null) {
			shootable[height] = new int[64][64];
		}
		shootable[height][x - regionAbsX][y - regionAbsY] += shift;
	}

	private void addClip(int x, int y, int height, int shift) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			clips[height] = new int[64][64];
		}
		clips[height][x - regionAbsX][y - regionAbsY] |= shift;
	}

	private int getClip(int x, int y, int height) {
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (clips[height] == null) {
			return 0;
		}
		return clips[height][x - regionAbsX][y - regionAbsY];
	}

	public static int[] getNextStep(int baseX, int baseY, int toX, int toY, int height, int xLength, int yLength) {
		int moveX = 0;
		int moveY = 0;
		if (baseX - toX > 0) {
			moveX--;
		} else if (baseX - toX < 0) {
			moveX++;
		}
		if (baseY - toY > 0) {
			moveY--;
		} else if (baseY - toY < 0) {
			moveY++;
		}
		if (canMove(baseX, baseY, baseX + moveX, baseY + moveY, height, xLength, yLength)) {
			return new int[] { baseX + moveX, baseY + moveY };
		} else if (moveX != 0 && canMove(baseX, baseY, baseX + moveX, baseY, height, xLength, yLength)) {
			return new int[] { baseX + moveX, baseY };
		} else if (moveY != 0 && canMove(baseX, baseY, baseX, baseY + moveY, height, xLength, yLength)) {
			return new int[] { baseX, baseY + moveY };
		}
		return new int[] { baseX, baseY };
	}

	public static boolean pathBlocked(Player attacker, NPC victim) {
		return pathBlocked(new Position(attacker.getX(), attacker.getY(), attacker.getHeightLevel()),
				new Position(victim.getX(), victim.getY(), victim.heightLevel));
	}

	public static boolean pathBlocked(Position source, Position destination) {

		double offsetX = Math.abs(source.getX() - destination.getX());
		double offsetY = Math.abs(source.getY() - destination.getY());

		int distance = Misc.distanceToPoint(source.getX(), source.getY(), destination.getX(), destination.getY());

		if (distance == 0) {
			return true;
		}

		offsetX = offsetX > 0 ? offsetX / distance : 0;
		offsetY = offsetY > 0 ? offsetY / distance : 0;

		int[][] path = new int[distance][5];

		int curX = source.getX();
		int curY = source.getY();
		int next = 0;
		int nextMoveX = 0;
		int nextMoveY = 0;

		double currentTileXCount = 0.0;
		double currentTileYCount = 0.0;

		while (distance > 0) {
			distance--;
			nextMoveX = 0;
			nextMoveY = 0;
			if (curX > destination.getX()) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX--;
					curX--;
					currentTileXCount -= offsetX;
				}
			} else if (curX < destination.getX()) {
				currentTileXCount += offsetX;
				if (currentTileXCount >= 1.0) {
					nextMoveX++;
					curX++;
					currentTileXCount -= offsetX;
				}
			}
			if (curY > destination.getY()) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY--;
					curY--;
					currentTileYCount -= offsetY;
				}
			} else if (curY < destination.getY()) {
				currentTileYCount += offsetY;
				if (currentTileYCount >= 1.0) {
					nextMoveY++;
					curY++;
					currentTileYCount -= offsetY;
				}
			}
			path[next][0] = curX;
			path[next][1] = curY;
			path[next][2] = source.getZ();
			path[next][3] = nextMoveX;
			path[next][4] = nextMoveY;
			next++;
		}
		for (int i = 0; i < path.length; i++) {
			if (!getClipping(path[i][0], path[i][1], path[i][2], path[i][3], path[i][4])) {
				return true;
			}
		}
		return false;
	}

	public static boolean canShoot(int x, int y, int z, int direction) {
		if (direction == 0) {
			return !projectileBlockedNorthWest(x, y, z) && !projectileBlockedNorth(x, y, z)
					&& !projectileBlockedWest(x, y, z);
		} else if (direction == 1) {
			return !projectileBlockedNorth(x, y, z);
		} else if (direction == 2) {
			return !projectileBlockedNorthEast(x, y, z) && !projectileBlockedNorth(x, y, z)
					&& !projectileBlockedEast(x, y, z);
		} else if (direction == 3) {
			return !projectileBlockedWest(x, y, z);
		} else if (direction == 4) {
			return !projectileBlockedEast(x, y, z);
		} else if (direction == 5) {
			return !projectileBlockedSouthWest(x, y, z) && !projectileBlockedSouth(x, y, z)
					&& !projectileBlockedWest(x, y, z);
		} else if (direction == 6) {
			return !projectileBlockedSouth(x, y, z);
		} else if (direction == 7) {
			return !projectileBlockedSouthEast(x, y, z) && !projectileBlockedSouth(x, y, z)
					&& !projectileBlockedEast(x, y, z);
		}
		return false;
	}

	public static boolean projectileBlockedNorth(int x, int y, int z) {
		return (getProjectileClipping(x, y + 1, z) & 0x1280120) != 0;
	}

	public static boolean projectileBlockedEast(int x, int y, int z) {
		return (getProjectileClipping(x + 1, y, z) & 0x1280180) != 0;
	}

	public static boolean projectileBlockedSouth(int x, int y, int z) {
		return (getProjectileClipping(x, y - 1, z) & 0x1280102) != 0;
	}

	public static boolean projectileBlockedWest(int x, int y, int z) {
		return (getProjectileClipping(x - 1, y, z) & 0x1280108) != 0;
	}

	public static boolean projectileBlockedNorthEast(int x, int y, int z) {
		return (getProjectileClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
	}

	public static boolean projectileBlockedNorthWest(int x, int y, int z) {
		return (getProjectileClipping(x - 1, y + 1, z) & 0x1280138) != 0;
	}

	public static boolean projectileBlockedSouthEast(int x, int y, int z) {
		return (getProjectileClipping(x + 1, y - 1, z) & 0x1280183) != 0;
	}

	public static boolean projectileBlockedSouthWest(int x, int y, int z) {
		return (getProjectileClipping(x - 1, y - 1, z) & 0x128010e) != 0;
	}

	public boolean canMove(int x, int y, int z, int direction) {
		if (direction == 0) {
			return !blockedNorthWest(x, y, z) && !blockedNorth(x, y, z) && !blockedWest(x, y, z);
		} else if (direction == 1) {
			return !blockedNorth(x, y, z);
		} else if (direction == 2) {
			return !blockedNorthEast(x, y, z) && !blockedNorth(x, y, z) && !blockedEast(x, y, z);
		} else if (direction == 3) {
			return !blockedWest(x, y, z);
		} else if (direction == 4) {
			return !blockedEast(x, y, z);
		} else if (direction == 5) {
			return !blockedSouthWest(x, y, z) && !blockedSouth(x, y, z) && !blockedWest(x, y, z);
		} else if (direction == 6) {
			return !blockedSouth(x, y, z);
		} else if (direction == 7) {
			return !blockedSouthEast(x, y, z) && !blockedSouth(x, y, z) && !blockedEast(x, y, z);
		}
		return false;
	}

	/*
	 * public static boolean canMove(int startX, int startY, int endX, int endY,
	 * int height, int xLength, int yLength) { int diffX = endX - startX; int
	 * diffY = endY - startY; int max = Math.max(Math.abs(diffX),
	 * Math.abs(diffY)); for (int ii = 0; ii < max; ii++) { int currentX = endX
	 * - diffX; int currentY = endY - diffY; for (int i = 0; i < xLength; i++) {
	 * for (int i2 = 0; i2 < yLength; i2++) { if (diffX < 0 && diffY < 0) { if
	 * ((getClipping(currentX + i - 1, currentY + i2 - 1, height) & 0x128010e)
	 * != 0 || (getClipping(currentX + i - 1, currentY + i2, height) &
	 * 0x1280108) != 0 || (getClipping(currentX + i, currentY + i2 - 1, height)
	 * & 0x1280102) != 0) { return false; } } else if (diffX > 0 && diffY > 0) {
	 * if ((getClipping(currentX + i + 1, currentY + i2 + 1, height) &
	 * 0x12801e0) != 0 || (getClipping(currentX + i + 1, currentY + i2, height)
	 * & 0x1280180) != 0 || (getClipping(currentX + i, currentY + i2 + 1,
	 * height) & 0x1280120) != 0) { return false; } } else if (diffX < 0 &&
	 * diffY > 0) { if ((getClipping(currentX + i - 1, currentY + i2 + 1,
	 * height) & 0x1280138) != 0 || (getClipping(currentX + i - 1, currentY +
	 * i2, height) & 0x1280108) != 0 || (getClipping(currentX + i, currentY + i2
	 * + 1, height) & 0x1280120) != 0) { return false; } } else if (diffX > 0 &&
	 * diffY < 0) { if ((getClipping(currentX + i + 1, currentY + i2 - 1,
	 * height) & 0x1280183) != 0 || (getClipping(currentX + i + 1, currentY +
	 * i2, height) & 0x1280180) != 0 || (getClipping(currentX + i, currentY + i2
	 * - 1, height) & 0x1280102) != 0) { return false; } } else if (diffX > 0 &&
	 * diffY == 0) { if ((getClipping(currentX + i + 1, currentY + i2, height) &
	 * 0x1280180) != 0) { return false; } } else if (diffX < 0 && diffY == 0) {
	 * if ((getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) !=
	 * 0) { return false; } } else if (diffX == 0 && diffY > 0) { if
	 * ((getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0)
	 * { return false; } } else if (diffX == 0 && diffY < 0) { if
	 * ((getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0)
	 * { return false; } } } } if (diffX < 0) { diffX++; } else if (diffX > 0) {
	 * diffX--; } if (diffY < 0) { diffY++; } else if (diffY > 0) { diffY--; } }
	 * return true; }
	 */

	public static boolean canMove(int startX, int startY, int endX, int endY, int height, int xLength, int yLength) {
		int diffX = endX - startX;
		int diffY = endY - startY;
		int max = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int ii = 0; ii < max; ii++) {
			int currentX = endX - diffX;
			int currentY = endY - diffY;
			for (int i = 0; i < xLength; i++) {
				for (int i2 = 0; i2 < yLength; i2++) {
					if (diffX < 0 && diffY < 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 - 1, height) & 0x128010e) != 0
								|| (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY > 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 + 1, height) & 0x12801e0) != 0
								|| (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY > 0) {
						if ((getClipping(currentX + i - 1, currentY + i2 + 1, height) & 0x1280138) != 0
								|| (getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0
								|| (getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY < 0) {
						if ((getClipping(currentX + i + 1, currentY + i2 - 1, height) & 0x1280183) != 0
								|| (getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0
								|| (getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					} else if (diffX > 0 && diffY == 0) {
						if ((getClipping(currentX + i + 1, currentY + i2, height) & 0x1280180) != 0) {
							return false;
						}
					} else if (diffX < 0 && diffY == 0) {
						if ((getClipping(currentX + i - 1, currentY + i2, height) & 0x1280108) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY > 0) {
						if ((getClipping(currentX + i, currentY + i2 + 1, height) & 0x1280120) != 0) {
							return false;
						}
					} else if (diffX == 0 && diffY < 0) {
						if ((getClipping(currentX + i, currentY + i2 - 1, height) & 0x1280102) != 0) {
							return false;
						}
					}
				}
			}
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
		}
		return true;
	}

	public static void findRoute(Player c, int destX, int destY, boolean moveNear, int xLength, int yLength) {
		if (destX == c.getX() && destY == c.getY() && !moveNear
				|| !c.goodDistance(c.getX(), c.getY(), destX, destY, 20)) {
			return;
		}
		destX = destX - 8 * c.mapRegionX;
		destY = destY - 8 * c.mapRegionY;
		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];
		LinkedList<Integer> tileQueueX = new LinkedList<>();
		LinkedList<Integer> tileQueueY = new LinkedList<>();
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				cost[xx][yy] = 99999999;
			}
		}
		int curX = c.absX - c.mapRegionX * 8;
		int curY = c.absY - c.mapRegionY * 8;
		via[curX][curY] = 99;
		cost[curX][curY] = 0;
		int tail = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		boolean foundPath = false;
		int pathLength = 4000;
		while (tail != tileQueueX.size() && tileQueueX.size() < pathLength) {
			curX = tileQueueX.get(tail);
			curY = tileQueueY.get(tail);
			int curAbsX = c.getMapRegionX() * 8 + curX;
			int curAbsY = c.getMapRegionY() * 8 + curY;
			if (curX == destX && curY == destY) {
				foundPath = true;
				break;
			}
			/*
			 * if (xLength != 0 && yLength != 0 && method422(curAbsX, curAbsY,
			 * c.heightLevel % 4, absDestX, absDestY, xLength, yLength)) {
			 * foundPath = true; break; }
			 */
			tail = (tail + 1) % pathLength;
			int thisCost = cost[curX][curY] + 1;
			if (curY > 0 && via[curX][curY - 1] == 0
					&& (Region.getClipping(curAbsX, curAbsY - 1, c.heightLevel % 4) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}
			if (curX > 0 && via[curX - 1][curY] == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY, c.heightLevel % 4) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}
			if (curY < 104 - 1 && via[curX][curY + 1] == 0
					&& (Region.getClipping(curAbsX, curAbsY + 1, c.heightLevel % 4) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && via[curX + 1][curY] == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY, c.heightLevel % 4) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}
			if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY - 1, c.heightLevel % 4) & 0x128010e) == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY, c.heightLevel % 4) & 0x1280108) == 0
					&& (Region.getClipping(curAbsX, curAbsY - 1, c.heightLevel % 4) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}
			if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY + 1, c.heightLevel % 4) & 0x1280138) == 0
					&& (Region.getClipping(curAbsX - 1, curAbsY, c.heightLevel % 4) & 0x1280108) == 0
					&& (Region.getClipping(curAbsX, curAbsY + 1, c.heightLevel % 4) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}
			if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY - 1, c.heightLevel % 4) & 0x1280183) == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY, c.heightLevel % 4) & 0x1280180) == 0
					&& (Region.getClipping(curAbsX, curAbsY - 1, c.heightLevel % 4) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}
			if (curX < 104 - 1 && curY < 104 - 1 && via[curX + 1][curY + 1] == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY + 1, c.heightLevel % 4) & 0x12801e0) == 0
					&& (Region.getClipping(curAbsX + 1, curAbsY, c.heightLevel % 4) & 0x1280180) == 0
					&& (Region.getClipping(curAbsX, curAbsY + 1, c.heightLevel % 4) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}
		if (!foundPath) {
			if (moveNear) {
				int i_223_ = 1000;
				int thisCost = 100;
				int i_225_ = 10;
				for (int x = destX - i_225_; x <= destX + i_225_; x++) {
					for (int y = destY - i_225_; y <= destY + i_225_; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104 && cost[x][y] < 100) {
							int i_228_ = 0;
							if (x < destX)
								i_228_ = destX - x;
							else if (x > destX + xLength - 1)
								i_228_ = x - (destX + xLength - 1);
							int i_229_ = 0;
							if (y < destY)
								i_229_ = destY - y;
							else if (y > destY + yLength - 1)
								i_229_ = y - (destY + yLength - 1);
							int i_230_ = i_228_ * i_228_ + i_229_ * i_229_;
							if (i_230_ < i_223_ || (i_230_ == i_223_ && (cost[x][y] < thisCost))) {
								i_223_ = i_230_;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}
				if (i_223_ == 1000)
					return;
			} else {
				// c.lastRouteX = destX;
				// c.lastRouteY = destY;
				return;
			}
		}
		tail = 0;
		tileQueueX.set(tail, curX);
		tileQueueY.set(tail++, curY);
		int l5;
		for (int j5 = l5 = via[curX][curY]; curX != c.localX() || curY != c.localY(); j5 = via[curX][curY]) {
			if (j5 != l5) {
				l5 = j5;
				tileQueueX.set(tail, curX);
				tileQueueY.set(tail++, curY);
			}
			if ((j5 & 2) != 0) {
				curX++;
			} else if ((j5 & 8) != 0) {
				curX--;
			}
			if ((j5 & 1) != 0) {
				curY++;
			} else if ((j5 & 4) != 0) {
				curY--;
			}
		}
		int size = tail--;
		c.resetWalkingQueue();
		c.addToWalkingQueue(tileQueueX.get(tail), tileQueueY.get(tail));
		for (int i = 1; i < size; i++) {
			tail--;
			c.addToWalkingQueue(tileQueueX.get(tail), tileQueueY.get(tail));
		}
	}

	public static boolean isBlockedPath(int sourceX, int sourceY, int destX, int destY, int z) {
		if (sourceX > destX) {
			if (sourceY > destY) {
				return blockedNorthEast(destX, destY, z);
			} else if (sourceY < destY) {
				return blockedSouthEast(destX, destY, z);
			}
			return blockedEast(destX, destY, z);
		} else if (sourceX < destX) {
			if (sourceY > destY) {
				return blockedNorthWest(destX, destY, z);
			} else if (sourceY < destY) {
				return blockedSouthWest(destX, destY, z);
			}
			return blockedWest(destX, destY, z);
		} else {
			if (sourceY > destY) {
				return blockedNorth(destX, destY, z);
			} else if (sourceY < destY) {
				return blockedSouth(destX, destY, z);
			}
		}
		return false;
	}

	public static boolean blockedNorth(int x, int y, int z) {
		return (getClipping(x, y + 1, z) & 0x1280120) != 0;
	}

	public static boolean blockedEast(int x, int y, int z) {
		return (getClipping(x + 1, y, z) & 0x1280180) != 0;
	}

	public static boolean blockedSouth(int x, int y, int z) {
		return (getClipping(x, y - 1, z) & 0x1280102) != 0;
	}

	public static boolean blockedWest(int x, int y, int z) {
		return (getClipping(x - 1, y, z) & 0x1280108) != 0;
	}

	public static boolean blockedNorthEast(int x, int y, int z) {
		return (getClipping(x + 1, y + 1, z) & 0x12801e0) != 0;
	}

	public static boolean blockedNorthWest(int x, int y, int z) {
		return (getClipping(x - 1, y + 1, z) & 0x1280138) != 0;
	}

	public static boolean blockedSouthEast(int x, int y, int z) {
		return (getClipping(x + 1, y - 1, z) & 0x1280183) != 0;
	}

	public static boolean blockedSouthWest(int x, int y, int z) {
		return (getClipping(x - 1, y - 1, z) & 0x128010e) != 0;
	}

	private static void addProjectileClipping(int x, int y, int height, int shift) {
		Region region = getRegion(x, y);
		if (region != null) {
			if (shift > 0) {
				region.addProjectileClip(x, y, height, shift);
			} else {
				region.removeProjectileClip(x, y, height, shift);
			}
		}
	}

	public static void addClipping(int x, int y, int height, int shift) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				r.addClip(x, y, height, shift);
				break;
			}
		}
	}

	public static Region getRegion(int x, int y) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = (regionX / 8 << 8) + regionY / 8;
		for (Region region : regions) {
			if (region.id() == regionId) {
				return region;
			}
		}
		return null;
	}

	private static Region[] regions;
	private int id;
	private int[][][] clips = new int[4][][];
	private int[][][] shootable = new int[4][][];
	private boolean members = false;

	public Region(int id, boolean members) {
		this.id = id;
		this.members = members;
	}

	public int id() {
		return id;
	}

	public boolean members() {
		return members;
	}

	public static boolean isMembers(int x, int y, int height) {
		if (x >= 3272 && x <= 3320 && y >= 2752 && y <= 2809)
			return false;
		if (x >= 2640 && x <= 2677 && y >= 2638 && y <= 2679)
			return false;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r.members();
			}
		}
		return false;
	}

	private static void setProjectileClippingForVariableObject(int x, int y, int height, int type, int direction,
			boolean flag, boolean negative) {
		if (type == 0) {
			if (direction == 0) {
				addProjectileClipping(x, y, height, negative ? -128 : 128);
				addProjectileClipping(x - 1, y, height, negative ? -8 : 8);
			} else if (direction == 1) {
				addProjectileClipping(x, y, height, negative ? -2 : 2);
				addProjectileClipping(x, y + 1, height, negative ? -32 : 32);
			} else if (direction == 2) {
				addProjectileClipping(x, y, height, negative ? -8 : 8);
				addProjectileClipping(x + 1, y, height, negative ? -128 : 128);
			} else if (direction == 3) {
				addProjectileClipping(x, y, height, negative ? -32 : 32);
				addProjectileClipping(x, y - 1, height, negative ? -2 : 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addProjectileClipping(x, y, height, negative ? -1 : 1);
				addProjectileClipping(x - 1, y + 1, height, negative ? -16 : 16);// wrong
																					// method217(16,
																					// x
																					// -
																					// 1,
																					// y
																					// +
																					// 1);
			} else if (direction == 1) {
				addProjectileClipping(x, y, height, negative ? -4 : 4);
				addProjectileClipping(x + 1, y + 1, height, negative ? -64 : 64);
			} else if (direction == 2) {
				addProjectileClipping(x, y, height, negative ? -16 : 16);
				addProjectileClipping(x + 1, y - 1, height, negative ? -1 : 1);
			} else if (direction == 3) {
				addProjectileClipping(x, y, height, negative ? -64 : 64);
				addProjectileClipping(x - 1, y - 1, height, negative ? -4 : 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addProjectileClipping(x, y, height, 130);
				addProjectileClipping(x - 1, y, height, negative ? -8 : 8);
				addProjectileClipping(x, y + 1, height, negative ? -32 : 32);
			} else if (direction == 1) {
				addProjectileClipping(x, y, height, negative ? -10 : 10);
				addProjectileClipping(x, y + 1, height, negative ? -32 : 32);
				addProjectileClipping(x + 1, y, height, negative ? -128 : 128);
			} else if (direction == 2) {
				addProjectileClipping(x, y, height, negative ? -40 : 40);
				addProjectileClipping(x + 1, y, height, negative ? -128 : 128);
				addProjectileClipping(x, y - 1, height, negative ? -2 : 2);
			} else if (direction == 3) {
				addProjectileClipping(x, y, height, negative ? -160 : 160);
				addProjectileClipping(x, y - 1, height, negative ? -2 : 2);
				addProjectileClipping(x - 1, y, height, negative ? -8 : 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addProjectileClipping(x, y, height, negative ? -0x10000 : 0x10000);
					addProjectileClipping(x - 1, y, height, negative ? -4096 : 4096);
				} else if (direction == 1) {
					addProjectileClipping(x, y, height, negative ? -1024 : 1024);
					addProjectileClipping(x, y + 1, height, negative ? -16384 : 16384);
				} else if (direction == 2) {
					addProjectileClipping(x, y, height, negative ? -4096 : 4096);
					addProjectileClipping(x + 1, y, height, negative ? -0x10000 : 0x10000);
				} else if (direction == 3) {
					addProjectileClipping(x, y, height, negative ? -16384 : 16384);
					addProjectileClipping(x, y - 1, height, negative ? -1024 : 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addProjectileClipping(x, y, height, negative ? -512 : 512);
					addProjectileClipping(x - 1, y + 1, height, negative ? -8192 : 8192);
				} else if (direction == 1) {
					addProjectileClipping(x, y, height, negative ? -2048 : 2048);
					addProjectileClipping(x + 1, y + 1, height, negative ? -32768 : 32768);
				} else if (direction == 2) {
					addProjectileClipping(x, y, height, negative ? -8192 : 8192);
					addProjectileClipping(x + 1, y + 1, height, negative ? -512 : 512);
				} else if (direction == 3) {
					addProjectileClipping(x, y, height, negative ? -32768 : 32768);
					addProjectileClipping(x - 1, y - 1, height, negative ? -2048 : 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addProjectileClipping(x, y, height, negative ? -0x10400 : 0x10400);
					addProjectileClipping(x - 1, y, height, negative ? -4096 : 4096);
					addProjectileClipping(x, y + 1, height, negative ? -16384 : 16384);
				} else if (direction == 1) {
					addProjectileClipping(x, y, height, negative ? -5120 : 5120);
					addProjectileClipping(x, y + 1, height, negative ? -16384 : 16384);
					addProjectileClipping(x + 1, y, height, negative ? -0x10000 : 0x10000);
				} else if (direction == 2) {
					addProjectileClipping(x, y, height, negative ? -20480 : 20480);
					addProjectileClipping(x + 1, y, height, negative ? -0x10000 : 0x10000);
					addProjectileClipping(x, y - 1, height, negative ? -1024 : 1024);
				} else if (direction == 3) {
					addProjectileClipping(x, y, height, negative ? -81920 : 81920);
					addProjectileClipping(x, y - 1, height, negative ? -1024 : 1024);
					addProjectileClipping(x - 1, y, height, negative ? -4096 : 4096);
				}
			}
		}
	}

	private static void addClippingForVariableObject(int x, int y, int height, int type, int direction, boolean flag) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, height, 128);
				addClipping(x - 1, y, height, 8);
			} else if (direction == 1) {
				addClipping(x, y, height, 2);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 2) {
				addClipping(x, y, height, 8);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 3) {
				addClipping(x, y, height, 32);
				addClipping(x, y - 1, height, 2);
			}
		} else if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, height, 1);
				addClipping(x - 1, y, height, 16);
			} else if (direction == 1) {
				addClipping(x, y, height, 4);
				addClipping(x + 1, y + 1, height, 64);
			} else if (direction == 2) {
				addClipping(x, y, height, 16);
				addClipping(x + 1, y - 1, height, 1);
			} else if (direction == 3) {
				addClipping(x, y, height, 64);
				addClipping(x - 1, y - 1, height, 4);
			}
		} else if (type == 2) {
			if (direction == 0) {
				addClipping(x, y, height, 130);
				addClipping(x - 1, y, height, 8);
				addClipping(x, y + 1, height, 32);
			} else if (direction == 1) {
				addClipping(x, y, height, 10);
				addClipping(x, y + 1, height, 32);
				addClipping(x + 1, y, height, 128);
			} else if (direction == 2) {
				addClipping(x, y, height, 40);
				addClipping(x + 1, y, height, 128);
				addClipping(x, y - 1, height, 2);
			} else if (direction == 3) {
				addClipping(x, y, height, 160);
				addClipping(x, y - 1, height, 2);
				addClipping(x - 1, y, height, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, height, 65536);
					addClipping(x - 1, y, height, 4096);
				} else if (direction == 1) {
					addClipping(x, y, height, 1024);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 2) {
					addClipping(x, y, height, 4096);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 3) {
					addClipping(x, y, height, 16384);
					addClipping(x, y - 1, height, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, height, 512);
					addClipping(x - 1, y + 1, height, 8192);
				} else if (direction == 1) {
					addClipping(x, y, height, 2048);
					addClipping(x + 1, y + 1, height, 32768);
				} else if (direction == 2) {
					addClipping(x, y, height, 8192);
					addClipping(x + 1, y + 1, height, 512);
				} else if (direction == 3) {
					addClipping(x, y, height, 32768);
					addClipping(x - 1, y - 1, height, 2048);
				}
			} else if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, height, 66560);
					addClipping(x - 1, y, height, 4096);
					addClipping(x, y + 1, height, 16384);
				} else if (direction == 1) {
					addClipping(x, y, height, 5120);
					addClipping(x, y + 1, height, 16384);
					addClipping(x + 1, y, height, 65536);
				} else if (direction == 2) {
					addClipping(x, y, height, 20480);
					addClipping(x + 1, y, height, 65536);
					addClipping(x, y - 1, height, 1024);
				} else if (direction == 3) {
					addClipping(x, y, height, 81920);
					addClipping(x, y - 1, height, 1024);
					addClipping(x - 1, y, height, 4096);
				}
			}
		}
	}

	private static void addProjectileClippingForSolidObject(int x, int y, int height, int xLength, int yLength,
			boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addProjectileClipping(i, i2, height, clipping);
			}
		}
	}

	private static void addClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag) {
		int clipping = 256;
		if (flag) {
			clipping += 0x20000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addClipping(i, i2, height, clipping);
			}
		}
	}

	public static void addObject(int objectId, int x, int y, int height, int type, int direction) {
		ObjectDef def = ObjectDef.getObjectDef(objectId);
		if (type < 4)
			def.setSolid(objectId);
		if (def == null) {
			return;
		}
		int xLength;
		int yLength;
		if (direction != 1 && direction != 3) {
			xLength = def.xLength();
			yLength = def.yLength();
		} else {
			xLength = def.yLength();
			yLength = def.xLength();
		}
		if (type == 22) {
			if (def.hasActions() && def.aBoolean767()) {
				addClipping(x, y, height, 0x200000);
				if (def.aBoolean757) {
					addProjectileClipping(x, y, height, 0x200000);
				}
			}
		} else if (type >= 9) {
			if (def.aBoolean767()) {
				addClippingForSolidObject(x, y, height, xLength, yLength, def.solid());
				if (def.aBoolean757) {
					addProjectileClippingForSolidObject(x, y, height, xLength, yLength, true);
				}
			}
		} else if (type >= 0 && type <= 3) {
			if (def.aBoolean767()) {
				addClippingForVariableObject(x, y, height, type, direction, def.solid());
				if (def.aBoolean757) {
					setProjectileClippingForVariableObject(x, y, height, type, direction, def.solid(), false);
				}
			}
		}
	}

	public static int getProjectileClipping(int x, int y, int height) {
		if (height > 3)
			height = 0;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r.getProjectileClip(x, y, height);
			}
		}
		return 0;
	}

	private int getProjectileClip(int x, int y, int height) {
		if (height > 3)
			height = height % 4;
		int regionAbsX = (id >> 8) * 64;
		int regionAbsY = (id & 0xff) * 64;
		if (shootable[height] == null) {
			return 0;
		}
		return shootable[height][x - regionAbsX][y - regionAbsY];
	}

	public static int getClipping(int x, int y, int height) {
		if (height > 3)
			height = 0;
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				return r.getClip(x, y, height);
			}
		}
		return 0;
	}

	public static boolean getClipping(int x, int y, int height, int moveTypeX, int moveTypeY) {
		try {
			if (height > 3)
				height = 0;
			int checkX = (x + moveTypeX);
			int checkY = (y + moveTypeY);
			if (moveTypeX == -1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280108) == 0;
			else if (moveTypeX == 1 && moveTypeY == 0)
				return (getClipping(x, y, height) & 0x1280180) == 0;
			else if (moveTypeX == 0 && moveTypeY == -1)
				return (getClipping(x, y, height) & 0x1280102) == 0;
			else if (moveTypeX == 0 && moveTypeY == 1)
				return (getClipping(x, y, height) & 0x1280120) == 0;
			else if (moveTypeX == -1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x128010e) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280102) == 0);
			else if (moveTypeX == 1 && moveTypeY == -1)
				return ((getClipping(x, y, height) & 0x1280183) == 0
						&& (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY - 1, height) & 0x1280102) == 0);
			else if (moveTypeX == -1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x1280138) == 0
						&& (getClipping(checkX - 1, checkY, height) & 0x1280108) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else if (moveTypeX == 1 && moveTypeY == 1)
				return ((getClipping(x, y, height) & 0x12801e0) == 0
						&& (getClipping(checkX + 1, checkY, height) & 0x1280180) == 0
						&& (getClipping(checkX, checkY + 1, height) & 0x1280120) == 0);
			else {
				// System.out.println("[FATAL ERROR]: At getClipping: " + x + ",
				// "
				// + y + ", " + height + ", " + moveTypeX + ", "
				// + moveTypeY);
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}

	private static Map<Integer, byte[]> loadMapData() {
		Map<Integer, byte[]> entries = new HashMap<>();
		try {

			ZipInputStream in = new ZipInputStream(
					new ByteArrayInputStream(Files.readAllBytes(Paths.get("./Data/cache/maps.dat"))));
			ZipEntry entry = null;
			while ((entry = in.getNextEntry()) != null) {
				byte[] buff =  ByteStreams.toByteArray(in);
				String entryName = entry.getName();
				int dotIndex = entryName.indexOf('.');
				int entryId = Integer.parseInt(entryName.substring(0, dotIndex));
				entries.put(entryId, getBuffer(buff));
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	public static void load() {
		try {
			Map<Integer, byte[]> mapData = loadMapData();
			File f = new File("./Data/cache/maps.idx");
			byte[] buffer = new byte[(int) f.length()];
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			ByteStream in = new ByteStream(buffer);
			int size = in.readUnsignedWord();
			int[] regionIds = new int[size];
			int[] mapGroundFileIds = new int[size];
			int[] mapObjectsFileIds = new int[size];
			for (int i = 0; i < size; i++) {
				regionIds[i] = in.readUnsignedWord();
				mapGroundFileIds[i] = in.readUnsignedWord();
				mapObjectsFileIds[i] = in.readUnsignedWord();
			}
			regions = new Region[size];
			for (int i = 0; i < size; i++) {
				regions[i] = new Region(regionIds[i], false);
			}
			for (int i = 0; i < size; i++) {
				byte[] file1 = mapData.get(mapObjectsFileIds[i]);
				byte[] file2 = mapData.get(mapGroundFileIds[i]);
				if (file1 == null || file2 == null) {
					continue;
				}
				try {
					loadMaps(regionIds[i], new ByteStream(file1), new ByteStream(file2));
				} catch (Exception e) {
					System.out.println("Error loading map region: " + regionIds[i]);
					e.printStackTrace();
				}
			}
			Arrays.asList(EXISTANT_OBJECTS).forEach(o -> addWorldObject(o));
			adjustClipping();
			System.out.println("Loaded: Region configuration.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void resetClipping(int x, int y, int z) {
		int regionX = x >> 3;
		int regionY = y >> 3;
		int regionId = ((regionX / 8) << 8) + (regionY / 8);
		for (Region r : regions) {
			if (r.id() == regionId) {
				int regionAbsX = (regionId >> 8) * 64;
				int regionAbsY = (regionId & 0xff) * 64;
				if (r.clips[z] == null) {
					return;
				}
				r.clips[z][x - regionAbsX][y - regionAbsY] = 0;
			}
		}
	}

	private static void adjustClipping() {
		/*
		 * Abyssal sire maps.
		 */
		for (int x = 2978; x < 2981; x++) {
			for (int y = 4849; y < 4859; y++) {
				resetClipping(x, y, 0);
			}
		}
	}

	private static void loadMaps(int regionId, ByteStream str1, ByteStream str2) {
		int absX = (regionId >> 8) * 64;
		int absY = (regionId & 0xff) * 64;
		int[][][] someArray = new int[4][64][64];
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					while (true) {
						int v = str2.getUByte();
						if (v == 0) {
							break;
						} else if (v == 1) {
							str2.skip(1);
							break;
						} else if (v <= 49) {
							str2.skip(1);
						} else if (v <= 81) {
							someArray[i][i2][i3] = v - 49;
						}
					}
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					if ((someArray[i][i2][i3] & 1) == 1) {
						int height = i;
						if ((someArray[1][i2][i3] & 2) == 2) {
							height--;
						}
						if (height >= 0 && height <= 3) {
							addClipping(absX + i2, absY + i3, height, 0x200000);
						}
					}
				}
			}
		}
		int objectId = -1;
		int incr;
		while ((incr = str1.getUSmart()) != 0) {
			objectId += incr;
			int location = 0;
			int incr2;
			while ((incr2 = str1.getUSmart()) != 0) {
				location += incr2 - 1;
				int localX = (location >> 6 & 0x3f);
				int localY = (location & 0x3f);
				int height = location >> 12;
				int objectData = str1.getUByte();
				int type = objectData >> 2;
				int direction = objectData & 0x3;
				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
					continue;
				}
				if ((someArray[1][localX][localY] & 2) == 2) {
					height--;
				}
				if (height >= 0 && height <= 3) {
					addObject(objectId, absX + localX, absY + localY, height, type, direction);
					addWorldObject(objectId, absX + localX, absY + localY, height, direction);
				}
			}
		}
		
	}

	public static byte[] getBuffer(byte[] buffer) throws Exception {
		byte[] gzipInputBuffer = new byte[999999];
		int bufferlength = 0;
		GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(buffer));
		do {
			if (bufferlength == gzipInputBuffer.length) {
				System.out.println("Error inflating data.\nGZIP buffer overflow.");
				break;
			}
			int readByte = gzip.read(gzipInputBuffer, bufferlength, gzipInputBuffer.length - bufferlength);
			if (readByte == -1)
				break;
			bufferlength += readByte;
		} while (true);
		byte[] inflated = new byte[bufferlength];
		System.arraycopy(gzipInputBuffer, 0, inflated, 0, bufferlength);
		buffer = inflated;
		if (buffer.length < 10)
			return null;
		return buffer;
	}

	public static boolean blockedNorthNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + i, y + size + 1, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x, y + 1, z) != 0);
	}

	public static boolean blockedEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y, z) != 0);
	}

	public static boolean blockedSouthNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + i, y - 1, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x, y - 1, z) != 0);
	}

	public static boolean blockedWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i, z) != 0);
				if (clipped) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y, z) != 0);
	}

	public static boolean blockedNorthEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i + 1, z) != 0);
				boolean clipped2 = (getClipping(x + i + 1, y + size + 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y + 1, z) != 0);
	}

	public static boolean blockedNorthWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i + 1, z) != 0);
				boolean clipped2 = (getClipping(x + i - 1, y + size + 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y + 1, z) != 0);
	}

	public static boolean blockedSouthEastNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x + size + 1, y + i - 1, z) != 0);
				boolean clipped2 = (getClipping(x + i + 1, y - 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x + 1, y - 1, z) != 0);
	}

	public static boolean blockedSouthWestNPC(int x, int y, int z, int size) {
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				boolean clipped = (getClipping(x - 1, y + i - 1, z) != 0);
				boolean clipped2 = (getClipping(x + i - 1, y - 1, z) != 0);
				if (clipped || clipped2) {
					return true;
				}
			}
		}
		return (getClipping(x - 1, y - 1, z) != 0);
	}

	public static Region[] getRegions() {
		return regions;
	}

	public static void setRegions(Region[] set) {
		regions = set;
	}

	private ArrayList<GameObject> realObjects = new ArrayList<GameObject>();

	public static void addObject(GameObject obj) {
		RSObjectDefinition def = RSObjectDefinition.forId(obj.getId());
		if (def == null) {
			return;
		}
		int xLength;
		int yLength;
		if (obj.getFace() != 1 && obj.getFace() != 3) {
			xLength = def.ysize;
			yLength = def.xsize;
		} else {
			xLength = def.xsize;
			yLength = def.ysize;
		}
		if (obj.getType() == 22) {
			if (def.hasActions && def.aBoolean767) {
				addClipping(obj.getX(), obj.getY(), obj.getZ(), 0x200000);
			}
		} else if (obj.getType() >= 9) {
			if (def.aBoolean767) {
				addClippingForSolidObject(obj.getX(), obj.getY(), obj.getZ(), xLength, yLength, def.solid);
			}
		} else if (obj.getType() >= 0 && obj.getType() <= 3) {
			if (def.aBoolean767) {
				addClippingForVariableObject(obj.getX(), obj.getY(), obj.getZ(), obj.getType(), obj.getType(),
						def.solid);
			}
		} else {
		}
	}

}