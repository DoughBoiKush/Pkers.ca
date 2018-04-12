package org.osps.model.players.packets.commands.developer;

import org.osps.model.npcs.PetHandler;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Pickuppet implements Command {

	@Override
	public void execute(Player c, String input) {
		PetHandler.pickupPet(c, Integer.parseInt(input));
	}

}
