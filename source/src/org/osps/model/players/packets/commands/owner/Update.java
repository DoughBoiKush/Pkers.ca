package org.osps.model.players.packets.commands.owner;

import java.util.Objects;

import org.osps.Server;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Start the update timer and update the server.
 * 
 * @author Emiel
 *
 */
public class Update implements Command {

	@Override
	public void execute(Player c, String input) {
		int seconds = Integer.parseInt(input);
		if (seconds < 15) {
			c.sendMessage("The timer cannot be lower than 15 seconds so other operations can be sorted.");
			seconds = 15;
		}
		PlayerHandler.updateSeconds = seconds;
		PlayerHandler.updateAnnounced = false;
		PlayerHandler.updateRunning = true;
		PlayerHandler.updateStartTime = System.currentTimeMillis();
		//HungerManager.getSingleton().endGame(true);
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			Player client = player;
			if (client.getPA().viewingOtherBank) {
				client.getPA().resetOtherBank();
				client.sendMessage("An update is now occuring, you cannot view banks.");
			}
			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(client, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession)) {
				if (duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERACTION) {
					if (!duelSession.getWinner().isPresent()) {
						duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
						duelSession.getPlayers().forEach(p -> {
							p.sendMessage("The duel has been cancelled by the server because of an update.");
							duelSession.moveAndClearAttributes(p);
						});
					}
				} else if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
					duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
					duelSession.getPlayers().forEach(p -> {
						p.sendMessage("The duel has been cancelled by the server because of an update.");
						duelSession.moveAndClearAttributes(p);
					});
				}
			}
		}
	}
}
