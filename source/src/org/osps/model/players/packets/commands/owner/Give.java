package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Gives the player the item specified in the command arguments.
 * @author Chris
 *
 */
public class Give implements Command {
	
	@Override
	public void execute(Player c, String input) {
		try {
			String[] args = input.split(" ");
			String item = args[0].replaceAll("_", " ");
			int amount = args.length == 2 ? Integer.parseInt(args[1]) : 1;
			int itemId = (item.equalsIgnoreCase("coins") ? 995 : c.getItems().getItemId(item));
			if ((itemId <= 20500) && (itemId >= 0)) {
				c.getItems().addItem(itemId, amount);
			}
		} catch (Exception e) {
			c.sendMessage("Invalid item name or syntax! Try this - ::give itemname amount");
		}
	}

}
