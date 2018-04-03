package org.osps.model.players.packets;

import java.util.Objects;
import java.util.Optional;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.items.GameItem;
import org.osps.model.items.ItemCombination;
import org.osps.model.items.ItemCombinations;
import org.osps.model.items.ItemDefinition;
import org.osps.model.minigames.FishingTourney;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.npcs.PetHandler;
import org.osps.model.players.Boundary;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.combat.Hitmark;
import org.osps.model.shops.ShopAssistant;
import org.osps.net.Packet;
/**
 * Drop Item Class
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {

		int itemId = packet.getUnsignedShortA();
		packet.getUnsignedByte();
		packet.getUnsignedByte();
		int slot = packet.getUnsignedShortA();
		c.alchDelay = System.currentTimeMillis();
		if (!c.canUsePackets) {
			return;
		}
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		if (!c.getItems().playerHasItem(itemId)) {
			return;
		}
		for (int item : Config.UNDROPPABLE_ITEMS) {
			if (item == itemId) {
				c.sendMessage("You can not drop this item!");
				return;
			}
		}
		/*
		 * if(c.getRights().isOwner()) { return; }
		 */
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		/*
		 * if (AlphaBeta.IN_BETA_ALPHA && AlphaBeta.RESTRICTED_DROPPING) {
		 * c.sendMessage(
		 * "You cannot drop items, it is restricted during alpha beta.");
		 * return; }
		 */
		if (PetHandler.spawnPet(c, itemId, slot, false)) {
			return;
		}
		if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
			c.sendMessage("You can't drop items inside the arena!");
			return;
		}

		if (itemId == 12926) {
			int ammo = c.getToxicBlowpipeAmmo();
			int amount = c.getToxicBlowpipeAmmoAmount();
			int charge = c.getToxicBlowpipeCharge();
			if (ammo > 0 && amount > 0) {
				c.sendMessage("You must unload before you can uncharge.");
				return;
			}
			if (charge <= 0) {
				c.sendMessage("The toxic blowpipe had no charge, it is emptied.");
				c.getItems().deleteItem2(itemId, 1);
				c.getItems().addItem(12924, 1);
				return;
			}
			if (c.getItems().freeSlots() < 2) {
				c.sendMessage("You need at least two free slots to do this.");
				return;
			}
			c.getItems().deleteItem2(itemId, 1);
			c.getItems().addItem(12924, 1);
			c.getItems().addItem(12934, charge);
			c.setToxicBlowpipeAmmo(0);
			c.setToxicBlowpipeAmmoAmount(0);
			c.setToxicBlowpipeCharge(0);
			return;
		}

		if (itemId == 12931) {
			int charge = c.getSerpentineHelmCharge();
			if (charge <= 0) {
				c.sendMessage("The serpentine helm had no charge, it is emptied.");
				c.getItems().deleteItem2(itemId, 1);
				c.getItems().addItem(12929, 1);
				return;
			}
			if (c.getItems().freeSlots() < 2) {
				c.sendMessage("You need at least two free slots to do this.");
				return;
			}
			c.getItems().deleteItem2(itemId, 1);
			c.getItems().addItem(12929, 1);
			c.getItems().addItem(12934, charge);
			c.setSerpentineHelmCharge(0);
			return;
		}
		if (itemId == 12904) {
			int charge = c.getToxicStaffOfDeadCharge();
			if (charge <= 0) {
				c.getItems().deleteItem2(12904, 1);
				c.getItems().addItem(12902, 1);
				c.sendMessage("The toxic staff of the dead had no charge, it is emptied.");
				return;
			}
			if (c.getItems().freeSlots() < 2) {
				c.sendMessage("You need at least two free inventory slots to do this.");
				return;
			}
			c.getItems().deleteItem2(itemId, 1);
			c.getItems().addItem(12902, 1);
			c.getItems().addItem(12934, charge);
			c.animation(1719);
			c.setToxicStaffOfDeadCharge(0);
			c.staffOfDeadCharge = 0;
			return;
		}

		if (c.underAttackBy > 0) {
			c.sendMessage("You can't drop items during a combat.");
			return;
		}
		if (c.inTrade) {
			c.sendMessage("You can't drop items while trading!");
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

		
		/*boolean destroy = true;
		for (int i : c.getPA().UNSPAWNABLE_ITEMS) {
			if (i == itemId) {
				c.droppedItem = itemId;
				c.getPA().destroyInterface(itemId);
				return;
			}
		}*/
		 

		boolean droppable = true;
		for (int i : Config.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}

		if (slot >= c.playerItems.length || slot < 0 || slot >= c.playerItems.length) {
			return;
		}
		if(c.fishTourneySession != null && c.fishTourneySession.running && !FishingTourney.isValidFish(itemId) && itemId != 4045) {
			c.sendMessage("You can only drop fish and explosive potions during the minigame!");
			return;
		}
		Optional<ItemCombination> revertableItem = ItemCombinations.isRevertable(new GameItem(itemId));
		if (revertableItem.isPresent()) {
			revertableItem.get().revert(c);
			c.nextChat = -1;
			return;
		}
		if (c.playerItemsN[slot] != 0 && itemId != -1 && c.playerItems[slot] == itemId + 1) {
			if (droppable) {
				if (c.underAttackBy > 0) {
					if (ItemDefinition.forId(itemId).getSpecialPrice() > 1000) {
						c.sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}
				org.osps.model.players.PlayerSave.saveGame(c);
				if(itemId == 4045) {
					int explosiveHit = c.getPA().getLevelForXP(c.playerXP[c.playerHitpoints]) / 5;
					c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
					c.forceChat("Ouch!");
					c.appendDamage(explosiveHit, Hitmark.HIT);
					c.getPA().refreshSkill(3);
					return;
				} else if(c.fishTourneySession == null || !c.hungerGames) {
					Server.itemHandler.createGroundItem(c, itemId, c.getX(), c.getY(), c.heightLevel, c.playerItemsN[slot], c.index);
				}
				//c.sendMessage("Your item has dissappeared.");
				c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
				org.osps.model.players.PlayerSave.saveGame(c);
			} else {
				c.sendMessage("You can't drop this item.");
			}
		}
	}
	// }
}
