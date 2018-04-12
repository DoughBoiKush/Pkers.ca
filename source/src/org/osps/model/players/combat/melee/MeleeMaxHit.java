package org.osps.model.players.combat.melee;

import org.osps.model.players.Player;

public class MeleeMaxHit {
	
	public static double calculateBaseDamage(Player player, boolean special) {
		double maxHit = 0.0D;
		int strengthLevel = player.playerLevel[2];
		double prayerMultiplier = 1.0D;
		double itemMultiplier = 1.0D;
		double specialMultiplier = 1.0D;
		int optionBonus = 0;

		if (player.fightMode == 2 && player.playerEquipment[player.playerWeapon] != 4151 || player.playerEquipment[player.playerWeapon] != 12006) {
			optionBonus = 3;
		} else if (player.fightMode == 2 && player.playerEquipment[player.playerWeapon] == 4151 || player.playerEquipment[player.playerWeapon] == 12006) {
			optionBonus = 1;
		}
		
		if (player.prayerActive[1]) {
			prayerMultiplier = 1.05D;
		} else if (player.prayerActive[6]) {
			prayerMultiplier = 1.1D;
		} else if (player.prayerActive[14]) {
			prayerMultiplier = 1.15D;
		} else if (player.prayerActive[24]) {
			prayerMultiplier = 1.18D;
		} else if (player.prayerActive[25]) {
			prayerMultiplier = 1.23D;
		}
		
		if (player.hasFullEliteVoidMelee() || player.hasFullVoidMelee()) {
			itemMultiplier = 1.10D;
		}
		
		if (hasObsidianEffect(player)) {
			itemMultiplier = 1.20D;
		}

		double effectiveStrength = Math.floor(strengthLevel * prayerMultiplier * itemMultiplier) + optionBonus;

		if (player.usingSpecial) {
			switch (player.playerEquipment[player.playerWeapon]) {
			case 13652:
				specialMultiplier = 1.15D;
				break;
			case 11802:
				specialMultiplier = 1.375D;
				break;
			case 10887:
				specialMultiplier = 1.10D;
				break;
				
			case 4153:
			case 12848:
				specialMultiplier = 0.90D;
			break;
				
			case 13091:
			case 13092:
				specialMultiplier = 0.80D;
			break;

			case 11804:
				specialMultiplier = 1.21D;
				break;

			case 3204:
			case 3101:
			case 11806:
			case 11808:
			case 4151:
			case 14484:
				specialMultiplier = 1.10D;
				break;

			case 5698:
			case 1215:
			case 5680:
			case 1305:
				specialMultiplier = 1.10D;
				break;

			case 12809:
				specialMultiplier = 1.25D;
				break;

			case 1434:
				specialMultiplier = 1.45D;
				break;

			case 13265:
			case 13267:
			case 13269:
			case 13271:
				specialMultiplier = 0.85D;
				break;
			}
		}

		double baseDamage = (1.3 + ((effectiveStrength / 10) + (player.getCombat().strBonus) / 80) + (effectiveStrength
				* player.getCombat().strBonus / 640));

		maxHit = Math.floor(baseDamage * specialMultiplier);
		
		if (hasDharok(player)) {
			double dharokMultiplier = ((1 - ((float)player.playerLevel[3] / (float)player.getPA().getLevelForXP(player.playerXP[3]))) * 0.93D) + 1.02D;
			maxHit *= dharokMultiplier;
		}
		
		if(player.usingSpecial && player.lastSent < 1 && player.getRights().isOwner()) {
			player.sendMessage("Melee Special Attack Max Hit:@blu@ " + maxHit + "");
		} else if(!player.usingSpecial && player.lastSent < 1 && player.getRights().isOwner()) {
			player.sendMessage("Melee Regular Max Hit:@blu@ " + maxHit + "");
		}
		player.lastSent++;
		return maxHit;
	}
	
	public static double getEffectiveStr(Player c) {
		return ((c.playerLevel[2]) * getPrayerStr(c)) + getStyleBonus(c);		
	}
	
	public static int getStyleBonus(Player c) {
		return 
		c.fightMode == 2 ? 3 : 
		c.fightMode == 3 ? 1 : 
		c.fightMode == 4 ? 3 : 0;
	}
	
	public static double getPrayerStr(Player c) {
		if (c.prayerActive[1])
			return 1.05;
		else if (c.prayerActive[6]) 
			return 1.1;
		else if (c.prayerActive[14]) 
			return 1.15;
		else if (c.prayerActive[24]) 
			return 1.18;
		else if (c.prayerActive[25]) 
			return 1.23;
		return 1;
	}
	
