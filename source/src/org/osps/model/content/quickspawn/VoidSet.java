package org.osps.model.content.quickspawn;

import org.osps.model.players.Player;
import org.osps.model.players.packets.ClickingButtons;

public class VoidSet {

	public static void equip(Player player) {		
		QuickSpawn.bankAll(player);
		ClickingButtons.resetCurrent(player);
		QuickSpawn.set126(player);
		
		player.getItems().wearItem(11664, 1, player.playerHat); //void helm
		player.getItems().wearItem(11978, 1, player.playerAmulet); //glory
		player.getItems().wearItem(868, 2500, player.playerWeapon); //knives
		player.getItems().wearItem(3842, 1, player.playerShield); //unholy
		player.getItems().wearItem(10499, 1, player.playerCape); //avas
		player.getItems().wearItem(8839, 1, player.playerChest); //void top
		player.getItems().wearItem(8840, 1, player.playerLegs); //void legs
		player.getItems().wearItem(2550, 1, player.playerRing); //recoil
		player.getItems().wearItem(3105, 1, player.playerFeet); //climbers
		player.getItems().wearItem(9244, 2500, player.playerArrows); //dstone bolts
		player.getItems().wearItem(8842, 1, player.playerHands); //void gloves
		player.getItems().addItem(9185, 1); // 0
		player.getItems().addItem(11936, 1);//2
		player.getItems().addItem(10925, 2); //1
		player.getItems().addItem(11936, 2);
		player.getItems().addItem(2444, 1);//5
		player.getItems().addItem(6685, 1);//6
		player.getItems().addItem(11936, 16);
		player.getItems().addItem(560, 1000); // death
		player.getItems().addItem(9075, 1000); // astral
		player.getItems().addItem(557, 1000); // earth
		player.getItems().addItem(2550, 1);//27
		
		player.setSidebarInterface(6, 29999); // lunar	
		player.playerMagicBook = 2;
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
