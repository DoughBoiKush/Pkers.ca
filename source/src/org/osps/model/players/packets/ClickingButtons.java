package org.osps.model.players.packets;

import java.text.DecimalFormat;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.text.WordUtils;
import org.osps.Config;
import org.osps.Server;
import org.osps.data.SerializablePair;
import org.osps.model.content.QuickPrayer;
import org.osps.model.content.Skillcape;
import org.osps.model.content.TeleportTab;
import org.osps.model.content.dialogue.DialogueManager;
import org.osps.model.content.dialogue.teleport.Teleports;
import org.osps.model.content.help.HelpDatabase;
import org.osps.model.content.newTitles.TitleHandler;
import org.osps.model.content.quickspawn.Hybrid4;
import org.osps.model.content.quickspawn.Hybrid5;
import org.osps.model.content.quickspawn.Hybrid6;
import org.osps.model.content.quickspawn.MainGranite;
import org.osps.model.content.quickspawn.MainRune;
import org.osps.model.content.quickspawn.MainRuneMace;
import org.osps.model.content.quickspawn.PureMelee;
import org.osps.model.content.quickspawn.PureMeleeRange;
import org.osps.model.content.quickspawn.PureTribrid;
import org.osps.model.content.quickspawn.QuickSpawn;
import org.osps.model.content.quickspawn.Tank;
import org.osps.model.content.quickspawn.VoidSet;
import org.osps.model.content.quickspawn.Zerker;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.Tablets;
import org.osps.model.content.teleport.Teleport;
import org.osps.model.content.teleport.Teleport.TeleportType;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.items.ItemCombination;
import org.osps.model.items.ItemDefinition;
import org.osps.model.items.bank.BankItem;
import org.osps.model.items.bank.BankTab;
import org.osps.model.minigames.FishingTourney;
import org.osps.model.minigames.bounty_hunter.BountyHunterEmblem;
import org.osps.model.minigames.gamble.GamblePKP;
import org.osps.model.minigames.lottery.LotteryController;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.multiplayer_session.duel.DuelSessionRules.Rule;
import org.osps.model.npcs.NPCDeathTracker.NPCName;
import org.osps.model.npcs.NpcDefinition;
import org.osps.model.npcs.PetHandler;
import org.osps.model.players.Boundary;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerAssistant.PointExchange;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.Rights;
import org.osps.model.players.combat.Degrade;
import org.osps.model.players.combat.Degrade.DegradableItem;
import org.osps.model.players.combat.Hitmark;
import org.osps.model.players.combat.melee.CombatPrayer;
import org.osps.model.players.skills.Smelting;
import org.osps.model.players.skills.cooking.Cooking;
import org.osps.model.players.skills.crafting.CraftingData.tanningData;
import org.osps.model.players.skills.crafting.LeatherMaking;
import org.osps.model.players.skills.crafting.Tanning;
import org.osps.model.players.skills.fletching.Fletching;
import org.osps.model.shops.playerOwned.PlayerShops;
import org.osps.net.Packet;
import org.osps.util.Misc;

/**
 * Clicking most buttons
 *
 */
public class ClickingButtons implements PacketType {

	@Override
	public void processPacket(Player player, Packet packet) {
		int buttonId = packet.hexToInt();
		System.out.println(buttonId);
		if (!player.canUsePackets) {
			return;
		}
		if (player.isDead || player.playerLevel[3] <= 0) {
			return;
		}

		if (player.getRights().isDeveloper() || (player.getRights().isOwner())) {
			Misc.println(player.playerName + " - actionbutton: " + buttonId);
		}
		if (player.getRights().isDeveloper() || (player.getRights().isOwner())) {
		player.sendMessage("Clicked: "+buttonId);
		}
		if (TitleHandler.clickButton(player, buttonId)) {
			return;
		}

		if (Teleports.onButton(player, buttonId)) {
			return;
		}

		if (player.getInterfaceEvent().isActive()) {
			player.getInterfaceEvent().clickButton(buttonId);
			return;
		}

		if (buttonId >= 232182 && buttonId <= 233022) {
			HelpDatabase.getDatabase().view(player, buttonId);
			HelpDatabase.getDatabase().delete(player, buttonId);

			return;
		}
                
                if (TeleportTab.clickButton(player, buttonId)) {
                    return;
                }

		player.getPestControlRewards().click(buttonId);
		
		if (player.getDialogue() != null && player.getDialogue().clickButton(buttonId)) {
			return;
		}

		if (player.craftDialogue) {
			LeatherMaking.craftLeather(player, buttonId);
		}

		for (tanningData t : tanningData.values()) {
			if (buttonId == t.getButtonId(buttonId)) {
				Tanning.tanHide(player, buttonId);
				return;
			}
		}
		
		if (buttonId >= 100121 && buttonId <= 100166) { // for pre-made kits
			if (player.ironman) {
				player.sendMessage("You cannot use this function as an ironman mode player.");
				return;
			}
			if (player.isExtreme) {
				player.sendMessage("You cannot use this function as an extreme mode player.");
				return;
			}
			QuickSpawn.handleActionButtons(player, buttonId);
			return;
		}
		
		  if (player.getPresets().clickButton(buttonId)) { return; }
		 
		int[] spellIds = { 4128, 4130, 4132, 4134, 4136, 4139, 4142, 4145, 4148, 4151, 4153, 4157, 4159, 4161, 4164,
				4165, 4129, 4133, 4137, 6006, 6007, 6026, 6036, 6046, 6056, 4147, 6003, 47005, 4166, 4167, 4168, 48157,
				50193, 50187, 50101, 50061, 50163, 50211, 50119, 50081, 50151, 50199, 50111, 50071, 50175, 50223, 50129,
				50091 };
		for (int i = 0; i < spellIds.length; i++) {
			if (buttonId == spellIds[i]) {
				player.autocasting = true;
				player.autocastId = i;
			}
		}
		/*
		 * if (Server.getHolidayController().clickButton(c, actionButtonId)) {
		 * return; }
		 */
		if (player.getPunishmentPanel().clickButton(buttonId)) {
			return;
		}
		DuelSession duelSession = null;
		if (player.playerFletch) {
			Fletching.attemptData(player, buttonId);
		}
		QuickPrayer.clickButton(player, buttonId);

		switch (buttonId) {
                    
                case 175057:
                    PlayerShops.confirmSale(player);
                    break;
                    
        //QuestTab//
          case 62155:
          if (player.clickTimer > 0)
              return;
              player.clickTimer = 2;
              if (System.currentTimeMillis() < player.muteEnd) {
                  player.sendMessage("You have been muted due to breaking a rule.");
                  player.sendMessage("To prevent further mutes please read the rules.");
              return;
              }
              player.forceChat("["+player.playerName+"] Amount Donated "+player.amDonated+"$!");
              break;
              
         case 62158:
         if (player.clickTimer > 0)
             return;
             player.clickTimer = 2;
             if (System.currentTimeMillis() < player.muteEnd) {
                 player.sendMessage("You have been muted due to breaking a rule.");
                 player.sendMessage("To prevent further mutes please read the rules.");
             return;
        	 }
        	 player.forceChat("["+player.playerName+"] I have "+player.KC+" Kills! ");
        	 break;
        	 
         case 62159:
         if (player.clickTimer > 0)
             return;
             player.clickTimer = 2;
             if (System.currentTimeMillis() < player.muteEnd) {
                 player.sendMessage("You have been muted due to breaking a rule.");
                 player.sendMessage("To prevent further mutes please read the rules.");
             return;
             }
             player.forceChat("["+player.playerName+"] I have "+player.DC+" Deaths! ");
            	 break;
            	 
         case 62160:
         if (player.clickTimer > 0)
             return;
             player.clickTimer = 2;
             if (System.currentTimeMillis() < player.muteEnd) {
                 player.sendMessage("You have been muted due to breaking a rule.");
                 player.sendMessage("To prevent further mutes please read the rules.");
             return;
             }
             DecimalFormat df = new DecimalFormat("#.##");
			 double KDR = ((double) player.KC) / ((double) player.DC);
	         player.forceChat("["+player.playerName+"] I have "+player.KC+" Kills,and "+player.DC+" Deaths -  "+ df.format(KDR) +". ");
             break; 
             
         case 62162:
         if (player.clickTimer > 0)
             return;
             player.clickTimer = 2;
             if (System.currentTimeMillis() < player.muteEnd) {
                 player.sendMessage("You have been muted due to breaking a rule.");
                 player.sendMessage("To prevent further mutes please read the rules.");
             return;
             }
             player.forceChat("["+player.playerName+"] I have "+player.pkp+" Pk Points!  ");
 			 player.sendMessage("You currently have @red@"+player.pkp+" Pk Points!  ");
             break;  
             
         case 62163:
         if (player.clickTimer > 0)
             return;
             player.clickTimer = 2;
             if (System.currentTimeMillis() < player.muteEnd) {
                 player.sendMessage("You have been muted due to breaking a rule.");
                 player.sendMessage("To prevent further mutes please read the rules.");
             return;
             }
             player.forceChat("["+player.playerName+"] I have "+player.donatorPoints+" Donator Points!  ");
             player.sendMessage("You currently have @red@"+player.donatorPoints+" Donator Points!  ");
             break; 
             
         case 62164:
         if (player.clickTimer > 0)
             return;
             player.clickTimer = 2;
             if (System.currentTimeMillis() < player.muteEnd) {
                 player.sendMessage("You have been muted due to breaking a rule.");
                 player.sendMessage("To prevent further mutes please read the rules.");
             return;
             }
             player.forceChat("["+player.playerName+"] I have "+player.votePoints+" Vote Points!  ");
             player.sendMessage("You currently have @red@"+player.votePoints+" Vote Points!  ");
             break;
             
//         case 62165:
//         if (player.clickTimer > 0)
//             return;
//             player.clickTimer = 2;
//             if (System.currentTimeMillis() < player.muteEnd) {
//                 player.sendMessage("You have been muted due to breaking a rule.");
//                 player.sendMessage("To prevent further mutes please read the rules.");
//             return;
//             }
//             player.forceChat("["+player.playerName+"] I have "+player.hungerPoints+" Sacrifice Points!  ");
//             player.sendMessage("You currently have @red@"+player.hungerPoints+" Sacrifice Points!  ");
//             break; 
             
         case 62167:
         if (player.clickTimer > 0)
             return;
             player.clickTimer = 2;
             player.sendMessage("You currently have @red@"+player.blackMarks+" @bla@Black Marks,once you reach @blu@5 @bla@you get banned.  ");
             break; 
             
         case 62169:
         if (player.clickTimer > 0)
             return;
             player.clickTimer = 2;
             if (System.currentTimeMillis() < player.muteEnd) {
                 player.sendMessage("You have been muted due to breaking a rule.");
                 player.sendMessage("To prevent further mutes please read the rules.");;
             return;
             }
             player.forceChat("["+player.playerName+"] I am currently on a "+player.killStreak+" Kill Streak!");
             break; 
                    
         case 62170:
         if (player.clickTimer > 0)
             return;
             player.clickTimer = 2;
             if (System.currentTimeMillis() < player.muteEnd) {
                 player.sendMessage("You have been muted due to breaking a rule.");
                 player.sendMessage("To prevent further mutes please read the rules.");
             return;
             }
             player.forceChat("["+player.playerName+"] My highest Kill Streak is "+player.highestKillStreak+"!");
             break;  
             
		/** QuestTab button **/
		case 64046:
			 player.setSidebarInterface(2, 638);
			break;
		/** AchievementTab button **/
		case 63002:
			player.setSidebarInterface(2, 16300);
			break;
		/** Yell on/off **/
		case 62174:
			player.getPA().yellon = !player.getPA().yellon;
			player.getPA().loadQuests();
			break;
		/** Preloaded gear setup button on questtab **/
		case 62181:
			player.getPA().showInterface(32000);
			player.getPA().loadQuests();
			break;
		/** Action interface system **/
		case 58210:
		if (player.clickTimer > 0)
            return;
        player.clickTimer = 2;
		player.getPA().closeAllWindows();
		player.getPA().showInterface(59525);
		player.getPA().loadQuests();
		break;
		case 58212:
			if (player.clickTimer > 0)
                return;
            player.clickTimer = 2;
			if(player.getRights().getValue() == 0) {
				player.sendMessage("This is a donator only function.");
				return;
			}
			if (player.inTrade || player.inDuelArena()) {
				return;
            }
            if (player.inWild()) {
                player.sendMessage("You can't do this while in the wilderness.");
                return;
            }
			player.getShops().openShop(81);	
			player.getPA().loadQuests();
		break;
		case 58213:
			if (player.clickTimer > 0)
                return;
            player.clickTimer = 2;
			if(player.getRights().getValue() == 0) {
			   player.sendMessage("This is a donator only function.");
			   return;
			}
			if (player.inTrade || player.inDuelArena()) {
				return;
            }
			if (player.inWild()) {
	            player.sendMessage("You can't do this while in the wilderness.");
	            return;
	            }
				player.getPA().movePlayer(2525, 4776, 0);
				player.sendMessage("@blu@Welcome to the Donator Zone!");
				player.getPA().loadQuests();
		        break;
		case 58214:
			if (player.clickTimer > 0) 
            return;
		    player.clickTimer = 2;
			if (player.getRights().HigherDonator()) {
			   player.sendMessage("You need to be higher rank than Donator to use this.");
			   return;
			}
			if (player.inTrade || player.inDuelArena()) {
				return;
            }
			if (player.inWild()) {
	            player.sendMessage("You can't do this while in the wilderness.");
	            return;
	            }
			    player.getPA().openUpBank();
			    player.getPA().loadQuests();
		        break;
		
		/** Title Shop close button **/
		case 101146:
		player.getPA().closeAllWindows();
		player.getPA().loadQuests();
		break;
		
	/** donator packages **/
		
		case 12149:
		player.sendMessage("Huge Package Costs 150 Donator Points.");
		break;
		
		
		case 12132:
		if(player.donatorPoints >= 150){ 
		player.getItems().addItem(15350,1);
		player.donatorPoints -= 150;
		player.getPA().loadQuests();
		player.sendMessage("You bought Huge Package,Thanks for donating!");
		return;
		} else
		player.sendMessage("You must have atleast 150 Donator Points to buy this package.");
		player.sendMessage("To Donate please do ::donate or got to www.osps.com/donate");
		break;
		
		case 12143:
		player.sendMessage("Rare Package Costs 50 Donator Points.");
		break;
		
		case 12134:
		if(player.donatorPoints >= 50){ 
		player.getItems().addItem(15351,1);
		player.donatorPoints -= 50;
		player.getPA().loadQuests();
		player.sendMessage("You bought Rare Package,Thanks for donating!");
		return;
		} else
		player.sendMessage("You must have atleast 50 Donator Points to buy this package.");
		player.sendMessage("To Donate please do ::donate or got to www.osps.com/donate");
		break;
		
		
		case 12146:
		player.sendMessage("H'ween Package Costs 50 Donator Points.");
		break;
		
		case 12136:
		if(player.donatorPoints >= 50){ 
		player.getItems().addItem(15349,1);
		player.donatorPoints -= 50;
		player.getPA().loadQuests();
		player.sendMessage("You bought H'ween Package,Thanks for donating!");
		return;
		} else
		player.sendMessage("You must have atleast 50 Donator Points to buy this package.");
		player.sendMessage("To Donate please do ::donate or got to www.osps.com/donate");
		break;
		
		
		case 12157:
		player.sendMessage("Party Package Costs 50 Donator Points.");
		break;
		
		case 12153:
		if(player.donatorPoints >= 50){ 
		player.getItems().addItem(15348,1);
		player.donatorPoints -= 50;
		player.getPA().loadQuests();
		player.sendMessage("You bought Party Package,Thanks for donating!");
		return;
		} else
		player.sendMessage("You must have atleast 50 Donator Points to buy this package.");
		player.sendMessage("To Donate please do ::donate or got to www.osps.com/donate");
		break;
		
		case 12163:
		player.sendMessage("Pking Package Costs 10 Donator Points.");
		break;
		
		case 12168:
		if(player.donatorPoints >= 10){
		player.getItems().addItem(15352,1);
		player.donatorPoints -= 10;
		player.getPA().loadQuests();
		player.sendMessage("You bought Pking Package,Thanks for donating!");
		return;
		} else
		player.sendMessage("You must have atleast 10 Donator Points to buy this package.");
		player.sendMessage("To Donate please do ::donate or got to www.osps.com/donate");
		break;
		
		case 12188:
		player.sendMessage("@red@This is a automatic Donation System,after you donate Please do ::reward!");
		break;
		
		/** end packages **/
		
		case 54185:
			player.gfx0(342);
			player.getPA().movePlayer(3179, 9758, 0);
			player.getPA().closeAllWindows();
		break;
		case 54201:
			player.gfx0(342);
			player.getPA().movePlayer(2525, 4776, 0);
			player.getPA().closeAllWindows();
		break;
		case 54193:
			player.gfx0(342);
			player.getPA().movePlayer(3109, 3700, 0);
			player.getPA().closeAllWindows();
		break;
		case 54209:
			player.gfx0(342);
			player.getPA().movePlayer(2739, 3464, 0);
			player.getPA().closeAllWindows();
		break;
		case 54197:
			player.gfx0(342);
			player.getPA().movePlayer(3159, 3730, 0);
			player.getPA().closeAllWindows();
		break;
		case 54205:
			player.gfx0(342);
			player.getPA().movePlayer(3368, 3266, 0);
			player.getPA().closeAllWindows();
		break;
		case 54178:
			player.gfx0(342);
			player.getPA().movePlayer(2540, 4714, 0);
			player.getPA().closeAllWindows();
		break;
		case 54189:
			player.gfx0(342);
			player.getPA().movePlayer(2980, 3866, 0);
			player.getPA().closeAllWindows();
		break;
		case 33206:
			if (player.isExtreme && !Player.attack) {
				player.sendMessage(
						"You are an extreme player and need to level to @blu@99@bla@ Attack before using this!");
				return;
			}
			player.outStream.createFrame(27);
			player.attackSkill = true;
			break;
		case 33209:
			if (player.isExtreme && !Player.strength) {
				player.sendMessage(
						"You are an extreme player and need to level to @blu@99@bla@ Strength before using this!");
				return;
			}
			player.outStream.createFrame(27);
			player.strengthSkill = true;
			break;
		case 33212:
			if (player.isExtreme && !Player.defence) {
				player.sendMessage(
						"You are an extreme player and need to level to @blu@99@bla@ Defence before using this!");
				return;
			}
			player.outStream.createFrame(27);
			player.defenceSkill = true;
			break;
		case 33215:
			if (player.isExtreme && !Player.range) {
				player.sendMessage(
						"You are an extreme player and need to level to @blu@99@bla@ Range before using this!");
				return;
			}
			player.outStream.createFrame(27);
			player.rangeSkill = true;
			break;
		case 33218:
			if (player.isExtreme && !Player.prayerLevel) {
				player.sendMessage(
						"You are an extreme player and need to level to @blu@99@bla@ Prayer before using this!");
				return;
			}
			player.outStream.createFrame(27);
			player.prayerSkill = true;
			break;
		case 33221:
			if (player.isExtreme && !Player.magic) {
				player.sendMessage(
						"You are an extreme player and need to level to @blu@99@bla@ Magic before using this!");
				return;
			}
			player.outStream.createFrame(27);
			player.mageSkill = true;
			break;
		case 33207:
			if (player.isExtreme && !Player.hitpoints) {
				player.sendMessage(
						"You are an extreme player and need to level to @blu@99@bla@ Hitpoints before using this!");
				return;
			}
			player.outStream.createFrame(27);
			player.healthSkill = true;
			break;
		case 66249:
			player.getPA().sendConfig(542, 0);
			break;
		case 72096: //Allow Teleport
			player.getPA().getClan().allowTeleport(player);
			break;
		case 72099: //Allow Copy kit
			player.getPA().getClan().allowCopyKit(player);
			break;
		case 74108:
			Skillcape.performEmote(player, buttonId);
		break;

		case 75007:
			player.getDH().sendDialogues(1000000, 1);
			break;

		case 33190:
			LeatherMaking.craftLeather(player, buttonId);
			break;
		case 9118:
                case 171226:
			player.getPA().closeAllWindows();
			break;
		case 14067:
			player.setAppearanceUpdateRequired(true);
			player.getPA().closeAllWindows();
			break;
		case 19136:
			QuickPrayer.toggle(player);
			break;
		case 19137:
			player.setSidebarInterface(5, 17200);
			break;
		/*
		 * case 141004: c.setSidebarInterface(5, 24500); break;
		 */
		case 109157:
		case 109159:
		case 109161:
			player.getBH().teleportToTarget();
			break;
		case 162235:
			player.getPA().closeAllWindows();
			break;
		case 55095:// This is the button id
			player.getPA().destroyItem(player.droppedItem);// Choosing Yes will
															// delete the
			// item and make it
			// dissapear
			player.droppedItem = -1;
			break;
		case 55096:// This is the button id
			player.getPA().removeAllWindows();// Choosing No will remove all the
												// windows
			player.droppedItem = -1;
			break;
		case 113249:
			for (int i = 8144; i < 8195; i++) {
				player.getPA().sendFrame126("", i);
			}
			int[] frames = { 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160, 8161, 8162, 8163,
					8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175 };
			player.getPA().sendFrame126("@dre@Boss Kills for @blu@" + player.playerName + "", 8144);
			player.getPA().sendFrame126("", 8145);
			player.getPA().sendFrame126("@blu@Total Boss kills: @red@" + player.getNpcDeathTracker().getTotal() + "",
					8147);
			player.getPA().sendFrame126("", 8148);
			int index1 = 0;
			for (Entry<NPCName, Integer> entry : player.getNpcDeathTracker().getTracker().entrySet()) {
				if (entry == null) {
					continue;
				}
				if (index1 > frames.length - 1) {
					break;
				}
				if (entry.getValue() > 0) {
					player.getPA()
							.sendFrame126("@blu@"
									+ WordUtils.capitalize(entry.getKey().name().toLowerCase().replaceAll("_", " "))
									+ " kills: @red@" + entry.getValue(), frames[index1]);
					index1++;
				}
			}
			player.getPA().showInterface(8134);
			break;
	/*	case 113243:
			DecimalFormat df = new DecimalFormat("#.##");
			Double KDR = ((double)player.KC)/((double)player.DC);
			player.forceChat("I have "+df.format(KDR)+" Kill Death Ratio!");
			break;*/
		case 113244:
			player.forceChat("I have "+player.slayerPoints+" Slayer Points!");
			break;
		case 113242:
			player.forceChat("I have "+player.KC+"/"+player.DC+" Kills/deaths!");
			break;
		case 113245:
			player.forceChat("I have "+player.votePoints+" vote points!");
			break;
		case 113247:
			for (int i = 8144; i < 8195; i++) {
				player.getPA().sendFrame126("", i);
			}
			player.getPA().sendFrame126("@dre@Slayer statistics for @blu@" + player.playerName + "", 8144);
			player.getPA().sendFrame126("", 8145);
			player.getPA().sendFrame126("@blu@Slayer points: @red@" + player.slayerPoints + "", 8147);
			player.getPA().sendFrame126("", 8148);
			player.getPA().sendFrame126("", 8149);
			if (player.slayerTask > 0 || player.bossSlayerTask > 0) {
				NpcDefinition def = NpcDefinition.DEFINITIONS[(player.slayerTask > 0 ? player.slayerTask
						: player.bossSlayerTask)];
				player.getPA().sendFrame126("@blu@Current task: @red@" + def.getName() + "", 8150);
				player.getPA().sendFrame126("", 8151);
				player.getPA()
						.sendFrame126(
								"@blu@Task location: @red@"
										+ (player.slayerTask > 0 ? player.getEasy().getLocation(player.slayerTask)
												: player.slayerTask > 0
														? player.getMedium().getLocation(player.slayerTask)
														: player.getHard().getLocation(player.slayerTask))
										+ "",
								8152);
				player.getPA().sendFrame126("", 8153);
				player.getPA().sendFrame126("@blu@Kills remaining: @red@"
						+ (player.slayerTask > 0 ? player.taskAmount : player.bossTaskAmount) + "", 8154);
			} else {
				player.getPA().sendFrame126("@blu@Current task: @red@No task currently assigned.", 8150);
			}
			player.getPA().showInterface(8134);
			break;
		case 113248:
			for (int i = 8144; i < 8195; i++) {
				player.getPA().sendFrame126("", i);
			}
			player.getPA().sendFrame126("@dre@PVP Slayer statistics for @blu@" + player.playerName + "", 8144);
			player.getPA().sendFrame126("", 8145);
			player.getPA().sendFrame126("@blu@Slayer points: @red@" + player.slayerPoints + "", 8147);
			player.getPA().sendFrame126("", 8148);
			player.getPA().sendFrame126("", 8149);
				if (player.slayerPvPTask > 0) {
				player.getPA().sendFrame126("@blu@Current task: @red@ Kill Players within the wilderness", 8150);
				player.getPA().sendFrame126("", 8151);
				player.getPA().sendFrame126("", 8153);
				player.getPA().sendFrame126("@blu@Kills remaining: @red@"
						+ player.pvpTaskAmount + "", 8154);
			} else {
				player.getPA().sendFrame126("@blu@Current task: @red@No task currently assigned.", 8150);
			}
			player.getPA().showInterface(8134);
			break;
		case 20174:
			player.getPA().closeAllWindows();
			org.osps.model.items.bank.BankPin pin = player.getBankPin();
			if (pin.getPin().length() <= 0)
				player.getBankPin().open(1);
			else if (!pin.getPin().isEmpty() && !pin.isAppendingCancellation())
				player.getBankPin().open(3);
			else if (!pin.getPin().isEmpty() && pin.isAppendingCancellation())
				player.getBankPin().open(4);
			break;

		case 226162:
			if (player.getPA().viewingOtherBank) {
				player.getPA().resetOtherBank();
				return;
			}
			if (!player.isBanking)
				return;
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			for (int slot = 0; slot < player.playerItems.length; slot++) {
				if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {
					player.getItems().addToBank(player.playerItems[slot] - 1, player.playerItemsN[slot], false);
				}
			}
			player.getItems().updateInventory();
			player.getItems().resetBank();
			player.getItems().resetTempItems();
			break;

		case 226170:
			if (player.getPA().viewingOtherBank) {
				player.getPA().resetOtherBank();
				return;
			}
			if (!player.isBanking)
				return;
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			if (player.getBankPin().requiresUnlock()) {
				player.isBanking = false;
				player.getBankPin().open(2);
				return;
			}
			for (int slot = 0; slot < player.playerEquipment.length; slot++) {
				if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
					player.getItems().addEquipmentToBank(player.playerEquipment[slot], slot,
							player.playerEquipmentN[slot], false);
					player.getItems().wearItem(-1, 0, slot);
				}
			}
			player.getItems().updateInventory();
			player.getItems().resetBank();
			player.getItems().resetTempItems();
			break;

		case 226186:
		case 226198:
		case 226209:
		case 226220:
		case 226231:
		case 226242:
		case 226253:
		case 227008:
		case 227019:
			if (!player.isBanking) {
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getBankPin().requiresUnlock()) {
				player.isBanking = false;
				player.getBankPin().open(2);
				return;
			}
			int tabId = buttonId == 226186 ? 0
					: buttonId == 226198 ? 1
							: buttonId == 226209 ? 2
									: buttonId == 226220 ? 3
											: buttonId == 226231 ? 4
													: buttonId == 226242 ? 5
															: buttonId == 226253 ? 6
																	: buttonId == 227008 ? 7
																			: buttonId == 227019 ? 8 : -1;
			if (tabId <= -1 || tabId > 8)
				return;
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset(tabId);
				return;
			}
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			BankTab tab = player.getBank().getBankTab(tabId);
			if (tab.getTabId() == player.getBank().getCurrentBankTab().getTabId())
				return;
			if (tab.size() <= 0 && tab.getTabId() != 0) {
				player.sendMessage("Drag an item into the new tab slot to create a tab.");
				return;
			}
			player.getBank().setCurrentBankTab(tab);
			player.getPA().openUpBank();
			break;

