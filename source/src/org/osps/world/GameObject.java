package org.osps.world;

import org.osps.model.Location;

public final class GameObject {
	private int id;
	private int type;
	private Location loc;
	private int face;

	public GameObject(int id, int type, Location loc, int face) {
		this.id = id;
		this.type = type;
		this.loc = loc;
		this.face = face;
	}

	public int getId() {
		return id;
	}
	
	public int getX() {
		return loc.getX();
	}
	
	public int getY() {
		return loc.getY();
	}
	
	public int getZ() {
		return loc.getZ();
	}

	public int getType() {
		return type;
	}

	public Location getLocation() {
		return loc;
	}

	public int getFace() {
		return face;
	}
}