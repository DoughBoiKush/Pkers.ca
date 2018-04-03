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

public class BossSlayer {

    public static final int BOSS_TASK = 1;

    private List<Task> tasks = new ArrayList<>();

    private Player player;

    public BossSlayer(Player player) {
        this.player = player;
    }

    public enum Task {
    	GIANT_MOLE(5779, 1, BOSS_TASK, "Falador Mole Lair, under Falador Park"),
    	CERBERUS(5862, 85, BOSS_TASK, "Deep beneath the Taverley Dungeon"),
    	THERMONUCLEAR_SMOKE_DEVIL(499, 93, BOSS_TASK, "Stronghold Slayer Cave"),
    	KRAKEN(494, 1, BOSS_TASK, "Stronghold Slayer Cave"),
    	KREE_ARRA(3162, 1, BOSS_TASK, "Godwars"),
    	KRIL_TSUTSAROTH(3129, 1, BOSS_TASK, "Godwars"),
    	GENERAL_GRAARDOR(2215, 1, BOSS_TASK, "Godwars"),
    	//BARRELCHEST(6342, 1, BOSS_TASK, "Barrelchest Teleport"),
    	COMMANDER_ZILYANA(2205, 1, BOSS_TASK, "Godwars"),
    	CORPOREAL_BEAST(319, 1, BOSS_TASK, "Corporeal Beast's lair"),
    	KING_BLACK_DRAGON(239, 1, BOSS_TASK, "KBD Lair"),
    	CHAOS_ELEMENTAL(2054, 1, BOSS_TASK, "West of the Rogue's Castle"),
    	DAGANNOTH_SUPREME(2265, 1, BOSS_TASK, "Waterbirth Island Dungeon"),
    	DAGANNOTH_PRIME(2266, 1, BOSS_TASK, "Waterbirth Island Dungeon"),
    	DAGANNOTH_REX(2267, 1, BOSS_TASK, "Waterbirth Island Dungeon"),
    	VETION(6611, 1, BOSS_TASK, "North of the Bone Yard"),
    	CALLISTO(6609, 1, BOSS_TASK, "South of the Demonic Ruins"),
    	VENENATIS(6610, 1, BOSS_TASK, "East of the Bone Yard"),
    	CHAOS_FANATIC(6619, 1, BOSS_TASK, "West of the Lava Maze"),
    	CRAZY_ARCHAEOLOGIST(6618, 1, BOSS_TASK, "South of The Forgotten Cemetery"),
    	ZULRAH(2042, 1, BOSS_TASK, "Zul-Andra Island");

        private int npcId, levelReq, diff;
        private String location;

        private Task(int npcId, int levelReq, int difficulty, String location) {
            this.npcId = npcId;
            this.levelReq = levelReq;
            this.diff = difficulty;
            this.location = location;
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
    		outer:
    		for (int removed : player.removedTasks) {
    			if (task.getNpcId() == removed) {
    				continue outer;
    			}
    		}
    		if (level < task.getLevelReq()) {
    			continue;
    		}
    		if (task.getLevelReq() > 1 && level >= task.getLevelReq()) {
    			tasks.add(task);
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
    	/*
    	 * For zulrah.
    	 */
    	if (npcId == 2043 || npcId == 2044) {
    		npcId = 2042;
    	}
    	
        if (isSlayerNpc(npcId)) {
            if (player.bossSlayerTask == npcId) {
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
        return player.bossSlayerTask > 0 || player.bossTaskAmount > 0;
    }


    public void generateTask() {
    	 if (hasTask() && !player.needsNewBossTask) {
             player.getDH().sendDialogues(707, 405);
             return;
         }
         if (hasTask() && player.needsNewBossTask) {
         	Task task = Task.forNpc(player.bossSlayerTask);
             int difficulty = task.getDifficulty();
             if (difficulty == BOSS_TASK) {
             	player.getDH().sendDialogues(707, 1597);
                 player.needsNewBossTask = false;
             } else {
             	difficulty -= 1;
             	resizeTable(difficulty);
             	int taskId = player.bossSlayerTask;
             	while (taskId == player.bossSlayerTask) {
             		taskId = getRandomTask(difficulty);
             	}
                 player.bossSlayerTask = taskId;
                 player.bossTaskAmount = getTaskAmount(difficulty);
                 player.needsNewBossTask = false;
                 player.getDH().sendDialogues(703, 405);
             }
             return;
         }
    	int difficulty = getSlayerDifficulty();
    	resizeTable(difficulty);
        player.bossSlayerTask = getRandomTask(difficulty);
        player.bossTaskAmount = getTaskAmount(difficulty);
        player.getDH().sendDialogues(703, 405);
        player.sendMessage("You have been assigned " + player.bossTaskAmount
                + " " + getTaskName(player.bossSlayerTask) + ". Good luck, "
                + Misc.capitalize(player.playerName) + ".");
    }


    public int getTaskAmount(int difficulty) {
    	switch (difficulty) {
    	case BOSS_TASK:
    		return 10 + Misc.random(5);
	}
    	return 10 + Misc.random(5);
    }


    public int getRandomTask(int difficulty) {
    	if (tasks.size() == 0) {
    		resizeTable(difficulty);
    	}
    	Task task = tasks.get(Misc.random(tasks.size()));
    	return task.getNpcId();
    }


    public int getSlayerDifficulty() {
        return BOSS_TASK;
    }


}