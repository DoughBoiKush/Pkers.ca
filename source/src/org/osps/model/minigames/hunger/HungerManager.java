package org.osps.model.minigames.hunger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.osps.Config;
import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.items.GroundItem;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.PlayerSave;
import org.osps.util.Misc;
import org.osps.world.objects.GlobalObject;

public class HungerManager {
	
	final int MIN_PLAYERS = 2;
	final int GAME_MINS = 30;
	final int LOBBY_MINS = 2;
	
	List<PlayerWrapper> players;
	List<HungerCrate> objects;
	List<NPC> npcs;
	NPC hops;
	public GroundItem southLog, northLog;
	public GlobalObject southFire, northFire;
	
	public void initializeLogs() {
		northLog = new GroundItem(3438, 2376, 3119, 0, 1);
		southLog = new GroundItem(3438, 2423, 3088, 0, 1);
		Server.itemHandler.addItem(northLog);
		Server.itemHandler.addItem(southLog);
	}
	
	public int getZombieCount() { //3137
		if(npcs == null)
			return 0;
		int wow = 0;
		for(NPC n : npcs) {
			if(n.npcType == 3137)
				wow++;
		}
		return wow;
	}
	
	public void startHopsFight(Player p) {
		if(hops == null)
			return;
		CycleEventHandler.getSingleton().addEvent(p, new CycleEvent() {
			int ticks = 0;
			@Override
			public void execute(CycleEventContainer container) {
				switch(ticks) {
				case 1:
					hops.animation(1327);
					break;
				case 4:
					hops.forceChat("OH GOD WHAT DID YOU FEED ME!");
					break;
				case 7:
					hops.animation(3170);
					break;
				case 9:
					int x = hops.absX;
					int y = hops.absY;
					hops.absX = -1;
					hops.absY = -1;
					npcs.remove(hops);
					hops = null;//6393
					
					announce("@or2@A boss has spawned under the base!");
					p.getDH().sendNpcChat1("You will pay for that, human!", 6393, "Vampire Lord");	
					NPC n = NPCHandler.spawnGenNpc(6393, x, y, 0, 0);
					n.hungerNPC = true;
					n.forceChat("PREPARE TO FACE THE HORROR!");
					p.getPA().drawHeadicon(1, n.index, 0, 0);
					n.underAttack = true;
					n.killerId = p.index;
					npcs.add(n);
					
					container.stop();
					break;
				}
				ticks++;
			}
		},1);
	}
	
	public boolean lightFire(int x, int y) {
		if(southLog != null && southLog.getX() == x && southLog.getY() == y) {
			Server.itemHandler.removeGlobalItem(southLog, southLog.getId(), southLog.getX(), 
					southLog.getY(), southLog.getHeight(), 1);
			southFire = new GlobalObject(5249, x, y, 0, 0, 10, -1);
			Server.getGlobalObjects().add(southFire);
			southLog = null;
			return true;
		}
		
		if(northLog != null && northLog.getX() == x && northLog.getY() == y) {
			Server.itemHandler.removeGlobalItem(northLog, northLog.getId(), northLog.getX(), 
					northLog.getY(), northLog.getHeight(), 1);			
			northFire = new GlobalObject(5249, x, y, 0, 0, 10, -1);
			Server.getGlobalObjects().add(northFire);
			northLog = null;
			return true;
		}
		
		return false;
	}
	
