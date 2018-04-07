package org.osps.model.players.packets;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.Tablets;
import org.osps.model.content.teleport.Teleport;
import org.osps.model.content.teleport.Teleport.TeleportType;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.items.pouch.GemBagDefinition;
import org.osps.model.items.pouch.HerbSackDefinition;
import org.osps.model.minigames.change_name_later.DigEvent;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.multiplayer_session.duel.DuelSessionRules.Rule;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Boundary;
import org.osps.model.players.Packages;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerAssistant;
import org.osps.model.players.PlayerAssistant.PointExchange;
import org.osps.model.players.PlayerCannon;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.TeleportTablets;
import org.osps.model.players.combat.Hitmark;
import org.osps.model.players.skills.Runecrafting.Pouches;
import org.osps.model.players.skills.prayer.Bone;
import org.osps.model.players.skills.prayer.Prayer;
import org.osps.net.Packet;
import org.osps.util.Misc;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements PacketType {
	
	public Player c;

	public ClickItem(Player c) {
		this.c = c;
	}
	public static int flower[] = {2980,2981,2982,2983,2984,2985,2986,2987};
	public int randomflower() {
                return flower[(int)(Math.random()*flower.length)];
        }
	public static int flowerX = 0;
	public static int flowerY = 0;
	public static int flowerTime = -1;
	public static int flowers = 0;
	
	@Override
	public void processPacket(Player c, Packet packet) {
		int frame = packet.getLEShortA(); // use to be
																	// readSignedWordBigEndianA();
		int itemSlot = packet.getShortA(); // use to be
														  // readUnsignedWordA();
		int itemId = packet.getLEShort(); // us to be
																// unsigned.
		if (!c.canUsePackets && (!c.hungerGames || itemId != 10025)) {
			return;
		}
		
		if (itemSlot >= c.playerItems.length || itemSlot < 0) {
			return;
		}
		if (itemId != c.playerItems[itemSlot] - 1) {
			return;
		}
		if (c.getRights().isDeveloper() && Config.SERVER_DEBUG) {
			Misc.println(c.playerName + " - FirstItemOption: " + itemId);
		}
		if (c.isDead || c.playerLevel[3] <= 0) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		for (int i = 0; i < Pouches.pouchData.length; i++) {
			if (itemId == Pouches.pouchData[i][0]) {
				Pouches.fillPouch(c, itemId);
			}
		}
		c.lastClickedItem = itemId;
		c.getHerblore().clean(itemId);
		Tablets.teleport(c, itemId);
		c.getFlowers().plantMithrilSeed(itemId);
		if (c.getFood().isFood(itemId)) {
			c.getFood().eat(itemId, itemSlot);
		} else if (c.getPotions().isPotion(itemId)) {
			c.getPotions().handlePotion(itemId, itemSlot);
		}
		Optional<Bone> bone = Prayer.isOperableBone(itemId);
		if (bone.isPresent()) {
			c.getPrayer().bury(bone.get());
			return;
		}
		for (int i = 0; i < Pouches.pouchData.length; i++) {
			if (itemId == Pouches.pouchData[i][0]) {
				Pouches.fillPouch(c, itemId);
			}
		}
		TeleportTablets.operate(c, itemId);
		if (itemId == 15098) {
			//c.sendMessage("Dicing is currently disabled.");
			//c.sendMessage("Because you guys are addicts.");
			return;
		}
		if (itemId == 11941) {
			c.setSidebarInterface(3, 26700);
			if (c.itemsInLootBag == 0) {
				return;
			} else
				c.getLoot().updateLootbagInterface();
			//c.getItems().resetItemsLoot(26701);
			//c.sendMessage("Worked.");
		}
		if (itemId == HerbSackDefinition.ITEM_ID) {
			c.getHerbSack().fill();
			return;
		}
		if (itemId == GemBagDefinition.ITEM_ID) {
			c.getGemBag().fill();
			return;
		}
		if (itemId == 2379) {
			if (c.getPoisonDamage() > 0 || c.getVenomDamage() > 0) {
				c.sendMessage("You are poisoned or effected by venom, you should heal this first.");
				return;
			}
			if (c.playerLevel[c.playerHitpoints] == 1) {
				c.sendMessage("I better not do that.");
				return;
			}
			c.sendMessage("Wow, the rock exploded in your mouth. That looks like it hurt.");
			c.playerLevel[c.playerHitpoints] = 1;
			c.getPA().refreshSkill(c.playerHitpoints);
			c.getItems().deleteItem2(itemId, 1);
			return;
		}
		if(itemId == 10025) {
			c.sendMessage("You open the box to find supplies!");
			c.getItems().deleteItem(10025, 1);
			c.getItems().addItem(2436, 1);
			c.getItems().addItem(2442, 1);
			c.getItems().addItem(2440, 1);
			c.getItems().addItem(385, 6);

		}
		if (itemId == 11738){
			int[] Secondaries = {222, 236, 226, 224, 1976, 5106, 9737, 232, 2971, 240, 6050, 244, 246, 3139, 248, 260, 12934, 270};
			int randomSecondary = Secondaries[Misc.random(Secondaries.length - 1)];
			int[] grimyHerbs = {200, 202, 204, 206, 208, 210, 212, 214, 216, 218, 220, 1525, 2486, 3050, 3052};
			int randomHerb = grimyHerbs[Misc.random(grimyHerbs.length - 1)];
			int randomHerb2 = grimyHerbs[Misc.random(grimyHerbs.length - 1)];
			if (c.getItems().freeSlots() < 3) {
				c.sendMessage("You need 3 free slots to open this box.");
				return;
			} 
			if(c.getItems().playerHasItem(11738)){
				c.getItems().deleteItem2(11738, 1);
				c.getItems().addItem(randomSecondary, 10);
				c.getItems().addItem(randomHerb, 1);
				c.getItems().addItem(randomHerb2, 1);

			}
			
		}
		if(itemId == 6) {
			if(c.playerCannon == null) {
				if(c.isInJail() || c.inWild() || c.inDuelArena() || c.inDuel || c.inTrade || c.inZeah() || c.inEdgeville()) {
					c.sendMessage("You cannot put a cannon down here!");
				} else if(PlayerHandler.cannonExists(c.absX, c.absY, c.heightLevel)) {
					c.sendMessage("A cannon already exists here!");
				} else {				
					PlayerHandler.addNewCannon(c.playerCannon = new PlayerCannon(c).setUpCannon());
				}
			} else {
				c.sendMessage("You already have a cannon put down!");
			}
		}
		
		if (itemId == 13441) {
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.nonNull(session)) {
					if (session.getRules().contains(Rule.NO_FOOD)) {
						c.sendMessage("Food has been disabled for this duel.");
						return;
					}
				}
			}
			if (System.currentTimeMillis() - c.foodDel >= 1500) {
				if (c.playerLevel[3] <= 99) {
					c.playerLevel[3] += c.getPA().getLevelForXP(c.playerXP[3]) * 0.24;
					c.getItems().deleteItem(13441, itemSlot, 1);
					c.animation(829);
					c.sendMessage("You eat the Anglerfish.");
					c.getPA().refreshSkill(3);
					c.foodDel = System.currentTimeMillis();
				} else {
					c.getItems().deleteItem(13441, itemSlot, 1);
					c.animation(829);
					c.sendMessage("You eat the Anglerfish.");
					c.getPA().refreshSkill(3);
					c.foodDel = System.currentTimeMillis();
				}
			}
		}
		
		if (itemId == 9553) {
			c.getPotions().eatChoc(9553, -1, itemSlot, 1, true);
		}
		if (itemId == 12791) {
			c.getPA().showInterface(41700);
			c.getRunePouch().clearRunePouchInterface();
			c.getRunePouch().updateRunePouchInterface();
		}
		/*if (itemId == 299 && c.seedtimer == 0) {
			flowers = randomflower();
			flowerX += c.absX;
			flowerY += c.absY;
			c.getPA().object(flowers, c.absX, c.absY, 0, 10);
			c.sendMessage("You plant the seed...");
			c.seedtimer += 3;
			c.getItems().deleteItem(299, 1);
			c.getPA().walkTo(-1,0); 
			c.getDH().sendDialogues(10000, -1);   
			} else {
			c.sendMessage("You may only plant a seed every 3 seconds...");
			}*/
		if (itemId == 12846) {
			c.getDH().sendDialogues(578, -1);
		}
		if (itemId == 10594) {
			c.getDH().sendDialogues(760, -1);
		}
		if (itemId == 2996) {
			c.getPA().exchangeItems(PointExchange.PK_POINTS, 2996, 1);	
			c.getPA().loadQuests();
		}
		if (itemId == 4067) {
			c.getPA().exchangeItems(PointExchange.DONATOR_POINTS, 4067, 1);	
			c.getPA().loadQuests();
		}
		if (itemId == 12938) {
			c.getDH().sendDialogues(638, -1);
			return;
		}
		if (itemId == 4155) {
			if (c.getSlayer().hasTask()) {
			c.sendMessage("I currently have @blu@" + c.taskAmount + " " + NPCHandler.getNpcListName(c.slayerTask) + "@bla@ to kill.");
			} else {
			c.sendMessage("I currently do not have a slayer task.");
			}
			if (c.getBossSlayer().hasTask()) {
			c.sendMessage("I currently have @blu@" + c.bossTaskAmount + " " + NPCHandler.getNpcListName(c.bossSlayerTask) + "@bla@ to kill.");
			} else {
			c.sendMessage("I currently do not have a boss task.");
			}
			c.getPA().closeAllWindows();
		}
		if (itemId == 2839) {
			if (c.slayerRecipe == true) {
				c.sendMessage("@blu@You have already learnt this recipe. You have no more use for this scroll.");
				return;
			}
			if (c.getItems().playerHasItem(2839)) {
			c.slayerRecipe = true;
			c.sendMessage("You have learnt the slayer helmet recipe. You can now assemble it");
			c.sendMessage("using a @blu@Black Mask@bla@, @blu@Facemask@bla@, @blu@Nose peg@bla@, @blu@Spiny helmet@bla@ and @blu@Earmuffs@bla@.");
		 	c.getItems().deleteItem(2839, 1);
			}
		}
