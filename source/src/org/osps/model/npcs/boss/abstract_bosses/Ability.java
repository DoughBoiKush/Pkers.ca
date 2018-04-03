package org.osps.model.npcs.boss.abstract_bosses;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;
import org.osps.model.players.combat.Hitmark;
import org.osps.util.Misc;

public class Ability {
	
	public static void KnockBack(Player c, int x, int y) {
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				if(container.getTotalTicks() == 1) {
					c.animation(807);
				}
				
				if(container.getTotalTicks() == 2) {
					c.resetWalkingQueue();
					c.teleportToX = x;
					c.teleportToY = y;
					c.getPA().requestUpdates();
					container.stop();
				}
			}
			
			public void stop() {
				c.appendDamage(Misc.random(10), Hitmark.HIT);
				c.sendMessage("You feel yourself get knocked back and take some damage!");
			}
		}, 2);
	}
}