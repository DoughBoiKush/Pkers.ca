package org.osps.cache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import org.apollo.fs.Cache;
import org.apollo.fs.archive.Archive;
import org.apollo.util.ByteBufferUtil;

public class RSObjectDefinition {

	public static RSObjectDefinition[] cache = new RSObjectDefinition[20];
	public static RSObjectDefinition[] definitions;

	public static RSObjectDefinition forId(int id) {
		if (id < 0 || id >= definitions.length)
			return null;
		return definitions[id];
	}

	public int id;

	private static int[] positions;
	private static ByteBuffer idxBuffer;
	private static ByteBuffer datBuffer;

	public static void parse() throws FileNotFoundException, IOException {
		Archive config = Archive.decode(Cache.fileSystem.getFile(0, 2));

		datBuffer = config.getEntry("loc.dat").getBuffer();
		idxBuffer = config.getEntry("loc.idx").getBuffer();

		//BufferedWriter writer = new BufferedWriter(new FileWriter("./object_ids.txt"));

		positions = new int[idxBuffer.getShort() & 0xFFFF];
		definitions = new RSObjectDefinition[positions.length];
		int off = 2;
		for (int i = 0; i < positions.length; i++) {
			positions[i] = off;
			off += idxBuffer.getShort() & 0xFFFF;
		}


		for (int i = 0; i < definitions.length; i++) {
			definitions[i] = valueOf(i);

		}


		for (int i = 0; i < cache.length; i++) {
			cache[i] = new RSObjectDefinition();
		}
		Logger.getAnonymousLogger().info("Cached "+positions.length+" Object Definitions.");
	}


	public void singleParse() {
		int flag = -1;
		while(true) {
			int type = datBuffer.get() & 0xFF;
			if (type == 0)
				break;
			if (type == 1) {
				int len = datBuffer.get() & 0xFF;
				if (len > 0) {
					if (models == null || lowMem) {
						children = new int[len];
						models = new int[len];
						for (int k1 = 0; k1 < len; k1++) {
							models[k1] = datBuffer.getShort() & 0xFFFF;
							children[k1] = datBuffer.get() & 0xFF;
						}
					} else {
						datBuffer.position(datBuffer.position() + (len * 3));
					}
				}
			} else if (type == 2)
				name = ByteBufferUtil.readString(datBuffer);
			else if (type == 3)
				description = ByteBufferUtil.readString(datBuffer);
			else if (type == 5) {
				int len = datBuffer.get() & 0xFF;
				if (len > 0) {
					if (models == null || lowMem) {
						children = null;
						models = new int[len];
						for (int l1 = 0; l1 < len; l1++)
							models[l1] = datBuffer.getShort() & 0xFFFF;
					} else {
						datBuffer.position(datBuffer.position() + (len * 2));
					}
				}
			} else if (type == 14)
				xsize = datBuffer.get() & 0xFF;
			else if (type == 15)
				ysize = datBuffer.get() & 0xFF;
			else if (type == 17)
				aBoolean767 = false;
			else if (type == 18)
				aBoolean757 = false;
			else if (type == 19)
				hasActions = (datBuffer.get() & 0xFF) == 1;
			else if (type == 21)
				aBoolean762 = true;
			else if (type == 22)
				aBoolean769 = true;
			else if (type == 23)
				aBoolean764 = true;
			else if (type == 24) {
				anInt781 = datBuffer.getShort() & 0xFFFF;
				if (anInt781 == 65535)
					anInt781 = -1;
			} else if (type == 28)
				anInt775 = datBuffer.get() & 0xFF;
			else if (type == 29)
				ambience = datBuffer.get();
			else if (type == 39)
				contrast = datBuffer.get();
			else if (type >= 30 && type < 39) {
				if (actions == null)
					actions = new String[5];
				actions[type - 30] = ByteBufferUtil.readString(datBuffer);
				if (actions[type - 30].equalsIgnoreCase("hidden"))
					actions[type - 30] = null;
			} else if (type == 40) {
				int i1 = datBuffer.get() & 0xFF;
				recol_s = new int[i1];
				recol_d = new int[i1];
				for (int i2 = 0; i2 < i1; i2++) {
					recol_s[i2] = datBuffer.getShort() & 0xFFFF;
					recol_d[i2] = datBuffer.getShort() & 0xFFFF;
				}

			} else if (type == 60)
				anInt746 = datBuffer.getShort() & 0xFFFF;
			else if (type == 62)
				aBoolean751 = true;
			else if (type == 64)
				solid = false;
			else if (type == 65)
				resizex = datBuffer.getShort() & 0xFFFF;
			else if (type == 66)
				resizey = datBuffer.getShort() & 0xFFFF;
			else if (type == 67)
				resizez = datBuffer.getShort() & 0xFFFF;
			else if (type == 68)
				anInt758 = datBuffer.getShort() & 0xFFFF;
			else if (type == 69)
				anInt768 = datBuffer.get() & 0xFF;
			else if (type == 70)
				anInt738 = datBuffer.getShort();
			else if (type == 71)
				anInt745 = datBuffer.getShort();
			else if (type == 72)
				anInt783 = datBuffer.getShort();
			else if (type == 73)
				aBoolean736 = true;
			else if (type == 74)
				aBoolean766 = true;
			else if (type == 75)
				anInt760 = datBuffer.get() & 0xFF;
			else if (type == 77) {
				anInt774 = datBuffer.getShort() & 0xFFFF;
				if (anInt774 == 65535)
					anInt774 = -1;
				anInt749 = datBuffer.getShort() & 0xFFFF;
				if (anInt749 == 65535)
					anInt749 = -1;
				int bits = datBuffer.get() & 0xFF;
				masklookup = new int[bits + 1];
				for (int bit = 0; bit <= bits; bit++) {
					masklookup[bit] = datBuffer.getShort() & 0xFFFF;
					if (masklookup[bit] == 65535)
						masklookup[bit] = -1;
				}
			}
		}
		if (flag == -1  && name != "null" && name != null) {
			hasActions = models != null
					&& (children == null || children[0] == 10);
			if (actions != null)
				hasActions = true;
		}
		if (aBoolean766) {
			aBoolean767 = false;
			aBoolean757 = false;
		}
		if (anInt760 == -1)
			anInt760 = aBoolean767 ? 1 : 0;
	}


