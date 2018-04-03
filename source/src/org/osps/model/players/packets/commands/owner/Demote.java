package org.osps.model.players.packets.commands.owner;

import java.util.Optional;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.Rights;
import org.osps.model.players.packets.commands.Command;

/**
 * Remove all ranks from a given player.
 * 
 * @author Emiel
 *
 */
public class Demote implements Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			c2.setRights(Rights.PLAYER);
			c.sendMessage("You've demoted the user:  " + c2.playerName + " IP: " + c2.connectedFrom);
			c2.disconnected = true;
		} else {
			c.sendMessage(input + " is not online. You can only demote online players.");
		}
	}
}
