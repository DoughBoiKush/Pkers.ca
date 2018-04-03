package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Vrunes implements Command {

	@Override
	public void execute(Player c, String input) {
		 if (c.inTrade || c.inDuelArena()) {
             return;
         }
         if (c.inWild()) {
             c.sendMessage("You can't do this while in the wilderness.");
             return;
         }
         c.getItems().addItem(560, 500);
         c.getItems().addItem(9075, 500);
         c.getItems().addItem(557, 1000);
         c.sendMessage("You get some Venge Runes.");
     }
}
