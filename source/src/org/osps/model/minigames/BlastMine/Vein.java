package org.osps.model.minigames.BlastMine;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.osps.model.players.skills.mining.Mineral;

/**
 * An enumeration of ore vein information.
 * 
 * @author Jason MacKeigan
 * @date Feb 18, 2015, 5:14:50 PM
 */
public enum Vein {
	SALTPETRE(new int[] {27433, 27434, 27435, 27436}, 13421, 1, 5, 5, 15),
	VOLCANIC_SULPHUR(new int[] {27433, 27434, 27435, 27436}, 13421, 1, 5, 5, 15),
	JUNIPER_CARCOAL(new int[] {27433, 27434, 27435, 27436}, 13421, 1, 5, 5, 15);
	/**
	 * An array of object ids that are associated with the mineral obtained from them
	 */
	private final int[] objectIds;
	
	/**
	 * The mineral extracted from the object
	 */
	private final int mineral;
	
	/**
	 * The level required to mine this ore
	 */
	private final int level;
	
	/**
	 * The probability that the mineral will deplete
	 */
	private final int depletionProbability;
	
	/**
	 * The amount of cycles that need to pass before the mineral is extractable
	 */
	private final int respawnRate;
	
	/**
	 * The default amount of cycles it takes to extract ore from a vein
	 */
	private final int extractionRate;
	/**
	 * The identification value of the object with no mineral remaining after extraction
	 */
	public static final int EMPTY_VEIN = 0;
	/**
	 * Constructs a new mineral
	 * @param objectIds		the objects that the mineral can be extracted from
	 * @param mineral		the item identification value of the mineral extracted
	 * @param level			the level required to extract minerals from the object(s)		
	 * @param experience	the experience gain after extraction
	 */
	private Vein(int[] objectIds, int mineral, int level, int depletionProbability,
			int respawnRate, int extractionRate) {
		this.objectIds = objectIds;
		this.mineral = mineral;
		this.level = level;
		this.depletionProbability = depletionProbability;
		this.respawnRate = respawnRate;
		this.extractionRate = extractionRate;
	}
	
	/**
	 * The array of objectId values associated with this mineral
	 * @return	the array of object id's 
	 */
	public int[] getObjectIds() {
		return objectIds;
	}
	
	/**
	 * The mineral extracted from an object
	 * @return	the identification value of the mineral received
	 */
	public int getMineral() {
		return mineral;
	}
	
	/**
	 * The level required to extract minerals
	 * @return	the level required
	 */
	public int getLevel() {
		return level;
	}
	
	
	/**
	 * The rate, in cycles, the mineral respawns at
	 * @return	the rate at which the mineral respawns
	 */
	public int getRespawnRate() {
		return respawnRate;
	}
	
	/**
	 * Every mineral has a difference rate at which they deplate. Some minerals
	 * faster then others, and some minerals instantly after being extracted from.
	 * @return	the probability of depletion. When the probability is 0 the chance
	 * of depletion is 1:1.
	 */
	public int getDepletionProbability() {
		return depletionProbability;
	}
	
	/**
	 * The default amount of cycles it takes for a single ore to be extracted
	 * @return	the default extraction rate
	 */
	public int getExtractionRate() {
		return extractionRate;
	}
	
	
	/**
	 * An unmodification set of {@link Mineral} objects that will be used as a constant
	 * for obtaining information about certain minerals.
	 */
	private static final Set<Vein> MINERALS = Collections.unmodifiableSet(EnumSet.allOf(Vein.class));
	
	/**
	 * Retrieves the {@link Mineral} object with the same objectId 
	 * as the parameter passed.
	 * @param objectId	the object id of the mineral
	 * @return	the mineral object with the corresponding object id
	 */
	public static Vein forObjectId(int objectId) {
		for (Vein mineral : MINERALS) {
			for (int objId : mineral.objectIds) {
				if (objId == objectId) {
					return mineral;
				}
			}
		}
		return null;
	}

}
