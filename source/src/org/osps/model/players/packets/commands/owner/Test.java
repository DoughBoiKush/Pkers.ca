package org.osps.model.players.packets.commands.owner;

import org.osps.Config;
import org.osps.model.items.ItemDefinition;
import org.osps.model.npcs.boss.abyssalsire.AbyssalSire;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;

/**
 * Promote a given player to a Sponsor.
 * 
 * @author Micheal
 *
 */
public class Test implements Command {

	@Override
	public void execute(Player c, String input) {
		if (Config.doublePKPointsWeekend) {
			Config.doublePKPointsWeekend = false;
			PlayerHandler.executeGlobalMessage("<img=14><col=0f12c5> [NEWS] </col> <col=800000>Double PK Points have been disabled by " +c.playerName, false);
		} else {
			Config.doublePKPointsWeekend = true;
			PlayerHandler.executeGlobalMessage("<img=14><col=0f12c5> [NEWS] </col> <col=800000>Double PK Points have been enabled by " +c.playerName, false);
		}
	}
}