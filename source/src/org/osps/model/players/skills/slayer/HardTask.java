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

public class HardTask {

	public static final int HARD_TASK = 1;

	private List<Hard> tasks = new ArrayList<>();

	private Player player;

	public HardTask(Player player) {
		this.player = player;
	}

	public enum Hard {
		IRON_DRAGON(273, 1, HARD_TASK, "Stronghold Slayer Cave & Brimhaven Dungeon"), 
		BRONZE_DRAGON(270, 1, HARD_TASK, "Stronghold Slayer Cave & Brimhaven Dungeon"), 
		BLACK_DRAGON(259, 1, HARD_TASK, "Taverley Dungeon"), 
		STEEL_DRAGON(274, 1, HARD_TASK, "Stronghold Slayer Cave & Brimhaven Dungeon"), 
		ABERRANT_SPECTRE(6, 60, HARD_TASK, "Stronghold Slayer Cave"), 
		SMOKE_DEVIL(498, 93, HARD_TASK, "Stronghold Slayer Cave"), 
		TZHAAR_XIL(2167, 1, HARD_TASK, "Tzhaar Cave"), 
		NECHRYAELS(11, 80, HARD_TASK, "Stronghold Slayer Cave & Slayer Tower"), 
		KURASK(411, 70, HARD_TASK, "Relleka Dungeon"), 
		HELLHOUND(135, 1, HARD_TASK, "Stronghold Slayer Cave & Taverley Dungeon"), 
		GREATER_DEMON(2026, 1, HARD_TASK, "Stronghold Slayer Cave & Brimhaven Dungeon"), 
		GARGOYLE(1543, 75, HARD_TASK, "Stronghold Slayer Cave & Slayer Tower"), 
		DUST_DEVIL(424, 65, HARD_TASK, "Slayer Tower"), 
		DARK_BEAST(4005, 90, HARD_TASK, "Taverly Dungeon"),
		CAVE_KRAKEN(492, 87, HARD_TASK, "Stronghold Slayer Cave"), 
		ABYSSAL_DEMON(415, 85, HARD_TASK, "Stronghold Slayer Cave & Slayer Tower");

		private int npcId, levelReq, diff;
		private String location;

		private Hard(int npcId, int levelReq, int difficulty, String location) {
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

		static final Set<Hard> TASKS = Collections.unmodifiableSet(EnumSet.allOf(Hard.class));

		public static Hard forNpc(int npc) {
			Optional<Hard> task = TASKS.stream().filter(t -> t.getNpcId() == npc).findFirst();
			return task.orElse(null);
		}
	}

	public void resizeTable(int difficulty) {
		tasks.clear();
		int level = player.playerLevel[Skill.SLAYER.getId()];
		for (Hard task : Hard.TASKS) {
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
		for (Hard task : Hard.values())
			if (task.npcId == npcId)
				return task.levelReq;
		return -1;
	}

	public String getLocation(int npcId) {
		for (Hard task : Hard.values())
			if (task.npcId == npcId)
				return task.location;
		return "";
	}

	public boolean isSlayerNpc(int npcId) {
		for (Hard task : Hard.values()) {
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
		for (Hard task : Hard.values())
			if (task.npcId == npcId)
				return task.name().replaceAll("_", " ").replaceAll("2", "").toLowerCase();
		return "";
	}

	public int getTaskId(String name) {
		for (Hard task : Hard.values())
			if (task.name() == name)
				return task.npcId;
		return -1;
	}

	public boolean hasTask() {
		return player.slayerTask > 0 || player.taskAmount > 0;
	}

	public int getTaskAmount(int difficulty) {
		switch (difficulty) {
		case HARD_TASK:
			return 90 + Misc.random(30);
		}
		return 50 + Misc.random(15);
	}

	public int getRandomTask(int difficulty) {
		if (tasks.size() == 0) {
			resizeTable(difficulty);
		}
		Hard task = tasks.get(Misc.random(tasks.size() - 1));
		return task.getNpcId();
	}

	public int getSlayerDifficulty() {
		if (player.combatLevel >= 105 && player.combatLevel <= 126) {
			return HARD_TASK;
		}
		return -1;
	}

	public void generateTask() {
		if (player.combatLevel < 105) {
			player.getDH().sendStatement("You need to be of atleast 105 combat to choose Hard Tasks.");
			player.dialogueAction = -1;
			return;
		} else if (!hasTask()) {
			Hard task = Hard.forNpc(player.slayerTask);
			int difficulty = getSlayerDifficulty();
			resizeTable(difficulty);
			player.slayerTask = getRandomTask(difficulty);
			player.taskAmount = getTaskAmount(difficulty);
			player.getDH().sendDialogues(3313, 1597);
			player.needsNewTask = false;
			player.HARD = true;
			player.sendMessage("You have been assigned " + player.taskAmount + " " + getTaskName(player.slayerTask)
					+ ". Good luck, " + Misc.capitalize(player.playerName) + ".");
		} else {
			player.getDH().sendDialogues(3316, 1597);
		}
	}
}
