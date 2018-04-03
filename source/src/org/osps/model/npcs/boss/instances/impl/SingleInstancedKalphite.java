package org.osps.model.npcs.boss.instances.impl;

import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.boss.Kalphite.Kalphite;
import org.osps.model.npcs.boss.instances.SingleInstancedArea;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;

public class SingleInstancedKalphite extends SingleInstancedArea {
	
	public SingleInstancedKalphite(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Kalphite kalphite = player.getKalphite();
		if (player.getKalphite().getNpc() != null) {
			NPCHandler.kill(player.getKalphite().getNpc().npcType, height);
		}
	}
}
