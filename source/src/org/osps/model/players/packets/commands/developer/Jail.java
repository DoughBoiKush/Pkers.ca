package org.osps.model.players.packets.commands.developer;

import java.util.Optional;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Jail a given player.
 * 
 * @author Emiel
 */
public class Jail implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String[] args = input.split("-");
			if (args.length != 3) {
				throw new IllegalArgumentException();
			}
			String name = args[0];
			int duration = Integer.parseInt(args[1]);
			long jailEnd = 0;
			if (duration == 0) {
				jailEnd = Long.MAX_VALUE;
			} else {
				jailEnd = System.currentTimeMillis() + duration * 1000 * 60;
			}
			String reason = args[2];

			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(name);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				if (Server.getMultiplayerSessionListener().inAnySession(c)) {
					c.sendMessage("The player is in a trade, or duel. You cannot do this at this time.");
					return;
				}
				c2.teleportToX = 3168;
				c2.teleportToY = 9758;
				c2.jailEnd = jailEnd;
				if (duration == 0) {
					c2.sendMessage("@red@You have been permanently jailed by " + c.playerName + " .");
					c.sendMessage("Permanently jailed " + c2.playerName + ".");

				} else {
					c2.sendMessage("@red@You have been jailed by " + c.playerName + " for " + duration + " minutes.");
					c2.sendMessage("@red@Type ::unjail after having served your time to be unjailed.");
					c.sendMessage("Successfully jailed " + c2.playerName + " for " + duration + " minutes.");
				}
			} else {
				c.sendMessage(name + " is not online. You can only jail online players.");
			}
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::jail-player-duration-reason");
		}
	}
}
