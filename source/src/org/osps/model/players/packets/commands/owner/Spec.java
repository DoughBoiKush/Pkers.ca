package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Spec implements Command {

	@Override
	public void execute(Player c, String input) {
		c.specAmount = 10;
	}
	
}