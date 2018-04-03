package org.osps.model.players.packets.commands.all;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Opens the vote page in the default web browser.
 * 
 * @author Emiel
 */
public class Vote implements Command {

	@Override
	public void execute(Player c, String input) {
		c.getPA().sendFrame126("https://www.osps.motivoters.com/motivote/", 12000);
	}
}
