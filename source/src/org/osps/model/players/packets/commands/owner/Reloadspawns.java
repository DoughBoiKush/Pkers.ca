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
public class Reloadspawns implements Command {

	@Override
	public void execute(Player c, String input) {
		NPCHandler.loadDefs();
		PlayerHandler.executeGlobalMessage("Reloaded spawns.", false);
	}
}
