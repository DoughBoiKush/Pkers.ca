package org.osps.model.npcs.boss.Kraken;

import java.util.HashMap;
import java.util.Map;

import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.boss.Kraken.impl.SpawnKrakenStageZero;
import org.osps.model.npcs.boss.instances.InstancedArea;
import org.osps.model.npcs.boss.instances.InstancedAreaManager;
import org.osps.model.npcs.boss.instances.SingleInstancedArea;
import org.osps.model.npcs.boss.instances.impl.SingleInstancedKraken;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;


public class Kraken {

	
	private final Object EVENT_LOCK = new Object();

	private final Player player;
	
	private SingleInstancedArea krakenInstance;
	
	public static final Boundary BOUNDARY = new Boundary(/*(X)*/3685, /*(Y)*/5798,
														/*(X)*/3707, /*(Y)*/5823);
	private NPC npc;

	private Map<Integer, KrakenStage> stages = new HashMap<>();

	public Kraken(Player player) {
		this.player = player;
		stages.put(0, new SpawnKrakenStageZero(this, player));
	}
	
	public void initialize() {
		if (krakenInstance != null) {
			InstancedAreaManager.getSingleton().disposeOf(krakenInstance);
		}
		int height = InstancedAreaManager.getSingleton().getNextOpenHeight(BOUNDARY);
		krakenInstance = new SingleInstancedKraken(player, BOUNDARY, height);
		InstancedAreaManager.getSingleton().add(height, krakenInstance);
		if (krakenInstance == null) {
			player.sendMessage("An error occured while trying to enter Kraken's shrine. Please try again.");
			return;
		}
		player.getPA().removeAllWindows();
		player.stopMovement();
		player.getPA().sendScreenFade("Welcome to Kraken's Cave...", 1, 5);
		CycleEventHandler.getSingleton().addEvent(player, stages.get(0), 1);
	}

	public void stop() {
		CycleEventHandler.getSingleton().stopEvents(EVENT_LOCK);
		krakenInstance.onDispose();
		InstancedAreaManager.getSingleton().disposeOf(krakenInstance);
		krakenInstance = null;
	}

	public InstancedArea getInstancedKraken() {
		return krakenInstance;
	}

	public NPC getNpc() {
		return npc;
	}
	
	public void setNpc(NPC npc) {
		this.npc = npc;
	}
	
	public static void enter(Player player) {
		if (!player.playerName.equalsIgnoreCase("dylan") && player.wildLevel > 20) {
			player.sendMessage("You cannot teleport above 20 wilderness.");
			player.getPA().closeAllWindows();
			return;
		}
		if (player.KRAKEN_CLICKS >= 1) {
			player.sendMessage("You already have an active instance!");
			player.getPA().closeAllWindows();
			return;
		}
		if (player.getKraken().getInstancedKraken() != null) {
			player.sendMessage("You already have an active instance!");
			player.getPA().closeAllWindows();
			return;
		}
		player.getKraken().initialize();
		player.KRAKEN_CLICKS = 1;
	}
}
