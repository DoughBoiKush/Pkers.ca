package org.osps.model.npcs;

import org.osps.Server;
import org.osps.clip.Region;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerSave;

/**
 *
 * @author DF
 *
 **/

public class PetHandler {

	public static final int RATS_NEEDED_TO_GROW = 25;
	
	public static int pets[] = {6644, 6643, 6633, 6634,
								6641, 6628, 2055, 6636,
								6640, 3504, 5558, 6651,
								2128, 2129, 6637, 5560, 
								6629, 5557, 2127, 318, 
								6715, 6716, 6717, 6718, 
								6719, 6720, 6721, 3099};

	private static enum Pets {
		
		/*
		 * Metamorphis ID's
		 * Vetion: 5559
		 */

		GRAARDOR(12650, 6644), 
		KREE(12649, 6643), 
		ZILYANA(12651, 6633), 
		TSUROTH(12652, 6634), 
		PRIME(12644, 6629), 
		REX(12645, 6641), 
		SUPREME(12643, 6628), 
		CHAOS(11995, 2055), 
		KBD(12653, 6636), 
		KRAKEN(12655, 6640), 
		CALLISTO(13178, 5558),
		MOLE(12646, 6651), 
		ZULRAH_GREEN(12921, 2127), 
		ZULRAH_RED(12939, 2128), 
		ZULRAH_BLUE(12940, 2129), 
		KAL_PRINCESS(12654, 6637), 
		VETION(13179, 5560), 
		VENENATIS(13177, 5557),
		SMOKE_DEV(12648, 6655),
		SCORPIA(13181, 5547),
		CORP(12816, 318),
		HERON(13320, 6715),
		TZREK_JAD(13225, 5892),
		ROCK_GOLEM(13321, 6716),
		BEAVER(13322, 6717),
		BABY_CHINCHOMPA(13323, 6718),
		BABY_CHINCHOMPA1(13324, 6719),
		BABY_CHINCHOMPA2(13325, 6720),
		BABY_CHINCHOMPA3(13326, 6721),
		HELLPUPPY(13247, 3099);
		private int itemId, npcId;

		private Pets(int itemId, int npcId) {
			this.itemId = itemId;
			this.npcId = npcId;
		}
		
		public int getItemId() {
			return itemId;
		}
		
		public int getNpcId() {
			return npcId;
		}
	}

	public static Pets forItem(int id) {
		for (Pets t : Pets.values()) {
			if (t.itemId == id) {
				return t;
			}
		}
		return null;
	}

	public static Pets forNpc(int id) {
		for (Pets t : Pets.values()) {
			if (t.npcId == id) {
				return t;
			}
		}
		return null;
	}

	public static int getItemIdForNpcId(int npcId) {
		return forNpc(npcId).itemId;
	}
	public static void ownerDeath(Player c) {
		if (!c.insure) {
			c.summonId = -1;
			c.hasNpc = false;
			c.sendMessage("@blu@You have died and lost your pet, you should've had pet insurance!");
		} else
		if (c.insure) {
			c.getItems().addItemToBank(c.spawnId, 1);
			c.spawnId = -1;
			c.summonId = -1;
			c.hasNpc = false;
			c.sendMessage("@blu@Your pet was insured and was added to your bank!");
		}
	}
	public static boolean spawnPet(Player c, int itemId, int slot,
			boolean ignore) {
		Pets pets = forItem(itemId);
		if (pets != null) {
			int npcId = pets.npcId;
			if (c.hasNpc && !ignore) {
				c.sendMessage("You already have a follower!");
				return true;
			}
			int offsetX = 0;
			int offsetY = 0;
			if (Region
					.getClipping(c.getX() - 1, c.getY(), c.heightLevel, -1, 0)) {
				offsetX = -1;
			} else if (Region.getClipping(c.getX() + 1, c.getY(),
					c.heightLevel, 1, 0)) {
				offsetX = 1;
			} else if (Region.getClipping(c.getX(), c.getY() - 1,
					c.heightLevel, 0, -1)) {
				offsetY = -1;
			} else if (Region.getClipping(c.getX(), c.getY() + 1,
					c.heightLevel, 0, 1)) {
				offsetY = 1;
			}
			Server.npcHandler.spawnNpc3(c, npcId, c.absX + offsetX, c.absY
					+ offsetY, c.heightLevel, 0, 120, 25, 200, 200, true,
					false, true);
			
			
			//ADD FOLLOWING HERE
			if (!ignore) {
				c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
				c.hasNpc = true;
				c.summonId = itemId;
				PlayerSave.saveGame(c);
			}
			return true;
		}
		return false;
	}
	

	public static boolean pickupPet(Player c, int npcId) {	
		Pets pets = forNpc(npcId);
		if (pets != null) {
			if (NPCHandler.npcs[c.rememberNpcIndex].spawnedBy == c.index && c.summonId == pets.itemId) {
				int itemId = pets.itemId;
				if (c.getItems().freeSlots() > 0) {
					NPCHandler.npcs[c.rememberNpcIndex].absX = 0;
					NPCHandler.npcs[c.rememberNpcIndex].absY = 0;
					NPCHandler.npcs[c.rememberNpcIndex] = null;
					c.animation(827);
					c.getItems().addItem(itemId, 1);
					c.summonId = -1;
					c.hasNpc = false;
					c.sendMessage("You pick up your pet.");
				} else {
					c.sendMessage("You do not have enough inventory space to do this.");
				}
			} else {
				c.sendMessage("This is not your pet.");
			}
			return true;
		}
		return false;
	}

	public static boolean talktoPet(Player c, int npcId) {
		Pets pets = forNpc(npcId);
		if (pets != null) {
			if (NPCHandler.npcs[c.rememberNpcIndex].spawnedBy == c.index) {
				switch (npcId) {
				case 4441:
					c.getDH().sendDialogues(14000, 3200);
					break;
				case 4439:
					c.getDH().sendDialogues(14003, 3200);
					break;
				case 4440:
					c.getDH().sendDialogues(14006, 3200);
					break;
				case 4446:
					c.getDH().sendDialogues(14009, 3200);
					break;
				case 4442:
					c.getDH().sendDialogues(14011, 3200);
					break;
				case 4438:
					c.getDH().sendDialogues(14014, 3200);
					break;
				case 4435:
					c.getDH().sendDialogues(14017, 4435);
					break;
				}
			} else {
				c.sendMessage("This is not your pet.");
			}
			return true;
		}
		return false;
	}
	
	public static boolean bankPet(Player c, int npcId) {
		for (Pets pet : Pets.values()) {
			if (npcId == pet.getNpcId()) {
				if (!c.hasNpc || c.summonId != pet.itemId || NPCHandler.npcs[c.rememberNpcIndex].spawnedBy != c.index) {
					c.sendMessage("This is not your pet to talk to.");
					return false;
				}
				if (c.inTrade || c.inDuel || c.isShopping || c.inWild()) {
					return false;
				}
				
				c.getDH().sendDialogues(200000, npcId);
				
				return true;
			}
		}
		return false;
	}

	public static boolean Pets() {
		// TODO Auto-generated method stub
		return false;
	}

}