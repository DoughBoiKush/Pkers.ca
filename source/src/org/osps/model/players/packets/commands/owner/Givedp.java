package org.osps.model.players.packets.commands.owner;

import java.util.Optional;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;

/**
 * Give a certain amount of an item to a player.
 * 
 * @author Emiel
 */
public class Givedp implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String args[] = input.split("-");
			String playerName = args[0];
			int amount = Integer.parseInt(args[1]);

			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(playerName);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				c2.donatorPoints += amount;
				c.sendMessage("You have just given @blu@" + amount + "@bla@ donator points to " + c2.playerName + ".");
				c2.disconnected = true;
			} else {
				c.sendMessage(playerName + " is not online.");
			}
		}catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::giveitem-player-amount");
			e.printStackTrace();
		}
	}
}
