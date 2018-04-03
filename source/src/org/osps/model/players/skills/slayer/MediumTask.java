package org.osps.model.players.skills.slayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.osps.model.players.Player;
import org.osps.model.players.skills.Skill;
import org.osps.util.Misc;

public class MediumTask {

	public static final int MEDIUM_TASK = 1;

	private List<Medium> tasks = new ArrayList<>();

	private Player player;

	public MediumTask(Player player) {
		this.player = player;
	}

	public enum Medium {
		BASILISK(417, 40, MEDIUM_TASK, "Relleka Dungeon"), 
		BLACK_DEMON(1432, 1, MEDIUM_TASK, "Taverley Dungeon"), 
		BLOODVELD(484, 40, MEDIUM_TASK, "Stronghold Slayer Cave & Slayer Tower"), 
		BLUE_DRAGON(268, 1, MEDIUM_TASK, "Taverley Dungeon"), 
		CAVE_HORROR(3209, 40, MEDIUM_TASK, "Relleka Dungeon"), 
		COCKATRICE(419, 25, MEDIUM_TASK, "Relleka Dungeon"), 
		FIRE_GIANT(2084, 1, MEDIUM_TASK, "Stronghold Slayer Cave & Brimhaven Dungeon"), 
		GREATER_DEMON(2026, 1, MEDIUM_TASK, "Stronghold Slayer Cave & Brimhaven Dungeon"), 
		GREEN_DRAGON(264, 1, MEDIUM_TASK, "The Wilderness"), 
		HELLHOUND(135, 1, MEDIUM_TASK, "Stronghold Slayer Cave & Taverley Dungeon"), 
		HILL_GIANT(2098, 1, MEDIUM_TASK, "Edgeville Dungeon"), 
		INFERNAL_MAGE(446, 40, MEDIUM_TASK, "Slayer Tower"), 
		KURASK2(411, 70, MEDIUM_TASK, "Relleka Dungeon"), 
		LESSER_DEMON(2006, 1, MEDIUM_TASK, "Taverley Dungeon"), 
		MOSS_GIANT(891, 1, MEDIUM_TASK, "Brimhaven Dungeon"), 
		RED_DRAGON(247, 1, MEDIUM_TASK, "Brimhaven Dungeon");

		private int npcId, levelReq, diff;
		private String location;

		private Medium(int npcId, int levelReq, int difficulty, String location) {
			this.npcId = npcId;
			this.levelReq = levelReq;
			this.location = location;
			this.diff = difficulty;
		}

		public int getNpcId() {
			return npcId;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public int getDifficulty() {
			return diff;
		}

		public String getLocation() {
			return location;
		}

		static final Set<Medium> TASKS = Collections.unmodifiableSet(EnumSet.allOf(Medium.class));

		public static Medium forNpc(int npc) {
			Optional<Medium> task = TASKS.stream().filter(t -> t.getNpcId() == npc).findFirst();
			return task.orElse(null);
		}
	}

	public void resizeTable(int difficulty) {
		tasks.clear();
		int level = player.playerLevel[Skill.SLAYER.getId()];
		for (Medium task : Medium.TASKS) {
			outer: for (int removed : player.removedTasks) {
				if (task.getNpcId() == removed) {
					continue outer;
				}
			}
			if (level < task.getLevelReq()) {
				continue;
			}
			if (task.getDifficulty() == difficulty) {
				tasks.add(task);
				continue;
			}
		}
	}

	public int getRequiredLevel(int npcId) {
		for (Medium task : Medium.values())
			if (task.npcId == npcId)
				return task.levelReq;
		return -1;
	}

	public String getLocation(int npcId) {
		for (Medium task : Medium.values())
			if (task.npcId == npcId)
				return task.location;
		return "";
	}

	public boolean isSlayerNpc(int npcId) {
		for (Medium task : Medium.values()) {
			if (task.getNpcId() == npcId)
				return true;
		}
		return false;
	}

	public boolean isSlayerTask(int npcId) {
		if (npcId == 252) {
			npcId = 259;
		}
		if (isSlayerNpc(npcId)) {
			if (player.slayerTask == npcId) {
				return true;
			}
		}
		return false;
	}

	public String getTaskName(int npcId) {
		for (Medium task : Medium.values())
			if (task.npcId == npcId)
				return task.name().replaceAll("_", " ").replaceAll("2", "").toLowerCase();
		return "";
	}

	public int getTaskId(String name) {
		for (Medium task : Medium.values())
			if (task.name() == name)
				return task.npcId;
		return -1;
	}

	public boolean hasTask() {
		return player.slayerTask > 0 || player.taskAmount > 0;
	}

	public int getTaskAmount(int difficulty) {
		switch (difficulty) {
		case MEDIUM_TASK:
			return 90 + Misc.random(30);
		}
		return 50 + Misc.random(15);
	}

	public int getRandomTask(int difficulty) {
		if (tasks.size() == 0) {
			resizeTable(difficulty);
		}
		Medium task = tasks.get(Misc.random(tasks.size() - 1));
		return task.getNpcId();
	}

	public int getSlayerDifficulty() {
		return MEDIUM_TASK;
	}

	public void generateTask() {
		if(player.combatLevel < 95)  {
			player.getDH().sendStatement("You need a combat level of 95 or higher to begin Medium Tasks!");
			player.dialogueAction = -1;
			return;
		} else
		if(!hasTask()) {
		Medium task = Medium.forNpc(player.slayerTask);
		int difficulty = getSlayerDifficulty();
		resizeTable(difficulty);
		player.slayerTask = getRandomTask(difficulty);
		player.taskAmount = getTaskAmount(difficulty);
		player.getDH().sendDialogues(3312, 1597);
		player.needsNewTask = false;
		player.MEDIUM = true;
		player.sendMessage("You have been assigned " + player.taskAmount + " " + getTaskName(player.slayerTask)
				+ ". Good luck, " + Misc.capitalize(player.playerName) + ".");
		} else {
            player.getDH().sendDialogues(3316, 1597);
		}
	}
}
