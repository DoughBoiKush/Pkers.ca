package org.osps.model.players.packets.commands.owner;

import org.osps.Connection;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Testing the teleport.
 * @author Chris
 * @date Aug 24, 2015 10:27:09 PM
 *
 */
public class Jig implements Command {

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

