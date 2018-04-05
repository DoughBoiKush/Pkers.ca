package org.osps.model.players.packets.commands.donator;

import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Teleports the player to the donator zone.
 * 
 * @author Emiel
 */
public class Dz implements Command {

	@Override
	public void execute(Player c, String input) {
		if (c.inTrade || c.inDuel || c.inWild()) {
			return;
		}
		TeleportExecutor.teleport(c, new Position(3482, 3237, 0), true);
	}
}
