package org.osps.model.players.packets.commands.developer;

import java.util.Optional;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;

/**
 * Teleport a given player to the player who issued the command.
 * 
 * @author Emiel
 */
public class Teletome implements Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			c2.teleportToX = c.absX;
			c2.teleportToY = c.absY;
			c2.heightLevel = c.heightLevel;
			c.sendMessage(Misc.formatPlayerName(c2.playerName)+ " has been teleported to you.");
		} else {
			c.sendMessage(input + " is offline. You can only teleport online players.");
		}
	}
}