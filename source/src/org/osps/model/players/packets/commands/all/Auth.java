package org.osps.model.players.packets.commands.all;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;

/**
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 * 
 */
/*hmm*//*hmm*/
public class Auth implements Command {

	@Override
	public void execute(Player c, String input) {
		
		if(System.currentTimeMillis() - c.lastAttempt <= 10000) {
			c.sendMessage("You must wait 10 seconds before using this command again!");
			return;
		}
		
		if(c.getItems().freeSlots() < 3) {
			c.sendMessage("You need at least 3 free inventory slots to claim an auth.");
			return;
		}
		
		c.setLastAttempt(System.currentTimeMillis() + 10000);
		
		String auth = input.replace("Auth ", "");
		
		boolean success = Server.getMotivote().redeemVote(auth);
		
		if(success) {
			
			c.setLastVote(System.currentTimeMillis() + 14400000); 
			c.pkp += 3;
			c.getItems().addItem(10594, 1);
			PlayerHandler.executeGlobalMessage("<img=10></img><col=255>" + Misc.capitalize(c.playerName)
			+ " </col>has just voted and received <col=CC0000>3 PK Points & 1 Voting Book</col>.", false);
			c.getPA().loadQuests();
			
			return;
			
		}
		
		c.sendMessage("Invalid auth supplied, please try again later.");
		
		
	}
	
}
