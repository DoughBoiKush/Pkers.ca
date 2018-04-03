package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.Rights;
import org.osps.model.players.packets.commands.Command;

public class Promote implements Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split("-");
		if (args.length != 2) {
			c.sendMessage("The correct format is '::promote-name-rights'.");
			return;
		}
		Player player = PlayerHandler.getPlayer(args[0]);
		if (player == null) {
			c.sendMessage("The player '"+args[0]+"' could not be found, try again.");
			return;
		}
		int right;
		try {
			right = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			c.sendMessage("The level of rights must be a whole number.");
			return;
		}
		Rights rights = Rights.get(right);
		if (rights == null) {
			c.sendMessage("The level of rights you've requested is unknown.");
			return;
		}
		if (player.getRights().isStaff() && !c.getRights().isOwner()) {
			c.sendMessage("Only the CEO can modify the level of other staff.");
			return;
		}
		if (player.getRights().equals(rights)) {
			c.sendMessage("That player already has this level of rights.");
			return;
		}
		player.setRights(rights);
		player.properLogout = true;
		player.disconnected = true;
		c.sendMessage("You have promoted " + args[0] + " to " + rights.name() + ".");
	}

}
