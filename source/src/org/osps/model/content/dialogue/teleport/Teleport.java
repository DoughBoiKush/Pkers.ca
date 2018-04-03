package org.osps.model.content.dialogue.teleport;

import java.util.function.Consumer;

import org.osps.model.content.teleport.Position;
import org.osps.model.players.Player;

public class Teleport extends Position {

	private final String name;
	private Consumer<Player> consumer;
	private boolean dangerous;
	
	public Teleport(String name, Consumer<Player> consumer) {
		this(name,0,0);
		this.consumer = consumer;
	}
	
	public Teleport(String name, int x, int y) {
		this(name, x, y, false);
	}

	public Teleport(String name, int x, int y, boolean dangerous) {
		this(name, x, y, 0, dangerous);
	}
	
	public Teleport(String name, int x, int y, int z) {
		this(name, x, y, z, false);
	}
	
	public Teleport(String name, int x, int y, int z, boolean dangerous) {
		super(x, y, z);
		this.name = name;
		this.dangerous = dangerous;
	}

	public String getName() {
		return name;
	}
	
	public Consumer<Player> getConsumer() {
		return consumer;
	}

	public boolean isDangerous() {
		return dangerous;
	}
	
}