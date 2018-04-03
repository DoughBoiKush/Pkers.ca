package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Force all players to be your bitch.
 * 
 * @author Emiel
 *
 */
public class Falem implements Command {

	@Override
	public void execute(Player c, String input) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				c2.forceChat(input);
				c2.forcedChatUpdateRequired = true;
				c2.updateRequired = true;
			}
		}
	}
}
