package org.osps.model.players.packets.commands.all;

import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;
import org.osps.model.players.skills.SkillConstants;

/**
 * Change the level of a given skill.
 * 
 * @author Emiel
 */
public class Setstat implements Command {

	@Override
	public void execute(Player c, String input) {
		int skillId;
		int skillLevel;
		String[] args = input.split(" ");
		if (args.length < 2) {
			throw new IllegalArgumentException();
		}
		try {
			skillId = Integer.parseInt(args[0]);
			skillLevel = Integer.parseInt(args[1]);
			if (skillId < 0 || skillId > c.playerLevel.length - 1) {
				c.sendMessage("Unable to set level, skill id cannot exceed the range of 0 -> " + (c.playerLevel.length - 1) + ".");
				return;
			}
			if (skillLevel < 1) {
				skillLevel = 1;
			} else if (skillLevel > 99) {
				skillLevel = 99;
			}
			c.playerLevel[skillId] = skillLevel;
			c.playerXP[skillId] = c.getPA().getXPForLevel(skillLevel) + 1;
			c.getPA().refreshSkill(skillId);
			c.sendMessage("set stat_" + SkillConstants.SKILL_NAMES[skillId] + ": to " + skillLevel);
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::setlevel skillid level");
		}
	}
}
