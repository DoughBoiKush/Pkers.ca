package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Server;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.net.Packet;
import org.osps.util.Misc;

public class ItemOnPlayer implements PacketType {
	
	@Override
	public void processPacket(Player c, Packet packet) {
		int interfaceId = packet.getUnsignedShortA();
		int playerIndex = packet.getUnsignedShort();
		int itemId = packet.getUnsignedShort();
		int slotId = packet.getUnsignedShort();
		c.setItemOnPlayer(null);
		if (!c.canUsePackets) {
			return;
		}
		if(c.teleTimer > 0)
			return;
		if(playerIndex > PlayerHandler.players.length) {
			return;
		}
		if(slotId > c.playerItems.length) {
			return;
		}
		if(PlayerHandler.players[playerIndex] == null) {
			return;
		}
		if(!c.getItems().playerHasItem(itemId, 1, slotId)) {
			return;
		}
		Player other = PlayerHandler.players[playerIndex];
		if (other == null) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (Misc.distanceBetween(c, other) > 15) {
			c.sendMessage("You need to move closer to do this.");
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
		c.setItemOnPlayer(other);
		switch (itemId) {
			case 962:
				if (other.connectedFrom.equalsIgnoreCase(c.connectedFrom)) {
					c.sendMessage("You cannot use this on another player that is on the same host as you.");
					return;
				}
				c.face(other.getX(), other.getY());
				c.getDH().sendDialogues(612, -1);
				break;
		}
	}
}