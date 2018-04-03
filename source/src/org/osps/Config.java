package org.osps;

import org.osps.model.players.Boundary;

//package org.osps;

public class Config {

	public static boolean BOUNTY_HUNTER_ACTIVE = true;
	public static boolean HUNGER_GAMES = true;

	public static boolean NEW_DUEL_ARENA_ACTIVE = true;

	public static boolean PLACEHOLDER_ECONOMY = false;

	public final static boolean CAPTCHA_REQUIRED = false;
	public final static boolean IMAGE_CAPTCHA = true;

	/**
	 * Version of client for updates - By Mikey96
	 */

	public static final int VERSION_OF_CLIENT = 3;

	/**
	 * DO NOT REMOVE OR CHANGE THIS UNLESS YOU WANT THE NEW COMBAT SYSTEM.
	 */
	public static boolean NEW_COMBAT_TEST = true;

	/**
	 * Enable or disable server debugging.
	 */
	public static boolean SERVER_DEBUG = true;

	/**
	 * Your server name.
	 */
	public static final String SERVER_NAME = "Pkers.Ca";

	/**
	 * The welcome message displayed once logged in the server.
	 */
	public static final String WELCOME_MESSAGE = "Pkers.Ca";

	/**
	 * The current message of the day, displayed once a player has logged in.
	 */
	public static final String MOTD = "Welcome to Pkers.Ca!";

	/**
	 * A URL to your server forums. Not necessary needed.
	 */
	public static final String FORUMS = "";

	/**
	 * The client version you are using. Not necessary needed.
	 */
	public static final int CLIENT_VERSION = 317;

	/**
	 * The delay it takes to type and or send a message.
	 */
	public static int MESSAGE_DELAY = 6000;

	public static boolean sendServerPackets = false;

	public static final int[] CHEATPACKETS =
			/** P1****P2***P3***P4****P5***P6**P7**P8 ***/
			{ 7376, 7575, 7227, 8904, 5096, 9002, 2330, 7826 };
	/**
	 * The highest amount ID. Change is not needed here unless loading items
	 * higher than the 667 revision.
	 */
	public static final int ITEM_LIMIT = 21048;

	public static final int TOKENS_ID = 13204;

	/**
	 * An integer needed for the above code.
	 */
	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;

	/**
	 * The size of a players bank.
	 */
	public static final int BANK_SIZE = 350;

	/**
	 * The max amount of players until your server is full.
	 */
	public static final int MAX_PLAYERS = 1024;

	/**
	 * The delay of logging in from connections. Do not change this.
	 */
	public static final int CONNECTION_DELAY = 100;

	/**
	 * How many IP addresses can connect from the same network until the server
	 * rejects another.
	 */
	public static final int IPS_ALLOWED = 2;

	/**
	 * Change to true if you want to stop the world --8. Can cause screen
	 * freezes on SilabSoft Clients. Change is not needed.
	 */
	public static final boolean WORLD_LIST_FIX = false;

	public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting" };

	public static final int HAT = 0;
	public static final int CAPE = 1;
	public static final int AMULET = 2;
	public static final int WEAPON = 3;
	public static final int CHEST = 4;
	public static final int SHIELD = 5;
	public static final int LEGS = 7;
	public static final int HANDS = 9;
	public static final int FEET = 10;
	public static final int RING = 12;
	public static final int ARROWS = 13;

	
	/**
	 * Infinity amount buy from shop.
	 */
	public static final int[] INFINITY 		= 	{9244,9245,9243,890,892,11212,868,6522,4212,9185,853,861,4740,3749,2503,2493,2497,2491,2581,2577,6585,6733,10374,10370,10372,10368,6524,10499,1169,1129,10390,10386,10388,10384,2499,493,2487,1095,10382,10378,10380,10376,1135,1099,1065,1063,
		4151,11804,11806,11808,11838,5698,1215,4587,1305,1434,3204,1377,7158,4153,6528,6523,6527,3202,1333,1319,6317,1327,
		4716,4718,4720,4722,4732,4734,4736,4738,4724,4726,4728,4730,4745,4747,4749,4751,4708,4710,4712,4714,4753,4755,4757,4759,11832,11834,3140,4087,6918,6916,6924,6922,6920,11826,11828,11830,4585,
		10828,1127,1079,8850,7462,6737,11840,6585,5574,5575,5576,8849,7459,6735,3105,1725,1161,1123,1073,1199,1159,1121,1071,1197,6623,6617,6625,6633,6619,6629,1187,1540,3751,3755,3753,11335,6524,4224,
		4675,1381,1383,1385,1387,6562,6912,6731,2415,2417,2416,3840,3842,3844,12612,9075,554,555,556,557,558,559,562,563,565,561,560,563,566,8008,8007,8009,8010,8011,
		4089,4091,4093,4095,4097,9096,9097,9098,4099,4101,4103,4105,4107,9100,9099,9101,4109,4111,4113,4115,4117,7400,7399,7398,3385,3387,3389,6109,6107,6108,6110,6106,3840,3842,3844,12612,2414,2412,2413,2579,
		7455,7459,7462,8845,8849,8850,3105,1949,2497,2491,2581,2577,1129,10499,542,544,2651,8950,4502,7003,6188,7122,7128,7110,7594,579,6109,6107,6108,5574,5575,5576,6666,7534,3840,3842,3843,12610,12612,12608,
		2436,2440,2442,6685,3024,2444,2446,7060,391,385,373,10925,2434,3040,113,2437,2441,2443,2437,6686,3025,2445,2447,3025,7061,392,386,374,3025,10926,2435,3041,114,12905,12906,
		6585,11128,1712,1725,1729,1727,1731,11090,6737,6735,6733,6731,2550,2552
		};
	
