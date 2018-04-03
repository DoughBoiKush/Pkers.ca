package org.osps.model.npcs.boss.instances.impl;

import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.boss.Armadyl.Armadyl;
import org.osps.model.npcs.boss.instances.SingleInstancedArea;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;

public class SingleInstancedArmadyl extends SingleInstancedArea {
	
	public SingleInstancedArmadyl(Player player, Boundary boundary, int height) {
		super(player, boundary, height);
	}
	
	@Override
	public void onDispose() {
		Armadyl armadyl = player.getArmadyl();
		if (player.getArmadyl().getNpc() != null) {
			NPCHandler.kill(player.getArmadyl().getNpc().npcType, height);
		}
		//Server.getGlobalObjects().remove(17000, height);
		//NPCHandler.kill(Zulrah.SNAKELING, height);
	}

}
