package org.osps.model.players.packets.commands.all;

import java.text.DecimalFormat;

import org.osps.Connection;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Kdr implements Command {

	@Override
	public void execute(Player c, String input) {
		 if (c.inTrade || c.inDuelArena()) {
             return;
         }
         if (Connection.isMuted(c)) {
             c.sendMessage("You are muted.");
             return;
         }
          DecimalFormat df = new DecimalFormat("#.##");
			 double KDR = ((double) c.KC) / ((double) c.DC);
	         c.forceChat("["+c.playerName+"] I have "+c.KC+" Kills,and "+c.DC+" Deaths -  "+ df.format(KDR) +". ");
	}
}
