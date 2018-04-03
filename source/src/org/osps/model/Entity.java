package org.osps.model;

import org.osps.model.content.teleport.Position;
import org.osps.model.players.Player;


/**
 * @author lare96 <http://github.org/lare96>
 */
public abstract class Entity {

	public int index = -1;
	public static int index1 = -1;
	public int absX, absY, heightLevel;

	public boolean updateRequired; // might have to be 'true' for players
	public boolean animUpdateRequired;
	public boolean hitUpdateRequired;
	public boolean hitUpdateRequired2;
	public boolean gfxUpdateRequired;
	public boolean faceUpdateRequired;
	public boolean forcedChatUpdateRequired;
	public boolean facePositionUpdateRequired;

	public int hitDiff;
	public int hitDiff2;
	public int animId;
	public int animDelay;
	public int gfxVar1;
	public int gfxVar2;
	public int face;
	public String forcedText;
	public int faceX;
	public int faceY;

	public final void animation(int anim, int delay) {
		animId = anim;
		animDelay = 0;
		updateRequired = true;
		animUpdateRequired = true;
	}

	public final void animation(int anim) {
		animation(anim, 0);
	}

	public final void stopAnimation() {
		animation(65535);
	}

	public final void primaryHit(int hit) {
		hitDiff = hit;
		updateRequired = true;
		hitUpdateRequired = true;
	}

	public final void secondaryHit(int hit) {
		hitDiff2 = hit;
		updateRequired = true;
		hitUpdateRequired2 = true;
	}

	public final void gfx0(int gfx) {
		gfxVar1 = gfx;
		gfxVar2 = 65536;
		gfxUpdateRequired = true;
		updateRequired = true;
	}

	public final void gfx100(int gfx) {
		gfxVar1 = gfx;
		gfxVar2 = 6553600;
		gfxUpdateRequired = true;
		updateRequired = true;
	}

	public final void face(Entity e) {
		face = e == null ? 65535 : e instanceof Player ? e.index + 32768 : e.index;
		faceUpdateRequired = true;
		updateRequired = true;
	}

	public final void forceChat(String t) {
		forcedText = t;
		updateRequired = true;
		forcedChatUpdateRequired = true;
	}

	public final void face(int x, int y) {
		faceX = 2 * x + 1;
		faceY = 2 * y + 1;
		updateRequired = true;
		facePositionUpdateRequired = true;
	}
	
	/**
	 * Gets an entities position
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * The position of this entity
	 */
	private Position position;
	
	/**
	 * Sets a players position to a new coordinate
	 * @param position
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	public final void onReset() {
		hitUpdateRequired = false;
		hitUpdateRequired2 = false;
		updateRequired = false;
		animUpdateRequired = false;
		gfxUpdateRequired = false;
		faceUpdateRequired = false;
		forcedChatUpdateRequired = false;
		facePositionUpdateRequired = false;
		reset();
	}

	public abstract void reset();

	/*public boolean hasFinished() {
		// TODO Auto-generated method stub
		return false;
	}*/
}
