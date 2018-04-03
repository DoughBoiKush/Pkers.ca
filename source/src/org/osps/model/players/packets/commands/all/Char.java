package org.osps.model.players.packets.commands.all;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Teleport the player to the Make-over Mage.
 * 
 * @author Emiel
 */
public class Char implements Command {

	@Override
	public void execute(Player c, String input) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (c.inWild() || c.inCamWild()) {
			return;
		}
		c.getPA().showInterface(3559);
	}
}
