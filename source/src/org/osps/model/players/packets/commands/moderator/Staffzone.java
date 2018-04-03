package org.osps.model.players.packets.commands.moderator;

import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Teleport the player to the staffzone.
 * 
 * @author Emiel
 */
public class Staffzone implements Command {

	@Override
	public void execute(Player c, String input) {
		TeleportExecutor.teleport(c, new Position(2912, 5475, 0));
	}
}
