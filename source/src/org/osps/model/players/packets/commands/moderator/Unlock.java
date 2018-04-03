package org.osps.model.players.packets.commands.moderator;

import org.osps.Connection;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;

/**
 * Unlocks the specified player's account.
 * @author Chris
 * @date Aug 23, 2015 10:36:27 PM
 *
 */
public class Unlock implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String[] args = input.split(" ");
			if (args.length != 1) {
				c.sendMessage("Invalid arguments specified!");
				throw new IllegalArgumentException();
			}
			String name = args[0];
			if (Connection.lockedAccounts.contains(name)) {
				Connection.unlockAccount(name);
				c.sendMessage(Misc.formatPlayerName(name) + "'s account has been unlocked.");
			} else {
				c.sendMessage("That player's account is not locked.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			c.sendMessage("Correct usage: ::unlock playername");
		}
	}
}
