package org.osps.model.content.quickspawn;

import org.osps.model.players.Player;
import org.osps.model.players.packets.ClickingButtons;

public class MainRuneMace {
	
	public static void equip(Player player) {		
		QuickSpawn.bankAll(player);
		ClickingButtons.resetCurrent(player);
		QuickSpawn.set126(player);
		
		player.getItems().wearItem(10828, 1, player.playerHat);
		player.getItems().wearItem(11978, 1, player.playerAmulet);
		player.getItems().wearItem(4151, 1, player.playerWeapon);
		player.getItems().wearItem(8850, 1, player.playerShield);
		player.getItems().wearItem(1052, 1, player.playerCape);
		player.getItems().wearItem(1127, 1, player.playerChest);
		player.getItems().wearItem(1079, 1, player.playerLegs);
		player.getItems().wearItem(2550, 1, player.playerRing);
		player.getItems().wearItem(4131, 1, player.playerFeet);
		player.getItems().wearItem(7462, 1, player.playerHands);
		player.getItems().addItem(11936, 1); // dark crab
		player.getItems().addItem(2436, 1); // super a
		player.getItems().addItem(2440, 1); // super s
		player.getItems().addItem(2442, 1); // super d
		player.getItems().addItem(11936, 1); // dark crab
		player.getItems().addItem(6685, 1); // sara brew
		player.getItems().addItem(10925, 2); // sanfew
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(1434, 1); // dragon mace
		player.getItems().addItem(11838, 1);//ss
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(11936, 1);
		player.getItems().addItem(5680, 1); // dds
		player.getItems().addItem(560, 1000); // death
		player.getItems().addItem(9075, 1000); // astral
		player.getItems().addItem(557, 1000); // earth
		
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
