package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Master implements Command {

	@Override
	public void execute(Player c, String input) {
		if (c.inTrade || c.inDuelArena()) {
            return;
        }
        if (c.inWild()) {
            c.sendMessage("You can't do this while in the wilderness.");
            return;
        }
        for (int i = 0; i < 23; i++) {
            c.playerLevel[i] = 99;
            c.playerXP[i] = c.getPA().getXPForLevel(100);
            c.getPA().refreshSkill(i);
        }
        c.sendMessage("@red@You get Master status!");
	}
}