package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Anim implements Command {

	@Override
	public void execute(Player c, String input) {
		String args[] = input.split(" ");
		if (args.length != 1) {
			throw new IllegalArgumentException();
		}
		c.animation(Integer.parseInt(args[0]));
	}

}
