package org.osps.model.players.packets.commands.owner;

import org.osps.Config;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Toggle the bonus XP weekend on or off.
 * 
 * @author Emiel
 *
 */
public class Bonus implements Command {

	@Override
	public void execute(Player c, String input) {
		if (Config.BONUS_WEEKEND) {
			Config.BONUS_WEEKEND = false;
			c.sendMessage("BONUS WEEKEND TOGGLED OFF");
		} else {
			Config.BONUS_WEEKEND = true;
			c.sendMessage("BONUS WEEKEND TOGGLED ON");
		}
	}
}