		case 226197:
		case 226208:
		case 226219:
		case 226230:
		case 226241:
		case 226252:
		case 227007:
		case 227018:
			if (player.getPA().viewingOtherBank) {
				player.getPA().resetOtherBank();
				return;
			}
			if (!player.isBanking) {
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getBankPin().requiresUnlock()) {
				player.isBanking = false;
				player.getBankPin().open(2);
				return;
			}
			tabId = buttonId == 226197 ? 1
					: buttonId == 226208 ? 2
							: buttonId == 226219 ? 3
									: buttonId == 226230 ? 4
											: buttonId == 226241 ? 5
													: buttonId == 226252 ? 6
															: buttonId == 227007 ? 7 : buttonId == 227018 ? 8 : -1;
			tab = player.getBank().getBankTab(tabId);
			if (tab == null || tab.getTabId() == 0 || tab.size() == 0) {
				player.sendMessage("You cannot collapse this tab.");
				return;
			}
			if (tab.size() + player.getBank().getBankTab()[0].size() >= Config.BANK_SIZE) {
				player.sendMessage("You cannot collapse this tab. The contents of this tab and your");
				player.sendMessage("main tab are greater than " + Config.BANK_SIZE + " unique items.");
				return;
			}
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			for (BankItem item : tab.getItems()) {
				player.getBank().getBankTab()[0].add(item);
			}
			tab.getItems().clear();
			if (tab.size() == 0) {
				player.getBank().setCurrentBankTab(player.getBank().getBankTab(0));
			}
			player.getPA().openUpBank();
			break;

		case 226185:
		case 226196:
		case 226207:
		case 226218:
		case 226229:
		case 226240:
		case 226251:
		case 227006:
		case 227017:
			if (player.getPA().viewingOtherBank) {
				player.getPA().resetOtherBank();
				return;
			}
			if (!player.isBanking) {
				player.getPA().removeAllWindows();
				return;
			}
			if (player.getBankPin().requiresUnlock()) {
				player.isBanking = false;
				player.getBankPin().open(2);
				return;
			}
			if (player.getBank().getBankSearch().isSearching()) {
				player.getBank().getBankSearch().reset();
				return;
			}
			tabId = buttonId == 226185 ? 0
					: buttonId == 226196 ? 1
							: buttonId == 226207 ? 2
									: buttonId == 226218 ? 3
											: buttonId == 226229 ? 4
													: buttonId == 226240 ? 5
															: buttonId == 226251 ? 6
																	: buttonId == 227006 ? 7
																			: buttonId == 227017 ? 8 : -1;
			tab = player.getBank().getBankTab(tabId);
			long value = 0;
			if (tab == null || tab.size() == 0)
				return;
			for (BankItem item : tab.getItems()) {
				player.getShops();
				// long tempValue = item.getId() - 1 == 995 ? 1 :
				// ShopAssistant.getItemShopValue(item.getId() - 1);
				long tempValue = item.getId() - 1 == 995 ? 1
						: ItemDefinition.forId(item.getId() - 1).getSpecialPrice();
				value += tempValue * item.getAmount();
			}
			player.sendMessage("<col=255>The total networth of tab " + tab.getTabId() + " is </col><col=600000>"
					+ Long.toString(value) + " gp</col>.");
			break;

		case 22024:
		case 86008:
			player.getPA().openUpBank();
			break;

		// TODO:
		case 226154:
			player.takeAsNote = !player.takeAsNote;
			break;
		case 113238:
			player.forceChat("I am currently on a " + player.killStreak + " kill streak!");
			break;
		case 2151:
			player.forceChat("I currently have " + player.donatorPoints + " donating points.");
			break;
		case 113239:
			//player.getPA().sendFrame126("", 12000);
			break;
		case 113240:
			player.forceChat("I currently have " + player.pkp + " pk points.");
			break;
		case 113241:
			//player.getPA().sendFrame126("", 12000);
			player.forceChat("I currently have " + player.donatorPoints + " donating points.");
			break;
		case 164034:
			player.removedTasks[0] = -1;
			player.getSlayer().updateCurrentlyRemoved();
			break;

		case 164035:
			player.removedTasks[1] = -1;
			player.getSlayer().updateCurrentlyRemoved();
			break;

		case 164036:
			player.removedTasks[2] = -1;
			player.getSlayer().updateCurrentlyRemoved();
			break;

		case 164037:
			player.removedTasks[3] = -1;
			player.getSlayer().updateCurrentlyRemoved();
			break;

		case 164028:
			player.getSlayer().cancelTask();
			break;
		case 164029:
			player.getSlayer().removeTask();
			break;
		case 160052:
			player.getSlayer().buySlayerExperience();
			break;
		case 160053:
			player.getSlayer().buyRespite();
			break;
		case 160054:
			player.getSlayer().buySlayerDart();
			break;
		case 160055:
			player.getSlayer().buyBroadArrows();
			break;

		case 160045:
		case 162033:
		case 164021:
			if (player.interfaceId != 41000)
				player.getSlayer().handleInterface("buy");
			break;

		case 160047:
		case 162035:
		case 164023:
			if (player.interfaceId != 41500)
				player.getSlayer().handleInterface("learn");
			break;

		case 160049:
		case 162037:
		case 164025:
			if (player.interfaceId != 42000)
				player.getSlayer().handleInterface("assignment");
			break;

		case 162030:
		case 164018:
		case 160042:
			player.getPA().removeAllWindows();
			break;
		case 113236:
			player.forceChat("I currently have: " + player.votePoints + " Vote Points.");
			break;
		case 113243:
			DecimalFormat df1 = new DecimalFormat("#.##");
			double ratio = ((double) player.KC) / ((double) player.DC);
			player.forceChat("I have a total of " + player.KC + " kills and " + player.DC
					+ " deaths. Kill/Death ratio is: " + df1.format(ratio) + "");
			break;
		case 113246:
			player.forceChat("I have donated a total of: " + player.amDonated + " to Pkers.Ca");
			break;
		case 15147:// Bronze, 1
			Smelting.startSmelting(player, buttonId, 0, 0);
			break;
		case 15146:// Bronze, 5
			Smelting.startSmelting(player, buttonId, 0, 1);
			break;
		case 10247:// Bronze, 10
			Smelting.startSmelting(player, buttonId, 0, 2);
			break;
		case 9110:// Bronze, 28
			Smelting.startSmelting(player, buttonId, 0, 3);
			break;
		case 15151:// Iron, 1
			Smelting.startSmelting(player, buttonId, 1, 0);
			break;
		case 15150:// Iron, 5
			Smelting.startSmelting(player, buttonId, 1, 1);
			break;
		case 15149:// Iron, 10
			Smelting.startSmelting(player, buttonId, 1, 2);
			break;
		case 15148:// Iron, 28
			Smelting.startSmelting(player, buttonId, 1, 3);
			break;
		case 15155:// silver, 1
			Smelting.startSmelting(player, buttonId, 2, 0);
			break;
		case 15154:// silver, 5
			Smelting.startSmelting(player, buttonId, 2, 1);
			break;
		case 15153:// silver, 10
			Smelting.startSmelting(player, buttonId, 2, 2);
			break;
		case 15152:// silver, 28
			Smelting.startSmelting(player, buttonId, 2, 3);
			break;
		case 15159:// steel, 1
			Smelting.startSmelting(player, buttonId, 3, 0);
			break;
		case 15158:// steel, 5
			Smelting.startSmelting(player, buttonId, 3, 1);
			break;
		case 15157:// steel, 10
			Smelting.startSmelting(player, buttonId, 3, 2);
			break;
		case 15156:// steel, 28
			Smelting.startSmelting(player, buttonId, 3, 3);
			break;
		case 15163:// gold, 1
			Smelting.startSmelting(player, buttonId, 4, 0);
			break;
		case 15162:// gold, 5
			Smelting.startSmelting(player, buttonId, 4, 1);
			break;
		case 15161:// gold, 10
			Smelting.startSmelting(player, buttonId, 4, 2);
			break;
		case 15160:// gold, 28
			Smelting.startSmelting(player, buttonId, 4, 3);
			break;
		case 29017:// mithril, 1
			Smelting.startSmelting(player, buttonId, 5, 0);
			break;
		case 29016:// mithril, 5
			Smelting.startSmelting(player, buttonId, 5, 1);
			break;
		case 24253:// mithril, 10
			Smelting.startSmelting(player, buttonId, 5, 2);
			break;
		case 16062:// mithril, 28
			Smelting.startSmelting(player, buttonId, 5, 3);
			break;
		case 29022:// addy, 1
			Smelting.startSmelting(player, buttonId, 6, 0);
			break;
		case 29021:// addy, 5
			Smelting.startSmelting(player, buttonId, 6, 1);
			break;
		case 29019:// addy, 10
			Smelting.startSmelting(player, buttonId, 6, 2);
			break;
		case 29018:// addy, 28
			Smelting.startSmelting(player, buttonId, 6, 3);
			break;
		case 29026:// rune, 1
			Smelting.startSmelting(player, buttonId, 7, 0);
			break;
		case 29025:// rune, 5
			Smelting.startSmelting(player, buttonId, 7, 1);
			break;
		case 29024:// rune, 10
			Smelting.startSmelting(player, buttonId, 7, 2);
			break;
		case 29023:// rune, 28
			Smelting.startSmelting(player, buttonId, 7, 3);
			break;

		/*
		 * case 58025: case 58026: case 58027: case 58028: case 58029: case
		 * 58030: case 58031: case 58032: case 58033: case 58034:
		 * c.getBankPin().pinEnter(actionButtonId); break;
		 */

		case 53152:
			Cooking.getAmount(player, 1);
			break;
		case 53151:
			Cooking.getAmount(player, 5);
			break;
		case 53150:
			Cooking.getAmount(player, 10);
			break;
		case 53149:
			Cooking.getAmount(player, 28);
			break;
		case 33224: // runecrafting
			player.getSI().runecraftingComplex(1);
			player.getSI().selected = 6;
			break;
		case 33210: // agility
			player.getSI().agilityComplex(1);
			player.getSI().selected = 8;
			break;
		case 33213: // herblore
			player.getSI().herbloreComplex(1);
			player.getSI().selected = 9;
			break;
		case 33216: // theiving
			player.getSI().thievingComplex(1);
			player.getSI().selected = 10;
			break;
		case 33219: // crafting
			player.getSI().craftingComplex(1);
			player.getSI().selected = 11;
			break;
		case 33222: // fletching
			player.getSI().fletchingComplex(1);
			player.getSI().selected = 12;
			break;
		case 47130:// slayer
			player.getSI().slayerComplex(1);
			player.getSI().selected = 13;
			break;
		case 33208:// mining
			player.getSI().miningComplex(1);
			player.getSI().selected = 14;
			break;
		case 33211: // smithing
			player.getSI().smithingComplex(1);
			player.getSI().selected = 15;
			break;
		case 33214: // fishing
			player.getSI().fishingComplex(1);
			player.getSI().selected = 16;
			break;
		case 33217: // cooking
			player.getSI().cookingComplex(1);
			player.getSI().selected = 17;
			break;
		case 33220: // firemaking
			player.getSI().firemakingComplex(1);
			player.getSI().selected = 18;
			break;
		case 33223: // woodcut
			player.getSI().woodcuttingComplex(1);
			player.getSI().selected = 19;
			break;
		case 54104: // farming
			player.getSI().farmingComplex(1);
			player.getSI().selected = 20;
			break;

		// 44 Portals
		// TeleportExecutor.teleport(c, new Position(2986 + Misc.random(1), 3860
		// + Misc.random(1), 0));
		// break;

		// Mage Bank
		// TeleportExecutor.teleport(c, new Position(2539 + Misc.random(1), 4715
		// + Misc.random(1), 0));
		// break;

		// TeleportExecutor.teleport(c, new Position(3011 + Misc.random(1), 3632
		// + Misc.random(1), 0));
		// break;

		// Graveyard
		// TeleportExecutor.teleport(c, new Position(3140 + Misc.random(1), 3670
		// + Misc.random(1), 0));
		// break;

		/*
		 * case 150: if (c.isUnderAttack) { c.autoRet = 1; } else { c.autoRet =
		 * 0; } break;
		 */
			
		        case 113073:
				player.outStream.createFrame(27);
				player.attackSkill = true;
				break;
		        case 113077:
				player.outStream.createFrame(27);
				player.defenceSkill = true;
				break;
				case 113081:
				player.outStream.createFrame(27);
				player.strengthSkill = true;
				break;
				case 113085:
				player.outStream.createFrame(27);
				player.healthSkill = true;
				break;
				case 113089:
				player.outStream.createFrame(27);
				player.rangeSkill = true;
				break;
				case 113093:
				player.outStream.createFrame(27);
				player.prayerSkill = true;
				break;
				case 113097:
				player.outStream.createFrame(27);
				player.mageSkill = true;
				break;
				case 62154:
				if (player.clickTimer > 0)
		            return;
		            player.clickTimer = 2;
					player.sendMessage("There are currently@red@ "+PlayerHandler.getPlayerCount()+" @bla@Players Online, [@red@ "+PlayerHandler.getStaffCount()+" @bla@Staff Members].  ");
				break;
		case 3138:
			player.setBrightness(1);
			break;
		case 3140:
			player.setBrightness(2);
			break;
		case 3142:
			player.setBrightness(3);
			break;
		case 3144:
			player.setBrightness(4);
			break;
		case 89061:
		case 93202:
		case 94051:
			if (player.autoRet == 0) {
				// player.sendMessage(""+c.autoRet+"");
				player.autoRet = 1;
			} else {
				player.autoRet = 0;
				// player.sendMessage(""+c.autoRet+"");
			}
			break;
		// 1st tele option
		case 9190:
			if (player.dialogueAction == 1033) {
				GamblePKP.gamblePKP(player, 100, player.npcType);
				return;
			}
			if (player.dialogueAction == 75007) {
				player.enchantOpal = true;
				player.getOutStream().createFrame(27);
				return;
			}

			if (player.dialogueAction == 75008) {
				player.enchantEmerald = true;
				player.getOutStream().createFrame(27);
				return;
			}

			if (player.dialogueAction == 128) {
				player.getFightCave().create(1);
				return;
			} else if (player.dialogueAction == 114) {
				if (player.getItems().playerHasItem(6737, 1)
						&& player.pkp >= 100) {
					player.getItems().deleteItem(6737, 1);
					player.pkp -= 100;
					player.getItems().addItem(11773, 1);
					player.sendMessage(
							"You imbue your berserker ring for the cost of @blu@100 @bla@PK Points.");
					player.getPA().loadQuests();
					player.getPA().removeAllWindows();
					player.dialogueAction = 0;
				} else {
					player.sendMessage("You need 100 PK Points and a Berserker Ring to do this.");
					player.getPA().removeAllWindows();
					player.dialogueAction = 0;
				}
			}
			if (player.teleAction == 205) {
				TeleportExecutor.teleport(player, new Position(1469, 3863, 0), true);
			} else if (player.teleAction == 200) {
				TeleportExecutor.teleport(player, new Position(3565, 3314, 0), true);
			} else if (player.teleAction == 201) {
				TeleportExecutor.teleport(player, new Position(2847, 3541, 0), true);
			}
			switch (player.teleAction) {

			case 2:
				TeleportExecutor.teleport(player, new Position(2680, 9563, 0), true);
				break;
			}
			if (player.dialogueAction == 123) {
				DegradableItem[] claimable = Degrade.getClaimedItems(player);
				if (claimable.length == 0) {
					return;
				}
				player.getPA().removeAllWindows();
				Degrade.claim(player, claimable[0].getItemId());
				return;
			}
			if (player.dialogueAction == 14400) {
				TeleportExecutor.teleport(player, new Position(3105, 3279, 0), true);
				return;
			}
			if (player.teleAction == 80) {
				TeleportExecutor.teleport(player, new Position(3429, 3538, 0), true);
				return;
			}
			if (player.teleAction == 65) {
				TeleportExecutor.teleport(player, new Position(2976, 4384, 2), true);
				player.dialogueAction = -1;
				player.teleAction = -1;
			}
			if (player.teleAction == 66) {
				TeleportExecutor.teleport(player, new Position(2872, 9847, 0), true);
				player.dialogueAction = -1;
				player.teleAction = -1;
			}
			if (player.teleAction == 14) {
				TeleportExecutor.teleport(player, new Position(2900, 4449, 0), true);
				player.teleAction = -1;
				player.dialogueAction = -1;
				return;
			}
			if (player.teleAction == 12) {
				TeleportExecutor.teleport(player, new Position(3302, 9361, 0), true);
			}
			if (player.teleAction == 11) {
				TeleportExecutor.teleport(player, new Position(3228, 9392, 0), true);
			}
			if (player.teleAction == 10) {
				TeleportExecutor.teleport(player, new Position(2705, 9487, 0), true);
			}
			if (player.teleAction == 9) {
				TeleportExecutor.teleport(player, new Position(3226, 3263, 0), true);
			}
			if (player.teleAction == 8) {
				TeleportExecutor.teleport(player, new Position(3293, 3178, 0), true);
			}
			if (player.teleAction == 7) {
				TeleportExecutor.teleport(player, new Position(3118, 9851, 0), true);
			}
			if (player.teleAction == 1) {
				// rock crabs
				TeleportExecutor.teleport(player, new Position(3087, 3514, 0), true);
			} else if (player.teleAction == 3) {
				TeleportExecutor.teleport(player, new Position(3005, 3850, 0), true);
			} else if (player.teleAction == 4) {
				// varrock wildy
				TeleportExecutor.teleport(player, new Position(3025, 3379, 0), true);
			} else if (player.teleAction == 5) {
				TeleportExecutor.teleport(player, new Position(3046, 9779, 0), true);
			} else if (player.teleAction == 2000) {
				// lum
				TeleportExecutor.teleport(player, new Position(3222, 3218, 0), true);// 3222
																				// 3218
			} /*
				 * else { DiceHandler.handleClick(c, actionButtonId); }
				 */
			if (player.dialogueAction == 10) {
				TeleportExecutor.teleport(player, new Position(2845, 4832, 0), true);
				player.dialogueAction = -1;

			} else if (player.dialogueAction == 11) {
				TeleportExecutor.teleport(player, new Position(2786, 4839, 0), true);
				player.dialogueAction = -1;
			} else if (player.dialogueAction == 12) {
				TeleportExecutor.teleport(player, new Position(2398, 4841, 0), true);
				player.dialogueAction = -1;
			}
			break;
		// mining - 3046,9779,0
		// smithing - 3079,9502,0

