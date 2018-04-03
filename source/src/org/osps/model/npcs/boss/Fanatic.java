package org.osps.model.npcs.boss;

import java.util.ArrayList;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class Fanatic {

	public static ArrayList<int[]> EARTH = new ArrayList<>(3);

	public static boolean FINISHED_ABILITY = false;
	public static int messages;
	public static int Weapon_Removal = 0;
	public static boolean DAMAGE_RECIEVED = false;

	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];

	public static void attackingData(int i, int attackType, int hitDelayTimer) {
		if (npcs[i] != null) {
			attackType = npcs[i].attackType;
			hitDelayTimer = npcs[i].hitDelayTimer;
		}
	}

	public static void removeWeapon(Player player) {
		int slot = 3;
		if (player.playerEquipment[slot] == -1)
			slot++;
		int item = player.playerEquipment[slot];
		player.getItems().removeItem(item, slot);
	}

	public static void EARTH_ABILITY(NPC npc, Player player) {
		if (player == null) {
			return;
		}
		int x = player.getX();
		int y = player.getY();
		EARTH.add(new int[] { x, y });
		for (int i = 0; i < 2; i++) {
			EARTH.add(new int[] { (x - 1) + Misc.random(3), (y - 1) + Misc.random(3) });
		}
		for (int[] point : EARTH) {
			int nX = npc.absX + 2;
			int nY = npc.absY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;
			player.lastX = player.absX;
			player.lastY = player.absY;
			player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, NPCHandler.getProjectileSpeed(npc.index),
					551, 31, 0, -1, 5);
			FINISHED_ABILITY = true;
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : EARTH) {
					int x2 = point[0];
					int y2 = point[1];
					player.getPA().createPlayersStillGfx(157, x2, y2, 0, 5);
				}
				EARTH.clear();
				container.stop();
			}
			public void stop () {
				DAMAGE_RECIEVED = true;
			}

		}, 4);
	}

	public static void Black_Ability(Player player) {
		if (player == null) {
			return;
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.gfx0(305);
				container.stop();
			}
			
			public void stop() {
				FINISHED_ABILITY = false;
				DAMAGE_RECIEVED = false;
			}

		}, 4);
	}

	public static String Fanatic_Quotes() {
		int quote = Misc.random(4);
		switch (quote) {
		case 0:
			return "Burn!";
		case 1:
			return "WEUGH!";
		case 2:
			return "Develish Oxen Roll!";
		case 3:
			return "All your wilderness are belong to them!";
		case 4:
			return "AhehHeheuhHhahueHuUEehEahAH";
		case 5:
			return "I shall call him squidgy and he shall be my squidgy!";

		}
		return "";
	}
}