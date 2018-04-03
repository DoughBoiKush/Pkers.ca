package org.osps.model.content;

import java.util.Arrays;

import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class BarrowsGamble {

	public static void gamble(Player player) {
		int barrowsItemCount = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			int itemId = player.playerItems[i] - 1;
			int itemAmount = player.playerItemsN[i];
			if (isBarrowsItem(itemId)) {
				barrowsItemCount += itemAmount;
			}
		}

		if (barrowsItemCount < 2) {
			player.sendMessage("You need at least 2 barrows items in your inventory in order to gamble.");
			return;
		}

		int deletedItemCount = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (deletedItemCount >= 2) {
				break;
			}

			int itemId = player.playerItems[i] - 1;
			if (!isBarrowsItem(itemId)) {
				continue;
			}

			int itemAmount = player.playerItemsN[i];
			if (itemAmount > 1) {
				itemAmount = 2 - deletedItemCount;
			}

			player.getItems().deleteItem2(itemId, itemAmount);
			deletedItemCount += itemAmount;
		}

		int randomItemId = getRandomBarrowsItem();
		player.getItems().addItem(randomItemId, 1);

		ItemDefinition def = ItemDefinition.forId(randomItemId);
		if (def == null) {
			return;
		}

		String name = def.getName();
		if (name == null || name.equalsIgnoreCase("null")) {
			return;
		}

		player.sendMessage("Congratulations! You've won " + (Misc.isVowel(name.charAt(0)) ? "an" : "a") + " " + name + ".");
	}

	private static int getRandomBarrowsItem() {
		int itemId = BARROWS_ITEMS[Misc.random(BARROWS_ITEMS.length - 1)];

		ItemDefinition def = ItemDefinition.forId(itemId);
		if (def == null) {
			return itemId;
		}

		if (def.isNoted()) {
			itemId--;
		}

		return itemId;
	}

	private static boolean isBarrowsItem(int itemId) {
		return Arrays.binarySearch(BARROWS_ITEMS, itemId) >= 0;
	}

	public static final int[] BARROWS_ITEMS = { 4708, 4709, 4710, 4711, 4712, 4713, 4714, 4715, 4716, 4717, 4718, 4719, 4720, 4721, 4722, 4723, 4724, 4725, 4726, 4727, 4728, 4729, 4730,
			4731, 4732, 4733, 4734, 4735, 4736, 4737, 4738, 4739, 4745, 4746, 4747, 4748, 4749, 4750, 4751, 4752, 4753, 4754, 4755, 4756, 4757, 4758, 4759, 4760 };

}