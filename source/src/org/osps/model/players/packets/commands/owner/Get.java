package org.osps.model.players.packets.commands.owner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Gives the player a specified game item.
 * @author Chris
 * @date Aug 24, 2015 12:20:00 AM
 *
 */
public class Get implements Command {
	
	@Override
	public void execute(Player player, String input) {
		try {
		String[] args = input.split(" ");
		String regex = args[0].replaceAll("_", " ").toLowerCase();
		List<ItemDefinition> list = new Gson().fromJson(FileUtils.readFileToString(new File("./Data/json/item_definitions.json")),new TypeToken<List<ItemDefinition>>() { }.getType());
		Optional<ItemDefinition> item = list.stream().filter(i -> i.getName().toLowerCase() == regex).findFirst();
		if (item.isPresent()) {
			final ItemDefinition def = item.get();
			int amount = Integer.parseInt(args[1]);
			player.getItems().addItem(def.getName() == "coins" ? 995 : def.getId(), amount);
		} else {
			player.sendMessage("Uh oh! That was quite ineffective. It seems you've tried to spawn " + regex + ".");
		}
		} catch (IOException ex) {
			System.err.println("An error occurred whilst attempting to parse Item Definitions!");
		}
	}
	

}
