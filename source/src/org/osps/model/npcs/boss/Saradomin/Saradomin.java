package org.osps.model.npcs.boss.Saradomin;

import java.util.HashMap;
import java.util.Map;

import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.boss.Saradomin.impl.RespawnNpc;
import org.osps.model.npcs.boss.Saradomin.impl.SpawnSaradominStageZero;
import org.osps.model.npcs.boss.instances.InstancedArea;
import org.osps.model.npcs.boss.instances.InstancedAreaManager;
import org.osps.model.npcs.boss.instances.SingleInstancedArea;
import org.osps.model.npcs.boss.instances.impl.SingleInstancedSaradomin;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;


public class Saradomin {
	
	private final Object EVENT_LOCK = new Object();

	private final Player player;
	
	private SingleInstancedArea saradominInstance;
	
	public static final Boundary BOUNDARY = new Boundary(2887, 5255, 2909, 5278);
	private NPC npc;

	private Map<Integer, SaradominStage> stages = new HashMap<>();

	public Saradomin(Player player) {
		this.player = player;
		stages.put(0, new SpawnSaradominStageZero(this, player));
		stages.put(1, new RespawnNpc(this, player));
	}
	
	public void initialize() {
		if (saradominInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(saradominInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		saradominInstance = new SingleInstancedSaradomin(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, saradominInstance);
		if (saradominInstance == null) {
			player.sendMessage("An error occured while trying to enter Saradomin instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Saradomin...", 1, 3);
		player.SARADOMIN_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}
	
	public void RespawnNpcs() {
		if (saradominInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(saradominInstance);
		}
		long elapsed = System.currentTimeMillis();
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		saradominInstance = new SingleInstancedSaradomin(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, saradominInstance);
		if (saradominInstance == null) {
			player.sendMessage("An error occured while trying to enter Saradomin instanced. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.SARADOMIN_INSTANCE = true;
		CycleEventHandler.getSingleton().addEvent(player, stages.get(1), 1);
	}

	public void stop() {
		try {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		saradominInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(saradominInstance);
		saradominInstance = null;
		//System.out.println("Saradomin ended.");
		} catch (Exception e) {	
		}
	}

	public InstancedArea getInstancedSaradomin() {
		return saradominInstance;
	}

	public NPC getNpc() {
		return npc;
	}
	
	public void setNpc(NPC npc) {
		this.npc = npc;
	}
	
	/*public int getStage() {
		return stage;
	}*/
}