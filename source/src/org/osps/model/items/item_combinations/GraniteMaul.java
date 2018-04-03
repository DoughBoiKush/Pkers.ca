package org.osps.model.items.item_combinations;

import java.util.List;
import java.util.Optional;

import org.osps.model.items.GameItem;
import org.osps.model.items.ItemCombination;
import org.osps.model.players.Player;

public class GraniteMaul extends ItemCombination {

	public GraniteMaul(Optional<int[]> skillRequirements, GameItem outcome, Optional<List<GameItem>> revertedItems, GameItem[] items) {
		super(skillRequirements, outcome, revertedItems, items);
	}
	
	@Override
	public void combine(Player player) {
		super.items.forEach(item -> player.getItems().deleteItem2(item.getId(), item.getAmount()));
		player.getItems().addItem(super.outcome.getId(), super.outcome.getAmount());
		player.getDH().sendStatement("You combined the items and created the Granite maul (or).");
		player.setCurrentCombination(Optional.empty());
		player.nextChat = -1;
	}
	
	@Override
	public void showDialogue(Player player) {
		player.getDH().sendStatement("The Granite maul (or) is untradeable.", 
				"You can revert this but you will lose the clamp.");
	}

}
