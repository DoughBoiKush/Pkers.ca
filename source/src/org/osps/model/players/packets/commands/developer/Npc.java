package org.osps.model.players.packets.commands.developer;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Spawn a specific Npc.
 * 
 * @author Emiel
 *
 */
public class Npc implements Command {

	@Override
	public void execute(Player c, String input) {
		int newNPC = Integer.parseInt(input);
		if (newNPC > 0) {
			//Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY, c.heightLevel, 0, 120, 7, 70, 70, false, false);
                        Server.npcHandler.spawnNpc(newNPC, c.absX, c.absY, c.heightLevel, 1, 1000, 84, 500, 500);
		} else {
			c.sendMessage("No such NPC.");
		}
	}
}
