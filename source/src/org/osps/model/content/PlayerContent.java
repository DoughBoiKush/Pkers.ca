package org.osps.model.content;

import org.osps.model.players.Player;

public class PlayerContent {

	private final Player player;
	
	public PlayerContent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
