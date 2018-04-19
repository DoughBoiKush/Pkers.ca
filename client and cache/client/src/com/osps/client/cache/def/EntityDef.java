package com.osps.client.cache.def;
import java.io.PrintWriter;

import com.osps.client.Client;
import com.osps.client.Signlink;
import com.osps.client.Stream;
import com.osps.client.VarBit;
import com.osps.client.cache.StreamLoader;
import com.osps.client.cache.anim.Class36;
import com.osps.client.entity.model.Model;
import com.osps.client.link.MRUNodes;

public final class EntityDef {

	public static EntityDef forID(int i) {
		for (int j = 0; j < 20; j++)
			if (i != 4178 && cache[j].interfaceType == i)
				return cache[j];

		anInt56 = (anInt56 + 1) % 20;
		EntityDef entityDef = cache[anInt56] = new EntityDef();
		stream.currentOffset = streamIndices[i];
		entityDef.interfaceType = i;
		entityDef.readValues(stream);
		if (i == 7095) {
			EntityDef demonic = forID(7151);
			entityDef.name = "Bearded Gorilla";
			entityDef.description = "Yeah, we know it's a tortured gorilla, this is a feature not a bug.".getBytes();
			entityDef.turn180AnimIndex = demonic.turn180AnimIndex;
			entityDef.turn90CCWAnimIndex = demonic.turn90CCWAnimIndex;
			entityDef.turn90CWAnimIndex = demonic.turn90CWAnimIndex;
		}
		if (i == 2055) { //chaos elemental jr
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Pick-up";
			entityDef.actions[3] = "@or2@Tele Home";
		}
		if (i == 6633) { //zilyana jr
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Pick-up";
			entityDef.actions[3] = "@or2@Restore Prayer";
		}
		if (i == 6636) { //prince black dragon
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Pick-up";
			entityDef.actions[3] = "@or2@Take BoB";
		}
		if (i == 5892) { //TzRek-Jad
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Pick-up";
			entityDef.actions[3] = "@or2@Take BoB";
		}
		if(i == 1571) { // NPC ID 
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade @or2@Pure Armor";
			entityDef.actions[3] = "Trade @or1@Food & Potions";
			entityDef.actions[4] = "Trade @or1@Amulet & Rings";
			entityDef.name = "Other Shops"; // NPC name
			entityDef.description = "Love his gold!".getBytes(); // NPC description
	}
		if(i == 1025) { // NPC ID 
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade @or2@F2P Items";
			entityDef.actions[3] = "Trade @or2@Costumes";
			entityDef.name = "Other Shops"; // NPC name
			entityDef.description = "Love his gold!".getBytes(); // NPC description
	}
		if(i == 4933) { // NPC ID 
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade @or1@Accessories";
			entityDef.actions[3] = "Trade @or1@Armor";
			entityDef.name = "Magic Shop"; // NPC name
			entityDef.description = "Slightly magical.".getBytes(); // NPC description
	}
		if(i == 2150) { // NPC ID 
			entityDef.actions = new String[6];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Search for shops";
			entityDef.actions[3] = "Open my Shop";
			entityDef.actions[4] = "Add items to my Shop";
			entityDef.name = "Pos Handler"; // NPC name
			entityDef.description = "Player Owned shop Handler.".getBytes(); // NPC description
	}
		if(i == 3479) { // NPC ID 
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Titles";
			entityDef.name = "Donator Guy"; // NPC name
			entityDef.description = "He looks really rich.".getBytes(); // NPC description
	}
		if(i == 4064) { // NPC ID 
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Trade";
			entityDef.actions[2] = "Titles";
			entityDef.name = "Voting Shop"; // NPC name
			entityDef.description = "Receive your voting rewards here.".getBytes(); // NPC description
	}
		if (i == 5721) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade-with";
			entityDef.name = "Sacrifice Shop"; // NPC name
			entityDef.description = "He looks really cool!".getBytes(); // NPC
																							// description
		}
		if (i == 402) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-To";
			entityDef.actions[2] = "Trade @or1@Pk Point Shop";
			entityDef.actions[3] = "Trade @or1@Pk Point Shop 2";
			entityDef.actions[4] = "Trade @or1@Pk Point Shop 3";
			entityDef.name = "Mazchna"; // NPC name
			entityDef.description = "He looks dangerous.".getBytes(); // NPC
																							// description
		}
		if (i == 403) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-To";
			entityDef.actions[2] = "Trade @or2@Weapons";
			entityDef.actions[3] = "Trade @or2@Barrows & More";
			entityDef.actions[4] = "Trade @or1@Main Armor";
			entityDef.name = "Melee Shop"; // NPC name
			entityDef.description = "He looks dangerous.".getBytes(); // NPC
																							// description
		}
		if (i == 5792) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-To";
			entityDef.actions[2] = "Quick-add";
			entityDef.actions[3] = "Lottery Status";
			entityDef.name = "Lottery Master"; // NPC name
			//entityDef.description = "From the Wizarding world of...Oh wait he just sells Magic stuff now".getBytes(); // NPC
																														// description
		}
		if (i == 3667) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Melee Shop"; // NPC name
			entityDef.description = "She will sell you melee related items.".getBytes(); // NPC
																							// description
		}
		if (i == 401) { // NPC ID
			entityDef.name = "PVP Slayer Master"; // NPC name
			entityDef.description = "Will assign pvp slayer tasks.".getBytes(); // NPC
																							// description
		}
		if (i == 2995) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade @or1@Weapons";
			entityDef.actions[3] = "Trade @or1@Armor";
			entityDef.name = "Range Store"; // NPC name
			entityDef.description = "A Ranger from the Pkers.Ca Lands, selling his Ranging goods.".getBytes(); // NPC
																								// description
		}
		if (i == 527) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Armour";
			entityDef.actions[2] = "Weapons";
			entityDef.actions[3] = "Supplies";
			entityDef.name = "PK Store"; // NPC name
			//entityDef.description = "From the Wizarding world of...Oh wait he just sells Magic stuff now".getBytes(); // NPC
																														// description
		}
		if (i == 637) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Magic";
			entityDef.actions[2] = "Range";
			entityDef.name = "PK Store"; // NPC name
			//entityDef.description = "From the Wizarding world of...Oh wait he just sells Magic stuff now".getBytes(); // NPC
																														// description
		}
		if (i == 4397) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Melee";
			entityDef.actions[2] = "Range";
			entityDef.actions[3] = "Magic";
			entityDef.name = "PK Point Store"; // NPC name
			entityDef.description = "From the Wizarding world of...Oh wait he just sells Magic stuff now".getBytes(); // NPC
																														// description
		}
		if (i == 944) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Pkers.Ca Vote Shop"; // NPC name
			entityDef.description = "You can redeem your votes here".getBytes(); // NPC
																					// description
		}
		if (i == 3984) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Doctor Blood Money"; // NPC name
			entityDef.actions[2] = "Heal";
			entityDef.description = "The Doctor is here to cure, but for the price of BLOOD".getBytes(); // NPC
																											// description
		}
		if (i == 3343) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Nurse Charitable"; // NPC name
			entityDef.actions[2] = "Heal";
			entityDef.description = "The Nurse is very kind, but only to those as kind".getBytes(); // NPC
																									// description
		}
		if (i == 6080) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Agility Gnome"; // NPC name
			entityDef.description = "He will train you the ways of Agility".getBytes(); // NPC
																						// description
		}
		if (i == 506) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Pkers.Ca Store"; // NPC name
			entityDef.description = "He sells the goods from the land of Pkers.Ca".getBytes(); // NPC
																								// description
		}
		if (i == 5809) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Crafting Shop"; // NPC name
			entityDef.actions[2] = "Trade";
			entityDef.description = "He can Tan and he can Craft!".getBytes(); // NPC
																				// description
		}
		if (i == 4733) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "The Forgotten Smither"; // NPC name
			entityDef.description = "He has made legendary weapons through the art of smithing".getBytes(); // NPC
																											// description
		}
		if (i == 2580) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "The Runecrafter"; // NPC name
			entityDef.description = "He has seen the abyss and found out you can runecr4raft there!".getBytes(); // NPC
																													// description
		}
		if (i == 3894) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Stolen Goods Buyer"; // NPC name
			entityDef.description = "He buy's whatever you steal".getBytes(); // NPC
																				// description
		}
		if (i == 3257) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Master Farmer"; // NPC name
			entityDef.description = "He has everything you need for farming!".getBytes(); // NPC
																							// description
		}
		if (i == 3113) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Pet Insurance"; // NPC name
			entityDef.description = "He loves and cares for your pets, once insured relog to get them back!".getBytes(); // NPC
																															// description
		}
		if (i == 1847) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "The Cape Keeper"; // NPC name
			entityDef.description = "Skill capes max capes she has them all!".getBytes(); // NPC
																							// description
		}
		if (i == 5518) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Claim Donation";
			entityDef.name = "Pkers.Ca Child"; // NPC name
			entityDef.description = "Claim your donations here!".getBytes(); // NPC
																				// description
		}
		if (i == 5523) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Shop 1";
			entityDef.actions[2] = "Shop 2";
			entityDef.name = "Donator store"; // NPC name
			entityDef.description = "He will sell items for donator points!".getBytes(); // NPC
																							// description
		}
		// new npcs for new shops
		if (i == 2400) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Pure Store"; // NPC name
			entityDef.description = "This Fella will sell you the pure goodies,".getBytes(); // NPC
																								// description
		}
		if (i == 2575) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "PKP Sell & Exchange"; // NPC name
			entityDef.description = "He'll buy and sell various items for pkp.".getBytes(); // NPC
																								// description
		}
		if (i == 1944) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Armor Store"; // NPC name
			entityDef.description = "Armor from the depths of Gelinor.".getBytes(); // NPC
																					// description
		}
		if (i == 3415) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Range Store"; // NPC name
			entityDef.description = "A Ranger from the Pkers.Ca Lands, selling his Ranging goods.".getBytes(); // NPC
																												// description
		}
		if (i == 5118) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Magic Store"; // NPC name
			entityDef.description = "A Magic Man with Magical Goods.".getBytes(); // NPC
																					// description
		}
		if (i == 4850) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Food and Potion Shop"; // NPC name
			entityDef.description = "How do you fit a Shark in your mouth you may ask? Only this guy knows.".getBytes(); // NPC
																															// description
		}
		if (i == 4850) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Food and Potion Shop"; // NPC name
			entityDef.description = "How do you fit a Shark in your mouth you may ask? Only this guy knows.".getBytes(); // NPC
																															// description
		}
		if (i == 5037) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Fashionscape"; // NPC name
			entityDef.description = "Romeo gets all the Juliets with High Fashion.".getBytes(); // NPC
																								// description
		}
		if (i == 3833) { // NPC ID
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.name = "Pkers.Ca Angel"; // NPC name
			entityDef.description = "Buys all the items that you obtained from skilling.".getBytes(); // NPC
																										// description
		}
		if (i == 6074) {
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade";
			entityDef.name = "The Convertor";
			entityDef.description = "Ever since the blood money inflated, people started using coins again and a new currency called platinum tokens.".getBytes();
		}
		if (i == 3257) {
			entityDef.actions = new String[] { "Pickpocket", null, "Trade", null, null };
		}
		if (i == 2580) {
			entityDef.actions = new String[] { "Talk-to", null, "Teleport to Abyss", null, null };
		}
		if (i == 5870) {
			entityDef.name = "Key Master"; // NPC name
			entityDef.npcModels = new int[] { 29241, 29246, 29244, 29248, 29242, 29247, 29243, 29245, 29251, 29257 };
		}
		if (i == 4178) {// Lanthus
			entityDef = forID(1213);
			entityDef.interfaceType = i;
			int[] temp = entityDef.npcModels;
			entityDef.npcModels = new int[10];
			System.arraycopy(temp, 0, entityDef.npcModels, 0, temp.length);
			entityDef.npcModels[6] = 564;
			entityDef.npcModels[8] = 15413;
			entityDef.npcModels[9] = 280;
			entityDef.npcModels[9] = 286;
			entityDef.additionalModels = new int[1];
			entityDef.additionalModels[0] = 149;
			entityDef.name = "Lanthus The Offerer";
			entityDef.combatLevel = 645;
			entityDef.description = "My god what is that thing!".getBytes(); // NPC
																				// description
			entityDef.originalModelColors = new int[6];
			entityDef.changedModelColors = new int[6];
			entityDef.originalModelColors[0] = 5231;
			entityDef.changedModelColors[0] = 7114;
			entityDef.originalModelColors[1] = 61;
			entityDef.changedModelColors[1] = 86933;
			entityDef.originalModelColors[2] = 41;
			entityDef.changedModelColors[2] = 19860;
			entityDef.originalModelColors[3] = 922;
			entityDef.changedModelColors[3] = 6073;
			entityDef.originalModelColors[4] = 74;
			entityDef.changedModelColors[4] = 6073;
			entityDef.originalModelColors[5] = 11200;
			entityDef.changedModelColors[5] = 926;
			entityDef.npcSize = 280;
			entityDef.npcHeight = 220;
			entityDef.spaceOccupied = 4;
		}
		if (i == 4851) {
			entityDef.name = "Goblin Butcher";
			entityDef.description = "He has gone savage!".getBytes(); // NPC
																		// description
			entityDef.npcSize = 260;
			entityDef.npcHeight = 140;
			entityDef.spaceOccupied = 3;
			entityDef.combatLevel = 217;
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.originalModelColors = new int[3];
			entityDef.changedModelColors = new int[3];
			entityDef.originalModelColors[0] = 28;
			entityDef.changedModelColors[0] = 926;
			entityDef.originalModelColors[1] = 20;
			entityDef.changedModelColors[1] = 926;
			entityDef.originalModelColors[2] = 8;
			entityDef.changedModelColors[2] = 926;
		}
		if (i == 1749) {// PURPLE PORTAL.
			entityDef.npcModels = new int[2];
			entityDef.npcModels[0] = 14533;
			entityDef.npcModels[1] = 14526;
			// entityDef.anIntArray94[2] = 14522;
			entityDef.name = "Portal";
			entityDef.changedModelColors = new int[] { -11149, -11033, -10792, -10317, -10333 };
			entityDef.originalModelColors = new int[] { -22293, -22184, -22320, -22330, -22343 };
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.standAnim = 3933;
			entityDef.walkAnim = 3933;
			entityDef.combatLevel = 0;
			entityDef.npcHeight = 128;
			entityDef.npcSize = 128;
		}
		if (i == 1747) {// PINK PORTAL
			entityDef.npcModels = new int[3];
			entityDef.npcModels[0] = 14533;
			entityDef.npcModels[1] = 14527;
			entityDef.npcModels[2] = 14528;
			entityDef.name = "Portal";
			entityDef.changedModelColors = new int[] { -11149, -11033, -10792, -10317, -10333 };
			entityDef.originalModelColors = new int[] { 10355, 10471, 10941, 11200, 11787 };
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.standAnim = 3933;
			entityDef.walkAnim = 3933;
			entityDef.combatLevel = 0;
			entityDef.npcHeight = 128;
			entityDef.npcSize = 128;
		}
		if (i == 1750) {// YELLOW PORTAL
			entityDef.npcModels = new int[2];
			entityDef.npcModels[0] = 14533;
			entityDef.npcModels[1] = 14522;
			entityDef.name = "Portal";
			entityDef.changedModelColors = new int[] { -11149, -11033, -10792, -10317, -10333 };
			entityDef.originalModelColors = new int[] { 10355, 10471, 10941, 11200, 11787 };
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.standAnim = 3933;
			entityDef.walkAnim = 3933;
			entityDef.combatLevel = 0;
			entityDef.npcHeight = 128;
			entityDef.npcSize = 128;
		}
		if (i == 7153) {
			entityDef.name = "Demonic gorilla";
			entityDef.description = "A demonic gorilla".getBytes();
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.combatLevel = 275;
			entityDef.walkAnim = 7233;
			entityDef.standAnim = 7230;
			entityDef.turn180AnimIndex = 7233;
			entityDef.turn90CWAnimIndex = 7233;
			entityDef.turn90CCWAnimIndex = 7233;
			entityDef.npcModels = new int[1];
			entityDef.npcModels[0] = 31241;
			entityDef.originalModelColors = new int[1];
			entityDef.originalModelColors[0] = 10266;
			entityDef.changedModelColors = new int[1];
			entityDef.changedModelColors[0] = 28;
			entityDef.npcSize = 64;
			entityDef.npcHeight = 64;
			entityDef.spaceOccupied = 2;
			entityDef.headIcon = 0;
		}
		if (i == 7152) {
			entityDef.name = "Demonic gorilla";
			entityDef.description = "A demonic gorilla".getBytes();
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.combatLevel = 275;
			entityDef.walkAnim = 7233;
			entityDef.standAnim = 7230;
			entityDef.turn180AnimIndex = 7233;
			entityDef.turn90CWAnimIndex = 7233;
			entityDef.turn90CCWAnimIndex = 7233;
			entityDef.npcModels = new int[1];
			entityDef.npcModels[0] = 31241;
			entityDef.originalModelColors = new int[1];
			entityDef.originalModelColors[0] = 10266;
			entityDef.changedModelColors = new int[1];
			entityDef.changedModelColors[0] = 28;
			entityDef.npcSize = 64;
			entityDef.npcHeight = 64;
			entityDef.spaceOccupied = 2; // dunno
			entityDef.headIcon = 2;
		}
		if (i == 7151) {
			entityDef.name = "Demonic gorilla";
			entityDef.description = "A demonic gorilla".getBytes();
			entityDef.combatLevel = 275;
			entityDef.walkAnim = 7233;
			entityDef.standAnim = 7230;
			entityDef.turn180AnimIndex = 7233;
			entityDef.turn90CWAnimIndex = 7233;
			entityDef.turn90CCWAnimIndex = 7233;
			entityDef.npcModels = new int[1];
			entityDef.npcModels[0] = 31241;
			entityDef.originalModelColors = new int[1];
			entityDef.originalModelColors[0] = 10266;
			entityDef.changedModelColors = new int[1];
			entityDef.changedModelColors[0] = 28;
			entityDef.npcSize = 64;
			entityDef.npcHeight = 64;
			entityDef.spaceOccupied = 2;
			entityDef.headIcon = 1;
		}
		if (i == 1746) {// RED PORTAL?
			entityDef.npcModels = new int[4];
			entityDef.npcModels[0] = 14533;
			entityDef.npcModels[1] = 14523;
			entityDef.npcModels[2] = 14524;
			entityDef.npcModels[3] = 14525;
			entityDef.name = "Portal";
			entityDef.changedModelColors = new int[] { -11149, -11033, -10792, -10317, -10333 };
			entityDef.originalModelColors = new int[] { -2125, -2249, -2373, -2490, -2490 };
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.standAnim = 3933;
			entityDef.walkAnim = 3933;
			entityDef.combatLevel = 0;
			entityDef.npcHeight = 128;
			entityDef.npcSize = 128;
		}

		if (i == 493) {// RED PORTAL?
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.combatLevel = 0;
		}

		if (i == 4435) {
			EntityDef jad = forID(3127);
			entityDef.name = "TzRek-Jad";
			entityDef.npcModels = new int[1];
			entityDef.npcModels[0] = 9319;
			// entityDef.description = "This is not going to hurt... but it
			// might tickle.";
			entityDef.standAnim = 2650;
			entityDef.walkAnim = 2651;
			entityDef.turn180AnimIndex = jad.turn180AnimIndex;
			entityDef.turn90CWAnimIndex = jad.turn90CWAnimIndex;
			entityDef.turn90CCWAnimIndex = jad.turn90CCWAnimIndex;
			entityDef.additionalModels = new int[] { 29320 };
			entityDef.actions = new String[] { "Talk-to", null, "Pick-up", null, null };
			entityDef.npcSize = 20;
			entityDef.npcHeight = 20;
		}

		if (i == 5057) {
			entityDef.npcSize = 130;
			entityDef.npcHeight = 130;
			entityDef.spaceOccupied = 3;
			entityDef.drawMinimapDot = false;
			entityDef.clickable = false;
		}

		if (i == 6611) {
			EntityDef skeleton = forID(83);
			entityDef.standAnim = skeleton.standAnim;
			entityDef.walkAnim = skeleton.walkAnim;
		}
		if (i == 4771) {
			entityDef.name = "Sir Rington";
			entityDef.actions[2] = "Imbue Rings";
		}
		switch (i) {
		case 5915:
			entityDef.name = "Respiratory system";
			entityDef.actions = new String[5];
			entityDef.actions[1] = "Attack";
			entityDef.standAnim = 7105;
			break; // try
		}
		return entityDef;
	}

	public Model method160() {
		if (childrenIDs != null) {
			EntityDef entityDef = method161();
			if (entityDef == null)
				return null;
			else
				return entityDef.method160();
		}
		if (additionalModels == null)
			return null;
		boolean flag1 = false;
		for (int i = 0; i < additionalModels.length; i++)
			if (!Model.method463(additionalModels[i]))
				flag1 = true;

		if (flag1)
			return null;
		Model aclass30_sub2_sub4_sub6s[] = new Model[additionalModels.length];
		for (int j = 0; j < additionalModels.length; j++)
			aclass30_sub2_sub4_sub6s[j] = Model.method462(additionalModels[j]);

		Model model;
		if (aclass30_sub2_sub4_sub6s.length == 1)
			model = aclass30_sub2_sub4_sub6s[0];
		else
			model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
		if (originalModelColors != null) {
			for (int k = 0; k < originalModelColors.length; k++)
				//model.method476(originalModelColors[k], changedModelColors[k]);
			model.method476(originalModelColors[k], changedModelColors[k],
					ItemDef.getTexture(-1));

		}
		return model;
	}

	public EntityDef method161() {
		int j = -1;
		if (varBitID != -1) {
			VarBit varBit = VarBit.cache[varBitID];
			int k = varBit.anInt648;
			int l = varBit.anInt649;
			int i1 = varBit.anInt650;
			int j1 = Client.anIntArray1232[i1 - l];
			j = clientInstance.variousSettings[k] >> l & j1;
		} else if (settingId != -1)
			j = clientInstance.variousSettings[settingId];
		if (j < 0 || j >= childrenIDs.length || childrenIDs[j] == -1)
			return null;
		else
			return forID(childrenIDs[j]);
	}

	public static void dumpList() {
		try {
			PrintWriter pw = new PrintWriter(Signlink.findcachedir() + "npcList.txt");
			for (int i = 0; i < streamIndices.length; i++) {
				EntityDef item = forID(i);
				if (item != null)
					if (item.name != null)
						pw.println(i + " - " + item.name);
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void unpackConfig(StreamLoader streamLoader) {
		stream = new Stream(streamLoader.getDataForName("npc.dat"));
		Stream stream2 = new Stream(streamLoader.getDataForName("npc.idx"));
		// stream = new Stream(FileOperations.ReadFile(Signlink.findcachedir()+
		// "npc.dat"));
		// Stream stream2 = new
		// Stream(FileOperations.ReadFile(Signlink.findcachedir()+ "npc.idx"));
		int totalNPCs = stream2.readUnsignedWord();
		streamIndices = new int[totalNPCs];
		int i = 2;
		for (int j = 0; j < totalNPCs; j++) {
			streamIndices[j] = i;
			i += stream2.readUnsignedWord();
		}

		cache = new EntityDef[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new EntityDef();
		for (int index = 0; index < totalNPCs; index++) {
			EntityDef ed = forID(index);
			// dumpList();
			if (ed == null)
				continue;
			if (ed.name == null)
				continue;
		}
	}

	public static void nullLoader() {
		mruNodes = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public Model method164(int j, int k, int ai[]) {
		if (childrenIDs != null) {
			EntityDef entityDef = method161();
			if (entityDef == null)
				return null;
			else
				return entityDef.method164(j, k, ai);
		}
		Model model = (Model) mruNodes.insertFromCache(interfaceType);
		if (model == null) {
			boolean flag = false;
			for (int i1 = 0; i1 < npcModels.length; i1++)
				if (!Model.method463(npcModels[i1]))
					flag = true;

			if (flag)
				return null;
			Model aclass30_sub2_sub4_sub6s[] = new Model[npcModels.length];
			for (int j1 = 0; j1 < npcModels.length; j1++)
				aclass30_sub2_sub4_sub6s[j1] = Model.method462(npcModels[j1]);

			if (aclass30_sub2_sub4_sub6s.length == 1)
				model = aclass30_sub2_sub4_sub6s[0];
			else
				model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
			if (originalModelColors != null) {
				for (int k1 = 0; k1 < originalModelColors.length; k1++)
					model.method476(originalModelColors[k1], changedModelColors[k1], ItemDef.getTexture(-1));

			}
			model.method469();
			model.method479(64 + lightModifier, 850 + shadowModifier, -30, -50, -30, true);
			mruNodes.removeFromCache(model, interfaceType);
		}
		Model model_1 = Model.aModel_1621;
		model_1.method464(model, Class36.method532(k) & Class36.method532(j));
		if (k != -1 && j != -1)
			model_1.method471(ai, j, k);
		else if (k != -1)
			model_1.method470(k);
		if (npcSize != 128 || npcHeight != 128)
			model_1.method478(npcSize, npcSize, npcHeight);
		model_1.method466();
		model_1.anIntArrayArray1658 = null;
		model_1.anIntArrayArray1657 = null;
		if (spaceOccupied == 1)
			model_1.aBoolean1659 = true;
		return model_1;
	}

	public void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1) {
				int j = stream.readUnsignedByte();
				npcModels = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					npcModels[j1] = stream.readUnsignedWord();

			} else if (i == 2)
				name = stream.readString();
			else if (i == 3)
				description = stream.readBytes();
			else if (i == 12)
				spaceOccupied = stream.readSignedByte();
			else if (i == 13)
				standAnim = stream.readUnsignedWord();
			else if (i == 14)
				walkAnim = stream.readUnsignedWord();
			else if (i == 17) {
				walkAnim = stream.readUnsignedWord();
				turn180AnimIndex = stream.readUnsignedWord();
				turn90CWAnimIndex = stream.readUnsignedWord();
				turn90CCWAnimIndex = stream.readUnsignedWord();
			} else if (i >= 30 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 30] = stream.readString();
				if (actions[i - 30].equalsIgnoreCase("hidden"))
					actions[i - 30] = null;
			} else if (i == 40) {
				int k = stream.readUnsignedByte();
				originalModelColors = new int[k];
				changedModelColors = new int[k];
				for (int k1 = 0; k1 < k; k1++) {
					originalModelColors[k1] = stream.readUnsignedWord();
					changedModelColors[k1] = stream.readUnsignedWord();
				}

			} else if (i == 60) {
				int l = stream.readUnsignedByte();
				additionalModels = new int[l];
				for (int l1 = 0; l1 < l; l1++)
					additionalModels[l1] = stream.readUnsignedWord();

			} else if (i == 90)
				stream.readUnsignedWord();
			else if (i == 91)
				stream.readUnsignedWord();
			else if (i == 92)
				stream.readUnsignedWord();
			else if (i == 93)
				drawMinimapDot = false;
			else if (i == 95)
				combatLevel = stream.readUnsignedWord();
			else if (i == 97)
				npcSize = stream.readUnsignedWord();
			else if (i == 98)
				npcHeight = stream.readUnsignedWord();
			else if (i == 99)
				priorityRender = true;
			else if (i == 100)
				lightModifier = stream.readSignedByte();
			else if (i == 101)
				shadowModifier = stream.readSignedByte() * 5;
			else if (i == 102)
				headIcon = stream.readUnsignedWord();
			else if (i == 103)
				degreesToTurn = stream.readUnsignedWord();
			else if (i == 106) {
				varBitID = stream.readUnsignedWord();
				if (varBitID == 65535)
					varBitID = -1;
				settingId = stream.readUnsignedWord();
				if (settingId == 65535)
					settingId = -1;
				int i1 = stream.readUnsignedByte();
				childrenIDs = new int[i1 + 1];
				for (int i2 = 0; i2 <= i1; i2++) {
					childrenIDs[i2] = stream.readUnsignedWord();
					if (childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}

			} else if (i == 107)
				clickable = false;
		} while (true);
	}

	public EntityDef() {
		turn90CCWAnimIndex = -1;
		varBitID = -1;
		turn180AnimIndex = -1;
		settingId = -1;
		combatLevel = -1;
		anInt64 = 1834;
		walkAnim = -1;
		spaceOccupied = 1;
		headIcon = -1;
		standAnim = -1;
		interfaceType = -1L;
		degreesToTurn = 32;
		turn90CWAnimIndex = -1;
		clickable = true;
		npcHeight = 128;
		drawMinimapDot = true;
		npcSize = 128;
		priorityRender = false;
	}

	public int turn90CCWAnimIndex;
	public static int anInt56;
	public int varBitID;
	public int turn180AnimIndex;
	public int settingId;
	public static Stream stream;
	public int combatLevel;
	public final int anInt64;
	public String name;
	public String actions[];
	public int walkAnim;
	public byte spaceOccupied;
	public int[] changedModelColors;
	public static int[] streamIndices;
	public int[] additionalModels;
	public int headIcon;
	public int[] originalModelColors;
	public int standAnim;
	public long interfaceType;
	public int degreesToTurn;
	public static EntityDef[] cache;
	public static Client clientInstance;
	public int turn90CWAnimIndex;
	public boolean clickable;
	public int lightModifier;
	public int npcHeight;
	public boolean drawMinimapDot;
	public int childrenIDs[];
	public byte description[];
	public int npcSize;
	public int shadowModifier;
	public boolean priorityRender;
	public int[] npcModels;
	public static MRUNodes mruNodes = new MRUNodes(30);

}
