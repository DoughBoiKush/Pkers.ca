package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Show a red skull above the player's head.
 * 
 * @author Emiel
 *
 */
public class Invis implements Command {

	@Override
	public void execute(Player c, String input) {
		if (c.isInvisible()) {
			c.setInvisible(false);
			c.sendMessage("You are no longer invisible.");
		} else {
			c.setInvisible(true);
			c.sendMessage("You are now invisible.");
		}
		c.getPA().requestUpdates();
	}
}
