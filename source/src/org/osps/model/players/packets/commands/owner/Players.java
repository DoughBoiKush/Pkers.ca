package org.osps.model.players.packets.commands.owner;

import org.osps.Config;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

public class Players implements Command {
		@Override
		public void execute(Player c, String input) {
				c.getPA().showInterface(8134);
				c.getPA().sendFrame126("@blu@"+ Config.SERVER_NAME +" Players:", 8144);
				c.getPA().sendFrame126("@red@Online players:" + PlayerHandler.getPlayerCount() + "", 8145);
				int line = 8147;
				for (int i = 0; i < Config.MAX_PLAYERS; i++)  {
					if (PlayerHandler.players[i] != null) {
						Player d = c.getClient(PlayerHandler.players[i].playerName);
						if (d.playerName != null){
							c.getPA().sendFrame126(d.playerName, line);
							line++;
						} else if (d.playerName == null) {
							c.getPA().sendFrame126("@gre@", line);
						}
					}
						}
						c.flushOutStream();
					}
				}