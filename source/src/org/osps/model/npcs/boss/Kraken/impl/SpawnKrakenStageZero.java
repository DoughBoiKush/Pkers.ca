package org.osps.model.npcs.boss.Kraken.impl;

import org.osps.Server;
import org.osps.event.CycleEventContainer;
import org.osps.model.npcs.boss.Kraken.Kraken;
import org.osps.model.npcs.boss.Kraken.KrakenStage;
import org.osps.model.players.Player;


public class SpawnKrakenStageZero extends KrakenStage {

	public SpawnKrakenStageZero(Kraken kraken, Player player) {
		super(kraken, player);
	}
		
	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || kraken == null || player == null || player.isDead || kraken.getInstancedKraken() == null) {
			container.stop();
			return;
		}
		int cycle = container.getTotalTicks();
		if (cycle == 8) {
			player.stopMovement();
			player.getPA().sendScreenFade("Welcome to Kraken's Cave...", -1, 4);
			player.getPA().movePlayer(3694, 5800, kraken.getInstancedKraken().getHeight());
		}
		else if (cycle == 13) {
			Server.npcHandler.spawnNpc(player, 496, 3694, 5810, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 3691, 5814, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 3691, 5809, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 3700, 5814, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			Server.npcHandler.spawnNpc(player, 493, 3700, 5809, kraken.getInstancedKraken().getHeight(), -1, 1, 41, 500, 500, false, false);
			player.KRAKEN_CLICKS = 0;
			container.stop();;
			}
			stop();
		}
	}


