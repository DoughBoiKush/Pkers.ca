package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Server;
import org.osps.model.content.enchanting.Bolts;
import org.osps.model.items.GameItem;
import org.osps.model.multiplayer_session.MultiplayerSession;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.multiplayer_session.trade.TradeSession;
import org.osps.model.players.Boundary;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.shops.playerOwned.PlayerShops;
import org.osps.net.Packet;

/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Player c, Packet packet) {
		int Xamount = packet.getInt();
		if (Xamount == 0)
			Xamount = 1;
			if (c.buyingX) {
			c.getShops().buyItem(c.xRemoveId, c.xRemoveSlot, Xamount);
			c.xRemoveSlot = 0;
			c.xInterfaceId = 0;
			c.xRemoveId = 0;
			c.buyingX = false;
		    }
		if (c.enchantOpal) {
			c.getPA().closeAllWindows();
			c.enchantOpal = false;
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 879, Xamount)) {
				return;
			}
		}
		
		if (c.enchantSapphire) {
			c.getPA().closeAllWindows();
			c.enchantSapphire = false;
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 9337, Xamount)) {
				return;
			}
		}
		
		if (c.enchantJade) {
			c.getPA().closeAllWindows();
			c.enchantJade = false;
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 9335, Xamount)) {
				return;
			}
		}
		
		if (c.enchantPearl) {
			c.enchantPearl = false;
			c.getPA().closeAllWindows();
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 880, Xamount)) {
				return;
			}
		}
		
		if (c.enchantEmerald) {
			c.enchantEmerald = false;
			c.getPA().closeAllWindows();
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 9338, Xamount)) {
				return;
			}
		}
		
		if (c.enchantTopaz) {
			c.enchantTopaz = false;
			c.getPA().closeAllWindows();
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 9336, Xamount)) {
				return;
			}
		}
		
		if (c.enchantRuby) {
			c.enchantRuby = false;
			c.getPA().closeAllWindows();
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 9339, Xamount)) {
				return;
			}
		}
		
		if (c.enchantDiamond) {
			c.enchantDiamond = false;
			c.getPA().closeAllWindows();
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 9340, Xamount)) {
				return;
			}
		}
		
		if (c.enchantDragon) {
			c.enchantDragon = false;
			c.getPA().closeAllWindows();
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 9341, Xamount)) {
				return;
			}
		}
		
		if (c.enchantOnyx) {
			c.enchantOnyx = false;
			c.getPA().closeAllWindows();
			if (Xamount <= 0) {
				return;
			}
			if (Bolts.enchant(c, 9342, Xamount)) {
				return;
			}
		}
                
                if (c.shopBuyAmount) {
                    c.shopBuyAmount = false;
                    PlayerShops.buy(c, c.shopBuySlot, Xamount);
                }
		
		switch (c.xInterfaceId) {
		 case 3900:
				c.getShops().buyItem(c.xRemoveId, c.xRemoveSlot, Xamount);
				break;
		case 5064:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
        		c.sendMessage("You cannot bank items whilst trading.");
        		return;
        	}
			if (!c.getItems().playerHasItem(c.xRemoveId, Xamount))
				return;
			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST &&
					duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
				c.sendMessage("You have declined the duel.");
				duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			c.getItems().addToBank(c.playerItems[c.xRemoveSlot] - 1, Xamount, true);
			break;
		case 5382:
			if (Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
        		c.sendMessage("You cannot remove items from the bank whilst trading.");
        		return;
        	}
			if (c.getBank().getBankSearch().isSearching()) {
				c.getBank().getBankSearch().removeItem(c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot).getId() - 1, Xamount);
				return;
			}
			if(c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot) != null)
				c.getItems().removeFromBank(c.getBank().getCurrentBankTab().getItem(c.xRemoveSlot).getId() - 1, Xamount, true);
			break;

		case 3322:
			MultiplayerSession session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession || session instanceof DuelSession) {
				session.addItem(c, new GameItem(c.xRemoveId, Xamount));
			}
			break;

		case 3415:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof TradeSession) {
				session.removeItem(c, c.xRemoveSlot, new GameItem(c.xRemoveId, Xamount));
			}
			break;

		case 6669:
			session = Server.getMultiplayerSessionListener().getMultiplayerSession(c);
			if (Objects.isNull(session)) {
				return;
			}
			if (session instanceof DuelSession) {
				session.removeItem(c, c.xRemoveSlot, new GameItem(c.xRemoveId, Xamount));
			}
			break;
		}
				
		if (c.settingMin) {
			if (Xamount < 0 || Xamount > Integer.MAX_VALUE)
				return;
			c.diceMin = Xamount;
			c.settingMin = false;
			c.settingMax = true;
			c.getDH().sendDialogues(9998, -1);
			return;
		} else if (c.settingMax) {
			if (Xamount < 0 || Xamount > Integer.MAX_VALUE)
				return;
			c.diceMax = Xamount;
			c.settingMax = false;
			c.settingMin = false;
			c.getDH().sendDialogues(9999, -1);
			return;
		} else if (c.settingBet) {
			Player o = c.diceHost;
			if (Xamount < 0 || Xamount > Integer.MAX_VALUE)
				return;
			if (!c.getItems().playerHasItem(2996, Xamount)) {
				c.sendMessage("You need more tickets!");
				o.sendMessage("The other player needs more tickets.");
				c.getPA().removeAllWindows();
				o.getPA().removeAllWindows();
				return;
			}
			if (!o.getItems().playerHasItem(2996, Xamount)) {
				c.sendMessage("The other player needs more tickets.");
				o.sendMessage("You need more tickets!");
				c.getPA().removeAllWindows();
				o.getPA().removeAllWindows();
				return;
			}
			if (Xamount < 0 || Xamount > Integer.MAX_VALUE)
				return;
			if (Xamount > o.diceMax || Xamount < o.diceMin) {
				c.sendMessage("That bet is too big or too small.");
				c.sendMessage("Please bet between " + o.diceMin + " and " + o.diceMax);
				c.getPA().removeAllWindows();
				o.getPA().removeAllWindows();
				return;
			}
			c.betAmount = Xamount;
			c.getItems().deleteItem(2996, Xamount);
			o.getItems().deleteItem(2996, Xamount);
			PlayerHandler.players[c.otherDiceId].betAmount = Xamount;
			c.settingBet = false;
			c.settingMax = false;
			c.settingMin = false;
			c.getDH().sendDialogues(11002, -1);
			return;
		}
		if (c.attackSkill) {
			if(c.underAttackBy > 0 || c.underAttackBy2 > 0) {
				c.sendMessage("You cannot change skills while in combat!");
				return;
			}
			if(c.inWild()) {
				c.sendMessage("You cannot change skills in the wilderness!");
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				c.sendMessage("You cannot spawn items in the duel arena!");
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 0;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 29031);
				c.getPA().requestUpdates();
				c.attackSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.defenceSkill) {
			if(c.underAttackBy > 0 || c.underAttackBy2 > 0) {
				c.sendMessage("You cannot change skills while in combat!");
				return;
			}
			if(c.inWild()) {
				c.sendMessage("You cannot change skills in the wilderness!");
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				c.sendMessage("You cannot spawn items in the duel arena!");
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 1;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getCombat().resetPrayers();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 29031);
				c.getPA().requestUpdates();
				c.defenceSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.strengthSkill) {
			if(c.inWild()) {
				c.sendMessage("You cannot change skills in the wilderness!");
				return;
			}
			if(c.underAttackBy > 0 || c.underAttackBy2 > 0) {
				c.sendMessage("You cannot change skills while in combat!");
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				c.sendMessage("You cannot spawn items in the duel arena!");
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 2;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 29031);
				c.getPA().requestUpdates();
				c.strengthSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.healthSkill) {
			if(c.inWild()) {
				c.sendMessage("You cannot change skills in the wilderness!");
				return;
			}
			if(c.underAttackBy > 0 || c.underAttackBy2 > 0) {
				c.sendMessage("You cannot change skills while in combat!");
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				c.sendMessage("You cannot spawn items in the duel arena!");
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 3;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 29031);
				c.getPA().requestUpdates();
				c.healthSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.rangeSkill) {
			if(c.inWild()) {
				c.sendMessage("You cannot change skills in the wilderness!");
				return;
			}
			if(c.underAttackBy > 0 || c.underAttackBy2 > 0) {
				c.sendMessage("You cannot change skills while in combat!");
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				c.sendMessage("You cannot spawn items in the duel arena!");
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 4;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 29031);
				c.getPA().requestUpdates();
				c.rangeSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.prayerSkill) {
			if(c.inWild()) {
				c.sendMessage("You cannot change skills in the wilderness!");
				return;
			}
			if(c.underAttackBy > 0 || c.underAttackBy2 > 0) {
				c.sendMessage("You cannot change skills while in combat!");
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				c.sendMessage("You cannot spawn items in the duel arena!");
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				c.getCombat().resetPrayers();
				int skill = 5;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 29031);
				c.getPA().requestUpdates();
				c.prayerSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.mageSkill) {
			if(c.inWild()) {
				c.sendMessage("You cannot change skills in the wilderness!");
				return;
			}
			if(c.underAttackBy > 0 || c.underAttackBy2 > 0) {
				c.sendMessage("You cannot change skills while in combat!");
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				c.sendMessage("You cannot spawn items in the duel arena!");
				return;
			}
			for (int j = 0; j < c.playerEquipment.length; j++) {
				if (c.playerEquipment[j] > 0) {
					c.sendMessage("Please remove all your equipment before setting your levels.");
					return;
				}
			}
			try {
				int skill = 6;
				int level = Xamount;
				if (level > 99)
					level = 99;
				else if (level < 0)
					level = 1;
				c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
				c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
				c.getPA().refreshSkill(skill);
				c.combatLevel = c.calculateCombatLevel();
				c.getPA().sendFrame126("Combat Level: " + c.combatLevel + "", 29031);
				c.getPA().requestUpdates();
				c.mageSkill = false;
			} catch (Exception e) {
			}
		}
		if (c.getPrayer().getAltarBone().isPresent()) {
			c.getPrayer().alter(Xamount);
			return;
		}
	}
}
