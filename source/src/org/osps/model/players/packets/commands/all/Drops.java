package org.osps.model.players.packets.commands.all;

import org.osps.model.npcs.drops.DropList;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Drops implements Command {

	@Override
	public void execute(Player c, String input) {
		final String query = input.toLowerCase().replace("drops ", "");
		if (query.length() > 0) {
			try {
				DropList.displayNPCDrops(c, query);
			} catch (Throwable error) {
				c.sendMessage("Could not display npc drops for npc: " + query + ".");
				error.printStackTrace();
			}
		} else {
			c.sendMessage("Use ::drops npcname.");
		}
	}
}
