package org.osps.model.players;

import java.util.Objects;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.content.Obelisks;
import org.osps.model.content.WildernessDitch;
import org.osps.model.content.dialogue.impl.ConverterDialogue;
import org.osps.model.content.dialogue.impl.DecantingDialogue;
import org.osps.model.content.dialogue.impl.SlayerAssignmentDialogue;
import org.osps.model.content.dialogue.impl.ZenyteDialogue;
import org.osps.model.content.hs.PKHighscore;
import org.osps.model.content.newTitles.TitleHandler;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.Teleport;
import org.osps.model.content.teleport.Teleport.TeleportType;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.minigames.Sailing;
import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.minigames.hunger.PlayerWrapper;
import org.osps.model.minigames.lottery.LotteryController;
import org.osps.model.minigames.pest_control.PestControl;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.multiplayer_session.duel.DuelSessionRules.Rule;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCCacheDefinition;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.PetHandler;
import org.osps.model.players.skills.Skill;
import org.osps.model.players.skills.Fishing.AnglerDig;
import org.osps.model.players.skills.Fishing.Fishing;
import org.osps.model.players.skills.crafting.JewelryMaking;
import org.osps.model.players.skills.crafting.Tanning;
import org.osps.model.players.skills.hunter.Hunter;
import org.osps.model.players.skills.hunter.Hunter.impData;
import org.osps.model.players.skills.hunter.ImpCatching;
import org.osps.model.players.skills.mining.Mineral;
import org.osps.model.players.skills.thieving.Thieving.Pickpocket;
import org.osps.model.players.skills.thieving.Thieving.Stall;
import org.osps.model.players.skills.woodcutting.Tree;
import org.osps.model.players.skills.woodcutting.Woodcutting;
import org.osps.model.shops.playerOwned.PlayerShops;
import org.osps.util.Location3D;
import org.osps.util.Misc;
import org.osps.world.objects.GlobalObject;

public class ActionHandler {

	private Player c;

	public ActionHandler(Player Client) {
		this.c = Client;
	}

