package org.osps.model.players.packets.commands.all;

import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

public class Getid implements Command {

	@Override
	public void execute(Player c, String input) {
		if (input.length() < 3) {
			c.sendMessage("[@red@Item Search@bla@]You must give at least 3 letters of input to narrow down the item.");
			return;
		}
		int results = 0;
		c.sendMessage("[@red@Item Search@bla@] Searching for @blu@" + input);
		for (ItemDefinition it : ItemDefinition.DEFINITIONS) {
			if (results == 100) {
				c.sendMessage("[@red@Item Search@bla@]Too many results! Please refine your search.");
				return;
			}
			if (it == null || it.getName() == null || it.getName().replaceAll(" ", "").equals(""))
				continue;
			if (!it.isNoted())
				if (it.getName().replace("_", " ").toLowerCase().contains(input.toLowerCase())) {
					c.sendMessage("[@red@" + it.getName().replace("_", " ") + "@bla@] - [@blu@" + it.getId()+ "@bla@]");
					results++;
				}
		}
		c.sendMessage("[@red@Item Search@bla@] Search returned @blu@" +results+" @bla@results.");
	}
}
