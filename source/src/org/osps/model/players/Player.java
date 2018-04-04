package org.osps.model.players;

import org.osps.Config;
import org.osps.Connection;
import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.Entity;
import org.osps.model.Location;
import org.osps.model.content.Flowers;
import org.osps.model.content.InstancedArea;
import org.osps.model.content.Lootbag;
import org.osps.model.content.PunishmentPanel;
import org.osps.model.content.QuickPrayer;
import org.osps.model.content.RandomEventInterface;
import org.osps.model.content.RunePouch;
import org.osps.model.content.Streak;
import org.osps.model.content.clan.Clan;
import org.osps.model.content.dialogue.Dialogue;
import org.osps.model.content.hs.Highscore;
import org.osps.model.content.kill_streaks.Killstreak;
import org.osps.model.content.presets.Presets;
import org.osps.model.content.quickspawn.QuickSpawn;
import org.osps.model.content.teleport.Position;
import org.osps.model.items.*;
import org.osps.model.items.bank.Bank;
import org.osps.model.items.bank.BankPin;
import org.osps.model.items.pouch.GemBagDefinition;
import org.osps.model.items.pouch.HerbSackDefinition;
import org.osps.model.items.pouch.ItemPouch;
import org.osps.model.minigames.Barrows;
import org.osps.model.minigames.Dicing;
import org.osps.model.minigames.FishingTourney;
import org.osps.model.minigames.BlastMine.BlastMine;
import org.osps.model.minigames.FishingTourney.FishingSession;
import org.osps.model.minigames.bounty_hunter.BountyHunter;
import org.osps.model.minigames.bounty_hunter.TargetState;
import org.osps.model.minigames.fight_cave.FightCave;
import org.osps.model.minigames.hunger.HungerManager;
import org.osps.model.minigames.pest_control.PestControl;
import org.osps.model.minigames.pest_control.PestControlRewards;
import org.osps.model.minigames.warriors_guild.WarriorsGuild;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.clan_wars.ClanWarsMap;
import org.osps.model.multiplayer_session.duel.Duel;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.multiplayer_session.trade.Trade;
import org.osps.model.npcs.*;
import org.osps.model.npcs.boss.Armadyl.Armadyl;
import org.osps.model.npcs.boss.Bandos.Bandos;
import org.osps.model.npcs.boss.Cerberus.Cerberus;
import org.osps.model.npcs.boss.Kalphite.Kalphite;
import org.osps.model.npcs.boss.Kalphite.Queen;
import org.osps.model.npcs.boss.Kraken.Kraken;
import org.osps.model.npcs.boss.Lizardman.Lizardman;
import org.osps.model.npcs.boss.Saradomin.Saradomin;
import org.osps.model.npcs.boss.Zamorak.Zamorak;
import org.osps.model.npcs.boss.instances.InstancedAreaManager;
import org.osps.model.npcs.boss.zulrah.Zulrah;
import org.osps.model.players.combat.*;
import org.osps.model.players.packets.ClickItem;
import org.osps.model.players.skills.*;
import org.osps.model.players.skills.agility.AgilityHandler;
import org.osps.model.players.skills.agility.impl.AlkharidRoof;
import org.osps.model.players.skills.agility.impl.CanifisRoof;
import org.osps.model.players.skills.agility.impl.DraynorRoof;
import org.osps.model.players.skills.agility.impl.GnomeAgility;
import org.osps.model.players.skills.agility.impl.SeersRoof;
import org.osps.model.players.skills.agility.impl.VarrockRoof;
import org.osps.model.players.skills.agility.impl.WildernessAgility;
import org.osps.model.players.skills.cooking.Cooking;
import org.osps.model.players.skills.crafting.Crafting;
import org.osps.model.players.skills.farming.Farming;
import org.osps.model.players.skills.herblore.Herblore;
import org.osps.model.players.skills.mining.Mining;
import org.osps.model.players.skills.prayer.Prayer;
import org.osps.model.players.skills.slayer.BossSlayer;
import org.osps.model.players.skills.slayer.EasyTask;
import org.osps.model.players.skills.slayer.HardTask;
import org.osps.model.players.skills.slayer.MediumTask;
import org.osps.model.players.skills.slayer.PVPSlayer;
import org.osps.model.players.skills.slayer.PVPSlayer.Task;
import org.osps.model.players.skills.slayer.Slayer;
import org.osps.model.players.skills.thieving.Thieving;
import org.osps.model.shops.ShopAssistant;
import org.osps.net.Packet;
import org.osps.net.Packet.Type;
import org.osps.net.outgoing.UnnecessaryPacketDropper;
import org.osps.util.Misc;
import org.osps.util.Stopwatch;
import org.osps.util.Stream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.osps.model.content.TeleportTab;
import org.osps.model.shops.playerOwned.Shop;

public class Player extends Entity {
	private RandomEventInterface randomEventInterface = new RandomEventInterface(this);
	private Mining mining = new Mining(this);
	private PestControlRewards pestControlRewards = new PestControlRewards(this);
	private WarriorsGuild warriorsGuild = new WarriorsGuild(this);
	public InstancedArea instancedArea;
	public Zulrah zulrah = new Zulrah(this);
	private Kraken kraken = new Kraken(this);
	private Kalphite kalphite = new Kalphite(this);
	private Bandos bandos = new Bandos(this);
	private Zamorak zamorak = new Zamorak(this);
	private Queen queen = new Queen(this);
	private Saradomin saradomin = new Saradomin(this);
	private Armadyl armadyl = new Armadyl(this);
	private Dialogue dialogue = null;
	public long lastClanTeleport;
	public int duration;
	public long plant;
	public static boolean attack;
	public static boolean defence;
	public static boolean strength;
	public static boolean hitpoints;
	public static boolean range;
	public static boolean magic;
	public static boolean prayerLevel;
        
        public Shop shopOpened = null;
        private Shop myShop = new Shop(this);
        public int shopSellAmount = 0;
        public int shopSellPrice = 0;
        public int shopSellItem = 0;
        public boolean shopBuyAmount = false;
        public int shopBuySlot = 0;
        
        
        public int teleTabPage = 0;
        
        public Shop shop() {
            return myShop;
        }
	
	public int[] titleView = {0, 0};
	public boolean[][] titleUnlocked = new boolean[3][20];
	
	
	private Flowers flower = new Flowers(this);
	public Flowers getFlowers() {
		return flower;
	}

	private Highscore highscore;

	public Highscore getHighscore() {
		return highscore;
	}

	public void setHighscore(Highscore highscore) {
		this.highscore = highscore;
	}
	private NPCDeathTracker npcDeathTracker = new NPCDeathTracker(this);
	private BossDeathTracker bossDeathTracker = new BossDeathTracker(this);
	private Lootbag lootbag = new Lootbag(this);
	private RunePouch runepouch = new RunePouch(this);
	private UnnecessaryPacketDropper packetDropper = new UnnecessaryPacketDropper();
	private DamageQueueEvent damageQueue = new DamageQueueEvent(this);
	private SeersRoof seers = new SeersRoof();
	private DraynorRoof draynor = new DraynorRoof();
	private AlkharidRoof alkharid = new AlkharidRoof();
	private VarrockRoof varrock = new VarrockRoof();
	private CanifisRoof canifis = new CanifisRoof();
	// private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Streak streak = new Streak(this);
	private HashMap<String, ArrayList<Damage>> damageReceived = new HashMap<>();
	private BountyHunter bountyHunter = new BountyHunter(this);
	private MysteryBox mysteryBox = new MysteryBox(this);
	private long lastContainerSearch;
	private PlayerKill playerKills;
	public String macAddress;
	public PlayerCannon playerCannon;
	private Duel duelSession = new Duel(this);
	private Player itemOnPlayer;
	private Presets presets = null;
	private Killstreak killstreaks;
	public FishingSession fishTourneySession = null;
	public int fishingTourneyPoints = 0;
	private PunishmentPanel punishmentPanel = new PunishmentPanel(this);
	private final QuickPrayer quick = new QuickPrayer();
	private final ItemPouch herbSack = new ItemPouch(this, "Herb Sack", 30, HerbSackDefinition.VALUES);
	private final ItemPouch gemBag = new ItemPouch(this, "Gem Bag", 60, GemBagDefinition.VALUES);
	public long newVar;
	public boolean EASY = false, MEDIUM = false, HARD = false, PVPTASK = false;
	public int specs;
	public int hasInteracted;
	public int lastX = absX;
	public int lastY = absY;
	public boolean usingObstacle = false;
	public boolean RANGE_ABILITY = false;
	public boolean Jumped = false;
	public boolean Digged = true;
	public boolean ironman = false;
	public boolean ARMADYL_INSTANCE = false;;
	public boolean BANDOS_INSTANCE = false;
	public boolean KALPHITE_INSTANCE = false;
	public boolean SARADOMIN_INSTANCE = false;
	public boolean ZAMORAK_INSTANCE = false;
	public boolean catchingImp = false;
	public int knockBack = 0;
	public boolean doingTutorial = false;
	public int usingKarils = 0;
	public int[] pouch = { 0, 0, 0, 0 };
	public boolean hungerGames = false;
	public int hungerPoints = 0;
	public boolean hunger1 = false, hunger2 = false;
	public long youtubeTimestamp;
	
	private Runnable walkInteractionTask;

	public void setWalkInteractionTask(Runnable walkInteractionTask) {
		this.walkInteractionTask = walkInteractionTask;
	}

	public void submitWalkInteractionTask() {
		if (walkInteractionTask == null) {
			return;
		}
		walkInteractionTask.run();
		walkInteractionTask = null;
	}

	/**
	 * Callisto
	 */

	public int CAST_KNOCK = 0;

	/**
	 * Cerberus
	 */

	public int CAST_ROCKS = 0;
	public int CAST_GHOSTS = 0;
	public boolean alreadySpawned = false;
	public int ticks = 0;
	public int MAGIC_ATTACK = 0;
	public int RANGE_ATTACK = 0;
	public int MELEE_ATTACK = 0;
	public int RANDOM = 0;
	public int RANDOM_MELEE = 0;
	public boolean TICKING_DAMAGE = false;
	public boolean hasDigged = false;
	public long digging;
	public boolean canWithdraw = true;
	public int SPAWN_MINIONS = 0;
	public long SPAWN_LIZARDS;
	public long JUMP_ABILITY;
	public long hunting;

	/*
	 * Mikey's new variables.
	 */

	private int clientVersion;

	public void setClientVersion(int clientVersion) {
		this.clientVersion = clientVersion;
	}

	public int getClientVersion() {
		return clientVersion;
	}

	public boolean enchantOpal;
	public boolean enchantSapphire;
	public boolean enchantJade;
	public boolean enchantPearl;
	public boolean enchantEmerald;
	public boolean enchantTopaz;
	public boolean enchantRuby;
	public boolean enchantDiamond;
	public boolean enchantDragon;
	public boolean enchantOnyx;

	public void openAccountPin() {
		if (!hasAccountPin) {
			getPA().showInterface(60000);
			isSettingAccountPin = true;
			return;
		} else
			sendMessage("You already have an account pin!");
	}

	public void resetAccountPin() {
		if (hasAccountPin) {
			getPA().showInterface(63000);
			sendMessage("You need to enter your pin to reset it!");
			return;
		} else
			sendMessage("You don't have an account pin?");
	}

	public void checkAccountPin() {
		if (hasAccountPin && getMacAddress().equalsIgnoreCase(getAccountPinMac())) {
			sendMessage("@red@You don't need to enter your account pin as you are on the same MAC address!");
			return;
		} else if (hasAccountPin) {
			canUsePackets = false;
			needsAccountPin = true;
			getPA().showInterface(64000);
			sendMessage("@red@Please enter your account pin to continue.");
		}
	}

	public double getDistanceToPoint(int x, int y) {
		double x1 = Math.pow((absX - x), 2);
		double y1 = Math.pow((absY - y), 2);
		return Math.sqrt(x1 + y1);
	}

	public boolean canUsePackets = true;;
	public boolean needsAccountPin;

	public boolean hasAccountPin;
	public boolean isSettingAccountPin;
	public String accountPin;
	public String accountPinMac;

	public void setAccountPin(String accountPin) {
		this.accountPin = accountPin;
		hasAccountPin = true;
	}

	public void setAccountPinMac(String accountPinMac) {
		this.accountPinMac = accountPinMac;
	}

	public String getAccountPin() {
		return accountPin;
	}

	public String getAccountPinMac() {
		return accountPinMac;
	}

	public int staffOfDeadCharge;

	public boolean inZeah() {
		if (absX > 1450 && absX < 1900 && absY > 3400 && absY < 3930) {
			return true;
		} else
			return false;
	}

	public boolean hasBeenPoisoned;
	public boolean hasBeenVenomed;

	/*
	 * End Of Mikey's New Variables.
	 */

	public void rspsdata(Player c, String username) {
		try {
			username = username.replaceAll(" ","_");
		    String secret = "8ace881b1f32ce53519b9d2b741f7c1c"; //YOUR SECRET KEY!
			URL url = new URL("http://app.gpay.io/api/runescape/"+username+"/"+secret);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String results = reader.readLine();
				if(results.toLowerCase().contains("!error:")){
					//Logger.log(this, "[GPAY]"+results);
				}else{
					String[] ary = results.split(",");
						 for(int i = 0; i < ary.length; i++){
					switch (ary[i]) {
					case "0":
						c.sendMessage("No donation found for: " + c.playerName);
						break;
					case "28265":
						GetScroll(10, 2690);
						break;
					case "16800":
						GetScroll(50, 2691);
						break;
					case "16801":
						GetScroll(150, 2692);
						break;
					case "16892":
						GetDice();
						break;
					}
				}
			}
		} catch (IOException e) {
		}
	}

	/*
	 * Author: ama
	 */
	public void GetScroll(int ScrollValue, int ScrollId) {
		if (getItems().freeSlots() >= 1) {
			getItems().addItem(ScrollId, 1);
		} else {
			getItems().addItemToBank(ScrollId, 1);
		}
		sendMessage("You have just donated for a $" + ScrollValue + " Donator Scroll. Thank you!");
		AnnounceDonation(ScrollValue);
	}
	public void GetDice() {
		if (getItems().freeSlots() >= 1) {
			getItems().addItem(15098, 1);
		} else {
			getItems().addItemToBank(15098, 1);
		}
		sendMessage("You have just donated for a $50 Dice bag. Thank you!");
		for (int z = 0; z < PlayerHandler.players.length; z++) {
			if (PlayerHandler.players[z] != null) {
				Player o = (Player) PlayerHandler.players[z];
				o.sendMessage("@red@" + Misc.optimizeText(playerName) + "@bla@" + " Has Just Donated For a $50 Dice bag!");
			}
		}
	}
	public void AnnounceDonation(int ScrollValue) {
		for (int z = 0; z < PlayerHandler.players.length; z++) {
			if (PlayerHandler.players[z] != null) {
				Player o = (Player) PlayerHandler.players[z];
				o.sendMessage("@red@" + Misc.optimizeText(playerName) + "@bla@" + " Has Just Donated For a $"
						+ ScrollValue + " Donator Scroll!");
			}
		}
	}
	public void AnnounceClaim(int ScrollValue) {
		for (int z = 0; z < PlayerHandler.players.length; z++) {
			if (PlayerHandler.players[z] != null) {
				Player o = (Player) PlayerHandler.players[z];
				o.sendMessage("@red@" + Misc.optimizeText(playerName) + "@bla@" + " Has Just Redeemed a $"
						+ ScrollValue + " Donator Scroll!");
			}
		}
	}
	public void PushPlayerCount() {
		// OnlinePlayerConnection.push(PlayerHandler.getPlayerCount());
	}

	/**
	 * Agility
	 */

	public boolean doingAgility = false;
	public boolean isSkilling;

	/**
	 * Obstacle Variables
	 */

	public boolean finishedLog = false, finishedNet1 = false, finishedBranch1 = false, finishedRope = false,
			finishedBranch2 = false, finishedNet2 = false, finishedPipe = false, finishedBarbRope = false,
			finishedBarbLog = false, finishedBarbNet = false, finishedBarbLedge = false, finishedBarbStairs = false,
			finishedBarbWall1 = false, finishedBarbWall2 = false, finishedBarbWall3 = false, finishedWildPipe = false,
			finishedWildRope = false, finishedWildStone = false, finishedWildLog = false, finishedWildRocks = false;

	public static final Boundary BOUNDARY_CORP = new Boundary(/* (X) */2970, /* (Y) */4370, /* (X) */2995,
			/* (Y) */4400);

	// public int[] playerXP = new int[25];

	public int[] getExperience() {
		return this.playerXP;
	}

	public int FocusPointX = -1;
	public int FocusPointY = -1;
	public int ZULRAH_CLICKS = 0;
	public int BANDOS_CLICKS = 0;
	public int ARMADYL_CLICKS = 0;
	public int ZAMORAK_CLICKS = 0;
	public int SARADOMIN_CLICKS = 0;
	public int KRAKEN_CLICKS = 0;
	public int KALPHITE_CLICKS = 0;
	public int ARMADYL_MINION = 0;
	public int BANDOS_MINION = 0;
	public int KALPHITE_MINION = 0;
	public int SARADOMIN_MINION = 0;
	public int ZAMORAK_MINION = 0;

	public void turnPlayerTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	// public long totalxp = 0L;

	public long getTotalXp() {
		int[] skillIds = { 0, 1, 2, 4, 6 };
		long totalxp = 0L;
		for (int i : skillIds) {
			totalxp += (double) playerXP[i];
			sendMessage("" + i + ":" + totalxp + ""); // debugging purposes
			getPA().addSkillXP(totalxp / 3, 3);
		}
		return totalxp;
	}

	/*
	 * public long getTotalXpp() { long totalxp = 0L; if(playerLevel[skill]) {
	 * totalxp += (double) playerXP[0]; getPA().addSkillXP(totalxp/3, 3); }
	 * return totalxp; }
	 */

	@Override
	public String toString() {
		return "player[" + playerName + "]";
	}

	public long playTimeTotal = 0;// total play time
	public long recordedLogin = 0;// the current time in ms set when you login.
									// used to calculate time logged in when you
									// logout
	public long abbyTime = 0;
	public boolean teleport;

	public Stream outStream = null;
	private Channel session;
	private Trade trade = new Trade(this);
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private BlastMine blastMine = new BlastMine(this);
	private CombatAssistant combat = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Friends friend = new Friends(this);
	private Ignores ignores = new Ignores(this);
	private Potions potions = new Potions(this);
	private PotionMixing potionMixing = new PotionMixing(this);
	private Food food = new Food(this);
	private Logs logs = new Logs(this);
	private SkillInterfaces skillInterfaces = new SkillInterfaces(this);

	/**
	 * Skill instances
	 */
	private PlayerAction playerAction = new PlayerAction(this);
	private Slayer slayer = new Slayer(this);
	private PVPSlayer pvpslayer = new PVPSlayer(this);
	private HardTask hard = new HardTask(this);
	private EasyTask easy = new EasyTask(this);
	private MediumTask medium = new MediumTask(this);
	private BossSlayer bossSlayer = new BossSlayer(this);
	private AgilityHandler agilityHandler = new AgilityHandler();
	private GnomeAgility gnomeAgility = new GnomeAgility();
	private WildernessAgility wildernessAgility = new WildernessAgility();
	private Agility agility = new Agility(this);
	private Cooking cooking = new Cooking();
	private Crafting crafting = new Crafting(this);
	private Prayer prayer = new Prayer(this);
	private Smithing smith = new Smithing(this);
	private FightCave fightcave = null;
	private SmithingInterface smithInt = new SmithingInterface(this);
	private Herblore herblore = new Herblore(this);
	private Thieving thieving = new Thieving(this);
	private Barrows barrows = new Barrows(this);
	public Stopwatch potionTimer = new Stopwatch();
	public Stopwatch timer = new Stopwatch();
	public Stopwatch sqlTimer = new Stopwatch();
	public int[] degradableItem = new int[Degrade.MAXIMUM_ITEMS];
	public boolean[] claimDegradableItem = new boolean[Degrade.MAXIMUM_ITEMS];
	private Optional<ItemCombination> currentCombination = Optional.empty();
	public static PlayerSave save;
	public static Player cliento2;
	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;
	public int returnCode = 2;
	private Future<?> currentTask;
	public int currentRegion = 0;
	public long lastRoll;
	public int diceItem;
	public int page;
	public int specRestore = 0;
	private int privateChat;
	public boolean slayerHelmetEffect;
	public boolean inArdiCC;
	public boolean lotteryAmount = false;
	public boolean attackSkill = false;
	public boolean strengthSkill = false;
	public boolean defenceSkill = false;
	public boolean mageSkill = false;
	public boolean rangeSkill = false;
	public boolean abbySpec = false;
	public int setDamage = 0;
	public boolean prayerSkill = false;
	public boolean healthSkill = false;
	private Skilling skilling = new Skilling(this);
	public int lastClickedItem;
	private int[] farmingSeedId = new int[Farming.MAX_PATCHES];
	private int[] farmingTime = new int[Farming.MAX_PATCHES];
	private int[] farmingState = new int[Farming.MAX_PATCHES];
	private int[] farmingHarvest = new int[Farming.MAX_PATCHES];
	private Farming farming = new Farming(this);
	public int pestControlDamage;
	public int bossDamage;
	private long bestZulrahTime;
	private LostItems lostItems;
	private int toxicBlowpipeCharge;
	private int toxicBlowpipeAmmo;
	private int toxicBlowpipeAmmoAmount;
	private int serpentineHelmCharge;
	private int toxicStaffOfDeadCharge;
	private int tridentCharge;
	private int toxicTridentCharge;
	public Clan clan;
	public String lastClanChat = "Pkers.Ca";
	public String onLoginClan = "01053";
	public String HitboxName = "";
	public String pokerName = "";
	public boolean hasPokerName;
	private long uniqueIdentifier;

