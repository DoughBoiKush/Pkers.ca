package org.osps.util;

public class Position {
	
	private int x;
	private int y;
	private int z;
	
	public Position(int x, int y, int z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

}
