package org.osps.model.players.packets.commands.moderator;

import org.osps.Connection;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Unbans a given player.
 * 
 * @author Emiel
 */
public class Unban implements Command {

	@Override
	public void execute(Player c, String input) {
		Connection.removeNameFromBanList(input);
		c.sendMessage(input + " has been unbanned.");
	}
}
