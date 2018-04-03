package org.osps.model.players.packets.commands.developer;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Empty implements Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().handleEmpty();
	}

}
