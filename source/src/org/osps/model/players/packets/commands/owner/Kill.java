package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.combat.Hitmark;
import org.osps.model.players.packets.commands.Command;

/**
 * Kill a player.
 * 
 * @author Emiel
 */
public class Kill implements Command {

	@Override
	public void execute(Player c, String input) {
		Player player = PlayerHandler.getPlayer(input);
		if (player == null) {
			c.sendMessage("Player is null.");
			return;
		}
		player.appendDamage(player.playerLevel[3], Hitmark.HIT);
		player.getPA().refreshSkill(3);
		player.sendMessage("You have been merked by " + c.playerName + ".");
	}
}
