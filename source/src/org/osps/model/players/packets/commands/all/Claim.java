package org.osps.model.players.packets.commands.all;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Checks if the player has unclaimed donations.
 * 
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 */
public class Claim implements Command {
	

	@Override
	public void execute(Player c, String input) {
		 
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				try {
				
					URL url = new URL("http://app.gpay.io/api/runescape/\"+username+"+c.playerName.replaceAll(" ", "_"));
			        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	
			        String string;
			        int tokens = 0;
			        
			        while((string = in.readLine()) != null) {
			        	
			        	int id = Integer.parseInt(string);
			        	
			        	switch(id) {
			        	case 0:
			        		tokens += 100;
			        		break;
			        	case 1:
			        		tokens += 500;
			        		break;
			        	case 2:
			        		tokens += 1000;
			        		break;
			        	case 3:
			        		tokens += 5000;
			        		break;
			        	case 4:
			        		tokens += 10000;
			        		break;
			        	case 5:
			        		tokens += 20000;
			        		break;
			        	}
			        	
			        }
			        
			        c.donatorPoints += tokens;
			        c.sendMessage("You have been awarded "+tokens+" for donating.");
			            
			        in.close();
		        
				} catch(Throwable t) {
					
				}
				
			}
			
		}).start();
		
	}
}
