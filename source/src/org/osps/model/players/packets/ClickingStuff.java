package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Server;
import org.osps.model.multiplayer_session.MultiplayerSession;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.net.Packet;

/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		if (!c.canUsePackets) {
			return;
		}
		if (c.tradeWith >= PlayerHandler.players.length || c.tradeWith < 0) {
			return;
		}
		MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c,
				MultiplayerSessionType.TRADE);
		if (session != null && Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			c.sendMessage("You have declined the trade.");
			session.getOther(c).sendMessage(c.playerName + " has declined the trade.");
			session.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST &&
				duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			c.sendMessage("You have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		c.setInterfaceOpen(-1);
		c.getPresets().hideSearch();
	}

}
