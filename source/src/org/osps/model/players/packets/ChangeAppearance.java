package org.osps.model.players.packets;

import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;

/**
 * Change appearance
 **/
public class ChangeAppearance implements PacketType {
//LOL I wrote this
	
	private static final int[][] MALE_VALUES = { { 0, 8 }, // head
			{ 10, 17 }, // jaw
			{ 18, 25 }, // torso
			{ 26, 31 }, // arms
			{ 33, 34 }, // hands
			{ 36, 40 }, // legs
			{ 42, 43 }, // feet
	};

	private static final int[][] FEMALE_VALUES = { { 0, 8 }, // head
			{ 10, 17 }, // jaw
			{ 18, 25 }, // torso
			{ 26, 31 }, // arms
			{ 33, 34 }, // hands
			{ 36, 40 }, // legs
			{ 42, 43 }, // feet
	};

	private static final int[][] ALLOWED_COLORS = { { 0, 11 }, // hair color
			{ 0, 15 }, // torso color
			{ 0, 15 }, // legs color
			{ 0, 5 }, // feet color
			{ 0, 16 } // skin color
	};

	@Override
	public void processPacket(final Player c, Packet packet) {
		final int gender = packet.getByte();
		   if(!c.canChangeAppearance)
            return;
		if (gender != 0 && gender != 1)
			return;

		final int[] apperances = new int[MALE_VALUES.length]; // apperance's
																// value
		// check
		for (int i = 0; i < apperances.length; i++) {
			int value = packet.getByte();
			if (value < (gender == 0 ? MALE_VALUES[i][0] : MALE_VALUES[i][0]) || value > (gender == 0 ? MALE_VALUES[i][1] : MALE_VALUES[i][1]))
				value = (gender == 0 ? MALE_VALUES[i][0] : MALE_VALUES[i][0]);
			apperances[i] = value;
		}

		final int[] colors = new int[ALLOWED_COLORS.length]; // color value
																// check
		for (int i = 0; i < colors.length; i++) {
			int value = packet.getByte();
			if (value < ALLOWED_COLORS[i][0] || value > ALLOWED_COLORS[i][1])
				value = ALLOWED_COLORS[i][0];
			colors[i] = value;
		}
		
		if (!c.canUsePackets) {
			return;
		}

		if (c.canChangeAppearance) {
			//c.playerAppearance[0] = gender; // gender
			c.playerAppearance[1] = apperances[0]; // head
			c.playerAppearance[2] = apperances[2]; // torso
			c.playerAppearance[3] = apperances[3]; // arms
			c.playerAppearance[4] = apperances[4]; // hands
			c.playerAppearance[5] = apperances[5]; // legs
			c.playerAppearance[6] = apperances[6]; // feet
			c.playerAppearance[7] = apperances[1]; // beard
			c.playerAppearance[8] = colors[0]; // hair colour
			c.playerAppearance[9] = colors[1]; // torso colour
			c.playerAppearance[10] = colors[2]; // legs colour
			c.playerAppearance[11] = colors[3]; // feet colour
			c.playerAppearance[12] = colors[4]; // skin colour

			c.getPA().removeAllWindows();
			c.getPA().requestUpdates();
			c.canChangeAppearance = false;
		}
	}

}