package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;

/**
 * Changes the password of the player.
 * 
 * @author Emiel
 *
 */
public class Changepassword implements Command {

	@Override
	public void execute(Player c, String input) {
		if(input.length() < 4) {
			c.sendMessage("Your password must contain atleast 4 characters.");
			return;
		}
		if (input.length() > 20) {
			c.sendMessage("Passwords cannot contain more than 20 characters.");
			c.sendMessage("The password you tried had " + input.length() + " characters.");
			return;
		}
		if (input.contains("character-rights") || input.contains("[CHARACTER]") || input.contains(".") || input.contains("_")) {
			c.sendMessage("Your password contains illegal characters.");
			return;
		}
		c.playerPass = Misc.getFilteredInput(input).toLowerCase();
		c.sendMessage("Your password is now: @red@" + c.playerPass);
	}
}