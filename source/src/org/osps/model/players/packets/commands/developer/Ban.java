package org.osps.model.players.packets.commands.developer;

import java.util.Optional;

import org.osps.Connection;
import org.osps.Server;
import org.osps.model.multiplayer_session.MultiplayerSession;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Ban a given player.
 * 
 * @author Emiel
 */
public class Ban implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String[] args = input.split("-");
			if (args.length != 3) {
				throw new IllegalArgumentException();
			}
			String name = args[0];
			int duration = Integer.parseInt(args[1]);
			long banEnd;
			if (duration == 0) {
				banEnd = Long.MAX_VALUE;
			} else {
				banEnd = System.currentTimeMillis() + duration * 1000 * 60;
			}
			String reason = args[2];
			Connection.addNameToBanList(name, banEnd);
			Connection.addNameToFile(name, banEnd);

			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(name);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				if (c2.getRights().isBetween(2,  3)) {
					c.sendMessage("You cannot ban this player.");
					return;
				}
				if (Server.getMultiplayerSessionListener().inAnySession(c2)) {
					MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c2);
					session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				}
				c2.disconnected = true;
				if (duration == 0) {
					c.sendMessage(name + " has been permanently banned.");
				} else {
					c.sendMessage(name + " has been banned for " + duration + " minute(s).");
				}
				return;
			}
			if (duration == 0) {
				c.sendMessage(name + " has been permanently banned.");
			} else {
				c.sendMessage(name + " has been banned for " + duration + " minute(s).");
			}
		} catch (Exception e) {
			c.sendMessage("Correct usage: ::ban-player-duration-reason (0 as duration for permanent)");
		}
	}
}
