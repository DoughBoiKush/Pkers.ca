package org.osps.model.npcs.boss.Kalphite;

import java.util.HashMap;
import java.util.Map;

import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.boss.Kalphite.impl.RespawnNpc;
import org.osps.model.npcs.boss.Kalphite.impl.SpawnKalphite;
import org.osps.model.npcs.boss.instances.InstancedArea;
import org.osps.model.npcs.boss.instances.InstancedAreaManager;
import org.osps.model.npcs.boss.instances.SingleInstancedArea;
import org.osps.model.npcs.boss.instances.impl.SingleInstancedKalphite;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;

public class Kalphite {
	
	private final Object EVENT_LOCK = new Object();

	private final Player player;
	
	private SingleInstancedArea kalphiteInstance;
	
	public static final Boundary BOUNDARY = new Boundary(3452, 9466, 3524, 9536);
	private NPC npc;

	private Map<Integer, Stage> stages = new HashMap<>();

	public Kalphite(Player player) {
		this.player = player;
		stages.put(0, new SpawnKalphite(this, player));
		stages.put(1, new RespawnNpc(this, player));
	}
	
	public void initialize() {
		if (kalphiteInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(kalphiteInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		kalphiteInstance = new SingleInstancedKalphite(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, kalphiteInstance);
		if (kalphiteInstance == null) {
			player.sendMessage("An error occured while trying to enter Bandos instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Kalphite...", 1, 3);
		player.KALPHITE_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}
	
	public void RespawnNpc() {
		if (kalphiteInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(kalphiteInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		kalphiteInstance = new SingleInstancedKalphite(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, kalphiteInstance);
		if (kalphiteInstance == null) {
			player.sendMessage("An error occured while trying to enter Bandos instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.KALPHITE_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(1), 1);
	}

	public void stop() {
		try {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		kalphiteInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(kalphiteInstance);
		kalphiteInstance = null;
		} catch (Exception e) {
		}
	}

	public InstancedArea getInstancedKalphite() {
		return kalphiteInstance;
	}

	public NPC getNpc() {
		return npc;
	}
	
	public void setNpc(NPC npc) {
		this.npc = npc;
	}
}
