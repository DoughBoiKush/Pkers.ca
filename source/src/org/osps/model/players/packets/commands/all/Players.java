package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Tells the player how many players are online.
 * 
 * @author Emiel
 */
public class Players implements Command {

	@Override
	public void execute(Player c, String input) {
		int count = 0;
		//int countAdded = 25;
		for (int i = 0; i < PlayerHandler.players.length; i++) { 
			if (PlayerHandler.players[i] != null && PlayerHandler.players[i].isActive) {
				count++;
			}
		}
		//count += countAdded;
		c.sendMessage("There are currently <col=255>" + count + "</col> players online on <col=255>Pkers.Ca.");
	}
}