	/**
	 * Items that cannot be sold in any stores.
	 */
	public static final int[] ITEM_SELLABLE = { 11770, 1171, 11772, 11773, 11772, 6570, 9008, 9007, 2944, 11907, 6686, 6685, 3025, 3024, 2946, 2949, 2950, 2437,
			2436, 2441, 2440, 3041, 3040, 2445, 2444, 4091, 4090, 4092, 4093, 4094, 3840, 3839, 3841, 2411, 2412, 2413,
			4096, 4097, 4098, 7460, 7461, 7462, 10827, 10828, 10829, 1711, 1712, 1713, 2549, 2550, 2551, 4674, 4675,
			4676, 1200, 1201, 1202, 1126, 1127, 1128, 4150, 5697, 5698, 5699, 2413, 2414, 2415, 11839, 11840, 11841,
			2449, 2550, 2551, 1078, 1079, 1080, 2502, 2503, 2504, 2496, 2497, 2498, 12899, 12926, 12931, 12954, 12006,
			11864, 11865, 15573, 8135, 7460, 7461, 7462, 1200, 1201, 1202, 1126, 1127, 1128, 2413, 2414, 2415, 3104,
			3105, 3106, 6106, 6107, 6108, 6109, 4501, 4502, 4503, 6567, 6568, 6569, 3841, 3842, 3843, 2496, 2497, 2498,
			7457, 7458, 7459, 9184, 9185, 19481, 9186, 891, 892, 893, 3748, 3749, 3750, 2502, 2503, 2504, 1186, 1187,
			1188, 12853, 611, 1959, 1960, 9920, 9921, 9922, 9923, 9924, 9925, 1050, 1051, 1044, 1045, 1046, 1047, 1048,
			1049, 1052, 3842, 3844, 3840, 8844, 8845, 8846, 8847, 8848, 8849, 10551, 10548, 6570, 7462, 7461, 7460,
			7459, 7458, 7457, 7456, 7455, 7454, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784, 9799, 9805,
			9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809,
			9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759,
			9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 8839, 8840, 8842,
			11663, 11664, 11665, 10499, 995, 12650, 12649, 8850, 7582, 12651, 12652, 15567, 12644, 12645, 12643, 15568,
			12653, 12655, 15571, 15572, 12806, 12807, 962, 551, 13140, 13107, 13115, 13120, 13124, 13132, 13103, 13136,
			13128, 13111, 13144, 12637, 12638, 12639, 6665, 6666, 12813, 12814, 12815, 12810, 12811, 12812, 12845, 9920,
			12887, 12888, 12889, 12890, 12891, 12892, 12893, 12894, 12895, 12896, 11919, 12956, 12957, 12958, 12959,
			2990, 2991, 2992, 2993, 2994, 2995, 12432, 12434, 2651, 8950, 8928, 12412, 12432, 12434, 2639, 2641, 2643,
			12321, 12323, 12325, 11280, 394, 430, 12335, 12414, 2653, 12436, 12434, 10396, 12337, 12327, 12325, 12323,
			2645, 2643, 2641, 536, 11943, 1753, 1747 };
	/**
	 * Items that cannot be traded or staked.
	 */
	public static final int[] ITEM_TRADEABLE = { 11770, 1171, 11772, 11773, 11772, 6570, 9008, 9007, 2944, 10025, 13280, 11941, 12791, 13329, 13331, 13333,
			13335, 13337, 13323, 13324, 13325, 13326, 13247, 2950, 2946, 2949, 12921, 8850, 12939, 12940, 12785, 12954,
			15573, 8135, 11864, 11865, 12432, 12389, 12373, 12379, 12369, 12367, 12365, 12363, 10507, 6822, 6824, 6826,
			6828, 6830, 6832, 6834, 6836, 6838, 6840, 6842, 6844, 6846, 6848, 6850, 12853, 1959, 1960, 611, 1959, 1960,
			9920, 9921, 9922, 9923, 9924, 9925, 12845, 3842, 3844, 3840, 8844, 8845, 8846, 8847, 8848, 8849, 10551,
			6570, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802,
			9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758,
			9761, 9764, 9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753,
			9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810,
			9765, 8839, 8840, 8842, 10271, 10273, 10275, 10277, 10279, 10269, 11663, 11664, 15572, 11665, 10499, 10548,
			15098, 12650, 12649, 12651, 12652, 15567, 12644, 12645, 12643, 15568, 12653, 12655, 15571, 7582, 12848,
			12855, 12856, 12808, 12809, 12806, 12807, 7449, 12796, 12748, 12749, 12750, 12751, 12752, 12753, 12754,
			12755, 12756, 12211, 12205, 12207, 12213, 12241, 12235, 12237, 12243, 12283, 12277, 12279, 12281, 12309,
			12311, 12313, 12514, 12299, 12301, 12303, 12305, 12307, 12321, 12323, 12325, 12516, 536, 11943, 1753, 1747,

			12806, 12807, 962, 11770, 11771, 11772, 11773, 12648, 13181, 12816, 13320, 13321, 13322, 13323, 13324,
			13325, 13326, 551, 13140, 13107, 13115, 13120, 13124, 13132, 13103, 13136, 13128, 13111, 13144, 12637,
			12638, 12639, 6665, 6666, 12813, 12814, 12815, 12810, 12811, 12812, 12845, 9920, 12887, 12888, 12889, 12890,
			12891, 12892, 12893, 12894, 12895, 12896, 11919, 12956, 12957, 12958, 12959, 2990, 2991, 2992, 2993, 2994,
			2995, 12432, 12434, 2651, 8950, 8928, 12412, 12432, 12434, 2639, 2641, 2643, 12321, 12323, 12325, 11280,
			394, 430, 12335, 12414, 2653, 12436, 12434, 10396, 12337, 12327, 12325, 12323, 2645, 2643, 2641,

			12816, 12650, 12649, 12651, 12652, 12644, 12645, 12643, 11995, 12653, 13178, 12655, 12646, 12921, 12939,
			12940, 12654, 13179, 13177, 13180, 13181, 12647, 12548 };

