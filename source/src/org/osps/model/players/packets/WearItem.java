package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Server;
import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.minigames.hunger.PlayerWrapper;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.DiceHandler;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.skills.Runecrafting.Pouches;
import org.osps.net.Packet;
import org.osps.util.Misc;

/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, Packet packet) {
		c.wearId = packet.getUnsignedShort();
		c.wearSlot = packet.getUnsignedShortA();
		c.interfaceId = packet.getUnsignedShortA();
		c.alchDelay = System.currentTimeMillis();
		if (!c.canUsePackets) {
			return;
		}
		if (!c.getItems().playerHasItem(c.wearId, 1)) {
			return;
		}
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if(c.getRights().isOwner()) {
			c.sendMessage(c.wearSlot + "");
		}
		/**if(c.hungerGames) {
			PlayerWrapper play = HungerManager.getSingleton().getWrapper(c);
			if(play != null && !play.betrayedLanthus()) {
				c.getDH().sendNpcChat3("No, no! You must keep the mark on!", "Otherwise our lord will not accept", "your sacrifice!", 5721, "Lanthus");
				return;
			} else if(play != null && play.betrayedLanthus()) {
				c.getDH().sendNpcChat1("You will regret the day you betrayed me...", 4178, "Lanthus");	
			}
		}**/
		//TODO: fix weilding
		if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(c, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			c.sendMessage("You cannot remove items from your equipment whilst trading, trade declined.");
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
			if (c.wearId == Pouches.pouchData[i][0]) {
				Pouches.emptyPouch(c, c.wearId);
			}
		}
		if ((c.playerIndex > 0 || c.npcIndex > 0) && c.wearId != 4153 && c.wearId != 12848 && !c.usingMagic && !c.usingBow
				&& !c.usingOtherRangeWeapons && !c.usingCross)
			c.getCombat().resetPlayerAttack();
		if (c.canChangeAppearance) {
			c.sendMessage("You can't wear an item while changing appearence.");
			return;
		}
		for (int arrows : c.ARROWS) {
			if (c.wearId == arrows) {
				if (c.rangeDelay > 0) {
					return;
				}
			}
		}
		for (int bowId : c.BOWS) {
			if (c.wearId == bowId) {
				c.switchDelay.reset();
			}
		}
/*		if(c.wearId > DiceHandler.DICE_BAG && c.wearId <= 15100) {
			c.sendMessage("Disabled for now.");
			return;
		}*/
		if ((c.wearId == 15317 || c.wearId == 15316 || c.wearId == 15315) && (c.getRights().getValue() == 0)) {
			c.sendMessage("This is a donator only item.");
			return;
		}
		if ((c.wearId == 15318) && (c.KC < 1000)) {
			c.sendMessage("You need atleast 1000 Kill Count to wear it.");
			return;
		}
		if(!c.inDice() && c.wearId > DiceHandler.DICE_BAG && c.wearId <= 15100) {
			c.sendMessage("You must be in the dicing area to dice.");
			c.sendMessage("The dicing area is located at ::dice");
			return;
		}
		if(c.getRights().getValue() == 0 && c.wearId > DiceHandler.DICE_BAG && c.wearId <= 15100) {
			c.sendMessage("This is a donator only feature! ::donate");
			return;
		}	
		if (c.inDice() && c.wearId > DiceHandler.DICE_BAG && c.wearId <= 15100) {		
			if (System.currentTimeMillis() - c.diceDelay >= 5000) {
				int random;
					random = Misc.random(100);
			for (int j = 0; j < Server.playerHandler.players.length; j++) {
				if (Server.playerHandler.players[j] != null) {
					Player c2 = (Player) Server.playerHandler.players[j];
									if (c2.inDice()) {
							c2.sendMessage("[@red@DICE@bla@] @blu@"+ Misc.ucFirst(c.playerName) + " @bla@rolled @red@"+random+"@bla@ on the percentile dice.");	
/*							if (random < 55) {
								c2.sendMessage("[@red@DICE@bla@] The gambler @blu@"+ Misc.ucFirst(c.playerName) + " @bla@wins.");
							} else if (random == 55) {
								c2.sendMessage("[@red@DICE@bla@] It's a tie for gambler @blu@"+ Misc.ucFirst(c.playerName) +"@bla@. Please re-roll!");
							} else {
								c2.sendMessage("[@red@DICE@bla@] The gambler @blu@"+ Misc.ucFirst(c.playerName) + " @bla@loses. Please pay out the winnings!");
							}*/
							c.diceDelay = System.currentTimeMillis();
						}
					}
				}
					} else {
						if (c.inDice()) {
							c.sendMessage("Please wait 10 seconds to re-roll.");
						}
						return;
					}
					return;
				}

		if (c.wearId >= 5509 && c.wearId <= 5515) {
			int pouch = -1;
			int a = c.wearId;
			if (a == 5509)
				pouch = 0;
			if (a == 5510)
				pouch = 1;
			if (a == 5512)
				pouch = 2;
			if (a == 5514)
				pouch = 3;
			c.getPA().emptyPouch(pouch);
			return;
		}
		if (!Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
			c.getItems().wearItem(c.wearId, c.wearSlot);
		//	c.getItems().update();
		}
	}

}
