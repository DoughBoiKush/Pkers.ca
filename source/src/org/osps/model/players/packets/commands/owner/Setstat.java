package org.osps.model.players.packets.commands.owner;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
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
		String playerName;
		int skillId;
		int skillLevel;
		String[] args = input.split(" ");
		if (args.length < 3) {
			throw new IllegalArgumentException();
		}
		try {
			skillId = Integer.parseInt(args[0]);
			skillLevel = Integer.parseInt(args[1]);
			playerName = args[2];
			
			if (skillId < 0 || skillId > c.playerLevel.length - 1) {
				c.sendMessage("Unable to set level, skill id cannot exceed the range of 0 -> " + (c.playerLevel.length - 1) + ".");
				return;
			}
			if (skillLevel < 1) {
				skillLevel = 1;
			} else if (skillLevel > 99) {
				skillLevel = 99;
			}
			
			Player target = PlayerHandler.getPlayer(playerName);
			
			if (target == null) {
				c.sendMessage("Player not found.");
				return;
			}
			
			target.playerLevel[skillId] = skillLevel;
			target.playerXP[skillId] = c.getPA().getXPForLevel(skillLevel) + 1;
			target.getPA().refreshSkill(skillId);
			c.sendMessage("set stat_" + SkillConstants.SKILL_NAMES[skillId] + ": to " + skillLevel);
		} catch (Exception e) {
			c.sendMessage("Error. Correct syntax: ::setlevel skillid level");
		}
	}
}
