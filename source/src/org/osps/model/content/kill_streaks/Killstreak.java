package org.osps.model.content.kill_streaks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.osps.Config;
import org.osps.model.players.Player;
/**
 * A killstreak can be described as a conjunction of successful kills that
 * are obtained one after the other. 
 * 
 * @author Jason MacKeigan
 * @date Jan 10, 2015, 3:38:55 AM
 */
public class Killstreak {
	
	/**
	 * A mapping of the different killstreaks the player has
	 */
	private Map<Type, Integer> killstreaks = new HashMap<>();
	
	/**
	 * The player receiving the killstreaks
	 */
	private Player player;
	
	/**
	 * Creates a new object that will manage player killstreaks
	 * @param player	the player
	 */
	public Killstreak(Player player) {
		this.player = player;
	}
	
	/**
	 * Resets the type of killstreak for the player
	 * @param type	the type of killstreak
	 */
	public void reset(Type type) {
		if (killstreaks.containsKey(type)) {
			killstreaks.remove(type);
		}
	}
	
	/**
	 * Resets all player killstreaks. This is generally called when a 
	 * player dies during PVP combat.
	 */
	public void resetAll() {
		killstreaks.clear();
	}
	
	/**
	 * Increases the killstreak for the specific type
	 * @param type	the type of killstreak
	 */
	public void increase(Type type) {
		int value = 1 + killstreaks.getOrDefault(type, 0);
		killstreaks.put(type, value);
		reward(type);
	}
	
	/**
	 * Rewards the player with some item, points, and or some other form of currency.
	 * @param type	the type of killstreak
	 */
	private void reward(Type type) {
		int streak = killstreaks.getOrDefault(type, 0);
		Optional<KillstreakReward> reward = Arrays.asList(type.rewards).stream().filter(s ->
			s.killstreak == streak).findFirst();
		if (reward.isPresent()) {
			reward.get().append(player);
		}
		if (streak >= type.maximumKillstreak) {
			killstreaks.put(type, 0);
			player.sendMessage("<col=CC0000>Your killstreak has been reset because it has reached the maximum, congratulations.</col>");
		}
	}
	
	/**
	 * The amount of killstreaks a player has for the type of killstreak
	 * @param type	the type of killstreak
	 * @return	zero will be returned if there is no mapping for the killstreak, otherwise
	 * the value for the killstreak mapping will be returned.
	 */
	public int getAmount(Type type) {
		return killstreaks.getOrDefault(type, 0);
	}
	
	/**
	 * A mapping of all of the killstreaks
	 * @return	a mapping
	 */
	public Map<Type, Integer> getKillstreaks() {
		return killstreaks;
	}
	
	/**
	 * Returns the sum of all killstreaks.
	 * @return The sum of all killstreaks.
	 */
	public int getTotalKillstreak() {
		int total = 0;
		for (Type type : Type.values()) {
			total += getAmount(type);
		}
		return total;
	}

