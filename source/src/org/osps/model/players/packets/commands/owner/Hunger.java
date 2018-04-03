package org.osps.model.players.packets.commands.owner;

import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Hunger implements Command {

	
	@Override
	public void execute(Player c, String input) {
		TeleportExecutor.teleport(c, new Position(2440, 3090, 0));
	}
	
	
}
