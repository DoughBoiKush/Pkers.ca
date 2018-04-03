package org.osps.model.players.packets.commands.all;

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
		if (!c.inWild() && !c.inCamWild() && !c.inDuel) {
			c.getPA().handleEmpty();
		} else {
			c.sendMessage("You cannot empty your inventory here.");
		}
	}
}