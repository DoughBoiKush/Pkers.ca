package org.osps.model.players.packets.commands.developer;

import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Endhunger implements Command {

	@Override
	public void execute(Player c, String input) {
		HungerManager.getSingleton().endGame(true);
	}


}
