package org.osps.model.npcs;

import java.awt.Point;

import org.osps.clip.Region;
import org.osps.model.Entity;
import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.npcs.boss.Armadyl.Armadyl;
import org.osps.model.npcs.boss.Bandos.Bandos;
import org.osps.model.npcs.boss.Cerberus.Cerberus;
import org.osps.model.npcs.boss.Kalphite.Kalphite;
import org.osps.model.npcs.boss.Kraken.Kraken;
import org.osps.model.npcs.boss.Lizardman.Lizardman;
import org.osps.model.npcs.boss.Saradomin.Saradomin;
import org.osps.model.npcs.boss.Zamorak.Zamorak;
import org.osps.model.npcs.boss.zulrah.Zulrah;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Location3D;
import org.osps.util.Misc;
import org.osps.util.Stopwatch;
import org.osps.util.Stream;

public class NPC extends Entity {

	public int npcType;
	public int summonedBy;
	public int makeX, makeY, maxHit, defence, rangeDefence, magicDefence, attack, moveX, moveY, direction, walkingType;
	public int resetTimer;
	public int spawnX, spawnY;
	public int viewX, viewY;
	public int hp;
	public int lastHP;
	public int lastX, lastY;
	public boolean summoner = false;
	public long singleCombatDelay = 0;
	public boolean specialForm = false;
	public long explodingRocks, spawnGhosts, spawnLizards, jumpAbility, rockAbility;
	public long lastHeal;
    public int coreTeleport = 0;
    public int canAttack = 0;

	public int size = 1;
	
	  public boolean isCore() {
	        return npcType == 320;
	    }
	    int coreId = 0;
	    int corpId = 0;
	    boolean isStunned = false;

	private boolean transformUpdateRequired;
	private int transformId;
	public Location3D targetedLocation;

	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	public long lastSpecialAttack;

	public boolean spawnedMinions;

	public int attackType, projectileId, endGfx, spawnedBy, hitDelayTimer, HP, maximumHealth, actionTimer, enemyX, enemyY;
	public boolean applyDead, isDead, needRespawn, respawns;
	public boolean walkingHome, underAttack;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex, underAttackBy;
	public int kree, zilyana, graardor, tsutsaroth;
	public long lastDamageTaken;
	public long lastText;
	public static int killerId1;
	public boolean playerStun;
	public boolean npcStun;
	public Stopwatch lastSpear = new Stopwatch();
	public boolean randomWalk;
	public boolean faceToUpdateRequired;
	public int firstAttacker;
	public int stage;
	public int totalAttacks;
	private boolean facePlayer = true;
	public int MAXHP;
	public int MAXHit;
	public int npcSize = 1;
	public int damageDealt;
	public int damageDone;
	public int defenceAnimation = 1;
	public int FocusPointX;
	public int FocusPointY;
	
	/*@Override
	public int getSize() {
		return npcSize;
	}*/

	public NPC(int _npcId, int _npcType) {
		NpcDefinition definition = NpcDefinition.DEFINITIONS[_npcType];
		index = _npcId;
		npcType = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
		if (definition != null) {
			size = definition.getSize();
			if (size < 1) {
				size = 1;
			}
			HP = definition.getHitpoints();
			maximumHealth = definition.getHitpoints();
			attack = definition.getAttackBonus();
			defence = definition.getMeleeDefence();
			rangeDefence = definition.getRangedDefence();
			magicDefence = definition.getMagicDefence();
			maxHit = definition.getMaxHit();
		}
	}
	
	public String Graardor() {
		int quote = Misc.random(9);
		switch (quote) {
		case 0:
			return "Death to our enemies!";
		case 1:
			return "Brargh!";
		case 2:
			return "Break their bones!";
		case 3:
			return "For the glory of the Big High War God!";
		case 4:
			return "Split their skulls!";
		case 5:
			return "We feast on the bones of our enemies tonight!";
		case 6:
			return "CHAAARGE!";
		case 7:
			return "Crush them underfoot!";
		case 8:
			return "All glory to Bandos!";
		case 9:
			return "GRAAAAAAAAAR!";
		}
		return "";
	}

	public String Tsutsaroth() {
		int quote = Misc.random(8);
		switch (quote) {
		case 0:
			return "Attack them!";
		case 1:
			return "Forward!";
		case 2:
			return "Death to Saradomin's dogs!";
		case 3:
			return "Kill them you cowards!";
		case 4:
			return "The Dark One will have their souls!";
		case 5:
			return "Zamorak curse them!";
		case 6:
			return "Rend them limb from limb!";
		case 7:
			return "No retreat!";
		case 8:
			return "Slay them all!!";
		}
		return "";
	}

	public String Zilyana() {
		int quote = Misc.random(9);
		switch (quote) {
		case 0:
			return "Death to the enemies of the light!";
		case 1:
			return "Slay the evil ones!";
		case 2:
			return "Saradomin lend me strength!";
		case 3:
			return "By the power of Saradomin!";
		case 4:
			return "May Saradomin be my sword!";
		case 5:
			return "Good will always triumph!";
		case 6:
			return "Forward! Our allies are with us!";
		case 7:
			return "Saradomin is with us!";
		case 8:
			return "In the name of Saradomin!";
		case 9:
			return "Attack! Find the Godsword!";
		}
		return "";
	}

