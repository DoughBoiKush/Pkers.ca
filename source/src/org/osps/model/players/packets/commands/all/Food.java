package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Food implements Command {

	@Override
	public void execute(Player c, String input) {
		 if (c.inTrade || c.inDuelArena()) {
             return;
         }
         if (c.inWild()) {
             c.sendMessage("You can't do this while in the wilderness.");
             return;
         }

         c.getItems().addItem(15354, 28);


         c.sendMessage("You get some food.");
	}
}
