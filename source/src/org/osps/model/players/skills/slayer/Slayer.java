package org.osps.model.players.skills.slayer;

import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;

public class Slayer {

    private Player player;

    public Slayer(Player player) {
        this.player = player;
    }

	public boolean hasTask() {
		return player.slayerTask > 0 || player.taskAmount > 0;
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
        if(player.slayerPoints < 30) {
            player.sendMessage("@red@This requires atleast 30 slayer points, which you don't have.");
            player.getPA().removeAllWindows();
            return;
        }
        player.sendMessage("You have cancelled your current task of "+player.taskAmount+" "+NPCHandler.getNpcName(player.slayerTask)+".");
        player.slayerTask = -1;
        player.EASY = false;
        player.HARD = false;
        player.MEDIUM = false;
        player.changedTaskAmount = 0;
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
                player.getPA().sendFrame126(NPCHandler.getNpcName(player.removedTasks[i]), line[i]);
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