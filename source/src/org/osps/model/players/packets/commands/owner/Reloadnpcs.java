package org.osps.model.players.packets.commands.owner;

import java.io.IOException;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.json.NpcDefinitionLoader;

public class Reloadnpcs implements Command {

	@Override
	public void execute(Player c, String input) {
		new NpcDefinitionLoader().load();
		c.sendMessage("Reloaded objects.");
	}

}
