package org.osps.model.players.skills.herblore;

public enum Potions {
	
	ATTACK_POTION(1,2,3,4),
	ANTIPOISON(1,2,3,4),
	STRENGTH_POTION(1,2,3,4),
	RESTORE_POTION(1,2,3,4),
	ENERGY_POTION(1,2,3,4),
	DEFENCE_POTION(1,2,3,4),
	GUTHIX_BALANCE(1,2,3,4),
	COMBAT_POTION(1,2,3,4),
	PRAYER_POTION(1,2,3,4),
	SUPER_ATTACK(1,2,3,4),
	SUPER_ANTIPOISON(1,2,3,4),
	SUPER_ENERGY(1,2,3,4),
	SUPER_STRENGTH(1,2,3,4),
	SUPER_RESTORE(1,2,3,4),
	SUPER_DEFENCE(1,2,3,4),
	ANTIDOTE_PLUS(1,2,3,4),
	ANTIFIRE(1,2,3,4),
	RANGING(1,2,3,4),
	MAGIC(1,2,3,4),
	ZAMORAK_BREW(1,2,3,4),
	ANTIDOTE_PLUS_PLUS(1,2,3,4),
	SARADOMIN_BREW(1,2,3,4),
	SUPER_COMBAT_POTION(1,2,3,4),
	ANTI_VENOM(1,2,3,4),
	ANTI_VENOM_PLUS(1,2,3,4);
	
	Potions(int fullId, int threeQuartersId, int halfId, int quarterId) {
		this.quarterId = quarterId;
		this.halfId = halfId;
		this.threeQuartersId = threeQuartersId;
		this.fullId = fullId;
	}
	
	private int quarterId, halfId, threeQuartersId, fullId;
	
	public int getQuarterId() {
		return this.quarterId;
	}
	
	public int getHalfId() {
		return this.halfId;
	}
	
	public int getThreeQuartersId() {
		return this.threeQuartersId;
	}
	
	public int getFullId() {
		return this.fullId;
	}


}