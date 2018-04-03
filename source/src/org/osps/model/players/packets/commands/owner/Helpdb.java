package org.osps.model.players.packets.commands.owner;

import org.osps.model.content.help.HelpDatabase;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Opens an interface containing all help tickets.
 * 
 * @author Emiel
 */
public class Helpdb implements Command {

	@Override
	public void execute(Player c, String input) {
		HelpDatabase.getDatabase().openDatabase(c);
	}
}
