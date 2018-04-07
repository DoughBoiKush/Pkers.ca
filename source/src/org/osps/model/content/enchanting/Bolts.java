package org.osps.model.content.enchanting;

import org.osps.model.players.Player;

/*
 * Bolt Enchanting - By Mikey96
 */

public class Bolts {
	
	public enum BoltData {
		
		OPAL(879, 9236, 4, 9),
		SAPPHIRE(9337, 9240, 7, 17),
		JADE(9335, 9237, 14, 19),
		PEARL(880, 9238, 24, 29),
		EMERALD(9338, 9241, 27, 37),
		TOPAZ(9336, 9239, 29, 39),
		RUBY(9339, 9242, 49, 59),
		DIAMOND(9340, 9243, 57, 67),
		DRAGON(9341, 9244, 68, 78),
		ONYX(9342, 9245, 87, 97);
		
		private int boltId, enchantId, levelReq;
		private double expGained;
		
		BoltData(int boltId, int enchantId, int levelReq, double expGained) {
			this.boltId = boltId;
			this.enchantId = enchantId;
			this.levelReq = levelReq;
			this.expGained = expGained;
		}
		
		public int getBoltId() {
			return boltId;
		}
		
		public int getEnchantId() {
			return enchantId;
		}
		
		public int getLevelReq() {
			return levelReq;
		}
		
		public double getExp() {
			return expGained;
		}
		
	}
	
	public static boolean enchant(Player c, int itemId, int amount) {
		for (BoltData bd : BoltData.values()) {
			if (bd.getBoltId() == itemId) {
				if (!c.getItems().playerHasItem(bd.getBoltId(), amount)) {
					c.sendMessage("You don't have that amount of " + c.getItems().getItemName(itemId) + ".");
					return false;
				}
				if (c.playerLevel[c.playerMagic] < bd.getLevelReq()) {
					c.sendMessage("You need a level of " + bd.getLevelReq() + " to do this");
					return false;
				}
				c.animation(c.MAGIC_SPELLS[50][2]);
			 	c.gfx100(c.MAGIC_SPELLS[50][3]); 
				c.getItems().deleteItem(itemId, amount);
				c.getItems().addItem(bd.getEnchantId(), amount);
				//c.getPA().addSkillXP(bd.getExp() * Config.MAGIC_EXP_RATE * amount, c.playerMagic);
				c.getPA().sendFrame106(6);
				c.getPA().refreshSkill(6);
				return true;
			}
		}
		return false;
	}

}
