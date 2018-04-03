package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Spawn a specific Object.
 * 
 * @author Emiel
 *
 */
public class Object implements Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().object(Integer.parseInt(input), c.absX, c.absY, 1, 10);
	}
}
