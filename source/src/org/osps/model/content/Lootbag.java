package org.osps.model.content;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.osps.Server;
import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;

public class Lootbag {
	
	private Player c;
	
	public Lootbag(Player c) {
		this.c = c;
	}

	public void clearLootbagInterface() {
		for (int z = 0; z < 28; z++) {
			c.getPA().sendFrame34a(26706, -1, z, -1);
		}
	}
	
	public void updateLootbagInterface() {
		for (int z = 0; z < c.itemsInLootBag; z++) {
			c.getPA().sendFrame34a(26706, c.lootBag[z], z, c.amountLoot[z]);
			DecimalFormatSymbols separator = new DecimalFormatSymbols();
			separator.setGroupingSeparator(',');
			DecimalFormat formatter = new DecimalFormat("#,###,###", separator);
			c.getPA().sendFrame126("Value:"+formatter.format(ItemDefinition.forId(c.lootBag[z]).getSpecialPrice()), 26707);
		}
	}
	
	public void handleLootbagDeath() {
		if (c.getItems().playerHasItem(11941)) {
			if (c.itemsInLootBag == 0) {
				return;
			}
			for (int i = 0; i < c.itemsInLootBag; i++) {
				Server.itemHandler.createGroundItem(c, c.lootBag[i], c.absX, c.absY, c.heightLevel, c.amountLoot[i], c.index);
				c.sendMessage("You have lost " + c.amountLoot[i] + " " + c.getItems().getItemName(c.lootBag[i]) + " as you have died.");
				c.lootBag[i] = -1;
				c.amountLoot[i] = -1;
				clearLootbagInterface();
				updateLootbagInterface();
			}
			c.itemsInLootBag = 0;
			c.sendMessage("@red@You can teleport back to where you died to collect them!");
		}
	}
	
	public void depositLootbag() {
		if (!c.inEdgeBank()) {
			c.sendMessage("You must be in edgeville bank to do this.");
			return;
		}
		for (int i = 0; i < c.itemsInLootBag; i++) {
			c.getItems().addItemToBank(c.lootBag[i], c.amountLoot[i]);
			c.lootBag[i] = -1;
			c.amountLoot[i] = -1;
			clearLootbagInterface();
			updateLootbagInterface();
		}
		c.itemsInLootBag = 0;
	}
	
	public void addItemToLootbag(int itemId, int amount) {
		if (!c.inWild()) {
			c.sendMessage("You need to be in the wilderness to do this.");
			return;
		}
		if (c.logoutDelay.elapsed() < 10000) {
			c.sendMessage("You must wait until you're out of combat to do this.");
			return;
		}
		if (c.itemsInLootBag == 28) {
			c.sendMessage("Your looting bag is full, you can empty it at edgeville bank.");
			return;
		}
		for (int i = c.itemsInLootBag; i < c.itemsInLootBag + 1; i++) {
			c.getItems().deleteItem(itemId, amount);
			c.lootBag[i] = itemId;
			c.amountLoot[i] = amount;
		}
		c.itemsInLootBag += 1;
	}

}
