package org.osps.model.players.packets.commands.admin;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;
import org.osps.model.players.skills.Skill;

/**
 * Give the player virtually infinite HP, Special Attack energy and Prayer.
 * 
 * @author Emiel
 */
public class Godmode implements Command {

	@Override
	public void execute(Player c, String input) {
		if (c.inGodmode()) {
			c.playerLevel[Skill.STRENGTH.getId()] = 99;
			c.getPA().refreshSkill(2);
			c.playerLevel[Skill.HITPOINTS.getId()] = 99;
			c.getPA().refreshSkill(3);
			c.playerLevel[Skill.PRAYER.getId()] = 99;
			c.getPA().refreshSkill(5);
			c.specAmount = 10.0;
			c.getPA().requestUpdates();
			c.setSafemode(false);
			c.setGodmode(false);
			c.sendMessage("Godmode deactivated. Return to base for debriefing.");
		} else {
			c.playerLevel[Skill.STRENGTH.getId()] = 9999;
			c.getPA().refreshSkill(Skill.STRENGTH.getId());
			c.playerLevel[3] = Integer.MAX_VALUE;
			c.getPA().refreshSkill(3);
			c.playerLevel[5] = Integer.MAX_VALUE;
			c.getPA().refreshSkill(5);
			c.specAmount = Integer.MAX_VALUE;
			c.getPA().requestUpdates();
			c.setSafemode(true);
			c.setGodmode(true);
			c.sendMessage("Godmode activated. Good luck soldier!");
		}
	}
}
