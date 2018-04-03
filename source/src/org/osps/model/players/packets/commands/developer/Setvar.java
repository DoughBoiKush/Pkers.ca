package org.osps.model.players.packets.commands.developer;

import org.osps.Config;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;
import org.osps.model.players.skills.Skill;

/**
 * Mirror, mirror on the wall. Set the variables of them all!
 * @author Chris
 * @see http://prntscr.com/88dlzq
 * @see http://prntscr.com/88dn4z
 *
 */
public class Setvar implements Command {
	
	private static int BASE_VARIABLE = 15;
	
	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		if (args.length != 2) {
			c.sendMessage("Code red! Invalid var arguments specified! Try again?");
			return;
		}
		String variable = args[0];
		String t;
		Double value2 = Double.parseDouble(args[1]);
		int value = value2.intValue();
		if (variable.startsWith("com_")) {
			t = variable.substring(4);
			switch (t) {
			case "god":
				c.setGodmode(value == 1 ? true : false);
				if (value == 0) {
					c.playerLevel[3] = 99;
					c.getPA().refreshSkill(3);
					c.playerLevel[5] = 99;
					c.specAmount = 10.0;
					c.getPA().refreshSkill(5);
					c.getPA().requestUpdates();
					c.setSafemode(false);
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
				}
				break;
			case "droprate":
				c.setDropModifier(value2);
				c.sendMessage("set " + variable + ": to " + value2);
				return;
			case "poison":
				c.setPoisonDamage((byte) value);
				c.getPA().requestUpdates();
				break;
			case "venom":
				c.setVenomDamage((byte) value);
				c.getPA().requestUpdates();
				break;
			default:
				throwInvalid(c);
				return;
			}
			c.sendMessage("set " + variable + ": to " + value + " basevar: " + BASE_VARIABLE);
		} else if (variable.startsWith("jmod_")) {
			t = variable.substring(5);
			switch (t) {
			case "bh":
				Config.BOUNTY_HUNTER_ACTIVE = value == 1 ? true : false;		
				break;
			case "debug":
				c.setDebug(value);
				break;
			case "dxp":
				Config.BONUS_WEEKEND = value == 1 ? true : false;
				break;
			case "attackable":
				Config.ADMIN_ATTACKABLE = value >= 1 ? true : false;
				break;
			default:
				throwInvalid(c);
				return;
			}
			c.sendMessage("set " + variable + ": to " + value);
		} else if (variable.startsWith("tzhaar_")) {
			t = variable.substring(7);
			switch (t) {
				case "wave":
					c.waveId = value;
					break;
			}
			c.sendMessage("set " + variable + ": to " + value + " basevar: " + BASE_VARIABLE);
		} else if (variable.startsWith("clanwars_")) {
			t = variable.substring(9);
			switch (t) {
			case "ffa":
				//ClanWars.FFA_ENABLED = value >= 1 ? true : false;
				break;
			case "challenge":
				//ClanWars.CHALLENGE_ENABLED = value >= 1 ? true : false;
				break;
			default:
				throwInvalid(c);
				return;
			}
			c.sendMessage("set " + variable + ": to " + value + " basevar: " + BASE_VARIABLE);
		} else if (variable.startsWith("wep_")) {
			t = variable.substring(4);
			switch (t) {
			case "blowpipe":
				c.setToxicBlowpipeCharge(value);
				c.setToxicBlowpipeAmmo(value);
				c.setToxicBlowpipeAmmoAmount(value);
				break;
			case "trident":
				c.setTridentCharge(value);
				c.setToxicTridentCharge(value);
				break;
			case "serp":
				c.setSerpentineHelmCharge(value);
				break;
			case "toxic":
				c.setToxicStaffOfDeadCharge(value);
				break;
			default:
				throwInvalid(c);
				return;
			}
			c.sendMessage("set " + variable + ": to " + value + " basevar: " + BASE_VARIABLE);
		} else if (variable.startsWith("help")) {
			c.getDH().sendStatement("Var arguments: com_god, config_bh, config_dxp, tzhaar_wave, config_attackable");
		} else { 
			throwInvalid(c);
		}
	}
	
	/**
	 * Throws a lovely invalid message should the player's arguments be invalid.
	 * @param c	the player
	 */
	private static void throwInvalid(Player c) {
		c.sendMessage("Code red! Invalid var arguments specified! Try again?");
	}

}
