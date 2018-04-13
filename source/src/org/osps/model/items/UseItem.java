package org.osps.model.items;

import java.util.Optional;

import org.osps.Config;
import org.osps.clip.ObjectDef;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.content.CrystalChest;
import org.osps.model.content.DiceNpc;
import org.osps.model.minigames.BlastMine.BlastMine;
import org.osps.model.minigames.gamble.GambleItems;
import org.osps.model.minigames.warriors_guild.AnimatedArmour;
import org.osps.model.npcs.PetHandler;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerCannon;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.combat.Degrade;
import org.osps.model.players.combat.Hitmark;
import org.osps.model.players.skills.Firemaking;
import org.osps.model.players.skills.Skill;
import org.osps.model.players.skills.crafting.GemCutting;
import org.osps.model.players.skills.crafting.JewelryMaking;
import org.osps.model.players.skills.crafting.LeatherMaking;
import org.osps.model.players.skills.fletching.BowStringing;
import org.osps.model.players.skills.fletching.Fletching;
import org.osps.model.players.skills.fletching.Arrow.Arrow;
import org.osps.model.players.skills.herblore.PotionMixing;
import org.osps.model.players.skills.prayer.Bone;
import org.osps.model.players.skills.prayer.Prayer;
import org.osps.util.Misc;

/**
 * @author Sanity
 * @author Ryan
 * @author Lmctruck30 Revised by Shawn Notes by Shawn
 */

public class UseItem {

	/**
	 * Using items on an object.
	 * 
	 * @param c
	 * @param objectID
	 * @param objectX
	 * @param objectY
	 * @param itemId
	 */
	public static void ItemonObject(Player c, int objectID, int objectX, int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		c.getFarming().patchObjectInteraction(objectID, itemId, objectX, objectY);
		if (itemId == 13066) {
			ObjectDef def = ObjectDef.getObjectDef(objectID);
			if (def.name.toLowerCase().contains("bank")) {
				if (c.getItems().freeSlots() < 3) {
					c.sendMessage("You need at least three slots to do this.");
					return;
				}
				c.getItems().deleteItem2(itemId, 1);
				c.getItems().addItem(2441, 1);
				c.getItems().addItem(2437, 1);
				c.getItems().addItem(2443, 1);
				c.sendMessage("You break the combat potion set into three individual potions.");
			}
		}

		if (c.playerCannon != null && PlayerCannon.CannonPart.isObjCannon(objectID)) {
			if (c.playerCannon.addItemToCannon(itemId, objectID, objectX, objectY)) {
				PlayerHandler.updateCannon(c.playerCannon);
			}
		}

		if (objectID == 5249 && itemId == 3728) {
			c.sendMessage("You heat the water up and it thaws.");
			c.getItems().deleteItem(3728, 1);
			c.getItems().addItem(1929, 1);
			c.animation(897);
		}

		if (((objectID == 4465 || objectID == 4467) && itemId == 1523) || (objectID == 4912 && itemId == 954)) {
			c.getActions().firstClickObject(objectID, objectX, objectY);
			return;
		}
		c.ObjectClick(c, objectID, objectX, objectY, itemId);
		switch (objectID) {
		case 28580:
		case 28579:
			BlastMine.blowAway(c);
			break;
		case 28592:
			BlastMine.redeem(c);
			break;
		case 23955:
			AnimatedArmour.itemOnAnimator(c, itemId);
			break;
		case 2031:
		case 2097:
			c.getSmithingInt().showSmithInterface(itemId);
			break;
		case 172:
			CrystalChest.searchChest(c);
			break;
		case 409:
			Optional<Bone> bone = Prayer.isOperableBone(itemId);
			if (bone.isPresent()) {
				c.getPrayer().setAltarBone(bone);
				c.getOutStream().createFrame(27);
				return;
			}
			break;
		/*
		 * case 2728: case 12269: c.getCooking().itemOnObject(itemId); break;
		 */
		default:
			if (c.getRights().isDeveloper() && Config.SERVER_DEBUG)
				Misc.println("Player At Object id: " + objectID + " with Item id: " + itemId);
			break;
		}

	}

	public static boolean combines(int itemA, int itemB, int combinationA, int combinationB) {
		return (itemA == combinationA && itemB == combinationB) || (itemA == combinationB && itemB == combinationA);
	}

