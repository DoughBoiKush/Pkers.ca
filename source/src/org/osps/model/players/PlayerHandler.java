package org.osps.model.players;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.osps.Config;
import org.osps.Server;
import org.osps.clip.Region;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.minigames.lottery.LotteryController;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.npcs.boss.NewWildernessBoss;
import org.osps.util.Misc;
import org.osps.util.Stream;
import org.osps.world.objects.GlobalObject;

public class PlayerHandler {

	public static Object lock = new Object();
	public static Player players[] = new Player[Config.MAX_PLAYERS];
	public static String messageToAll = "";

	public static boolean updateAnnounced;
	public static boolean updateRunning;
	public static int updateSeconds;
	public static long updateStartTime;
	private boolean kickAllPlayers = false;
	
	static int playerCount;
	
	public static int staffCount = 0;
	
	private static final Map<String, Player> playerByUsername = new HashMap<>();
	
	private static final Map<Long, Player> playerByHash = new HashMap<>();
	
	public static String playersCurrentlyOn[] = new String[Config.MAX_PLAYERS];
	
	private static List<PlayerCannon> cannons = new ArrayList<PlayerCannon>();

	public static PlayerSave save;
	
	
	
	public static void serverTimerProcess() {
		CycleEventHandler.getSingleton().addEvent(0, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				for (int i = 0; i < players.length; i++) {
					Player c = players[i];
					if (c != null) {
						String timeOfDay = Server.calendar.getHMS();
						String[] split = timeOfDay.split(":");
						String hoursAndMinutes = ""+split[0]+":"+split[1];
						c.getPA().sendFrame126("@or1@Server Time: @whi@[@yel@ " +hoursAndMinutes+ " @whi@]", 16044);
					}
				}
			}
		}, 100);
	}
	
	
	public final void getDoublePKPWeekend() {
		Calendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		Config.doublePKPointsWeekend = false;
		if (day > 6) {
			Config.doublePKPointsWeekend = true;
			return;
		} else if (day < 2) {
			Config.doublePKPointsWeekend = true;
			return;
		}
	}
	public final void getDoubleDonationWeekend() {
		Calendar calendar = new GregorianCalendar();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		Config.ChristmasDonation = false;
		if (day > 6) {
			Config.ChristmasDonation = true;
			return;
		} else if (day < 2) {
			Config.ChristmasDonation = true;
			return;
		}
	}
	
	public static boolean cannonExists(int x, int y, int height) {
		for(PlayerCannon can : cannons) {
			if(can.getX() == x && can.getY() == y && can.getHeight() == height) 
				return true;
		}
		return false;
	}
	
	public static void addNewCannon(PlayerCannon cannon) {
		cannons.add(cannon);
		if(cannon.getGlobalObj() != null)
			Server.getGlobalObjects().add(cannon.getGlobalObj());
	}
	
	public static void removeCannon(PlayerCannon cannon) {
		cannons.remove(cannon);
		if(cannon.getGlobalObj() != null)
			Server.getGlobalObjects().remove(cannon.getGlobalObj());
	}
	
	public static void updateCannon(PlayerCannon cannon) {
		if(cannon.getGlobalObj() != null) {
			Server.getGlobalObjects().updateObject(cannon.getGlobalObj(), cannon.phase.getObj(), true);
		}
	}
	
	public static PlayerCannon claimCannon(Player p) {
		for(PlayerCannon cannon : cannons) {
			if(p.playerName.equalsIgnoreCase(cannon.name))  {
				cannon.c = p;
				return cannon;
			}
		}
		return null;
	}
	
	public static void updateCannonOrientation(PlayerCannon cannon, int animationID) {
		for (Player p : PlayerHandler.players) {
			if(p != null && p.isActive && p.heightLevel == cannon.getHeight()) {
				if(p.distanceToPoint(cannon.getX(), cannon.getY()) <= 25) {
					p.getPA().createPlayersObjectAnim(cannon.getX(), cannon.getY(), animationID, 10, -1);	
				}
			}
		}
	}
	
	
	public void updatePlayerNames() {
		playerCount = 0;
		staffCount = 0;
		for(int i = 0; i < Config.MAX_PLAYERS; i++) {
			if(players[i] != null) {
				playersCurrentlyOn[i] = players[i].playerName;
				playerCount++;
				if (players[i].getRights().OnlineStaff()) {
					staffCount++;
				}
			} else {
				playersCurrentlyOn[i] = "";
			}
			
		}
	}

	public static Player getPlayer(String name) {
		if (name == null)
			return null;
		return playerByUsername.get(name.toLowerCase().replaceAll("_", " "));
	}
	
	public static Player getPlayer(int playerId) {
        if (playerId < 0 || playerId >= players.length || players[playerId] == null) {
            return null;
        }
        return (Player) players[playerId];
    }

	public static Optional<Player> getOptionalPlayer(String name) {
		return getPlayers().stream().filter(Objects::nonNull).filter(client -> client.playerName.equalsIgnoreCase(name)).findFirst();
	}
	
	// lmao this is the wrong one nayway

	public static Player getPlayerByLongName(long name) {
		return playerByHash.get(name);
	}

	public static int getPlayerID(String playerName) {
		Player player = getPlayer(playerName);
		if (player == null)
		return -1;
		return player.getId();
	}
	
	public static void sendMessageToAll(String str) {
		sendMessage(str, Arrays.asList(players));
	}

	public boolean newPlayerClient(Player client1) {
		int slot = -1;
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if ((players[i] == null) || players[i].disconnected) {
				slot = i;
				break;
			}
		}
		if (slot == -1)
			return false;
		client1.handler = this;
		client1.index = slot;
		players[slot] = client1;
		players[slot].isActive = true;
		players[slot].connectedFrom = ((InetSocketAddress) client1.getSession().getRemoteAddress()).getAddress().getHostAddress();
		//Server.getLoginLogHandler().logLogin(players[slot], "Login");
		// faster referencing
		playerByUsername.put(client1.playerName.toLowerCase().replaceAll("_", " "), client1);
		playerByHash.put(client1.getUsernameHash(), client1);
		playerCount++;
		return true;
	}

	public void destruct() {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null)
				continue;
			players[i].destruct();
			players[i] = null;
		}
	}

	public static int getPlayerCount() {
		return playerCount;
	}
	public static int getStaffCount() {
		return staffCount;
	}
	
	public static int getPlayersOnline() {
	int count = 0;
	//int countAdded = 25;
		for (int i = 0; i < PlayerHandler.players.length; i++) { 
			if (PlayerHandler.players[i] != null && PlayerHandler.players[i].isActive) {
				count++;
			}
		}
		return count;
	}
	private static final Logger LOGGER = Logger.getLogger(PlayerHandler.class.getSimpleName());

	
	public static boolean isPlayerOn(String playerName) {
		// synchronized (PlayerHandler.players) {
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] != null) {
				if (players[i].playerName.equalsIgnoreCase(playerName)) {
					//LOGGER.log(Level.WARNING, "Possible nullity for player " + playerName +"!");
					//LOGGER.log(Level.WARNING, "disconnected = " + players[i].disconnected + ", properLogout = " + players[i].properLogout);
					//LOGGER.log(Level.WARNING, "isActive = " + players[i].isActive);

					return true;
				}
			}
		}
		return false;
		// }
	}

	/**
	 * Create an int array of the specified length, containing all values
	 * between 0 and length once at random positions.
	 * 
	 * @param length
	 *            The size of the array.
	 * @return The randomly shuffled array.
	 */
	private int[] shuffledList(int length) {
		int[] array = new int[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		Random rand = new Random();
		for (int i = 0; i < array.length; i++) {
			int index = rand.nextInt(i + 1);
			int a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
		return array;
	}

	public void process() {
		try {
			synchronized (lock) {
				updatePlayerNames();
				getDoublePKPWeekend();
				getDoubleDonationWeekend();
				NewWildernessBoss.tick();
				if (kickAllPlayers) {
					for (int i = 1; i < Config.MAX_PLAYERS; i++) {
						if (players[i] != null) {
							players[i].disconnected = true;
						}
					}
				}
				int[] randomOrder = shuffledList(Config.MAX_PLAYERS);
				// System.out.println(Arrays.toString(randomOrder));
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (players[randomOrder[i]] == null || !players[randomOrder[i]].isActive || !players[randomOrder[i]].initialized)
						continue;
					try {
						Player player = players[randomOrder[i]];
						if (players[randomOrder[i]].disconnected
								&& (players[randomOrder[i]].logoutDelay.elapsed(90000) || players[randomOrder[i]].properLogout
										|| kickAllPlayers || Boundary.isIn(player, Boundary.DUEL_ARENAS)
										&& Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.DUEL))) {
							if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
								Server.getMultiplayerSessionListener().finish(player, MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
							}
							DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(
									players[randomOrder[i]], MultiplayerSessionType.DUEL);
							if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
								if (duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
									duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
								} else {
									Player winner = duelSession.getOther(players[randomOrder[i]]);
									duelSession.setWinner(winner);
									duelSession.finish(MultiplayerSessionFinalizeType.GIVE_ITEMS);
								}
							}
							if (Config.BOUNTY_HUNTER_ACTIVE) {
								if (player.getBH().hasTarget()) {
									player.getBH().setWarnings(player.getBH().getWarnings() + 1);
								}
							}
							if(player.fishTourneySession != null) {
								player.fishTourneySession.removePlayer(player);
							}
							if(player.hungerGames) {
								HungerManager.getSingleton().exitGame(player);
									
							}
							Player o = PlayerHandler.players[randomOrder[i]];
							if (PlayerSave.saveGame(o)) {
								System.out.println("Game saved for player " + players[randomOrder[i]].playerName);
							} else {
								System.out.println("Could not save for " + players[randomOrder[i]].playerName);
							}
							playerByUsername.remove(o.playerName.toLowerCase().replaceAll("_", " "));
							playerByHash.remove(o.getUsernameHash());
							removePlayer(players[randomOrder[i]]);
							players[randomOrder[i]] = null;
							continue;
						}
						players[randomOrder[i]].process();
						players[randomOrder[i]].postProcessing();
						players[randomOrder[i]].getNextPlayerMovement();

				//		players[randomOrder[i]].preProcessing();
						
						//player.doFollow(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (int i = 0; i < Config.MAX_PLAYERS; i++){
					if (players[randomOrder[i]] == null || !players[randomOrder[i]].isActive || !players[randomOrder[i]].initialized)
						continue;
					try {
						players[randomOrder[i]].update();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (int i = 0; i < Config.MAX_PLAYERS; i++) {
					if (players[randomOrder[i]] == null || !players[randomOrder[i]].isActive || !players[randomOrder[i]].initialized)
						continue;
					try {
						players[randomOrder[i]].onReset();

						players[randomOrder[i]].preProcessing();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (updateRunning && !updateAnnounced) {
					updateAnnounced = true;
					Server.UpdateServer = true;
				}
				if (updateRunning && (System.currentTimeMillis() - updateStartTime > (updateSeconds * 1000))) {
					kickAllPlayers = true;
				}
			}
		} catch (Exception e) {
			System.out.println("PlayerHandler" + e);
			e.printStackTrace();
		}

	}

	public void updateNPC(Player plr, Stream str) {
		// synchronized(plr) {
		updateBlock.currentOffset = 0;

		str.createFrameVarSizeWord(65);
		str.initBitAccess();

		str.writeBits(8, plr.npcListSize);
		int size = plr.npcListSize;
		plr.npcListSize = 0;
		for (int i = 0; i < size; i++) {
			if (plr.rebuildNPCList == false && plr.withinDistance(plr.npcList[i]) == true) {
				plr.npcList[i].updateNPCMovement(str);
				plr.npcList[i].appendNPCUpdateBlock(updateBlock);
				plr.npcList[plr.npcListSize++] = plr.npcList[i];
			} else {
				int id = plr.npcList[i].index;
				plr.npcInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				int id = NPCHandler.npcs[i].index;
				if (plr.rebuildNPCList == false && (plr.npcInListBitmap[id >> 3] & (1 << (id & 7))) != 0) {

				} else if (plr.withinDistance(NPCHandler.npcs[i]) == false) {

				} else {
					plr.addNewNPC(NPCHandler.npcs[i], str, updateBlock);
				}
			}
		}

		plr.rebuildNPCList = false;

		if (updateBlock.currentOffset > 0) {
			str.writeBits(14, 16383);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else {
			str.finishBitAccess();
		}
		str.endFrameVarSizeWord();
		// }
	}

	private Stream updateBlock = new Stream(new byte[Config.BUFFER_SIZE]);

	public void updatePlayer(Player plr, Stream str) {
		// synchronized(plr) {
		updateBlock.currentOffset = 0;
		if (updateRunning && !updateAnnounced) {
			str.createFrame(114);
			str.writeWordBigEndian(updateSeconds * 50 / 30);
		}
		plr.updateThisPlayerMovement(str);
		boolean saveChatTextUpdate = plr.isChatTextUpdateRequired();
		plr.setChatTextUpdateRequired(false);
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setChatTextUpdateRequired(saveChatTextUpdate);
		str.writeBits(8, plr.playerListSize);
		int size = plr.playerListSize;
		if (size >= 250)
			size = 250;
		plr.playerListSize = 0;
		for (int i = 0; i < size; i++) {
			if (!plr.didTeleport && !plr.playerList[i].didTeleport && plr.withinDistance(plr.playerList[i])) {
				plr.playerList[i].updatePlayerMovement(str);
				plr.playerList[i].appendPlayerUpdateBlock(updateBlock);
				plr.playerList[plr.playerListSize++] = plr.playerList[i];
			} else {
				int id = plr.playerList[i].index;
				plr.playerInListBitmap[id >> 3] &= ~(1 << (id & 7));
				str.writeBits(1, 1);
				str.writeBits(2, 3);
			}
		}

		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (players[i] == null || !players[i].isActive || players[i] == plr)
				continue;
			int id = players[i].index;
			if ((plr.playerInListBitmap[id >> 3] & (1 << (id & 7))) != 0)
				continue;
			if (!plr.withinDistance(players[i]))
				continue;
			plr.addNewPlayer(players[i], str, updateBlock);
		}

		if (updateBlock.currentOffset > 0) {
			str.writeBits(11, 2047);
			str.finishBitAccess();
			str.writeBytes(updateBlock.buffer, updateBlock.currentOffset, 0);
		} else
			str.finishBitAccess();

		str.endFrameVarSizeWord();
		// }
	}

	private void removePlayer(Player plr) {
		plr.destruct();
	}

	public static void executeGlobalMessage(String message, boolean yell) {
		for (int i = 0; i < players.length; i++) {
			Player c = players[i];
			if (c != null) {
				if (c.getPA().yellon && yell)
					c.sendMessage(message);
				else if (!yell)
					c.sendMessage(message);
			}
		}
	}
	
	public static void executeBroadcast(String message) {
		System.out.println("Created new broadcast: " + message);
		Player[] clients = new Player[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		Arrays.asList(clients).stream().filter(Objects::nonNull).forEach(player -> player.getPA().sendFrame126(message, 3755));
	}

	public static void sendMessage(String message, List<Player> players) {
		for (Player player : players) {
			if (Objects.isNull(player)) {
				continue;
			}
			player.sendMessage(message);
		}
	}

	public static List<Player> getPlayers() {
		Player[] clients = new Player[players.length];
		System.arraycopy(players, 0, clients, 0, players.length);
		return Arrays.asList(clients).stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	public static List<Player> getPlayerList() {
		return Arrays.asList(players);
	}

	public static java.util.stream.Stream<Player> stream() {
		return Arrays.stream(players);
	}

	public static Logger getLogger() {
		return LOGGER;
	}
}