	public String Kree() {
		int quote = Misc.random(6);
		switch (quote) {
		case 0:
			return "Attack with your talons!";
		case 1:
			return "Face the wratch of Armadyl";
		case 2:
			return "SCCCRREEEEEEEEEECHHHHH";
		case 3:
			return "KA KAW KA KAW";
		case 4:
			return "Fight my minions!";
		case 5:
			return "Good will always triumph!";
		case 6:
			return "Attack! Find the Godsword!";
		}
		return "";
	}
	
	public boolean insideOf(int x, int y) {
		for (Point point : getTiles()) {
			if (point.x == x && point.y == y) {
				return true;
			}
		}
		
		return false;
	}
	
	public double getDistance(int x, int y) {
		double low = 9999;
		for (Point a : getBorder()) {
			double dist = Misc.distance(a.x, a.y, x, y);
			if (dist < low) {
				low = dist;
			}
		}
		return low;
	}
	
	/**
	 * Gets the border around the edges of the actor.
	 * @return
	 * 		the border around the edges of the actor, depending on the actor's size.
	 */
	public Point[] getBorder() {
		int x = getX();
		int y = getY();
		if (size <= 1) {
			return new Point[] { new Point(x, y) };
		}

		Point[] border = new Point[(size) + (size - 1) + (size - 1) + (size - 2)];
		int j = 0;

		border[0] = new Point(x, y);

		for (int i = 0; i < 4; i++) {
			for (int k = 0; k < (i < 3 ? (i == 0 || i == 2 ? size : size)  - 1 : (i == 0 || i == 2 ? size : size) - 2); k++) {
				if (i == 0)
					x++;
				else if (i == 1)
					y++;
				else if (i == 2)
					x--;
				else if (i == 3) {
					y--;
				}
				border[(++j)] = new Point(x, y);
			}
		}

		return border;
	}
	
	/**
	 * Get the tiles this actor would take up on the location.
	 * @param location
	 * 			the desired src location for these tiles.
	 * @return
	 * 		the tiles this actor would take up on the location.
	 */
	public Point[] getTiles() {
		Point[] tiles = new Point[getSize() == 1 ? 1 : (int) Math.pow(getSize(), 2)];
		int index = 0;
		
		for (int i = 1; i < size + 1; i++) {
			for (int k = 0; k < NPCClipping.SIZES[i].length; k++) {
				int x3 = getX() + NPCClipping.SIZES[i][k][0];
				int y3 = getY() + NPCClipping.SIZES[i][k][1];
				tiles[index] = new Point(x3, y3);
				index++;
			}
		}
		return tiles;
	}
	
	public NpcDefinition definition() {
		return NpcDefinition.DEFINITIONS[npcType];
	}

	/**
	 * Determines if the npc can face another player
	 * 
	 * @return {@code true} if the npc can face players
	 */
	public boolean canFacePlayer() {
		return facePlayer;
	}

	/**
	 * Makes the npcs either able or unable to face other players
	 * 
	 * @param facePlayer
	 *            {@code true} if the npc can face players
	 */
	public void setFacePlayer(boolean facePlayer) {
		this.facePlayer = facePlayer;
	}
	
	public void facePlayer(int player) {
		if (player == 65535 || player == -1) {
			face = -1;
		} else {
			face = player + 32768;
		}
		faceUpdateRequired = true;
		updateRequired = true;
	}

	/**
	 * Sends the request to a client that the npc should be transformed into
	 * another.
	 * 
	 * @param Id
	 *            the id of the new npc
	 */
	public void requestTransform(int id) {
		transformId = id;
		npcType = id;
		transformUpdateRequired = true;
		updateRequired = true;
	}

