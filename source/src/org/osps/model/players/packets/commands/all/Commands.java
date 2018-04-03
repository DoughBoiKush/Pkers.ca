package org.osps.model.players.packets.commands.all;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Shows a list of commands.
 * 
 * @author Emiel
 *
 */
public class Commands implements Command {

	@Override
	public void execute(Player c, String input) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		for (int i = 8144; i < 8195; i++) {
			c.getPA().sendFrame126("", i);
		}
		  c.sendMessage("::item,::getid,::food,::kdr,::resetkdr,::empty,::skull,::unskull");

          c.sendMessage("::players,::master,::brunes,::vrunes,::changepassword,::yell");


	}
}
