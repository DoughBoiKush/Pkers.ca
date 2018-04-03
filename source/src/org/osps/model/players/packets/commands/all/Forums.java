package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Open the forums in the default web browser.
 * 
 * @author Emiel
 */
public class Forums implements Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().sendFrame126("www.forum.osps.com/", 12000);
	}
}
