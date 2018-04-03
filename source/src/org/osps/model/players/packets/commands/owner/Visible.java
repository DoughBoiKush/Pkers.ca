package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Show a red skull above the player's head.
 * 
 * @author Emiel
 *
 */
public class Visible implements Command {

	@Override
	public void execute(Player c, String input) {
		c.isNpc = false;
		c.updateRequired = true;
		c.appearanceUpdateRequired = true;
		c.sendMessage("Rogue mode disabled!");
	}
}
