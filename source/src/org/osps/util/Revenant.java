package org.osps.util;

public class Revenant {
	
	private int mobId;
	private Position position;
	private int roamTile;
	private String face;
	private String mobName;

	public Revenant(int mobId, Position position, int roamTile, String face, String mobName) {
		this.setMobId(mobId);
		this.setPosition(position);
		this.setRoamTile(roamTile);
		this.setFace(face);
		this.setMobName(mobName);
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public int getRoamTile() {
		return roamTile;
	}

	public void setRoamTile(int roamTile) {
		this.roamTile = roamTile;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getMobId() {
		return mobId;
	}

	public void setMobId(int mobId) {
		this.mobId = mobId;
	}

	public String getMobName() {
		return mobName;
	}

	public void setMobName(String mobName) {
		this.mobName = mobName;
	}
}
