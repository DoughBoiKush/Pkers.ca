package org.osps.model.npcs.boss.Kraken.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;

public class SpawnEntity {
	
	public static final Map<NPC, Player> DISTURBED_POOLS = new ConcurrentHashMap<>();
	
	public static void spawnEntity(Player c, int i) {
		final NPC whirlpool = NPCHandler.npcs[i];
		if (whirlpool == null || whirlpool.npcType != 493 && whirlpool.npcType != 496)
			return;
		final Player player = DISTURBED_POOLS.get(whirlpool);
		if (player != null && player == c) {
			c.sendMessage("You've already disturbed this pool!");
			return;
		}
		final boolean head = whirlpool.npcType == 496;
		c.getCombat().resetPlayerAttack();
		NPCHandler.KILL_POOLS(c, head ? 496 : 493, whirlpool.absX, whirlpool.absY, whirlpool.heightLevel);
		DISTURBED_POOLS.put(whirlpool,c);
		if (head) {
			for (NPC n : DISTURBED_POOLS.keySet()) {
				if (n == null)
					continue;
				Player p = DISTURBED_POOLS.get(n);
				if (p == null)
					continue;
				if (p == c)
					DISTURBED_POOLS.remove(p);
				p.krakenTent = 0;
			}
		}
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				NPC npc = Server.npcHandler.spawnNpc(c, head ? 494 : 5535, whirlpool.absX, whirlpool.absY, c.heightLevel, -1, head ? 255 : 80, 10, 500, 500, true, false);
				c.krakenTent++;
				if (head) {
					npc.animation(3617);
				}
				npc.killerId = c.getId();
				npc.face(c);
				container.stop();
			}
		}, 3);
	} 
}