package org.osps.model.players.packets;

/**
 * @author Ryan / Lmctruck30
 */

import java.util.Objects;

import org.osps.Server;
import org.osps.model.items.UseItem;
import org.osps.model.minigames.FishingTourney;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.skills.cooking.Cooking;
import org.osps.net.Packet;

public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		/*
		 * a = ? b = ?
		 */

		int a = packet.getUnsignedShort();
		int objectId = packet.getLEShort();
		int objectY = packet.getLEShortA();
		int b = packet.getUnsignedShort();
		int objectX = packet.getLEShortA();
		int itemId = packet.getUnsignedShort();
		if (!c.canUsePackets) {
			return;
		}
		if (!c.getItems().playerHasItem(itemId, 1)) {
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
		UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		switch (objectId) {
		/*
		 * case ###: //Glory recharging if (itemId == 1710 || itemId == 1708 ||
		 * itemId == 1706 || itemId == 1704) { int amount =
		 * (c.getItems().getItemCount(1710) + c.getItems().getItemCount(1708) +
		 * c.getItems().getItemCount(1706) + c.getItems().getItemCount(1704));
		 * int[] glories = {1710, 1708, 1706, 1704}; for (int i : glories) {
		 * c.getItems().deleteItem(i, c.getItems().getItemCount(i)); }
		 * c.startAnimation(832); c.getItems().addItem(1712, amount); } break;
		 */
		case 12269:
		case 2732:
		case 3039:
		case 114:
		case 4488:
		case 25155:
                    if (c.inCook() || FishingTourney.getSingleton().inFishingArea(c)) {
                    	c.face(objectX, objectY);
                    	Cooking.cookThisFood(c, itemId, objectId);
                    } else {
                        c.sendMessage("You must move closer to the range!");
                    }
			break;
		}

	}

}
