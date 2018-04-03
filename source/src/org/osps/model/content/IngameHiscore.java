package org.osps.model.content;

import java.util.ArrayList;
import java.util.Collections;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Misc;

public class IngameHiscore {

	private final static long UPDATEINTERVAL = 10000;

	private static long lastTimeUpdated = System.currentTimeMillis() - UPDATEINTERVAL;

	public static String[] mostWanted = new String[10];
	public static int[] topBounties = new int[10];

	private static void updateArrays() {
		/**
		 * If updated recently, do not update.
		 */
		if (System.currentTimeMillis() - lastTimeUpdated < UPDATEINTERVAL) {
			return;
		}
		lastTimeUpdated = System.currentTimeMillis();
		/**
		 * Reset bestOnlinePkers
		 */
		for (int i = 0; i < 10; i++) {
			mostWanted[i] = null;
		}
		/**
		 * Declare local ArrayList that holds the KillCounts of Players and the
		 * playerNames
		 */
		ArrayList<Integer> bounties = new ArrayList<Integer>();
		ArrayList<String> names = new ArrayList<String>();
		/**
		 * Put the kill counts from all players in an arrayList and their
		 * playerNames
		 */
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player p = (Player) PlayerHandler.players[j];
				if (p == null)
					continue;
				bounties.add(p.KC);
			}
		}
		/**
		 * Sort the arrayList
		 */
		Collections.sort(bounties);
		Collections.reverse(bounties);
		/**
		 * Get top 10
		 */
		int playersOnline = PlayerHandler.getPlayersOnline();
		int arraysize = playersOnline;
		if (playersOnline > 10)
			arraysize = 10;
		int[] top = new int[arraysize];

		for (int i = 0; i < arraysize; i++) {
			top[i] = bounties.get(i);
		}
		/**
		 * Find top 10 by userName and put in bestOnlinePkers.
		 */
		for (int i = 0; i < arraysize; i++) {
			for (Player p : PlayerHandler.getPlayers()) {
				if (top[i] == p.KC && !names.contains(p.playerName)) {
					names.add(p.playerName);
					mostWanted[i] = p.playerName;
					if(p.killStreak >= 5) {
						int bounty = (p.killStreak * 10 + Misc.random(10, 20));
						topBounties[i] = bounty;
					} else
					topBounties[i] = 0;
					break;
				}
			}
		}
	}

	public static final int[] QUEST_MENU_IDS = { 8145, 8147, 8148, 8149, 8150, 8151, 8152, 8153, 8154, 8155, 8156, 8157,
			8158, 8159, 8160, 8161, 8162, 8163, 8164, 8165, 8166, 8167, 8168, 8169, 8170, 8171, 8172, 8173, 8174, 8175,
			8176, 8177, 8178, 8179, 8180, 8181, 8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190, 8191, 8192, 8193,
			8194, 8195, 12174, 12175, 12176, 12177, 12178, 12179, 12180, 12181, 12182, 12183, 12184, 12185, 12186,
			12187, 12188, 12189, 12190, 12191, 12192, 12193, 12194, 12195, 12196, 12197, 12198, 12199, 12200, 12201,
			12202, 12203, 12204, 12205, 12206, 12207, 12208, 12209, 12210, 12211, 12212, 12213, 12214, 12215, 12216,
			12217, 12218, 12219, 12220, 12221, 12222, 12223 };
	
	public static String getRank(Player player){
		switch(player.getRights().getValue()) {
		case 0:
			return "";
		case 3:
			return "<img=3>";
		}
		return "";
	}

	public static void showScoreboard(Player player) {
		updateArrays();
		player.getPA().sendFrame126("Most wanted:", 8144);
		int i = 0;
		for (String name : mostWanted) {
			if (name == null)
				continue;
			player.getPA().sendFrame126("Rank: "+(i + 1)+"): " + name + "            Bounty: " + topBounties[i] + " Pk Points",
					QUEST_MENU_IDS[i++]);
		}
		for (; i < QUEST_MENU_IDS.length; i++) {
			player.getPA().sendFrame126("", QUEST_MENU_IDS[i]);
		}
		player.getPA().showInterface(8134);
	}
}
