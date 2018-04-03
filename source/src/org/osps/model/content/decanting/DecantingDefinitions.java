package org.osps.model.content.decanting;

import java.util.HashMap;
import java.util.Map;

public enum DecantingDefinitions {

	STRENGTH_POTION(119, 117, 115, 113), /**/
	ATTACK_POTION(125, 123, 121, 2428), /**/
	DEFENCE_POTION(137, 135, 133, 2432), /**/
	PRAYER_POTION(143, 141, 139, 2434), /**/
	SUPER_ATTACK_POTION(149, 147, 145, 2436), /**/
	SUPER_STRENGTH_POTION(161, 159, 157, 2440), /**/
	SUPER_DEFENCE_POTION(167, 165, 163, 2442), /**/
	RANGING_POTION(173, 171, 169, 2444), /**/
	ANTI_POISON_POTION(179, 177, 175, 2446), /**/
	SUPER_ANTI_POISON_POTION(185, 183, 181, 2448), /**/
	ANTIFIRE_POTION(2458, 2456, 2454, 2452), /**/
	SUPER_RESTORE_POTION(3030, 3028, 3026, 3024), /**/
	MAGIC_POTION(3046, 3044, 3042, 3040), /**/
	SARADOMIN_BREW_POTION(6691, 6689, 6687, 6685), /**/
	SANFEW_SERUM_POTION(10931, 10929, 10927, 10925), /**/
	OVERLOAD_POTION(11733, 11732, 11731, 11730), /**/
	SUPER_COMBAT_POTION(12701, 12699, 12697, 12695), /**/
	ANTI_VENOM_POTION(12911, 12909, 12907, 12905), /**/
	ANTI_VENOM_PLUS_POTION(12919, 12917, 12915, 12913), /**/
	;

	private final int maximumDoses;
	private final Map<Integer, Integer> doseToPotion, potionToDose;

	/**
	 * @param potionIds
	 *            the ids of the potions in ascending order based on their dose
	 *            value.
	 */
	private DecantingDefinitions(int... potionIds) {
		doseToPotion = new HashMap<>();
		potionToDose = new HashMap<>();
		for (int i = 0; i < potionIds.length; i++) {
			int dose = i + 1;
			doseToPotion.put(dose, potionIds[i]);
			potionToDose.put(potionIds[i], dose);
		}
		maximumDoses = potionIds.length;
	}

	public int getDoseByPotion(int potionId) {
		Integer dose = potionToDose.get(potionId);
		return dose == null ? -1 : dose;
	}

	public int getPotionByDose(int dose) {
		Integer potionId = doseToPotion.get(dose);
		return potionId == null ? -1 : potionId;
	}

	public int getMaximumDose() {
		return maximumDoses;
	}

	private static Map<Integer, DecantingDefinitions> definitions;

	public static DecantingDefinitions getByItemId(int itemId) {
		return definitions.get(itemId);
	}

	static {
		definitions = new HashMap<>();
		for (DecantingDefinitions def : values()) {
			for (int dose = 1; dose <= def.getMaximumDose(); dose++) {
				definitions.put(def.getPotionByDose(dose), def);
			}
		}
	}

}