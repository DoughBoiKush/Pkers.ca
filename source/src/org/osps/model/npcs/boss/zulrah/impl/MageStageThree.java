package org.osps.model.npcs.boss.zulrah.impl;

import org.osps.event.CycleEventContainer;
import org.osps.model.npcs.boss.zulrah.Zulrah;
import org.osps.model.npcs.boss.zulrah.ZulrahLocation;
import org.osps.model.npcs.boss.zulrah.ZulrahStage;
import org.osps.model.players.Player;
import org.osps.model.players.combat.CombatType;


public class MageStageThree extends ZulrahStage {

	public MageStageThree(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDead
				|| player == null || player.isDead || zulrah.getInstancedZulrah() == null) {
			container.stop();
			return;
		}
		zulrah.getNpc().setFacePlayer(true);
		if (zulrah.getNpc().totalAttacks > 5) {
			player.getZulrahEvent().changeStage(4, CombatType.RANGE, ZulrahLocation.WEST);
			zulrah.getNpc().totalAttacks = 0;
			container.stop();
			return;
		}
	}

}
