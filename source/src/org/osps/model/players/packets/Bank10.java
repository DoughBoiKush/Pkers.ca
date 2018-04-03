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
 * Bank 10 Items
 **/
public class Bank10 implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, Packet packet) {
		//int interfaceId = packet.getUnsignedShort();
		//int removeId = packet.getUnsignedShortA();
		//int removeSlot = packet.getUnsignedShortA();
		int interfaceId = packet.getLEShort();
		int removeId = packet.getUnsignedShortA();
		int removeSlot = packet.getUnsignedShortA();
		if (!c.canUsePackets) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if(c.getRights().isOwner()) {
			c.sendMessage("InterfaceID: "+interfaceId+"");
		}
		switch (interfaceId) {
                    
                    case -20733:
                        c.outStream.createFrame(27);
                        c.shopBuyAmount = true;
                        c.shopBuySlot = removeSlot;
                        break;
                    
                    
		case 4233:
		case 4239:
		case 4245:
			JewelryMaking.mouldItem(c, removeId, 10);
			break;
		case 1119:
		case 1120:
		case 1121:
		case 1122:
		case 1123:
			c.getSmithing().readInput(c.playerLevel[c.playerSmithing],
					Integer.toString(removeId), c, 10);
			break;
		case 3900:
			if (c.myShopId == 81 && c.getRights().getValue() == 0) {
				c.sendMessage("You must be a donator to buy from this shop.");
				return;
			}
			c.getShops().buyItem(removeId, removeSlot, 5);
			break;

		case 3823:
			c.getShops().sellItem(removeId, removeSlot, 5);
			break;

			
		case 5064:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				c.sendMessage("You cannot do this whilst trading.");
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
				c.getItems().addToBank(removeId, 10, true);
			}
			break;
			
		case 5382:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				c.sendMessage("You cannot do this whilst trading.");
				return;
			}
        	if(c.getBank().getBankSearch().isSearching()) {
        		c.getBank().getBankSearch().removeItem(removeId, 10);
        		return;
        	}
			c.getItems().removeFromBank(removeId, 10, true);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession || session instanceof DuelSession) {
				session.addItem(c, new GameItem(removeId, 10));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, 10));
			}
			break;

		case 6669:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof DuelSession) {
				session.removeItem(c, removeSlot, new GameItem(removeId, 10));
			}
			break;

		}
	}

}
