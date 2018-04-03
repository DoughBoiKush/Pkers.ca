package org.osps.model.content.drops.vetion;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.items.ItemDefinition;
import org.osps.model.npcs.NPC;
import org.osps.model.players.Player;
import org.osps.util.Misc;


public class VDrops {
	
	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];
	
	private static int[] DROP_LIST_RARE = {11920, 12601, 10977
			
	};
	
	public static int RARE_DROP() {
		return DROP_LIST_RARE[(int)(Math.random()*DROP_LIST_RARE.length - 1)];
	}
	
	public final static void dropLoot(Player c, int i) {
		final int logs = Misc.random(200);
		final int coins = Misc.random(5);
		final int food = Misc.random(20);
		if (Misc.random(100) >= 0 && Misc.random(100) <= 25) {
			Server.itemHandler.createGroundItem(c, 1514, c.absX, c.absY, c.heightLevel, logs, c.index);
		} else 
		if (Misc.random(100) >= 26 && Misc.random(100) <= 36) {
				Server.itemHandler.createGroundItem(c, 1516, c.absX, c.absY, c.heightLevel, logs, c.index);
		} else 
		if (Misc.random(100) >= 37 && Misc.random(100) <= 49) {
				Server.itemHandler.createGroundItem(c, 995, c.absX, c.absY, c.heightLevel, coins, c.index);
		} else 
		if (Misc.random(100) >= 50 && Misc.random(100) <= 97) {
			Server.itemHandler.createGroundItem(c, 11937, c.absX, c.absY, c.heightLevel, food, c.index);

		} else 
		if(Misc.random(100) >= 98 && Misc.random(100) <= 100) {
				Server.itemHandler.createGroundItem(c, RARE_DROP(), c.absX, c.absY, c.heightLevel, 1, c.index);
				int id = RARE_DROP();
				c.sendMessage("You recieve a " + ItemDefinition.forId(id).getName() + " from the <shad=1><col=FF9933>RARE</shad></col> drop table!");
		}
	}	
}