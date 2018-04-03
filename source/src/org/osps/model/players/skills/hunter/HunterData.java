package org.osps.model.players.skills.hunter;

public class HunterData {
	
	public enum ImpData {
		
		//NPCID - JARID - EXP - LEVEL REQ - MISS CHANCE
		
		BABY(1635, 11238, 25, 1, 50),
		YOUNG(1636, 11240, 65, 22, 40),
		GOURMET(1637, 11242, 24, 28, 35),
		EARTH(1638, 11244, 126, 36, 30),
		ESSENCE(1639, 11246, 29, 42, 25),
		ECLECTIC(1640, 11248, 289, 50, 25),
		NATURE(1641, 11250, 353, 58, 20),
		MAGPIE(1642, 11252, 54, 65, 10),
		NINJA(1643, 11254, 240, 74, 5),
		DRAGON(1644, 11256, 300, 83, 3);
		
		
		private int npcId, jarId, levelReq, missChance;
		private double expGained;
		
		private ImpData(int npcId, int jarId, double expGained, int levelReq, int missChance) {
			this.npcId = npcId;
			this.jarId = jarId;
			this.expGained = expGained;
			this.levelReq = levelReq;
			this.missChance = missChance;
		}
		
		public int getNpcId() {
			return npcId;
		}
		
		public int getJarId() {
			return jarId;
		}
		
		public double getExp() {
			return expGained;
		}
		
		public int getLevelReq() {
			return levelReq;
		}
		
		public int getChance() {
			return missChance;
		}
		
	}

}
