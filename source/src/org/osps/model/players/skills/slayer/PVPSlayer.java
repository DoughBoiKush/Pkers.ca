package org.osps.model.players.skills.slayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.osps.model.players.Player;
import org.osps.model.players.skills.Skill;
import org.osps.model.players.skills.slayer.EasyTask.Task;
import org.osps.util.Misc;

public class PVPSlayer {

	public static final int EASY_TASK = 1, MEDIUM_TASK = 2, HARD_TASK = 3;

	private List<Task> tasks = new ArrayList<>();

	private Player player;

	public PVPSlayer(Player player) {
		this.player = player;
	}

	public enum Task {
		PLAYER_KILLS(1, 0, EASY_TASK, "Kill players within the wilderess"), 
		PLAYER_KILLS1(3, 100, MEDIUM_TASK, "Kill players within the wilderess"),
		PLAYER_KILLS2(6, 500, HARD_TASK, "Kill players within the wilderess");

		private int killRequirement, tierRequirement, diff;
		private String location;

		private Task(int killRequirement, int tierRequirement, int difficulty, String location) {
			this.killRequirement = killRequirement;
			this.tierRequirement = tierRequirement;
			this.location = location;
			this.diff = difficulty;
		}

		public int getKillRequirement() {
			return killRequirement;
		}

		public int getTierRequirement() {
			return tierRequirement;
		}

		public int getDifficulty() {
			return diff;
		}

		public String getLocation() {
			return location;
		}

		static final Set<Task> TASKS = Collections.unmodifiableSet(EnumSet.allOf(Task.class));

		public static Task forKills(int amount) {
			Optional<Task> task = TASKS.stream().filter(t -> t.getKillRequirement() == amount).findFirst();
			return task.orElse(null);
		}
	}

	public void resizeTable(int difficulty) {
		tasks.clear();
		int kills = player.KC;
		for (Task task : Task.TASKS) {
			outer: for (int removed : player.removedTasks) {
				if (task.getKillRequirement() == removed) {
					continue outer;
				}
			}
			if (kills < task.getTierRequirement()) {
				continue;
			}
			if (task.getTierRequirement() > 1 && kills >= task.getTierRequirement() && difficulty == HARD_TASK) {
				tasks.add(task);
				continue;
			}
			if (task.getDifficulty() == difficulty) {
				tasks.add(task);
				continue;
			}
		}
	}

	public int getRequiredLevel(int amount) {
		for (Task task : Task.values())
			if (task.killRequirement == amount)
				return task.tierRequirement;
		return -1;
	}

	public String getLocation(int npcId) {
		for (Task task : Task.values())
			if (task.killRequirement == npcId)
				return task.location;
		return "";
	}

	public String getTaskName(int amount) {
		for (Task task : Task.values())
			if (task.killRequirement == amount)
				return task.name().replaceAll("_", " ").replaceAll("2", "").replaceAll("1", "").toLowerCase();
		return "";
	}

	public int getTaskId(String name) {
		for (Task task : Task.values())
			if (task.name() == name)
				return task.killRequirement;
		return -1;
	}

	public boolean hasTask() {
		return player.slayerPvPTask > 0 || player.pvpTaskAmount > 0;
	}

	public void generateTask(int difficulty) {
		System.out.println(""+hasTask()+" : "+player.pvpTaskAmount+"");
		if (hasTask()) {
			player.getDH().sendStatement("You already have a task to kill: "+player.pvpTaskAmount+" players.");
			return;
		}
		if (hasTask() && player.needsNewPvPTask) {
			Task task = Task.forKills(player.slayerPvPTask);
			int containedDiff = task.getDifficulty();
			if (containedDiff == EASY_TASK) {
				player.getDH().sendDialogues(3309, 1597);
				player.needsNewPvPTask = false;
			} else {
				containedDiff -= 1;
				resizeTable(containedDiff);
				int taskId = player.slayerPvPTask;
				while (taskId == player.slayerPvPTask) {
					taskId = getRandomTask(containedDiff);
				}
				if (difficulty == MEDIUM_TASK) {
					if (getSlayerDifficulty() < task.getTierRequirement()) {
						player.getDH().sendStatement("You need @blu@"+task.getTierRequirement()+"@bla@ Player Kills to select a task from this tier.");
						return;
					}
				}
				if (difficulty == HARD_TASK) {
					if (getSlayerDifficulty() < task.getTierRequirement()) {
						player.getDH().sendStatement("You need @blu@"+task.getTierRequirement()+"@bla@ Player Kills to select a task from this tier.");
						return;
					}
				}
				player.slayerPvPTask = taskId;
				player.pvpTaskAmount = getTaskAmount(containedDiff);
				player.needsNewPvPTask = false;
				player.getDH().sendDialogues(3306, 1597);
			}
			return;
		}
		//Task task = Task.forKills(player.KC);
		if (difficulty == MEDIUM_TASK) {
			if (player.KC < 100) {
				player.getDH().sendStatement("You need @blu@100@bla@ Player Kills to select a task from this tier.");
				player.sendMessage("HMMMK.");
				return;
			}
		}
		if (difficulty == HARD_TASK) {
			if (player.KC < 500) {
				player.getDH().sendStatement("You need @blu@500@bla@ Player Kills to select a task from this tier.");
				player.sendMessage("HMMMK.");
				return;
			}
		}

		resizeTable(difficulty);
		player.slayerPvPTask = getRandomTask(difficulty);
		player.pvpTaskAmount = getTaskAmount(difficulty);
		player.getDH().sendDialogues(3306, 1597);
		player.sendMessage("You have been assigned @blu@" + player.pvpTaskAmount + "@bla@ " + getTaskName(player.slayerPvPTask)
				+ ". Good luck, " + Misc.capitalize(player.playerName) + ".");
		player.getPA().removeAllWindows();
	}
	
	public int getTaskAmount(int difficulty) {
		switch (difficulty) {
		case EASY_TASK:
			return 30 + Misc.random(30);
		case MEDIUM_TASK:
			return 60 + Misc.random(30);
		case HARD_TASK:
			return 90 + Misc.random(30);
		}
		return 50 + Misc.random(15);
	}

	public int getRandomTask(int difficulty) {
		if (tasks.size() == 0) {
			resizeTable(difficulty);
		}
		Task task = tasks.get(Misc.random(tasks.size() - 1));
		return task.getKillRequirement();
	}

	public int getSlayerDifficulty() {
		int level = player.KC;
		if (level < 99) {
			return EASY_TASK;
		} else if (level > 100) {
			return MEDIUM_TASK;
		} else if (level > 500) {
			return HARD_TASK;
		}
		return EASY_TASK;
	}
}