package org.osps.model.content.decanting;

import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;

public class Decanting {

	public static void decantInventory(Player player) {
		for (int i = 0; i < player.playerItems.length; i++) {
			int potionId = player.playerItems[i] - 1;

			if (potionId < 0) {
				continue;
			}
			
			ItemDefinition itemDef = ItemDefinition.forId(potionId);
			if (itemDef == null) {
				continue;
			}
			if (itemDef.isNoted()) {
				potionId -= 1;
			}

			DecantingDefinitions decantingDef = DecantingDefinitions.getByItemId(potionId);
			if (decantingDef == null || decantingDef.getDoseByPotion(potionId) == decantingDef.getMaximumDose()) {
				continue;
			}

			decantPotion(player, decantingDef, itemDef.isNoted());
		}
	}

	private static void decantPotion(Player player, DecantingDefinitions def, boolean noted) {
		int totalPotions = 0, totalDoses = 0;

		for (int dose = 1; dose <= def.getMaximumDose(); dose++) {
			int potionId = def.getPotionByDose(dose) + (noted ? 1 : 0);
			int potionsAmount = player.getItems().getItemCount(potionId);
			if (potionsAmount <= 0) {
				continue;
			}
			totalPotions += potionsAmount;
			totalDoses += potionsAmount * dose;
			player.getItems().deleteItem2(potionId, potionsAmount);
		}

		if (totalDoses < def.getMaximumDose()) {
			player.getItems().addItem(def.getPotionByDose(totalDoses) + (noted ? 1 : 0), 1);
			if (totalPotions > 1) {
				player.getItems().addItem(EMPTY_VIAL_ID + (noted ? 1 : 0), totalPotions - 1);
			}
			return;
		}

		int maximumDosePotions = totalDoses / def.getMaximumDose();
		int vialsToAdd = totalPotions - maximumDosePotions;

		player.getItems().addItem(def.getPotionByDose(def.getMaximumDose()) + (noted ? 1 : 0), maximumDosePotions);

		int remainderDose = totalDoses % def.getMaximumDose();

		if (remainderDose > 0) {
			vialsToAdd -= 1;
			player.getItems().addItem(def.getPotionByDose(remainderDose) + (noted ? 1 : 0), 1);
		}

		if (vialsToAdd > 0) {
			player.getItems().addItem(EMPTY_VIAL_ID + (noted ? 1 : 0), vialsToAdd);
		}
	}

	private static final int EMPTY_VIAL_ID = 229;

}