package org.osps.model.items.pouch;

public enum HerbSackDefinition implements ItemPouchDefinition {

	GRIMY_GUAM_LEAF(199),
	GRIMY_MARRENTILL(201),
	GRIMY_TARROMIN(203),
	GRIMY_HARRALANDER(205),
	GRIMY_RANARR_WEED(207),
	GRIMY_TOADFLAX(3049),
	GRIMY_IRIT_LEAF(209),
	GRIMY_AVANTOE(211),
	GRIMY_KWUARM(213),
	GRIMY_SNAPDRAGON(3051),
	GRIMY_CADANTINE(215),
	GRIMY_LANTADYME(2485),
	GRIMY_DWARF_WEED(217),
	GRIMY_TORSTOL(219);
	
	private final int itemId;
	
	private HerbSackDefinition(int itemId) {
		this.itemId = itemId;
	}
	
	@Override
	public int getIndex() {
		return ordinal();
	}
	
	@Override
	public int getItemId() {
		return itemId;
	}
	
	@Override
	public String getName() {
		String name = name();
		name = name.toLowerCase();
		name = name.replace("GRIMY_", "");
		name = name.replace('_', ' ');
		name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
		return name;
	}
	
	public static final int ITEM_ID = 13226;
	public static final HerbSackDefinition[] VALUES = values();
	
}