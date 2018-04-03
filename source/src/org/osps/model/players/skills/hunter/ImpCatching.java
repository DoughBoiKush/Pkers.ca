package org.osps.model.players.skills.hunter;

import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;
import org.osps.model.players.skills.SkillHandler;
import org.osps.util.Misc;

/*
 * Imp Catching - Mikey96.
 */

public class ImpCatching extends SkillHandler {
	
	public static void impDeath(Player c, int npcType, int idx) {
		for (HunterData.ImpData imp : HunterData.ImpData.values()) {
			NPC n = NPCHandler.npcs[idx];
			if(n != null && n.npcType == imp.getNpcId()) {
				int x = n.absX;
				int y = n.absY;
				int z = n.heightLevel;
				n.absX = 0;
				n.absY = 0;
				n.isDead = true;
				NPCHandler.npcs[idx] = null;
				//respawnImp(c, npcType, x, y, z);
			}
		}
	}

	
	public static int netAnim = 1; // to be added.
	
	public static boolean catchImpling(Player c, int npcId) {
		for (HunterData.ImpData imp : HunterData.ImpData.values()) {
			if (npcId == imp.getNpcId()) {
				if (c.getItems().freeSlots() <= 0) {
					c.sendMessage("You don't have enough inventory space to catch this.");
					return false;
				}
				if (c.playerLevel[22] < imp.getLevelReq()) {
					c.sendMessage("You need a hunter level of " + imp.getLevelReq() + " to catch this impling.");
					return false;
				}
				if (!c.getItems().playerHasItem(10010) && !c.getItems().isWearingItem(10010)) {
					c.sendMessage("You need a butterfly net to catch this impling.");
					return false;
				}
				if (Misc.random(imp.getChance()) == 0) {
					c.sendMessage("You fail to catch the impling, try again.");
					return false;
				}
				
				//c.animation(netAnim);
				c.sendMessage("You successfully catch a " + NPCHandler.getNpcListName(imp.getNpcId()) + " and gain " + imp.getExp() + " experience.");
				c.getItems().addItem(imp.getJarId(), 1);
				impDeath(c, npcId, npcId);
				
				return true;	
			}
		}
		return false;
	}

}
