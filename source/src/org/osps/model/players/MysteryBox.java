package org.osps.model.players;

import java.util.Objects;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.util.Misc;
/**
 * Revamped a simple means of receiving a random item based on chance.
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class MysteryBox extends CycleEvent{

	private static final int MYSTERY_BOX = 6199;
	
	public static int Common [] = 
	{3140, 4087, 11335, 1187, 6737, 6731, 6733, 6735, 2577, 2581, 6916, 6918, 6920, 6922, 6924}; // Add more item Id's
	
	public static int Uncommon [] = 
	{4745, 4747, 4749, 4751, 4732, 4734, 4736, 4738, 4708, 4710, 4712, 4714, 4753, 4755, 4757, 4759, 4716, 4718, 4720, 4722, 12414, 12415, 12417, 12418, 6570, 12954, 4151}; // Add more item Id's
	
	public static int Rare [] = 
	{10330, 10332, 10334, 10336, 10338, 10340, 10342, 10344, 10346, 10348, 10350, 10352}; // Add more item Id's
	
	public static int Very_Rare [] = 
	{12422, 12426, 12424, 12825, 12821}; // Add more item Id's
	
	public static int Ultra_Rare [] = 
	{13655, 12600}; // Add more item Id's

	/**
	 * The player object that will be triggering this event
	 */
	private Player c;
	
	/**
	 * Constructs a new myster box to handle item receiving for this player
	 * and this player alone
	 * @param player the player
	 */
	public MysteryBox(Player player) {
		this.c = player;
	}

			
	public void execute(CycleEventContainer container) {
		if (c.disconnected || Objects.isNull(c)) {
			container.stop();
			return;
		}
		int bm = Misc.random(1, 5);
		int random = Misc.random(300);
		c.pkp += bm;
					if (random <= 198) {
						c.getItems().addItem(Common[(int) (Math.random() * Common.length)], 1);
						c.sendMessage("You have recieved a @gre@common @bla@item and @blu@"+ bm +" @bla@PK Points.");
						container.stop();
					} else if (random >= 199 && random <= 287) {
						c.getItems().addItem(Uncommon[(int) (Math.random() * Uncommon.length)], 1);
						c.sendMessage("You have recieved an @yel@uncommon @bla@item and @blu@"+ bm +" @bla@PK Points.");
						container.stop();
					} else if (random <= 288 && random <= 296) {
						c.getItems().addItem(Rare[(int) (Math.random() * Rare.length)], 1);
						c.sendMessage("You have recieved a @red@rare @bla@item and @blu@"+ bm +" @bla@PK Points.");
						container.stop();
					} else if (random <= 297 && random <= 299) {
						c.getItems().addItem(Very_Rare[(int) (Math.random() * Very_Rare.length)], 1);
						c.sendMessage("You have recieved a @red@very rare @bla@item and @blu@"+ bm +" @bla@PK Points.");
						container.stop();
					} else if (random == 300) {
						c.getItems().addItem(Ultra_Rare[(int) (Math.random() * Ultra_Rare.length)], 1);
						c.sendMessage("You have recieved a @red@ultra rare @bla@item and @blu@"+ bm +" @bla@PK Points.");
						container.stop();
					}
				}
			
	
	public void open() {
		if (System.currentTimeMillis() - c.lastMysteryBox < 600 * 2) {
			return;
		}
		if (c.getItems().freeSlots() < 2) {
			c.sendMessage("You need atleast two free slots to open a mystery box.");
			return;
		}
		if (!c.getItems().playerHasItem(MYSTERY_BOX)) {
			c.sendMessage("You need a mystery box to do this.");
			return;
		}
		c.getItems().deleteItem(MYSTERY_BOX, 1);
		c.lastMysteryBox = System.currentTimeMillis();
		CycleEventHandler.getSingleton().stopEvents(this);
		CycleEventHandler.getSingleton().addEvent(this, this, 2);
		}
	}
	
