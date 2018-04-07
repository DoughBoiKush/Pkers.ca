package org.osps;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Misc;

import com.rspserver.motivote.MotivoteHandler;
import com.rspserver.motivote.Reward;

public class RewardHandler extends MotivoteHandler<Reward> {
	@Override
	public void onCompletion(Reward reward) {
		// SOME OF THIS CODE WILL BE DIFFERENT FOR YOUR SERVER, CHANGE IT
		// ACCORDINGLY. everything to do with motivote will stay the same!
		int itemID = -1;
		
		if (reward.rewardName().equalsIgnoreCase("Vote Book"))
		{
			itemID = 10594;
			
		}

		if (PlayerHandler.isPlayerOn(reward.username())) {
			Player p = PlayerHandler.getPlayer(reward.username());

			if (p != null && p.isActive == true) // check isActive to make sure
													// player is active. some
													// servers, like project
													// insanity, need extra
													// checks.
			{
				synchronized (p) {
					Player c = p; 
					if (c.getItems().addItem(itemID, reward.amount())) {
						c.votePoints += 1;
						for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Player c2 = PlayerHandler.players[j];
								c2.sendMessage("<img=10> <col=255>" + Misc.capitalize(c.playerName)
										+ " <col=0>has just voted for the server and been rewarded! <col=255>::vote <img=10>");
							}
						}
						c.sendMessage("<col=255>You've received your vote reward! Congratulations!");
						reward.complete();
					} else {
						c.sendMessage("<col=255>Could not give you your reward item, try creating space.");
					}
				}
			}
		}
	}
}