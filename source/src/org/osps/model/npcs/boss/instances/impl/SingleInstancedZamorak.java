package org.osps.model.npcs.boss.instances.impl;

import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.boss.Zamorak.Zamorak;
import org.osps.model.npcs.boss.instances.SingleInstancedArea;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;

public class SingleInstancedZamorak extends SingleInstancedArea {
	
	public SingleInstancedZamorak(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Zamorak zamorak = player.getZamorak();
		if (player.getZamorak().getNpc() != null) {
			NPCHandler.kill(player.getZamorak().getNpc().npcType, height);
		}
		//Server.getGlobalObjects().remove(17000, height);
		//NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
