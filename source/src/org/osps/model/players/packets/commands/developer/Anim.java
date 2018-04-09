package org.osps.model.players.packets.commands.developer;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Anim implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			int a = Integer.parseInt(input);
			c.animation(a);
		} catch (Exception e) {
			c.sendMessage("::anim ####");
		}
	}
}