	public void enterGame(Player p) {
		if(p.getItems().freeSlots() < 28 || p.getItems().isWearingItems()) {
			p.sendMessage("Please bank all items before entering the game.");
			return;
		} else if(PlayerHandler.updateRunning) {
			p.sendMessage("You cannot join a game when an update is running!");
			return;
		} else if(!Config.HUNGER_GAMES) {
			p.sendMessage("Hunger games is currently disabled!");
			return;
		}
		
		if(running) {
			p.sendMessage("You cannot enter while a game is running.");
			return;
		} else {
			p.getPA().movePlayer(2376, 9487, 0);
			p.sendMessage("You enter the lobby.");	
			p.canUsePackets = false;
			PlayerSave.saveGame(p);
			synchronized(players) {
				players.add(new PlayerWrapper(p, PlayerWrapper.Type.PLAYER));
			}
			if(getActivePlayers() >= MIN_PLAYERS && waitTimer == null) {
				waitTimer = new Timer();
				waitTimer.scheduleAtFixedRate(new TimerTask() {

					int time = 0;
					
					@Override
					public void run() {
						time += 1;
						if(time > LOBBY_MINS * 60) {
							startGame();
							cancel();
						} else {
							int olTime = time;
							int secsRemaining = (LOBBY_MINS * 60) - time;
							String time = String.format("%02d:%02d", 
								    TimeUnit.SECONDS.toMinutes(secsRemaining),
								    TimeUnit.SECONDS.toSeconds(secsRemaining) - 
								    TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(secsRemaining))
								);
							updateLobbyInterface(time);
							if((LOBBY_MINS * 60) - olTime == 90) {
								PlayerHandler.executeGlobalMessage("[<col=255>HUNGER GAMES</col>] The hunger games wil begin in 90 seconds!", false);
							}
						}
					}
					
				}, 1000, 1000);
			} else if(waitTimer == null) {
				updateLobbyInterface(null);
			}
		}
	}
	
	public void updateLobbyInterface(String time) {
		for(PlayerWrapper w : players) {
			CycleEventHandler.getSingleton().addEvent(w.getPlayer(), new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if(w == null || w.getPlayer() == null || w.getPlayer().disconnected) {
						players.remove(w);
					} else {
						if(gameTimer != null)
							w.getPlayer().getPA().sendFrame126(time, 11480);
						else if(time == null)
							w.getPlayer().getPA().sendFrame126("Waiting for " +  (MIN_PLAYERS - 
									getActivePlayers()) + " more player(s) to begin.", 11480);
						else 
							w.getPlayer().getPA().sendFrame126("Game begins in: " + time + " minute(s).", 11480);
					}
					container.stop();
				}
			}, 1);
		}
	}
	
	public boolean exitGame(Player p) {
		synchronized(players) {
			Iterator<PlayerWrapper> $it = players.iterator();
			while ($it.hasNext()) {
				PlayerWrapper wrap = $it.next();
				if (wrap.getPlayer() == p) {
					exitGame(wrap);
					$it.remove();
					return true;
				}
			}
			return false;
		}
	}
	
	public PlayerWrapper getWrapper(Player p) {
		synchronized(players) {
			for(PlayerWrapper p2 : players) {
				if(p2.getPlayer() == p)
					return p2;
			}
		}
		return null;
	}

	
	public void cleanAssets() {
		if(objects!=null) {
			for(HungerCrate c : objects) {
				if(c == null)
					continue;
				c.destroy();
			}
			objects = null;
		}
		
		if(npcs!=null) {
			for(NPC c : npcs) {
				if(c == null)
					continue;
				c.absX = -1;
				c.absY = -1;
				c = null;
			}
		}
		npcs = null;
		hops = null;
		
		if(northLog != null) {
			Server.itemHandler.removeGlobalItem(northLog, northLog.getId(), northLog.getX(), 
					northLog.getY(), northLog.getHeight(), 1);	
			northLog = null;
		}
		if(southLog != null) {
			Server.itemHandler.removeGlobalItem(southLog, southLog.getId(), southLog.getX(), 
					southLog.getY(), southLog.getHeight(), 1);	
			southLog = null;
		}
		if(northFire != null) {
			Server.getGlobalObjects().remove(northFire);
			northFire = null;
		}
		if(southFire != null) {
			Server.getGlobalObjects().remove(southFire);
			southFire = null;
		}
	}
	
	public boolean undergroundUnlocked() {
		return underground;
	}
	
	public void unlockDoor(Player p) {
		underground = true;
		announce("@or2@"+p.playerName + " has unlocked the trapdoors!");
	}
	
	public boolean doorUnlocked() {
		return picklock;
	}
	
	public void unlockSmithDoor(Player p) {
		picklock = true;
		announce("@or2@"+p.playerName + " has unlocked the doors to the base!");
	}
	
	public void endGame(boolean arupt) {
		running = false;
		otChat = false;
		underground = false;
		picklock = false;
		CycleEventHandler.getSingleton().addEvent(null, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				cleanAssets();
				container.stop();
			}
		}, 1);
		String name = "";
		synchronized(players) {
			if(!finalBattle && !arupt) {
				Optional<PlayerWrapper> p = players.stream().filter(e -> e.t == PlayerWrapper.Type.PLAYER && !e.getPlayer().isDead && !e.getPlayer().disconnected).findAny();
				if(p.isPresent() && p.get().getPlayer() != null) {
					name = p.get().getPlayer().playerName;
					p.get().getPlayer().sendMessage("Congratulations! You have won the tournament!");
					p.get().getPlayer().hungerPoints += netDamage;
					p.get().getPlayer().sendMessage("You receive @red@" + netDamage + "@bla@ sacrifice points for the total damage dealt.");
					exitGame(p.get());
				}
			} else if(finalBattle && !arupt) {
				for(PlayerWrapper p : players) {
					if(p != null && p.getPlayer()!= null) {
						if(p.betrayedLanthus()) {
							p.getPlayer().getDH().sendNpcChat1("We did it! We have defeated Lanthus!", 3775, "Safalaan");	
							p.getPlayer().sendMessage("You receive @red@" + netDamage + "@bla@ sacrifice points for the total damage dealt.");
							p.getPlayer().sendMessage("You receive @red@2000@bla@ extra sacrifice points for defeating Lanthus.");
							p.getPlayer().hungerPoints += 2000;
							p.getPlayer().hungerPoints += netDamage;
							exitGame(p);
						} else {
							p.getPlayer().sendMessage("The players have overthrown Lanthus!");
							exitGame(p);
						}
					} else {
						players.remove(p);
					}
				}
			} else if(arupt) {
				players.stream().filter(p -> p != null && p.getPlayer() != null).forEach(w->exitGame(w));
				players.clear();
			}
		}
		finalBattle = false;
		if(gameTimer != null) {
			gameTimer.cancel();
			gameTimer = null;
		}
		if(waitTimer != null) {
			waitTimer.cancel();
			waitTimer = null;
		}
		players.clear();
		PlayerHandler.executeGlobalMessage("</img>[<col=255>HUNGER GAMES</col>] The hunger games have ended with the winner being " + name + "!", false);
	}
		
	public void startGame() {
		try {
			if(running || gameTimer != null)
				return;
			objects = new ArrayList<HungerCrate>();
			npcs = new ArrayList<NPC>();
			spawnCrates(players.size() * 3);
			objects.add(new HungerCrate(2400, 3104, HungerCrate.Quality.SUPERITEM));
			netDamage = 0;
			List<Long> points = generateSpawnPoints(players.size());
			IntStream.range(0, players.size()).distinct()
				.parallel().forEach(i->players.get(i).
						moveToSpawnPoint(points.get(i)));
			//(int)(l>>>32), (int)(l&(1L<<32)-1)
			//2440, 3090
			initializeLogs();
			
			hops = NPCHandler.spawnGenNpc(1108, 2421, 9519, 0, 1);
			hops.hungerNPC = true;
			npcs.add(hops);
			
			gameTimer = new Timer();
			updateLobbyInterface("@red@Game starting...");

			gameTimer.scheduleAtFixedRate(new TimerTask() {
				int time = 0;		
				@Override
				public void run() {
					try {
						time += 1;
						if(time >= 10 && !running) {
							for(PlayerWrapper w : players) {
								if(w!= null && w.getPlayer() != null && !w.getPlayer().disconnected) {
									w.getPlayer().getDH().sendNpcChat1("Let the sacrifice begin!", 5721, "Lanthus");
									w.getPlayer().canUsePackets = true;
								}
							}
							running = true;
							cancel();
							gameTimer.scheduleAtFixedRate(new TimerTask() {
								int time = 0;		
								@Override
								public void run() {
									try {
										time += 1;
										int playerC = getActivePlayers();
										if(playerC <= 1) {
											endGame(false);
											cancel();
											return;
										}
										if(time == 30 || time % 60 == 0) {
											if(npcs.size() - 4 < playerC * 4) {
												announce("Monsters have spawned inside the arena!");
												spawnNPCS((playerC * 4) + 6 - npcs.size());
											}
										} else if(time % 75 == 0) {
											if(objects.size() < playerC * 3) {
												announce("Survival crates have been spawned!");
												spawnCrates((playerC * 3) - objects.size());
											}
										}
										
										if(time >= (GAME_MINS * 60) && running) { //OT
											for(PlayerWrapper w : players) {
												if(w == null || w.getPlayer() == null || w.getPlayer().disconnected) {
													exitGame(w);
													continue;
												}
												if(!otChat) {
													updateLobbyInterface("@red@OVERTIME");	
													w.getPlayer().getDH().sendNpcChat2("It is now time for overtime!", 
															"Let the blood be shed!", 5721, "Lanthus");
													otChat = true;
												}
												w.applyPoison();
											}
										} else {
											int secsRemaining = (GAME_MINS * 60) - time;
											String time = String.format("%02d:%02d", 
												    TimeUnit.SECONDS.toMinutes(secsRemaining),
												    TimeUnit.SECONDS.toSeconds(secsRemaining) - 
												    TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(secsRemaining))
												);
											if(secsRemaining <= 5 * 60)
												updateLobbyInterface("@red@Players: " + playerC + "  |  " + "Damage Dealt: " + netDamage + "  |  " +
														"Time remaining: " +time);									
											else 
												updateLobbyInterface("Players: " + playerC + "  |  " + "Damage Dealt: " + netDamage + "  |  " +
														"Time remaining: " + time);	
										}
									} catch(Exception e) {
										e.printStackTrace();
									}
								}

							}, 500, 1000);
						} else if(!running) {
							players.stream().filter(p -> p != null && p.getPlayer() != null).forEach(w -> w.getPlayer().forceChat(""+(10 - time)));
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
				
			}, 1000, 1000);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void spawnCrates(int count) {
		for(int i = 0; i < count; i++) {
			int[] point = getRandomPoint();
			HungerCrate crate = new HungerCrate(point[0], point[1]);
			objects.add(crate);
		}
	}
	
	public void claimCrate(Player p, int x, int y) {
		PlayerWrapper player = getWrapper(p);
		if(player == null)
			return;
		for(HungerCrate c : objects) {
			if(c.isCrate(x, y)) {
				c.claimCrate(player);
				objects.remove(c);
				return;
			}
		}
	}
	
	public void playerHasAllDiamonds(PlayerWrapper w) {
		announce("@or2@A boss has spawned in the center of the arena!");
		w.getPlayer().getDH().sendNpcChat1("What you do with me diamonds human!", 4851, "Goblin Butcher");	
		NPC n = NPCHandler.spawnGenNpc(4851, 2399, 3103, 0, 0);
		n.hungerNPC = true;
		n.forceChat("WHERE ME DIAMONDS GO!");
		w.getPlayer().getPA().drawHeadicon(1, n.index, 0, 0);
		n.underAttack = true;
		n.killerId = w.getPlayer().index;
		npcs.add(n);
	}
	
	private void spawnNPCS(int count) {
		int combat = getAvgCombat();
		int id1 = 3048;
		int id2 = -1;
		if(combat > 6 && combat <= 15) {
			id2 = 70;	
		} else if(combat > 15 && combat <= 30) {
			id1= 1066;
			id2 = 231;
		} else if(combat > 30 && combat < 60) {
			id1 = 6267;
			id2 = 1545;
		} else if(combat >= 60 && combat <= 70) {
			id1 = 2085;
			id2 = 2594;
		} else if(combat > 70 && combat <= 90) {
			id1 = 2005;
			id2 = 2077;
		} else if(combat > 90 && combat < 110) {
			id1 = 272;
			id2 = 415;
		} else if(combat >= 110) {
			id1 = 1537;
		}
		for(int i = 0; i < count; i++) {
			int type = id1;
			if(id2 > 0 && Misc.random(2) == 0)
				type = id2;
			int[] point = getRandomPoint();
			NPC n = NPCHandler.spawnGenNpc(type, point[0], point[1], 0, 1);
			n.hungerNPC = true;
			npcs.add(n);
		}
	}
	
	public int getFoodForCombat(int combat) {
		if(combat >= 1 && combat <= 15) {
			return 315;	
		} else if(combat > 15 && combat <= 30) {
			return 361;
		} else if(combat > 30 && combat < 60) {
			return 379;
		} else if(combat >= 60 && combat <= 70) {
			return 373;
		} else if(combat > 70 && combat <= 90) {
			return 373;
		} else if(combat > 90 && combat < 110) {
			return 385;
		}
		return 7060;
	}
	
	
	public void handleNPCDeath(Player player, NPC n) {
		if (npcs == null) {
			return;
		}
		if(n.npcType == 4851) {
			player.sendMessage("You have defeated the goblin butcher!");
			player.getPA().addSkillXP(7000000, player.playerStrength, false);
			Server.itemHandler.createGroundItem(player, 2440, n.absX, n.absY,
					player.heightLevel, 3, player.index);	
			Server.itemHandler.createGroundItem(player, 1949, n.absX, n.absY,
					player.heightLevel, 1, player.index);
			PlayerWrapper wrap = getWrapper(player);
			if(wrap!= null && wrap.hasAllDiamonds()) {
				player.sendMessage("More prizes await you outside the arena!");
				Server.itemHandler.createGroundItem(player, 9008, n.absX, n.absY,
						player.heightLevel, 1, player.index);
				player.hunger1 = true;
			}
		} else if(n.npcType == 6393) {
			player.sendMessage("You have defeated the goblin butcher!");
			player.getPA().addSkillXP(7000000, player.playerStrength, false);
			Server.itemHandler.createGroundItem(player, 2440, n.absX, n.absY,
					player.heightLevel, 3, player.index);	
			Server.itemHandler.createGroundItem(player, 1949, n.absX, n.absY,
					player.heightLevel, 1, player.index);
			PlayerWrapper wrap = getWrapper(player);
			if(wrap!= null && wrap.hasAllDiamonds()) {
				player.sendMessage("More prizes await you outside the arena!");
				Server.itemHandler.createGroundItem(player, 9008, n.absX, n.absY,
						player.heightLevel, 1, player.index);
				player.hunger2 = true;
			}					
		} else if(n.npcType == 4178) {

			endGame(false);
		}
		if(player.combatLevel > 80 && Misc.random(50) == 2) {
			PlayerWrapper w = getWrapper(player);
			if(w!= null && !w.hasAllDiamonds()) {
				int diamond = w.getRandomDiamond();
				if(diamond != -1)
					Server.itemHandler.createGroundItem(player, diamond, n.absX, n.absY,
							player.heightLevel, 1, player.index);
			}
		}
		
		if(Misc.random(5) == 0) {
			Server.itemHandler.createGroundItem(player, picklock ? 8125 : 1523, n.absX, n.absY,
					player.heightLevel, 1, player.index);	
		} else if(Misc.random(5) == 0) {
			Server.itemHandler.createGroundItem(player, 526, n.absX, n.absY,
					player.heightLevel, 1, player.index);	
		}  
		if(Misc.random(20) == 0) {
			Server.itemHandler.createGroundItem(player, 141, n.absX, n.absY,
					player.heightLevel, 1, player.index);	
		} else {
			Server.itemHandler.createGroundItem(player, getFoodForCombat(player.combatLevel), n.absX, n.absY,
					player.heightLevel, 1 + Misc.random(2), player.index);				
		}
		npcs.remove(n);

	}
	 
	public int[] getRandomPoint() {
		int[] point = new int[2];
	
		switch(Misc.random(3)) {
			case 0:
				point[0] = 2413 + Misc.random(18);
				point[1] = 3097 + Misc.random(20);
				break;
			case 1:
				point[0] = 2390 + Misc.random(16);
				point[1] = 3114 + Misc.random(20);
				break;			
			case 2:
				point[0] = 2368 + Misc.random(13);
				point[1] = 3093 + Misc.random(19);
				break;				
			default:
				point[0] = 2389 + Misc.random(21);
				point[1] = 3074 + Misc.random(16);				
				break;
		}
		return point;
	}
	
	private int getAvgCombat() {
		int level =0;
		int count  = 0;
		for(PlayerWrapper p : players ) {
			if(p != null && p.getPlayer() != null && p.t == PlayerWrapper.Type.PLAYER) {
				level += p.getPlayer().combatLevel;
				count++;
			}
		}
		return level / count;
	}
	
	
	public void applyDamage(int dmg) {
		netDamage += dmg;
	}
	
	public void exitGame(PlayerWrapper p) {
		CycleEventHandler.getSingleton().addEvent(null, new CycleEvent() {
			int ticks = 0;
			@Override
			public void execute(CycleEventContainer container) {
				switch(ticks) {
					case 0:
						if(p == null || p.getPlayer() == null) 
							return;
						if(running && p.t == PlayerWrapper.Type.PLAYER)
							announce(p.getPlayer().playerName + " has been eliminated!");
						p.destroy();
						p.getPlayer().hungerGames = false;
						p.getPlayer().getPA().walkableInterface(-1);
						
						if(!running && waitTimer != null && getActivePlayers() < MIN_PLAYERS) {
							waitTimer.cancel();
							waitTimer = null;
							updateLobbyInterface(null);
						}
						break;
					
					case 1:
						p.getPlayer().getPA().movePlayer(2440, 3090, 0);
						container.stop();
						break;
					
				}
				ticks++;
			}
		}, 2);
	}
	
	public void announce(String s) {
		for(PlayerWrapper p : players) {
			if(p == null || p.getPlayer() == null)
				continue;
			p.getPlayer().sendMessage(s);
		}
	}

	public List<Long> generateSpawnPoints(int num) {
		int count=SPAWN_COUNT;

		assert count > num;

		List<Long> points = new Random().longs(0, count).distinct().limit(num).
				mapToObj(i -> allPoints().skip(i).findFirst().getAsLong())
		        .collect(Collectors.toList());
		Collections.shuffle(points);
		return points;
	}
	
	final int SPAWN_COUNT = (int) allPoints().count();
	
	public LongStream allPoints() {
		return 
			LongStream.concat(
				LongStream.concat(
					LongStream.concat(LongStream.range(2384, 2413).map(x -> x <<32 | 3072),
						LongStream.range(3072, 3084).map(y -> 2413L <<32 | y)
					),
					LongStream.concat(LongStream.range(2386, 2415).map(x -> x <<32 | 3135),
							LongStream.range(3088, 3117).map(y -> 2368L <<32 | y)
					)
				),
				LongStream.concat(LongStream.range(3090, 3119).map(y -> 2431L <<32 | y),
					LongStream.range(3123, 3135).map(y -> 2386L <<32 | y)
				)
			);
	}

	private HungerManager() {
		players = new ArrayList<PlayerWrapper>();
	}
	
	public static HungerManager getSingleton() {
		if(man == null)
			man = new HungerManager();
		return man;
	}
	

	public boolean gameRunning() {
		return running;
	}
	
	public int getActivePlayers() {
		return (int) players.stream().filter(e -> e.t == PlayerWrapper.Type.PLAYER && !e.getPlayer().disconnected && e.getPlayer().isActive).count();
	}
	
	private static final Boundary BOUNDS = new Boundary(2365, 3060, 2435, 3139, 0);
	//private static final Boundary MULTIBOUNDS = new Boundary(2368, 3071, 2400, 3135, 0);
	//private static final Boundary SINGLEBOUNDS = new Boundary(2401, 3072, 2435, 3139, 0);
	private static final Boundary UNDER = new Boundary(2392, 9495, 2410, 9515, 0);
	private static final Boundary UNDER2 = new Boundary(2409, 9510, 2432, 9535, 0);

	private static final Boundary LOBBY = new Boundary(2368, 9480, 2398, 9500, 0);

	
	public boolean inArena(Player p) {
		return Boundary.isIn2(p, BOUNDS, UNDER, UNDER2);
	}
	
	
	public boolean inLobby(Player p) {
		return Boundary.isIn2(p, LOBBY, BOUNDS);
	}

	public boolean inArea(int x, int y) {
		return Boundary.isIn(x, y, BOUNDS,UNDER,UNDER2,LOBBY);
	}
	
	
	static HungerManager man;
	Timer gameTimer, waitTimer;
	boolean running;
	boolean otChat = false;
	boolean finalBattle = false, underground = false, picklock = false;
	int netDamage;


}
