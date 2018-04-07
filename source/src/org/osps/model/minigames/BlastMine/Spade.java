package org.osps.model.minigames.BlastMine;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.osps.model.players.Player;
import org.osps.model.players.skills.Skill;
import org.osps.model.players.skills.mining.Pickaxe;
/**
 * An enumeration of mining pickaxes.
 * 
 * @author Jason MacKeigan
 * @date Feb 18, 2015, 5:47:35 PM
 */
public enum Spade {
	SHOVEL(0, 952, 1, 831, 0);
	
	/**
	 * The priority of the pickaxe. The higher the value, the higher the priority.
	 * This serves as a replacement to the {@link Enum.ordinal()} function.
	 */
	private final int priority;
	
	/**
	 * The item identification value of the pickaxe
	 */
	private final int itemId;
	
	/**
	 * The level required to operate the pickaxe
	 */
	private final int level;
	
	/**
	 * The animation displayed when the pickaxe is being operated
	 */
	private final int animation;
	
	/**
	 * The amount of cycles operating this pickaxe will reduce the time by 
	 */
	private final int extractionReduction;
	
	/**
	 * Constructs a new {@link Pickaxe} object
	 * @param itemId	the item id value of the pickaxe
	 * @param level		the level required to operate
	 * @param animation	the animation displayed when used
	 */
	private Spade(int priority, int itemId, int level, int animation, int extractionReduction) {
		this.priority = priority;
		this.itemId = itemId;
		this.level = level;
		this.animation = animation;
		this.extractionReduction = extractionReduction;
	}
	
	/**
	 * The item identification value of the pickaxe
	 * @return	the pickaxe id
	 */
	public int getItemId() {
		return itemId;
	}
	
	/**
	 * The level required to operate the pickaxe
	 * @return	the level to operate the pickaxe is always above 1 and cannot exceed 61
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * The animation displayed when the Shovel is in use
	 * @return	the animation displayed
	 */
	public int getAnimation() {
		return animation;
	}
	
	/**
	 * The amount of reduction this Shovel effects the extraction process
	 * @return	the reduction
	 */
	public int getExtractionReduction() {
		return extractionReduction;
	}
	
	/**
	 * The level of priority the Shovel has.
	 * @return	the level of priority
	 */
	public int getPriority() {
		return priority;
	}
	
	/**
	 * A set of all Shovels that will act as as constant
	 */
	private static final Set<Spade> SPADE = Collections.unmodifiableSet(EnumSet.allOf(Spade.class));
	
	/**
	 * Attempts to retrieve the best Shovel the player has on their person.
	 * @param player	the	player we're trying to get the best Shovel of
	 * 
	 * @return the best Shovel the person has will be returned, otherwise 
	 * null to ensure the player has no Shovel;
	 */
	public static Spade getBestShovel(Player player) {
		Spade shovel = null;
		for (Spade pick : SPADE) {
			if (player.getItems().playerHasItem(pick.itemId) ||
					player.getItems().isWearingItem(pick.itemId)) {
				if (player.playerLevel[Skill.MINING.getId()]
						>= pick.level) {
					if (shovel == null || pick.priority > shovel.priority) {
						shovel = pick;
					}
				}
			}
		}
		return shovel;
	}

}