		// 2nd tele option
		case 9191:
			if (player.dialogueAction == 1033) {
				GamblePKP.gamblePKP(player, 250, player.npcType);
				return;
			}
			if (player.dialogueAction == 75007) {
				player.enchantSapphire = true;
				player.getOutStream().createFrame(27);
				return;
			}
			if (player.dialogueAction == 75008) {
				player.enchantTopaz = true;
				player.getOutStream().createFrame(27);
				return;
			}
			if (player.teleAction == 205) {
				// TeleportExecutor.teleport(c, new Position(1834, 3669, 0));
				player.getDH().sendOption2("Hunter (1-50)", "Hunter (50-99)");
				player.teleAction = 3000;
				return;
			}
			if (player.dialogueAction == 128) {
				player.getFightCave().create(2);
				return;
			} else if (player.dialogueAction == 114) {
				if (player.getItems().playerHasItem(6733, 1)
						&& player.pkp >= 100) {
					player.getItems().deleteItem(6733, 1);
					player.pkp -= 100;
					player.getItems().addItem(11771, 1);
					player.sendMessage("You imbue your archer ring for the cost of @blu@100 @bla@PK Points.");
					player.getPA().removeAllWindows();
					player.getPA().loadQuests();
					player.dialogueAction = 0;
				} else {
					player.sendMessage("You need 100 PK Points and a Archer Ring to do this.");
					player.dialogueAction = 0;
					player.getPA().removeAllWindows();
				}
			}
			if (player.teleAction == 66) {
				TeleportExecutor.teleport(player, new Position(3000, 3383, 0), true);
				player.sendMessage("You need a Spade and dig below you!");
				player.dialogueAction = -1;
				player.teleAction = -1;
			} else if (player.teleAction == 200) {
				player.sendMessage("@red@Stake only what you can afford to lose!");
				TeleportExecutor.teleport(player, new Position(3365, 3266, 0), true);
			}
			switch (player.teleAction) {
			case 2:
				TeleportExecutor.teleport(player, new Position(3117, 9856, 0), true);
				break;
			case 205:
				TeleportExecutor.teleport(player, new Position(1469, 3863, 0), true);
				break;
			}
			if (player.teleAction == 80) {
				TeleportExecutor.teleport(player, new Position(2884, 9801, 0), true);
			}
			if (player.dialogueAction == 123) {
				DegradableItem[] claimable = Degrade.getClaimedItems(player);
				if (claimable.length < 2) {
					return;
				}
				player.getPA().removeAllWindows();
				Degrade.claim(player, claimable[1].getItemId());
				return;
			}
			if (player.dialogueAction == 14400) {// Al kharid agil
				TeleportExecutor.teleport(player, new Position(3273, 3197, 0), true);
				return;
			}
			if (player.teleAction == 65) {
				TeleportExecutor.teleport(player, new Position(1495, 3700, 0), true);
				player.dialogueAction = -1;
				player.teleAction = -1;
			}

			if (!player.playerName.equalsIgnoreCase("dylan") && player.teleAction == 14) {
				if (!player.playerName.equalsIgnoreCase("dylan") && player.wildLevel > 20) {
					player.sendMessage("You cannot teleport above 20 wilderness.");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.KRAKEN_CLICKS >= 1) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getKraken().getInstancedKraken() != null) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				player.getKraken().initialize();
				player.KRAKEN_CLICKS = 1;
				player.dialogueAction = -1;
				player.teleAction = -1;
				return;
			}
			if (player.teleAction == 12) {
				TeleportExecutor.teleport(player, new Position(2908, 9694, 0), true);
			}
			if (player.teleAction == 11) {
				TeleportExecutor.teleport(player, new Position(3237, 9384, 0), true);
			}
			if (player.teleAction == 10) {
				TeleportExecutor.teleport(player, new Position(3219, 9366, 0), true);
			}
			if (player.teleAction == 9) {
				TeleportExecutor.teleport(player, new Position(2916, 9800, 0), true);
			}
			if (player.teleAction == 8) {
				TeleportExecutor.teleport(player, new Position(2903, 9849, 0), true);
			}
			if (player.teleAction == 7) {
				TeleportExecutor.teleport(player, new Position(2859, 9843, 0), true);
			}
			if (player.teleAction == 3) {
				TeleportExecutor.teleport(player, new Position(3318, 3834, 0), true);
				return;
			} else
			// player.getPA()().closeAllWindows();
			/*
			 * if (c.teleAction == 1) { //rock crabs
			 * TeleportExecutor.teleport(c, new Position(2676, 3715, 0); } else
			 * if (c.teleAction == 2) { //taverly dungeon
			 * TeleportExecutor.teleport(c, new Position(2884, 9798, 0); } else
			 * if (c.teleAction == 3) { //kbd TeleportExecutor.teleport(c, new
			 * Position(3007, 3849, 0); } else if (c.teleAction == 4) { //west
			 * lv 10 wild TeleportExecutor.teleport(c, new Position(2979, 3597,
			 * 0); } else if (c.teleAction == 5) { TeleportExecutor.teleport(c,
			 * new Position(3079,9502,0); }
			 */
			if (player.teleAction == 1) {
				// slay dungeon
				TeleportExecutor.teleport(player, new Position(2561, 3311, 0), true);
			} else if (player.teleAction == 4) {
				// graveyard
				TeleportExecutor.teleport(player, new Position(3043, 9779, 0), true);
			} else if (player.teleAction == 5) {
				TeleportExecutor.teleport(player, new Position(3079, 9502, 0), true);

			} else if (player.teleAction == 2000) {
				TeleportExecutor.teleport(player, new Position(3210, 3424, 0), true);// 3210
																				// 3424
			} else if (player.dialogueAction == 10) {
				TeleportExecutor.teleport(player, new Position(2796, 4818, 0), true);
				player.dialogueAction = -1;
			} else if (player.dialogueAction == 11) {
				TeleportExecutor.teleport(player, new Position(2527, 4833, 0), true);
				player.dialogueAction = -1;
			} else if (player.dialogueAction == 12) {
				TeleportExecutor.teleport(player, new Position(2464, 4834, 0), true);
				player.dialogueAction = -1;
			}
			// 3rd tele option

		case 9192:
			if (player.dialogueAction == 1033) {
				GamblePKP.gamblePKP(player, 500, player.npcType);
				return;
			}
			if (player.dialogueAction == 75007) {
				player.enchantJade = true;
				player.getOutStream().createFrame(27);
				return;
			}
			if (player.dialogueAction == 75008) {
				player.enchantRuby = true;
				player.getOutStream().createFrame(27);
				return;
			}
			if (player.teleAction == 205) {
				TeleportExecutor.teleport(player, new Position(1590, 3482, 0), true);
				return;
			}
			if (player.teleAction == 200) {
				TeleportExecutor.teleport(player, new Position(2439, 5169, 0), true);
				player.sendMessage("Use the cave entrance to start.");
			} else if (player.dialogueAction == 114) {
				if (player.getItems().playerHasItem(6731, 1)
						&& player.pkp > 100) {
					player.getItems().deleteItem(6731, 1);
					player.pkp -= 100;
					player.getItems().addItem(11770, 1);
					player.sendMessage("You imbue your Seers Ring for the cost of @blu@100 @bla@PK Points.");
					player.getPA().removeAllWindows();
					player.getPA().loadQuests();
					player.dialogueAction = 0;
				} else {
					player.sendMessage("You need 100 PK Points and a Seers Ring to do this.");
					player.getPA().removeAllWindows();
					player.dialogueAction = 0;
				}
			}
			if (player.dialogueAction == 128) {
				player.getFightCave().create(3);
				return;
			}
			switch (player.teleAction) {
			case 2:
				TeleportExecutor.teleport(player, new Position(1749, 5331, 0), true);
				break;
			}
			if (player.dialogueAction == 123) {
				DegradableItem[] claimable = Degrade.getClaimedItems(player);
				if (claimable.length < 3) {
					return;
				}
				player.getPA().removeAllWindows();
				Degrade.claim(player, claimable[2].getItemId());
				return;
			}

			if (player.dialogueAction == 14400) {
				TeleportExecutor.teleport(player, new Position(3223, 3414, 0), true);
				return;
			}
			if (player.teleAction == 80) {// Elven Camp
				TeleportExecutor.teleport(player, new Position(2171, 3125, 0), true);
			}

			if (player.teleAction == 65) {
				TeleportExecutor.teleport(player, new Position(2980, 3705, 0), true);
				player.dialogueAction = -1;
				player.teleAction = -1;
			}
			if (!player.playerName.equalsIgnoreCase("dylan") && player.teleAction == 66) {
				if (player.Digged) {
					player.sendMessage("Kalphite Queen is currently disabled!");
					player.getPA().closeAllWindows();
					return;
				}
				if (!player.playerName.equalsIgnoreCase("dylan") && player.wildLevel > 20) {
					player.sendMessage("You cannot teleport above 20 wilderness.");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.KALPHITE_CLICKS >= 1) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getKalphite().getInstancedKalphite() != null) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				player.getKalphite().initialize();
				player.KALPHITE_CLICKS = 1;
				player.dialogueAction = -1;
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 121) {
				player.getDH().sendDialogues(617, -1);
				player.teleAction = -1;
				player.dialogueAction = -1;
				return;
			}
			if (player.teleAction == 14) {
				TeleportExecutor.teleport(player, new Position(3352, 3730, 0), true);
			}
			if (player.teleAction == 12) {
				TeleportExecutor.teleport(player, new Position(2739, 5088, 0), true);
			}
			if (player.teleAction == 11) {
				TeleportExecutor.teleport(player, new Position(3280, 9372, 0), true);
			}
			if (player.teleAction == 10) {
				TeleportExecutor.teleport(player, new Position(3241, 9364, 0), true);
			}
			if (player.teleAction == 9) {
				TeleportExecutor.teleport(player, new Position(3159, 9895, 0), true);
			}
			if (player.teleAction == 8) {
				TeleportExecutor.teleport(player, new Position(2912, 9831, 0), true);
			}
			if (player.teleAction == 7) {
				TeleportExecutor.teleport(player, new Position(2843, 9555, 0), true);
			}
			if (player.teleAction == 3) {
				player.getDH().sendOption4("Armadyl", "Bandos", "Zamorak", "Saradomin");
				player.teleAction = 13;
			}
			/*
			 * if (c.teleAction == 1) { //experiments
			 * TeleportExecutor.teleport(c, new Position(3555, 9947, 0); } else
			 * if (c.teleAction == 2) { //brimhavem dung
			 * TeleportExecutor.teleport(c, new Position(2709, 9564, 0); } else
			 * if (c.teleAction == 3) { //dag kings TeleportExecutor.teleport(c,
			 * new Position(2479, 10147, 0); } else if (c.teleAction == 4) {
			 * //easts lv 18 TeleportExecutor.teleport(c, new Position(3351,
			 * 3659, 0); } else if (c.teleAction == 5) {
			 * TeleportExecutor.teleport(c, new Position(2813,3436,0); }
			 */
			if (player.teleAction == 1) {
				TeleportExecutor.teleport(player, new Position(3347, 3672, 0), true);
			} else if (player.teleAction == 4) {
				// Hillz
				TeleportExecutor.teleport(player, new Position(2726, 3487, 0), true);
			} else if (player.teleAction == 5) {
				TeleportExecutor.teleport(player, new Position(2813, 3436, 0), true);
			} else if (player.teleAction == 2000) {
				TeleportExecutor.teleport(player, new Position(3222, 3219, 0), true);
			}
			if (player.dialogueAction == 10) {
				TeleportExecutor.teleport(player, new Position(2713, 4836, 0), true);
				player.dialogueAction = -1;
			} else if (player.dialogueAction == 11) {
				TeleportExecutor.teleport(player, new Position(2162, 4833, 0), true);
				player.dialogueAction = -1;
			} else if (player.dialogueAction == 12) {
				TeleportExecutor.teleport(player, new Position(2207, 4836, 0), true);
				player.dialogueAction = -1;
			}
			break;

		// 4th tele option
		case 9193:
			if (player.dialogueAction == 1033) {
				GamblePKP.gamblePKP(player, 1000, player.npcType);
				return;
			}
			if (player.dialogueAction == 75007) {
				player.enchantPearl = true;
				player.getOutStream().createFrame(27);
				return;
			}
			if (player.dialogueAction == 75008) {
				player.enchantDiamond = true;
				player.getOutStream().createFrame(27);
				return;
			}
			if (player.teleAction == 205) {
				TeleportExecutor.teleport(player, new Position(3184, 3945, 0), true);
				player.dialogueAction = -1;
				player.teleAction = -1;
			} else if (player.dialogueAction == 114) {
				if (player.getItems().playerHasItem(6735, 1)
						&& player.pkp >= 60) {
					player.getItems().deleteItem(6735, 1);
					player.pkp -= 60;
					player.getItems().addItem(11772, 1);
					player.sendMessage("You imbue your warrior ring for the cost of @blu@60 @bla@PK Points.");
					player.getPA().removeAllWindows();
					player.dialogueAction = 0;
				} else {
					player.sendMessage("You need 60 PK Points and a Warrior Ring to do this.");
					player.getPA().removeAllWindows();
					player.dialogueAction = 0;
				}
			}
			if (player.teleAction == 200) {
				TeleportExecutor.teleport(player, new Position(2639, 3441, 0), true);
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 128) {
				player.getDH().sendDialogues(634, -1);
				return;
			}
			switch (player.teleAction) {
			case 66:
				TeleportExecutor.teleport(player, new Position(3260, 3927, 0), true);
				player.teleAction = -1;
				player.dialogueAction = -1;
				break;
			case 2:
				TeleportExecutor.teleport(player, new Position(2791, 10018, 0), true);
				break;
			}
			if (player.teleAction == 80) {// neitiznot
				TeleportExecutor.teleport(player, new Position(2317, 3823, 0), true);
			}
			if (player.teleAction == 65) {
				TeleportExecutor.teleport(player, new Position(2990, 3850, 0), true);
				player.dialogueAction = -1;
				player.teleAction = -1;
			}

			if (player.dialogueAction == 14400) {
				TeleportExecutor.teleport(player, new Position(3506, 3487, 0), true);
				return;
			}
			if (player.teleAction == 14) {
				TeleportExecutor.teleport(player, new Position(3179, 3774, 0), true);
				player.sendMessage("Vet'ion is located to the North of here.");
				player.teleAction = -1;
				player.dialogueAction = -1;
				return;
			}
			if (player.dialogueAction == 123) {
				DegradableItem[] claimable = Degrade.getClaimedItems(player);
				if (claimable.length < 4) {
					return;
				}
				player.getPA().removeAllWindows();
				Degrade.claim(player, claimable[3].getItemId());
				return;
			}
			if (player.dialogueAction == 121) {
				player.getDH().sendDialogues(618, -1);
				player.teleAction = -1;
				player.dialogueAction = -1;
				return;
			}
			if (player.teleAction == 12) {
				player.getDH().sendOption5("GarGoyle", "Bloodveld", "Banshee", "-- Previous Page --",
						"-- Next Page --");
				player.teleAction = 11;
				break;
			}
			if (player.teleAction == 11) {
				player.getDH().sendOption5("Black Demon", "Dust Devils", "Nechryael", "-- Previous Page --",
						"-- Next Page --");
				player.teleAction = 10;
				break;
			}
			if (player.teleAction == 10) {
				player.getDH().sendOption5("Goblins", "Baby blue dragon", "Moss Giants", "-- Previous Page --",
						"-- Next Page --");
				player.teleAction = 9;
				break;
			}
			if (player.teleAction == 9) {
				player.getDH().sendOption5("Al-kharid warrior", "Ghosts", "Giant Bats", "-- Previous Page --",
						"-- Next Page --");
				player.teleAction = 8;
				break;
			}
			if (player.teleAction == 8) {
				player.getDH().sendOption5("Hill Giants", "Hellhounds", "Lesser Demons", "Chaos Dwarf",
						"-- Next Page --");
				player.teleAction = 7;
				break;
			}
			if (player.teleAction == 7) {
				TeleportExecutor.teleport(player, new Position(2923, 9759, 0), true);
			}
			if (player.teleAction == 1) {
				// brimhaven dungeon
				TeleportExecutor.teleport(player, new Position(2980, 3871, 0), true);
			} else if (player.teleAction == 3) {
				//TeleportExecutor.teleport(player, new Position(3808, 2844, 4));
				// player.sendMessage("Vet'ion is located to the North of here.");

			} else if (player.teleAction == 4) {
				// Fala
				/*
				 * player.getPA()().removeAllWindows(); c.teleAction = 0;
				 */
				TeleportExecutor.teleport(player, new Position(2815, 3461, 0), true);
				player.getDH().sendStatement("You need a Rake, Watering can, Seed Dibber and a seed.");
			} else if (player.teleAction == 5) {
				TeleportExecutor.teleport(player, new Position(2724, 3484, 0), true);
				player.sendMessage("For magic logs, try north of the duel arena.");
			}
			if (player.dialogueAction == 10) {
				TeleportExecutor.teleport(player, new Position(2660, 4839, 0), true);
				player.dialogueAction = -1;
			} else if (player.dialogueAction == 11) {
				// TeleportExecutor.teleport(c, new Position(2527, 4833, 0);
				// astrals here
				// c.getRunecrafting().craftRunes(2489);
				player.dialogueAction = -1;
			} else if (player.dialogueAction == 12) {
				// TeleportExecutor.teleport(c, new Position(2464, 4834, 0);
				// bloods here
				// c.getRunecrafting().craftRunes(2489);
				player.dialogueAction = -1;
			}
			break;
		// 5th tele option
		case 9194:
			if (player.dialogueAction == 1033) {
				GamblePKP.gamblePKP(player, 2000, player.npcType);
				return;
			}
			if (player.teleAction == 66) {
				TeleportExecutor.teleport(player, new Position(2116, 5658, 0), true);
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 75007) {
				player.getDH().sendDialogues(1000001, 1);
				return;
			}
			if (player.dialogueAction == 75008) {
				player.getDH().sendDialogues(1000002, 1);
				return;
			}
			if (player.teleAction == 205) {
				TeleportExecutor.teleport(player, new Position(1803, 3788, 0), true);
				player.dialogueAction = -1;
				player.teleAction = -1;
			}
			if (player.dialogueAction == 114) {
				player.getPA().closeAllWindows();
				player.getPA().makeSlayerHelm(player);
				return;
			}
