package org.osps.model.npcs.drops;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class BeardedGorillaDrops {
	
	private static final int[] RARE = { 11770, 11771, 11772, 11773, 12831, 12829, 6914, 11836,
			6916, 6918, 6920, 6922, 6924 };
	private static final int[] ULTRA_RARE = { 19478, 11832, 11834 };
	
	public static enum Stackable {
		
		KARAMBWAN_8(3145, 8),
		DRAGON_CHAINBODY_2(3141, 2),
		DRAGON_PLATELEGS_2(4088, 2),
		PKP_50(2996, 50),
		PKP_85(2996, 85),
		PKP_120(2996, 120),
		PKP_150(2996, 150),
		PKP_200(2996, 200),
		PKP_220(2996, 220),
		PKP_1000(2996, 1000);
		
		private int item;
		private int amount;
		
		private Stackable(int item, int amount) {
			this.item = item;
			this.amount = amount;
		}
		
		public int getItem() {
			return this.item;
		}
		
		public int getAmount() {
			return this.amount;
		}
		
	}
	
	private static final List<Stackable> STACKABLES = Collections.unmodifiableList(Arrays.asList(Stackable.values()));
	private static final int SIZE = STACKABLES.size();
	private static final Random RANDOM = new Random();
	
	public static Stackable stackableDrop() {
		return STACKABLES.get(RANDOM.nextInt(SIZE));
	}
	
	
	public static int rareDrop() {
		return RARE[(int)(Math.random()*RARE.length - 1)];
	}
	
	public static int ultraRareDrop() {
		return ULTRA_RARE[(int)(Math.random()*ULTRA_RARE.length - 1)];
	}

	public static void dropLoot(Player player, int i, int npcX, int npcY, int height) {
		int dropChance = Misc.random(0, 1000);
		int amount = 1;
		if (dropChance < 800) {
			Stackable drop = stackableDrop();
			Server.itemHandler.createGroundItem(player, drop.getItem(), npcX, npcY, height, drop.getAmount(), player.index);
		}  else if (dropChance < 990) {
			Server.itemHandler.createGroundItem(player, rareDrop(), npcX, npcY, height, amount, player.index);
		} else if (dropChance < 1000) {
			Server.itemHandler.createGroundItem(player, ultraRareDrop(), npcX, npcY, height, amount, player.index);
		}
	}
}
