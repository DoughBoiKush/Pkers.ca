package org.osps.model.npcs.boss.instances.impl;

import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.boss.Kraken.Kraken;
import org.osps.model.npcs.boss.instances.SingleInstancedArea;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;

public class SingleInstancedKraken extends SingleInstancedArea {
	
	public SingleInstancedKraken(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Kraken kraken = player.getKraken();
		if (player.getKraken().getNpc() != null) {
			NPCHandler.kill(player.getKraken().getNpc().npcType, height);
		}
		//Server.getGlobalObjects().remove(17000, height);
		//NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
