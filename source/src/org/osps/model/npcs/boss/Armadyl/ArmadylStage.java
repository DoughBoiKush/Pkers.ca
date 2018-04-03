package org.osps.model.npcs.boss.Armadyl;

import org.osps.event.CycleEvent;
import org.osps.model.players.Player;

public abstract class ArmadylStage extends CycleEvent {
	
	protected Armadyl armadyl;
	
	protected Player player;
	
	public ArmadylStage(Armadyl armadyl, Player player) {
		this.armadyl = armadyl;
		this.player = player;
	}

}
