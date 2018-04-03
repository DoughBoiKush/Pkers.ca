package org.osps.cache;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.apollo.fs.Cache;
import org.apollo.fs.archive.Archive;
import org.apollo.util.ByteBufferUtil;
import org.apollo.util.CompressionUtil;
import org.osps.clip.Region;
import org.osps.model.Location;
import org.osps.world.GameObject;

public class RegionParser {

	private static int objectsParsed;

	private static void parse() throws IOException {
		/*Logger.getAnonymousLogger().info("Parsing regions...");

		Archive versionList = Archive.decode(Cache.fileSystem.getFile(0, 5));
		ByteBuffer buffer = versionList.getEntry("map_index").getBuffer();

		int indices = buffer.getShort();
		int[] areas = new int[indices];
		int[] landscapes = new int[indices];
		int[] maps = new int[indices];

		Region.setRegions(new Region[indices]);

		for (int i = 0; i < indices; i++) {
			areas[i] = buffer.getShort() & 0xFFFF;
			maps[i] = buffer.getShort() & 0xFFFF;
			landscapes[i] = buffer.getShort() & 0xFFFF;

			Region.getRegions()[i] = new Region(areas[i], true);
		}
		for (int i = 0; i < indices; i++) {
			ByteBuffer compressed = Cache.fileSystem.getFile(4, landscapes[i]);
			ByteBuffer uncompressed = ByteBuffer.wrap(CompressionUtil.ungzip(compressed));

			ByteBuffer mapCompressed = Cache.fileSystem.getFile(4, maps[i]);
			ByteBuffer mapUncompressed = ByteBuffer.wrap(CompressionUtil.ungzip(mapCompressed));

//			ObjectManager.addObjectsToRegion();
			parseArea(areas[i], uncompressed, mapUncompressed);

		}
		Logger.getAnonymousLogger().info(
				"Loaded " + objectsParsed + " objects for game regions.");*/
		
	}

	private static void parseArea(int area, ByteBuffer objectBuffer, ByteBuffer mapBuffer) {
		int x = (area >> 8 & 0xFF) * 64;// i think this function must be
										// broken... do u think there is that
										// many objects? actually 1 sec.
		int y = (area & 0xFF) * 64;

		int id = -1;
		int idOffset;

		int[][][] landscape = new int[4][64][64];
		for (int i = 0; i < 4; i++) {
			for (int i2 = 0; i2 < 64; i2++) {
				for (int i3 = 0; i3 < 64; i3++) {
					while (true) {
						int v = mapBuffer.get() & 0xFF;
						if (v == 0) {
							break;
						} else if (v == 1) {
							mapBuffer.get();
							break;
						} else if (v <= 49) {
							mapBuffer.get();
						} else if (v <= 81) {
							landscape[i][i2][i3] = v - 49;
						}
					}
				}
			}
		}
		for (int h = 0; h < 4; h++) {
			for (int localX = 0; localX < 64; localX++) {
				for (int localY = 0; localY < 64; localY++) {
					if ((landscape[h][localX][localY] & 1) == 1) {
						int height = h;
						if ((landscape[1][localX][localY] & 2) == 2)
							height--;
						if (height >= 0 && height <= 3) {
							Region.addClipping(x + localX, y + localY, height, 0x200000);
						}
					}
				}
			}
		}
		while ((idOffset = ByteBufferUtil.readSmart2(objectBuffer)) != 0) {
			id += idOffset;

			int position = 0;
			int positionOffset;

			while ((positionOffset = ByteBufferUtil.readSmart(objectBuffer)) != 0) {
				position += positionOffset - 1;
				// this code must be wrong maybe
				int localX = position >> 6 & 0x3F;
				int localY = position & 0x3F;
				int height = position >> 12;

				int info = objectBuffer.get() & 0xFF;
				int type = info >> 2;
				int rotation = info & 3;
				if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
					continue;
				}
				if ((landscape[1][localX][localY] & 2) == 2) {
                    height--;
                }
				if (height >= 0 && height <= 3) {
					Location pos = Location.create(x + localX, y + localY, height);

					if (id < RSObjectDefinition.definitions.length) {
						RSObjectDefinition obj = RSObjectDefinition.valueOf(id);
						obj.type = type;
						obj.orientation = rotation;
					}
					
					GameObject object = new GameObject(id, type, pos, rotation);
					Region.addObject(object);
					objectsParsed++;// is this why? its going over to many
									// objects lol = objectsParsed
				}
			}
		}
	}
}
