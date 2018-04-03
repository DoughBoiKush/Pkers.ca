package org.osps.model.npcs.boss.zulrah.impl;

import org.osps.Server;
import org.osps.event.CycleEventContainer;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.boss.zulrah.Zulrah;
import org.osps.model.npcs.boss.zulrah.ZulrahLocation;
import org.osps.model.npcs.boss.zulrah.ZulrahStage;
import org.osps.model.players.Player;
import org.osps.model.players.combat.CombatType;


public class SpawnZulrahStageZero extends ZulrahStage {

	public SpawnZulrahStageZero(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || player == null || player.isDead || zulrah.getInstancedZulrah() == null) {
			container.stop();
			return;
		}
		int cycle = container.getTotalTicks();
		if (cycle == 8) {
			player.getPA().sendScreenFade("Welcome to Zulrah's shrine", -1, 4);
			player.getPA().movePlayer(2268, 3069, zulrah.getInstancedZulrah().getHeight());
		}
		if (cycle == 13) {
			Server.npcHandler.spawnNpc(player, 2042, 2266, 3072, zulrah.getInstancedZulrah().getHeight(), -1, 500, 41, 500, 500, true, false);
			NPC npc = NPCHandler.getNpc(2042, 2266, 3072, zulrah.getInstancedZulrah().getHeight());
			if (npc == null) {
				player.sendMessage("Something seems to have gone wrong! Please contact a staff member of Pkers.Ca.");
				container.stop();
				return;
			}
			zulrah.setNpc(npc);
			npc.setFacePlayer(false);
			npc.face(player);
			npc.animation(5073);
			player.ZULRAH_CLICKS = 0;
		}
		if (cycle == 18) {
			zulrah.changeStage(1, CombatType.RANGE, ZulrahLocation.NORTH);
			container.stop();
		}
	}

}