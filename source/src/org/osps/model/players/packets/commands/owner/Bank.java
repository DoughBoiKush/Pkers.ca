package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Open the banking interface.
 * 
 * @author Emiel
 */
public class Bank implements Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().openUpBank();
	}
}
