package com.osps.client.cache.anim;

import com.osps.client.Stream;
import com.osps.client.cache.StreamLoader;

public final class Animation {

    public static void unpackConfig(StreamLoader streamLoader)
    {
		Stream stream = new Stream(streamLoader.getDataForName("seq.dat"));
		//Stream stream = new Stream(FileOperations.ReadFile(signlink.findcachedir()+ "seq.dat"));
        int length = stream.readUnsignedWord();
        if(anims == null)
            anims = new Animation[length + 5000];
        for(int j = 0; j < length; j++) {
            if(anims[j] == null)
                anims[j] = new Animation();
            anims[j].readValues(stream);

			/*
			 * if (j == 5070) { anims[j] = new Animation(); anims[j].frameCount
			 * = 9; anims[j].loopDelay = -1; anims[j].forcedPriority = 5;
			 * anims[j].leftHandItem = -1; anims[j].rightHandItem = -1;
			 * anims[j].frameStep = 99; anims[j].resetWhenWalk = 0;
			 * anims[j].priority = 0; anims[j].delayType = 2;
			 * anims[j].oneSquareAnimation = false; anims[j].frameIDs = new
			 * int[] { 11927608, 11927625, 11927598, 11927678, 11927582,
			 * 11927600, 11927669, 11927597, 11927678 }; anims[j].delays = new
			 * int[] { 5, 4, 4, 4, 5, 5, 5, 4, 4 }; } if (j == 5069) {
			 * anims[j].frameCount = 15; anims[j].loopDelay = -1;
			 * anims[j].forcedPriority = 6; anims[j].leftHandItem = -1;
			 * anims[j].rightHandItem = -1; anims[j].frameStep = 99;
			 * anims[j].resetWhenWalk = 0; anims[j].priority = 0;
			 * anims[j].delayType = 1; anims[j].oneSquareAnimation = false;
			 * anims[j].frameIDs = new int[] { 11927613, 11927599, 11927574,
			 * 11927659, 11927676, 11927562, 11927626, 11927628, 11927684,
			 * 11927647, 11927602, 11927576, 11927586, 11927653, 11927616 };
			 * anims[j].delays = new int[] { 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5,
			 * 5, 5, 5 }; } if (j == 5072) { anims[j].frameCount = 21;
			 * anims[j].loopDelay = 1; anims[j].forcedPriority = 8;
			 * anims[j].leftHandItem = -1; anims[j].rightHandItem = -1;
			 * anims[j].frameStep = 99; anims[j].resetWhenWalk = 0;
			 * anims[j].priority = 0; anims[j].delayType = 2;
			 * anims[j].oneSquareAnimation = false; anims[j].frameIDs = new
			 * int[] { 11927623, 11927595, 11927685, 11927636, 11927670,
			 * 11927579, 11927664, 11927666, 11927661, 11927673, 11927633,
			 * 11927624, 11927555, 11927588, 11927692, 11927667, 11927556,
			 * 11927630, 11927695, 11927643, 11927640 }; anims[j].delays = new
			 * int[] { 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
			 * 3, 3 }; } if (j == 5073) { anims[j].frameCount = 21;
			 * anims[j].loopDelay = -1; anims[j].forcedPriority = 9;
			 * anims[j].leftHandItem = -1; anims[j].rightHandItem = -1;
			 * anims[j].frameStep = 99; anims[j].resetWhenWalk = 0;
			 * anims[j].priority = 0; anims[j].delayType = 2;
			 * anims[j].oneSquareAnimation = false; anims[j].frameIDs = new
			 * int[] { 11927640, 11927643, 11927695, 11927630, 11927556,
			 * 11927667, 11927692, 11927588, 11927555, 11927624, 11927633,
			 * 11927673, 11927661, 11927666, 11927664, 11927579, 11927670,
			 * 11927636, 11927685, 11927595, 11927623 }; anims[j].delays = new
			 * int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2,
			 * 2, 2 }; }
			 */
			if (j == 5806) {
			anims[j].count = 55;
			anims[j].anInt356 = -1;
			anims[j].anInt359 = 6;
			anims[j].anInt360 = -1;
			anims[j].anInt361 = -1;
			anims[j].anInt362 = 99;
			anims[j].anInt363 = 0;
			anims[j].anInt364 = 0;
			anims[j].anInt365 = 2;
			anims[j].aBoolean358 = true;
			anims[j].frameLengths = new int[] { 11927612, 11927677, 11927615, 11927573, 11927618, 11927567, 11927564,
					11927606, 11927675, 11927657, 11927690, 11927583, 11927672, 11927552, 11927563, 11927683, 11927639,
					11927635, 11927668, 11927614, 11927627, 11927610, 11927693, 11927644, 11927656, 11927660, 11927629,
					11927635, 11927668, 11927614, 11927627, 11927610, 11927693, 11927644, 11927656, 11927660, 11927635,
					11927668, 11927614, 11927560, 11927687, 11927577, 11927569, 11927557, 11927569, 11927577, 11927687,
					11927560, 11927651, 11927611, 11927680, 11927622, 11927691, 11927571, 11927601 };
			anims[j].anIntArray355 = new int[] { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
					4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 3 };
			}
			if (j == 5807) {
			anims[j].count = 53;
			anims[j].anInt356 = -1;
			anims[j].anInt359 = 6;
			anims[j].anInt360 = -1;
			anims[j].anInt361 = -1;
			anims[j].anInt362 = 99;
			anims[j].anInt363 = 0;
			anims[j].anInt364 = 0;
			anims[j].anInt365 = 2;
			anims[j].aBoolean358 = true;
			anims[j].frameLengths = new int[] { 11927612, 11927677, 11927615, 11927573, 11927618, 11927567, 11927564,
					11927606, 11927675, 11927657, 11927690, 11927583, 11927672, 11927552, 11927563, 11927683, 11927639,
					11927635, 11927668, 11927614, 11927627, 11927610, 11927693, 11927644, 11927656, 11927660, 11927629,
					11927635, 11927668, 11927614, 11927627, 11927610, 11927693, 11927644, 11927656, 11927604, 11927637,
					11927688, 11927696, 11927681, 11927605, 11927681, 11927696, 11927688, 11927637, 11927604, 11927656,
					11927611, 11927680, 11927622, 11927691, 11927571, 11927601 };
			anims[j].anIntArray355 = new int[] { 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
					4, 4, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 3 };
			}
			
        }
    }

