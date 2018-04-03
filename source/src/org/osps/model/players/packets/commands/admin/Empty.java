package org.osps.model.players.packets.commands.admin;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Empty the inventory of the player.
 * 
 * @author Emiel
 */
public class Empty implements Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().handleEmpty();
	}
}
