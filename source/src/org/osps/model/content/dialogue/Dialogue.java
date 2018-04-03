package org.osps.model.content.dialogue;

import org.osps.model.players.Player;

public abstract class Dialogue {

	protected int next = 0;
	protected int option;
	protected Player player;

	public boolean clickButton(int id) {
		return false;
	}

	public void end() {
		next = -1;
	}

	public abstract void execute();

	public int getNext() {
		return next;
	}

	public int getOption() {
		return option;
	}

	public Player getPlayer() {
		return player;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public void setOption(int option) {
		this.option = option;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
