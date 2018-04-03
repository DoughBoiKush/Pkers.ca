package org.osps.model.content;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class DiceNpc {

	public static void diceNpc(final Player player, final int item) {
		if (player == null || player.disconnected || player.teleporting || player.isDead) {
			return;
		}
		int amount = player.getItems().getItemAmount(item);
		if (player.getItems().freeSlots() < 2) {
			player.sendMessage("You need atleast 2 free slots to gamble.");
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
				int roll = Misc.random(100);
				if(Math.random() > 0.5) {
					roll = Math.min(roll, Misc.random(100));
				}
				if (!player.getItems().playerHasItem(item)) {
					player.getDH().sendStatement("Error! Item not found...");
					return;
				}
				if (roll > 55) {
					if (ItemDefinition.forId(item).isStackable()) {
						player.getItems().addItem(item, amount);
					} else if (!ItemDefinition.forId(item).isStackable()) {
						player.getItems().addItem(item, 1);
					}
					player.getDH().sendStatement("Congratulations you have rolled: @blu@" + roll + "!");
				} else if (roll < 55) {
					if (ItemDefinition.forId(item).isStackable()) {
						player.getItems().deleteItem(item, amount);
					} else if (!ItemDefinition.forId(item).isStackable()) {
						player.getItems().deleteItem(item, 1);
					}
					player.getDH().sendStatement("Better luck next time!");
				}
			}
		}, 1);
	}
}
