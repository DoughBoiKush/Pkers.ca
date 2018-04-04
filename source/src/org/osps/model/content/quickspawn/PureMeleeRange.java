package org.osps.model.content.quickspawn;

import org.osps.model.players.Player;
import org.osps.model.players.packets.ClickingButtons;

public class PureMeleeRange {

	public static void equip(Player player) {		
		QuickSpawn.bankAll(player);
		ClickingButtons.resetCurrent(player);
		QuickSpawn.set90(player);
		
		player.getItems().wearItem(1169, 1, player.playerHat); //coif
		player.getItems().wearItem(11978, 1, player.playerAmulet); //glory
		player.getItems().wearItem(861, 1, Player.playerWeapon); //magic shortbow
		player.getItems().wearItem(892, 2500, player.playerArrows); //rune arrows
		player.getItems().wearItem(10499, 1, player.playerCape); //ava's accumulator
		player.getItems().wearItem(1129, 1, player.playerChest); //leather body
		player.getItems().wearItem(2497, 1, player.playerLegs); //black d'hide chaps
		player.getItems().wearItem(2550, 1, player.playerRing); //recoil
		player.getItems().wearItem(3105, 1, player.playerFeet); //climbing boots
		player.getItems().wearItem(7458, 1, player.playerHands); //mithril gloves
		player.getItems().addItem(6685, 2); // brew 0, 1
		player.getItems().addItem(10925, 2); // sanfew 2, 3
		player.getItems().addItem(2436, 1); // super a 4
		player.getItems().addItem(2440, 1); // super s 5
		player.getItems().addItem(2444, 1); // ranging potion 6 
		player.getItems().addItem(11936, 9); // dark crab 7, 8, 9, 10, 11, 12, 13, 14, 15
		player.getItems().addItem(868, 5000); // rune knife 16
		player.getItems().addItem(3842, 1); // unholy book 17
		player.getItems().addItem(11936, 2); // dark crab 18, 19
		player.getItems().addItem(7158, 1); // d2h 20
		player.getItems().addItem(5680, 1); // dds 21
		player.getItems().addItem(11936, 5); // dark crab 22, 23, 24, 25, 26
		player.getItems().addItem(2550, 1); //recoil 27
		
		player.setSidebarInterface(6, 1151); // modern	
		player.playerMagicBook = 0;
		player.getPA().resetAutocast();
		player.getItems().updateInventory();
		player.getPA().requestUpdates();
		player.getCombat().resetPrayers();
		player.vengOn = false;
		player.getItems().updateSpecialBar();
		player.combatLevel = player.calculateCombatLevel();
		player.getPA().removeAllWindows();
		player.getPA().requestUpdates();
	}
}
