package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.items.ItemDefinition;
import org.osps.model.items.pouch.GemBagDefinition;
import org.osps.model.items.pouch.HerbSackDefinition;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerAssistant.PointExchange;
import org.osps.model.players.TeleportTablets;
import org.osps.model.players.skills.Runecrafting.Pouches;
import org.osps.model.players.skills.hunter.JarRewards;
import org.osps.model.players.skills.hunter.JarRewards.ImpRewards;
import org.osps.net.Packet;
import org.osps.util.Misc;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 *         Proper Streams
 */

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		int itemId = packet.getShortA();
		if (!c.canUsePackets) {
			return;
		}
		
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		if(JarRewards.ImpRewards.impReward.containsKey(itemId)) {
			ImpRewards.getReward(c, itemId);
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
		for (int i = 0; i < Pouches.pouchData.length; i++) {
			if (itemId == Pouches.pouchData[i][0]) {
				Pouches.checkPouch(c, itemId);
			}
		}
		TeleportTablets.operate(c, itemId);
		ItemDefinition def = ItemDefinition.forId(itemId);
		switch (itemId) {
		case HerbSackDefinition.ITEM_ID:
			c.getHerbSack().empty();
			break;
		case GemBagDefinition.ITEM_ID:
			c.getGemBag().check();
			break;
		case 11941:
			c.getLoot().depositLootbag();
			break;
		case 12877:
			if (c.getItems().freeSlots() < 4) {
				c.sendMessage("You need atleast 4 inventory spots to open this!");
				return;
			}
			c.getItems().addItem(4716, 1);
			c.getItems().addItem(4718, 1);
			c.getItems().addItem(4720, 1);
			c.getItems().addItem(4722, 1);
			c.getItems().deleteItem2(12877, 1);
		break;
		case 2996:
			c.getPA().exchangeItems(PointExchange.PK_POINTS, 2996, 1);	
		break;
		case 12873:
			if (c.getItems().freeSlots() < 4) {
				c.sendMessage("You need atleast 4 inventory spots to open this!");
				return;
			}
			c.getItems().addItem(4724, 1);
			c.getItems().addItem(4726, 1);
			c.getItems().addItem(4728, 1);
			c.getItems().addItem(4730, 1);
			c.getItems().deleteItem2(12873, 1);
		break;
		case 12883:
			if (c.getItems().freeSlots() < 4) {
				c.sendMessage("You need atleast 4 inventory spots to open this!");
				return;
			}
			c.getItems().addItem(4732, 1);
			c.getItems().addItem(4734, 1);
			c.getItems().addItem(4736, 1);
			c.getItems().addItem(4738, 1);
			c.getItems().deleteItem2(12883, 1);
		break;
		case 12875:
			if (c.getItems().freeSlots() < 4) {
				c.sendMessage("You need atleast 4 inventory spots to open this!");
				return;
			}
			c.getItems().addItem(4753, 1);
			c.getItems().addItem(4755, 1);
			c.getItems().addItem(4757, 1);
			c.getItems().addItem(4759, 1);
			c.getItems().deleteItem2(12875, 1);
		break;
		case 12879:
			if (c.getItems().freeSlots() < 4) {
				c.sendMessage("You need atleast 4 inventory spots to open this!");
				return;
			}
			c.getItems().addItem(4745, 1);
			c.getItems().addItem(4747, 1);
			c.getItems().addItem(4749, 1);
			c.getItems().addItem(4751, 1);
			c.getItems().deleteItem2(12879, 1);
		break;
		case 12881:
			if (c.getItems().freeSlots() < 4) {
				c.sendMessage("You need atleast 4 inventory spots to open this!");
				return;
			}
			c.getItems().addItem(4708, 1);
			c.getItems().addItem(4710, 1);
			c.getItems().addItem(4712, 1);
			c.getItems().addItem(4714, 1);
			c.getItems().deleteItem2(12881, 1);
		break;
		case 13173:
			if (c.getItems().freeSlots() < 6) {
				c.sendMessage("You need atleast 6 inventory spots to open this!");
				return;
			}
			c.getItems().addItem(1038, 1);
			c.getItems().addItem(1040, 1);
			c.getItems().addItem(1042, 1);
			c.getItems().addItem(1044, 1);
			c.getItems().addItem(1046, 1);
			c.getItems().addItem(1048, 1);
			c.getItems().deleteItem2(13173, 1);
		break;
		case 13175:
			if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need atleast 3 inventory spots to open this!");
				return;
			}
			c.getItems().addItem(1053, 1);
			c.getItems().addItem(1055, 1);
			c.getItems().addItem(1057, 1);
			c.getItems().deleteItem2(13175, 1);
		break;
		case 11907:
		case 12899:
			int charge = itemId == 11907 ? c.getTridentCharge() : c.getToxicTridentCharge();
			c.sendMessage("The " + def.getName() + " has " + charge + " charges remaining.");
			break;
		case 12926:
			def = ItemDefinition.forId(c.getToxicBlowpipeAmmo());
			c.sendMessage("The blowpipe has "+c.getToxicBlowpipeAmmoAmount()+" darts and " + c.getToxicBlowpipeCharge() + " charge remaining.");
			break;
		case 12931:
			def = ItemDefinition.forId(c.getToxicBlowpipeAmmo());
			c.sendMessage("The serpentine helm has "+c.getSerpentineHelmCharge()+" charge remaining.");
			break;
		case 12904:
			def = ItemDefinition.forId(c.getToxicStaffOfDeadCharge());
			c.sendMessage("The toxic staff of the dead has "+c.staffOfDeadCharge+" charge remaining.");
			break;
		case 8901:
			c.getPA().assembleSlayerHelmet();
		break;
		case 11283:
		case 11285:
		case 11284:
			c.sendMessage("Your dragonfire shield currently has "+c.getDragonfireShieldCharge()+" charges.");
			break;
		case 4155:
			c.sendMessage("I currently have@blu@ "+c.slayerPoints+"@bla@ slayer points.");
			break;
	/*	case 15098:
			c.getItems().deleteItem(itemId, 1);
			c.getItems().addItem(15088, 1);
			break;
		case 15088:
			c.getItems().deleteItem(itemId, 1);
			c.getItems().addItem(15098, 1);
			break;*/
		case 11802:
			c.sendMessage("Dismantle has been disabled.");
			break;
		case 11804:
			c.sendMessage("Dismantle has been disabled.");
			break;
		case 11806:
			c.sendMessage("Dismantle has been disabled.");
			break;
		case 11808:
			c.sendMessage("Dismantle has been disabled.");
			break;
		default:
			if (c.getRights().isDeveloper() && Config.SERVER_DEBUG)
				Misc.println(c.playerName + " - Item3rdOption: " + itemId);
			break;
		}

	}

}
