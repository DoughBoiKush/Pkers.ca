package org.osps.model.npcs.drops;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class RevenantDrops {
	
	private static final int[] RARE = { 4087, 2513, 1187, 11335, 11840, 4151, 10548, 10551, 12954, 11088,
			11235 };
	private static final int[] ULTRA_RARE = { 19553, 19547, 13870, 13873, 13876,
			13887, 13893, 13884, 13890, 13896, 13858, 13861, 13863 };
	
	public static enum Stackable {
		
		DRAGON_BONES_2(537, 2),
		DRAGON_BONES_3(537, 3),
		DRAGON_BONES_5(537, 5),
		DRAGON_JAVELINS_5(19484, 5),
		DRAGON_JAVELINS_10(19484, 10),
		DRAGON_JAVELINS_20(19484, 20),
		DRAGON_DARTS_10(11230, 10),
		DRAGON_DARTS_20(11230, 20),
		DRAGON_DARTS_30(11230, 30),
		PKP_50(2996, 50),
		PKP_85(2996, 85),
		PKP_110(2996, 110),
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
		double multiplyDrop = Misc.random(1, 101);
		int dropChance = Misc.random(0, 1000);
		int amount = 1;
		if (multiplyDrop == 100) {
			player.sendMessage("You lucked out and got @dre@10x@bla@ loot from the revenant!");
			amount *= 10;
		} else if (multiplyDrop > 97) {
			player.sendMessage("You lucked out and got @dre@3x@bla@ loot from the revenant!");
			amount *= 3;
		} else if (multiplyDrop > 95) {
			player.sendMessage("You lucked out and got @dre@2x@bla@ loot from the revenant!");
			amount *= 2;	
		}
		Server.itemHandler.createGroundItem(player, 4278, npcX, npcY, height, Misc.random(25, 100) * amount, player.index);
		if (dropChance < 600) {
			Stackable drop = stackableDrop();
			Server.itemHandler.createGroundItem(player, drop.getItem(), npcX, npcY, height, drop.getAmount() * amount, player.index);
		}  else if (dropChance < 990) {
			Server.itemHandler.createGroundItem(player, rareDrop(), npcX, npcY, height, amount, player.index);
		} else if (dropChance < 1000) {
			Server.itemHandler.createGroundItem(player, ultraRareDrop(), npcX, npcY, height, amount, player.index);
		}
	}

}
