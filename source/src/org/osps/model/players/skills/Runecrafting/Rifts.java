package org.osps.model.players.skills.Runecrafting;

import org.osps.model.players.Player;

/**
 * @author Micheal 
 * http://www.rune-server.org/members/01053/
 */

public class Rifts {

	private static enum riftData {
		Air_Rift(25378, 1, 2841, 4829, 0),
		Mind_Rift(25379, 2, 2793, 4828, 0),
		Water_Rift(25376, 5, 2725, 4832, 0),
		Earth_Rift(24972, 9, 2655, 4830, 0),
		Fire_Rift(24971, 14, 2574, 4848, 0),
		Body_Rift(24973, 20, 2521, 4835, 0),
		Cosmic_Rift(24974, 27, 2162, 4833, 0),
		Chaos_Rift(24976, 35, 2281, 4837, 0),
		Nature_Rift(24975, 44, 2400, 4835, 0),
		Law_Rift(25034, 54, 2464, 4818, 0),
		Death_Rift(25035, 65, 2208, 4830, 0),
		Blood_Rift(25380, 77, 1716, 3827, 0), //Don't know the coordinates
		Soul_Rift(25377, 90, 1813, 3852, 0); //Don't know the coordinates

		private int riftType, requirement, coordX, coordY, height;

		private riftData(int riftType, int requirement, int coordX, int coordY, int height) {
			this.riftType = riftType;
			this.requirement = requirement;
			this.coordX = coordX;
			this.coordY = coordY;
			this.height = height;
		}

		public int getRiftId() {
			return riftType;
		}

		public int getRequiredLevel() {
			return requirement;
		}

		public int getCoordinateX() {
			return coordX;
		}

		public int getCoordinateY() {
			return coordY;
		}

		public int getHeight() {
			return height;
		}
	}

	public static void teleport(final Player c, final int objectId) {
		for (riftData r : riftData.values()) {
			if (objectId == r.getRiftId()) {
				if (c.playerLevel[20] >= r.getRequiredLevel()) {
					c.getPA().movePlayer(r.getCoordinateX(), r.getCoordinateY(), r.getHeight());
				} else {
					c.sendMessage("You need a runecrafting level of " + r.getRequiredLevel() + " to teleport to this altar.");
				}
			}
		}
	}
}
