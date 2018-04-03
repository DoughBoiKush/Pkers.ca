package org.osps.model.npcs.boss.abstract_bosses;

import java.util.ArrayList;

import org.osps.event.CycleEvent;
import org.osps.model.npcs.NPC;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;

public abstract class AbstractBossEvent {
	
	protected transient int index;
	protected int default_cycle_time = 300;
	protected transient NPC npc;
	protected transient ArrayList<Player> attackers;
	
	public NPC getNPC() {
		return npc;
	}
	
	public static final Boundary BOUNDARY = new Boundary(3685, 5798,
			3707, 5823);
	
	public ArrayList<Player> getAttackers() {
		return attackers;
	}
	
	protected abstract int[] npcIds();
	
	protected abstract int[][] locations();
	
	protected abstract String[] announcements();
	
	protected abstract boolean checkAll();
	
	protected abstract void submit();
	
	protected abstract CycleEvent game_event();
	
	public abstract void handleBossEvent(boolean mustreturn);
	
	public abstract void addContributingPlayer(final Player player);
	
	protected abstract void messageContributingPlayers(String message);
	
	public void bossMessage(final String message, final boolean global) {
		if (global) {
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Player c2 = (Player)PlayerHandler.players[j];
					c2.sendMessage(message);
				}
			}
		} else {
			if (attackers != null) {
				for (final Player player : attackers) {
					if (player == null)
						continue;
					player.sendMessage(message);
				}
			}
		}
	}
	
}