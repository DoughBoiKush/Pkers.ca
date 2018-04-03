package org.osps.model.items.pouch;

import java.util.LinkedList;
import java.util.List;

import org.osps.model.content.PlayerContent;
import org.osps.model.players.Player;

public class ItemPouch extends PlayerContent {

	private final String name;
	private final int capacity;
	
	private final int[] itemAmounts;
	private final ItemPouchDefinition[] definitions;
	
	public ItemPouch(Player player, String name, int capacity, ItemPouchDefinition[] definitions) {
		super(player);
		
		this.name = name;
		this.capacity = capacity;
		
		this.itemAmounts = new int[definitions.length];
		this.definitions = definitions;
	}

	public int[] getItemAmounts() {
		return itemAmounts;
	}
	
	public void fill() {
		int itemsAdded = 0;
		for (int itemId : getPlayer().playerItems) {
			itemId = itemId - 1;
			
			ItemPouchDefinition def = findDefinition(itemId);
			if (def == null) {
				continue;
			}
			
			int index = def.getIndex();
			if (itemAmounts[index] >= capacity) {
				getPlayer().sendMessage("Your " + name + " cannot contain more than " + capacity + " of " + def.getName() + ".");
				continue;
			}
			
			itemAmounts[index] += 1;
			getPlayer().getItems().deleteItem2(itemId, 1);
			itemsAdded++;
		}
		if (itemsAdded > 0) {
			getPlayer().sendMessage(itemsAdded + " " + (itemsAdded > 1 ? "items have" : "item has") + " been added to your " + name + ".");
		}
	}
	
	public void check() {
		List<String> messages = new LinkedList<>();
		
		String message = "";
		for (int i = 0; i < itemAmounts.length; i++) {
			if (itemAmounts[i] <= 0) {
				continue;
			}
			
			if (!message.isEmpty()) {
				message += ", ";
			}
			
			message += itemAmounts[i] + " x " + definitions[i].getName();
			
			if (message.length() > 50 || i == itemAmounts.length - 1) {
				messages.add(message + ".");
				message = "";
			}
		}
		
		if (!message.isEmpty()) {
			messages.add(message + ".");
		}
		
		if (messages.isEmpty()) {
			getPlayer().sendMessage("Your " + name + " is empty.");
			return;
		}
		
		getPlayer().sendMessage("Your " + name + " contains:");
		for (String msg : messages) {
			getPlayer().sendMessage(msg);
		}
	}
	
	public void empty() {
		for (int i = 0; i < itemAmounts.length; i++) {
			if (itemAmounts[i] <= 0) {
				continue;
			}
			
			ItemPouchDefinition def = definitions[i];
			
			int amount = itemAmounts[i];
			int freeSlots = getPlayer().getItems().freeSlots();
			
			if (freeSlots == 0) {
				break;
			}
			
			if (freeSlots < itemAmounts[i] ) {
				amount = freeSlots;
			}
			
			getPlayer().getItems().addItem(def.getItemId(), amount);
			itemAmounts[i] -= amount;
		}
	}
	
	private ItemPouchDefinition findDefinition(int itemId) {
		for (ItemPouchDefinition def : definitions) {
			if (def.getItemId() != itemId) {
				continue;
			}
			return def;
		}
		return null;
	}
	
}