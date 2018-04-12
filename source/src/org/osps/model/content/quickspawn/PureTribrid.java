package org.osps.model.content.quickspawn;

import org.osps.model.players.Player;
import org.osps.model.players.packets.ClickingButtons;

public class PureTribrid {
	
	public static void equip(Player player) {		
		QuickSpawn.bankAll(player);
		ClickingButtons.resetCurrent(player);
		QuickSpawn.set90(player);
		
		player.getItems().wearItem(6109, 1, player.playerHat); //ghostly hood
		player.getItems().wearItem(11978, 1, player.playerAmulet); //glory
		player.getItems().wearItem(4675, 1, player.playerWeapon); //ancient staff	
		player.getItems().wearItem(3842, 1, player.playerShield); //unholy book
		player.getItems().wearItem(9244, 2500, player.playerArrows); //dragon bolts (e)
		player.getItems().wearItem(2412, 1, player.playerCape); //saradomin cape
		player.getItems().wearItem(6107, 1, player.playerChest); //ghostly body
		player.getItems().wearItem(6108, 1, player.playerLegs); //ghostly robe
		player.getItems().wearItem(2550, 1, player.playerRing); //recoil
		player.getItems().wearItem(3105, 1, player.playerFeet); //climbing boots
		player.getItems().wearItem(7458, 1, player.playerHands); //mithril gloves
		player.getItems().addItem(10499, 1); // 0 ava
		player.getItems().addItem(2497, 1); //1 chaps
		player.getItems().addItem(2440, 1);//2 super strength
		player.getItems().addItem(2444, 1);//3 ranging pot
		player.getItems().addItem(9185, 1);//4
		player.getItems().addItem(5680, 1);//5
		player.getItems().addItem(10925, 2);
		player.getItems().addItem(4587, 1);//8
		player.getItems().addItem(11936, 1);//9
		player.getItems().addItem(6685, 2);
		player.getItems().addItem(11936, 12);
		player.getItems().addItem(560, 5000);//24
		player.getItems().addItem(555, 5000);//25
		player.getItems().addItem(565, 5000);//26
		player.getItems().addItem(2550, 1);//27
		
		
		player.setSidebarInterface(6, 12855); // ancient	
		player.playerMagicBook = 1;
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
