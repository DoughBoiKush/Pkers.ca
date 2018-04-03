package org.osps.model.players.skills.Runecrafting;

import org.osps.model.players.Player;

/**
 * @author Micheal 
 * http://www.rune-server.org/members/01053/
 */

public class Portals {

	private static enum PortalData {
		Death_Portal(14894, 3039, 4835, 0),
		Air_Portal(14841, 3039, 4835, 0),
		Mind_Portal(14842, 3039, 4835, 0),
		Water_Portal(14843, 3039, 4835, 0),
		Earth_Portal(14844, 3039, 4835, 0),
		Fire_Portal(14845, 3039, 4835, 0),
		Body_Portal(14846, 3039, 4835, 0),
		Cosmic_Portal(14847, 3039, 4835, 0),
		Nature_Portal(14892, 3039, 4835, 0),
		Chaos_Portal(14893, 3039, 4835, 0),
		Law_Portal(14848, 3039, 4835, 0);

		private int portalType, coordX, coordY, height;

		private PortalData(int portalType, int coordX, int coordY, int height) {
			this.portalType = portalType;
			this.coordX = coordX;
			this.coordY = coordY;
			this.height = height;
		}

		public int getPortalId() {
			return portalType;
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
		for (PortalData r : PortalData.values()) {
			if (objectId == r.getPortalId()) {
				c.getPA().movePlayer(r.getCoordinateX(), r.getCoordinateY(), r.getHeight());
			}
		}
	}
}
