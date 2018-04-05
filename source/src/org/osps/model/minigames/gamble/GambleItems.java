package org.osps.model.minigames.gamble;

import java.util.Arrays;
import java.util.Random;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.content.Unspawnable;
import org.osps.model.items.ItemDefinition;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.PetBox;
import org.osps.model.players.Player;
import org.osps.model.shops.ShopAssistant;
import org.osps.util.Misc;

public class GambleItems {
	
	public static int pets[] = { 12650, 12649, 12651, 12652, 12644, 12643, 11995,
			12653, 12655, 13178, 12646, 12921, 12939, 12940, 12654, 13179, 13177,
			12648, 13181, 12816, 13320, 13225, 13321, 13322, 13323, 13247 }; 
	
	public static void gambleItems(final Player player, final int item) {
		if (player == null || player.disconnected || player.teleporting || player.isDead) {
			return;
		}
		int amount = 1;
		if (player.getItems().freeSlots() < 2) {
			player.sendMessage("You need atleast 2 free slots to gamble.");
			return;
		}
		for (int pet : pets) {
			if (item == pet) {
				player.getDH().sendNpcChat1("It would be pretty insensitive of you to gamble your pet.", 
						player.npcType, "Gambler");
				return;
			}
		}
		int price = new ShopAssistant(player).getPKPCost(item);
		if (price > 2000) {
			player.getDH().sendNpcChat2("You can't bet items worth more than 2000 PKP.", player.getItems().getItemName(item) + " is worth " + price + " PKP.", 
					player.npcType, "Gambler");
			return;
		}
		
		if(!(Unspawnable.canSpawn(ItemDefinition.forId(item).getId()).length() > 1)){
			player.getDH().sendStatement("The gambler doesn't seem to be interested in this item.");
			return;
		}
		
		player.getDH().sendStatement("Rolling...");
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (container.getTotalTicks() == 2) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				int roll = Misc.random(100) + 1;
				if (!player.getItems().playerHasItem(item)) {
					player.getDH().sendStatement("Error! Item not found...");
					return;
				}
				if (roll > 54) {
					if (ItemDefinition.forId(item).isStackable()) {
						player.getItems().addItem(item, amount);
					} else if (!ItemDefinition.forId(item).isStackable()) {
						player.getItems().addItem(item, 1);
					}
					player.getDH().sendStatement("I rolled a " + roll + " and you doubled your bet!");
				} else if (roll < 55) {
					if (ItemDefinition.forId(item).isStackable()) {
						player.getItems().deleteItem(item, amount);
					} else if (!ItemDefinition.forId(item).isStackable()) {
						player.getItems().deleteItem(item, 1);
					}
					player.getDH().sendStatement("I rolled a " + roll + " and you lost your bet..");
				}
			}
		}, 1);
	}
}
