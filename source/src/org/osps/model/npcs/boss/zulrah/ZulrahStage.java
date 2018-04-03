package org.osps.model.npcs.boss.zulrah;

import org.osps.event.CycleEvent;
import org.osps.model.players.Player;

public abstract class ZulrahStage extends CycleEvent {
	
	protected Zulrah zulrah;
	
	protected Player player;
	
	public ZulrahStage(Zulrah zulrah, Player player) {
		this.zulrah = zulrah;
		this.player = player;
	}

}
