package org.osps.model.content.quickspawn;

import org.osps.model.players.Player;
import org.osps.model.players.packets.ClickingButtons;

public class Hybrid5 {

	public static void equip(Player player) {		
		QuickSpawn.bankAll(player);
		ClickingButtons.resetCurrent(player);
		QuickSpawn.set126(player);
		
		player.getItems().wearItem(10828, 1, player.playerHat); //nezzy
		player.getItems().wearItem(11978, 1, player.playerAmulet); //glory
		player.getItems().wearItem(4675, 1, Player.playerWeapon); //ancient staff	
		player.getItems().wearItem(3842, 1, player.playerShield); //unholy book
		player.getItems().wearItem(2412, 1, player.playerCape); //saradomin cape
		player.getItems().wearItem(4091, 1, player.playerChest); //mystic body
		player.getItems().wearItem(4093, 1, player.playerLegs); //mystic robe
		player.getItems().wearItem(2550, 1, player.playerRing); //recoil
		player.getItems().wearItem(2579, 1, player.playerFeet); //wizard boots
		player.getItems().wearItem(7462, 1, player.playerHands); //barrows gloves
		player.getItems().addItem(1127, 1); // 0
		player.getItems().addItem(4151, 1); //1
		player.getItems().addItem(4131, 1); //1
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(1079, 1);//4
		player.getItems().addItem(8850, 1);//5
		player.getItems().addItem(11936, 2);
		player.getItems().addItem(2503, 1);//8
		player.getItems().addItem(5680, 1);//9
		player.getItems().addItem(11936, 6);
		player.getItems().addItem(2436, 1); // super a
		player.getItems().addItem(6685, 2); // sara brew
		player.getItems().addItem(11936, 1); // dark crab
		player.getItems().addItem(2440, 1); // super s
		player.getItems().addItem(10925, 2); // sanfew
		player.getItems().addItem(11936, 1); // dark crab
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
