package org.osps.model.players.packets.commands.all;

import org.osps.Config;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Skull implements Command {

	@Override
	public void execute(Player c, String input) {
		 if (c.isSkulled) {
             c.sendMessage("You are already skulled!");
             return;
         }

         if (c.inTrade || c.inDuelArena()) {
             return;
         }
         c.headIconPk = 0;
         c.skullTimer = Config.SKULL_TIMER;
         c.isSkulled = true;
         c.getPA().requestUpdates();
         c.sendMessage("You are now skulled. Skull timer -> " + ((Config.SKULL_TIMER / 2) / 60) + " minutes.");
         return;
	}
}
