package org.osps.model.content.dialogue.impl;

import java.util.function.Consumer;

import org.osps.model.content.dialogue.Dialogue;
import org.osps.model.content.dialogue.OptionDialogue;
import org.osps.model.players.Player;
import org.osps.model.players.skills.slayer.PVPSlayer;

public final class SlayerAssignmentDialogue extends Dialogue {

	@Override
	public boolean clickButton(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			player.start(new OptionDialogue(
					"Easy PVP Slayer task (@blu@0 @bla@Kills Required)", assign(PVPSlayer.EASY_TASK),
					"Medium PVP Slayer task (@blu@100 @bla@Kills Required)", assign(PVPSlayer.MEDIUM_TASK),
					"Hard PVP Slayer task (@blu@500 @bla@Kills Required)", assign(PVPSlayer.HARD_TASK)
			));
			end();
			break;
		}
	}

	private Consumer<Player> assign(int diff) {
		return (p) -> {

			end();
			p.setDialogue(null);
			p.getPVPSlayer().generateTask(diff);
			//p.getPA().removeAllWindows();
		};
	}

}

