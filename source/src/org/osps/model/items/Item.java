package org.osps.model.items;

import org.osps.model.players.Player;

/**
 * Handles The Item Wielding Of The Server Used When Hardcoding Additional Items
 * Fixing Bugged Items
 * 
 * @author Sanity Revised by Shawn Notes by Shawn
 */
public class Item {
	
	enum Tier {
		BRONZE("bronze"),
		IRON("iron"),
		STEEL("steel"),
		BLACK("black"),
		MITH("mithril"),
		ADDY("adamant"),
		RUNE("rune"),
		DRAGON("dragon");
		
		Tier(String s) {
			this.s = s;
		}
		
		@Override
		public String toString() {
			return s;
		}
		
		String s;
	}
	
	public static int getNextTier(int tier) {
		String next = getNextTierItem(getItemName(tier));
		return ItemDefinition.getIdForName(next);
	}
	
	public static String getNextTierItem(String name) {
		for(int i = Tier.values().length - 1; i >= 0; i--) {
			if(name.toLowerCase().contains(Tier.values()[i].toString())) {
				if(i != Tier.values().length - 1) {
					name = name.toLowerCase().replaceAll(Tier.values()[i].toString(), Tier.values()[i + 1].toString());
				}
			}
		}
		return name;
	}

	public static String getNameForItemInSlot(Player player, int slot) {
		if (slot < 0 || slot > player.playerEquipment.length) {
			throw new IllegalStateException();
		}
		int id = player.playerEquipment[slot];
		return id == -1 ? "null" : player.getItems().getItemName(id);
	}

	/**
	 * Calls if an item is a full body item.
	 */
	public static boolean isFullBody(int itemId) {
		if(itemId == -1)
			return false;
		return ItemDefinition.forId(itemId).isPlatebody();
	}

	/**
	 * Calls if an item is a full helm item.
	 */
	public static boolean isFullHelm(int itemId) {
		if(itemId == -1)
			return false;
		return ItemDefinition.forId(itemId).isFullHelm();
	}

	/**
	 * Calls if an item is a full mask item.
	 */
	public static boolean isFullMask(int itemId) {
		if(itemId == -1)
			return false;
		return ItemDefinition.forId(itemId).isFullHelm();
	}

	/**
	 * Gets an item name from the itemlist.
	 */
	public static String getItemName(int id) {
		return ItemDefinition.forId(id).getName();
	}
}