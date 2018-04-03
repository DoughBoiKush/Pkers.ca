package org.osps.model.players.packets.commands.developer;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Force the player to perform a given emote.
 * 
 * @author Emiel
 *
 */
public class Emote implements Command {

	@Override
	public void execute(Player c, String input) {
		c.animation(Integer.parseInt(input));
		c.getPA().requestUpdates();
	}
}
