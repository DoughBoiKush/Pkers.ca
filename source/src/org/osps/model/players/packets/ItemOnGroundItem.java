package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;
import org.osps.util.Misc;

public class ItemOnGroundItem implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		int a1 = packet.getLEShort();
		int itemUsed = packet.getShortA();
		int groundItem = packet.getUnsignedShort();
		int gItemY = packet.getShortA();
		int itemUsedSlot = packet.getLEShortA();
		int gItemX = packet.getUnsignedShort();
		if (!c.canUsePackets) {
			return;
		}
		if (!c.getItems().playerHasItem(itemUsed, 1)) {
			return;
		}
		if (!Server.itemHandler.itemExists(groundItem, gItemX, gItemY, c.heightLevel)) {
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
		if(itemUsed == 590 && groundItem == 1511) {
			
		}
		
		if(itemUsed == 590 && groundItem == 3438 && c.hungerGames && HungerManager.getSingleton().gameRunning()) {
			HungerManager.getSingleton().lightFire(gItemX, gItemY);
			c.sendMessage("You light the fire.");
			c.animation(733);
		}
		
		switch (itemUsed) {
		/*case 590:
			Firemaking.attemptFire(c, itemUsed, groundItem, gItemX, gItemY,
					true);
			break;*/

		default:
			if (c.getRights().isDeveloper() && Config.SERVER_DEBUG)
				Misc.println("ItemUsed " + itemUsed + " on Ground Item "
						+ groundItem);
			break;
		}
	}

}
