package org.osps.model.players;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.osps.Config;
import org.osps.Connection;
import org.osps.Server;
import org.osps.clip.PathChecker;
import org.osps.clip.Region;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.content.IngameHiscore;
import org.osps.model.content.clan.Clan;
import org.osps.model.content.enchanting.Jewellery;
import org.osps.model.content.kill_streaks.Killstreak;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.items.GameItem;
import org.osps.model.items.Item;
import org.osps.model.items.ItemDefinition;
import org.osps.model.items.bank.BankTab;
import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.minigames.pest_control.PestControl;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.multiplayer_session.duel.DuelSessionRules.Rule;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.boss.Armadyl.Armadyl;
import org.osps.model.npcs.boss.Bandos.Bandos;
import org.osps.model.npcs.boss.Cerberus.Cerberus;
import org.osps.model.npcs.boss.Kalphite.Kalphite;
import org.osps.model.npcs.boss.Kraken.Kraken;
import org.osps.model.npcs.boss.Saradomin.Saradomin;
import org.osps.model.npcs.boss.Zamorak.Zamorak;
import org.osps.model.npcs.boss.abyssalsire.AbyssalSireConstants;
import org.osps.model.npcs.boss.instances.InstancedArea;
import org.osps.model.npcs.boss.instances.InstancedAreaManager;
import org.osps.model.npcs.boss.zulrah.Zulrah;
import org.osps.model.players.combat.Damage;
import org.osps.model.players.combat.Degrade;
import org.osps.model.players.combat.Degrade.DegradableItem;
import org.osps.model.players.combat.effects.DragonfireShieldEffect;
import org.osps.model.players.skills.crafting.CraftingData;
import org.osps.net.outgoing.messages.ComponentVisibility;
import org.osps.util.Misc;
import org.osps.util.Stream;


import com.google.common.base.Stopwatch;

public class PlayerAssistant {

	private Player player;

	public PlayerAssistant(Player Client) {
		this.player = Client;
	}

	public int CraftInt, Dcolor, FletchInt;

	public void openItemsKeptOnDeath() {
		ArrayList<List<GameItem>> items = player.getItems().getItemsKeptOnDeath();

		List<GameItem> lostItems = items.get(0);
		List<GameItem> keptItems = items.get(1);

		int keptCount = player.getItems().getKeptItemsCount();
		if (keptCount > keptItems.size()) {
			keptCount = keptItems.size();
		}

		for (int i = 0; i < 4; i++) {
			int id = keptCount > i ? keptItems.get(i).getId() : -1;
			sendFrame34a(10494, id, i, 1);
		}

		for (int i = 0; i < 64; i++) {
			int id = lostItems.size() > i ? lostItems.get(i).getId() : -1;
			int amount = lostItems.size() > i ? lostItems.get(i).getAmount() : 1;
			sendFrame34a(10600, id, i, amount);
		}

		sendFrame126("Pkers.Ca - Items Kept on Death", 17103);
		sendKeptItemsInformation();

		showInterface(17100);
	}

	public static int[] ranks = new int[11];
	
	public void sendKeptItemsInformation() {
		for (int i = 17109; i < 17131; i++) {
			sendFrame126("", i);
		}

		int count = player.getItems().getKeptItemsCount();

		sendFrame126("Items you will keep on death:", 17104);
		sendFrame126("Items you will lose on death:", 17105);
		sendFrame126("Player Information", 17106);
		sendFrame126("Max items kept on death:", 17107);
		sendFrame126("~ " + count + " ~", 17108);
		sendFrame126("The normal amount of", 17111);
		sendFrame126("items kept is three.", 17112);

		if (count < 2) {
			sendFrame126("You're marked with a", 17111);
			sendFrame126("@red@skull. @lre@This reduces the", 17112);
			sendFrame126("items you keep from", 17113);
			sendFrame126("three to zero!", 17114);
			if (count > 0) {
				sendFrame126("However, you also have", 17115);
				sendFrame126("the @red@Protect @lre@Items prayer", 17116);
				sendFrame126("active, which saves you", 17117);
				sendFrame126("one extra item!", 17118);
			}
		} else if (count < 4) {
			sendFrame126("You have no factors", 17111);
			sendFrame126("affecting the items you", 17112);
			sendFrame126("keep.", 17113);
		} else {
			sendFrame126("You have the @red@Protect", 17111);
			sendFrame126("@red@Item @lre@prayer active,", 17112);
			sendFrame126("which saves you one", 17113);
			sendFrame126("extra item!", 17114);
		}
	}

	public int getInventoryAmount() {
		int count = 0;
		for (int j = 0; j < player.playerItems.length; j++) {
			if (player.playerItems[j] > 0)
				count++;
		}
		return count;
	}

	List<Integer> walkableInterfaceList;

	public void sendParallellInterfaceVisibility(int interfaceId, boolean visible) {
		if (walkableInterfaceList == null)
			walkableInterfaceList = new ArrayList<Integer>();
		if (player != null && player.getOutStream() != null) {
			if (visible) {
				if (walkableInterfaceList.contains(interfaceId)) {
					return;
				} else {
					walkableInterfaceList.add(interfaceId);
				}
			} else {
				if (!walkableInterfaceList.contains(interfaceId)) {
					return;
				}
				walkableInterfaceList.remove((Integer) interfaceId);
			}
			player.getOutStream().createFrame(209);
			player.getOutStream().writeWordBigEndianA(interfaceId);
			player.getOutStream().writeWordBigEndianA(visible ? 1 : 0);
			player.flushOutStream();
		}
	}
	
	public void handleEmpty() {
		player.getDH().sendOption2("Yes, empty my inventory please.", "No, don't empty my inventory.");
		player.dialogueAction = 850;        
		}
	public boolean yellon = true;
	
	public void rewardPoints(int amount, String message) {
		player.sendMessage(message, 255);
		player.pkp += amount;
	}

	public boolean wearingDharok(Player c) {
		return (c.playerEquipment[Player.playerWeapon] == 4718 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4716 && c.playerEquipment[c.playerChest] == 4720
				&& c.playerEquipment[c.playerLegs] == 4722);
	}

	public boolean wearingVerac(Player c) {
		return (c.playerEquipment[Player.playerWeapon] == 4755 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4753 && c.playerEquipment[c.playerChest] == 4757
				&& c.playerEquipment[c.playerLegs] == 4759);
	}

	public boolean wearingAhrim(Player c) {
		return (c.playerEquipment[Player.playerWeapon] == 4710 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4708 && c.playerEquipment[c.playerChest] == 4712
				&& c.playerEquipment[c.playerLegs] == 4714);
	}

	public boolean wearingTorag(Player c) {
		return (c.playerEquipment[Player.playerWeapon] == 4747 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4745 && c.playerEquipment[c.playerChest] == 4749
				&& c.playerEquipment[c.playerLegs] == 4751);
	}

	public boolean wearingGuthan(Player c) {
		return (c.playerEquipment[Player.playerWeapon] == 4726 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4724 && c.playerEquipment[c.playerChest] == 4728
				&& c.playerEquipment[c.playerLegs] == 4730);
	}

	public boolean wearingKaril(Player c) {
		return (c.playerEquipment[Player.playerWeapon] == 4734 && c.playerEquipment[c.playerAmulet] == 12851
				&& c.playerEquipment[c.playerHat] == 4732 && c.playerEquipment[c.playerChest] == 4736
				&& c.playerEquipment[c.playerLegs] == 4738);
	}

	public boolean corpSpear() {
		return (player.playerEquipment[Player.playerWeapon] == 11824
				|| player.playerEquipment[Player.playerWeapon] == 11889);
	}

	public boolean Keris() {
		return (player.playerEquipment[Player.playerWeapon] == 10581);
	}

	public void destroyItem(int itemId) {
		itemId = player.droppedItem;
		String itemName = player.getItems().getItemName(itemId);
		player.getItems().deleteItem(itemId, player.getItems().getItemSlot(itemId),
				player.playerItemsN[player.getItems().getItemSlot(itemId)]);
		player.sendMessage("Your " + itemName + " vanishes as you drop it on the ground.");
		removeAllWindows();
	}

	public void moveCheck(int xMove, int yMove) {
		int random = Misc.random(3);
		if (random == 0) {
			movePlayer(3263, 3816, player.heightLevel);
		} else if (random == 1) {
			movePlayer(3252, 3818, player.heightLevel);
		} else if (random == 2) {
			movePlayer(3222, 3791, player.heightLevel);
		} else if (random == 3) {
			movePlayer(3237, 3778, player.heightLevel);
		}
	}

	public void getCallisto(int npcIndex, int playerX, int playerY) {
		try {
			if (NPCHandler.npcs[npcIndex] != null) {
				int x = NPCHandler.npcs[npcIndex].absX - playerX;
				int y = NPCHandler.npcs[npcIndex].absY - playerY;
				y = 6;
				moveCheck(x, y);
				player.newVar = System.currentTimeMillis();
				player.updateRequired = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public long getPlayTime(Player c) {
		return (System.currentTimeMillis() - c.recordedLogin) + c.playTimeTotal;
	}

	public long totalPlaytime(Player c) {
		return (getPlayTime(c) / 1000);
	}

	public String getPlaytime(Player c) {
		long DAY = (totalPlaytime(c) / 86400);
		long HR = (totalPlaytime(c) / 3600) - (DAY * 24);
		long MIN = (totalPlaytime(c) / 60) - (DAY * 1440) - (HR * 60);
		return ("Days:" + DAY + " Hours:" + HR + " Minutes:" + MIN + "");
	}

	public String getSmallPlaytime(Player c) {
		long DAY = (totalPlaytime(c) / 86400);
		long HR = (totalPlaytime(c) / 3600) - (DAY * 24);
		long MIN = (totalPlaytime(c) / 60) - (DAY * 1440) - (HR * 60);
		long SEC = totalPlaytime(c) - (DAY * 86400) - (HR * 3600) - (MIN * 60);
		return ("Day:" + DAY + "/Hr:" + HR + "/Min:" + MIN + "/Sec:" + SEC);
	}

	public void destroyInterface(int itemId) {// Destroy item created by Remco
		itemId = player.droppedItem;// The item u are dropping
		String itemName = player.getItems().getItemName(player.droppedItem);
		String[][] info = { // The info the dialogue gives
				{ "Are you sure you want to drop this item?", "14174" }, { "Yes.", "14175" }, { "No.", "14176" },
				{ "", "14177" }, { "This item is valuable, you will not", "14182" },
				{ "get it back once clicked yes.", "14183" }, { itemName, "14184" } };
		sendFrame34(itemId, 0, 14171, 1);
		for (int i = 0; i < info.length; i++)
			sendFrame126(info[i][0], Integer.parseInt(info[i][1]));
		sendFrame164(14170);
	}

	@SuppressWarnings("static-access")
	public void assembleSlayerHelmet() {
		if (player.slayerRecipe == false) {
			player.sendMessage("<col=255>You must purchase the recipe from the slayer shop");
			player.sendMessage("<col=255>before assembling a slayer helmet.");
			return;
		}
		if (player.playerLevel[Player.playerCrafting] < 55) {
			player.sendMessage("<col=255>You need a crafting level of 55 to assemble a slayer helmet.");
			return;
		}
		if (player.getItems().playerHasItem(4166) && player.getItems().playerHasItem(4168)
				&& player.getItems().playerHasItem(4164) && player.getItems().playerHasItem(4551)
				&& player.getItems().playerHasItem(8901)) {
			player.sendMessage("<col=255>You assemble the pieces and create a full slayer helmet!");
			player.getItems().deleteItem(4166, 1);
			player.getItems().deleteItem(4164, 1);
			player.getItems().deleteItem(4168, 1);
			player.getItems().deleteItem(4551, 1);
			player.getItems().deleteItem(8901, 1);
			player.getItems().addItem(11864, 1);
		} else {
			player.sendMessage(
					"You need a <col=255>Facemask</col>, <col=255>Nose peg</col>, <col=255>Spiny helmet</col> and <col=255>Earmuffs");
			player.sendMessage("</col>in order to assemble a slayer helmet.");
		}
	}

	public long lastReward = System.currentTimeMillis();

	Properties p = new Properties();

	public void loadAnnouncements() {
		try {
			loadIni();
			if (p.getProperty("announcement1").length() > 0) {
				player.sendMessage(p.getProperty("announcement1"));
			}
			if (p.getProperty("announcement2").length() > 0) {
				player.sendMessage(p.getProperty("announcement2"));
			}
			if (p.getProperty("announcement3").length() > 0) {
				player.sendMessage(p.getProperty("announcement3"));
			}
		} catch (Exception e) {
		}
	}

	private void loadIni() {
		try {
			p.load(new FileInputStream("./Announcements.ini"));
		} catch (Exception e) {
		}
	}

	public boolean wearingCape(int cape) {
		int capes[] = { 9747, 9748, 9750, 9751, 9753, 9754, 9756, 9757, 9759, 9760, 9762, 9763, 9765, 9766, 9768, 9769,
				9771, 9772, 9774, 9775, 9777, 9778, 9780, 9781, 9783, 9784, 9786, 9787, 9789, 9790, 9792, 9793, 9795,
				9796, 9798, 9799, 9801, 9802, 9804, 9805, 9807, 9808, 9810, 9811, 10662 };
		for (int i = 0; i < capes.length; i++) {
			if (capes[i] == cape) {
				return true;
			}
		}
		return false;
	}

	public int skillcapeGfx(int cape) {
		int capeGfx[][] = { { 9747, 823 }, { 9748, 823 }, { 9750, 828 }, { 9751, 828 }, { 9753, 824 }, { 9754, 824 },
				{ 9756, 832 }, { 9757, 832 }, { 9759, 829 }, { 9760, 829 }, { 9762, 813 }, { 9763, 813 }, { 9765, 817 },
				{ 9766, 817 }, { 9768, 833 }, { 9769, 833 }, { 9771, 830 }, { 9772, 830 }, { 9774, 835 }, { 9775, 835 },
				{ 9777, 826 }, { 9778, 826 }, { 9780, 818 }, { 9781, 818 }, { 9783, 812 }, { 9784, 812 }, { 9786, 827 },
				{ 9787, 827 }, { 9789, 820 }, { 9790, 820 }, { 9792, 814 }, { 9793, 814 }, { 9795, 815 }, { 9796, 815 },
				{ 9798, 819 }, { 9799, 819 }, { 9801, 821 }, { 9802, 821 }, { 9804, 831 }, { 9805, 831 }, { 9807, 822 },
				{ 9808, 822 }, { 9810, 825 }, { 9811, 825 }, { 10662, 816 } };
		for (int i = 0; i < capeGfx.length; i++) {
			if (capeGfx[i][0] == cape) {
				return capeGfx[i][1];
			}
		}
		return -1;
	}

	public int skillcapeEmote(int cape) {
		int capeEmote[][] = { { 9747, 4959 }, { 9748, 4959 }, { 9750, 4981 }, { 9751, 4981 }, { 9753, 4961 },
				{ 9754, 4961 }, { 9756, 4973 }, { 9757, 4973 }, { 9759, 4979 }, { 9760, 4979 }, { 9762, 4939 },
				{ 9763, 4939 }, { 9765, 4947 }, { 9766, 4947 }, { 9768, 4971 }, { 9769, 4971 }, { 9771, 4977 },
				{ 9772, 4977 }, { 9774, 4969 }, { 9775, 4969 }, { 9777, 4965 }, { 9778, 4965 }, { 9780, 4949 },
				{ 9781, 4949 }, { 9783, 4937 }, { 9784, 4937 }, { 9786, 4967 }, { 9787, 4967 }, { 9789, 4953 },
				{ 9790, 4953 }, { 9792, 4941 }, { 9793, 4941 }, { 9795, 4943 }, { 9796, 4943 }, { 9798, 4951 },
				{ 9799, 4951 }, { 9801, 4955 }, { 9802, 4955 }, { 9804, 4975 }, { 9805, 4975 }, { 9807, 4957 },
				{ 9808, 4957 }, { 9810, 4963 }, { 9811, 4963 }, { 10662, 4945 } };
		for (int i = 0; i < capeEmote.length; i++) {
			if (capeEmote[i][0] == cape) {
				return capeEmote[i][1];
			}
		}
		return -1;
	}

	public void otherInv(Player c, Player o) {
		if (o == c || o == null || c == null)
			return;
		int[] backupItems = c.playerItems;
		int[] backupItemsN = c.playerItemsN;
		c.playerItems = o.playerItems;
		c.playerItemsN = o.playerItemsN;
		c.getItems().resetItems(3214);
		c.playerItems = backupItems;
		c.playerItemsN = backupItemsN;
	}

	public void multiWay(int i1) {
		// synchronized(c) {
		player.outStream.createFrame(61);
		player.outStream.writeByte(i1);
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);

	}

	public boolean salveAmulet() {
		return (player.playerEquipment[player.playerAmulet] == 4081);
	}

	public int backupItems[] = new int[Config.BANK_SIZE];
	public int backupItemsN[] = new int[Config.BANK_SIZE];

	public void otherBank(Player c, Player o) {
		if (o == c || o == null || c == null) {
			return;
		}

		for (int i = 0; i < o.bankItems.length; i++) {
			backupItems[i] = c.bankItems[i];
			backupItemsN[i] = c.bankItemsN[i];
			c.bankItemsN[i] = o.bankItemsN[i];
			c.bankItems[i] = o.bankItems[i];
		}
		openUpBank();

		for (int i = 0; i < o.bankItems.length; i++) {
			c.bankItemsN[i] = backupItemsN[i];
			c.bankItems[i] = backupItems[i];
		}
	}

	public void sendString(final String s, final int id) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(126);
			player.getOutStream().writeString(s);
			player.getOutStream().writeWordA(id);
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}

	}

	/**
	 * Changes the main displaying sprite on an interface. The index represents
	 * the location of the new sprite in the index of the sprite array.
	 * 
	 * @param componentId
	 *            the interface
	 * @param index
	 *            the index in the array
	 */
	public void sendChangeSprite(int componentId, byte index) {
		if (player == null || player.getOutStream() == null) {
			return;
		}
		Stream stream = player.getOutStream();
		stream.createFrame(7);
		stream.writeDWord(componentId);
		stream.writeByte(index);
		player.flushOutStream();
	}

	/* Treasure */
	public static int lowLevelReward[] = { 1077, 1107, 1089, 1168, 1165, 1179, 1195, 1283, 1297, 1313, 1327, 1341, 1361,
			1367, 1426, 2633, 7362, 7368, 7366, 2583, 2585, 2587, 2589, 2591, 2593, 2595, 2597, 7332, 7338, 7350, 2635,
			2637, 7388, 7386, 7392, 7390, 7396, 7394, 2631, 7364, 7356 };
	public static int mediemLevelReward[] = { 2599, 2601, 2603, 2605, 2607, 2609, 2611, 2613, 7334, 7340, 7346, 7352,
			7358, 7319, 7321, 7323, 7325, 7327, 7372, 7370, 7380, 7378, 2645, 2647, 2649, 2577, 2579, 1073, 1091, 1099,
			1111, 1135, 1124, 1145, 1161, 1169, 1183, 1199, 1211, 1245, 1271, 1287, 1301, 1317, 1332, 1357, 1371, 1430,
			6916, 6918, 6920, 6922, 6924, 10400, 10402, 10416, 10418, 10420, 10422, 10436, 10438, 10446, 10448, 10450,
			10452, 10454, 10456, 6889 };
	public static int highLevelReward[] = { 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201, 1275, 1303, 1319, 1333,
			1359, 1373, 2491, 2497, 2503, 861, 859, 2581, 2577, 2651, 1079, 1093, 1113, 1127, 1147, 1163, 1185, 1201,
			1275, 1303, 1333, 1359, 1373, 2491, 2497, 2503, 861, 859, 2581, 2577, 2651, 2615, 2617, 2619, 2621, 2623,
			2625, 2627, 2629, 2639, 2641, 2643, 2651, 2653, 2655, 2657, 2659, 2661, 2663, 2665, 2667, 2669, 2671, 2673,
			2675, 7342, 7348, 7374, 7376, 7382, 10330, 10338, 10348, 10332, 10340, 10346, 10334, 10342, 10350, 10336,
			10344, 10352, 10368, 10376, 10384, 10370, 10378, 10386, 10372, 10380, 10388, 10374, 10382, 10390, 10470,
			10472, 10474, 10440, 10442, 10444, 6914, 7384, 7398, 7399, 7400, 3481, 3483, 3485, 3486, 3488, 1079, 1093,
			1113, 1127, 1148, 1164, 1185, 1201, 1213, 1247, 1275, 1289, 1303, 1359, 1374, 1432, 2615, 2617, 2619, 2621,
			2623, 1319, 1333, 1347, };

