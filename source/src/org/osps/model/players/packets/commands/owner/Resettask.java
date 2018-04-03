package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

public class Resettask implements Command {

	@Override
	public void execute(Player player, String input) {
		Player target = PlayerHandler.getPlayer(input);
		if (target == null) {
			player.sendMessage("Player not found.");
		}
		
		target.bossSlayerTask = 0;
		target.bossTaskAmount = 0;
		target.slayerTask = 0;
		target.taskAmount = 0;
		
		player.sendMessage("Slayer task has been reset.");
		target.sendMessage("Your slayer task has been reset.");
	}

}