//			if (player.teleAction == 200) {
//				player.getDH().sendOption5("Warrior's Guild", "", "", "", "@blu@Previous Page");
//				player.teleAction = 201;
//				return;
//			} else 
			if (player.teleAction == 201) {
				player.getDH().sendOption3("Barrows", "Duel Arena", "Fight Caves");
				player.teleAction = 200;
				return;
			}
			if (player.dialogueAction == 128) {
				player.getDH().sendDialogues(636, -1);
				return;
			}
			switch (player.teleAction) {
			case 2:
				player.getDH().sendDialogues(3333, -1);
				return;
			case 14:
				// c.getDH().sendDialogues(3325, -1);
				player.getDH().sendDialogues(3380, -1);
				return;
			}

			if (player.dialogueAction == 14400) {
				//TeleportExecutor.teleport(player, new Position(2729, 3487, 0));
				player.sendMessage("Coming soon.");
				return;
			}

			if (player.teleAction == 80) {
				TeleportExecutor.teleport(player, new Position(2672, 3712, 0), true);
				return;
			}
			if (player.teleAction == 65) {
				player.getDH().sendOption5("Cerberus", "Giant Mole 'Spade'", "Kalphite Queen",
						"Chaos Elemental @red@(Wild)", "Demonic Gorilla (Wild 50)");
				player.teleAction = 66;
				return;
			}
			if (player.teleAction == 66) {
				player.getDH().sendOption5("Corporal Beast", "Lizardman Shaman", "Crazy Archaeologist @red@(Wild)",
						"Chaos Fanatic @red@(Wild)", "@blu@Previous Page");
				player.teleAction = 65;
				return;
			}
			if (player.dialogueAction == 121 || player.dialogueAction == 123) {
				player.getPA().removeAllWindows();
				player.teleAction = -1;
				player.dialogueAction = -1;
				return;
			}
			if (player.teleAction == 8) {
				player.getDH().sendOption5("Goblins", "Baby blue dragon", "Moss Giants", "-- Previous Page --",
						"-- Next Page --");
				player.teleAction = 9;
				break;
			}
			if (player.teleAction == 9) {
				player.getDH().sendOption5("Black Demon", "Dust Devils", "Nechryael", "-- Previous Page --",
						"-- Next Page --");
				player.teleAction = 10;
				break;
			}
			if (player.teleAction == 11) {
				player.getDH().sendOption5("Infernal Mage", "Dark Beasts", "Abyssal Demon", "-- Previous Page --", "");
				player.teleAction = 12;
				break;
			}
			if (player.teleAction == 10) {
				player.getDH().sendOption5("GarGoyle", "Bloodveld", "Banshee", "-- Previous Page --",
						"-- Next Page --");
				player.teleAction = 11;
				break;
			}
			if (player.teleAction == 7) {
				player.getDH().sendOption5("Al-kharid warrior", "Ghosts", "Giant Bats", "-- Previous Page --",
						"-- Next Page --");
				player.teleAction = 8;
				break;
			}
			if (player.teleAction == 1) {
				// TeleportExecutor.teleport(c, new Position(3073, 3932, 0));
				player.getDH().sendOption4("Mage Bank", "West Dragons @red@(Wild)", " ", "@blu@Previous");
				player.teleAction = 50;
				break;

			} else if (player.teleAction == 3) {
				player.getDH().sendOption5("Dagannoth Kings", "Kraken", "Venenatis @red@(Wild & Multi)",
						"Vet'ion @red@(Wild & Multi)", "@blu@Next Page");
				player.teleAction = 14;
			} else if (player.teleAction == 4) {
				TeleportExecutor.teleport(player, new Position(3039, 4836, 0), true);
			} else if (player.teleAction == 5) {
				TeleportExecutor.teleport(player, new Position(2812, 3463, 0), true);
			}
			if (player.dialogueAction == 10 || player.dialogueAction == 11) {
				player.dialogueId++;
				player.getDH().sendDialogues(player.dialogueId, 0);
			} else if (player.dialogueAction == 12) {
				player.dialogueId = 17;
				player.getDH().sendDialogues(player.dialogueId, 0);
			}
			break;

		case 108005:
			if (Server.getMultiplayerSessionListener().inAnySession(player)) {
				return;
			}
			player.getPA().showInterface(15106);
			// player.getItems()().writeBonus();
			break;
		case 108006: // items kept on death
			if (Server.getMultiplayerSessionListener().inAnySession(player)) {
				return;
			}
			player.getPA().openItemsKeptOnDeath();
			break;

		case 59004:
			player.getPA().removeAllWindows();
			break;
			
			//
			
		case 100100: // Main 126 - handleMainLevels(client);
			if (player.inWild()) {
				player.sendMessage("Please move to a safe location first.");
				return;
			}
			if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
				player.sendMessage("You cannot use this in the duel arena!");
				return;
			}
			player.getDH().sendDialogues(12345, 1);
			break;

		case 100103: // Pure 88 - handlePureLevels(client);
			if (player.inWild()) {
				player.sendMessage("Please move to a safe location first.");
				return;
			}
			if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
				player.sendMessage("You cannot use this in the duel arena!");
				return;
			}
			player.getDH().sendDialogues(12346, 1);
			break;

		case 100106: // Hybrid Set - handleMainLevels(client);
			if (player.inWild()) {
				player.sendMessage("Please move to a safe location first.");
				return;
			}
			if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
				player.sendMessage("You cannot use this in the duel arena!");
				return;
			}
			player.getDH().sendDialogues(12347, 1);
			break;

		case 100109: // Zerker Set - handleZerkerLevels(client);
			if (player.inWild()) {
				player.sendMessage("Please move to a safe location first.");
				return;
			}
			if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
				player.sendMessage("You cannot use this in the duel arena!");
				return;
			}
			player.getDH().sendDialogues(12348, 1);
			break;

		case 100112: // Tribrid SET - handleZerkerLevels(client);
			if (player.inWild()) {
				player.sendMessage("Please move to a safe location first.");
				return;
			}
			if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
				player.sendMessage("You cannot use this in the duel arena!");
				return;
			}
			player.getDH().sendDialogues(12349, 1);
			break;

		case 100115: // F2P SET - handleMainLevels(client);
			if (player.inWild()) {
				player.sendMessage("Please move to a safe location first.");
				return;
			}
			if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
				player.sendMessage("You cannot use this in the duel arena!");
				return;
			}
			player.getDH().sendDialogues(12350, 1);
			break;

		case 100118: // 2006 Set - handleMainLevels(client);
			if (player.inWild()) {
				player.sendMessage("Please move to a safe location first.");
				return;
			}
			if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
				player.sendMessage("You cannot use this in the duel arena!");
				return;
			}
			player.getDH().sendDialogues(12351, 1);
			break;
			
			//

		case 9178:
			//melee weapon shop
			if (player.dialogueAction == 12345) {
				MainRune.equip(player);
			}
			if (player.dialogueAction == 12346) {
				PureMelee.equip(player);
			}
			if (player.dialogueAction == 12347) {
				Hybrid4.equip(player);
			}
			if (player.dialogueAction == 12351) {
				player.getDH().sendStatement("Suggest 2006 setups on the forums! Coming soon...");
			}
			
			if (player.dialogueAction == 12351) {
				for (int slot = 0; slot < player.playerItems.length; slot++) {
					if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {
						player.getItems().addToBank(player.playerItems[slot] - 1, player.playerItemsN[slot], false);
					}
				}
				player.getItems().updateInventory();
				player.getItems().resetBank();
				player.getItems().resetTempItems();

				for (int slot = 0; slot < player.playerEquipment.length; slot++) {
					if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
						player.getItems().addEquipmentToBank(player.playerEquipment[slot], slot,
								player.playerEquipmentN[slot], false);
						player.getItems().wearItem(-1, 0, slot);
					}
				}
				player.getItems().updateInventory();
				player.getItems().resetBank();
				player.getItems().resetTempItems();
			}
		    if (player.dialogueAction == 3609) {
			player.getShops().openShop(51);
			player.dialogueAction = 0;
		    }
			if(player.dialogueAction == 42) {
				if(player.pkp >= 10) {
					if (player.inWild())
					return;
					for (int j = 0; j < player.playerEquipment.length; j++) {
						if (player.playerEquipment[j] > 0) {
							player.getPA().closeAllWindows();
							player.sendMessage("You need to remove all your equipment before doing this!");
							return;
						}
					}
					try {
						int skilld = 1;
						int leveld = 1;
						player.pkp -= 10;
						player.playerXP[skilld] = player.getPA().getXPForLevel(leveld)+5;
						player.playerLevel[skilld] = player.getPA().getLevelForXP(player.playerXP[skilld]);
						player.getPA().refreshSkill(skilld);
									//	player.getPA()().closeAllWindows();
						player.getDH().sendDialogues(230, 7053);
					} catch (Exception e){}
					} else {
						player.sendMessage("You need 10 PK Points!");
					}
				}
			switch (player.teleAction) {
			case 50:
				TeleportExecutor.teleport(player, new Position(2540, 4715, 0), true); // MAGE
																				// BANK
				break;
			case 1:
				TeleportExecutor.teleport(player, new Position(3011, 3632, 0), true);
				break;
			case 2:
				TeleportExecutor.teleport(player, new Position(2441, 3090, 0), true);
				break;
			}
			if (player.dialogueAction == 3301) {
				player.getDH().sendDialogues(3302, player.npcType);
			} else if (player.dialogueAction == 1445) {
				player.getDH().sendDialogues(822, 4227);
			} else if (player.dialogueAction == 1446) {
				player.getDH().sendDialogues(838, 4227);
			}
			if (player.dialogueAction == 122) {
				player.getDH().sendDialogues(621, 954);
				return;
			}
			if (player.teleAction == 13) {
				TeleportExecutor.teleport(player, new Position(2839, 5292, 2), true);
			}
			if (player.teleAction == 80) {
				TeleportExecutor.teleport(player, new Position(3421, 3536, 0), true);
			}
			if (player.teleAction == 3) {
				TeleportExecutor.teleport(player, new Position(2273, 4681, 0), true);
			}
			if (player.teleAction == 201) {
				// pest
				TeleportExecutor.teleport(player, new Position(3565, 3308, 0), true);
			}
			if (player.dialogueAction == 2299) {
				player.playerXP[0] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[0] = player.getPA().getLevelForXP(player.playerXP[0]);
				player.getPA().refreshSkill(0);
				player.playerXP[1] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[1] = player.getPA().getLevelForXP(player.playerXP[1]);
				player.getPA().refreshSkill(1);
				player.playerXP[2] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[2] = player.getPA().getLevelForXP(player.playerXP[2]);
				player.getPA().refreshSkill(2);
				player.playerXP[3] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[3] = player.getPA().getLevelForXP(player.playerXP[3]);
				player.getPA().refreshSkill(3);
				player.playerXP[4] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[4] = player.getPA().getLevelForXP(player.playerXP[4]);
				player.getPA().refreshSkill(4);
				player.playerXP[5] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[5] = player.getPA().getLevelForXP(player.playerXP[5]);
				player.getPA().refreshSkill(5);
				player.playerXP[6] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[6] = player.getPA().getLevelForXP(player.playerXP[6]);
				player.getPA().refreshSkill(6);
				player.getItems().wearItem(10828, 1, player.playerHat);
				player.getItems().wearItem(6585, 1, player.playerAmulet);
				player.getItems().wearItem(4587, 1, Player.playerWeapon);
				player.getItems().wearItem(8850, 1, player.playerShield);
				player.getItems().wearItem(1052, 1, player.playerCape);
				player.getItems().wearItem(1127, 1, player.playerChest);//
				player.getItems().wearItem(1079, 1, player.playerLegs);//
				player.getItems().wearItem(2550, 1, player.playerRing);
				player.getItems().wearItem(11840, 1, player.playerFeet);
				player.getItems().wearItem(7461, 1, player.playerHands);
				player.getItems().addItem(5698, 1);
				player.getItems().addItem(2440, 1);
				player.getItems().addItem(2436, 1);
				player.getItems().addItem(3024, 1);
				player.getItems().addItem(557, 1000);
				player.getItems().addItem(560, 1000);
				player.getItems().addItem(9075, 1000);
				player.getItems().addItem(385, 15);
				player.getItems().addItem(3144, 5);
				player.setSidebarInterface(6, 29999); // lunar
				player.playerMagicBook = 2;
				player.getPA().resetAutocast();
				player.getItems().updateInventory();
				player.getPA().requestUpdates();
				player.getItems().updateSpecialBar();
				player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
				player.sendMessage("<img=10>An appropriate starter package has been given to you.");
				player.getPA().sendFrame126("Combat Level: " + player.combatLevel + "", 3983);
				player.getPA().removeAllWindows();
			}
			if (player.dialogueAction == 1337) {
				player.getDH().sendDialogues(802, 4070);
			}
			if (player.dialogueAction == 1658) {
				if (!player.getItems().playerHasItem(995, 2230000)) {
					player.sendMessage("You must have 2,230,000 coins to buy this package.");
					player.getPA().removeAllWindows();
					player.dialogueAction = 0;
					break;
				}
				player.dialogueAction = 0;
				player.getItems().addItemToBank(560, 4000);
				player.getItems().addItemToBank(565, 2000);
				player.getItems().addItemToBank(555, 6000);
				player.getItems().deleteItem(995, player.getItems().getItemSlot(995), 2230000);
				player.sendMessage("@red@The runes has been added to your bank.");
				player.getPA().removeAllWindows();
				break;
			}
			if (player.usingGlory) // player.getPA()().useCharge();
			{
				TeleportExecutor.teleport(player, new Position(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0), true);
			}
			if (player.dialogueAction == 2) {
				TeleportExecutor.teleport(player, new Position(3428, 3538, 0), true);
			}
			if (player.dialogueAction == 3) {
				TeleportExecutor.teleport(player, new Position(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0), true);
			}
			if (player.dialogueAction == 4) {
				TeleportExecutor.teleport(player, new Position(3565, 3314, 0), true);
			}
			if (player.dialogueAction == 20) {
				TeleportExecutor.teleport(player, new Position(2897, 3618, 4), true);
				player.killCount = 0;
			}

			if (player.caOption4a) {
				player.getDH().sendDialogues(102, player.npcType);
				player.caOption4a = false;
			}
			if (player.caOption4c) {
				player.getDH().sendDialogues(118, player.npcType);
				player.caOption4c = false;
			}
			break;

		case 9179:
			if (player.dialogueAction == 12345) {
				MainRuneMace.equip(player);
			}
			if (player.dialogueAction == 12346) {
				PureMeleeRange.equip(player);
			}
				
			if (player.dialogueAction == 12347) {
				Hybrid5.equip(player);
			}
			if (player.dialogueAction == 12351) {
				player.getDH().sendStatement("Suggest 2006 setups on the forums! Coming soon...");
			}
			
			if (player.dialogueAction == 12350) {
				for (int slot = 0; slot < player.playerItems.length; slot++) {
					if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {
						player.getItems().addToBank(player.playerItems[slot] - 1, player.playerItemsN[slot], false);
					}
				}
				player.getItems().updateInventory();
				player.getItems().resetBank();
				player.getItems().resetTempItems();

				for (int slot = 0; slot < player.playerEquipment.length; slot++) {
					if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
						player.getItems().addEquipmentToBank(player.playerEquipment[slot], slot,
								player.playerEquipmentN[slot], false);
						player.getItems().wearItem(-1, 0, slot);
					}
				}
				player.getItems().updateInventory();
				player.getItems().resetBank();
				player.getItems().resetTempItems();
			}
			if (player.dialogueAction == 12351) {
				for (int slot = 0; slot < player.playerItems.length; slot++) {
					if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {
						player.getItems().addToBank(player.playerItems[slot] - 1, player.playerItemsN[slot], false);
					}
				}
				player.getItems().updateInventory();
				player.getItems().resetBank();
				player.getItems().resetTempItems();

				for (int slot = 0; slot < player.playerEquipment.length; slot++) {
					if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
						player.getItems().addEquipmentToBank(player.playerEquipment[slot], slot,
								player.playerEquipmentN[slot], false);
						player.getItems().wearItem(-1, 0, slot);
					}
				}
				player.getItems().updateInventory();
				player.getItems().resetBank();
				player.getItems().resetTempItems();
			}
			//barrows shop
		    if (player.dialogueAction == 3609) {
			player.getShops().openShop(52);
			player.dialogueAction = 0;
		    }
			if(player.dialogueAction == 42) { //prayer
				if(player.pkp >= 10) {
				if (player.inWild())
				return;
				for (int j = 0; j < player.playerEquipment.length; j++) {
					if (player.playerEquipment[j] > 0) {
						player.getPA().closeAllWindows();
						player.getDH().sendDialogues(420, 7053);
						return;
					}
				}
				try {
					int skillp = 5;
					int levelp = 1;
					player.pkp -= 10;
					player.playerXP[skillp] = player.getPA().getXPForLevel(levelp)+5;
					player.playerLevel[skillp] = player.getPA().getLevelForXP(player.playerXP[skillp]);
					player.getPA().refreshSkill(skillp);
					player.getDH().sendDialogues(260, 7053);
				} catch (Exception e){}
				} else {
					player.sendMessage("You need 10 PK Points!");
				}
			}
			switch (player.teleAction) {
			case 50:
				TeleportExecutor.teleport(player, new Position(2979, 3597, 0), true);
				break;
			case 1:
				TeleportExecutor.teleport(player, new Position(3170, 3886, 0), true);
				break;
			case 2:
				TeleportExecutor.teleport(player, new Position(2400, 5179, 0), true);
				break;
			}
			if (player.dialogueAction == 122) {
				player.getDH().sendDialogues(623, 954);
				return;
			} else if (player.dialogueAction == 1445) {
				int items[][] = { { 841, 882, }, { 869 }, { 849, 884 }, { 8880, 9140 }, { 811 }, { 861, 888 },
						{ 861, 892 }, { 9185, 9338 }, { 9185, 9342, 892, 11235 }, { 11785, 9342, 11212, 11235 } };
				int tier = player.getPA().getLevelForXP(player.playerXP[player.playerRanged]) / 10;
				for (int i = 0; i < items[tier].length; i++) {
					int amount = ItemDefinition.forId(items[tier][i]).isStackable() ? 60 : 1;
					if ((!player.getItems().playerHasItem(items[tier][i])
							&& !player.getItems().isWearingItem(items[tier][i])) || amount > 1) {
						if (player.getItems().freeSlots() < 1)
							Server.itemHandler.createGroundItem(player, items[tier][i], player.absX, player.absY,
									player.heightLevel, amount, player.index);
						else
							player.getItems().addItem(items[tier][i], amount);
					}
				}
				player.animation(3170);
				player.appendDamage(player.getPA().getLevelForXP(player.playerXP[player.playerHitpoints]) / 2,
						Hitmark.HIT);
				player.dialogueAction = -1;
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 3301) {
				player.getDH().sendDialogues(3304, player.npcType);
			}
			if (player.dialogueAction == 1337) {
				if (player.getItems().freeSlots() < 28) {
					player.getDH().sendDialogues(806, 4070);
				} else {
					player.getPA().removeAllWindows();
					if (player.fishTourneySession == null || !player.fishTourneySession.running) {
						FishingTourney.getSingleton().addPlayerToSession(player);
					}
				}
			} else if (player.dialogueAction == 1446) {
				player.getDH().sendDialogues(839, 4227);
			}
			if (player.teleAction == 13) {
				TeleportExecutor.teleport(player, new Position(2860, 5354, 2), true);
			}
			if (player.teleAction == 200) {
				TeleportExecutor.teleport(player, new Position(3565, 3306, 0), true);

			}
			if (player.teleAction == 201) {
				// warr guild
				TeleportExecutor.teleport(player, new Position(2847, 3543, 0), true);
				player.sendMessage("@blu@Use the animators to gain tokens, then head upstairs to the cyclops.");
			}
			if (player.teleAction == 3) {
				TeleportExecutor.teleport(player, new Position(3262, 3929, 0), true);
			}
			if (player.dialogueAction == 2299) {
				player.playerXP[0] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[0] = player.getPA().getLevelForXP(player.playerXP[0]);
				player.getPA().refreshSkill(0);
				player.playerXP[1] = player.getPA().getXPForLevel(45) + 5;
				player.playerLevel[1] = player.getPA().getLevelForXP(player.playerXP[1]);
				player.getPA().refreshSkill(1);
				player.playerXP[2] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[2] = player.getPA().getLevelForXP(player.playerXP[2]);
				player.getPA().refreshSkill(2);
				player.playerXP[3] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[3] = player.getPA().getLevelForXP(player.playerXP[3]);
				player.getPA().refreshSkill(3);
				player.playerXP[4] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[4] = player.getPA().getLevelForXP(player.playerXP[4]);
				player.getPA().refreshSkill(4);
				player.playerXP[5] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[5] = player.getPA().getLevelForXP(player.playerXP[5]);
				player.getPA().refreshSkill(5);
				player.playerXP[6] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[6] = player.getPA().getLevelForXP(player.playerXP[6]);
				player.getPA().refreshSkill(6);
				player.getItems().wearItem(3751, 1, player.playerHat);
				player.getItems().wearItem(6585, 1, player.playerAmulet);
				player.getItems().wearItem(4587, 1, Player.playerWeapon);
				player.getItems().wearItem(8850, 1, player.playerShield);
				player.getItems().wearItem(1052, 1, player.playerCape);
				player.getItems().wearItem(1127, 1, player.playerChest);
				player.getItems().wearItem(1079, 1, player.playerLegs);
				player.getItems().wearItem(2550, 1, player.playerRing);
				player.getItems().wearItem(3105, 1, player.playerFeet);
				player.getItems().wearItem(7461, 1, player.playerHands);
				player.getItems().addItem(4587, 1);
				player.getItems().addItem(2440, 1);
				player.getItems().addItem(2436, 1);
				player.getItems().addItem(3024, 1);
				player.getItems().addItem(557, 1000);
				player.getItems().addItem(560, 1000);
				player.getItems().addItem(9075, 1000);
				player.getItems().addItem(385, 17);
				player.getItems().addItem(3144, 4);
				player.setSidebarInterface(6, 29999); // lunar
				player.playerMagicBook = 2;
				player.getPA().resetAutocast();
				player.getItems().updateInventory();
				player.getPA().requestUpdates();
				player.getItems().updateSpecialBar();
				player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
				player.sendMessage("<img=10>An appropriate starter package has been given to you.");
				player.getPA().removeAllWindows();
				player.getPA().sendFrame126("Combat Level: " + player.combatLevel + "", 3983);
				player.dialogueAction = 0;
			}
			if (player.dialogueAction == 1658) {
				if (!player.getItems().playerHasItem(995, 912000)) {
					player.sendMessage("You must have 912,000 coins to buy this package.");
					player.getPA().removeAllWindows();
					player.dialogueAction = 0;
					break;
				}
				player.dialogueAction = 0;
				player.getItems().addItemToBank(560, 2000);
				player.getItems().addItemToBank(9075, 4000);
				player.getItems().addItemToBank(557, 10000);
				player.getItems().deleteItem(995, player.getItems().getItemSlot(995), 912000);
				player.sendMessage("@red@The runes has been added to your bank.");
				player.getPA().removeAllWindows();
				break;
			}
			if (player.usingGlory) // player.getPA()().useCharge();
			{
				TeleportExecutor.teleport(player, new Position(Config.AL_KHARID_X, Config.AL_KHARID_Y, 0), true);
			}
			if (player.dialogueAction == 2) {
				TeleportExecutor.teleport(player, new Position(2884, 3395, 0), true);
			}
			if (player.dialogueAction == 3) {
				TeleportExecutor.teleport(player, new Position(3243, 3513, 0), true);
			}
			if (player.dialogueAction == 4) {
				TeleportExecutor.teleport(player, new Position(2444, 5170, 0), true);
			}

			if (player.dialogueAction == 20) {
				TeleportExecutor.teleport(player, new Position(2897, 3618, 12), true);
				player.killCount = 0;
			} else if (player.teleAction == 200) {
				// assault
				TeleportExecutor.teleport(player, new Position(2605, 3153, 0), true);
			}
			if (player.caOption4c) {
				player.getDH().sendDialogues(120, player.npcType);
				player.caOption4c = false;
			}
			if (player.caPlayerTalk1) {
				player.getDH().sendDialogues(125, player.npcType);
				player.caPlayerTalk1 = false;
			}
			break;

		case 9180:
			if (player.dialogueAction == 12345) {
				MainGranite.equip(player);
			}
			if (player.dialogueAction == 12346) {
				PureTribrid.equip(player);
			}
			if (player.dialogueAction == 12347) {
				Hybrid6.equip(player);
			}
			if (player.dialogueAction == 12351) {
				player.getDH().sendStatement("Suggest 2006 setups on the forums! Coming soon...");
			}
			//melee armor shop
		    if (player.dialogueAction == 3609) {
			player.getShops().openShop(53);
			player.dialogueAction = 0;
		    }
			if(player.dialogueAction == 42) { //attack
				if(player.pkp >= 10) {
				if (player.inWild())
				return;
				for (int j = 0; j < player.playerEquipment.length; j++) {
					if (player.playerEquipment[j] > 0) {
					player.getPA().closeAllWindows();
					player.sendMessage("You need to remove all your equipment before doing this!");
						return;
					}
				}
				try {
					int skill = 0;
					int levela = 1;
					player.pkp -= 10;
					player.playerXP[skill] = player.getPA().getXPForLevel(levela)+5;
					player.playerLevel[skill] = player.getPA().getLevelForXP(player.playerXP[skill]);
					player.getPA().refreshSkill(skill);
									//player.getPA().closeAllWindows();
				player.getDH().sendDialogues(240, 7053);
				} catch (Exception e){}
				} else {
					player.sendMessage("You need 10 PK Points!");
				}
			}
			switch (player.teleAction) {

			case 50:

				break;
			case 1:
				TeleportExecutor.teleport(player, new Position(3289, 3639, 0), true);
				break;
			case 2:
				TeleportExecutor.teleport(player, new Position(2846, 3541, 0), true);
				break;
			/*
			 * case 2: TeleportExecutor.teleport(c, new Position(2667, 3424, 0);
			 * break;
			 */
			}
			/*
			 * if (c.dialogueAction == 122) { c.getDH().sendDialogues(624, 954);
			 * return; }
			 */

			if (player.dialogueAction == 3301) {
				player.getDH().sendDialogues(3310, player.npcType);
			}
			if (player.dialogueAction == 1337) {
				player.getPA().removeAllWindows();
				player.getShops().openShop(68);
			} else if (player.dialogueAction == 1446) {
				player.getShops().openShop(99);
			}
			if (player.teleAction == 13) {
				TeleportExecutor.teleport(player, new Position(2925, 5335, 2), true);
			} else if (player.dialogueAction == 1445) {
				int items[][] = { { 556, 558, }, { 556, 558, 557 }, { 556, 562, 557 }, { 556, 556, 562, 557, 554 },
						{ 556, 556, 560, 557, 554 }, { 556, 556, 560, 557, 554, 560, 557, 554, 555 },
						{ 556, 556, 560, 557, 554, 555, 560, 557, 554, 555, 565 },
						{ 556, 556, 556, 556, 560, 557, 554, 555, 565, 9075 },
						{ 556, 556, 556, 556, 560, 557, 554, 555, 565, 556, 556, 556, 556, 560, 557, 554, 555, 565,
								9075, 4675 },
						{ 556, 556, 556, 556, 560, 557, 554, 555, 565, 556, 556, 556, 556, 560, 557, 554, 555, 565,
								9075, 4675 } };
				int tier = player.getPA().getLevelForXP(player.playerXP[player.playerMagic]) / 10;
				for (int i = 0; i < items[tier].length; i++) {
					int amount = ItemDefinition.forId(items[tier][i]).isStackable() ? 60 : 1;
					if ((!player.getItems().playerHasItem(items[tier][i])
							&& !player.getItems().isWearingItem(items[tier][i])) || amount > 1) {
						if (player.getItems().freeSlots() < 1)
							Server.itemHandler.createGroundItem(player, items[tier][i], player.absX, player.absY,
									player.heightLevel, amount, player.index);
						else
							player.getItems().addItem(items[tier][i], amount);
					}
				}
				player.animation(3170);
				player.appendDamage(player.getPA().getLevelForXP(player.playerXP[player.playerHitpoints]) / 2,
						Hitmark.HIT);
				player.dialogueAction = -1;
				player.getPA().closeAllWindows();
				//player.getDH().sendDialogues(825, 4227);
			}

			if (player.dialogueAction == 2299) {
				player.playerXP[0] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[0] = player.getPA().getLevelForXP(player.playerXP[0]);
				player.getPA().refreshSkill(0);
				player.playerXP[1] = player.getPA().getXPForLevel(1) + 5;
				player.playerLevel[1] = player.getPA().getLevelForXP(player.playerXP[1]);
				player.getPA().refreshSkill(1);
				player.playerXP[2] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[2] = player.getPA().getLevelForXP(player.playerXP[2]);
				player.getPA().refreshSkill(2);
				player.playerXP[3] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[3] = player.getPA().getLevelForXP(player.playerXP[3]);
				player.getPA().refreshSkill(3);
				player.playerXP[4] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[4] = player.getPA().getLevelForXP(player.playerXP[4]);
				player.getPA().refreshSkill(4);
				player.playerXP[5] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[5] = player.getPA().getLevelForXP(player.playerXP[5]);
				player.getPA().refreshSkill(5);
				player.playerXP[6] = player.getPA().getXPForLevel(99) + 5;
				player.playerLevel[6] = player.getPA().getLevelForXP(player.playerXP[6]);
				player.getPA().refreshSkill(6);
				player.getItems().wearItem(12453, 1, player.playerHat);
				player.getItems().wearItem(6585, 1, player.playerAmulet);
				player.getItems().wearItem(861, 1, Player.playerWeapon);
				player.getItems().wearItem(892, 500, player.playerArrows);
				player.getItems().wearItem(10499, 1, player.playerCape);
				player.getItems().wearItem(6107, 1, player.playerChest);
				player.getItems().wearItem(12383, 1, player.playerLegs);
				player.getItems().wearItem(2550, 1, player.playerRing);
				player.getItems().wearItem(3105, 1, player.playerFeet);
				player.getItems().wearItem(7458, 1, player.playerHands);
				player.getItems().addItem(4587, 1);
				player.getItems().addItem(4153, 1);
				player.getItems().addItem(5698, 1);
				player.getItems().addItem(2440, 1);
				player.getItems().addItem(2436, 1);
				player.getItems().addItem(2444, 1);
				player.getItems().addItem(2434, 1);
				player.getItems().addItem(385, 17);
				player.getItems().addItem(3144, 4);
				player.setSidebarInterface(6, 1151); // modern
				player.playerMagicBook = 0;
				player.getPA().resetAutocast();
				player.getItems().updateInventory();
				player.getPA().requestUpdates();
				player.getItems().updateSpecialBar();
				player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
				player.sendMessage("<img=10>An appropriate starter package has been given to you.");
				player.getPA().sendFrame126("Combat Level: " + player.combatLevel + "", 3983);
				player.getPA().removeAllWindows();
				player.dialogueAction = 0;
			}
			if (player.dialogueAction == 1658) {
				if (!player.getItems().playerHasItem(995, 1788000)) {
					player.sendMessage("You must have 1,788,000 coins to buy this package.");
					player.getPA().removeAllWindows();
					player.dialogueAction = 0;
					break;
				}
				player.dialogueAction = 0;
				player.getItems().addItemToBank(556, 1000);
				player.getItems().addItemToBank(554, 1000);
				player.getItems().addItemToBank(558, 1000);
				player.getItems().addItemToBank(557, 1000);
				player.getItems().addItemToBank(555, 1000);
				player.getItems().addItemToBank(560, 1000);
				player.getItems().addItemToBank(565, 1000);
				player.getItems().addItemToBank(566, 1000);
				player.getItems().addItemToBank(9075, 1000);
				player.getItems().addItemToBank(562, 1000);
				player.getItems().addItemToBank(561, 1000);
				player.getItems().addItemToBank(563, 1000);
				player.getItems().deleteItem(995, player.getItems().getItemSlot(995), 1788000);
				player.sendMessage("@red@The runes has been added to your bank.");
				player.getPA().removeAllWindows();
				break;
			}
			if (player.usingGlory) // player.getPA()().useCharge();
			{
				TeleportExecutor.teleport(player, new Position(Config.KARAMJA_X, Config.KARAMJA_Y, 0), true);
			}
			if (player.dialogueAction == 2) {
				TeleportExecutor.teleport(player, new Position(2471, 10137, 0), true);
			}
			if (player.dialogueAction == 3) {
				TeleportExecutor.teleport(player, new Position(3363, 3676, 0), true);
			}
			if (player.dialogueAction == 4) {
				TeleportExecutor.teleport(player, new Position(2659, 2676, 0), true);
			}
			if (player.dialogueAction == 20) {
				TeleportExecutor.teleport(player, new Position(2897, 3618, 8), true);
				player.killCount = 0;
			}
			if (player.caOption4c) {
				player.getDH().sendDialogues(122, player.npcType);
				player.caOption4c = false;
			}
			if (player.caPlayerTalk1) {
				player.getDH().sendDialogues(127, player.npcType);
				player.caPlayerTalk1 = false;
			}
			break;

		case 9181:
			if (player.dialogueAction == 12345) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 12346) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 12347) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 12350) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 12351) {
				player.getPA().closeAllWindows();
			}
			//pure armor shop
		    if (player.dialogueAction == 3609) {
			player.getShops().openShop(54);
			player.dialogueAction = 0;
		    }
			if(player.dialogueAction == 42) { //allstats
				if(player.pkp >= 50) {
				if (player.inWild())
				return;
				for (int j = 0; j < player.playerEquipment.length; j++) {
					if (player.playerEquipment[j] > 0) {
					player.getPA().closeAllWindows();
					player.sendMessage("You need to remove all your equipment before doing this!");
						return;
					}
				}
				try {
					int skill1 = 0;
					int level = 1;
					player.pkp -= 50;
					player.playerXP[skill1] = player.getPA().getXPForLevel(level)+5;
					player.playerLevel[skill1] = player.getPA().getLevelForXP(player.playerXP[skill1]);
					player.getPA().refreshSkill(skill1);
					int skill2 = 1;
					player.playerXP[skill2] = player.getPA().getXPForLevel(level)+5;
					player.playerLevel[skill2] = player.getPA().getLevelForXP(player.playerXP[skill2]);
					player.getPA().refreshSkill(skill2);
					int skill3 = 2;
					player.playerXP[skill3] = player.getPA().getXPForLevel(level)+5;
					player.playerLevel[skill3] = player.getPA().getLevelForXP(player.playerXP[skill3]);
					player.getPA().refreshSkill(skill3);
					int skill4 = 3;
					level = 10;
					player.playerXP[skill4] = player.getPA().getXPForLevel(level)+5;
					player.playerLevel[skill4] = player.getPA().getLevelForXP(player.playerXP[skill4]);
					player.getPA().refreshSkill(skill4);
					int skill5 = 4;
					level = 1;
					player.playerXP[skill5] = player.getPA().getXPForLevel(level)+5;
					player.playerLevel[skill5] = player.getPA().getLevelForXP(player.playerXP[skill5]);
					player.getPA().refreshSkill(skill5);
					int skill6 = 5;
					player.playerXP[skill6] = player.getPA().getXPForLevel(level)+5;
					player.playerLevel[skill6] = player.getPA().getLevelForXP(player.playerXP[skill6]);
					player.getPA().refreshSkill(skill6);
					int skill7 = 6;
					player.playerXP[skill7] = player.getPA().getXPForLevel(level)+5;
					player.playerLevel[skill7] = player.getPA().getLevelForXP(player.playerXP[skill7]);
					player.getPA().refreshSkill(skill7);
				player.getDH().sendDialogues(250, 7053);
				} catch (Exception e){}
				} else {
					player.sendMessage("You need 50 PK Points!");
				}
			}
			switch (player.teleAction) {
			case 50:
				player.getDH().sendOption5("Edgeville", "Ardougne Lever", "East Dragons @red@(Wild)",
						"44 Portals @red@(Wild)", "@blu@Next Page");
				player.teleAction = 1;
				break;
			case 1:
				TeleportExecutor.teleport(player, new Position(3153, 3923, 0), true);
				break;
			case 2:
				player.getDH().sendDialogues(3325, -1);
				return;
			}
			if (player.dialogueAction == 3301) {
				if (player.MEDIUM && player.changedTaskAmount == 0 && player.getMedium().hasTask()) {
					player.needsNewTask = true;
					player.changedTaskAmount++;
					player.taskAmount = 0;
					player.slayerTask = 0;
					player.MEDIUM = false;
					player.getEasy().generateTask();
				} else if (player.HARD && player.changedTaskAmount == 0 && player.getHard().hasTask()) {
					player.needsNewTask = true;
					player.changedTaskAmount++;
					player.taskAmount = 0;
					player.slayerTask = 0;
					player.HARD = false;
					player.getMedium().generateTask();
				} else {
					if (player.slayerTask == 0 && player.taskAmount == 0) {
						player.getDH().sendStatement("You don't have a task currently.");
					} else if (player.changedTaskAmount > 1 || player.EASY || player.MEDIUM) {
						player.getDH().sendDialogues(3309, 490);
					}
				}
			}
			if (player.dialogueAction == 122) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 1337) {
				player.getDH().sendDialogues(807, 4070);
			} else if (player.dialogueAction == 1445) {
				player.getPA().removeAllWindows();
			} else if (player.dialogueAction == 1446) {
				player.getPA().removeAllWindows();
			}
			if (player.teleAction == 201 || player.dialogueAction == 129) {
				// pest
				player.getPA().removeAllWindows();
			}
			if (player.teleAction == 13) {
				TeleportExecutor.teleport(player, new Position(2909, 5265, 0), true);
			}
			if (player.teleAction == 3) {
				TeleportExecutor.teleport(player, new Position(3331, 3706, 0), true);
			}

			/*
			 * if (c.teleAction == 200) { c.getDH().sendDialogues(2002, -1);
			 * c.dialogueAction = -1; return; }
			 */
			if (player.dialogueAction == 2299) {
				player.sendMessage("<img=10>You can set your stats by clicking them in the stats tab.");
				player.getPA().removeAllWindows();
				player.dialogueAction = 0;
			}
			if (player.dialogueAction == 1658) {
				player.getShops().openShop(5);
				player.dialogueAction = 0;
			}
			if (player.usingGlory) // player.getPA()().useCharge();
			{
				TeleportExecutor.teleport(player, new Position(Config.MAGEBANK_X, Config.MAGEBANK_Y, 0), true);
			}
			if (player.dialogueAction == 2) {
				TeleportExecutor.teleport(player, new Position(2669, 3714, 0), true);
			}
			if (player.dialogueAction == 3) {
				TeleportExecutor.teleport(player, new Position(2540, 4716, 0), true);
			}
			if (player.dialogueAction == 4) {
				TeleportExecutor.teleport(player, new Position(3366, 3266, 0), true);

			} else if (player.teleAction == 200) {
				// tzhaar
				TeleportExecutor.teleport(player, new Position(2444, 5170, 0), true);
			}
			if (player.dialogueAction == 20) {
				// TeleportExecutor.teleport(c, new Position(3366, 3266, 0);
				// c.killCount = 0;
				player.sendMessage("This will be added shortly");
			}
			if (player.caOption4c) {
				player.getDH().sendDialogues(124, player.npcType);
				player.caOption4c = false;
			}
			if (player.caPlayerTalk1) {
				player.getDH().sendDialogues(130, player.npcType);
				player.caPlayerTalk1 = false;
			}
			break;
		case 26010:
			player.getPA().resetAutocast();
			break;
		case 1093:
		case 1094:
		case 1097:
			if (player.autocastId > 0) {
				player.getPA().resetAutocast();
			} else {
				if (player.playerMagicBook == 1) {
					if (player.playerEquipment[Player.playerWeapon] == 4675
							|| player.playerEquipment[Player.playerWeapon] == 6914
							|| player.playerEquipment[Player.playerWeapon] == 13867
							|| player.playerEquipment[Player.playerWeapon] == 4710
							|| player.playerEquipment[Player.playerWeapon] == 12904) {
						player.setSidebarInterface(0, 1689);
					} else {
						player.sendMessage("You can't autocast ancients without a proper staff.");
					}
				} else if (player.playerMagicBook == 0) {
					if (player.playerEquipment[Player.playerWeapon] == 4170) {
						player.setSidebarInterface(0, 12050);
					} else {
						player.setSidebarInterface(0, 1829);
					}
				}
			}
			break;

		case 9157:
			if (player.dialogueAction == 12348) {
				VoidSet.equip(player);
			}
			if (player.dialogueAction == 12349) {
				Zerker.equip(player);
			}
			if (player.dialogueAction == 12350) {
				Tank.equip(player);
			}
			if (player.dialogueAction == 12351) {
				player.getDH().sendStatement("Suggest 2006 setups on the forums! Coming soon...");
			}
			
			
			if (player.dialogueAction == 4072) {
				if (player.inWild() || player.inCamWild() || /*player.inPvP()||*/ player.safeTimer > 0|| player.inDuelArena()
						|| Server.getMultiplayerSessionListener().inAnySession(player)) {
					return;
				}
				if (player.getItems().playerHasItem(20770, 1)) {
					if (player.preservePrayerScroll == true) {
						player.getDH().sendStatement("You cannot read this scroll as you have already unlocked this prayer.");
						return;
					}
					player.getItems().deleteItem(20770, 1);
					player.gfx100(263);
					player.preservePrayerScroll = true;
					player.getDH().sendItemChat1("","You study the scroll and learn a new prayer: @dre@Preserve", 20770, 200);
					return;
				}
			}
			if (player.dialogueAction == 4074) {
				if (player.inWild() || player.inCamWild() || /*player.inPvP()||*/ player.safeTimer > 0|| player.inDuelArena()
						|| Server.getMultiplayerSessionListener().inAnySession(player)) {
					return;
				}
				if (player.getItems().playerHasItem(20771, 1)) {
					if (player.rigourPrayerScroll == true) {
						player.getDH().sendStatement("You cannot read this scroll as you have already unlocked this prayer.");
						return;
					}
					player.getItems().deleteItem(20771, 1);
					player.gfx100(263);
					player.rigourPrayerScroll = true;
					player.getDH().sendItemChat1("","You study the scroll and learn a new prayer: @dre@Rigour", 21034, 200);
					return;
				}
			}
			if (player.dialogueAction == 4076) {
				if (player.inWild() || player.inCamWild() || /*player.inPvP()||*/ player.safeTimer > 0|| player.inDuelArena()
						|| Server.getMultiplayerSessionListener().inAnySession(player)) {
					return;
				}
				if (player.getItems().playerHasItem(20772, 1)) {
					if (player.auguryPrayerScroll == true) {
						player.getDH().sendStatement("You cannot read this scroll as you have already unlocked this prayer.");
						return;
					}
					player.getItems().deleteItem(20772, 1);
					player.gfx100(263);
					player.auguryPrayerScroll = true;
					player.getDH().sendItemChat1("","You study the scroll and learn a new prayer: @dre@Augury", 21034, 200);
					return;
				}
			}
			//F2P Items
			if (player.dialogueAction == 3617) {
			player.getShops().openShop(59);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			//Donator Shop
		    if (player.dialogueAction == 3613) {
			player.getShops().openShop(81);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			//empty dialogue
			if (player.dialogueAction == 850) {
			player.getPA().removeAllItems();
			player.getPA().removeAllWindows();
			player.getPA().loadQuests();
			player.sendMessage("You've emptied your inventory!");
			return;
			}
			//wilderness zones dialogue
		    if (player.dialogueAction == 3610) {
		    	player.getPA().loadQuests();
		    	player.getDH().sendDialogues(3611, player.npcType);
		    }
		    //magic accessories shop
		    if (player.dialogueAction == 3615) {
			player.getShops().openShop(55);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			//range weapon shop
		    if (player.dialogueAction == 3608) {
			player.getShops().openShop(49);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			if(player.teleAction == 6666) {
				Tablets.teleport(player, 19627);
				player.teleAction = -1;
			}
			if(player.teleAction == 6671) {
				Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(player.memberName);
				if (optionalPlayer.isPresent()) {
					Player c2 = optionalPlayer.get();
					if(c2.playerName == player.playerName) {
						return;
					}
					if (System.currentTimeMillis() - player.lastClanTeleport < 120_000) {
						player.sendMessage("You can only use this once every 2 minutes.");
						return;
					}
					
					if(!player.clan.getTeleport()) {
						player.sendMessage("The clan founder has not enabled the use of this ability.");
						return;
					}
					
					if (!c2.inWild()) {
						player.sendMessage("The player your teleporting to must be in the wilderness.");
						return;
					}
					if (!player.inWild()) {
						player.sendMessage("You must be in atleast @blu@1@bla@ wilderness to use this.");
						return;
					}
					if (player.playerIndex > 0 || player.npcIndex > 0) {
						player.sendMessage("You cannot teleport whilst in combat.");
						return;
					}
					if (player.wildLevel > 20) {
						player.sendMessage("You can't use this above level @blu@20@bla@ wilderness.");
						return;
					}

					if (c2.hungerGames) {
						player.sendMessage("You can't use this in the hunger games.");
						return;
					}

					TeleportExecutor.teleport(player, new Position(c2.getX(), c2.getY() - 1, c2.heightLevel), true);
					player.getPA().closeAllWindows();
					player.lastClanTeleport = System.currentTimeMillis();
				} else {
					player.sendMessage(player.memberName + " is not online. You can only teleport to online players.");
				}
			}
			if (player.dialogueAction == 3601) {
				int ITEMS[][] = { { 451, 452 }, { 11934, 11935 }, { 440, 441 }, { 453, 454 }, { 444, 445 },
						{ 447, 448 }, { 449, 450 }, { 1515, 1516 }, { 1513, 1514 } };
				player.getPA().closeAllWindows();

				for (int i = 0; i < ITEMS.length; i++) {
					if (player.getItems().playerHasItem(ITEMS[i][0])) {
						int amount = player.getItems().getItemAmount(ITEMS[i][0]);
						int payment = player.getItems().getItemAmount(ITEMS[i][0]) * 1;

						if (player.pkp != payment) {
							DialogueManager.sendStatement(player, Misc.format(payment)
									+ " PK Points is required to do this; which you do not have!");
							break;
						}
						player.pkp -= payment;
						player.getItems().deleteItem2(ITEMS[i][0], amount);
						player.getItems().addItem(ITEMS[i][1], amount);
						DialogueManager.sendInformationBox(player, "Piles", "You have noted:",
								"@blu@" + amount + " </col>items", "You have paid:",
								"@blu@" + Misc.format(payment) + " </col>PK points");
						break;
					} else {
						DialogueManager.sendStatement(player,
								"You do not contain any items that are allowed to be noted!");
					}
				}
			}
			if (player.dialogueAction == 9500) {
				if (!Config.PLACEHOLDER_ECONOMY) {
					if (Server.getMultiplayerSessionListener().inAnySession(player)) {
						return;
					}
					if (player.inWild() || player.inCamWild()) {
						return;
					}
					TeleportExecutor.teleport(player, new Position(3280, 3878, 0), true);
					return;
				}
			}
			if (player.dialogueAction == 761) {
				if (player.getItems().playerHasItem(10594, 1) && player.getItems().freeSlots() >= 1) {
					player.getItems().deleteItem(10594, 1);
					player.votePoints += 1;
					player.bonusXpTime += 2000;
					player.sendMessage("You have redeemed a vote book!");
					player.sendMessage(
							"@red@Your current bonus EXP time left is: " + player.bonusXpTime / 100 + " Minutes.");
					player.getPA().closeAllWindows();
				} else {
					player.sendMessage("You need at least one open inventory slot!");
				}
			}
			if (player.teleAction == 3000) {
				TeleportExecutor.teleport(player, new Position(1834, 3669, 0), true);
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 1100001) {
				// player.getPA()().closeAllWindows();
				player.getPA().makeAvasAccumulator(player);
				return;
			}
			if (player.dialogueAction == 400002) {
				player.openAccountPin();
			}
			if (player.dialogueAction == 400004) {
				player.resetAccountPin();
			}
			if (player.dialogueAction == 200001) {
				player.getPA().closeAllWindows();
				PetHandler.checkBOB(player);
			}
			if (player.dialogueAction == 86000) {
				if (player.pkp >= 25
						&& player.getItems().playerHasItem(11824, 1)) {
					player.pkp -= 25;
					player.getItems().deleteItem(11824, 1);
					player.getItems().addItem(11889, 1);
					player.getPA().closeAllWindows();
				} else
					player.sendMessage("You need 25 PK Points to turn this into a hasta.");
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 87000) {
				if (player.pkp >= 25
						&& player.getItems().playerHasItem(11889, 1)) {
					player.pkp -= 25;
					player.getItems().deleteItem(11889, 1);
					player.getItems().addItem(11824, 1);
					player.getPA().closeAllWindows();
				} else
					player.sendMessage("You need 25 PK Points to turn this into a spear.");
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 85000) {
				player.getDH().sendDialogues(1600, 306);
				return;
			}
			if (player.dialogueAction == 1000) {
				player.getItems().addItem(player.floweritem, 1);
				player.getPA().removeObject(ClickItem.flowerX, ClickItem.flowerY);
				ClickItem.flowerX = 0;
				ClickItem.flowerY = 0;
				ClickItem.flowers = 0;
				player.getPA().closeAllWindows();
			}

			if (player.dialogueAction == 6290) {
				if (player.pkp != 10) {
					player.getDH().sendDialogues(6192, player.npcType);
					return;
				} else if (player.pkp >= 10) {
					player.pkp -= 10;
					if (player.playerLevel[3] < Player.getLevelForXP(player.playerXP[3])) {
						player.playerLevel[3] = Player.getLevelForXP(player.playerXP[3]);
						player.getPA().refreshSkill(3);
						player.sendMessage("Your hitpoints have been Restored.");
					} else {
						player.sendMessage("You already have full Hitpoints.");
					}
					player.specAmount = 0.0;
					player.animation(645);
					player.spawnSet = System.currentTimeMillis();
					player.specAmount += 10.0;
					player.getItems().updateSpecialBar();
					player.setPoisonDamage((byte) 0);
					player.setVenomDamage((byte) 0);
					player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
					player.sendMessage("You have restored your spec!");
					player.getDH().sendDialogues(6193, player.npcType);
					return;
				}
			}
			if (player.teleAction == 110) {// armadyl
				TeleportExecutor.teleport(player, new Position(2839, 5296, 2), true);
			}
			if (player.teleAction == 111) {// bandos
				TeleportExecutor.teleport(player, new Position(2864, 5354, 2), true);
			}
			if (player.teleAction == 112) {// saradomin
				TeleportExecutor.teleport(player, new Position(2907, 5265, 0), true);
			}
			if (player.teleAction == 113) {// zamorak
				TeleportExecutor.teleport(player, new Position(2925, 5331, 2), true);
			}
			if (!player.playerName.equalsIgnoreCase("dylan") && player.dialogueAction == 129) {
				if (!player.playerName.equalsIgnoreCase("dylan") && player.wildLevel > 20) {
					player.sendMessage("You cannot teleport above 20 wilderness.");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.ZULRAH_CLICKS >= 1) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.zulrah.getInstancedZulrah() != null) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getItems().playerHasItem(12938, 1)) {
					player.getZulrahEvent().initialize();
					player.getItems().deleteItem(12938, 1);
					player.ZULRAH_CLICKS = 1;
					return;
				}
				player.getZulrahEvent().initialize();
				player.ZULRAH_CLICKS = 1;
				return;
			}

			if (player.dialogueAction == 2223) {
				TeleportExecutor.teleport(player, new Position(3041, 4532, 0), true);
				player.getPA().closeAllWindows();
			}

			/*
			 * if (c.dialogueAction == 130) { if (c.getLostItems().size() > 0) {
			 * c.getLostItems().retain(); } return; }
			 */
			if (player.dialogueAction == 16500) {
				player.getDH().sendDialogues(3802, player.npcType);
				return;
			}
			if (player.dialogueAction == 1140) {
				player.getShops().openSkillCape();
				return;
			}
			if (player.dialogueAction == 1141) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 1142) {
				if (!player.getItems().playerHasItem(6570, 1)) {
					player.sendMessage("You need a firecape to use this option.");
					player.getPA().closeAllWindows();
					return;
				}
				player.getItems().deleteItem(6570, player.getItems().getItemSlot(6570), 1);
				int jadpet = Misc.random(200);
				if (jadpet == 200) {
					player.getDH().sendDialogues(654, -1);
					player.getItems().addItemToBank(13225, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							Player c2 = PlayerHandler.players[j];
							c2.sendMessage("<col=006600>" + player.playerName
									+ " received a TzRek-Jad from gambling firecapes!");
						}
					}
				} else {
					player.getDH().sendDialogues(653, -1);
				}
			}
			if (player.dialogueAction == 16501) {
				if (player.pkp < 120000) {
					player.getDH().sendDialogues(3804, player.npcType);
					return;
				} else if (player.pkp >= 120000) {
					player.pkp -= 120000;
					player.insure = true;
					player.getDH().sendDialogues(3805, player.npcType);
					player.sendMessage("@blu@You have insured all of your pets and will no longer lose them on death!");
					player.getPA().loadQuests();
					return;
				}
			}
			if (player.dialogueAction == 12400) {
				player.getShops().openShop(77);
				return;
			}
			if (player.dialogueAction == 12600) {
				player.getShops().openShop(116);
				return;
			}
			if (player.dialogueAction == 14400) {
				TeleportExecutor.teleport(player, new Position(2474, 3438, 0), true);
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 127) {
				if (player.absX == 3184 && player.absY == 3945) {
					if (player.pkp >= 5) {
						player.getPA().movePlayer(3184, 3944, 0);
						player.pkp -= 5;
						player.getPA().removeAllWindows();
					} else {
						player.getDH().sendStatement("You need at least 5 PK Points to enter this area.");
					}
				}
				player.dialogueAction = -1;
				player.nextChat = -1;
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 947) {
				player.getShops().openShop(113);
				player.dialogueAction = -1;
			}
			if (player.dialogueAction == 1338) {
				if (player.fishTourneySession != null && player.fishTourneySession.running) {
					player.fishTourneySession.turnInTask(player);
					player.getPA().removeAllWindows();
				} else if (player.fishTourneySession != null && !player.fishTourneySession.running) {
					player.getDH().sendDialogues(802, 1);
				}
			}
			if (player.dialogueAction == 126) {
				TeleportExecutor.teleport(player, new Position(3039, 4835, 0), true);
				player.dialogueAction = -1;
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 125) {
				if (player.getItems().playerHasItem(8851, 100)) {
					player.getPA().movePlayer(2847, 3540, 2);
					player.getItems().deleteItem2(8851, 100);
					player.getPA().removeAllWindows();
					player.getWarriorsGuild().cycle();
				} else {
					player.getDH().sendNpcChat2("You need atleast 100 warrior guild tokens.",
							"You can get some by operating the armour animator.", 4289, "Kamfreena");
					player.nextChat = 0;
				}
				player.dialogueAction = -1;
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 120) {
				if (player.getItemOnPlayer() == null) {
					return;
				}
				if (!player.getItems().playerHasItem(962)) {
					return;
				}
				if (player.getItemOnPlayer().getItems().freeSlots() < 1) {
					player.sendMessage("The other player must have at least 1 free slot.");
					return;
				}
				int[] partyHats = { 1038, 1040, 1042, 1044, 1046, 1048 };
				int points = Misc.random(15);
				int hat = partyHats[Misc.random(partyHats.length - 1)];
				Player winner = Misc.random(1) == 0 ? player : player.getItemOnPlayer();
				Player loser = winner == player ? player.getItemOnPlayer() : player;
				if (Objects.equals(winner, loser)) {
					return;
				}
				player.getPA().closeAllWindows();
				loser.face(winner.getX(), winner.getY());
				winner.face(loser.getX(), loser.getY());
				winner.animation(881);
				loser.animation(881);
				player.getItems().deleteItem(962, 1);
				winner.getItems().addItem(hat, 1);
				loser.getItems().addItem(2996, points);
				winner.sendMessage(
						"You have received a " + player.getItems().getItemName(hat) + " from the christmas cracker.");
				loser.sendMessage("You didn't get the partyhat. You received pk tickets as consolation.");
			}
			if (player.dialogueAction == -1 && player.getCurrentCombination().isPresent()) {
				ItemCombination combination = player.getCurrentCombination().get();
				if (combination.isCombinable(player)) {
					combination.combine(player);
				} else {
					player.getDH().sendStatement("You don't have all the items you need for this combination.");
					player.nextChat = -1;
					player.setCurrentCombination(Optional.empty());
				}
				return;
			}
			if (player.dialogueAction == 115) {
				if (player.getItems().playerHasItem(12526) && player.getItems().playerHasItem(6585)) {
					player.getItems().deleteItem2(12526, 1);
					player.getItems().deleteItem2(6585, 1);
					player.getItems().addItem(12436, 1);
					player.getDH().sendDialogues(582, -1);
				} else {
					player.getPA().removeAllWindows();
				}
			}
			if (player.dialogueAction == 114) {
				player.getDH().sendDialogues(579, -1);
				return;
			}
			if (player.dialogueAction == 110) {
				if (player.getItems().playerHasItem(11235) && player.getItems().playerHasItem(12757)) {
					player.getItems().deleteItem2(11235, 1);
					player.getItems().deleteItem2(12757, 1);
					player.getItems().addItem(12766, 1);
					player.getDH().sendDialogues(568, 315);
				} else {
					player.getPA().removeAllWindows();
				}
			}
			if (player.dialogueAction == 111) {
				if (player.getItems().playerHasItem(11235) && player.getItems().playerHasItem(12759)) {
					player.getItems().deleteItem2(11235, 1);
					player.getItems().deleteItem2(12759, 1);
					player.getItems().addItem(12765, 1);
					player.getDH().sendDialogues(571, 315);
				} else {
					player.getPA().removeAllWindows();
				}
			}
			if (player.dialogueAction == 112) {
				if (player.getItems().playerHasItem(11235) && player.getItems().playerHasItem(12761)) {
					player.getItems().deleteItem2(11235, 1);
					player.getItems().deleteItem2(12761, 1);
					player.getItems().addItem(12767, 1);
					player.getDH().sendDialogues(574, 315);
				} else {
					player.getPA().removeAllWindows();
				}
			}
			if (player.dialogueAction == 113) {
				if (player.getItems().playerHasItem(11235) && player.getItems().playerHasItem(12763)) {
					player.getItems().deleteItem2(11235, 1);
					player.getItems().deleteItem2(12763, 1);
					player.getItems().addItem(12768, 1);
					player.getDH().sendDialogues(577, 315);
				} else {
					player.getPA().removeAllWindows();
				}
			}
			if (player.dialogueAction == 109) {
				if (player.getItems().playerHasItem(4153) && player.getItems().playerHasItem(12849)) {
					player.getItems().deleteItem2(4153, 1);
					player.getItems().deleteItem2(12849, 1);
					player.getItems().addItem(12848, 1);
					player.getDH().sendDialogues(565, 315);
				} else {
					player.getPA().removeAllWindows();
				}
			}
			if (player.dialogueAction == 108) {
				if (player.getItems().playerHasItem(11924) && player.getItems().playerHasItem(12802)) {
					player.getItems().deleteItem2(11924, 1);
					player.getItems().deleteItem2(12802, 1);
					player.getItems().addItem(12806, 1);
					player.getDH().sendDialogues(560, 315);
				} else {
					player.getPA().removeAllWindows();
				}
			}
			if (player.dialogueAction == 107) {
				if (player.getItems().playerHasItem(11926) && player.getItems().playerHasItem(12802)) {
					player.getItems().deleteItem2(11926, 1);
					player.getItems().deleteItem2(12802, 1);
					player.getItems().addItem(12807, 1);
					player.getDH().sendDialogues(560, 315);
				} else {
					player.getPA().removeAllWindows();
				}
			}
			if (player.dialogueAction == 106) {
				int worth = player.getBH().getNetworthForEmblems();
				long total = (long) worth + player.getBH().getBounties();
				if (total > Integer.MAX_VALUE) {
					player.sendMessage("You have to spend some bounties before obtaining any more.");
					player.getPA().removeAllWindows();
					player.nextChat = -1;
					return;
				}
				if (worth > 0) {
					BountyHunterEmblem.EMBLEMS.forEach(emblem -> player.getItems().deleteItem2(emblem.getItemId(),
							player.getItems().getItemAmount(emblem.getItemId())));
					player.getBH().setBounties(player.getBH().getBounties() + worth);
					player.sendMessage("You sold all of the emblems in your inventory for "
							+ Misc.insertCommas(Integer.toString(worth)) + " bounties.");
					player.getDH().sendDialogues(557, 315);
				} else {
					player.nextChat = -1;
					player.getPA().closeAllWindows();
				}
				return;
			}
			if (player.dialogueAction == 105) {
				if (player.getItems().playerHasItem(12804) && player.getItems().playerHasItem(11838)) {
					player.getItems().deleteItem2(12804, 1);
					player.getItems().deleteItem2(11838, 1);
					player.getItems().addItem(12809, 1);
					player.getDH().sendDialogues(552, -1);
				} else {
					player.getPA().removeAllWindows();
				}
				player.dialogueAction = -1;
				player.nextChat = -1;
				return;
			}
			if (player.dialogueAction == 104) {
				player.getDH().sendDialogues(549, 315);
				player.dialogueAction = -1;
				return;
			}
			if (player.dialogueAction == 101) {
				player.getDH().sendDialogues(546, 315);
				player.dialogueAction = -1;
				return;
			}
			if (player.dialogueAction == 102) {
				player.getDH().sendDialogues(547, 315);
				player.dialogueAction = -1;
				return;
			}
			if (player.dialogueAction == 200) {
				player.getPA().exchangeItems(PointExchange.PK_POINTS, 2996, 1);
				player.dialogueAction = -1;
				player.teleAction = -1;
				return;
			} else if (player.dialogueAction == 201) {
				player.getDH().sendDialogues(503, -1);
				return;
			} else if (player.dialogueAction == 202) {
				player.getPA().exchangeItems(PointExchange.VOTE_POINTS, 1464, 1);
				player.dialogueAction = -1;
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 2258) {
				TeleportExecutor.teleport(player, new Position(3039, 4834, 0), true); // first
				// click
				// teleports
				// second
				// click
				// open
				// shops
			}
			if (player.dialogueAction == 12000) {
				for (int i = 8144; i < 8195; i++) {
					player.getPA().sendFrame126("", i);
				}
				player.getPA().sendFrame126("@dre@Account Information for @blu@" + player.playerName + "", 8144);
				player.getPA().sendFrame126("", 8145);
				player.getPA().sendFrame126("@blu@Donator Points@bla@ - " + player.donatorPoints + "", 8150);
				player.getPA().sendFrame126("@blu@Vote Points@bla@ - " + player.votePoints + "", 8149);
				player.getPA().sendFrame126("@blu@Amount Donated@bla@ - " + player.amDonated + "", 8151);
				player.getPA().sendFrame126("@blu@PC Points@bla@ - " + player.pcPoints + "", 8152);
				player.getPA().sendFrame126("@blu@Time Played: @bla@" + player.pTime / 2 / 60 + " mins.", 8153);
				player.getPA().showInterface(8134);
			}
			if (player.dialogueAction == 4000) {
				if (player.inWild() || player.inCamWild() || player.inDuelArena()
						|| Server.getMultiplayerSessionListener().inAnySession(player)) {
					return;
				}
				if (player.getItems().playerHasItem(2697, 1)) {
					if (player.getRights().getValue() >= 1) {
						player.getDH()
								.sendStatement("You cannot read this scroll as you are already a Donator or higher.");
						return;
					}
					player.getItems().deleteItem(2697, 1);
					player.gfx100(263);
					player.setRights(Rights.DONATOR);
					player.sendMessage("You are now a Donator. You must relog for changes to take effect!");
					player.getPA().closeAllWindows();
				}
			}
			if (player.dialogueAction == 4001) {
				if (player.inWild() || player.inCamWild() || player.inDuelArena()
						|| Server.getMultiplayerSessionListener().inAnySession(player)) {
					return;
				}
				if (player.getItems().playerHasItem(2698, 1)) {
					if (player.getRights().getValue() > 5) {
						player.getDH().sendStatement(
								"You cannot read this scroll as you are already a Super Donator or higher.");
						return;
					}
					player.getItems().deleteItem(2698, 1);
					player.gfx100(263);
					player.setRights(Rights.HONORED_DONATOR);
					player.sendMessage("You are now a Super Donator. You must relog for changes to take effect!");
					player.getPA().closeAllWindows();
				}
			}
			if (player.dialogueAction == 4002) {
				if (player.inWild() || player.inCamWild() || player.inDuelArena()
						|| Server.getMultiplayerSessionListener().inAnySession(player)) {
					return;
				}
				if (player.getItems().playerHasItem(2699, 1)) {
					if (player.getRights().getValue() >= 1 && player.getRights().getValue() < 5
							|| player.getRights().getValue() == 7) {
						player.getDH()
								.sendStatement("You cannot read this scroll as you are already a Extreme Donator.");
						return;
					}
					player.getItems().deleteItem(2699, 1);
					player.gfx100(263);
					player.setRights(Rights.RESPECTED_DONATOR);
					player.sendMessage("You are now a Extreme Donator. You must relog for changes to take effect!");
					player.getPA().closeAllWindows();
				}
			}
			if (player.dialogueAction == 4003) {
				if (player.inWild() || player.inCamWild() || player.inDuelArena()
						|| Server.getMultiplayerSessionListener().inAnySession(player)) {
					return;
				}
				if (player.getItems().playerHasItem(2700, 1)) {
					if (player.getRights().getValue() >= 1 && player.getRights().getValue() < 5
							|| player.getRights().getValue() == 8 || player.getRights().getValue() == 9) {
						player.getDH().sendStatement("You cannot read this scroll as you are already a VIP or higher.");
						return;
					}
					player.getItems().deleteItem(2700, 1);
					player.gfx100(263);
					player.setRights(Rights.LEGENDARY_DONATOR);
					player.sendMessage("You are now a VIP. You must relog for changes to take effect!");
					player.getPA().closeAllWindows();
				}
			}
			if (player.dialogueAction == 4004) {
				if (player.inWild() || player.inCamWild() || player.inDuelArena()
						|| Server.getMultiplayerSessionListener().inAnySession(player)) {
					return;
				}
				if (player.getItems().playerHasItem(2701, 1)) {
					player.getItems().deleteItem(2701, 1);
					player.gfx100(263);
					player.playerTitle = "Gambler";
					player.getItems().addItemUnderAnyCircumstance(15098, 1);
					player.sendMessage("You are now a Gambler. A dice has been added to your bank!");
					player.getPA().closeAllWindows();
				}
			}
			if (player.dialogueAction == 206) {
				player.getItems().resetItems(3214);
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 2109) {
				if (player.absX >= 2438 && player.absX <= 2439 && player.absY >= 5168 && player.absY <= 5169) {
					player.getFightCave().create(1);
				}
			}
			if (player.dialogueAction == 113239) {
				if (player.inDuelArena()) {
					return;
				}
				player.getItems().addItem(557, 1000);
				player.getItems().addItem(560, 1000);
				player.getItems().addItem(9075, 1000);
				player.getPA().removeAllWindows();
				player.dialogueAction = 0;
			}
			if (player.newPlayerAct == 1) {
				// c.isNewPlayer = false;
				player.newPlayerAct = 0;
				TeleportExecutor.teleport(player, new Position(Config.EDGEVILLE_X, Config.EDGEVILLE_Y, 0), true);
				player.getPA().removeAllWindows();
			}
			if (player.dialogueAction == 6) {
				player.sendMessage("Slayer will be enabled in some minutes.");
				// c.getSlayer().generateTask();
				// player.getPA()().sendFrame126("@whi@Task:
				// @gre@"+Server.npcHandler.getNpcListName(c.slayerTask)+
				// " ", 7383);
				// player.getPA()().closeAllWindows();
			}
			if (player.dialogueAction == 29) {
				if (player.isInBarrows() || player.isInBarrows2()) {
					player.getBarrows().checkCoffins();
					player.getPA().removeAllWindows();
					return;
				} else {
					player.getPA().removeAllWindows();
					player.sendMessage("@blu@You can only do this while you're at barrows.");
				}
			} else if (player.dialogueAction == 27) {
				player.getBarrows().cantWalk = false;
				player.getPA().removeAllWindows();
				// c.getBarrowsChallenge().start();
				return;
			} else if (player.dialogueAction == 25) {
				player.getDH().sendDialogues(26, 0);
				return;
			}
			if (player.dialogueAction == 162) {
				player.sendMessage("You successfully emptied your inventory.");
				player.getPA().removeAllItems();
				player.dialogueAction = 0;
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 508) {
				player.getDH().sendDialogues(1030, 925);
				return;
			}
			if (player.doricOption2) {
				player.getDH().sendDialogues(310, 284);
				player.doricOption2 = false;
			}
			if (player.rfdOption) {
				player.getDH().sendDialogues(26, -1);
				player.rfdOption = false;
			}
			if (player.horrorOption) {
				player.getDH().sendDialogues(35, -1);
				player.horrorOption = false;
			}
			if (player.dtOption) {
				player.getDH().sendDialogues(44, -1);
				player.dtOption = false;
			}
			if (player.dtOption2) {
				if (player.lastDtKill == 0) {
					player.getDH().sendDialogues(65, -1);
				} else {
					player.getDH().sendDialogues(49, -1);
				}
				player.dtOption2 = false;
			}

			if (player.caOption2) {
				player.getDH().sendDialogues(106, player.npcType);
				player.caOption2 = false;
			}
			if (player.caOption2a) {
				player.getDH().sendDialogues(102, player.npcType);
				player.caOption2a = false;
			}

			if (player.dialogueAction == 1) {
				player.getDH().sendDialogues(38, -1);
			}
			break;

		case 9167:
			if (player.dialogueAction == 5793) {
				//player.outStream.createFrame(27);
				//player.lotteryAmount = true;
				LotteryController.newEntryPKP(player, player.npcType);
			}
			if (player.dialogueAction == 1012) {
				player.getDH().sendDialogues(1033, player.npcType);
				return;
			}
                    if (player.dialogueAction == 2500000) {
                        PlayerShops.openMainInterface(player);
                        return;
                    }
			if (player.dialogueAction == 75009) {
				player.enchantDragon = true;
				player.getOutStream().createFrame(27);
				return;
			}
			//pure armor shop
			if (player.dialogueAction == 3616) {
			player.getShops().openShop(54);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			//melee weapon shop
			if (player.dialogueAction == 3609) {
			player.getShops().openShop(51);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			if (player.dialogueAction == 750) {// Deposit
				if (player.hasPokerName == false) {
					player.getPA().removeAllWindows();
					player.getDH().sendStatement("You must first create an account at", "::Poker",
							"Then you come ingame and do the command", " ::setpoker pokername pokerpassword");
					player.nextChat = -1;
					return;
				}
				player.getPA().showInterface(62500);
				// player.getPA()().showInterface(62500);
			}
			if (player.dialogueAction == 3700) {
				player.needsNewTask = true;
				player.getEasy().generateTask();
				return;
			}
			if (player.dialogueAction == 700) {
				player.getDH().sendDialogues(702, player.npcType);
			}
			if (player.dialogueAction == 3500) {
				player.getShops().openShop(58);
			}
			// }
			switch (player.teleAction) {

			case 2:
				TeleportExecutor.teleport(player, new Position(2804, 10001, 0), true);
				break;
			}

			if (player.teleAction == 60) {
				TeleportExecutor.teleport(player, new Position(3565, 3306, 0), true);
				return;
			}
			if (player.teleAction == 200) {
				player.sendMessage("@red@Stake only what you can afford to lose!");
				TeleportExecutor.teleport(player, new Position(3365, 3266, 0), true);
				return;
			}
			if (player.teleAction == 80) {
				TeleportExecutor.teleport(player, new Position(3429, 3538, 0), true);
				return;
			}
			if (player.dialogueAction == 12200) {
				player.getShops().openShop(12);
				return;
			}
			if (player.dialogueAction == 4005) {
				player.pkp += 5000;
				player.amDonated += 10;
				player.getPA().loadQuests();
				if (player.amDonated >= 10 && player.amDonated < 50 && player.getRights().getValue() == 0) {
					player.setRights(Rights.DONATOR);
				}
				if (player.amDonated >= 50 && player.amDonated < 150 && player.getRights().getValue() <= 5
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.HONORED_DONATOR);
				}
				if (player.amDonated >= 150 && player.amDonated < 500 && player.getRights().getValue() <= 6
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.RESPECTED_DONATOR);
				}
				if (player.amDonated >= 500 && player.getRights().getValue() <= 7 && !player.getRights().isStaff()) {
					player.setRights(Rights.LEGENDARY_DONATOR);
				}
				player.AnnounceClaim(10);
				player.getItems().deleteItem(2690, 1);
				player.getPA().closeAllWindows();
				player.sendMessage("Thank you, for your contribution to Pkers.Ca.");
				player.sendMessage("You will need to re-log for these changes to apply properly.");
				return;
			}
			if (player.dialogueAction == 4006) {
				player.pkp += 30000;
				player.amDonated += 50;
				player.getPA().loadQuests();
				player.getPA().closeAllWindows();
				if (player.amDonated >= 10 && player.amDonated < 50 && player.getRights().getValue() == 0) {
					player.setRights(Rights.DONATOR);
				}
				if (player.amDonated >= 50 && player.amDonated < 150 && player.getRights().getValue() <= 5
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.HONORED_DONATOR);
				}
				if (player.amDonated >= 150 && player.amDonated < 500 && player.getRights().getValue() <= 6
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.RESPECTED_DONATOR);
				}
				if (player.amDonated >= 500 && player.getRights().getValue() <= 7 && !player.getRights().isStaff()) {
					player.setRights(Rights.LEGENDARY_DONATOR);
				}
				player.AnnounceClaim(50);
				player.getPA().closeAllWindows();
				player.getItems().deleteItem(2691, 1);
				player.sendMessage("Thank you, for your contribution to Pkers.Ca.");
				player.sendMessage("You will need to re-log for these changes to apply properly.");
				return;
			}
			if (player.dialogueAction == 4007) {
				player.pkp += 100000;
				player.amDonated += 150;
				player.getPA().loadQuests();
				if (player.amDonated >= 10 && player.amDonated < 50 && player.getRights().getValue() == 0) {
					player.setRights(Rights.DONATOR);
				}
				if (player.amDonated >= 50 && player.amDonated < 150 && player.getRights().getValue() <= 5
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.HONORED_DONATOR);
				}
				if (player.amDonated >= 150 && player.amDonated < 500 && player.getRights().getValue() <= 6
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.RESPECTED_DONATOR);
				}
				if (player.amDonated >= 500 && player.getRights().getValue() <= 7 && !player.getRights().isStaff()) {
					player.setRights(Rights.LEGENDARY_DONATOR);
				}
				player.AnnounceClaim(150);
				player.getPA().closeAllWindows();
				player.getItems().deleteItem(2692, 1);
				player.sendMessage("Thank you, for your contribution to Pkers.Ca.");
				player.sendMessage("You will need to re-log for these changes to apply properly.");
				return;
			}
			if (player.dialogueAction == 100) {
				player.getShops().openShop(80);
				return;
			}
			if (player.dialogueAction == 14400) {
				TeleportExecutor.teleport(player, new Position(2474, 3438, 0), true);
				player.sendMessage("You will gain XP after each lap");
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 2245) {
				TeleportExecutor.teleport(player, new Position(2110, 3915, 0), true);
				player.sendMessage("High Priest teleported you to @red@Lunar Island@bla@.");
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 508) {
				player.getDH().sendDialogues(1030, 925);
				return;
			}
			if (player.dialogueAction == 502) {
				player.getDH().sendDialogues(1030, 925);
				return;
			}
			if (player.dialogueAction == 251) {
				player.getPA().openUpBank();
			}
			if (player.teleAction == 200) {
				// TeleportExecutor.teleport(c, new Position(2662, 2652, 0));
				player.sendMessage("Currently disabled for repairs");
			}
			if (player.dialogueAction == 1340) {
				player.getDH().sendDialogues(814, 5721);
			}
			if (player.doricOption) {
				player.getDH().sendDialogues(306, 284);
				player.doricOption = false;
			}
			break;
		case 9168:
			if (player.dialogueAction == 5793) {
				player.getDH().sendDialogues(5794, player.npcType);
			}
			if (player.dialogueAction == 1012) {
				player.getDH().sendDialogues(1020, player.npcType);
				return;
			}
                    if (player.dialogueAction == 2500000) {
                        PlayerShops.openShop(player, player);
                        return;
                    }
			if (player.dialogueAction == 75009) {
				player.enchantOnyx = true;
				player.getOutStream().createFrame(27);
				return;
			}
			//food and potions shop
			if (player.dialogueAction == 3616) {
			player.getShops().openShop(57);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			//barrows and more shop
			if (player.dialogueAction == 3609) {
			player.getShops().openShop(52);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			if (player.dialogueAction == 1340) {
				player.getDH().sendDialogues(816, 5721);
			}
			if (player.dialogueAction == 3700) {
				player.needsNewTask = true;
				player.getMedium().generateTask();
				return;
			}
			if (player.dialogueAction == 750) {// Withdraw
				if (player.hasPokerName == false) {
					player.getPA().removeAllWindows();
					player.getDH().sendStatement("You must first create an account at", "::Poker",
							"Then you come ingame and do the command", " ::setpoker pokername pokerpassword");
					player.nextChat = -1;

					return;
				}
				player.getDH().sendStatement("To withdraw use the command ", "::withdraw amount");
				player.nextChat = -1;
			}
			if (player.dialogueAction == 700) {
				player.getDH().sendDialogues(705, player.npcType);
			}
			/*if (player.dialogueAction == 3500) {
				player.getPA().removeAllWindows();
				player.sendMessage("Coming soon to a Pkers.Ca near you!");
			}*/
			switch (player.teleAction) {

			case 2:
				TeleportExecutor.teleport(player, new Position(1748, 5326, 0), true);
				player.teleAction = -1;
				break;
			}
			if (player.teleAction == 200) {
				TeleportExecutor.teleport(player, new Position(3565, 3306, 0), true);
				return;
			}
			if (player.dialogueAction == 4005) {
				player.donatorPoints += 50;
				player.amDonated += 10;
				player.getPA().loadQuests();
				if (player.amDonated >= 10 && player.amDonated < 50 && player.getRights().getValue() == 0) {
					player.setRights(Rights.DONATOR);
				}
				if (player.amDonated >= 50 && player.amDonated < 150 && player.getRights().getValue() <= 5
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.HONORED_DONATOR);
				}
				if (player.amDonated >= 150 && player.amDonated < 500 && player.getRights().getValue() <= 6
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.RESPECTED_DONATOR);
				}
				if (player.amDonated >= 500 && player.getRights().getValue() <= 7 && !player.getRights().isStaff()) {
					player.setRights(Rights.LEGENDARY_DONATOR);
				}
				player.AnnounceClaim(10);
				player.getPA().closeAllWindows();
				player.getItems().deleteItem(2690, 1);
				player.sendMessage("Thank you, for your contribution to Pkers.Ca.");
				player.sendMessage("You will need to re-log for these changes to apply properly.");
				return;
			}
			if (player.dialogueAction == 4006) {
				player.donatorPoints += 250;
				player.amDonated += 50;
				player.getPA().loadQuests();
				if (player.amDonated >= 10 && player.amDonated < 50 && player.getRights().getValue() == 0) {
					player.setRights(Rights.DONATOR);
				}
				if (player.amDonated >= 50 && player.amDonated < 150 && player.getRights().getValue() <= 5
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.HONORED_DONATOR);
				}
				if (player.amDonated >= 150 && player.amDonated < 500 && player.getRights().getValue() <= 6
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.RESPECTED_DONATOR);
				}
				if (player.amDonated >= 500 && player.getRights().getValue() <= 7 && !player.getRights().isStaff()) {
					player.setRights(Rights.LEGENDARY_DONATOR);
				}
				player.AnnounceClaim(50);
				player.getPA().closeAllWindows();
				player.getItems().deleteItem(2691, 1);
				player.sendMessage("Thank you, for your contribution to Pkers.Ca.");
				player.sendMessage("You will need to re-log for these changes to apply properly.");
				return;
			}
			if (player.dialogueAction == 4007) {
				player.donatorPoints += 1000;
				player.amDonated += 150;
				player.getPA().loadQuests();
				if (player.amDonated >= 10 && player.amDonated < 50 && player.getRights().getValue() == 0) {
					player.setRights(Rights.DONATOR);
				}
				if (player.amDonated >= 50 && player.amDonated < 150 && player.getRights().getValue() <= 5
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.HONORED_DONATOR);
				}
				if (player.amDonated >= 150 && player.amDonated < 500 && player.getRights().getValue() <= 6
						&& !player.getRights().isStaff()) {
					player.setRights(Rights.RESPECTED_DONATOR);
				}
				if (player.amDonated >= 500 && player.getRights().getValue() <= 7 && !player.getRights().isStaff()) {
					player.setRights(Rights.LEGENDARY_DONATOR);
				}
				player.AnnounceClaim(150);
				player.getPA().closeAllWindows();
				player.getItems().deleteItem(2692, 1);
				player.sendMessage("Thank you, for your contribution to Pkers.Ca.");
				player.sendMessage("You will need to re-log for these changes to apply properly.");
				return;
			}
			if (player.dialogueAction == 12200) {
				player.getShops().openShop(49);
				return;
			}
			if (player.teleAction == 80) {
				TeleportExecutor.teleport(player, new Position(2884, 9800, 0), true);
				return;
			}
			if (player.teleAction == 60) {
				TeleportExecutor.teleport(player, new Position(2847, 3541, 0), true);
				return;
			}
			if (player.dialogueAction == 100) {
				player.getDH().sendDialogues(545, 315);
				return;
			}
			if (player.dialogueAction == 14400) {
				TeleportExecutor.teleport(player, new Position(3004, 3935, 0), true);
				player.sendMessage("You will gain XP after each lap. Use the portal at the gate to get home.");
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 2245) {
				TeleportExecutor.teleport(player, new Position(3230, 2915, 0), true);
				player.sendMessage("High Priest teleported you to @red@Desert Pyramid@bla@.");
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 508) {
				player.getDH().sendDialogues(1027, 925);
				return;
			}
			if (player.dialogueAction == 502) {
				player.getDH().sendDialogues(1027, 925);
				return;
			}
			if (player.teleAction == 200) {
				TeleportExecutor.teleport(player, new Position(3365, 3266, 0), true);

			}
			if (player.doricOption) {
				player.getDH().sendDialogues(303, 284);
				player.doricOption = false;
			}

			break;
		case 9169:
			if (player.dialogueAction == 5793) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 1012) {
				player.getDH().sendDialogues(1021, player.npcType);
				return;
			}
                    if (player.dialogueAction == 2500000) {
                        PlayerShops.openSellInterface(player);
                        return;
                    }
			if (player.dialogueAction == 3500) {
				player.sendMessage("Currently disabled till full release :)");
				player.getPA().closeAllWindows();
				// c.getDH().sendDialogues(3501, c.npcType);
			}
			//amulet and rings shop
			if (player.dialogueAction == 3616) {
			player.getShops().openShop(58);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			//melee armor shop
			if (player.dialogueAction == 3609) {
			player.getShops().openShop(53);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			if (player.dialogueAction == 3700) {
				player.needsNewTask = true;
				player.getHard().generateTask();
				return;
			}
			if (player.dialogueAction == 1340) {
				player.getShops().openShop(74);
			}
			if (player.dialogueAction == 75009) {
				player.getPA().closeAllWindows();
				return;
			}
			if (player.dialogueAction == 750) {
				player.getShops().openShop(56);
				return;
			}
			if (player.dialogueAction == 129) {
				if (player.getLostItems().size() == 0) {
					player.getDH().sendDialogues(639, 2040);
				} else {
					player.getDH().sendDialogues(640, 2040);
				}
				return;
			}

			switch (player.teleAction) {
			case 2:
				player.getDH().sendDialogues(3324, -1);
				return;
			}
			if (player.dialogueAction == 14400 || player.dialogueAction == 100 || player.dialogueAction == 4005
					|| player.dialogueAction == 4006 || player.dialogueAction == 4007) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 12200) {
				player.getShops().openShop(50);
				return;
			}
			if (player.teleAction == 80) {
				player.getDH().sendOption5("Brimhaven Dungeon", "Edgeville Dungeon", "Mithril Dragons",
						"Relekka Dungeon", "@blu@Next Page");
				player.teleAction = 2;
				return;
			}
			if (player.teleAction == 60) {
				player.getDH().sendOption5("Barrows", "Duel Arena", "Fight Caves", "Fishing tourney", "@blu@Next Page");
				player.teleAction = 200;
				return;
			}
			if (player.dialogueAction == 2245) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 508) {
				player.nextChat = 0;
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 502 || player.dialogueAction == 700) {
				player.nextChat = 0;
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 251) {
				player.getDH().sendDialogues(1015, 394);
			}
			if (player.teleAction == 200) {
				TeleportExecutor.teleport(player, new Position(2439, 5169, 0), true);
				player.sendMessage("Use the cave entrance to start.");
			}
			if (player.doricOption) {
				player.getDH().sendDialogues(299, 284);
			}
			break;

		case 9158:
			if (player.dialogueAction == 12348) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 12349) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 12351) {
				player.getDH().sendStatement("Suggest 2006 setups on the forums! Coming soon...");
			}
			//costumes items
			if (player.dialogueAction == 3617) {
			player.getShops().openShop(60);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			//Donator shop Titles
			if (player.dialogueAction == 3613) {
			player.getPA().showInterface(3200);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			//empty dialogue
			if (player.dialogueAction == 850) {
			player.getPA().removeAllWindows();
			player.getPA().loadQuests();
			player.sendMessage("You decided not to empty your inventory!");
			return;
			}
			  //magic armor shop
		    if (player.dialogueAction == 3615) {
			player.getShops().openShop(56);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			//range armor shop
			if (player.dialogueAction == 3608) {
			player.getShops().openShop(50);
			player.getPA().loadQuests();
			player.dialogueAction = 0;
		    }
			if(player.teleAction == 6666) {
				player.getPA().closeAllWindows();
				player.teleAction = -1;
			}
			if (player.dialogueAction == 3601) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 2223) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 3501) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 761) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 9500) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 1140) {
				if (player.getItems().playerHasItem(9748) && player.getItems().playerHasItem(9751)
						&& player.getItems().playerHasItem(9754) && player.getItems().playerHasItem(9757)
						&& player.getItems().playerHasItem(9760) && player.getItems().playerHasItem(9763)
						&& player.getItems().playerHasItem(9766) && player.getItems().playerHasItem(9769)
						&& player.getItems().playerHasItem(9772) && player.getItems().playerHasItem(9775)
						&& player.getItems().playerHasItem(9778) && player.getItems().playerHasItem(9781)
						&& player.getItems().playerHasItem(9784) && player.getItems().playerHasItem(9787)
						&& player.getItems().playerHasItem(9793) && player.getItems().playerHasItem(9796)
						&& player.getItems().playerHasItem(9799) && player.getItems().playerHasItem(9802)
						&& player.getItems().playerHasItem(9805) && player.getItems().playerHasItem(9808)
						&& player.getItems().playerHasItem(9811) && player.getItems().playerHasItem(9949)) {
					player.getItems().deleteItem2(9748, 1);
					player.getItems().deleteItem2(9751, 1);
					player.getItems().deleteItem2(9754, 1);
					player.getItems().deleteItem2(9757, 1);
					player.getItems().deleteItem2(9760, 1);
					player.getItems().deleteItem2(9763, 1);
					player.getItems().deleteItem2(9766, 1);
					player.getItems().deleteItem2(9769, 1);
					player.getItems().deleteItem2(9772, 1);
					player.getItems().deleteItem2(9775, 1);
					player.getItems().deleteItem2(9778, 1);
					player.getItems().deleteItem2(9781, 1);
					player.getItems().deleteItem2(9784, 1);
					player.getItems().deleteItem2(9787, 1);
					player.getItems().deleteItem2(9793, 1);
					player.getItems().deleteItem2(9796, 1);
					player.getItems().deleteItem2(9799, 1);
					player.getItems().deleteItem2(9802, 1);
					player.getItems().deleteItem2(9805, 1);
					player.getItems().deleteItem2(9808, 1);
					player.getItems().deleteItem2(9811, 1);
					player.getItems().deleteItem2(9949, 1);
					player.getItems().addItem(13280, 1);
					player.getItems().addItem(13281, 1);
					player.getDH().sendStatement("You combined your skillcapes to create a Max Cape.");
					player.nextChat = -1;
				} else {
					player.getDH().sendStatement("You need all the skillcapes in your inventory.");
					player.nextChat = -1;
				}
				return;
			}
			if (player.dialogueAction == 1141) {
				player.getDH().sendDialogues(652, -1);
				return;
			}
			if (player.teleAction == 3000) {
				TeleportExecutor.teleport(player, new Position(1829, 3601, 0), true);
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 1142) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 200001 || player.dialogueAction == 400002 || player.dialogueAction == 400004) {
				player.getPA().closeAllWindows();
			}

			if (player.dialogueAction == 1100001) {
				player.getPA().closeAllWindows();
			}

			if (player.dialogueAction == 85000 || player.dialogueAction == 125) {
				player.getPA().closeAllWindows();
				return;
			}

			if (player.dialogueAction == 1000) {
				player.getPA().closeAllWindows();
				ClickItem.flowerTime = 20;
			}

			if (player.dialogueAction == 6290) {
				player.getPA().closeAllWindows();
				return;
			}
			if (player.dialogueAction == 129) {
				SerializablePair<String, Long> pair = Server.getServerData().getZulrahTime();
				if (pair == null || pair.getFirst() == null || pair.getSecond() == null) {
					player.getDH().sendDialogues(643, 2040);
				} else {
					// c.getDH().sendDialogues(644, 2040);
					player.getPA().closeAllWindows();
				}
				return;
			}
			if (player.dialogueAction == 12400) {
				player.getShops().openShop(115);
				return;
			}
			if (player.dialogueAction == 14400) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 12600) {
				player.getShops().openShop(116);
				return;
			}
			if (player.dialogueAction == 149) {
				player.getShops().openShop(9);
				player.dialogueAction = -1;
				return;
			}
			/*
			 * if (c.teleAction == 110) { player.sendMessage(
			 * "Armadyl instance is disabled till furthur notice!"); return; }
			 */
			if (player.teleAction == 110) {
				if (player.ARMADYL_CLICKS >= 1) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getArmadyl().getInstancedArmadyl() != null) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.pkp >= 25) {
					player.getArmadyl().initialize();
					player.pkp -= 25;
					player.ARMADYL_CLICKS = 1;
					player.dialogueAction = -1;
					player.teleAction = -1;
					return;
				} else {
					player.sendMessage("You need 25 PK Points to activate this instance!");
					player.getPA().closeAllWindows();
					return;
				}
			}
			if (player.teleAction == 111) {
				if (player.BANDOS_CLICKS >= 1) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getBandos().getInstancedBandos() != null) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.pkp >= 25) {
					player.getBandos().initialize();
					player.pkp -= 25;
					player.BANDOS_CLICKS = 1;
					player.dialogueAction = -1;
					player.teleAction = -1;
					return;
				} else {
					player.sendMessage("You need 25 PK Points to activate this instance!");
					player.getPA().closeAllWindows();
					return;
				}
			}
			if (player.teleAction == 112) {
				if (player.SARADOMIN_CLICKS >= 1) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getSaradomin().getInstancedSaradomin() != null) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.pkp >= 25) {
					player.getSaradomin().initialize();
					player.pkp -= 25;
					player.SARADOMIN_CLICKS = 1;
					player.dialogueAction = -1;
					player.teleAction = -1;
					return;
				} else {
					player.sendMessage("You need 25 PK Points to activate this instance!");
					player.getPA().closeAllWindows();
					return;
				}
			}
			if (player.teleAction == 113) {
				if (player.ZAMORAK_CLICKS >= 1) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.getZamorak().getInstancedZamorak() != null) {
					player.sendMessage("You already have an active instance!");
					player.getPA().closeAllWindows();
					return;
				}
				if (player.pkp >= 25) {
					player.getZamorak().initialize();
					player.pkp -= 25;
					player.ZAMORAK_CLICKS = 1;
					player.dialogueAction = -1;
					player.teleAction = -1;
					return;
				} else {
					player.sendMessage("You need 25 PK Points to activate this instance!");
					player.getPA().closeAllWindows();
					return;
				}
			}
			if (player.dialogueAction == 86000 || player.dialogueAction == 87000) {
				player.getPA().removeAllWindows();
				player.dialogueAction = -1;
			}
			if (player.dialogueAction == 126 || player.dialogueAction == 130) {
				player.getPA().removeAllWindows();
				player.dialogueAction = -1;
				player.teleAction = -1;
				return;
			}
			if (player.dialogueAction == 947) {
				player.getShops().openShop(111);
				player.dialogueAction = -1;
			}
			if (player.dialogueAction == 1338) {
				if (player.fishTourneySession != null) {
					player.fishTourneySession.removePlayer(player);
					player.getPA().removeAllWindows();
				}
			}
			if (player.dialogueAction == -1 && player.getCurrentCombination().isPresent()) {
				player.setCurrentCombination(Optional.empty());
				player.getPA().removeAllWindows();
				return;
			}
			if (player.dialogueAction == 3308) {
				player.getPA().removeAllWindows();
			}
			if (player.dialogueAction == 100 || player.dialogueAction == 120) {
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 200 || player.dialogueAction == 202
					|| player.dialogueAction >= 101 && player.dialogueAction <= 103 || player.dialogueAction == 106
					|| player.dialogueAction >= 109 && player.dialogueAction <= 114) {
				player.getPA().removeAllWindows();
				player.dialogueAction = -1;
				player.teleAction = -1;
				return;
			} else if (player.dialogueAction == 201) {
				player.getDH().sendDialogues(501, -1);
				return;
			}
			if (player.dialogueAction == 162) {
				player.dialogueAction = 0;
				player.getPA().closeAllWindows();
			}
			if (player.dialogueAction == 12001) {
				player.getPA().closeAllWindows();
			}
			/*
			 * if (c.dialogueAction == 12000) { for (int i = 8144; i < 8195;
			 * i++) { player.getPA()().sendFrame126("", i); } int[] frames1 = { 8149,
			 * 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160,
			 * 8161, 8162, 8163, 8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171,
			 * 8172, 8173, 8174, 8175 }; player.getPA()().sendFrame126(
			 * "@dre@Kill Tracker for @blu@" + c.playerName + "", 8144);
			 * player.getPA()().sendFrame126("", 8145); player.getPA()().sendFrame126(
			 * "@blu@Total kills@bla@ - " + c.getNpcDeathTracker().getTotal() +
			 * "", 8147); player.getPA()().sendFrame126("", 8148); int index1 = 0; for
			 * (Entry<NPCName, Integer> entry :
			 * c.getNpcDeathTracker().getTracker().entrySet()) { if (entry ==
			 * null) { continue; } if (index1 > frames1.length - 1) { break; }
			 * if (entry.getValue() > 0) { player.getPA()().sendFrame126("@blu@" +
			 * WordUtils.capitalize(entry.getKey().name().toLowerCase()) +
			 * ": @red@" + entry.getValue(), frames1[index1]); index1++; } }
			 * player.getPA()().showInterface(8134); }
			 */
			if (player.dialogueAction == 109) {
				player.getPA().removeAllWindows();
				player.dialogueAction = 0;
			}
			if (player.dialogueAction == 113239) {
				if (player.inDuelArena()) {
					return;
				}
				player.getItems().addItem(555, 1000);
				player.getItems().addItem(560, 1000);
				player.getItems().addItem(565, 1000);
				player.getPA().removeAllWindows();
				player.dialogueAction = 0;
			}
			if (player.dialogueAction == 2301) {
				player.getPA().removeAllWindows();
				player.dialogueAction = 0;
			}
			if (player.newPlayerAct == 1) {
				player.newPlayerAct = 0;
				player.sendMessage(
						"@red@There is nothing to do in Crandor, i must teleport home and start playing ab.");
				player.getPA().removeAllWindows();
			}
			if (player.doricOption2) {
				player.getDH().sendDialogues(309, 284);
				player.doricOption2 = false;
			}
			/*
			 * if (c.dialogueAction == 8) { player.getPA()().fixAllBarrows(); } else {
			 * c.dialogueAction = 0; player.getPA()().removeAllWindows(); }
			 */
			if (player.dialogueAction == 27) {
				player.getPA().removeAllWindows();
			}
			if (player.caOption2a) {
				player.getDH().sendDialogues(136, player.npcType);
				player.caOption2a = false;
			}
			break;
		/* VENG */
		case 118098:
			player.getPA().castVeng();
			break;
		/**
		 * Specials *
		 */
		case 48034:
			player.specBarId = 12335;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29163:
			player.specBarId = 7611;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29124:
			player.specBarId = 7561;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29074:
			player.specBarId = 7511;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		/*
		 * case 29049: c.specBarId = 7486; c.usingSpecial = !c.usingSpecial;
		 * player.getItems()().updateSpecialBar(); break;
		 */

		case 33033:
			player.specBarId = 8505;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29199:
			player.specBarId = 7636;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29049: // needs testing
		case 29038:
			if (player.playerEquipment[Player.playerWeapon] == 4153
					|| player.playerEquipment[Player.playerWeapon] == 12848) {
				player.specBarId = 7486;
				player.getCombat().handleGmaulPlayer();
				player.getItems().updateSpecialBar();
			} else {
				player.specBarId = 7486;
				player.usingSpecial = !player.usingSpecial;
				player.getItems().updateSpecialBar();
			}
			break;

		case 30108:
			player.specBarId = 7812;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 48023:
			player.specBarId = 12335;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29138:
			player.specBarId = 7586;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29113:
			player.specBarId = 7561;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29238:
			player.specBarId = 7686;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		/**
		 * Dueling *
		 */
		/*
		 * case 26065: // no forfeit case 26040: c.duelSlot = -1;
		 * c.getTradeAndDuel().selectRule(0); break;
		 */

		case 26065:
		case 26040:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.FORFEIT);
			break;

		case 26066: // no movement
		case 26048:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			if (!duelSession.getRules().contains(Rule.FORFEIT)) {
				duelSession.toggleRule(player, Rule.FORFEIT);
			}
			duelSession.toggleRule(player, Rule.NO_MOVEMENT);
			break;

		case 26069: // no range
		case 26042:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_RANGE);
			break;

		case 26070: // no melee
		case 26043:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_MELEE);
			break;

		case 26071: // no mage
		case 26041:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_MAGE);
			break;

		case 26072: // no drinks
		case 26045:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_DRINKS);
			break;

		case 26073: // no food
		case 26046:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_FOOD);
			break;

		case 26074: // no prayer
		case 26047:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_PRAYER);
			break;

		case 26076: // obsticals
		case 26075:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.OBSTACLES);
			break;

		case 2158: // fun weapons
		case 2157:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			if (duelSession.getRules().contains(Rule.WHIP_AND_DDS)) {
				duelSession.toggleRule(player, Rule.WHIP_AND_DDS);
				return;
			}
			if (!Rule.WHIP_AND_DDS.getReq().get().meets(player)) {
				player.getPA().sendString("You must have a whip and dragon dagger to select this.", 6684);
				return;
			}
			if (!Rule.WHIP_AND_DDS.getReq().get().meets(duelSession.getOther(player))) {
				player.getPA().sendString("Your opponent does not have a whip and dragon dagger.", 6684);
				return;
			}
			if (duelSession.getStage().getStage() != MultiplayerSessionStage.OFFER_ITEMS) {
				player.sendMessage("You cannot change rules whilst on the second interface.");
				return;
			}
			duelSession.getRules().reset();
			for (Rule rule : Rule.values()) {
				int index11 = rule.ordinal();
				if (index11 == 3 || index11 == 8 || index11 == 10 || index11 == 14) {
					continue;
				}
				duelSession.toggleRule(player, rule);
			}
			break;

		case 30136: // sp attack
		case 30137:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_SPECIAL_ATTACK);
			break;

		case 53245: // no helm
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_HELM);
			break;

		case 53246: // no cape
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_CAPE);
			break;

		case 53247: // no ammy
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_AMULET);
			break;

		case 53249: // no weapon
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_WEAPON);
			break;

		case 53250: // no body
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_BODY);
			break;

		case 53251: // no shield
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_SHIELD);
			break;

		case 53252: // no legs
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_LEGS);
			break;

		case 53255: // no gloves
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_GLOVES);
			break;

		case 53254: // no boots
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_BOOTS);
			break;

		case 53253: // no rings
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_RINGS);
			break;

		case 53248: // no arrows
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.isNull(duelSession)) {
				return;
			}
			duelSession.toggleRule(player, Rule.NO_ARROWS);
			break;

		case 26018:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (System.currentTimeMillis() - player.getDuel().getLastAccept() < 1000) {
				return;
			}
			player.getTrade().setLastAccept(System.currentTimeMillis());
			if (Objects.nonNull(duelSession) && duelSession instanceof DuelSession) {
				duelSession.accept(player, MultiplayerSessionStage.OFFER_ITEMS);
			}
			break;

		case 25120:
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (System.currentTimeMillis() - player.getDuel().getLastAccept() < 1000) {
				return;
			}
			player.getTrade().setLastAccept(System.currentTimeMillis());
			if (Objects.nonNull(duelSession) && duelSession instanceof DuelSession) {
				duelSession.accept(player, MultiplayerSessionStage.CONFIRM_DECISION);
			}
			break;

		case 4169: // god spell charge
			player.usingMagic = true;
			if (player.getCombat().checkMagicReqs(48)) {
				if (System.currentTimeMillis() - player.godSpellDelay < 300000L) {
					player.sendMessage("You still feel the charge in your body!");
				} else {
					player.godSpellDelay = System.currentTimeMillis();
					player.sendMessage("You feel charged with a magical power!");
					player.gfx100(player.MAGIC_SPELLS[48][3]);
					player.animation(player.MAGIC_SPELLS[48][2]);
					player.usingMagic = false;
				}
			}
			break;

		case 154:
			if (player.getPA().wearingCape(player.playerEquipment[player.playerCape])) {
				player.stopMovement();
				player.gfx0(player.getPA().skillcapeGfx(player.playerEquipment[player.playerCape]));
				player.animation(player.getPA().skillcapeEmote(player.playerEquipment[player.playerCape]));
			} else {
				player.sendMessage("You must be wearing a Skillcape to do this emote.");
			}
			break;
		case 152:
		case 121042:
			if (player.runEnergy < 1) {
				player.isRunning = false;
				player.getPA().setConfig(173, 0);
				return;
			}
			player.isRunning2 = !player.isRunning2;
			int frame = player.isRunning2 == true ? 1 : 0;
			player.getPA().sendFrame36(173, frame);
			break;
		case 9154:
			long buttonDelay = 0;
			if (System.currentTimeMillis() - buttonDelay > 2000) {
				player.logout();
				buttonDelay = System.currentTimeMillis();
			}
			break;

		case 21010:
			player.takeAsNote = true;
			break;

		case 21011:
			player.takeAsNote = false;
			break;

		// home teleports
		case 4171:
		case 117048:
		case 75010:
		case 84237:
			TeleportExecutor.teleport(player, new Position(3087, 3500, 0), true);
			break;
		case 50056:
			TeleportExecutor.teleport(player, new Position(3087, 3500, 0), true);
			break;

		/* TELEPORTS */
		case 6005:
		case 51023:
		case 117210:
			TeleportExecutor.teleport(player, new Position(2672, 3712, 0), true);
			break;
		case 51031:
			TeleportExecutor.teleport(player, new Teleport(new Position(3287, 3887, 0), TeleportType.ANCIENT));
			break;
		/*
		 * case 50235: case 4140: case 117112:
		 * c.getDH().sendOption5("Edgeville", "Chaos Altar @red@(Multi)",
		 * "West Dragons", "East Dragons", "Next Page");
		 */
		case 50235:
		case 4140:
		case 117112:
			// c.getDH().sendOption5("Ardougne Lever", "44 Portals", "West
			// Drags", "Hill Giants", "Giant Moles @red@(50+ Wild)");
			player.getDH().sendOption5("Edgeville", "Ardougne Lever", "East Dragons @red@(Wild)",
					"44 Portals @red@(Wild)", "@blu@Next Page");
			player.teleAction = 1;
			break;

		case 4143:
		case 50245:
		case 117123:
			// c.getDH().sendOption4("Pest Control", "Duel Arena", "Fight
			// Caves", "@blu@Next");
			player.getDH().sendOption5("Brimhaven Dungeon", "Edgeville Dungeon", "Mithril Dragons", "Relekka Dungeon",
					"@blu@Next Page");
			player.teleAction = 2;
			// c.teleAction = 200;
			break;

		case 50253:
		case 117131:
		case 4146:
			player.getDH().sendOption4("KBD Entrance @red@(Wild)", "Callisto @red@(Wild)", "Godwars"/*, "Barrelchest"*/,
					"@blu@Next Page");
			player.teleAction = 3;
			break;

		case 51005:
		case 117154:
		case 4150:
			// TeleportExecutor.teleport(c, new Position(3027, 3379, 0));
			player.getDH().sendOption4("Barrows", "Duel Arena", "Fight Caves"/*, "Fishing tourney"*/, "@blu@Next Page");
			player.teleAction = 200;
			break;
		case 117186:
		case 6004:
		case 51013:
