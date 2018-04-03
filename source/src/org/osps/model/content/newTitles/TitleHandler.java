package org.osps.model.content.newTitles;

import org.osps.model.players.Player;

public class TitleHandler {
	
	public static String[][] titles = {
		{"Lord", "Lady", "Sir", "Dame", "Cheerful", "Grumpy", "Duderino", "Dudette", "Crusader", "Doctor" , "Baron" , "Baroness" , "Overlord" , "Bandito" , "Master" , "Miss" , "Prince" , "Princess" , "The Undefeated" , "God of War",}, 
		{"Esquire", "The", "Emperor", "Empress", "The Fallen", "The Warrior", "The Mysterious"}, 
		{"Desperado", "The Magnificent", "The Awesome", "The Strange", "Wunderkind", "Lionheart", "Hellraiser", "Swagmaster", "Queen", "King",}
		};
	
	public static int[][] titlePrices = {
		{1000, 1000, 2500, 2500, 5000, 5000, 7500, 7500, 10000, 10000, 15000, 15000, 20000, 20000, 25000, 25000, 50000, 50000, 100000, 250000},
		{50, 50, 100, 100, 150, 200, 250},
		{100, 500, 750, 1250, 1500, 2000, 2500, 5000, 6000, 6000},
		};
	
	public static void open(Player c) {
		c.getPA().sendFrame126("DP:"+c.donatorPoints, 26006);
		c.getPA().sendFrame126("PKP:"+c.pkp, 26007);
		c.getPA().sendFrame126("VP:"+c.votePoints, 26008);
		String bonus = "";
		if (c.getRights().GotDollarMark()) {
			bonus = "@yel@[$] ";
		}
		c.getPA().sendFrame126(""+bonus+"@or2@"+c.title+" @whi@"+c.playerName+" @yel@(level-"+c.combatLevel+")", 26009);
		c.getPA().sendFrame126("@or1@No Title Selected", 26010);
                
                int cur = 0;
                for (int i = 0; i < titles.length; i++) {
                    for (int b = 0; b <titles[i].length; b++) {
                        if (c.titleUnlocked[i][b]) {
                            c.getPA().sendFrame126("USE", 26121 + cur);
                        }
                        cur++;
                    }
                }
		
		c.getPA().showInterface(26000);
	}
	
	
	public static boolean clickButton(Player c, int button) {
		if (button >= 102009 && button <= 102070) {
			clickTitle(c, button - 102009);
			return true;
		}
		if (button == 102088) {
			c.title = "";
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
			open(c);
			c.sendMessage("@red@You have removed your title!");
			return true;
		}
		if (button == 102091) {
			buy(c);
			c.updateRequired = true;
			c.setAppearanceUpdateRequired(true);
			return true;
		}
		return false;
	}
	
	public static void clickTitle(Player c, int id) {
		int current = 0;
		for (int i = 0; i < titles.length; i++) {
			for (int j = 0; j < titles[i].length; j++) {
				if (current == id) {
					selectTitle(c, i, j);
					return;
				}
				current++;
			}
		}
	}
	
	
	public static void selectTitle(Player c, int group, int id) {
		String bonus = "";
		if (c.getRights().GotDollarMark()) {
			bonus = "@yel@[$] ";
		}
		c.getPA().sendFrame126(""+bonus+"@or2@"+titles[group][id]+" @whi@"+c.playerName+" @yel@(level-"+c.combatLevel+")", 26009);
		String[] currency = {"Pk Points", "Donator Points", "Vote Points"};
		c.getPA().sendFrame126(""+titlePrices[group][id]+" "+currency[group], 26010);
		c.titleView = new int[] {group, id};
		if (c.titleUnlocked[group][id]) {
			c.getPA().sendFrame126("USE", 26207);
		} else {
			c.getPA().sendFrame126("BUY", 26207);
		}
	}
	
	public static void buy(Player c) {
		if (c.titleUnlocked[c.titleView[0]][c.titleView[1]]) {
			c.title = titles[c.titleView[0]][c.titleView[1]];
			c.sendMessage("Your title has been changed.");
		} else {
			if (c.titleView[0] == 0) {
				if (c.pkp >= titlePrices[c.titleView[0]][c.titleView[1]]) {
					c.pkp -= titlePrices[c.titleView[0]][c.titleView[1]];
					c.title = titles[c.titleView[0]][c.titleView[1]];
					c.sendMessage("Your title has been changed.");
					c.titleUnlocked[c.titleView[0]][c.titleView[1]] = true;
					open(c);
				} else {
					c.sendMessage("You don't have enough points to buy this item.");
				}
			} else if (c.titleView[0] == 1) {
				if (c.donatorPoints >= titlePrices[c.titleView[0]][c.titleView[1]]) {
					c.donatorPoints -= titlePrices[c.titleView[0]][c.titleView[1]];
					c.title = titles[c.titleView[0]][c.titleView[1]];
					c.sendMessage("Your title has been changed.");
					c.titleUnlocked[c.titleView[0]][c.titleView[1]] = true;
					open(c);
				} else {
					c.sendMessage("You don't have enough points to buy this item.");
				}
			} else if (c.titleView[0] == 2) {
				if (c.votePoints >= titlePrices[c.titleView[0]][c.titleView[1]]) {
					c.votePoints -= titlePrices[c.titleView[0]][c.titleView[1]];
					c.title = titles[c.titleView[0]][c.titleView[1]];
					c.sendMessage("Your title has been changed.");
					c.titleUnlocked[c.titleView[0]][c.titleView[1]] = true;
					open(c);
				} else {
					c.sendMessage("You don't have enough points to buy this item.");
				}
			}
		}
	}
	
}
