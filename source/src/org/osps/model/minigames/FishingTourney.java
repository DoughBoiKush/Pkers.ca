package org.osps.model.minigames;

import java.util.ArrayList;
import java.util.List;

import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;
import org.osps.model.players.combat.Hitmark;
import org.osps.util.Misc;

/***
 * 
 * Made by Lester Knome A.k.a raw dog king
 * 
 **/

public class FishingTourney {

	public static FishingTourney tourney;

	public static FishingTourney getSingleton() {
		if (tourney == null)
			tourney = new FishingTourney();
		return tourney;
	}

	// 4070 -- sinister stranger
	private static List<FishingSession> sessions;
	private static List<NPC> spots;
	private static final Boundary BOUNDS = new Boundary(2623, 3411, 2643, 3447);
	private static final int[][] FISHING_LOCS = { { 2637, 3444 }, { 2637, 3444 }, { 2637, 3444 }, { 2630, 3435 },
			{ 2632, 3430 }, { 2632, 3428 }, { 2632, 3427 }, { 2632, 3425 }, { 2632, 3428 }, { 2632, 3427 },
			{ 2632, 3425 }, { 2632, 3428 }, { 2632, 3427 }, { 2632, 3425 }, { 2632, 3428 }, { 2632, 3427 },
			{ 2632, 3425 }, { 2632, 3430 }, { 2629, 3420 }, { 2627, 3415 }, { 2629, 3420 }, { 2629, 3420 },
			{ 2628, 3417 }, { 2628, 3417 } };
	private static final int[] FISHING_SPOTS = { 3913, 3913, 3657, 3657, 1520, 1520 };

	public FishingTourney() {
		for (int spot : FISHING_SPOTS)
			spots.add(NPCHandler.newNPC(spot, 2637, 3444, 0, 0, 0, 0, 0, 0));
		shuffleSpots();
	}

	public void shuffleSpots() {
		for(int i = 0; i < spots.size(); i++) {
			if(Misc.random(2) == 0)
				continue;
			NPC spot = spots.get(i);
					
			int index = (int)(FISHING_LOCS.length * Math.random());
			int x = FISHING_LOCS[index][0];
			int y = FISHING_LOCS[index][1];
			spots.remove(spot);
			spots.add(i, NPCHandler.newNPC(spot.npcType, x, y, 0, 0, 0, 0, 0, 0));
			
			spot.absX = 0;
			spot.absY = 0;
			NPCHandler.npcs[spot.index] = null;
			spot = null;
		}
	}

	public boolean inFishingArea(Player p) {
		return Boundary.isIn(p, BOUNDS);
	}

	public synchronized void endSession(FishingSession session) {
		
		// purge other sessions too
		for (FishingSession session2 : sessions) {
			if (session2 != null && !session2.running && !session2.validSession()) {
				sessions.remove(session2);
				session2 = null;
				continue;
			} else if(session2 == null)
				sessions.remove(session2);
		}

		sessions.remove(session);
		session = null;
	}

	public synchronized void addPlayerToSession(Player p) {
		for (FishingSession session : sessions) {
			if (session.running)
				continue;
			int openSpot = session.openSpot();
			if (openSpot != -1) {
				p.fishTourneySession = session;
				session.players[openSpot] = p;
				if (session.sessionReady()) {
					session.start();
				} else {
					p.sendMessage("You have been added to an existing session. Waiting for "
							+ (4 - session.getPlayerCount()) + " more players.");
					for (int i = 0; i < 4; i++) {
						if (session.players[i] == null || session.players[i] == p)
							continue;
						session.players[i].sendMessage("Another player has joined your session. Waiting for "
								+ (4 - session.getPlayerCount()) + " more players.");
					}
				}
				return;
			}
		}

		FishingSession session = new FishingSession();
		p.fishTourneySession = session;
		session.players[0] = p;
		sessions.add(session);
		p.sendMessage("A new session has been created, waiting for 3 more players.");
	}

	public class FishingSession {

		Player[] players;
		byte[] task;
		public boolean running;
		public int rounds;

		FishingSession() {
			players = new Player[4];
			task = new byte[4];
			running = false;
			rounds = 0;
		}

