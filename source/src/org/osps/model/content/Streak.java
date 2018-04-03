package org.osps.model.content;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;

/**
 * Killing Streak System
 * 
 * @author Rene
 **/

public class Streak {

	Player c;

	public Streak(Player c) {
		this.c = c;
	}

	/**
	 * Handles the message being sent to all players online.
	 */

	public void yell(String msg) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = (Player) PlayerHandler.players[j];
				c2.sendMessage(msg);
			}
		}
	}

	/**
	 * Checks the players kill streak and sends a message to all players.
	 */

	public void checkKillStreak() {
		int bounty = (c.killStreak * 2 + 8);
		if (c.killStreak >= 5) {
			yell("[@blu@STREAK@bla@]@red@" + c.playerName + "@bla@ is on a kill streak, and has killed @blu@" + c.killStreak + "@bla@ players!");
			yell("[@blu@STREAK@bla@]Current Bounty: @blu@" + bounty + "@bla@ PK Points.");
		}
	}


	
	/**
	 * Checks the how many players have killed for Achievement.
	 */

	public void checkPlayerKilleds() {
		if ((c.KC == 500) || (c.KC == 750) || (c.KC == 1000) || (c.KC == 1250) || (c.KC ==1500) || (c.KC == 1750) || (c.KC == 2000) || (c.KC == 2250) || (c.KC == 2500) || (c.KC == 2750) || (c.KC == 3000)
				|| (c.KC == 3250) || (c.KC == 3500) || (c.KC == 3750) || (c.KC == 4000) || (c.KC == 4250) || (c.KC == 4500) || (c.KC == 4750) || (c.KC == 5000)) {
			    for (int i1 = 0; i1 <= PlayerHandler.getPlayerCount(); i1++) {
				if (PlayerHandler.players[i1]!=null){
					Player all = (Player) PlayerHandler.players[i1];
					all.sendMessage("[@or2@Achievement@bla@]@red@"+c.playerName+" @bla@has just reached @red@"+c.KC+" @bla@kills!");
		    	}
			}
		}
	}
	/**
	 * Will eventually be used to change the skulls of the person on a kill streak.
	 */

	public void drawSkulls() {
		if (c.killStreak >= 5 && c.killStreak <= 9) {
			c.bhSkull = 3;
		} else if (c.killStreak >= 10 && c.killStreak <= 14) {
			c.bhSkull = 4;
		} else if (c.killStreak >= 15 && c.killStreak <= 19) {
			c.bhSkull = 5;
		} else if (c.killStreak >= 20 && c.killStreak <= 24) {
			c.bhSkull = 6;
		} else if (c.killStreak >= 25) {
			c.bhSkull = 7;
		}
		c.getPA().requestUpdates();
	}

	/**
	 * Rewards the killer with the points equivalent to the players kill streak.
	 * resets the victims kill streak back to 0.
	 * sends a message to all online players notifying them the player with a kill streak has been killed and who by.
	 */

	public void endStreak() {
		Player o = PlayerHandler.getPlayer(c.getKiller());
			if (c.getKiller() != null && o != null) {	
		}
	}
}