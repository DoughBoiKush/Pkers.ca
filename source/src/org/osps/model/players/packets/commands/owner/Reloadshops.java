package org.osps.model.players.packets.commands.owner;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Reload all shops.
 * 
 * @author Emiel
 *
 */
public class Reloadshops implements Command {

	@Override
	public void execute(Player c, String input) {
		Server.shopHandler = new org.osps.world.ShopHandler();
		c.sendMessage("Reloaded shops.");
	}
}
