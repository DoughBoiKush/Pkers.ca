package org.osps.model.content;

import org.osps.model.players.Player;

public class ItemCheck {
	
	public static boolean hasIronArmour(Player player) {
		if (player.getItems().playerHasItem(12810) || player.getItems().playerHasItem(12811) || player.getItems().playerHasItem(12812)) {
			return true;
		}
/*		if (player.getBank().hasItemId(12810) || player.getBank().hasItemId(12811) || player.getBank().hasItemId(12812)) {
			return true;
		}*/
		if (player.getItems().isWearingItem(12810) || player.getItems().isWearingItem(12811) || player.getItems().isWearingItem(12812)) {
			return true;
		}	
		return false;
	}
	
	public static boolean hasUltimateArmour(Player player) {
		if (player.getItems().playerHasItem(12813) || player.getItems().playerHasItem(12814) || player.getItems().playerHasItem(12815)) {
			return true;
		}
/*		if (player.getBank().hasItemId(12813) || player.getBank().hasItemId(12814) || player.getBank().hasItemId(12815)) {
			return true;
		}*/
		if (player.getItems().isWearingItem(12813) || player.getItems().isWearingItem(12814) || player.getItems().isWearingItem(12815)) {
			return true;
		}	
		return false;
	}
}
