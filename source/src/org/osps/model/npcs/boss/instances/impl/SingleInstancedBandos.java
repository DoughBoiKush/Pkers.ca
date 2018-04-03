package org.osps.model.npcs.boss.instances.impl;

import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.boss.Bandos.Bandos;
import org.osps.model.npcs.boss.instances.SingleInstancedArea;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;

public class SingleInstancedBandos extends SingleInstancedArea {
	
	public SingleInstancedBandos(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Bandos bandos = player.getBandos();
		if (player.getBandos().getNpc() != null) {
			NPCHandler.kill(player.getBandos().getNpc().npcType, height);
		}
		//Server.getGlobalObjects().remove(17000, height);
		//NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
