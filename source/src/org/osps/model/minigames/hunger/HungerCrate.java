package org.osps.model.minigames.hunger;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.items.Item;
import org.osps.model.items.ItemDefinition;
import org.osps.model.players.combat.Hitmark;
import org.osps.util.Misc;
import org.osps.world.objects.GlobalObject;

public class HungerCrate {

	public HungerCrate(int x, int y) {
		if(((int) Math.random() * 250) == 25) {
			q = Quality.SUPERITEM;
		} else if (((int) Math.random() * 2) != 1) {
			q = Quality.ITEM;
		} else {
			q = Quality.values()[(int) (Math.random() * (Quality.values().length - 1))];
		}
		this.x = x;
		this.y = y;
		create();
	}
	
	public boolean isCrate(int x, int y) {
		return this.x == x && this.y == y;
	}
	
	public HungerCrate(int x, int y, Quality q) {
		this.q = q;
		this.x = x;
		this.y = y;
		create();
	}
	
	public void create() {
		obj = new GlobalObject(OBJECT_ID, x, y, 0, 0, 10, -1);
		Server.getGlobalObjects().add(obj);
	}
	
	final int OBJECT_ID = 1990;

	public void destroy() {
		Server.getGlobalObjects().remove(obj);
	}
	
	public void claimCrate(PlayerWrapper p) {
		p.getPlayer().sendMessage("You open the crate...");
		p.getPlayer().animation(1895);
		CycleEventHandler.getSingleton().addEvent(p.getPlayer(), new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				int skill = Misc.random(6);
				switch(q) {
					case EXP:
						p.getPlayer().sendMessage("...and find exp!");
						int amount = 4 + Misc.random(4);
						if(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[skill]) + amount > 99) {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(99), skill);
						} else {
							p.getPlayer().getPA().addSkillXP(p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP
									(p.getPlayer().playerXP[skill]) + amount)
									- p.getPlayer().getPA().getXPForLevel(p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[skill])), skill, false);
						}
						break;
					case FOOD:
						p.getPlayer().sendMessage("...and find some food!");
						p.getPlayer().getItems().addItem(385, 2);
						break;
					case BLOOD:
						p.getPlayer().sendMessage("...and find some blood?");
						p.getPlayer().getItems().addItem(8125, 1);
						break;
					case ITEM:
						int tier = p.getPlayer().getPA().getLevelForXP(p.getPlayer().playerXP[skill]) / 10;
						int index =Misc.random(ITEMS[skill][tier].length - 1);
						int drop = ITEMS[skill][tier][index];
						if(ItemDefinition.forId(drop).isStackable())
							p.getPlayer().getItems().addItem(drop, Misc.random(150) + 50);
						else
							p.getPlayer().getItems().addItem(drop, 1);
						p.getPlayer().sendMessage("...and receive some " + Item.getItemName(drop) + "!");
						break;
					case SUPERITEM:
						if(Misc.random(15) == 0 && !p.hasAllDiamonds()) {
							int item = p.getRandomDiamond();
							if(item != -1) {
								p.getPlayer().getItems().addItem(item, 1);
								p.getPlayer().sendMessage("...you find an ultra rare diamond!");
								break;
							}
						}
						int drop2 = SUPERITEMS[Misc.random(SUPERITEMS.length - 1)];
						if(ItemDefinition.forId(drop2).isStackable())
							p.getPlayer().getItems().addItem(drop2, Misc.random(400) + 100);
						else
							p.getPlayer().getItems().addItem(drop2, 1);
						p.getPlayer().sendMessage("...and receive a rare " + Item.getItemName(drop2) + "!");

						break;
					case TRAP:
						p.getPlayer().sendMessage("...and find a trap!");
						if(Misc.random(3) == 0) {
							p.applyPoison();
							p.getPlayer().gfx100(267);
						} else {
							p.getPlayer().appendDamage(p.getPlayer().playerLevel[p.getPlayer().playerHitpoints] / 4, Hitmark.HIT);
							p.getPlayer().gfx100(287);
						}
						break;	
				}
				destroy();
				container.stop();
			}
			
		}, 1);
	}
	
	final static int[] SUPERITEMS = {6570,13237,13235,11802,6585,12006,12809,11905,12904};
	
	final static int[][][] ITEMS = { // lev 0 10 20 30 40 50 60 70 80 90
			{{1321,1291,1323},{1313,1297,1341},{1299,1209,1285},{1271,3100,1301},{6897,1213,3101,8850,11037},{4153,1333,1333,3101,11037,11037},
				{4153,1305,1305,1305,5698,7158,11838},{4151,1305,4153,5698,4587,11838},{4151,4151,4153,5698,11804},{4151,11802,4151}}, //attack
			
			{{1075,1067, 1117, 1155, 1153},{2593,2595,2597},{4127,12289,12287,12285,1198},
					{7462,10551,8849,3475,1146,1124,6894},{7462,7462,10551,10551,8850},{7462,10551,12954,8850,8850,6524},
					{12954,12954,8850,6524,6524},{12954,11283,12484,11832,11834,6524},
					{11283,11283,12484,11832,11834},{12484,11832,11834,11283}}, //defence
			
			{{1321,1337,1323,1323},{1426,1426,1327},{1329,1329},{6737,1331},{6737,6737,1333},
				{6737,6737,6737,3204,6528},{6737,6737,4722,4720,4718,4716,4587,4587,11838,3204,3204,6528},{6737,4722,4720,4718,4716,11838,11838,11808,3204,3204},
				{4722,4720,4718,4716,11838,11808,11808},{4722,4720,4718,4716,11808,11808,11808}}, //strength
			
			{{385,6568,6568,6568},{385,6568,6568},{385,6568},{385,4131},{385,4131,4131},{385,6570,4131,11840},
				{385,6570,6570,11840,11840},
				{385,4730,4728,4726,4724,4730,4728,4726,4724,6570,6570,6570,11840},
				{385,4730,4728,4726,4724,4730,4728,4726,4724,13239,6570,6570},{385,4730,4728,4726,4724,13239,13239,6570}}, //hp
			
			{{861,1095,},{869,869},{866,808},{867,809},{3749,868,868,868,3749,3749,6733,1099,1099},{3749,3749,6733,6733,9185},
				{3749,6733,6733,6733,2497,2495,2495,9185,9185},
				{6733,6733,4738,4738,4736,4736,4736,4732,4732,12512,12510,12508,12476,12474,12472,12470,2577,2497,9185},
				{4738,4738,4736,4736,4736,4732,4732,12512,12510,12508,12476,12474,12472,12470,2577},
				{12512,12510,12508,12476,12474,12472,12470,2577,9185}}, //range
			
			{{526,2434},{526,2434,3842},{532,3842,3842},{532,2434,3842,3842},{532,4759,4757,4755,4753},{536,2434,4759,4757,4755,4753},
				{536,4759,4757,4755,4753},{536,2434,4759,4757,4755,4753},{536,4759,4757,4755,4753},
				{536,2434,2434,2434,4759,4757,4755,4753,4759,4757,4755,4753,3842,3842}}, //prayer
			
			{{556,557,554,555,558},{556,557,554,555,558},{556,557,554,555,562},
				{556,557,554,555,565},{556,557,554,555,565,560,560,560,6920,6920,6918,6918,6916,4117,4117,4115,4115,4113,4113,4111,4111,4109},
				{556,557,554,555,560,6920,6920,6920,6918,6918,6916,4675,4117,4117,4115,4115,4113,4113,4111,4111,4109,4109},
				{556,557,554,555,565,565,565,565,560,560,560,9075,9075,4714,4714,4714,4714,4714,4712,4712,4710,4710,6924,6924,6924,6916,4675,4675},
				{556,557,554,555,565,565,565,565,9075,9075,9075,4714,4714,4714,4714,4714,4712,4712,4712,4710,4710,4710,6924,6924,6920,6914,4675,4675},
				{556,557,554,555,565,565,565,565,565,9075,9075,4714,4714,4714,4714,4712,4712,4712,4710,4710,4710,6924,6924,6924,6920,6914,6914,4675,4675,11905,12904},
				{556,557,565,565,560,560,9075,9075,4714,4714,4714,4712,4712,4712,4710,4710,4710,6924,6924,6920,6914,6914,6914,6914,4675,4675,11905,12904}}, //magic
	};
	
	public static HungerCrate createNewCrate() {
	
		return null;
	}
	
	GlobalObject obj;
	Quality q;
	int x, y;
	
	enum Quality {
		EXP,
		FOOD,
		BLOOD,
		ITEM,
		TRAP,
		SUPERITEM;
	}
	
}
