package org.osps.model.npcs.drops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import org.osps.model.items.ItemDefinition;
import org.osps.model.npcs.NPCCacheDefinition;
import org.osps.model.players.Player;
import org.osps.util.Chance;

public class DropList {

	private static final ArrayList<Chance> chances = new ArrayList<>();

	static {
		for (final Chance chance : Chance.values())
			chances.add(chance);
		Collections.reverse(chances);
	}
	
	/**
	 * Sets text colour
	 * @param chance
	 * @return
	 */

	private static String getColour(Chance chance) {
		switch (chance) {
		case RARE:
			return "@red@";
		case VERY_RARE:
			return "@or3@";
		case EXTREMELY_RARE:
			return "@dre@";
		case UNCOMMON:
			return "@yel@";
		case COMMON:
			return "@gr1@";
		case VERY_UNCOMMON:
			return "@yel@";
		default:
			return "@gre@";

		}
	}
	
	/**
	 * Displays specific NPC Drops on a interface
	 * @param player
	 * @param query
	 * @return
	 */

	public static boolean displayNPCDrops(Player player, String query) {
		if (player == null || query.replaceAll(" ", "").equals(""))
			return false;
		query = query.substring(0, 1).toUpperCase() + query.substring(1);
		final ArrayList<Integer> npcIds = new ArrayList<>();
		for (int i = 0; i < NPCCacheDefinition.newTotalNpcs; i++) {
			final NPCCacheDefinition def = NPCCacheDefinition.forID(i);
			if (def == null || def.getName() == null || def.getName().replaceAll(" ", "").equals(""))
				continue;
			if (def.getName().toLowerCase().equalsIgnoreCase(query))
				npcIds.add(i);
		}
		if (npcIds.isEmpty()) {
			player.sendMessage("No drops were found for the query: " + query + ".");
			return false;
		}
		final ArrayList<NpcDrop> drops = new ArrayList<>();
		for (final int id : npcIds) {
			final NpcDropTable table = NpcDropManager.TABLES.get(id);
			if (table == null)
				continue;
			final String name = NPCCacheDefinition.forID(id).getName();
			if (name == null || name.replaceAll(" ", "").equals(""))
				continue;
			if (table.getUnique() == null || table.getUnique().length == 0)
				continue;
			for (int i = 0; i < table.getUnique().length; i++) {
				if (table.getUnique()[i] != null)
					drops.add(table.getUnique()[i]);
			}
		}
		if (drops.isEmpty()) {
			player.sendMessage("No drops were found for the query: " + query + ".");
			return false;
		}

		final int capacity = 8195;
		for (int i = 8144; i < capacity; i++) {
			player.getPA().sendFrame126("", i);
		}
		int frame = 8144;
		player.getPA().sendFrame126("<img=10>" + query + "<img=10>", frame++);
		final ArrayList<Key<Chance, ArrayList<NpcDrop>>> dropsToDisplay = new ArrayList<>();
		chances.stream().filter(Objects::nonNull).forEach(chance -> {
			final ArrayList<NpcDrop> values = new ArrayList<>();
			drops.stream().filter(Objects::nonNull).filter(drop -> drop.getChance().ordinal() == chance.ordinal())
					.forEach(values::add);
			if (!values.isEmpty()) {
				dropsToDisplay.add(new Key<Chance, ArrayList<NpcDrop>>(chance, values));
			}
		});
		frame += 2;

		for (final Key<Chance, ArrayList<NpcDrop>> value : dropsToDisplay) {
			final Chance chance = value.key;
			int range = chance.getDenominator() + 1 - chance.getNumerator();

			if (chance.ordinal() == Chance.VERY_RARE.ordinal()) {
				range = chance.getDenominator() - 269 - chance.getNumerator();
			}
			if (chance.ordinal() == Chance.EXTREMELY_RARE.ordinal()) {
				range = chance.getDenominator() - 334 - chance.getNumerator();
			}
			if (chance.ordinal() == Chance.RARE.ordinal()) {
				range = chance.getDenominator() - 195 - chance.getNumerator();
			}
			if (chance.ordinal() == Chance.ALWAYS.ordinal())
				range = 1;
			/**
			 * Remove what is below to get ACTUAL rarity rates (static)
			 */

			String colour = getColour(chance);

			player.getPA().sendFrame126("Rarity - " + colour.toString() + " " + chance.toString() + " @bla@[Ratio "
					+ colour.toString() + "1:" + range + "@bla@]", frame++);
			if (frame >= capacity) {
				break;
			}
			for (final NpcDrop drop : value.value) {
				final ItemDefinition defs = ItemDefinition.DEFINITIONS[drop.getId()];
				if (defs == null || defs.getName() == null || defs.getName().equalsIgnoreCase("null")) {
					continue;
				}
				final String message = "Drop: " + colour.toString() + "" + defs.getName() + " @bla@- Min: "
						+ colour.toString() + "" + drop.getMinimum() + ", @bla@Max: " + colour.toString() + ""
						+ drop.getMaximum() + ".";
				player.getPA().sendFrame126(message, frame++);
				if (frame >= capacity) {
					break;
				}
			}
			player.getPA().sendFrame126("", frame++);
		}
		player.getPA().showInterface(8134);
		return true;
	}

	private static final class Key<K, V> {

		private final K key;
		private final V value;

		private Key(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
}
