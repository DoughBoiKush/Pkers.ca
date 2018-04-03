package org.osps.model.content.herbsack;

import java.util.LinkedList;
import java.util.List;

import org.osps.model.content.PlayerContent;
import org.osps.model.items.pouch.HerbSackDefinition;
import org.osps.model.players.Player;

public class HerbSack extends PlayerContent {

	//private final int[] herbAmount = new int[HerbSackDefinition.getDefinitionsLength()];
	
	public HerbSack(Player player) {
		super(player);
	}
	
	/*
	public void fill() {
		int herbsAdded = 0;
		for (int itemId : getPlayer().playerItems) {
			itemId = itemId - 1;
			
			HerbSackDefinition def = HerbSackDefinition.getByItemId(itemId);
			if (def == null) {
				continue;
			}
			
			int herbId = def.ordinal();
			if (herbAmount[herbId] >= SACK_CAPACITY) {
				getPlayer().sendMessage("Your sack cannot contain more than 30 of " + def.getName() + ".");
				continue;
			}
			
			herbAmount[def.ordinal()] += 1;
			getPlayer().getItems().deleteItem2(itemId, 1);
			herbsAdded++;
		}
		if (herbsAdded > 0) {
			getPlayer().sendMessage(herbsAdded + " " + (herbsAdded > 1 ? "herbs have" : "herb has") + " been added to the herb sack.");
		}
	}
	
	public void check() {
		List<String> messages = new LinkedList<>();
		
		String message = "";
		for (int i = 0; i < herbAmount.length; i++) {
			if (herbAmount[i] <= 0) {
				continue;
			}
			
			if (!message.isEmpty()) {
				message += ", ";
			}
			
			message += herbAmount[i] + " x " + HerbSackDefinition.getByIndex(i).getName();
			
			if (message.length() > 50 || i == herbAmount.length - 1) {
				messages.add(message + ".");
				message = "";
			}
		}
		
		if (!message.isEmpty()) {
			messages.add(message + ".");
		}
		
		if (messages.isEmpty()) {
			getPlayer().sendMessage("Your herb sack is empty.");
			return;
		}
		
		getPlayer().sendMessage("Your herb sack contains:");
		for (String msg : messages) {
			getPlayer().sendMessage(msg);
		}
	}
	
	public void empty() {
		for (int i = 0; i < herbAmount.length; i++) {
			if (herbAmount[i] <= 0) {
				continue;
			}
			
			HerbSackDefinition def = HerbSackDefinition.getByIndex(i);
			
			int amount = herbAmount[i];
			int freeSlots = getPlayer().getItems().freeSlots();
			
			if (freeSlots == 0) {
				break;
			}
			
			if (freeSlots < herbAmount[i] ) {
				amount = freeSlots;
			}
			
			getPlayer().getItems().addItem(def.getItemId(), amount);
			herbAmount[i] -= amount;
		}
	}
	
	public int[] getHerbAmount() {
		return herbAmount;
	}
	
	public static final int ITEM_ID = 13226;
	private static final int SACK_CAPACITY = 30;*/
	
}
