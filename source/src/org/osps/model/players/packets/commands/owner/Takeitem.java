package org.osps.model.players.packets.commands.owner;

import java.util.Optional;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;

/**
 * Take a certain amount of items from a player.
 * 
 * @author Emiel
 */
public class Takeitem implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String args[] = input.split("-");
			if (args.length != 3) {
				throw new IllegalArgumentException();
			}
			String playerName = args[0];
			int itemID = Integer.parseInt(args[1]);
			int amount = Misc.stringToInt(args[2]);

			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(playerName);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				if (c2.getItems().playerHasItem(itemID, 1)) {
					c2.getItems().deleteItem(itemID, amount);
					c.sendMessage("You have just taken " + amount + " " + c.getItems().getItemName(itemID) + " from " + c2.playerName + ".");
					c2.sendMessage(c.playerName + " has taken " + amount + " " + c.getItems().getItemName(itemID) + " from you.");
				} else {
					c.sendMessage("This player doesn't have this item!");
				}

			} else {
				c.sendMessage(playerName + " is not online.");
			}
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::takeitem-player-itemid-amount");
		}
	}
}
