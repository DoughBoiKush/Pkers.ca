package org.osps.model.players.packets.commands;

import org.osps.model.players.Player;

public interface Command {
	
	public void execute(Player c, String input);

}