    public int method258(int i) {
        int j = anIntArray355[i];
        if(j == 0)
        {
            Class36 class36 = Class36.method531(frameLengths[i]);
            if(class36 != null)
                j = anIntArray355[i] = class36.anInt636;
        }
        if(j == 0)
            j = 1;
        return j;
    }

private void readValues(Stream stream) {
			int i;
			while ((i = stream.readUnsignedByte()) != 0){
				
			
			if (i == 1) {
				/*
				 * anInt352 = stream.readUnsignedWord();
				anIntArray353 = new int[anInt352];
				anIntArray354 = new int[anInt352];
				anIntArray355 = new int[anInt352];
				for (int j = 0; j < anInt352; j++) {
					anIntArray353[j] = stream.readUnsignedWord();
					anIntArray354[j] = -1;
				}

				for (int j = 0; j < anInt352; j++)
					anIntArray355[j] = stream.readUnsignedWord();

			//	for (int j = 0; j < anInt352; j++)
			//		anIntArray355[j] += stream.readUnsignedWord() << 16;


				 */
				count = stream.readUnsignedWord();
				frameLengths = new int[count];
				anIntArray354 = new int[count];
				anIntArray355 = new int[count];
				for (int j = 0; j < count; j++) {
						frameLengths[j] = stream.readDWord();
						anIntArray354[j] = -1;
					}
				

					for (int j = 0; j < count; j++)
						anIntArray355[j] = stream.readUnsignedByte();
				
			} else if (i == 2)
				anInt356 = stream.readUnsignedWord();
			else if (i == 3) {
				int k = stream.readUnsignedByte();
				anIntArray357 = new int[k + 1];
				for (int l = 0; l < k; l++)
					anIntArray357[l] = stream.readUnsignedByte();
				anIntArray357[k] = 9999999;
			} else if (i == 4)
				aBoolean358 = true;
			else if (i == 5)
				anInt359 = stream.readUnsignedByte();
			else if (i == 6)
				anInt360 = stream.readUnsignedWord();
			else if (i == 7)
				anInt361 = stream.readUnsignedWord();
			else if (i == 8)
				anInt362 = stream.readUnsignedByte();
			else if (i == 9)
				anInt363 = stream.readUnsignedByte();
			else if (i == 10)
				anInt364 = stream.readUnsignedByte();
			else if (i == 11)
				anInt365 = stream.readUnsignedByte();
			else if (i == 12)
				stream.readDWord();
			else
				System.out.println("Error unrecognised seq config code: " + i);
			}
			if (count == 0) {
			count = 1;
			frameLengths = new int[1];
			frameLengths[0] = -1;
			anIntArray354 = new int[1];
			anIntArray354[0] = -1;
			anIntArray355 = new int[1];
			anIntArray355[0] = -1;
		}
		if (anInt363 == -1)
			if (anIntArray357 != null)
				anInt363 = 2;
			else
				anInt363 = 0;
		if (anInt364 == -1) {
			if (anIntArray357 != null) {
				anInt364 = 2;
				return;
			}
			anInt364 = 0;
		}
	}

    private Animation() {
        anInt356 = -1;
        aBoolean358 = false;
        anInt359 = 5;
        anInt360 = -1; //Removes shield
        anInt361 = -1; //Removes weapon
        anInt362 = 99;
        anInt363 = -1; //Stops character from moving
        anInt364 = -1;
        anInt365 = 1; 
    }

    public static Animation anims[];
    public int count;
    public int frameLengths[];
    public int anIntArray354[];
    public int[] anIntArray355;
    public int anInt356;
    public int anIntArray357[];
    public boolean aBoolean358;
    public int anInt359;
    public int anInt360;
    public int anInt361;
    public int anInt362;
    public int anInt363;
    public int anInt364;
    public int anInt365;
    public static int anInt367;
}
