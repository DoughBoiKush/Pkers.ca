package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Unskull implements Command {

	@Override
	public void execute(Player c, String input) {
		 if (!c.isSkulled) {
             c.sendMessage("@bla@You are not skulled...");
             return;
         }
         if (c.inWild()) {
             c.sendMessage("You can't do this while in the wilderness.");
             return;
         }
         if (c.inTrade || c.inDuelArena()) {
             return;
         }

         c.skullTimer = 0;
         c.isSkulled = false;
         c.headIconPk = -1;
         c.getPA().requestUpdates();
         return;
     }
}
