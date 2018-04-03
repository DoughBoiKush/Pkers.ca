package org.osps.model.players.packets.commands.all;

import org.apache.commons.lang3.text.WordUtils;
import org.osps.Config;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Sends the player a message containing a list of all online players with a
 * dice bag in their inventory.
 * 
 * @author Emiel
 */
public class Hosts implements Command {

	@Override
	public void execute(Player c, String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player c2 = PlayerHandler.players[i];
				if (c2.getItems().playerHasItem(15098)) {
					sb.append(c2.playerName + ", ");
				}
			}
		}
		if (sb.length() > 0) {
			String result = "@blu@Available hosts@bla@: " + sb.substring(0, sb.length() - 2);
			String[] wrappedLines = WordUtils.wrap(result, 80).split(System.getProperty("line.separator"));
			for (String line : wrappedLines) {
				c.sendMessage(line);
			}
		} else {
			c.sendMessage("@blu@No hosts available!");
		}
	}
}
