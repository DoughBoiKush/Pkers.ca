package org.osps.model.npcs.boss;

import java.util.ArrayList;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class Archaeologist {

	public static ArrayList<int[]> SPELL_COORDINATES = new ArrayList<>(3);

	public static boolean FINISHED_ABILITY = false;
	public static int messages;

	public static void Rain_of_Knowledge(NPC npc, Player player) {
		if (player == null) {
			return;
		}
		int x = player.getX();
		int y = player.getY();
		SPELL_COORDINATES.add(new int[] { x, y });
		for (int i = 0; i < 2; i++) {
			SPELL_COORDINATES.add(new int[] { (x - 1) + Misc.random(3), (y - 1) + Misc.random(3) });
		}
		for (int[] point : SPELL_COORDINATES) {
			int nX = npc.absX + 2;
			int nY = npc.absY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;
			player.lastX = player.absX;
			player.lastY = player.absY;
			player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, NPCHandler.getProjectileSpeed(npc.index), 1260, 31, 0, -1, 5);

		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : SPELL_COORDINATES) {
					int x2 = point[0];
					int y2 = point[1];
					player.getPA().createPlayersStillGfx(157, x2, y2, 0, 5);
				}
				SPELL_COORDINATES.clear();
				container.stop();
			}

			@Override
			public void stop() {
				Next_Rain(npc, player);
			}

		}, 4);
	}

	public static ArrayList<int[]> NEXT_SPELL = new ArrayList<>(2);

	public static void Next_Rain(NPC npc, Player player) {
		if (player == null) {
			return;
		}
		int x = player.lastX;
		int y = player.lastY;
		NEXT_SPELL.add(new int[] { x, y });
		for (int i = 0; i < 1; i++) {
			NEXT_SPELL.add(new int[] { (player.lastX - 1) + Misc.random(2), (player.lastY - 1) + Misc.random(2) });
		}
		for (int[] point : NEXT_SPELL) {
			int nX = player.lastX + 2;
			int nY = player.lastY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;
			player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, NPCHandler.getProjectileSpeed(npc.index), 1260, 10, 0, -1, 5);
			FINISHED_ABILITY = true;
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : NEXT_SPELL) {
					int x2 = point[0];
					int y2 = point[1];
					player.getPA().createPlayersStillGfx(157, x2, y2, 0, 5);

				}
				NEXT_SPELL.clear();
				container.stop();
			}

			@Override
			public void stop() {
				FINISHED_ABILITY = false;
			}

		}, 4);
	}

	public static void MUSEUM_ABILITY(Player player) {
		if (player == null) {
			return;
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.gfx0(305);
				container.stop();
			}

		}, 4);
	}

	public static String Archaeologist_Quotes() {
		int quote = Misc.random(3);
		switch (quote) {
		case 0:
			return "I'm Bellock - respect me!";
		case 1:
			return "Get off my site!";
		case 2:
			return "No-one messes with Bellock's dig!";
		case 3:
			return "These ruins are mine!";
		case 4:
			return "Taste my knowledge!";

		}
		return "";
	}
}
