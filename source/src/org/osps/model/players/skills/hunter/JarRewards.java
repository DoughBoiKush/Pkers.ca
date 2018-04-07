package org.osps.model.players.skills.hunter;

import java.util.HashMap;
import java.util.Random;

import org.osps.model.players.Player;
import org.osps.util.Misc;

public class JarRewards {
	
	public static final Random random = new Random();
	public enum ImpRewards {
		BABY(11238, new int[][] {{5291, 1}, {5292, 1}, {5293, 1}, {5294, 1}, {5295, 1}, {5296, 1}, {5297, 1}, {5298, 1}, {5299, 1}, {5300, 1}, {5301, 1}, {5302, 1}, {5303, 1}, {5304, 1}}, new int[][] {{5304, 10}}), //Level 1 Hunter
		YOUNG(11240, new int[][] {{1778, 1}, {222, 1}, {236, 1}, {1526, 1}, {224, 1}, {9737, 1}, {232, 1}, {226, 1}, {240, 1}, {244, 1}, {6050, 1},}, new int[][] {{228, 10}}), //Level 22 Hunter
		GOURMENT(11242, new int[][] {{7947, 5}, {386, 3}, {3145, 1}, {11937, 1}, {13441, 1}}, new int[][] {{11937, 10}}), //Level 28 Hunter
		EARTH(11244, new int[][] {{1512, 5}, {1522, 4}, {1520, 3}, {1518, 3}, {1516, 2}, {1514, 2}}, new int[][] {{1514, 25}}), //Level 36 Hunter
		ESSENCE(11246, new int[][] {{560, 5}, {565, 5}, {566, 5}, {9075, 5}, {562, 5}, {563, 5}, {561, 4}}, new int[][] {{1437, 50}}), //Level 42 Hunter
		ECLECTIC(11248, new int[][] {{448, 4}, {454, 5}, {450, 2}, {452, 1}}, new int[][] {{452, 15}}), //Level 50 Hunter
		NATURE(11250, new int[][] {{200, 1}, {202, 1}, {204, 1}, {206, 1}, {208, 1}, {3050, 1}, {210, 1}, {212, 1}, {212, 1}, {214, 1}, {218, 1}, {216, 1}, {2486, 1}, {218, 1}, {220, 1}}, new int[][] {{6688, 5}}), //Level 58 Hunter
		MAGPIE(11252, new int[][] {{1746, 1}, {2506, 1}, {2508, 1}, {2510, 1}}, new int[][] {{2510, 15}} ), //Level 65 Hunter
		NINJA(11254, new int[][] {{1894, 2}, {6815, 1}, {1806, 1}, {1614, 1}, {1994, 1}}, new int[][] {{995, 35_000}}), //Level 74 Hunter
		DRAGON(11256, new int[][] {{2352, 5}, {2358, 3}, {2354, 5}, {2360, 4}, {2362, 3}, {2364, 2}}, new int[][] {{2364, 15}} ); //Level 83 Hunter

		public static HashMap<Integer, ImpRewards> impReward = new HashMap<>();

		static {
			for(ImpRewards t : ImpRewards.values()) {
				impReward.put(t.itemId, t);
			}
		}

		private int itemId;
		private int[][] rewards;
		private int[][] rareRewards;

		ImpRewards(int itemId, int[][] rewards, int[][] rareRewards) {
			this.itemId = itemId;
			this.rewards = rewards;
			this.rareRewards = rareRewards;
		}
		public int getItemId() {
			return itemId;
		}
		public int[][] getRewards() {
			return rewards;
		}
		public int[][] getRareRewards() {
			return rareRewards;
		}

		public static void getReward(Player c, int itemId) {
			if(c.getItems().freeSlots() < 2) {
				c.sendMessage("You need atleast 2 free slot");
				return;
			}

			ImpRewards t = ImpRewards.impReward.get(itemId);
			c.getItems().deleteItem(t.getItemId(), c.getItems().getItemSlot(t.getItemId()), 1);
			int r = random.nextInt(t.getRewards().length);
			int rare = random.nextInt(t.getRareRewards().length);
			int chance = Misc.random(150);
			if(chance == 1) {
				c.sendMessage("Congratulations you've received a @blu@RARE @bla@reward: @blu@" + c.getItems().getItemName(t.getRareRewards()[rare][0]) + ": @bla@x@blu@" + t.getRareRewards()[rare][1] + "");
				c.getItems().addItem(t.getRareRewards()[rare][0], t.getRareRewards()[rare][1]);
			}
			if(t.getRewards()[r].length == 3) {
				int amount = t.getRewards()[r][1] + random.nextInt(t.getRewards()[r][2] - t.getRewards()[r][1]);
				c.getItems().addItem(t.getRewards()[r][0], amount);
			} else {
				c.getItems().addItem(t.getRewards()[r][0], t.getRewards()[r][1]);
			}
			if(Misc.random(15) == 0) {
				c.sendMessage("The impling jar breaks but you do get a reward");
			} else {
				c.sendMessage("You open the impling jar and get a reward");
			}
		}
	}
}
