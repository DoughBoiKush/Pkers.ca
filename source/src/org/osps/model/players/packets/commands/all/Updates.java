package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Updates implements Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().sendFrame126("www.forums.osps.com/index.php?/forum/3-updates/", 12000);
	}

}
