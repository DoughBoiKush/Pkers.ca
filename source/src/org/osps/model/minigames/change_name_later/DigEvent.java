package org.osps.model.minigames.change_name_later;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.Location;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Misc;

public class DigEvent {
	
	private static final Location[] POSSIBLE_LOCATIONS = {new Location(3135, 3528, 0)
			
	};
	
	private static final String[] ANNOUNCEMENTS = {"By the tree in 1 wilderness in edgeville multi."

	};
	
	private static final int[][] REWARDS = {{4151, 1}
	
	};
	
	private static boolean found;
	private static Location current_location;
	
	private static final CycleEvent GAME_TASK = new CycleEvent() {
		@Override
		public void execute(CycleEventContainer container) {
			if (found)
				found = false;
			final int index = Misc.random(POSSIBLE_LOCATIONS.length - 1);
			current_location = POSSIBLE_LOCATIONS[index];
			globalMessage("@blu@The dig for rewards event has begun the current location is.");
			globalMessage("<col=255>"+ANNOUNCEMENTS[index]+"</col>");
		}
	};
	
	public static void init() {
		CycleEventHandler.getSingleton().addEvent(0, GAME_TASK, 1000);
		
	}
	
	public static void globalMessage(String q) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				final Player c2 = (Player)PlayerHandler.players[j];
				c2.sendMessage(q);
			}
		}
	}
	
	public static void checkDigSpot(final Player player) {
		if (current_location == null)
			return;
		if (current_location.getX() == player.getX() && current_location.getY() == player.getY() && current_location.getZ() == player.getHeight()) {
			if (found) {
				player.sendMessage("This digging location has already been found the next round begins soon!");
				return;
			}
			player.animation(831);
			performAnimation(player);
			found = true;
			final int index = Misc.random(REWARDS.length - 1);
			player.getItems().addItem(REWARDS[index][0], REWARDS[index][1]);
			globalMessage("<col=255>"+player.playerName+" has just found the dig location and received a reward!</col>");
		}
	}
	
	public static final void performAnimation(final Player player) {
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {	
			@Override
			public void execute(CycleEventContainer container) {	
					container.stop();	
			}
			@Override
			public void stop() {
				player.stopAnimation();
			}
		}, 1);
	}
}
	