	public Player(Channel s, int _playerId) {
		index = _playerId;
		rights = Rights.PLAYER;

		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) {
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) {
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}

		for (int i = 0; i < playerXP.length; i++) {
			if (i == 3) {
				playerXP[i] = 1300;
			} else {
				playerXP[i] = 0;
			}
		}
		for (int i = 0; i < Config.BANK_SIZE; i++) {
			bankItems[i] = 0;
		}

		for (int i = 0; i < Config.BANK_SIZE; i++) {
			bankItemsN[i] = 0;
		}

		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 0; // head
		playerAppearance[2] = 18;// Torso
		playerAppearance[3] = 26; // arms
		playerAppearance[4] = 33; // hands
		playerAppearance[5] = 36; // legs
		playerAppearance[6] = 42; // feet
		playerAppearance[7] = 10; // beard
		playerAppearance[8] = 0; // hair colour
		playerAppearance[9] = 0; // torso colour
		playerAppearance[10] = 0; // legs colour
		playerAppearance[11] = 0; // feet colour
		playerAppearance[12] = 0; // skin colour

		apset = 0;
		actionID = 0;

		playerEquipment[playerHat] = -1;
		playerEquipment[playerCape] = -1;
		playerEquipment[playerAmulet] = -1;
		playerEquipment[playerChest] = -1;
		playerEquipment[playerShield] = -1;
		playerEquipment[playerLegs] = -1;
		playerEquipment[playerHands] = -1;
		playerEquipment[playerFeet] = -1;
		playerEquipment[playerRing] = -1;
		playerEquipment[playerArrows] = -1;
		playerEquipment[playerWeapon] = -1;

		heightLevel = 0;

