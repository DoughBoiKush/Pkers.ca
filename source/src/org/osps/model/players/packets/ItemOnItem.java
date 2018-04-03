package org.osps.model.players.packets;

/**
 * @author Ryan / Lmctruck30
 */

import java.util.Objects;

import org.osps.Server;
import org.osps.model.items.UseItem;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		int usedWithSlot = packet.getUnsignedShort();
		int itemUsedSlot = packet.getUnsignedShortA();
		if(usedWithSlot >= c.playerItems.length || usedWithSlot < 0 || itemUsedSlot >= c.playerItems.length || itemUsedSlot < 0) {
			return;
		}
		int useWith = c.playerItems[usedWithSlot] - 1;
		int itemUsed = c.playerItems[itemUsedSlot] - 1;
		if (!c.getItems().playerHasItem(useWith, 1)
				|| !c.getItems().playerHasItem(itemUsed, 1)) {
			return;
		}
		if (!c.canUsePackets) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		c.getSkilling().stop();
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		UseItem.ItemonItem(c, itemUsed, useWith, itemUsedSlot, usedWithSlot);
	}

}
