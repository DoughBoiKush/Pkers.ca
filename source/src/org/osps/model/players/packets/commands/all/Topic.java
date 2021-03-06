package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Opens the store page in the default web browser.
 * 
 * @author Emiel
 */
public class Topic implements Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().sendFrame126("https://www.osps.com/forums/topic/"+ input +"-", 12000);
	}
}
