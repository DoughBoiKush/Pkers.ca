package org.osps.model.minigames.bounty_hunter;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;

public abstract class TargetEvent extends CycleEvent {
	/**
	 * The owner of this TargetSelector object
	 */
	protected BountyHunter bountyHunter;
	
	/**
	 * Creates a new TargetSelector object that will be used to select targets
	 * for the owner of the BountyHunter object, the player.
	 * @param bountyHunter	the BountyHunter instance
	 */
	public TargetEvent(BountyHunter bountyHunter) {
		this.bountyHunter = bountyHunter;
	}
	
	@Override
	public abstract void execute(CycleEventContainer container);

}
