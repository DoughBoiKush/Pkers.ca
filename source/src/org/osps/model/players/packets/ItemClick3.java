package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.Teleport;
import org.osps.model.content.teleport.Teleport.TeleportType;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.items.pouch.GemBagDefinition;
import org.osps.model.items.pouch.HerbSackDefinition;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.Rights;
import org.osps.model.players.TeleportTablets;
import org.osps.model.players.combat.Degrade;
import org.osps.net.Packet;
import org.osps.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		int itemId11 = packet.getLEShortA();
		int itemId1 = packet.getShortA();
		int itemId = packet.getShortA();
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
		TeleportTablets.operate(c, itemId);
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			c.sendMessage("Your actions have declined the duel.");
			duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
			duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		switch (itemId) {
		case HerbSackDefinition.ITEM_ID:
			c.getHerbSack().check();
			break;
		case GemBagDefinition.ITEM_ID:
			c.getGemBag().empty();
			break;
		case 12791:
			c.getRunePouch().emptyRunePouch();
			break;
		case 2690:
			if(c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast 1 free slot.");
				return;
			}
			c.getItems().addItem(4067, 10500);
			break;
		case 2691:
			if(c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast 1 free slot.");
				return;
			}
			c.getItems().addItem(4067, 21000);
			break;
		case 2692:
			if(c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast 1 free slot.");
				return;
			}
			c.getItems().addItem(4067, 21500);
			break;
		case 2697:
			Rights rights = Rights.get(5);
			break;
		case 2698:
			Rights.get(6);
			break;
		case 2699:
			Rights.get(7);
			break;
		case 12877:
			c.getItems().addItem(4716, 1);
			c.getItems().addItem(4718, 1);
			c.getItems().addItem(4720, 1);
			c.getItems().addItem(4722, 1);
		break;
		case 12873:
			c.getItems().addItem(4724, 1);
			c.getItems().addItem(4726, 1);
			c.getItems().addItem(4728, 1);
			c.getItems().addItem(4730, 1);
		break;
		case 12883:
			c.getItems().addItem(4732, 1);
			c.getItems().addItem(4734, 1);
			c.getItems().addItem(4736, 1);
			c.getItems().addItem(4738, 1);
		break;
		case 12875:
			c.getItems().addItem(4753, 1);
			c.getItems().addItem(4755, 1);
			c.getItems().addItem(4757, 1);
			c.getItems().addItem(4759, 1);
		break;
		case 12879:
			c.getItems().addItem(4745, 1);
			c.getItems().addItem(4747, 1);
			c.getItems().addItem(4749, 1);
			c.getItems().addItem(4751, 1);
		break;
		case 12881:
			c.getItems().addItem(4708, 1);
			c.getItems().addItem(4710, 1);
			c.getItems().addItem(4712, 1);
			c.getItems().addItem(4714, 1);
		break;
		case 13173:
			c.getItems().addItem(1038, 1);
			c.getItems().addItem(1040, 1);
			c.getItems().addItem(1042, 1);
			c.getItems().addItem(1044, 1);
			c.getItems().addItem(1046, 1);
			c.getItems().addItem(1048, 1);
		break;
		case 13175:
			c.getItems().addItem(1053, 1);
			c.getItems().addItem(1055, 1);
			c.getItems().addItem(1057, 1);
		break;
		case 11907:
		case 12899:
			int charge = itemId == 11907 ? c.getTridentCharge() : c.getToxicTridentCharge();
			if (charge <= 0) {
				c.sendMessage("Your trident currently has no charge.");
				return;
			}
			if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need at least 3 free slots for this.");
				return;
			}
			c.getItems().addItem(554, charge * 5);
			c.getItems().addItem(560, charge);
			c.getItems().addItem(562, charge);
			if (itemId == 11907) {
				c.setTridentCharge(0);
			} else {
				c.setToxicTridentCharge(0);
			}
			c.sendMessage("You revoke " + charge + " charges from the staff.");
			break;
			
		case 12006:
			Degrade.checkRemaining(c, 12006);
		break;
			
		case 12926:
			if (c.getToxicBlowpipeAmmo() == 0 || c.getToxicBlowpipeAmmoAmount() == 0) {
				c.sendMessage("You have no ammo in the pipe.");
				return;
			}
			if (c.getItems().addItem(c.getToxicBlowpipeAmmo(), c.getToxicBlowpipeAmmoAmount())) {
				c.setToxicBlowpipeAmmoAmount(0);
				c.sendMessage("You unload the pipe.");
			}
			break;
		case 11864:
		c.sendMessage("@blu@You don't see a reason why you would want to do this.");
		break;
		case 2552:
		case 2554:
		case 2556:
		case 2558:
		case 2560:
		case 2562:
		case 2564:
		case 2566:
			TeleportExecutor.teleport(c, new Teleport(new Position(3362, 3263, 0), TeleportType.NORMAL));
			break;
		case 1712:
			c.getPA().handleGlory(itemId);
			c.isOperate = true;
			c.itemUsing = itemId;
			break;

		case 1710:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;

		case 1708:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;

		case 1706:
			c.getPA().handleGlory(itemId);
			c.itemUsing = itemId;
			c.isOperate = true;
			break;

		default:
			if (c.getRights().isDeveloper() && Config.SERVER_DEBUG)
				Misc.println(c.playerName + " - Item3rdOption: " + itemId
						+ " : " + itemId11 + " : " + itemId1);
			break;
		}

	}

}
