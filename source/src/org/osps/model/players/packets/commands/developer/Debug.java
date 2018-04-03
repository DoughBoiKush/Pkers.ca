package org.osps.model.players.packets.commands.developer;

import org.osps.Config;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

public class Debug implements Command {
	@Override
	public void execute(Player c, String input) {
		if (Config.SERVER_DEBUG) {
			Config.SERVER_DEBUG = false;
			PlayerHandler.executeGlobalMessage("<img=10><col=0f12c5> [NEWS] </col> <col=800000>Debug mode has been toggled off by " +c.playerName, false);
		} else {
			Config.SERVER_DEBUG = true;
			PlayerHandler.executeGlobalMessage("<img=10><col=0f12c5> [NEWS] </col> <col=800000>Debug mode has been toggled on by " +c.playerName, false);
		}
	}
}
