package org.osps.model.items.item_combinations;

import java.util.List;
import java.util.Optional;

import org.osps.model.items.GameItem;
import org.osps.model.items.ItemCombination;
import org.osps.model.players.Player;

public class SaradominsBlessedSword extends ItemCombination {

	public SaradominsBlessedSword(Optional<int[]> skillRequirements, GameItem outcome, Optional<List<GameItem>> revertedItems, GameItem[] items) {
		super(skillRequirements, outcome, revertedItems, items);
	}

	@Override
	public void combine(Player player) {
		items.forEach(item -> player.getItems().deleteItem2(item.getId(), item.getAmount()));
		player.getItems().addItem(outcome.getId(), outcome.getAmount());
		player.getDH().sendStatement("You combined the items and created Saradomin's blessed sword.");
		player.setCurrentCombination(Optional.empty());
		player.nextChat = -1;
	}
	
	@Override
	public void showDialogue(Player player) {
		player.getDH().sendStatement("Saradomon's blessed sword is untradable.",
				"You can revert this but the saradomin sword will be lost.");
	}

}