//			player.getDH().sendOption5("Blast Mining", "Hunter", "Woodcutting Guild", "Resource Area @red@(Wild)",
//					"Zeah Skilling Area");
//			player.teleAction = 205;
			// TeleportExecutor.teleport(c, new Position(1803, 3788, 0));
			break;

		/*
		 * case 51013: case 6004: case 118242:
		 * c.getDH().sendOption5("Lumbridge", "Varrock", "Camelot", "Falador",
		 * "Canifis"); c.teleAction = 20; break;
		 */

		case 9125: // Accurate
		case 6221: // range accurate
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
		case 22228: // unarmed

			player.fightMode = 0;
			if (player.autocasting) {
				player.getPA().resetAutocast();
			}
			break;

		case 9126: // Defensive
		case 48008: // deflect (whip)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 33019: // fend (hally)
		case 18078: // block (spear)
		case 8235: // block (dagger)
			// case 22231: //unarmed
		case 22229: // unarmed

			player.fightMode = 1;
			if (player.autocasting) {
				player.getPA().resetAutocast();
			}
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
		case 33018: // jab (hally)
		case 6234: // longrange (long bow)
		case 6219: // longrange
		case 18077: // lunge (spear)
		case 18080: // swipe (spear)
		case 18079: // pound (spear)
		case 17100: // longrange (darts)
			player.fightMode = 3;
			if (player.autocasting) {
				player.getPA().resetAutocast();
			}
			break;

		case 9128: // Aggressive
		case 6220: // range rapid
		case 21203: // impale (pickaxe)
		case 21202: // smash (pickaxe)
		case 1079: // pound (staff)
		case 6171: // hack (axe)
		case 6170: // smash (axe)
		case 33020: // swipe (hally)
		case 6235: // rapid (long bow)
		case 17101: // repid (darts)
		case 8237: // lunge (dagger)
		case 8236: // slash (dagger)
		case 22230: // kick
			player.fightMode = 2;
			if (player.autocasting) {
				player.getPA().resetAutocast();
			}
			break;

			/**
			 * Prayers *
			 */
			case 21233: // thick skin
				player.getCombat().activatePrayer(player, 0);
				break;
			case 21234: // burst of str
				player.getCombat().activatePrayer(player, 1);
				break;
			case 21235: // charity of thought
				player.getCombat().activatePrayer(player, 2);
				break;
			case 70080: // range
				player.getCombat().activatePrayer(player, 3);
				break;
			case 70082: // mage
				player.getCombat().activatePrayer(player, 4);
				break;
			case 21236: // rockskin
				player.getCombat().activatePrayer(player, 5);
				break;
			case 21237: // super human
				player.getCombat().activatePrayer(player, 6);
				break;
			case 21238: // improved reflexes
				player.getCombat().activatePrayer(player, 7);
				break;
			case 21239: // hawk eye
				player.getCombat().activatePrayer(player, 8);
				break;
			case 21240:
				player.getCombat().activatePrayer(player, 9);
				break;
			case 21241: // protect Item
				/*if (player.inHighRisk()) {
					player.sendMessage("@red@You can't use protect item prayer in a high risk zone!");
					CombatPrayer.resetPrayers(player);
				}
			
			
				if (player.inSafeZone()) {
					player.sendMessage("@red@You can't use protect item prayer in a high risk zone!");
					CombatPrayer.resetPrayers(player);
				}*/
			
