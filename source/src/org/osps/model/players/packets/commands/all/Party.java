package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Party implements Command {

	@Override
	public void execute(Player c, String input) {
		 if (c.inTrade) {
             return;
         }
		//TeleportExecutor.teleport(c, new Position(3291, 3027, 0));
		 c.sendMessage("Coming Soon!");
	}
}
