package org.osps.model.minigames.gamble;

import java.util.Random;

import org.osps.model.players.Player;

public class GamblePKP {
	
	public static void gamblePKP(Player player, int amount, int npcId) {
		if (player == null || player.disconnected || player.teleporting || player.isDead) {
			return;
		}
		Random random = new Random();
		if (player.pkp < amount) {
			player.getDH().sendDialogues(1050, npcId);
			return;
		}
		if (Integer.MAX_VALUE - amount < player.pkp) {
			player.getDH().sendDialogues(1051, npcId);
			return;
		}
		player.pkp -= amount;
		int roll = random.nextInt(100) + 1;
		if (roll > 54) {
			player.pkp += amount * 2;
			player.getDH().sendNpcChat("I rolled a " + roll + " and you doubled your bet!");
		} else {
			player.getDH().sendNpcChat("I rolled a " + roll + " and you lost your bet..");
		}
		player.getPA().updatePKP();
	}

}
