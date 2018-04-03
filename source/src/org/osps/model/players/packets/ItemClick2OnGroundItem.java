package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Server;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;

public class ItemClick2OnGroundItem implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		final int itemX = packet.getLEShort();
		final int itemY = packet.getLEShortA();
		final int itemId = packet.getUnsignedShortA();
		if (!c.canUsePackets) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		//
	}
}