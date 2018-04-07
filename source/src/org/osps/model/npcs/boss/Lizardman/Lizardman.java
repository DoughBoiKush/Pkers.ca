package org.osps.model.npcs.boss.Lizardman;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Boundary;
import org.osps.model.players.Player;
import org.osps.model.players.combat.Hitmark;
import org.osps.util.Misc;

public class Lizardman {
	
public static final Boundary LIZARD = new Boundary(1474, 3675, 1577, 3726);
	
	public static void Minions(NPC npc, Player c) {
		if (c == null) {
			return;
		}
			int x = c.getX() + 2;
			int y = c.getY();
			Server.npcHandler.spawnNpc(c, 6768, x, y, 0, 0, 100, 10, 300, 50, true, false);
			applyDamage(npc, c);
		}
	
	public static void applyDamage(NPC npc, Player c) {
		try {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (npc != null) {
						c.getPA().createPlayersStillGfx(305, npc.getX(), npc.getY(), 0, 1);
					}
					container.stop();
				}

				public void stop() {
					if (c.goodDistance(c.getX(), c.getY(), npc.getX(), npc.getY(), 4)) {
						c.appendDamage(Misc.random(35), Hitmark.HIT);
						NPCHandler.kill(6768, 0);
					}
				}
			}, 6);
		} catch (Exception e) {

		}
	}
	
	public static void jump(NPC npc, Player c) {
		try {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if (npc != null) {
						npc.animation(7152);
						npc.lastHP = npc.HP;
						c.lastX = c.getX();
						c.lastY = c.getY();
					}
					container.stop();
				}
				public void stop() {
					c.Jumped = false;
					endJump(npc, c);
					
				}
			}, 10);
		} catch (Exception e) {
		}
	}
	
	public static void endJump(NPC npc, Player c) {
		try {
			if(c == null) {
				npc.resetCombat();
				return;
			}
			if(c.Jumped) {
				return;
			}
			c.lastX = c.getX();
			c.lastY = c.getY();
			npc.size = 0;
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

				@Override
				public void execute(CycleEventContainer container) {
					if(c.Jumped) {
						container.stop();
						return;
					}
					if (npc != null) {
						npc.animation(6946);
						npc.absX = c.lastX;
						npc.absY = c.lastY;
						npc.heightLevel = c.heightLevel;
						npc.updateRequired = true;
						if (c.absX == c.lastX || c.absY == c.lastY) {
							c.appendDamage(Misc.random(30), Hitmark.HIT);
							c.sendMessage("The Lizard has fallen from the sky and landed on you!");
							npc.size = 3;
						} else {
							c.sendMessage("You only just avoided the Lizard Shaman!");
							npc.size = 3;
						}
					}
					container.stop();
				}
			}, 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