	/**
	 * There are several different types of killstreaks. Allowing early 
	 * support for different types of killstreaks will allow for the addition
	 * of more in the future without having to do an overhaul. 
	 */
	public enum Type {
		ROGUE(500, new KillstreakReward(2) {

				@Override
				public void append(Player player) {
					player.pkp += 2;
					player.sendMessage("You are on a 2 killstreak, you have been given 2 Pk Points.");
					//player.sendMessageToAll("[<img=10><col=255>Streak</col>] "+ Misc.ucFirst(player.playerName) + " </col>is on a killstreak of 2");
				}
				
			},
			new KillstreakReward(3) {

				@Override
				public void append(Player player) {
					player.pkp += 3;
					player.sendMessage("You are on a 3 killstreak, you have been given 3 Pk Points.");
				}
				
			},
			new KillstreakReward(4) {

				@Override
				public void append(Player player) {
					player.pkp += 4;
					player.sendMessage("You are on a 4 killstreak, you have been given 4 Pk Points.");
				}
				
			},
			new KillstreakReward(5) {
				
				@Override
				public void append(Player player) {
					player.pkp += 5;
					player.sendMessage("You are on a 5 killstreak, you have been given 5 Pk Points.");
				}
			},
			new KillstreakReward(6) {
				
				@Override
				public void append(Player player) {
					player.pkp += 6;
					player.sendMessage("You are on a 6 killstreak, you have been given 6 Pk Points.");
				}
			},
			new KillstreakReward(7) {
				
				@Override
				public void append(Player player) {
					player.pkp += 7;
					player.sendMessage("You are on a 7 killstreak, you have been given 7 Pk Points.");
				}
			},
			new KillstreakReward(8) {
				
				@Override
				public void append(Player player) {
					player.pkp += 8;
					player.sendMessage("You are on a 8 killstreak, you have been given 8 Pk Points.");
				}
			},
			new KillstreakReward(9) {
				
				@Override
				public void append(Player player) {
					player.pkp += 9;
					player.sendMessage("You are on a 9 killstreak, you have been given 9 Pk Points.");
				}
			},
			new KillstreakReward(10) {
				
				@Override
				public void append(Player player) {
					player.pkp += 10;
					player.sendMessage("You are on a 10 killstreak, you have been given 10 Pk Points.");
					player.sendMessage("Congratulations on killing 10 players in a row.");
				}
			}
		),
		HUNTER(500, new KillstreakReward(10) {

				@Override
				public void append(Player player) {
					player.pkp += 2;
					player.sendMessage("You are on a 2 hunting killstreak, you have been given 2 Pk Points.");
				}
				
			},
			new KillstreakReward(3) {

				@Override
				public void append(Player player) {
					player.pkp += 3;
					player.sendMessage("You are on a 3 hunting killstreak, you have been given 3 Pk Points.");
				}
				
			},
			new KillstreakReward(4) {

				@Override
				public void append(Player player) {
					player.pkp += 4;
					player.sendMessage("You are on a 4 hunting killstreak, you have been given 4 Pk Points.");
				}
				
			},
			new KillstreakReward(5) {

				@Override
				public void append(Player player) {
					player.pkp += 5;
					player.sendMessage("You are on a 5 hunting killstreak, you have been given 5 Pk Points.");
				}
				
			},
			new KillstreakReward(6) {

				@Override
				public void append(Player player) {
					player.pkp += 6;
					player.sendMessage("You are on a 6 hunting killstreak, you have been given 6 Pk Points.");
				}
				
			},
			new KillstreakReward(7) {

				@Override
				public void append(Player player) {
					player.pkp += 7;
					player.sendMessage("You are on a 7 hunting killstreak, you have been given 7 Pk Points.");
				}
				
			},
			new KillstreakReward(8) {

				@Override
				public void append(Player player) {
					player.pkp += 8;
					player.sendMessage("You are on a 8 hunting killstreak, you have been given 8 Pk Points.");
				}
				
			},
			new KillstreakReward(9) {

				@Override
				public void append(Player player) {
					player.pkp += 9;
					player.sendMessage("You are on a 9 hunting killstreak, you have been given 9 Pk Points.");
				}
				
			},
			new KillstreakReward(10) {

				@Override
				public void append(Player player) {
					player.pkp += 10;
					player.getItems().addItemUnderAnyCircumstance(12751, 1);
					player.sendMessage("You are on a 10 hunting killstreak, you have been given 10 Pk Points.");
					player.sendMessage("You have also been given 1 Mysterious emblem (tier-5).");
					player.sendMessage("Congratulations on killing 10 players in a row.");
				}
				
			}
		);
		
		private int maximumKillstreak;
		private KillstreakReward[] rewards;
		
		private Type(int maximumKillstreak, KillstreakReward... rewards) {
			this.maximumKillstreak = maximumKillstreak;
			this.rewards = rewards;
		}
		
		public static Type get(String name) {
			Optional<Type> op = Arrays.asList(values()).stream().filter(t -> t.name().equals(name)).findFirst();
			return op.orElse(null);
		}
		
	}
}