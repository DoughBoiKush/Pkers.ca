package org.osps.model.npcs.boss.Bandos;

import org.osps.event.CycleEvent;
import org.osps.model.players.Player;

public abstract class BandosStage extends CycleEvent {

	protected Bandos bandos;

	protected Player player;

	public BandosStage(Bandos bandos, Player player) {
		this.bandos = bandos;
		this.player = player;
	}
}
