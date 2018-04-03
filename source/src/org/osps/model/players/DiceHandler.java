package org.osps.model.players;

public class DiceHandler {

	public static final int ROLL_TIMER = 1000, DICE_BAG = 15084;

	interface Data {
		public int diceId();

		public int diceSize();

		public int diceGfx();
	}

	enum Dice implements Data {
		DICE_UP_TO_100(
				15098, 100, 2075);

		private int id, sides, gfx;

		Dice(int id, int sides, int gfx) {
			this.id = id;
			this.sides = sides;
			this.gfx = gfx;
		}

		@Override
		public int diceId() {
			return id;
		}

		@Override
		public int diceSize() {
			return sides;
		}

		@Override
		public int diceGfx() {
			return gfx;
		}
	}

	/**
	 * Handles the rolling of the dice to a player.
	 * 
	 * @param c
	 *            The player.
	 * @param roll
	 *            What the player rolled on the dice.
	 * @param item
	 *            The id of the dice.
	 */
	public static void selfRoll(Player c, int roll, int item) {
		c.sendMessage("You rolled @red@" + roll + "@bla@ on the "
				+ c.getItems().getItemName(item) + ".");
	}

	/**
	 * Handles selecting the dice
	 * 
	 * @param c
	 *            The player.
	 * @param item
	 *            The dice id.
	 * @return Whether or not a dice were selected.
	 */
	public static boolean selectDice(Player c, int item) {
		for (Dice d : Dice.values()) {
			if (item == d.diceId() || item == DICE_BAG) {
				c.getDH().sendOption(
						"Next Page");
				c.diceItem = item;
				return true;
			}
		}
		return false;
	}

	/**
	 * Handles all the clicking for the dice.
	 * 
	 * @param c
	 *            The player.
	 * @param actionButtonId
	 *            Action button id of what is clicked.
	 * @return Whether or not a click was handled.
	 */
	public static boolean handleClick(Player c, int actionButtonId) {
		int[][] dice = {{ Dice.DICE_UP_TO_100.diceId() }};
		int DICE = 0;
		if (actionButtonId - 9190 >= 0 && actionButtonId - 9190 <= 5) {
			if (c.page == 0) {
				c.getPA().removeAllWindows();
				if (actionButtonId - 9190 <= 3) {
					if (c.getItems().playerHasItem(c.diceItem, 1)) {
						c.getItems().deleteItem(c.diceItem, 1);
						c.getItems().addItem(dice[actionButtonId - 9190][DICE],
								1);
					}
				} else {
					c.getDH()
							.sendOption2(
									c.getItems().getItemName(
											Dice.DICE_UP_TO_100.diceId()),
									"Return");
					c.page = 1;
				}
			} else if (c.page == 1) {
				c.getPA().removeAllWindows();
				if (actionButtonId - 9190 <= 3) {
					if (c.getItems().playerHasItem(c.diceItem, 1)) {
						c.getItems().deleteItem(c.diceItem, 1);
						c.getItems().addItem(dice[actionButtonId - 9186][DICE],
								1);
					}
				} else {
					c.getPA().closeAllWindows();
				}
				c.page = 0;
			}
			return true;
		}
		return false;
	}

}