package org.osps.model.players.packets.commands.helper;

import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Teleport the player to the staffzone.
 * 
 * @author Emiel
 */
public class Jailzone implements Command {

	@Override
	public void execute(Player c, String input) {
		TeleportExecutor.teleport(c, new Position(3168, 9758, 0), true);
	}
}