	public static final int[] ITEMS_KEPT_ON_DEATH = {
		
    };
	public static final int[] DROP_AND_DELETE_ON_DEATH = {

			6822, 6824, 6826, 6828, 6830, 6832, 6834, 6836, 6838, 6840, 6842, 6844, 6846, 6848, 6850, 10507, 10025 };

	/**
	 * Items that cannot be dropped.
	 */
	public static final int[] UNDROPPABLE_ITEMS = { 13282, 13280, 13342, 13329, 13330, 13331, 13332, 13333, 13334, 13335, 13336, 13337, 13338, 9008, 9007, 2944, 12899, 11907, 12432, 12389, 12373, 12379, 12369,
			12367, 12365, 12363, 6822, 6824, 6826, 6828, 6830, 6832, 6834, 6836, 6838, 6840, 6842, 6844, 6846, 6848,
			6850 };

	/**
	 * Items that will be warned before destroyed.
	 */
	public static final int[] WARNING_ITEMS = { Config.TOKENS_ID, 11802, 11803 /* AGS */ , /* scrolls */ 2690, 2691,
			2692, 2996 /* PKP */ , 4151, 4152 /* whip */ , /* arma cbow */ 11875, 11876, 12914, 12915, 12916, 12917,
			12918, 12919, 12920 /* ANTI_VEN */ , 10551 /* Torso */ , 10548 /* Fighter_Hat */ , 7462 /* B_GLOVES */ ,
			1893, 1894, 6814, 6815, 1807, 1806, 1613, 1614, 11230, 11231, 11232, 11233, 11234, 3140, 3141, 12414, 12415,
			12417, 12418, 6570, 12954, 4151, Config.TOKENS_ID, 13637, 13638, 13636, 13635, 13634, 13633, 13631, 13629,
			13627, 1993, 1994, 4087, 4088, 4181, 4180, 4585, 4586, 12415, 12416, 4566, 2572, 2573, 11980, 11981, 11982,
			11983, 11984, 11985, 11986, 11987, 11988, 11989, 12397, 12398, 9005, 9006, 11937, 11936, 11934, 11935, 377,
			378, 2513, 2514, 3140, 3141, 12141, 12534, 12535, 1755, 1756, 5601, 1592, 1593, 1595, 1596, 1597, 1598,
			7509 /* ROCK_CAKE */ , 6737, 6738 /* B_RING */ , 6735, 6736 /* WARRIOR_RING */ , 1419, 11804 /* BGS */ ,
			11806, 11807 /* SGS */ , 11808, 11809 /* ZGS */ , 11838, 11839 /* SS */ , 11832, 11833 /* BCP */ , 11834,
			11835 /* TASSETS */ , 11283, 11284, 11285, 11286, 11287 /* DFS */ , 11235, 11236 /* DARK_BOW */ , 11785,
			11786, /* ACB */ 10735, 10734, 2577, 2578 /* RANGER_BOOTS */ , 2581, 2582 /* ROBIN_HAT */ , 6733,
			6734 /* ARCHER_RING */ , 6914, 6915 /* Master_Wand */ , 6889,
			6890 /* Mages'_Book */ , 6731, 6732 /* SEERS_RING */ , 6916, 6917, 6918, 6919, 6920, 6921, 6922, 6923, 6924,
			6925 /* INFINITY */ , 12855 /* Hunters_Honour */ , 12856 /* Rogues_Revenge */ , 12804,
			12805 /* Sara_Tear */ , 12851 /* Amulet_Damned */ , 12849,
			12527 /* Fury(o) */ , 11837, 12850, 12852, 12853, 12820, 12608 /* Grante_Clamp */ , 12610 /* Book_War */ ,
			12612 /* Book_Law */ , 12757 /* Book_Darkness */ , 12526 /* Blue_Dbow */ , 12527, 12528, 12529, 12530,
			12531, 12759, 12760 /* Green_Dbow */ , 12761, 12762 /* Yellow_Dbow */ , 12763, 12764 /* White_Dbow */ ,
			12457 /* Dark_Infinity_Hat */ , 12458 /* Dark_Inifinity_Robe */ , 12459, 12419, 12420, 1242, 12421,
			12846 /* (MORE INFINITY) */ , 12817, 12818, 12819 /* Elysian_Spirit */ , 12821, 12822,
			12823 /* Spectral_Spirit */ , 12825, 12826, 12827 /* Arcane_Spirit */ , 12829, 12830 /* Spiri_Shield */ ,
			12831, 12832 /* Blessed_Spirit */ , 11773 /* B_RING_(i) */ ,
			11771 /* ARCHER_RING_(i) */ , 11770 /* SEER_RING_(i) */ , 11772, 11810, 11811 /* Armadyl_Hilt */ , 11812,
			11813 /* Bandos_Hilt */ , 11814, 11815 /* Saradomin_Hilt */ , 11816, 11817 /* Zamorak_Hilt */ , 11818,
			11819 /* GS_SHARD1 */ , 11820, 11821 /* GS_SHARD2 */ , 11822, 11823 /* GS_SHARD3 */ , 11824,
			11825 /* ZAMORAK_SPEAR */ , 11826, 11827 /* ARMA_HELM */ , 11828, 11829 /* ARMDAYL_CHEST */ , 11830,
			11831 /* ARMA_SKIRT */ , 11836, 11837 /* BANDOS_BOOTS */ , 11847 /* BLACK_HWEEN */ , 11850,
			11851 /* GRACE_HOOD */ , 11852, 11853 /* GRACE_CAPE */ , 11854, 11855 /* GRACE_TOP */ , 11856,
			11857 /* GRACE_LEG */ , 11858, 11859 /* GRACE_GLOVE */ , 11860, 11861 /* GRACE_BOOT */ ,
			11861 /* BLACK_PHAT */ , 11863 /* RAINBOW_PHAT */ , 11864, 11865, 8901, 2839 /* SLAYER_HELM */ , 11889,
			11890 /* Zamorak_Hasta */ , 11905, 11906, 11907, 11908, 11909 /* TRIDENT_OF_SEAS */ , 11919, 12956, 12957,
			12958, 12959, /* COW_SET */ 11920, 11921 /* D_PICK */ , 11924, 11925 /* MALEDICTION_WARD */ ,
			11926 /* ODIUM WARD */ , 11928, 11929, 11930 /* ODIUM_SHARDS */ , 11931, 11932,
			11933 /* MALEDICTION SHARDS */ , 11941, 11942 /* LOOTING_BAG */ , 11990, 11991 /* FEDORA */ , 12002,
			12003 /* OCCULT_NECK */ , 12004, 12005, 12006 /* KRAKEN_TENT */ , 12954 /* D_DEFENDER */ , 12960, 12962,
			12964, 12968, 12970, 11791, 11792, 11794, 11795, 11796, 11797, 11798, 11799, 11800, 11801, 11805, 12809,
			12810, 12811, 12812, 12813, 12814, 12815, 12436, 13208, 13207, 11927, 13206, 13576, 2678, 4067, 13182, 1037,
			4084, 4083, 6199, 12905, 12906, 12907, 12908, 12909, 12910, 12911, 12912, 12913, 12914, 12915, 12916, 12917,
			12918, 12919, 12920, 1039, 1041, 1043, 1045, 1047, 1049, 2422, 4561, 10476, 11730, 11731, 11732, 11733,
			11734, 12806, 12807, 12808, 13196, 13197, 13198, 13199, 13080, 13081, 20770, 20771, 20772,

			1050, 6737, 6733, 6731, 6735, 12954, 6570, 7462, 13140, 13107, 13115, 13120, 13124, 13131, 13124, 13103,
			13136, 13128, 13111, 13144, 12813, 12814, 12815, 12810, 12811, 12812, 12887, 12888, 12889, 12890, 12891,
			12892, 12893, 12894, 12895, 12896, 11919, 12956, 12957, 12958, 12959, 12637, 12638, 6665, 6666, 2990, 2991,
			2992, 2993, 2994, 2995, 12845, 9920, 12359, 2651, 8950, 8928, 12412, 12432, 12434, 2639, 2641, 2643, 12321,
			12323, 12325, 11280, 10394, 340, 12335, 12514, 12537,

			8839, 8840, 8842, 11663, 11664, 11665, 11862,

			9747, 9748, 9749, 9750, 9751, 9752, 9753, 9754, 9755, 9756, 9757, 9758, 9759, 9760, 9761, 9762, 9763, 9764,
			9765, 9766, 9767, 9768, 9769, 9770, 9771, 9772, 9773, 9774, 9775, 9776, 9778, 9779, 9780, 9781, 9782, 9783,
			9784, 9785, 9786, 9787, 9788, 9789, 9790, 9791, 9792, 9793, 9794, 9795, 9796, 9797, 9798, 9799, 9800, 9801,
			9802, 9803, 9804, 9805, 9806, 9807, 9808, 9809, 9810, 9811, 9812, 9813, 9950, 9814, 10639, 10640, 10641,
			10642, 10643, 10644, 10645, 10646, 10647, 10648, 10649, 10650, 10651, 10652, 10653, 10654, 10655, 10656,
			10657, 10658, 10659, 10660, 10661, 10662, 13069, 13068,

			/* PETS */
			12816, 12650, 12649, 12651, 12652, 12644, 12645, 12643, 11995, 12653, 13178, 12655, 12646, 12921, 12939,
			12940, 12654, 13179, 13177, 13180, 13181, 12647, 12548,

			/* PARTYHAT_SET */
			13174, 13173, 13175, 13176, 12399, 12400,

			/* SIRE */
			13207, 13205,

			/* DONATION_SCROLLS */
			2701, 2700, 2699, 2698, 2697, 2679, 2677, 2677,

			12637, 12638, 12639, 12596, 12597,

			12601, 12602, 12603, 12604, 12605, 12606,

			/* BOOTS */
			13211, 13213, 13215,

			/* 3rd_Age */
			10330, 10331, 10332, 10333, 10334, 10335, 10336, 10337, 10338, 10339, 10340, 10341, 10342, 10343, 10344,
			10345, 10346, 10347, 10348, 10349, 10350, 10351, 10352, 10353, 12422, 12423, 12424, 12425, 12426, 12427,
			12437, 12438, 12425, 12426, 12424,

			6570, 10566, 10637, 12833, 12791,

			12877, 12878 /* DH_SET */ , 12873, 12874 /* GUTH_SET */ , 12875, 12876 /* VERAC_SET */ , 12879,
			12880 /* TORAG_SET */ , 12881, 12882 /* AHRIM_SET */ , 12883, 12884 /* KARIL_SET */ ,

			/* HOLIDAY_ITEMS */
			12887, 12888, 12889, 12890, 12891 /* SANTA */ ,

			12892, 12893, 12894, 12895, 12896, 12897, 12898 /* ANTI_SANTA */ ,

			2996, 995, 12695, 12696, 12701, 12702, 12697, 12698, 12699, 12700,

			/* ZULRAH */
			12921, 12922, 12923, 12924, 12925, 12926, 12927, 12929, 12928, 12931, 12932, 12933, 12934, 12938, 12904,
			12939, 12930, 12940, 12902, 12899, 12900, 12901, 12903,

			/* PARTYHATS */
			1038, 1040, 1042, 1044, 1046, 1048, 1050, 1053, 1055, 1057,

			/* MISC */
			12798, 12799, 12800, 12801, 12802, 12786, 12783, 12013, 12014, 12015, 12016, 12775, 12776, 12777, 1277,
			12779, 12780, 12781, 12782 };

