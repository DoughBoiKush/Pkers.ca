package org.osps.model.npcs.boss.abstract_bosses;

import java.util.ArrayList;

import org.osps.Config;
import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.items.Item;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class DefaultBossEvent extends AbstractBossEvent {
	
	private static int[] DROP_LIST_RARE = {11804, 11806, 11808, 11824, 12006,
			
	};
	
	public static int RARE_DROP() {
		return DROP_LIST_RARE[(int)(Math.random()*DROP_LIST_RARE.length - 1)];
	}
	
	public DefaultBossEvent() {
		attackers = new ArrayList<>();
		submit();
	}

	@Override
	protected int[] npcIds() {
		return null;
	}
	
	@Override
	protected int[][] locations() {
		return null;
	}

	@Override
	protected String[] announcements() {
		return null;
	}

	@Override
	protected boolean checkAll() {
		if (attackers == null)
			return false;
		if (npcIds() == null || game_event() == null || announcements() == null || locations() == null)
			throw new NullPointerException("Arrays / game_event cannot be equal to null!");
		if (npcIds().length != announcements().length || announcements().length != locations().length)
			throw new IndexOutOfBoundsException("Array lengths must be identical.");
		return true;
	}

	@Override
	protected void submit() {
		if (!checkAll())
			return;
		CycleEventHandler.getSingleton().addEvent(this, game_event(), (int) 600);
	}

	@Override
	protected CycleEvent game_event() {
		return new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				handleBossEvent(false);
				//container.stop();
			}
		};
	}

	@Override
	public void handleBossEvent(boolean mustreturn) {
		if (npc != null) {
			if(npc.npcType == 7101 == npc.isDead == false) {
				bossMessage("[::wb] @blu@The wilderness Boss is currently alive and located at greater demons!", true);
				return;
			}
			if (!npc.isDead) {
				npc.isDead = true;
				npc.updateRequired = true;
				npc.animUpdateRequired = true;
				npc.needRespawn = false;
				npc.absX = -1;
				npc.absY = -1;
				npc.heightLevel = -1;
				Server.npcHandler.dropItems(npc.index);
				Server.npcHandler.appendSlayerExperience(npc.index);
				Server.npcHandler.appendBossKC(npc.index);
				Server.npcHandler.appendKillCount(npc.index);
			}
			NPCHandler.npcs[npc.index] = npc = null;
			if (mustreturn)
				return;
		}
		if (attackers == null)
			attackers = new ArrayList<>();
		attackers.clear();
	//}
		index = Misc.random(npcIds().length - 1);
		npc = NPCHandler.spawnNpcAndReturn(npcIds()[index], locations()[index][0],locations()[index][1], 0, 1, 250/*hitpoints*/, 10/*max hit*/, 500, 500);
		if (npc != null)
			bossMessage(announcements()[index], true);
	}
	
	public void handleDeath() {
		if (attackers == null)
			attackers = new ArrayList<>();
				attackers.clear();
	}

	@Override
	protected void messageContributingPlayers(String message) {
		if (npc != null)
			bossMessage(message, false);
	}

	@Override
	public void addContributingPlayer(Player player) {
		if (attackers.contains(player))
			return;
		attackers.add(player);
		messageContributingPlayers("@blu@"+player.playerName+" @bla@has joined the boss fight against Glough!");
		messageContributingPlayers("There is currently @blu@"+attackers.size()+" @bla@participating in the fight against Glough!");
		
		if (npc != null)
			npc.HP += 250;
		messageContributingPlayers("Glough currently has "+npc.HP+"");
	}
	
	public static ArrayList <String>dropRecieved = new ArrayList<String>();
	
	public void dropLoot(Player c) {
		if(c.getItems().canAdd(995, 250_000)) {
		if(Misc.random(100) >= 1 && Misc.random(100) <= 97 && c.bossDamage >= 150) {
			c.sendMessage("You recieve: @blu@250,000 @bla@coins for your efforts!");
			c.getItems().addItem(995, 250_000);
			c.getPA().loadQuests();
		} else 
		if(Misc.random(100) >= 98 && Misc.random(100) <= 100 && c.bossDamage >= 250) {
			c.getItems().addItem(RARE_DROP(), 1);
			int id = RARE_DROP();
			c.sendMessage("You recieve: @blu@"+Item.getItemName(id)+" @bla@& 250,000 coins");
			c.getItems().addItem(995, 250_000);
			c.getPA().loadQuests();
		} else if(c.bossDamage <= 149) {
			c.sendMessage("You haven't done enough damage to be eligible for loot!");
		}
		} else {
			if(Misc.random(100) >= 1 && Misc.random(100) <= 97 && c.bossDamage >= 150) {
				c.sendMessage("You recieve: @blu@250,000 @bla@coins for your efforts!");
				Server.itemHandler.createGroundItem(c, 995, c.getX(), c.getY(), c.heightLevel, 250000, c.index);
				c.getPA().loadQuests();
			} else 
			if(Misc.random(100) >= 98 && Misc.random(100) <= 100 && c.bossDamage >= 250) {
				Server.itemHandler.createGroundItem(c, RARE_DROP(), c.getX(), c.getY(), c.heightLevel, 1, c.index);
				int id = RARE_DROP();
				c.sendMessage("You recieve: @blu@"+Item.getItemName(id)+" @bla@& 250,000 coins");
				Server.itemHandler.createGroundItem(c, 995, c.getX(), c.getY(), c.heightLevel, 250000,
						c.index);
				c.getPA().loadQuests();
			} else if(c.bossDamage <= 149) {
				c.sendMessage("You haven't done enough damage to be eligible for loot!");
			}
			
		}
			
	}
}