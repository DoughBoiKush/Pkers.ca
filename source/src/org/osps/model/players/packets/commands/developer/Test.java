package org.osps.model.players.packets.commands.developer;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Test implements Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().openItemsKeptOnDeath();
	}
}