	/**
	 * Items that are listed as fun weapons for duelling.
	 */
	public static final int[] FUN_WEAPONS = { 4151, 5698, 1231, 1215, 5680 };

	public static final String[] UNSPAWNABLE = { "ahrim", "reindeer", "token", "lamp", "warrior guild", "manta",
			"sea turtle", "tuna potato", "star bauble", "bauble", "tokkul", "grimy", "herb", "torag", "dharok",
			"overload", "tenderiser", "woolly", "bobble", "jester", "gilded", "legend", "hell", "dragon spear", "odium",
			"malediction", "callisto", "gods", "yrannical", "treasonous", "granite maul", "ancient mace",
			"super combat", "dragon halberd", "torstol", "d hally", "dragon hally", "karil", "defender icon",
			"attacker icon", "picture", "collector icon", "collecter icon", "healer icon", "crystal key", "essence",
			"3rd", "third", "bomb", "karamb", "guthan", "verac", "dark crab", "void", "uncut", "Restrict",
			"onyx amulet", "onyx ring", "spirit", "chisel", "statius", "vesta", "morrigan", "zuriel", "occult",
			"trident", "mystic smoke", "mystic steam", "tentacle", "dark bow", "ranger boots", "robin hood hat",
			"attack cape", "defence cape", "strength cape", "prayer cape", "constitution", "range cape", "ranged cape",
			"ranging cape", "magic cape", "herblore", "agility", "fletching", "crafting", "runecrafting", "runecraft",
			"farming", "hunter", "slayer", "summoning", "construction", "woodcutting", "firemaking", "fishing",
			"cooking", "smithing", "mining", "thieving", "arcane", "divine", "spectral", "wealth", "elysian", "spirit",
			"status", "hand cannon", "visage", "raw", "logs", "bar", "ore", "uncut", "dragon leather", "scroll",
			"hatchet", "iron axe", "steel axe", "bronze axe", "rune axe", "adamant axe", "mithril axe", "black axe",
			"dragon axe", "vesta", "pumpkin", "statius's", "zuriel", "morrigan", "dwarven", "statius", "fancy",
			"rubber", "sled", "flippers", "camo", "lederhosen", "mime", "lantern", "santa", "scythe", "bunny", "h'ween",
			"hween", "clue", "casket", "cash", "box", "cracker", "zuriel's", "Statius's", "torso", "fighter", "Statius",
			"skeleton", "chicken", "zamorak platebody", "guthix platebody", "saradomin plate", "grim reaper hood",
			"armadyl", "bandos", "armadyl cross", "graardor", "zilyana", "kree", "tsut", "mole", "kraken", "dagannoth",
			"king black dragon", "chaos ele", "staff of the dead", "staff of dead", "(i)", "ticket", "PK Point",
			"jester", "dragon defender", "fury", "mithril defender", "adamant defender", "rune defender", "elysian",
			"mystery box", "arcane", "chaotic", "Chaotic", "stream", "broken", "deg", "corrupt", "fire cape", "sigil",
			"godsword", "void seal", "lunar", "hilt", "(g)", "mage's book", "berserker ring", "warriors ring",
			"warrior ring", "warrior's ring", "archer", "archer's ring", "archer ring", "archers' ring", "seers' ring",
			"seer's ring", "seers ring", "mages' book", "wand", "(t)", "guthix", "zamorak", "saradomin", "scythe",
			"bunny ears", "zaryte bow", "armadyl battlestaff", "(i)", "infinity", "slayer", "korasi", "staff of light",
			"dice", "ardougne", "unarmed", "gloves", "dragon claws", "party", "santa", "completionist", "null", "coins",
			"tokhaar-kal", "tokhaar", "sanfew", "dragon defender", "zaryte", "coupon", "flippers", "dragonfire shield",
			"sled", "tzrek", "holiday tool", "ironman", "slayer helmet recipe", "ring of charos", "slayer gloves",
			"salve amulet", "nose peg", "earmuffs", "spiny helmet", "facemask", "rotten potato", "notes",
			"preserve prayer scroll", "rigour prayer scroll", "augury prayer scroll", "Package", "PartyHat", "Partyhat",
			"Santa", "spirit shield", "Elite void", "3rd", "Dice bag", "150$ scroll", "50$ scroll", "10$ scroll",
			"Primordial", "Eternal", "Pegasian", "Anguish", "Tome", "Third", "Berserker ring (i)", "Seers ring (i)",
			"Archers ring (i)", "Warrior ring (i)", "Armadyl godsword", "Staff of the dead", "Smouldering", "Fighter",
			"tentacle", "Fire cape", "Super combat", "partyhat", "h'ween", "halloween", "blowpipe", "Toxic blowpipe",
			"Bounty teleport scroll", "Halloween", "Odium", "Dark infinity", "Light infinity", "dark bow paint",
			"Volcanic", "Malediction", "Magic shortbow (i)", "Max cape", "max cape", "hilt", "Occult", "Graceful",
			"Pet", "granite clamp", "sigil", "Bunny", "Overload", "Crystal hal", "Magma", "Antisanta",
			"Rogue's revenge", "Saradomin's tear", "Cow", "Black mask", "Slayer helm", "slayer helm", "Baby",
			"Kalphite", "Vet'ion", "Sagacious spectacles", "musketeer", "ballista", "Ballista", "crystal hal",
			"Mysterious", "Looting", "Toxic", "Trident", "trident", "toxic", "Big pirate hat", "deerstalker",
			"Kodai wand", "fury", "Dragonfire", "Sled", "Dragon warhammer", "of anguish", "Ring of suffering",
			"Tormented bracelet", "afro", "tophat", "Hunter's honour", "crate", "torture", "Graceful", "Super combat",
			"fury (or)", "Draconic", "Serp", "Zenyte", "Onyx", "Royal", "briefcase", "halo", "Master wand",
			"Morrigan's", "Pk Points", "Zuriel", "infinity", "bulwrak", "Ring of wealth", "Dark crab", "Raw dark crab",
			"Angel Cape", "Deadly Slayer Cape", "PKers cape", "Donator Cape", "Elder maul", "Dinh's bulwark", "notes",
			"preserve prayer scroll", "rigour prayer scroll", "augury prayer scroll", "dragon bones",
			"lava dragon bones", "green dragonhide", "black dragonhide", "bandos", "armadyl", "bandos chestplate",
			"armadyl chestplate", "bandos tassets", "armadyl plateskirt", "ahrim", "arhim's", "karil", "karil's",
			"torag", "torag's", "guthan", "guthan's", "Dharok's armour set", "dharok", "dharok's", "verac", "verac's" };
	/**
	 * If administrators can trade or not.
	 */
	public static final boolean ADMIN_CAN_TRADE = true;

