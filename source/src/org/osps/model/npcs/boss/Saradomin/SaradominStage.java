package org.osps.model.npcs.boss.Saradomin;

import org.osps.event.CycleEvent;
import org.osps.model.players.Player;

public abstract class SaradominStage extends CycleEvent {
	
	protected Saradomin saradomin;
	
	protected Player player;
	
	public SaradominStage(Saradomin saradomin, Player player) {
		this.saradomin = saradomin;
		this.player = player;
	}

}