	/**
	 * Using items on items.
	 * 
	 * @param c
	 * @param itemUsed
	 * @param useWith
	 */
	public static void ItemonItem(final Player c, final int itemUsed, final int useWith, final int itemUsedSlot,
			final int usedWithSlot) {
		if (itemUsed == -1 || useWith == -1)
			return;
		GameItem gameItemUsed = new GameItem(itemUsed, c.playerItemsN[itemUsedSlot], itemUsedSlot);
		GameItem gameItemUsedWith = new GameItem(useWith, c.playerItemsN[itemUsedSlot], usedWithSlot);
		Fletching.resetFletching(c);
		// Object runes;
		BlastMine.makeDynamite(c);
		//RunePouch.runeOnPouch(c, itemUsed, useWith);
		Arrow.initialize(c, itemUsed, useWith);
		c.getPA().resetVariables();
		Optional<ItemCombinations> itemCombination = ItemCombinations.isCombination(new GameItem(itemUsed),
				new GameItem(useWith));
		if (itemUsed == 1777 || useWith == 1777) {
			BowStringing.stringBow(c, itemUsed, useWith);
		}
		if (itemCombination.isPresent()) {
			ItemCombination combination = itemCombination.get().getItemCombination();
			if (combination.isRequirable() && !combination.hasRequirements(c)) {
				combination.insufficientRequirements(c);
				return;
			}
			if (combination.isCombinable(c)) {
				c.setCurrentCombination(Optional.of(combination));
				c.dialogueAction = -1;
				c.nextChat = -1;
				combination.showDialogue(c);
			} else {
				c.getDH().sendStatement("You don't have all of the items required for this combination.");
				return;
			}
			return;
		}
		if ((itemUsed == 4278 && useWith == 11088) || (itemUsed == 11088 && useWith == 4278)) {
			int amount = c.getItems().getItemAmount(4278);
			if (amount == 0)
				return;
			if (amount > 16000)
				amount = 16000;
			if (c.getEther() >= 16000) {
				c.sendMessage("You're already charged with 16,000 ether.");
				return;
			}
			if (c.getEther() + amount > 16000)
				amount = 16000 - c.getEther();
			c.getItems().deleteItem(4278, amount);
			c.setEther(c.getEther() + amount);
			c.sendMessage("You're now charged with " + c.getEther() + " ether.");
		}
		if (itemUsed == 19592 && useWith == 19589 || itemUsed == 19589 && useWith == 19592) {
			if (c.playerLevel[9] > 72 && c.getItems().playerHasItem(19589) && c.getItems().playerHasItem(19592)) {
				c.getItems().deleteItem(19592, 1);
				c.getItems().deleteItem(19589, 1);
				c.getItems().addItem(19598, 1);
			} else {
				if (c.playerLevel[9] < 72) {
					c.sendMessage("Creating the Heavy Ballista requires 72 fletching!");
				} else if (!c.getItems().playerHasItem(19589) || !c.getItems().playerHasItem(19592)) {
					c.sendMessage("You don't have the required items to make this.");

				}
			}
		}

		if (itemUsed == 19601 && useWith == 19598 || itemUsed == 19598 && useWith == 19601) {
			if (c.playerLevel[9] >= 72 && c.getItems().playerHasItem(19601) && c.getItems().playerHasItem(19598)) {
				c.getItems().deleteItem(19598, 1);
				c.getItems().deleteItem(19601, 1);
				c.getItems().addItem(19607, 1);
				
			} else {
				if (c.playerLevel[9] < 72) {
					c.sendMessage("Creating the Heavy Ballista requires 72 fletching!");
				} else if (!c.getItems().playerHasItem(19589) || !c.getItems().playerHasItem(19592)) {
					c.sendMessage("You don't have the required items to make this.");

				}
			}
		}

		if (itemUsed == 19607 && useWith == 19610 || itemUsed == 19610 && useWith == 19607) {
			if (c.playerLevel[9] > 72 && c.getItems().playerHasItem(19610) && c.getItems().playerHasItem(19607)) {
				c.getItems().deleteItem(19610, 1);
				c.getItems().deleteItem(19607, 1);
				c.getItems().addItem(19481, 1);
			} else {
				if (c.playerLevel[9] < 72) {
					c.sendMessage("Creating the Heavy Ballista requires 72 fletching!");
				} else if (!c.getItems().playerHasItem(19589) || !c.getItems().playerHasItem(19592)) {
					c.sendMessage("You don't have the required items to make this.");

				}
			}
		}

		if ((itemUsed >= 19570 && itemUsed <= 19584) && (useWith >= 19570 && useWith <= 19584)) {
			Fletching.makeJavelins(c, itemUsed, useWith);
			return;
		}

		if (combines(itemUsed, useWith, 19529, 6571)) {
			c.getCrafting().createZenyte();
			return;
		}

		if (c.hungerGames) {
			if (itemUsed == 1549 || useWith == 1549) {
				c.sendMessage("That just makes no sense.");
			} else if (itemUsed == 1798 || useWith == 1798) {
				c.sendMessage("This silver cup will probably be of more use later.");
			} else if (itemUsed == 1469 || useWith == 1469) {
				c.sendMessage("You cut yourself on the broken glass!");
				c.appendDamage(8, Hitmark.HIT);
				c.forceChat("Ouch!");
				c.getItems().deleteItem(1469, 1);
			} else if ((itemUsed == 6018 && useWith == 1919) || (useWith == 6018 && useWith == 1919)) {
				if (c.getItems().playerHasItem(1929)) {
					c.getItems().deleteItem(1929, 1);
					c.getItems().deleteItem(6018, 1);
					c.getItems().deleteItem(1919, 1);
					c.getItems().addItem(1907, 1);
					c.sendMessage("You make a poisoned beverage.");
				} else {
					c.sendMessage("I need some water to mix this with.");
				}
			} else if ((itemUsed == 1929 && useWith == 1919) || (useWith == 1929 && useWith == 1919)) {
				if (c.getItems().playerHasItem(6018)) {
					c.getItems().deleteItem(1929, 1);
					c.getItems().deleteItem(6018, 1);
					c.getItems().deleteItem(1919, 1);
					c.getItems().addItem(1907, 1);
					c.sendMessage("You make a poisoned beverage.");
				} else {
					c.sendMessage("I need something poisonous to add to this.");
				}
			}
		}

		if (Firemaking.playerLogs(c, itemUsed, useWith)) {
			Firemaking.grabData(c, itemUsed, useWith);
		}
		/*
		 * if(itemUsed >= 1 && useWith == 11941) { LootingBag.draw(c); }
		 */

		if (useWith == 11941) {
			if (ItemDefinition.forId(itemUsed).isStackable() || ItemDefinition.forId(itemUsed).isNoted()) {
				c.getLoot().addItemToLootbag(itemUsed, c.getItems().getItemAmount(itemUsed));
			} else {
				c.getLoot().addItemToLootbag(itemUsed, 1);
			}
		}

		if (itemUsed == 11941) {
			if (ItemDefinition.forId(useWith).isStackable() || ItemDefinition.forId(useWith).isNoted()) {
				c.getLoot().addItemToLootbag(useWith, c.getItems().getItemAmount(useWith));
			} else {
				c.getLoot().addItemToLootbag(useWith, 1);
			}
		}

		if (c.hungerGames) {
			if ((itemUsed == 9007 && useWith == 9008) || (itemUsed == 9008 && useWith == 9007)) {
				c.getItems().deleteItem(9007, 1);
				c.getItems().deleteItem(9008, 1);
				c.getItems().addItem(2944, 1);
				c.sendMessage("You combine the keys... and feel a strange force.");
				c.getDH().sendDialogues(832, 5721);
			}
		}

		if (useWith == 12791) {
			c.getRunePouch().addItemToRunePouch(itemUsed, c.getItems().getItemAmount(itemUsed));
		}

		if (itemUsed == 12791) {
			c.getRunePouch().addItemToRunePouch(useWith, c.getItems().getItemAmount(useWith));
		}

		if (itemUsed == 3150 && useWith == 3157 || itemUsed == 3157 && useWith == 3150) {
			if (c.getItems().playerHasItem(3150) && c.getItems().playerHasItem(3157)) {
				c.getItems().deleteItem2(itemUsed, 1);
				c.getItems().deleteItem2(useWith, 1);
				c.getItems().addItem(3159, 1);
			}
		}
		if (itemUsed == 12932 && useWith == 11907 || itemUsed == 11907 && useWith == 12932) {
			if (c.playerLevel[Skill.CRAFTING.getId()] < 59) {
				c.sendMessage("You need 59 crafting to do this.");
				return;
			}
			if (!c.getItems().playerHasItem(1755)) {
				c.sendMessage("You need a chisel to do this.");
				return;
			}
			if (c.getTridentCharge() > 0) {
				c.sendMessage("You cannot do this whilst your trident has charge.");
				return;
			}
			c.getItems().deleteItem2(itemUsed, 1);
			c.getItems().deleteItem2(useWith, 1);
			c.getItems().addItem(12899, 1);
			c.sendMessage("You attach the magic fang to the trident and create a trident of the swamp.");
			return;
		}
		if (itemUsed == 5733 || useWith == 5733) {
			c.sendMessage("Whee! " + c.getItems().getItemName(itemUsed == 5733 ? useWith : itemUsed) + " all gone!");
			c.getItems().deleteItem(itemUsed == 5733 ? useWith : itemUsed, 1);
		}
		if (itemUsed == 12932 && useWith == 11791 || itemUsed == 11791 && useWith == 12932) {
			if (c.playerLevel[Skill.CRAFTING.getId()] < 59) {
				c.sendMessage("You must have a Crafting level of 59 to do this.");
				return;
			}
			if (!c.getItems().playerHasItem(1755)) {
				c.sendMessage("You need a chisel to do this.");
				return;
			}
			c.getItems().deleteItem2(itemUsed, 1);
			c.getItems().deleteItem2(useWith, 1);
			c.getItems().addItem(12904, 1);
			c.sendMessage("You attach the magic fang to the staff of the dead and create a toxic staff of the dead.");
			return;
		}
		if (((itemUsed == 554 || itemUsed == 560 || itemUsed == 562) && (useWith == 12899 || useWith == 11907))
				|| ((useWith == 554 || useWith == 560 || useWith == 562) && (itemUsed == 12899 || itemUsed == 11907))) {
			int trident;
			if (itemUsed == 11907 || itemUsed == 12899) {
				trident = itemUsed;
			} else if (useWith == 11907 || useWith == 12899) {
				trident = useWith;
			} else {
				return;
			}
			if (!c.getItems().playerHasItem(995, 1000000) && trident == 11907) {
				c.sendMessage("You need at least 1M coins to add charge.");
				return;
			}
			if (!c.getItems().playerHasItem(12934, 2500) && trident == 12899) {
				c.sendMessage("You need 2500 zulrah scales to charge this.");
				return;
			}
			if (!c.getItems().playerHasItem(554, 1250)) {
				c.sendMessage("You need at least 1250 fire runes to add charge.");
				return;
			}
			if (!c.getItems().playerHasItem(560, 500)) {
				c.sendMessage("You need at least 500 death rune to add charge.");
				return;
			}
			if (!c.getItems().playerHasItem(562, 1000)) {
				c.sendMessage("You need at least 1000 chaos rune to add charge.");
				return;
			}
			if (c.getTridentCharge() >= 1000 && trident == 11907) {
				c.sendMessage("Your trident already has 1000 charge.");
				return;
			}
			if (c.getToxicTridentCharge() >= 1000 && trident == 12899) {
				c.sendMessage("Your trident already has 1000 charge.");
				return;
			}
			c.getItems().deleteItem2(554, 1250);
			c.getItems().deleteItem2(560, 1000);
			c.getItems().deleteItem2(562, 500);
			if (trident == 11907) {
				c.getItems().deleteItem2(995, 1000000);
				c.setTridentCharge(c.getTridentCharge() + 2500);
				c.animation(7137);
				c.gfx100(1250);

			} else {
				c.getItems().deleteItem2(12934, 2500);
				c.setToxicTridentCharge(c.getToxicTridentCharge() + 2500);
			}
			return;
		}
		if (itemUsed == 12927 && useWith == 1755 || itemUsed == 1755 && useWith == 12927) {
			int visage = itemUsed == 12927 ? itemUsed : useWith;
			if (c.playerLevel[Skill.CRAFTING.getId()] < 52) {
				c.sendMessage("You need a crafting level of 52 to do this.");
				return;
			}
			c.getItems().deleteItem2(visage, 1);
			c.getItems().addItem(12929, 1);
			c.sendMessage("You craft the serpentine visage into a serpentine helm (empty).");
			c.sendMessage("Charge the helm with 500 scales.");
			return;
		}
		if (itemUsed == 12929 && useWith == 12934 || itemUsed == 12934 && useWith == 12929) {
			if (!c.getItems().playerHasItem(12934, 500)) {
				c.sendMessage("You need 500 scales to do this.");
				return;
			}
			if (c.getSerpentineHelmCharge() > 0) {
				c.sendMessage("You must uncharge your current helm to re-charge.");
				return;
			}
			int amount = c.getItems().getItemAmount(12934);
			if (amount > 500) {
				amount = 500;
				c.sendMessage("The helm only required 500 zulrah scales to fully charge.");
			}
			c.getItems().deleteItem2(12934, amount);
			c.getItems().deleteItem2(12929, 1);
			c.getItems().addItem(12931, 1);
			c.setSerpentineHelmCharge(amount);
			c.sendMessage("You charge the serpentine helm for 500 zulrah scales.");
			return;
		}
		if (itemUsed == 13233 && useWith == 6739) {
			if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need atleast @blu@3 @bla@free slots.");
				return;
			}
			c.getItems().deleteItem(6739, 1);
			c.getItems().deleteItem(13233, 1);
			c.getItems().addItem(13241, 1);
			c.sendMessage("You combine your @blu@Dragon Axe @bla@ and @blu@Smouldering Stone@bla@.");
		}
		if (itemUsed == 13233 && useWith == 11920) {
			if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need atleast @blu@3 @bla@free slots.");
				return;
			}
			c.getItems().deleteItem(11920, 1);
			c.getItems().deleteItem(13233, 1);
			c.getItems().addItem(13243, 1);
			c.sendMessage("You combine your @blu@Dragon Pickaxe @bla@ and @blu@Smouldering Stone@bla@.");
		}
		if (itemUsed == 12902 && useWith == 12934 || itemUsed == 12934 && useWith == 12902) {
			if (!c.getItems().playerHasItem(12934, 500)) {
				c.sendMessage("You need 500 scales to do this.");
				return;
			}
			if (c.staffOfDeadCharge > 0) {
				c.sendMessage("You must uncharge your current staff to re-charge.");
				return;
			}
			int amount = c.getItems().getItemAmount(12934);
			if (amount > 500) {
				amount = 500;
				c.sendMessage("The staff only required 500 zulrah scales to fully charge.");
			}
			c.getItems().deleteItem2(12934, amount);
			c.getItems().deleteItem2(12902, 1);
			c.getItems().addItem(12904, 1);
			c.staffOfDeadCharge = amount;
			c.animation(1720);
			c.sendMessage("You charge the toxic staff of the dead for 500 zulrah scales.");
			return;
		}
		if (itemUsed == 12924 || useWith == 12924) {
			int ammo = itemUsed == 12924 ? useWith : itemUsed;
			ItemDefinition definition = ItemDefinition.forId(ammo);
			int amount = c.getItems().getItemAmount(ammo);
			if (ammo == 12934) {
				c.sendMessage("Select a dart to store and have the equivellent amount of scales.");
				return;
			}
			if (definition == null || !definition.getName().toLowerCase().contains("dart")) {
				c.sendMessage("That item cannot be equipped with the blowpipe.");
				return;
			}
			if (c.getToxicBlowpipeAmmo() > 0) {
				c.sendMessage("The blowpipe already has ammo, you need to unload it first.");
				return;
			}
			if (amount < 500) {
				c.sendMessage("You need 500 of this item to store it in the pipe.");
				return;
			}
			if (!c.getItems().playerHasItem(12934, amount)) {
				c.sendMessage("You need at least " + amount + " scales in combination with the " + definition.getName()
						+ " to charge this.");
				return;
			}
			if (!c.getItems().playerHasItem(12924)) {
				c.sendMessage("You need a toxic blowpipe (empty) to do this.");
				return;
			}
			if (amount > 16383) {
				c.sendMessage("The blowpipe can only store 16,383 charges at any given time.");
				amount = 16383;
			}
			c.getItems().deleteItem2(12924, 1);
			c.getItems().addItem(12926, 1);
			c.getItems().deleteItem2(ammo, amount);
			c.getItems().deleteItem2(12934, amount);
			c.setToxicBlowpipeCharge(amount);
			c.setToxicBlowpipeAmmo(ammo);
			c.setToxicBlowpipeAmmoAmount(amount);
			c.sendMessage("You store " + amount + " " + definition.getName()
					+ " into the blowpipe and charge it with scales.");
			return;
		}
		if (itemUsed == 12922 && useWith == 1755 || itemUsed == 1755 && useWith == 12922) {
			if (c.playerLevel[Skill.FLETCHING.getId()] >= 53) {
				c.getItems().deleteItem2(12922, 1);
				c.getItems().addItem(12924, 1);
				c.getPA().addSkillXP(10000, Skill.FLETCHING.getId());
				c.sendMessage("You fletch the fang into a toxic blowpipe.");
			} else {
				c.sendMessage("You need a fletching level of 53 to do this.");
			}
			return;
		}
		if (itemUsed == 1733 || useWith == 1733) {
			LeatherMaking.craftLeatherDialogue(c, itemUsed, useWith);

		}
		if (itemUsed == 1759 || useWith == 1759) {
			JewelryMaking.stringAmulet(c, itemUsed, useWith);
		}
		if (itemUsed == 1755 || useWith == 1755) {
			GemCutting.cutGem(c, itemUsed, useWith);
		}
		if ((useWith == 1511 || itemUsed == 1511) && (useWith == 946 || itemUsed == 946)) {
			Fletching.normal(c, itemUsed, useWith);
		} else if (useWith == 946 || itemUsed == 946) {
			Fletching.others(c, itemUsed, useWith);
		}
		if (itemUsed == 12526 && useWith == 6585 || itemUsed == 6585 && useWith == 12526) {
			c.getDH().sendDialogues(580, -1);
		}
		if (itemUsed == 11235 || useWith == 11235) {
			if (itemUsed == 11235 && useWith == 12757 || useWith == 11235 && itemUsed == 12757) {
				c.getDH().sendDialogues(566, 315);
			} else if (itemUsed == 11235 && useWith == 12759 || useWith == 11235 && itemUsed == 12759) {
				c.getDH().sendDialogues(569, 315);
			} else if (itemUsed == 11235 && useWith == 12761 || useWith == 11235 && itemUsed == 12761) {
				c.getDH().sendDialogues(572, 315);
			} else if (itemUsed == 11235 && useWith == 12763 || useWith == 11235 && itemUsed == 12763) {
				c.getDH().sendDialogues(575, 315);
			}
		}
		if (itemUsed == 12804 && useWith == 11838 || itemUsed == 11838 && useWith == 12804) {
			// c.getDH().sendDialogues(550, -1);
		}
		if (itemUsed == 11926 && useWith == 12802 || itemUsed == 12802 && useWith == 11926) {
			if (c.getItems().playerHasItem(12929) && c.getItems().playerHasItem(12802)) {
				c.getItems().deleteItem2(11926, 1);
				c.getItems().deleteItem2(12802, 1);
				c.getItems().addItem(12807, 1);
				c.getDH().sendStatement("You combined your Upgrade kit with a Odium Ward.");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 11924 && useWith == 12802 || itemUsed == 12802 && useWith == 11924) {
			if (c.getItems().playerHasItem(12929) && c.getItems().playerHasItem(12802)) {
				c.getItems().deleteItem2(11924, 1);
				c.getItems().deleteItem2(12802, 1);
				c.getItems().addItem(12806, 1);
				c.getDH().sendStatement("You combined your Upgrade kit with a Malediction Ward.");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 4153 && useWith == 12849 || itemUsed == 12849 && useWith == 4153) {
			c.getDH().sendDialogues(563, 315);
		}
		if (itemUsed == 12929 && useWith == 13201 || itemUsed == 13201 && useWith == 12929) {
			if (c.getItems().playerHasItem(12929) && c.getItems().playerHasItem(13201)) {
				c.getItems().deleteItem2(12929, 1);
				c.getItems().deleteItem2(13201, 1);
				c.getItems().addItem(13198, 1);
				c.getDH().sendStatement("You combined your Mutagen with a Serpentine helm (uncharged).");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 12929 && useWith == 13200 || itemUsed == 13200 && useWith == 12929) {
			if (c.getItems().playerHasItem(12929) && c.getItems().playerHasItem(13201)) {
				c.getItems().deleteItem2(12929, 1);
				c.getItems().deleteItem2(13200, 1);
				c.getItems().addItem(13196, 1);
				c.getDH().sendStatement("You combined your Mutagen with a Serpentine helm (uncharged).");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 13280 && useWith == 6570 || itemUsed == 6570 && useWith == 13280) {
			if (c.getItems().playerHasItem(13280) && c.getItems().playerHasItem(6570)) {
				c.getItems().deleteItem2(13280, 1);
				c.getItems().deleteItem2(6570, 1);
				c.getItems().addItem(13329, 1);
				c.getItems().addItem(13330, 1);
				c.getDH().sendStatement("You combined your max cape with a fire cape.");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 13280 && useWith == 2412 || itemUsed == 2412 && useWith == 13280) {
			if (c.getItems().playerHasItem(13280) && c.getItems().playerHasItem(2412)) {
				c.getItems().deleteItem2(13280, 1);
				c.getItems().deleteItem2(2412, 1);
				c.getItems().addItem(13331, 1);
				c.getItems().addItem(13332, 1);
				c.getDH().sendStatement("You combined your max cape with a saradomin cape.");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 13227 && useWith == 6920 || itemUsed == 6920 && useWith == 13227) {
			c.getPA().makeEternalBoots(c);
		}
		if (itemUsed == 13229 && useWith == 2577 || itemUsed == 2577 && useWith == 13229) {
			c.getPA().makePegasianBoots(c);
		}
		if (itemUsed == 13231 && useWith == 11840 || itemUsed == 11840 && useWith == 13231) {
			c.getPA().makePrimordialBoots(c);
		}
		if (itemUsed == 13280 && useWith == 2414 || itemUsed == 2414 && useWith == 13280) {
			if (c.getItems().playerHasItem(13280) && c.getItems().playerHasItem(2414)) {
				c.getItems().deleteItem2(13280, 1);
				c.getItems().deleteItem2(2414, 1);
				c.getItems().addItem(13333, 1);
				c.getItems().addItem(13334, 1);
				c.getDH().sendStatement("You combined your max cape with a zamorak cape.");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 13280 && useWith == 2413 || itemUsed == 2413 && useWith == 13280) {
			if (c.getItems().playerHasItem(13280) && c.getItems().playerHasItem(2413)) {
				c.getItems().deleteItem2(13280, 1);
				c.getItems().deleteItem2(2413, 1);
				c.getItems().addItem(13335, 1);
				c.getItems().addItem(13336, 1);
				c.getDH().sendStatement("You combined your max cape with a guthix cape.");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 13280 && useWith == 10499 || itemUsed == 10499 && useWith == 13280) {
			if (c.getItems().playerHasItem(13280) && c.getItems().playerHasItem(10499)) {
				c.getItems().deleteItem2(13280, 1);
				c.getItems().deleteItem2(10499, 1);
				c.getItems().addItem(13337, 1);
				c.getItems().addItem(13338, 1);
				c.getDH().sendStatement("You combined your max cape with a avas accumilator.");
				c.nextChat = -1;
			}
		}
		if (itemUsed == 12786 && useWith == 861 || useWith == 12786 && itemUsed == 861) {
			if (c.getItems().playerHasItem(12786) && c.getItems().playerHasItem(861)) {
				c.getItems().deleteItem2(12786, 1);
				c.getItems().deleteItem2(861, 1);
				c.getItems().addItem(12788, 1);
				c.getDH().sendStatement("You have imbued your Magic Shortbow.");
				c.nextChat = -1;
			}
		}
		if (PotionMixing.get().isPotion(gameItemUsed) && PotionMixing.get().isPotion(gameItemUsedWith)) {
			if (PotionMixing.get().matches(gameItemUsed, gameItemUsedWith)) {
				PotionMixing.get().mix(c, gameItemUsed, gameItemUsedWith);
			} else {
				c.sendMessage("You cannot combine two potions of different types.");
			}
			return;
		}
		if (itemUsed == 227 || useWith == 227) {
			int primary = itemUsed == 227 ? useWith : itemUsed;
			c.getHerblore().mix(primary);
			return;
		}
		/*
		 * Start of unsystematic code for cutting bolt tips and fletching the
		 * actual bolts
		 */
		if (itemUsed == 9142 && useWith == 9190 || itemUsed == 9190 && useWith == 9142) {
			if (c.playerLevel[c.playerFletching] >= 58) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9241, boltsMade);
				c.getPA().addSkillXP(boltsMade * 6 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 58 to fletch this item.");
			}
		}
		if (itemUsed == 9143 && useWith == 9191 || itemUsed == 9191 && useWith == 9143) {
			if (c.playerLevel[c.playerFletching] >= 63) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9242, boltsMade);
				c.getPA().addSkillXP(boltsMade * 7 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 63 to fletch this item.");
			}
		}
		if (itemUsed == 9143 && useWith == 9192 || itemUsed == 9192 && useWith == 9143) {
			if (c.playerLevel[c.playerFletching] >= 65) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9243, boltsMade);
				c.getPA().addSkillXP(boltsMade * 7 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 65 to fletch this item.");
			}
		}
		if (itemUsed == 9144 && useWith == 9193 || itemUsed == 9193 && useWith == 9144) {
			if (c.playerLevel[c.playerFletching] >= 71) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9244, boltsMade);
				c.getPA().addSkillXP(boltsMade * 10 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 71 to fletch this item.");
			}
		}
		if (itemUsed == 9144 && useWith == 9194 || itemUsed == 9194 && useWith == 9144) {
			if (c.playerLevel[c.playerFletching] >= 58) {
				int boltsMade = c.getItems().getItemAmount(itemUsed) > c.getItems().getItemAmount(useWith)
						? c.getItems().getItemAmount(useWith) : c.getItems().getItemAmount(itemUsed);
				c.getItems().deleteItem(useWith, c.getItems().getItemSlot(useWith), boltsMade);
				c.getItems().deleteItem(itemUsed, c.getItems().getItemSlot(itemUsed), boltsMade);
				c.getItems().addItem(9245, boltsMade);
				c.getPA().addSkillXP(boltsMade * 13 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 58 to fletch this item.");
			}
		}
		if (itemUsed == 1601 && useWith == 1755 || itemUsed == 1755 && useWith == 1601) {
			if (c.playerLevel[c.playerFletching] >= 63) {
				c.getItems().deleteItem(1601, c.getItems().getItemSlot(1601), 1);
				c.getItems().addItem(9192, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 63 to fletch this item.");
			}
		}
		if (itemUsed == 1607 && useWith == 1755 || itemUsed == 1755 && useWith == 1607) {
			if (c.playerLevel[c.playerFletching] >= 65) {
				c.getItems().deleteItem(1607, c.getItems().getItemSlot(1607), 1);
				c.getItems().addItem(9189, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 65 to fletch this item.");
			}
		}
		if (itemUsed == 1605 && useWith == 1755 || itemUsed == 1755 && useWith == 1605) {
			if (c.playerLevel[c.playerFletching] >= 71) {
				c.getItems().deleteItem(1605, c.getItems().getItemSlot(1605), 1);
				c.getItems().addItem(9190, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 71 to fletch this item.");
			}
		}
		if (itemUsed == 1603 && useWith == 1755 || itemUsed == 1755 && useWith == 1603) {
			if (c.playerLevel[c.playerFletching] >= 73) {
				c.getItems().deleteItem(1603, c.getItems().getItemSlot(1603), 1);
				c.getItems().addItem(9191, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 73 to fletch this item.");
			}
		}
		if (itemUsed == 1615 && useWith == 1755 || itemUsed == 1755 && useWith == 1615) {
			if (c.playerLevel[c.playerFletching] >= 73) {
				c.getItems().deleteItem(1615, c.getItems().getItemSlot(1615), 1);
				c.getItems().addItem(9193, 15);
				c.getPA().addSkillXP(8 * Config.FLETCHING_EXPERIENCE, c.playerFletching);
			} else {
				c.sendMessage("You need a fletching level of 73 to fletch this item.");
			}
		}
		/*
		 * End of unsystematic code for cutting bolt tips and fletching the
		 * actual bolts
		 */

		if ((itemUsed == 1540 && useWith == 11286) || (itemUsed == 11286 && useWith == 1540)) {
			if (c.playerLevel[Player.playerSmithing] >= 90) {
				c.getItems().deleteItem(1540, c.getItems().getItemSlot(1540), 1);
				c.getItems().deleteItem(11286, c.getItems().getItemSlot(11286), 1);
				c.getItems().addItem(11284, 1);
				c.getDH().sendStatement("You combine the two materials to create a dragonfire shield.");
				c.getPA().addSkillXP(500 * Config.SMITHING_EXPERIENCE, Player.playerSmithing);
			} else {
				c.sendMessage("You need a smithing level of 90 to create a dragonfire shield.");
			}
		}
		if (itemUsed >= 11818 && itemUsed <= 11822 && useWith >= 11818 && useWith <= 11822) {
			if (c.getItems().hasAllShards()) {
				c.getItems().makeBlade();
			} else {
				c.sendMessage("<col=255>You need to have all the shards to combine them into a blade.");
			}
		}
		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366 && useWith == 2368) {
			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368), 1);
			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366), 1);
			c.getItems().addItem(1187, 1);
			c.getDH().sendStatement("You combine the two shield halves to create a full shield.");
		}

		if (c.getItems().isHilt(itemUsed) || c.getItems().isHilt(useWith)) {
			int hilt = c.getItems().isHilt(itemUsed) ? itemUsed : useWith;
			int blade = c.getItems().isHilt(itemUsed) ? useWith : itemUsed;
			if (blade == 11798) {
				c.getItems().makeGodsword(hilt);
			}
		}

		switch (itemUsed) {
		/*
		 * case 1511: case 1521: case 1519: case 1517: case 1515: case 1513:
		 * case 590: c.getFiremaking().checkLogType(itemUsed, useWith); break;
		 */

		default:
			if (c.getRights().isDeveloper() && Config.SERVER_DEBUG)
				Misc.println("Player used Item id: " + itemUsed + " with Item id: " + useWith);
			break;
		}
	}

	/**
	 * Using items on NPCs.
	 * 
	 * @param c
	 * @param itemId
	 * @param npcId
	 * @param slot
	 */
	public static void ItemonNpc(Player c, int itemId, int npcId, int slot) {
		if (npcId == 5892) {
			PetHandler.storeItemJad(c, itemId);
			return;
		}
		if (npcId == 6636) {
			PetHandler.storeItemKBD(c, itemId);
			return;
		}
		if (npcId == 1011) {
			GambleItems.gambleItems(c, itemId);
			return;
		}
		if (npcId == 954) {
			Degrade.repair(c, itemId);
			return;
		}
		if(npcId == 1012) {
			DiceNpc.diceNpc(c, itemId);
		}
		if (npcId == 4018) {
			if (itemId == 8125) {
				c.getDH().sendNpcChat1("Use the item on me not the blood!", 4018, "Blacsmith Smith");
			} else if (!c.getItems().playerHasItem(8125)) {
				c.getDH().sendNpcChat1("I need some blood to upgrade your item!", 4018, "Blacsmith Smith");
			} else {
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						int newItem = Item.getNextTier(itemId);
						if (newItem != itemId && newItem != -1) {
							c.getItems().deleteItem(itemId, slot, 1);
							c.getItems().addItem(newItem, 1);
							c.getItems().deleteItem(8125, 1);
							c.getDH().sendNpcChat1("There we go, fresh and new!", 4018, "Blacsmith Smith");
						} else {
							c.getDH().sendNpcChat2("Sorry there was nothing I could do", "to improve this item...",
									4018, "Blacsmith Smith");
						}
						container.stop();
					}
				}, 1);
			}
		}
		if (c.fishTourneySession != null && c.fishTourneySession.running) {
			c.fishTourneySession.turnInTask(c);
		}
		switch (itemId) {

		default:
			if (c.getRights().isDeveloper())
				Misc.println("Player used Item id: " + itemId + " with Npc id: " + npcId + " With Slot : " + slot);
			break;
		}

	}

}
