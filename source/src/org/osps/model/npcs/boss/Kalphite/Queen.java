package org.osps.model.npcs.boss.Kalphite;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.players.Player;

public class Queen {
	
	Player c;

	public Queen(Player c) {
		this.c = c;
	}
	
	public static boolean changePhase = false;

	public static void Phase_Change(NPC npc) {
		if (npc != null) {
			npc.isDead = false;
			npc.requestTransform(6720);
			npc.gfx0(1055);
			Player.transforming = true;
			CycleEventHandler.getSingleton().addEvent(npc, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (container.getTotalTicks() == 1) {
						npc.HP += 255;
						npc.requestTransform(965);
						npc.animation(6270);
					} else if (container.getTotalTicks() == 11) {
						Player.transforming = false;
						changePhase = false;
						container.stop();
					}
				}
			}, 1);
		}
	}
	
	public static void graphic(NPC npc, Player player) {
		if (player == null) {
			return;
		}
		if(player.RANGE_ABILITY == true) {
			return;
		}
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				player.gfx0(281);
				container.stop();
			}

		}, 4);
	}
	
	public static boolean usingRange;
	
	public static void graphic1(NPC npc, Player player) {
		if (player == null) {
			return;
		}
		player.RANGE_ABILITY = true;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				npc.gfx0(278);
				container.stop();
			}
			
			public void stop() {
				player.RANGE_ABILITY = false;
			}

		}, 1);
	}
}
