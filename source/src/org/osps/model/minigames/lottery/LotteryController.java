package org.osps.model.minigames.lottery;

import org.osps.Server;
import org.osps.model.players.Player;

public class LotteryController {
	
	public static void newEntryPKP(Player player, int npcId) {
		int amount = 1000;
		if (player == null || player.disconnected || player.teleporting || player.isDead) {
			return;
		}
		if (amount < 0) {
			return;
		}
		if (player.pkp < amount) {
			player.sendMessage("You don't have enough PKP for that.");
			return;
		}
		
		player.pkp -= amount;
		player.getDH().sendStatement("You've entered the lottery.");
    	Server.getLottery().enterLottery(player.getUsername1());
	}
	
	/*

    public static final int MINUTES_BETWEEN_SPAWNS = 2;
    private static final int NOTIF_COUNTDOWN =  (int) TimeUnit.MINUTES.toSeconds(5);
    private static final int SECONDARY_NOTIFICATION =  (int) TimeUnit.MINUTES.toSeconds(2);
    private static final int COUNTDOWN = (int) TimeUnit.MINUTES.toSeconds(MINUTES_BETWEEN_SPAWNS * 2);
	

    private static int timer = COUNTDOWN;
    public static Lottery lottery = new Lottery( null );
    
    public static final void tick() {
        if (timer == NOTIF_COUNTDOWN) {
        	if (lottery.getEntries().size() == 0) {
        		int winnings = (int) (lottery.getJackpot() * .85);
        		msgAll("@bla@[@blu@Lottery@bla@] The lottery will be drawn in about @red@" + MINUTES_BETWEEN_SPAWNS + " @bla@minutes! (@red@" + winnings + " @bla@PKP Jackpot)");
        	}
        }
        if (timer == SECONDARY_NOTIFICATION) {
        	if (lottery.getEntries().size() == 0) {
        		int winnings = (int) (lottery.getJackpot() * .85);
        		msgAll("@bla@[@blu@Lottery@bla@] The lottery will be drawn in about @red@2 @bla@minutes! (@red@" + winnings + " @bla@PKP Jackpot)");
        	}
        }
        if (timer == 0) {
        	if (lottery.getEntries().size() == 0) {
        		drawWinner();
        	} else {
        		timer = COUNTDOWN;
        	}
        }
        if (timer != 0) {
            timer--;
        }
    }
    
    public static int chances(Player player) {
    	if (lottery.getEntries().size() == 0) {
    		return 0;
    	}
    	for (Entry entries : lottery.getEntries()) {
    		if (entries.getName().equals(player.getUsername())) {
    			return (int) ((double) entries.getPkpEntered() / (double) lottery.getJackpot());
    		}
    	}
    	return 0;
    }
    
    public static void newEntryPKP(Player player, int amount, int npcId) {
    	System.out.println("got here?");
		if (player == null || player.disconnected || player.teleporting || player.isDead) {
			return;
		}
		if (amount < 0) {
			return;
		}
		if (player.pkp < amount) {
			player.sendMessage("You don't have enough PKP for that.");
			return;
		}
		
		player.pkp -= amount;
		System.out.println("got here 2");
		if (lottery.getEntries() != null) {
			for (Entry entry : lottery.getEntries()) {
				if (entry.getName().equals(player.getUsername())) {
					entry.increasePkpEnteredBy(amount);
					return;
				}
			}
		}
    	System.out.println("got here 3");
    	lottery.addEntry(new Entry(player.getUsername(), amount));
    	System.out.println("got here 5");
    	lottery.print();
	}
    
    public static void newEntryItem(final Player player, final int item) {
    	if (player == null || player.disconnected || player.teleporting || player.isDead) {
			return;
		}
		int amount = 1;
		int price = ShopAssistant.getItemShopValue(item);
		
		if(!(Unspawnable.canSpawn(ItemDefinition.forId(item).getId()).length() > 1)){
			player.getDH().sendStatement("The lottery doesn't seem to be interested in this item.");
			return;
		}
		
		player.getDH().sendStatement("Contributing...");
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				if (container.getTotalTicks() == 2) {
					container.stop();
				}
			}

			@Override
			public void stop() {
				if (!player.getItems().playerHasItem(item)) {
					player.getDH().sendStatement("Error! Item not found...");
					return;
				}
				if (ItemDefinition.forId(item).isStackable()) {
					player.getItems().deleteItem(item, amount);
				} else if (!ItemDefinition.forId(item).isStackable()) {
						player.getItems().deleteItem(item, 1);
				}
				
			}
		}, 1);
		
		enter(player.getUsername(), price);
    }
    
	private static void enter(String username, int amount) {
    	System.out.println("got here 2");
    	for (Entry entry : lottery.entries) {
    		if (entry.getName().equals(username)) {
    			entry.increasePkpEnteredBy(amount);
    			return;
    		}
    	}
    	System.out.println("got here 3");
    	Entry entry = new Entry(username, amount);
    	lottery.addEntry(entry);
    }
    
    public static void drawWinner() {
    	Random random = new Random();
    	int jackpot = lottery.getJackpot();
    	int winner = (int) (jackpot * random.nextDouble());
    	int count = 0;
    	List<Entry> entries = lottery.getEntries();
    	for (Entry entry : entries) {
    		count += entry.getPkpEntered();
    		if (count >= winner) {
    			if (PlayerHandler.isPlayerOn(entry.getName())) {
    				Player player = PlayerHandler.getPlayer(entry.getName());
    				int winnings = (int) (jackpot * .85);
    				player.pkp += winnings;
    				msgAll("@bla@[@blu@Lottery@bla@] The lottery has been drawn! @red@" + entry.getName() + "@bla@ has won the @red@" + winnings + " @bla@ PKP Jackpot)");
    				return;
    			} else {
    				continue;
    			}
    		}
    	}
    }
    
    public static void restart() {
        timer = COUNTDOWN;
        lottery = new Lottery(null);
    }
    
    
    private static void msgAll(String msg) {
        Arrays.stream(PlayerHandler.players).filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(msg));
    }
    */
    
	/**
	 * No longer using Gson for this, but might want this code later on.
	 */
/*	public static void newEntryPKP(Player player, int amount, int npcId) throws IOException {
		if (player == null || player.disconnected || player.teleporting || player.isDead) {
			return;
		}
		if (player.pkp < amount) {
			player.sendMessage("You don't have enough PKP for that.");
			return;
		}
		
		player.pkp -= amount;
		
		Entry entry = new Entry(player.getUsername(), amount);
		Lottery lottery = readLottery();
		lottery.addEntry(entry);
		writeLottery(lottery);
	}
	
	public static void newEntryPKP(String username, int amount) throws IOException {
		Entry entry = new Entry(username, amount);
		Lottery lottery = readLottery();
		lottery.addEntry(entry);
		writeLottery(lottery);
	}
	
	public static Lottery readLottery() throws IOException {
		try (Reader reader = new InputStreamReader(LotteryController.class.getResourceAsStream("Lottery.json"), "UTF-8")) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Lottery l = gson.fromJson(reader, Lottery.class);
			return l;
		}
	}
	
	public static void writeLottery(Lottery lottery) throws IOException {
		try (Writer writer = new OutputStreamWriter(new FileOutputStream("Lottery.json"), "UTF-8")) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(lottery, writer);
		}
	} */
	
}
