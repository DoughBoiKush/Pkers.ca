package org.osps.model.players.packets.commands.moderator;

import java.util.Optional;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Mute a given player.
 * 
 * @author Emiel
 */
public class Mute implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String[] args = input.split("-");
			if (args.length != 3) {
				throw new IllegalArgumentException();
			}
			String name = args[0];
			int duration = Integer.parseInt(args[1]);
			long muteEnd = 0;
			if (duration == 0) {
				muteEnd = Long.MAX_VALUE;
			} else {
				muteEnd = System.currentTimeMillis() + duration * 1000 * 60;
			}
			String reason = args[2];
			
			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(name);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				c2.muteEnd = muteEnd;
				if (duration == 0) {
					c2.sendMessage("You have been permanently muted by " + c.playerName + " due to rulebreaking.");
					c2.sendMessage("To prevent further mutes please read the rules.");
					c.sendMessage("Successfully permanently " + c2.playerName + " for " + duration + " minutes.");
				} else {
					c2.sendMessage("You have been temporarily muted by " + c.playerName + " for " + duration + " minutes");
					c2.sendMessage("To prevent further mutes please read the rules.");
					c.sendMessage("Successfully muted " + c2.playerName + " for " + duration + " minutes.");
				}
			} else {
				c.sendMessage(name + " is not online. You can only mute online players.");
			}
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::mute-player-duration-reason.");
		}
	}
}
