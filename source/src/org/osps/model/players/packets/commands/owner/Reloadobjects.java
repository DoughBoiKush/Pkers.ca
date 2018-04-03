package org.osps.model.players.packets.commands.owner;

import java.io.IOException;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Reloadobjects implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			Server.getGlobalObjects().reloadObjectFile(c);
			c.sendMessage("Reloaded objects.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
