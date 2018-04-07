package org.osps.model.npcs.boss.Callisto;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;

/**
 * Callisto Ability class
 * @author Micheal/01053
 *
 */

public class Callisto {
	
	public static void KnockBack(Player c, int x, int y) {
		c.animation(807);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				c.resetWalkingQueue();
				c.teleportToX = x;
				c.teleportToY = y;
				c.getPA().requestUpdates();
				container.stop();
			}
		}, 2);
	}
}
