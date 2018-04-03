package org.osps.model.players.packets.commands.developer;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Show the current position.
 * 
 * @author Emiel
 *
 */
public class Pos implements Command {

	@Override
	public void execute(Player c, String input) {
		c.sendMessage("loc=[absX: " + c.absX + " absY:" + c.absY + " h:" + c.height + "] region= id: " + c.getPosition().getRegionId() + " " + c.getPosition().getZ() + "," + c.getPosition().getLocalX() + "," + c.getPosition().getLocalY());
	}
}