	public int[] solidObjects = {1902,1903,1904,1905,
			1906,1907,1908,1909,1910,1911,1912,1536,1535,1537,1538,5139,5140,5141,5142,5143,5144,5145,5146,5147,5148,5149,5150,
			1534,1533,1532,1531,1530,1631,1624,733,1658,1659,1631,1620,14723,14724,14726,14622,14625,14627,11668,11667,
			14543,14749,14561,14750,14752,14751,1547,1548,1415,1508,1506,1507,1509,1510,1511,1512,1513,1514,1515,1516,
			1517,1518,1519,1520,1521,1522,1523,1524,1525,1526,1527,1528,1529,1505,1504,3155,3154,3152,10748,9153,9154,
			9473,1602,1603,1601,1600,9544,9563,9547,2724,6966,6965,9587,9588,9626,9627,9596,9598,11712,11713,11773,11776,
			11652,11818,11716,11721,14409,11715,11714,11825,11409,11826,11819,14411,14410,11719,11717,14402,11828,11772,
			11775,11686,12278,1853,11611,11610,11609,11608,11607,11561,11562,11563,11564,11558,11616,11617,11625,11624,12990,
			12991,5634,1769,1770,135,134,11536,11512,11529,11513,11521,11520,11519,11518,11517,11516,11514,11509,11538,11537,
			11470,11471,136,11528,11529,11530,11531,1854,1000,9265,9264,1591,11708,11709,11851};
	public void setSolid(int type) {
		solid = false;
		for(int i = 0; i < solidObjects.length;i++) {
			if(type == solidObjects[i]) {
				aBoolean767 = true;
				solid = true;
				continue;
			}
		}

	}

	public static RSObjectDefinition valueOf(int id) {
		
		RSObjectDefinition obj = definitions[id];
		
		if (obj != null) {
			return obj;
		}
		
		obj = new RSObjectDefinition();
		datBuffer.position(positions[id]);

		definitions[id] = obj;
		
		obj.id = id;
		obj.singleParse();
		return obj;
	}

	public RSObjectDefinition() {
		models = null;
		children = null;
		name = null;
		description = null;
		recol_s = null;
		recol_d = null;
		xsize = 1;
		ysize = 1;
		aBoolean767 = true;
		aBoolean757 = true;
		hasActions = false;
		aBoolean762 = false;
		aBoolean769 = false;
		aBoolean764 = false;
		anInt781 = -1;
		anInt775 = 16;
		ambience = 0;
		contrast = 0;
		actions = null;
		anInt746 = -1;
		anInt758 = -1;
		aBoolean751 = false;
		solid = true;
		resizex = 128;
		resizey = 128;
		resizez = 128;
		anInt768 = 0;
		anInt738 = 0;
		anInt745 = 0;
		anInt783 = 0;
		aBoolean736 = false;
		aBoolean766 = false;
		anInt760 = -1;
		anInt774 = -1;
		anInt749 = -1;
		masklookup = null;
	}

	public boolean aBoolean736;
	public byte ambience;
	public int anInt738;
	public String name;
	public int resizez;
	public byte contrast;
	public int xsize;
	public int anInt745;
	public int anInt746;
	public int[] recol_d;
	public int resizex;
	public int anInt749;
	public boolean aBoolean751;
	public static boolean lowMem;
	public int type;
	public static int[] streamIndices;
	public boolean aBoolean757;
	public int anInt758;
	public int masklookup[];
	public int anInt760;
	public int ysize;
	public boolean aBoolean762;
	public boolean aBoolean764;
	public boolean aBoolean766;
	public boolean aBoolean767;
	public int anInt768;
	public boolean aBoolean769;
	public static int cacheIndex;
	public int resizey;
	public int[] models;
	public int anInt774;
	public int anInt775;
	public int[] children;
	public String description;
	public boolean hasActions;
	public boolean solid;
	public int anInt781;
	public int anInt783;
	public int[] recol_s;
	public String actions[];
	public int orientation;

	public int getId() {
		return id;
	}

}