	/**
	 * If administrators can sell items or not.
	 */
	public static final boolean ADMIN_CAN_SELL_ITEMS = true;

	/**
	 * If administrators can drop items or not.
	 */
	public static final boolean ADMIN_DROP_ITEMS = true;

	/**
	 * Represents whether administrators are interactable.
	 */
	public static boolean ADMIN_ATTACKABLE = true;

	/**
	 * The starting location of your server.
	 */
	public static final int START_LOCATION_X = 3087;
	public static final int START_LOCATION_Y = 3500;

	/**
	 * The re-spawn point of when someone dies.
	 */
	public static final int RESPAWN_X = 3087;
	public static final int RESPAWN_Y = 3500;

	/**
	 * The re-spawn point of when a duel ends.
	 */
	public static final int DUELING_RESPAWN_X = 3362;
	public static final int DUELING_RESPAWN_Y = 3263;

	/**
	 * The point in where you spawn in a duel. Do not change this.
	 */
	public static final int RANDOM_DUELING_RESPAWN = 0;

	/**
	 * The level in which you can not teleport in the wild, and higher.
	 */
	public static final int NO_TELEPORT_WILD_LEVEL = 20;

	/**
	 * The time, in game cycles that the skull above a player should exist for.
	 * Every game cycle is 600ms, every minute has 60 seconds. Therefor there
	 * are 100 cycles in 1 minute. .600 * 100 = 60.
	 */
	public static final int SKULL_TIMER = 500;

