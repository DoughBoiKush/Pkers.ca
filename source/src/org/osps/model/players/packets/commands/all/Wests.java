package org.osps.model.players.packets.commands.all;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Teleport the player to wests.
 * 
 * @author Emiel
 */
public class Wests implements Command {

	@Override
	public void execute(Player c, String input) {
		if (!Config.PLACEHOLDER_ECONOMY) {
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.inWild() || c.inCamWild()) {
				return;
			}
			TeleportExecutor.teleport(c, new Position(2979, 3597, 0));
		}
	}
}
