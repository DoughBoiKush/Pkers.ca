package org.osps.model.players.packets.commands.admin;

import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Party implements Command {

	@Override
	public void execute(Player c, String input) {
		 if (c.inTrade) {
             return;
         }
		TeleportExecutor.teleport(c, new Position(2096, 3206, 0));
	}
}