	public static int lowLevelStacks[] = { 995, 380, 561, 886, };
	public static int mediumLevelStacks[] = { 995, 374, 561, 563, 890, };
	public static int highLevelStacks[] = { 995, 386, 561, 563, 560, 892 };

	public static void addClueReward(Player c, int clueLevel) {
		int chanceReward = Misc.random(2);
		if (clueLevel == 0) {
			switch (chanceReward) {
			case 0:
				displayReward(c, lowLevelReward[Misc.random(40)], 1, lowLevelReward[Misc.random(16)], 1,
						lowLevelStacks[Misc.random(3)], 1 + Misc.random(150));
				break;
			case 1:
				displayReward(c, lowLevelReward[Misc.random(26)], 1, lowLevelStacks[Misc.random(3)],
						1 + Misc.random(150), -1, 1);
				break;
			case 2:
				displayReward(c, lowLevelReward[Misc.random(40)], 1, lowLevelReward[Misc.random(16)], 1, -1, 1);
				break;
			}
		} else if (clueLevel == 1) {
			switch (chanceReward) {
			case 0:
				displayReward(c, mediemLevelReward[Misc.random(68)], 1, mediemLevelReward[Misc.random(13)], 1,
						mediumLevelStacks[Misc.random(4)], 1 + Misc.random(200));
				break;
			case 1:
				displayReward(c, mediemLevelReward[Misc.random(46)], 1, mediumLevelStacks[Misc.random(4)],
						1 + Misc.random(200), -1, 1);
				break;
			case 2:
				displayReward(c, mediemLevelReward[Misc.random(68)], 1, mediemLevelReward[Misc.random(13)], 1, -1, 1);
				break;
			}
		} else if (clueLevel == 2) {
			switch (chanceReward) {
			case 0:
				displayReward(c, highLevelReward[Misc.random(135)], 1, highLevelReward[Misc.random(60)], 1,
						highLevelStacks[Misc.random(5)], 1 + Misc.random(350));
				break;
			case 1:
				displayReward(c, highLevelReward[Misc.random(52)], 1, highLevelStacks[Misc.random(5)],
						1 + Misc.random(350), -1, 1);
				break;
			case 2:
				displayReward(c, highLevelReward[Misc.random(135)], 1, highLevelReward[Misc.random(60)], 1, -1, 1);
				break;
			}
		}
	}

	public static void displayReward(Player c, int item, int amount, int item2, int amount2, int item3, int amount3) {
		int[] items = { item, item2, item3 };
		int[] amounts = { amount, amount2, amount3 };
		c.outStream.createFrameVarSizeWord(53);
		c.outStream.writeWord(6963);
		c.outStream.writeWord(items.length);
		for (int i = 0; i < items.length; i++) {
			if (c.playerItemsN[i] > 254) {
				c.outStream.writeByte(255);
				c.outStream.writeDWord_v2(amounts[i]);
			} else {
				c.outStream.writeByte(amounts[i]);
			}
			if (items[i] > 0) {
				c.outStream.writeWordBigEndianA(items[i] + 1);
			} else {
				c.outStream.writeWordBigEndianA(0);
			}
		}
		c.outStream.endFrameVarSizeWord();
		c.flushOutStream();
		c.getItems().addItem(item, amount);
		c.getItems().addItem(item2, amount2);
		c.getItems().addItem(item3, amount3);
		c.getPA().showInterface(6960);
	}

	public void removeAllItems() {
		for (int i = 0; i < player.playerItems.length; i++) {
			player.playerItems[i] = 0;
		}
		for (int i = 0; i < player.playerItemsN.length; i++) {
			player.playerItemsN[i] = 0;
		}
		player.getItems().resetItems(3214);
	}

