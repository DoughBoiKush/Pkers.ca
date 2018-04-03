package org.osps.model.players.packets.commands.moderator;

import java.util.Optional;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Teleport to a given player.
 * 
 * @author Emiel
 */
public class Teleto implements Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			if (c2.hungerGames) {
				return;
			}
			c.getPA().movePlayer(c2.getX(), c2.getY(), c2.heightLevel);			
		} else {
			c.sendMessage(input + " is not line. You can only teleport to online players.");
		}
	}
}
