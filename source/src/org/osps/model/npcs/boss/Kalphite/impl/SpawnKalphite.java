package org.osps.model.npcs.boss.Kalphite.impl;

import org.osps.Server;
import org.osps.event.CycleEventContainer;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.boss.Kalphite.Kalphite;
import org.osps.model.npcs.boss.Kalphite.Stage;
import org.osps.model.players.Player;

public class SpawnKalphite extends Stage {

	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];

	public SpawnKalphite(Kalphite kalphite, Player player) {
		super(kalphite, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		try {
			if (container.getOwner() == null || kalphite == null || player == null || player.isDead
					|| kalphite.getInstancedKalphite() == null) {
				container.stop();
				return;
			}
			int cycle = container.getTotalTicks();
			if (cycle == 8) {
				player.stopMovement();
				player.getPA().sendScreenFade("Welcome to Kalphite...", -1, 4);
				player.getPA().movePlayer(3484, 9511, kalphite.getInstancedKalphite().getHeight());
			} else if (cycle == 13) {
				Server.npcHandler.spawnNpc(player, 963, 3484, 9511, kalphite.getInstancedKalphite().getHeight(), 1, 255, 20, 350, 350, false, false);
				player.KALPHITE_CLICKS = 0;
				container.stop();
				
			}
			stop();
		} catch (Exception e) {

		}
	}
}