		teleportToX = Config.START_LOCATION_X;
		teleportToY = Config.START_LOCATION_Y;

		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
		this.session = s;
		// synchronized(this) {
		outStream = new Stream(new byte[Config.BUFFER_SIZE]);
		outStream.currentOffset = 0;
		// }
	}

	public Player getClient(String name) {
		name = name.toLowerCase();
		for (int i = 0; i < Config.MAX_PLAYERS; i++) {
			if (validClient(i)) {
				Player client = getClient(i);
				if (client.playerName.toLowerCase().equalsIgnoreCase(name)) {
					return client;
				}
			}
		}
		return null;
	}

	public void setForceMovement(int xx1, int yy1, int xx2, int yy2, int speedd1, int speedd2) {
		this.x1 = xx1;
		this.y1 = yy1;
		this.x2 = xx2;
		this.y2 = yy2;
		this.speed1 = speedd1;
		this.speed2 = speedd2;
		this.forceMovementUpdateRequired = true;
		this.updateRequired = true;
	}

	public void setForceMovement1(int xx1, int yy1, int xx2, int yy2, int speedd1, int speedd2, int height) {
		this.x1 = xx1;
		this.y1 = yy1;
		this.x2 = xx2;
		this.y2 = yy2;
		this.speed1 = speedd1;
		this.speed2 = speedd2;
		this.heightLevel = height;
		this.forceMovementUpdateRequired = true;
		this.updateRequired = true;
	}

	public void setForceMovement(int xx1, int yy1, int xx2, int yy2, int speedd1, int speedd2, int directionn) {
		this.x1 = xx1;
		this.y1 = yy1;
		this.x2 = xx2;
		this.y2 = yy2;
		this.speed1 = speedd1;
		this.speed2 = speedd2;
		this.direction = directionn;
		this.forceMovementUpdateRequired = true;
		this.updateRequired = true;
	}

	private Bank bank;

	public Bank getBank() {
		if (bank == null)
			bank = new Bank(this);
		return bank;
	}

	private BankPin pin;

	public BankPin getBankPin() {
		if (pin == null)
			pin = new BankPin(this);
		return pin;
	}

	public void sendMessage(String s, int color) {
		if (getOutStream() != null) {
			s = "<col=" + color + ">" + s + "</col>";
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}

	}

	public void sendDebugMessage(String s) {
		if (getRights().isDeveloper() || isDebug()) {
			if (getOutStream() != null) {
				outStream.createFrameVarSize(253);
				outStream.writeString(s);
				outStream.endFrameVarSize();
			}
		}
	}

	public Player getClient(int id) {
		return PlayerHandler.players[id];
	}

	public boolean validClient(int id) {
		if (id < 0 || id > Config.MAX_PLAYERS) {
			return false;
		}
		return validClient(getClient(id));
	}

	public boolean validClient(String name) {
		return validClient(getClient(name));
	}

	public boolean validClient(Player client) {
		return (client != null && !client.disconnected);
	}

	public void flushOutStream() {
		if (!session.isConnected() || disconnected || outStream.currentOffset == 0)
			return;

		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		outStream.currentOffset = 0;

	}

	private Map<Integer, TinterfaceText> interfaceText = new HashMap<>();

	public class TinterfaceText {
		public int id;
		public String currentState;

		public TinterfaceText(String s, int id) {
			this.currentState = s;
			this.id = id;
		}

	}

	public boolean checkPacket126Update(String text, int id) {
		if (text == null)
			return false;
		if (interfaceText.containsKey(id)) {
			TinterfaceText t = interfaceText.get(id);
			if (t != null && text.equals(t.currentState)) {
				return false;
			}
		}
		interfaceText.put(id, new TinterfaceText(text, id));
		return true;
	}

	public void sendClan(String name, String message, String clan, int rights) {
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		message = message.substring(0, 1).toUpperCase() + message.substring(1);
		clan = clan.substring(0, 1).toUpperCase() + clan.substring(1);
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}

	public static final int PACKET_SIZES[] = { 0, 0, 0, 1, -1, 0, 0, 0, 4, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 16, 0, -1, -1, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			6, 10, -1, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, /* 0 */4, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};

	public void resetRanks() {
		for (int i = 0; i < 10; i++) {
			ranks[i] = 0;
			rankPpl[i] = "";
		}
	}

	public int playerRank = 0;
	public static int[] ranks = new int[11];
	public static String[] rankPpl = new String[11];

	public void homeTeleport(int x, int y, int h) {
		if (homeTele == 9) {
			animation(4850);
		} else if (homeTele == 7) {
			animation(4853);
			gfx0(802);
		} else if (homeTele == 5) {
			animation(4855);
			gfx0(803);
		} else if (homeTele == 3) {
			animation(4857);
			gfx0(804);
		} else if (homeTele == 1) {
			homeTeleDelay = 0;
			homeTele = 0;
			teleportToX = x;
			teleportToY = y;
			heightLevel = h;
		}
	}

	public int amountDon;

	public void destruct() {
		if (session == null)
			return;
		if (zulrah.getInstancedZulrah() != null) {
			InstancedAreaManager.getSingleton().disposeOf(zulrah.getInstancedZulrah());
		}
		if (kraken.getInstancedKraken() != null) {
			InstancedAreaManager.getSingleton().disposeOf(kraken.getInstancedKraken());
		}
		if (kalphite.getInstancedKalphite() != null) {
			InstancedAreaManager.getSingleton().disposeOf(kalphite.getInstancedKalphite());
		}
		if (bandos.getInstancedBandos() != null) {
			InstancedAreaManager.getSingleton().disposeOf(bandos.getInstancedBandos());
		}
		if (zamorak.getInstancedZamorak() != null) {
			InstancedAreaManager.getSingleton().disposeOf(zamorak.getInstancedZamorak());
		}
		if (saradomin.getInstancedSaradomin() != null) {
			InstancedAreaManager.getSingleton().disposeOf(saradomin.getInstancedSaradomin());
		}
		if (armadyl.getInstancedArmadyl() != null) {
			InstancedAreaManager.getSingleton().disposeOf(armadyl.getInstancedArmadyl());
		}
		/*
		 * if (cerberus.getInstancedCerberus() != null) {
		 * InstancedAreaManager.getSingleton().disposeOf(cerberus.
		 * getInstancedCerberus()); }
		 */
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}
		if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
			PestControl.removeGameMember(this);
		}
		if (Boundary.isIn(this, PestControl.LOBBY_BOUNDARY)) {
			PestControl.removeFromLobby(this);
		}
		if (underAttackBy > 0 || underAttackBy2 > 0)
			return;
		if (playTimeTotal > 900000) {
			if (!rights.isAdministrator() && !rights.isOwner() && !rights.isDeveloper()) {
				new Thread(new HighscoresHandler(this)).start();
				new Thread(new PKHighscoresHandler(this)).start();
			}
		}
		if (disconnected == true) {
			saveCharacter = true;
		}
		Server.getMultiplayerSessionListener().removeOldRequests(this);
		if (clan != null) {
			clan.removeMember(this);
		}
		getFriends().notifyFriendsOfUpdate();
		PlayerHandler.playerCount--;
		Misc.println("[Logged out]: " + playerName + "");
		CycleEventHandler.getSingleton().stopEvents(this);
		disconnected = true;
		session.close();
		session = null;
		outStream = null;
		isActive = false;
		playerListSize = 0;
		for (int i = 0; i < maxPlayerListSize; i++)
			playerList[i] = null;
		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}

	
	public void loginMessage() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c2 = (Player)PlayerHandler.players[j];
				if (getRights().isOwner()) {
					c2.sendMessage("[@red@"+playerName+"@bla@] Has logged in. Feel free to ask him for help!");
				}
				else if (getRights().isAdministrator()) {
					c2.sendMessage("[@yel@"+playerName+"@bla@] Has logged in. Feel free to ask him for help!");
				}
				else if (getRights().isModerator()) {
					c2.sendMessage("[@or3@"+playerName+"@bla@] Has logged in. Feel free to ask him for help!");
				}
				else if (getRights().isHelper()) {
					c2.sendMessage("[@blu@"+playerName+"@bla@] Has logged in. Feel free to ask him for help!");
				}
				else if (getRights().isDeveloper()) {
					c2.sendMessage("[@yel@"+playerName+"@bla@] Has logged in. Feel free to ask him for help!");
				}
			}
		}
	}
	public void sendMessage(String s) {
		// synchronized (this) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}
	}

	public void setSidebarInterface(int menuId, int form) {

		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}
	}
	
	public int rank = 0;
	public static String[] top = new String[10];
	public static int[] kills = new int[10];

	public void calculateLevels(Player player) {
		int calcKills = (player.KC);
		;
		for (int i = 0; i < 10; i++) {
			if (calcKills >= kills[i]) {
				if (i == 0) {
					rank = i + 1;
					kills[i] = calcKills;
					top[i] = player.playerName;
				} else if (i < 10) {
					if (calcKills < kills[i - 1]) {
						rank = i + 1;
						kills[i] = calcKills;
						top[i] = player.playerName;
					}
				} else {
					if (calcKills < kills[i - 1]) {
						rank = 0;
					}
				}
			}
		}
	}
	
    public void highscores() {
        getPA().sendFrame126("Pkers.Ca PK Board", 42002);  //Title
        for(int i = 0; i < 10; i++) {
                if(ranks[i] > 0) {
                        getPA().sendFrame126(""+(i+1)+"): @gre@"+rankPpl[i]+ "", 42013+i);
                        getPA().sendFrame126(""+kills[i]+ "", 42023+i);		
                }
        }
        getPA().showInterface(42000);
        flushOutStream();
        resetRanks();
}

	public void loadHighscore() {
		getPA().sendFrame126("Pkers.Ca - Top PKers Online", 6399);
		getPA().sendFrame126("Close Window", 6401);
		getPA().sendFrame126(" ", 6402);
		getPA().sendFrame126(" ", 6403);
		getPA().sendFrame126(" ", 6404);
		getPA().sendFrame126(" ", 6405);
		getPA().sendFrame126("Pkers.Ca", 640);
		getPA().sendFrame126(" ", 6406);
		getPA().sendFrame126(" ", 6407);
		getPA().sendFrame126(" ", 6408);
		getPA().sendFrame126(" ", 6409);
		getPA().sendFrame126(" ", 6410);
		getPA().sendFrame126(" ", 6411);
		getPA().sendFrame126(" ", 8578);
		getPA().sendFrame126(" ", 8579);
		getPA().sendFrame126(" ", 8580);
		getPA().sendFrame126(" ", 8581);
		getPA().sendFrame126(" ", 8582);
		getPA().sendFrame126(" ", 8583);
		getPA().sendFrame126(" ", 8584);
		getPA().sendFrame126(" ", 8585);
		getPA().sendFrame126(" ", 8586);
		getPA().sendFrame126(" ", 8587);
		getPA().sendFrame126(" ", 8588);
		getPA().sendFrame126(" ", 8589);
		getPA().sendFrame126(" ", 8590);
		getPA().sendFrame126(" ", 8591);
		getPA().sendFrame126(" ", 8592);
		getPA().sendFrame126(" ", 8593);
		getPA().sendFrame126(" ", 8594);
		getPA().sendFrame126(" ", 8595);
		getPA().sendFrame126(" ", 8596);
		getPA().sendFrame126(" ", 8597);
		getPA().sendFrame126(" ", 8598);
		getPA().sendFrame126(" ", 8599);
		getPA().sendFrame126(" ", 8600);
		getPA().sendFrame126(" ", 8601);
		getPA().sendFrame126(" ", 8602);
		getPA().sendFrame126(" ", 8603);
		getPA().sendFrame126(" ", 8604);
		getPA().sendFrame126(" ", 8605);
		getPA().sendFrame126(" ", 8606);
		getPA().sendFrame126(" ", 8607);
		getPA().sendFrame126(" ", 8608);
		getPA().sendFrame126(" ", 8609);
		getPA().sendFrame126(" ", 8610);
		getPA().sendFrame126(" ", 8611);
		getPA().sendFrame126(" ", 8612);
		getPA().sendFrame126(" ", 8613);
		getPA().sendFrame126(" ", 8614);
		getPA().sendFrame126(" ", 8615);
		getPA().sendFrame126(" ", 8616);
		getPA().sendFrame126(" ", 8617);
	}

	public void updateHighscores() {
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				int totalz = KC;
				for (int d = 0; d <= 10; d++) {
					if (totalz >= ranks[d]) {
						if (d == 0) {
							playerRank = d + 1;
							ranks[d] = totalz;
							rankPpl[d] = playerName;
						} else if (d < 10) {
							if (totalz < ranks[d - 1]) {
								playerRank = d + 1;
								ranks[d] = totalz;
								rankPpl[d] = playerName;
							}
						} else {
							if (totalz < ranks[d - 1]) {
								playerRank = 0;
							}
						}
					}
				}
				// System.out.println("Highscores updated succesfully!");
			}

			@Override
			public void stop() {

			}
		}, 50);
	}

	public String randomMessage() {
		int randomMessage = Misc.random(5);
		switch (randomMessage) {
		case 0:
			return "@bla@[@blu@Did You Know@bla@] @red@You can help the server grow by voting every 12 hours! ::vote";
		case 1:
			return "@bla@[@blu@Did You Know@bla@] @red@You can type ::help to submit a ticket to all staff members!";
		case 2:
			return "@bla@[@blu@Did You Know@bla@] @red@You can upgrade your account and get great benefits! ::store";
		case 3:
			return "@bla@[@blu@Did You Know@bla@] @red@Help the server grow, invite your friends! Recieve rewards!";
		case 4:
			return "@bla@[@blu@Did You Know@bla@] @red@That you get 5 Pk Points per kill(10 during weekends).";
		}
		return "@bla@[@blu@Did You Know@bla@] @red@We have a great set of community forums! Join now! ::forums";
	}

	public void serverMessages() {
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				sendMessage(randomMessage());
			}

			@Override
			public void stop() {
			}
		}, 1500);
	}

	public void sendMessageToAll(String message) {
		for (Player player : PlayerHandler.players) {
			if (player == null)
				continue;
			final Player c = (Player) player;
			c.sendMessage(message);
		}
	}

	public boolean allow = false, allow1 = false;

	public void identifyLevel() {
		int[] skillIds = { 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 };
		for (int i : skillIds) {
			if (!ironman && playerLevel[i] > 2) {
				allow = true;
			} else if (ironman && playerLevel[i] > 2) {
				allow1 = true;
			}
		}
	}

	public void initialize() {
		try {
			identifyLevel();
			getStreak().drawSkulls();
			getPA().loadQuests();
			recordedLogin = System.currentTimeMillis();
			setStopPlayer(false);
			getPlayerAction().setAction(false);
			getPlayerAction().canWalk(true);
			getPA().sendFrame126(runEnergy + "%", 149);
			isFullHelm = Item.isFullHelm(playerEquipment[playerHat]);
			isFullMask = Item.isFullMask(playerEquipment[playerHat]);
			isFullBody = Item.isFullBody(playerEquipment[playerChest]);
			getPA().sendFrame36(173, isRunning2 ? 1 : 0);
			getPA().sendFrame36(166, 4);
			serverMessages();
			getPA().sendFrame126("PvP Teleports", 45005);
			combatLevel = calculateCombatLevel();
			setBrightness(getBrightness());
                        
                        TeleportTab.loadPage(this);

			outStream.createFrame(249);
			outStream.writeByteA(1); // 1 for members, zero for free
			outStream.writeWordBigEndianA(index);
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (j == index)
					continue;
				if (PlayerHandler.players[j] != null) {
					if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName))
						disconnected = true;
				}
			}
			for (int i = 0; i < 25; i++) {
				getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
				getPA().refreshSkill(i);
			}
			for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
				prayerActive[p] = false;
				getPA().sendFrame36(PRAYER_GLOW[p], 0);
			}

			long start = System.currentTimeMillis();
			getPA().handleWeaponStyle();
			getPA().handleLoginText();
			accountFlagged = getPA().checkForFlags();
			getPA().sendFrame36(108, 0);// resets autocast button
			getPA().sendFrame36(172, 1);
			getPA().sendFrame107(); // reset screen
			setSidebarInterface(1, 29000);
			setSidebarInterface(2, 638);
			setSidebarInterface(3, 3213);
			setSidebarInterface(4, 1644);
			setSidebarInterface(13, 15001);
			setSidebarInterface(5, 5608);
			setSidebarInterface(14, 15051); //veng action tab
			setSidebarInterface(16, 17000); //color change tab
			if (this.playerMagicBook == 0)
				setSidebarInterface(6, 1151);
			else if (this.playerMagicBook == 1)
				setSidebarInterface(6, 12855);
			else if (this.playerMagicBook == 2)
				setSidebarInterface(6, 29999);
			setSidebarInterface(7, 18128);
			setSidebarInterface(8, 5065);
			setSidebarInterface(9, 5715);
			setSidebarInterface(10, 2449);
			setSidebarInterface(11, 904); // wrench tab
			setSidebarInterface(12, 147); // run tab
			// setSidebarInterface(13, 28100);
			setSidebarInterface(0, 2423);
			getPA().showOption(4, 0, "Follow", 3);
			getPA().showOption(5, 0, "Trade with", 4);
			// getPA().showOption(6, 0, "Moderate", 5);
			if (hasNpc) {
				if (summonId > 0) {
					PetHandler.spawnPet(this, summonId, -1, true);
				}
			}
			if (splitChat) {
				getPA().sendFrame36(502, 1);
				getPA().sendFrame36(287, 1);
			}
			getItems().resetItems(3214);
			getItems(); // wtf?

			getItems().sendWeapon(playerEquipment[playerWeapon],
					this.getItems().getItemName(playerEquipment[playerWeapon]));
			getItems().resetBonus();
			getItems().getBonus();
			getItems().writeBonus();
			getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
			getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
			getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
			getItems().setEquipment(playerEquipment[playerArrows], playerEquipmentN[playerArrows], playerArrows);
			getItems().setEquipment(playerEquipment[playerChest], 1, playerChest);
			getItems().setEquipment(playerEquipment[playerShield], 1, playerShield);
			getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
			getItems().setEquipment(playerEquipment[playerHands], 1, playerHands);
			getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
			getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
			getItems().setEquipment(playerEquipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon);
			getCombat().getPlayerAnimIndex(this.getItems().getItemName(playerEquipment[playerWeapon]).toLowerCase());
			if (getPrivateChat() > 2) {
				setPrivateChat(0);
			}
			if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
				getPA().movePlayer(2662, 2652, 0);
			}
			if (Boundary.isIn(this, Boundary.FIGHT_CAVE)) {
				getPA().movePlayer(2438, 5168, 0);
			}
			  if (getRights().isPlayer()) {
				   if (Config.doublePKPointsWeekend == true) {
				    sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are not a donator @bla@(@blu@::donate@bla@).");
				   } else if (Config.doublePKPointsWeekend == false) {
				    sendMessage("@bla@Welcome to @blu@Pkers.Ca!");
				    sendMessage("@bla@For assistance, join the clan chat; @blu@Pkers.Ca@bla@.");
				   }
				  } else if (getRights().isModerator()) {
				    if (Config.doublePKPointsWeekend == true) {
				     sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Mod.");
				    } else if (Config.doublePKPointsWeekend == false) {
				     sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently a @blu@Mod.");
				    }
				  } else if (getRights().isAdministrator()) {
				     if (Config.doublePKPointsWeekend == true) {
				      sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Admin.");
				     } else if (Config.doublePKPointsWeekend == false) {
				      sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently an @blu@Admin.");
				     }
				  } else if (getRights().isOwner()) {
				      if (Config.doublePKPointsWeekend == true) {
				       sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Owner.");
				      } else if (Config.doublePKPointsWeekend == false) {
				       sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently an @blu@Owner.");
				      }
				  } else if (getRights().isHelper()) {
				      if (Config.doublePKPointsWeekend == true) {
				       sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Support.");
				      } else if (Config.doublePKPointsWeekend == false) {
				       sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently a @blu@Support.");
				      }
				  } else if (getRights().isYoutuber()) {
				      if (Config.doublePKPointsWeekend == true) {
				       sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Youtuber.");
				      } else if (Config.doublePKPointsWeekend == false) {
				       sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently a @blu@Youtuber.");
				      }
				  } else if (getRights().isDeveloper()) {
				      if (Config.doublePKPointsWeekend == true) {
				       sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Developer.");
				      } else if (Config.doublePKPointsWeekend == false) {
				       sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently a @blu@Developer.");
				  }
				  }
			  if (getRights().isContributor()) {
				   if (Config.doublePKPointsWeekend == true) {
				    sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Donator.");
				   } else if (Config.doublePKPointsWeekend == false) {
				    sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently a @blu@Donator.");
				   }
				  } else if (getRights().isSponsor()) {
				   if (Config.doublePKPointsWeekend == true) {
				    sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Sponsor.");
				   } else if (Config.doublePKPointsWeekend == false) {
				    sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently a @blu@Sponsor.");
				   }
				  } else if (getRights().isSupporter()) {
					   if (Config.doublePKPointsWeekend == true) {
					    sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Legendary Donator.");
					   } else if (Config.doublePKPointsWeekend == false) {
					    sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently a @blu@Legendary Donator.");
					   }
				  } else if (getRights().isVIP()) {
					   if (Config.doublePKPointsWeekend == true) {
					    sendMessage("@bla@Welcome back to @blu@Pkers.Ca! @bla@-(@blu@Double PkP Weekend@bla@)- @bla@You are currently a @blu@Respected Donator.");
					   } else if (Config.doublePKPointsWeekend == false) {
					    sendMessage("@bla@Welcome to @blu@Pkers.Ca! @bla@You are currently a @blu@Respected Donator.");
					   }
				  }
				  
				  if (Config.ChristmasDonation) {
				   sendMessage("[@blu@DONATIONS@bla@]Weekend Sale: @red@25% @bla@off everything at @blu@::donate@bla@!");
				  }
			/* Login Friend List */
			outStream.createFrame(221);
			outStream.writeByte(2);

			outStream.createFrame(206);
			outStream.writeByte(0);
			outStream.writeByte(getPrivateChat());
			outStream.writeByte(0);
			getFriends().sendList();
			getIgnores().sendList();

			getItems().addSpecialBar(playerEquipment[playerWeapon]);
			saveTimer = Config.SAVE_TIMER;
			saveCharacter = true;
			// HighscoresConfig.updateHighscores(this);
			Misc.println("[Logged in]: " + playerName + "");
			// PushPlayerCount();
			// handler.updatePlayer(this, outStream);
			// handler.updateNPC(this, outStream);
			// flushOutStream();
			totalLevel = getPA().totalLevel();
			xpTotal = getPA().xpTotal();
			getPA().sendFrame126("Combat Level: " + combatLevel + "", 3983);
			getPA().sendFrame126("Level: " + totalLevel + "", 3984);
			getPA().resetFollow();
			getPA().clearClanChat();
			getPA().resetFollow();
			getPA().setClanData();
			if (lastClanChat != null && lastClanChat.length() > 0) {
				Clan clan = Server.clanManager.getClan(lastClanChat);
				if (clan != null) {
					clan.LoginStageClan(this);
				}
			}
			if (autoRet == 1)
				getPA().sendFrame36(172, 1);
			else
				getPA().sendFrame36(172, 0);
			addEvents();
			if (Config.BOUNTY_HUNTER_ACTIVE) {
				bountyHunter.updateTargetUI();
				bountyHunter.updateStatisticsUI();
			}
			for (int i = 0; i < playerLevel.length; i++) {
				/**
				 * WARNING; Do not tamper with the uncommented code.
				 */
				// if (i == 3 || i == 5) {
				// continue;
				// }
				// playerLevel[i] = getPA().getLevelForXP(playerXP[i]);
				getPA().refreshSkill(i);
			}
			correctCoordinates();
			checkAccountPin();
			loginMessage();
			Server.triLottery.checkUnclaimedWinners(this);
			long end = System.currentTimeMillis() - start;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		public boolean checkReq(Player player) {
			if (Connection.hasRecieved1stStarter(PlayerHandler.players[player.index].connectedFrom)
					&& Connection.hasRecieved2ndStarter(PlayerHandler.players[player.index].connectedFrom)) {
				player.sendMessage("You have already received both starters!");
				return false;
			}

			if (!Connection.hasRecieved1stStarter(PlayerHandler.players[player.index].connectedFrom)) {
				Connection.addIpToStarterList1(PlayerHandler.players[player.index].connectedFrom);
				Connection.addIpToStarter1(PlayerHandler.players[player.index].connectedFrom);
			} else {
				Connection.addIpToStarterList2(PlayerHandler.players[player.index].connectedFrom);
				Connection.addIpToStarter2(PlayerHandler.players[player.index].connectedFrom);
			}

			return true;
		}

	public void addEvents() {
		if (Config.BOUNTY_HUNTER_ACTIVE) {
			CycleEventHandler.getSingleton().addEvent(this, bountyHunter, 1);
		}
		CycleEventHandler.getSingleton().addEvent(CycleEventHandler.Event.PLAYER_COMBAT_DAMAGE, this, damageQueue, 1,
				true);
	}

	public void update() {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();

	}

	public void wildyWarning() {
		getPA().showInterface(1908);
	}

	public boolean inProcess = false;

	String name;

	public void logout() {
		if (this.clan != null) {
			this.clan.removeMember(this);
		}
		//getPA().getOnlineStaff();
		if (getPA().viewingOtherBank) {
			getPA().resetOtherBank();
		}

		playTimeTotal += (System.currentTimeMillis() - recordedLogin);
		DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(this,
				MultiplayerSessionType.DUEL);
		if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST) {
			if (duelSession.getStage().getStage() >= MultiplayerSessionStage.FURTHER_INTERACTION) {
				sendMessage("You are not permitted to logout during a duel. If you forcefully logout you will");
				sendMessage("lose all of your staked items, if any, to your opponent.");
			}
		}
		if (underAttackBy > 0 || underAttackBy2 > 0)
			return;
		// synchronized (this) {
		if (logoutDelay.elapsed(10000)) {
			if (fishTourneySession != null) {
				fishTourneySession.removePlayer(this);
			}
			if (hungerGames) {
				if (!HungerManager.getSingleton().exitGame(this)) {
					getItems().removeAllItems();
					getItems().deleteAllItems();
					getPA().movePlayer(2440, 3090, 0);
				}
			}
			outStream.createFrame(109);
			CycleEventHandler.getSingleton().stopEvents(this);
			properLogout = true;
			ConnectedFrom.addConnectedFrom(this, connectedFrom);
		} else {
			sendMessage("You must wait a few seconds from being out of combat to logout.");
		}

	}

	private long lastOverloadBoost;
	public double secondsPlayed;
	public long minutesPlayed;
	public long hoursPlayed;
	public long daysPlayed;

	public long lastDamageCalculation;
	public long lastUpdate = System.currentTimeMillis();

	public void targetDied(Player c) {
		if (getBH().target != null) {
			Player target = PlayerHandler.getPlayer(getBH().target.getName());
			if (getBH().hasTarget() && getBH().getTarget().getName().equalsIgnoreCase(target.playerName)
					&& target.getBH().hasTarget()
					&& target.getBH().getTarget().getName().equalsIgnoreCase(playerName)) {
				c.setKiller(c.getPlayerKiller());
				Player o = PlayerHandler.getPlayer(c.getKiller());
				if (o != target) {
					getBH().removeTarget();
					getBH().updateTargetUI();
					getBH().updateStatisticsUI();
					target.getBH().removeTarget();
					target.getBH().updateStatisticsUI();
					target.getBH().updateTargetUI();
					target.getBH().setTargetState(TargetState.RECENT_TARGET_KILL);
					getBH().setTargetState(TargetState.RECENT_TARGET_KILL);
				} else if (o == target) {
					getBH().dropPlayerEmblem(target);
					target.getBH().upgradePlayerEmblem();
					target.getKillstreak().increase(Killstreak.Type.HUNTER);
					target.getBH().setCurrentHunterKills(target.getBH().getCurrentHunterKills() + 1);
					target.getBH().setTotalHunterKills(target.getBH().getTotalHunterKills() + 1);
					if (target.getBH().getCurrentHunterKills() > target.getBH().getRecordHunterKills()) {
						target.getBH().setRecordHunterKills(target.getBH().getCurrentHunterKills());
					}
					target.getBH().setTargetState(TargetState.RECENT_TARGET_KILL);
					getBH().setTargetState(TargetState.RECENT_TARGET_KILL);
					target.sendMessage("<col=255>You have killed your target: " + playerName + ".");
					getBH().removeTarget();
					getBH().updateTargetUI();
					getBH().updateStatisticsUI();
					target.getBH().removeTarget();
					target.getBH().updateTargetUI();
					target.getBH().updateStatisticsUI();
				}
			}
		}
	}

	/*
	 * public void targetDied() { Player o =
	 * PlayerHandler.getPlayer(getKiller()); if(getBH().target != null) { if
	 * (getBH().hasTarget()) { Player target =
	 * PlayerHandler.getPlayer(getBH().target.getName()); if (o == target) {
	 * getBH().dropPlayerEmblem(target); target.getBH().upgradePlayerEmblem();
	 * target.getKillstreak().increase(Killstreak.Type.HUNTER);
	 * target.getBH().setCurrentHunterKills(target.getBH().getCurrentHunterKills
	 * () + 1);
	 * target.getBH().setTotalHunterKills(target.getBH().getTotalHunterKills() +
	 * 1); if (target.getBH().getCurrentHunterKills() >
	 * target.getBH().getRecordHunterKills()) {
	 * target.getBH().setRecordHunterKills(target.getBH().getCurrentHunterKills(
	 * )); } target.getBH().setTargetState(TargetState.RECENT_TARGET_KILL);
	 * getBH().setTargetState(TargetState.RECENT_TARGET_KILL);
	 * target.sendMessage("<col=255>You have killed your target: " + playerName
	 * + "."); getBH().removeTarget(); getBH().updateTargetUI();
	 * target.killStreak -= target.killStreak; target.getBH().removeTarget();
	 * target.getBH().updateTargetUI(); } getBH().updateStatisticsUI();
	 * getBH().updateTargetUI(); } else this.getPA().applyDead(); } }
	 */
	public int floweritem = 0;
	public int seedtimer = 0;
	public boolean insure;
	public static int spawnId;
	public long lastHeal;

	private long usernameHash;

	public Long getUsernameHash() {
		return usernameHash;
	}

	public void setUsernameHash(long hash) {
		this.usernameHash = hash;
	}

	public void process() {
		if (clickTimer > 0) {
		}
			if (clickTimer > 0 ) {
				clickTimer --;
		}
		getItems().update();
		if (TICKING_DAMAGE && System.currentTimeMillis() - lastHeal > 1_500) {
			lastHeal = System.currentTimeMillis();
			for (int[] point : Cerberus.ROCKS) {
				int x1 = point[0] + 1;
				int y1 = point[1] + 2;
				if (absX == x1 && absY == y1 || absX == lastX && absY == lastY) {
					appendDamage(Misc.random(1, 7), Hitmark.HIT);
				}
			}
		}

		if (RANGE_ATTACK == 1) {
			MAGIC_ATTACK = 0;
			MELEE_ATTACK = 0;
			RANGE_ATTACK = 0;
			RANDOM = 0;
			RANDOM_MELEE = 0;
		}
		getPA().sendFrame126((int) (specAmount * 10) + "", 155);
		getPA().sendFrame126("@gre@Players online: @yel@ "+PlayerHandler.getPlayerCount(), 16026);
		farming.farmingProcess();

		if (getInstancedArea() != null) {
			getInstancedArea().process();
		}

		/*
		 * if (updateItems) { itemAssistant.updateItems(); }
		 */
		if (isDead && respawnTimer == -6) {
			targetDied(this);
			getPA().applyDead();
		}
		secondsPlayed += 1; // done in ticks
		if (secondsPlayed >= 100) {
			minutesPlayed += 1;
			getPA().sendFrame126("@or1@Days: @whi@" + daysPlayed + "@or1@ Hrs: @whi@" + hoursPlayed
					+ "@or1@ Mins: @whi@" + minutesPlayed + "", 29162);
			secondsPlayed = 0;
		}
		if (minutesPlayed == 60) {
			hoursPlayed += 1;
			minutesPlayed = 0;
		}
		if (hoursPlayed == 24) {
			daysPlayed += 1;
			hoursPlayed = 0;
		}

		if (bonusXpTime > 0) {
			bonusXpTime--;
		}
		if (bonusXpTime == 1) {
			sendMessage("@red@Your time is up. Your XP is no longer boosted by the voting reward.");
		}
		if (bonusXpTime == 1000) {
			sendMessage("@red@You have 10 minutes left! Use it Wisely!");
		}
		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			animation(0x900);
		}
		if (Boundary.isIn(this, Zulrah.BOUNDARY) && getZulrahEvent().isInToxicLocation()) {
			appendDamage(1 + Misc.random(3), Hitmark.VENOM);
		}
		if (fishTourneySession != null && !FishingTourney.getSingleton().inFishingArea(this)) {
			fishTourneySession.removePlayer(this);
		}

		if (hungerGames) {
			if (!HungerManager.getSingleton().inArena(this) && HungerManager.getSingleton().gameRunning()) {
				HungerManager.getSingleton().exitGame(this);
			} else if (!HungerManager.getSingleton().inLobby(this) && !HungerManager.getSingleton().gameRunning()) {
				HungerManager.getSingleton().exitGame(this);
			}
		} else if (HungerManager.getSingleton().inArea(absX, absY)) {
			if (!getRights().isDeveloper() && !hungerGames) {
				//getItems().deleteAllItems();
				//getPA().movePlayer(2440, 3090, 0);
			}
		}
		if (getPoisonDamage() > 0
				&& ((System.currentTimeMillis() - getLastPoisonHit() > TimeUnit.MINUTES.toMillis(1) && !hungerGames)
						|| System.currentTimeMillis() - getLastPoisonHit() > 12000 && hungerGames)) {
			if (!hasBeenPoisoned && playerEquipment[playerHat] != 12931) {
				sendMessage("You have been poisoned!");
			}
			hasBeenPoisoned = true;
			appendPoisonDamage();
			setLastPoisonHit(System.currentTimeMillis());
		}
		if (getVenomDamage() > 0 && System.currentTimeMillis() - getLastVenomHit() > 15_0000) {
			if (!hasBeenVenomed && playerEquipment[playerHat] != 12931) {
				sendMessage("You have been infected by venom!");
			}
			hasBeenVenomed = true;
			appendVenomDamage();
			setLastVenomHit(System.currentTimeMillis());
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (hitDelay > 0) {
			hitDelay--;
		}

		getAgilityHandler().agilityProcess(this);
		if (specRestore > 0) {
			specRestore--;
		}
		/*
		 * if (pTime != 2147000000) { pTime++; }
		 */
		if (ClickItem.flowerTime == 0) {
			getPA().removeObject(ClickItem.flowerX, ClickItem.flowerY);
			getPA().removeObject(ClickItem.flowerX, ClickItem.flowerY);
			ClickItem.flowerTime = -1;
			ClickItem.flowerX = 0;
			ClickItem.flowerY = 0;
		}
		if (ClickItem.flowers == 2980) {
			floweritem = 2460;
		} else if (ClickItem.flowers == 2981) {
			floweritem = 2462;
		} else if (ClickItem.flowers == 2982) {
			floweritem = 2464;
		} else if (ClickItem.flowers == 2983) {
			floweritem = 2466;
		} else if (ClickItem.flowers == 2984) {
			floweritem = 2468;
		} else if (ClickItem.flowers == 2985) {
			floweritem = 2470;
		} else if (ClickItem.flowers == 2986) {
			floweritem = 2472;
		} else if (ClickItem.flowers == 2987) {
			floweritem = 2474;
		}
		if (seedtimer > 0) {
			seedtimer--;
		}
		if (ClickItem.flowerTime > 0) {
			ClickItem.flowerTime--;
		}
		if (specDelay.elapsed(Config.INCREASE_SPECIAL_AMOUNT)) {
			specDelay.reset();
			if (specAmount < 10) {
				specAmount += 1;
				if (specAmount > 10)
					specAmount = 10;
				getItems().addSpecialBar(playerEquipment[playerWeapon]);
			}
		}

		getCombat().handlePrayerDrain();
		if (singleCombatDelay.elapsed(6000)) {
			underAttackBy = 0;
		}
		if (singleCombatDelay2.elapsed(6000)) {
			underAttackBy2 = 0;
		}
		if (hasOverloadBoost) {
			if (System.currentTimeMillis() - lastOverloadBoost > 15000) {
				getPotions().doOverloadBoost();
				lastOverloadBoost = System.currentTimeMillis();
			}
		}
		if (System.currentTimeMillis() - restoreStatsDelay > 60000) {
			restoreStatsDelay = System.currentTimeMillis();
			for (int level = 0; level < playerLevel.length; level++) {
				if (playerLevel[level] < getLevelForXP(playerXP[level])) {
					if (level < 7 && level != 5 && level != 3) {
						if (hasOverloadBoost)
							continue;
					}
					if (level != 5 && (fishTourneySession == null || !fishTourneySession.running)) { // prayer
																										// doesn't
																										// restore
						playerLevel[level] += 1;
						getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
						getPA().refreshSkill(level);
					}
				} else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
					playerLevel[level] -= 1;
					getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
					getPA().refreshSkill(level);
				}
			}
		}

		if(inWild() && !isInYanille()) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3521) / 8) + 1);
			if (Config.SINGLE_AND_MULTI_ZONES) {
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			}
			
			getPA().showOption(3, 0, "Attack", 1);
			if (Config.BOUNTY_HUNTER_ACTIVE) {
				getPA().walkableInterface(28000);
				getPA().sendFrame171(1, 28070);
				getPA().sendFrame171(0, 196);
			} else {
				getPA().walkableInterface(197);
			}
			
		} 
		 else if(isInYanille()){
				int modY = absY > 6400 ?  absY - 6400 : absY;
				wildLevel = 12;
				getPA().walkableInterface(197);
				getPA().showOption(3, 0, "Attack", 1);
				if(Config.SINGLE_AND_MULTI_ZONES) {
					if(inMulti()) {
					getPA().sendFrame126("@yel@Level: "+wildLevel, 25);
						} else {
						getPA().sendFrame126("@yel@Level: "+wildLevel, 25);
					}
				} else {
					getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: "+wildLevel, 25);}
				getPA().showOption(3, 0, "Attack", 1);
			}
		else if (inClanWars() || (hungerGames && !isNpc && HungerManager.getSingleton().gameRunning()) || inFunPk()) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (inEdgeville()) {
			if (Config.BOUNTY_HUNTER_ACTIVE) {
				if (bountyHunter.hasTarget()) {
					getPA().walkableInterface(28000);
					getPA().sendFrame171(0, 28070);
					getPA().sendFrame171(1, 196);
					bountyHunter.updateOutsideTimerUI();
				} else {
					getPA().walkableInterface(-1);
				}
			} else {
				getPA().sendFrame99(0);
				getPA().showOption(3, 0, "Null", 1);
			}
			getPA().showOption(3, 0, "null", 1);
			
		} else if (Dicing.inDiceArea(this)) {
			if (!this.getItems().playerHasItem(Dicing.DICE)) {
				getPA().showOption(3, 1, "Dice With", 1);
			} else if (this.getItems().playerHasItem(Dicing.DICE)) {
				getPA().showOption(3, 1, "null", 1);
			}
			 
		} else if (Boundary.isIn(this, PestControl.LOBBY_BOUNDARY)) {
			getPA().walkableInterface(21119);
			PestControl.drawInterface(this, "lobby");
		} else if (Boundary.isIn(this, PestControl.GAME_BOUNDARY)) {
			getPA().walkableInterface(21100);
			PestControl.drawInterface(this, "game");
		} else if ((inDuelArena() || Boundary.isIn(this, Boundary.DUEL_ARENAS)) && !inDice()) {
			getPA().walkableInterface(201);
			if (Boundary.isIn(this, Boundary.DUEL_ARENAS)) {
				getPA().showOption(3, 0, "Attack", 1);
			} else {
				getPA().showOption(3, 0, "Challenge", 1);
			}
			wildLevel = 126;
		} else if (isInBarrows() || isInBarrows2()) {
			getPA().walkableInterface(16128);
			getPA().sendFrame126("" + barrowsKillCount, 16137);
			if (barrowsNpcs[2][1] == 2) {
				getPA().sendFrame126("@red@Karils", 16135);
			}
			if (barrowsNpcs[3][1] == 2) {
				getPA().sendFrame126("@red@Guthans", 16134);
			}
			if (barrowsNpcs[1][1] == 2) {
				getPA().sendFrame126("@red@Torags", 16133);
			}
			if (barrowsNpcs[5][1] == 2) {
				getPA().sendFrame126("@red@Ahrims", 16132);
			}
			if (barrowsNpcs[0][1] == 2) {
				getPA().sendFrame126("@red@Veracs", 16131);
			}
			if (barrowsNpcs[4][1] == 2) {
				getPA().sendFrame126("@red@Dharoks", 16130);
			}
		} else if(inFunPk2()) {
			getPA().walkableInterface(197);
			getPA().sendFrame126("@yel@FunPk", 199);
			getPA().showOption(3, 0, "Attack", 1);
			wildLevel = 55;
		} else if (inCwGame || inPits) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (getPA().inPitsWait()) {
			getPA().showOption(3, 0, "Null", 1);
		} else if (!inCwWait && !hungerGames) {
			getPA().sendFrame99(0);
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null", 1);}
			else if(isInYanille()){
				int modY = absY > 6400 ?  absY - 6400 : absY;
				wildLevel = 12;
				getPA().walkableInterface(197);
				getPA().showOption(3, 0, "Attack", 1);
				if(Config.SINGLE_AND_MULTI_ZONES) {
					if(inMulti()) {
					getPA().sendFrame126("@yel@Level: "+wildLevel, 199);
						} else {
						getPA().sendFrame126("@yel@Level: "+wildLevel, 199);
					}
				} else {
					getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: "+wildLevel, 199);}
				getPA().showOption(3, 0, "Attack", 1);
			}
		

		if (!inWild()) {
			wildLevel = 0;
		}
		if (!hasMultiSign && inMulti() && !hungerGames) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}

		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}
		if (!inMulti() && inWild())
			getPA().sendFrame70(30, 0, 196);
		else if (inMulti() && inWild())
			getPA().sendFrame70(0, 0, 196);
		if (this.skullTimer > 0) {
			--skullTimer;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (freezeTimer == 0) {
				getPA().sendFrame126("freezetimer:-2", 1810);
			}
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
					getPA().sendFrame126("freezetimer:-2", 1810);
				} else if (!Misc.goodDistance(absX, absY, PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 12)) {
					freezeTimer = -1;
					frozenBy = -1;
					getPA().sendFrame126("freezetimer:-2", 1810);
				}
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		if (followId > 0) {
			getPA().followPlayer();
		} else if (followId2 > 0) {
			getPA().followNpc();
		}

		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				getCombat().delayedHit(this, oldNpcIndex);
			}
		}
		if (attackTimer <= 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		}

	}

	public void setCurrentTask(Future<?> task) {
		currentTask = task;
	}

	public Future<?> getCurrentTask() {
		return currentTask;
	}

	public Stream getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public Lootbag getLoot() {
		return lootbag;
	}

	public RunePouch getRunePouch() {
		return runepouch;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public void setDialogue(Dialogue dialogue) {
		this.dialogue = dialogue;
	}

	public Dialogue getDialogue() {
		return dialogue;
	}

	public BlastMine getBM() {
		return blastMine;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public CombatAssistant getCombat() {
		return combat;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	/*
	 * public Killstreak getStreak() { return killingStreak; }
	 */

	/*
	 * public KillingStreak getStreak() { return killingStreak; }
	 */

	public Channel getSession() {
		return session;
	}

	public Potions getPotions() {
		return potions;
	}

	public PotionMixing getPotMixing() {
		return potionMixing;
	}

	public Food getFood() {
		return food;
	}

	/**
	 * Gets the entities attributes
	 * 
	 * @return
	 */
	public Attributes1 getAttributes() {
		return attributes1;
	}

	public void start(Dialogue dialogue) {
		this.dialogue = dialogue;
		if (dialogue != null) {
			getPA().closeAllWindows();
			dialogue.setNext(0);
			dialogue.setPlayer(this);
			dialogue.execute();
		} /*
			 * else if (getAttributes().get("pauserandom") != null) {
			 * getAttributes().remove("pauserandom"); }
			 */
	}

	/*
	 * private final DialogueChainBuilder dialogueChain = new
	 * DialogueChainBuilder(this); public DialogueChainBuilder dialogue() {
	 * return dialogueChain; }
	 */
	public boolean ardiRizal() {
		if ((playerEquipment[playerHat] == -1) && (playerEquipment[playerCape] == -1)
				&& (playerEquipment[playerAmulet] == -1) && (playerEquipment[playerChest] == -1)
				&& (playerEquipment[playerShield] == -1) && (playerEquipment[playerLegs] == -1)
				&& (playerEquipment[playerHands] == -1) && (playerEquipment[playerFeet] == -1)
				&& (playerEquipment[playerWeapon] == -1)) {
			return true;
		}
		return false;
	}

	private boolean isBusy = false;
	private boolean isBusyHP = false;
	public boolean isBusyFollow = false;

	public boolean checkBusy() {
		/*
		 * if (getCombat().isFighting()) { return true; }
		 */
		if (isBusy) {
			// actionAssistant.sendMessage("You are too busy to do that.");
		}
		return isBusy;
	}

	public boolean checkBusyHP() {
		return isBusyHP;
	}

	public boolean checkBusyFollow() {
		return isBusyFollow;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusyFollow(boolean isBusyFollow) {
		this.isBusyFollow = isBusyFollow;
	}

	public void setBusyHP(boolean isBusyHP) {
		this.isBusyHP = isBusyHP;
	}

	public boolean isBusyHP() {
		return isBusyHP;
	}

	public boolean isBusyFollow() {
		return isBusyFollow;
	}

	public int makeTimes;
	public int event;
	public long lastBankDeposit;
	public long spawnSets;
	public boolean hasOverloadBoost;

	public boolean canWalk() {
		return canWalk;
	}

	public void setCanWalk(boolean canWalk) {
		this.canWalk = canWalk;
	}

	public PlayerAssistant getPlayerAssistant() {
		return playerAssistant;
	}

	public SkillInterfaces getSI() {
		return skillInterfaces;
	}

	/**
	 * Skill Constructors
	 */

	public Slayer getSlayer() {
		return slayer;
	}
	
	public PVPSlayer getPVPSlayer() {
		return pvpslayer;
	}

	public HardTask getHard() {
		return hard;
	}

	public EasyTask getEasy() {
		return easy;
	}

	public MediumTask getMedium() {
		return medium;
	}

	public BossSlayer getBossSlayer() {
		return bossSlayer;
	}

	public Cooking getCooking() {
		return cooking;
	}

	public Agility getAgility() {
		return agility;
	}

	public Crafting getCrafting() {
		return crafting;
	}

	public Barrows getBarrows() {
		return barrows;
	}

	public Thieving getThieving() {
		return thieving;
	}

	public Herblore getHerblore() {
		return herblore;
	}

	public GnomeAgility getGnomeAgility() {
		return gnomeAgility;
	}

	public SeersRoof getSeers() {
		return seers;
	}

	public DraynorRoof getDraynor() {
		return draynor;
	}

	public AlkharidRoof getAlkharid() {
		return alkharid;
	}

	public CanifisRoof getCanifis() {
		return canifis;
	}

	public VarrockRoof getVarrock() {
		return varrock;
	}

	public PlayerAction getPlayerAction() {
		return playerAction;
	}

	public WildernessAgility getWildernessAgility() {
		return wildernessAgility;
	}

	public AgilityHandler getAgilityHandler() {
		return agilityHandler;
	}

	public Smithing getSmithing() {
		return smith;
	}

	public Logs getLogs() {
		return logs;
	}

	public FightCave getFightCave() {
		if (fightcave == null)
			fightcave = new FightCave(this);
		return fightcave;
	}

	public SmithingInterface getSmithingInt() {
		return smithInt;
	}

	/*
	 * public Fletching getFletching() { return fletching; }
	 */

	public Prayer getPrayer() {
		return prayer;
	}

	/**
	 * End of Skill Constructors
	 */

	public void correctCoordinates() {
		final Boundary pc = PestControl.GAME_BOUNDARY;
		final Boundary fc = Boundary.FIGHT_CAVE;
		int x = teleportToX;
		int y = teleportToY;
		if (x > pc.getMinimumX() && x < pc.getMaximumX() && y > pc.getMinimumY() && y < pc.getMaximumY()) {
			getPA().movePlayer(2657, 2639, 0);
		} else if (x > fc.getMinimumX() && x < fc.getMaximumX() && y > fc.getMinimumY() && y < fc.getMaximumY()) {

			getPA().movePlayer(absX, absY, heightLevel * 4);
			sendMessage("Wave " + (this.waveId + 1) + " will start in approximately 5-10 seconds. ");
			getFightCave().spawn();
		} else if (HungerManager.getSingleton().inArea(x, y)) {
			if (!HungerManager.getSingleton().exitGame(this))
				getPA().movePlayer(2440, 3090, 0);
			getItems().deleteAllItems();
			getItems().deleteEquipment();
		}

	}

	public int getPrivateChat() {
		return privateChat;
	}

	public Friends getFriends() {
		return friend;
	}

	public Ignores getIgnores() {
		return ignores;
	}

	public void setPrivateChat(int option) {
		this.privateChat = option;
	}

	public Trade getTrade() {
		return trade;
	}

	public Position getPosition() {
		return new Position(absX, absY, heightLevel);
	}

	public int localX() {
		return this.getX() - this.getMapRegionX() * 8;
	}

	public int localY() {
		return this.getY() - this.getMapRegionY() * 8;
	}

	public long getLastContainerSearch() {
		return lastContainerSearch;
	}

	public void setLastContainerSearch(long lastContainerSearch) {
		this.lastContainerSearch = lastContainerSearch;
	}

	public MysteryBox getMysteryBox() {
		return mysteryBox;
	}

	public DamageQueueEvent getDamageQueue() {
		return damageQueue;
	}

	public void addDamageReceived(String player, int damage) {
		if (damage <= 0) {
			return;
		}
		Damage combatDamage = new Damage(damage);
		if (damageReceived.containsKey(player)) {
			damageReceived.get(player).add(new Damage(damage));
		} else {
			damageReceived.put(player, new ArrayList<>(Arrays.asList(combatDamage)));
		}
	}

	public void resetDamageReceived() {
		damageReceived.clear();
	}

	private String killer;
	public boolean craftDialogue;

	public String getPlayerKiller() {
		String killer = null;
		int totalDamage = 0;
		for (Entry<String, ArrayList<Damage>> entry : damageReceived.entrySet()) {
			String player = entry.getKey();
			ArrayList<Damage> damageList = entry.getValue();
			int damage = 0;
			for (Damage d : damageList) {
				if (System.currentTimeMillis() - d.getTimestamp() < 90000) {
					damage += d.getAmount();
				}
			}
			if (totalDamage == 0 || damage > totalDamage || killer == null) {
				totalDamage = damage;
				killer = player;
			}
		}
		return killer;
	}

	public String getKiller() {
		return killer;
	}

	public void setKiller(String killer) {
		this.killer = killer;
	}

	private boolean killedByZombie = false;
	public boolean isAnimatedArmourSpawned;
	private Hitmark hitmark = null;
	private Hitmark secondHitmark = null;
	protected Rights rights = Rights.PLAYER;
	private boolean invisible;
	private boolean godmode;
	private boolean safemode;
	private double dropModifier = 1;
	private int debug = 0;
	private boolean trading;
	public boolean playerIsCrafting;
	public boolean hasNpc = false;
	public boolean updateItems = false;
	public int ratsCaught;
	public int summonId;
	public int bossKills;
	public int droppedItem = -1;
	public int kbdCount;
	public int dagannothCount;
	public int krakenCount;
	public int chaosCount;
	public int armaCount;
	public int bandosCount;
	public int saraCount;
	public int zammyCount;
	public int barrelCount;
	public int moleCount;
	public int callistoCount;
	public int venenatisCount;
	public int vetionCount;
	public int rememberNpcIndex;
	public boolean slayerRecipe;
	public boolean claimedReward;
	public long lastMove;
	public long bonusXpTime;
	public long actionTimer;
	public boolean settingMin;
	public boolean settingMax;
	public boolean settingBet;
	public int diceMin;
	public int diceMax;
	public int otherDiceId;
	public int betAmount;
	public int totalProfit;
	public int betsWon;
	public int betsLost;
	public Player diceHost;
	public int removedTasks[] = { -1, -1, -1, -1 };
	public long buySlayerTimer;
	public boolean needsNewTask = false;
	public boolean needsNewPvPTask = false;
	public boolean needsNewBossTask = false;
	public boolean keepTitle = false;
	public boolean killTitle = false;
	public int slayerPoints = 0;
	public int slayerSpree = 0;
	public int pTime;
	public int killStreak;
	public int highestKillStreak;
	public boolean hide = false;
	public ArrayList<String> killedPlayers = new ArrayList<>();
	public ArrayList<Integer> attackedPlayers = new ArrayList<>();
	public ArrayList<String> lastConnectedFrom = new ArrayList<>();
	private boolean stopPlayer;
	/**
	 * Clan Chat Variables
	 */
	public String clanName;
	public String properName;
	public long lastMysteryBox;
	public long lastPackageBox;
	public int waveType;
	public int[] waveInfo = new int[3];
	public String date;
	public String currentTime;
	public int day;
	public int month;
	public int YEAR;
	public int totalLevel;
	public int xpTotal;
	public int smeltAmount = 0;
	public int smeltEventId = 5567;
	public String barType = "";
	public Smelting.Bars bar = null;
	public boolean isSmelting = false;
	/**
	 * Title Variables
	 */
	public String playerTitle = "";
	/**
	 * Event Variables
	 */
	public boolean hasEvent;
	/**
	 * Achievement Variables
	 */
	public int achievementsCompleted;
	public int achievementPoints;
	public int fireslit;
	public int crabsKilled;
	public int treesCut;
	public boolean expLock = false;
	public boolean buyingX;
	public boolean leverClicked = false;
	public double crossbowDamage;
	public int pkp;
	public int KC;
	public int DC;
	public int votePoints;
	public int blackMarks;
	public int amDonated;
	public boolean[] clanWarRule = new boolean[10];
	public int follow2 = 0;
	public int antiqueSelect = 0;
	public boolean usingLamp = false;
	public boolean normalLamp = false;
	public boolean antiqueLamp = false;
	public int[][] playerSkillProp = new int[20][15];
	public boolean[] playerSkilling = new boolean[20];
	public boolean setPin = false;
	public String bankPin = "";
	public boolean teleporting;
	public long jailEnd;
	public long muteEnd;
	public long marketMuteEnd;
	public long banEnd;
	public long lastReport = 0;
	public int level1 = 0;
	public int level2 = 0;
	public int level3 = 0;
	public String lastReported = "";
	public long lastButton;
	public int leatherType = -1;
	public boolean isWc;
	public int homeTele = 0;
	public int homeTeleDelay = 0;
	public boolean wcing;
	public int treeX;
	public int treeY;
	public long miscTimer;
	public boolean canWalk = true;
	public long waitTime;
	public boolean usingROD = false;
	public int DELAY = 1250;
	public long saveButton = 0;
	public int attempts = 3;
	public boolean isOperate;
	public int itemUsing;
	public boolean isFullBody = false;
	public boolean isFullHelm = false;
	public boolean isFullMask = false;
	public long lastCast;
	public long lastVeng;
	public boolean hasBankPin;
	public boolean enterdBankpin;
	public boolean firstPinEnter;
	public boolean requestPinDelete;
	public boolean secondPinEnter;
	public boolean thirdPinEnter;
	public boolean fourthPinEnter;
	public boolean hasBankpin;
	public int lastLoginDate;
	public int playerBankPin;
	public int recoveryDelay = 3;
	public int attemptsRemaining = 3;
	public int lastPinSettings = -1;
	public int setPinDate = -1;
	public int changePinDate = -1;
	public int deletePinDate = -1;
	public int firstPin;
	public int secondPin;
	public int thirdPin;
	public int fourthPin;
	public int bankPin1;
	public int bankPin2;
	public int bankPin3;
	public int bankPin4;
	public int pinDeleteDateRequested;
	public boolean isBanking = true;
	public boolean isCooking = false;
	public boolean initialized = false;
	public boolean disconnected = false;
	public boolean ruleAgreeButton = false;
	public boolean rebuildNPCList = false;
	public static boolean transforming;
	public boolean spawnKal = false;
	public boolean isActive = false;
	public boolean isKicked = false;
	public boolean isSkulled = false;
	public boolean friendUpdate = false;
	public boolean newPlayer = false;
	public boolean hasMultiSign = false;
	public boolean saveCharacter = false;
	public boolean mouseButton = false;
	public boolean splitChat = false;
	public boolean chatEffects = true;
	public boolean nextDialogue = false;
	public boolean autocasting = false;
	public boolean usedSpecial = false;
	public boolean mageFollow = false;
	public boolean dbowSpec = false;
	public boolean craftingLeather = false;
	public boolean properLogout = false;
	public boolean secDbow = false;
	public boolean maxNextHit = false;
	public boolean acbSpec = false;
	public boolean bpSpec = false;
	public boolean ssSpec = false;
	public boolean vengOn = false;
	public boolean accountFlagged = false;
	public boolean msbSpec = false;
	public boolean dtOption = false;
	public boolean dtOption2 = false;
	public boolean doricOption = false;
	public boolean doricOption2 = false;
	public boolean caOption2 = false;
	public boolean caOption2a = false;
	public boolean caOption4a = false;
	public boolean caOption4b = false;
	public boolean caOption4c = false;
	public boolean caPlayerTalk1 = false;
	public boolean horrorOption = false;
	public boolean rfdOption = false;
	public boolean inDt = false;
	public boolean inHfd = false;
	public boolean disableAttEvt = false;
	public boolean AttackEventRunning = false;
	public boolean npcindex;
	public boolean spawned = false;
	public int saveDelay;
	public int height = 0;
	public int playerKilled;
	public int totalPlayerDamageDealt;
	public int killedBy;
	public int lastChatId = 1;
	public int friendSlot = 0;
	public int dialogueId;
	public int randomCoffin;
	public int newLocation;
	public int specEffect;
	public int specBarId;
	public int attackLevelReq;
	public int defenceLevelReq;
	public int strengthLevelReq;
	public int rangeLevelReq;
	public int magicLevelReq;
	public int followId;
	public int skullTimer;
	public int votingPoints;
	public int nextChat = 0;
	public int talkingNpc = -1;
	public int dialogueAction = 0;
	public int autocastId;
	public int followDistance;
	public int followId2;
	public int barrageCount = 0;
	public int delayedDamage = 0;
	public int delayedDamage2 = 0;
	public int pcPoints = 0;
	public int donatorPoints = 0;
	public int magePoints = 0;
	public int lastArrowUsed = -1;
	public int clanId = -1;
	public int autoRet = 0;
	public int pcDamage = 0;
	public int xInterfaceId = 0;
	public int xRemoveId = 0;
	public int xRemoveSlot = 0;
	public int tzhaarToKill = 0;
	public int tzhaarKilled = 0;
	public int waveId;
	public int frozenBy = 0;
	public int teleAction = 0;
	public int newPlayerAct = 0;
	public int bonusAttack = 0;
	public int lastNpcAttacked = 0;
	public int killCount = 0;
	public int rfdRound = 0;
	public int roundNpc = 0;
	public int desertTreasure = 0;
	public int horrorFromDeep = 0;
	public int QuestPoints = 0;
	public int doricQuest = 0;
	public int[] voidStatus = new int[5];
	public int[] pouches = new int[4];
	public final int[] POUCH_SIZE = { 3, 6, 9, 12 };
	public long friends[] = new long[200];
	public double specAmount = 0;
	public double specAccuracy = 1;
	public double specDamage = 1;
	public double prayerPoint = 1.0;
	public boolean storing = false;
	public int teleGrabItem;
	public int teleGrabX;
	public int teleGrabY;
	public int duelCount;
	public int underAttackBy;
	public int underAttackBy2;
	public int wildLevel;
	public int teleTimer;
	public int respawnTimer;
	public int saveTimer = 0;
	public int teleBlockLength;
	public long lastPlayerMove = System.currentTimeMillis();
	public Stopwatch lastSpear = new Stopwatch();
	public Stopwatch lastProtItem = new Stopwatch();
	public long dfsDelay = System.currentTimeMillis();
	// public Stopwatch lastVeng = new Stopwatch();
	public Stopwatch lastYell = new Stopwatch();
	public long teleGrabDelay = System.currentTimeMillis();
	public long protMageDelay = System.currentTimeMillis();
	public long protMeleeDelay = System.currentTimeMillis();
	public long protRangeDelay = System.currentTimeMillis();
	public long lastLockPick = System.currentTimeMillis();
	public long alchDelay = System.currentTimeMillis();
	public long specCom = System.currentTimeMillis();
	public Stopwatch specDelay = new Stopwatch();
	public long duelDelay = System.currentTimeMillis();
	public long teleBlockDelay = System.currentTimeMillis();
	public long godSpellDelay = System.currentTimeMillis();
	public Stopwatch singleCombatDelay = new Stopwatch();
	public Stopwatch singleCombatDelay2 = new Stopwatch();
	public long reduceStat = System.currentTimeMillis();
	public long restoreStatsDelay = System.currentTimeMillis();
	public Stopwatch logoutDelay = new Stopwatch();
	public long buryDelay = System.currentTimeMillis();
	public long cleanDelay = System.currentTimeMillis();
	public Stopwatch lastRare = new Stopwatch();
	public long diceDelay;
	public long foodDel;
	public Stopwatch foodDelay = new Stopwatch();
	public Stopwatch ditchDelay = new Stopwatch();
	public Stopwatch switchDelay = new Stopwatch();
	public Stopwatch potDelay = new Stopwatch();
	public boolean canChangeAppearance = false;
	public boolean mageAllowed;
	public int focusPointX = -1;
	public int focusPointY = -1;
	public int questPoints = 0;
	public int cooksA;
	public int lastDtKill = 0;
	public int dtHp = 0;
	public int dtMax = 0;
	public int dtAtk = 0;
	public int dtDef = 0;
	public int desertT;
	public long lastChat;
	public long lastRandom;
	public long lastCaught = 0;
	public long lastAttacked;
	public long homeTeleTime;
	public long lastDagChange = -1;
	public long reportDelay;
	public long lastPlant;
	public long objectTimer;
	public long npcTimer;
	public long lastEss;
	public long lastClanMessage;
	public int DirectionCount = 0;
	public boolean appearanceUpdateRequired = true;
	public boolean isDead = false;
	public boolean randomEvent = false;
	public boolean FirstClickRunning = false;
	public boolean WildernessWarning = false;
	protected boolean faceNPCupdate = false;
	public int faceNPC = -1;
	public int EquipStatus;
	public boolean check = false;
	public boolean isExtreme = false;

	public final int[] BOWS = { 12788, 9185, 11785, 19481, 839, 845, 847, 851, 855, 859, 841, 843, 849, 853, 857, 12424,
			861, 4212, 4214, 4215, 12765, 12766, 12767, 12768, 11235, 4216, 4217, 4218, 4219, 4220, 4734, 4221, 4222,
			4223, 6724, 4934, 4935, 4936, 4937 };
	public final int[] ARROWS = { 882, 884, 886, 888, 890, 892, 4740, 11212, 9140, 9141, 4142, 9143, 9144, 9240, 9241,
			9242, 9243, 9244, 9245, 19484, 825, 826, 827, 828, 829, 830 };
	public final int[] NO_ARROW_DROP = { 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734, 4934,
			4935, 4936, 4937 };
	public final int[] OTHER_RANGE_WEAPONS = { 863, 864, 865, 866, 867, 868, 869, 806, 807, 808, 809, 810, 811, 825,
			826, 827, 828, 829, 830, 800, 801, 802, 803, 804, 805, 812, 813, 814, 815, 816, 817, 818, 6522, 870, 871,
			872, 873, 874, 875, 876, 877 };
	public final int[][] MAGIC_SPELLS = {
			// example {magicId, level req, animation, startGFX, projectile Id,
			// endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2
			// amount, rune 3, rune 3 amount, rune 4, rune 4 amount}

			// Modern Spells
			{ 1152, 1, 711, 90, 91, 92, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0 }, // wind
			// strike
			{ 1154, 5, 711, 93, 94, 95, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0 }, // water
			// strike
			{ 1156, 9, 711, 96, 97, 98, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0 }, // earth
			// strike
			{ 1158, 13, 711, 99, 100, 101, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0 }, // fire
			// strike
			{ 1160, 17, 711, 117, 118, 119, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0 }, // wind
			// bolt
			{ 1163, 23, 711, 120, 121, 122, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0 }, // water
			// bolt
			{ 1166, 29, 711, 123, 124, 125, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0 }, // earth
			// bolt
			{ 1169, 35, 711, 126, 127, 128, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0 }, // fire
			// bolt
			{ 1172, 41, 711, 132, 133, 134, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0 }, // wind
			// blast
			{ 1175, 47, 711, 135, 136, 137, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0 }, // water
			// blast
			{ 1177, 53, 711, 138, 139, 140, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0 }, // earth
			// blast
			{ 1181, 59, 711, 129, 130, 131, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0 }, // fire
			// blast
			{ 1183, 62, 711, 158, 159, 160, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0 }, // wind
			// wave
			{ 1185, 65, 711, 161, 162, 163, 18, 37, 556, 5, 555, 7, 565, 1, 0, 0 }, // water
			// wave
			{ 1188, 70, 711, 164, 165, 166, 19, 40, 556, 5, 557, 7, 565, 1, 0, 0 }, // earth
			// wave
			{ 1189, 75, 711, 155, 156, 157, 20, 42, 556, 5, 554, 7, 565, 1, 0, 0 }, // fire
			// wave
			{ 1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0 }, // confuse
			{ 1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0 }, // weaken
			{ 1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0 }, // curse
			{ 1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0 }, // vulnerability
			{ 1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0 }, // enfeeble
			{ 1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0 }, // stun
			{ 1572, 20, 711, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0 }, // bind
			{ 1582, 50, 711, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0 }, // snare
			{ 1592, 79, 711, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0 }, // entangle
			{ 1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0 }, // crumble
			// undead
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0 }, // iban
			// blast
			{ 12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0 }, // magic
			// dart
			{ 1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0 }, // sara
			// strike
			{ 1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0 }, // cause
			// of
			// guthix
			{ 1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0 }, // flames
			// of
			// zammy
			{ 12445, 85, 1819, 0, 344, 345, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0 }, // teleblock

			// Ancient Spells
			{ 12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1 }, // smoke
			// rush
			{ 12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1 }, // shadow
			// rush
			{ 12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0 }, // blood
			// rush
			{ 12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0 }, // ice
			// rush
			{ 12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554, 2 }, // smoke
			// burst
			{ 13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566, 2 }, // shadow
			// burst
			{ 12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0 }, // blood
			// burst
			{ 12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0 }, // ice
			// burst
			{ 12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2, 556, 2 }, // smoke
			// blitz
			{ 12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2, 566, 2 }, // shadow
			// blitz
			{ 12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0 }, // blood
			// blitz
			{ 12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0, 0 }, // ice
			// blitz
			{ 12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554, 4 }, // smoke
			// barrage
			{ 13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566, 3 }, // shadow
			// barrage
			{ 12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0 }, // blood
			// barrage
			{ 12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0 }, // ice
			// barrage

			{ -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0 }, // charge
			{ -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0 }, // low
			// alch
			{ -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0 }, // high
			// alch
			{ -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0 }, // telegrab

			{ -1, 75, 1167, 1251, 1252, 1253, 29, 35, 0, 0, 0, 0, 0, 0, 0, 0 }, // trident
																				// of
																				// the
																				// seas

			{ -1, 75, 1167, 665, 1040, 1042, 32, 35, 0, 0, 0, 0, 0, 0, 0, 0 } // trident
																				// of
																				// the
																				// swamp

	};
	public boolean multiAttacking;
	public boolean rangeEndGFXHeight;
	public boolean playerFletch;
	public boolean alreadyFletching;
	public boolean playerIsFletching;
	public boolean playerIsMining;
	public boolean playerIsFiremaking;
	public boolean playerIsFishing;
	public boolean playerIsCooking;
	public boolean below459 = true;
	public boolean defaultWealthTransfer;
	public boolean updateInventory;
	public boolean oldSpec;
	public boolean stopPlayerSkill;
	public boolean alreadyFishing;
	public boolean playerStun;
	public boolean stopPlayerPacket;
	public boolean usingClaws;
	public boolean usingAbby;
	public boolean playerBFishing;
	public boolean finishedBarbarianTraining;
	public boolean ignoreDefence;
	public boolean secondFormAutocast;
	public boolean usingArrows;
	public boolean usingOtherRangeWeapons;
	public boolean usingCross;
	public boolean magicDef;
	public boolean spellSwap;
	public boolean recoverysSet;
	public int rangeEndGFX;
	public int boltDamage;
	public int teleotherType;
	public int playerTradeWealth;
	public int doAmount;
	public int woodcuttingTree;
	public int stageT;
	public int dfsCount;
	public int recoilHits;
	public int playerDialogue;
	public int clawDelay;
	public int previousDamage;
	public int newDamage;
	public int lightWood;
	public int smeltOre;
	public int previousDamage2;
	public boolean protectItem = false;
	public int[] autocastIds = { 51133, 32, 51185, 33, 51091, 34, 24018, 35, 51159, 36, 51211, 37, 51111, 38, 51069, 39,
			51146, 40, 51198, 41, 51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47, 7038, 0, 7039, 1,
			7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6, 7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 7050, 12, 7051,
			13, 7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13, 47023, 14, 47024, 15 };
	public int[][] barrowsNpcs = { { 1677, 0 }, // verac
			{ 1676, 0 }, // toarg
			{ 1675, 0 }, // karil
			{ 1674, 0 }, // guthan
			{ 1673, 0 }, // dharok
			{ 1672, 0 } // ahrim
	};
	public int barrowsKillCount;
	public int reduceSpellId;
	public final int[] REDUCE_SPELL_TIME = { 250000, 250000, 250000, 500000, 500000, 500000 };
	public long[] reduceSpellDelay = new long[6];
	public final int[] REDUCE_SPELLS = { 1153, 1157, 1161, 1542, 1543, 1562 };
	public boolean[] canUseReducingSpell = { true, true, true, true, true, true };
	public int slayerTask;
	public int taskAmount;
	public int slayerPvPTask;
	public int pvpTaskAmount;
	public int bossSlayerTask;
	public int bossTaskAmount;
	public int prayerId = -1;
	public int headIcon = -1;
	public int bhSkull = -1;
	public int bountyIcon = 0;
	public long stopPrayerDelay;
	public long prayerDelay;
	public boolean usingPrayer;
	public final int[] PRAYER_DRAIN_RATE = { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
			500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};
	public final int[] PRAYER_LEVEL_REQUIRED = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37, 40, 43,
			44, 45, 46, 49, 52, 55, 60, 70, 74, 77 };
	public final int[] PRAYER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25, 26, 27, 28};
	public final String[] PRAYER_NAME = { "Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye",
			"Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might",
			"Retribution", "Redemption", "Smite", "Preserve" , "Chivalry", "Piety", "Rigour", "Augury" };
	public final int[] PRAYER_GLOW = { 83, 84, 85, 601, 602, 86, 87, 88, 89, 90, 91, 603, 604, 92, 93, 94, 95, 96, 97,
			605, 606, 98, 99, 100, 607, 608, 609, 610, 611};
	public final int[] PRAYER_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0,
			-1, -1, 3, 5, 4, -1, -1, -1, -1, -1};
	public boolean[] prayerActive = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
	public long[] prayerLastUsed = new long[prayerActive.length];
	public boolean[] prayerFlicked = new boolean[prayerActive.length];

	public int headIconPk = -1;
	public int headIconHints;
	public boolean[] duelRule = new boolean[22];
	public boolean doubleHit;
	public boolean usingSpecial;
	public boolean npcDroppingItems;
	public boolean usingRangeWeapon;
	public boolean usingBow;
	public boolean usingMagic;
	public boolean castingMagic;
	public boolean usingMelee;
	public int specMaxHitIncrease;
	public int freezeDelay;
	public int freezeTimer = -6;
	public int killerId;
	public int playerIndex;
	public int oldPlayerIndex;
	public int lastWeaponUsed;
	public int projectileStage;
	public int crystalBowArrowCount;
	public int playerMagicBook;
	public int teleGfx;
	public int teleEndAnimation;
	public int teleHeight;
	public int teleX;
	public int teleY;
	public int rangeItemUsed;
	public int killingNpcIndex;
	public int totalDamageDealt;
	public int oldNpcIndex;
	public int fightMode;
	public int attackTimer;
	public int oldCombatTimer = attackTimer;
	public int npcIndex;
	public int krakenTent;
	public int checkNPC = 0;
	public int checkNPC1 = 0;
	public int checkNPC2 = 0;
	public int checkNPC3 = 0;
	public int checkNPC4 = 0;
	public int npcClickIndex;
	public int npcType;
	public int castingSpellId;
	public int oldSpellId;
	public int spellId;
	public int hitDelay;
	public boolean magicFailed;
	public boolean oldMagicFailed;
	public int bowSpecShot;
	public int clickNpcType;
	public int clickObjectType;
	public int objectId;
	public int objectX;
	public int objectY;
	public int objectXOffset;
	public int objectYOffset;
	public int objectDistance;
	public int pItemX;
	public int pItemY;
	public int pItemId;
	public boolean isMoving;
	public boolean walkingToItem;
	public boolean isShopping;
	public boolean updateShop;
	public int myShopId;
	public int tradeStatus;
	public int tradeWith;
	public int traded;
	public int amountGifted;
	public boolean inDuel;
	public boolean tradeAccepted;
	public boolean goodTrade;
	public boolean inTrade;
	public boolean tradeRequested;
	public boolean tradeResetNeeded;
	public boolean tradeConfirmed;
	public boolean tradeConfirmed2;
	public boolean canOffer;
	public boolean acceptTrade;
	public boolean acceptedTrade;
	public int attackAnim;
	public int[] playerBonus = new int[12];
	public boolean isRunning2 = true;
	public boolean takeAsNote;
	public int combatLevel;
	public boolean saveFile = false;
	public int playerAppearance[] = new int[13];
	public int apset;
	public int actionID;
	public int wearItemTimer;
	public int wearId;
	public int wearSlot;
	public int interfaceId;
	public int XremoveSlot;
	public int XinterfaceID;
	public int XremoveID;
	public int Xamount;
	public int tutorial = 15;
	public boolean usingGlory = false;
	public boolean usingObelisk = false;
	public int[] woodcut = new int[7];
	public int wcTimer = 0;
	public int miningTimer = 0;
	public boolean fishing = false;
	public int fishingNpc = -1;
	public int fishTimer = 0;
	public int smeltType;
	public int smeltTimer = 0;
	public boolean smeltInterface;
	public boolean patchCleared;
	public int[] farm = new int[2];
	public long lastAntifirePotion;
	public long antifireDelay;
	/**
	 * Castle Wars
	 */
	public int castleWarsTeam;
	public boolean inCwGame;
	public boolean inCwWait;
	public int npcId2 = 0;
	public boolean isNpc;

	/**
	 * Fight Pits
	 */

	public String getUsername() {
		return Misc.formatPlayerName(username);
	}

	public String getUsername1() {
		return username;
	}

	private String username = null;
	public String firstIP = "0", lastIP = "0";
	public boolean inPits = false;
	public int pitsStatus = 0;
	public String connectedFrom = "";
	public String globalMessage = "";
	public int safeTimer = 0;
	public String playerName = null;
	public String playerName2 = null;
	public String playerPass = null;
	private long nameAsLong;
	public PlayerHandler handler = null;
	public int playerItems[] = new int[28];
	public int playerItemsN[] = new int[28];
	public int bankItems[] = new int[Config.BANK_SIZE];
	public int bankItemsN[] = new int[Config.BANK_SIZE];
	public boolean bankNotes = false;
	private int dragonfireShieldCharge;
	private long lastDragonfireShieldAttack;
	private boolean dragonfireShieldActive;
	public int playerStandIndex = 0x328;
	public int playerTurnIndex = 0x337;
	public int playerWalkIndex = 0x333;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CWIndex = 0x335;
	public int playerTurn90CCWIndex = 0x336;
	public int playerRunIndex = 0x338;
	public int playerHat = 0;
	public int playerCape = 1;
	public int playerAmulet = 2;
	public static int playerWeapon = 3;
	public int playerChest = 4;
	public int playerShield = 5;
	public int playerLegs = 7;
	public int playerHands = 9;
	public int playerFeet = 10;
	public int playerRing = 12;
	public int playerArrows = 13;
	public int playerAttack = 0;
	public int playerDefence = 1;
	public int playerStrength = 2;
	public int playerHitpoints = 3;
	public int playerRanged = 4;
	public int playerPrayer = 5;
	public int playerMagic = 6;
	public int playerCooking = 7;
	public int playerWoodcutting = 8;
	public int playerFletching = 9;
	public int playerFishing = 10;
	public int playerFiremaking = 11;
	public static int playerCrafting = 12;
	public static int playerSmithing = 13;
	public int playerMining = 14;
	public int playerHerblore = 15;
	public int playerAgility = 16;
	public int playerThieving = 17;
	public int playerSlayer = 18;
	public int playerFarming = 19;
	public int playerRunecrafting = 20;
	public int fletchingType;
	public int[] playerEquipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	public int[] playerLevel = new int[25];
	public int[] playerXP = new int[25];
	public boolean seedPlanted = false;
	public boolean seedWatered = false;
	public boolean patchCleaned = false;
	public boolean patchRaked = false;
	public int[][] barrowCrypt = { { 4921, 0 }, { 2035, 0 } };
	public Player playerList[] = new Player[maxPlayerListSize];
	public int playerListSize = 0;
	public byte playerInListBitmap[] = new byte[(Config.MAX_PLAYERS + 7) >> 3];
	public NPC npcList[] = new NPC[maxNPCListSize];
	public int npcListSize = 0;
	public byte npcInListBitmap[] = new byte[(NPCHandler.maxNPCs + 7) >> 3];
	public int getHeightLevel;
	public int mapRegionX;
	public int mapRegionY;
	public int currentX;
	public int currentY;
	public int currentH;
	public int playerSE = 0x328;
	public int playerSEW = 0x333;
	public int playerSER = 0x334;
	public final int walkingQueueSize = 50;
	public int walkingQueueX[] = new int[walkingQueueSize];
	public int walkingQueueY[] = new int[walkingQueueSize];
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public boolean isRunning = true;
	public int teleportToX = -1;
	public int teleportToY = -1;
	public int runEnergy = 100;
	public long lastRunRecovery;
	public long rangeDelay;
	public boolean inSpecMode = false;
	public boolean didTeleport = false;
	public boolean mapRegionDidChange = false;
	public int dir1 = -1;
	public int dir2 = -1;
	public boolean createItems = false;
	public int poimiX = 0;
	public int poimiY = 0;
	public byte cachedPropertiesBitmap[] = new byte[(Config.MAX_PLAYERS + 7) >> 3];
	private boolean chatTextUpdateRequired = false;
	private byte chatText[] = new byte[4096];
	private byte chatTextSize = 0;
	private int chatTextColor = 0;
	private int chatTextEffects = 0;
	/**
	 * Graphics
	 **/

	/**
	 * Represents whether the force-movement update mask is required.
	 */
	public boolean forceMovementUpdateRequired;

	public void setForceMovementRequired(boolean required) {
		forceMovementUpdateRequired = required;
	}

	public void appendForceMovement(Stream str) {
		str.writeByteS(absX);
		str.writeByteS(absY);
		str.writeByteS(absX);
		str.writeByteS(absY + 5);
		str.writeWordBigEndianA(4);
		str.writeWordA(100);
		str.writeByteS(2);
	}

	/**
	 * Face Update
	 **/
	private int newWalkCmdX[] = new int[walkingQueueSize];
	private int newWalkCmdY[] = new int[walkingQueueSize];
	public int newWalkCmdSteps = 0;
	private boolean newWalkCmdIsRunning = false;
	protected int travelBackX[] = new int[walkingQueueSize];
	protected int travelBackY[] = new int[walkingQueueSize];
	protected int numTravelBackSteps = 0;
	public int[] damageTaken = new int[Config.MAX_PLAYERS];
	private long lastVenomHit;
	private long lastVenomCure;
	private long venomImmunity;
	private byte venomDamage;
	private long lastPoisonHit;
	private long lastPoisonCure;
	private long poisonImmunity;
	private byte poisonDamage;
	private List<Byte> poisonDamageHistory = new ArrayList<>(4);

	public boolean isKilledByZombie() {
		return killedByZombie;
	}

	public void setKilledByZombie(boolean state) {
		killedByZombie = state;
	}

	public int getFarmingSeedId(int index) {
		return farmingSeedId[index];
	}

	public void setFarmingSeedId(int index, int farmingSeedId) {
		this.farmingSeedId[index] = farmingSeedId;
	}

	public int getFarmingTime(int index) {
		return this.farmingTime[index];
	}

	public void setFarmingTime(int index, int farmingTime) {
		this.farmingTime[index] = farmingTime;
	}

	public int getFarmingState(int index) {
		return farmingState[index];
	}

	public void setFarmingState(int index, int farmingState) {
		this.farmingState[index] = farmingState;
	}

	public int getFarmingHarvest(int index) {
		return farmingHarvest[index];
	}

	public void setFarmingHarvest(int index, int farmingHarvest) {
		this.farmingHarvest[index] = farmingHarvest;
	}

	public Farming getFarming() {
		if (farming == null) {
			farming = new Farming(this);
		}
		return farming;
	}

	/**
	 * Retrieves the bounty hunter instance for this client object. We use lazy
	 * initialization because we store values from the player save file in the
	 * bountyHunter object upon login. Without lazy initialization the value
	 * would be overwritten.
	 * 
	 * @return the bounty hunter object
	 */
	public BountyHunter getBH() {
		if (Objects.isNull(bountyHunter)) {
			bountyHunter = new BountyHunter(this);
		}
		return bountyHunter;
	}

	public UnnecessaryPacketDropper getPacketDropper() {
		return packetDropper;
	}

	public Optional<ItemCombination> getCurrentCombination() {
		return currentCombination;
	}

	public void setCurrentCombination(Optional<ItemCombination> combination) {
		this.currentCombination = combination;
	}

	public PlayerKill getPlayerKills() {
		if (Objects.isNull(playerKills)) {
			playerKills = new PlayerKill();
		}
		return playerKills;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public int getMaximumHealth() {
		int base = getLevelForXP(playerXP[3]);
		if (EquipmentSet.GUTHAN.isWearingBarrows(this) && getItems().isWearingItem(12853)) {
			base += 10;
		}
		return base;
	}
	
	public boolean rigourPrayerScroll = false;
	public boolean auguryPrayerScroll = false;
	public boolean preservePrayerScroll = false;

	public int getMaximumPrayer() {
		int base = getLevelForXP(playerXP[5]);
		return base;
	}

	public Duel getDuel() {
		return duelSession;
	}

	public void setItemOnPlayer(Player player) {
		this.itemOnPlayer = player;
	}

	public Player getItemOnPlayer() {
		return itemOnPlayer;
	}

	public Skilling getSkilling() {
		return skilling;
	}

	public Presets getPresets() {
		if (presets == null) {
			presets = new Presets(this);
		}
		return presets;
	}

	public Killstreak getKillstreak() {
		if (getKillstreaks() == null) {
			setKillstreaks(new Killstreak(this));
		}
		return getKillstreaks();
	}

	public Streak getStreak() {
		return streak;
	}

	/*
	 * public KillStreak getStreak() { return KillStreak; }
	 */

	/**
	 * Returns the single instance of the {@link NPCDeathTracker} class for this
	 * player.
	 * 
	 * @return the tracker clas
	 */
	public NPCDeathTracker getNpcDeathTracker() {
		return npcDeathTracker;
	}

	/**
	 * Returns the single instance of the {@link NPCDeathTracker} class for this
	 * player.
	 * 
	 * @return the tracker clas
	 */
	public BossDeathTracker getBossDeathTracker() {
		return bossDeathTracker;
	}

	/**
	 * The zulrah event
	 * 
	 * @return event
	 */
	public Zulrah getZulrahEvent() {
		return zulrah;
	}

	public void setInstancedArea(InstancedArea instancedArea) {
		this.instancedArea = instancedArea;
		// instancedArea.onStart();
	}

	public InstancedArea getInstancedArea() {
		return instancedArea;
	}

	/*
	 * public Cerberus getCerberusEvent() { return cerberus; }
	 */

	public Kraken getKraken() {
		return kraken;
	}

	public Kalphite getKalphite() {
		return kalphite;
	}

	public Bandos getBandos() {
		return bandos;
	}

	public Armadyl getArmadyl() {
		return armadyl;
	}

	public Zamorak getZamorak() {
		return zamorak;
	}

	public Queen getQueen() {
		return queen;
	}

	public Saradomin getSaradomin() {
		return saradomin;
	}

	/**
	 * The single {@link WarriorsGuild} instance for this player
	 * 
	 * @return warriors guild
	 */
	public WarriorsGuild getWarriorsGuild() {
		return warriorsGuild;
	}

	/**
	 * The single instance of the {@link PestControlRewards} class for this
	 * player
	 * 
	 * @return the reward class
	 */
	public PestControlRewards getPestControlRewards() {
		return pestControlRewards;
	}

	public Mining getMining() {
		return mining;
	}

	public PunishmentPanel getPunishmentPanel() {
		return punishmentPanel;
	}

	public void appendFaceNPCUpdate(Stream str) {
		str.writeWordBigEndian(faceNPC);
	}

	public boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button)
				return true;
		}
		return false;
	}

	public void assignAutocast(int button) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				Player c = PlayerHandler.players[this.index];
				autocasting = true;
				autocastId = autocastIds[j + 1];
				c.getPA().sendFrame36(108, 1);
				c.setSidebarInterface(0, 328);
				// spellName = getSpellName(autocastId);
				// spellName = spellName;
				// c.getPA().sendFrame126(spellName, 354);
				c = null;
				break;
			}
		}
	}

	public int getLocalX() {
		return getX() - 8 * getMapRegionX();
	}

	public int getLocalY() {
		return getY() - 8 * getMapRegionY();
	}

	public String getSpellName(int id) {
		switch (id) {
		case 0:
			return "Air Strike";
		case 1:
			return "Water Strike";
		case 2:
			return "Earth Strike";
		case 3:
			return "Fire Strike";
		case 4:
			return "Air Bolt";
		case 5:
			return "Water Bolt";
		case 6:
			return "Earth Bolt";
		case 7:
			return "Fire Bolt";
		case 8:
			return "Air Blast";
		case 9:
			return "Water Blast";
		case 10:
			return "Earth Blast";
		case 11:
			return "Fire Blast";
		case 12:
			return "Air Wave";
		case 13:
			return "Water Wave";
		case 14:
			return "Earth Wave";
		case 15:
			return "Fire Wave";
		case 32:
			return "Shadow Rush";
		case 33:
			return "Smoke Rush";
		case 34:
			return "Blood Rush";
		case 35:
			return "Ice Rush";
		case 36:
			return "Shadow Burst";
		case 37:
			return "Smoke Burst";
		case 38:
			return "Blood Burst";
		case 39:
			return "Ice Burst";
		case 40:
			return "Shadow Blitz";
		case 41:
			return "Smoke Blitz";
		case 42:
			return "Blood Blitz";
		case 43:
			return "Ice Blitz";
		case 44:
			return "Shadow Barrage";
		case 45:
			return "Smoke Barrage";
		case 46:
			return "Blood Barrage";
		case 47:
			return "Ice Barrage";
		default:
			return "Select Spell";
		}
	}

	public boolean hasFullVoidRange() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean hasFullEliteVoidRange() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 13073
				&& playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] == 8842;
	}
	
	public boolean hasFullVoidMage() {
		return playerEquipment[playerHat] == 11663 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean hasFullEliteVoidMage() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 13073
				&& playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] == 8842;
	}
	
	public boolean hasFullVoidMelee() {
		return playerEquipment[playerHat] == 11665 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}
	
	public boolean hasFullEliteVoidMelee() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 13073
				&& playerEquipment[playerChest] == 13072 && playerEquipment[playerHands] == 8842;
	}
	
	public boolean fullVoidRange() {
		return playerEquipment[playerHat] == 11664 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidElite() {
		return playerEquipment[playerChest] == 13072 && playerEquipment[playerLegs] == 13073;
	}

	public boolean Ballista() {
		return playerEquipment[playerWeapon] == 19481;
	}
//	public boolean isIronMan() {
	//	return equals(Rights.IRONMAN);
	//}
	public boolean fullVoidMage() {
		return playerEquipment[playerHat] == 11663 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidMelee() {
		return playerEquipment[playerHat] == 11665 && playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839 && playerEquipment[playerHands] == 8842;
	}
	

	/**
	 * SouthWest, NorthEast, SouthWest, NorthEast
	 */
	public boolean isInTut() {
		if (absX >= 2625 && absX <= 2687 && absY >= 4670 && absY <= 4735) {
			return true;
		}
		return false;
	}

	public boolean isInMole() {
		if (absX > 2998 && absX < 3001 && absY > 3382 && absY < 3384) {
			return true;
		}
		return false;
	}

	public boolean isInBarrows() {
		if (absX > 3543 && absX < 3584 && absY > 3265 && absY < 3311) {
			return true;
		}
		return false;
	}

	public boolean isInBarrows2() {
		if (absX > 3529 && absX < 3581 && absY > 9673 && absY < 9722) {
			return true;
		}
		return false;
	}

	public boolean inBarrows() {
		if (absX > 3520 && absX < 3598 && absY > 9653 && absY < 9750) {
			return true;
		}
		return false;
	}

	public boolean inArea(int x, int y, int x1, int y1) {
		if (absX > x && absX < x1 && absY < y && absY > y1) {
			return true;
		}
		return false;
	}

	public boolean Area(final int x1, final int x2, final int y1, final int y2) {
		return (absX >= x1 && absX <= x2 && absY >= y1 && absY <= y2);
	}

	public boolean inBank() {
		return Area(3090, 3099, 3487, 3500) || Area(3089, 3090, 3492, 3498) || Area(3248, 3258, 3413, 3428)
				|| Area(3179, 3191, 3432, 3448) || Area(2944, 2948, 3365, 3374) || Area(2942, 2948, 3367, 3374)
				|| Area(2944, 2950, 3365, 3370) || Area(3008, 3019, 3352, 3359) || Area(3017, 3022, 3352, 3357)
				|| Area(3203, 3213, 3200, 3237) || Area(3212, 3215, 3200, 3235) || Area(3215, 3220, 3202, 3235)
				|| Area(3220, 3227, 3202, 3229) || Area(3227, 3230, 3208, 3226) || Area(3226, 3228, 3230, 3211)
				|| Area(3227, 3229, 3208, 3226);
	}
	public boolean inMasterZone() {
		if (absX >= 1222 && absX <= 1279 && absY >= 3546 && absY <= 3568) {
			return true;
		}
		return false;
	}

	public boolean isInJail() {
		if (absX >= 2065 && absX <= 2111 && absY >= 4415 && absY <= 4455) {
			return true;
		}
		return false;
	}

	public boolean inCamWild() {
		if (absX > 3231 && absX < 3300 && absY > 3180 && absY < 3300
				|| absX > 3120 && absX < 3300 && absY > 3236 && absY < 3300
				|| absX > 3227 && absX < 3300 && absY > 3225 && absY < 3300) {
			return true;
		}
		return false;
	}

	public boolean inMining() {
		if (absY > 9700 && absY < 10366) {
			return true;
		}
		return false;
	}

	public boolean inWc() {
		if (absY > 3346 && absY < 3530 && absX > 2600 && absX < 2730) {
			return true;
		}
		return false;
	}

	public boolean inFarm() {
		if (absY > 3446 && absY < 3480 && absX > 2802 && absX < 2820) {
			return true;
		}
		return false;
	}

	public boolean inDz() {
		if (absY > 9620 && absY < 9657 && absX > 3344 && absX < 3381) {
			return true;
		}
		return false;
	}

	public boolean inFm() {
		if (absX > 2333 && absX < 2355 && absY > 3663 && absY < 3702) {
			return true;
		}
		return false;
	}

	public boolean inWild() {
		if (inFunPk2()) {
			return true;
		}
		if(isInYanille()) {
			return true;
			}

		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366
				|| absX >= 3194 && absX <= 3251 && absY >= 3190 && absY <= 3288
				|| absX <= 2777 && absX >= 2674 && absY <= 3520 && absY >= 3469
				|| absX >= 2935 && absX <= 3061 && absY >= 3302 && absY <= 3395
				|| absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711
				
				|| absX > 3543 && absX < 3584 && absY > 3265 && absY < 3311
				|| absX > 3529 && absX < 3581 && absY > 9673 && absY < 9722
				|| absX > 3520 && absX < 3598 && absY > 9653 && absY < 9750) {
			
			
			
//			public boolean isInBarrows() {
//				if (absX > 3543 && absX < 3584 && absY > 3265 && absY < 3311) {
//					return true;
//				}
//				return false;
//			}
//
//			public boolean isInBarrows2() {
//				if (absX > 3529 && absX < 3581 && absY > 9673 && absY < 9722) {
//					return true;
//				}
//				return false;
//			}
//
//			public boolean inBarrows() {
//				if (absX > 3520 && absX < 3598 && absY > 9653 && absY < 9750) {
//					return true;
//				}
//				return false;
//			}
			
			return true;
		}
		return false;
	}
	

	/*
	 * public boolean inWild() { //TRY THIS if(absX > 2941 && absX < 3392 &&
	 * absY > 3518 && absY < 3966 || absX > 2941 && absX < 3060 && absY > 3314
	 * && absY < 3399 || absX > 2941 && absX < 3392 && absY > 9918 && absY <
	 * 10366){ return true; } return false; }
	 */
	

	public boolean inClanWars() {
		if (Boundary.isIn(this, ClanWarsMap.getBoundaries())) {
			return true;
		}
		return false;
	}

	public boolean inFunPk() {
		return Boundary.isIn(this, Config.FUN_PK_BOUNDARY);
	}

	public boolean inFunPk2() {
		return (absX > 3156 && absX < 3176 && absY > 9748 && absY < 9768);
	}
	
	public boolean inEdgeville() {
		return (absX > 3040 && absX < 3200 && absY > 3460 && absY < 3519);
	}

	public boolean inDice() {
		if (absX > 2424 && absX < 2475 && absY > 3038 && absY < 3165) {
			return true;
		}
		return false;
	}

	public boolean arenas() {
		if (absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260) {
			return true;
		}
		return false;
	}

	public boolean inDuelArena() {
		if ((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291)
				|| (absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248)) {
			return true;
		}
		return false;
	}

	public boolean inMulti() {
		if (Boundary.isIn(this, Cerberus.WEST)) {
			return true;
		}
		
		if (HungerManager.getSingleton().inArena(this)) {
			return true;
		}
		
		if (Boundary.isIn(this, Lizardman.LIZARD)) {
			return true;
		}
		if (Boundary.isIn(this, Kalphite.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Cerberus.NORTH)) {
			return true;
		}
		if (Boundary.isIn(this, Cerberus.EAST)) {
			return true;
		}
		if (Boundary.isIn(this, Zulrah.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Kraken.BOUNDARY)) {
			return true;
		}

		if (Boundary.isIn(this, Armadyl.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Bandos.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Saradomin.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, Zamorak.BOUNDARY)) {
			return true;
		}
		if (Boundary.isIn(this, BOUNDARY_CORP)) {
			return true;
		}
		if (Boundary.isIn(this, Config.FUN_PK_MULTI_BOUNDARY)) {
			return true;
		}
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 2824 && absX <= 2944 && absY >= 5258 && absY <= 5369)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)
				|| (absX >= 2962 && absX <= 3006 && absY >= 3621 && absY <= 3659)
				|| (absX >= 3155 && absX <= 3214 && absY >= 3755 && absY <= 3803)) {
			return true;
		}
		return false;
	}

	public boolean inFightCaves() {
		return absX >= 2360 && absX <= 2445 && absY >= 5045 && absY <= 5125;
	}

	public boolean inPirateHouse() {
		return absX >= 3038 && absX <= 3044 && absY >= 3949 && absY <= 3959;
	}

	public boolean checkFullGear(Player c) {
		if (c.playerEquipment[0] >= 0 && c.playerEquipment[1] >= 0 && c.playerEquipment[2] >= 0
				&& c.playerEquipment[3] >= 0 && c.playerEquipment[4] >= 0 && c.playerEquipment[5] >= 0
				&& c.playerEquipment[7] >= 0 && c.playerEquipment[9] >= 0 && c.playerEquipment[10] >= 0
				&& c.playerEquipment[12] >= 0) {
			return true;
		}
		return false;
	}

	public void updateshop(int i) {
		Player p = PlayerHandler.players[index];
		p.getShops().resetShop(i);
	}

	public void println_debug(String str) {
		System.out.println("[player-" + index + "]: " + str);
	}

	public void println(String str) {
		System.out.println("[player-" + index + "]: " + str);
	}

	public boolean WithinDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel)
			return false;
		int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(NPC npc) {
		if (heightLevel != npc.heightLevel)
			return false;
		if (npc.needRespawn == true)
			return false;
		int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(int absX, int getY, int getHeightLevel) {
		if (this.getHeightLevel() != getHeightLevel)
			return false;
		int deltaX = this.getX() - absX, deltaY = this.getY() - getY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public int getHeightLevel() {
		return getHeightLevel;
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;

		for (int i = 0; i < walkingQueueSize; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	public void addToWalkingQueue(int x, int y) {
		// if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
		int next = (wQueueWritePtr + 1) % walkingQueueSize;
		if (next == wQueueWritePtr)
			return;
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
		// }
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if ((objectX + i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if ((objectX - i) == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& ((objectY + j) == playerY || (objectY - j) == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks the combat distance to see if the player is in an appropriate
	 * location based on the attack style.
	 * 
	 * @param attacker
	 * @param target
	 * @return
	 */
	public boolean checkCombatDistance(Player attacker, Player target) {
		int distance = Misc.distanceBetween(attacker, target);
		int required_distance = this.getDistanceRequired();
		return (this.usingMagic || this.usingRangeWeapon || this.usingBow || this.autocasting)
				&& distance <= required_distance
						? true
						: (this.usingMelee && this.isMoving && distance <= required_distance ? true
								: distance == 1 && (this.freezeTimer <= 0 || this.getX() == target.getX()
										|| this.getY() == target.getY()));
	}

	public int getDistanceRequired() {
		return !this.usingMagic && !this.usingRangeWeapon && !usingBow && !this.autocasting ? (this.isMoving ? 3 : 1)
				: 9;
	}

	public int getNextWalkingDirection() {

		if (wQueueReadPtr == wQueueWritePtr)
			return -1;
		int dir;

		do {
			dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);

			if (dir == -1) {
				wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
			} else if ((dir & 1) != 0) {
				println_debug("Invalid waypoint in walking queue!");
				resetWalkingQueue();
				return -1;
			}
		} while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
		if (dir == -1)
			return -1;
		dir >>= 1;
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];
		absX += Misc.directionDeltaX[dir];
		absY += Misc.directionDeltaY[dir];
		setLocation(Location.create(absX, absY, heightLevel));
		return dir;
	}

	public int oldX = absX;
	public int oldY = absY;

	public int oldHeight = heightLevel;

	private Location location;
	private Location lastLocation;

	private Position walkingDestination = new Position(absX, absY, heightLevel);

	public void setWalkingDestination(Position walkingDestination) {
		this.walkingDestination = walkingDestination;
	}

	public boolean hasWalkingDestination() {
		return walkingDestination.getX() != absX || walkingDestination.getY() != absY;
	}

	public Position getWalkingDestination() {
		return walkingDestination;
	}

	public Location getLastLocation() {
		if (lastLocation == null)
			lastLocation = getLocation().transform(Misc.random(-1, 1), Misc.random(-1, 1), 0);
		return lastLocation;
	}

	/*
	 * public void doFollow(boolean postProcess) {
	 * attr(A.POST_PROCESS_FOR_FOLLOWING, postProcess); if (followId > 0) {
	 * getPA().followPlayer(); } }
	 */

	// public static int otherDirection;

	public void setLastLocation() {
		this.lastLocation = getLocation();
	}

	public void setLocation(Location location) {
		setLastLocation();
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isRunning() {
		return isNewWalkCmdIsRunning() || (isRunning2 && isMoving);
	}

	public void getNextPlayerMovement() {
		mapRegionDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;
		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (mapRegionX != -1 && mapRegionY != -1) {
				int relX = teleportToX - mapRegionX * 8, relY = teleportToY - mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8)
					mapRegionDidChange = false;
			}
			if (mapRegionDidChange) {
				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
			}
			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			lastTeleport[0] = currentX;
			lastTeleport[1] = currentY;
			absX = teleportToX;
			absY = teleportToY;
			resetWalkingQueue();
			teleportToX = teleportToY = -1;
			didTeleport = true;
		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1) {
				setWalkingDestination(new Position(absX, absY, heightLevel));
				submitWalkInteractionTask();
				return;
			}
			if (isRunning) {
				dir2 = getNextWalkingDirection();
			}
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}

			if (mapRegionDidChange) {
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < walkingQueueSize; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}

		}
	}

	public void updateThisPlayerMovement(Stream str) {
		if (mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(mapRegionX + 6);
			str.writeWord(mapRegionY + 6);
			/*
			 * if(teleport == true) { teleport = false; }
			 */
		}

		if (didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3);
			str.writeBits(2, heightLevel);
			str.writeBits(1, 1);
			str.writeBits(1, (updateRequired) ? 1 : 0);
			str.writeBits(7, currentY);
			str.writeBits(7, currentX);
			return;
		}

		if (dir1 == -1) {
			// don't have to update the character position, because we're just
			// standing
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			isMoving = false;
			if (updateRequired) {
				// tell client there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
			if (DirectionCount < 50) {
				DirectionCount++;
			}
		} else {
			DirectionCount = 0;
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if (dir2 == -1) {
				isMoving = true;
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			} else {
				isMoving = true;
				str.writeBits(2, 2);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			}
		}
	}

	public void updatePlayerMovement(Stream str) {
		// synchronized(this) {
		if (dir1 == -1) {
			if (updateRequired || isChatTextUpdateRequired()) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else
				str.writeBits(1, 0);
		} else if (dir2 == -1) {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		}

	}

	public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {
		// synchronized(this) {
		int id = npc.index;
		npcInListBitmap[id >> 3] |= 1 << (id & 7);
		npcList[npcListSize++] = npc;

		str.writeBits(14, id);

		int z = npc.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = npc.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);

		str.writeBits(1, 0);
		str.writeBits(14, npc.npcType);

		boolean savedUpdateRequired = npc.updateRequired;
		npc.updateRequired = true;
		npc.appendNPCUpdateBlock(updateBlock);
		npc.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
	}

	public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
		// synchronized(this) {
		if (playerListSize >= 255) {
			return;
		}
		int id = plr.index;
		playerInListBitmap[id >> 3] |= 1 << (id & 7);
		playerList[playerListSize++] = plr;
		str.writeBits(11, id);
		str.writeBits(1, 1);
		boolean savedFlag = plr.isAppearanceUpdateRequired();
		boolean savedUpdateRequired = plr.updateRequired;
		plr.setAppearanceUpdateRequired(true);
		plr.updateRequired = true;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setAppearanceUpdateRequired(savedFlag);
		plr.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
		int z = plr.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = plr.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
	}
	
	public String title = "";
	public String titleColor = "ffb000";
	
	protected void appendPlayerAppearance(Stream str) {
		playerProps.currentOffset = 0;
		playerProps.writeByte(playerAppearance[0]);
		playerProps.writeString(title);
		playerProps.writeString(""+rights.GotDollarMark());
		if (venomDamage > 0) {
			playerProps.writeByte(2);
		} else if (poisonDamage > 0) {
			playerProps.writeByte(1);
		} else {
			playerProps.writeByte(0);
		}
		playerProps.writeByte(headIcon);
		playerProps.writeByte(headIconPk);
		playerProps.writeByte(bhSkull);
		if (isNpc == false) {
			if (playerEquipment[playerHat] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerHat]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerCape] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerCape]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerAmulet] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerAmulet]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerWeapon] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerWeapon]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerChest] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerChest]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[2]);
			}

			if (playerEquipment[playerShield] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerShield]);
			} else {
				playerProps.writeByte(0);
			}

			if (!Item.isFullBody(playerEquipment[playerChest])) {
				playerProps.writeWord(0x100 + playerAppearance[3]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerLegs] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerLegs]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[5]);
			}

			if (!Item.isFullHelm(playerEquipment[playerHat]) && !Item.isFullMask(playerEquipment[playerHat])) {
				playerProps.writeWord(0x100 + playerAppearance[1]);
			} else {
				playerProps.writeByte(0);
			}

			if (playerEquipment[playerHands] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerHands]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[4]);
			}

			if (playerEquipment[playerFeet] > 1) {
				playerProps.writeWord(0x200 + playerEquipment[playerFeet]);
			} else {
				playerProps.writeWord(0x100 + playerAppearance[6]);
			}

			if (playerAppearance[0] != 1 && !Item.isFullMask(playerEquipment[playerHat])) {
				playerProps.writeWord(0x100 + playerAppearance[7]);
			} else {
				playerProps.writeByte(0);
			}
		} else {
			playerProps.writeWord(-1);
			playerProps.writeWord(npcId2);
		}
		playerProps.writeByte(playerAppearance[8]);
		playerProps.writeByte(playerAppearance[9]);
		playerProps.writeByte(playerAppearance[10]);
		playerProps.writeByte(playerAppearance[11]);
		playerProps.writeByte(playerAppearance[12]);
		playerProps.writeWord(playerStandIndex); // standAnimIndex
		playerProps.writeWord(playerTurnIndex); // standTurnAnimIndex
		playerProps.writeWord(playerWalkIndex); // walkAnimIndex
		playerProps.writeWord(playerTurn180Index); // turn180AnimIndex
		playerProps.writeWord(playerTurn90CWIndex); // turn90CWAnimIndex
		playerProps.writeWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
		playerProps.writeWord(playerRunIndex); // runAnimIndex
		playerProps.writeQWord(Misc.playerNameToInt64(playerName));
		playerProps.writeByte(invisible ? 1 : 0);
		combatLevel = calculateCombatLevel();
		playerProps.writeByte(combatLevel); // combat level
		playerProps.writeByte(rights.getValue());
		playerProps.writeWord(0);
		str.writeByteC(playerProps.currentOffset);
		str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
	}

	public int calculateCombatLevel() {
		int j = getLevelForXP(playerXP[playerAttack]);
		int k = getLevelForXP(playerXP[playerDefence]);
		int l = getLevelForXP(playerXP[playerStrength]);
		int i1 = getLevelForXP(playerXP[playerHitpoints]);
		int j1 = getLevelForXP(playerXP[playerPrayer]);
		int k1 = getLevelForXP(playerXP[playerRanged]);
		int l1 = getLevelForXP(playerXP[playerMagic]);
		int combatLevel = (int) (((k + i1) + Math.floor(j1 / 2)) * 0.25D) + 1;
		double d = (j + l) * 0.32500000000000001D;
		double d1 = Math.floor(k1 * 1.5D) * 0.32500000000000001D;
		double d2 = Math.floor(l1 * 1.5D) * 0.32500000000000001D;
		if (d >= d1 && d >= d2) {
			combatLevel += d;
		} else if (d1 >= d && d1 >= d2) {
			combatLevel += d1;
		} else if (d2 >= d && d2 >= d1) {
			combatLevel += d2;
		}
		return combatLevel;
	}

	public static int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp)
				return lvl;
		}
		return 99;
	}

	protected void appendPlayerChatText(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8) + (getChatTextEffects() & 0xFF));
		str.writeByte(rights.getValue());
		str.writeByteC(getChatTextSize());
		str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);

	}

	public void appendForcedChat(Stream str) {
		// synchronized(this) {
		str.writeString(forcedText);
	}

	public void appendMask100Update(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(gfxVar1);
		str.writeDWord(gfxVar2);

	}

	public boolean wearing2h() {
		Player c = this;
		c.getItems();
		String s = c.getItems().getItemName(c.playerEquipment[c.playerWeapon]);
		if (s.contains("2h"))
			return true;
		else if (s.contains("godsword"))
			return true;
		return false;
	}

	public void appendAnimationRequest(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian((animId == -1) ? 65535 : animId);
		str.writeByteC(animDelay);

	}

	public void appendFaceUpdate(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(face);

	}

	public void ObjectClick(Player c, int objectID, int objectX, int objectY, int itemId) {
		switch (objectID) {
		case 24044:
			getPA().interface_components2(itemId);
			break;
		}
	}

	public boolean Interface(int id) {
		for (int i = 0; ItemDefinition.forId(id).isNoted(); i++) {
			if (id == i) {
				return true;
			}
		}
		return false;
	}

	private void appendSetFocusDestination(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndianA(faceX);
		str.writeWordBigEndian(faceY);

	}

	/**
	 * Hit Update
	 **/
	protected void appendHitUpdate(Stream str) {
		str.writeByte(hitDiff);
		if (hitmark == null) {
			str.writeByteA(0);
		} else {
			str.writeByteA(hitmark.getId());
		}
		if (playerLevel[3] <= 0) {
			playerLevel[3] = 0;
			isDead = true;
		}
		str.writeByteC(playerLevel[3]);
		str.writeByte(getLevelForXP(playerXP[3]));
	}

	protected void appendHitUpdate2(Stream str) {
		str.writeByte(hitDiff2);
		if (secondHitmark == null) {
			str.writeByteS(0);
		} else {
			str.writeByteS(secondHitmark.getId());
		}
		if (playerLevel[3] <= 0) {
			playerLevel[3] = 0;
			isDead = true;
		}
		str.writeByte(playerLevel[3]);
		str.writeByteC(getLevelForXP(playerXP[3]));

	}

	public void appendPlayerUpdateBlock(Stream str) {
		// synchronized(this) {
		if (!updateRequired && !isChatTextUpdateRequired())
			return; // nothing required
		int updateMask = 0;
		if (forceMovementUpdateRequired) {
			updateMask |= 0x400;
		}
		if (gfxUpdateRequired) {
			updateMask |= 0x100;
		}
		if (animUpdateRequired) {
			updateMask |= 8;
		}
		if (forcedChatUpdateRequired) {
			updateMask |= 4;
		}
		if (isChatTextUpdateRequired()) {
			updateMask |= 0x80;
		}
		if (isAppearanceUpdateRequired()) {
			updateMask |= 0x10;
		}
		if (faceUpdateRequired) {
			updateMask |= 1;
		}
		if (facePositionUpdateRequired) {
			updateMask |= 2;
		}
		if (hitUpdateRequired) {
			updateMask |= 0x20;
		}

		if (hitUpdateRequired2) {
			updateMask |= 0x200;
		}

		if (updateMask >= 0x100) {
			updateMask |= 0x40;
			str.writeByte(updateMask & 0xFF);
			str.writeByte(updateMask >> 8);
		} else {
			str.writeByte(updateMask);
		}
		if (forceMovementUpdateRequired) {
			appendMask400Update(str);
		}
		if (gfxUpdateRequired) {
			appendMask100Update(str);
		}
		if (animUpdateRequired) {
			appendAnimationRequest(str);
		}
		if (forcedChatUpdateRequired) {
			appendForcedChat(str);
		}
		if (isChatTextUpdateRequired()) {
			appendPlayerChatText(str);
		}
		if (faceUpdateRequired) {
			appendFaceUpdate(str);
		}
		if (isAppearanceUpdateRequired()) {
			appendPlayerAppearance(str);
		}
		if (facePositionUpdateRequired) {
			appendSetFocusDestination(str);
		}
		if (hitUpdateRequired) {
			appendHitUpdate(str);
		}
		if (hitUpdateRequired2) {
			appendHitUpdate2(str);
		}

	}

	private int x1 = -1;
	private int y1 = -1;
	private int x2 = -1;
	private int y2 = -1;
	private int speed1 = -1;
	private int speed2 = -1;
	private int direction = -1;

	/**
	 * Creates a force-movement update block. (0x400)
	 * 
	 * @param x2
	 * @param y2
	 * @param x1
	 * @param y1
	 * @param speed1
	 * @param speed2
	 * @param direction
	 * @param ticks
	 */
	public void setForceMovement(final int x2, final int y2, boolean x1, boolean y1, final int speed1, final int speed2,
			final int direction, final int ticks) {
		setCanWalk(false);
		this.x1 = getPosition().getLocalX();
		this.y1 = getPosition().getLocalY();
		this.x2 = x1 ? getPosition().getLocalX() + x2 : getPosition().getLocalX() - x2;
		this.y2 = y1 ? getPosition().getLocalY() + y2 : getPosition().getLocalY() - y2;
		this.speed1 = speed1;
		this.speed2 = speed2;
		this.direction = direction;
		updateRequired = true;
		forceMovementUpdateRequired = true;
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				absX = (x1 ? absX + x2 : absX - x2);
				absY = (y1 ? absY + y2 : absY - y2);
				container.stop();
			}
		}, ticks);
		CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				getPA().movePlayer(getPosition().getX(), getPosition().getY(), getPosition().getZ());
				setForceMovementRequired(false);
				setCanWalk(true);
				container.stop();
			}
		}, ticks + 1);
	}

	public void appendMask400Update(Stream str) {
		str.writeByteS(x1);
		str.writeByteS(y1);
		str.writeByteS(x2);
		str.writeByteS(y2);
		str.writeWordBigEndianA(speed1);
		str.writeWordA(speed2);
		str.writeByteS(direction);
	}

	@Override
	public void reset() {
		setChatTextUpdateRequired(false);
		setAppearanceUpdateRequired(false);
		forceMovementUpdateRequired = false;
	}

	public int[] lastTeleport = new int[2];

	/*
	 * public void stopMovement() { if (lastTeleport[0] == currentX &&
	 * lastTeleport[1] == currentY) return; if (teleportToX <= 0 && teleportToY
	 * <= 0) { teleportToX = absX; teleportToY = absY; } newWalkCmdSteps = 0;
	 * getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] =
	 * travelBackY[0] = 0; getNextPlayerMovement(); }
	 */

	public void stopMovement() {
		if (lastTeleport[0] == currentX && lastTeleport[1] == currentY) {
			newWalkCmdSteps = 0;
			getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
			return;
		}
		if (teleportToX <= 0 && teleportToY <= 0) {
			teleportToX = absX;
			teleportToY = absY;
		}
		newWalkCmdSteps = 0;
		getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
		getNextPlayerMovement();
	}

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}

	public void postProcessing() {
		if (newWalkCmdSteps > 0) {
			int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];

			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				do {
					lastDir = dir;
					if (--ptr < 0)
						ptr = walkingQueueSize - 1;

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break;
					}

				} while (ptr != wQueueWritePtr);
			} else
				found = true;

			if (!found)
				println_debug("Fatal: couldn't find connection vertex! Dropping packet.");
			else {
				wQueueWritePtr = wQueueReadPtr;

				addToWalkingQueue(currentX, currentY);

				if (dir != -1 && (dir & 1) != 0) {

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					int wayPointX2 = travelBackX[numTravelBackSteps - 1],
							wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}

					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
						println_debug("Fatal: The walking queue is corrupt! wp1=(" + wayPointX1 + ", " + wayPointY1
								+ "), " + "wp2=(" + wayPointX2 + ", " + wayPointY2 + ")");
					} else {
						dir >>= 1;
						found = false;
						int x = wayPointX1, y = wayPointY1;
						while (x != wayPointX2 || y != wayPointY2) {
							x += Misc.directionDeltaX[dir];
							y += Misc.directionDeltaY[dir];
							if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
							println_debug("Fatal: Internal error: unable to determine connection vertex!" + "  wp1=("
									+ wayPointX1 + ", " + wayPointY1 + "), wp2=(" + wayPointX2 + ", " + wayPointY2
									+ "), " + "first=(" + firstX + ", " + firstY + ")");
						} else
							addToWalkingQueue(wayPointX1, wayPointY1);
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
				}

			}

			isRunning = isNewWalkCmdIsRunning() || isRunning2;
		}
	}

	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public int getId() {
		return index;
	}

	public boolean inCook() {
		return absX >= 3029 && absX <= 3032 && absY >= 3382 && absY <= 3384;
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}

	public boolean inPcGame() {
		return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619;
	}

	public void setHitDiff(int hitDiff) {
		this.hitDiff = hitDiff;
	}

	public void setHitDiff2(int hitDiff2) {
		this.hitDiff2 = hitDiff2;
	}

	public int getHitDiff() {
		return hitDiff;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.chatTextUpdateRequired = chatTextUpdateRequired;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setNewWalkCmdX(int newWalkCmdX[]) {
		this.newWalkCmdX = newWalkCmdX;
	}

	public int[] getNewWalkCmdX() {
		return newWalkCmdX;
	}

	public void setNewWalkCmdY(int newWalkCmdY[]) {
		this.newWalkCmdY = newWalkCmdY;
	}

	public int[] getNewWalkCmdY() {
		return newWalkCmdY;
	}

	public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
		this.newWalkCmdIsRunning = newWalkCmdIsRunning;
	}

	public boolean isNewWalkCmdIsRunning() {
		return newWalkCmdIsRunning;
	}

	public void setInStreamDecryption() {
	}

	public void setOutStreamDecryption() {
	}

	public boolean samePlayer() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == index)
				continue;
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName.equalsIgnoreCase(playerName)) {
					disconnected = true;
					return true;
				}
			}
		}
		return false;
	}

	public void putInCombat(int attacker) {
		underAttackBy = attacker;
		logoutDelay.reset();
		singleCombatDelay.reset();
	}

	public int appendDamage(int damage, Hitmark h) {
		if (damage <= 0) {
			damage = 0;
			h = Hitmark.MISS;
		}
		if (playerLevel[playerHitpoints] - damage < 0) {
			damage = playerLevel[playerHitpoints];
		}
		if (teleTimer <= 0) {
			playerLevel[3] -= damage;
			if (!hitUpdateRequired) {
				hitUpdateRequired = true;
				hitDiff = damage;
				hitmark = h;
			} else if (!hitUpdateRequired2) {
				hitUpdateRequired2 = true;
				hitDiff2 = damage;
				secondHitmark = h;
			}
			this.getPA().refreshSkill(3);
		} else {
			if (hitUpdateRequired) {
				hitUpdateRequired = false;
			}
			if (hitUpdateRequired2) {
				hitUpdateRequired2 = false;
			}
		}
		updateRequired = true;
		if (hungerGames && HungerManager.getSingleton().gameRunning())
			HungerManager.getSingleton().applyDamage(damage);
		return damage;
	}

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public String getLastClanChat() {
		return lastClanChat;
	}

	public long getNameAsLong() {
		return nameAsLong;
	}

	public void setNameAsLong(long hash) {
		this.nameAsLong = hash;
	}

	public boolean isStopPlayer() {
		return stopPlayer;
	}

	public void setStopPlayer(boolean stopPlayer) {
		this.stopPlayer = stopPlayer;
	}

	public int getPlayerIdentifier() {
		return this.index;
	}

	public int getFace() {
		return this.index + '\u8000';
	}

	public int getLockIndex() {
		return -this.index - 1;
	}

	public int getHeight() {
		return this.heightLevel;
	}

	public boolean isDead() {
		return this.playerLevel[3] <= 0 || this.isDead;
	}

	public void healPlayer(int heal) {
		this.playerLevel[3] += heal;
		if (this.playerLevel[3] > Player.getLevelForXP(this.playerXP[3])) {
			this.playerLevel[3] = Player.getLevelForXP(this.playerXP[3]);
		}

	}

	int maxLevel() {
		return 99;
	}

	public void sendGraphic(int id, int height) {
		if (height == 0) {
			this.gfx0(id);
		}

		if (height == 100) {
			this.gfx100(id);
		}

	}

	public boolean protectingRange() {
		return this.prayerActive[17];
	}

	public boolean protectingMagic() {
		return this.prayerActive[16];
	}

	public boolean protectingMelee() {
		return this.prayerActive[18];
	}

	public void setTrading(boolean trading) {
		this.trading = trading;
	}

	public boolean isTrading() {
		return this.trading;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
	}

	public boolean inGodmode() {
		return godmode;
	}

	public void setGodmode(boolean godmode) {
		this.godmode = godmode;
	}

	public double getDropModifier() {
		return dropModifier;
	}

	public void setDropModifier(double modifier) {
		this.dropModifier = modifier;
	}

	public boolean isDebug() {
		return debug == 1 || getRights().isOwner() || getRights().isAdministrator();
	}

	public void setDebug(int debug) {
		this.debug = debug;
	}

	public boolean inSafemode() {
		return safemode;
	}

	public void setSafemode(boolean safemode) {
		this.safemode = safemode;
	}

	public void setDragonfireShieldCharge(int charge) {
		this.dragonfireShieldCharge = charge;
	}

	public int getDragonfireShieldCharge() {
		return dragonfireShieldCharge;
	}

	public void setLastDragonfireShieldAttack(long lastAttack) {
		this.lastDragonfireShieldAttack = lastAttack;
	}

	public long getLastDragonfireShieldAttack() {
		return lastDragonfireShieldAttack;
	}

	public boolean isDragonfireShieldActive() {
		return dragonfireShieldActive;
	}

	public void setDragonfireShieldActive(boolean dragonfireShieldActive) {
		this.dragonfireShieldActive = dragonfireShieldActive;
	}

	/**
	 * Retrieves the rights for this player.
	 * 
	 * @return the rights
	 */
	public Rights getRights() {
		return rights;
	}

	/**
	 * Updates the rights for this player by comparing the players current
	 * rights to that of the available rights and assigning the first rank
	 * found.
	 */
	public void setRights(Rights rights) {
		this.rights = rights;
	}

	/**
	 * Mutates the current hitmark to be that of the new hitmark. This is
	 * commonly done in battle.
	 * 
	 * @param hitmark
	 *            the hitmark
	 */
	public void setHitmark(Hitmark hitmark) {
		this.hitmark = hitmark;
	}

	/**
	 * Mutates the first and second hitmark. This is commonly done in battle.
	 * 
	 * @param hitmark
	 *            the first hitmark
	 * @param secondHitmark
	 *            the second hitmark
	 */
	public void setHitmarks(Hitmark hitmark, Hitmark secondHitmark) {
		this.hitmark = hitmark;
		this.secondHitmark = secondHitmark;
	}

	public void appendPoisonDamage() {
		if (hasBeenVenomed) {
			hasBeenPoisoned = false;
			getPA().requestUpdates();
			return;
		}
		if (poisonDamage <= 0) {
			sendMessage("The poison has subsided.");
			hasBeenPoisoned = false;
			getPA().requestUpdates();
			return;
		}
		Player client = this;
		if (poisonDamageHistory.size() >= 4) {
			poisonDamageHistory.clear();
			poisonDamage--;
		}
		poisonDamageHistory.add(poisonDamage);
		appendDamage(poisonDamage, Hitmark.POISON);
		client.getPA().requestUpdates();
	}

	public void appendVenomDamage() {
		if (getItems().isWearingItem(12931)) {
			sendMessage("Your sepertine helm protects you from the venom.");
			getPA().requestUpdates();
			return;
		}
		if (venomDamage <= 0) {
			sendMessage("Your venom infection has cleared up!");
			hasBeenVenomed = false;
			getPA().requestUpdates();
			return;
		}
		venomDamage += 2;
		if (venomDamage >= 20) {
			venomDamage = 20;
		}
		appendDamage(venomDamage, Hitmark.VENOM);
		getPA().requestUpdates();
	}

	/**
	 * Determines if the player is susceptible to poison but comparing the
	 * duration of their immunity to the time of the last cure.
	 * 
	 * @return true of they can be poisoned.
	 */
	public boolean isSusceptibleToPoison() {
		return System.currentTimeMillis() - this.lastPoisonCure > this.poisonImmunity;
	}

	/**
	 * The duration of time in milliseconds the player is immune to poison for
	 * 
	 * @return the duration of time the player is immune to poison for
	 */
	public long getPoisonImmunity() {
		return poisonImmunity;
	}

	/**
	 * Modifies the current duration of poison immunity
	 * 
	 * @param duration
	 *            the new duration
	 */
	public void setPoisonImmunity(long duration) {
		this.poisonImmunity = duration;
	}

	/**
	 * The amount of damage received when hit by toxic
	 * 
	 * @return the toxic damage
	 */
	public byte getPoisonDamage() {
		return poisonDamage;
	}

	/**
	 * Sets the current amount of damage received when hit by toxic
	 * 
	 * @param toxicDamage
	 *            the new amount of damage received
	 */
	public void setPoisonDamage(byte toxicDamage) {
		this.poisonDamage = toxicDamage;
	}

	/**
	 * The time in milliseconds of the last toxic damage the player received
	 * 
	 * @return the time in milliseconds of the last toxic damage hit
	 */
	public long getLastPoisonHit() {
		return lastPoisonHit;
	}

	/**
	 * Sets the last time, in milliseconds, the toxic damaged the player
	 * 
	 * @param toxic
	 *            the time in milliseconds
	 */
	public void setLastPoisonHit(long toxic) {
		lastPoisonHit = toxic;
	}

	/**
	 * Sets the current amount of damage received when hit by venom
	 * 
	 * @param venomDamage
	 *            the new amount of damage received
	 */
	public void setVenomDamage(byte venomDamage) {
		this.venomDamage = venomDamage;
	}

	/**
	 * The time in milliseconds of the last venom damage the player received
	 * 
	 * @return the time in milliseconds of the last venom damage hit
	 */
	public long getLastVenomHit() {
		return lastVenomHit;
	}

	/**
	 * Sets the last time, in milliseconds, the venom damaged the player
	 * 
	 * @param toxic
	 *            the time in milliseconds
	 */
	public void setLastVenomHit(long toxic) {
		lastVenomHit = toxic;
	}

	/**
	 * Determines if the player is susceptible to venom by comparing the
	 * duration of their immunity to the time of the last cure.
	 * 
	 * @return true of they can be infected by venom.
	 */
	public boolean isSusceptibleToVenom() {
		return System.currentTimeMillis() - lastVenomCure > venomImmunity && !getItems().isWearingItem(12931);
	}

	/**
	 * The duration of time in milliseconds the player is immune to venom for
	 * 
	 * @return the duration of time the player is immune to poison for
	 */
	public long getVenomImmunity() {
		return venomImmunity;
	}

	/**
	 * Modifies the current duration of venom immunity
	 * 
	 * @param duration
	 *            the new duration
	 */
	public void setVenomImmunity(long duration) {
		this.venomImmunity = duration;
	}

	/**
	 * The amount of damage received when hit by venom
	 * 
	 * @return the venom damage
	 */
	public byte getVenomDamage() {
		return venomDamage;
	}

	/**
	 * The time in milliseconds that the player healed themselves of venom
	 * 
	 * @return the last time the player cured themself of poison
	 */
	public long getLastVenomCure() {
		return lastVenomCure;
	}

	/**
	 * Sets the time in milliseconds that the player cured themself of poison
	 * 
	 * @param lastVenomCure
	 *            the last time the player cured themselves
	 */
	public void setLastVenomCure(long lastVenomCure) {
		this.lastVenomCure = lastVenomCure;
	}

	/**
	 * Mutates the current hitmark to be that of the new hitmark. This is
	 * commonly done in battle.
	 * 
	 * @param secondHitmark
	 *            the hitmark
	 */
	public void setSecondHitmark(Hitmark secondHitmark) {
		this.secondHitmark = secondHitmark;
	}

	/**
	 * The time in milliseconds that the player healed themselves of poison
	 * 
	 * @return the last time the player cured themself of poison
	 */
	public long getLastPoisonCure() {
		return lastPoisonCure;
	}

	/**
	 * Sets the time in milliseconds that the player cured themself of poison
	 * 
	 * @param lastPoisonCure
	 *            the last time the player cured themselves
	 */
	public void setLastPoisonCure(long lastPoisonCure) {
		this.lastPoisonCure = lastPoisonCure;
	}

	/**
	 * Retrieves the current hitmark
	 * 
	 * @return the hitmark
	 */
	public Hitmark getHitmark() {
		return hitmark;
	}

	/**
	 * Retrieves the second hitmark
	 * 
	 * @return the second hitmark
	 */
	public Hitmark getSecondHitmark() {
		return secondHitmark;
	}

	public RandomEventInterface getInterfaceEvent() {
		return randomEventInterface;
	}

	/**
	 * The interface that is currently open
	 */
	private int interfaceOpen;

	/**
	 * Modifies the current interface open
	 * 
	 * @param interfaceOpen
	 *            the interface id
	 */
	public void setInterfaceOpen(int interfaceOpen) {
		this.interfaceOpen = interfaceOpen;
	}

	/**
	 * The interface that is opened
	 * 
	 * @return the interface id
	 */
	public int getInterfaceOpen() {
		return interfaceOpen;
	}

	public long setBestZulrahTime(long bestZulrahTime) {
		return this.bestZulrahTime = bestZulrahTime;
	}

	public long getBestZulrahTime() {
		return bestZulrahTime;
	}

	public LostItems getLostItems() {
		if (lostItems == null) {
			lostItems = new LostItems(this);
		}
		return lostItems;
	}

	public int getToxicBlowpipeCharge() {
		return toxicBlowpipeCharge;
	}

	public void setToxicBlowpipeCharge(int charge) {
		this.toxicBlowpipeCharge = charge;
	}

	public int getToxicBlowpipeAmmo() {
		return toxicBlowpipeAmmo;
	}

	public int getToxicBlowpipeAmmoAmount() {
		return toxicBlowpipeAmmoAmount;
	}

	public void setToxicBlowpipeAmmoAmount(int amount) {
		this.toxicBlowpipeAmmoAmount = amount;
	}

	public void setToxicBlowpipeAmmo(int ammo) {
		this.toxicBlowpipeAmmo = ammo;
	}

	public int getSerpentineHelmCharge() {
		return this.serpentineHelmCharge;
	}

	public int getToxicStaffOfDeadCharge() {
		return this.toxicStaffOfDeadCharge;
	}

	public void setSerpentineHelmCharge(int charge) {
		this.serpentineHelmCharge = charge;
	}

	public void setToxicStaffOfDeadCharge(int charge) {
		this.toxicStaffOfDeadCharge = charge;
	}

	public int getTridentCharge() {
		return tridentCharge;
	}

	public void setTridentCharge(int tridentCharge) {
		this.tridentCharge = tridentCharge;
	}

	public int getToxicTridentCharge() {
		return toxicTridentCharge;
	}

	public void setToxicTridentCharge(int toxicTridentCharge) {
		this.toxicTridentCharge = toxicTridentCharge;
	}

	public static final int maxPlayerListSize = Config.MAX_PLAYERS;
	public static final int maxNPCListSize = NPCHandler.maxNPCs;
	protected static Stream playerProps;
	public static boolean grabProjectile = false;
	// public int specDelay = 0;

	static {
		playerProps = new Stream(new byte[100]);
	}

	public void updateRank() {
		if (amDonated >= 10 && amDonated < 50 && getRights().getValue() == 0) {
			setRights(Rights.DONATOR);
		}
		if (amDonated >= 50 && amDonated < 150 && getRights().getValue() <= 5 && !getRights().isStaff()) {
			setRights(Rights.HONORED_DONATOR);
		}
		if (amDonated >= 150 && amDonated < 500 && getRights().getValue() <= 6 && !getRights().isStaff()) {
			setRights(Rights.RESPECTED_DONATOR);
		}
		if (amDonated >= 500 && amDonated < 1000 && getRights().getValue() <= 7 && !getRights().isStaff()) {
			setRights(Rights.LEGENDARY_DONATOR);
		}
		/*
		 * if (amDonated >= 300 && !getRights().isStaff()) {
		 * setRights(Rights.SUPER_V_I_P); }
		 */
	}

	public QuickPrayer getQuick() {
		return quick;
	}

	public Killstreak getKillstreaks() {
		return killstreaks;
	}

	public void setKillstreaks(Killstreak killstreaks) {
		this.killstreaks = killstreaks;
	}

	public int  musicLevel = 1, soundLevel = 1, areaLevel = 1, mouseButtons = 1, clickState = 1;
	public boolean splitPrivateChat, acceptAid = true, showOrbs, hideRoofs, mouseCameraScroll;
	public boolean acceptAidOn;

	/**
	 * The option value used for npc dialogues.
	 */
	// public OptionType option;

	public void setSplitPrivateChat(boolean splitPrivateChat) {
		this.splitPrivateChat = splitPrivateChat;
		getPA().setConfig(287, splitPrivateChat ? 1 : 0);
	}

	public int getMouseButtons() {
		return mouseButtons;
	}

	public void setMouseButtons(int mouseButtons) {
		this.mouseButtons = mouseButtons;
		getPA().setConfig(170, mouseButtons == 2 ? 0 : 1);
		getPA().setConfig(313, mouseButtons == 2 ? 1 : 0);
	}

	public void setAcceptAid(boolean acceptAid) {
		this.acceptAid = acceptAid;
		getPA().setConfig(304, acceptAid ? 1 : 0);
		getPA().setConfig(427, acceptAid ? 1 : 0);
	}

	public void setRoofs(boolean hideRoofs) {
		this.hideRoofs = hideRoofs;
		getPA().setConfig(307, hideRoofs ? 1 : 0);
		getPA().sendString(":roofs:" + hideRoofs, -1);
	}

	public void setClickState(int clickState) {
		this.clickState = clickState;
		getPA().sendString(":clickstate:" + clickState, -1);
		switch (clickState) {
		case 1:
			getPA().setConfig(315, 1);
			getPA().setConfig(316, 0);
			getPA().setConfig(317, 0);
			break;
		case 2:
			getPA().setConfig(315, 0);
			getPA().setConfig(316, 1);
			getPA().setConfig(317, 0);
			break;
		case 3:
			getPA().setConfig(315, 0);
			getPA().setConfig(316, 0);
			getPA().setConfig(317, 1);
			break;
		}
	}

	public void setMouseCameraScroll(boolean mouseCameraScroll) {
		this.mouseCameraScroll = mouseCameraScroll;
		getPA().sendString(":mousecamerascroll:" + mouseCameraScroll, -1);
		getPA().setConfig(314, mouseCameraScroll ? 1 : 0);
	}

	public void setOrbs(boolean showOrbs) {
		this.showOrbs = showOrbs;
		getPA().setConfig(306, showOrbs ? 1 : 0);
		getPA().sendString(":orbs:" + showOrbs, -1);
	}

	private final int STANDARD_BRIGHTNESS = 2;
	
	public int getBrightness() {
		return screenBrightness;
	}
	public void setBrightness(int level){
		if (level > 4) level = 4;
		else if (level < 0) level = 1;
		else if (level == 0) level = STANDARD_BRIGHTNESS;
		
		getPA().sendFrame36(505, level == 1 ? 1 : 0);
		getPA().sendFrame36(506, level == 2 ? 1 : 0);
		getPA().sendFrame36(507, level == 3 ? 1 : 0);
		getPA().sendFrame36(508, level == 4 ? 1 : 0);
		getPA().sendFrame36(166, level);
		screenBrightness = level;
		}

	/*
	 * public int getPkWorth() { return 40000 + (killStreak > 1 ? (killStreak *
	 * 12500) : 0); }
	 */
	/**
	 * Constructs a new attributes instance
	 */
	private final Attributes1 attributes1 = new Attributes1();
	private Attributes attributes = new Attributes();
	public int pouch1;
	public int pouch1N;
	public int pouch2;
	public int pouch2N;
	public int pouch3;
	public int pouch3N;
	public int damagedealt;
	public int screenBrightness = 0;
	public boolean hasRejuvinated = false;
	public int addToCount;
	public boolean showLocation = false;
	// public long recordLogin;
	public long CORP_TIME;
	public boolean spawnbrid = false;
	public long spawnSet;
	public int dropChance;
	public int dropChance2;
	public int dropChance3;

	@SuppressWarnings("unchecked")
	public <T> T attr(int key) {
		return (T) attributes.getAttribute(key);
	}

	public void attr(int key, Object value) {
		attributes.setAttribute(key, value);
	}

	public long getLastVote() {
		return lastVoted;
	}

	public void setLastVote(long time) {
		this.lastVoted = time;
	}

	public void setLastAttempt(long time) {
		this.lastAttempt = time;
	}

	private long lastVoted;
	public long lastAttempt;

	public boolean hasVoted() {
		long timeLeft = (lastVoted - System.currentTimeMillis());
		long hours = TimeUnit.MILLISECONDS.toHours(timeLeft);
		long min = TimeUnit.MILLISECONDS.toMinutes(timeLeft)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeLeft));
		long sec = TimeUnit.MILLISECONDS.toSeconds(timeLeft)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeft));
		String time = "" + hours + "h " + min + "m " + sec + "s";
		if (timeLeft > 0) {
			sendMessage("You must wait <col=FF0000>" + time + "</col> before you may claim another reward.");
			return true;
		}
		return false;
	}

	public void faceUpdate(int index) {
		face = index;
		faceUpdateRequired = true;
		updateRequired = true;
	}

	public long getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(long id) {
		this.uniqueIdentifier = id;
	}

	public Stopwatch getSqlTimer() {
		return sqlTimer;
	}

	public boolean hasValidMac() {
		if (getMacAddress().length() == 17) {
			if (getMacAddress().substring(2, 3).equals("-") && getMacAddress().substring(5, 6).equals("-")
					&& getMacAddress().substring(8, 9).equals("-") && getMacAddress().substring(11, 12).equals("-")
					&& getMacAddress().substring(14, 15).equals("-")) {
				return true;
			}
		}
		return false;
	}

	int[] barrowsPieces = { 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734, 4736,
			4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759 };
	int randomBarrowsPiece = barrowsPieces[Misc.random(barrowsPieces.length - 1)];
	public int blastPoints = 0;

	public boolean hasBarrowsPieces() {
		for (int i = 0; i < barrowsPieces.length; i++) {
			if (getItems().playerHasItem(barrowsPieces[i], 1) && getItems().playerHasItem(barrowsPieces[i], 1)) {
				return true;
			}
		}
		return false;
	}

	public int[] runes = new int[3];
	public int[] runeAmount = new int[3];
	public int runesInPouch;

	public int[] lootBag = new int[28];
	public int[] amountLoot = new int[28];
	public int itemsInLootBag;
	public int reset = 0;
	public int hits = 0;
	public int changedTaskAmount = 0;
	public int lastSent;
	public long specAlter;
	public long lastSpec;
	public String memberName = "";
	public int clickTimer = 0;
	
	public int quickSelected;
	public boolean inWild;

	public boolean inEdgeBank() {
		if (absX > 3091 && absX < 3098 && absY < 3499 && absY > 3488) {
			return true;
		} else
			return false;
	}
	public boolean isInYanille() {
		if(absX > 2607 && absX < 2539 && absY > 3093 && absY < 3091) {	
			return true;
		} else
			return inWild;
	}

	public boolean hasBoneCrusher() {
		if (getItems().playerHasItem(13116)) {
			return true;
		}
		return false;
	}

	public ItemPouch getHerbSack() {
		return herbSack;
	}

	public ItemPouch getGemBag() {
		return gemBag;
	}

}