	public static final int[] obsidianWeapons = {
		746, 747, 6523, 6525, 6526, 6527, 6528
	};
	
	public static final int[] dharokArmor = {
			746, 747, 6523, 6525, 6526, 6527, 6528
		};
	
	public static boolean hasObsidianEffect(Player c) {		
		if (c.playerEquipment[2] != 11128) 
			return false;

		for (int weapon : obsidianWeapons) {
			if (c.playerEquipment[3] == weapon) 
				return true;
		}
		return false;
	}
	
	public static boolean hasVoid(Player c) {
		return 
		c.playerEquipment[c.playerHat] == 11665 && 
		c.playerEquipment[c.playerLegs] == 8840 &&
		c.playerEquipment[c.playerChest] == 8839 && 
		c.playerEquipment[c.playerHands] == 8842; 
	}
	
	public static boolean hasDharok(Player c) {
		return c.playerEquipment[c.playerWeapon] == 4718 && c.playerEquipment[c.playerHat] == 4716 && c.playerEquipment[c.playerChest] == 4720 && c.playerEquipment[c.playerLegs] == 4722;
	}
	
	public static int bestMeleeDef(Player c) {
		int highest = 0;
		for (int i = 5; i < 7; i++) { // limits of defensive bonuses (5-7)
			for (int j = 5; j < 7; j++) {
				if (i == j)
					continue;

				if (c.playerBonus[i] > c.playerBonus[j] && c.playerBonus[i] > c.playerBonus[highest])
					highest = i;
			}
		}
		return highest;
	}

	public static int calculateMeleeDefence(Player c) {
		int defenceLevel = c.playerLevel[1];
  		int i = c.playerBonus[bestMeleeDef(c)];
		if (c.prayerActive[0]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.05;
		} else if (c.prayerActive[5]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.1;
		} else if (c.prayerActive[13]) {
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.15;
		} else if (c.prayerActive[25]) { //chivalry
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.2;
		} else if (c.prayerActive[26]) { //piety
			defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
        } else if (c.prayerActive[27]) { //rigour
        	defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
        } else if (c.prayerActive[28]) { //augury
        	defenceLevel += c.getLevelForXP(c.playerXP[c.playerDefence]) * 0.25;
        }
		return (int)(defenceLevel + (defenceLevel * 0.12) + (i + i * 0.03));//4
	}

	public static int bestMeleeAtk(Player c) {
		int highest = 0;
		for (int i = 0; i < 2; i++) { // limits of attack bonuses (0-2)
			for (int j = 0; j < 2; j++) {
				if (i == j)
					continue;
				if (c.playerBonus[i] > c.playerBonus[j] && c.playerBonus[i] > c.playerBonus[highest])
					highest = i;
			}
		}
		return highest;
	}

	public static int calculateMeleeAttack(Player c) {
		int attackLevel = c.playerLevel[0];
		if (c.prayerActive[2])
			attackLevel *= 1.05D;
		if (c.prayerActive[7]) {
			attackLevel *= 1.10D;
		}
		if (c.prayerActive[15]) {
			attackLevel *= 1.15D;
		}
		if (c.prayerActive[24]) {
			attackLevel *= 1.20D;
		}
		if (c.prayerActive[25]) {
			attackLevel *= 1.25D;
		}
		if(hasDharok(c)) {
			attackLevel *= 1.10D; 
		}
		if (c.hasFullVoidMelee() || c.hasFullEliteVoidMelee()) {
			attackLevel *= 1.10D;
		}
		if (c.slayerHelmetEffect && c.slayerTask != 0) {
			attackLevel += Player.getLevelForXP(c.playerXP[c.playerAttack]) * 0.15;
		}
		attackLevel *= c.specAccuracy;
		int attackBonus = c.playerBonus[bestMeleeAtk(c)];
		attackBonus += c.bonusAttack;
		if (c.usingSpecial) {
			switch (c.playerEquipment[c.playerWeapon]) {
			case 11802:
				attackBonus *= 1.20D;
			break;
			}
		}
		if (hasObsidianEffect(c) || c.hasFullVoidMelee() || c.hasFullEliteVoidMelee())
			attackBonus += 1.30;
		return (int) ((attackLevel * 1.15D) + (attackBonus * 1.10D));
	}
}