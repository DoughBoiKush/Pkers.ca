package org.osps.model.players.combat.magic;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.combat.Hitmark;


public class CastOnOther extends MagicRequirements {

	public static boolean castOnOtherSpells(Player c) {
		int[] spells = {
			12435, 12455, 12425, 30298, 30290, 30282, 
		};
		for(int i = 0; i < spells.length; i++) {
			if(c.castingSpellId == spells[i]) {
				return true;
			}
		}
		return false;
	}

	public static void healOther(Player c, int i) {
		Player p = PlayerHandler.players[i];
		double hpPercent = c.playerLevel[3] * 0.75;
		if(!hasRequiredLevel(c, 92)) {
			c.sendMessage("You need to have a magic level of 92 to cast this spell.");
			return;
		}
		if(!hasRunes(c, new int[] {ASTRAL, LAW, BLOOD}, new int[] {3, 3, 1})) {
			return;
		}
		if(p.playerLevel[3] < 1) {
			return;
		}

		deleteRunes(c, new int[] {ASTRAL, LAW, BLOOD}, new int[] {3, 3, 1});

		if(p.playerLevel[3] + (int)hpPercent >= Player.getLevelForXP(p.playerXP[3])) {
			if(p.playerLevel[3] > (int)hpPercent) {
				hpPercent = (p.playerLevel[3] - (int)hpPercent);
			} else {
				hpPercent = ((int)hpPercent - p.playerLevel[3]);
			}
		}

		if(p.playerLevel[3] >= Player.getLevelForXP(p.playerXP[3])) {
			c.sendMessage(""+p.playerName+" already has full hitpoints.");
			p.playerLevel[3] = Player.getLevelForXP(p.playerXP[3]);
			return;
		}

		c.appendDamage((int) hpPercent, hpPercent >= 1.0 ? Hitmark.HIT : Hitmark.MISS); 

		p.playerLevel[3] += (int)hpPercent;

		p.getPA().refreshSkill(3);
		c.getPA().refreshSkill(3);
		c.updateRequired = true;
		c.face(PlayerHandler.players[i]);

		c.animation(4411);
		c.gfx100(727);
	}

	public static void specialEnergyTransfer(Player c, int i) {
		Player p = PlayerHandler.players[i];
		if(!hasRequiredLevel(c, 91)) {
			c.sendMessage("You need to have a magic level of 91 to cast this spell.");
			return;
		}
		if(p.specAmount >= 5.0) {
			c.sendMessage("You can't transfer special energy to "+p.playerName+"!");
			return;
		}
		if(!hasRunes(c, new int[] {ASTRAL, LAW, NATURE}, new int[] {3, 2, 1})) {
			return;
		}
		deleteRunes(c, new int[] {ASTRAL, LAW, NATURE}, new int[] {3, 2, 1});
		c.face(PlayerHandler.players[i]);
		c.animation(4411);
		p.gfx0(1069);
		p.specAmount += 5;
		c.specAmount -= 5;
		c.getItems().updateSpecialBar();
		p.getItems().updateSpecialBar();
		p.sendMessage("Your special energy has been restored by 50%!");
		c.sendMessage("You transfer 50% of your energy to "+p.playerName+".");
	}

	public static void castOtherVengeance(Player c, int i) {
		Player p = PlayerHandler.players[i];
		if(!hasRequiredLevel(c, 93)) {
			c.sendMessage("You need to have a magic level of 93 to cast this spell.");
			return;
		}
		if(p.vengOn) {
			c.sendMessage("This player has already casted vengeance!");
			return;
		}
		if (Server.currentServerTime - c.lastCast < 30000) {
			c.sendMessage("You must wait 30 seconds before casting this again.");
			return;
		}
		if(!hasRunes(c, new int[] {ASTRAL, DEATH, EARTH }, new int[] {3, 2, 10})) {
			return;
		}
		deleteRunes(c, new int[] {ASTRAL, DEATH, EARTH }, new int[] {3, 2, 10});
		c.face(PlayerHandler.players[i]);
		c.animation(4411);
        p.vengOn = true;
        p.getPA().sendFrame126("vengtimer:50", 1811);
		p.lastVeng = 0;
		p.gfx100(725);
		p.sendMessage("You have the power of vengeance!");
	}

	public static void teleOtherDistance(Player c, int type, int i) {
		Player castOn = PlayerHandler.players[i];
		int[][] data = {
			{74, SOUL, LAW, EARTH, 1, 1, 1},
			{82, SOUL, LAW, WATER, 1, 1, 1},
			{90, SOUL, LAW, -1, 2, 1, -1},
		};
		if(!hasRequiredLevel(c, data[type][0])) {
			c.sendMessage("You need to have a magic level of "+data[type][0]+" to cast this spell.");
			return;
		}
		if(!hasRunes(c, new int[] {data[type][1], data[type][2], data[type][3] }, new int[] {data[type][4], data[type][5], data[type][6]})) {
			return;
		}
		deleteRunes(c, new int[] {data[type][1], data[type][2], data[type][3] }, new int[] {data[type][4], data[type][5], data[type][6]});
		String[] location = {
			"Lumbridge", "Falador", "Camelot",
		};
		c.face(PlayerHandler.players[i]);
		c.animation(1818);
		c.gfx0(343);
		if(castOn != null) {
			if(castOn.distanceToPoint(c.absX, c.absY) <= 15){
				if (c.heightLevel == castOn.heightLevel) {
					castOn.getPA().sendFrame126(location[type], 12560);
					castOn.getPA().sendFrame126(c.playerName, 12558);
					castOn.getPA().showInterface(12468);
					castOn.teleotherType = type;
				}
			}
		}
	}

	public static void teleOtherLocation(final Player c, final int i, boolean decline) {
		c.getPA().removeAllWindows();
		final int[][] coords = {
			{3222, 3218}, // LUMBRIDGE
			{2964, 3378}, // FALADOR
			{2757, 3477}, // CAMELOT
		};
		if(!decline) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					c.animation(715);
					c.teleportToX = coords[c.teleotherType][0];
					c.teleportToY = coords[c.teleotherType][1];
					c.teleotherType = -1;
					container.stop();
				}
				@Override
				public void stop() {

				}
			}, 3);
			c.animation(1816);
			c.gfx100(342);
		}
	}
}