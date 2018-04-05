package org.osps.model.content.dialogue.teleport;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.ArrayUtils;
import org.osps.model.content.dialogue.OptionDialogue;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.npcs.boss.Kraken.Kraken;
import org.osps.model.players.Player;

public enum Teleports {

	PVP(/**/
			new int[] { 4140, 50235, 117112 }, /**/
			new Teleport("Rock Crabs @red@ (PVP)", 2672, 3712), /**/
			new Teleport("East Dragons (Wild)", 3347, 3672, true), /**/
			new Teleport("Lava Dragons", 3202, 3859), /**/
			new Teleport("Mage Bank", 2540, 4715), /**/
			new Teleport("44 Portals (Wild)", 2980, 3871, true) /**/
	), /**/
	TRAINING(
			new int[] { 4143, 50245, 117123 } /**/
			
	), 
	
	MINIGAME( /**/
			new int[] { 4150, 51005, 117154 }, /**/
//			new Teleport("Barrows", 3565, 3314), /**/
			new Teleport("Duel Arena", 3365, 3266) /**/
//			new Teleport("Fishing tourney", 2639, 3441), /**/
//			new Teleport("Warrior's Guild", 2846, 3542) /**/
	), /**/
	SKILLING( /**/
			new int[] { 6004, 51013, 117186 } /**/
//			new Teleport("Agility Courses", CustomTeleports.agilityTeleport),/**/
//			new Teleport("Blast Mining", 1469, 3863), /**/
//			new Teleport("Skilling Area", 3027, 3379), /**/
//			new Teleport("Hunter", CustomTeleports.hunterTeleport), /**/
//			new Teleport("Resource Area (Wild)", 3184, 3945, true), /**/
//			new Teleport("Runecrating Abyss", 3039, 4835), /**/
//			new Teleport("Farming", 3001, 3374), /**/
//			new Teleport("Woodcutting Guild", 1590, 3482) /**/
	); /**/

	private final int[] buttonIds;
	private final Teleport[] teleports;

	private Teleports(int[] buttonIds, Teleport... teleports) {
		this.buttonIds = buttonIds;
		this.teleports = teleports;
	}

	public int[] getButtonIds() {
		return buttonIds;
	}

	public Teleport[] getTeleports() {
		return teleports;
	}

	private static Teleports[] values = values();

	public static boolean onButton(Player player, int buttonId) {
		Optional<Teleports> optional = Arrays.stream(values)
				.filter(value -> ArrayUtils.contains(value.getButtonIds(), buttonId)).findFirst();

		if (optional.isPresent()) {
			Teleports value = optional.get();
			player.start(new TeleportDialogue(value.getTeleports()));
			return true;
		}

		return false;
	}

	private static class CustomTeleports {

		public static final Consumer<Player> godwarsTeleport = player -> {
			player.start(new OptionDialogue(/**/
					"Armadyl", p -> TeleportExecutor.teleport(p, new Position(2839, 5292, 2), true), /**/
					"Bandos", p -> TeleportExecutor.teleport(p, new Position(2860, 5354, 2), true), /**/
					"Zamorak", p -> TeleportExecutor.teleport(p, new Position(2925, 5335, 2), true), /**/
					"Saradomin", p -> TeleportExecutor.teleport(p, new Position(2909, 5265, 0), true) /**/
			));
		};
		public static final Consumer<Player> agilityTeleport = player -> {
			player.start(new OptionDialogue(/**/
					"Draynor Agility Course", p -> TeleportExecutor.teleport(p, new Position(3105, 3279, 0), true), /**/
					"Al Kharid Agility Course", p -> TeleportExecutor.teleport(p, new Position(3273, 3197, 0), true), /**/
					"Varrock Agility Course", p -> TeleportExecutor.teleport(p, new Position(3223, 3414, 0), true), /**/
					"Canifis Agility Course", p -> TeleportExecutor.teleport(p, new Position(3506, 3487, 0), true), /**/
					"Coming Soon", p -> player.getDH().sendStatement("This teleport is coming soon.") /**/
			));
		};

		public static final Consumer<Player> hunterTeleport = player -> {
			player.start(new OptionDialogue(/**/
					"Hunter (1-50)", p -> TeleportExecutor.teleport(p, new Position(1834, 3669), true), /**/
					"Hunter (50-99)", p -> TeleportExecutor.teleport(p, new Position(1829, 3601), true) /**/
			));
		};

	}

}