/*		if (itemId == DiceHandler.DICE_BAG) {
			DiceHandler.selectDice(c, itemId);
		}*/
		if (itemId == 15098) {
/*			c.sendMessage("Dicing is currently disabled.");
			c.sendMessage("Because you guys are addicts.");*/
			/*if (System.currentTimeMillis() - c.diceDelay >= 5000) {
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("Nothing channel mate "
								+ Misc.ucFirst(c.playerName) + " rolled @red@"
								+ Misc.random(100)
								+ "@bla@ on the percentile dice.");
						c.diceDelay = System.currentTimeMillis();
					}
				}
			} else {
				c.sendMessage("You must wait 10 seconds to roll dice again.");
			}*/
		}
		if (itemId == 2690) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2690, 1)) {
				c.getDH().sendDialogues(4005, -1);
			}
		}
		if (itemId == 2691) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2691, 1)) {
				c.getDH().sendDialogues(4006, -1);
			}
		}
		if (itemId == 2692) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2692, 1)) {
				c.getDH().sendDialogues(4070, -1);
			}
		}
		if (itemId == 20770) { //p
			if (c.inWild() || /*c.inPvP()||*/ c.safeTimer > 0|| c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(20770, 1)) {
				c.getDH().sendDialogues(4071, -1);//4006
			}
		}
		if (itemId == 20771) { //r
			if (c.inWild() || /*c.inPvP()||*/ c.safeTimer > 0|| c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(20771, 1)) {
				c.getDH().sendDialogues(4073, -1);//4006
			}
		}
		if (itemId == 20772) { //a
			if (c.inWild() || /*c.inPvP()||*/ c.safeTimer > 0|| c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(20772, 1)) {
				c.getDH().sendDialogues(4075, -1);//4006
			}
		}
		// Packages
		//Hween Package Box
		 if (itemId == 15349 && c.getItems().freeSlots() >= 3) {
	            c.getItems().deleteItem(15349, 1);
	            c.getItems().addItem(Packages.hweenpackage1(), 1);
	            c.getItems().addItem(Packages.hweenpackage1(), 1);
	            c.getItems().addItem(Packages.hweenpackage2(), 1);
	            c.sendMessage("You open the package and get a reward!");
	        } else if (itemId == 15349 && c.getItems().freeSlots() <= 3) {
	            c.sendMessage("@red@You need at least 3 inventory slots to open the package.");
	        }
		 
		//Partyhat Package Box
		 if (itemId == 15348 && c.getItems().freeSlots() >= 3) {
	            c.getItems().deleteItem(15348, 1);
	            c.getItems().addItem(Packages.partyhatpackage1(), 1);
	            c.getItems().addItem(Packages.partyhatpackage1(), 1);
	            c.getItems().addItem(Packages.partyhatpackage2(), 1);
	            c.sendMessage("You open the package and get a reward!");
	        } else if (itemId == 15348 && c.getItems().freeSlots() <= 3) {
	            c.sendMessage("@red@You need at least 3 inventory slots to open the package.");
	        }
		 
		//Pking Package Box
		 if (itemId == 15352 && c.getItems().freeSlots() >= 1) {
	            c.getItems().deleteItem(15352, 1);
	            c.getItems().addItem(Packages.pkingpackage1(), 1);
	            c.sendMessage("You open the package and get a reward!");
	        } else if (itemId == 15352 && c.getItems().freeSlots() <= 1) {
	            c.sendMessage("@red@You need at least 1 inventory slots to open the package.");
	        }
		//Rare Package Box
		 if (itemId == 15351 && c.getItems().freeSlots() >= 1) {
	            c.getItems().deleteItem(15351, 1);
	            c.getItems().addItem(Packages.rarepackage1(), 1);
	            c.sendMessage("You open the package and get a reward!");
	        } else if (itemId == 15351 && c.getItems().freeSlots() <= 1) {
	            c.sendMessage("@red@You need at least 1 inventory slots to open the package.");
	        }
		 
	    //Huge Package Box
		 if (itemId == 15350) {
		        if (c.getItems().freeSlots() >= 9) {
		        	c.getItems().deleteItem(15350, 1);
		            c.sendMessage("You open the package and get a reward!");
		            for (int i = 0; i < 9; i++) {
		                 int randomItem = Packages.hugepackage1();
		                 int quantity = 1;
		                 if (randomItem == 2996) { //If its the item 2996
		                  if (ThreadLocalRandom.current().nextInt(1) == 0) { //1 in 5 chance
		                   int[] possibleQuantities = new int[] { 100, 500, 1000, 1500, 2000, 2500, 3000, 3500, 4000, 5000, 6000, 7000, 8000, 9000, 10000 };
		                   quantity = possibleQuantities[ThreadLocalRandom.current().nextInt(possibleQuantities.length)];
		                  c.sendMessage("item 2996, quantity: " + quantity);
		                  } else {
		                  c.sendMessage("You are unlucky");
		                  }
		                 }
		                 c.getItems().addItem(randomItem, quantity);                   
		            }
		        } else {
		            c.sendMessage("@red@You need at least 9 inventory slots to open the package.");
		        }
		           
		        }
		 
		/*if (itemId == 2697) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2697, 1)) {
				c.getDH().sendDialogues(4000, -1);
			}
		}
		if (itemId == 2698) { 
			if (c.inWild() || c.inCamWild() || c.inDuelArena() ||Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2698, 1)) {
				c.getDH().sendDialogues(4001, -1);
			}
		}
		if (itemId == 2699) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2699, 1)) {
				c.getDH().sendDialogues(4002, -1);
			}
		}
		if (itemId == 2700) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2700, 1)) {
				c.getDH().sendDialogues(4003, -1);
			}
		}
		if (itemId == 2701) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena() || Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			if (c.getItems().playerHasItem(2701, 1)) {
				c.getDH().sendDialogues(4004, -1);
			}
		}*/
		if (itemId == 7509) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena() || Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				return;
			}
			if (c.getItems().playerHasItem(7509, 1)) {
				int damage = (c.playerLevel[3] - 10);
				c.animation(829);
				c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
				c.getItems().deleteItem(7509, 1);
				c.forceChat("Ouch! I nearly broke a tooth!");
				c.getPA().refreshSkill(3);
			}
		}
		if (itemId == 10269) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena()) {
				return;
			}
			if (c.getItems().playerHasItem(10269, 1)) {
				c.getItems().addItem(995, 30000);
				c.getItems().deleteItem(10269, 1);
			}
		}
		if (itemId == 952) {
			if (c.isInMole()){
				TeleportExecutor.teleport(c, new Teleport(new Position(1752, 5236, 0), TeleportType.NORMAL));
				return;
			}
			DigEvent.checkDigSpot(c);
			c.getBarrows().spadeDigging();
			return;
		}
		if (itemId == 10271) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena()) {
				return;
			}
			if (c.getItems().playerHasItem(10271, 1)) {
				c.getItems().addItem(995, 10000);
				c.getItems().deleteItem(10271, 1);
			}
		}
		if (itemId == 10273) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena()) {
				return;
			}
			if (c.getItems().playerHasItem(10273, 1)) {
				c.getItems().addItem(995, 14000);
				c.getItems().deleteItem(10273, 1);
			}
		}
		if (itemId == 10275) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena()) {
				return;
			}
			if (c.getItems().playerHasItem(10275, 1)) {
				c.getItems().addItem(995, 18000);
				c.getItems().deleteItem(10275, 1);
			}
		}
		if (itemId == 10277) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena()) {
				return;
			}
			if (c.getItems().playerHasItem(10277, 1)) {
				c.getItems().addItem(995, 22000);
				c.getItems().deleteItem(10277, 1);
			}
		}
		if (itemId == 10279) {
			if (c.inWild() || c.inCamWild() || c.inDuelArena()) {
				return;
			}
			if (c.getItems().playerHasItem(10279, 1)) {
				c.getItems().addItem(995, 26000);
				c.getItems().deleteItem(10279, 1);
			}
		}
		/* Mystery box */
		if (itemId == 6199)
			if (c.getItems().playerHasItem(6199)) {
				c.getMysteryBox().open();
				return;
			}
		if (itemId == 2714) { // Easy Clue Scroll Casket
			c.getItems().deleteItem(itemId, 1);
			PlayerAssistant.addClueReward(c, 0);
		}
		if (itemId == 2802) { // Medium Clue Scroll Casket
			c.getItems().deleteItem(itemId, 1);
			PlayerAssistant.addClueReward(c, 1);
		}
		if (itemId == 2775) { // Hard Clue Scroll Casket
			c.getItems().deleteItem(itemId, 1);
			PlayerAssistant.addClueReward(c, 2);
		}
		if (itemId == 2677) {
			int randomClue = Misc.random(11);
			if (randomClue == 0) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2714, 1);
				c.sendMessage("You've recieved a easy clue scroll casket.");
			}
			if (randomClue == 0) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2714, 1);
				c.sendMessage("You've recieved a easy clue scroll casket.");
			}
			if (randomClue == 1) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2714, 1);
				c.sendMessage("You've recieved a easy clue scroll casket.");
			}
			if (randomClue == 2) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2714, 1);
				c.sendMessage("You've recieved a easy clue scroll casket.");
			}
			if (randomClue == 3) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2714, 1);
				c.sendMessage("You've recieved a easy clue scroll casket.");
			}
			if (randomClue == 4) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2802, 1);
				c.sendMessage("You've recieved a medium clue scroll casket.");
			}
			if (randomClue == 5) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2802, 1);
				c.sendMessage("You've recieved a medium clue scroll casket.");
			}
			if (randomClue == 6) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2802, 1);
				c.sendMessage("You've recieved a medium clue scroll casket.");
			}
			if (randomClue == 7) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2802, 1);
				c.sendMessage("You've recieved a medium clue scroll casket.");
			}
			if (randomClue == 8) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2802, 1);
				c.sendMessage("You've recieved a medium clue scroll casket.");
			}
			if (randomClue == 9) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2775, 1);
				c.sendMessage("You've recieved a hard clue scroll casket.");
			}
			if (randomClue == 10) {
				c.getItems().deleteItem(itemId, 1);
				c.getItems().addItem(2775, 1);
				c.sendMessage("You've recieved a hard clue scroll casket.");
			}
		}
		if (itemId == 2528) { c.usingLamp
			 = true; c.normalLamp = true; c.antiqueLamp = false;
			  c.sendMessage("You rub the lamp...");
			  c.getPA().showInterface(2808); 
		}
		if(itemId == 1907) {
			c.sendMessage("Against your better nature you drink the poison.");
			c.animation(829);
			c.getItems().deleteItem(1907, 1);
			c.getItems().addItem(1919, 1);
			c.setPoisonDamage((byte) 3);
		}
	}

}