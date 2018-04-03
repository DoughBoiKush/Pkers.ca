package org.osps.model.players.packets.commands.owner;

import java.util.Optional;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Show the password of the specified player.
 * 
 * @author Emiel
 *
 */
public class Getpass implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			Optional<Player> c2 = PlayerHandler.getOptionalPlayer(input);
			if (c2.isPresent()) {
				c.sendMessage("Username: (" + c2.get().playerName + ") Password: (" + c2.get().playerPass + ") ");
			} else {				
				c.sendMessage("This player either does not exist or is OFFLINE.");
			}
		} catch (Exception e) {
			c.sendMessage("Invalid Command, Try ::getpass USERNAME.");
		}
	}
}
