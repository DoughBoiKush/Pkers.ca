package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Open the banking interface.
 * 
 * @author Emiel
 */
public class Bank implements Command {

	@Override
	public void execute(Player c, String input) {
		if (c.getRights().HigherDonator()) {
			   c.sendMessage("You need to be higher rank than Donator to use this.");
			   return;
			}
	     if (c.inTrade || c.inDuelArena()) {
	            return;
	        }
	        if (c.inWild()) {
	            c.sendMessage("You can't do this while in the wilderness.");
	            return;
	        }
	        c.getPA().openUpBank();
	    }
	  }
