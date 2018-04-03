package org.osps.model.players.packets.commands.developer;

import java.util.Optional;

import org.osps.Server;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.ConnectedFrom;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Forces a given player to log out.
 * 
 * @author Emiel
 */
public class Kick implements Command {

	@Override
	public void execute(Player c, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(input);
		
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				c.sendMessage("The player is in a trade, or duel. You cannot do this at this time.");
				return;
			}
			c2.outStream.createFrame(109);
			CycleEventHandler.getSingleton().stopEvents(c2);
			c2.properLogout = true;
			ConnectedFrom.addConnectedFrom(c2, c2.connectedFrom);
			c.sendMessage("Kicked " + c2.playerName);
		} else {
			c.sendMessage(input + " is not online. You can only kick online players.");
		}
	}
}
