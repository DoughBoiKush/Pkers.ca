package org.osps.model.npcs.boss.Kraken;

import org.osps.event.CycleEvent;
import org.osps.model.players.Player;

public abstract class KrakenStage extends CycleEvent {
	
	protected Kraken kraken;
	
	protected Player player;
	
	public KrakenStage(Kraken kraken, Player player) {
		this.kraken = kraken;
		this.player = player;
	}

}
