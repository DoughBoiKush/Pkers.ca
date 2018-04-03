package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Server;
import org.osps.model.items.GameItem;
import org.osps.model.multiplayer_session.MultiplayerSession;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.multiplayer_session.trade.TradeSession;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.skills.crafting.JewelryMaking;
import org.osps.model.shops.playerOwned.PlayerShops;
import org.osps.net.Packet;

/**
 * Bank 5 Items
 **/
public class Bank5 implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, Packet packet) {
		int interfaceId = packet.getLEShortA();
		int removeId = packet.getLEShortA();
		int removeSlot = packet.getLEShort();
		if (!c.canUsePackets) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		switch (interfaceId) {
                    
                    
                     case -20733:
                         PlayerShops.buy(c, removeSlot, 1);
                        break;
                    
		case 4233:
		case 4239:
		case 4245:
			JewelryMaking.mouldItem(c, removeId, 5);
			break;
			
		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 5);
			break;
		case 1688:
			c.getPA().useOperate(removeId);
			break;
		case 3900:
			if (c.myShopId == 81 && c.getRights().getValue() == 0) {
				c.sendMessage("You must be a donator to buy from this shop.");
				return;
			}
			c.getShops().buyItem(removeId, removeSlot, 1);
			break;

		case 3823:
			c.getShops().sellItem(removeId, removeSlot, 1);
			break;

		case 5064:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				c.sendMessage("You cannot add items to the bank whilst trading.");
				return;
			}
			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
				c.sendMessage("You have declined the duel.");
				duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			if (c.isBanking) {
				c.getItems().addToBank(removeId, 5, true);
			}
			break;
		case 5382:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				c.sendMessage("You cannot remove items from the bank whilst trading.");
				return;
			}
        	if(c.getBank().getBankSearch().isSearching()) {
        		c.getBank().getBankSearch().removeItem(removeId, 5);
        		return;
        	}
			c.getItems().removeFromBank(removeId, 5, true);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession || session instanceof DuelSession) {
				session.addItem(c, new GameItem(removeId, 5));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, 5));
			}
			break;

		case 6669:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof DuelSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, 5));
			}
			break;

		}
	}

}
