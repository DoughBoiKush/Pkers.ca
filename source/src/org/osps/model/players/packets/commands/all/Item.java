package org.osps.model.players.packets.commands.all;

import org.osps.Server;
import org.osps.model.content.Unspawnable;
import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;

/**
 * Puts a given amount of the item in the player's inventory.
 * 
 * @author Emiel
 */
public class Item implements Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		try {
			int itemId = Integer.parseInt(args[0]);
			int amount = Misc.stringToInt(args[1]);
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.inWild() || c.inCamWild()) {
				return;
			}
			if (itemId >= 2677 && itemId <= 3000 || itemId >= 19400 && itemId <= 19700
					|| itemId >= 11818 && itemId <= 11822 || itemId == 20770 || itemId == 20771 || itemId == 20772

					|| itemId >= 1038 && itemId <= 1058 || itemId >= 15319 && itemId <= 15352
					|| itemId >= 11785 && itemId <= 11831 || itemId >= 12253 && itemId <= 12264
					|| itemId >= 12470 && itemId <= 12479 || itemId >= 12924 && itemId <= 12934
					|| itemId >= 19478 && itemId <= 19502 || itemId >= 21043 && itemId <= 21048
					|| itemId >= 12817 && itemId <= 12827 || itemId == 15001 || itemId == 13513 || itemId == 13652
					|| itemId == 21015 || itemId == 20784 || itemId == 20848) {
				if (!c.getRights().isDeveloper() && !c.getRights().isOwner()) {
					c.sendMessage("This item is not spawnable.");
					return;
				}
			}
			if (input.endsWith("Noted")) {
                if (ItemDefinition.forId(itemId).isNoteable()) {
                    itemId++;
                }
            }
			if(c.underAttackBy > 0 || c.underAttackBy2 > 0) {
				c.sendMessage("You can't do this whilst in combat!");
				return;
			}
			Unspawnable.spawnItem(c, itemId, amount);
		} catch (NumberFormatException nfe) {
			c.sendMessage("Improper use of the command; '::item itemid amount'.");
		}
	}
}
