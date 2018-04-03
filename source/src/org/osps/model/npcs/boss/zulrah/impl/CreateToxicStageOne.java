package org.osps.model.npcs.boss.zulrah.impl;

import java.util.Arrays;

import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.boss.zulrah.DangerousEntity;
import org.osps.model.npcs.boss.zulrah.DangerousLocation;
import org.osps.model.npcs.boss.zulrah.SpawnDangerousEntity;
import org.osps.model.npcs.boss.zulrah.Zulrah;
import org.osps.model.npcs.boss.zulrah.ZulrahLocation;
import org.osps.model.npcs.boss.zulrah.ZulrahStage;
import org.osps.model.players.Player;
import org.osps.model.players.combat.CombatType;



public class CreateToxicStageOne extends ZulrahStage {

	public CreateToxicStageOne(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDead
				|| player == null || player.isDead || zulrah.getInstancedZulrah() == null) {
			container.stop();
			return;
		}
		if (container.getTotalTicks() == 1) {
			zulrah.getNpc().setFacePlayer(false);
			CycleEventHandler.getSingleton().addEvent(player, new SpawnDangerousEntity(zulrah, player, Arrays.asList(
					DangerousLocation.values()), DangerousEntity.TOXIC_SMOKE, 40), 1);
		} else if (container.getTotalTicks() >= 19) {
			zulrah.getNpc().totalAttacks = 0;
			zulrah.changeStage(2, CombatType.MELEE, ZulrahLocation.NORTH);
			container.stop();
		}
	}

}
