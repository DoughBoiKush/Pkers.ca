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

public class PVESlayer {

    public static final int EASY_TASK = 1, MEDIUM_TASK = 2, HARD_TASK = 3;

    private List<Task> tasks = new ArrayList<>();

    private Player player;

    public PVESlayer(Player player) {
        this.player = player;
    }

    public enum Task {
    	PKER(1, 0, EASY_TASK, "Kill players within the wilderess"),
        LEGENDARY_PKER(3, 100, MEDIUM_TASK, "Kill players within the wilderess"),
        EXTREME_PKER(6, 500, HARD_TASK, "Kill players within the wilderess");
        
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
        
        public static Task forNpc(int amount) {
        	Optional<Task> task = TASKS.stream().filter(t -> t.getKillRequirement() == amount).findFirst();
        	return task.orElse(null);
        }
    }

    public void resizeTable(int difficulty) {
    	tasks.clear();
    	int kills = player.KC;
    	for (Task task : Task.TASKS) {
    		outer:
    		for (int removed : player.removedTasks) {
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


   /* public boolean isSlayerNpc(int amount) {
        for (Task task : Task.values()) {
            if (task.getKillRequirement() == amount)
                return true;
        }
        return false;
    }


    public boolean isSlayerTask(int npcId) {
        if (isSlayerNpc(npcId)) {
            if (player.slayerTask == npcId) {
                return true;
            }
        }
        return false;
    } */


    public String getTaskName(int amount) {
        for (Task task : Task.values())
            if (task.killRequirement == amount)
                return task.name().replaceAll("_", " ").replaceAll("2", "").toLowerCase();
        return "";
    }


    public int getTaskId(String name) {
        for (Task task : Task.values())
            if (task.name() == name)
                return task.killRequirement;
        return -1;
    }


    public boolean hasTask() {
        return player.slayerTask > 0 || player.taskAmount > 0;
    }


    public void generateTask(int difficulty) {
    	 if (hasTask() && !player.needsNewTask) {
             player.getDH().sendDialogues(3307, 1597);
             return;
         }
        if (hasTask() && player.needsNewTask) {
        	Task task = Task.forNpc(player.slayerTask);
            int containedDiff = task.getDifficulty();
            if (containedDiff == EASY_TASK) {
            	player.getDH().sendDialogues(3309, 1597);
                player.needsNewTask = false;
            } else {
            	containedDiff -= 1;
            	resizeTable(containedDiff);
            	int taskId = player.slayerTask;
            	while (taskId == player.slayerTask) {
            		taskId = getRandomTask(containedDiff);
            	}
            	if (difficulty ==HARD_TASK) {
            	if (getSlayerDifficulty() < difficulty) {
                	player.sendMessage("You cannot have this task as it's above your difficulty.");
                	return;
                }
            	}
                player.slayerTask = taskId;
                player.taskAmount = getTaskAmount(containedDiff);
                player.needsNewTask = false;
                player.getDH().sendDialogues(3306, 1597);
            }
            return;
        }
        if (difficulty ==HARD_TASK) {
        if (getSlayerDifficulty() < difficulty) {
        	player.sendMessage("You cannot have this task as it's above your difficulty.");
        	return;
        }
        }
        
    	resizeTable(difficulty);
        player.slayerTask = getRandomTask(difficulty);
        player.taskAmount = getTaskAmount(difficulty);
        player.getDH().sendDialogues(3306, 1597);
        player.sendMessage("You have been assigned " + player.taskAmount
                + " " + getTaskName(player.slayerTask) + ". Good luck, "
                + Misc.capitalize(player.playerName) + ".");
    }
    
    public void generateTask() {
    	 if (hasTask() && !player.needsNewTask) {
             player.getDH().sendDialogues(3307, 1597);
             return;
         }
        if (hasTask() && player.needsNewTask) {
        	Task task = Task.forNpc(player.slayerTask);
            int difficulty = task.getDifficulty();
            if (difficulty == EASY_TASK) {
            	player.getDH().sendDialogues(3309, 1597);
                player.needsNewTask = false;
            } else {
            	difficulty -= 1;
            	resizeTable(difficulty);
            	int taskId = player.slayerTask;
            	while (taskId == player.slayerTask) {
            		taskId = getRandomTask(difficulty);
            	}
                player.slayerTask = taskId;
                player.taskAmount = getTaskAmount(difficulty);
                player.needsNewTask = false;
                player.getDH().sendDialogues(3306, 1597);
            }
            return;
        }
    	int difficulty = getSlayerDifficulty();
    	resizeTable(difficulty);
        player.slayerTask = getRandomTask(difficulty);
        player.taskAmount = getTaskAmount(difficulty);
        player.getDH().sendDialogues(3306, 1597);
        player.sendMessage("You have been assigned " + player.taskAmount
                + " " + getTaskName(player.slayerTask) + ". Good luck, "
                + Misc.capitalize(player.playerName) + ".");
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
    	int level = player.playerLevel[Skill.SLAYER.getId()];
        if (player.combatLevel > 0 && player.combatLevel <= 45 || level < 40) {
            return EASY_TASK;
        } else if (player.combatLevel > 45 && player.combatLevel <= 110) {
            return MEDIUM_TASK;
        } else if (player.combatLevel > 100) {
            return HARD_TASK;
        }
        return EASY_TASK;
    }


    public void handleInterface(String shop) {
        if (shop.equalsIgnoreCase("buy")) {
            player.getPA().sendFrame126("Slayer Points: " + player.slayerPoints, 41011);
            player.getPA().showInterface(41000);
        } else if (shop.equalsIgnoreCase("learn")) {
            player.getPA().sendFrame126("Slayer Points: " + player.slayerPoints, 41511);
            player.getPA().showInterface(41500);
        } else if (shop.equalsIgnoreCase("assignment")) {
            player.getPA().sendFrame126("Slayer Points: " + player.slayerPoints, 42011);
            updateCurrentlyRemoved();
            player.getPA().showInterface(42000);
        }
    }
    
    public void cancelTask() {
        if(!hasTask()) {
            player.sendMessage("You must have a task to cancel first.");
            return;
        }
        if(player.slayerPoints < 20) {
            player.sendMessage("@red@This requires atleast 20 slayer points, which you don't have.");
            player.getPA().removeAllWindows();
            return;
        }
        player.sendMessage("You have cancelled your current task of "+player.taskAmount+" "+getTaskName(player.slayerTask)+".");
        player.slayerTask = -1;
        player.taskAmount = 0;
        player.slayerPoints -= 30;
    }
    
    public void removeTask() {
        int counter = 0;
        if(!hasTask()) {
            player.sendMessage("You must have a task to remove first.");
            return;
        }
        if(player.slayerPoints < 100) {
            player.sendMessage("This requires atleast 100 slayer points, which you don't have.");
            return;
        } 
        for(int i = 0; i < player.removedTasks.length; i++) {
            if(player.removedTasks[i] != -1) {
                counter++;
            }
            if(counter == 4) {
                player.sendMessage("You don't have any open slots left to remove tasks.");
                return;
            }
            if(player.removedTasks[i] == -1) {
                player.removedTasks[i] = player.slayerTask;
                player.slayerPoints -= 100;
                player.slayerTask = -1;
                player.taskAmount = 0;
                player.sendMessage("Your current slayer task has been removed, you can't obtain this task again.");
                updateCurrentlyRemoved();
                return;
            }
        }
    }
    
    public void updatePoints() {
        player.getPA().sendFrame126("Slayer Points: " + player.slayerPoints, 41011);
        player.getPA().sendFrame126("Slayer Points: " + player.slayerPoints, 41511);
        player.getPA().sendFrame126("Slayer Points: " + player.slayerPoints, 42011);
        player.getPA().sendFrame126("@red@Slayer Points: @or2@"+player.slayerPoints, 7336);
    }
    
    public void updateCurrentlyRemoved() {
        int line[] = {42014, 42015, 42016, 42017};
        for(int i = 0; i < player.removedTasks.length; i++) {
            if(player.removedTasks[i] != -1) {
                player.getPA().sendFrame126(this.getTaskName(player.removedTasks[i]), line[i]);
            } else {
                player.getPA().sendFrame126("", line[i]);
            }
        }
    }


    public void buySlayerExperience() {
        if(System.currentTimeMillis() - player.buySlayerTimer < 500)
            return;
        if(player.slayerPoints < 50) {
            player.sendMessage("You need at least 50 slayer points to gain 60,000 Experience.");
            return;
        }
        player.buySlayerTimer = System.currentTimeMillis();
        player.slayerPoints -= 50;
        player.getPA().addSkillXP(60000, 18);
        player.sendMessage("You spend 50 slayer points and gain 60,000 experience in slayer.");
        updatePoints();
    }
    
    public void buySlayerDart() {
        if(System.currentTimeMillis() - player.buySlayerTimer < 500)
            return;
        if(player.slayerPoints < 35) {
            player.sendMessage("You need at least 35 slayer points to buy Slayer darts.");
            return;
        }
        if(player.getItems().freeSlots() < 2 && !player.getItems().playerHasItem(560) && !player.getItems().playerHasItem(558)) {
            player.sendMessage("You need at least 2 free lots to purchase this.");
            return;
        }


        player.buySlayerTimer = System.currentTimeMillis();
        player.slayerPoints -= 35;
        player.sendMessage("You spend 35 slayer points and aquire 250 casts of Slayer darts.");
        player.getItems().addItem(558, 1000);
        player.getItems().addItem(560, 250);
        updatePoints();
    }
    
    public void buyBroadArrows() {
        if(System.currentTimeMillis() - player.buySlayerTimer < 500)
            return;
        if(player.slayerPoints < 25) {
            player.sendMessage("You need at least 25 slayer points to buy Broad arrows.");
            return;
        }
        if(player.getItems().freeSlots() < 1 && !player.getItems().playerHasItem(4160)) {
            player.sendMessage("You need at least 1 free lot to purchase this.");
            return;
        }
        player.buySlayerTimer = System.currentTimeMillis();
        player.slayerPoints -= 25;
        player.sendMessage("You spend 35 slayer points and aquire 250 Broad arrows.");
        player.getItems().addItem(4160, 250);
        updatePoints();
    }
    
    public void buyRespite() {
        if(System.currentTimeMillis() - player.buySlayerTimer < 1000)
            return;
        if(player.slayerPoints < 25) {
            player.sendMessage("You need at least 25 slayer points to buy Slayer's respite.");
            return;
        }
        if(player.getItems().freeSlots() < 1) {
            player.sendMessage("You need at least 1 free lot to purchase this.");
            return;
        }
        player.buySlayerTimer = System.currentTimeMillis();
        player.slayerPoints -= 25;
        player.sendMessage("You spend 25 slayer points and aquire a useful Slayer's respite.");
        player.getItems().addItem(5759, 1);
        updatePoints();
    }


}