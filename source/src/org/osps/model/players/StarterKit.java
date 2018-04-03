package org.osps.model.players;

import java.util.function.Consumer;

import org.osps.Config;
import org.osps.model.items.GameItem;

public enum StarterKit {

	NORMAL("Ignore for now.",
			new GameItem[] { new GameItem(1153, 1, Config.HAT), new GameItem(1704, 1, Config.AMULET),
					new GameItem(1323, 1, Config.WEAPON), new GameItem(1191, 1, Config.SHIELD),
					new GameItem(1115, 1, Config.CHEST), new GameItem(1067, 1, Config.LEGS),
					new GameItem(4121, 1, Config.FEET), new GameItem(7458, 1, Config.HANDS) },
			new GameItem[] { new GameItem(995, 100), new GameItem(995, 250_000), new GameItem(1333),
					new GameItem(1381), new GameItem(579), new GameItem(577), new GameItem(1011),
					new GameItem(554, 100), new GameItem(555, 100), new GameItem(557, 100), new GameItem(558, 100),
					new GameItem(562, 25), new GameItem(1169), new GameItem(1129), new GameItem(1095),
					new GameItem(1063), new GameItem(841), new GameItem(884, 50), new GameItem(1725),
					new GameItem(4155), new GameItem(1351), new GameItem(1265), new GameItem(303),
					new GameItem(12938, 3), new GameItem(7947, 15) },
			null);

	private final String description;
	private final GameItem[] equipment;
	private final GameItem[] inventory;
	private final int[] skills;
	private Consumer<Player> consumer;

	private StarterKit(String description, GameItem[] equipment, GameItem[] inventory, int[] skills) {
		this(description, equipment, inventory, skills, null);
	}

	private StarterKit(String description, GameItem[] equipment, GameItem[] inventory, int[] skills,
			Consumer<Player> consumer) {
		this.description = description;
		this.equipment = equipment;
		this.inventory = inventory;
		this.skills = skills;
		this.consumer = consumer;
	}

	public void giveStarterKit(Player player) {
		for (GameItem equipItem : equipment) {
			player.getItems().wearItem(equipItem.getId(), equipItem.getAmount(), equipItem.getSlot());
		}

		for (GameItem inventoryItem : inventory) {
			player.getItems().addItem(inventoryItem.getId(), inventoryItem.getAmount());
		}

		/*
		 * for (int i = 0; i < skills.length; i++) { player.playerLevel[i] =
		 * skills[i]; player.playerXP[i] =
		 * player.getPA().getXPForLevel(skills[i]); }
		 */

		player.getItems().updateInventory();
		player.getPA().requestUpdates();

		if (consumer != null) {
			consumer.accept(player);
		}
	}

	public String getDescription() {
		return description;
	}

}