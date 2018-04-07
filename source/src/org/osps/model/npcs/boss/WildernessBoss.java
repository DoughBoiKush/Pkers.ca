package org.osps.model.npcs.boss;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Misc;

public class WildernessBoss {
	
	public static final int RESPAWN_TIME = 1200;//in seconds
	public static int amountSpawns = 0;
	public static int lastSpawn = 1;
	public static boolean hasSpawned = false;
	public static long spawnsIn = 0L;
	public static final int MIN_DAMAGE = 150;
	
	public static void SpawnBoss() {
		lastSpawn++;
	    amountSpawns++;
	    hasSpawned = true;
		int Npcs[] = {6345};
		int location [][] = {{3138, 3529}};
		int grabNPC = Misc.random(Npcs.length-1); 
		String name[] = {"Edgeville Bridge"};
		int where = Misc.random(location.length-1);
		NPCHandler.spawnNpc7(Npcs[grabNPC], location[where][0],location[where][1], 0, 1, 1, 1, 500, 500);
		bossMessage("[@blu@Server Npc Spawn@bla@]@blu@" + NPCHandler.getNpcListName(Npcs[grabNPC]) + " has spawned near the "+ name[where] +".");
	}
	
	public static final void health(Player c, int i) {
					NPCHandler.npcs[i].HP += 250;
					bossMessage(""+c.playerName+" has joined the fight and the NPC rejuvenates 250 health.");
					bossMessage("Wilderness Boss Current Health:"+NPCHandler.npcs[i].HP);			
				//return 0;
	}
		
	public static void startDeathEvent() {
		spawnsIn = Server.currentServerTime + (RESPAWN_TIME * 1000);
		hasSpawned = false;
		bossMessage("<col=255>Kamil</col> has died and will respawn in: <col=255>" + getTime() + "</col>");
		CycleEventHandler.getSingleton().addEvent(RESPAWN_TIME, new CycleEvent() {
			
			@Override
			public void execute(CycleEventContainer container) {
					SpawnBoss();
					container.stop();
			}			
		}, (int) (RESPAWN_TIME / 0.6));
	}
	
	public static String getTime() {
        int timeLeft = (int) ((spawnsIn - Server.currentServerTime) / 1000);
        if (timeLeft < 60) {
            return timeLeft + " seconds";
        } else {
            int minutes = (int) Math.ceil(timeLeft / 60);
            int seconds = timeLeft - (minutes * 60);
            if (minutes == 1) {
                return minutes + " minute";
            } else {
                return minutes + " minutes";
            }
        }
    }

	public static void bossMessage(String q) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
		if (PlayerHandler.players[j] != null) {
			Player c2 = (Player)PlayerHandler.players[j];
			c2.sendMessage(q);
			}
		}
	}
}	