//					if (player.nearHighRiskPortal() || player.inSafeZone() || player.inHighRisk() ) {
//						CombatPrayer.resetPrayers(player);
//						player.sendMessage("@red@You can't use protect item prayer in a high risk zone!");
//					return;
//				}

				player.getCombat().activatePrayer(player, 10);
				break;
			case 70084: // 26 range
				player.getCombat().activatePrayer(player, 11);
				break;
			case 70086: // 27 mage
				player.getCombat().activatePrayer(player, 12);
				break;
			case 21242: // steel skin
				player.getCombat().activatePrayer(player, 13);
				break;
			case 21243: // ultimate str
				player.getCombat().activatePrayer(player, 14);
				break;
			case 21244: // incredible reflex
				player.getCombat().activatePrayer(player, 15);
				break;
			case 21245: // protect from magic
				player.getCombat().activatePrayer(player, 16);
				break;
			case 21246: // protect from range
				player.getCombat().activatePrayer(player, 17);
				break;
			case 21247: // protect from melee
				player.getCombat().activatePrayer(player, 18);
				break;
			case 70088: // 44 range
				player.getCombat().activatePrayer(player, 19);
				break;
			case 70090: // 45 mystic
				player.getCombat().activatePrayer(player, 20);
				break;
			case 2171: // retrui
				player.getCombat().activatePrayer(player, 21);
				break;
			case 2172: // redem
				player.getCombat().activatePrayer(player, 22);
				break;
			case 2173: // smite
				player.getCombat().activatePrayer(player, 23);
				break;
			case 70092: // preserve
				if (player.preservePrayerScroll == false) {
					CombatPrayer.resetPrayers(player);
					player.getDH().sendStatement("You must read the Prayer Scroll to use this prayer.");
					return;
				}
				player.getCombat().activatePrayer(player, 24);
				break;
			case 70094: // chivalry
				if (player.playerLevel[1] >= 65) {
					player.getCombat().activatePrayer(player, 25);
				} else {
					player.sendMessage("You must have a defence level of 65 to use this prayer.");
					player.getPA().sendFrame36(player.PRAYER_GLOW[25], 0);
				}
				break;
			case 70200: // piety
				if (player.playerLevel[1] >= 70) {
					player.getCombat().activatePrayer(player, 26);
				} else {
					player.sendMessage("You must have a defence level of 70 to use this prayer.");
					player.getPA().sendFrame36(player.PRAYER_GLOW[26], 0);
				}
				break;
			case 70202: // rigour
				if (player.rigourPrayerScroll == false) {
					CombatPrayer.resetPrayers(player);
					player.getDH().sendStatement("You must read the Prayer Scroll to use this prayer.");
					return;
				}
				if (player.playerLevel[1] >= 70) {
					player.getCombat().activatePrayer(player, 27);
				} else {
					player.sendMessage("You must have a defence level of 70 to use this prayer.");
					player.getPA().sendFrame36(player.PRAYER_GLOW[27], 0);
				}
				break;
			case 70204: // augury
				if (player.auguryPrayerScroll == false) {
					CombatPrayer.resetPrayers(player);
					player.getDH().sendStatement("You must read the Prayer Scroll to use this prayer.");
					return;
				}
				if (player.playerLevel[1] >= 70) {
					player.getCombat().activatePrayer(player, 28);
				} else {
					player.sendMessage("You must have a defence level of 70 to use this prayer.");
					player.getPA().sendFrame36(player.PRAYER_GLOW[28], 0);
				}
				break;

		case 13092:
			if (!Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
				player.sendMessage("You are not trading!");
				return;
			}
			if (System.currentTimeMillis() - player.getTrade().getLastAccept() < 1000) {
				return;
			}
			player.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.TRADE)
					.accept(player, MultiplayerSessionStage.OFFER_ITEMS);
			break;

		case 13218:
			if (!Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
				player.sendMessage("You are not trading!");
				return;
			}
			if (System.currentTimeMillis() - player.getTrade().getLastAccept() < 1000) {
				return;
			}
			player.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener().getMultiplayerSession(player, MultiplayerSessionType.TRADE)
					.accept(player, MultiplayerSessionStage.CONFIRM_DECISION);
			break;

		case 125011: // Click agree
			if (!player.ruleAgreeButton) {
				player.ruleAgreeButton = true;
				player.getPA().sendFrame36(701, 1);
			} else {
				player.ruleAgreeButton = false;
				player.getPA().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (player.ruleAgreeButton) {
				player.getPA().showInterface(3559);
				player.newPlayer = false;
			} else if (!player.ruleAgreeButton) {
				player.sendMessage("You need to agree before you can carry on.");
			}
			break;
		case 125006:// Decline
			player.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */
		case 74176:
			if (!player.mouseButton) {
				player.mouseButton = true;
				player.getPA().sendFrame36(500, 1);
				player.getPA().sendFrame36(170, 1);
			} else if (player.mouseButton) {
				player.mouseButton = false;
				player.getPA().sendFrame36(500, 0);
				player.getPA().sendFrame36(170, 0);
			}
			break;
		case 74184:
		case 66154:
			if (!player.splitChat) {
				player.splitChat = true;
				player.getPA().sendFrame36(502, 1);
				player.getPA().sendFrame36(287, 1);
			} else {
				player.splitChat = false;
				player.getPA().sendFrame36(502, 0);
				player.getPA().sendFrame36(287, 0);
			}
			break;
		case 3189:
			if (!player.splitChat) {
				player.splitChat = true;
				player.getPA().sendFrame36(502, 1);
				player.getPA().sendFrame36(287, 1);
			} else {
				player.splitChat = false;
				player.getPA().sendFrame36(502, 0);
				player.getPA().sendFrame36(287, 0);
			}
			break;
		case 74180:
			if (!player.chatEffects) {
				player.chatEffects = true;
				player.getPA().sendFrame36(501, 1);
				player.getPA().sendFrame36(171, 0);
			} else {
				player.chatEffects = false;
				player.getPA().sendFrame36(501, 0);
				player.getPA().sendFrame36(171, 1);
			}
			break;
		case 74188:
			if (!player.acceptAid) {
				player.acceptAid = true;
				player.getPA().sendFrame36(503, 1);
				player.getPA().sendFrame36(427, 1);
			} else {
				player.acceptAid = false;
				player.getPA().sendFrame36(503, 0);
				player.getPA().sendFrame36(427, 0);
			}
			break;
		case 74192:
			if (!player.isRunning2) {
				player.isRunning2 = true;
				player.getPA().sendFrame36(504, 1);
				player.getPA().sendFrame36(173, 1);
			} else {
				player.isRunning2 = false;
				player.getPA().sendFrame36(504, 0);
				player.getPA().sendFrame36(173, 0);
			}
			break;
		case 74201:// brightness1
			player.getPA().sendFrame36(505, 1);
			player.getPA().sendFrame36(506, 0);
			player.getPA().sendFrame36(507, 0);
			player.getPA().sendFrame36(508, 0);
			player.getPA().sendFrame36(166, 1);
			break;
		case 74203:// brightness2
			player.getPA().sendFrame36(505, 0);
			player.getPA().sendFrame36(506, 1);
			player.getPA().sendFrame36(507, 0);
			player.getPA().sendFrame36(508, 0);
			player.getPA().sendFrame36(166, 2);
			break;

		case 74204:// brightness3
			player.getPA().sendFrame36(505, 0);
			player.getPA().sendFrame36(506, 0);
			player.getPA().sendFrame36(507, 1);
			player.getPA().sendFrame36(508, 0);
			player.getPA().sendFrame36(166, 3);
			break;

		case 74205:// brightness4
			player.getPA().sendFrame36(505, 0);
			player.getPA().sendFrame36(506, 0);
			player.getPA().sendFrame36(507, 0);
			player.getPA().sendFrame36(508, 1);
			player.getPA().sendFrame36(166, 4);
			break;
		case 74206:// area1
			player.getPA().sendFrame36(509, 1);
			player.getPA().sendFrame36(510, 0);
			player.getPA().sendFrame36(511, 0);
			player.getPA().sendFrame36(512, 0);
			break;
		case 74207:// area2
			player.getPA().sendFrame36(509, 0);
			player.getPA().sendFrame36(510, 1);
			player.getPA().sendFrame36(511, 0);
			player.getPA().sendFrame36(512, 0);
			break;
		case 74208:// area3
			player.getPA().sendFrame36(509, 0);
			player.getPA().sendFrame36(510, 0);
			player.getPA().sendFrame36(511, 1);
			player.getPA().sendFrame36(512, 0);
			break;
		case 74209:// area4
			player.getPA().sendFrame36(509, 0);
			player.getPA().sendFrame36(510, 0);
			player.getPA().sendFrame36(511, 0);
			player.getPA().sendFrame36(512, 1);
			break;
		case 168:
			player.animation(855);
			break;
		case 169:
			player.animation(856);
			break;
		case 162:
			player.animation(857);
			break;
		case 164:
			player.animation(858);
			break;
		case 165:
			player.animation(859);
			break;
		case 161:
			player.animation(860);
			break;
		case 170:
			player.animation(861);
			break;
		case 171:
			player.animation(862);
			break;
		case 163:
			player.animation(863);
			break;
		case 167:
			player.animation(864);
			break;
		case 172:
			player.animation(865);
			break;
		case 166:
			player.animation(866);
			break;
		case 52050:
			player.animation(2105);
			break;
		case 52051:
			player.animation(2106);
			break;
		case 52052:
			player.animation(2107);
			break;
		case 52053:
			player.animation(2108);
			break;
		case 52054:
			player.animation(2109);
			break;
		case 52055:
			player.animation(2110);
			break;
		case 52056:
			player.animation(2111);
			break;
		case 52057:
			player.animation(2112);
			break;
		case 52058:
			player.animation(2113);
			break;
		case 43092:
			player.animation(0x558);
			break;
		case 2155:
			player.animation(0x46B);
			break;
		case 25103:
			player.animation(0x46A);
			break;
		case 25106:
			player.animation(0x469);
			break;
		case 2154:
			player.animation(0x468);
			break;
		case 52071:
			player.animation(0x84F);
			break;
		case 52072:
			player.animation(0x850);
			break;
		case 59062:
			player.animation(2836);
			break;
		case 72032:
			player.animation(3544);
			break;
		case 72033:
			player.animation(3543);
			break;
		/*
		 * case 72254: //c.startAnimation(3866); break; /* END OF EMOTES
		 */

		case 24017:
			player.getPA().resetAutocast();
			// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
			player.getItems().sendWeapon(player.playerEquipment[Player.playerWeapon],
					player.getItems().getItemName(player.playerEquipment[Player.playerWeapon]));
			// c.setSidebarInterface(0, 328);
			// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 1151 :
			// c.playerMagicBook == 1 ? 12855 : 1151);
			break;

		case 104078:
			player.setSidebarInterface(3, 3213);
			break;

		}

		if (player.isAutoButton(buttonId)) {
			player.assignAutocast(buttonId);
		}
	}

	public static void resetCurrent(Player player) {
		int[] skillIds = { 0, 1, 2, 3, 4, 5, 6

		};
		for (int i : skillIds) {
			int skilld = 1;
			int leveld = 1;
			player.playerXP[i] = 1;
			player.playerLevel[i] = 1;
			player.getPA().refreshSkill(i);
		}
	}

	private static void handleMainLevels(Player player) {
		if (player.playerLevel[0] == 99 && player.playerLevel[5] == 99 && player.playerLevel[1] == 99
				&& player.playerLevel[2] == 99 && player.playerLevel[3] == 99 && player.playerLevel[4] == 99
				&& player.playerLevel[6] == 99 && player.playerLevel[7] == 99 && player.playerLevel[8] == 99
				&& player.playerLevel[9] == 99 && player.playerLevel[10] == 99 && player.playerLevel[11] == 99
				&& player.playerLevel[12] == 99 && player.playerLevel[13] == 99 && player.playerLevel[14] == 99
				&& player.playerLevel[15] == 99 && player.playerLevel[16] == 99 && player.playerLevel[17] == 99
				&& player.playerLevel[18] == 99 && player.playerLevel[19] == 99 && player.playerLevel[20] == 99
				&& player.playerLevel[21] == 99 && player.playerLevel[22] == 99 && player.playerLevel[23] == 99) {
			return;
		}
		int[] skillIds = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23

		};
		for (int i : skillIds) {
			player.playerLevel[i] = 99;
			player.getPA().addSkillXP10(13034431, i);
			player.getPA().refreshSkill(i);
		}
	}

	private static void handleTankLevels(Player player) {
		if (player.playerLevel[0] == 75 && player.playerLevel[1] == 75 && player.playerLevel[5] == 70) {
			return;
		}
		int[] skillIds = { 2, 3, 4, 6

		};
		for (int i : skillIds) {
			player.playerLevel[i] = 99;
			player.getPA().addSkillXP10(130341308, i);
			player.getPA().refreshSkill(i);
		}
		player.playerLevel[0] = 75;
		player.getPA().addSkillXP10(1210421, 0);
		player.playerLevel[5] = 70;
		player.getPA().addSkillXP10(668051, 5);
		player.playerLevel[1] = 75;
		player.getPA().addSkillXP10(1210421, 1);
		player.getPA().refreshSkill(1);
		player.getPA().refreshSkill(4);
		player.getPA().refreshSkill(5);
	}

	private static void handlePureLevels(Player player) {
		if (player.playerLevel[0] == 75 && player.playerLevel[5] == 52) {
			return;
		}
		int[] skillIds = { 2, 3, 4, 6

		};
		for (int i : skillIds) {
			player.playerLevel[i] = 99;
			player.getPA().addSkillXP10(130341038, i);
			player.getPA().refreshSkill(i);
		}
		player.playerLevel[0] = 75;
		player.getPA().addSkillXP10(1210421, 0);
		player.playerLevel[5] = 52;
		player.getPA().addSkillXP10(123660, 5);
		player.playerLevel[1] = 1;
		player.getPA().addSkillXP10(1, 1);
		player.getPA().refreshSkill(1);
		player.getPA().refreshSkill(4);
		player.getPA().refreshSkill(5);
	}

	private static void handleZerkerLevels(Player player) {
		if (player.playerLevel[0] == 90 && player.playerLevel[1] == 70 && player.playerLevel[5] == 52) {
			return;
		}
		int[] skillIds = { 2, 3, 4, 6

		};
		for (int i : skillIds) {
			player.playerLevel[i] = 99;
			player.getPA().addSkillXP10(130304138, i);
			player.getPA().refreshSkill(i);
		}
		player.playerLevel[0] = 90;
		player.getPA().addSkillXP10(5346332, 0);
		player.playerLevel[5] = 52;
		player.getPA().addSkillXP10(123660, 5);
		player.playerLevel[1] = 70;
		player.getPA().addSkillXP10(737627, 1);
		player.getPA().refreshSkill(1);
		player.getPA().refreshSkill(4);
		player.getPA().refreshSkill(5);
	}
		
	}
//}