	/**
	 * The maximum time for a player skull with an extension in the length.
	 */
	public static final int EXTENDED_SKULL_TIMER = 2000;

	/**
	 * How long the teleport block effect takes.
	 */
	public static final int TELEBLOCK_DELAY = 20000;

	/**
	 * Single and multi player killing zones.
	 */
	public static final boolean SINGLE_AND_MULTI_ZONES = true;

	/**
	 * Wilderness levels and combat level differences. Used when attacking
	 * players.
	 */
	public static final boolean COMBAT_LEVEL_DIFFERENCE = true;

	/**
	 * Combat level requirements needed to wield items.
	 */
	public static final boolean itemRequirements = true;

	public static final int ATTACK = 0;
	public static final int DEFENCE = 1;
	public static final int STRENGTH = 2;
	public static final int HITPOINTS = 3;
	public static final int RANGED = 4;
	public static final int PRAYER = 5;
	public static final int MAGIC = 6;
	public static final int COOKING = 7;
	public static final int WOODCUTTING = 16;
	public static final int FLETCHING = 9;
	public static final int FISHING = 10;
	public static final int FIREMAKING = 11;
	public static final int CRAFTING = 12;
	public static final int SMITHING = 13;
	public static final int MINING = 14;
	public static final int HERBLORE = 15;
	public static final int AGILITY = 16;
	public static final int THIEVING = 17;
	public static final int SLAYER = 18;
	public static final int FARMING = 19;
	public static final int RUNECRAFTING = 20;
	/**
	 * Combat experience rates.
	 */
	public static final int MELEE_EXP_RATE = 1;
	public static final int RANGE_EXP_RATE = 1;
	public static final int MAGIC_EXP_RATE = 1;

