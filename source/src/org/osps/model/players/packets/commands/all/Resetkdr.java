package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Resetkdr implements Command {

	@Override
	public void execute(Player c, String input) {
		  c.KC = 0;
          c.DC = 0;
          c.getPA().loadQuests();
	}
}

