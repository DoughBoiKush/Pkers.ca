package org.osps.model.players.packets.commands.developer;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Damageme implements Command {
	
	@Override
	public void execute(Player c, String input) {
		c.playerLevel[3] = 10;
		c.playerLevel[5] = 10;
		c.getPA().refreshSkill(3);
		c.getPA().refreshSkill(5);
	}
}
