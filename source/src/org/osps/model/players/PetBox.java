package org.osps.model.players;

import java.util.Arrays;
import java.util.Objects;

public class PetBox {

	private static final int PET_BOX = 7630;

	public static int rewards[] = { 12650, 12649, 12651, 12652, 12644, 12643, 11995,
			12653, 12655, 13178, 12646, 12921, 12939, 12940, 12654, 13179, 13177,
			12648, 13181, 12816, 13320, 13225, 13321, 13322, 13323, 13247 }; 

	/**
	 * The player object that will be triggering this event
	 */
	private Player c;

	/**
	 * Constructs a new myster box to handle item receiving for this player and
	 * this player alone
	 * 
	 * @param player
	 *            the player
	 */
	public PetBox(Player player) {
		this.c = player;
	}

	
	private static void msgAll(String msg) {
        Arrays.stream(PlayerHandler.players).filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(msg));
    }

	public void open() {
		if (System.currentTimeMillis() - c.lastMysteryBox < 600 * 2) {
			return;
		}
		if (c.getItems().freeSlots() < 2) {
			c.sendMessage("You need atleast two free slots to open a mystery box.");
			return;
		}
		if (!c.getItems().playerHasItem(PET_BOX)) {
			c.sendMessage("You need a pet box to do this.");
			return;
		}
		c.getItems().deleteItem(PET_BOX, 1);
		c.lastMysteryBox = System.currentTimeMillis();
		int reward = rewards[(int) (Math.random() * rewards.length)];
		c.getItems().addItem(reward, 1);
		msgAll(c.getCrown() + c.getYellTextColor() + c.playerName + "@bla@ has just received a @blu@" + c.getItems().getItemName(reward) + " @bla@from a Pet Box!");
	}
}
