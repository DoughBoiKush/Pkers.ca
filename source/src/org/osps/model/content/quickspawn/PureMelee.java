package org.osps.model.content.quickspawn;

import org.osps.model.players.Player;
import org.osps.model.players.packets.ClickingButtons;

public class PureMelee {
	
	public static void equip(Player player) {		
		QuickSpawn.bankAll(player);
		ClickingButtons.resetCurrent(player);
		QuickSpawn.set90(player);
		
		player.getItems().wearItem(1153, 1, player.playerHat); //iron full helm
		player.getItems().wearItem(11978, 1, player.playerAmulet); //glory
		player.getItems().wearItem(4587, 1, player.playerWeapon); //dragon scimitar
		player.getItems().wearItem(3842, 1, player.playerShield); //unholy book
		player.getItems().wearItem(1052, 1, player.playerCape); //legends cape
		player.getItems().wearItem(1115, 1, player.playerChest); //iron platebody
		player.getItems().wearItem(2497, 1, player.playerLegs); //black d'hide chaps
		player.getItems().wearItem(2550, 1, player.playerRing); //recoil
		player.getItems().wearItem(3105, 1, player.playerFeet); //climbing boots
		player.getItems().wearItem(7458, 1, player.playerHands); //mithril gloves
		player.getItems().addItem(6685, 2); // brew 0, 1
		player.getItems().addItem(11936, 2); // dark crab 2, 3
		player.getItems().addItem(10925, 2); // sanfew 4, 5
		player.getItems().addItem(11936, 2); // dark crab 6, 7
		player.getItems().addItem(2436, 1); // super a 8
		player.getItems().addItem(2440, 1); // super s 9
		player.getItems().addItem(11936, 6); // dark crab 10, 11, 12, 13, 14, 15
		player.getItems().addItem(7158, 1); // d2h 16
		player.getItems().addItem(11936, 3); // dark crab 17, 18, 19
		player.getItems().addItem(5680, 1); // dds 20
		player.getItems().addItem(11936, 6); // dark crab 21, 22, 23, 24, 25, 26
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
