package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.json.ItemDefinitionLoader;

/**
 * Reload the item and price configs.
 * 
 * @author Emiel
 *
 */
public class Reloaditems implements Command {

	@Override
	public void execute(Player c, String input) {
		// should really be done asynchronously...
		new ItemDefinitionLoader().load();
		
		c.sendMessage("Reloaded items.");
	}
}