	public void appendTransformUpdate(Stream str) {
		str.writeWordBigEndianA(transformId);
	}

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {

			if (updateRequired) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	public void dealDamage(int damage) {
		if (damage > HP) {
			damage = HP;
		}
		HP -= damage;
	}



	/**
	 * @return the npcId
	 */
	public int getPlayerIdentifier() {
		return index;
	}

	/**
	 * Graphics
	 **/

	public void appendMask80Update(Stream str) {
		str.writeWord(gfxVar1);
		str.writeDWord(gfxVar2);
	}

	public int getOffset() {
		return (int) Math.floor(NPCHandler.getNpcDef()[this.npcType].getSize() / 2);
	}

	public static int getSpeedForDistance(int distance) {
		switch (distance) {
		case 1:
			return 90;
		case 2:
			return 95;
		case 3:
			return 100;
		case 4:
			return 105;
		case 5:
			return 115;
		case 6:
			return 125;
		case 7:
			return 135;
		case 8:
			return 150;
		default:
			return 150;
		}
	}
	
	public void doAnimation(int id) {
		animId = animId;
		animUpdateRequired = true;
		updateRequired = true;
	}
	
		public void turnNpc(int i, int j) {
		  FocusPointX = 2 * i + 1;
		  FocusPointY = 2 * j + 1;
		  updateRequired = true;
		}
	//public int defenceAnimation = -1;
	
	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animId);
		str.writeByte(1);
	}

	/*public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animId);
		str.writeByte(1);
	}*/

	private void appendSetFocusDestination(Stream str) {
		str.writeWordBigEndian(faceX);
		str.writeWordBigEndian(faceY);
	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(face);
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}

	public void appendNPCUpdateBlock(Stream str) {
		if (!updateRequired)
			return;
		int updateMask = 0;
		if (animUpdateRequired)
			updateMask |= 0x10;
		if (hitUpdateRequired2)
			updateMask |= 8;
		if (gfxUpdateRequired)
			updateMask |= 0x80;
		if (faceUpdateRequired)
			updateMask |= 0x20;
		if (forcedChatUpdateRequired)
			updateMask |= 1;
		if (hitUpdateRequired)
			updateMask |= 0x40;
		if (transformUpdateRequired)
			updateMask |= 2;
		if (facePositionUpdateRequired)
			updateMask |= 4;

		str.writeByte(updateMask);

		if (animUpdateRequired)
			appendAnimUpdate(str);
		if (hitUpdateRequired2)
			appendHitUpdate2(str);
		if (gfxUpdateRequired)
			appendMask80Update(str);
		if (faceUpdateRequired)
			appendFaceEntity(str);
		if (forcedChatUpdateRequired) {
			str.writeString(forcedText);
		}
		if (hitUpdateRequired)
			appendHitUpdate(str);
		if (transformUpdateRequired)
			appendTransformUpdate(str);
		if (facePositionUpdateRequired)
			appendSetFocusDestination(str);
	}

	@Override
	public void reset() {
		transformUpdateRequired = false;
		moveX = 0;
		moveY = 0;
		direction = -1;
	}
	
	public void resetCombat() {
		walkingHome = true;
		underAttack = false;
		randomWalk = true;
		HP = maximumHealth;
		resetTimer = 0;
	}
	
	public void yell(String msg) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = (Player) PlayerHandler.players[j];
				c2.sendMessage(msg);
			}
		}
	}

	public int getNextWalkingDirection(int i) {
		if (!Region.canMove(absX, absY, (absX + moveX), (absY + moveY), heightLevel, 1, 1))
		return -1;
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if (dir == -1)
			return -1;
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	public void getNextNPCMovement(int i) {
		direction = -1;
		if (NPCHandler.npcs[i].freezeTimer == 0) {
			direction = getNextWalkingDirection(i);
		}
	}
	
	public void appendHitUpdate(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteC(hitDiff);
		if (hitDiff > 0) {
			str.writeByteS(1);
		} else {
			str.writeByteS(0);
		}
		str.writeWord(HP);
		str.writeWord(maximumHealth);
	}
	
	public void appendHitUpdate2(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteA(hitDiff2);
		if (hitDiff2 > 0) {
			str.writeByteC(1);
		} else {
			str.writeByteC(0);
		}
		str.writeWord(HP);
		str.writeWord(maximumHealth);
	}

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	/*
	 * public NPCDefinition getDefinition() { return definition; }
	 */
	
	public boolean hungerNPC = false;

	public static final Boundary BOUNDARY_CORP = new Boundary(/*(X)*/2974, /*(Y)*/4370,
			/*(X)*/3000, /*(Y)*/4390);
	
	public static final Boundary BOUNDARY_BOSS = new Boundary(/*(X)*/3254, /*(Y)*/3870,
			/*(X)*/3272, /*(Y)*/3888);

	public boolean inMulti() {
		if (Boundary.isIn(this, Lizardman.LIZARD)) {
			return true;
		}
		
		if(HungerManager.getSingleton().inArea(absX, absY)) {
			return true;
		}
		if (Boundary.isIn(this, Cerberus.WEST)) {
			return true;
		}
		if (Boundary.isIn(this, Kalphite.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Cerberus.NORTH)) {
			return true;
		}
		if (Boundary.isIn(this, Cerberus.EAST)) {
			return true;
		}
		if (Boundary.isIn(this, Zulrah.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Kraken.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Armadyl.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Bandos.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Saradomin.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Zamorak.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, BOUNDARY_CORP)) {
			return true;
		}
		if (Boundary.isIn(this, BOUNDARY_BOSS)) {
			return true;
		}
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607) || (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967) || (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831) || (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 2824 && absX <= 2944 && absY >= 5258 && absY <= 5369) || (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647) || (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117) || (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464) || (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)
				|| (absX >= 2962 && absX <= 3006 && absY >= 3621 && absY <= 3659) || (absX >= 3155 && absX <= 3214 && absY >= 3755 && absY <= 3803)) {
			return true;
		}
		return false;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966 || absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}

	public int getSize() {
		return size;
	}
}
