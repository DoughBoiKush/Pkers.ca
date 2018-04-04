package org.osps.model.content.quickspawn;

import org.osps.model.players.Boundary;
import org.osps.model.players.Player;

public class QuickSpawn {
	
	public static void bankAll(Player player) {
		for (int slot = 0; slot < player.playerItems.length; slot++) {
			if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {
				player.getItems().addToBankQuickSpawn(player.playerItems[slot] - 1, player.playerItemsN[slot], false);
			}
		}
		for (int slot = 0; slot < player.playerEquipment.length; slot++) {
			if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
				player.getItems().addEquipmentToBankQuickSpawn(player.playerEquipment[slot], slot,
						player.playerEquipmentN[slot], false);
				player.getItems().wearItem(-1, 0, slot);
			}
		}
		player.getItems().updateInventory();
		player.sendMessage("Your previous equipment and inventory have been sent to your bank.");
	}

	public static void set126(Player client) {
		if (client.playerLevel[0] == 99 && client.playerLevel[5] == 99 && client.playerLevel[1] == 99
				&& client.playerLevel[2] == 99 && client.playerLevel[3] == 99 && client.playerLevel[4] == 99
				&& client.playerLevel[6] == 99 && client.playerLevel[7] == 99 && client.playerLevel[8] == 99
				&& client.playerLevel[9] == 99 && client.playerLevel[10] == 99 && client.playerLevel[11] == 99
				&& client.playerLevel[12] == 99 && client.playerLevel[13] == 99 && client.playerLevel[14] == 99
				&& client.playerLevel[15] == 99 && client.playerLevel[16] == 99 && client.playerLevel[17] == 99
				&& client.playerLevel[18] == 99 && client.playerLevel[19] == 99 && client.playerLevel[20] == 99
				&& client.playerLevel[21] == 99 && client.playerLevel[22] == 99 && client.playerLevel[23] == 99) {
			return;
		}
		int[] skillIds = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23 };
		
		for (int i : skillIds) {
			client.playerLevel[i] = 99;
			client.getPA().addSkillXP10(14000000, i);
			client.getPA().refreshSkill(i);
		}
	}

	public static void set116(Player client) {
		if (client.playerLevel[0] == 75 && client.playerLevel[1] == 75 && client.playerLevel[5] == 70) {
			return;
		}
		int[] skillIds = { 0, 2, 3, 4, 6};
		for (int i : skillIds) {
			client.playerLevel[i] = 99;
			client.getPA().addSkillXP10(client.getPA().getXPForLevel(99), i);
			client.getPA().refreshSkill(i);
		}
		//client.playerLevel[0] = 75;
		//client.getPA().addSkillXP10(1096278, 0);
		client.playerLevel[5] = 70;
		client.getPA().addSkillXP10(client.getPA().getXPForLevel(70), 5);
		client.playerLevel[1] = 70;
		client.getPA().addSkillXP10(client.getPA().getXPForLevel(70), 5);
		//client.playerLevel[1] = 75;
		//client.getPA().addSkillXP10(1096278, 1);
		client.getPA().refreshSkill(1);
		client.getPA().refreshSkill(4);
		client.getPA().refreshSkill(5);
	}

	public static void set90(Player client) {
		if (client.playerLevel[0] == 75 && client.playerLevel[5] == 52) {
			return;
		}
		int[] skillIds = { 2, 3, 4, 6

		};
		for (int i : skillIds) {
			client.playerLevel[i] = 99;
			client.getPA().addSkillXP10(14000000, i);
			client.getPA().refreshSkill(i);
		}
		client.playerLevel[0] = 80;
		client.getPA().addSkillXP10(client.getPA().getXPForLevel(80), 0);
		client.playerLevel[5] = 52;
		client.getPA().addSkillXP10(client.getPA().getXPForLevel(52), 5);
		client.playerLevel[1] = 1;
		client.getPA().addSkillXP10(1, 1);
		client.getPA().refreshSkill(1);
		client.getPA().refreshSkill(4);
		client.getPA().refreshSkill(5);
	}

	public static void set110(Player client) {
		if (client.playerLevel[0] == 90 && client.playerLevel[1] == 70 && client.playerLevel[5] == 52) {
			return;
		}
		int[] skillIds = { 2, 3, 4, 6

		};
		for (int i : skillIds) {
			client.playerLevel[i] = 99;
			client.getPA().addSkillXP10(client.getPA().getXPForLevel(99), i);
			client.getPA().refreshSkill(i);
		}
		client.playerLevel[0] = 90;
		client.getPA().addSkillXP10(client.getPA().getXPForLevel(90), 0);
		client.playerLevel[5] = 52;
		client.getPA().addSkillXP10(client.getPA().getXPForLevel(52), 5);
		client.playerLevel[1] = 70;
		client.getPA().addSkillXP10(client.getPA().getXPForLevel(70), 1);
		client.getPA().refreshSkill(1);
		client.getPA().refreshSkill(4);
		client.getPA().refreshSkill(5);
	}

	//private static int[] packageItems;

	public static void handleActionButtons(Player client, int actionButtonId) {
		if (client.inWild()) {
			client.sendMessage("Please move to a safe location first.");
			return;
		}
		if (Boundary.isIn(client, Boundary.DUEL_ARENAS)) {
			client.sendMessage("You cannot use this in the duel arena!");
			return;
		}
		boolean canSpawn = true;
		switch (actionButtonId) {
		// case 109197:
		// packageItems = new int[] { 386, 392, 3145 };
		// if (client.getItems().freeSlots() < packageItems.length) {
		// client.sendMessage(
		// "You need atleast @blu@" + packageItems.length + "@bla@ available inventory
		// slots to do this.");
		// return;
		// }
		// for (int i = 0; i < packageItems.length; i++) {
		// client.getItems().addItem(packageItems[i], 1000);
		// }
		// break;
		//
		// case 109233:
		// packageItems = new int[] { 2441, 2437, 2443, 2445, 3041, 6686, 3025, 10926 };
		// if (client.getItems().freeSlots() < packageItems.length) {
		// client.sendMessage(
		// "You need atleast @blu@" + packageItems.length + "@bla@ available inventory
		// slots to do this.");
		// return;
		// }
		// for (int i = 0; i < packageItems.length; i++) {
		// client.getItems().addItem(packageItems[i], 1000);
		// }
		// break;
		//
		// case 109237:
		// packageItems = new int[] { 560, 557, 9075 };
		// if (client.getItems().freeSlots() < packageItems.length) {
		// client.sendMessage(
		// "You need atleast @blu@" + packageItems.length + "@bla@ available inventory
		// slots to do this.");
		// return;
		// }
		// for (int i = 0; i < packageItems.length; i++) {
		// client.getItems().addItem(packageItems[i], 5000);
		// }
		// break;
		// case 109204:
		// packageItems = new int[] { 555, 560, 565 };
		// if (client.getItems().freeSlots() < packageItems.length) {
		// client.sendMessage(
		// "You need atleast @blu@" + packageItems.length + "@bla@ available inventory
		// slots to do this.");
		// return;
		// }
		// for (int i = 0; i < packageItems.length; i++) {
		// client.getItems().addItem(packageItems[i], 5000);
		// }
		// break;

		// for (int slot = 0; slot < player.playerItems.length; slot++) {
		// if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {
		// player.getItems().addToBank(player.playerItems[slot] - 1,
		// player.playerItemsN[slot], false);
		// }
		// }
		// player.getItems().updateInventory();
		// player.getItems().resetBank();
		// player.getItems().resetTempItems();
		//
		// for (int slot = 0; slot < player.playerEquipment.length; slot++) {
		// if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
		// player.getItems().addEquipmentToBank(player.playerEquipment[slot], slot,
		// player.playerEquipmentN[slot], false);
		// player.getItems().wearItem(-1, 0, slot);
		// }
		// }
		// player.getItems().updateInventory();
		// player.getItems().resetBank();
		// player.getItems().resetTempItems();
		// player.getItems().wearItem(10828, 1, player.playerHat);
		// player.getItems().wearItem(6585, 1, player.playerAmulet);
		// player.getItems().wearItem(4151, 1, Player.playerWeapon);
		// player.getItems().wearItem(8850, 1, player.playerShield);
		// player.getItems().wearItem(1052, 1, player.playerCape);
		// player.getItems().wearItem(1127, 1, player.playerChest);
		// player.getItems().wearItem(1079, 1, player.playerLegs);
		// player.getItems().wearItem(6737, 1, player.playerRing);
		// player.getItems().wearItem(11840, 1, player.playerFeet);
		// player.getItems().wearItem(7462, 1, player.playerHands);
		// player.getItems().addItem(11936, 1); // dark crab
		// player.getItems().addItem(2436, 1); // super a
		// player.getItems().addItem(2440, 1); // super s
		// player.getItems().addItem(2442, 1); // super d
		// player.getItems().addItem(11936, 1); // dark crab
		// player.getItems().addItem(6685, 1); // sara brew
		// player.getItems().addItem(10925, 2); // sanfew
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11808, 1); // godsword
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(5680, 1); // dds
		// player.getItems().addItem(566, 1000); // soul
		// player.getItems().addItem(9075, 1000); // astral
		// player.getItems().addItem(557, 1000); // earth
		// player.setSidebarInterface(6, 29999); // lunar
		// player.playerMagicBook = 2;
		// player.getPA().resetAutocast();
		// player.getItems().updateInventory();
		// player.getPA().requestUpdates();
		// handleMainLevels(player);
		// player.getCombat().resetPrayers();
		// player.vengOn = false;
		// player.getItems().updateSpecialBar();
		// player.combatLevel = player.calculateCombatLevel();
		// player.getPA().removeAllWindows();
		// player.sendMessage("Your previous equipment and inventory have been sent to
		// your bank.");

		// for (int slot = 0; slot < player.playerItems.length; slot++) {
		// if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {
		// player.getItems().addToBank(player.playerItems[slot] - 1,
		// player.playerItemsN[slot], false);
		// }
		// }
		// player.getItems().updateInventory();
		// player.getItems().resetBank();
		// player.getItems().resetTempItems();
		//
		// for (int slot = 0; slot < player.playerEquipment.length; slot++) {
		// if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
		// player.getItems().addEquipmentToBank(player.playerEquipment[slot], slot,
		// player.playerEquipmentN[slot], false);
		// player.getItems().wearItem(-1, 0, slot);
		// }
		// }
		// player.getItems().updateInventory();
		// player.getItems().resetBank();
		// player.getItems().resetTempItems();
		// player.getItems().wearItem(10828, 1, player.playerHat);
		// player.getItems().wearItem(6585, 1, player.playerAmulet);
		// player.getItems().wearItem(4151, 1, Player.playerWeapon);
		// player.getItems().wearItem(8850, 1, player.playerShield);
		// player.getItems().wearItem(1052, 1, player.playerCape);
		// player.getItems().wearItem(1127, 1, player.playerChest);
		// player.getItems().wearItem(1079, 1, player.playerLegs);
		// player.getItems().wearItem(6737, 1, player.playerRing);
		// player.getItems().wearItem(11840, 1, player.playerFeet);
		// player.getItems().wearItem(7462, 1, player.playerHands);
		// player.getItems().addItem(11936, 1); // dark crab
		// player.getItems().addItem(2436, 1); // super a
		// player.getItems().addItem(2440, 1); // super s
		// player.getItems().addItem(2442, 1); // super d
		// player.getItems().addItem(11936, 1); // dark crab
		// player.getItems().addItem(6685, 1); // sara brew
		// player.getItems().addItem(10925, 2); // sanfew
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(1434, 1); // mace
		// player.getItems().addItem(11838, 1); // godsword
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(5680, 1); // dds
		// player.getItems().addItem(566, 1000); // soul
		// player.getItems().addItem(9075, 1000); // astral
		// player.getItems().addItem(557, 1000); // earth
		// player.setSidebarInterface(6, 29999); // lunar
		// player.playerMagicBook = 2;
		// player.getPA().resetAutocast();
		// player.getItems().updateInventory();
		// player.getPA().requestUpdates();
		// handleMainLevels(player);
		// player.getCombat().resetPrayers();
		// player.vengOn = false;
		// player.getItems().updateSpecialBar();
		// player.combatLevel = player.calculateCombatLevel();
		// player.getPA().removeAllWindows();
		// player.sendMessage("Your previous equipment and inventory have been sent to
		// your bank.");

		// for (int slot = 0; slot < player.playerItems.length; slot++) {
		// if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {
		// player.getItems().addToBank(player.playerItems[slot] - 1,
		// player.playerItemsN[slot], false);
		// }
		// }
		// player.getItems().updateInventory();
		// player.getItems().resetBank();
		// player.getItems().resetTempItems();
		//
		// for (int slot = 0; slot < player.playerEquipment.length; slot++) {
		// if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
		// player.getItems().addEquipmentToBank(player.playerEquipment[slot], slot,
		// player.playerEquipmentN[slot], false);
		// player.getItems().wearItem(-1, 0, slot);
		// }
		// }
		// player.getItems().updateInventory();
		// player.getItems().resetBank();
		// player.getItems().resetTempItems();
		// player.getItems().wearItem(10828, 1, player.playerHat);
		// player.getItems().wearItem(6585, 1, player.playerAmulet);
		// player.getItems().wearItem(4151, 1, Player.playerWeapon);
		// player.getItems().wearItem(8850, 1, player.playerShield);
		// player.getItems().wearItem(1052, 1, player.playerCape);
		// player.getItems().wearItem(10564, 1, player.playerChest);
		// player.getItems().wearItem(6809, 1, player.playerLegs);
		// player.getItems().wearItem(6737, 1, player.playerRing);
		// player.getItems().wearItem(11840, 1, player.playerFeet);
		// player.getItems().wearItem(7462, 1, player.playerHands);
		// player.getItems().addItem(11936, 1); // dark crab
		// player.getItems().addItem(2436, 1); // super a
		// player.getItems().addItem(2440, 1); // super s
		// player.getItems().addItem(2442, 1); // super d
		// player.getItems().addItem(11936, 1); // dark crab
		// player.getItems().addItem(6685, 1); // sara brew
		// player.getItems().addItem(10925, 2); // sanfew
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(1434, 1); // mace
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(11936, 1);
		// player.getItems().addItem(5680, 1); // dds
		// player.getItems().addItem(566, 1000); // soul
		// player.getItems().addItem(9075, 1000); // astral
		// player.getItems().addItem(557, 1000); // earth
		// player.setSidebarInterface(6, 29999); // lunar
		// player.playerMagicBook = 2;
		// player.getPA().resetAutocast();
		// player.getItems().updateInventory();
		// player.getPA().requestUpdates();
		// handleMainLevels(player);
		// player.getCombat().resetPrayers();
		// player.vengOn = false;
		// player.getItems().updateSpecialBar();
		// player.combatLevel = player.calculateCombatLevel();
		// player.getPA().removeAllWindows();
		// player.sendMessage("Your previous equipment and inventory have been sent to
		// your bank.");

//		case 100100: // Main 126 - handleMainLevels(client);
//			client.getDH().sendOption4("Main Rune (ZGS)", "Main Rune (Mace/SS)", "Main Granite (Mace)", "Cancel");
//			break;
//
//		case 100103: // Pure 88 - handlePureLevels(client);
//			client.getDH().sendOption4("Pure Melee", "Pure Melee & Range", "Pure Tribrid", "Cancel");
//			break;
//
//		case 100106: // Hybrid Set - handleMainLevels(client);
//			client.getDH().sendOption4("Hybrid (4 way)", "Hybrid (5 way)", "Hybrid (6 way)", "Cancel");
//			break;
//
//		case 100109: // Zerker Set - handleZerkerLevels(client);
//			client.getDH().sendOption2("Zerker", "Cancel");
//			break;
//
//		case 100112: // Tribrid SET - handleZerkerLevels(client);
//			client.getDH().sendOption2("Tribrid", "Cancel");
//			break;
//
//		case 100115: // F2P SET - handleMainLevels(client);
//			client.getDH().sendOption4("Melee", "Range", "Magic", "Cancel");
//			break;
//
//		case 100118: // 2006 Set - handleMainLevels(client);
//			client.getDH().sendOption4("Melee", "Range", "Magic", "Cancel");
//			break;

		//

		case 100121: // Bandos Set - handleMainLevels(client);
			client.getPA().closeAllWindows();
			client.sendMessage("Currently disabled.");
			break;

		//

		case 100124: // Dharok Set - handleMainLevels(client);
			client.getPA().closeAllWindows();
			client.sendMessage("Currently disabled.");
			break;

		case 100127: // Veracs Set - handleMainLevels(client);
			client.getPA().closeAllWindows();
			client.sendMessage("Currently disabled.");
			break;

		case 100130: // Torag Set - handleMainLevels(client);
			client.getPA().closeAllWindows();
			client.sendMessage("Currently disabled.");
			break;

		case 100133: // Guthan Set - handleMainLevels(client);
			client.getPA().closeAllWindows();
			client.sendMessage("Currently disabled.");
			break;

		case 100136: // Karil Set - handleMainLevels(client);
			client.getPA().closeAllWindows();
			client.sendMessage("Currently disabled.");
			break;

		case 100139: // Ahrim Set - handleMainLevels(client);
			client.getPA().closeAllWindows();
			client.sendMessage("Currently disabled.");
			break;

		//

		case 100142: // Ghostly Set - handleMainLevels(client);
			client.getPA().closeAllWindows();
			client.sendMessage("Currently disabled.");
			break;

		//

		case 100145: // Food
			client.getPA().closeAllWindows();
			client.getItems().addItem(15354, 28);
			break;

		case 100148: // Potions
			client.getPA().closeAllWindows();
			client.getItems().addItem(2436, 1);
			client.getItems().addItem(2440, 1);
			client.getItems().addItem(2442, 1);
			client.getItems().addItem(6685, 1);
			client.getItems().addItem(10925, 2);
			break;

		case 100151: // Veng
			client.getPA().closeAllWindows();
			client.getItems().addItem(557, 1000);
			client.getItems().addItem(560, 1000);
			client.getItems().addItem(9075, 1000);
			break;

		case 100154: // Barrage
			client.getPA().closeAllWindows();
			client.getItems().addItem(555, 1000);
			client.getItems().addItem(560, 1000);
			client.getItems().addItem(565, 1000);
			break;

		case 100157: // Godswords
			client.sendMessage("Currently disabled.");
			break;

		case 100160: // Vote
			client.getPA().closeAllWindows();
			client.sendMessage("Opening: @blu@https://Pkers.Ca/Vote/");
			break;

		case 100163: // Forum
			client.getPA().closeAllWindows();
			client.sendMessage("Opening: @blu@https://Pkers.Ca/Forum/");
			break;

		case 100166: // Donate
			client.getPA().closeAllWindows();
			client.sendMessage("Opening: @blu@https://Pkers.Ca/Donate/");
			break;
		}
	}
}
