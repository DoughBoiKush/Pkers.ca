package org.osps.model.content.enchanting;

import org.osps.Config;
import org.osps.model.players.Player;

/*
 * Jewellery Enchanting - By Mikey96
 */

public class Jewellery {
	
	public enum JewelleryData {
		
		SAPPHIRE_RING(1637, 2550, 7, 17.5),
		SAPPHIRE_NECK(1656, 3853, 7, 17.5),
		SAPPHIRE_AM(1694, 1727, 7, 17.5),
		SAPPHIRE_BRAC(11072, 11074, 7, 17.5),
		EMERALD_RING(1639, 2552, 27, 37),
		EMERALD_NECK(1658, 5521, 27, 37),
		EMERALD_AM(1696, 1729, 27, 37),//
		EMERALD_BRAC(11076, 11079, 27, 37),
		RUBY_RING(1641, 2568, 49, 59),
		RUBY_NECK(1660, 11194, 49, 59),
		RUBY_AM(1698, 1725, 49, 59),//
		RUBY_BRAC(11085, 11088, 49, 59),
		DIAMOND_RING(1643, 2570, 57, 67),
		DIAMOND_NECK(1662, 11090, 57, 67),
		DIAMOND_AM(1700, 1731, 57, 67),
		//DIAMOND_BRAC(11092, ),
		//DSTONE_RING(1645, ),
		DSTONE_NECK(1664, 11968, 68, 78),
		DSTONE_AM(1702, 1704, 68, 78),
		DSTONE_BRAC(11115, 11126, 68, 78),
		ONYX_RING(6575, 6583, 87, 97),
		//ONYX_NECK(6565, ),
		ONYX_AM(6581, 6585, 87, 97),
		ONYX_BRAC(11130, 11133, 87, 97);
		
		private int unId, enchantedId, levelReq;
		private double expGained;
		
		JewelleryData(int unId, int enchantedId, int levelReq, double expGained) {
			this.unId = unId;
			this.enchantedId = enchantedId;
			this.levelReq = levelReq;
			this.expGained = expGained;
		}
		
		public int getUnId() {
			return unId;
		}
		
		public int getEnchId() {
			return enchantedId;
		}
		
		public int getLevelReq() {
			return levelReq;
		}
		
		public double getExp() {
			return expGained;
		}
		
	}
	
	public static boolean enchant(Player c, int itemId) {
		for (JewelleryData jd : JewelleryData.values()) {
			if (jd.getUnId() == itemId) {
				if (!c.getItems().playerHasItem(jd.getUnId(), 1)) {
					return false;
				}
				if (c.playerLevel[c.playerMagic] < jd.getLevelReq()) {
					c.sendMessage("You need a magic level of " + jd.getLevelReq() + " to enchant this " + c.getItems().getItemName(jd.getUnId()) + ".");
					return false;
				}
				c.animation(c.MAGIC_SPELLS[50][2]);
			 	c.gfx100(c.MAGIC_SPELLS[50][3]); 
				c.getItems().deleteItem(jd.getUnId(), 1);
				c.getItems().addItem(jd.getEnchId(), 1);
				c.getPA().addSkillXP(jd.getExp() * 1 / 1, c.playerMagic);
				c.getPA().sendFrame106(6);
				c.getPA().refreshSkill(6);
				return true;
			}
		}
		return false;
	}

}
