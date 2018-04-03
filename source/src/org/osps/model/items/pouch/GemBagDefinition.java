package org.osps.model.items.pouch;

public enum GemBagDefinition implements ItemPouchDefinition {
	
	UNCUT_SAPPHIRE(1623),
	UNCUT_EMERALD(1621),
	UNCUT_RUBY(1619),
	UNCUT_DIAMOND(1617),
	UNCUT_DRAGONSTONE(1631);

	private final int itemId;
	
	private GemBagDefinition(int itemId) {
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
		name = name.replace("UNCUT_", "");
		name = name.replace('_', ' ');
		name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
		return name;
	}

	public static final int ITEM_ID = 12020;
	public static final GemBagDefinition[] VALUES = values();
	
}