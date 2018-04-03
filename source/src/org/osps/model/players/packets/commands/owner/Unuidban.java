package org.osps.model.players.packets.commands.owner;

import org.osps.Connection;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Unuidban implements Command {

	@Override
	public void execute(Player c, String input) {
		boolean success = Connection.removeUidBan(input);
		
		if (!success) {
			c.sendMessage("Unable to un-uid ban " + input + ".");
		} else {
			c.sendMessage("Successfully un-uid banned " + input + ".");
		}
	}
	

}
