package org.osps.model.npcs.boss.Cerberus;

import java.util.ArrayList;
import java.util.Random;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;
import org.osps.model.players.combat.Hitmark;
import org.osps.util.Misc;

/**
 * @author Micheal/01053
 * @author Joshua/Exilius
 */

public class Cerberus {
	
	private enum ghostPositions {
		WLeft(5867, 1239, 1256), 
		WMiddle(5869, 1240, 1256), 
		WRight(5868, 1241, 1256);

		private int ghostType;
		private int ghostX, ghostY;

	private ghostPositions(int ghostType, int ghostX, int ghostY) {
			this.ghostType = ghostType;
			this.ghostX = ghostX;
			this.ghostY = ghostY;
		}
	
		private int getGhostType() {
			return ghostType;
		}

		private int getX() {
			return ghostX;
		}
		
		private int getY() {
			return ghostY;
		}
	}
	
	private enum northGhostPositions {
		NLeft(5867, 1303, 1320), 
		NMiddle(5869, 1304, 1320), 
		NRight(5868, 1305, 1320);

		private int ghostType;
		private int ghostX, ghostY;

	private northGhostPositions(int ghostType, int ghostX, int ghostY) {
			this.ghostType = ghostType;
			this.ghostX = ghostX;
			this.ghostY = ghostY;
		}
	
		private int getGhostType() {
			return ghostType;
		}

		private int getX() {
			return ghostX;
		}
		
		private int getY() {
			return ghostY;
		}
	}
	
	private enum eastGhostPositions {
		ELeft(5867, 1367, 1256), 
		EMiddle(5869, 1368, 1256), 
		ERight(5868, 1369, 1256);

		private int ghostType;
		private int ghostX, ghostY;

	private eastGhostPositions(int ghostType, int ghostX, int ghostY) {
			this.ghostType = ghostType;
			this.ghostX = ghostX;
			this.ghostY = ghostY;
		}
	
		private int getGhostType() {
			return ghostType;
		}

		private int getX() {
			return ghostX;
		}
		
		private int getY() {
			return ghostY;
		}
	}
	
	public static final Boundary WEST = new Boundary(1217, 1226, 1268, 1273);
	public static final Boundary NORTH = new Boundary(1294, 1300, 1321, 1335);
	public static final Boundary EAST = new Boundary(1358, 1236, 1385, 1271);
	
	public static final Random random = new Random();

	public static void ghostData(Player c) {
		if(c.alreadySpawned) {
			return;
		}
		despawnGhosts(c);
		if (Boundary.isIn(c, WEST)) {
		for (ghostPositions ghosts : ghostPositions.values()) {
			Server.npcHandler.spawnNpc(c, ghosts.getGhostType(), ghosts.getX(), ghosts.getY(), 0, 0, 100, 25, 300, 50, true, false);
			c.alreadySpawned = true;
			}
		}
		if (Boundary.isIn(c, NORTH)) {
		for (northGhostPositions ghosts : northGhostPositions.values()) {
			Server.npcHandler.spawnNpc(c, ghosts.getGhostType(), ghosts.getX(), ghosts.getY(), 0, 0, 100, 25, 300, 50, true, false);
			c.alreadySpawned = true;
		}
	}
		if (Boundary.isIn(c, EAST)) {
		for (eastGhostPositions ghosts : eastGhostPositions.values()) {
			Server.npcHandler.spawnNpc(c, ghosts.getGhostType(), ghosts.getX(), ghosts.getY(), 0, 0, 100, 25, 300, 50, true, false);
			c.alreadySpawned = true;
		}
		}
	}
	
	public static void despawnGhosts(Player c) {
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if(container.getTotalTicks() == 6) {
					stopRange();
				}
				if(container.getTotalTicks() == 8) {
					stopMelee();
				}
				if(container.getTotalTicks() == 10) {
					stopMage();
					container.stop();
					return;
				}
			}
			public void stopRange() {
				if (Boundary.isIn(c, WEST)) {
				NPCHandler.kill(c, 5867, 1239, 1256, 0);
				}
				if (Boundary.isIn(c, NORTH)) {
				NPCHandler.kill(c, 5867, 1303, 1320, 0);
				}
				if (Boundary.isIn(c, EAST)) {
				NPCHandler.kill(c, 5867, 1367, 1256, 0);
				}
			}
			public void stopMelee() {
				if (Boundary.isIn(c, WEST)) {
				NPCHandler.kill(c, 5869, 1240, 1256, 0);
			}
				if (Boundary.isIn(c, NORTH)) {
				NPCHandler.kill(c, 5869, 1304, 1320, 0);
			}
				if (Boundary.isIn(c, EAST)) {
				NPCHandler.kill(c, 5869, 1368, 1256, 0);
			}
		}
			public void stopMage() {
				c.alreadySpawned = false;
				if (Boundary.isIn(c, WEST)) {
				NPCHandler.kill(c, 5868, 1241, 1256, 0);
			}
				if (Boundary.isIn(c, NORTH)) {
				NPCHandler.kill(c, 5868, 1305, 1320, 0);
			}
				if (Boundary.isIn(c, EAST)) {
				NPCHandler.kill(c, 5868, 1369, 1256, 0);
			}
		}
		}, 1);
	}
	
	public static ArrayList<int[]> ROCKS = new ArrayList<>(2);
	
	public static void EXPLODING_ROCKS(NPC npc, Player c) {
		if (c == null) {
			return;
		}
		int x = c.getX();
		int y = c.getY();
		ROCKS.add(new int[] { x, y });
		for (int i = 0; i < 1; i++) {
			ROCKS.add(new int[] { (x - 1) + Misc.random(3), (y - 1) + Misc.random(3) });
		}
		c.TICKING_DAMAGE = true;
		for (int[] point : ROCKS) {
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			c.getPA().createPlayersStillGfx(1246, x1, y1, 0, 6);
			c.getPA().createPlayersStillGfx(1246, c.absX, c.absY, 0, 6);
			c.lastX = c.absX;
			c.lastY = c.absY;
		}
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : ROCKS) {
					int x1 = point[0] + 1;
					int y1 = point[1] + 2;
					c.getPA().createPlayersStillGfx(1247, x1, y1, 0, 1);
					c.getPA().createPlayersStillGfx(1247, c.lastX, c.lastY, 0, 1);
				if(c.absX == x1 && c.absY == y1 || c.absX == c.lastX && c.absY == c.lastY) {
						c.appendDamage(Misc.random(23), Hitmark.HIT);
					}
				}
				ROCKS.clear();
				container.stop();
			}
			
			public void stop() {
				c.TICKING_DAMAGE = false;
			}
	
		}, 4);
	}
	
	public static void GhostDamage(Player c) {
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				c.appendDamage(30, Hitmark.HIT);
				container.stop();
			}
		}, 1);
	}
}