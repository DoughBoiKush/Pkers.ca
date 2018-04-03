package org.osps.model.players.packets.commands.developer;

import org.osps.Config;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

public class Alert implements Command {

	@Override
	public void execute(Player c, String input) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				 Player c2 = (Player)PlayerHandler.players[i];
				c2.sendMessage("Alert##Notification##" + input + "##By: " + c.playerName);
			}
		}
	}
}
