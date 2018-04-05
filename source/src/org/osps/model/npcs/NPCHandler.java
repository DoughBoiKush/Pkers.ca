package org.osps.model.npcs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.osps.Config;
import org.osps.Server;
import org.osps.clip.Region;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.content.drops.vetion.VDrops;
import org.osps.model.items.Item;
import org.osps.model.minigames.FishingTourney;
import org.osps.model.minigames.Wave;
import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.minigames.warriors_guild.AnimatedArmour;
import org.osps.model.npcs.boss.Archaeologist;
import org.osps.model.npcs.boss.Fanatic;
import org.osps.model.npcs.boss.NewWildernessBoss;
import org.osps.model.npcs.boss.Scorpia;
import org.osps.model.npcs.boss.Callisto.Callisto;
import org.osps.model.npcs.boss.Cerberus.Cerberus;
import org.osps.model.npcs.boss.Gorillas.DemonicGorilla;
import org.osps.model.npcs.boss.Kalphite.Queen;
import org.osps.model.npcs.boss.Kraken.impl.SpawnEntity;
import org.osps.model.npcs.boss.Lizardman.Lizardman;
import org.osps.model.npcs.boss.Vetion.Vetion;
import org.osps.model.npcs.boss.abstract_bosses.Ability;
import org.osps.model.npcs.boss.abyssalsire.AbyssalSireConstants;
import org.osps.model.npcs.boss.zulrah.Zulrah;
import org.osps.model.npcs.drops.NpcDropManager;
import org.osps.model.players.Boundary;
import org.osps.model.players.Events;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.combat.Hitmark;
import org.osps.model.players.skills.slayer.BossSlayer;
import org.osps.model.players.skills.slayer.EasyTask;
import org.osps.model.players.skills.slayer.HardTask;
import org.osps.model.players.skills.slayer.MediumTask;
import org.osps.model.players.skills.slayer.EasyTask.Task;
import org.osps.model.players.skills.slayer.HardTask.Hard;
import org.osps.model.players.skills.slayer.MediumTask.Medium;
import org.osps.util.Location3D;
import org.osps.util.Misc;

