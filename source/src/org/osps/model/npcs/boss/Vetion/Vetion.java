package org.osps.model.npcs.boss.Vetion;

import java.util.ArrayList;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class Vetion {

	public static ArrayList<int[]> vetionSpellCoordinates = new ArrayList<>(3);

	public static void createVetionSpell(NPC npc, Player player) {
		if (player == null) {
			return;
		}
		int x = player.getX();
		int y = player.getY();
		vetionSpellCoordinates.add(new int[] { x, y });
		for (int i = 0; i < 2; i++) {
			vetionSpellCoordinates.add(new int[] { (x - 1) + Misc.random(3), (y - 1) + Misc.random(3) });
		}
		for (int[] point : vetionSpellCoordinates) {
			int nX = npc.absX + 2;
			int nY = npc.absY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;
			player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, NPCHandler.getProjectileSpeed(npc.index),
					280, 31, 0, -1, 5);

		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : vetionSpellCoordinates) {
					int x2 = point[0];
					int y2 = point[1];
					player.getPA().createPlayersStillGfx(281, x2, y2, 0, 5);
				}
				vetionSpellCoordinates.clear();
				container.stop();
			}

		}, 4);
	}

	public static void createVetionEarthquake(Player player) {
		player.getPA().shakeScreen(3, 2, 3, 2);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.getPA().sendFrame107();
				container.stop();
			}

		}, 1);
	}
}
