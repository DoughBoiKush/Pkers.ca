package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Teleports the player to the given coordinate. Note: to teleport by region, separate args[0] 
 * with commas (i.e. - <i>0,50,53</i>, wherein <b>0 is the <i>height</i></b>, <b>50 is the <i>regionX</i></b>, 
 * and <b>53 is the <i>regionY</i></b>).
 * @author Chris
 * @date Aug 24, 2015 5:15:22 PM
 *
 */
public class Tele implements Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		if (args.length > 1) {
			switch (args.length) {
			case 2:
				c.getPA().movePlayer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), c.heightLevel);
				break;
			case 3:
				c.getPA().movePlayer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
				break;
			}
		} else {
		String[] coords = args[0].split(",");
		int height = Integer.parseInt(coords[0]);
		int smallX = Integer.parseInt(coords[1]);
		int smallY = Integer.parseInt(coords[2]);
		switch (coords.length) {
			//Simple region - syntax x,y,height
			case 2:
				c.getPA().movePlayer(smallX + (c.getPosition().getRegionX() * 8), smallY + (8 * c.getPosition().getRegionY()), c.heightLevel);
				break;
			case 3:
				c.getPA().movePlayer(smallX + (c.getPosition().getRegionX() * 8), smallY + (8 * c.getPosition().getRegionY()), height);
				break;
			case 4:
				//c.getPA().movePlayer(smallX + (c.getPosition().getRegionX() * 8), smallY + (8 * c.getPosition().getRegionY()), height);
				break;
			}	
		} 
	}
}
