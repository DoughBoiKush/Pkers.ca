package org.osps.model.minigames.lottery;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Misc;

public class TriLottery {

	private final List<String> entries = new LinkedList<String>();
	private final List<String> unclaimedNames = new LinkedList<String>();
	private final Map<String, Integer> unclaimedJackpots = new HashMap<String, Integer>();
	//private final long lotteryTime = 3600000;
	private final long lotteryTime = 600;
	private long lastLotteryTime = 0;
	public int jackpot = 1;
	public static String timeLeft;

	public TriLottery() {
		init();
	}

	public void init() {
		CycleEventHandler.getSingleton().addEvent(null, new CycleEvent() {
			public void execute(final CycleEventContainer b) {
				timeLeft = formatTime(getLotteryTime()
						- (System.currentTimeMillis() - lastLotteryTime));
				final int rand = Misc.random(600);
				if (System.currentTimeMillis() - lastLotteryTime >= getLotteryTime()) {
					msgAll("[Lottery] - The lottery has just been drawn.");
					if (getEntries().size() > 0)
						determineWinners();
					msgAll("[Lottery] - The next lottery will be drawn 1 hour from now.");
					lastLotteryTime = System.currentTimeMillis();
				} else {
					if (jackpot > 199) {
						msgAll("[Lottery] - The lottery has just reached its max and been drawn.");
						if (getEntries().size() > 0)
							determineWinners();
						msgAll("[Lottery] - The next lottery will be drawn 1 hour from now.");
						lastLotteryTime = System.currentTimeMillis();
					}
				}
			}
		}, 1000);
	}
	
	private static void msgAll(String msg) {
        Arrays.stream(PlayerHandler.players).filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(msg));
    }
	
	public void determineWinners() {
		boolean foundWinner = false;
		final int pickWinner = Misc.random(getEntries().size() - 1);
		final String winner = getEntries().get(pickWinner);
		if (winner != null) {
			msgAll("[Lottery] - The winner is: "
					+ Misc.optimizeText(winner) + ", congratualations!");
			for (final Player p : Server.playerHandler.players) {
				if (p == null)
					continue;
				if (p.getUsername1().equalsIgnoreCase(winner)) {
					foundWinner = true;
					if (jackpot < 2 && jackpot > 0) {
						p.pkp += jackpot * 1000 * 2;
						p.sendMessage("Congratualations, you have won the lottery, " + jackpot * 2000 + " PKP was added to your account!");
					} else if (jackpot > 1) {
						p.pkp += jackpot * 1000;
						p.sendMessage("Congratualations, you have won the lottery, "
										+ (jackpot * 1000)
										+ " PKP was added to your account!");
					}
					getEntries().clear();
					jackpot = 1;
					break;
				}
			}
			if (!foundWinner) {
				unclaimedNames.add(winner);
				unclaimedJackpots.put(winner, jackpot);
				getEntries().clear();
				jackpot = 1;
			}
		}
	}

	public void checkUnclaimedWinners(final Player c) {
		for (final String s : unclaimedNames) {
			if (s.equalsIgnoreCase(c.playerName)) {
				final int pot = unclaimedJackpots.get(s);
				c.pkp += pot * 1000;
				c.sendMessage("Congratualations, you have won the lottery, "
								+ (pot * 1000)
								+ " PKP was added to your account!");
				unclaimedJackpots.remove(s);
				unclaimedNames.remove(s);
			}
		}
	}
	
	public boolean alreadyEntered(final String name) {
		for (final String n : getEntries()) {
			if (n.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}

	public void enterLottery(final String name) {
		getEntries().add(name);
		jackpot++;
		if (jackpot > 199)
			jackpot = 200;
	}

	public static String formatTime(final long millis) {
		int mins = 0;
		int hrs = 0;
		int secs = (int) millis / 1000;
		mins = (secs / 60);
		hrs = (mins / 60);
		return hrs + "H:" + (mins - (hrs * 60)) + "M:" + (secs - (mins * 60))
				+ "S";
	}

	public List<String> getEntries() {
		return entries;
	}

	public List<String> getUnclaimed() {
		return unclaimedNames;
	}

	public long getLotteryTime() {
		return lotteryTime;
	}

	public Map<String, Integer> getUnclaimedJackpots() {
		return unclaimedJackpots;
	}
	
}