	/**
	 * Special server experience bonus rates. (Double experience weekend etc)
	 */

	public static final double SERVER_EXP_BONUS = 1;

	/**
	 * XP given when XP is boosted by a voting reward only
	 */
	public static final double SERVER_EXP_BONUS_BOOSTED = 1;
	/**
	 * XP given when XP is boosted by a voting reward and bonus mode
	 */
	public static final double SERVER_EXP_BONUS_WEEKEND_BOOSTED = 1;

	/**
	 * XP given when XP is boosted by bonus mode only
	 */
	public static final double SERVER_EXP_BONUS_WEEKEND = 1;

	public static boolean BONUS_WEEKEND = false;

	public static boolean CYBER_MONDAY = false;

	public static boolean mySql = false;
	
	public static boolean ChristmasDonation = false;
	
	public static boolean doublePKPointsWeekend = false;
	/**
	 * How fast the special attack bar refills.
	 */
	public static final int INCREASE_SPECIAL_AMOUNT = 17500;

	/**
	 * If you need more than one prayer point to use prayer.
	 */
	public static final boolean PRAYER_POINTS_REQUIRED = true;

	/**
	 * If you need a certain prayer level to use a certain prayer.
	 */
	public static final boolean PRAYER_LEVEL_REQUIRED = true;

	/**
	 * If you need a certain magic level to use a certain spell.
	 */
	public static final boolean MAGIC_LEVEL_REQUIRED = true;

	/**
	 * How long the god charge spell lasts.
	 */
	public static final int GOD_SPELL_CHARGE = 300000;

	/**
	 * If you need runes to use magic spells.
	 */
	public static final boolean RUNES_REQUIRED = true;

	/**
	 * If you need correct arrows to use with bows.
	 */
	public static final boolean CORRECT_ARROWS = true;

	/**
	 * If the crystal bow degrades.
	 */
	public static final boolean CRYSTAL_BOW_DEGRADES = true;

	/**
	 * How often the server saves data.
	 */
	public static final int SAVE_TIMER = 60; // Saves every one minute.

	/**
	 * How far NPCs can walk.
	 */
	public static final int NPC_RANDOM_WALK_DISTANCE = 50; // 5x5 square, NPCs
	// would be able to
	// walk 25 squares
	// around.

	/**
	 * How far NPCs can follow you when attacked.
	 */
	public static final int NPC_FOLLOW_DISTANCE = 10; // 10 squares

