package org.osps.model.npcs.boss.Zamorak;

import org.osps.event.CycleEvent;
import org.osps.model.players.Player;

public abstract class ZamorakStage extends CycleEvent {
	
	protected Zamorak zamorak;
	
	protected Player player;
	
	public ZamorakStage(Zamorak zamorak, Player player) {
		this.zamorak = zamorak;
		this.player = player;
	}

}
