package org.osps.model.npcs.boss.instances.impl;

import org.osps.Server;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.boss.instances.SingleInstancedArea;
import org.osps.model.npcs.boss.zulrah.Zulrah;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;

public class SingleInstancedZulrah extends SingleInstancedArea {
	
	public SingleInstancedZulrah(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Zulrah zulrah = player.getZulrahEvent();
		if (zulrah.getNpc() != null) {
			NPCHandler.kill(zulrah.getNpc().npcType, height);
		}
		Server.getGlobalObjects().remove(17000, height);
		NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