	/**
	 * NPCs that act as if they are dead. (For salve amulet, etc)
	 */
	public static final int[] UNDEAD_NPCS = { 90, 91, 92, 93, 94, 103, 104, 73, 74, 75, 76, 77 };
	// public static final int[] KRAKEN_NPCS = { 493 };
	public static final String[] UNDEAD = { "armoured zombie", "ankou", "banshee", "crawling hand", "dried zombie",
			"ghost", "ghostly warrior", "ghast", "mummy", "mighty banshee", "revenant imp", "revenant goblin",
			"revenant icefiend", "revenant pyrefiend", "revenant hobgoblin", "revenant vampyre", "revenant werewolf",
			"revenant cyclops", "revenant darkbeast", "revenant demon", "revenant ork", "revenant hellhound",
			"revenant knight", "revenant dragon", "shade", "skeleton", "skeleton brute", "skeleton thug",
			"skeleton warlord", "summoned zombie", "skorge", "tortured soul", "undead chicken", "undead cow",
			"undead one", "undead troll", "zombie", "zombie rat", "zogre" };
	/**
	 * Glory locations.
	 */
	public static final int EDGEVILLE_X = 3087;
	public static final int EDGEVILLE_Y = 3500;
	public static final String EDGEVILLE = "";
	public static final int AL_KHARID_X = 3293;
	public static final int AL_KHARID_Y = 3174;
	public static final String AL_KHARID = "";
	public static final int KARAMJA_X = 3087;
	public static final int KARAMJA_Y = 3500;
	public static final String KARAMJA = "";
	public static final int MAGEBANK_X = 2538;
	public static final int MAGEBANK_Y = 4716;
	public static final String MAGEBANK = "";

	/**
	 * Teleport spells.
	 **/
	/*
	 * Modern spells
	 */
	public static final int VARROCK_X = 3210;
	public static final int VARROCK_Y = 3424;
	public static final String VARROCK = "";

	public static final int LUMBY_X = 3222;
	public static final int LUMBY_Y = 3218;
	public static final String LUMBY = "";

	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;
	public static final String FALADOR = "";

	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;
	public static final String CAMELOT = "";

	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;
	public static final String ARDOUGNE = "";

	public static final int WATCHTOWER_X = 3087;
	public static final int WATCHTOWER_Y = 3500;
	public static final String WATCHTOWER = "";

	public static final int TROLLHEIM_X = 3243;
	public static final int TROLLHEIM_Y = 3513;
	public static final String TROLLHEIM = "";

	/*
	 * Ancient spells
	 */
	public static final int PADDEWWA_X = 3098;
	public static final int PADDEWWA_Y = 9884;

	public static final int SENNTISTEN_X = 3322;
	public static final int SENNTISTEN_Y = 3336;

	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;

	public static final int LASSAR_X = 3006;
	public static final int LASSAR_Y = 3471;

	public static final int DAREEYAK_X = 3161;
	public static final int DAREEYAK_Y = 3671;

	public static final int CARRALLANGAR_X = 3156;
	public static final int CARRALLANGAR_Y = 3666;

	public static final int ANNAKARL_X = 3288;
	public static final int ANNAKARL_Y = 3886;

	public static final int GHORROCK_X = 2977;
	public static final int GHORROCK_Y = 3873;
	
	public static final Boundary FUN_PK_BOUNDARY = new Boundary(3269, 4760, 3388, 4861);
	public static final Boundary FUN_PK_MULTI_BOUNDARY = new Boundary(3269, 4760, 3327, 4861);
	public static final Boundary ROCK_CRABSWILD = new Boundary(2682, 3736, 2665, 3643);
	public static final Boundary ROCK_CRABSWILD2 = new Boundary(3269, 4760, 3327, 4861);
	

	
	/**
	 * Timeout time.
	 */
	public static final int TIMEOUT = 20;

	/**
	 * Cycle time.
	 */
	public static final int CYCLE_TIME = 600;

	/**
	 * Buffer size.
	 */
	public static final int BUFFER_SIZE = 4092;

	/**
	 * Slayer Variables.
	 */
	public static final int[][] SLAYER_TASKS = { { 1, 87, 90, 4, 5 }, // Low
																		// Tasks
			{ 6, 7, 8, 9, 10 }, // Medium tasks
			{ 11, 12, 13, 14, 15 }, // High tasks
			{ 1, 1, 15, 20, 25 }, // Low requirements
			{ 30, 35, 40, 45, 50 }, // Medium requirements
			{ 60, 75, 80, 85, 90 } }; // High requirements

	/**
	 * Skill experience multipliers.
	 */
	public static final int WOODCUTTING_EXPERIENCE = 0;
	public static final int MINING_EXPERIENCE = 0;
	public static final int SMITHING_EXPERIENCE = 0;
	public static final int FARMING_EXPERIENCE = 0;
	public static final int FIREMAKING_EXPERIENCE = 0;
	public static final int HERBLORE_EXPERIENCE = 0;
	public static final int FISHING_EXPERIENCE = 0;
	public static final int AGILITY_EXPERIENCE = 0;
	public static final int PRAYER_EXPERIENCE = 0;
	public static final int RUNECRAFTING_EXPERIENCE = 0;
	public static final int CRAFTING_EXPERIENCE = 0;
	public static final int THIEVING_EXPERIENCE = 0;
	public static final int SLAYER_EXPERIENCE = 0;
	public static final int COOKING_EXPERIENCE = 0;
	public static final int FLETCHING_EXPERIENCE = 0;
	public static boolean ROCKCRABSWILD() {
		// TODO Auto-generated method stub
		return false;
	}

}