		public  void turnInTask(Player p) {
			for(int i = 0; i < 4; i++) {
				if(players[i] == p) {
					int count = p.getItems().getItemCount(currentTask.itemId);
					if(count >= task[i]) {
						p.getItems().deleteItem2(currentTask.itemId, task[i]);
						p.sendMessage("Congratulations! You are the first to complete the task!");
						p.sendMessage("You receive <col=255>1</col> fishing tourney point!");
						p.fishingTourneyPoints++;
						p.getPA().addSkillXP(((count * currentTask.minLevel) * 500) + 1000, p.playerFishing);
						for(int k = 0; k < 4; k++) {
							if(k == i || players[k] == null)
								continue;
							players[k].sendMessage(players[i].playerName + " has completed the task first! You receive 4 potions!");
							if(players[k].getItems().freeSlots() < 4) {
								int bombs = 4 - players[k].getItems().freeSlots();
								players[k].getItems().addItem(4045, players[k].getItems().freeSlots());
								int explosiveHit = players[k].getPA().getLevelForXP(players[k].playerXP[players[k].playerHitpoints]) / 5;
								players[k].forceChat("Ouch!");
								players[k].appendDamage(explosiveHit * bombs, Hitmark.HIT);
								players[k].getPA().refreshSkill(3);
								players[k].sendMessage("You have no room to hold the potion, so it falls from your hands!");
							} else {
								players[k].getItems().addItem(4045, 4);
							}
						}
						newTask(false);
					} else {
						task[i] -= count;
						p.getItems().deleteItem2(currentTask.itemId, count);
						p.sendMessage("You still need " + task[i] + " " + currentTask.name + "(s).");
						p.getPA().addSkillXP(((count * currentTask.minLevel) * 500), p.playerFishing);
					}
				}
			}
		}

		public void newTask(boolean first) {
			int amount = ((int) (5 * Math.random())) + 2;
			if (Misc.random(3) == 2) {
				amount += ((int) (5 * Math.random()));
			}
			currentTask = FishingTask.getTaskForLevel(getMinLevel());
			for (int i = 0; i < 4; i++) {
				task[i] = (byte) amount;
				if (players[i] != null) {
					if (players[i].fishTourneySession != this)
						removePlayer(players[i]);
					if (first)
						players[i].getDH().sendNpcChat2("The tourney begins..... now!",
								"Bring me " + amount + " " + currentTask.name + "s!", 4070, "Sinister Stranger");
					else
						players[i].getDH().sendNpcChat2("Alright you fools! Here's another task!",
								"Bring me " + amount + " " + currentTask.name + "s!", 4070, "Sinister Stranger");
					players[i].nextChat = -1;
					players[i].sendMessage("Your next task is to catch " + amount + " " + currentTask.name + "(s).");
				}
			}
			rounds++;
		}

		public synchronized void start() {
			running = true;
			for(int i = 0; i < players.length; i++) {
				if(players[i] == null || players[i].fishTourneySession != this) {
					endGame(true);
					return;
				}
				players[i].getPA().setSkillLevel(players[i].playerHitpoints, 
						players[i].getPA().getLevelForXP(players[i].playerXP[players[i].playerHitpoints]),
						players[i].playerXP[players[i].playerHitpoints]);
				players[i].getPA().refreshSkill(players[i].playerHitpoints);
				players[i].getItems().addItem(303, 1);
				players[i].getItems().addItem(301, 1);
				players[i].getItems().addItem(311, 1);
				players[i].getItems().addItem(1917, 1);
				players[i].sendMessage("The fishing tournament has started!");
				
			}
			
			newTask(true);
		}