public class NPCHandler {
	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];
	public static int ScorpX;
	public static int ScorpY;

	public static void loadDefs() {
		if (!Config.PLACEHOLDER_ECONOMY) {
			loadAutoSpawn("./Data/cfg/spawn-config.cfg");
		} else {
			loadAutoSpawn("./Data/cfg/spawn-config2.cfg");
		}
	}

	/**
	 * Handles the message being sent to all players online.
	 */

	public void yell(String msg) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = (Player) PlayerHandler.players[j];
				c2.sendMessage(msg);
			}
		}
	}

	public void rareDrops(Player c, int item, int amount) {
		if (c.dropChance == 0) {
			// c.sendMessage("No rare drop acquired.");
			return;

		} else if (c.dropChance == 1) {
			for (int loot : c.getPA().RARE_ITEMS) {			
				if (loot == item) {
					yell("<img=10>[@blu@DROP@bla@] @bla@" + c.playerName + " received a @blu@RARE @bla@drop: @blu@"
							+ amount + "x " + Item.getItemName(item));
					c.dropChance = 0;
					return;
						}
				
			}
				for (int loot1 : c.getPA().VERY_RARE_ITEMS) {
				if (loot1 == item) {
					yell("<img=10>[@blu@DROP@bla@] @bla@" + c.playerName + " received a @blu@VERY RARE @bla@drop: @blu@"
							+ amount + "x " + Item.getItemName(item));
					c.dropChance = 0;
					return;
						}
				}
				for (int loot2 : c.getPA().EXTREMELY_RARE_ITEMS) {
				if (loot2 == item) {
					yell("<img=10>[@blu@DROP@bla@] @bla@" + c.playerName + " received a @blu@EXTREMELY RARE @bla@drop: @blu@" 
							+ amount + "x " + Item.getItemName(item));
					c.dropChance = 0;
					return;
						}
					}
				
				}
			}
	
	private void doProjectiles(NPC npc, Player player) {
		if (player == null) {
			return;
		}
		int x = player.getX();
		int y = player.getY();

		player.getPA().createPlayersProjectile(npc.absX, npc.absY, player.absX, player.absY, 40,
				getProjectileSpeed(npc.index), 314, 31, 0, -1, 5);

		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				int pX = player.getX();
				int pY = player.getY();
				int nX = npc.getX();
				int nY = npc.getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;
				int offX1 = (pY - nY) * -1;
				int offY1 = (pX - nX);
				int offX2 = (pY - nY) * -1;
				int offY2 = (pX - nX) * +2;
				player.getPA().createPlayersProjectile(pX, pY, offX1, offY1, 50,
						player.getCombat().getProjectileSpeed(), 315, 25, 10, player.oldPlayerIndex, 5);
				player.getPA().createPlayersProjectile(pX, pY, offX2, offY2, 50,
						player.getCombat().getProjectileSpeed(), 315, 31, 10, player.oldPlayerIndex, 5);
				/* Front */
				int offX3 = (pY - nY) * +1;
				int offY3 = (pX - nX);
				int offX4 = (pY - nY) * +1;
				int offY4 = (pX - nX) * +2;

				player.getPA().createPlayersProjectile(pX, pY, offX3, offY3, 50,
						player.getCombat().getProjectileSpeed(), 315, 36, 10, player.oldPlayerIndex, 10);
				player.getPA().createPlayersProjectile(pX, pY, offX4, offY4, 50,
						player.getCombat().getProjectileSpeed(), 315, 41, 10, player.oldPlayerIndex, 10);
				container.stop();
			}
		}, 3);
	}

	private ArrayList<int[]> coreCoordinates = new ArrayList<>(3);
	private boolean hasMinions = false;
	private int newMinion = 0;

	private void handleDarkCores(NPC npc, Player player) {
		if (player == null) {
			return;
		}
		int x = player.getX();
		int y = player.getY();
		coreCoordinates.add(new int[] { x, y });
		for (int[] point : coreCoordinates) {
			int nX = npc.absX + 2;
			int nY = npc.absY + 2;
			int iX = player.absX + 2;
			int iY = player.absY + 2;
			int x1 = point[0] + 1;
			int y1 = point[1] + 2;
			int offY = (nX - x1) * -1;
			int offX = (nY - y1) * -1;

			player.getPA().createPlayersProjectile(nX, nY, offX, offY, 40, getProjectileSpeed(npc.index), 319, 31, 0,
					-1, 5);
		}
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				for (int[] point : coreCoordinates) {
					int coordX = point[0];
					int coordY = point[1];
					// player.getPA().createPlayersStillGfx(317, coordX, coordY,
					// 0, 5);
					NPCHandler.spawnNpc67(player, 320, player.absX, player.absY, 2, -1, 50, 20, 2000, 80, true, true);
				}
				coreCoordinates.clear();
				container.stop();
			}

		}, 4);
	}

	public static NPC spawnNpcAndReturn(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.HP = HP;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.needRespawn = false;
		return npcs[slot] = newNPC;
	}

	public static void removeNpc(int id) {
		if (npcs[id] == null) {
			return;
		}
		// Server.region.remove(npcs[id], npcs[id].absX, npcs[id].absY,
		// npcs[id].heightLevel);
		npcs[id] = null;
	}

	public static NPC spawnNpc67(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP,
			int maxHit, int attack, int defence, boolean headIcon, boolean attackPlayer) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		// newNPC.HP = WildernessBoss.healthCalculations(c, i);
		newNPC.HP = HP;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.needRespawn = false;
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.index;
			}
		}
		return npcs[slot] = newNPC;
	}

	@SuppressWarnings("null")
	public void spawnNpc3(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon, boolean summonFollow) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.spawnedBy = c.getId();
		newNPC.underAttack = true;
		newNPC.face(c);
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (summonFollow) {
			newNPC.summoner = true;
			newNPC.summonedBy = c.index;
			c.hasNpc = true;
		}
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.index;
			}
		}
		npcs[slot] = newNPC;
	}

	public void spawnNpc6(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int MAXHit, int attack,
			int defence) {
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return;
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MAXHP = HP;
		newNPC.MAXHit = MAXHit * 10;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.needRespawn = false;
		npcs[slot] = newNPC;
	}

	public void stepAway(int i) {
		int[][] points = { { -1, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 } };

		for (int[] k : points) {
			int dir = NPCClipping.getDirection(k[0], k[1]);
			if (NPCDumbPathFinder.canMoveTo(npcs[i], dir)) {
				NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].getX() + NPCClipping.DIR[dir][0],
						npcs[i].getY() + NPCClipping.DIR[dir][1]);
				break;
			}
		}
	}

	public boolean isUndead(int index) {
		String name = getNpcListName(npcs[index].npcType);
		for (String s : Config.UNDEAD)
			if (s.equalsIgnoreCase(name))
				return true;
		return false;
	}

	@SuppressWarnings("unused")
	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY, 15)) {
					int nX = NPCHandler.npcs[i].getX() + offset(i);
					int nY = NPCHandler.npcs[i].getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
							npcs[i].projectileId, getProjectileStartHeight(npcs[i].npcType, npcs[i].projectileId),
							getProjectileEndHeight(npcs[i].npcType, npcs[i].projectileId), -c.getId() - 1, 65);
				}
			}
		}
	}

	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcType) {
		case 2208:
		case 239:
		case 6611:
		case 2054:
		case 3127:
		case 6612:
		case 3998:
		case 497:
		case 2551:
		case 6609:
		case 6342:
		case 2552:
		case 2553:
		case 2559:
		case 2560:
		case 2561:
		case 2563:
		case 2564:
		case 2565:
		case 2892:
		case 2894:
		case 6528:
			return true;

		}

		return false;
	}

	public static boolean isSpawnedBy(Player player, NPC npc) {
		if (player != null && npc != null)
			if (npc.spawnedBy == player.index || npc.killerId == player.index)
				return true;
		return false;
	}

	public int getClosePlayer(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (j == npcs[i].spawnedBy)
					return j;
				if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].npcSize,
						npcs[i].absX, npcs[i].absY, extraDistance(i) + distanceRequired(i) + followDistance(i))
						|| isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							return j;
				}
			}
		}
		return 0;
	}

	public boolean goodDistance(int npcX, int npcY, int npcSize, int playerX, int playerY, int distance) {
		return playerX >= (npcX - distance) && playerX <= (npcX + npcSize + distance) && playerY >= (npcY - distance)
				&& playerY <= (npcY + npcSize + distance);
	}

	public int getCloseRandomPlayer(int i) {
		ArrayList<Integer> players = new ArrayList<>();
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
					if (!Boundary.isIn(PlayerHandler.players[j], Boundary.GODWARS_BOSSROOMS)) {
						npcs[i].killerId = 0;
						continue;
					}
				}
				if (goodDistance(PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, npcs[i].absX,
						npcs[i].absY, distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((PlayerHandler.players[j].underAttackBy <= 0 && PlayerHandler.players[j].underAttackBy2 <= 0)
							|| PlayerHandler.players[j].inMulti())
						if (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel)
							players.add(j);

				}
			}
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		return 0;
	}

	public int extraDistance(int i) {
		switch (npcs[i].npcType) {
		case 494:
		case 496:
		case 2042:
		case 2043:
		case 2044:
		case 493:
			return 20;
		default:
			return 0;
		}
	}

	public boolean isAggressive(int i) {
		if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
			return true;
		}
		if (Boundary.isIn(npcs[i], Zulrah.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(npcs[i], NPC.BOUNDARY_CORP)) {
			return true;
		}
		switch (npcs[i].npcType) {
		case 5535:
		case 5867:
		case 5868:
		case 5869:
		case 5363:
		case 6609:
		case 6342:
		case 6618:
		case 6619:
		case 6611:
		case 2054:
		case 6615:
		case 2550:
		case 2551:
		case 2562:
		case 2563:
		case 3129:
		case 3132:
		case 3130:
		case 3131:
		case 2205:
		case 2208:
		case 2207:
		case 2206:
		case 6829:
		case 2215:
		case 2218:
		case 2217:
		case 2216:
		case 3163:
		case 3164:
		case 3165:
		case 3162:
		case 494:
		case 498:
		case 3943:
		case 6610:
		case Wave.TZ_KEK_SPAWN:
		case Wave.TZ_KIH:
		case Wave.TZ_KEK:
		case Wave.TOK_XIL:
		case Wave.YT_MEJKOT:
		case Wave.KET_ZEK:
		case Wave.TZTOK_JAD:
			return true;
		}
		return false;
		// return npcs[i].definition().isAggressive();
	}

	public static boolean allowUnclippedDamage(int i) {
		switch (npcs[i].npcType) {
		case AbyssalSireConstants.SLEEPING_NPC_ID:
			return true;
		}
		return false;
	}

	public static boolean isFightCaveNpc(int i) {
		if (npcs[i] == null)
			return false;
		switch (npcs[i].npcType) {
		case Wave.TZ_KEK_SPAWN:
		case Wave.TZ_KIH:
		case Wave.TZ_KEK:
		case Wave.TOK_XIL:
		case Wave.YT_MEJKOT:
		case Wave.KET_ZEK:
		case Wave.TZTOK_JAD:
			return true;
		}
		return false;
	}

	/**
	 * Summon npc, barrows, etc
	 **/
	@SuppressWarnings("null")
	public NPC spawnNpc(final Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		final NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.face(c);
		newNPC.HP = HP;
		newNPC.maximumHealth = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.index;
			}
		}

		for (int[] Guard : Events.NPCs) {
			if (newNPC.npcType == Guard[2]) {
				newNPC.forceChat("Halt, Thief!");
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						container.stop();
					}

					@Override
					public void stop() {
						newNPC.isDead = true;
						newNPC.updateRequired = true;
						c.hasEvent = false;
					}
				}, 200);
			}
		}
		npcs[slot] = newNPC;
		return newNPC;
	}

	public void spawnNpc34(final Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP,
			int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		final NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.face(c);
		newNPC.HP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		// newNPC.spawnedBy = c.getId();
		if (headIcon)
			c.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.index;
			}
		}

		for (int[] Guard : Events.NPCs) {
			if (newNPC.npcType == Guard[2]) {
				newNPC.forceChat("Halt, Thief!");
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						container.stop();
					}

					@Override
					public void stop() {
						newNPC.isDead = true;
						newNPC.updateRequired = true;
						c.hasEvent = false;
					}
				}, 200);
			}
		}
		npcs[slot] = newNPC;
	}

	static int i;

	public static void spawnNpc7(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		// newNPC.HP = WildernessBoss.healthCalculations(c, i);
		newNPC.HP = HP;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		npcs[slot] = newNPC;
	}

	/**
	 * Emotes
	 **/

	public static int getAttackEmote(int i) {
		if (AnimatedArmour.isAnimatedArmourNpc(npcs[i].npcType)) {
			return 412;
		}
		switch (NPCHandler.npcs[i].npcType) {
		case 7101:
			return npcs[i].attackType == 2 ? 4651 : 4652;
		case 7151:
			return 7227;
		case 7152:
			return 7225;
		case 7153:
			return 7224;
		case 499:
			return 3847;
		case 963:
			return 6232;
		case 965:
			return 1170;
		case 6767:
			return npcs[i].attackType == 0 ? 7192 : 7193;
		case 6615:
			return 6254;
		case 6619:
			return 811;
		case 5862:
			return npcs[i].attackType == 0 ? 4491 : 4490;
		case 5867:
			return 4503;
		case 5868:
		case 5869:
			return 4504;
		case 6618:
			return 3353;
		case 6345:
			return 1979;
		// case 5535:
		// return 3729;
		case 494:
			return npcs[i].attackType == 0 ? 3991 : 3992;
		// return 3992;
		case Zulrah.SNAKELING:
			return 1741;
		case 2208:
			return 7026;
		case 6600:
			return 153;
		case 2043:
			return 5806;
		case 411:
			return 1512;
		case 1734:
			return 3897;
		case 1724:
			return 3920;
		case 1709:
			return 3908;
		case 1704:
			return 3915;
		case 1699:
			return 3908;
		case 1689:
			return 3891;
		case 437:
			return -1;
		case 3209:
			if (npcs[i].attackType == 0)
				return 4234;
			else if (npcs[i].attackType == 2)
				return 4237;
		case 2037:// Skeleton
		case 70:
			return 5485;
		case 2042:
		case 2044:
			return 5069;
		case 5054:
			return 6562;
		case 6528:
			return 711;
		case 6611:
		case 6612:
			return Misc.random(1) == 0 ? 5485 : 5487;
		case 6610:
			return 5327;
		case 419:
			return -1;
		case 3998:
			return 3991;
		case 6609: // Callisto
			return 4925;
		case 73:
		case 5399:
		case 751: // zombie
		case 77:
			return 5568;
		case 5535:
			return 3618;
		case 484:
			return 1552;
		case 28:
		case 420:
		case 421:
		case 422:
		case 423:
			return 5568;
		case 5779: // giant mole
			return 3312;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 94;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 284;
		case 891: // moss
			return 4658;
		case 85: // ghost
			return 5540;
		case 2834: // bats
			return 4915;
		case 414: // banshee
			return 1523;
		case 4005: // dark beast
			return 2731;
		case 2206:
			return 6376;
		case 2207:
			return 7036;
		case 2216:
		case 2217:
		case 2218:
			return 6154;
		case 135:
			return 6579;
		case 2205: // 6575
			if (npcs[i].attackType == 2) {
				return 6969;
			} else if (npcs[i].attackType == 0) {
				return 6970;
			}
			// case 3129:
		case 3130:
		case 3131:
		case 3132:
			// return 6945;
			return 64;
		case 3129:
			return 6948;
		case 3163:
		case 3164:
		case 3165:
			return 6953;
		case 3162:
			return 6976;
		case 6267:
			return 359;
		case 6268:
			return 2930;
		case 6269:
			return 4652;
		case 6270:
			return 4652;
		case 6271:
			return 4320;
		case 6272:
			return 4320;
		case 6273:
			return 4320;
		case 6274:
			return 4320;
		case 1459:
			return 1402;
		case 2215:
			if (npcs[i].attackType == 0)
				return 7018;
			else
				return 7021;
		case 86:
		case 87:
			return 4933;
		case 871:// Ogre Shaman
		case 5181:// Ogre Shaman
		case 5184:// Ogre Shaman
		case 5187:// Ogre Shaman
		case 5190:// Ogre Shaman
		case 5193:// Ogre Shaman
			return 359;

		case 2892:
		case 2894:
			return 2868;
		case 3116:
			return 2621;
		case 3120:
			return 2625;
		case 3123:
			return 2637;
		case 2746:
			return 2637;
		case 3121:
		case 2167:
			return 2611;
		case 3125:// 360
			return 2647;
		case 5247:
			return 5411;
		case 13: // wizards
			return 711;

		case 655:
			return 5532;

		case 424:
			return 1557;

		case 448:
			return 1590;

		case 415: // abby demon
			return 1537;

		case 11: // nech
			return 1528;

		case 1543:
		case 1611: // garg
			return 1519;

		case 417: // basilisk
			return 1546;

		// case 924: //skele
		// return 260;

		case 239:// drags
			return npcs[i].attackType == 0 ? 80 : 81;
		case 247:
		case 259:
		case 268:
		case 264:
		case 2919:
		case 6593:
		case 270:
		case 1270:
		case 273:
		case 274:
		case 272:
			if (npcs[i].attackType == 3) {
				return 84;
			} else if (npcs[i].attackType == 0) {
				return 80;
			}
		case 2840: // earth warrior
			return 390;

		case 803: // monk
			return 422;

		case 52: // baby drag
			return 25;

		case 58: // Shadow Spider
		case 59: // Giant Spider
		case 60: // Giant Spider
		case 61: // Spider
		case 62: // Jungle Spider
		case 63: // Deadly Red Spider
		case 64: // Ice Spider
		case 3021:
			return 143;

		case 105: // Bear
		case 106:// Bear
			return 41;

		case 412:
			// case 2834:
			return 30;

		case 2033: // rat
			return 138;

		case 2031: // bloodworm
			return 2070;

		case 1769:
		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 101: // goblin
			return 6184;

		case 1767:
		case 397:
		case 1766:
		case 1768:
		case 81: // cow
			return 5849;

		case 21: // hero
			return 451;

		case 41: // chicken
			return 55;

		case 9: // guard
		case 32: // guard
		case 20: // paladin
			return 451;

		case 1338: // dagannoth
		case 1340:
		case 1342:

			return 1341;

		case 19: // white knight
			return 406;

		case 2084:
		case 111: // ice giant
		case 2098:
		case 2463:
			return 4651;
		case 3127:
			if (npcs[i].attackType == 2)
				return 2656;
			else if (npcs[i].attackType == 1)
				return 2652;
			else if (npcs[i].attackType == 0)
				return 2655;
		case 2452:
			return 1312;

		case 2889:
			return 2859;

		case 118:
		case 291:
			return 99;

		case 2006:// Lesser Demon
		case 2026:// Greater Demon
		case 1432:// Black Demon
		case 1472:// jungle demon
			return 64;

		case 1267:
		case 100:
			return 1312;

		case 2841: // ice warrior
		case 178:
			return 451;

		case 1153: // Kalphite Worker
		case 1154: // Kalphite Soldier
		case 1155: // Kalphite guardian
		case 1156: // Kalphite worker
		case 1157: // Kalphite guardian
			return 1184;

		case 123:
		case 122:
			return 164;

		case 1675: // karil
			return 2075;

		case 1672: // ahrim
			return 729;

		case 1673: // dharok
			return 2067;

		case 1674: // guthan
			return 2080;

		case 1676: // torag
			return 0x814;

		case 1677: // verac
			return 2062;

		case 2265: // supreme
			return 2855;

		case 2266: // prime
			return 2854;

		case 2267: // rex
			return 2851;
		case 6342:
			int test = Misc.random(2);
			if (test == 2) {
				return 5895;
			} else if (test == 1) {
				return 5894;
			} else {
				return 5896;
			}

		case 2054:
			return 3146;
		case AbyssalSireConstants.MELEE_NPC_ID:
			return AbyssalSireConstants.ATTACK_ANIMATION;

		default:
			NpcDefinition def = npcs[i].definition();
			return def != null ? def.getAttackAnimation() : -1;
		}
	}

	public int getDeadEmote(int i) {
		if (AnimatedArmour.isAnimatedArmourNpc(npcs[i].npcType)) {
			return 836;
		}

		switch (npcs[i].npcType) {
		case 7101:
			return 4673;
		case 7151:
		case 7152:
		case 7153:
			return 7229;
		case AbyssalSireConstants.SLEEPING_NPC_ID:
		case AbyssalSireConstants.MELEE_NPC_ID:
		case AbyssalSireConstants.CHARGING_NPC_ID:
			return AbyssalSireConstants.DEATH_ANIMATION;
		case 499:
			return 3849;
		case 963:
			Queen.Phase_Change(npcs[i]);
			return 6233;
		case 965:
			return 6233;
		case 6767:
			return 7194;
		case 6618:
		case 6619:
			return 836;
		case 5862:
			return 4495;
		case 6615:
			return 6256;
		case 494:
			return 3993;
		case 5535:
			return 3620;
		case 2042:
		case 2043:
		case 2044:
			return -1;
		case 2208:
			return 7028;

		case Zulrah.SNAKELING:
			return 2408;
		case 6601:
		case 6602:
			return 65535;
		case 1739:
		case 1740:
		case 1741:
		case 1742:
		case 1747:
			return 65535;
		case 1734:
			return 3894;
		case 1724:
			return 3922;
		case 1709:
			return 3910;
		case 1704:
			return 3917;
		case 1699:
			return 3901;
		case 1689:
			return 3888;
		case 2037:// Skeleton
		case 70:
			return 5491;
		case 437:
			return -1;
		case 3209:
			return 4233;
		case 411:
			return 1513;
		case 5054:
			return 6564;
		case 6611:
		case 6612:
			return 5491;
		case 6610:
			return 5329;
		case 3998:
			return 3987;
		case 871:// Ogre Shaman
			return 361;
		case 6609: // Castillo
			return 4929;
		case 73:
		case 5399:
		case 77:
			return 5569;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 97;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 287;
		case 420:
		case 421:
		case 422:
		case 423:
		case 28:
			return 5569;
		// begin new updates
		case 891: // moss
			return 4659;
		case 85: // ghost
			return 5542;
		case 2834: // bats
			return 4917;
		// end
		case 3163:
		case 3164:
		case 3165:
			return 6956;
		case 2206:
			return 6377;
		case 2207:
			return 7034;
		case 2216:
		case 2217:
		case 2218:
			return 6156;
		case 6142:
			return 1;
		case 6143:
			return 1;
		case 6144:
			return 1;
		case 6145:
			return 1;
		case 100:
			return 1314;
		case 414:// Battle Mage
			return 1524;
		case 3742:// Ravager
		case 3743:
		case 3744:
		case 3745:
		case 3746:
			return 3916;
		case 3772:// Brawler
		case 3773:
		case 3774:
		case 3775:
		case 3776:
			return 3894;
		case 5779: // giant mole
			return 3313;
		case 135:
			return 6576;
		case 2205:
			return 6968;
		// case 3129:
		case 3130:
		case 3131:
		case 3132:
			return 67;
		case 3129:
			return 6949;
		/*
		 * case 2215: return 7062;
		 */
		case 3162:
			return 6975;
		case 6267:
			return 357;
		case 6268:
			return 2938;
		case 6269:
			return 4653;
		case 6270:
			return 4653;
		case 6271:
			return 4321;
		case 6272:
			return 4321;
		case 6273:
			return 4321;
		case 6274:
			return 4321;
		case 2098:
		case 2463:
			return 4651;
		case 1459:
			return 1404;
		/*
		 * case 414: // banshee return 1524;
		 */
		case 2559:
		case 2560:
		case 2561:
			return 6956;
		case 3121:
		case 2167:
			return 2607;
		case 3116:
			return 2620;
		case 3120:
			return 2627;
		case 3118:
			return 2627;
		case 3123:
			return 2638;
		case 2746:
			return 2638;
		case 3125:
			return 2646;
		case 3127:
			return 2654;
		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return -1;
		case 6342:
			return 5898;
		case 2054:
			return 3147;
		case 2035: // spider
			return 146;
		case 2033: // rat
			return 141;
		case 2031: // bloodvel
			return 2073;
		case 1769:
		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 101: // goblin
			return 6182;
		case 1767:
		case 397:
		case 1766:
		case 1768:
		case 81: // cow
			return 5851;
		case 41: // chicken
			return 57;
		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1342;
		case 2265:
		case 2266:
		case 2267:
			return 2856;
		case 111: // ice giant
			return 131;
		case 2841: // ice warrior
			return 843;
		case 751:// Zombies!!
			return 302;
		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
		case 1632: // turoth!
			return 1597;
		case 417: // basilisk
			return 1548;
		case 1653: // hand
			return 1590;
		case 2006:// demons
		case 2026:
		case 1432:
			return 67;
		case 6:// abby spec
			return 1508;
		case 51:// baby drags
		case 52:
		case 1589:
		case 3376:
			return 28;
		case 1543:
		case 1611:
			return 1518;
		case 484:
		case 1619:
			return 1553;
		case 419:
		case 1621:
			return 1563;
		case 4005:
			return 2732;
		case 415:
			return 1538;
		case 424:
			return 1558;
		case 11:
			return 1530;
		case 435:
		case 1634:
		case 1635:
		case 1636:
			return 1580;
		case 448:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1654:
		case 1655:
		case 1656:
		case 1657:
			return 1590;
		case 102:
			return 313;
		case 105:
		case 106:
			return 44;
		case 412:
			// case 2834:
			return 36;
		case 122:
		case 123:
			return 167;
		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
		case 3021:
			return 146;
		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 1190;
		case 104:
			return 5534;
		case 118:
		case 291:
			return 102;
		case 239:// drags
			return 92;
		case 247:
		case 259:
		case 268:
		case 264:
		case 270:
		case 2919:
		case 1270:
		case 273:
		case 274:
			return 92;
		default:
			NpcDefinition def = npcs[i].definition();
			return def != null ? def.getDeathAnimation() : -1;
		}
	}

	/**
	 * Attack delays
	 **/
	public int getNpcDelay(int i) {
		switch (npcs[i].npcType) {
		case 3127:
			return 8;
		default:
			NpcDefinition def = npcs[i].definition();
			return def != null ? def.getAttackSpeed() : 4;
		}
	}

	private int getProjectileStartHeight(int npcType, int projectileId) {
		switch (npcType) {
		case 3127:
			return 110;
		case 2044:
			return 60;
		case 3163:
		case 3164:
		case 3165:
			return 60;
		case 6610:
			switch (projectileId) {
			case 165:
				return 20;
			}
			break;
		}
		return 43;
	}

	private int getProjectileEndHeight(int npcType, int projectileId) {
		switch (npcType) {
		case 6610:
			switch (projectileId) {
			case 165:
				return 30;
			}
			break;
		}
		return 31;
	}

	/**
	 * Hit delays
	 **/
	public int getHitDelay(int i) {
		switch (npcs[i].npcType) {
		case 6611:
		case 6612:
			return npcs[i].attackType == 2 ? 3 : 2;
		case 6618:
			return npcs[i].attackType == 2 ? 3 : 2;
		case 6528:
		case 6610:
			return 3;
		case 6619:
			return npcs[i].attackType == 2 ? 3 : 2;
		case 2265:
		case 2266:
			// case 2267:
		case 2054:
		case 2892:
		case 2894:
			return 3;
		case 2215:
			npcs[i].graardor = Misc.random(5);
			if (npcs[i].graardor == 5) {
				return 3;
			}
			return 2;

		case 3129:
			npcs[i].tsutsaroth = Misc.random(4);
			if (npcs[i].tsutsaroth == 4) {
				return 3;
			}
			return 2;
		case 2205:
			npcs[i].zilyana = Misc.random(6);
			if (npcs[i].zilyana == 6 || npcs[i].zilyana == 5 || npcs[i].zilyana == 4) {
				return 6;
			}
			return 2;

		case 3125:
		case 3121:
		case 2167:
		case 2558:
		case 2559:
		case 2560:
			return 3;

		case 3127:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
				return 5;
			else
				return 2;

		case 2025:
			return 4;
		case 2028:
			return 3;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public int getRespawnTime(int i) {
		NpcDefinition def = npcs[i].definition();
		if (def != null) {
			return def.getRespawnTime();
		}
		return 30;

	}

	public static NPC newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1)
			return null; // no free slot found
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType; // try
		npcs[slot] = newNPC;
		return newNPC;
	}
	/*
	 * public void handleClipping(int i) { NPC npc = npcs[i]; if (npc.moveX == 1
	 * && npc.moveY == 1) { if ((Region.getClipping(npc.absX + 1, npc.absY + 1,
	 * npc.heightLevel) & 0x12801e0) != 0) { npc.moveX = 0; npc.moveY = 0; if
	 * ((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel) &
	 * 0x1280120) == 0) npc.moveY = 1; else npc.moveX = 1; } } else if
	 * (npc.moveX == -1 && npc.moveY == -1) { if ((Region.getClipping(npc.absX -
	 * 1, npc.absY - 1, npc.heightLevel) & 0x128010e) != 0) { npc.moveX = 0;
	 * npc.moveY = 0; if ((Region.getClipping(npc.absX, npc.absY - 1,
	 * npc.heightLevel) & 0x1280102) == 0) npc.moveY = -1; else npc.moveX = -1;
	 * } } else if (npc.moveX == 1 && npc.moveY == -1) { if
	 * ((Region.getClipping(npc.absX + 1, npc.absY - 1, npc.heightLevel) &
	 * 0x1280183) != 0) { npc.moveX = 0; npc.moveY = 0; if
	 * ((Region.getClipping(npc.absX, npc.absY - 1, npc.heightLevel) &
	 * 0x1280102) == 0) npc.moveY = -1; else npc.moveX = 1; } } else if
	 * (npc.moveX == -1 && npc.moveY == 1) { if ((Region.getClipping(npc.absX -
	 * 1, npc.absY + 1, npc.heightLevel) & 0x128013) != 0) { npc.moveX = 0;
	 * npc.moveY = 0; if ((Region.getClipping(npc.absX, npc.absY + 1,
	 * npc.heightLevel) & 0x1280120) == 0) npc.moveY = 1; else npc.moveX = -1; }
	 * } // Checking Diagonal movement.
	 * 
	 * if (npc.moveY == -1) { if ((Region.getClipping(npc.absX, npc.absY - 1,
	 * npc.heightLevel) & 0x1280102) != 0) npc.moveY = 0; } else if (npc.moveY
	 * == 1) { if ((Region.getClipping(npc.absX, npc.absY + 1, npc.heightLevel)
	 * & 0x1280120) != 0) npc.moveY = 0; } // Checking Y movement. if (npc.moveX
	 * == 1) { if ((Region.getClipping(npc.absX + 1, npc.absY, npc.heightLevel)
	 * & 0x1280180) != 0) npc.moveX = 0; } else if (npc.moveX == -1) { if
	 * ((Region.getClipping(npc.absX - 1, npc.absY, npc.heightLevel) &
	 * 0x1280108) != 0) npc.moveX = 0; } }
	 * 
	 */
	public static void kill(int npcId) {
		if (npcs[npcId] == null) {
			return;
		}
		npcs[npcId].animId = 0x328;
		npcs[npcId].updateRequired = true;
		npcs[npcId].isDead = true;
		npcs[npcId].animUpdateRequired = true;
		npcs[npcId].actionTimer = 0;
		npcs[npcId].needRespawn = true;
	}

	public void process() {
		try {
			// Player player = PlayerHandler.players[npcs[i].spawnedBy];
			for (int i = 0; i < maxNPCs; i++) {
				if (npcs[i] == null)
					continue;
				npcs[i].onReset();
			}

			if (Misc.random(45) == 0) {
				FishingTourney.getSingleton().shuffleSpots();
			}

			for (int i = 0; i < maxNPCs; i++) {
				if (npcs[i] != null) {
					int type = npcs[i].npcType;
					Player slaveOwner = (PlayerHandler.players[npcs[i].summonedBy] != null
							? (Player) PlayerHandler.players[npcs[i].summonedBy] : null);
					if (npcs[i] != null && slaveOwner == null && npcs[i].summoner) {
						npcs[i].absX = 0;
						npcs[i].absY = 0;
					}
					if (npcs[i] != null && slaveOwner != null && slaveOwner.hasNpc
							&& (!slaveOwner.goodDistance(npcs[i].getX(), npcs[i].getY(), slaveOwner.absX,
									slaveOwner.absY, 15) || slaveOwner.heightLevel != npcs[i].heightLevel)
							&& npcs[i].summoner) {
						npcs[i].absX = slaveOwner.absX;
						npcs[i].absY = slaveOwner.absY;
						npcs[i].heightLevel = slaveOwner.heightLevel;

					}

					if (npcs[i].actionTimer > 0) {
						npcs[i].actionTimer--;
					}

					if (npcs[i].npcType == 306) {
						if (System.currentTimeMillis() - npcs[i].lastText > 15000) {
							npcs[i].updateRequired = true;
							npcs[i].forceChat("Please read the Pkers.Ca tutorial, it is essential to understand Pkers.Ca.");
							npcs[i].lastText = System.currentTimeMillis();
						}
					}

					if (npcs[i].freezeTimer > 0) {
						npcs[i].freezeTimer--;
					}
					// Player playerr = PlayerHandler.players[i];

					if (npcs[i].hitDelayTimer > 0) {
						npcs[i].hitDelayTimer--;
					}

					if (npcs[i].hitDelayTimer == 1) {
						npcs[i].hitDelayTimer = 0;
						applyDamage(i);
					}

					if (npcs[i].attackTimer > 0) {
						npcs[i].attackTimer--;
					}

					if (npcs[i].HP > 0 && !npcs[i].isDead) {
						if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
							if (npcs[i].HP < (npcs[i].maximumHealth / 2) && !npcs[i].spawnedMinions) {
								NPCHandler.spawnNpc(5054, npcs[i].getX() - 1, npcs[i].getY(), 0, 1, 175, 14, 100, 120);
								NPCHandler.spawnNpc(5054, npcs[i].getX() + 1, npcs[i].getY(), 0, 1, 175, 14, 100, 120);
								npcs[i].spawnedMinions = true;
							}
						}
					}

					if (npcs[i].npcType == 6602 && !npcs[i].isDead) {
						NPC runiteGolem = getNpc(6600);
						if (runiteGolem != null && !runiteGolem.isDead) {
							npcs[i].isDead = true;
							npcs[i].needRespawn = false;
							npcs[i].actionTimer = 0;
						}
					}
					if (npcs[i].spawnedBy > 0) { // delete summons npc
						if (PlayerHandler.players[npcs[i].spawnedBy] == null
								|| PlayerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
								|| PlayerHandler.players[npcs[i].spawnedBy].respawnTimer > 0
								|| !PlayerHandler.players[npcs[i].spawnedBy].goodDistance(npcs[i].getX(),
										npcs[i].getY(), PlayerHandler.players[npcs[i].spawnedBy].getX(),
										PlayerHandler.players[npcs[i].spawnedBy].getY(),
										NPCHandler.isFightCaveNpc(i) ? 60 : 20)) {

							if (PlayerHandler.players[npcs[i].spawnedBy] != null) {
								for (int o = 0; o < PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs.length; o++) {
									if (npcs[i].npcType == PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][0]) {
										if (PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] == 1)
											PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] = 0;
									}
								}
							}
							npcs[i] = null;
						}
					}

					if (npcs[i] == null)
						continue;
					if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
						npcs[i].lastX = npcs[i].getX();
						npcs[i].lastY = npcs[i].getY();
					}

					if (type >= 2042 && type <= 2044 && npcs[i].HP > 0) {
						Player player = PlayerHandler.players[npcs[i].spawnedBy];
						if (player != null && player.getZulrahEvent().getNpc() != null
								&& npcs[i].equals(player.getZulrahEvent().getNpc())) {
							int stage = player.getZulrahEvent().getStage();
							if (type == 2042) {
								if (stage == 0 || stage == 1 || stage == 4 || stage == 9 && npcs[i].totalAttacks >= 20
										|| stage == 11 && npcs[i].totalAttacks >= 5) {
									continue;
								}
							}
							if (type == 2044) {
								if ((stage == 5 || stage == 8) && npcs[i].totalAttacks >= 5) {
									continue;
								}
							}
						}
					}
					/**
					 * Attacking player
					 **/
					// Player player = PlayerHandler.players[npcs[i].spawnedBy];
					if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead && !switchesAttackers(i)) {
						npcs[i].killerId = getCloseRandomPlayer(i);
					} else if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead && switchesAttackers(i)) {
						npcs[i].killerId = getCloseRandomPlayer(i);
					}

					if (NpcDefinition.DEFINITIONS[i].isAggressive() && !npcs[i].underAttack && !npcs[i].isDead
							&& !switchesAttackers(i)) {
						npcs[i].killerId = getCloseRandomPlayer(i);
					} else if (NpcDefinition.DEFINITIONS[i].isAggressive() && !npcs[i].underAttack && !npcs[i].isDead
							&& switchesAttackers(i)) {
						npcs[i].killerId = getCloseRandomPlayer(i);
					}
					if (npcs[i].npcType == 320) {
						npcs[i].damageDealt += npcs[i].damageDone;

					}

					if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000) {
						npcs[i].underAttackBy = 0;
						npcs[i].underAttack = false;
					}

					if ((npcs[i].killerId > 0 || npcs[i].underAttack) && !npcs[i].walkingHome
							&& retaliates(npcs[i].npcType)) {
						if (!npcs[i].isDead) {
							int p = npcs[i].killerId;
							if (PlayerHandler.players[p] != null) {
								if (npcs[i].summoner == false) {
									Player c = PlayerHandler.players[p];
									followPlayer(i, c.index);
									if (npcs[i] == null)
										continue;
									if (npcs[i].attackTimer == 0) {
										attackPlayer(c, i);
										// Player player =
										// PlayerHandler.players[npcs[i].spawnedBy];
										// npcs[i].face(player);
										// npcs[i].lastKillerId = c.playerId;
									}
								} else {
									Player c = PlayerHandler.players[p];
									if (npcs[i].absX == c.absX && npcs[i].absY == c.absY) {
										stepAway(i);
										npcs[i].randomWalk = false;
									} else {
										followPlayer(i, c.index);
									}
								}
							} else {
								npcs[i].killerId = 0;
								npcs[i].underAttack = false;
								npcs[i].face(null);
								// npcs[i].face(c);
							}

						}
					}

					/**
					 * Random walking and walking home
					 **/
					if (npcs[i] == null)
						continue;
					if ((!npcs[i].underAttack || npcs[i].walkingHome) && !isFightCaveNpc(i) && npcs[i].randomWalk
							&& !npcs[i].isDead) {
						npcs[i].face(null);
						npcs[i].killerId = 0;
						if (npcs[i].spawnedBy == 0) {
							if ((npcs[i].absX > npcs[i].makeX + Config.NPC_RANDOM_WALK_DISTANCE)
									|| (npcs[i].absX < npcs[i].makeX - Config.NPC_RANDOM_WALK_DISTANCE)
									|| (npcs[i].absY > npcs[i].makeY + Config.NPC_RANDOM_WALK_DISTANCE)
									|| (npcs[i].absY < npcs[i].makeY - Config.NPC_RANDOM_WALK_DISTANCE)) {
								npcs[i].walkingHome = true;
							}
						}
						if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
							npcs[i].walkingHome = false;
						} else if (npcs[i].walkingHome) {
							NPCDumbPathFinder.walkTowards(npcs[i], npcs[i].makeX, npcs[i].makeY);
							//npcs[i].updateRequired = true;
						}
						if (npcs[i].walkingType >= 0) {
							switch (npcs[i].walkingType) {

							case 5:
								npcs[i].face(npcs[i].absX - 1, npcs[i].absY);
								break;

							case 4:
								npcs[i].face(npcs[i].absX + 1, npcs[i].absY);
								break;

							case 3:
								npcs[i].face(npcs[i].absX, npcs[i].absY - 1);
								break;
							case 2:
								npcs[i].face(npcs[i].absX, npcs[i].absY + 1);
								break;

							default:
								if (npcs[i].walkingType >= 0) {
									npcs[i].face(npcs[i].absX, npcs[i].absY);
								}
								break;
							}
						}
						if (npcs[i].walkingType == 1) {
							if (Misc.random(3) == 1 && !npcs[i].walkingHome) {
								int direction = Misc.random3(8);
								int movingToX = npcs[i].getX() + NPCClipping.DIR[direction][0];
								int movingToY = npcs[i].getY() + NPCClipping.DIR[direction][1];
								if (Math.abs(npcs[i].makeX - movingToX) <= 1 && Math.abs(npcs[i].makeY - movingToY) <= 1
										&& NPCDumbPathFinder.canMoveTo(npcs[i], direction)) {
									NPCDumbPathFinder.walkTowards(npcs[i], movingToX, movingToY);
								}
							}
						}
					}
					if (npcs[i].isDead == true) {
						if (npcs[i].npcType == 6618) {
							npcs[i].forceChat("Ow!");
						}
						if (SpawnEntity.DISTURBED_POOLS.get(npcs[i]) != null)
							SpawnEntity.DISTURBED_POOLS.remove(npcs[i]);

						Player player = PlayerHandler.players[npcs[i].spawnedBy];

						if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false && npcs[i].needRespawn == false) {
							if (npcs[i].npcType == 6611) {
								npcs[i].requestTransform(6612);
								npcs[i].HP = 255;
								npcs[i].isDead = false;
								npcs[i].spawnedMinions = false;
								npcs[i].forceChat("Do it again!!");
							} else {
								if (npcs[i].npcType == 6612) {
									npcs[i].npcType = 6611;
									npcs[i].spawnedMinions = false;
								}
								npcs[i].updateRequired = true;
								npcs[i].face(null);
								npcs[i].killedBy = getNpcKillerId(i);
								npcs[i].animId = getDeadEmote(i); // dead emote
								npcs[i].animUpdateRequired = true;
								npcs[i].freezeTimer = 0;
								npcs[i].applyDead = true;
								killedBarrow(i);
								killedCrypt(i);
								if (player != null) {
									this.tzhaarDeathHandler(player, i);
									continue;
								}
								npcs[i].actionTimer = 4; // delete time
								resetPlayersInCombat(i);
							}
						} else if (npcs[i].actionTimer == 0 && npcs[i].applyDead == true
								&& npcs[i].needRespawn == false) {
							if (npcs[i].hungerNPC) {
								HungerManager.getSingleton().handleNPCDeath(PlayerHandler.players[npcs[i].killedBy],
										npcs[i]);
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i] = null;
								continue;
							}
							npcs[i].needRespawn = true;
							npcs[i].actionTimer = getRespawnTime(i); // respawn
																		// //
																		// time
							dropItems(i);
							appendBossSlayerExperience(i);
							appendSlayerExperience(i);
							appendBossKC(i);
							appendKillCount(i);
							npcs[i].absX = npcs[i].makeX;
							npcs[i].absY = npcs[i].makeY;
							npcs[i].HP = npcs[i].maximumHealth;
							npcs[i].animId = 0x328;
							npcs[i].updateRequired = true;
							npcs[i].animUpdateRequired = true;
							if (npcs[i].npcType == 3127) {
								handleJadDeath(i);
							}

							if (npcs[i].npcType == 320) {
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i] = null;
								return;
							}
	                        Player c = PlayerHandler.players[npcs[i].killedBy];
							
							if (npcs[i].npcType == NewWildernessBoss.NPC_ID) {
								try {
								NewWildernessBoss.restart(Optional.ofNullable(c));
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i].isDead = true;
								npcs[i] = null;
							//	Server.task.handleDeath();
								return;
								}catch (Exception e) {
									e.printStackTrace();
								}
							}

							if (npcs[i].npcType == 6345) {
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i].isDead = true;
								npcs[i] = null;
								Server.task.handleDeath();
								return;
							}

							if (npcs[i].npcType == 7101) {
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i].isDead = true;
								npcs[i] = null;
								Server.task.handleDeath();
								return;
							}

							if (npcs[i].npcType == 5054) {
								npcs[i].absX = -1;
								npcs[i].absY = -1;
								npcs[i].heightLevel = -1;
								npcs[i].needRespawn = false;
								npcs[i] = null;
								return;
							}

							if (npcs[i].npcType == 6600) {
								spawnNpc(6601, npcs[i].absX, npcs[i].absY, 0, 0, 0, 0, 0, 0);
							} else if (npcs[i].npcType == 6601) {
								spawnNpc(6602, npcs[i].absX, npcs[i].absY, 0, 0, 0, 0, 0, 0);
								npcs[i] = null;
								NPC golem = getNpc(6600);
								if (golem != null) {
									golem.actionTimer = 150;
								}
							}
						} else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn == true && npcs[i].npcType != 1739
								&& npcs[i].npcType != 1740 && npcs[i].npcType != 1741 && npcs[i].npcType != 1742
								&& npcs[i].npcType != 1747) {
							if (player != null) {
								npcs[i] = null;
							} else {
								int old1 = npcs[i].npcType;
								int old2 = npcs[i].makeX;
								int old3 = npcs[i].makeY;
								int old4 = npcs[i].heightLevel;
								int old5 = npcs[i].walkingType;
								int old6 = npcs[i].maximumHealth;
								int old7 = npcs[i].maxHit;
								int old8 = npcs[i].attack;
								int old9 = npcs[i].defence;
								npcs[i] = null;
								newNPC(old1, old2, old3, old4, old5, old6, old7, old8, old9);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void applyDamage(int i) {
		if (npcs[i] != null) {
			if (PlayerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead)
				return;
			if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType == 1747) {
				return;
			}
			Player c = PlayerHandler.players[npcs[i].oldIndex];
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (c.playerIndex <= 0 && c.npcIndex <= 0)
				if (c.autoRet == 1)
					c.npcIndex = i;
			if (c.attackTimer <= 3 || c.attackTimer == 0 && c.npcIndex == 0 && c.oldNpcIndex == 0) {
				if (!NPCHandler.isFightCaveNpc(i))
					c.animation(c.getCombat().getBlockEmote());
			}

			npcs[i].totalAttacks++;
			boolean protectionIgnored = prayerProtectionIgnored(i);
			if (c.respawnTimer <= 0) {
				int damage = 0;
				int secondDamage = -1;
				if (npcs[i].attackType == 0) {
					damage = Misc.random(getMaxHit(i));
					// damage = Misc.random(npcs[i].maxHit); //TODO
					// damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(c.getCombat().calculateMeleeDefence()) > Misc
							.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
					}

					if (npcs[i].npcType == 2043 && c.getZulrahEvent().getNpc() != null
							&& c.getZulrahEvent().getNpc().equals(npcs[i])) {
						Boundary boundary = new Boundary(npcs[i].targetedLocation.getX(),
								npcs[i].targetedLocation.getY(), npcs[i].targetedLocation.getX(),
								npcs[i].targetedLocation.getY());
						if (!Boundary.isIn(c, boundary)) {
							return;
						}
						damage = 20 + Misc.random(25);
					}
					if (c.prayerActive[18] && !protectionIgnored) { // protect
																	// from
																	// melee
						if (npcs[i].npcType == 1677 || npcs[i].npcType == 1158 || npcs[i].npcType == 7101
								|| npcs[i].npcType == 7153 || npcs[i].npcType == 5779 || npcs[i].npcType == 1160
								|| npcs[i].npcType == 8349 || npcs[i].npcType == 8133 || npcs[i].npcType == 2054
								|| npcs[i].npcType == 239 || npcs[i].npcType == 998 || npcs[i].npcType == 319
								|| npcs[i].npcType == 6342 || npcs[i].npcType == 2205 || npcs[i].npcType == 2206
								|| npcs[i].npcType == 999 || npcs[i].npcType == 239 || npcs[i].npcType == 1000)
							damage = (damage / 2);
						else
							damage = 0;
					}
					if (c.playerEquipment[c.playerShield] == 12817) {
						if (Misc.random(100) > 30 && damage > 0) {
							damage *= .75;
						}
					}

					if (npcs[i].npcType == 5867) {
						if (!c.prayerActive[17]) {
							Cerberus.GhostDamage(c);
						}
					}
					if (npcs[i].npcType == 5868) {
						if (!c.prayerActive[16]) {
							Cerberus.GhostDamage(c);
						}
					}
					if (npcs[i].npcType == 5869) {
						if (!c.prayerActive[18]) {
							Cerberus.GhostDamage(c);
						}
					}

					if (damage == 0 && npcs[i].npcType == 7153) {
						c.hits++;
					}
					
//					if (npcs[i].npcType == 2054) {
//						damage = Misc.random(25);
//					}

					if (npcs[i].npcType == 320) {
						if (npcs[i] != null) {
							npcs[319].HP += damage;
							npcs[320].animId = 1689;
							npcs[320].animUpdateRequired = true;
							npcs[i].updateRequired = true;
							c.sendMessage("The corporal beast has been healed: " + damage);
						}
					}

					if (c.vengOn && damage > 0) {
						c.getCombat().appendVengeanceNPC(i, damage);
					}

					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
				} else if (npcs[i].attackType == 1) { // range
					damage = Misc.random(getMaxHit(i)); // TODO
					// damage = Misc.random(npcs[i].maxHit);
					int rangeDefence = Misc.random(c.getCombat().calculateRangeDefence());
					int npcAttack = Misc.random(NPCHandler.npcs[i].attack);
					System.out.println("range def " + rangeDefence + " npc attack " + npcAttack);
					if (rangeDefence > npcAttack) {
						damage = 0;
					}
					if (c.prayerActive[17] && !protectionIgnored) { // protect
																	// from
																	// range
						if (npcs[i].npcType == 1677 || npcs[i].npcType == 963 || npcs[i].npcType == 7151
								|| npcs[i].npcType == 965 || npcs[i].npcType == 3129 || npcs[i].npcType == 1158
								|| npcs[i].npcType == 1160 || npcs[i].npcType == 8349 || npcs[i].npcType == 8133
								|| npcs[i].npcType == 319 || npcs[i].npcType == 2054 || npcs[i].npcType == 2208
								|| npcs[i].npcType == 239 || npcs[i].npcType == 239) {
							damage = (damage / 2);
						} else {
							damage = 0;
						}
						if (c.playerLevel[3] - damage < 0) {
							damage = c.playerLevel[3];
						}
					}
					if (npcs[i].npcType == 2042 || npcs[i].npcType == 2044 || npcs[i].npcType == 320) {
						if (c.isSusceptibleToVenom()) {
							c.setVenomDamage((byte) 6);
						}
					}
					if (damage == 0 && npcs[i].npcType == 7151) {
						c.hits++;
					}
					if (npcs[i].endGfx > 0 && isFightCaveNpc(i)) {
						c.gfx100(npcs[i].endGfx);
					}
				} else if (npcs[i].attackType == 2) { // magic
					damage = Misc.random(getMaxHit(i)); // TODO
					// damage = Misc.random(npcs[i].maxHit);
					boolean magicFailed = false;
					if (npcs[i].npcType == 2205) {
						secondDamage = Misc.random(27);
					}
					if (10 + Misc.random(c.getCombat().mageDef()) > Misc.random(NPCHandler.npcs[i].attack)) {
						damage = 0;
						if (secondDamage > -1) {
							secondDamage = 0;
						}
						magicFailed = true;
					}
					if (npcs[i].npcType == 6609) {
						damage = Misc.random(getMaxHit(i));
						c.sendMessage("Callisto's fury sends an almighty shockwave through you.");
					}
					if (npcs[i].npcType == 2205 && npcs[i].attackType == 2) {
						damage = Misc.random(31);
					}
					if (npcs[i].npcType == 6342) {
						damage = Misc.random(35);
					}
					if (c.prayerActive[16] && !protectionIgnored) {
						if (npcs[i].npcType == 494 || npcs[i].npcType == 5535 || npcs[i].npcType == 7101
								|| npcs[i].npcType == 7152 || npcs[i].npcType == 963 || npcs[i].npcType == 965
								|| npcs[i].npcType == 239 || npcs[i].npcType == 319 || npcs[i].npcType == 2205
								|| npcs[i].npcType == 2207 || npcs[i].npcType == 6609 || npcs[i].npcType == 6610
								|| npcs[i].npcType == 319) {
							int max = npcs[i].npcType == 494 ? 2 : 0;
							if (Misc.random(2) == 0) {
								damage = 1 + Misc.random(max);
							} else {
								damage = 0; // TODO
								if (secondDamage > -1) {
									secondDamage = 0;
								}
							}
						} else if (npcs[i].npcType == 1677 || npcs[i].npcType == 1158 || npcs[i].npcType == 1160
								|| npcs[i].npcType == 8349 || npcs[i].npcType == 8133 || npcs[i].npcType == 2054
								|| npcs[i].npcType == 239 || npcs[i].npcType == 6528) {
							damage /= 2;
						} else {
							damage = 0;
							if (secondDamage > -1) {
								secondDamage = 0;
							}
							magicFailed = true;
						}
					}
					if (c.playerLevel[3] - damage < 0) {
						damage = c.playerLevel[3];
					}
					if (damage == 0 && npcs[i].npcType == 7152) {
						c.hits++;
					}
					if (npcs[i].endGfx > 0 && (!magicFailed || isFightCaveNpc(i))) {
						c.gfx100(npcs[i].endGfx);
					} else {
						c.gfx100(85);
					}
				} else if (npcs[i].attackType == 3 && !c.hungerGames) { // fire
																		// breath
					int resistance = c.getItems().isWearingItem(1540) || c.getItems().isWearingItem(11283)
							|| c.getItems().isWearingItem(11284) ? 1 : 0;
					if (System.currentTimeMillis() - c.lastAntifirePotion < c.antifireDelay) {
						resistance++;
					}
					if (c.summonId == 12653) {
						resistance = 3;
					}
					if (resistance == 0) {
						if (getNpcDef()[npcs[i].npcType].getName().contains("baby")) {
							damage = Misc.random(15);
						} else {
							damage = Misc.random(35);
						}
						c.sendMessage("You are badly burnt by the dragon fire!");
					} else if (resistance == 1) {
						damage = Misc.random(10);
					} else if (resistance == 2) {
						damage = 0;
					} else if (resistance == 3) {
						damage = 0;
						c.sendMessage("Your Prince Black Dragon fully protects you from the dragon fire.");
					}
					if (npcs[i].endGfx != 430 && resistance == 2) {
						damage = 5 + Misc.random(5);
					}
					switch (npcs[i].endGfx) {
					case 429:
						if (c.isSusceptibleToPoison()) {
							c.setPoisonDamage((byte) 6);
						}
						break;

					case 428:
						c.freezeTimer = 10;
						break;

					case 431:
						c.lastSpear.reset();
						break;
					}
					if (c.playerLevel[3] - damage < 0)
						damage = c.playerLevel[3];
					c.gfx100(npcs[i].endGfx);
				} else if (npcs[i].attackType == 4) { // special attacks
					damage = Misc.random(getMaxHit(i));
					// damage = Misc.random(npcs[i].maxHit(i));
					switch (npcs[i].npcType) {
					case 3129:
						int prayerReduction = c.playerLevel[5] / 2;
						if (prayerReduction < 1) {
							break;
						}
						c.playerLevel[5] -= prayerReduction;
						if (c.playerLevel[5] < 0) {
							c.playerLevel[5] = 0;
						}
						c.getPA().refreshSkill(5);
						c.sendMessage(
								"K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.");
						break;
					case 6528:
						prayerReduction = c.playerLevel[5] / 10;
						if (prayerReduction < 1) {
							break;
						}
						c.playerLevel[5] -= prayerReduction;
						if (c.playerLevel[5] < 0) {
							c.playerLevel[5] = 0;
						}
						c.getPA().refreshSkill(5);
						c.sendMessage("Your prayer has been drained drastically.");
						break;
					case 6610:
						if (c.prayerActive[16]) {
							damage *= .7;
						}
						secondDamage = Misc.random(getMaxHit(i));
						if (secondDamage > 0) {
							c.gfx0(80);
						}
						break;
					}
				}
				int poisonDamage = isPoisionous(npcs[i]);
				if (poisonDamage > 0 && c.isSusceptibleToPoison() && Misc.random(10) == 1) {
					c.setPoisonDamage((byte) poisonDamage);
				}
				if (c.playerLevel[3] - damage < 0 || secondDamage > -1 && c.playerLevel[3] - secondDamage < 0) {
					damage = c.playerLevel[3];
					if (secondDamage > -1) {
						secondDamage = 0;
					}
				}
				handleSpecialEffects(c, i, damage);
				c.logoutDelay.reset();
				if (damage > -1) {
					c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
				}
				if (secondDamage > -1) {
					c.appendDamage(secondDamage, secondDamage > 0 ? Hitmark.HIT : Hitmark.MISS);
				}
				c.getCombat().applyRecoilNPC(damage + (secondDamage > 0 ? secondDamage : 0), i);
				c.getCombat().applyDharokRecoil(damage + (secondDamage > 0 ? secondDamage : 0), i);
				if (c.playerLevel[3] <= 0 && npcs[i].npcType == 28) {
					c.setKilledByZombie(true);
				}
				c.getPA().refreshSkill(3);
				c.updateRequired = true;
			}
		}
	}

	private int isPoisionous(NPC npc) {
		switch (npc.npcType) {
		case 961:
			return 8;
		case 3129:
			return 16;
		case 2042:
		case 2043:
		case 2044:
			return 6;
		}
		return 0;
	}

	/*
	 * private int getPoisonDamage(NPC npc) { if (npc == null) { return 0; }
	 * switch (npc.npcType) { case 3129: return 16; }
	 * if(npc.definition().isPoisonous()){ return 6; }
	 * 
	 * return 0; }
	 */

	private int multiAttackDistance(NPC npc) {
		if (npc == null) {
			return 0;
		}
		switch (npc.npcType) {
		case 239:
			return 35;
		}
		return 15;
	}

	public void multiAttackDamage(int i) {
		int damage = Misc.random(getMaxHit(i));
		// int damage = getMaxHit(i);
		// int max = getMaxHit(i);
		Hitmark hitmark = damage > 0 ? Hitmark.HIT : Hitmark.MISS;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if (c.isDead || c.heightLevel != npcs[i].heightLevel)
					continue;
				if (PlayerHandler.players[j].goodDistance(c.absX, c.absY, npcs[i].absX, npcs[i].absY,
						multiAttackDistance(npcs[i]))) {
					if (npcs[i].attackType == 4) {
						if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
							if (!(c.absX > npcs[i].absX - 5 && c.absX < npcs[i].absX + 5 && c.absY > npcs[i].absY - 5
									&& c.absY < npcs[i].absY + 5)) {
								continue;
							}
							c.sendMessage(
									"Vet'ion pummels the ground sending a shattering earthquake shockwave through you.");
							Vetion.createVetionEarthquake(c);
						}
						c.appendDamage(damage, hitmark);
					} else if (npcs[i].attackType == 3) {
						int resistance = c.getItems().isWearingItem(1540) || c.getItems().isWearingItem(11283)
								|| c.getItems().isWearingItem(11284) ? 1 : 0;
						if (System.currentTimeMillis() - c.lastAntifirePotion < c.antifireDelay) {
							resistance++;
						}
						c.sendMessage("Resistance: " + resistance);
						if (resistance == 0) {
							damage = Misc.random(getMaxHit(i));
							c.sendMessage("You are badly burnt by the dragon fire!");
						} else if (resistance == 1)
							damage = Misc.random(15);
						else if (resistance == 2)
							damage = 0;
						if (c.playerLevel[3] - damage < 0)
							damage = c.playerLevel[3];
						c.gfx100(npcs[i].endGfx);
						c.appendDamage(damage, hitmark);
					} else if (npcs[i].attackType == 2) {
						if (npcs[i].npcType == 6611 || npcs[i].npcType == 6612) {
							if (Vetion.vetionSpellCoordinates.stream()
									.noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}
						if (npcs[i].npcType == 319) {
							if (coreCoordinates.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}

						if (npcs[i].npcType == 6619) {
							if (Fanatic.EARTH.stream().noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								continue;
							}
						}

						if (npcs[i].npcType == 6618) {
							if (Archaeologist.SPELL_COORDINATES.stream()
									.noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
								if (Archaeologist.NEXT_SPELL.stream()
										.noneMatch(p -> p[0] == c.absX && p[1] == c.absY)) {
									continue;
								}
							}
						}

						if (!c.prayerActive[16]) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().mageDef())) {
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else {
								c.appendDamage(0, Hitmark.MISS);
							}

						} else {
							if (npcs[i].npcType == 6610) {
								damage *= .7;
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else if (npcs[i].npcType == 6528) {
								damage *= .5;
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else {
								c.appendDamage(0, Hitmark.MISS);
							}
						}
					} else if (npcs[i].attackType == 1) {
						if (!c.prayerActive[17]) {
							if (Misc.random(500) + 200 > Misc.random(c.getCombat().calculateRangeDefence())) {
								c.appendDamage(damage, damage > 0 ? Hitmark.HIT : Hitmark.MISS);
							} else {
								c.appendDamage(0, Hitmark.MISS);
							}
						} else {
							c.appendDamage(0, Hitmark.MISS);
						}
					}
					if (npcs[i].endGfx > 0) {
						c.gfx0(npcs[i].endGfx);
					}
				}
				c.getPA().refreshSkill(3);
			}
		}
	}

	public boolean getsPulled(int i) {
		switch (npcs[i].npcType) {
		case 2215:
			if (npcs[i].firstAttacker > 0)
				return false;
			break;
		}
		return true;
	}

	public boolean multiAttacks(int i) {
		switch (npcs[i].npcType) {
		case 6345:
			if (npcs[i].attackType == 2) {
				return true;
			}
		case 7101:
			return true;
		case 6618:
			return npcs[i].attackType == 2 || npcs[i].attackType == 1 ? true : false;
		case 6611:
		case 6612:
		case 6619:
			return npcs[i].attackType == 2 || npcs[i].attackType == 4 ? true : false;
		case 6528:
			return npcs[i].attackType == 2 || npcs[i].attackType == 4 && Misc.random(3) == 0 ? true : false;
		case 6610:
			return npcs[i].attackType == 2;
		case 2558:
			return true;
		case 2562:
			if (npcs[i].attackType == 2)
				return true;
		case 2215:
			return npcs[i].attackType == 1;
		case 3162:
			return true;
		default:
			return false;
		}

	}

	/**
	 * Npc killer id?
	 **/

	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int killerId = 0;
		int count = 0;
		for (int p = 1; p < Config.MAX_PLAYERS; p++) {
			if (PlayerHandler.players[p] != null) {
				if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
					if (PlayerHandler.players[p].totalDamageDealt > oldDamage
							|| (killerId != 0 && PlayerHandler.players[killerId].ironman)) {
						if (count > 0 && PlayerHandler.players[p].ironman)
							continue;
						oldDamage = PlayerHandler.players[p].totalDamageDealt;
						killerId = p;
						count++;
					}
					PlayerHandler.players[p].totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	/**
	 * 
	 */
	private void killedCrypt(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			for (int o = 0; o < c.barrowCrypt.length; o++) {
				if (npcs[i].npcType == c.barrowCrypt[o][0]) {
					c.barrowsKillCount++;
					c.getPA().sendFrame126("" + c.barrowsKillCount, 16137);
				}
			}
		}
	}

	private void killedBarrow(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			for (int o = 0; o < c.barrowsNpcs.length; o++) {
				if (npcs[i].npcType == c.barrowsNpcs[o][0]) {
					c.barrowsNpcs[o][1] = 2; // 2 for dead
					c.barrowsKillCount++;
				}
			}
		}
	}

	private void tzhaarDeathHandler(Player player, int i) {// hold a vit plz
		if (npcs[i] != null) {
			if (player != null) {
				if (player.getFightCave() != null) {
					if (isFightCaveNpc(i))
						killedTzhaar(player, i);
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private void killedTzhaar(Player player, int i) {
		if (player.getFightCave() != null) {
			player.getFightCave().setKillsRemaining(player.getFightCave().getKillsRemaining() - 1);
			if (player.getFightCave().getKillsRemaining() == 0) {
				player.waveId++;
				player.getFightCave().spawn();
			}
		}
	}

	public void handleJadDeath(int i) {
		Player c = PlayerHandler.players[npcs[i].spawnedBy];
	}

	/**
	 * Dropping Items!
	 **/

	public void dropItems(int i) {

		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			if (npcs[i].npcType == 2042 || npcs[i].npcType == 2043 || npcs[i].npcType == 2044) {
				c.getZulrahEvent().stop();
			}

			if (npcs[i].npcType == 7151 || npcs[i].npcType == 7152 || npcs[i].npcType == 7153) {
				c.hits = 0;
			}

			if (npcs[i].npcType == 965) {
				c.spawnKal = true;
			}

			if (npcs[i].npcType == 963) {
				c.getCombat().resetPlayerAttack();
			}

			if (npcs[i].npcType == 6612 || npcs[i].npcType == 6611) {
				VDrops.dropLoot(c, i);
			}

			if (npcs[i].npcType == 5862) {
				c.CAST_GHOSTS = 0;
				c.CAST_ROCKS = 0;
			}

			if (npcs[i].npcType == 6767) {
				c.SPAWN_LIZARDS = 0;
			}

			if (npcs[i].npcType == 319) {
				newMinion = 0;
				hasMinions = false;
				kill(320, 2);
			}

			if (c.ARMADYL_INSTANCE == true && npcs[i].npcType == 3162 || npcs[i].npcType == 3163
					|| npcs[i].npcType == 3164 || npcs[i].npcType == 3165) {
				c.ARMADYL_MINION++;
			}

			if (c.ARMADYL_MINION == 4) {
				c.ARMADYL_MINION = 0;
				c.ARMADYL_INSTANCE = false;
				c.getArmadyl().stop();
				c.getArmadyl().RespawnNpcs();
			}

			if (c.BANDOS_INSTANCE == true && npcs[i].npcType == 2215 || npcs[i].npcType == 2216
					|| npcs[i].npcType == 2217 || npcs[i].npcType == 2218) {
				c.BANDOS_MINION++;
			}

			if (c.BANDOS_MINION == 4) {// true that
				c.BANDOS_MINION = 0;
				c.BANDOS_INSTANCE = false;
				c.getBandos().stop();
				c.getBandos().RespawnNpcs();
			}

			if (c.KALPHITE_INSTANCE == true && npcs[i].npcType == 965) {
				c.KALPHITE_MINION++;
			}

			if (c.KALPHITE_MINION == 1) {// true that
				c.KALPHITE_MINION = 0;
				c.KALPHITE_INSTANCE = false;
				c.getKalphite().stop();
				c.getKalphite().RespawnNpc();
			}

			if (c.SARADOMIN_INSTANCE == true && npcs[i].npcType == 2205 || npcs[i].npcType == 2206
					|| npcs[i].npcType == 2207 || npcs[i].npcType == 2208) {
				c.SARADOMIN_MINION++;
			}

			if (c.SARADOMIN_MINION == 4) {
				c.SARADOMIN_MINION = 0;
				c.SARADOMIN_INSTANCE = false;
				c.getSaradomin().stop();
				c.getSaradomin().RespawnNpcs();
			}

			if (c.ZAMORAK_INSTANCE == true && npcs[i].npcType == 3129 || npcs[i].npcType == 3130
					|| npcs[i].npcType == 3131 || npcs[i].npcType == 3132) {
				c.ZAMORAK_MINION++;
			}

			if (c.ZAMORAK_MINION == 4) {
				c.ZAMORAK_MINION = 0;
				c.ZAMORAK_INSTANCE = false;
				c.getZamorak().stop();
				c.getZamorak().RespawnNpcs();
			}

			if (npcs[i].npcType == 494) {
				if (c.getKraken().getInstancedKraken() != null) {
					KILL_TENT(c, 5535, 3691, 5814, c.getKraken().getInstancedKraken().getHeight());
					KILL_TENT(c, 5535, 3691, 5809, c.getKraken().getInstancedKraken().getHeight());
					KILL_TENT(c, 5535, 3700, 5814, c.getKraken().getInstancedKraken().getHeight());
					KILL_TENT(c, 5535, 3700, 5809, c.getKraken().getInstancedKraken().getHeight());
					// KILL_TENT(c, npcid, npcs[i].absX, npcs[i].absY,
					// c.heightLevel);
					Server.itemHandler.createGroundItem(c, 526, 3696, 5807, c.heightLevel, 1, c.index);
					c.getKraken().stop();
				}
			}

			if (npcs[i].npcType == 6615) {
				// KILL_TENT(c, 6617, npcs[i].absX + 1, npcs[i].absY, 0);
				// KILL_TENT(c, 6617, npcs[i].absX + 2, npcs[i].absY, 0);
				kill(6617, 0);
				spawnedGuardian = false;
			}

			if (npcs[i].npcType == 2461 || npcs[i].npcType == 2463 || npcs[i].npcType == 2464) {
				c.getWarriorsGuild().dropDefender(npcs[i].absX, npcs[i].absY);
			}

			/*
			 * BRONZE(2450, 1155, 1117, 1075, 5, 10, 2, 20, 20), IRON(2451,
			 * 1153, 1115, 1067, 10, 20, 4, 30, 30), STEEL(2452, 1157, 1119,
			 * 1069, 15, 40, 6, 50, 50), MITHRIL(2454, 1159, 1121, 1071, 50, 80,
			 * 10, 100, 100), ADAMANT(2455, 1161, 1123, 1073, 60, 100, 13, 120,
			 * 120), RUNE(2456, 1163, 1127, 1079, 80, 120, 18, 150, 150);
			 */

			if (Server.task != null) {
				if (Server.task.getNPC() != null && Server.task.getNPC() == npcs[i]) {
					if (Server.task.getAttackers() != null && !Server.task.getAttackers().isEmpty()) {
						for (final Player p : Server.task.getAttackers()) {
							int id = 0;
							Server.task.dropLoot(p);
						}
						return;
					}
				}
			}

			c.getNpcDeathTracker().add(getNpcListName(npcs[i].npcType));
			c.getBossDeathTracker().add(getNpcListName(npcs[i].npcType));

			if (AnimatedArmour.isAnimatedArmourNpc(npcs[i].npcType)) {
				AnimatedArmour.dropTokens(c, npcs[i].npcType, npcs[i].absX, npcs[i].absY);
			}

			int random = Misc.random(1500);
			int chaos = Misc.random(1000);
			int chaos1 = Misc.random(300);
			int zulrahpet = Misc.random(3000);
			if (npcs[i].npcType == 2054 && chaos == 100) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(11995, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Chaos Elemental pet.");
					}
				}
			}
			if (npcs[i].npcType == 6619 && chaos == 600) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(11995, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Chaos Elemental pet.");
					}
				}
			}
			if ((npcs[i].npcType >= 2042 && npcs[i].npcType <= 2044) && zulrahpet == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12921, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Zulrah snakeling.");
					}
				}
			}
			if ((npcs[i].npcType >= 2042 && npcs[i].npcType <= 2044) && zulrahpet == 201) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12939, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Zulrah snakeling.");
					}
				}
			}
			if ((npcs[i].npcType >= 2042 && npcs[i].npcType <= 2044) && zulrahpet == 202) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12940, 1);

				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Zulrah snakeling.");

					}
				}
			}
			if (npcs[i].npcType == 239 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12653, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Prince Black Dragon.");
					}
				}
			}
			if (npcs[i].npcType == 2265 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12643, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Dagannoth Supreme Pet.");
					}
				}
			}
			if (npcs[i].npcType == 2267 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12645, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Dagannoth Rex pet.");
					}
				}
			}
			if (npcs[i].npcType == 5862 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(13247, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x HellPuppy pet.");
					}
				}
			}
			if (npcs[i].npcType == 3129 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12652, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x K'ril Tsutsaroth pet.");
					}
				}
			}
			if (npcs[i].npcType == 2205 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12651, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Commander Zilyana pet.");
					}
				}
			}
			if (npcs[i].npcType == 3162 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12649, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Kree'Arra pet.");
					}
				}
			}
			if (npcs[i].npcType == 2215 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12650, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x General Graardor Jr.");
					}
				}
			}
			if (npcs[i].npcType == 494 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12655, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Kraken Jr.");
					}
				}
			}
			if (npcs[i].npcType == 6609 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(15572, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Callisto pet.");
					}
				}
			}
			if ((npcs[i].npcType == 6611 || npcs[i].npcType == 6612) && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(15573, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Vet'ion pet.");
					}
				}
			}
			if (npcs[i].npcType == 6610 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(8135, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Venenatis pet.");
					}
				}
			}
			if (npcs[i].npcType == 5779 && random == 200) {
				c.sendMessage("@red@You receive a boss pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(12646, 1);
				for (int j = 0; j < PlayerHandler.players.length; j++) {
					if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a drop: 1 x Baby Mole.");
					}
				}
			}
			if (npcs[i].npcType == 912 || npcs[i].npcType == 913 || npcs[i].npcType == 914)
				c.magePoints += 1;
			int dropX = npcs[i].absX;
			int dropY = npcs[i].absY;
			int dropHeight = npcs[i].heightLevel;
			NpcDropManager.dropItems(Optional.of(c), npcs[i]);
		}
	}

	public void sendLootShareMessage(String message) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				c2.sendMessage("<col=006600>" + message + "");
			}
		}
	}

	public int[] unallowed = { 2366, 592, 4587, 1149, 530, 526, 536, 1333, 1247, 1089, 1047, 1319 };

	public void handleLootShare(Player c, int item, int amount) {
		for (int i = 0; i < unallowed.length; i++) {
			if (item == unallowed[i]) {
				return;
			}
		}
		if (c.getShops().getItemShopValue(item, 1, c.getItems().getItemSlot(item)) > 100000) {
			sendLootShareMessage(c.playerName + " received a drop: " + amount + " x "
					+ org.osps.model.items.Item.getItemName(item));
			/*
			 * int npcId = 0; NpcDefinition.DEFINITIONS[npcId].getName();
			 * PlayerHandler.executeGlobalMessage("<col=CC0000>" +
			 * WordUtils.capitalize(c.playerName) + "</col><col=255>" +
			 * " Has received a </col><col=CC0000>" +
			 * org.osps.model.items.Item.getItemName(item) +
			 * "</col><col=255>'s" + " drop from "+
			 * NpcDefinition.DEFINITIONS[npcId].getName());
			 */
		}
	}

	// id of bones dropped by npcs
	public int boneDrop(int type) {
		switch (type) {
		case 1:// normal bones
		case 9:
		case 12:
		case 17:
		case 803:
		case 18:
		case 81:
		case 101:
		case 41:
		case 19:
		case 90:
		case 75:
		case 86:
		case 2834:
		case 1543:
		case 1611:
		case 414:
		case 448:
		case 446:
		case 484:
		case 424:
		case 181:
		case 291:
		case 135:
		case 26:
		case 1341:
			return 526;
		case 2098:
		case 2099:
		case 2100:
		case 2101:
		case 2102:
		case 2103:
		case 2463:
		case 891:
			return 532;// big bones
		case 239:// drags
		case 247:
		case 259:
		case 268:
		case 264:
		case 2919:
		case 1270:
		case 270:
		case 273:
		case 274:
		case 4385:
		case 252:
		case 265:
		case 260:
		case 272:
			return 536;
		case 1432:
		case 415:
		case 11:
		case 2006:
		case 2054:
			return 592;
		case 2265:
		case 2266:
		case 2267:
			return 6729;
		default:
			return -1;
		}
	}

	public void appendKillCount(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] kcMonsters = { 122, 49, 2558, 2559, 2560, 2561, 2550, 2551, 2552, 2553, 2562, 2563, 2564, 2565 };
			for (int j : kcMonsters) {
				if (npcs[i].npcType == j) {
					if (c.killCount < 20) {
						// c.killCount++;
						// c.sendMessage("Killcount: " + c.killCount);
					} else {
						// c.sendMessage("You already have 20 kill count");
					}
					break;
				}
			}
		}
	}

	public void appendBossKC(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int[] bossKC = { 3998, 5779, 239, 2054, 1158, 1159, 1160, 2205, 3162, 3129, 2215, 6342, 6609, 6610 };
			for (int j : bossKC) {
				if (npcs[i].npcType == j) {
					c.bossKills += 1;
					break;
				}
			}
		}
	}

	public int getStackedDropAmount(int itemId, int npcId) {
		switch (itemId) {
		case 995:
			switch (npcId) {
			case 1:
				return 50 + Misc.random(50);
			case 9:
				return 133 + Misc.random(100);
			case 424:
				return 1000 + Misc.random(300);
			case 484:
				return 1000 + Misc.random(300);
			case 446:
				return 1000 + Misc.random(300);
			/*
			 * case 1543: return 1000 + Misc.random(1000);
			 */
			case 11:
				return 1500 + Misc.random(1250);
			case 415:
				return 3000;
			case 18:
				return 500;
			case 101:
				return 60;
			case 1611:
			case 1543:
			case 414:
				return 750 + Misc.random(500);
			/*
			 * case 414: return 250 + Misc.random(500);
			 */
			case 448:
				return 250 + Misc.random(250);
			case 90:
				return 200;
			case 2006:
				return 1000 + Misc.random(455);
			case 52:
				return 400 + Misc.random(200);
			case 135:
				return 1500 + Misc.random(2000);
			case 1341:
				return 1500 + Misc.random(500);
			case 26:
				return 500 + Misc.random(100);
			case 20:
				return 750 + Misc.random(100);
			case 21:
				return 890 + Misc.random(125);
			case 2098:
				return 500 + Misc.random(250);
			case 3121:
				return 500 + Misc.random(350);
			}
			break;
		case 11212:
			return 10 + Misc.random(4);
		case 565:
		case 561:
			return 10;
		case 560:
		case 563:
		case 562:
			return 15;
		case 555:
		case 554:
		case 556:
		case 557:
			return 20;
		case 892:
			return 40;
		case 886:
			return 100;
		case 6522:
			return 6 + Misc.random(5);

		}

		return 1;
	}

	public void appendBossSlayerExperience(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			if (c.bossTaskAmount < 0 || BossSlayer.Task.forNpc(c.bossSlayerTask) == null) {
				c.bossSlayerTask = 0;
				c.bossTaskAmount = 0;
				return;
			}
			if (c.getBossSlayer().isSlayerTask(npcs[i].npcType)) {
				c.bossTaskAmount--;
				if (c.playerEquipment[c.playerHands] == 6720) {
					c.getPA().addSkillXP(npcs[i].maximumHealth * (Config.SLAYER_EXPERIENCE * 1.10), 18);
				} else {
					c.getPA().addSkillXP(npcs[i].maximumHealth * Config.SLAYER_EXPERIENCE, 18);
				}
				if (c.bossTaskAmount <= 0) {
					if (c.playerEquipment[c.playerHands] == 6720) {
						c.getPA().addSkillXP((npcs[i].maximumHealth * 8) * (Config.SLAYER_EXPERIENCE * 1.10), 18);
					} else {
						c.getPA().addSkillXP((npcs[i].maximumHealth * 8) * Config.SLAYER_EXPERIENCE, 18);
					}
					int points = 10;
					c.bossSlayerTask = -1;
					c.getPA().loadQuests();
					c.slayerPoints += points;
					c.slayerSpree += 1;
					c.sendMessage("Task complete! You receive: " + points
							+ " slayer points. Report back to the Slayer master!");
					if (c.slayerSpree == 10) {
						c.slayerPoints += 10;
						c.sendMessage(
								"You receive 10 extra points for completing " + c.slayerSpree + " tasks in a row.");
					} else if (c.slayerSpree == 25) {
						c.slayerPoints += 20;
						c.sendMessage(
								"You receive 20 extra points for completing " + c.slayerSpree + " tasks in a row.");
					} else if (c.slayerSpree == 50) {
						c.slayerPoints += 25;
						c.sendMessage(
								"You receive 25 extra points for completing " + c.slayerSpree + " tasks in a row.");
					} else if (c.slayerSpree == 100) {
						c.slayerPoints += 50;
						c.sendMessage(
								"You receive 100 extra points for completing " + c.slayerSpree + " tasks in a row.");
					} else if (c.slayerSpree >= 110 && c.slayerSpree % 10 == 0) {
						c.slayerPoints += 15;
						c.sendMessage(
								"You receive 15 extra points for completing " + c.slayerSpree + " tasks in a row.");
					}
					if (Misc.random(5) == 0) {
						c.getPA().rewardPoints(5, "Congrats, You randomly got @blu@5 @bla@PK Points from boss slayer!");
					}
				}
			}
		}
	}

	/**
	 * Slayer Experience
	 **/
	public void appendSlayerExperience(int i) {
		Player c = PlayerHandler.players[npcs[i].killedBy];
		if (c != null) {
			int points = -1;
			if (EasyTask.Task.forNpc(c.slayerTask) != null) {
				points = EasyTask.Task.forNpc(c.slayerTask).getDifficulty() * 4;
			}
			if (MediumTask.Medium.forNpc(c.slayerTask) != null) {
				points = MediumTask.Medium.forNpc(c.slayerTask).getDifficulty() * 6;
			}
			if (HardTask.Hard.forNpc(c.slayerTask) != null) {
				points = HardTask.Hard.forNpc(c.slayerTask).getDifficulty() * 8;
			}
			if (c.taskAmount < 0 || points == -1) {
				c.slayerTask = 0;
				c.taskAmount = 0;
				return;
			}
			if (c.getEasy().isSlayerTask(npcs[i].npcType) || c.getMedium().isSlayerTask(npcs[i].npcType)
					|| c.getHard().isSlayerTask(npcs[i].npcType)) {
				c.taskAmount--;
				if (c.playerEquipment[c.playerHands] == 6720) {
					c.getPA().addSkillXP(npcs[i].maximumHealth * (Config.SLAYER_EXPERIENCE * 1.10), 18);
				} else {
					c.getPA().addSkillXP(npcs[i].maximumHealth * Config.SLAYER_EXPERIENCE, 18);
				}
				if (c.taskAmount <= 0) {
					if (c.playerEquipment[c.playerHands] == 6720) {
						c.getPA().addSkillXP((npcs[i].maximumHealth * 2) * (Config.SLAYER_EXPERIENCE * 1.10), 18);
					} else {
						c.getPA().addSkillXP((npcs[i].maximumHealth * 2) * Config.SLAYER_EXPERIENCE, 18);
					}
					c.slayerTask = -1;
					c.getPA().loadQuests();
					c.slayerPoints += points;
					c.slayerSpree += 1;
					c.EASY = false;
					c.HARD = false;
					c.MEDIUM = false;
					c.changedTaskAmount = 0;
					c.getPA().addSkillXP(Player.getLevelForXP(c.playerXP[18]) * 1000, 18);
					c.sendMessage("Task complete! You receive: " + points
							+ " slayer points. Report back to the Slayer master!");
					if (c.slayerSpree == 10) {
						c.slayerPoints += 10;
						c.sendMessage(
								"You receive 10 extra points for completing " + c.slayerSpree + " tasks in a row.");
					} else if (c.slayerSpree == 25) {
						c.slayerPoints += 20;
						c.sendMessage(
								"You receive 20 extra points for completing " + c.slayerSpree + " tasks in a row.");
					} else if (c.slayerSpree == 50) {
						c.slayerPoints += 25;
						c.sendMessage(
								"You receive 25 extra points for completing " + c.slayerSpree + " tasks in a row.");
					} else if (c.slayerSpree == 100) {
						c.slayerPoints += 50;
						c.sendMessage(
								"You receive 100 extra points for completing " + c.slayerSpree + " tasks in a row.");
					} else if (c.slayerSpree >= 110 && c.slayerSpree % 10 == 0) {
						c.slayerPoints += 15;
						c.sendMessage(
								"You receive 15 extra points for completing " + c.slayerSpree + " tasks in a row.");
					}
					if (Misc.random(5) == 0) {
						c.getPA().rewardPoints(5, "Congrats, You randomly got @blu@5 @bla@PK Points from slayer!");
					}
				}
			}
		}
	}

	/**
	 * Resets players in combat
	 */
	public static NPC getNpc(int npcType) {
		for (NPC npc : npcs)
			if (npc != null && npc.npcType == npcType)
				return npc;
		return null;
	}

	public static void spawnNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
                newNPC.maximumHealth = HP;
                newNPC.HP = HP;
                newNPC.maxHit = maxHit;
                
		npcs[slot] = newNPC;
	}

	public static NPC spawnGenNpc(int npcType, int x, int y, int heightLevel, int WalkingType) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.needRespawn = false;
		npcs[slot] = newNPC;
		return newNPC;
	}

	public static void spawnNpc111(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.needRespawn = false;
		npcs[slot] = newNPC;
	}

	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null)
				if (PlayerHandler.players[j].underAttackBy2 == i)
					PlayerHandler.players[j].underAttackBy2 = 0;
		}
	}

	public static NPC getNpc(int npcType, int x, int y) {
		for (NPC npc : npcs)
			if (npc != null && npc.npcType == npcType && npc.absX == x && npc.absY == y)
				return npc;
		return null;
	}

	public static NPC getNpc(int npcType, int x, int y, int height) {
		for (NPC npc : npcs) {
			if (npc != null && npc.npcType == npcType && npc.absX == x && npc.absY == y && npc.heightLevel == height) {
				return npc;
			}
		}
		return null;
	}

	/**
	 * Npc names
	 **/

	public static String getNpcName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return "None";
		}
		return NpcDefinition.DEFINITIONS[npcId].getName();
	}

	/**
	 * Npc Follow Player
	 **/

	public int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean followPlayer(int i) {
		switch (npcs[i].npcType) {
		case 5867:
		case 5868:
		case 5869:
		case 2042:
		case 2043:
		case 2044:
		case 494:
		case 497:
		case 5535:
		case 2892:
		case 2894:
		case 1739:
		case 1747:
		case 1740:
		case 1741:
		case 1742:
			return false;
		}
		return true;
	}

	public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null) {
			return;
		}
		if (!canAttack(i)) {
			return;
		}
		Player player = PlayerHandler.players[playerId];
		if (PlayerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].face(null);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}
		/*
		 * if (Boundary.isIn(npcs[i], Cerberus.WEST) || Boundary.isIn(npcs[i],
		 * Cerberus.NORTH) || Boundary.isIn(npcs[i], Cerberus.EAST)) { if
		 * (!Boundary.isIn(player, Cerberus.WEST) || !Boundary.isIn(player,
		 * Cerberus.NORTH) || !Boundary.isIn(player, Cerberus.EAST)) {
		 * npcs[i].killerId = 0; return; } }
		 */
		if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
			if (!Boundary.isIn(player, Boundary.GODWARS_BOSSROOMS)) {
				npcs[i].killerId = 0;
				return;
			}
		}
		Player c = PlayerHandler.players[npcs[i].oldIndex];
		if (Boundary.isIn(npcs[i], Zulrah.BOUNDARY)
				&& (npcs[i].npcType >= 2042 && npcs[i].npcType <= 2044 || npcs[i].npcType == 6720)) {
			return;
		}
		if (!followPlayer(i)) {
			npcs[i].face(PlayerHandler.players[playerId]);
			return;
		}
		if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType == 1747) {
			return;
		}
		int playerX = PlayerHandler.players[playerId].absX;
		int playerY = PlayerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		double distance = distanceRequired(i);
		if (npcs[i].getSize() > 1)
			distance += 0.5;
		if (npcs[i].getDistance(playerX, playerY) <= distance) {
			return;
		}

		if ((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX + Config.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absX > npcs[i].makeX - Config.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absY < npcs[i].makeY + Config.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absY > npcs[i].makeY - Config.NPC_FOLLOW_DISTANCE))) {

			if (npcs[i].heightLevel == PlayerHandler.players[playerId].heightLevel) {
				if (PlayerHandler.players[playerId] != null && npcs[i] != null) {
					NPCDumbPathFinder.follow(npcs[i], player);
				}
			}
		} else {
			npcs[i].face(null);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;

		}
	}

	private boolean spawnedGuardian = false;

	public void loadSpell(Player player, int npcId) {
		int chance = 0;

		if (DemonicGorilla.isDemonicGorilla(npcs[npcId])) {
			DemonicGorilla.switchPrayer(npcs[npcId]);
		}

		switch (npcs[npcId].npcType) {
		case 2054:
			int r2 = Misc.random(10);
			if (r2 <= 2) {
				npcs[npcId].attackType = 1;
				npcs[npcId].gfx100(550);
				npcs[npcId].projectileId = 551;
				npcs[npcId].endGfx = 552;
			} else if (r2 >= 3 && r2 <= 8) {
				npcs[npcId].attackType = 2;
				npcs[npcId].gfx100(553);
				npcs[npcId].projectileId = 554;
				npcs[npcId].endGfx = 555;
			} else if (r2 == 9) {
				npcs[npcId].attackType = 2;
				npcs[npcId].gfx100(553);
				npcs[npcId].projectileId = 554;
				npcs[npcId].endGfx = 555;
				int r = Misc.random(2);
				switch(r) {
				case 0:
					if(player.getItems().freeSlots() == 0 && player.playerEquipment[Player.playerWeapon] != -1) {
						Server.itemHandler.createGroundItem(player, player.playerEquipment[Player.playerWeapon], npcs[i].absX, npcs[i].absY, 1, 1, player.getId());
						player.sendMessage("The Chaos Elemental has un-equipped your weapon and placed it on the ground.");
					} else if(player.getItems().freeSlots() > 0 && player.playerEquipment[Player.playerWeapon] != -1) {
						player.getItems().addItem(player.playerEquipment[Player.playerWeapon], 1);
						player.sendMessage("The Chaos Elemental has un-equipped your weapon and placed it in your inventory.");
					}
					player.playerEquipment[Player.playerWeapon] = -1;
					player.getItems().updateSlot(Player.playerWeapon);
					player.getItems().sendWeapon(player.playerEquipment[Player.playerWeapon], player.getItems().getItemName(player.playerEquipment[Player.playerWeapon]));
					player.getCombat().getPlayerAnimIndex(player.getItems().getItemName(player.playerEquipment[Player.playerWeapon]).toLowerCase());
					player.getItems().resetBonus();
					player.getItems().getBonus();
					player.getItems().writeBonus();
					player.getPA().requestUpdates();
					break;
				case 1:
					player.getPA().movePlayer(player.absX - Misc.random(3), player.absY - Misc.random(3), player.heightLevel);
					player.sendMessage("The Chaos Elemental has teleported you away from it.");
					break;
				}
			} else {
				npcs[npcId].attackType = 0;
			}
			break;
		// vetion
		case 6611:
		case 6612:
			chance = Misc.random(100);
			if (chance < 25) {
				npcs[npcId].attackType = 2;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].hitDelayTimer = 4;
				Vetion.createVetionSpell(npcs[npcId], player);
			} else if (chance > 90 && System.currentTimeMillis() - npcs[npcId].lastSpecialAttack > 15_000) {
				npcs[npcId].attackType = 4;
				npcs[npcId].attackTimer = 5;
				npcs[npcId].hitDelayTimer = 2;
				npcs[npcId].lastSpecialAttack = System.currentTimeMillis();
			} else {
				npcs[npcId].attackType = 0;
				npcs[npcId].attackTimer = 5;
				npcs[npcId].hitDelayTimer = 2;
			}
			break;
		case 963:
			chance = Misc.random(100);
			if (chance <= 50) {
				player.RANGE_ABILITY = true;
				npcs[npcId].attackType = 2;
				npcs[npcId].attackTimer = 7;
				Queen.graphic1(npcs[npcId], player);
				npcs[npcId].hitDelayTimer = 4;
			} else if (chance >= 51 && chance <= 100 && !player.RANGE_ABILITY) {
				npcs[npcId].attackType = 2;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].projectileId = 280;
				Queen.graphic(npcs[npcId], player);
				npcs[npcId].hitDelayTimer = 4;
			}
			break;

		case 965:
			npcs[npcId].attackType = 2;
			npcs[npcId].attackTimer = 7;
			npcs[npcId].projectileId = 473;
			Queen.graphic(npcs[npcId], player);
			npcs[npcId].hitDelayTimer = 4;
			break;

		case 6767:
			chance = Misc.random(100);
			if (player.SPAWN_LIZARDS >= 3 && System.currentTimeMillis() - npcs[npcId].spawnLizards > 7_500) {
				npcs[npcId].attackType = 1;
				Lizardman.Minions(npcs[npcId], player);
				npcs[npcId].spawnLizards = System.currentTimeMillis();
				player.SPAWN_LIZARDS = 0;
			} else if (player.JUMP_ABILITY >= 4 && System.currentTimeMillis() - npcs[npcId].jumpAbility > 8_500) {
				Lizardman.jump(npcs[npcId], player);
				npcs[npcId].jumpAbility = System.currentTimeMillis();
				player.JUMP_ABILITY = 0;
			} else if (chance >= 0 || chance <= 100) {
				npcs[npcId].attackType = 0;
				npcs[npcId].attackTimer = 5;
				npcs[npcId].hitDelayTimer = 3;
				player.SPAWN_LIZARDS++;
				player.JUMP_ABILITY++;
			}
			break;

		case 499:
			npcs[npcId].attackType = 2;
			npcs[npcId].projectileId = 280;
			npcs[npcId].attackTimer = 3;
			break;

		/**
		 * Cerberus
		 */
		case 5867: // range
			if (player.MAGIC_ATTACK >= 2 && player.MAGIC_ATTACK <= 10
					|| player.MELEE_ATTACK >= 2 && player.MELEE_ATTACK <= 10) {
				return;
			}
			if (!player.prayerActive[17]) {
				npcs[npcId].projectileId = 1248;
				npcs[npcId].hitDelayTimer = 4;
				npcs[npcId].attackTimer = 7;
				player.MELEE_ATTACK += 4;
			} else if (player.prayerActive[17]) {
				npcs[npcId].attackType = 1;
				npcs[npcId].projectileId = 1248;
				npcs[npcId].hitDelayTimer = 4;
				npcs[npcId].attackTimer = 7;
				player.MELEE_ATTACK += 4;
			}
			break;
		case 5868: // mage
			if (!player.prayerActive[16] && player.MAGIC_ATTACK == 10 && player.RANDOM == 3) {
				npcs[npcId].projectileId = 100;
				npcs[npcId].hitDelayTimer = 4;
				npcs[npcId].endGfx = 101;
				npcs[npcId].attackTimer = 7;
				player.RANGE_ATTACK++;
			} else if (player.prayerActive[16] && player.MAGIC_ATTACK == 10 && player.RANDOM == 3) {
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 100;
				npcs[npcId].hitDelayTimer = 4;
				npcs[npcId].endGfx = 101;
				npcs[npcId].attackTimer = 7;
				player.RANGE_ATTACK++;
			}
			break;
		case 5869: // melee
			if (!player.prayerActive[18] && player.MELEE_ATTACK == 10 && player.RANDOM_MELEE == 3) {
				npcs[npcId].projectileId = 1248;
				npcs[npcId].hitDelayTimer = 4;
				npcs[npcId].attackTimer = 7;
				player.MAGIC_ATTACK += 4;
				player.MELEE_ATTACK += 2;
			} else if (player.prayerActive[18] && player.MELEE_ATTACK == 10 && player.RANDOM_MELEE == 3) {
				npcs[npcId].attackType = 0;
				npcs[npcId].projectileId = 1248;
				npcs[npcId].hitDelayTimer = 4;
				npcs[npcId].attackTimer = 7;
				player.MAGIC_ATTACK += 4;
				player.MELEE_ATTACK += 2;
			}
			break;

		case 5862:
			chance = Misc.random(100);
			if (npcs[npcId].HP <= 400 && player.CAST_GHOSTS >= 5
					&& System.currentTimeMillis() - npcs[npcId].spawnGhosts > 35_000) {
				npcs[npcId].forceChat("Aaarrrooooooo");
				Cerberus.ghostData(player);
				npcs[npcId].spawnGhosts = System.currentTimeMillis();
				player.CAST_GHOSTS = 0;
			} else if (npcs[npcId].HP <= 200 && player.CAST_ROCKS >= 3
					&& System.currentTimeMillis() - npcs[npcId].explodingRocks > 3_000) {
				npcs[npcId].animation(4890);
				npcs[npcId].forceChat("Grrrrrrrrrrrrrr");
				Cerberus.EXPLODING_ROCKS(npcs[npcId], player);
				npcs[npcId].explodingRocks = System.currentTimeMillis();
				player.CAST_ROCKS = 0;
			}
			if (chance < 55) {
				npcs[npcId].attackType = 2;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].projectileId = 1244;
				npcs[npcId].hitDelayTimer = 4;
				npcs[npcId].endGfx = 1245;
				player.CAST_ROCKS++;
				player.CAST_GHOSTS++;
			} else if (chance > 56) {
				npcs[npcId].attackType = 1;
				npcs[npcId].projectileId = 1242;
				npcs[npcId].hitDelayTimer = 4;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].endGfx = 1243;
				player.CAST_ROCKS++;
				player.CAST_GHOSTS++;
			}
			break;

		case 6615:
			ScorpX = npcs[npcId].absX;
			ScorpY = npcs[npcId].absY;
			break;

		case 2218:
			npcs[npcId].projectileId = 48;
			npcs[npcId].endGfx = 1206;
			npcs[npcId].attackType = 1;
			break;
		case 6345:
			Misc.random(100);
			npcs[npcId].forceChat("Die now, in a prison of ice!");
			npcs[npcId].animation(1979);// barrage?
			npcs[npcId].attackType = 2;
			if (player.freezeTimer <= 0) {
				player.freezeTimer = 15;
			}
			player.gfx0(369);
			player.sendMessage("You have been frozen.");
			break;
		case 2217:
			npcs[npcId].projectileId = 1203;
			npcs[npcId].endGfx = 1204;
			npcs[npcId].attackType = 2;
			break;
		case 494:
		case 5535:
			npcs[npcId].attackType = 2;
			if (Misc.random(5) > 0 && npcs[npcId].npcType == 494 || npcs[npcId].npcType == 5535) {
				npcs[npcId].gfx0(161);
				npcs[npcId].projectileId = 162;
				npcs[npcId].endGfx = 163;
			} else {
				npcs[npcId].gfx0(161);
				npcs[npcId].projectileId = 162;
				npcs[npcId].endGfx = 163;
			}
			break;

		case 2042:
			chance = 1;
			if (player != null) {
				if (player.getZulrahEvent().getStage() == 9) {
					chance = 2;
				}
			}
			chance = Misc.random(chance);
			npcs[npcId].setFacePlayer(true);
			if (chance < 2) {
				npcs[npcId].attackType = 1;
				npcs[npcId].projectileId = 97;
				npcs[npcId].endGfx = -1;
				npcs[npcId].hitDelayTimer = 3;
				npcs[npcId].attackTimer = 4;
			} else {
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 156;
				npcs[npcId].endGfx = -1;
				npcs[npcId].hitDelayTimer = 3;
				npcs[npcId].attackTimer = 4;
			}
			break;

		case 2044:
			npcs[npcId].setFacePlayer(true);
			if (Misc.random(3) > 0) {
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 1046;
				npcs[npcId].endGfx = -1;
				npcs[npcId].hitDelayTimer = 3;
				npcs[npcId].attackTimer = 4;
			} else {
				npcs[npcId].attackType = 1;
				npcs[npcId].projectileId = 1044;
				npcs[npcId].endGfx = -1;
				npcs[npcId].hitDelayTimer = 3;
				npcs[npcId].attackTimer = 4;
			}
			break;

		case 2043:
			npcs[npcId].setFacePlayer(false);
			npcs[npcId].face(player.getX(), player.getY());
			npcs[npcId].targetedLocation = new Location3D(player.getX(), player.getY(), player.heightLevel);
			npcs[npcId].attackType = 0;
			npcs[npcId].attackTimer = 9;
			npcs[npcId].hitDelayTimer = 6;
			npcs[npcId].projectileId = -1;
			npcs[npcId].endGfx = -1;
			break;

		case 6342:
			npcs[npcId].attackType = 0;
			npcs[npcId].projectileId = -1;
			npcs[npcId].endGfx = -1;
			break;
		case 6609:
			int attackStyles1 = Misc.random(100);
			final Random random5551 = new Random();
			if (attackStyles1 >= 0 && attackStyles1 <= 40 && player.CAST_KNOCK <= 4) {
				npcs[npcId].attackType = 0;
				npcs[npcId].projectileId = -1;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].endGfx = -1;
				player.CAST_KNOCK++;
			} else if (attackStyles1 >= 41 && attackStyles1 <= 70 && player.CAST_KNOCK <= 4) {
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 395;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].endGfx = 431;
				player.CAST_KNOCK++;
			} else if (attackStyles1 >= 71 && attackStyles1 <= 100 && player.CAST_KNOCK <= 4) {
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 395;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].endGfx = 431;
				player.CAST_KNOCK++;
			} else if (player.CAST_KNOCK >= 5) {
				Callisto.KnockBack(player, npcs[npcId].absX - random5551.nextInt(5), npcs[npcId].absY);
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 395;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].endGfx = 431;
				player.CAST_KNOCK = 0;
			}
			break;
		case 319:
			int ATTACK_STYLE;
			ATTACK_STYLE = Misc.random(2);
			if (npcs[npcId].HP < 1000 && hasMinions == false && newMinion == 0) {
				npcs[npcId].attackType = 2;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].hitDelayTimer = 4;
				handleDarkCores(npcs[npcId], player);
				newMinion = 1;
				hasMinions = true;
			} else if (npcs[npcId].HP < 650 && hasMinions == false && newMinion == 1) {
				npcs[npcId].attackType = 2;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].hitDelayTimer = 4;
				handleDarkCores(npcs[npcId], player);
				newMinion = 2;
				hasMinions = true;
			} else if (npcs[npcId].HP < 250 && hasMinions == false && newMinion == 2) {
				npcs[npcId].attackType = 2;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].hitDelayTimer = 4;
				handleDarkCores(npcs[npcId], player);
				newMinion = 3;
				hasMinions = true;
			} else if (ATTACK_STYLE == 1) {
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 316;
				npcs[npcId].endGfx = -1;
				npcs[npcId].hitDelayTimer = 3;
				npcs[npcId].attackTimer = 4;
				hasMinions = false;
			} else if (ATTACK_STYLE == 2) {
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 314;
				npcs[npcId].endGfx = -1;
				npcs[npcId].hitDelayTimer = 3;
				npcs[npcId].attackTimer = 4;
				hasMinions = false;
				doProjectiles(npcs[npcId], player);
			}
			break;

		case 6618:
			chance = Misc.random(100);
			npcs[npcId].forceChat(Archaeologist.Archaeologist_Quotes());
			npcs[npcId].face(player.getX(), player.getY());
			if (chance >= 66 && Archaeologist.FINISHED_ABILITY == false) {
				npcs[npcId].attackType = 2;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].hitDelayTimer = 4;
				npcs[npcId].forceChat("Rain of Knowledge!");
				Archaeologist.Rain_of_Knowledge(npcs[npcId], player);
			} else if (Archaeologist.FINISHED_ABILITY == true) {
				npcs[npcId].forceChat("You belong in a museum!");
				npcs[npcId].attackType = 1;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].projectileId = 1259;
				npcs[npcId].hitDelayTimer = 4;
				Archaeologist.MUSEUM_ABILITY(player);
			} else if (chance >= 0 && chance <= 65 && Fanatic.FINISHED_ABILITY == false) {
				npcs[npcId].face(player.getX(), player.getY());
				npcs[npcId].attackType = 1;
				npcs[npcId].attackTimer = 5;
				npcs[npcId].hitDelayTimer = 4;
			}
			break;

		case 6619:
			npcs[npcId].forceChat(Fanatic.Fanatic_Quotes());
			chance = Misc.random(100);
			if (chance >= 54 && Fanatic.FINISHED_ABILITY == false) {
				npcs[npcId].attackType = 2;
				npcs[npcId].attackTimer = 7;
				npcs[npcId].hitDelayTimer = 4;
				Fanatic.Weapon_Removal++;
				Fanatic.EARTH_ABILITY(npcs[npcId], player);
			} else if (Fanatic.FINISHED_ABILITY == true && Fanatic.DAMAGE_RECIEVED == true) {
				npcs[npcId].projectileId = 195;
				npcs[npcId].attackTimer = 7;
				Fanatic.attackingData(npcId, 1, 4);
				Fanatic.Weapon_Removal++;
				Fanatic.Black_Ability(player);
			} else if (Fanatic.Weapon_Removal > 2 && Fanatic.FINISHED_ABILITY == false) {
				npcs[npcId].projectileId = 195;
				npcs[npcId].attackTimer = 5;
				Fanatic.attackingData(npcId, 2, 4);
				Fanatic.removeWeapon(player);
				player.sendMessage("You have been disarmed.");
				Fanatic.Weapon_Removal = 0;
			} else if (chance >= 0 && chance <= 53 && Fanatic.FINISHED_ABILITY == false) {
				npcs[npcId].projectileId = 195;
				npcs[npcId].attackTimer = 5;
				Fanatic.attackingData(npcId, 1, 2);
			}
			break;

		case 6528:
			if (Misc.random(10) > 0) {
				npcs[npcId].attackType = 2;
				npcs[npcId].gfx100(194);
				npcs[npcId].projectileId = 195;
				npcs[npcId].endGfx = 196;
			} else {
				npcs[npcId].attackType = 4;
				npcs[npcId].gfx100(194);
				npcs[npcId].projectileId = 195;
				npcs[npcId].endGfx = 576;

			}
			break;
		case 6610:
			if (Misc.random(15) > 0) {
				npcs[npcId].attackType = 2;
				npcs[npcId].gfx100(164);
				npcs[npcId].projectileId = 165;
				npcs[npcId].endGfx = 166;
			} else {
				npcs[npcId].attackType = 4;
				npcs[npcId].gfx0(164);
				npcs[npcId].projectileId = 165;
				npcs[npcId].endGfx = 166;
			}
			break;

		case 497:
		case 3998:
			npcs[npcId].attackType = 2;
			if (Misc.random(5) > 0 && npcs[npcId].npcType == 3998 || npcs[npcId].npcType == 497) {
				npcs[npcId].gfx0(161);
				npcs[npcId].projectileId = 162;
				npcs[npcId].endGfx = 163;
			} else {
				npcs[npcId].gfx0(155);
				npcs[npcId].projectileId = 156;
				npcs[npcId].endGfx = 157;
			}
			break;
		case 2892:
			npcs[npcId].projectileId = 94;
			npcs[npcId].attackType = 2;
			npcs[npcId].endGfx = 95;
			break;
		case 2894:
			npcs[npcId].projectileId = 298;
			npcs[npcId].attackType = 1;
			break;
		case 264:
		case 259:
		case 247:
		case 268:
		case 270:
		case 274:
		case 272:
		case 273:
		case 2919:
		case 6593:
			int random2 = Misc.random(2);
			if (random2 == 0) {
				npcs[npcId].projectileId = 393;
				npcs[npcId].endGfx = 430;
				npcs[npcId].attackType = 3;
			} else {
				npcs[npcId].attackType = 0;
				npcs[npcId].projectileId = -1;
				npcs[npcId].endGfx = -1;
			}
			break;
		case 239:
			int random = Misc.random(100);
			int distance1 = player.distanceToPoint(npcs[npcId].absX, npcs[npcId].absY);
			if (random >= 60 && random < 65) {
				npcs[npcId].projectileId = 394; // green
				npcs[npcId].endGfx = 429;
				npcs[npcId].attackType = 3;
			} else if (random >= 65 && random < 75) {
				npcs[npcId].projectileId = 395; // white
				npcs[npcId].endGfx = 431;
				npcs[npcId].attackType = 3;
			} else if (random >= 75 && random < 80) {
				npcs[npcId].projectileId = 396; // blue
				npcs[npcId].endGfx = 428;
				npcs[npcId].attackType = 3;
			} else if (random >= 80 && distance1 <= 4) {
				npcs[npcId].projectileId = -1; // melee
				npcs[npcId].endGfx = -1;
				npcs[npcId].attackType = 1;
			} else {
				npcs[npcId].projectileId = 393; // red
				npcs[npcId].endGfx = 430;
				npcs[npcId].attackType = 3;
			}
			break;
		// arma npcs
		case 2561:
			npcs[npcId].attackType = 0;
			break;
		case 2560:
			npcs[npcId].attackType = 1;
			npcs[npcId].projectileId = 1190;
			break;
		case 2559:
			npcs[npcId].attackType = 2;
			npcs[npcId].projectileId = 1203;
			break;
		case 2558:
			random = Misc.random(1);
			npcs[npcId].attackType = 1 + random;
			if (npcs[npcId].attackType == 1) {
				npcs[npcId].projectileId = 1197;
			} else {
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 1198;
			}
			break;
		// sara npcs
		case 2562: // sara
			random = Misc.random(1);
			if (random == 0) {
				npcs[npcId].attackType = 2;
				npcs[npcId].endGfx = 1224;
				npcs[npcId].projectileId = -1;
			} else if (random == 1)
				npcs[npcId].attackType = 0;
			break;
		case 2563: // star
			npcs[npcId].attackType = 0;
			break;
		case 2564: // growler
			npcs[npcId].attackType = 2;
			npcs[npcId].projectileId = 1203;
			break;
		case 2565: // bree
			npcs[npcId].attackType = 1;
			npcs[npcId].projectileId = 9;
			break;
		case 2551:
			npcs[npcId].attackType = 0;
			break;
		case 2552:
			npcs[npcId].attackType = 2;
			npcs[npcId].projectileId = 1203;
			break;
		case 2553:
			npcs[npcId].attackType = 1;
			npcs[npcId].projectileId = 1206;
			break;
		case 2025:
			npcs[npcId].attackType = 2;
			int r = Misc.random(3);
			if (r == 0) {
				npcs[npcId].gfx100(158);
				npcs[npcId].projectileId = 159;
				npcs[npcId].endGfx = 160;
			}
			if (r == 1) {
				npcs[npcId].gfx100(161);
				npcs[npcId].projectileId = 162;
				npcs[npcId].endGfx = 163;
			}
			if (r == 2) {
				npcs[npcId].gfx100(164);
				npcs[npcId].projectileId = 165;
				npcs[npcId].endGfx = 166;
			}
			if (r == 3) {
				npcs[npcId].gfx100(155);
				npcs[npcId].projectileId = 156;
			}
			break;
		case 2265:// supreme
			npcs[npcId].attackType = 1;
			npcs[npcId].projectileId = 298;
			break;

		case 2266:// prime
			npcs[npcId].attackType = 2;
			npcs[npcId].projectileId = 162;
			npcs[npcId].endGfx = 477;
			break;

		case 2028:
			npcs[npcId].attackType = 1;
			npcs[npcId].projectileId = 27;
			break;

		case 3163:
			npcs[npcId].attackType = 2;
			npcs[npcId].projectileId = 1190;
			npcs[npcId].endGfx = -1;
			break;
		case 3164:
			npcs[npcId].attackType = 1;
			npcs[npcId].projectileId = 1190;
			npcs[npcId].endGfx = -1;
			break;
		case 3165:
			npcs[npcId].attackType = 0;
			npcs[npcId].projectileId = 1190;
			npcs[npcId].endGfx = -1;
			break;
		case 6257:// saradomin strike
			npcs[npcId].attackType = 2;
			npcs[npcId].endGfx = 76;
			break;
		case 6221:// zamorak strike
			npcs[npcId].attackType = 2;
			npcs[npcId].endGfx = 78;
			break;
		case 6231:// arma
			npcs[npcId].attackType = 2;
			npcs[npcId].projectileId = 1199;
			break;
		/*
		 * case 3162:// kree random = Misc.random(10); if (random < 2) {
		 * npcs[i].attackType = 2; npcs[i].projectileId = -1; npcs[i].endGfx =
		 * -1; } else if (random > 1 && random < 10){ npcs[i].attackType = 1;
		 * npcs[i].projectileId = -1; npcs[i].endGfx = -1; } else {
		 * npcs[i].attackType = 0; npcs[i].projectileId = -1; npcs[i].endGfx =
		 * -1; } break;
		 */
		// sara npcs
		/*
		 * case 3129: random = Misc.random(15); if (random > 0 && random < 7) {
		 * npcs[i].attackType = 0; npcs[i].projectileId = -1; } else if (random
		 * >= 7){ npcs[i].attackType = 2; npcs[i].projectileId = 1211; } else if
		 * (random == 0) { npcs[i].attackType = 4; npcs[i].projectileId = -1; }
		 * break;
		 */
		/*
		 * case 2205: // sara random = Misc.random(3); if (random > 0) {
		 * npcs[i].attackType = 2; npcs[i].endGfx = -1; npcs[i].projectileId =
		 * -1; } else if (random == 0) { npcs[i].attackType = 0; npcs[i].endGfx
		 * = -1; npcs[i].projectileId = -1; } break;
		 */
		case 2205:
			switch (npcs[npcId].zilyana) {
			case 0:
			case 1:
			case 2:
			case 3:
				npcs[npcId].attackType = 0;
				break;
			case 4:
			case 5:
			case 6:
				npcs[npcId].attackType = 2;
				break;
			}
			break;
		case 3162:
			npcs[npcId].kree = Misc.random(2);
			switch (npcs[npcId].kree) {
			case 0:
				npcs[npcId].attackType = 2;
				npcs[npcId].projectileId = 1199;
				break;
			case 1:
			case 2:
				npcs[npcId].attackType = 1;
				npcs[npcId].projectileId = 1198;
				break;
			}
			if (Misc.random(5) >= 3) {
				npcs[npcId].forceChat(npcs[npcId].Kree());
			}
			break;
		case 3129:
			if (Misc.random(5) >= 3) {
				npcs[npcId].forceChat(npcs[npcId].Tsutsaroth());
			}
			switch (npcs[npcId].tsutsaroth) {
			case 0:
			case 1:
			case 2:
			case 3:
				npcs[npcId].attackType = 0;
				npcs[npcId].projectileId = -1;
				break;
			case 4:
				npcs[npcId].attackType = 2;
				// npcs[i].multiAttack = true;
				npcs[npcId].gfx0(1165);
				npcs[npcId].projectileId = 1166;
				break;
			}
			break;
		case 2206: // star
			npcs[npcId].setFacePlayer(true);
			npcs[npcId].attackType = 0;
			npcs[npcId].projectileId = -1;
			break;
		case 2207: // growler
			npcs[npcId].setFacePlayer(true);
			npcs[npcId].attackType = 2;
			npcs[npcId].projectileId = 1203;
			break;
		case 2208: // bree
			npcs[npcId].setFacePlayer(true);
			npcs[npcId].attackType = 1;
			npcs[npcId].projectileId = 9;
			break;
		// bandos npcs
		/*
		 * case 2215:// bandos random = Misc.random(3); if (random == 0 ||
		 * random == 1) { npcs[i].attackType = 0; npcs[i].projectileId = -1;
		 * npcs[i].endGfx = -1; } else { npcs[i].attackType = 1;
		 * npcs[i].projectileId = 1200; npcs[i].endGfx = -1; } break;
		 */
		case 2215:
			switch (npcs[npcId].graardor) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
				if (Misc.random(5) >= 4) {
					npcs[npcId].forceChat(npcs[npcId].Graardor());
				}
				npcs[npcId].attackType = 0;
				npcs[npcId].projectileId = -1;
				break;
			case 5:
				if (Misc.random(5) >= 3) {
					npcs[npcId].forceChat(npcs[npcId].Graardor());
				}
				npcs[npcId].attackType = 1;
				// npcs[i].multiAttack = true;
				npcs[npcId].gfx0(1203);
				npcs[npcId].projectileId = 1202;
				break;
			}
			break;
		case 3209:// cave horror
			random = Misc.random(3);
			if (random == 0 || random == 1) {
				npcs[npcId].attackType = 0;
			} else {
				npcs[npcId].attackType = 2;
			}
			break;
		case 3127:
			int r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[npcId].attackType = 2;
				npcs[npcId].endGfx = 157;
				npcs[npcId].projectileId = 448;
			} else if (r3 == 1) {
				npcs[npcId].attackType = 1;
				npcs[npcId].endGfx = 451;
				npcs[npcId].projectileId = -1;
				npcs[npcId].hitDelayTimer = 6;
				npcs[npcId].attackTimer = 9;
			} else if (r3 == 2) {
				npcs[npcId].attackType = 0;
				npcs[npcId].projectileId = -1;
				npcs[npcId].endGfx = -1;
			}
			break;
		case 3125:
			npcs[npcId].attackType = 2;
			npcs[npcId].projectileId = 445;
			npcs[npcId].endGfx = 446;
			break;

		case 3121:
		case 2167:
			npcs[npcId].attackType = 1;
			npcs[npcId].projectileId = 443;
			break;
		}
	}

	public int npcSize(int i) {
		switch (npcs[i].npcType) {
		case 2883:
		case 2882:
		case 2881:
			return 3;
		case 493:
			return 10;
		}
		return 0;
	}

	/**
	 * Distanced required to attack
	 **/
	public int distanceRequired(int i) {
		switch (npcs[i].npcType) {
		case 7152:
		case 7151:
			return 5;
		/*
		 * case 6767: return 25;
		 */
		case 5869:
		case 5868:
		case 5867:
			return 50;
		case 5862:
			return 2;
		case 319:
			return 20;
		case 494:
		case 5535:
			return 25;
		case Zulrah.SNAKELING:
			return 1;
		case 6619:
		case 6618:
			return 4;

		case 2205:
			if (npcs[i].attackType == 0) {
				return 2;
			} else if (npcs[i].attackType == 2) {
				return 10;
			}
		case 6615:
			if (npcs[i].freezeTimer > 0) {
				return 10;
			} else if (npcs[i].freezeTimer < 0) {
				return 1;
			}
		case 3163:
		case 3162:
		case 3164:
		case 3165:
		case 2208:
			return 13;
		/*
		 * case 493: return 200;
		 */
		case 2215:
			return npcs[i].attackType == 0 ? npcs[i].getSize() : 6;
		/*
		 * case 493: return npcs[i].attackType == 0 ? npcs[i].getSize() : 6;
		 */
		case 2217:
		case 2218:
			return 6;
		case 2044:
		case 2043:
		case 2042:
			return 50;
		case 6611:
		case 6612:
			return npcs[i].attackType == 4 ? 8 : 3;
		case 6528:
		case 6610:
			return 8;
		case 6609:
			return 2;
		case 3998:
		case 497:
			return 10;
		case 2025:
		case 2028:
			return 6;
		case 2562:
		case 3131:
		case 3132:
		case 3130:
		case 2206:
			return 2;
		case 2207:
			return 13;
		case 2265:// dag kings
		case 2266:
		case 2054:// chaos ele
		case 3125:
		case 3121:
		case 2167:
		case 3127:
			return 8;
		case 2267:
			return 4;
		case 239:
			return npcs[i].attackType == 3 ? 18 : 4;
		case 2552:
		case 2553:
		case 2556:
		case 2557:
		case 2558:
		case 2559:
		case 2560:
		case 2564:
		case 2565:
			return 9;
		// things around dags
		case 2892:
		case 2894:
			return 10;
		default:
			return 1;
		}
	}

	public int followDistance(int i) {
		if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
			return 8;
		}
		switch (npcs[i].npcType) {
		case 319:
			return 20;
		case 2045:
		case 494:
			return 20;
		case 2206:
			return 100;
		case 2205:
			return 100;
		case 6618:
		case 6619:
			return 15;

		case 1739:
		case 1740:
		case 1741:
		case 1742:
		case 1747:
			return -1;
		case 239:
			return 40;
		case 6611:
		case 6612:
		case 2215:
		case 2551:
		case 2562:
		case 2563:
			return 8;
		case 2054:
			return 10;
		case 2265:
		case 2266:
			return 6;
		case 2267:
			return 6;

		}
		return 0;

	}

	public static int getProjectileSpeed(int i) {
		switch (npcs[i].npcType) {
		case 494:
			return 130;
		case 2265:
		case 2266:
		case 2054:
			return 85;

		case 3127:
			return 130;

		case 239:
			return 90;

		case 2025:
			return 85;

		case 2028:
			return 80;

		default:
			return 85;
		}
	}

	/**
	 * NPC Attacking Player
	 **/

	public void attackPlayer(final Player c, int i) {
		if (npcs[i].lastX != npcs[i].getX() || npcs[i].lastY != npcs[i].getY()) {
			return;
		}
		
		int[] resetCombat = { 239, 319, 600, 2054, 2642, 5779, 5862, 6342, 6609, 6611, 6618, 6619, 6767 };
		
		if (npcs[i].resetTimer > 60 && Arrays.binarySearch(resetCombat, npcs[i].npcType) >= 0) {
			npcs[i].resetCombat();
		}
		
		if (npcs[i].npcType == 6767 && c.Jumped) {
			return;
		}

		if (npcs[i].npcType == 965 && Player.transforming) {
			return;
		}

		if (npcs[i].npcType == 6720) {
			return;
		}

		if (!canAttack(i)) {
			return;
		}

		int[] ignoreClipping = { 494, 2042, 2043, 2044, 3162, 3163, 3164, 3165, 5535, 5862, 6618 };

		if (Arrays.binarySearch(ignoreClipping, npcs[i].npcType) < 0 && npcs[i].attackType == 0) {
			if (Region.isBlockedPath(npcs[i].absX, npcs[i].absY, c.absX, c.absY, c.heightLevel)
					|| Server.getGlobalObjects().anyExists(c.absX, c.absY, c.heightLevel)) {
				npcs[i].resetTimer++;
				return;
			}
		}

		if (npcs[i] != null) {
			if (npcs[i].isDead)
				return;
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != c.index) {
				npcs[i].killerId = 0;
				return;
			}
			if (!npcs[i].inMulti() && (c.underAttackBy > 0 || (c.underAttackBy2 > 0 && c.underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].heightLevel != c.heightLevel) {
				npcs[i].killerId = 0;
				return;
			}

			if (npcs[i].npcType == 5869) {
				if (c.MELEE_ATTACK == 4 || c.MELEE_ATTACK == 6 || c.MELEE_ATTACK == 8) {
					c.MELEE_ATTACK += 2;
					c.RANDOM_MELEE++;
					return;
				}
			}

			if (npcs[i].npcType == 5868) {
				if (c.MAGIC_ATTACK == 4 || c.MAGIC_ATTACK == 6 || c.MAGIC_ATTACK == 8) {
					c.MELEE_ATTACK = 0;
					c.MAGIC_ATTACK += 2;
					c.RANDOM++;
					return;
				}
			}

			if (npcs[i].npcType == 5868) {
				if (c.MAGIC_ATTACK == 10 && c.RANGE_ATTACK == 1) {
					return;
				}
			}

			if (npcs[i].npcType == 5868) {
				if (c.MAGIC_ATTACK <= 3) {
					return;
				}
			}

			if (npcs[i].npcType == 5869) {
				if (c.MELEE_ATTACK == 12) {
					return;
				}
			}

			if (npcs[i].npcType == 5869) {
				if (c.MELEE_ATTACK <= 3) {
					return;
				}
			}

			if (npcs[i].npcType == 5867) {
				if (c.MAGIC_ATTACK >= 3 || c.MELEE_ATTACK >= 2) {
					return;
				}
			}

			if (npcs[i].npcType >= 1739 && npcs[i].npcType <= 1742 || npcs[i].npcType > 6600 && npcs[i].npcType <= 6602
					|| npcs[i].npcType == 1747) {
				npcs[i].killerId = 0;
				return;
			}

			if (Boundary.isIn(npcs[i], Boundary.GODWARS_BOSSROOMS)) {
				if (!Boundary.isIn(c, Boundary.GODWARS_BOSSROOMS)) {
					npcs[i].killerId = 0;
					return;
				}
			}

			npcs[i].face(c);
			double distance = distanceRequired(i);
			if (npcs[i].getSize() > 1)
				distance += 0.5;
			if (npcs[i].getDistance(c.getX(), c.getY()) > distance) {
				npcs[i].resetTimer++;
				return;
			}
			if (c.respawnTimer <= 0) {
				npcs[i].attackTimer = getNpcDelay(i);
				npcs[i].hitDelayTimer = getHitDelay(i);
				npcs[i].attackType = 0;
				loadSpell(c, i);
				if (npcs[i].attackType == 3) {
					npcs[i].hitDelayTimer += 2;
					c.getCombat().absorbDragonfireDamage();
				}
				if (multiAttacks(i)) {
					multiAttackGfx(i, npcs[i].projectileId);
					startAnimation(getAttackEmote(i), i);
					npcs[i].oldIndex = c.index;
					return;
				}
				if (npcs[i].projectileId > 0) {
					int nX = NPCHandler.npcs[i].getX() + offset(i);
					int nY = NPCHandler.npcs[i].getY() + offset(i);
					int pX = c.getX();
					int pY = c.getY();
					int offX = (nX - pX) * -1;
					int offY = (nY - pY) * -1;
					c.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
							npcs[i].projectileId, getProjectileStartHeight(npcs[i].npcType, npcs[i].projectileId),
							getProjectileEndHeight(npcs[i].npcType, npcs[i].projectileId), -c.getId() - 1, 65);
				}
				c.underAttackBy2 = i;
				c.singleCombatDelay2.reset();
				npcs[i].oldIndex = c.index;
				startAnimation(getAttackEmote(i), i);
				c.getPA().removeAllWindows();
				/*
				 * if (c.teleporting) { c.animation(65535); c.teleporting =
				 * false; c.isRunning = false; c.gfx0(-1); c.animation(-1); }
				 */
			}
			// }
		}
	}

	public boolean canAttack(int i) {
		switch (npcs[i].npcType) {
		case AbyssalSireConstants.SLEEPING_NPC_ID:
			return false;
		}
		return true;
	}

	public int offset(int i) {
		switch (npcs[i].npcType) {
		case 2044:
			return 0;
		case 6611:
		case 6612:
		case 6618:
		case 6619:
			return 3;
		case 6610:
			return 2;
		case 239:
			return 2;
		case 2265:
		case 2266:
			return 1;
		case 3127:
		case 3125:
			return 1;
		}
		return 0;
	}

	public boolean retaliates(int npcType) {
            if (npcType == 3127)
                return true;
            
		return npcType < 3777 || npcType > 3780 && !(npcType >= 2440 && npcType <= 2446);
	}

	private boolean prayerProtectionIgnored(int npcId) {
		switch (npcs[npcId].npcType) {
		case 6610:
			return npcs[npcId].attackType == 2 || npcs[npcId].attackType == 4 ? true : false;
		case 6767:
			return true;
		case 5862:
			return true;
		case 6611:
		case 6612:
		case 6618:
		case 6619:
		case 963:
		case 965:
			return npcs[npcId].attackType == 2 ? true : false;
		case 6609:
			return npcs[npcId].attackType == 2 || npcs[npcId].attackType == 4 ? true : false;
		}
		return false;
	}

	public void handleSpecialEffects(Player c, int i, int damage) {
		if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
			if (damage > 0) {
				if (c != null) {
					if (c.playerLevel[5] > 0) {
						c.playerLevel[5]--;
						c.getPA().refreshSkill(5);
						// c.getPA().appendPoison(12);
					}
				}
			}
		}

	}

	public static void startAnimation(int animId, int i) {
		npcs[i].animId = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;
	}

	public NPC[] getNPCs() {
		return npcs;
	}

	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return Math.sqrt(Math.pow(objectX - playerX, 2) + Math.pow(objectY - playerY, 2)) <= distance;
	}

	public int getMaxHit(int i) {
		switch (npcs[i].npcType) {
                    case 3127:
                        return 79;
		case 499:
			if (npcs[i].attackType == 2) {
				return 10;
			}
		case 5779:
			return 32;
		case 963:
		case 965:
			return 37;
		case 5054:
			return 7;
		case 6767:
			return npcs[i].attackType == 0 ? 31 : npcs[i].attackType == 1 ? 10 : 15;
		case Zulrah.SNAKELING:
			return 15;
		case 5862:
			return npcs[i].attackType == 0 ? 28 : npcs[i].attackType == 1 ? 30 : 38;
		case 5868:
		case 5867:
		case 5869:
			return 18;
		case 7101:
			return npcs[i].attackType == 2 ? 30 : 20;
		case 6345:
			if (npcs[i].attackType == 2) {
				return 30;
			}

		case 6619:
		case 6618:
			if (npcs[i].attackType == 2) {
				return 58;
			} else if (npcs[i].attackType == 1) {
				return 38;
			}
		case 2042:
		case 2043:
		case 2044:
			return 41;
		case 2054:
			return 50;
		case 320:
			return 20;
		case 6609:
			if (npcs[i].attackType == 0 || npcs[i].attackType == 2) {
				return 32;
			}
		case 6342:
			if (npcs[i].attackType == 0) {
				return 40;
			}

		case 494:
			// if (npcs[i].attackType == 2)
			// return 37;
			// else
			// return 33;
			return 37;
		case 5535:
			return 8;
		case 239:
			return npcs[i].attackType == 3 ? 50 : 20;
		case 2208:
		case 2207:
		case 2206:
			return 16;
		case 3129:
			return npcs[i].attackType == 0 ? 60 : npcs[i].attackType == 4 ? 49 : 30;
		case 6611:
		case 6612:
			return 46;
		case 6613:
			return 7;
		case 6528:
			return npcs[i].attackType == 2 ? 40 : 50;
		case 6610:
			return 30;
		case 2558:
			return npcs[i].attackType == 2 ? 38 : 68;
		case 2562:
			return 31;
		case 6593:
			return 15;
		case 3162:
			if (npcs[i].attackType == 0) {
				return 26;
			} else if (npcs[i].attackType == 1) {
				return 71;
			} else if (npcs[i].attackType == 2)
				return 21;
		case 2215:
			if (npcs[i].attackType == 1)
				return 35;
			else
				return 60;
			/*
			 * case 3162: return npcs[i].attackType == 1 ? 71 :
			 * npcs[i].attackType == 2 ? 21 : 15;
			 */
		case 1158:
			if (npcs[i].attackType == 2)
				return 30;
			return 21;
		case 1160:
			if (npcs[i].attackType == 2 || npcs[i].attackType == 1)
				return 30;
			return 21;
		case 2205:
			if (npcs[i].attackType == 0) {
				return 31;
			} else if (npcs[i].attackType == 1) {
				return 31;
			}
		}
		return npcs[i].maxHit == 0 ? 1 : npcs[i].maxHit;
	}

	@SuppressWarnings("resource")
	public static boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]), Integer.parseInt(token3[4]),
							getNpcListHP(Integer.parseInt(token3[0])), getNpcMaxHit(Integer.parseInt(token3[0])),
							getNpcAttack(Integer.parseInt(token3[0])), getNpcCombatDefence(Integer.parseInt(token3[0])));
				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public static int getNpcMaxHit(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return 0;
		}
		return NpcDefinition.DEFINITIONS[npcId].getMaxHit();

	}

	public static int getNpcAttack(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return 0;
		}
		return NpcDefinition.DEFINITIONS[npcId].getAttackBonus();

	}

	public static int getNpcCombatDefence(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return 0;
		}
		return NpcDefinition.DEFINITIONS[npcId].getMeleeDefence();

	}

	public static int getNpcRangeDefence(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return 0;
		}
		return NpcDefinition.DEFINITIONS[npcId].getRangedDefence();

	}

	public static int getNpcMagicDefence(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return 0;
		}
		return NpcDefinition.DEFINITIONS[npcId].getMagicDefence();

	}
	
	public static int getNpcListHP(int npcId) {
		if (npcId <= -1) {
			return 0;
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return 0;
		}
		return NpcDefinition.DEFINITIONS[npcId].getHitpoints();

	}

	public static String getNpcListName(int npcId) {
		if (npcId <= -1) {
			return "None";
		}
		if (NpcDefinition.DEFINITIONS[npcId] == null) {
			return "None";
		}
		return NpcDefinition.DEFINITIONS[npcId].getName();
	}

	public static void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		npcs[slot] = newNPC;
	}

	public static NpcDefinition[] getNpcDef() {
		return NpcDefinition.DEFINITIONS;
	}

	public static void kill(int npcType, int height) {
		Arrays.asList(npcs).stream().filter(Objects::nonNull)
				.filter(n -> n.npcType == npcType && n.heightLevel == height).forEach(npc -> npc.isDead = true);
	}

	public static void kill(Player player, int npcType, int absX, int absY, int height) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Arrays.asList(npcs).stream().filter(Objects::nonNull).filter(
						n -> n.npcType == npcType && n.absX == absX && n.absY == absY && n.heightLevel == height)
						.forEach(npc -> npc.isDead = true);
				container.stop();
			}
		}, 1);
	}

	public static void KILL_POOLS(Player player, int npcType, int absX, int absY, int height) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Arrays.asList(npcs).stream().filter(Objects::nonNull).filter(
						n -> n.npcType == npcType && n.absX == absX && n.absY == absY && n.heightLevel == height)
						.forEach(npc -> npc.isDead = true);
				container.stop();
			}
		}, 1);
	}

	public static void KILL_TENT(Player player, int npcType, int absX, int absY, int height) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Arrays.asList(npcs).stream().filter(Objects::nonNull).filter(
						n -> n.npcType == npcType && n.absX == absX && n.absY == absY && n.heightLevel == height)
						.forEach(npc -> npc.isDead = true);
				container.stop();
			}
		}, 1);
	}
}
