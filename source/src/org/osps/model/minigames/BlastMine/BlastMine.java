package org.osps.model.minigames.BlastMine;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.skills.Skill;
import org.osps.model.players.skills.mining.Mining;
import org.osps.util.Location3D;
import org.osps.util.Misc;

public class BlastMine {
	
	/**
	 * The player that this {@link Mining} object is created for
	 */
	private final Player c;
	
	/**
	 * Constructs a new class for a singular player
	 * @param player	the player this class is being created for
	 */
	public BlastMine(Player c) {
		this.c = c;
	}
	
	
	public static int SPADE = 952;
	public static int JUNIPER_CHARCOAL = 13570;
	public static int VOLCANIC_SULFUR = 13571;
	public static int DYNAMITE_POT = 13572;
	public static int DYNAMITE = 13573;
	public static int BLASTED_ORE = 13575;
	public static int SALTPETRE = 13421;
	public static final int ANIMATION = 881;
	public static final int GFX = 1295;
	public static final int SPADE_ANIMATION = 831;
	private static final int MINIMUM_EXTRACTION_TIME = 2;
	

	public static void makeDynamite(Player c) {
		if (c.getItems().playerHasItem(DYNAMITE_POT, 1) && c.getItems().playerHasItem(VOLCANIC_SULFUR, 1) /*&& c.getItems().playerHasItem(JUNIPER_CHARCOAL, 1) && c.getItems().playerHasItem(SALTPETRE,1)*/) {
			if (c.playerLevel[12] < 30) {
				c.sendMessage("You need a crafting level of 30 to make dynamite.");
				return;
			}
			c.getItems().deleteItem(DYNAMITE_POT, 1);
			c.getItems().deleteItem(VOLCANIC_SULFUR, 1);
/*			c.getItems().deleteItem(JUNIPER_CHARCOAL, 1);
			c.getItems().deleteItem(SALTPETRE, 1);*/
			c.getItems().addItem(DYNAMITE, 1);
			c.getPA().addSkillXP(1800, 12);
		}
	}
	
	public static void forcedChat(Player c) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				c.forceChat("Ouch that hurt!");
				c.forcedChatUpdateRequired = true;
				c.updateRequired = true;
			}
		}
	}
	
	public static void blowAway(Player c) {
		if (c.getItems().playerHasItem(DYNAMITE)) {
		c.getItems().deleteItem(DYNAMITE, 1);
		c.animation(ANIMATION);
		c.gfx100(GFX);
		Server.itemHandler.createGroundItem(c, BLASTED_ORE, c.getX(), c.getY(), c.heightLevel, 1, c.index);
		forcedChat(c);
		c.sendMessage("<col=255>You stick the dynamite in the rock but it accidently explodes!");
		c.getPA().addSkillXP(1500, 11);
		} else {
			c.sendMessage("<col=255>I need dynamite to do this!");
			return;
		}
	}
	
	public static void redeem(Player c) {
		if (c.getItems().playerHasItem(BLASTED_ORE)) {
		c.getItems().deleteItem(BLASTED_ORE, 1);
		c.animation(ANIMATION);
		c.sendMessage("<col=255>You deposit the ore in the ore sack and recieve a blast point!.. ");
		c.blastPoints += 1;
		int chance = Misc.random(100);
		if (chance <= 70) {
				Server.itemHandler.createGroundItem(c, 2, c.getX(), c.getY(), c.heightLevel, 25, c.index);
				c.sendMessage("<col=255>Aww, you got 3 cannonballs! Its on the ground!");
			} else if (chance >= 71 && chance <= 95) {
				Server.itemHandler.createGroundItem(c, 2, c.getX(), c.getY(), c.heightLevel, 100, c.index);
				c.sendMessage("<col=255>Close!, you got 5 cannonballs! Its on the ground!");
			} else if (chance >= 96) {
				Server.itemHandler.createGroundItem(c, 2, c.getX(), c.getY(), c.heightLevel, 200, c.index);
				c.sendMessage("<col=255>Epic!, you got 10 cannonballs! Its on the ground!");
			}
		}
	}
	public void dig(int objectId, Location3D location) {
		Vein mineral = Vein.forObjectId(objectId);
		if (mineral == null) {
			return;
		}
		Spade spade = Spade.getBestShovel(c);
		if (spade == null) {
			c.sendMessage("You need a spade to dig this vein.");
			return;
		}
		if (c.getItems().freeSlots() == 0) {
			c.getDH().sendStatement("You have no more free slots.");
			return;
		}
		int levelReduction = (int) Math.floor(c.playerLevel[Skill.MINING.getId()] / 10);
		int pickaxeReduction = spade.getExtractionReduction();
		int extractionTime = mineral.getExtractionRate() - (levelReduction + pickaxeReduction);
		if (extractionTime < MINIMUM_EXTRACTION_TIME) {
			extractionTime = MINIMUM_EXTRACTION_TIME;
		}
		c.sendMessage("You start digging at the Saltpetre.");
		c.animation(spade.getAnimation());
		c.face(location.getX(), location.getY());
		c.getSkilling().stop();
		c.getSkilling().add(new VeinEvent(c, objectId, location, mineral, spade), extractionTime);
	}

}