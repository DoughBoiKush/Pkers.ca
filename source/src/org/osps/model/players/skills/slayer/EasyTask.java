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

public class EasyTask {

	public static final int EASY_TASK = 1;

	private List<Task> tasks = new ArrayList<>();

	private Player player;

	public EasyTask(Player player) {
		this.player = player;
	}

	public enum Task {
		BANSHEE(414, 15, EASY_TASK, "Slayer Tower"),
		BLACK_KNIGHT(1545, 1, EASY_TASK, "Taverly Dungeon"),
		CHAOS_DRUID(2878, 1, EASY_TASK, "Taverly Dungeon"),
		CRAWLING_HAND(448, 5, EASY_TASK,"Slayer Tower"),
		EARTH_WARRIOR(2840, 1, EASY_TASK, "Edgeville Dungeon"),
		GHOST(85, 1, EASY_TASK, "Taverley Dungeon"),
        GIANT_BAT(2834, 1, EASY_TASK, "Taverley Dungeon"),
        MAGIC_AXE(2844, 1, EASY_TASK, "Taverly Dungeon"),
        PYREFIEND(435, 31, EASY_TASK, "Relleka Dungeon"),
        SKELETON(70, 1, EASY_TASK, "Edgeville Dungeon");

		private int npcId, levelReq, diff;
		private String location;

		private Task(int npcId, int levelReq, int difficulty, String location) {
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

		static final Set<Task> TASKS = Collections.unmodifiableSet(EnumSet.allOf(Task.class));

		public static Task forNpc(int npc) {
			Optional<Task> task = TASKS.stream().filter(t -> t.getNpcId() == npc).findFirst();
			return task.orElse(null);
		}
	}

	public void resizeTable(int difficulty) {
		tasks.clear();
		int level = player.playerLevel[Skill.SLAYER.getId()];
		for (Task task : Task.TASKS) {
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
		for (Task task : Task.values())
			if (task.npcId == npcId)
				return task.levelReq;
		return -1;
	}

	public String getLocation(int npcId) {
		for (Task task : Task.values())
			if (task.npcId == npcId)
				return task.location;
		return "";
	}

	public boolean isSlayerNpc(int npcId) {
		for (Task task : Task.values()) {
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
		for (Task task : Task.values())
			if (task.npcId == npcId)
				return task.name().replaceAll("_", " ").replaceAll("2", "").toLowerCase();
		return "";
	}

	public int getTaskId(String name) {
		for (Task task : Task.values())
			if (task.name() == name)
				return task.npcId;
		return -1;
	}

	public boolean hasTask() {
		return player.slayerTask > 0 || player.taskAmount > 0;
	}

	public int getTaskAmount(int difficulty) {
		switch (difficulty) {
		case EASY_TASK:
			return 90 + Misc.random(30);
		}
		return 50 + Misc.random(15);
	}

	public int getRandomTask(int difficulty) {
		if (tasks.size() == 0) {
			resizeTable(difficulty);
		}
		Task task = tasks.get(Misc.random(tasks.size() - 1));
		return task.getNpcId();
	}
	
	public int getSlayerDifficulty() {    	
        return EASY_TASK;
    }
	
	public void generateTask() {
		if (!hasTask()) {
			Task task = Task.forNpc(player.slayerTask);
			int difficulty = getSlayerDifficulty();
			resizeTable(difficulty);
			player.slayerTask = getRandomTask(difficulty);
			player.taskAmount = getTaskAmount(difficulty);
			player.getDH().sendDialogues(3306, 1597);
			player.needsNewTask = false;
			player.EASY = true;
			player.sendMessage("You have been assigned " + player.taskAmount + " " + getTaskName(player.slayerTask)
					+ ". Good luck, " + Misc.capitalize(player.playerName) + ".");
		} else {
			player.getDH().sendDialogues(3316, 1597);
		}
	}
}