	public void firstClickObject(int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (c.getPlayerAction().checkAction()) {
			return;
		}
		if ((c.getRights().isDeveloper() || c.getRights().isOwner()) && Config.SERVER_DEBUG ) {
			c.sendMessage("<col=255>[SERVER DEBUG] " + " - FirstClickObject: X: " + c.absX + " Y: " + c.absY
					+ " Height: " + c.heightLevel + " ObjectID: " + objectType);
		}
		c.getPA().resetVariables();
		c.clickObjectType = 0;
		c.face(obX, obY);
		c.getFarming().patchObjectInteraction(objectType, -1, obX, obY);
		// ClanWars.enterClanPortal(c, objectType);
		Tree tree = Tree.forObject(objectType);
		if (tree != null) {
			Woodcutting.getInstance().chop(c, objectType, obX, obY);
			return;
		}

		if (c.playerCannon != null && (PlayerCannon.CannonPart.isObjCannon(objectType) && objectType != 6)) {
			if (c.playerCannon.pickUpCannon(objectType, obX, obY)) {
				PlayerHandler.removeCannon(c.playerCannon);
				c.playerCannon = null;
			}
		} else if (c.playerCannon != null && objectType == 6) {
			c.playerCannon.fireCannon();
		} else if (PlayerCannon.CannonPart.isObjCannon(objectType)) {
			c.sendMessage("This is not your cannon!");
		}

		c.getGnomeAgility().agilityCourse(c, objectType);
		c.getSeers().initialize(c, objectType);
		c.getVarrock().initialize(c, objectType);
		c.getCanifis().initialize(c, objectType);
		c.getDraynor().initialize(c, objectType);
		c.getAlkharid().initialize(c, objectType);

		// c.getGnomeAgility().agilityCourse(c, objectType);
		// if (c.getWildernessAgility().wildernessCourse(c, objectType)) {
		// return;
		// }

		c.getMining().mine(objectType, new Location3D(obX, obY, c.heightLevel));
		c.getBM().dig(objectType, new Location3D(obX, obY, c.heightLevel));
		Obelisks.get().activate(c, objectType);

		if (Config.SERVER_DEBUG == true) {
			c.sendMessage("<col=255>[SERVER DEBUG] " + " - FirstClickObject: " + objectType);
		}

		if (c.hungerGames && objectType == 1990) {
			HungerManager.getSingleton().claimCrate(c, obX, obY);
		}

		if (objectType == 4411 && c.canWalk && !c.forceMovementUpdateRequired) {
			/* Going forward */
			if (c.absX == obX && c.absY + 1 == obY) {
				c.face(c.absX, c.absY + 10);
				c.animation(2588);
				c.setForceMovement(c.localX(), c.localY(), c.localX(), c.localY() + 1, 0, 10);
				c.getPA().movePlayer(c.absX, c.absY + 1, 0);
			}

			if (c.absX - 1 == obX && c.absY == obY) {
				c.face(c.absX - 10, c.absY);
				c.animation(2588);
				c.setForceMovement(c.localX(), c.localY(), c.localX() - 1, c.localY(), 0, 10);
				c.getPA().movePlayer(c.absX - 1, c.absY, 0);
			}
			/* Going back */
			if (c.absX == obX && c.absY - 1 == obY) {
				c.face(c.absX, c.absY - 10);
				c.animation(2588);
				c.setForceMovement(c.localX(), c.localY(), c.localX(), c.localY() - 1, 0, 10);
				c.getPA().movePlayer(c.absX, c.absY - 1, 0);
			}

			if (c.absX + 1 == obX && c.absY == obY) {
				c.face(c.absX + 10, c.absY);
				c.animation(2588);
				c.setForceMovement(c.localX(), c.localY(), c.localX() + 1, c.localY(), 0, 10);
				c.getPA().movePlayer(c.absX + 1, c.absY, 0);
			}
		}
		Location3D location = new Location3D(obX, obY, c.heightLevel);
		switch (objectType) {
//		case 12355:
//			if (c.clickTimer > 0)
//                return;
//            c.clickTimer = 2;
//            if (c.KC < 1000) {
//			   c.sendMessage("@red@You must have 1000 Kills to enter the Mazter Zone!");
//			   return;
//			}
//			TeleportExecutor.teleport(c, new Position(1237, 3561, 0));
//			c.sendMessage("@blu@Welcome to the Master Zone!");
//			c.getPA().loadQuests();
//	        break;
		case 4152:
			TeleportExecutor.teleport(c, new Position(3233, 2887, 0), true);
			break;
		case 104: //creations chest
			c.getPA().openUpBank();
		break;
		 case 4121:
			  if (c.clickTimer > 0)
	              return;
	              c.clickTimer = 3;
	  			c.animation(7061);
	  			c.sendMessage("You find nothing lol.");
	  		break;
		case 2194: //portal multi
			c.getDH().sendDialogues(3614, -1);
		break;
		case 4151: //portal multi
			//c.getDH().sendDialogues(3610, -1);
			c.sendMessage("Currently disabled for now.");
		break;
		case 4150: //portal multi
			c.getPA().showInterface(14000);
			//c.sendMessage("You'll need V1.1 Client for this portal to work correctly.");
		break;
		case 3192:
			 c.setHighscore(new PKHighscore());
             c.getHighscore().process();
             c.getHighscore().generateList(c);
		break;
		case 411:
			if (c.playerLevel[3] < Player.getLevelForXP(c.playerXP[3])) {
				c.playerLevel[3] = Player.getLevelForXP(c.playerXP[3]);
				c.getPA().refreshSkill(3);
				c.animation(645);
				c.sendMessage("Your hitpoints have been Restored.");
			} else {
				c.sendMessage("You already have full Hitpoints.");
			}
			
			break;
		case 26645:
			c.getPA().movePlayer(3328, 4751, 0);
			c.sendMessage("Welcome to the Fun PK! Here you can find single and multi PvP areas.");
			c.sendMessage("This area is safe, so all your items will be kept on death.");
			break;
		case 26646:
			c.getPA().movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			break;
		case 16671:
			c.getPA().movePlayer(2840, 3539, 2);
			break;
		case 4417:
			if(c.heightLevel == 0 && obX == 2419 && obY == 3078) {
				c.getPA().movePlayer(2420, 3080, 1);
			} else if(c.heightLevel == 1) {
				c.getDH().sendPlayerChat1("Do I really want to go up there?");
				c.nextChat = -1;
			} 
			break;
		case 4418:
			if(c.heightLevel == 0 && obX == 2380 && obY == 3127 && c.absX>=2380) {
				c.getPA().movePlayer(2379, 3127, 1);
			} else if(c.heightLevel == 1) {
				c.getDH().sendPlayerChat1("Do I really want to go up there?");
				c.nextChat = -1;
			} 			
			break;
		case 4469:
		case 4470:
			//TODO: monk
		case 14995:
			TeleportExecutor.teleport(c, new Position(2141, 3944, 0), true);
			break;
		case 14996:
			TeleportExecutor.teleport(c, new Position(2329, 10353, 2), true);
			break;
		case 4415:
			if(c.heightLevel == 1 && obX == 2419 && obY == 3080) {
				c.getPA().movePlayer(2419, 3077, 0);
			} else if(c.heightLevel == 1 && obX == 2380 && obY == 3127) {
				c.getPA().movePlayer(2380, 3130, 0);
			}	
			break;
		case 4911:
		case 16683:
			c.sendMessage("This ladder looks broken.");
			break;
		case 4390:
			if(c.underAttackBy2 > 0) {
				c.sendMessage("The vampire prevents you from leaving!");
			} else {
				c.getPA().movePlayer(2399, 3104, 0);
				c.sendMessage("The portal moves you to the center of the arena.");
			}
			break;
		case 4912:
			if(c.hungerGames) {
				PlayerWrapper wrap = HungerManager.getSingleton().getWrapper(c);
				if(wrap != null) {
					if(!wrap.usedRope) {
						if(c.getItems().playerHasItem(954)) {
							c.getItems().deleteItem(954, 1);
							c.getDH().sendPlayerChat1("I should be able to go down here now!");
							c.nextChat = -1;
							wrap.usedRope = true;
						} else {
							c.getDH().sendPlayerChat2("Something looks wrong with this ladder...", "I will need a rope to go down.");	
							c.nextChat = -1;
						}
					} else {
						c.forceChat("Oof!");
						c.sendMessage("You lose grip of the rope and fall!");
						c.getPA().movePlayer(2421, 9523, 0);
					}
				}
			}
			break;
		case 356:
			if(c.hungerGames) {
				c.getItems().addIfHasNone(3728);
			}		
			break;
		case 366:
			if(c.hungerGames) {
				PlayerWrapper wrap = HungerManager.getSingleton().getWrapper(c);
				if(wrap != null && !wrap.freeDiamond) {
					int diamond = wrap.getRandomDiamond();
					if(diamond != -1) {
						c.getItems().addItem(diamond, 1);
						wrap.freeDiamond = true;
						c.sendMessage("You find an ultra rare diamond!");
					}
				}
			}
			break;
		case 358:
			if(c.hungerGames) {
				if(obX == 2369 && obY == 3122) {
					c.getItems().addIfHasNone(590);
				} else if(obX == 2369 && obY == 3121) {
					c.getItems().addIfHasNone(1919);
				} else if(obX == 2379 && obY == 3130) {
					PlayerWrapper wrap = HungerManager.getSingleton().getWrapper(c);
					if(wrap != null && !wrap.freeDiamond) {
						int diamond = wrap.getRandomDiamond();
						if(diamond != -1) {
							c.getItems().addItem(diamond, 1);
							wrap.freeDiamond = true;
							c.sendMessage("You find an ultra rare diamond!");
						}
					}
				} else {
					c.getItems().addIfHasNone(3728);
				}
			}
			break;
		case 355:
			if(c.hungerGames) {
				c.getItems().addIfHasNone(590);
			}		
			break;
		case 4459:
			if(c.hungerGames) {
				c.getItems().addIfHasNone(1798);
			}		
			break;
		case 4464:
			if(c.hungerGames && c.playerEquipment[c.playerWeapon] != 1275) {
				c.getItems().addIfHasNone(1275);
			}		
			break;
		case 4460:
			if(c.hungerGames) {
				c.getItems().addIfHasNone(4043);
			}
			break;
		case 4461:
			if(c.hungerGames) {
				c.getItems().addIfHasNone(1549);
			}		
			break;
		case 4463:
			if(c.hungerGames) {
				c.getItems().addIfHasNone(1469);
			}
			break;
		case 357:			
			if(c.hungerGames) {
				c.getItems().addIfHasNone(1919);
			}
			break;
		case 4462:
			if(c.hungerGames) {
				c.getItems().addIfHasNone(954);
			}			
			break;
		case 8720:
			c.getPA().sendFrame126("https://www.osps.com", 12000);
			break;
		case 28857:
			if (c.absX == 1576 && c.absY >= 3490 && c.absY <= 3495) {
				c.getPA().movePlayer(1574, 3493, 2);
			} else if (c.absX == 1576 && c.absY >= 3480 && c.absY <= 3485) {
				c.getPA().movePlayer(1574, 3482, 2);
			} else if (c.absX == 1565 && c.absY >= 3489 && c.absY <= 3495) {
				c.getPA().movePlayer(1567, 3493, 2);
			} else if (c.absX == 1565 && c.absY >= 3479 && c.absY <= 3486) {
				c.getPA().movePlayer(1567, 3482, 2);
			}
			break;
		case 28858:
			if (c.absX == 1574 && c.absY >= 3492 && c.absY <= 3493) {
				c.getPA().movePlayer(1576, 3493, 0);
			} else if (c.absX == 1574 && c.absY >= 3482 && c.absY <= 3483) {
				c.getPA().movePlayer(1576, 3482, 0);
			} else if (c.absX == 1567 && c.absY >= 3492 && c.absY <= 3493) {
				c.getPA().movePlayer(1565, 3493, 0);
			} else if (c.absX == 1567 && c.absY >= 3482 && c.absY <= 3483) {
				c.getPA().movePlayer(1565, 3482, 0);
			}
			break;
		case 4420:
		case 4419:
			if (obX == 2382 && obY == 3131) {
				if (c.absX < 2383) {
					c.getPA().movePlayer(2383, 3133, 0);
				} else {
					c.getPA().movePlayer(2382, 3130, 0);

				}
			} else if (obX == 2417 && obY == 3074) {
				if (c.absX >= 2417) {
					c.getPA().movePlayer(2416, 3074, 0);
				} else {
					c.getPA().movePlayer(2417, 3077, 0);

				}
			}
			break;
		case 17387:
			if (obX == 2400 && obY == 9508)
				c.getPA().movePlayer(2400, 3107, 0);
			else if (obX == 2399 && obY == 9499)
				c.getPA().movePlayer(2399, 3100, 0);
			break;
		case 7179:
			if (HungerManager.getSingleton().undergroundUnlocked()) {
				if (obX == 2400 && obY == 3108 && c.absX == 2400) {
					c.getPA().movePlayer(2400, 9507, 0);
				} else if (obX == 2399 && obY == 3099 && c.absX == 2399) {
					c.getPA().movePlayer(2399, 9500, 0);
				}
			} else if (c.getItems().playerHasItem(2944)) {
				c.getItems().deleteItem(2944, 1);
				c.sendMessage("You unlock the trapdoor!");
				HungerManager.getSingleton().unlockDoor(c);
			} else if (!HungerManager.getSingleton().undergroundUnlocked()) {
				c.sendMessage("The door appears to be locked! Perhaps there is a key?");
			}
			break;
		case 4389:
			if (!HungerManager.getSingleton().exitGame(c))
			c.getPA().movePlayer(2440, 3090, 0);
			break;
		case 4408:
		case 4387:
		case 4388:
			//if(c.combatLevel >= 70) {
				//HungerManager.getSingleton().enterGame(c);
			//} else {
				//c.sendMessage("You need a combat level of 70+ to play hunger games!");
			//}
			c.sendMessage("Currently disabled for now.");
			break;
		case 4465:
		case 4467:
			if (obX == 2415 && obY == 3073) { // south door
				if (c.absX >= 2415 && c.absY == 3073) {
					c.getPA().movePlayer(2414, 3073, 0);
				} else if (HungerManager.getSingleton().doorUnlocked()) {
					c.getPA().movePlayer(2415, 3073, 0);
				} else if (c.getItems().playerHasItem(1523)) {
					c.sendMessage("You lockpick your way into the base.");
					c.getItems().deleteItem(1523, 1);
					c.getPA().movePlayer(2415, 3073, 0);
					HungerManager.getSingleton().unlockSmithDoor(c);
				} else {
					c.getDH().sendPlayerChat1("There has to be a way of lockpicking this door...");
				}
			}
			if (obX == 2384 && obY == 3134) { // north door
				if (c.absX <= 2384 && c.absY == 3134) {
					c.getPA().movePlayer(2385, 3134, 0);
				} else if (HungerManager.getSingleton().doorUnlocked()) {
					c.getPA().movePlayer(2384, 3134, 0);
				} else if (c.getItems().playerHasItem(1523)) {
					c.sendMessage("You lockpick your way into the base.");
					c.getItems().deleteItem(1523, 1);
					c.getPA().movePlayer(2384, 3134, 0);
					HungerManager.getSingleton().unlockSmithDoor(c);
				} else {
					c.getDH().sendPlayerChat1("There has to be a way of lockpicking this door...");
				}
			}
			break;
		case 4428:
		case 4427:
			if (c.absY >= 3120) {
				if ((c.absX == 2372 || c.absX == 2373))
					c.getPA().movePlayer(c.absX, 3119, 0);
				break;
			}
		case 4424:
		case 4423:
			if (c.absY <= 3087) {
				if ((c.absX == 2427 || c.absX == 2426))
					c.getPA().movePlayer(c.absX, 3088, 0);
				break;
			}
			c.getDH().sendDialogues(834, 4018);
			break;
		case 21600:
			if (c.absY == 3802) {
				c.getPA().movePlayer(2326, 3800, 0);
			} else if (c.absY == 3801) {
				c.getPA().movePlayer(2326, 3802, 0);
			}
			break;
		case 3999:
			if (c.absY == 3162) {
				c.getPA().movePlayer(2188, 3165, 0);
			} else if (c.absY == 3165) {
				c.getPA().movePlayer(2188, 3162, 0);
			}
			break;
		case 3939:
			if (c.absY == 3165) {
				c.getPA().movePlayer(2188, 3168, 0);
			} else if (c.absY == 3168) {
				c.getPA().movePlayer(2188, 3165, 0);
			}
			break;
		case 3998:
			if (c.absY == 3168) {
				c.getPA().movePlayer(2188, 3171, 0);
			} else if (c.absY == 3171) {
				c.getPA().movePlayer(2188, 3168, 0);
			}
			break;
		case 21733:
			c.getPA().movePlayer(2687, 9506, 0);
			break;
		case 20884:
			c.getPA().movePlayer(2672, 9499, 0);
			break;
		case 534:
			c.getPA().movePlayer(2356, 9782, 0);
			break;
		case 154:
			c.getPA().movePlayer(3748, 5761, 0);
			break;
		case 535:
			c.getPA().movePlayer(3677, 5775, 0);
			break;
		case 536:
			c.getPA().movePlayer(3723, 5798, 0);
			break;
		case 26709:
			c.getPA().movePlayer(2444, 9825, 0);
			break;
		case 26710:
			c.getPA().movePlayer(3094, 3486, 0);
			break;
		case 26711:
			c.getPA().movePlayer(3748, 5849, 0);
			break;
		case 26712:
			c.getPA().movePlayer(2436, 9823, 0);
			break;
		case 26721:
			if (c.absY == 9745) {
				c.getPA().movePlayer(2367, 9747, 0);
			} else if (c.absY == 9747) {
				c.getPA().movePlayer(2366, 9745, 0);
			}
			break;
		case 26724:
			if (c.absY == 9762) {
				c.getPA().movePlayer(2427, 9767, 0);
			} else if (c.absY == 9767) {
				c.getPA().movePlayer(2427, 9762, 0);
			}
			break;
		case 27557:
			AnglerDig.removeWorm(c);
			break;
		case 26502:// armadyl
			c.getDH().sendDialogues(644, -1);
			break;
		case 26503:// bandos
			c.getDH().sendDialogues(645, -1);
			break;
		case 26504:// saradomin
			c.getDH().sendDialogues(646, -1);
			break;
		case 26505:// zamorak
			c.getDH().sendDialogues(647, -1);
			break;
		case 26564:
		case 26565:
		case 26566:
			c.getPA().movePlayer(2873, 9847, 0);
			break;
		case 26569:
		case 26568:
		case 26567:
			if (c.playerLevel[c.playerSlayer] < 85) {
				c.sendMessage("You need a slayer level of 85 to enter cerberus lair.");
				return;
			}
			c.getPA().movePlayer(1310, 1237, 0);
			break;
		case 21772:
			c.getPA().movePlayer(1310, 1237, 0);
			break;
		case 23104:
			if (c.absX == 1292 || c.absX == 1291) {
				c.getPA().movePlayer(1240, 1226, 0);
			} else if (c.absY == 1270 || c.absY == 1269) {
				c.getPA().movePlayer(1304, 1290, 0);
			} else if (c.absX == 1328 || c.absX == 1329) {
				c.getPA().movePlayer(1368, 1226, 0);
			}
			break;
		case 21734:
			if (c.absX == 2674) {
				c.getPA().movePlayer(2676, 9479, 0);
			} else if (c.absX == 2676) {
				c.getPA().movePlayer(2674, 9479, 0);
			}
			break;
		case 21735:
			if (c.absX == 2693) {
				c.getPA().movePlayer(2695, 9482, 0);
			} else if (c.absX == 2695) {
				c.getPA().movePlayer(2693, 9482, 0);
			}
			break;

		/*
		 * case 24600: c.getPA().trollItems(); break;
		 */

		case 677:
			if (c.absX == 2970 || c.absX == 2969) {
				c.getPA().movePlayer(2974, 4383, 2);
			} else if (c.absX == 2974 || c.absX == 2975) {
				c.getPA().movePlayer(2970, 4383, 2);
			}
			break;
		/*
		 * case 23145: c.getPA().walkTo(c.absX - obX, c.absY - obY); if (c.absX
		 * == 2474 && c.absY == 3436) { // c.getGnomeAgility();
		 * GnomeAgility.walkAcross(c, 762, 0, -7, obX, obY, 0); } break;
		 */
		/*
		 * case 23271: if (c.goodDistance(c.getX(), c.getY(), c.objectX,
		 * c.objectY, 2)) { c.face(c.objectX, c.objectY); if (c.isBusy()) {
		 * return; } c.setBusy(true); //c.animation(6132); c.isRunning = false;
		 * c.isRunning2 = false; c.getPA().sendFrame36(173, 0); if (c.getY() <=
		 * c.objectY) { c.setForceMovement(c.localX(), c.localY(), c.localX(),
		 * c.localY() + 3, 33, 60, 0); } else { c.setForceMovement(c.localX(),
		 * c.localY(), c.localX(), c.localY() - 3, 33, 60, 4); }
		 * CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
		 * 
		 * @Override public void execute(CycleEventContainer container) { int
		 * newY = c.absY >= 3523 ? c.absY - 3 : c.absY + 3;
		 * c.getPA().movePlayer(c.absX, newY, 0); c.isRunning = true;
		 * c.isRunning2 = true; c.setBusy(false); container.stop(); }
		 * 
		 * }, 3); } break;
		 */
		case 23271:
			if (!c.ditchDelay.elapsed(2000)) {
				return;
			}
			c.face(obX, obY);
			c.ditchDelay.reset();
			if (c.getY() >= 3523) {
				WildernessDitch.leave(c);
			} else
				WildernessDitch.enter(c);
			break;

		// case 23271: WildernessDitch.handleWildernessDitch(c); break;

		case 11795:
			if (!c.getRights().isPlayer()) {
				c.getPA().movePlayer(3577, 9927, 0);
				c.animation(828);
				c.sendMessage("Welcome to the donator's only Slayer Cave.");
			}
			break;
		case 21725:
			if (c.absX > 2634 && c.absX < 2640 && c.absY > 9512 && c.absY < 9518) {
			c.getPA().movePlayer(2636, 9510, 2);
			}
			break;
		case 21726:
			c.getPA().movePlayer(2636, 9517, 0);
			break;
		case 21722:
			c.getPA().movePlayer(2643, 9594, 2);
			break;
		case 21724:
			c.getPA().movePlayer(2649, 9591, 0);
			break;
		case 2102:
		case 2104:
			if (c.absY <= 3555) {
				c.getPA().movePlayer(c.absX, c.absY + 1, c.heightLevel);
			} else {
				c.getPA().movePlayer(c.absX, c.absY - 1, c.heightLevel);
			}
			break;
		case 23566:
			if (obX == 3120 && obY == 9964 || obX == 3121 && obY == 9964 || obX == 3120 && obY == 9963
					|| obX == 3121 && obY == 99643) {
				c.getPA().movePlayer(3120, 9970, 0);
			} else if (obX == 3120 && obY == 9969 || obX == 3121 && obY == 9969 || obX == 3121 && obY == 9970
					|| obX == 3120 && obY == 9970) {
				c.getPA().movePlayer(3120, 9963, 0);
			}
			break;
		case 26760:
			if (c.absX == 3184 && c.absY == 3945) {
				c.getDH().sendDialogues(631, -1);
			} else if (c.absX == 3184 && c.absY == 3944) {
				c.getPA().movePlayer(3184, 3945, 0);
			}
			break;
		case 16511:
			if (c.absX == 3150 && c.absY == 9905 || c.absX == 3150 && c.absY == 9907 || c.absX == 3149 && c.absY == 9907
					|| c.absX == 3149 && c.absY == 9906 || c.absX == 3149 && c.absY == 9905) {
				c.getPA().movePlayer(3155, 9906, 0);
			} else if (c.absX == 3155 && c.absY == 9906 || c.absX == 3155 && c.absY == 9905
					|| c.absX == 3155 && c.absY == 9907 || c.absX == 3154 && c.absY == 9907
					|| c.absX == 3154 && c.absY == 9905) {
				c.getPA().movePlayer(3149, 9906, 0);
			}
			break;
		case 9326:
			if (c.playerLevel[16] < 62) {
				c.sendMessage("You need an Agility level of 62 to pass this.");
				return;
			}
			if (c.absX < 2769) {
				c.getPA().movePlayer(2775, 10003, 0);
			} else {
				c.getPA().movePlayer(2768, 10002, 0);
			}
			break;
		case 2120:
		case 2119:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(3412, 3540, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(3418, 3540, 2);
			}
			break;
		case 2100:
			if (c.absY <= 3554) {
				c.getPA().movePlayer(c.absX, c.absY + 1, c.heightLevel);
			} else {
				c.getPA().movePlayer(c.absX, c.absY - 1, c.heightLevel);
			}
			break;
		case 16544:
			if (c.absX == 2768) {
				c.getPA().movePlayer(c.absX + 2, c.absY, c.heightLevel);
			} else if (c.absX == 2770) {
				c.getPA().movePlayer(c.absX - 2, c.absY, c.heightLevel);
			}
			if (c.absX == 2773) {
				c.getPA().movePlayer(c.absX + 2, c.absY, c.heightLevel);
			} else if (c.absX == 2775) {
				c.getPA().movePlayer(c.absX - 2, c.absY, c.heightLevel);
			}
			break;
		case 16539:
			if (c.absX == 2735) {
				c.getPA().movePlayer(c.absX - 5, c.absY, c.heightLevel);
			} else if (c.absX == 2730) {
				c.getPA().movePlayer(c.absX + 5, c.absY, c.heightLevel);
			}
			break;
		case 2114:
		case 2118:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(3433, 3537, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(3438, 3537, 0);
			}
			break;
		case 16538:
		case 16537:
			if (c.heightLevel == 0)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			else if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 0);
			else if (c.heightLevel == 2)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			break;
		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 4495:
			if (c.heightLevel == 1 && c.absY > 3538 && c.absY < 3543) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			} else {
				c.sendMessage("I can't reach that!");
			}
			break;
		case 2623:
			if (c.absX == 2924 && c.absY == 9803) {
				c.getPA().movePlayer(c.absX - 1, c.absY, 0);
			} else if (c.absX == 2923 && c.absY == 9803) {
				c.getPA().movePlayer(c.absX + 1, c.absY, 0);
			}
			break;
		case 15644:
		case 15641:
		case 24306:
		case 24309:
			if (c.heightLevel == 2) {
				// if(Boundary.isIn(c, WarriorsGuild.WAITING_ROOM_BOUNDARY) &&
				// c.heightLevel == 2) {
				c.getWarriorsGuild().handleDoor();
				return;
				// }
			}
			if (c.heightLevel == 0) {
				if (c.absX == 2855 || c.absX == 2854) {
					if (c.absY == 3546)
						c.getPA().movePlayer(c.absX, c.absY - 1, 0);
					else if (c.absY == 3545)
						c.getPA().movePlayer(c.absX, c.absY + 1, 0);
					c.face(obX, obY);
				}
			}
			break;
		case 15653:
			if (c.absY == 3546) {
				if (c.absX == 2877)
					c.getPA().movePlayer(c.absX - 1, c.absY, 0);
				else if (c.absX == 2876)
					c.getPA().movePlayer(c.absX + 1, c.absY, 0);
				c.face(obX, obY);
			}
			break;
		case 24303:
			c.getPA().movePlayer(2840, 3539, 0);
			break;
		case 16469:
		case 4308:
			JewelryMaking.mouldInterface(c);
			break;
		case 16664:
			if (c.absY > 3920 && c.inWild())
				c.getPA().movePlayer(3045, 10323, 0);
			break;
		case 16665:
			if (c.absY > 9000 && c.inWild())
				c.getPA().movePlayer(3044, 3927, 0);
			break;
		case 4156:
			if (c.absY > 3920 && c.inWild())
				c.getPA().movePlayer(3087, 3491, 0);
			break;
		case 4157:
			TeleportExecutor.teleport(c, new Position(2604, 3154, 0), true);
			c.sendMessage("This is the dicing area. Place a bet on designated hosts.");
			break;
		case 2309:
			if (c.getX() == 2998 && c.getY() == 3916) {
				c.getAgility().doWildernessEntrance(c);
			}
			break;
		case 18988:
			if (c.inWild() && c.absX == 3069 && c.absY == 10255) {
				c.getPA().movePlayer(3017, 3850, 0);
			}
			break;
		case 18987:
			if (c.inWild() && c.absY >= 3847 && c.absY <= 3860) {
				c.getPA().movePlayer(3069, 10255, 0);
			}
			break;
		case 7407:
		case 7408:
			if (c.absY < 9000) {
				if (c.absY > 3903) {
					c.getPA().movePlayer(c.absX, c.absY - 1, 0);
				} else {
					c.getPA().movePlayer(c.absX, c.absY + 1, 0);
				}
			} else if (c.absY > 9917) {
				c.getPA().movePlayer(c.absX, c.absY - 1, 0);
			} else {
				c.getPA().movePlayer(c.absX, c.absY + 1, 0);
			}
			break;
			
