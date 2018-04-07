package org.osps.model.players.skills.Runecrafting;

import org.osps.model.players.Player;

/**
 * @Author 01053/Micheal
 * http://www.rune-server.org/members/01053/
 */

public class Runecrafting {

	private static enum AltarData {

		Air_Altar(14897, 556, 5527, 5, 190, new int[] {1, 11, 22, 33, 44, 55, 66, 77, 88, 99}),
		Mind_Altar(14898, 558, 5529, 6, 200, new int[] {1, 14, 28, 42, 56, 70, 84, 98}),
		Water_Altar(14899, 555, 5531, 7, 210, new int[] {5, 19, 38, 57, 76, 95}),
		Earth_Altar(14900, 557, 5535, 8, 220, new int[] {9, 26, 52, 78}),
		Fire_Altar(14901, 554, 5537, 9, 230, new int[] {14, 35, 70}),
		Body_Altar(14902, 559, 5533, 10, 250, new int[] {20, 46, 92}),
		Cosmic_Altar(14903, 564, 5539, 11, 280, new int[] {27, 59}),
		Chaos_Altar(14906, 562, 5543, 12, 310, new int[] {35, 74}),
		Nature_Altar(14905, 561, 5541, 13, 330, new int[] {44, 91}),
		Law_Altar(14904, 563, 5545, 14, 360, new int[] {54}),
		Death_Altar(14907, 560, 5547, 15, 380, new int[] {65}),
		Astral_Altar(14911, 9075, 5547, 15, 390, new int[] {40, 82}),
		Blood_Altar(27978, 565, 5549, 16, 410, new int[] {77}),
		Soul_Altar(27980, 566, 5551, 17, 430, new int[] {90});

		private int altarType, runeType, tiaraType, experience;
		private double tiaraBonus;
		private int[] multiplier;

		private AltarData(int altarType, int runeType, int tiaraType, double tiaraBonus, int experience, int[] multiplier) {
			this.altarType = altarType;
			this.runeType = runeType;
			this.tiaraType = tiaraType;
			this.tiaraBonus = tiaraBonus;
			this.experience = experience;
			this.multiplier = multiplier;
		}

		public int getAltarId() {
			return altarType;
		}

		public int getRuneId() {
			return runeType;
		}
		
		public int getTiaraId() {
			return tiaraType;
		}
		
		public double getTiaraBonus() {
			return tiaraBonus;
		}

		public double getXp() {
			return experience;
		}

		public int getLevel() {
			return multiplier[0];
		}
	}

	public static void Craft(final Player c, final int objectId) {
		for (AltarData r : AltarData.values()) {
			if (objectId == r.getAltarId()) {
				if (c.playerLevel[20] >= r.getLevel()) {
					if (c.getItems().playerHasItem(1436)) {
						c.animation(791);
						c.gfx100(186);
						int multiplier = 1;
						for (int i = 1; i < r.multiplier.length; i++) {
							if (c.playerLevel[20] >= r.multiplier[i]) {
								multiplier = i;
							}
						}
						if(r == AltarData.Nature_Altar)
							multiplier *= 2;
						while(c.getItems().playerHasItem(1436) && (c.playerEquipment[c.playerHat] == r.getTiaraId()) ) {
							c.getItems().deleteItem(1436, 1);
							c.getItems().addItem(r.getRuneId(), multiplier);
							c.getPA().addSkillXP((int) (r.getXp() + r.getTiaraBonus()) * 3, 20);
						}
						while (c.getItems().playerHasItem(1436) && (c.playerEquipment[c.playerHat] != r.getTiaraId())) {
							c.getItems().deleteItem(1436, 1);
							c.getItems().addItem(r.getRuneId(), multiplier);
							c.getPA().addSkillXP((int) (r.getXp()) * 3, 20);
						} 
					}
				} else {
					c.sendMessage("You need a runecrafting level of " + r.getLevel() + " to craft this rune.");
					return;
				}
			}
		}
	}
}
