package org.osps;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.osps.event.CycleEventHandler;
import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.players.PacketHandler;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.net.Packet;
import org.osps.util.Benchmark;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * @author lare96 <http://github.org/lare96>
 */
public final class GameEngine implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(GameEngine.class.getSimpleName());

	private final ScheduledExecutorService gameExecutor = Executors.newSingleThreadScheduledExecutor();

	private final ListeningExecutorService asyncExecutor = MoreExecutors
			.listeningDecorator(Executors.newCachedThreadPool());

	private final AtomicBoolean engineStarted = new AtomicBoolean();

	private boolean cycle = false;
	
	@Override
	public void run() {
		try {
			if (cycle) {
				Benchmark.start("ItemHandler");
				Server.itemHandler.process();
				Benchmark.start("PlayerHandler");
				Server.playerHandler.process();
				Benchmark.start("NpcHandler");
				Server.npcHandler.process();
				Benchmark.start("ShopHandler");
				Server.shopHandler.process();
				Benchmark.start("GlobalObjects");
				Server.globalObjects.pulse();
				Benchmark.start("CycleEventHandler");
				Server.currentServerTime = System.currentTimeMillis();
				CycleEventHandler.getSingleton().process();
				Benchmark.start("ServerData");
				Server.serverData.processQueue();
				cycle = false;
			} else {
				for (int i = 1; i < Config.MAX_PLAYERS; i++) {
					if (PlayerHandler.players[i] != null) {
						PlayerHandler.players[i].getItems().update();
					}
				}
				cycle = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*private static void subCycle() {
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			player.getItems().update();
		}
	}*/
	
	public void start() {
		if (!engineStarted.getAndSet(true)) {
			gameExecutor.scheduleAtFixedRate(this, 0, 300, TimeUnit.MILLISECONDS);
		}
	}

	public void execute(Runnable t) {
		gameExecutor.execute(t);
	}

	public void execute(Packet packet, Player p) {
		if (packet.getOpcode() < 1)
			return;
		gameExecutor.submit(new Runnable() {

			@Override
			public void run() {
				PacketHandler.processPacket(p, packet);
			}
			
		});
	}
	public ListenableFuture<?> executeAsync(Runnable t) {
		return asyncExecutor.submit(t);
	}

	public <T> ListenableFuture<T> executeAsync(Callable<T> t) {
		return asyncExecutor.submit(t);
	}

}