	public void setConfig(int id, int state) {
		// synchronized (c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(36);
			player.getOutStream().writeWordBigEndian(id);
			player.getOutStream().writeByte(state);
			player.flushOutStream();
		}
		// }
	}

	public void sendConfig(final int id, final int state) {
		if (this.player.getOutStream() != null && this.player != null) {
			if (state < 128) {
				this.player.getOutStream().createFrame(36);
				this.player.getOutStream().writeWordBigEndian(id);
				this.player.getOutStream().writeByte(state);
			} else {
				this.player.getOutStream().createFrame(87);
				this.player.getOutStream().writeWordBigEndian_dup(id);
				this.player.getOutStream().writeDWord_v1(state);
			}
			this.player.flushOutStream();
		}
	}

	public double getAgilityRunRestore(Player c) {
		return 2260 - (c.playerLevel[16] * 10);
	}

	public Player getClient(String playerName) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.playerName.equalsIgnoreCase(playerName)) {
					return p;
				}
			}
		}
		return null;
	}

	public void normYell(String Message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				c2.sendClan(Misc.optimizeText(player.playerName), Message, "Pkers.Ca Spawn",
						player.getRights().getValue());
			}
		}
	}

	public void admYell(String Message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				c2.sendClan(Misc.optimizeText(player.playerName), Message, "Developer", player.getRights().getValue());
			}
		}
	}

	public void itemOnInterface(int interfaceChild, int zoom, int itemId) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(246);
			player.getOutStream().writeWordBigEndian(interfaceChild);
			player.getOutStream().writeWord(zoom);
			player.getOutStream().writeWord(itemId);
			player.flushOutStream();
		}
	}

	public void playerWalk(int x, int y) {
		PathFinder.getPathFinder().findRoute(player, x, y, true, 1, 1);
	}

	/**
	 * If the player is using melee and is standing diagonal from the opponent,
	 * then move towards opponent.
	 */

	public void movePlayerDiagonal(int i) {
		Player c2 = PlayerHandler.players[i];
		boolean hasMoved = false;
		int c2X = c2.getX();
		int c2Y = c2.getY();
		if (player.goodDistance(c2X, c2Y, player.getX(), player.getY(), 1)) {
			if (player.getX() != c2.getX() && player.getY() != c2.getY()) {
				if (player.getX() > c2.getX() && !hasMoved) {
					if (Region.getClipping(player.getX() - 1, player.getY(), player.heightLevel, -1, 0)) {
						hasMoved = true;
						walkTo(-1, 0);
					}
				} else if (player.getX() < c2.getX() && !hasMoved) {
					if (Region.getClipping(player.getX() + 1, player.getY(), player.heightLevel, 1, 0)) {
						hasMoved = true;
						walkTo(1, 0);
					}
				}

				if (player.getY() > c2.getY() && !hasMoved) {
					if (Region.getClipping(player.getX(), player.getY() - 1, player.heightLevel, 0, -1)) {
						hasMoved = true;
						walkTo(0, -1);
					}
				} else if (player.getY() < c2.getY() && !hasMoved) {
					if (Region.getClipping(player.getX(), player.getY() + 1, player.heightLevel, 0, 1)) {
						hasMoved = true;
						walkTo(0, 1);
					}
				}
			}
		}
		hasMoved = false;
	}

	/**
	 * 
	 * @return: Gets players Clan
	 */
	public Clan getClan() {
		if (Server.clanManager.clanExists(player.playerName)) {
			return Server.clanManager.getClan(player.playerName);
		}
		return null;
	}

	/*
	 * public void sendClan(String name, String message, String clan, int
	 * rights) { c.outStream.createFrameVarSizeWord(217);
	 * c.outStream.writeString(name);
	 * c.outStream.writeString(Misc.formatPlayerName(message));
	 * c.outStream.writeString(clan); c.outStream.writeWord(rights);
	 * c.outStream.endFrameVarSize(); }
	 */

	public void sendClan(String name, String message, String clan, int rights) {
		player.outStream.createFrameVarSizeWord(217);
		player.outStream.writeString(name);
		player.outStream.writeString(Misc.formatPlayerName(message));
		player.outStream.writeString(clan);
		player.outStream.writeWord(rights);
		player.outStream.endFrameVarSize();
	}

	public void clearClanChat() {
		player.clanId = -1;
		player.getPA().sendFrame126("Talking in: ", 18139);
		player.getPA().sendFrame126("Owner: ", 18140);
		for (int j = 18144; j < 18244; j++) {
			player.getPA().sendFrame126("", j);
		}
	}

	/**
	 * Sets the clan information for the player's clan.
	 */
	public void setClanData() {
		boolean exists = Server.clanManager.clanExists(player.playerName);
		if (!exists || player.clan == null) {
			sendFrame126("Join chat", 18135);
			sendFrame126("Talking in: Not in chat", 18139);
			sendFrame126("Owner: None", 18140);
		}
		if (!exists) {
			sendFrame126("Chat Disabled", 18306);
			String title = "";
			for (int id = 18307; id < 18317; id += 3) {
				if (id == 18307) {
					title = "Anyone";
				} else if (id == 18310) {
					title = "Anyone";
				} else if (id == 18313) {
					title = "General+";
				} else if (id == 18316) {
					title = "Only Me";
				}
				sendFrame126(title, id + 2);
			}
			for (int index = 0; index < 100; index++) {
				sendFrame126("", 18323 + index);
			}
			for (int index = 0; index < 100; index++) {
				sendFrame126("", 18424 + index);
			}
			return;
		}
		Clan clan = Server.clanManager.getClan(player.playerName);
		sendFrame126(clan.getTitle(), 18306);
		String title = "";
		for (int id = 18307; id < 18317; id += 3) {
			if (id == 18307) {
				title = clan.getRankTitle(clan.whoCanJoin)
						+ (clan.whoCanJoin > Clan.Rank.ANYONE && clan.whoCanJoin < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18310) {
				title = clan.getRankTitle(clan.whoCanTalk)
						+ (clan.whoCanTalk > Clan.Rank.ANYONE && clan.whoCanTalk < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18313) {
				title = clan.getRankTitle(clan.whoCanKick)
						+ (clan.whoCanKick > Clan.Rank.ANYONE && clan.whoCanKick < Clan.Rank.OWNER ? "+" : "");
			} else if (id == 18316) {
				title = clan.getRankTitle(clan.whoCanBan)
						+ (clan.whoCanBan > Clan.Rank.ANYONE && clan.whoCanBan < Clan.Rank.OWNER ? "+" : "");
			}
			sendFrame126(title, id + 2);
		}
		if (clan.rankedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.rankedMembers.size()) {
					sendFrame126("<clan=" + clan.ranks.get(index) + ">" + clan.rankedMembers.get(index), 18323 + index);
				} else {
					sendFrame126("", 18323 + index);
				}
			}
		}
		if (clan.bannedMembers != null) {
			for (int index = 0; index < 100; index++) {
				if (index < clan.bannedMembers.size()) {
					sendFrame126(clan.bannedMembers.get(index), 18424 + index);
				} else {
					sendFrame126("", 18424 + index);
				}
			}
		}
	}

	public void resetAutocast() {
		player.autocastId = 0;
		player.spellId = 0;
		player.autocasting = false;
		player.getPA().sendFrame36(108, 0);
		player.getItems().sendWeapon(player.playerEquipment[Player.playerWeapon],
		player.getItems().getItemName(player.playerEquipment[Player.playerWeapon])); 
	}

	public int getItemSlot(int itemID) {
		for (int i = 0; i < player.playerItems.length; i++) {
			if ((player.playerItems[i] - 1) == itemID) {
				return i;
			}
		}
		return -1;
	}

	public boolean isItemInBag(int itemID) {
		for (int i = 0; i < player.playerItems.length; i++) {
			if ((player.playerItems[i] - 1) == itemID) {
				return true;
			}
		}
		return false;
	}

	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public void turnTo(int pointX, int pointY) {
		player.focusPointX = 2 * pointX + 1;
		player.focusPointY = 2 * pointY + 1;
		player.updateRequired = true;
	}

	public void movePlayer(int x, int y, int h) {
		if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
			if (!player.getRights().isStaff()) {
				player.sendMessage("You cannot teleport whilst in the duel arena!");
				return;
			}
		}
		/*
		 * (if (c.inDuelArena() ||
		 * Server.getMultiplayerSessionListener().inAnySession(c)) { if
		 * (!c.getRights().isStaff()) { return; } }
		 */
		if (!player.lastSpear.elapsed(4000)) {
			player.sendMessage("You're trying to move too fast.");
			return;
		}
		player.resetWalkingQueue();
		player.teleportToX = x;
		player.teleportToY = y;
		player.heightLevel = h;
		requestUpdates();
		player.lastMove = System.currentTimeMillis();
	}

	public void movePlayerDuel(int x, int y, int h) {
		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(session) && session.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERACTION
				&& Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
			// c.getPA().removeAllWindows();
			return;
		}
		player.resetWalkingQueue();
		player.teleportToX = x;
		player.teleportToY = y;
		player.heightLevel = h;
		requestUpdates();
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public int absX, absY;
	public int heightLevel;


	@SuppressWarnings("unused")
	public static void QuestReward(Player c, String questName, int PointsGain, String Line1, String Line2, String Line3,
			String Line4, String Line5, String Line6, int itemID) {
		c.getPA().sendFrame126("You have completed " + questName + "!", 12144);
		sendQuest(c, "" + (c.QuestPoints), 12147);
		// c.QuestPoints += PointsGain;
		sendQuest(c, Line1, 12150);
		sendQuest(c, Line2, 12151);
		sendQuest(c, Line3, 12152);
		sendQuest(c, Line4, 12153);
		sendQuest(c, Line5, 12154);
		sendQuest(c, Line6, 12155);
		c.getPlayerAssistant().sendFrame246(12145, 250, itemID);
		c.getPA().showInterface(12140);
		Server.getStillGraphicsManager().stillGraphics(c, c.getX(), c.getY(), c.getHeightLevel(), 199, 0);
	}

	public static void sendQuest(Player client, String s, int i) {
		client.getOutStream().createFrameVarSizeWord(126);
		client.getOutStream().writeString(s);
		client.getOutStream().writeWordA(i);
		client.getOutStream().endFrameVarSizeWord();
		client.flushOutStream();
	}

	public void sendStillGraphics(int id, int heightS, int y, int x, int timeBCS) {
		player.getOutStream().createFrame(85);
		player.getOutStream().writeByteC(y - (player.mapRegionY * 8));
		player.getOutStream().writeByteC(x - (player.mapRegionX * 8));
		player.getOutStream().createFrame(4);
		player.getOutStream().writeByte(0);// Tiles away (X >> 4 + Y & 7)
											// //Tiles away from
		// absX and absY.
		player.getOutStream().writeWord(id); // Graphic ID.
		player.getOutStream().writeByte(heightS); // Height of the graphic when
													// cast.
		player.getOutStream().writeWord(timeBCS); // Time before the graphic
													// plays.
		player.flushOutStream();
	}

	public void createArrow(int type, int id) {
		if (player != null) {
			player.getOutStream().createFrame(254); // The packet ID
			player.getOutStream().writeByte(type); // 1=NPC, 10=Player
			player.getOutStream().writeWord(id); // NPC/Player ID
			player.getOutStream().write3Byte(0); // Junk
		}
	}

	public void createArrow(int x, int y, int height, int pos) {
		if (player != null) {
			player.getOutStream().createFrame(254); // The packet ID
			player.getOutStream().writeByte(pos); // Position on Square(2 =
													// middle, 3
													// = west, 4 = east, 5 =
													// south,
													// 6 = north)
			player.getOutStream().writeWord(x); // X-Coord of Object
			player.getOutStream().writeWord(y); // Y-Coord of Object
			player.getOutStream().writeByte(height); // Height off Ground
		}
	}

	public void loadQuests() {
		/** Quest Tab **/
		player.getPA().loadAchievements();
		DecimalFormat df = new DecimalFormat("#.##");
		double KDR = ((double) player.KC) / ((double) player.DC);
		sendFrame126("@or1@Player Panel", 640);
		sendFrame126("Pk Points " +player.pkp, 15037);
		sendFrame126("@or1@DP: "+ player.donatorPoints, 3211);
		sendFrame126("Pkers.Ca Username @gre@"+ player.playerName+"", 16323);
		sendFrame126("Pkers.Ca Username @gre@"+ player.playerName+"", 16023);
		sendFrame126("@gre@Players online: @yel@ "+PlayerHandler.getPlayerCount(), 16026);
		sendFrame126("@gre@Amount Donated: @yel@"+player.amDonated+ " ", 16027);
		if (player.getRights().getValue() == 0)
			sendFrame126("@gre@Rank: @yel@Player", 16028);
		if (player.getRights().getValue() == 1)
			sendFrame126("@gre@Rank: @yel@Moderator ", 16028);
		if (player.getRights().getValue() == 2)
			sendFrame126("@gre@Rank: @yel@Administrator ", 16028);
		if (player.getRights().getValue() == 3)
			sendFrame126("@gre@Rank: @yel@Owner ", 16028);
		if (player.getRights().getValue() == 5)
			sendFrame126("@gre@Rank: @yel@Donator] ", 16028);
		if (player.getRights().getValue() == 6)
			sendFrame126("@gre@Rank: @yel@Sponsor] ", 16028);
		if (player.getRights().getValue() == 7)
			sendFrame126("@gre@Rank: @yel@Respected Donator ", 16028);
		if (player.getRights().getValue() == 8)
			sendFrame126("@gre@Rank: @yel@Legendary Donator ", 16028);
		if (player.getRights().getValue() == 9)
			sendFrame126("@gre@Rank: @yel@Developer] ", 16028);
		if (player.getRights().getValue() == 10)
			sendFrame126("@gre@Rank: @yel@Youtuber] ", 16028);
		if (player.getRights().getValue() == 11)
			sendFrame126("@gre@Rank: @yel@Server-Support ", 16028);
		    sendFrame126("@bla@-   -   -   -   -   -   -", 16029);
		    sendFrame126("@or1@Kill Count = @yel@ [ "+player.KC+ " ]", 16030);
			sendFrame126("@or1@Death Count = @yel@ [ "+player.DC+ " ]", 16031);
			sendFrame126("@or2@KDR = @yel@ [ " + df.format(KDR) + " ]", 16032);
			sendFrame126("@bla@-   -   -   -   -   -   -", 16033);
			sendFrame126("@gre@Pk Points: @yel@ [ " +player.pkp+ " ]", 16034);
			sendFrame126("@gre@Donator Points: @yel@ [  "+player.donatorPoints+ " ]", 16035);
			sendFrame126("@gre@Vote Points: @yel@[ " +player.votePoints+ " ]", 16036);
			//sendFrame126("@gre@Sacrifice Points: @yel@[ " + player.hungerPoints + " ]", 16037);
			sendFrame126("", 16037);
			sendFrame126("@bla@-   -   -   -   -   -   -", 16038);
			sendFrame126("@gre@BlackMarks: @yel@"+player.blackMarks+"@whi@ [5 = BAN]", 16039);
			sendFrame126("@bla@-   -   -   -   -   -   -", 16040);
			sendFrame126("@or1@Current Kill Streak: @whi@[@yel@ " +player.killStreak+ " @whi@]", 16041);
			sendFrame126("@or1@Highest Kill Streak: @whi@[@yel@ " +player.highestKillStreak+ " @whi@]", 16042);
			sendFrame126("@bla@-   -   -   -   -   -   -", 16043);
			String timeOfDay = Server.calendar.getHMS();
			String[] split = timeOfDay.split(":");
			String hoursAndMinutes = ""+split[0]+":"+split[1];
			sendFrame126("@or1@Server Time: @whi@[@yel@ " + hoursAndMinutes + " @whi@]", 16044);
			sendFrame126("@bla@-   -   -   -   -   -   -", 16045);
			if(yellon == true)
				sendFrame126("@or2@Yell Chat: @whi@[ @gre@ON@whi@ ]", 16046);
			if(yellon == false)
				sendFrame126("@or2@Yell Chat: @whi@[ @red@OFF@whi@ ]", 16046);
			sendFrame126("@bla@-   -   -   -   -   -   -", 16047);
			double maxHit = 0.0D;
			sendFrame126("@or1@Max Melee Hit = @whi@[@yel@ " +maxHit+ " @whi@]", 16048);
			sendFrame126("@or1@Max Range Hit = @whi@[@yel@ " +maxHit+ " @whi@]", 16049);
			sendFrame126("@or1@Max Magic Hit = @whi@[@yel@ " +maxHit+ " @whi@]", 16050);
			sendFrame126("@or1@Max Vengeance Hit = @whi@[@yel@ " +maxHit+ " @whi@]", 16051);
			sendFrame126("@bla@-   -   -   -   -   -   -", 16052);
			sendFrame126("@or2@Preloaded Gear Setup", 16053);
			sendFrame126("@bla@-   -   -   -   -   -   -", 16054);
			
	        }
	
	public void updatePKP() {
		sendFrame126("@gre@Pk Points: @yel@ [ " +player.pkp+ " ]", 16034);
	}
			
			public void loadAchievements() {
				/** Achievement Tab **/
				sendFrame126("        @or1@Achievements", 16301);
				sendFrame126("@or2@Kills Achievements", 16326);
				sendFrame126("@bla@-   -   -   -   -   -   -", 16327);
				sendFrame126("50 Kills", 16328);
				sendFrame126("100 Kills", 16329);
				sendFrame126("250 Kills", 16330);
				sendFrame126("500 Kills", 16331);
				sendFrame126("750 Kills", 16332);
			    sendFrame126("1000 Kills", 16333);
				sendFrame126("1250 Kills", 16334);
				sendFrame126("1500 Kills", 16335);
				sendFrame126("1750 Kills", 16336);
				sendFrame126("2000 Kills", 16337);
				sendFrame126("2250 Kills", 16338);
				sendFrame126("2500 Kills", 16339);
				sendFrame126("2750 Kills", 16340);
				sendFrame126("3000 Kills", 16341);
				sendFrame126("3250 Kills", 16342);
				sendFrame126("3500 Kills", 16343);
				sendFrame126("3750 Kills", 16344);
				sendFrame126("4000 Kills", 16345);
				sendFrame126("4250 Kills", 16346);
				sendFrame126("4500 Kills", 16347);
				sendFrame126("4750 Kills", 16348);
				sendFrame126("5000 Kills", 16349);
				sendFrame126("@or2@KillStreak Achievements", 16350);
				sendFrame126("@bla@-   -   -   -   -   -   -", 16351);
				sendFrame126("5 KillStreak", 16352);
				sendFrame126("10 KillStreak", 16353);
				sendFrame126("25 KillStreak", 16354);
				sendFrame126("50 KillStreak", 16355);
				sendFrame126("75 KillStreak", 16356);
				sendFrame126("100 KillStreak", 16357);
			    sendFrame126("125 KillStreak", 16358);
				sendFrame126("150 KillStreak", 16359);
				sendFrame126("175 KillStreak", 16360);
				sendFrame126("200 KillStreak", 16361);
				if (player.KC >= 50)
					sendFrame126("@gre@50 Kills", 16328);
				if (player.KC >= 100)
					sendFrame126("@gre@100 Kills", 16329);
				if (player.KC >= 250)
					sendFrame126("@gre@250 Kills", 16330);
				if (player.KC >= 500)
					sendFrame126("@gre@500 Kills", 16331);
				if (player.KC >= 750)
					sendFrame126("@gre@750 Kills", 16332);
				if (player.KC >= 1000)
					sendFrame126("@gre@1000 Kills", 16333);
				if (player.KC >= 1250)
					sendFrame126("@gre@1250 Kills", 16334);
				if (player.KC >= 1500)
					sendFrame126("@gre@1500 Kills", 16335);
				if (player.KC >= 1750)
					sendFrame126("@gre@1750 Kills", 16336);
				if (player.KC >= 2000)
					sendFrame126("@gre@2000 Kills", 16337);
				if (player.KC >= 2250)
					sendFrame126("@gre@2250 Kills", 16338);
				if (player.KC >= 2500)
					sendFrame126("@gre@2500 Kills", 16339);
				if (player.KC >= 2750)
					sendFrame126("@gre@2750 Kills", 16340);
				if (player.KC >= 3000)
					sendFrame126("@gre@3000 Kills", 16341);
				if (player.KC >= 3250)
					sendFrame126("@gre@3250 Kills", 16342);
				if (player.KC >= 3500)
					sendFrame126("@gre@3500 Kills", 16343);
				if (player.KC >= 3750)
					sendFrame126("@gre@3750 Kills", 16344);
				if (player.KC >= 4000)
					sendFrame126("@gre@4000 Kills", 16345);
				if (player.KC >= 4250)
					sendFrame126("@gre@4250 Kills", 16346);
				if (player.KC >= 4500)
					sendFrame126("@gre@4500 Kills", 16347);
				if (player.KC >= 4750)
					sendFrame126("@gre@4750 Kills", 16348);
				if (player.KC >= 5000)
					sendFrame126("@gre@5000 Kills", 16349);
				updateKillStreak();
			}
			public void updateKillStreak() {
				if (player.highestKillStreak >= 5)
					sendFrame126("@gre@5 KillStreak", 16352);
				if (player.highestKillStreak >= 10)
					sendFrame126("@gre@10 KillStreak", 16353);
				if (player.highestKillStreak >= 25)
					sendFrame126("@gre@25 KillStreak", 16354);
				if (player.highestKillStreak >= 50)
					sendFrame126("@gre@50 KillStreak", 16355);
				if (player.highestKillStreak >= 75)
					sendFrame126("@gre@75 KillStreak", 16356);
				if (player.highestKillStreak >= 100)
					sendFrame126("@gre@100 KillStreak", 16357);
				if (player.highestKillStreak >= 125)
					sendFrame126("@gre@125 KillStreak", 16358);
				if (player.highestKillStreak >= 150)
					sendFrame126("@gre@150 KillStreak", 16359);
				if (player.highestKillStreak >= 175)
					sendFrame126("@gre@175 KillStreak", 16360);
				if (player.highestKillStreak >= 200)
					sendFrame126("@gre@200 KillStreak", 16361);

			}

	public void sendFrame126(String s, int id) {
		if (!player.checkPacket126Update(s, id)) {
			return;
		}
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(126);
			player.getOutStream().writeString(s);
			player.getOutStream().writeWordA(id);
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}

	}

	public void sendLink(String s) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(187);
			player.getOutStream().writeString(s);
		}
	}

	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(134);
			player.getOutStream().writeByte(skillNum);
			player.getOutStream().writeDWord_v1(XP);
			player.getOutStream().writeByte(currentLevel);
			player.flushOutStream();
		}
	}

	public void sendFrame106(int sideIcon) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(106);
			player.getOutStream().writeByteC(sideIcon);
			player.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame107() {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(107);
			player.flushOutStream();
		}
	}

	public void sendFrame36(int id, int state) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(36);
			player.getOutStream().writeWordBigEndian(id);
			player.getOutStream().writeByte(state);
			player.flushOutStream();
		}

	}

	public void sendFrame185(int Frame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(185);
			player.getOutStream().writeWordBigEndianA(Frame);
		}

	}

	public void showInterface(int interfaceid) {
		if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(player, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		if (player.getOutStream() != null && player != null) {
			player.setInterfaceOpen(interfaceid);
			player.getOutStream().createFrame(97);
			player.getOutStream().writeWord(interfaceid);
			player.flushOutStream();
		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(248);
			player.getOutStream().writeWordA(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.flushOutStream();

		}
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(246);
			player.getOutStream().writeWordBigEndian(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.getOutStream().writeWord(SubFrame2);
			player.flushOutStream();
		}
	}

	public void sendFrame171(int state, int componentId) {
		if (player.getPacketDropper().requiresUpdate(171, new ComponentVisibility(state, componentId))) {
			if (player.getOutStream() != null && player != null) {
				player.getOutStream().createFrame(171);
				player.getOutStream().writeByte(state);
				player.getOutStream().writeWord(componentId);
				player.flushOutStream();
			}
		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(200);
			player.getOutStream().writeWord(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(70);
			player.getOutStream().writeWord(i);
			player.getOutStream().writeWordBigEndian(o);
			player.getOutStream().writeWordBigEndian(id);
			player.flushOutStream();
		}

	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(75);
			player.getOutStream().writeWordBigEndianA(MainFrame);
			player.getOutStream().writeWordBigEndianA(SubFrame);
			player.flushOutStream();
		}

	}

	public void sendFrame164(int Frame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(164);
			player.getOutStream().writeWordBigEndian_dup(Frame);
			player.flushOutStream();
		}

	}

	public void sendFrame87(int id, int state) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(87);
			player.getOutStream().writeWordBigEndian_dup(id);
			player.getOutStream().writeDWord_v1(state);
			player.flushOutStream();
		}

	}

	public void sendPM(long name, int rights, byte[] chatMessage) {
		player.getOutStream().createFrameVarSize(196);
		player.getOutStream().writeQWord(name);
		player.getOutStream().writeDWord(new Random().nextInt());
		player.getOutStream().writeByte(rights);
		player.getOutStream().writeBytes(chatMessage, chatMessage.length, 0);
		player.getOutStream().endFrameVarSize();

	}

	public void createPlayerHints(int type, int id) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(254);
			player.getOutStream().writeByte(type);
			player.getOutStream().writeWord(id);
			player.getOutStream().write3Byte(0);
			player.flushOutStream();
		}

	}

	public void createObjectHints(int x, int y, int height, int pos) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(254);
			player.getOutStream().writeByte(pos);
			player.getOutStream().writeWord(x);
			player.getOutStream().writeWord(y);
			player.getOutStream().writeByte(height);
			player.flushOutStream();
		}

	}

	public void sendFriend(long friend, int world) {
		player.getOutStream().createFrame(50);
		player.getOutStream().writeQWord(friend);
		player.getOutStream().writeByte(world);

	}

	public void removeAllWindows() {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getPA().resetVariables();
			player.getOutStream().createFrame(219);
			player.flushOutStream();
		}
		player.setDialogue(null);
		resetVariables();
	}

	public void closeAllWindows() {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(219);
			player.flushOutStream();
			player.isBanking = false;
			resetVariables();
		}
	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.outStream.createFrameVarSizeWord(34); // init item to smith
															// screen
			player.outStream.writeWord(column); // Column Across Smith Screen
			player.outStream.writeByte(4); // Total Rows?
			player.outStream.writeDWord(slot); // Row Down The Smith Screen
			player.outStream.writeWord(id + 1); // item
			player.outStream.writeByte(amount); // how many there are?
			player.outStream.endFrameVarSizeWord();
		}

	}

	public void walkableInterface(int id) {
		synchronized(player) {
			if(player.getOutStream() != null && player != null) {
				player.getOutStream().createFrame(208);
		        player.getOutStream().writeWordBigEndian_dup(id);
				player.flushOutStream();
			}
		}
	}

	private int interfaceid = 1;

	public boolean interface_components(int list, int string) {
		if (string < 1) {
			string = 1;
		}
		if (list <= 0) {
			return false;
		}
		if ((((freeSlots() >= 1) || player.getItems().playerHasItem(list, 1))
				&& ItemDefinition.forId(list).isStackable()
				|| ((freeSlots() > 0) && !ItemDefinition.forId(list).isStackable()))) {
			for (int i = 0; i < player.playerItems.length; i++) {
				if ((player.playerItems[i] == (list + 1)) && ItemDefinition.forId(list).isStackable()
						&& (player.playerItems[i] > 0)) {
					player.playerItems[i] = (list + 1);
					if (((player.playerItemsN[i] + string) < LIST_AMOUNT) && ((player.playerItemsN[i] + string) > -1)) {
						player.playerItemsN[i] += string;
					} else {
						player.playerItemsN[i] = LIST_AMOUNT;
					}
					if (player.getOutStream() != null && player != null) {
						player.getOutStream().createFrameVarSizeWord(34);
						player.getOutStream().writeWord(3214);
						player.getOutStream().writeByte(i);
						player.getOutStream().writeWord(player.playerItems[i]);
						if (player.playerItemsN[i] > 254) {
							player.getOutStream().writeByte(255);
							player.getOutStream().writeDWord(player.playerItemsN[i]);
						} else {
							player.getOutStream().writeByte(player.playerItemsN[i]);
						}
						player.getOutStream().endFrameVarSizeWord();
						player.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] <= 0) {
					player.playerItems[i] = list + 1;
					if ((string < LIST_AMOUNT) && (string > -1)) {
						player.playerItemsN[i] = 1;
						if (string > 1) {
							player.getItems().addItem(list, string - 1);
							return true;
						}
					} else {
						player.playerItemsN[i] = LIST_AMOUNT;
					}
					player.getItems().resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		}
		player.getItems().resetItems(3214);
		player.sendMessage("Not enough space in your inventory.");
		return false;
	}

	public void shakeScreen(int verticleAmount, int verticleSpeed, int horizontalAmount, int horizontalSpeed) {
		if (player != null && player.getOutStream() != null) {
			player.outStream.createFrame(35);
			player.outStream.writeByte(verticleAmount);
			player.outStream.writeByte(verticleSpeed);
			player.outStream.writeByte(horizontalAmount);
			player.outStream.writeByte(horizontalSpeed);
		}
	}

	public void resetShaking() {
		shakeScreen(1, 1, 1, 1);
	}

	public int mapStatus = 0;

	public void sendFrame99(int state) { // used for disabling map
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			if (mapStatus != state) {
				mapStatus = state;
				player.getOutStream().createFrame(99);
				player.getOutStream().writeByte(state);
				player.flushOutStream();
			}

		}
	}

	public void interface_components2(int Interface) {
		if (player.Interface(Interface)) {
			interface_components(Interface, interfaceid);
		}
	}

	public void sendCrashFrame() {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(123);
			player.flushOutStream();
		}
	}

	/**
	 * Reseting animations for everyone
	 **/

	public void frame1() {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player person = PlayerHandler.players[i];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (player.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getPA().requestUpdates();
						}
					}
				}

			}
		}
	}

	/**
	 * Creating projectile
	 **/
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC((y - (player.getMapRegionY() * 8)) - 2);
			player.getOutStream().writeByteC((x - (player.getMapRegionX() * 8)) - 3);
			player.getOutStream().createFrame(117);
			player.getOutStream().writeByte(angle);
			player.getOutStream().writeByte(offY);
			player.getOutStream().writeByte(offX);
			player.getOutStream().writeWord(lockon);
			player.getOutStream().writeWord(gfxMoving);
			player.getOutStream().writeByte(startHeight);
			player.getOutStream().writeByte(endHeight);
			player.getOutStream().writeWord(time);
			player.getOutStream().writeWord(speed);
			player.getOutStream().writeByte(16);
			player.getOutStream().writeByte(64);
			player.flushOutStream();

		}
	}

	private ArrayList<int[]> coreCoordinates = new ArrayList<>(3);

	public void handleCorpAttack(NPC npc, Player player) {
		if (player == null) {
			return;
		}
		for (int[] point : coreCoordinates) {
			int nX = npc.absX + 2;
			int nY = npc.absY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;
			player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, 5, 314, 31, 0, -1, 5);
		}
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				int pX = player.getX();
				int pY = player.getY();
				int oX = player.getX();
				int oY = player.getY();
				int offX = (pY - oY);
				int offY = (pX - oX);
				/*
				 * player.getPA().createPlayersProjectile(oX, oY, offX2-2,
				 * offY2+1, 40, getProjectileSpeed(npc.index), 315, 31, 0, -1,
				 * 5); player.getPA().createPlayersProjectile(oX, oY, offX2,
				 * offY2, 40, getProjectileSpeed(npc.index), 315, 31, 0, -1, 5);
				 * player.getPA().createPlayersProjectile(oX, oY, offX2, offY2,
				 * 40, getProjectileSpeed(npc.index), 315, 31, 0, -1, 5);
				 */
				player.getPA().createPlayersProjectile(pX, pY, offX - 2, offY + 1, 50, 5, 315 /* GFX */, 25, 10,
						player.oldNpcIndex + 1, 45);
				player.sendMessage("We made it =S");
				container.stop();
			}

		}, 4);
	}

	public void createProjectile4(int x, int y, int angle, int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC((y - (player.getMapRegionY() * 8)) - 2);
			player.getOutStream().writeByteC((x - (player.getMapRegionX() * 8)) - 3);
			player.getOutStream().createFrame(117);
			player.getOutStream().writeByte(angle);
			player.getOutStream().writeWord(lockon);
			player.getOutStream().writeWord(gfxMoving);
			player.getOutStream().writeByte(startHeight);
			player.getOutStream().writeByte(endHeight);
			player.getOutStream().writeWord(time);
			player.getOutStream().writeWord(speed);
			player.getOutStream().writeByte(16);
			player.getOutStream().writeByte(64);
			player.flushOutStream();

		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC((y - (player.getMapRegionY() * 8)) - 2);
			player.getOutStream().writeByteC((x - (player.getMapRegionX() * 8)) - 3);
			player.getOutStream().createFrame(117);
			player.getOutStream().writeByte(angle);
			player.getOutStream().writeByte(offY);
			player.getOutStream().writeByte(offX);
			player.getOutStream().writeWord(lockon);
			player.getOutStream().writeWord(gfxMoving);
			player.getOutStream().writeByte(startHeight);
			player.getOutStream().writeByte(endHeight);
			player.getOutStream().writeWord(time);
			player.getOutStream().writeWord(speed);
			player.getOutStream().writeByte(slope);
			player.getOutStream().writeByte(64);
			player.flushOutStream();
		}

	}

	public void createProjectile3(int casterY, int casterX, int offsetY, int offsetX, int gfxMoving, int StartHeight,
			int endHeight, int speed, int AtkIndex) {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player p = PlayerHandler.players[i];
				if (p.WithinDistance(player.absX, player.absY, p.absX, p.absY, 60)) {
					if (p.heightLevel == player.heightLevel) {
						if (PlayerHandler.players[i] != null && !PlayerHandler.players[i].disconnected) {
							p.outStream.createFrame(85);
							p.outStream.writeByteC((casterY - (p.mapRegionY * 8)) - 2);
							p.outStream.writeByteC((casterX - (p.mapRegionX * 8)) - 3);
							p.outStream.createFrame(117);
							p.outStream.writeByte(50);
							p.outStream.writeByte(offsetY);
							p.outStream.writeByte(offsetX);
							p.outStream.writeWord(AtkIndex);
							p.outStream.writeWord(gfxMoving);
							p.outStream.writeByte(StartHeight);
							p.outStream.writeByte(endHeight);
							p.outStream.writeWord(51);
							p.outStream.writeWord(speed);
							p.outStream.writeByte(16);
							p.outStream.writeByte(64);
						}
					}
				}
			}
		}
	}
	
	public void createProjectile(int x, int y, int offX, int offY, int angle,
			int speed, int gfxMoving, int startHeight, int endHeight,
			int lockon, int time, int slope, int lol) {

		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC((y - (player.getMapRegionY() * 8)) - 2);
			player.getOutStream().writeByteC((x - (player.getMapRegionX() * 8)) - 3);
			player.getOutStream().createFrame(117);
			player.getOutStream().writeByte(angle);
			player.getOutStream().writeByte(offY);
			player.getOutStream().writeByte(offX);
			player.getOutStream().writeWord(lockon);
			player.getOutStream().writeWord(gfxMoving);
			player.getOutStream().writeByte(startHeight);
			player.getOutStream().writeByte(endHeight);
			player.getOutStream().writeWord(time);
			player.getOutStream().writeWord(speed);
			player.getOutStream().writeByte(slope);
			player.getOutStream().writeByte(lol);
			player.flushOutStream();
		}
	}
	
	public void createPlayersProjectile(int x, int y, int offX, int offY,
			int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time, int slope, int lol) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.heightLevel == player.heightLevel)
								person.getPA().createProjectile(x, y, offX,
										offY, angle, speed, gfxMoving,
										startHeight, endHeight, lockon, time,
										slope, lol);
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = p;
				if (person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25) {
						if (p.heightLevel == player.heightLevel)
							person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time);
					}
				}
			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = p;
				if (person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25 && player.heightLevel == p.heightLevel) {
						person.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
								endHeight, lockon, time, slope);
					}
				}
			}

		}
	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(y - (player.getMapRegionY() * 8));
			player.getOutStream().writeByteC(x - (player.getMapRegionX() * 8));
			player.getOutStream().createFrame(4);
			player.getOutStream().writeByte(0);
			player.getOutStream().writeWord(id);
			player.getOutStream().writeByte(height);
			player.getOutStream().writeWord(time);
			player.flushOutStream();
		}

	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = p;
				if (person.getOutStream() != null) {
					if (person.distanceToPoint(x, y) <= 25) {
						person.getPA().stillGfx(id, x, y, height, time);
					}
				}
			}
		}
	}

	public void createPlayersObjectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		try {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(Y - (player.mapRegionY * 8));
			player.getOutStream().writeByteC(X - (player.mapRegionX * 8));
			int x = 0;
			int y = 0;
			player.getOutStream().createFrame(160);
			player.getOutStream().writeByteS(((x & 7) << 4) + (y & 7));// tiles
																		// away
																		// -
																		// could
																		// just
																		// send
																		// 0
			player.getOutStream().writeByteS((tileObjectType << 2) + (orientation & 3));
			player.getOutStream().writeWordA(animationID);// animation id
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(objectY - (player.getMapRegionY() * 8));
			player.getOutStream().writeByteC(objectX - (player.getMapRegionX() * 8));
			player.getOutStream().createFrame(101);
			player.getOutStream().writeByteC((objectType << 2) + (face & 3));
			player.getOutStream().writeByte(0);
			if (objectId != -1) { // removing
				player.getOutStream().createFrame(151);
				player.getOutStream().writeByteS(0);
				player.getOutStream().writeWordBigEndian(objectId);
				player.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			player.flushOutStream();
		}
	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		Region.addWorldObject(objectId, objectX, objectY, player.heightLevel, face); // height
		// level
		// should
		// be
		// a
		// param
		// :s
		if (player.distanceToPoint(objectX, objectY) > 60)
			return;
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(objectY - (player.getMapRegionY() * 8));
			player.getOutStream().writeByteC(objectX - (player.getMapRegionX() * 8));
			player.getOutStream().createFrame(101);
			player.getOutStream().writeByteC((objectType << 2) + (face & 3));
			player.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				player.getOutStream().createFrame(151);
				player.getOutStream().writeByteS(0);
				player.getOutStream().writeWordBigEndian(objectId);
				player.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			player.flushOutStream();
		}

	}

	/**
	 * Show option, attack, trade, follow etc
	 **/
	public String optionType = "null";

	@SuppressWarnings("unused")
	public void showOption(int i, int l, String s, int a) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				player.getOutStream().createFrameVarSize(104);
				player.getOutStream().writeByteC(i);
				player.getOutStream().writeByteA(l);
				player.getOutStream().writeString(s);
				player.getOutStream().endFrameVarSize();
				player.flushOutStream();
			}

		}
	}

	/**
	 * Open bank
	 **/
	public void sendFrame34a(int frame, int item, int slot, int amount) {
		player.outStream.createFrameVarSizeWord(34);
		player.outStream.writeWord(frame);
		player.outStream.writeByte(slot);
		player.outStream.writeWord(item + 1);
		player.outStream.writeByte(255);
		player.outStream.writeDWord(amount);
		player.outStream.endFrameVarSizeWord();
	}

	public int[] VERY_RARE_ITEMS = { 6585, 12002, 11286, 13233, 11838, 11785, 11824, 11791, 11828, 11826, 11830, 11810,
			11812, 11834, 11832, 12823, 12827, 12932, 12932, };
	public int[] EXTREMELY_RARE_ITEMS = { 12819, 11908, 12004, 13576, 11828, 11826, 11830, 11812, 11834, 11832,
			12819, };
	public int[] RARE_ITEMS = { 12807, 12806, 12007, 11907, 12927, 12922, 11822, 11820, 11818, 11816, 11824, 11812,
			6739, 6733, 6731, 6737, 6735, 11920, 12605, 11286, 12603, 10887, 4151, 6568, 6524, 6528, 11235, 7158, 10581,
			13092, 13233, 13231, 13229, 13227, 3140, 8901, 11840, 12002, 4087, 4585, 3204, 11335, 11838, 11990, };

	public void openUpBank() {
		resetVariables();
		if (player.getBankPin().isLocked() && player.getBankPin().getPin().trim().length() > 0) {
			player.getBankPin().open(2);
			player.isBanking = false;
			return;
		}
		if (player.takeAsNote)
			sendFrame36(115, 1);
		else
			sendFrame36(115, 0);

		if (player.inWild() && !(player.getRights().isBetween(2, 3))) {
			player.sendMessage("You can't bank in the wilderness!");
			return;
		}

		if (player.fishTourneySession != null && player.fishTourneySession.running) {
			player.sendMessage("You can't bank during a fishing tourney!");
			return;
		} else if (player.hungerGames) {
			player.sendMessage("You can't bank during hunger games!");
			return;
		}

		if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
			Server.getMultiplayerSessionListener().finish(player, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			return;
		}
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
			if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
			} else {
				player.sendMessage("You cannot bank whilst dueling.");
				return;
			}
		}
		if (player.getBank().getBankSearch().isSearching()) {
			player.getBank().getBankSearch().reset();
		}
		player.getPA().sendFrame126("Search", 58063);
		if (player.getOutStream() != null && player != null) {
			player.isBanking = true;
			player.getItems().resetItems(5064);
			player.getItems().resetBank();
			player.getItems().resetTempItems();
			player.getOutStream().createFrame(248);
			player.getOutStream().writeWordA(5292);// ok perfect
			player.getOutStream().writeWord(5063);
			player.flushOutStream();
		}
	}

	public boolean viewingOtherBank;
	BankTab[] backupTabs = new BankTab[9];

	public void resetOtherBank() {
		for (int i = 0; i < backupTabs.length; i++)
			player.getBank().setBankTab(i, backupTabs[i]);
		viewingOtherBank = false;
		removeAllWindows();
		player.isBanking = false;
		player.getBank().setCurrentBankTab(player.getBank().getBankTab()[0]);
		player.getItems().resetBank();
		player.sendMessage("You are no longer viewing another players bank.");
	}

	public void openOtherBank(Player otherPlayer) {
		if (otherPlayer == null)
			return;

		if (player.getPA().viewingOtherBank) {
			player.getPA().resetOtherBank();
			return;
		}
		if (otherPlayer.getPA().viewingOtherBank) {
			player.sendMessage("That player is viewing another players bank, please wait.");
			return;
		}
		for (int i = 0; i < backupTabs.length; i++)
			backupTabs[i] = player.getBank().getBankTab(i);
		for (int i = 0; i < otherPlayer.getBank().getBankTab().length; i++)
			player.getBank().setBankTab(i, otherPlayer.getBank().getBankTab(i));
		player.getBank().setCurrentBankTab(player.getBank().getBankTab(0));
		viewingOtherBank = true;
		openUpBank();
	}

	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId, int healType) {

		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
				MultiplayerSessionType.DUEL);
		if (!Objects.isNull(session)) {
			if (session.getRules().contains(Rule.NO_DRINKS)) {
				player.sendMessage("Drinks have been disabled for this duel.");
				return;
			}
		}
		if (!player.isDead && player.foodDelay.elapsed(2000)) {
			if (player.getItems().playerHasItem(itemId, 1, itemSlot)) {
				player.sendMessage("You drink the " + player.getItems().getItemName(itemId).toLowerCase() + ".");
				player.foodDelay.reset();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				player.animation(0x33D);
				player.getItems().deleteItem(itemId, itemSlot, 1);
				player.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}

	/**
	 * Magic on items
	 **/

	@SuppressWarnings("unused")
	public void magicOnItems(Player c, int slot, int itemId, int spellId) {

		switch (spellId) {
		case 1162: // low alch
			/*
			 * if (System.currentTimeMillis() - c.alchDelay > 1000) {
			 * 
			 * if (!c.getCombat().checkMagicReqs(49)) { break; } if
			 * (!c.getItems().playerHasItem(itemId, 1, slot) || itemId == 995) {
			 * return; } c.getItems().deleteItem(itemId, slot, 1);
			 * c.getItems().addItem(995, c.getShops().getItemShopValue(itemId) /
			 * 3); c.startAnimation(c.MAGIC_SPELLS[49][2]);
			 * c.gfx100(c.MAGIC_SPELLS[49][3]); c.alchDelay =
			 * System.currentTimeMillis(); sendFrame106(6);
			 * addSkillXP(c.MAGIC_SPELLS[49][7] * Config.MAGIC_EXP_RATE, 6);
			 * refreshSkill(6);
			 * 
			 * }
			 */
			break;

		case 1178: // high alch
			if (System.currentTimeMillis() - c.alchDelay > 2000) {
				if (!c.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (!c.getItems().playerHasItem(itemId, 1, slot) || itemId == 995 || itemId == 995
						|| c.getItems().freeSlots() < 1) {
					return;
				}
				c.getItems().deleteItem(itemId, slot, 1);
				c.getItems().addItem(995, 1);
				c.animation(c.MAGIC_SPELLS[50][2]);
				c.gfx100(c.MAGIC_SPELLS[50][3]);
				c.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(c.MAGIC_SPELLS[50][7] * 1 / 1, 6);
				refreshSkill(6);
			}
			break;

		case 1155: // Lvl-1 enchant sapphire
		case 1165: // Lvl-2 enchant emerald
		case 1176: // Lvl-3 enchant ruby
		case 1180: // Lvl-4 enchant diamond
		case 1187: // Lvl-5 enchant dragonstone
		case 6003: // Lvl-6 enchant onyx
			if (Jewellery.enchant(c, itemId)) {
				return;
			}
			break;
		}
	}

	public int[][] boltData = { { 1155, 879, 9, 9236 }, { 1155, 9337, 17, 9240 }, { 1165, 9335, 19, 9237 },
			{ 1165, 880, 29, 9238 }, { 1165, 9338, 37, 9241 }, { 1176, 9336, 39, 9239 }, { 1176, 9339, 59, 9242 },
			{ 1180, 9340, 67, 9243 }, { 1187, 9341, 78, 9244 }, { 6003, 9342, 97, 9245 } };

	public int[][] runeData = { { 1155, 555, 1, -1, -1 }, { 1165, 556, 3, -1, -1 }, { 1176, 554, 5, -1, -1 },
			{ 1180, 557, 10, -1, -1 }, { 1187, 555, 15, 557, 15 }, { 6003, 554, 20, 557, 20 } };

	public void enchantBolt(int spell, int bolt, int amount) {
		for (int i = 0; i < boltData.length; i++) {
			if (spell == boltData[i][0]) {
				if (bolt == boltData[i][1]) {
					for (int a = 0; a < runeData.length; a++) {
						if (spell == runeData[a][0]) {
							if (!player.getItems().playerHasItem(564, 1)
									|| !player.getItems().playerHasItem(runeData[a][1], runeData[a][2])
									|| (!player.getItems().playerHasItem(runeData[a][3], runeData[a][4])
											&& runeData[a][3] > 0)) {
								player.sendMessage("You do not have the required runes to cast this spell!");
								return;
							}
							player.getItems().deleteItem(564, player.getItems().getItemSlot(564), 1);
							player.getItems().deleteItem(runeData[a][1], player.getItems().getItemSlot(runeData[a][1]),
									runeData[a][2]);
							if (runeData[a][3] > 0)
								player.getItems().deleteItem(runeData[a][3],
										player.getItems().getItemSlot(runeData[a][3]), runeData[a][4]);
						}
					}
					if (!player.getItems().playerHasItem(boltData[i][1], amount))
						amount = player.getItems().getItemAmount(bolt);
					player.getItems().deleteItem(boltData[i][1], player.getItems().getItemSlot(boltData[i][1]), amount);
					player.getPA().addSkillXP(boltData[i][2] * amount, 6);
					player.getItems().addItem(boltData[i][3], amount);
					player.getPA().sendFrame106(6);
					return;
				}
			}
		}
	}

	/**
	 * Dieing
	 **/
	public void yell(String msg) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				c2.sendMessage(msg);
			}
		}
	}
	private Stopwatch stopwatch = Stopwatch.createUnstarted();
	public void applyDead() {
		player.isFullHelm = Item.isFullHelm(player.playerEquipment[player.playerHat]);
		player.isFullMask = Item.isFullMask(player.playerEquipment[player.playerHat]);
		player.isFullBody = Item.isFullBody(player.playerEquipment[player.playerChest]);
		player.getPA().requestUpdates();
		player.respawnTimer = 15;
		player.isDead = false;
		player.freezeTimer = 1;
		player.recoilHits = 0;
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession)
				&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
			duelSession = null;
		}
		if (Objects.isNull(duelSession)) {
			if (player.getItems().playerHasItem(12926) || player.getItems().isWearingItem(12926)) {
				player.setToxicBlowpipeCharge(0);
				player.setToxicBlowpipeAmmo(0);
				player.setToxicBlowpipeAmmoAmount(0);
				player.sendMessage("<col=255>You have lost your blowpipe's charges and ammo!");
			}
			if (player.getItems().playerHasItem(12931) || player.getItems().isWearingItem(12931)) {
				player.getItems().removeItem(-1, 0);
				player.getItems().deleteItem(12931, 1);
				player.getItems().addItem(12929, 1);
				//player.setSerpentineHelmCharge(0);
				player.sendMessage("<col=255>You have lost your serpentine helm's charges!");
			}
//			if (player.getItems().playerHasItem(12904) || player.getItems().isWearingItem(12904)) {
//				player.setToxicStaffOfDeadCharge(0);
//				player.sendMessage("<col=255>You have lost your staffs charges!");
//			}
			player.setKiller(player.getPlayerKiller());
			Player killer = PlayerHandler.getPlayer(player.getKiller());
			if (player.getKiller() != null && killer != null && !player.isKilledByZombie()) {
				player.killerId = killer.index;
				if (player.killerId != player.index)
					if (player.inWild() || player.inCamWild()) {
						if (killer.connectedFrom.equalsIgnoreCase(player.connectedFrom)) {
						killer.sendMessage("You cannot be rewarded for killing someone from your own network!");
						return;
						}
						player.DC++;
						killer.KC++;
						killer.killStreak += 1;
						killer.getStreak().checkPlayerKilleds();
						killer.getStreak().checkKillStreak();
						killer.getStreak().drawSkulls();
						if (player.killStreak > player.highestKillStreak) {
							player.highestKillStreak = player.killStreak;
						}
						player.getKillstreak().resetAll();
						if (player.killStreak >= 5) {
							int bounty = (player.killStreak * 2 + 8);
							killer.sendMessage("Your bounty reward has been sent to your bank.");
							killer.pkp += bounty;
							yell("[@blu@STREAK@bla@] @blu@" + killer.playerName + "@bla@ has ended @blu@"
									+ player.playerName + "'s@bla@ killing streak of @blu@" + player.killStreak
									+ "@bla@, and recieved @blu@" + bounty + " @bla@PK Points!");
							player.killStreak -= player.killStreak;
							player.bhSkull = -1;
							player.getPA().requestUpdates();
						} else if(player.killStreak < 5) {
							if (Config.doublePKPointsWeekend) {
							killer.pkp += 10;
							player.killStreak -= player.killStreak;
							killer.sendMessage("You defeated <col=255>" + Misc.optimizeText(player.playerName) + "</col>.");
							} else if (!Config.doublePKPointsWeekend) {
							killer.pkp += 5;
							player.killStreak -= player.killStreak;
							killer.sendMessage("You defeated <col=255>" + Misc.optimizeText(player.playerName) + "</col>.");
							}
						}
						killer.specAmount = 10.0;
						killer.getItems().updateSpecialBar();
						killer.sendMessage("Your special attack has been restored.");
						killer.getItems().addSpecialBar(killer.playerEquipment[Player.playerWeapon]);
						player.killStreak -= player.killStreak;
					/*	killer.getKillstreak().increase(Killstreak.Type.ROGUE);
						killer.getBH().setCurrentRogueKills(killer.getBH().getCurrentRogueKills() + 1);
						killer.getBH().setTotalRogueKills(killer.getBH().getTotalRogueKills() + 1);
						if (killer.getBH().getCurrentRogueKills() > killer.getBH().getRecordRogueKills()) {
							killer.getBH().setRecordRogueKills(killer.getBH().getCurrentRogueKills());
						}*/
						player.getPA().loadQuests();
						killer.getPA().loadQuests();
					}
				player.playerKilled = player.index;
				PlayerSave.saveGame(player);
				PlayerSave.saveGame(killer);
			}
			if (player.inFunPk2()) {
				if (killer.inFunPk()) {
					killer.sendMessage("You defeated <col=255>" + Misc.optimizeText(player.playerName) + "</col>.");
				}
			}
			player.sendMessage("Oh dear you are dead!");
			player.setPoisonDamage((byte) 0);
			player.setVenomDamage((byte) 0);
		}
		if (Config.BOUNTY_HUNTER_ACTIVE) {
			player.getBH().setCurrentHunterKills(0);
			player.getBH().setCurrentRogueKills(0);
			player.getBH().updateStatisticsUI();
			player.getBH().updateTargetUI();
		}
		player.face(null);
		player.stopMovement();
		if (duelSession != null && duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERACTION) {
			if (!duelSession.getWinner().isPresent()) {
				player.sendMessage("You have lost the duel!");
				Player opponent = duelSession.getOther(player);
				opponent.logoutDelay.reset();
				if (!duelSession.getWinner().isPresent()) {
					duelSession.setWinner(opponent);
				}
				PlayerSave.saveGame(opponent);
			} else {
				player.sendMessage("Congratulations, you have won the duel.");
			}
			player.logoutDelay.reset();
		}
		PlayerSave.saveGame(player);
		player.specAmount = 10;
		player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
		player.lastVeng = 0;
		player.animation(2304);
		player.vengOn = false;
		resetFollowers();
		player.attackTimer = 10;
		player.tradeResetNeeded = true;
		player.doubleHit = false;

		removeAllWindows();
		closeAllWindows();
	}
	// }

	public void resetTb() {
		player.teleBlockLength = 0;
		player.teleBlockDelay = 0;
	}

	public void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].followId == player.index) {
					Player c = PlayerHandler.players[j];
					c.getPA().resetFollow();
				}
			}
		}
	}

	public void giveLife() {
		player.getLoot().handleLootbagDeath();
		player.isFullHelm = Item.isFullHelm(player.playerEquipment[player.playerHat]);
		player.isFullMask = Item.isFullMask(player.playerEquipment[player.playerHat]);
		player.isFullBody = Item.isFullBody(player.playerEquipment[player.playerChest]);
		player.isDead = false;
		player.face(null);
		player.freezeTimer = 0;
		player.hasInteracted = 0;
		player.getPA().sendFrame126("freezetimer:-2", 1810);
		if (!player.inDuelArena() && !player.inClanWars() && !Boundary.isIn(player, Boundary.DUEL_ARENAS)
				&& !Boundary.isIn(player, Boundary.FIGHT_CAVE) && !Boundary.isIn(player, Cerberus.WEST)
				&& !Boundary.isIn(player, Cerberus.NORTH) && !Boundary.isIn(player, Cerberus.EAST)
				&& !Boundary.isIn(player, Kraken.BOUNDARY) && !Boundary.isIn(player, Zulrah.BOUNDARY)
				&& !Boundary.isIn(player, Armadyl.BOUNDARY) && !Boundary.isIn(player, Bandos.BOUNDARY)
				&& !Boundary.isIn(player, Saradomin.BOUNDARY) && !Boundary.isIn(player, Zamorak.BOUNDARY)
				&& !Boundary.isIn(player, Kalphite.BOUNDARY)) {
			if (!Boundary.isIn(player, PestControl.GAME_BOUNDARY) && !player.inSafemode() && !player.isKilledByZombie()
					&& !player.hungerGames && !player.inFunPk() && !player.inFunPk2()
					&& (player.fishTourneySession == null || !player.fishTourneySession.running)) {
				if(!player.getRights().isOwner()) { 
				for (int itemId : Config.DROP_AND_DELETE_ON_DEATH) {
					if (player.getItems().isWearingItem(itemId)) {
						int slot = player.getItems().getItemSlot(itemId);
						if (slot != -1) {
							player.getItems().removeItem(itemId, slot);
						}
					}
					if (player.getItems().playerHasItem(itemId)) {
						player.getItems().deleteItem2(itemId, player.getItems().getItemAmount(itemId));
					}
				}

				ArrayList<List<GameItem>> items = player.getItems().getItemsKeptOnDeath();
				List<GameItem> lostItems = items.get(0);
				List<GameItem> keptItems = items.get(1);

				player.getItems().dropItems(lostItems);
				player.getItems().deleteAllItems();

				for (GameItem item : keptItems) {
					player.getItems().addItem(item.getId(), item.getAmount());
				}
				} else {
					player.sendMessage("You don't lose items!");
				}
				Player killer = PlayerHandler.getPlayer(player.getKiller());
				if (player.getItems().isWearingItem(12931)
						&& player.getItems().getWornItemSlot(12931) == player.playerHat
						|| player.getItems().playerHasItem(12931)) {
					if (player.getSerpentineHelmCharge() > 0) {
						Server.itemHandler.createGroundItem(killer == null ? player : killer, 12934, player.getX(),
								player.getY(), player.heightLevel, player.getSerpentineHelmCharge(),
								killer == null ? player.index : killer.index);
						Server.itemHandler.createGroundItem(killer == null ? player : killer, 12929, player.getX(),
								player.getY(), player.heightLevel, 1, killer == null ? player.index : killer.index);
						if (player.getItems().isWearingItem(12931)
								&& player.getItems().getWornItemSlot(12931) == player.playerHat) {
							player.getItems().wearItem(-1, 0, player.playerHat);
						} else {
							player.getItems().deleteItem2(12931, 1);
						}
						player.sendMessage(
								"The serpentine helm has been dropped on the floor. You lost the helm and it's charge.");
						player.setSerpentineHelmCharge(0);
					}
				}
				if (player.getItems().isWearingItem(12904)
						&& player.getItems().getWornItemSlot(12904) == Player.playerWeapon
						|| player.getItems().playerHasItem(12904)) {
					if (player.staffOfDeadCharge > 0) {
						Server.itemHandler.createGroundItem(killer == null ? player : killer, 12934, player.getX(),
								player.getY(), player.heightLevel, player.staffOfDeadCharge,
								killer == null ? player.index : killer.index);
						Server.itemHandler.createGroundItem(killer == null ? player : killer, 12904, player.getX(),
								player.getY(), player.heightLevel, 1, killer == null ? player.index : killer.index);
						if (player.getItems().isWearingItem(12904)
								&& player.getItems().getWornItemSlot(12904) == Player.playerWeapon) {
							player.getItems().wearItem(-1, 0, Player.playerWeapon);
						} else {
							player.getItems().deleteItem2(12904, 1);
						}
						player.sendMessage(
								"The toxic staff of the dead has been dropped on the floor. You lost the staff and its charge.");
						player.staffOfDeadCharge = 0;
					}
				}
				if (player.getItems().isWearingItem(12926)
						&& player.getItems().getWornItemSlot(12926) == Player.playerWeapon
						|| player.getItems().playerHasItem(12926)) {
					if (player.getToxicBlowpipeAmmo() > 0 && player.getToxicBlowpipeAmmoAmount() > 0
							&& player.getToxicBlowpipeCharge() > 0) {
						Server.itemHandler.createGroundItem(killer == null ? player : killer, 12924, player.getX(),
								player.getY(), player.heightLevel, 1, killer == null ? player.index : killer.index);
						Server.itemHandler.createGroundItem(killer == null ? player : killer, 12934, player.getX(),
								player.getY(), player.heightLevel, player.getToxicBlowpipeCharge(),
								killer == null ? player.index : killer.index);
						Server.itemHandler.createGroundItem(killer == null ? player : killer,
								player.getToxicBlowpipeAmmo(), player.getX(), player.getY(), player.heightLevel,
								player.getToxicBlowpipeAmmoAmount(), killer == null ? player.index : killer.index);
						if (player.getItems().isWearingItem(12926)
								&& player.getItems().getWornItemSlot(12926) == Player.playerWeapon) {
							player.getItems().wearItem(-1, 0, Player.playerWeapon);
						} else {
							player.getItems().deleteItem2(12926, 1);
						}
						player.setToxicBlowpipeAmmo(0);
						player.setToxicBlowpipeAmmoAmount(0);
						player.setToxicBlowpipeCharge(0);
						player.sendMessage(
								"Your blowpipe has been dropped on the floor. You lost the ammo, pipe, and charge.");
					}
				}
				/*
				 * } else if (c.inPits) {
				 * Server.fightPits.removePlayerFromPits(c.playerId);
				 * c.pitsStatus = 1;
				 */
			} else if (Boundary.isIn(player, Boundary.PEST_CONTROL_AREA)) {
				player.getPA().movePlayer(2657, 2639, 0);
			} else if (player.fishTourneySession != null && player.fishTourneySession.running) {
				player.getDH().sendNpcChat1("Looks like you lost! Better luck next time!", 4070, "Sinister Stranger");
				player.nextChat = -1;
			}
		}
		player.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			player.playerLevel[i] = getLevelForXP(player.playerXP[i]);
			player.getPA().refreshSkill(i);
		}
		/*
		 * if (Boundary.isIn(player, Cerberus.BOUNDARY)) {
		 * player.getPA().movePlayer(3092, 3494, 0); InstancedArea instance =
		 * player.getCerberusEvent().getInstancedCerberus(); if (instance !=
		 * null) { InstancedAreaManager.getSingleton().disposeOf(instance);
		 * player.getCerberusEvent().stop(); } } else
		 */
		if (Boundary.isIn(player, Kalphite.BOUNDARY)) {
			player.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance7 = player.getKalphite().getInstancedKalphite();
			if (instance7 != null) {
				InstancedAreaManager.getSingleton().disposeOf(player.getKalphite().getInstancedKalphite());
				player.KALPHITE_INSTANCE = false;
				player.getKalphite().stop();
			}
		} else if (Boundary.isIn(player, Armadyl.BOUNDARY)) {
			player.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance6 = player.getArmadyl().getInstancedArmadyl();
			if (instance6 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance6);
				player.ARMADYL_INSTANCE = false;
				player.getArmadyl().stop();
			}
		} else if (Boundary.isIn(player, Bandos.BOUNDARY)) {
			player.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance1 = player.getBandos().getInstancedBandos();
			if (instance1 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance1);
				player.BANDOS_INSTANCE = false;
				player.getBandos().stop();
			}
		} else if (Boundary.isIn(player, Saradomin.BOUNDARY)) {
			player.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance2 = player.getSaradomin().getInstancedSaradomin();
			if (instance2 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance2);
				player.SARADOMIN_INSTANCE = false;
				player.getSaradomin().stop();
			}
		} else if (Boundary.isIn(player, Zamorak.BOUNDARY)) {
			player.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance3 = player.getZamorak().getInstancedZamorak();
			if (instance3 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance3);
				player.ZAMORAK_INSTANCE = false;
				player.getZamorak().stop();
			}
		} else if (Boundary.isIn(player, Kraken.BOUNDARY)) {
			player.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance4 = player.getKraken().getInstancedKraken();
			if (instance4 != null) {
				InstancedAreaManager.getSingleton().disposeOf(player.getKraken().getInstancedKraken());
				player.getKraken().stop();
			}
		} else if (Boundary.isIn(player, PestControl.GAME_BOUNDARY)) {
			player.getPA().movePlayer(2656 + Misc.random(2), 2614 - Misc.random(3), 0);
		} else if (Boundary.isIn(player, Zulrah.BOUNDARY)) {
			player.getPA().movePlayer(3092, 3494, 0);
			InstancedArea instance5 = player.getZulrahEvent().getInstancedZulrah();
			if (instance5 != null) {
				InstancedAreaManager.getSingleton().disposeOf(instance5);
				player.getZulrahEvent().DISPOSE_EVENT();
			}
			/*
			 * c.getLostItems().store(); c.talkingNpc = 2040;
			 * c.getDH().sendNpcChat("You have lost!",
			 * "I'll give you your items back for 5 PKP.");
			 */
		} else if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession)
					&& duelSession.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERACTION) {
				Player opponent = duelSession.getWinner().get();
				if (opponent != null) {
					opponent.getPA().createPlayerHints(10, -1);
					duelSession.finish(MultiplayerSessionFinalizeType.GIVE_ITEMS);
				}
			}
		} else if (Boundary.isIn(player, Boundary.FIGHT_CAVE)) {
			player.getFightCave().handleDeath();
		} else if ((player.fishTourneySession != null && player.fishTourneySession.running)) {
			player.sendMessage("You survived " + player.fishTourneySession.rounds + " and earn <col=255>"
					+ (player.fishTourneySession.rounds / 2) + "</col> fishing tourney points.");
			player.fishingTourneyPoints += (player.fishTourneySession.rounds / 2);
			player.fishTourneySession.removePlayer(player);
			player.getPA().movePlayer(2642, 3441, 0);
		} else if (player.hungerGames) {
			HungerManager.getSingleton().exitGame(player);
			player.hungerGames = false;
		} else {
			if (player.inFunPk()) {
				movePlayer(3328, 4754, 0);
			} else {
				movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			}
			if (player.inFunPk2()) {
				movePlayer(3179, 9758, 0);
			} else {
				movePlayer(Config.RESPAWN_X, Config.RESPAWN_Y, 0);
			}
			player.isSkulled = false;
			player.skullTimer = 0;
			player.attackedPlayers.clear();
			removeAllWindows();
			closeAllWindows();
		}
		PlayerSave.saveGame(player);
		player.resetDamageReceived();
		player.getCombat().resetPlayerAttack();
		resetAnimation();
		player.animation(65535);
		frame1();
		resetTb();
		player.isSkulled = false;
		player.attackedPlayers.clear();
		player.headIconPk = -1;
		player.skullTimer = -1;
		requestUpdates();
		player.tradeResetNeeded = true;
		player.setKiller(null);
		player.setKilledByZombie(false);
	}

	/**
	 * Location change for digging, levers etc
	 **/

	public void changeLocation() {
		switch (player.newLocation) {
		case 1:
			sendFrame99(2);
			movePlayer(3578, 9706, -1);
			break;
		case 2:
			sendFrame99(2);
			movePlayer(3568, 9683, -1);
			break;
		case 3:
			sendFrame99(2);
			movePlayer(3557, 9703, -1);
			break;
		case 4:
			sendFrame99(2);
			movePlayer(3556, 9718, -1);
			break;
		case 5:
			sendFrame99(2);
			movePlayer(3534, 9704, -1);
			break;
		case 6:
			sendFrame99(2);
			movePlayer(3546, 9684, -1);
			break;
		}
		player.newLocation = 0;
	}

	/**
	 * Teleporting
	 **/

	public void startLeverTeleport(int x, int y, int height, String teleportType) {
		if (!player.playerName.equalsIgnoreCase("dylan") && System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return;
		}

		if (!player.isDead && player.teleTimer == 0 && player.respawnTimer == -6) {
			if (player.playerIndex > 0 || player.npcIndex > 0)
				player.getCombat().resetPlayerAttack();
			player.stopMovement();
			removeAllWindows();
			player.teleX = x;
			player.teleY = y;
			player.npcIndex = 0;
			player.playerIndex = 0;
			player.face(null);
			player.teleHeight = height;
			if (teleportType.equalsIgnoreCase("lever")) {
				player.animation(2140);
				player.teleTimer = 8;
				player.sendMessage("You pull the lever..");
			}
		}
		player.getSkilling().stop();
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			Server.getMultiplayerSessionListener().finish(player, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
		}
	}

	public boolean canTeleport(String type) {
		if (!player.playerName.equalsIgnoreCase("dylan") && !player.lastSpear.elapsed(4000)) {
			player.sendMessage("You are stunned and can not teleport!");
			return false;
		}
		if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (Objects.nonNull(session)) {
				player.sendMessage("You cannot teleport whilst in a duel.");
				return false;
			}
		}
		if (Boundary.isIn(player, Boundary.FIGHT_CAVE)) {
			player.sendMessage("You cannot teleport out of fight caves.");
			return false;
		}
		if (!player.playerName.equalsIgnoreCase("dylan") && Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("You must finish what you're doing before you can teleport.");
			return false;
		}
		if (player.isInJail() && !(player.getRights().isBetween(1, 3))) {
			player.sendMessage("You cannot teleport out of jail.");
			return false;
		}
		if (!player.playerName.equalsIgnoreCase("dylan") && player.inWild()) {
			if (!player.playerName.equalsIgnoreCase("dylan") && !type.equals("glory")) {
				if (player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
					player.sendMessage(
							"You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
					player.getPA().closeAllWindows();
					return false;
				}
			} else {
				if (!player.playerName.equalsIgnoreCase("dylan") && player.wildLevel > 30) {
					player.sendMessage("You can't teleport above level 30 in the wilderness.");
					player.getPA().closeAllWindows();
					return false;
				}
			}
		}
		if (!player.playerName.equalsIgnoreCase("dylan") && System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return false;
		}
		return true;
	}

	public void teleTabTeleport(int x, int y, int height, String teleportType) {
		if (!player.playerName.equalsIgnoreCase("dylan") && Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("You cannot teleport until you finish what you're doing.");
			return;
		}
		if (!player.playerName.equalsIgnoreCase("dylan") && player.inWild() && player.wildLevel > Config.NO_TELEPORT_WILD_LEVEL) {
			player.sendMessage(
					"You can't teleport above level " + Config.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		}
		if (!player.playerName.equalsIgnoreCase("dylan") && System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (Boundary.isIn(player, Boundary.FIGHT_CAVE)) {
			player.sendMessage("You cannot teleport out of fight caves.");
			return;
		}
		if (!player.isDead && player.teleTimer == 0 && player.respawnTimer == -6) {
			if (player.playerIndex > 0 || player.npcIndex > 0)
				player.getCombat().resetPlayerAttack();
			player.stopMovement();
			removeAllWindows();
			player.teleX = x;
			player.teleY = y;
			player.npcIndex = 0;
			player.playerIndex = 0;
			player.face(null);
			player.teleHeight = height;
			player.getSkilling().stop();
			if (teleportType.equalsIgnoreCase("teleTab")) {
				player.animation(4731);
				player.teleEndAnimation = 0;
				player.teleTimer = 8;
				player.gfx0(678);
			}
		}
	}

	public void startTeleport2(int x, int y, int height) {
		if (!player.playerName.equalsIgnoreCase("dylan") && Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("You cannot teleport until you finish what you're doing.");
			return;
		}
		if (!player.playerName.equalsIgnoreCase("dylan") && !player.lastSpear.elapsed(4000)) {
			player.sendMessage("You are stunned and can not teleport!");
			return;
		}
		if (!player.playerName.equalsIgnoreCase("dylan") && System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (Boundary.isIn(player, Boundary.FIGHT_CAVE)) {
			player.sendMessage("You cannot teleport out of fight caves.");
			return;
		}
		if (!player.isDead && player.teleTimer == 0) {
			player.stopMovement();
			removeAllWindows();
			player.teleX = x;
			player.teleY = y;
			player.npcIndex = 0;
			player.playerIndex = 0;
			player.face(null);
			player.teleHeight = height;
			player.animation(714);
			player.teleTimer = 11;
			player.teleGfx = 308;
			player.teleEndAnimation = 715;
			player.getSkilling().stop();
		}
	}

	public void processTeleport() {
		player.teleportToX = player.teleX;
		player.teleportToY = player.teleY;
		player.heightLevel = player.teleHeight;
		if (player.teleEndAnimation > 0) {
			player.animation(player.teleEndAnimation);
		}
	}

	/*
	 * public void walkTo(int x, int y) {
	 * PathFinder.getPathFinder().findRoute(c, x, y, true, 1, 1); }
	 */

	public void followNpc() {
		if (NPCHandler.npcs[player.followId2] == null || NPCHandler.npcs[player.followId2].isDead) {
			player.followId2 = 0;
			return;
		}
		if (player.freezeTimer > 0) {
			return;
		}
		if (player.isDead || player.playerLevel[3] <= 0)
			return;
		int otherX = NPCHandler.npcs[player.followId2].getX();
		int otherY = NPCHandler.npcs[player.followId2].getY();

		NPC npc = NPCHandler.npcs[player.followId2];
		double distance = npc.getDistance(player.getX(), player.getY());

		boolean withinDistance = distance <= 2;
		boolean hallyDistance = distance <= 2;
		boolean bowDistance = distance <= 8;
		boolean rangeWeaponDistance = distance <= 4;
		boolean sameSpot = player.absX == otherX && player.absY == otherY;

		if (!player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25)) {
			player.followId2 = 0;
			return;
		}

		player.face(npc);

		if (distance <= 1) {
			if (!npc.insideOf(player.getX(), player.getY())) {
				return;
			}
		}

		boolean projectile = player.usingBow || player.mageFollow || (player.npcIndex > 0 && player.autocastId > 0)
				|| player.usingRangeWeapon || player.usingOtherRangeWeapons;

		if (projectile && npc.npcType == AbyssalSireConstants.SLEEPING_NPC_ID) {
			return;
		}

		if (player.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (!npc.insideOf(player.getX(), player.getY())) {
			if (!projectile || projectile && (PathChecker.isProjectilePathClear(player.getX(), player.getY(),
					player.heightLevel, npc.getX(), npc.getY())
					|| PathChecker.isProjectilePathClear(npc.getX(), npc.getY(), player.heightLevel, player.getX(),
							player.getY()))) {

				if ((player.usingBow || player.mageFollow || (player.npcIndex > 0 && player.autocastId > 0))
						&& bowDistance && !sameSpot) {
					return;
				}

				if (player.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
					return;
				}
			}
		}

		boolean isWaterNPC = npc.npcType == 2042 || npc.npcType == 2043 || npc.npcType == 2044 || npc.npcType == 6656
				|| npc.npcType == 6640 || npc.npcType == 494 || npc.npcType == 496 || npc.npcType == 493;

		if (isWaterNPC) {
			if (player.isRunning2 && !withinDistance) {
				if (otherY > player.getY() && otherX == player.getX()) {
					// walkTo(0, getMove(c.getY(), otherY - 1) +
					// getMove(c.getY(),
					// otherY - 1));
					playerWalk(otherX, otherY - 1);
				} else if (otherY < player.getY() && otherX == player.getX()) {
					// walkTo(0, getMove(c.getY(), otherY + 1) +
					// getMove(c.getY(),
					// otherY + 1));
					playerWalk(otherX, otherY + 1);
				} else if (otherX > player.getX() && otherY == player.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(),
					// otherX - 1), 0);
					playerWalk(otherX - 1, otherY);
				} else if (otherX < player.getX() && otherY == player.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
					// otherX + 1), 0);
					playerWalk(otherX + 1, otherY);
				} else if (otherX < player.getX() && otherY < player.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
					// otherX + 1), getMove(c.getY(), otherY + 1) +
					// getMove(c.getY(), otherY + 1));
					playerWalk(otherX + 1, otherY + 1);
				} else if (otherX > player.getX() && otherY > player.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1) + getMove(c.getX(),
					// otherX - 1), getMove(c.getY(), otherY - 1) +
					// getMove(c.getY(), otherY - 1));
					playerWalk(otherX - 1, otherY - 1);
				} else if (otherX < player.getX() && otherY > player.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
					// otherX + 1), getMove(c.getY(), otherY - 1) +
					// getMove(c.getY(), otherY - 1));
					playerWalk(otherX + 1, otherY - 1);
				} else if (otherX > player.getX() && otherY < player.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1) + getMove(c.getX(),
					// otherX + 1), getMove(c.getY(), otherY - 1) +
					// getMove(c.getY(), otherY - 1));
					playerWalk(otherX + 1, otherY - 1);
				}
			} else {
				if (otherY > player.getY() && otherX == player.getX()) {
					// walkTo(0, getMove(c.getY(), otherY - 1));
					playerWalk(otherX, otherY - 1);
				} else if (otherY < player.getY() && otherX == player.getX()) {
					// walkTo(0, getMove(c.getY(), otherY + 1));
					playerWalk(otherX, otherY + 1);
				} else if (otherX > player.getX() && otherY == player.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1), 0);
					playerWalk(otherX - 1, otherY);
				} else if (otherX < player.getX() && otherY == player.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1), 0);
					playerWalk(otherX + 1, otherY);
				} else if (otherX < player.getX() && otherY < player.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(),
					// otherY + 1));
					playerWalk(otherX + 1, otherY + 1);
				} else if (otherX > player.getX() && otherY > player.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(),
					// otherY - 1));
					playerWalk(otherX - 1, otherY - 1);
				} else if (otherX < player.getX() && otherY > player.getY()) {
					// walkTo(getMove(c.getX(), otherX + 1), getMove(c.getY(),
					// otherY - 1));
					playerWalk(otherX + 1, otherY - 1);
				} else if (otherX > player.getX() && otherY < player.getY()) {
					// walkTo(getMove(c.getX(), otherX - 1), getMove(c.getY(),
					// otherY + 1));
					playerWalk(otherX - 1, otherY + 1);
				}
			}
		} else {

			final int x = player.absX;
			final int y = player.absY;
			final int z = player.heightLevel;
			final int x2 = otherX;
			final int y2 = otherY;
			double lowDist = 9999;
			int lowX = 0;
			int lowY = 0;
			int x3 = x2;
			int y3 = y2 - 1;
			final int loop = npc.getSize();

			for (int k = 0; k < 4; k++) {
				for (int i = 0; i < loop - (k == 0 ? 1 : 0); i++) {
					if (k == 0) {
						x3++;
					} else if (k == 1) {
						if (i == 0) {
							x3++;
						}
						y3++;
					} else if (k == 2) {
						if (i == 0) {
							y3++;
						}
						x3--;
					} else if (k == 3) {
						if (i == 0) {
							x3--;
						}
						y3--;
					}

					if (Misc.distance(x, y, x3, y3) < lowDist) {
						if (!projectile && PathChecker.isMeleePathClear(x3, y3, z, x2, y2)
								|| projectile && PathChecker.isProjectilePathClear(x3, y3, z, x2, y2)) {
							if (PathFinder.getPathFinder().accessable(player, x3, y3)) {
								lowDist = Misc.distance(x, y, x3, y3);
								lowX = x3;
								lowY = y3;
							}
						}
					}
				}
			}

			if (lowX != 0 && lowY != 0) {
				PathFinder.getPathFinder().findRoute(player, lowX, lowY, true, 18, 18);
			} else {
				PathFinder.getPathFinder().findRoute(player, npc.absX, npc.absY, true, 18, 18);
			}

		}
	}

	/**
	 * Following
	 **/

	public void followPlayer() {
		if (player.followId > PlayerHandler.players.length) {
			player.followId = 0;
			return;
		}
		if (PlayerHandler.players[player.followId] == null) {
			player.followId = 0;
			return;
		}
		if (PlayerHandler.players[player.followId].isDead) {
			player.followId = 0;
			return;
		}
		if (player.freezeTimer > 0) {
			return;
		}
		if (Boundary.isIn(player, Boundary.DUEL_ARENAS)) {
			DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
					MultiplayerSessionType.DUEL);
			if (!Objects.isNull(session)) {
				if (session.getRules().contains(Rule.NO_MOVEMENT)) {
					player.followId = 0;
					return;
				}
			}
		}
		if (inPitsWait()) {
			player.followId = 0;
		}

		if (player.isDead || player.playerLevel[3] <= 0)
			return;
		if (!player.playerName.equalsIgnoreCase("dylan") && !player.lastSpear.elapsed(4000)) {
			player.sendMessage("You are stunned, you cannot move.");
			player.followId = 0;
			return;
		}
		int otherX = PlayerHandler.players[player.followId].getX();
		int otherY = PlayerHandler.players[player.followId].getY();

		boolean sameSpot = (player.absX == otherX && player.absY == otherY);

		boolean hallyDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		boolean withinDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		boolean rangeWeaponDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 6);
		boolean bowDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 6);

		if (!player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25)) {
			player.followId = 0;
			return;
		}

		player.face(PlayerHandler.players[player.followId]);

		boolean projectile = player.usingBow || player.mageFollow || player.autocastId > 0 || player.usingRangeWeapon
				|| player.usingOtherRangeWeapons;
		if (!projectile || projectile && (PathChecker.isProjectilePathClear(player.absX, player.absY,
				player.heightLevel, otherX, otherY)
				|| PathChecker.isProjectilePathClear(otherX, otherY, player.heightLevel, player.absX, player.absY))) {
			if (player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1)) {
				if (otherX != player.getX() || otherY != player.getY()) {
					return;
				}
			}

			if ((player.usingBow || player.mageFollow || (player.playerIndex > 0 && player.autocastId > 0))
					&& bowDistance && !sameSpot) {
				return;
			}

			if (player.getCombat().usingHally() && hallyDistance && !sameSpot) {
				return;
			}

			if (player.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
				return;
			}
		}

		final int x = player.absX;
		final int y = player.absY;
		final int z = player.heightLevel;
		final int x2 = otherX;
		final int y2 = otherY;
		double lowDist = 9999;
		int lowX = 0;
		int lowY = 0;
		int x3 = x2;
		int y3 = y2 - 1;

		int[][] nonDiags = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		for (int k = 0; k < 4; k++) {
			x3 = otherX + nonDiags[k][0];
			y3 = otherY + nonDiags[k][1];
			if (Misc.distance(x, y, x3, y3) < lowDist) {
				if (!projectile && PathChecker.isMeleePathClear(x3, y3, z, x2, y2)
						|| projectile && PathChecker.isProjectilePathClear(x3, y3, z, x2, y2)) {
					if (PathFinder.getPathFinder().accessable(player, x3, y3)) {
						lowDist = Misc.distance(x, y, x3, y3);
						lowX = x3;
						lowY = y3;
					}
				}
			}
		}

		if (lowX != 0 && lowY != 0) {
			PathFinder.getPathFinder().findRoute(player, lowX, lowY, true, 18, 18);
		} else {
			PathFinder.getPathFinder().findRoute(player, otherX, otherY, true, 18, 18);
		}
	}

	public int getRunningMove(int i, int j) {
		if (j - i > 2)
			return 2;
		else if (j - i < -2)
			return -2;
		else
			return j - i;
	}

	public void sendStatement(String s) {
		sendFrame126(s, 357);
		sendFrame126("Click here to continue", 358);
		sendFrame164(356);
	}

	public void resetFollow() {
		player.followId = 0;
		player.followId2 = 0;
		player.mageFollow = false;
		if (player.outStream != null) {
			player.outStream.createFrame(174);
			player.outStream.writeWord(0);
			player.outStream.writeByte(0);
			player.outStream.writeWord(1);
		}
	}

	public void stepAway(NPC npc) {
		if (npc == null) {
			return;
		}
		npc.getSize();
		if (npc.getX() > player.getX()) {
			if (npc.getY() > player.getY()) {

			} else if (npc.getY() < player.getY()) {

			}
		}
	}

	public void walkTo3(int i, int j) {
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.absX + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = tmpNWCX[0] = tmpNWCY[0] = 0;
		int l = player.absY + j;
		l -= player.mapRegionY * 8;
		player.isRunning2 = false;
		player.isRunning = false;
		player.getNewWalkCmdX()[0] += k;
		player.getNewWalkCmdY()[0] += l;
		player.poimiY = l;
		player.poimiX = k;
	}

	int tmpNWCX[] = new int[50];
	int tmpNWCY[] = new int[50];

	/*
	 * public void walkTo(int i, int j) { c.newWalkCmdSteps = 0; if
	 * (++c.newWalkCmdSteps > 50) c.newWalkCmdSteps = 0; int k = c.getX() + i; k
	 * -= c.mapRegionX * 8; c.getNewWalkCmdX()[0] = c.getNewWalkCmdY()[0] = 0;
	 * int l = c.getY() + j; l -= c.mapRegionY * 8;
	 * 
	 * for (int n = 0; n < c.newWalkCmdSteps; n++) { c.getNewWalkCmdX()[n] += k;
	 * c.getNewWalkCmdY()[n] += l; } }
	 */

	public void walkTo(int x, int y) {
		PathFinder.getPathFinder().findRoute(player, x, y, true, 1, 1);
	}

	public void walkTo5(int i, int j) {
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.getX() + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + j;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkTo2(int i, int j) {
		if (player.freezeDelay > 0)
			return;
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.getX() + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + j;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}
	}

	public void stopDiagonal(int otherX, int otherY) {
		if (player.freezeDelay > 0)
			return;
		if (player.freezeTimer > 0) // player can't move
			return;
		player.newWalkCmdSteps = 1;
		int xMove = otherX - player.getX();
		int yMove = 0;
		if (xMove == 0)
			yMove = otherY - player.getY();
		/*
		 * if (!clipHor) { yMove = 0; } else if (!clipVer) { xMove = 0; }
		 */

		int k = player.getX() + xMove;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + yMove;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}

	}

	public void walkToCheck(int i, int j) {
		if (player.freezeDelay > 0)
			return;
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.getX() + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + j;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if (!player.lastSpear.elapsed(4000))
			return 0;
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean fullVeracs() {
		return player.playerEquipment[player.playerHat] == 4753 && player.playerEquipment[player.playerChest] == 4757
				&& player.playerEquipment[player.playerLegs] == 4759
				&& player.playerEquipment[Player.playerWeapon] == 4755;
	}

	public boolean fullGuthans() {
		return player.playerEquipment[player.playerHat] == 4724 && player.playerEquipment[player.playerChest] == 4728
				&& player.playerEquipment[player.playerLegs] == 4730
				&& player.playerEquipment[Player.playerWeapon] == 4726;
	}

	/**
	 * reseting animation
	 **/
	public void resetAnimation() {
		player.getCombat().getPlayerAnimIndex(
				player.getItems().getItemName(player.playerEquipment[Player.playerWeapon]).toLowerCase());
		player.animation(player.playerStandIndex);
		requestUpdates();
	}

	public void requestUpdates() {
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);
	}

	/*
	 * public void Obelisks(int id) { if (!c.getItems().playerHasItem(id)) {
	 * c.getItems().addItem(id, 1); } }
	 */

	public void levelUp(int skill) {
		int totalLevel = (getLevelForXP(player.playerXP[0]) + getLevelForXP(player.playerXP[1])
				+ getLevelForXP(player.playerXP[2]) + getLevelForXP(player.playerXP[3])
				+ getLevelForXP(player.playerXP[4]) + getLevelForXP(player.playerXP[5])
				+ getLevelForXP(player.playerXP[6]) + getLevelForXP(player.playerXP[7])
				+ getLevelForXP(player.playerXP[8]) + getLevelForXP(player.playerXP[9])
				+ getLevelForXP(player.playerXP[10]) + getLevelForXP(player.playerXP[11])
				+ getLevelForXP(player.playerXP[12]) + getLevelForXP(player.playerXP[13])
				+ getLevelForXP(player.playerXP[14]) + getLevelForXP(player.playerXP[15])
				+ getLevelForXP(player.playerXP[16]) + getLevelForXP(player.playerXP[17])
				+ getLevelForXP(player.playerXP[18]) + getLevelForXP(player.playerXP[19])
				+ getLevelForXP(player.playerXP[20]) + getLevelForXP(player.playerXP[21]) + getLevelForXP(player.playerXP[22]));
		sendFrame126("Level: " + totalLevel, 3984);
		if (player.doingTutorial)
			return;
//		switch (skill) {
//		case 0:
//			if(!player.isExtreme) {
//				return;
//			}
//			sendFrame126("Congratulations, you just advanced an attack level!", 6248);
//			sendFrame126("Your attack level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6249);
//			player.sendMessage("Congratulations, you just advanced an attack level.");
//			sendFrame164(6247);
//			if(getLevelForXP(player.playerXP[0]) >= 99 && player.isExtreme) {
//				Player.attack = true;
//				player.sendMessage("HitPoints: "+Player.hitpoints);
//				player.sendMessage("defence: "+Player.defence);
//				player.sendMessage("attack: "+Player.attack);
//				player.sendMessage("strength: "+Player.strength);
//				player.sendMessage("range: "+Player.range);
//				player.sendMessage("magic: "+Player.magic);
//				player.sendMessage("prayerLevel: "+Player.prayerLevel);
//			}
//			break;
//
//		case 1:
//			if(!player.isExtreme) {
//				return;
//			}
//			sendFrame126("Congratulations, you just advanced a defence level!", 6254);
//			sendFrame126("Your defence level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6255);
//			player.sendMessage("Congratulations, you just advanced a defence level.");
//			sendFrame164(6253);
//			if(getLevelForXP(player.playerXP[1]) >= 99 && player.isExtreme) {
//				Player.defence = true;
//				player.sendMessage("HitPoints: "+Player.hitpoints);
//				player.sendMessage("defence: "+Player.defence);
//				player.sendMessage("attack: "+Player.attack);
//				player.sendMessage("strength: "+Player.strength);
//				player.sendMessage("range: "+Player.range);
//				player.sendMessage("magic: "+Player.magic);
//				player.sendMessage("prayerLevel: "+Player.prayerLevel);
//			}
//			break;
//
//		case 2:
//			if(!player.isExtreme) {
//				return;
//			}
//			sendFrame126("Congratulations, you just advanced a strength level!", 6207);
//			sendFrame126("Your strength level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6208);
//			player.sendMessage("Congratulations, you just advanced a strength level.");
//			sendFrame164(6206);
//			if(getLevelForXP(player.playerXP[2]) >= 99 && player.isExtreme) {
//				Player.strength = true;
//				player.sendMessage("HitPoints: "+Player.hitpoints);
//				player.sendMessage("defence: "+Player.defence);
//				player.sendMessage("attack: "+Player.attack);
//				player.sendMessage("strength: "+Player.strength);
//				player.sendMessage("range: "+Player.range);
//				player.sendMessage("magic: "+Player.magic);
//				player.sendMessage("prayerLevel: "+Player.prayerLevel);
//			}
//			break;
//
//		case 3:
//			if(!player.isExtreme) {
//				return;
//			}
//			sendFrame126("Congratulations, you just advanced a hitpoints level!", 6217);
//			sendFrame126("Your hitpoints level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6218);
//			player.sendMessage("Congratulations, you just advanced a hitpoints level.");
//			sendFrame164(6216);
//			if(getLevelForXP(player.playerXP[3]) >= 99 && player.isExtreme) {
//				Player.hitpoints = true;
//				player.sendMessage("HitPoints: "+Player.hitpoints);
//				player.sendMessage("defence: "+Player.defence);
//				player.sendMessage("attack: "+Player.attack);
//				player.sendMessage("strength: "+Player.strength);
//				player.sendMessage("range: "+Player.range);
//				player.sendMessage("magic: "+Player.magic);
//				player.sendMessage("prayerLevel: "+Player.prayerLevel);
//			}
//			break;
//
//		case 4:
//			if(!player.isExtreme) {
//				return;
//			}
//			player.sendMessage("Congratulations, you just advanced a ranging level.");
//			if(getLevelForXP(player.playerXP[4]) >= 99 && player.isExtreme) {
//				Player.range = true;
//				player.sendMessage("HitPoints: "+Player.hitpoints);
//				player.sendMessage("defence: "+Player.defence);
//				player.sendMessage("attack: "+Player.attack);
//				player.sendMessage("strength: "+Player.strength);
//				player.sendMessage("range: "+Player.range);
//				player.sendMessage("magic: "+Player.magic);
//				player.sendMessage("prayerLevel: "+Player.prayerLevel);
//			}
//			break;
//
//		case 5:
//			if(!player.isExtreme) {
//				return;
//			}
//			sendFrame126("Congratulations, you just advanced a prayer level!", 6243);
//			sendFrame126("Your prayer level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6244);
//			player.sendMessage("Congratulations, you just advanced a prayer level.");
//			sendFrame164(6242);
//			if(getLevelForXP(player.playerXP[5]) >= 99 && player.isExtreme) {
//				Player.prayerLevel = true;
//				player.sendMessage("HitPoints: "+Player.hitpoints);
//				player.sendMessage("defence: "+Player.defence);
//				player.sendMessage("attack: "+Player.attack);
//				player.sendMessage("strength: "+Player.strength);
//				player.sendMessage("range: "+Player.range);
//				player.sendMessage("magic: "+Player.magic);
//				player.sendMessage("prayerLevel: "+Player.prayerLevel);
//			}
//			break;
//
//		case 6:
//			if(!player.isExtreme) {
//				return;
//			}
//			sendFrame126("Congratulations, you just advanced a magic level!", 6212);
//			sendFrame126("Your magic level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6213);
//			player.sendMessage("Congratulations, you just advanced a magic level.");
//			sendFrame164(6211);
//			if(getLevelForXP(player.playerXP[6]) >= 99 && player.isExtreme) {
//				Player.magic = true;
//				player.sendMessage("HitPoints: "+Player.hitpoints);
//				player.sendMessage("defence: "+Player.defence);
//				player.sendMessage("attack: "+Player.attack);
//				player.sendMessage("strength: "+Player.strength);
//				player.sendMessage("range: "+Player.range);
//				player.sendMessage("magic: "+Player.magic);
//				player.sendMessage("prayerLevel: "+Player.prayerLevel);
//			}
//			break;
//
//		case 7:
//			sendFrame126("Congratulations, you just advanced a cooking level!", 6227);
//			sendFrame126("Your cooking level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6228);
//			player.sendMessage("Congratulations, you just advanced a cooking level.");
//			sendFrame164(6226);
//			if (getLevelForXP(player.playerXP[7]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> cooking!</col>");
//			}
//			break;
//
//		case 8:
//			sendFrame126("Congratulations, you just advanced a woodcutting level!", 4273);
//			sendFrame126("Your woodcutting level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4274);
//			player.sendMessage("Congratulations, you just advanced a woodcutting level.");
//			sendFrame164(4272);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> woodcutting!</col>");
//			}
//			break;
//
//		case 9:
//			sendFrame126("Congratulations, you just advanced a fletching level!", 6232);
//			sendFrame126("Your fletching level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6233);
//			player.sendMessage("Congratulations, you just advanced a fletching level.");
//			sendFrame164(6231);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> fletching!</col>");
//			}
//			break;
//
//		case 10:
//			sendFrame126("Congratulations, you just advanced a fishing level!", 6259);
//			sendFrame126("Your fishing level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6260);
//			player.sendMessage("Congratulations, you just advanced a fishing level.");
//			sendFrame164(6258);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> fishing!</col>");
//			}
//			break;
//
//		case 11:
//			sendFrame126("Congratulations, you just advanced a fire making level!", 4283);
//			sendFrame126("Your firemaking level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4284);
//			player.sendMessage("Congratulations, you just advanced a fire making level.");
//			sendFrame164(4282);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> firemaking!</col>");
//			}
//			break;
//
//		case 12:
//			sendFrame126("Congratulations, you just advanced a crafting level!", 6264);
//			sendFrame126("Your crafting level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6265);
//			player.sendMessage("Congratulations, you just advanced a crafting level.");
//			sendFrame164(6263);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> crafting!</col>");
//			}
//			break;
//
//		case 13:
//			sendFrame126("Congratulations, you just advanced a smithing level!", 6222);
//			sendFrame126("Your smithing level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6223);
//			player.sendMessage("Congratulations, you just advanced a smithing level.");
//			sendFrame164(6221);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> smithing!</col>");
//			}
//			break;
//
//		case 14:
//			sendFrame126("Congratulations, you just advanced a mining level!", 4417);
//			sendFrame126("Your mining level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4438);
//			player.sendMessage("Congratulations, you just advanced a mining level.");
//			sendFrame164(4416);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> mining!</col>");
//			}
//			break;
//
//		case 15:
//			sendFrame126("Congratulations, you just advanced a herblore level!", 6238);
//			sendFrame126("Your herblore level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6239);
//			player.sendMessage("Congratulations, you just advanced a herblore level.");
//			sendFrame164(6237);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> herblore!</col>");
//			}
//			break;
//
//		case 16:
//			sendFrame126("Congratulations, you just advanced a agility level!", 4278);
//			sendFrame126("Your agility level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4279);
//			player.sendMessage("Congratulations, you just advanced an agility level.");
//			sendFrame164(4277);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> agility!</col>");
//			}
//			break;
//
//		case 17:
//			sendFrame126("Congratulations, you just advanced a thieving level!", 4263);
//			sendFrame126("Your theiving level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4264);
//			player.sendMessage("Congratulations, you just advanced a thieving level.");
//			sendFrame164(4261);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> thieving!</col>");
//			}
//			break;
//
//		case 18:
//			sendFrame126("Congratulations, you just advanced a slayer level!", 12123);
//			sendFrame126("Your slayer level is now " + getLevelForXP(player.playerXP[skill]) + ".", 12124);
//			player.sendMessage("Congratulations, you just advanced a slayer level.");
//			sendFrame164(12122);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> slayer!</col>");
//			}
//			break;
//
//		case 19:
//			player.sendMessage("Congratulations! You've just advanced a Farming level.");
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> farming!</col>");
//			}
//			break;
//
//		case 20:
//			sendFrame126("Congratulations, you just advanced a runecrafting level!", 4268);
//			sendFrame126("Your runecrafting level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
//			player.sendMessage("Congratulations, you just advanced a runecrafting level.");
//			sendFrame164(4267);
//			if (getLevelForXP(player.playerXP[skill]) == 99) {
//				PlayerHandler.sendMessageToAll("[<col=255>News</col>] <col=CC0000>" + player.playerName
//						+ " has just acheived <b>99</b> runecrafting!</col>");
//			}
//			break;
//		}
//		/*
//		 * if (getLevelForXP(player.playerXP[skill]) == 99) { Skill s =
//		 * Skill.forId(skill);
//		 * player.getItems().addItem(Config.TOKENS_ID,Misc.random(200,250));
//		 * PlayerHandler.
//		 * executeGlobalMessage("<img=10></img>[<col=255>News</col>] <col=CC0000>"
//		 * + Misc.capitalize(player.playerName) +
//		 * "</col> has reached level 99 <col=CC0000>" + s.toString() +
//		 * "</col>, congratulations."); }
//		 */
//		player.dialogueAction = 0;
//		player.nextChat = 0;
	}

	public void refreshSkill(int i) {
		int totalLevel = (getLevelForXP(player.playerXP[0])
				+ getLevelForXP(player.playerXP[1]) + getLevelForXP(player.playerXP[2])
				+ getLevelForXP(player.playerXP[3]) + getLevelForXP(player.playerXP[4])
				+ getLevelForXP(player.playerXP[5]) + getLevelForXP(player.playerXP[6]) + getLevelForXP(player.playerXP[7]));
		sendFrame126("Level: " + totalLevel, 29035);
		int totalExp = (player.playerXP[0] + player.playerXP[1] + player.playerXP[2]
				+ player.playerXP[3] + player.playerXP[4] + player.playerXP[5] + player.playerXP[6] + player.playerXP[7]);
		sendFrame126("Exp :" + totalExp, 29036);
		switch (i) {
		case 0:
			sendFrame126("Level: " + player.playerLevel[0] + "/"
					+ getLevelForXP(player.playerXP[0]) + "", 29003);
			sendFrame126("Exp: " + player.playerXP[0] + "", 29004);
			break;

		case 1:
			sendFrame126("Level: " + player.playerLevel[1] + "/"
					+ getLevelForXP(player.playerXP[1]) + "", 29007);
			sendFrame126("Exp: " + player.playerXP[1] + "", 29008);
			break;

		case 2:
			sendFrame126("Level: " + player.playerLevel[2] + "/"
					+ getLevelForXP(player.playerXP[2]) + "", 29011);
			sendFrame126("Exp: " + player.playerXP[2] + "", 29012);
			break;

		case 3:
			sendFrame126("Level: " + player.playerLevel[3] + "/"
					+ getLevelForXP(player.playerXP[3]) + "", 29015);
			sendFrame126("Exp: " + player.playerXP[3] + "", 29016);
			sendFrame126("" + player.playerLevel[3] + "", 4016);// hp orb
			sendFrame126("" + getLevelForXP(player.playerXP[3]) + "", 4017);// hp orb
			break;

		case 4:
			sendFrame126("Level: " + player.playerLevel[4] + "/"
					+ getLevelForXP(player.playerXP[4]) + "", 29019);
			sendFrame126("Exp: " + player.playerXP[4] + "", 29020);
			break;

		case 5:
			sendFrame126("Level: " + player.playerLevel[5] + "/"
					+ getLevelForXP(player.playerXP[5]) + "", 29023);
			sendFrame126("Exp: " + player.playerXP[5] + "", 29024);
			sendFrame126("" + player.playerLevel[5] + "/"
					+ getLevelForXP(player.playerXP[5]) + "", 687);// Prayer frame
			sendFrame126("" + player.playerLevel[5] + "", 4012);// pray orb
			sendFrame126("" + getLevelForXP(player.playerXP[5]) + "", 4013);// pray
																		// orb
			break;

		case 6:
			sendFrame126("Level: " + player.playerLevel[6] + "/"
					+ getLevelForXP(player.playerXP[6]) + "", 29027);
			sendFrame126("Please click on the skill you would", 29028);
			break;

		
		}
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public boolean isCombatSkill(int skill) {
		return skill == Config.ATTACK || skill == Config.STRENGTH || skill == Config.DEFENCE
				|| skill == Config.HITPOINTS || skill == Config.MAGIC || skill == Config.RANGED
				|| skill == Config.PRAYER;
	}

	public boolean addSkillXP(double amount, int skill) {
		return addSkillXP((int) amount, skill);
	}

	public boolean addSkillXP(int amount, int skill) {
		return addSkillXP(amount, skill, true);
	}
	
	public boolean addSkillXP10(int amount, int skill) {
		if (player.expLock == true) {
			return false;
		}
		if (amount + player.playerXP[skill] < 0) {
			return false;
		}
		if (Config.BONUS_WEEKEND && player.bonusXpTime == 0) {
			amount *= Config.SERVER_EXP_BONUS_WEEKEND;
		} else if (Config.BONUS_WEEKEND && player.bonusXpTime > 0) {
			amount *= Config.SERVER_EXP_BONUS_WEEKEND_BOOSTED;
		} else if (!Config.BONUS_WEEKEND && player.bonusXpTime > 0) {
			amount *= Config.SERVER_EXP_BONUS_BOOSTED;
		} else {
			amount *= Config.SERVER_EXP_BONUS;
		}
		int oldLevel = getLevelForXP(player.playerXP[skill]);
		if (player.playerXP[skill] + amount > 200000000) {
			player.playerXP[skill] = 200000000;
		} else {
			player.playerXP[skill] += amount;
		}
		if (oldLevel < getLevelForXP(player.playerXP[skill])) {
			if (player.playerLevel[skill] < Player.getLevelForXP(player.playerXP[skill]) && skill != 3 && skill != 5)
				player.playerLevel[skill] = Player.getLevelForXP(player.playerXP[skill]);
			player.combatLevel = player.calculateCombatLevel();
			levelUp(skill);
			player.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, player.playerLevel[skill], player.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	public boolean addSkillXP(int amount, int skill, boolean bonus) {
		int original = amount;
		if (player.expLock == true && !player.hungerGames) {
			return false;
		}
		if (amount + player.playerXP[skill] < 0) {
			return false;
		}
		if (bonus) {
			if (Boundary.isIn(player, Boundary.RESOURCE_AREA)) {
				amount *= 1.50;
			}
			if (Config.BONUS_WEEKEND && player.bonusXpTime == 0) {
				amount *= Config.SERVER_EXP_BONUS_WEEKEND;
			} else if (Config.BONUS_WEEKEND && player.bonusXpTime > 0) {
				amount *= Config.SERVER_EXP_BONUS_WEEKEND_BOOSTED;
			} else if (!Config.BONUS_WEEKEND && player.bonusXpTime > 0) {
				amount *= Config.SERVER_EXP_BONUS_BOOSTED;
			} else {
				amount *= Config.SERVER_EXP_BONUS;
			}

			if (isCombatSkill(skill) && skill != Config.PRAYER) {

				if (player.isExtreme) {
					amount *= 60;
				} else if (player.ironman) {
					amount *= 30;
				} else {
					amount *= 300;
				}

			} else if (player.isExtreme) {
				amount *= 0.5;
			}

			if (player.hungerGames && amount > 0) {
				amount = (int) (original * (Math
						.log(Player.getLevelForXP(player.playerXP[skill] / 2) / Math.pow(player.playerXP[skill] + 1, 5))
						+ (15 * Player.getLevelForXP(player.playerXP[skill])))) + 60;

			}
		}
		int oldLevel = getLevelForXP(player.playerXP[skill]);
		if (player.playerXP[skill] + amount > 200000000) {
			player.playerXP[skill] = 200000000;
		}
		if(isCombatSkill(skill) && skill != Config.PRAYER && player.inWild() && player.playerIndex > 0) {
			player.playerXP[skill] += original;
		} else
		if(!player.inWild() || player.inWild() && player.npcIndex > 0) {
			player.playerXP[skill] += amount;
		}
		if (oldLevel < getLevelForXP(player.playerXP[skill])) {
			if (player.playerLevel[skill] < Player.getLevelForXP(player.playerXP[skill]) && skill != 3 && skill != 5)
				player.playerLevel[skill] = Player.getLevelForXP(player.playerXP[skill]);
			player.combatLevel = player.calculateCombatLevel();
			levelUp(skill);
			player.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, player.playerLevel[skill], player.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	private static final int LIST_AMOUNT = Integer.MAX_VALUE;

	public void resetBarrows() {
		player.barrowsNpcs[0][1] = 0;
		player.barrowsNpcs[1][1] = 0;
		player.barrowsNpcs[2][1] = 0;
		player.barrowsNpcs[3][1] = 0;
		player.barrowsNpcs[4][1] = 0;
		player.barrowsNpcs[5][1] = 0;
		player.barrowsKillCount = 0;
		player.randomCoffin = Misc.random(3) + 1;
	}

	// public static int Barrows[] = {4708, 4710, 4712, 4714, 4716, 4718, 4720,
	// 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736, 4738, 4745, 4747, 4749,
	// 4751, 4753, 4755, 4757, 4759};
	public static int Runes[] = { 4740, 558, 560, 565 };
	public static int Pots[] = {};

	/*
	 * public int randomBarrows() { return
	 * Barrows[(int)(Math.random()*Barrows.length)]; }
	 */

	public int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
	 * @Param j - The player/Npc that the arrow will be displayed above.
	 * @Param k - Keep this set as 0
	 * @Param l - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		// synchronized(c) {
		player.outStream.createFrame(254);
		player.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			player.outStream.writeWord(j);
			player.outStream.writeWord(k);
			player.outStream.writeByte(l);
		} else {
			player.outStream.writeWord(k);
			player.outStream.writeWord(l);
			player.outStream.writeByte(j);
		}

	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].index == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public void removeObject(int x, int y) {
		object(-1, x, y, 10, 10);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	public void removeObjects() {
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);
	}

	@SuppressWarnings("unused")
	public void handleGlory(int gloryId) {
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("You cannot do that right now.");
			return;
		}
		player.getDH().sendOption4("Edgeville", "Al Kharid", "Karamja", "Mage Bank");
		player.sendMessage("You rub the amulet...");
		player.usingGlory = true;
	}

	public void resetVariables() {
		if (player.playerIsCrafting) {
			CraftingData.resetCrafting(player);
		}
		if (player.playerSkilling[9]) {
			player.playerSkilling[9] = false;
		}
		if (player.isBanking) {
			player.isBanking = false;
		}
		player.usingGlory = false;
		player.smeltInterface = false;
		// c.smeltAmount = 0;
		if (player.dialogueAction > -1) {
			player.dialogueAction = -1;
		}
		if (player.teleAction > -1) {
			player.teleAction = -1;
		}
		if (player.craftDialogue) {
			player.craftDialogue = false;
		}
		player.setInterfaceOpen(-1);
		CycleEventHandler.getSingleton().stopEvents(player, CycleEventHandler.Event.BONE_ON_ALTAR);
	}

	public boolean inPitsWait() {
		return player.getX() <= 2404 && player.getX() >= 2394 && player.getY() <= 5175 && player.getY() >= 5169;
	}

	public void castleWarsObjects() {
		object(-1, 2373, 3119, -3, 10);
		object(-1, 2372, 3119, -3, 10);
	}

	public double getAgilityRunRestore() {
		return 2260 - (player.playerLevel[16] * 10);
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 }, { 2402, 5 }, { 746, 5 }, { 4151, 150 },
				{ 565, 100000 }, { 560, 100000 }, { 555, 300000 }, { 11235, 10 } };
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < player.getItems().getTotalCount(itemsToCheck[j][0]))
				return true;
		}
		return false;
	}

	/*
	 * Vengeance
	 */
	public void castVeng() {
		if (player.duelRule[4]) {
			player.lastCast = 0;
			player.vengOn = false;
			return;
		}
		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(session)) {
			player.sendMessage("You cannot use vengeance in duel arena!");
			return;
		}
		if (player.playerLevel[6] < 94) {
			player.sendMessage("You need a magic level of 94 to cast this spell.");
			return;
		}
		if (player.playerLevel[1] < 40) {
			player.sendMessage("You need a defence level of 40 to cast this spell.");
			return;
		}
		if (!player.getItems().playerHasItem(9075, 4) || !player.getItems().playerHasItem(557, 10)
				|| !player.getItems().playerHasItem(560, 2)) {
			player.sendMessage("You don't have the required runes to cast this spell.");
			return;
		}
		if (Server.currentServerTime - player.lastCast < 30000) {
			player.sendMessage("You can only cast vengeance every 30 seconds.");
			return;
		}
		if (player.vengOn) {
			player.sendMessage("You already have vengeance casted.");
			return;
		}
		player.animation(4410);
		player.gfx100(726);// Just use c.gfx100
		player.getItems().deleteItem2(9075, 4);
		player.getItems().deleteItem2(557, 10);// For these you need to change
												// to deleteItem(item, itemslot,
												// amount);.
		player.getItems().deleteItem2(560, 2);
		addSkillXP(112, 6);
		player.stopMovement();
		refreshSkill(6);
		player.vengOn = true;
		player.getPA().sendFrame126("vengtimer:50", 1810);
		// c.getPA().sendFrame126("vengtimer:50", 1809);
		player.lastCast = Server.currentServerTime;
	}

	public void vengMe() {
		if (Server.currentServerTime - player.lastVeng > 30000) {
			if (player.getItems().playerHasItem(557, 10) && player.getItems().playerHasItem(9075, 4)
					&& player.getItems().playerHasItem(560, 2)) {
				player.vengOn = true;
				player.lastCast = Server.currentServerTime;
				player.getPA().sendFrame126("vengtimer:50", 1809);
				player.animation(4410);
				player.gfx100(726);
				player.getItems().deleteItem(557, player.getItems().getItemSlot(557), 10);
				player.getItems().deleteItem(560, player.getItems().getItemSlot(560), 2);
				player.getItems().deleteItem(9075, player.getItems().getItemSlot(9075), 4);
			} else {
				player.sendMessage("You do not have the required runes to cast this spell. (9075 for astrals)");
			}
		} else {
			player.sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public int totalLevel() {
		int total = 0;
		for (int i = 0; i <= 20; i++) {
			total += getLevelForXP(player.playerXP[i]);
		}
		return total;
	}

	public int xpTotal() {
		int xp = 0;
		for (int i = 0; i <= 20; i++) {
			xp += player.playerXP[i];
		}
		return xp;
	}

	public void sendFrame34P2(int item, int slot, int frame, int amount) {
		player.outStream.createFrameVarSizeWord(34);
		player.outStream.writeWord(frame);
		player.outStream.writeByte(slot);
		player.outStream.writeWord(item + 1);
		player.outStream.writeByte(255);
		player.outStream.writeDWord(amount);
		player.outStream.endFrameVarSizeWord();
	}

	public void sendContainer(int frame, int[] items, int[] amount) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(frame);
			player.getOutStream().writeWord(items.length);
			for (int i = 0; i < items.length; i++) {
				if (amount[i] > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(amount[i]);
				} else {
					player.getOutStream().writeByte(amount[i]);
				}
				player.getOutStream().writeWordBigEndianA(items[i] + 1);
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < player.playerEquipment.length; j++) {
			if (player.playerEquipment[j] > 0)
				count++;
		}
		return count;
	}

	public void startSlayerTeleport() {
		if (player.inWild()) {
			player.sendMessage("You can not teleport to your task whilst in the wilderness.");
			return;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			player.sendMessage("Finish what you are doing before teleporting to your task.");
			return;
		}
		switch (player.slayerTask) {
		case 273:
			TeleportExecutor.teleport(player, new Position(2706, 9450, 0), true);
			break;
		case 270:
			TeleportExecutor.teleport(player, new Position(2731, 9484, 0), true);
			break;
		case 85:
			TeleportExecutor.teleport(player, new Position(2903, 9849, 0), true);
			break;
		case 448:
			TeleportExecutor.teleport(player, new Position(3416, 3536, 0), true);
			break;
		case 2834:
			TeleportExecutor.teleport(player, new Position(2913, 9832, 0), true);
			break;
		case 437:
			TeleportExecutor.teleport(player, new Position(2705, 10028, 0), true);
			break;
		case 419:
			TeleportExecutor.teleport(player, new Position(2802, 10034, 0), true);
			break;
		case 417:
			TeleportExecutor.teleport(player, new Position(2745, 10005, 0), true);
			break;
		case 435:
			TeleportExecutor.teleport(player, new Position(2761, 10011, 0), true);
			break;
		case 2840:
			TeleportExecutor.teleport(player, new Position(3121, 9970, 0), true);
			break;
		case 2025:
			TeleportExecutor.teleport(player, new Position(2635, 9505, 2), true);
			break;
		case 2098:
			TeleportExecutor.teleport(player, new Position(3118, 9838, 0), true);
			break;
		case 2084:
			TeleportExecutor.teleport(player, new Position(2645, 9491, 0), true);
			break;
		case 411:
			TeleportExecutor.teleport(player, new Position(2701, 9997, 0), true);
			break;
		case 119:
			TeleportExecutor.teleport(player, new Position(2895, 9769, 0), true);
			break;
		case 18:
			TeleportExecutor.teleport(player, new Position(3293, 3171, 0), true);
			break;
		case 181:
			TeleportExecutor.teleport(player, new Position(2932, 9847, 0), true);
			break;
		case 446:
			TeleportExecutor.teleport(player, new Position(3436, 3560, 1), true);
			break;
		case 484:
			TeleportExecutor.teleport(player, new Position(3418, 3563, 1), true);
			break;
		case 264:
			TeleportExecutor.teleport(player, new Position(2988, 3597, 0), true);
			break;
		case 2005:
			TeleportExecutor.teleport(player, new Position(2932, 9800, 0), true);
			break;
		case 52:
			TeleportExecutor.teleport(player, new Position(2916, 9801, 0), true);
			break;
		case 1612:
			TeleportExecutor.teleport(player, new Position(3438, 3560, 0), true);
			break;
		case 891:
			TeleportExecutor.teleport(player, new Position(2675, 9549, 0), true);
			break;
		case 125:
			TeleportExecutor.teleport(player, new Position(2954, 3892, 0), true);
			break;
		case 1341:
			TeleportExecutor.teleport(player, new Position(2452, 10152, 0), true);
			break;
		case 424:
			TeleportExecutor.teleport(player, new Position(3422, 3541, 1), true);
			break;
		case 1543:
			TeleportExecutor.teleport(player, new Position(3442, 3554, 2), true);
			break;
		case 11:
			TeleportExecutor.teleport(player, new Position(3440, 3566, 2), true);
			break;
		case 2919:
			TeleportExecutor.teleport(player, new Position(1748, 5328, 0), true);
			break;
		case 415:
			TeleportExecutor.teleport(player, new Position(3419, 3568, 2), true);
			break;
		case 268:
			TeleportExecutor.teleport(player, new Position(2897, 9800, 0), true);
			break;
		case 259:
			TeleportExecutor.teleport(player, new Position(2833, 9824, 0), true);
			break;
		case 1432:
			TeleportExecutor.teleport(player, new Position(2864, 9775, 0), true);
			break;
		case 135:
			TeleportExecutor.teleport(player, new Position(2857, 9840, 0), true);
			break;
		case 4005:
			TeleportExecutor.teleport(player, new Position(2907, 9692, 0), true);
			break;
		case 247:
			TeleportExecutor.teleport(player, new Position(2698, 9512, 0), true);
			break;
		case 924:
			TeleportExecutor.teleport(player, new Position(3105, 9949, 0), true);
			break;
		case 274:
			TeleportExecutor.teleport(player, new Position(2712, 9432, 0), true);
			break;
		case 50:
		case 3200:
			player.sendMessage("Use the boss teleports for this task.");
			break;
		case 2745:
		case 2167:
			player.sendMessage("Use the minigames teleports for this task.");
			break;
		case 9467:
			TeleportExecutor.teleport(player, new Position(2521, 4646, 0), true);
			break;
		case 9465:
			TeleportExecutor.teleport(player, new Position(2521, 4646, 0), true);
			break;
		case 9463:
			TeleportExecutor.teleport(player, new Position(2521, 4646, 0), true);
			break;
		default:
			player.sendMessage("Could not find a teleportation location.");
			break;
		}

	}

	public void useOperate(int itemId) {
		Optional<DegradableItem> d = DegradableItem.forId(itemId);
		if (d.isPresent()) {
			Degrade.checkPercentage(player, itemId);
			return;
		}
		switch (itemId) {
		case 4202:
			startSlayerTeleport();
			break;
		case 11283:
			DragonfireShieldEffect dfsEffect = new DragonfireShieldEffect();
			if (player.npcIndex <= 0 && player.playerIndex <= 0) {
				return;
			}
			if (dfsEffect.isExecutable(player)) {
				Damage damage = new Damage(Misc.random(25));
				if (player.playerIndex > 0) {
					Player target = PlayerHandler.players[player.playerIndex];
					if (Objects.isNull(target)) {
						return;
					}
					player.attackTimer = 7;
					dfsEffect.execute(player, target, damage);
					player.setLastDragonfireShieldAttack(System.currentTimeMillis());
				} else if (player.npcIndex > 0) {
					NPC target = NPCHandler.npcs[player.npcIndex];
					if (Objects.isNull(target)) {
						return;
					}
					player.attackTimer = 7;
					dfsEffect.execute(player, target, damage);
					player.setLastDragonfireShieldAttack(System.currentTimeMillis());
				}
			}
			break;

		case 1712:
		case 1710:
		case 1708:
		case 1706:
			player.getPA().handleGlory(itemId);
			player.itemUsing = itemId;
			player.isOperate = true;
			break;
		case 2552:
		case 2554:
		case 2556:
		case 2558:
		case 2560:
		case 2562:
		case 2564:
		case 2566:
			TeleportExecutor.teleport(player, new Position(3362, 3263, 0), true);
			break;
		}
	}

	public void getSpeared(int otherX, int otherY, int distance) {
		int x = player.absX - otherX;
		int y = player.absY - otherY;
		int xOffset = 0;
		int yOffset = 0;
		DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(session) && session.getStage().getStage() == MultiplayerSessionStage.FURTHER_INTERACTION) {
			player.sendMessage("You cannot use this special whilst in the duel arena.");
			return;
		}
		if (x > 0) {
			if (Region.getClipping(player.getX() + distance, player.getY(), player.heightLevel, 1, 0)) {
				xOffset = distance;
			}
		} else if (x < 0) {
			if (Region.getClipping(player.getX() - distance, player.getY(), player.heightLevel, -1, 0)) {
				xOffset = -distance;
			}
		}
		if (y > 0) {
			if (Region.getClipping(player.getX(), player.getY() + distance, player.heightLevel, 0, 1)) {
				yOffset = distance;
			}
		} else if (y < 0) {
			if (Region.getClipping(player.getX(), player.getY() - distance, player.heightLevel, 0, -1)) {
				yOffset = -distance;
			}
		}
		moveCheck(xOffset, yOffset, distance);
		player.lastSpear.reset();
	}

	public void moveCheck(int x, int y, int distance) {
		PathFinder.getPathFinder().findRoute(player, player.getX() + x, player.getY() + y, true, 1, 1);
	}

	public void resetTzhaar() {
		player.waveId = -1;
		player.tzhaarToKill = -1;
		player.tzhaarKilled = -1;
	}

	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y)
					return true;
			}
		}
		return false;
	}

	public void checkPouch(int i) {
		if (i < 0)
			return;
		player.sendMessage("This pouch has " + player.pouches[i] + " rune ess in it.");
	}

	public void fillPouch(int i) {
		if (i < 0)
			return;
		int toAdd = player.POUCH_SIZE[i] - player.pouches[i];
		if (toAdd > player.getItems().getItemAmount(1436)) {
			toAdd = player.getItems().getItemAmount(1436);
		}
		if (toAdd > player.POUCH_SIZE[i] - player.pouches[i])
			toAdd = player.POUCH_SIZE[i] - player.pouches[i];
		if (toAdd > 0) {
			player.getItems().deleteItem(1436, toAdd);
			player.pouches[i] += toAdd;
		}
	}

	public void emptyPouch(int i) {
		if (i < 0)
			return;
		int toAdd = player.pouches[i];
		if (toAdd > player.getItems().freeSlots()) {
			toAdd = player.getItems().freeSlots();
		}
		if (toAdd > 0) {
			player.getItems().addItem(1436, toAdd);
			player.pouches[i] -= toAdd;
		}
	}

	/*
	 * public void fixAllBarrows() { int totalCost = 0; int cashAmount =
	 * c.getItems().getItemAmount(995); for (int j = 0; j <
	 * c.playerItems.length; j++) { boolean breakOut = false; for (int i = 0; i
	 * < c.getItems().brokenBarrows.length; i++) { if (c.playerItems[j]-1 ==
	 * c.getItems().brokenBarrows[i][1]) { if (totalCost + 80000 > cashAmount) {
	 * breakOut = true; c.sendMessage("You have run out of money."); break; }
	 * else { totalCost += 80000; } c.playerItems[j] =
	 * c.getItems().brokenBarrows[i][0]+1; } } if (breakOut) break; } if
	 * (totalCost > 0) c.getItems().deleteItem(995,
	 * c.getItems().getItemSlot(995), totalCost); }
	 */

	public void handleLoginText() {

		/*
		 * STRING ID; NAME : DESCRIPTION
		 * 
		 * - Ancients - Paddawa: 21833 : 21834 Sennisten: 21933 : 21934
		 * Kharyrll: 22053 Lassar: 22123 : 22124 Dareeyak: 22232 : 22233
		 * Carrallangar: 22307 : 22308 Annakarl: 22415 : 22416 Ghorrock: 22490 :
		 * 22491
		 * 
		 * - Normal Magic - Varrock: 19641 : 19642 Lumbridge: 19722 : 19723
		 * Falador: 19803 : 19804 House Teleport: 19877 : 19878 Camelot: 19960 :
		 * 19961 Ardougne: 20195 : 20196 Watchtower: 20354: 20355 Trollheim:
		 * 20570: 20571
		 */

		// modern
		player.getPA().sendFrame126("@gre@PVP Teleport", 19641); // varrock
		player.getPA().sendFrame126("List of PVP Teleports", 19642); // varrock
																		// description
		//player.sendMessage("Disabled", 19722); // lumbridge
		//player.getPA().sendFrame126("List of Training Teleports", 19723); // lumbridge
																			// description
		player.getPA().sendFrame126("@gre@Boss Teleport", 19803); // falador
		player.getPA().sendFrame126("List of Boss Teleports", 19804); // falador
																		// description
		player.getPA().sendFrame126("@gre@Minigame Teleports", 19960); // camelot
		player.getPA().sendFrame126("List of Minigame Teleports", 19961); // camelot
																			// description
		player.getPA().sendFrame126("@gre@Skilling Teleport", 20195); // ardougne
		player.getPA().sendFrame126("Skilling Area", 20196); // ardougne
																// description
		player.getPA().sendFrame126("Watchtower Teleport", 20354); // watchtower
		player.getPA().sendFrame126("Teleports you to Watchtower", 20355); // watchtower
																			// description
		player.getPA().sendFrame126("Trollheim Teleport", 20570); // trollheim
		player.getPA().sendFrame126("Teleports you to Trollheim", 20571); // trollheim
																			// description

		// ancients
		player.getPA().sendFrame126("@gre@PVP Teleport", 21833); // paddewwa
		player.getPA().sendFrame126("List of PVP Teleports", 21834); // paddewwa
		//player.getPA().sendFrame126("@gre@Training Teleport", 21933); // senntisten
		//player.getPA().sendFrame126("List of Training Teleports", 21934); // senntisten
																			// description
		player.getPA().sendFrame126("@gre@Boss Teleport", 22053); // kharyll
		player.getPA().sendFrame126("@gre@Minigame Teleport", 22123); // lassar
		player.getPA().sendFrame126("List of Minigame Teleports", 22124); // lassar
																			// description
		player.getPA().sendFrame126("@gre@Skilling Teleport", 22232); // dareeyak
		player.getPA().sendFrame126("Teleports you to Skilling Area", 22233); // dareeyak
																				// description
		player.getPA().sendFrame126("Carralanger Teleport", 22307); // carrallanger
		player.getPA().sendFrame126("Teleports you to Carralanger", 22308); // carralanger
																			// description
		/*
		 * c.getPA().sendFrame126("Teleport name", 13089); //annakarl
		 * c.getPA().sendFrame126("Description", 13090); //annakarl description
		 * c.getPA().sendFrame126("Teleport name", 13097); //ghorrock
		 * c.getPA().sendFrame126("Description", 13098); //ghorrock description
		 */

		// lunar
		player.getPA().sendFrame126("@gre@PVP Teleport", 21833); // paddewwa
		player.getPA().sendFrame126("List of PVP Teleports", 21834); // paddewwa
		//player.getPA().sendFrame126("@gre@Training Teleport", 21933); // senntisten
		//player.getPA().sendFrame126("List of Training Teleports", 21934); // senntisten
																			// description
		player.getPA().sendFrame126("@gre@Boss Teleport", 22053); // kharyll
		player.getPA().sendFrame126("@gre@Minigame Teleport", 22123); // lassar
		player.getPA().sendFrame126("List of Minigame Teleports", 22124); // lassar
																			// description
		player.getPA().sendFrame126("@gre@Skilling Teleport", 22232); // dareeyak
		player.getPA().sendFrame126("Teleports you to Skilling Area", 22233); // dareeyak
																				// description
		player.getPA().sendFrame126("Carralanger Teleport", 22307); // carrallanger
		player.getPA().sendFrame126("Teleports you to Carralanger", 22308); // carralanger
																			// description
	}

	public void handleWeaponStyle() {
		if (player.fightMode == 0) {
			player.getPA().sendFrame36(43, player.fightMode);
		} else if (player.fightMode == 1) {
			player.getPA().sendFrame36(43, 3);
		} else if (player.fightMode == 2) {
			player.getPA().sendFrame36(43, 1);
		} else if (player.fightMode == 3) {
			player.getPA().sendFrame36(43, 2);
		}
	}

	/**
	 * 
	 * @author Jason MacKeigan (http://www.rune-server.org/members/jason)
	 * @date Sep 26, 2014, 12:57:42 PM
	 */
	public enum PointExchange {
		PK_POINTS, VOTE_POINTS, DONATOR_POINTS;
	}

	/**
	 * Exchanges all items in the player owners inventory to a specific to
	 * whatever the exchange specifies. Its up to the switch statement to make
	 * the conversion.
	 * 
	 * @param pointVar
	 *            the point exchange we're trying to make
	 * @param itemId
	 *            the item id being exchanged
	 * @param exchangeRate
	 *            the exchange rate for each item
	 */
	public void exchangeItems(PointExchange pointVar, int itemId, int exchangeRate) {
		try {
			int amount = player.getItems().getItemAmount(itemId);
			String pointAlias = Misc.ucFirst(pointVar.name().toLowerCase().replaceAll("_", " "));
			if (exchangeRate <= 0 || itemId < 0) {
				throw new IllegalStateException();
			}
			if (amount <= 0) {
				player.getDH().sendStatement("You do not have the items required to exchange",
						"for " + pointAlias + ".");
				player.nextChat = -1;
				return;
			}
			if (player.inWild()) {
				player.getDH().sendStatement("You cannot claim pkp in the wilderness.");
				player.nextChat = -1;
				return;
			}
			int exchange = amount * exchangeRate;
			player.getItems().deleteItem2(itemId, amount);
			switch (pointVar) {
			case PK_POINTS:
				player.pkp += exchange;
				break;

			case VOTE_POINTS:
				player.votePoints += exchange;
				break;

			case DONATOR_POINTS:
				player.donatorPoints += exchange;
				break;
			}
			player.getDH()
					.sendStatement("You exchange " + amount + " tickets for " + exchange + " " + pointAlias + ".");
			player.nextChat = -1;
		} catch (IllegalStateException exception) {
			Misc.println("WARNING: Illegal state has been reached.");
			exception.printStackTrace();
		}
	}

	/**
	 * Sends some information to the client about screen fading.
	 * 
	 * @param text
	 *            the text that will be displayed in the center of the screen
	 * @param state
	 *            the state should be either 0, -1, or 1.
	 * @param seconds
	 *            the amount of time in seconds it takes for the fade to
	 *            transition.
	 *            <p>
	 *            If the state is -1 then the screen fades from black to
	 *            transparent. When the state is +1 the screen fades from
	 *            transparent to black. If the state is 0 all drawing is
	 *            stopped.
	 */
	public void sendScreenFade(String text, int state, int seconds) {
		if (player == null || player.getOutStream() == null) {
			return;
		}
		if (seconds < 1 && state != 0) {
			throw new IllegalArgumentException("The amount of seconds cannot be less than one.");
		}
		player.getOutStream().createFrameVarSize(9);
		player.getOutStream().writeString(text);
		player.getOutStream().writeByte(state);
		player.getOutStream().writeByte(seconds);
		player.getOutStream().endFrameVarSize();
	}

	public void stillCamera(int x, int y, int height, int speed, int angle) {
		player.outStream.createFrame(177);
		player.outStream.writeByte(x / 64);
		player.outStream.writeByte(y / 64);
		player.outStream.writeWord(height);
		player.outStream.writeByte(speed);
		player.outStream.writeByte(angle);
	}

	public void spinCamera(int i1, int i2, int i3, int i4, int i5) {
		player.outStream.createFrame(166);
		player.outStream.writeByte(i1);
		player.outStream.writeByte(i2);
		player.outStream.writeWord(i3);
		player.outStream.writeByte(i4);
		player.outStream.writeByte(i5);
	}

	public void resetCamera() {
		player.outStream.createFrame(107);
		player.updateRequired = true;
		player.appearanceUpdateRequired = true;
	}

	public void sendNpcModelOnInterface(int i, int npc) {
		sendFrame75(npc, i);
	}

	public void makePrimordialBoots(Player c) {
		if (c.getItems().playerHasItem(13231, 1) && c.getItems().playerHasItem(11840, 1)) {
			if (c.playerLevel[c.playerMagic] < 60 || c.playerLevel[c.playerRunecrafting] < 60) {
				c.sendMessage("You need both a magic and runecrafting level of 60 to do this.");
				return;
			}
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free inventory slot to do this.");
				return;
			}
			c.getItems().deleteItem(13231, 1);
			c.getItems().deleteItem(11840, 1);
			c.getItems().addItem(13239, 1);
			c.getPA().addSkillXP(200, c.playerRunecrafting);
			c.getPA().addSkillXP(200, c.playerMagic);
			c.sendMessage("You make primordial boots and gain 200 experience in both skills.");
		}
	}

	public void makePegasianBoots(Player c) {
		if (c.getItems().playerHasItem(13229, 1) && c.getItems().playerHasItem(2577, 1)) {
			if (c.playerLevel[c.playerMagic] < 60 || c.playerLevel[c.playerRunecrafting] < 60) {
				c.sendMessage("You need both a magic and runecrafting level of 60 to do this.");
				return;
			}
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free inventory slot to do this.");
				return;
			}
			c.getItems().deleteItem(13229, 1);
			c.getItems().deleteItem(2577, 1);
			c.getItems().addItem(13237, 1);
			c.getPA().addSkillXP(200, c.playerRunecrafting);
			c.getPA().addSkillXP(200, c.playerMagic);
			c.sendMessage("You make pegasian boots and gain 200 experience in both skills.");
		}
	}

	public void makeEternalBoots(Player c) {
		if (c.getItems().playerHasItem(13227, 1) && c.getItems().playerHasItem(6920, 1)) {
			if (c.playerLevel[c.playerMagic] < 60 || c.playerLevel[c.playerRunecrafting] < 60) {
				c.sendMessage("You need both a magic and runecrafting level of 60 to do this.");
				return;
			}
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free inventory slot to do this.");
				return;
			}
			c.getItems().deleteItem(13227, 1);
			c.getItems().deleteItem(6920, 1);
			c.getItems().addItem(13235, 1);
			c.getPA().addSkillXP(200, c.playerRunecrafting);
			c.getPA().addSkillXP(200, c.playerMagic);
			c.sendMessage("You make eternal boots and gain 200 experience in both skills.");
		}
	}

	public void makeSlayerHelm(Player c) {
		if (c.getItems().playerHasItem(8901, 1) && c.getItems().playerHasItem(4166, 1)
				&& c.getItems().playerHasItem(4164, 1) && c.getItems().playerHasItem(4168, 1)
				&& c.getItems().playerHasItem(4551, 1) && c.getItems().playerHasItem(4155, 1)) {
			if (c.getItems().freeSlots() <= 1) {
				c.sendMessage("You need two free inventory slots to do this.");
				return;
			}
			if (c.playerLevel[Player.playerCrafting] < 55) {
				c.sendMessage("You need a crafting level of 55 to do this.");
				return;
			}
			int amount = 1;
			c.getItems().deleteItem(8901, amount);
			c.getItems().deleteItem(4166, amount);
			c.getItems().deleteItem(4164, amount);
			c.getItems().deleteItem(4168, amount);
			c.getItems().deleteItem(4551, amount);
			c.getItems().deleteItem(4155, amount);
			c.getItems().addItem(11864, amount);
			c.sendMessage("You successfully make a slayer helmet!");
		} else {
			c.sendMessage("You need: a black mask(10), pair of earmuffs, a facemask, a nose peg,");
			c.sendMessage("a spiny helmet and an enchanted gem to make this.");
		}
	}

	public void makeAvasAccumulator(Player c) {
		if (c.getItems().playerHasItem(1742, 10) && c.getItems().playerHasItem(995, 999)
				&& c.getItems().playerHasItem(886, 100)) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free inventory slot to do this.");
				return;
			}
			c.getItems().deleteItem(1742, 10);
			c.getItems().deleteItem(995, 999);
			c.getItems().deleteItem(886, 100);
			c.getItems().addItem(10499, 1);
			c.getDH().sendNpcChat1("Your ava's accumulator will now be in your inventory!", 4407,
					NPCHandler.getNpcName(4407));
		} else
			c.getDH().sendNpcChat1("You do not have the required materials, sorry.", 4407,
					NPCHandler.getNpcName(4407));
	}
}
