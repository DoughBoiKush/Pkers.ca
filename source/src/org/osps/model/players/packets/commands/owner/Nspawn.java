package org.osps.model.players.packets.commands.owner;

import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Reload all NPCs.
 * 
 * @author Emiel
 *
 */
public class Nspawn implements Command {

	@Override
	public void execute(Player c, String input) {
		NPCHandler.loadDefs();
		PlayerHandler.executeGlobalMessage("[<col=255>" + c.playerName + "</col>] " + "NPC Spawns have been reloaded.", false);
	}
}