		case 20672:
			c.objectYOffset = 0;
			c.objectDistance = 3;
			c.getPA().movePlayer(3557, 3297, 0);
			break;
			
		case 20671:
			c.objectYOffset = 0;
			c.objectDistance = 5;
			c.getPA().movePlayer(3554, 3282, 0);
			break;
			
		case 6702:
		case 6703:
		case 6704:
		case 6705:
		case 6706:
		case 6707:
		//case 20672:
		case 20667:
		case 20668:
		case 20669:
		case 20670:
		//case 20671:
		case 20699:
			c.getBarrows().useStairs();
			break;
		case 20973:
			c.getBarrows().useChest();
			break;

		case 20772:
			if (c.barrowsNpcs[0][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1677, c.getX(), c.getY() - 1, 3, 0, 120, 25, 200, 200, true, true);
				c.barrowsNpcs[0][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20721:
			if (c.barrowsNpcs[1][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1676, c.getX() + 1, c.getY(), 3, 0, 120, 20, 200, 200, true, true);
				c.barrowsNpcs[1][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20771:
			if (c.barrowsNpcs[2][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1675, c.getX(), c.getY() - 1, 3, 0, 90, 17, 200, 200, true, true);
				c.barrowsNpcs[2][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20722:
			if (c.barrowsNpcs[3][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1674, c.getX(), c.getY() - 1, 3, 0, 120, 23, 200, 200, true, true);
				c.barrowsNpcs[3][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20770:
			// c.getDH().sendDialogues(2900, 2026);
			c.getBarrows().checkCoffins();
			c.getPA().removeAllWindows();
			break;

		case 20720:
			if (c.barrowsNpcs[4][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1673, c.getX() - 1, c.getY(), 3, 0, 90, 19, 200, 200, true, true);
				c.barrowsNpcs[4][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		case 14315:
			PestControl.addToLobby(c);
			break;

		case 14314:
			PestControl.removeFromLobby(c);
			break;
		case 21731:
			if (c.absX > 2689) {
				c.getPA().movePlayer(c.absX - 2, c.absY, 0);
			} else {
				c.getPA().movePlayer(c.absX + 2, c.absY, 0);
			}
			break;
		case 21732:
			if (c.absY > 9568) {
				c.getPA().movePlayer(c.absX, c.absY - 2, 0);
			} else {
				c.getPA().movePlayer(c.absX, c.absY + 2, 0);
			}
			break;
		case 14235:
		case 14233:
			if (c.objectX == 2670) {
				if (c.absX <= 2670) {
					c.absX = 2671;
				} else {
					c.absX = 2670;
				}
			}
			if (c.objectX == 2643) {
				if (c.absX >= 2643) {
					c.absX = 2642;
				} else {
					c.absX = 2643;
				}
			}
			if (c.absX <= 2585) {
				c.absY += 1;
			} else {
				c.absY -= 1;
			}
			c.getPA().movePlayer(c.absX, c.absY, 0);
			break;

		case 245:
			this.c.getPA().movePlayer(this.c.absX, this.c.absY + 2, 2);
			break;
		case 246:
			this.c.getPA().movePlayer(this.c.absX, this.c.absY - 2, 1);
			break;
		case 272:
			this.c.getPA().movePlayer(this.c.absX, this.c.absY, 1);
			break;
		case 273:
			this.c.getPA().movePlayer(this.c.absX, this.c.absY, 0);
			break;

		case 5960:
			if (c.leverClicked == false) {
				c.getDH().sendDialogues(114, 9985);
				c.leverClicked = true;
			} else {
				TeleportExecutor.executeLeverTeleport(c, new Teleport(new Position(3090, 3956), TeleportType.LEVER));
			}
			break;

		case 5959:
			TeleportExecutor.executeLeverTeleport(c, new Teleport(new Position(2539, 4712), TeleportType.LEVER));
			break;
		case 4950:
			TeleportExecutor.executeLeverTeleport(c, new Teleport(new Position(3087, 3500), TeleportType.LEVER));
			break;
		case 1816:
			TeleportExecutor.executeLeverTeleport(c, new Teleport(new Position(2271, 4680), TeleportType.LEVER));
			break;
		case 1817:
			TeleportExecutor.executeLeverTeleport(c, new Teleport(new Position(3067, 10253), TeleportType.LEVER));
			break;
		case 26761:
			TeleportExecutor.executeLeverTeleport(c, new Teleport(new Position(3158, 3953), TeleportType.LEVER));
			break;
		/* Start Brimhavem Dungeon */
		case 2879:
			c.getPA().movePlayer(2542, 4718, 0);
			break;
		case 2878:
			c.getPA().movePlayer(2509, 4689, 0);
			break;
		case 5083:
			c.getPA().movePlayer(2713, 9564, 0);
			c.sendMessage("You enter the dungeon.");
			break;

		case 7122:
			if (c.absX == 2564 && c.absY == 3310) {
				c.getPA().movePlayer(2563, 3310, 0);
			} else if (c.absX == 2563 && c.absY == 3310) {
				c.getPA().movePlayer(2564, 3310, 0);
			}
			break;
		case 5103:
			if (c.absX == 2691 && c.absY == 9564) {
				c.getPA().movePlayer(2689, 9564, 0);
			} else if (c.absX == 2689 && c.absY == 9564) {
				c.getPA().movePlayer(2691, 9564, 0);
			}
			break;
		case 5106:
			if (c.absX == 2674 && c.absY == 9479) {
				c.getPA().movePlayer(2676, 9479, 0);
			} else if (c.absX == 2676 && c.absY == 9479) {
				c.getPA().movePlayer(2674, 9479, 0);
			}
			break;

		case 5105:
			if (c.absX == 2672 && c.absY == 9499) {
				c.getPA().movePlayer(2674, 9499, 0);
			} else if (c.absX == 2674 && c.absY == 9499) {
				c.getPA().movePlayer(2672, 9499, 0);
			}
			break;

		case 5107:
			if (c.absX == 2693 && c.absY == 9482) {
				c.getPA().movePlayer(2695, 9482, 0);
			} else if (c.absX == 2695 && c.absY == 9482) {
				c.getPA().movePlayer(2693, 9482, 0);
			}
			break;

		case 5104:
			if (c.absX == 2683 && c.absY == 9568) {
				c.getPA().movePlayer(2683, 9570, 0);
			} else if (c.absX == 2683 && c.absY == 9570) {
				c.getPA().movePlayer(2683, 9568, 0);
			}
			break;

		case 5100:
			if (c.absY <= 9567) {
				c.getPA().movePlayer(2655, 9573, 0);
			} else if (c.absY >= 9572) {
				c.getPA().movePlayer(2655, 9566, 0);
			}
			break;
		case 5099:
			if (c.playerLevel[16] < 34) {
				c.sendMessage("You need an Agility level of 34 to pass this.");
				return;
			}
			if (c.objectX == 2698 && c.objectY == 9498) {
				c.getPA().movePlayer(2698, 9492, 0);
			} else if (c.objectX == 2698 && c.objectY == 9493) {
				c.getPA().movePlayer(2698, 9499, 0);
			}
			break;
		case 5088:
			if (c.playerLevel[16] < 30) {
				c.sendMessage("You need an Agility level of 30 to pass this.");
				return;
			}
			c.getPA().movePlayer(2687, 9506, 0);
			break;
		case 5090:
			if (c.playerLevel[16] < 30) {
				c.sendMessage("You need an Agility level of 30 to pass this.");
				return;
			}
			c.getPA().movePlayer(2682, 9506, 0);
			break;

		case 21738:
			if (c.playerLevel[16] < 12) {
				c.sendMessage("You need an Agility level of 12 to pass this.");
				return;
			}
			c.getPA().movePlayer(2647, 9557, 0);
			break;
		case 21739:
			if (c.playerLevel[16] < 12) {
				c.sendMessage("You need an Agility level of 12 to pass this.");
				return;
			}
			c.getPA().movePlayer(2649, 9562, 0);
			break;

		case 5084:
			c.getPA().movePlayer(2744, 3151, 0);
			c.sendMessage("You exit the dungeon.");
			break;
		/* End Brimhavem Dungeon */
		case 6481:
			c.getPA().movePlayer(3233, 9315, 0);
			break;

		case 1551:
			if (c.absX == 3252 && c.absY == 3266) {
				c.getPA().movePlayer(3253, 3266, 0);
			}
			if (c.absX == 3253 && c.absY == 3266) {
				c.getPA().movePlayer(3252, 3266, 0);
			}
			break;
		case 1553:
			if (c.absX == 3252 && c.absY == 3267) {
				c.getPA().movePlayer(3253, 3266, 0);
			}
			if (c.absX == 3253 && c.absY == 3267) {
				c.getPA().movePlayer(3252, 3266, 0);
			}
			break;
		case 6189:
		case 26300:
			c.getSmithing().sendSmelting();
			break;

		/* AL KHARID */
		case 2883:
		case 2882:
			c.getDH().sendDialogues(1023, 925);
			break;
		case 2412:
			Sailing.startTravel(c, 1);
			break;
		case 2414:
			Sailing.startTravel(c, 2);
			break;
		case 2083:
			Sailing.startTravel(c, 5);
			break;
		case 2081:
			Sailing.startTravel(c, 6);
			break;
		case 14304:
			Sailing.startTravel(c, 14);
			break;
		case 14306:
			Sailing.startTravel(c, 15);
			break;

		case 10083:
		case 11744:
		case 3045:
		case 14367:
		case 3193:
		case 10517:
		case 11402:
		case 26972:
		case 25808:
		case 27718:
		case 27719:
		case 27720:
		case 27721:
		case 28594:
		case 28595:
		case 4483:
		case 28861:
		case 6943:
		case 40859:
		case 2213:
		case 11758:
			c.getPA().openUpBank();
			break;

		/**
		 * Entering the Fight Caves.
		 */
		case 11833:
			if (Boundary.entitiesInArea(Boundary.FIGHT_CAVE) >= 50) {
				c.sendMessage("There are too many people using the fight caves at the moment. Please try again later");
				return;
			}
			if (System.currentTimeMillis() - c.hunting < 1500) {
				c.sendMessage("No");
				return;
			}
			c.getFightCave().create(0);
			c.hunting = System.currentTimeMillis();
			break;

		case 11834:
			if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
				c.getFightCave().leaveGame();
				return;
			}
			break;

		/**
		 * Clicking on the Ancient Altar.
		 */
		case 6552:
			if (c.playerMagicBook == 0) { // modern
				c.setSidebarInterface(6, 12855); // ancient
				c.playerMagicBook = 1;
				c.getPA().resetAutocast();
			} else if (c.playerMagicBook == 1) {
				c.setSidebarInterface(6, 29999); // lunar
				c.playerMagicBook = 2;
				c.getPA().resetAutocast();
			} else {
				c.setSidebarInterface(6, 1151); // modern
				c.playerMagicBook = 0;
				c.getPA().resetAutocast();
			}
			break;

		/**
		 * c.setSidebarInterface(6, 1151); Recharing prayer points.
		 */
		// case 23145:
		// if (c.absY >= 2474 && c.absY <= 3436) {
		// c.getGnomeAgility().CLICK_LOG(c);
		// }
		// break;

		case 409:
			if (c.inWild()) {
				return;
			}
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.animation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
			}
			break;

		/**
		 * Aquring god capes.
		 */
		case 2873:
			if (!c.getItems().ownsCape()) {
				c.animation(645);
				c.sendMessage("Saradomin blesses you with a cape.");
				c.getItems().addItem(2412, 1);
			} else {
				c.sendMessage("You already have a cape");
			}
			break;
		case 2875:
			if (!c.getItems().ownsCape()) {
				c.animation(645);
				c.sendMessage("Pkers.Ca blesses you with a cape.");
				c.getItems().addItem(2413, 1);
			} else {
				c.sendMessage("You already have a cape");
			}
			break;
		case 2874:
			if (!c.getItems().ownsCape()) {
				c.animation(645);
				c.sendMessage("Zamorak blesses you with a cape.");
				c.getItems().addItem(2414, 1);
			} else {
				c.sendMessage("You already have a cape");
			}
			break;

		/**
		 * Oblisks in the wilderness.
		 */
		case 14829:
		case 14830:
		case 14827:
		case 14828:
		case 14826:
		case 14831:

			break;

		/**
		 * Clicking certain doors.
		 */
		case 6749:
			if (obX == 3562 && obY == 9678) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9677) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;

		case 6730:
			if (obX == 3558 && obY == 9677) {
				c.getPA().object(3562, 9678, 6749, -3, 0);
				c.getPA().object(3562, 9677, 6730, -1, 0);
			} else if (obX == 3558 && obY == 9678) {
				c.getPA().object(3558, 9677, 6749, -1, 0);
				c.getPA().object(3558, 9678, 6730, -3, 0);
			}
			break;

		case 6727:
			if (obX == 3551 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;

		case 6746:
			if (obX == 3552 && obY == 9684) {
				c.sendMessage("You cant open this door..");
			}
			break;

		case 6748:
			if (obX == 3545 && obY == 9678) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9677) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;

		case 6729:
			if (obX == 3545 && obY == 9677) {
				c.getPA().object(3545, 9678, 6748, -3, 0);
				c.getPA().object(3545, 9677, 6729, -1, 0);
			} else if (obX == 3541 && obY == 9678) {
				c.getPA().object(3541, 9677, 6748, -1, 0);
				c.getPA().object(3541, 9678, 6729, -3, 0);
			}
			break;

		case 6726:
			if (obX == 3534 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3535 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;

		case 6745:
			if (obX == 3535 && obY == 9684) {
				c.getPA().object(3534, 9684, 6726, -4, 0);
				c.getPA().object(3535, 9684, 6745, -2, 0);
			} else if (obX == 3534 && obY == 9688) {
				c.getPA().object(3535, 9688, 6726, -2, 0);
				c.getPA().object(3534, 9688, 6745, -4, 0);
			}
			break;

		case 6743:
			if (obX == 3545 && obY == 9695) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9694) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;

		case 6724:
			if (obX == 3545 && obY == 9694) {
				c.getPA().object(3545, 9694, 6724, -1, 0);
				c.getPA().object(3545, 9695, 6743, -3, 0);
			} else if (obX == 3541 && obY == 9695) {
				c.getPA().object(3541, 9694, 6724, -1, 0);
				c.getPA().object(3541, 9695, 6743, -3, 0);
			}
			break;

		case 1516:
		case 1519:
			if (c.objectY == 9698) {
				if (c.absY >= c.objectY)
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1);
			}
			break;
		case 5126:
			if (c.absY == 3554)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;

		case 1759:
			if (c.objectX == 2884 && c.objectY == 3397)
				c.getPA().movePlayer(c.absX, c.absY + 6400, 0);
			break;
		case 7168:
		case 7169:
			if (c.absX == 3105 && c.absY == 9943 || c.absX == 3105 && c.absY == 9944 || c.absX == 3106 && c.absY == 9944
					|| c.absX == 3106 && c.absY == 9943) {
				c.getPA().movePlayer(3105, 9945, 0);
			} else if (c.absX == 3105 && c.absY == 9945 || c.absX == 3105 && c.absY == 9946
					|| c.absX == 3106 && c.absY == 9946 || c.absX == 3106 && c.absY == 9945) {
				c.getPA().movePlayer(3105, 9944, 0);
			}
			if (c.absX == 3146 && c.absY == 9870 || c.absX == 3147 && c.absY == 9870 || c.absX == 3147 && c.absY == 9871
					|| c.absX == 3146 && c.absY == 9871) {
				c.getPA().movePlayer(3145, 9870, 0);
			} else if (c.absX == 3145 && c.absY == 9870 || c.absX == 3144 && c.absY == 9870
					|| c.absX == 3144 && c.absY == 9871 || c.absX == 3145 && c.absY == 9871) {
				c.getPA().movePlayer(3146, 9870, 0);
			}
			break;
		case 11727:
			c.sendMessage("This door is locked.");
			break;

		case 16510:
			if (c.absX < c.objectX) {
				c.getPA().movePlayer(c.objectX + 1, c.absY, 0);
			} else if (c.absX > c.objectX) {
				c.getPA().movePlayer(c.objectX - 1, c.absY, 0);
			}
			break;

		case 16509:
			if (c.absX == 2886 || c.absX == 2885) {
				c.getPA().movePlayer(2892, 9799, 0);
			} else if (c.absX == 2891) {
				c.getPA().movePlayer(2886, 9799, 0);
			}
			break;
		case 9706:
			if (c.absX == 3105 || c.absX == 3956) {
				c.getPA().movePlayer(3105, 3951, 0);
			}
			break;
		case 9707:
			if (c.absX == 3105 || c.absX == 3951) {
				c.getPA().movePlayer(3105, 3956, 0);
			}
			break;
		case 10529:
		case 10527:
			if (c.absY <= c.objectY)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;

		case 733:
			if (c.inWild()) {
				c.animation(451);
				if (c.absX == 3095 || c.absX == 3957) {
					c.getPA().movePlayer(3094, 3957, 0);
				} else if (c.absX == 3094 || c.absX == 3957) {
					c.getPA().movePlayer(3095, 3957, 0);
				} else if (c.absX == 3092 || c.absX == 3957) {
					c.getPA().movePlayer(3093, 3957, 0);
				} else if (c.absX == 3093 || c.absX == 3957) {
					c.getPA().movePlayer(3092, 3957, 0);
				} else if (c.absX == 3105 && c.absY == 3959) {
					c.getPA().movePlayer(3105, 3957, 0);
				} else if (c.absX == 3106 && c.absY == 3959) {
					c.getPA().movePlayer(3106, 3957, 0);
				} else if (c.absX == 3105 && c.absY == 3957) {
					c.getPA().movePlayer(3105, 3959, 0);
				} else if (c.absX == 3106 && c.absY == 3957) {
					c.getPA().movePlayer(3106, 3959, 0);
				}
				if (c.objectX == 3158 && c.objectY == 3951) {
					Server.getGlobalObjects()
							.add(new GlobalObject(734, c.objectX, c.objectY, c.heightLevel, 1, 10, 50, 733));
				}
				/*
				 * else if (c.objectX == 3095 && c.objectY == 3957) {
				 * Server.getGlobalObjects().add(new GlobalObject(734, 3095,
				 * 3957, c.heightLevel, 2, 10, 50, 733)); } else {
				 * Server.getGlobalObjects().add(new GlobalObject(734, c.objectX
				 * - 1, c.objectY, c.heightLevel, 2, 10, 50, 733)); }
				 */
			}
			break;

		default:
			//ScriptManager.callFunc("objectClick1_" + objectType, c, objectType, obX, obY);
			break;

		/**
		 * Forfeiting a duel.
		 */
		case 3203:
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(session)) {
				return;
			}
			if (!Boundary.isIn(c, Boundary.DUEL_ARENAS)) {
				return;
			}
			if (session instanceof DuelSession) {
				if (session.getRules().contains(Rule.FORFEIT)) {
					c.sendMessage("You are not permitted to forfeit the duel.");
					return;
				}
			} else {
				/**
				 * TO-DO: FORFEIT
				 */
			}
			break;

		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (Config.SERVER_DEBUG == true) {
			c.sendMessage("<col=255>[SERVER DEBUG]Second Click Object: " + objectType + " " + obX + " " + obY);
		}
		c.clickObjectType = 0;
		c.getFarming().patchObjectInteraction(objectType, -1, obX, obY);
		Location3D location = new Location3D(obX, obY, c.heightLevel);

		if (c.playerCannon != null && objectType == 6) {
			if (c.playerCannon.pickUpCannon(objectType, obX, obY)) {
				PlayerHandler.removeCannon(c.playerCannon);
				c.playerCannon = null;
			}
		} else if (PlayerCannon.CannonPart.isObjCannon(objectType)) {
			c.sendMessage("This is not your cannon!");
		}

		switch (objectType) {
		/*
		 * case 409: if(c.getRights().getValue() == 0) {
		 * c.sendMessage("This is a donator only feature! ::donate"); return; }
		 * if (System.currentTimeMillis() - c.specs < 180000) {
		 * c.sendMessage("You can only use this feature once every 3 minutes.");
		 * return; } if (c.specAmount >= 9.0) {
		 * c.sendMessage("You have full special attack already!"); return; }
		 * if(c.getRights().getValue() >= 3) { c.spawnSet =
		 * System.currentTimeMillis(); c.specAmount += 10.0;
		 * c.getItems().updateSpecialBar();
		 * c.sendMessage("You have restored your spec!"); } break;
		 */
		case 411:
			if (c.inWild()) {
				return;
			}
			if (c.getRights().getValue() == 0) {
				c.sendMessage("This is a donator only feature! ::donate");
				return;
			}
			if (System.currentTimeMillis() - c.specs < 180000) {
				c.sendMessage("You can only use this feature once every 3 minutes.");
				return;
			}
			if (c.specAmount >= 9.0) {
				c.sendMessage("You have full special attack already!");
				return;
			}
			if (c.absY >= 3508 && c.absY <= 3513) {
				if (c.getRights().getValue() >= 1) {
					c.specAmount = 0.0;
					c.animation(645);
					c.spawnSet = System.currentTimeMillis();
					c.specAmount += 10.0;
					c.getItems().updateSpecialBar();
					c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
					c.sendMessage("You have restored your spec!");
				}
			}
			break;
		case 823:
			if (c.clickTimer > 0)
                return;
            c.clickTimer = 4;
            c.getCombat().calculateMeleeMaxHit();
            c.getPA().requestUpdates();
			c.face(obX, obY);
			c.animation(423);
			break;
		case 11338:
			c.getPA().openUpBank();
			break;
		case 51884:
			c.getPA().openUpBank();
		break;
		case 7041:
			if(c.getRights().getValue() == 0) {
			c.sendMessage("@red@This is a Donator only function!"); return; }
			if(System.currentTimeMillis() - c.specAlter < 300000) {
				c.sendMessage("@red@You can only use this feature once every 3 minutes!");
			return; } if (c.specAmount >= 9.0) {
			c.sendMessage("You have full special attack already!"); return; }
			if(c.getRights().getValue() >= 3) { c.spawnSet =
			System.currentTimeMillis(); c.specAmount += 10.0;
			c.specAlter = System.currentTimeMillis();
			c.getItems().updateSpecialBar();
			c.animation(645);
			c.sendMessage("@red@You have restored your spec!"); 
			}
			break;
		case 16469:
		case 4308:
			JewelryMaking.mouldInterface(c);
			break;
		case 20672:
			c.objectYOffset = 0;
			c.objectDistance = 3;
			c.getPA().movePlayer(3557, 3297, 0);
			break;
			
		case 20671:
			c.objectYOffset = 0;
			c.objectDistance = 3;
			c.getPA().movePlayer(3554, 3282, 0);
			break;
			
		//case 20672:
		case 20667:
		case 20668:
		case 20670:
		//case 20671:
		case 20669:
			c.getBarrows().useStairs();
			c.sendMessage("Stairs working??");
			break;
		case 10284:
			c.getBarrows().useChest();
			break;
		case 23271:
			if (c.getY() >= 3523) {
				WildernessDitch.leave(c);
			} else
				WildernessDitch.enter(c);
			break;
		case 20721:
			if (c.barrowsNpcs[1][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1676, c.getX() + 1, c.getY(), 3, 0, 120, 20, 200, 200, true, true);
				c.barrowsNpcs[1][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20771:
			if (c.barrowsNpcs[2][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1675, c.getX(), c.getY() - 1, 3, 0, 90, 17, 200, 200, true, true);
				c.barrowsNpcs[2][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20722:
			if (c.barrowsNpcs[3][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1674, c.getX(), c.getY() - 1, 3, 0, 120, 23, 200, 200, true, true);
				c.barrowsNpcs[3][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 20770:
			// c.getDH().sendDialogues(29, 2026);
			c.getBarrows().checkCoffins();
			c.getPA().removeAllWindows();
			c.sendMessage("Is working ??????");

			break;

		case 20720:
			if (c.barrowsNpcs[5][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1673, c.getX() - 1, c.getY(), 3, 0, 90, 19, 200, 200, true, true);
				c.barrowsNpcs[5][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		case 20772:
			if (c.barrowsNpcs[0][1] == 0) {
				Server.npcHandler.spawnNpc(c, 1677, c.getX(), c.getY() - 1, 3, 0, 120, 25, 200, 200, true, true);
				c.barrowsNpcs[0][1] = 1;
			} else {
				c.sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		case 21165:
			c.getThieving().steal(Stall.CAKE, objectType, location);
			break;
		case 21421:
			c.getThieving().steal(Stall.GEM, objectType, location);
			break;
		case 21677:
			c.getThieving().steal(Stall.FUR, objectType, location);
			break;
		case 22189:
			c.getThieving().steal(Stall.SILVER, objectType, location);
			break;
		case 14011:
			c.getThieving().steal(Stall.WINE, objectType, location);
			break;
		case 11744:
		case 27718:
		case 27719:
		case 27720:
		case 27721:
		case 28594:
		case 28595:
			c.getPA().openUpBank();
			break;
		case 11727:
		case 11726:
			if (System.currentTimeMillis() - c.lastLockPick < 1000 || c.freezeTimer > 0) {
				return;
			}
			c.lastLockPick = System.currentTimeMillis();
			if (c.getItems().playerHasItem(1523, 1)) {

				if (Misc.random(10) <= 2) {
					c.sendMessage("You fail to pick the lock.");
					break;
				}
				if (c.objectX == 3044 && c.objectY == 3956) {
					if (c.absX == 3045) {
						c.getPA().walkTo(-1, 0);
					} else if (c.absX == 3044) {
						c.getPA().walkTo(1, 0);
					}

				} else if (c.objectX == 3038 && c.objectY == 3956) {
					if (c.absX == 3037) {
						c.getPA().walkTo(1, 0);
					} else if (c.absX == 3038) {
						c.getPA().walkTo(-1, 0);
					}
				} else if (c.objectX == 3041 && c.objectY == 3959) {
					if (c.absY == 3960) {
						c.getPA().walkTo(0, -1);
					} else if (c.absY == 3959) {
						c.getPA().walkTo(0, 1);
					}
				} else if (c.objectX == 3191 && c.objectY == 3963) {
					if (c.absY == 3963) {
						c.getPA().walkTo(0, -1);
					} else if (c.absY == 3962) {
						c.getPA().walkTo(0, 1);
					}
				} else if (c.objectX == 3190 && c.objectY == 3957) {
					if (c.absY == 3957) {
						c.getPA().walkTo(0, 1);
					} else if (c.absY == 3958) {
						c.getPA().walkTo(0, -1);
					}
				}
			} else {
				c.sendMessage("I need a lockpick to pick this lock.");
			}
			break;
		case 17010:
			if (c.playerMagicBook == 0) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 28062);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 1) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 28064);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 28060);
				c.playerMagicBook = 0;
				c.autocasting = false;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			break;
		/*
		 * One stall that will give different amount of money depending on your
		 * thieving level, also different amount of xp.
		 */

		case 2781:
		case 26814:
		case 11666:
		case 6189:
		case 26300:
			c.getSmithing().sendSmelting();
			break;
		/*
		 * case 2646: Flax.pickFlax(c, obX, obY); break;
		 */

		/**
		 * Opening the bank.
		 */
		case 10083:
		case 14367:
		case 11758:
		case 10517:
		case 26972:
		case 25808:
		case 6943:
		case 40859:
			c.getPA().openUpBank();
			break;

		}
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (Config.SERVER_DEBUG == true) {
			c.sendMessage("<col=255>[SERVER DEBUG]Third Click Object: " + objectType + " " + obX + " " + obY);
		}

		c.clickObjectType = 0;
		// c.sendMessage("Object type: " + objectType);
		switch (objectType) {
		case 11338:
			c.getPA().openUpBank();
			break;
		case 51884:
			c.getPA().openUpBank();
		break;
		default:
			if (c.getRights().isDeveloper() && Config.SERVER_DEBUG) {
				c.sendMessage("First Click Mob: " + objectType + " " + obX + " " + obY);
			}
		//	ScriptManager.callFunc("objectClick3_" + objectType, c, objectType, obX, obY);
			break;
		}
	}

	public void firstClickNpc(int npcType, int i) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		/*
		 * if (ImpCatching.catchImpling(c, npcType)) { return; }
		 */

		if (c.getRights().equals(Rights.DEVELOPER)) {
			c.sendMessage("<col=255>[SERVER DEBUG] First Click Mob: " + npcType);
		}
		int impling = c.npcClickIndex;
		c.clickNpcType = 0;
		c.rememberNpcIndex = c.npcClickIndex;
		c.npcClickIndex = 0;

		if (Hunter.impData.implings.containsKey(npcType)) {
			impData.Catch(c, npcType, impling);
		}
		if (PetHandler.bankPet(c, npcType)) {
			return;
		}
		if (PetHandler.talktoPet(c, npcType))
			return;
		/*
		 * if (npcType == 311) { c.start(new AdamDialogue(c)); }
		 */
		switch (npcType) {
		case 1025:
			c.getDH().sendDialogues(3617, npcType);	
		break;	
		case 1571:
			c.getDH().sendDialogues(3616, npcType);	
		break;	
		case 4933:
			c.getDH().sendDialogues(3615, npcType);	
		break;	
		case 3479:
			c.getDH().sendDialogues(3613, npcType);	
		break;	
                case 2150:
			c.getDH().sendDialogues(2500000, npcType);	
			c.getPA().requestUpdates();
		    c.getPA().loadQuests();
		break;	
		case 4064:
			c.getShops().openShop(77);
		break;
		case 402:
			c.getDH().sendDialogues(3607, npcType);	
		break;	
		case 637:
			c.getShops().openShop(5);
			break;
		case 527:
			c.getShops().openShop(3);
			break;
		case 401:
		//	c.getSlayer().generateTask();
		c.start(new SlayerAssignmentDialogue());
		break;
		case 7053:
		if(c.getItems().playerHasItem(13204, 5000)) {
			c.getDH().sendDialogues(200, npcType);
			break;
		} else {
			c.sendMessage("You need atleast 5K platnium tokens to reset a skill.");
			break;	
		}
		case 5919:
			c.getShops().openShop(200);
		break;
		case 7236:
			c.getShops().openShop(210);
		break;
		case DecantingDialogue.NPC_ID:
			c.start(new DecantingDialogue());
			break;
		/*case ConverterDialogue.NPC_ID:
			c.start(new ConverterDialogue());
			break;*/
		case ZenyteDialogue.NPC_ID:
			c.start(new ZenyteDialogue());
			break;
		case 4407:
			if (!c.ironman) {
				return;
			} else {
				c.getDH().sendDialogues(1100000, 4407);
			}
			break;
		case 4018:
			c.getDH().sendDialogues(836, 4018);
			break;
		case 4227:
			if (c.hungerGames && (c.playerLevel[c.playerHitpoints] > ((c.getPA().getLevelForXP(c.playerXP[c.playerHitpoints]) / 2) + 1))) {
				c.getDH().sendDialogues(820, 4227);
			} else {
				c.getDH().sendDialogues(823, 4227);
			}
			break;
		case 5165: // blood
			if (c.hungerGames) {
				PlayerWrapper p = HungerManager.getSingleton().getWrapper(c);
				if (p == null)
					break;
				if (!p.hasDiamonds[0]) {
					if (c.getItems().playerHasItem(PlayerWrapper.DIAMONDS[0])) {
						c.getDH().sendDialogues(831, 5165);
						c.getItems().deleteItem(PlayerWrapper.DIAMONDS[0], 1);
						p.hasDiamonds[0] = true;
						int amount = 15;
						if(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[0]) + amount > 99) {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(99), 0);
						} else {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP
									(p.getPlayer().playerXP[0]) + amount)
									- p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[0])), 0, false);
						}
						if(p.hasAllDiamonds())
							HungerManager.getSingleton().playerHasAllDiamonds(p);
					} else {
						c.getDH().sendDialogues(827, 5165);
					}
				} else {
					c.getDH().sendDialogues(826, 5165);
				}
			} else {
				c.sendMessage("The goblin doesn't seem very interested...");
			}
			break;
		case 5166: // ice
			if (c.hungerGames) {
				PlayerWrapper p = HungerManager.getSingleton().getWrapper(c);
				if (p == null)
					break;
				if (!p.hasDiamonds[1]) {
					if (c.getItems().playerHasItem(PlayerWrapper.DIAMONDS[1])) {
						c.getDH().sendDialogues(831, 5165);
						c.getItems().deleteItem(PlayerWrapper.DIAMONDS[1], 1);
						p.hasDiamonds[1] = true;
						int amount = 15;
						if(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[1]) + amount > 99) {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(99), 1);
						} else {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP
									(p.getPlayer().playerXP[1]) + amount)
									- p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[1])), 1, false);
						}
						if (p.hasAllDiamonds())
							HungerManager.getSingleton().playerHasAllDiamonds(p);
					} else {
						c.getDH().sendDialogues(828, 5165);
					}
				} else {
					c.getDH().sendDialogues(826, 5165);
				}
			} else {
				c.sendMessage("The goblin doesn't seem very interested...");
			}
			break;
		case 5167: // smoke
			if (c.hungerGames) {
				PlayerWrapper p = HungerManager.getSingleton().getWrapper(c);
				if (p == null)
					break;
				if (!p.hasDiamonds[2]) {
					if (c.getItems().playerHasItem(PlayerWrapper.DIAMONDS[2])) {
						c.getDH().sendDialogues(831, 5165);
						c.getItems().deleteItem(PlayerWrapper.DIAMONDS[2], 1);
						p.hasDiamonds[2] = true;
						int amount = 15;
						if(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[2]) + amount > 99) {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(99), 2);
						} else {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP
									(p.getPlayer().playerXP[2]) + amount)
									- p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[2])), 2, false);
						}
						if(p.hasAllDiamonds())
							HungerManager.getSingleton().playerHasAllDiamonds(p);
					} else {
						c.getDH().sendDialogues(829, 5165);
					}
				} else {
					c.getDH().sendDialogues(826, 5165);
				}
			} else {
				c.sendMessage("The goblin doesn't seem very interested...");
			}
			break;
		case 5168: // shadow
			if (c.hungerGames) {
				PlayerWrapper p = HungerManager.getSingleton().getWrapper(c);
				if (p == null)
					break;
				if (!p.hasDiamonds[3]) {
					if (c.getItems().playerHasItem(PlayerWrapper.DIAMONDS[3])) {
						c.getDH().sendDialogues(831, 5165);
						c.getItems().deleteItem(PlayerWrapper.DIAMONDS[3], 1);
						p.hasDiamonds[3] = true;
						int amount = 15;
						if(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[3]) + amount > 99) {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(99), 3);
						} else {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP
									(p.getPlayer().playerXP[3]) + amount)
									- p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[3])), 3, false);
						}
						if(p.hasAllDiamonds())
							HungerManager.getSingleton().playerHasAllDiamonds(p);
					} else {
						c.getDH().sendDialogues(830, 5165);
					}
				} else {
					c.getDH().sendDialogues(826, 5165);
				}
			} else {
				c.sendMessage("The goblin doesn't seem very interested...");
			}
			break;
		case 317:
			if (c.ironman)
				c.getShops().openShop(70);
			else
				c.sendMessage("You must be an ironman to use this shop.");
			break;
		case 311:
			if (c.ironman)
				c.getShops().openShop(69);
			else
				c.sendMessage("You must be an ironman to use this shop.");
			break;
		case 5519:
			if (c.ironman)
				c.getShops().openShop(71);
			else
				c.sendMessage("You must be an ironman to use this shop.");
			break;
		case 2914:
			if (c.getItems().playerHasItem(11824)) {
				c.getDH().sendDialogues(1651, 2914);
			} else if (c.getItems().playerHasItem(11889)) {
				c.getDH().sendDialogues(1652, 2914);
			} else
				c.getDH().sendDialogues(1655, 2914);
			break;
		case 5721:
			c.getDH().sendDialogues(812, 5721);
			break;
		case 5442:
			if (!c.hasAccountPin) {
				c.getDH().sendDialogues(400001, 5442);
			} else if (c.hasAccountPin) {
				c.getDH().sendDialogues(400003, 5442);
			}
			break;
		case 306:
			c.getDH().sendDialogues(1610, 306);
			break;
		case 1108:
			c.getDH().sendDialogues(840,1108);
			break;
		case 493:
			c.clickNpcType = 50;
			c.sendMessage("LELELELEL");
			break;
		case 7069:
			TeleportExecutor.teleport(c, new Teleport(new Position(1470, 3860, 0), TeleportType.NORMAL));
			c.dialogueAction = -1;
			c.teleAction = -1;
			break;
		case 13: // Piles
			c.getDH().sendDialogues(3600, 13);
			break;
		case 3985:
			TeleportExecutor.teleport(c, new Teleport(new Position(3001, 3374, 0), TeleportType.NORMAL));
			c.dialogueAction = -1;
			c.teleAction = -1;
			break;
		case 7071:
			TeleportExecutor.teleport(c, new Teleport(new Position(1449, 3860, 0), TeleportType.NORMAL));
			c.dialogueAction = -1;
			c.teleAction = -1;
			break;
		case 4733:
			c.getShops().openShop(63);
			break;
		case 2997:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getDH().sendDialogues(3500, 2997);
			break;

		case 4070:
			if (c.fishTourneySession != null) {
				if (c.fishTourneySession.running)
					c.getDH().sendDialogues(808, 4070);
				else
					c.getDH().sendDialogues(810, 4070);
			} else {
				c.getDH().sendDialogues(800, 4070);
			}
			break;

		case 7072:
			c.getShops().openShop(57);
			break;
		case 5518:
			if ((System.currentTimeMillis() - c.actionTimer) <= 5000) {
				c.sendMessage("Please give some time before checking again.");
			} else {
				c.actionTimer = System.currentTimeMillis();
				c.rspsdata(c, c.playerName);
				c.sendMessage("Searching For Donation Under Username: " + c.playerName);
			}
			break;

		case 1846:
			c.getDH().sendDialogues(5444, 1846);
			break;

		case 1847:
			c.getDH().sendDialogues(648, 1847);
			break;
		case 3984:
			c.getDH().sendDialogues(6190, 3984);
			break;
		case 2040:
			c.getDH().sendDialogues(638, -1);
			break;
		case 2580:
			c.getDH().sendDialogues(629, 2580);
			break;
		case 2184:
			c.getShops().openShop(29);
			break;
		case 6601:
			NPC golem = NPCHandler.npcs[c.rememberNpcIndex];
			if (golem != null) {
				c.getMining().mine(golem, Mineral.RUNITE,
						new Location3D(golem.getX(), golem.getY(), golem.heightLevel));
			}
			break;
		case 1850:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(116);
			break;
		case 3257:
			c.getThieving().steal(Pickpocket.FARMER, NPCHandler.npcs[c.rememberNpcIndex]);
			break;
		case 3894:
			c.getShops().openShop(26);
			break;
		case 3220:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(25);
			break;

		case 732:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(16);
			break;
		case 520:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(114);
			break;
		case 2400:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(60);
			break;
		case 1944:// armour
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(8);
			break;
		case 2575: //pkp exchange
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(111);
			break;
		case 3415:// range
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(4);
			break;
		case 5118:// magic
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(5);
			break;
		case 4850:// Food and pot
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(61);
			break;
		case 5037:// Fashionscape
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(65);
			break;
		case 3833:// crafting and fletching
			c.getShops().openShop(62);
			break;
		case 997:// Hunter and runecrafting Shop
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(67);
			break;
		case 2949:
			c.getPestControlRewards().showInterface();
			break;
		case 2461:
			c.getWarriorsGuild().handleDoor();
			break;
		/*
		 * case 490: NieveDialogue.beginConversation(c, npcType); break; case
		 * 394: BankerDialogue.beginConversation(c, npcType); break; case 1308:
		 * DiangoDialogue.beginConversation(c, npcType); break;
		 */
		case 490:
			c.getDH().sendDialogues(3300, c.npcType);
			break;
		case 405:
			if (c.playerLevel[Skill.SLAYER.getId()] >= 85) {
				c.getDH().sendDialogues(699, c.npcType);
				return;
			}
			c.getDH().sendDialogues(701, c.npcType);
			break;
		case 3113:
			if (c.insure == true) {
				c.getDH().sendDialogues(3806, c.npcType);
				return;
			}
			c.getDH().sendDialogues(3800, c.npcType);
			break;
		case 1308:
			c.getDH().sendDialogues(538, npcType);
			break;
		/*
		 * case 315: c.getDH().sendDialogues(548, 315); break;
		 */
		case 315:
			c.getDH().sendDialogues(539, npcType);
			break;
		/*
		 * case 5523: HatiusDialogue.beginConversation(c, npcType); break;
		 */
		case 3951:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(78);
			c.sendMessage("<col=255>Please suggest new items to the shop at ::forums");
			break;
		case 954:
			c.getDH().sendDialogues(619, npcType);
			break;
		case 3341: // I prefer ''Doctor Orbon" For the NPC id, it is 290
			c.getDH().sendDialogues(2603, 959);
			break;

		case 1306:
		case 1307:
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
			break;
		case 6080:
			c.getDH().sendDialogues(14400, npcType);
			break;
		case 5523:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(116);
			break;
		case 2995:
			c.getDH().sendDialogues(3608, npcType);
			break;
		case 403:
			c.getDH().sendDialogues(3609, npcType);
			break;
		case 3667:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(12);
			break;
		case 4397:
			c.getShops().openShop(12);
			c.sendMessage("You have: @blu@"+c.pkp+"@bla@ PK Points.");
		break;
		case 2460:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(88);
			break;
		case 4342:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(66);
			break;

		case 944:
			c.getShops().openShop(77);
			// c.getDH().sendDialogues(12400, -1);
			break;

		case 4771:
			c.getDH().sendDialogues(2401, 4771);
			break;
		case 3789:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(75);
			break;
		// FISHING
		case 3913: // NET + BAIT
			// Fishing.attemptdata(c, 1);
			Fishing.startFishing(c, 1, i);
			break;
		/*
		 * case 3317: Fishing.attemptdata(c, 14); break;
		 */
		case 4712:
			// Fishing.attemptdata(c, 15);
			Fishing.startFishing(c, 8, i);
			break;
		case 1524:
			// Fishing.attemptdata(c, 11);
			Fishing.startFishing(c, 10, i);
			break;
		case 3417: // TROUT
			Fishing.startFishing(c, 3, i);
			break;
		case 3657:
			// Fishing.attemptdata(c, 8);
			Fishing.startFishing(c, 6, i);
			break;
		case 635:
			// Fishing.attemptdata(c, 13); // DARK CRAB FISHING
			Fishing.startFishing(c, 13, i);
			break;
		case 1520: // LURE
		case 310:
		case 314:
		case 318:
		case 328:
		case 331:
			// Fishing.attemptdata(c, 9);
			Fishing.startFishing(c, 7, i);
			break;

		case 559:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(16);
			break;
		case 5809:
			Tanning.sendTanningInterface(c);
			break;

		case 2913:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(22);
			break;
		case 1599:
			if (c.slayerTask <= 0) {
				c.getDH().sendDialogues(11, npcType);
				// c.sendMessage("Slayer will be enabled in some minutes.");
			} else {
				c.sendMessage("You must complete or reset your slayer task before start another.");
			}
			break;

		case 953: // Banker
		case 2574: // Banker
		case 166: // Gnome Banker
		case 1702: // Ghost Banker
		case 494: // Banker
		case 495: // Banker
		case 496: // Banker
		case 497: // Banker
		case 498: // Banker
		case 499: // Banker
		case 567: // Banker
		case 766: // Banker
		case 1036: // Banker
		case 1360: // Banker
		case 2163: // Banker
		case 2164: // Banker
		case 2354: // Banker
		case 2355: // Banker
		case 2568: // Banker
		case 2569: // Banker
		case 2570: // Banker
		case 27718:
		case 27719:
		case 27720:
		case 27721:
			c.getPA().openUpBank();
			break;
		case 1986:
			c.getDH().sendDialogues(2244, c.npcType);
			break;
		case 5792:
			c.getDH().sendDialogues(5792, c.npcType);
			break;
		case 1011:
			//Gambler
			c.getDH().sendDialogues(1011, c.npcType);
			break;

		case 536:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(48);
			break;

		case 1785:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(8);
			break;

		case 1860:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(47);
			break;

		case 519:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(48);
			break;

		case 548:
			c.getDH().sendDialogues(69, c.npcType);
			break;

		case 2258:
			c.getDH().sendOption2("Teleport me to Runecrafting Abyss.", "I want to stay here, thanks.");
			c.dialogueAction = 2258;
			break;

		case 532:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(47);
			break;

		case 535:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(8);
			break;

		case 506:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(2);
			break;
		case 507:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(1);
			break;
		default:
			String dialogueNPC = NPCCacheDefinition.forID(npcType).getName().toLowerCase().replaceAll(" ", "_");
			c.sendDebugMessage("Attempting to call NPC dialogue from repository: " + dialogueNPC);
			// c.bootlegDialogue().start(dialogueNPC + "_dialogue", (Object)
			// null);
			break;

		/*
		 * case 198: c.getShops().openSkillCape(); break;
		 */

		}
	}

	public void secondClickNpc(int npcType, int i) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (ImpCatching.catchImpling(c, npcType)) {
			return;
		}
		if (c.getRights().equals(Rights.DEVELOPER)) {
			c.sendMessage("<col=255>[SERVER DEBUG] Second Click Mob: " + npcType);
		}
		c.clickNpcType = 0;
		c.rememberNpcIndex = c.npcClickIndex;
		c.npcClickIndex = 0;

		/*
		 * if(Fishing.fishingNPC(c, npcType)) { Fishing.fishingNPC(c, 2,
		 * npcType); return; }
		 */

		if (PetHandler.pickupPet(c, npcType))
			return;
		switch (npcType) {
		case 5792:
			//player.outStream.createFrame(27);
			//player.lotteryAmount = true;
			LotteryController.newEntryPKP(c, c.npcType);
			break;
		case 1025:
			c.getShops().openShop(59);
		break;
		case 1571:
			c.getShops().openShop(54);
		break;
		case 4933:
			c.getShops().openShop(55);
		break;
		 case 2150:
		     PlayerShops.openMainInterface(c);
		     c.getPA().requestUpdates();
		     c.getPA().loadQuests();
	    	 break;
		case 3479:
			TitleHandler.open(c);
			break;
		case 4064:
			TitleHandler.open(c);
			break;
		case 5721:
			c.getShops().openShop(74);
			break;
		case 403:
			c.getShops().openShop(51);
			break;
		case 2995:
			c.getShops().openShop(49);
			break;
		case 637:
			c.getShops().openShop(4);
			break;
//		case 527:
//			//c.getDH().sendDialogues(947, 527);
//			c.getShops().openShop(111);
//			break;
		case 4397:
			c.getShops().openShop(49);
		break;
		case 5523:
				c.getShops().openShop(9);
				c.sendMessage("You have: @blu@"+c.donatorPoints+" Donator Points.");
			break;
		case 5919:
			c.getShops().openShop(200);
		break;
		case ConverterDialogue.NPC_ID:
			c.getShops().openShop(ConverterDialogue.SHOP_ID);
			break;
		case ZenyteDialogue.NPC_ID:
			c.start(new ZenyteDialogue());
			break;
		case 4018:
			c.getShops().openShop(99);
			break;
		case 490:
			c.getDH().sendOption3("Easy Task", "Medium Task", "Hard Task");
			c.dialogueAction = 3700;
			break;

		case 7071:
			c.getShops().openShop(72);
			c.sendMessage("Current Blast Points:" + c.blastPoints);
			break;

		case 311:
			if (c.ironman)
				c.getShops().openShop(69);
			else
				c.sendMessage("You must be an ironman to use this shop.");
			break;

		case 317:
			if (c.ironman)
				c.getShops().openShop(70);
			else
				c.sendMessage("You must be an ironman to use this shop.");
			break;
		case 402:
				c.getShops().openShop(29);
			break;
		case 405:
			if (c.playerLevel[Skill.SLAYER.getId()] >= 85) {
				c.needsNewBossTask = true;
				c.getBossSlayer().generateTask();
				return;
			}
			c.getDH().sendDialogues(701, c.npcType);
			break;
		/*
		 * case 5523: HatiusDialogue.startConversation(c, npcType); break;
		 */
		case 3343:
			if (c.getRights().getValue() == 0) {
				c.sendMessage("This is a donator only feature! ::donate");
				return;
			}
			long duration = 0;
			if (c.playerLevel[3] < Player.getLevelForXP(c.playerXP[3])) {
				c.playerLevel[3] = Player.getLevelForXP(c.playerXP[3]);
				c.getPA().refreshSkill(3);
				c.sendMessage("Your hitpoints have been Restored.");
			} else {
				c.sendMessage("You already have full Hitpoints.");
			}
			if (c.inWild()) {
				return;
			}
			if (c.getRights().getValue() >= 1) {
				c.specAmount = 0.0;
				c.animation(645);
				c.spawnSet = System.currentTimeMillis();
				c.specAmount += 10.0;
				c.getItems().updateSpecialBar();
				c.getItems().addSpecialBar(c.playerEquipment[c.playerWeapon]);
				c.sendMessage("You have restored your spec!");
			}
			c.setPoisonDamage((byte) 0);
			c.setVenomDamage((byte) 0);
			break;

		case 3515:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(77);
			break;
		case 2184:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(29);
			break;
		case 3891:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(11);
			break;
		case 2580:
			TeleportExecutor.teleport(c, new Teleport(new Position(3039, 4835, 0), TeleportType.NORMAL));
			c.dialogueAction = -1;
			c.teleAction = -1;
			break;
		case 3894:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(26);
			break;
		case 3257:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(16);
			break;
		case 3078:
			c.getThieving().steal(Pickpocket.MAN, NPCHandler.npcs[c.rememberNpcIndex]);
			break;
		case 2180:
			if (c.getItems().playerHasItem(6570, 1)) {
				c.getDH().sendDialogues(650, -1);
			} else {
				c.getDH().sendStatement("You need a firecape to use this option");
			}
			break;
		case 520:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(114);
			break;
		case 732:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(16);
			break;
		case 2575: //pkp exchange
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(111);
			break;
		case 5809:
			c.getShops().openShop(20);
			break;
		case 315:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(80);
			break;
		case 1308:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(79);
			break;
		case 3341: // I prefer ''Doctor Orbon" For the NPC id, it is 290
			if (c.getRights().isPlayer()) {
				c.sendMessage("You need to be a donator to use this future!");
				return;
			}
			if (c.playerLevel[3] < Player.getLevelForXP(c.playerXP[3]))
				c.playerLevel[3] = Player.getLevelForXP(c.playerXP[3]);
			c.getPA().refreshSkill(3);
			c.sendMessage("@red@Your hitpoints have been restored!");
			break;
		case 535:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(8);
			break;
		case 4771:
			c.getDH().sendDialogues(2400, -1);
			break;
		case 3913: // BAIT + NET
			// Fishing.attemptdata(c, 2);
			Fishing.startFishing(c, 2, i);
			break;
		case 310:
		case 314:
		case 318:
		case 328:
		case 329:
		case 331:
		case 3417: // BAIT + LURE //PIKE
			// Fishing.attemptdata(c, 6);
			Fishing.startFishing(c, 4, i);
			break;
		case 1524:
			Fishing.startFishing(c, 12, i);
			break;
		case 3657:
		case 321:
		case 324:// SWORDIES+TUNA-CAGE+HARPOON
			// Fishing.attemptdata(c, 7);
			Fishing.startFishing(c, 5, i);
			break;
		case 1520:
		case 322:
		case 334: // NET+HARPOON
			// Fishing.attemptdata(c, 10);
			Fishing.startFishing(c, 9, i);
			break;
		case 4928: // Sacred Eel

			break;
		case 532:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(47);
			break;
		case 1599:
			c.getShops().openShop(10);
			c.sendMessage("You currently have @red@" + c.slayerPoints + " @bla@slayer points.");
			break;
		case 953: // Banker
		case 2574: // Banker
		case 166: // Gnome Banker
		case 1702: // Ghost Banker
		case 494: // Banker
		case 495: // Banker
		case 496: // Banker
		case 497: // Banker
		case 498: // Banker
		case 499: // Banker
		case 394:
		case 567: // Banker
		case 766:
		case 1036: // Banker
		case 1360: // Banker
		case 2163: // Banker
		case 2164: // Banker
		case 2354: // Banker
		case 2355: // Banker
		case 2568: // Banker
		case 2569: // Banker
		case 2570: // Banker
		case 27718:
		case 27719:
		case 27720:
		case 27721:
			c.getPA().openUpBank();
			break;

		case 1785:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(8);
			break;

		case 536:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(48);
			break;

		case 3796:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(6);
			break;

		case 1860:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(6);
			break;

		case 519:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(7);
			break;

		case 548:
			c.getDH().sendDialogues(69, c.npcType);
			break;

		case 2258:
			c.sendMessage("They do not seem interested in trading.");
			// c.getPA().startTeleport(3039, 4834, 0, "modern"); //first click
			// teleports second click open shops
			break;

		case 506:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(2);
			break;

		case 528:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(116);
			break;

		}
	}

	public void thirdClickNpc(int npcType) {
		if (c.getRights().equals(Rights.DEVELOPER)) {
			c.sendMessage("<col=255>[SERVER DEBUG] Third Click Mob: " + npcType);
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (ImpCatching.catchImpling(c, npcType)) {
			return;
		}
		c.clickNpcType = 0;
		c.rememberNpcIndex = c.npcClickIndex;
		c.npcClickIndex = 0;
		switch (npcType) {
		case 2055:
			if (c.summonId == 11995)
				TeleportExecutor.teleport(c, new Position(Config.RESPAWN_X, Config.RESPAWN_Y, 0), false);
			break;
		case 5892:
		case 6636:
			if (c.summonId == 13225 || c.summonId == 12653) {
				PetHandler.withdrawBOB(c);
			}
			break;
		case 5792:
			c.getDH().sendDialogues(5794, c.npcType);
			break;
		case 1025:
			c.getShops().openShop(60);
		break;
		case 1571:
			c.getShops().openShop(57);
		break;
		case 4933:
			c.getShops().openShop(56);
		break;
		case 2150:
			  PlayerShops.openShop(c, c);
			  c.getPA().requestUpdates();
		      c.getPA().loadQuests();
	    	 break;
		case 403:
			c.getShops().openShop(52);
			break;
		case 2995:
			c.getShops().openShop(50);
			break;
		case 527:
			//c.getDH().sendDialogues(947, 527);
			c.getShops().openShop(113);
			break;
		case 4397:
			c.getShops().openShop(50);
			c.sendMessage("You have: @blu@"+c.pkp+"@bla@ PK Points.");
		break;
		case 405:
		case 490:
			c.getShops().openShop(44);
			c.sendMessage("I currently have <col=255>" + c.slayerPoints + " <col=000000>slayer points.");
			break;
		case 402:
			c.getShops().openShop(38);
			break;
		case 315:
			c.getDH().sendDialogues(548, 315);
			break;
		case 5721:
			c.getShops().openShop(74);
			break;
		case 1599:
			c.getShops().openShop(10);
			c.sendMessage("You currently have <col=ff0000>" + c.slayerPoints + " <col=000000>slayer points.");
			break;
		case 548:
			if (!c.ardiRizal()) {
				c.sendMessage("You must remove your equipment before changing your appearence.");
				c.canChangeAppearance = false;
			} else {
				c.getPA().showInterface(3559);
				c.canChangeAppearance = true;
			}
			break;

		case 836:
			if (c.ironman) {
				c.sendMessage("You cannot open this shop as an ironman");
				return;
			} else
				c.getShops().openShop(103);
			break;

		}
	}

	public void operateNpcAction4(int npcType) {
		System.out.println("i got here");
		if (c.getRights().equals(Rights.DEVELOPER)) {
			c.sendMessage("<col=255>[SERVER DEBUG] Fourth Click Mob: " + npcType);
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickNpcType = 0;
		c.rememberNpcIndex = c.npcClickIndex;
		c.npcClickIndex = 0;

		switch (npcType) {
		case 5892:
			if (c.summonId == 13225) {
				PetHandler.withdrawBOB(c);
			}
			break;
		case 6636:
			if (c.summonId == 12653) {
				PetHandler.withdrawBOB(c);
			}
			break;
		case 1571:
			c.getShops().openShop(58);
		break;
		case 2150:
			 PlayerShops.openSellInterface(c);
			 c.getPA().requestUpdates();
		     c.getPA().loadQuests();
		     break;
		case 402:
			c.getShops().openShop(40);
			break;
		case 403:
			c.getShops().openShop(53);
			break;
		//case 402:
		case 405:
		case 490:
			c.getSlayer().handleInterface("buy");
			break;

		case 315:
			//c.getDH().sendDialogues(545, npcType);
			c.sendMessage("@blu@If you want to become skulled, type @red@::skull, @blu@to have your skull removed, type @red@::unskull@bla@.");
			break;
		}
	}
		public void operateNpcAction5(int npcType) {
			if (c.getRights().equals(Rights.DEVELOPER)) {
				c.sendMessage("<col=255>[SERVER DEBUG] First Click Mob: " + npcType);
			}
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				return;
			}
			c.clickNpcType = 0;
			c.rememberNpcIndex = c.npcClickIndex;
			c.npcClickIndex = 0;

			switch (npcType) {
			case 403:
				c.getShops().openShop(54);
				break;

			}
	}

}