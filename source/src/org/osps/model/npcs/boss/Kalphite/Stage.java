package org.osps.model.npcs.boss.Kalphite;

import org.osps.event.CycleEvent;
import org.osps.model.players.Player;

public abstract class Stage extends CycleEvent {

	protected Kalphite kalphite;

	protected Player player;

	public Stage(Kalphite kalphite, Player player) {
		this.kalphite = kalphite;
		this.player = player;
	}
}
