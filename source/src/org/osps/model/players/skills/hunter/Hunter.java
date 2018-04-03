package org.osps.model.players.skills.hunter;

import java.util.HashMap;
import java.util.Random;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.npcs.NPC;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.skills.hunter.Hunter.impData;
import org.osps.util.Misc;

public class Hunter {

	public enum impData {
		BABY_IMPLING(1645, 1, 50, 11238),
		YOUNG_IMPLING(1646, 22, 75, 11240),
		GOURMET_IMPLING(1647, 28, 101, 11242),
		EARTH_IMPLING(1648, 36, 154, 11244),
		ESSENCE_IMPLING(1649, 42, 170, 11246),
		ECLECTIC_IMPLING(1650, 50, 180, 11248),
		NATURE_IMPLING(1651, 58, 190, 11250),
		MAGPIE_IMPLING(1652, 65, 200, 11252),
		NINJA_IMPLING(1653, 74, 245, 11254),
		DRAGON_IMPLING(1654, 83, 310, 11256);

		public static HashMap<Integer, impData> implings = new HashMap<>();

		static {
			for(impData t : impData.values()) {
				implings.put(t.impType, t);
			}
		}

		private int impType;
		private int levelRequired;
		private int experience;
		private int itemId;

		impData(final int impType, final int levelRequired, final int experience, final int itemId) {
			this.impType = impType;
			this.levelRequired = levelRequired;
			this.experience = experience;
			this.itemId = itemId;
		}
		
		private int getImpId() {
			return impType;
		}
		
		private int getLevelRequired() {
			return levelRequired;
		}
		
		private int getXp() {
			return experience;
		}
		
		private int getJar() {
			return itemId;
		}
		
		private String getName() {
			return Misc.optimizeText(toString().toLowerCase().replaceAll("_", " "));
		}
		
		public static impData forId(int id) {
			return implings.get(id);
		}
		public static int getRandom(int[] array) {
		    int rnd = new Random().nextInt(array.length);
		    return array[rnd];
		}
	public static void Catch(Player c, int npcType, final int npcidx) {
		
		impData t = impData.implings.get(npcType);
		int chance = Misc.random(50);
		int[] items = new int[]{13323, 13324, 13325, 13326};
		int chance1 = Misc.random(6000);
		int chance2 = Misc.random(3500);

		if (System.currentTimeMillis() - c.hunting < 1500) {
			return;
		}
		
		if (c.catchingImp) {
			return;
		}

		if (c.playerEquipment[c.playerWeapon] != 10010 && c.playerEquipment[c.playerWeapon] != 11259) {
			c.sendMessage("You need a butterfly net");
			return;
		}

		if (c.playerLevel[22] < t.getLevelRequired()) {
			c.sendMessage("You need a hunter of " + t.getLevelRequired() + " to catch a " + t.getName() + "");
			return;
		}

		if (!c.getItems().playerHasItem(11260)) {
			c.sendMessage("You need a impling jar");
			return;
		}
		c.catchingImp = true;
		if (Misc.random(10) >= ((c.playerLevel[22] - 10) / 10) + 1) {
			c.animation(6605);
			c.sendMessage("You fail catching the " + t.getName() + "!");
			c.hunting = System.currentTimeMillis();
		} else {
			if(chance == 0)	{
				c.getPA().rewardPoints(2, "You were lucky enough to be rewarded with @blu@2 @bla@PK Points.");
			}
			if (chance1 == 6000){
			if (npcType == 1645 || npcType == 1646 || npcType == 1647
			 || npcType == 1648	|| npcType == 1649){
				c.sendMessage("@red@You receive a skilling pet. It has been added to your bank. Congratulations!");
				c.getItems().addItemToBank(getRandom(items), 1);
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
				Player c2 = PlayerHandler.players[j];
				c2.sendMessage("<col=006600>" + c.playerName + " received a skilling pet: 1 x Random Chincompa.");
						}
					}
				}
			}
			if (chance2 == 3500){
			if (npcType == 1650 || npcType == 1651 || npcType == 1652
					 || npcType == 1653	|| npcType == 1654){
						c.sendMessage("@red@You receive a skilling pet. It has been added to your bank. Congratulations!");
						c.getItems().addItemToBank(getRandom(items), 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
						Player c2 = PlayerHandler.players[j];
						c2.sendMessage("<col=006600>" + c.playerName + " received a skilling pet: 1 x Random Chincompa.");
						}
					}
				}
			}
			c.animation(6605);
			c.getItems().addItem(t.getJar(), 1);
			c.getItems().deleteItem(11260, c.getItems().getItemSlot(11260), 1);
			c.getPA().addSkillXP(t.getXp() * 15, 22);
			c.sendMessage("You catch the Imp");
			IMPLING_DEATH(c, npcType, npcidx);
			c.hunting = System.currentTimeMillis();
		}
		c.catchingImp = false;
	}
		
		public static void IMPLING_RESPAWN(Player c, int id, int x, int y, int z) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				int i = 0;
				public void execute(CycleEventContainer container) {
					if(i++ == 1) {
						NPCHandler.newNPC(id, x, y, z, 1, 0, 0, 0, 0);
						//System.out.println("Hunter Respawn");
						container.stop();
					}
				}

				public void stop() {
				}
			}, 2);
		}
		
		public static void IMPLING_DEATH(Player c, int npcType, int impling) {
			impData t = impData.implings.get(npcType);
			NPC n = NPCHandler.npcs[impling];
			if(n != null && n.npcType == t.getImpId()) {
					int x = n.absX;
					int y = n.absY;
					int z = n.heightLevel;
					n.absX = 0;
					n.absY = 0;
					n.isDead = true;
					NPCHandler.npcs[impling] = null;
					IMPLING_RESPAWN(c, npcType, x, y, z);
			}
		}
	}
}