		public synchronized void endGame(boolean arupt) {
			for (int i = 0; i < players.length; i++) {
				if (players[i] == null)
					continue;
				players[i].fishTourneySession = null;
				players[i].getItems().removeAllItems();
				if (arupt) {
					players[i].sendMessage("The game has aruptly ended.");
				} else if(running) {
					players[i].getDH().sendNpcChat2("Well done " + players[i].playerName + "!",
							"It seems like you have won the tourney!", 4070, "Sinister Stranger");
					players[i].nextChat = -1;
					players[i].sendMessage("Congratulations! You have won the tournament!");
					if(rounds > 5) {
						players[i].sendMessage("You survived " + rounds + " round(s) so you earn <col=255>" + ((rounds / 2) + 5) + " fishing tourney points!" );
						players[i].fishingTourneyPoints += ((rounds / 2) + 5);
					} else { 
						players[i].sendMessage("You survived " + rounds + " round(s) so you earn <col=255>" + (rounds / 2) + " fishing tourney points!" );
						players[i].fishingTourneyPoints += (rounds / 2);
					}
					rounds = 0;
				}
				players[i] = null;
			}
			running = false;
			endSession(this);
		}

		public synchronized boolean validSession() {
			for (int i = 0; i < players.length; i++) {
				if (players[i] != null) {
					if (players[i].fishTourneySession != this
							|| FishingTourney.getSingleton().inFishingArea(players[i])) {
						players[i].fishTourneySession = null;
						players[i] = null;
						continue;
					}
				} else {
					return true;
				}
			}
			return false;
		}

		public synchronized boolean sessionReady() {
			for (int i = 0; i < players.length; i++) {
				if (players[i] == null)
					return false;
				if (!FishingTourney.getSingleton().inFishingArea(players[i]) || players[i].fishTourneySession != this) {
					players[i].fishTourneySession = null;
					players[i] = null;
					return false;
				}
			}
			return true;
		}

		public synchronized int openSpot() {
			for (int i = 0; i < players.length; i++) {
				if (players[i] == null)
					return i;
				if (!FishingTourney.getSingleton().inFishingArea(players[i]) || players[i].fishTourneySession != this) {
					players[i].fishTourneySession = null;
					players[i] = null;
					return i;
				}
			}
			return -1;
		}

		public synchronized int getPlayerCount() {
			int count = 0;
			int added = Misc.random(20,30);
			for (int i = 0; i < players.length; i++) {
				if (players[i] == null)
					continue;
				if (FishingTourney.getSingleton().inFishingArea(players[i]) && players[i].fishTourneySession == this) {
					count++;
				}
			}
			return (count + added);
		}

		public synchronized void removePlayer(Player p) {
			int count = 0;
			for (int i = 0; i < players.length; i++) {
				if (players[i] == p) {
					p.fishTourneySession = null;
					p.getItems().removeAllItems();
					players[i] = null;
				} else if (players[i] != null && players[i].fishTourneySession == this) {
					players[i].sendMessage(p.playerName + " has forfeited from the tourney!");
					count++;
				}
			}

			if (count == 1 && running) {
				endGame(false);
			}
		}

		public int getMinLevel() {
			int min = 99;
			if (running) {
				for (int i = 0; i < players.length; i++) {
					if (players[i] == null)
						continue;
					if (players[i].playerLevel[players[i].playerFishing] < min)
						min = players[i].playerLevel[players[i].playerFishing];
				}
			}
			return min;
		}

		FishingTask currentTask;

	}

	public static boolean isValidFish(int fish) {
		for (FishingTask task : FishingTask.values()) {
			if (task.itemId == fish) {
				return true;
			}
		}
		return false;
	}

	enum FishingTask {
		SHRIMP(1, 317, "shrimp"), ANCHOVIE(1, 321, "anchovie"), TUNA(35, 359, "tuna"), SWORDFISH(35, 371,
				"swordfish"), LOBSTER(40, 377, "lobster"), MONKFISH(62, 7944, "monkfish"), SHARK(76, 383, "shark");

		int minLevel, itemId;
		String name;

		FishingTask(int minLevel, int itemId, String name) {
			this.minLevel = minLevel;
			this.itemId = itemId;
			this.name = name;
		}

		public static FishingTask getTaskForLevel(int minLevel) {
			for (int i = 0; i < FishingTask.values().length; i++) {
				if (minLevel < FishingTask.values()[i].minLevel)
					return FishingTask.values()[((int) (Math.random() * i))];
			}

			return SHRIMP;
		}
	}

	static {
		sessions = new ArrayList<FishingSession>();
		spots = new ArrayList<NPC>();
	}

}
