package com.osps.client;

import com.osps.client.cache.graphics.RSInterface;
import static com.osps.client.cache.graphics.RSInterface.interfaceCache;
import com.osps.client.cache.graphics.TextDrawingArea;

public class Interfaces extends RSInterface {

	public static void loadInterfaces() {
		prayerTab(defaultTextDrawingAreas);
		runepouch(defaultTextDrawingAreas);
		barrowText(defaultTextDrawingAreas);
		Pestpanel(defaultTextDrawingAreas);
		Pestpanel2(defaultTextDrawingAreas);
		addPestControlRewardWidget(defaultTextDrawingAreas);
		pinComponent(defaultTextDrawingAreas);
		checkPinComponent(defaultTextDrawingAreas);
		resetPinComponent(defaultTextDrawingAreas);
		pokerComponent(defaultTextDrawingAreas);
		helpComponent(defaultTextDrawingAreas);
		helpDatabaseComponent(defaultTextDrawingAreas);
		spawnInterface(defaultTextDrawingAreas);
		addAntibotWidget(defaultTextDrawingAreas);
		timerInterface(defaultTextDrawingAreas);
		titleInterface(defaultTextDrawingAreas);
                playerShops(defaultTextDrawingAreas);
                viewPlayerShop(defaultTextDrawingAreas);
                sellPlayerShop(defaultTextDrawingAreas);
                spawnTab(defaultTextDrawingAreas);
                teleTab(defaultTextDrawingAreas);
		//guthixAchievements(defaultTextDrawingAreas);
	}
        
        
        
        public static void teleTab(TextDrawingArea[] tda) {
            RSInterface i = addInterface(46600);
		String dir = "/TeleTab/SPRITE";
		addSprite(46601, 0, dir);
		addText(46602, "Teleports", tda, 2, 0xFFA500, true, true);
                addSprite(46603, 1, dir);
                addText(46604, "", tda, 3, 0xFFA500, true, true);
                addHoverButton(46605, dir, 2, 16, 16, "Next List", 0, 46606, 1);
		addHoveredButton(46606, dir, 3, 16, 16, 46607);
                addHoverButton(46608, dir, 4, 16, 16, "Previous List", 0, 46609, 1);
		addHoveredButton(46609, dir, 5, 16, 16, 46610);
		i.totalChildren(9);
		i.child(0, 46601, 12, 77);
		i.child(1, 46602, 95, 5);
                i.child(2, 46603, 12, 25);
                i.child(3, 46604, 95, 33);
                i.child(4, 46611, 14, 79);
                i.child(5, 46605, 153, 33);
                i.child(6, 46606, 153, 33);
                i.child(7, 46608, 22, 33);
                i.child(8, 46609, 22, 33);
                
                
                
                RSInterface t = addInterface(46611);
                t.totalChildren(20);
                int y = 5;
                for (int f = 0; f < 20; f++) {
                    addClickableText(46612 + f, "", "Teleport", tda, 1, 0xff7000, false, true, 145);
                    t.child(f, 46612+f, 3, y);
                    y += 17;
                }
                t.width = 147;
		t.height = 157;
		t.scrollMax = y;
        }
        
        
        
        
        public static void spawnTab(TextDrawingArea[] tda) {
		RSInterface i = addInterface(47200);
		String dir = "/SpawnTab/SPRITE";
		addSprite(47201, 0, dir);
		addText(47202, "Item Spawn Tab", tda, 2, 0xFFA500, true, true);
		RSInterface t = addInterface(47203);
		addConfigButton(47206, 47200, 1, 2, dir, 14, 15, "Toggle", 1, 4, 930);
		//addConfigButton(47207, 47200, 1, 2, dir, 14, 15, "Toggle", 1, 4, 931);
		addText(47208, "Note items", tda, 0, -1793722, false, true);
		//addText(47209, "Bank items", tda, 0, -1793722, false, true);
		i.totalChildren(7);
		i.child(0, 47201, 12, 77);
		i.child(1, 47202, 95, 5);
		i.child(2, 47203, 14, 79);
		i.child(3, 47204, 12, 22);
		i.child(4, 47205, 12, 49);
		i.child(5, 47206, 12, 242);
		//i.child(6, 47207, 100, 242);
		i.child(6, 47208, 28, 244);
		//i.child(8, 47209, 116, 244);
		
		itemGroup(47611, 1, 100, 0, 3);
		t.totalChildren(301);
		int add = 0;
		for (int j = 0; j < 100; j++) {
			addHoverButton(47210+j, dir, 3, 163, 33, "Spawn Item", 0, 47310+j, 1);
			addHoveredButton(47310+j, dir, 4, 163, 33, 47410+j);
			addText(47510+j, "text", tda, 0, 0xffffff, false, true);
			t.child(add, 47210+j, 0, 2 + (j*35));
			add++;
			t.child(add, 47310+j, 0, 2 + (j*35));
			add++;
			t.child(add, 47510+j, 34, 13 + (j*35));
			add++;
		}
		t.child(300, 47611, 1, 1);
		t.width = 147;
		t.height = 0;
		t.scrollMax = 0;
		
		
		addTextInput(47204, 167, 20, 1, "Search for an item", i, new InterfaceTextInput() {
			@Override
			public void handleInput() {
				int results = 0;
				t.scrollMax = 0;
				String s = interfaceCache[47204].inputText;
				if (!s.equals("")) {
					for (int l = 0; l < SearchItems.list.length; l++) {
						SearchItems def = SearchItems.list[l];
						if (def != null && def.name != null && def.lname.contains(s.toLowerCase())) {
							interfaceCache[47510 + results].message = def.name;
							interfaceCache[47611].inv[results] = def.id + 1;
							interfaceCache[47611].invStackSizes[results] = 1;
							results++;
							if (results == 100)
								break;
						}
					}
					t.scrollMax = 2 + (results * 35);
					if (t.scrollMax < 157) {
						t.height = t.scrollMax;
						t.width = 163;
					} else {
						t.height = 157;
						t.width = 147;
					}
				} else {
					t.height = 0;
					t.scrollMax = 0;
				}
			}
		});
		
		addTextInput(47205, 167, 20, 1, "Amount to spawn", i, new InterfaceTextInput() {
			@Override
			public void handleInput() {
			}
		});
	}
        
        
        
        
        
        
        
        
        
        
        public static void sellPlayerShop(TextDrawingArea[] tda) {
            RSInterface tab = addInterface(44850);
            String dir = "/PlayerShops/SPRITE";
            addSprite(44851, 6, dir);
            addText(44852, "Add an item to your shop", tda, 2, 0xFFA500, true, true);
            itemGroup(44853, 1, 1, 0, 0);
            addText(44854, "Select an item from your inventory", tda, 1, 0xFFA500, true, true);
            addTextInput(44855, 115, 19, 0, "Enter Amount", tab, new InterfaceTextInput() {
			@Override
			public void handleInput() {
                            Client.instance.sendShopPacket(0, interfaceCache[44855].inputText);
			}
		});
            addTextInput(44856, 115, 19, 0, "Enter Price", tab, new InterfaceTextInput() {
			@Override
			public void handleInput() {
                            Client.instance.sendShopPacket(1, interfaceCache[44856].inputText);
			}
		});
            addHoverButton(44857, dir, 7, 80, 31, "Add item to shop", 0, 44858, 1);
            addHoveredButton(44858, dir, 8, 80, 31, 44859);
            addText(44860, "Confirm", tda, 1, 0xFFA500, true, true);
            int x = 50, y = 40;
            tab.totalChildren(11);
            tab.child(0, 44851, x, y);
            tab.child(1, 44002, 345+x, 4+y);
            tab.child(2, 44003, 345+x, 4+y);
            tab.child(3, 44852, 184+x, 5+y);
            tab.child(4, 44853, 40+x, 74+y);
            tab.child(5, 44854, 184+x, 43+y);
            tab.child(6, 44855, 97+x, 68+y);
            tab.child(7, 44856, 97+x, 91+y);
            tab.child(8, 44857, 269+x, 176+y);
            tab.child(9, 44858, 269+x, 176+y);
            tab.child(10, 44860, 309+x, 183+y);
        }
        
        public static void viewPlayerShop(TextDrawingArea[] tda) {
            RSInterface tab = addInterface(44800);
            String dir = "/PlayerShops/SPRITE";
            addSprite(44801, 5, dir);
            addText(44802, "Viewing Shop of: @gre@Nobody", tda, 2, 0xFFA500, true, true);
            addText(44840, "You currently have : 0 Pk Points", tda, 3, 0xFFA500, true, true);
            itemGroup(44803, 6, 5, 37, 10);
            interfaceCache[44803].actions = new String[] {"Value", "Buy 1", "Buy X", "Buy All", null};
            int x = 10, y = 10;
            tab.totalChildren(36);
            tab.child(0, 44801, x, y);
            tab.child(1, 44002, 463+x, 4+y);
            tab.child(2, 44003, 463+x, 4+y);
            tab.child(3, 44802, 243+x, 5+y);
            tab.child(4, 44803, 50+x, 25+y);
            tab.child(5, 44840, 243+x, 277+y);
            int xx = 16, yy = 38;
            for (int i = 0; i < 30; i++) {
                addText(44805 + i, " ", tda, 0, 0xffffff, true, true);
                tab.child(6 + i, 44805 + i, 50+x + xx, 20+y + yy);
                xx += 69;
                if (xx == 16 + ((69)*6)) {
                    yy += 42;
                    xx = 16;
                }
            }
        }
        
        public static void playerShops(TextDrawingArea[] tda) {
            RSInterface tab = addInterface(44000);
            String dir = "/PlayerShops/SPRITE";
            addSprite(44001, 0, dir);
            addHoverButton(44002, dir, 1, 16, 16, "Close", -1, 44003, 1);
            addHoveredButton(44003, dir, 2, 16, 16, 44004);
            addText(44005, "Player Shop Finder", tda, 2, 0xFFA500, true, true);
            addText(44006, "Featured Item   @lre@-   @whi@Player Name   @lre@-   @whi@Total Items   @lre@-   @whi@PkP Value", tda, 0, 0xffffff, false, false);
            addTextInput(44008, 115, 19, 0, "Search Players", tab, new InterfaceTextInput() {
			@Override
			public void handleInput() {
                            for (int i = 0; i < 100; i++) {
                                interfaceCache[44700 + i].message = interfaceCache[44700 + i].fake;
                            }
                            String search = interfaceCache[44008].inputText;
                            String results[] = new String[100];
                            int cur = 0;
                            for (int i = 0; i < 100; i++) {
                                if (interfaceCache[44700 + i].message.toLowerCase().startsWith(search)) {
                                    results[cur] = interfaceCache[44700 + i].message;
                                    cur++;
                                }
                            }
                            interfaceCache[44007].scrollMax = 3;
                            for (int i = 0; i < 100; i++) {
                                if (results[i] != null && results[i].length() > 0) {
                                    interfaceCache[44700+i].message = results[i];
                                    interfaceCache[44007].scrollMax += 13;
                                } else {
                                    interfaceCache[44700+i].message = "";
                                }
                            }
			}
		});
            int x = 10, y = 10;
            tab.totalChildren(8);
            tab.child(0, 44001, x, y);
            tab.child(1, 44002, 463+x, 4+y);
            tab.child(2, 44003, 463+x, 4+y);
            tab.child(3, 44005, 243+x, 5+y);
            tab.child(4, 44006, 150+x, 50+y);
            tab.child(5, 44020, 139+x, 66+y);
            tab.child(6, 44007, 14+x, 66+y);
            tab.child(7, 44008, 12+x, 46+y);
            RSInterface players = addInterface(44007);
            players.totalChildren(100);
            for (int i = 0; i < 100; i++) {
                addClickableText(44700 + i, "", "Open Shop", tda, 0, 0xffb000, false, true, 95);
                players.child(i, 44700 + i, 2, 3 + (i * 13));
            }
            players.width = 95;
            players.height = 211;
            players.scrollMax = 1310;
            RSInterface scroll = addInterface(44020);
            scroll.totalChildren(600);
            int yy = 0;
            for (int i = 0; i < 100; i++) {
                addHoverButton(44100 + i, dir, 3, 327, 32, "View shop", 0, 44200 + i, 1);
		addHoveredButton(44200 + i, dir, 4, 327, 32, 44099);
                itemGroup(44300 + i, 1, 1, 0, 0);
                addText(44400 + i, "", tda, 1, 0xff9040, true, true);
                addText(44500 + i, "", tda, 1, 0xff9040, true, true);
                addText(44600 + i, "", tda, 1, 0xff9040, true, true);
                scroll.child(i, 44100 + i, 0, 0 + yy);
                scroll.child(i + 100, 44200 + i, 0, 0 + yy);
                scroll.child(i + 200, 44300 + i, 23, 0 + yy);
                scroll.child(i + 300, 44400 + i, 133, 10 + yy);
                scroll.child(i + 400, 44500 + i, 213, 10 + yy);
                scroll.child(i + 500, 44600 + i, 280, 10 + yy);
                yy += 33;
            }
            scroll.width = 311;
            scroll.height = 211;
            scroll.scrollMax = yy;
        }
        
        public static void itemGroup(int id, int w, int h, int x, int y) {
		RSInterface rsi = addInterface(id);
		rsi.width = w;
		rsi.height = h;
		rsi.inv = new int[w * h];
		rsi.invStackSizes = new int[w * h];
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.invSpritePadX = x;
		rsi.invSpritePadY = y;
		rsi.spritesX = new int[20];
		rsi.spritesY = new int[20];
		rsi.sprites = new Sprite[20];
		rsi.type = 2;
	}
	
	
	public static void titleInterface(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(26000);
		String dir = "/Titles/SPRITE";
		addSprite(26001, 0, dir);
		addHoverButton(26002, dir, 1, 16, 16, "Close", 0, 26003, 1);
		addHoveredButton(26003, dir, 2, 16, 16, 26004);
		addText(26005, "Pkers.Ca Titles", tda, 3, 0xffb000, true, true);
		addText(26006, "DP: 0", tda, 3, 0xffb000, false, true);
		addText(26007, "PKP: 0", tda, 3, 0xffb000, false, true);
		addText(26008, "VP: 0", tda, 3, 0xffb000, false, true);
		addText(26009, "Example Text Here", tda, 2, 0xffffff, false, true);
		addText(26010, "Price Text Here", tda, 2, 0xffb000, true, true);
		addText(26011, "Select a Loyalty title!", tda, 2, 0xffb000, true, true);
		
		addHoverButton(26200, dir, 4, 72, 32, "Select", 0, 26201, 1);
		addHoveredButton(26201, dir, 5, 72, 32, 26202);
		addHoverButton(26203, dir, 4, 72, 32, "Select", 0, 26204, 1);
		addHoveredButton(26204, dir, 5, 72, 32, 26205);
		addText(26206, "Remove Title", tda, 0, 0xffb000, true, true);
		addText(26207, "BUY", tda, 2, 0xffb000, true, true);
		
		int x = 12, y = 40;
		tab.totalChildren(17);
		tab.child(0, 26001, x, y);
		tab.child(1, 26002, 463+x, 5+y);
		tab.child(2, 26003, 463+x, 5+y);
		tab.child(3, 26005, 244+x, 4+y);
		tab.child(4, 26006, 188+x, 32+y);
		tab.child(5, 26007, 288+x, 32+y);
		tab.child(6, 26008, 388+x, 32+y);
		tab.child(7, 26020, 7+x, 59+y);
		tab.child(8, 26009, 220+x, 81+y);
		tab.child(9, 26010, 346+x, 145+y);
		tab.child(10, 26011, 92+x, 37+y);
		tab.child(11, 26200, 240+x, 209+y);
		tab.child(12, 26201, 240+x, 209+y);
		tab.child(13, 26203, 376+x, 209+y);
		tab.child(14, 26204, 376+x, 209+y);
		tab.child(15, 26206, 276+x, 218+y);
		tab.child(16, 26207, 412+x, 216+y);
		
		
		
		RSInterface scroll = addInterface(26020);
		String[][] titles = Client.titles;
		addText(26012, "Pk Points", tda, 2, 0xff7000, true, true);
		addText(26013, "Donator Points", tda, 2, 0xff7000, true, true);
		addText(26014, "Vote Points", tda, 2, 0xff7000, true, true);
		addSprite(26015, 3, dir);
		int bb = (titles[0].length * 2) + (titles[1].length * 2) + (titles[2].length * 2);
		int b = (titles[0].length) + (titles[1].length) + (titles[2].length);
		scroll.totalChildren(6 + bb);
		scroll.child(0, 26012, 96, 3);
		scroll.child(1, 26015, 1, 20);
		scroll.child(2, 26013, 96, 33 + (titles[0].length * 20));
		scroll.child(3, 26015, 1, 50 + (titles[0].length * 20));
		scroll.child(4, 26014, 96, (27 + (titles[0].length * 20)) + (36 + (titles[1].length * 20)));
		scroll.child(5, 26015, 1, (27 + (titles[0].length * 20)) + (53 + (titles[1].length * 20)));
		int cur = 0;
		int yb = 29;
		for (int i = 0; i < titles.length; i++) {
			for (int j = 0; j < titles[i].length; j++) {
				addClickableText(26021+cur, titles[i][j], "Select", tda, 2, 0xffffff, false, true, 140);
				addClickableText(26121+cur, "BUY", "Select", tda, 2, 0xffb000, false, true, 20);
				scroll.child(6+cur, 26021+cur, 2, (cur * 20) + yb);
				scroll.child(6+b+cur, 26121+cur, 160, (cur * 20) + yb);
				cur++;
			}
			yb += 29;
		}
		
		scroll.width = 192;
		scroll.height = 188;
		scroll.scrollMax = (b * 20) + 90;
	}
	
	
	
	public static void timerInterface(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(33400);
		addText(33401, "Players: ", tda, 0, 0x333399, true, true);
		addText(33402, "Time remaining: ", tda, 0, 0x333399, true, true);

	}

	public static void addAntibotWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(33300);
		addSprite(33301, 0, "Interfaces/AntiBot/IMAGE");
		addText(33302, "Click the 'Abyssal Whip'", tda, 2, 0xFF981F, true, true);
		addText(33303, "1:00", tda, 0, 0xFF981F, true, true);
		addText(33304,
				"If you click the wrong item or the time depletes to 0,\\nYou will be teleported to a new location.",
				tda, 0, 0xFF981F, true, true);
		setChildren(7, widget);
		setBounds(33301, 115, 96, 0, widget);
		setBounds(33302, 253, 105, 1, widget);
		setBounds(33303, 375, 105, 2, widget);
		setBounds(33304, 255, 190, 3, widget);
		setBounds(33310, 180, 140, 4, widget);
		setBounds(33313, 240, 140, 5, widget);
		setBounds(33316, 300, 140, 6, widget);

		for (int i = 0; i < 9; i += 3) {
			RSInterface option = addInterface(33310 + i);
			addToItemGroup(33311 + i, 1, 1, 0, 0, false, "", "", "");
			interfaceCache[33311 + i].inv = new int[] { 4152 };
			interfaceCache[33311 + i].invStackSizes = new int[] { 1 };
			addButton(33312 + i, 1, "Interfaces/AntiBot/IMAGE", "Select");
			setChildren(2, option);
			setBounds(33311 + i, 0, 0, 0, option);
			setBounds(33312 + i, 0, 0, 1, option);
		}
	}

	public static void addTransparentSprite(int id, int spriteId, String spriteName, int opacity) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.aByte254 = (byte) 0;
		tab.mOverInterToTrigger = 52;
		tab.sprite1 = imageLoader(spriteId, spriteName);
		tab.sprite2 = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.opacity = (byte) opacity;
		tab.drawsTransparent = true;
	}
	
	/*public static void guthixAchievements(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(32200);
		RSInterface scroll = addInterface(32230);
		String dir = "Achievements/ach";
		addSprite(32201, 0, dir);
		addHoverButton(32202, dir, 1, 16, 16, "Close", 0, 32203, 1);
		addHoveredButton(32203, dir, 2, 16, 16, 32204);
		addText(32205, "Achievements", tda, 2, 0xFFA500, true, true);
		addText(32206, "Selected Achievement Title", tda, 2, 0xB58338, true, true);
		addText(32207, "Progress: 0/0", tda, 1, 65280, true, true);
		addText(32208, "Description:", tda, 2, 0xffb000, false, true);
		addText(32209, "Rewards:", tda, 2, 0xffb000, false, true);
		addHoverButton(32210, dir, 3, 45, 20, "Select", 0, 32212, 1);
		addHoveredButton(32212, dir, 4, 45, 20, 32220);
		addHoverButton(32213, dir, 3, 45, 20, "Select", 0, 32214, 1);
		addHoveredButton(32214, dir, 4, 45, 20, 32220);
		addHoverButton(32215, dir, 3, 45, 20, "Select", 0, 32216, 1);
		addHoveredButton(32216, dir, 4, 45, 20, 32220);
		addHoverButton(32217, dir, 3, 45, 20, "Select", 0, 32218, 1);
		addHoveredButton(32218, dir, 4, 45, 20, 32220);
		addText(32221, "Easy", tda, 0, 0xffffff, true, true);
		addText(32222, "Medium", tda, 0, 0xffffff, true, true);
		addText(32223, "Hard", tda, 0, 0xffffff, true, true);
		addText(32224, "Elite", tda, 0, 0xffffff, true, true);
		int x = 10, y = 10;
		tab.totalChildren(21);
		tab.child(0, 32201, x, y);
		tab.child(1, 32202, x+464, y+4);
		tab.child(2, 32203, x+464, y+4);
		tab.child(3, 32205, x+243, y+4);
		tab.child(4, 32206, x+314, y+29);
		tab.child(5, 32207, x+314, y+44);
		tab.child(6, 32230, x+7, y+69);
		tab.child(7, 32208, x+162, y+87);
		tab.child(8, 32209, x+162, y+166);
		tab.child(9, 32210, x+32, y+24);
		tab.child(10, 32212, x+32, y+24);
		tab.child(11, 32213, x+79, y+24);
		tab.child(12, 32214, x+79, y+24);
		tab.child(13, 32215, x+32, y+46);
		tab.child(14, 32216, x+32, y+46);
		tab.child(15, 32217, x+79, y+46);
		tab.child(16, 32218, x+79, y+46);
		tab.child(17, 32221, x+54, y+29);
		tab.child(18, 32222, x+101, y+29);
		tab.child(19, 32223, x+54, y+51);
		tab.child(20, 32224, x+101, y+51);
		scroll.totalChildren(50);
		scroll.scrollMax = 3;
		for (int i = 0; i < 50; i++) {
			addClickableText(32231 + i, "Achivement #"+i, "View", tda, 0, 0xffffff, false, true, 135);
			scroll.child(i, 32231+i, 3, 3 + (i * 14));
			scroll.scrollMax += 14;
		}
		scroll.width = 124;
		scroll.height = 230;
	}*/
	public static void helpComponent(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(59525);
		// addSprite(59526, 1, "Interfaces/HelpInterface/IMAGE");
		addTransparentSprite(59526, 1, "Interfaces/HelpInterface/IMAGE", 150);
		addInputField(59527, 200, 0xFF981F, "What do you need help with? (200 characters max)", 430, 28, false, false,
				"[A-Za-z0-9 .,]");
		addText(59528, "Request Help", tda, 2, 0xFF981F, true, true);
		addText(59529,
				"You are only allowed to request help when you need it. Situations such as being stuck,\\n"
						+ "threatened by another player, problems with a donation, or experiencing a bug are all\\n"
						+ "viable uses of the help system. Improper use of this system may lead to punishment.",
				tda, 0, 0xFF981F, false, true);
		addHoverButton(59530, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 59531, 3);
		addHoveredButton(59531, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 59532);
		setChildren(6, widget);
		setBounds(59526, 33, 106, 0, widget);
		setBounds(59527, 40, 192, 1, widget);
		setBounds(59528, 256, 113, 2, widget);
		setBounds(59529, 40, 135, 3, widget);
		setBounds(59530, 456, 112, 4, widget);
		setBounds(59531, 456, 112, 5, widget);
	}

	public static void pinComponent(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(60000);
		// addSprite(59526, 1, "Interfaces/HelpInterface/IMAGE");
		addTransparentSprite(60001, 1, "Interfaces/HelpInterface/IMAGE", 150);
		addInputField(60002, 6, 0xFF981F, "What do you want to set your pin?", 430, 28, false, false,
				"[0-9]");
		addText(60003, "Account Pin", tda, 2, 0xFF981F, true, true);
		addText(60004,
				" Please type in a sequence of numbers to be your secure account pin,\\n"
						+ " this pin will be required every time you log on from a different connection and\\n"
						+ " has to be between 4 and 6 digits.",
				tda, 0, 0xFF981F, false, true);
		addHoverButton(60005, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 60006, 3);
		addHoveredButton(60006, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 60007);
		setChildren(6, widget);
		setBounds(60001, 33, 106, 0, widget);
		setBounds(60002, 40, 192, 1, widget);
		setBounds(60003, 256, 113, 2, widget);
		setBounds(60004, 40, 135, 3, widget);
		setBounds(60005, 456, 112, 4, widget);
		setBounds(60006, 456, 112, 5, widget);
	}
	
	public static void checkPinComponent(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(64000);
		// addSprite(59526, 1, "Interfaces/HelpInterface/IMAGE");
		addTransparentSprite(64001, 1, "Interfaces/HelpInterface/IMAGE", 150);
		addInputField(64002, 6, 0xFF981F, "What is your current account pin?", 430, 28, false, false,
				"[0-9]");
		addText(64003, "Please Enter Your Current Pin", tda, 2, 0xFF981F, true, true);
		addText(64004,
				" Please type in your account pin to unlock your account.\\n"
						+ ""
						+ "",
				tda, 0, 0xFF981F, false, true);
		addHoverButton(64005, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 64006, 3);
		addHoveredButton(64006, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 64007);
		setChildren(6, widget);
		setBounds(64001, 33, 106, 0, widget);
		setBounds(64002, 40, 192, 1, widget);
		setBounds(64003, 256, 113, 2, widget);
		setBounds(64004, 40, 135, 3, widget);
		setBounds(64005, 456, 112, 4, widget);
		setBounds(64006, 456, 112, 5, widget);
	}
	
	public static void resetPinComponent(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(63000);
		// addSprite(59526, 1, "Interfaces/HelpInterface/IMAGE");
		addTransparentSprite(63001, 1, "Interfaces/HelpInterface/IMAGE", 150);
		addInputField(63002, 6, 0xFF981F, "What is your current account pin?", 430, 28, false, false,
				"[0-9]");
		addText(63003, "Please Enter Your Current Pin", tda, 2, 0xFF981F, true, true);
		addText(63004,
				" Please type in your account pin to remove and reset it.\\n"
						+ ""
						+ "",
				tda, 0, 0xFF981F, false, true);
		addHoverButton(63005, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 63006, 3);
		addHoveredButton(63006, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 63007);
		setChildren(6, widget);
		setBounds(63001, 33, 106, 0, widget);
		setBounds(63002, 40, 192, 1, widget);
		setBounds(63003, 256, 113, 2, widget);
		setBounds(63004, 40, 135, 3, widget);
		setBounds(63005, 456, 112, 4, widget);
		setBounds(63006, 456, 112, 5, widget);
	}
	
	public static void pokerComponent(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(62500);
		// addSprite(59526, 1, "Interfaces/HelpInterface/IMAGE");
		addTransparentSprite(62501, 1, "Interfaces/HelpInterface/IMAGE", 150);
		addInputField(62502, 15, 0xFF981F, "How much do you want to deposit?", 430, 28, false, false,
				"[0-9]");
		addText(62503, "Poker Deposit", tda, 2, 0xFF981F, true, true);
		addText(62504,
				" Please type in the amount of blood money you want to deposit,\\n"
						+ " your poker username must be the same as your ingame account name and\\n"
						+ " your password doesn't have to be the same.",
				tda, 0, 0xFF981F, false, true);
		addHoverButton(62505, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 62506, 3);
		addHoveredButton(62506, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 62507);
		setChildren(6, widget);
		setBounds(62501, 33, 106, 0, widget);
		setBounds(62502, 40, 192, 1, widget);
		setBounds(62503, 256, 113, 2, widget);
		setBounds(62504, 40, 135, 3, widget);
		setBounds(62505, 456, 112, 4, widget);
		setBounds(62506, 456, 112, 5, widget);
	}
	public static void helpDatabaseComponent(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(59550);
		// addSprite(59551, 8, "Interfaces/HelpInterface/IMAGE");
		addTransparentSprite(59551, 8, "Interfaces/HelpInterface/IMAGE", 150);
		addHoverButton(59552, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 59553, 3);
		addHoveredButton(59553, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 59554);
		addText(59555, "Help Database", tda, 2, 0xFF981F, true, true);
		addText(59556, "Username/Date", tda, 1, 0xFF981F, false, true);
		addText(59557, "Line2", tda, 1, 0xFF981F, true, true);
		addText(59558, "Line3", tda, 1, 0xFF981F, true, true);
		addText(59559, "Line4", tda, 1, 0xFF981F, true, true);
		addText(59560, "Line5", tda, 1, 0xFF981F, true, true);
		setChildren(10, widget);
		setBounds(59551, 0, 2, 0, widget);
		setBounds(59552, 375, 8, 1, widget);
		setBounds(59553, 375, 8, 2, widget);
		setBounds(59570, 120, 30, 3, widget);
		setBounds(59555, 256, 8, 4, widget);
		setBounds(59556, 20, 225, 5, widget);
		setBounds(59557, 256, 245, 6, widget);
		setBounds(59558, 256, 265, 7, widget);
		setBounds(59559, 256, 285, 8, widget);
		setBounds(59560, 256, 305, 9, widget);
		RSInterface scroll = addInterface(59570);
		scroll.scrollMax = 400;
		scroll.width = 255;
		scroll.height = 170;
		setChildren(20, scroll);

		int y = 0;
		int index = 0;
		for (int i = 0; i < 100; i += 5) {
			RSInterface subwidget = addInterface(59571 + i);
			addSprite(59572 + i, 11, "Interfaces/HelpInterface/IMAGE");
			addText(59573 + i, "Username / Date : Time", tda, 1, 0xFF981F, false, true);
			addButton(59574 + i, 10, "Interfaces/HelpInterface/IMAGE", 13, 10, "View Request", 1);
			addButton(59575 + i, 9, "Interfaces/HelpInterface/IMAGE", 16, 15, "Remove Request", 1);
			setChildren(4, subwidget);
			y = index * 20;
			setBounds(59572 + i, 0, 0, 0, subwidget);
			setBounds(59573 + i, 4, 3, 1, subwidget);
			setBounds(59574 + i, 210, 5, 2, subwidget);
			setBounds(59575 + i, 230, 3, 3, subwidget);
			setBounds(59571 + i, 0, y, index, scroll);
			index++;
		}
	}

	public static void PrayerPotion(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(59550);
		addTransparentSprite(59551, 8, "Interfaces/HelpInterface/IMAGE", 150);
	}

	public static void spawnInterface(TextDrawingArea[] tda) {
		RSInterface rs = addTabInterface(28100);
		addSprite(28103, 0, "spawn/BAG");
		addTransparentSprite(28103, 0, "spawn/BAG", 150);
		addHoverButton(28101, "spawn/button", 0, 81, 32, "Food", 201, 28102, 5);
		addHoveredButton(28102, "spawn/button", 1, 81, 32, 2001);
		// addButton(28103, 0, "spawn/food", "Spawn food", 28104, 1, 32, 32);
		addTooltip(28104, "Spawn food");
		addText(28106, "Food", tda, 1, 0xE68A00, true, true);

		/* NEW ROW */
		addHoverButton(28137, "spawn/button", 0, 81, 32, "Potions", 201, 28138, 5);
		addHoveredButton(28138, "spawn/button", 1, 81, 32, 2001);
		// addButton(28103, 0, "spawn/food", "Spawn food", 28104, 1, 32, 32);
		addTooltip(28140, "Buy potions");
		addText(28139, "Potions", tda, 1, 0xE68A00, true, true);

		addHoverButton(28141, "spawn/button", 0, 81, 32, "Veng Runes", 201, 28142, 5);
		addHoveredButton(28142, "spawn/button", 1, 81, 32, 2001);
		// addButton(28103, 0, "spawn/food", "Spawn food", 28104, 1, 32, 32);
		addTooltip(28144, "Buy Vengeance Runes");
		addText(28143, "Veng", tda, 1, 0xE68A00, true, true);

		addHoverButton(28145, "spawn/button", 0, 81, 32, "Pure Hybrid", 201, 28146, 5);
		addHoveredButton(28146, "spawn/button", 1, 81, 32, 2001);
		// addButton(28103, 0, "spawn/food", "Spawn food", 28104, 1, 32, 32);
		addTooltip(28147, "Buy Pure Hybrid");
		addText(28148, "Pure Brid", tda, 1, 0xE68A00, true, true);

		addHoverButton(28149, "spawn/button", 0, 81, 32, "Tank Brid", 201, 28150, 5);
		addHoveredButton(28150, "spawn/button", 1, 81, 32, 2001);
		// addButton(28103, 0, "spawn/food", "Spawn food", 28104, 1, 32, 32);
		addTooltip(28151, "Buy Main Brid");
		addText(28152, "Main Brid", tda, 1, 0xE68A00, true, true);

		addHoverButton(28153, "spawn/button", 0, 81, 32, "Tank Brid Set", 201, 28154, 5);
		addHoveredButton(28154, "spawn/button", 1, 81, 32, 2001);
		// addButton(28103, 0, "spawn/food", "Spawn food", 28104, 1, 32, 32);
		addTooltip(28155, "Buy Tank Set");
		addText(28156, "Tank Brid", tda, 1, 0xE68A00, true, true);

		addHoverButton(28157, "spawn/button", 0, 81, 32, "Zerker Brid", 201, 28158, 5);
		addHoveredButton(28158, "spawn/button", 1, 81, 32, 2001);
		// addButton(28103, 0, "spawn/food", "Spawn food", 28104, 1, 32, 32);
		addTooltip(28159, "Buy Zerker Brid");
		addText(28160, "Zerker Brid", tda, 1, 0xE68A00, true, true);
		/* END OF NEW */

		addHoverButton(28108, "spawn/button", 0, 81, 32, "Barrage Runes", 201, 28109, 5);
		addHoveredButton(28109, "spawn/button", 1, 81, 32, 2001);
		addTooltip(28110, "Buy Barrage");
		addText(28112, "Barrage", tda, 1, 0xE68A00, true, true);

		addHoverButton(28114, "spawn/button", 0, 81, 32, "Pure", 201, 28115, 5);
		addHoveredButton(28115, "spawn/button", 1, 81, 32, 2001);
		addTooltip(28116, "Buy Pure");
		addText(28118, "Pure", tda, 1, 0xE68A00, true, true);

		addHoverButton(28120, "spawn/button", 0, 81, 32, "Main set", 201, 28121, 5);
		addHoveredButton(28121, "spawn/button", 1, 81, 32, 2001);
		addTooltip(28122, "Buy Main");
		addText(28124, "Main", tda, 1, 0xE68A00, true, true);

		addHoverButton(28126, "spawn/button", 0, 81, 32, "Tank set", 201, 28127, 5);
		addHoveredButton(28127, "spawn/button", 1, 81, 32, 2001);
		addTooltip(28128, "Buy Tank Set");
		addText(28130, "Tank", tda, 1, 0xE68A00, true, true);

		addHoverButton(28132, "spawn/button", 0, 81, 32, "Zerker set", 201, 28133, 5);
		addHoveredButton(28133, "spawn/button", 1, 81, 32, 2001);
		addTooltip(28134, "Buy Zerker");
		addText(28136, "Zerker", tda, 1, 0xE68A00, true, true);
		addText(28131, "Pkers.Ca Spawn", tda, 1, 0xE68A00, true, true);

		setChildren(49, rs);

		rs.child(0, 28103, 3, 1);
		rs.child(1, 28101, 10, 26);
		rs.child(2, 28102, 10, 26);
		rs.child(3, 28106, 49, 34);
		rs.child(4, 28104, 6, 16);

		rs.child(5, 28108, 10, 63);
		rs.child(6, 28109, 10, 63);
		rs.child(7, 28112, 49, 71);
		rs.child(8, 28110, 6, 56);

		rs.child(9, 28114, 10, 100);
		rs.child(10, 28115, 10, 100);
		rs.child(11, 28118, 49, 108);
		rs.child(12, 28116, 6, 96);

		rs.child(13, 28120, 10, 137);
		rs.child(14, 28121, 10, 137);
		rs.child(15, 28124, 50, 145);
		rs.child(16, 28122, 6, 136);

		rs.child(17, 28126, 10, 174);
		rs.child(18, 28127, 10, 174);
		rs.child(19, 28130, 50, 181);
		rs.child(20, 28128, 6, 176);

		rs.child(21, 28132, 10, 211);
		rs.child(22, 28133, 10, 211);
		rs.child(23, 28136, 49, 211 + 7);
		rs.child(24, 28134, 49, 212);
		rs.child(25, 28131, 98, 3);

		/* NEW ROW */
		rs.child(26, 28137, 102, 26);
		rs.child(27, 28138, 102, 26);
		rs.child(28, 28139, 143, 34);
		rs.child(29, 28140, 6, 16);

		rs.child(30, 28141, 102, 63);
		rs.child(31, 28142, 102, 63);
		rs.child(32, 28143, 143, 71);
		rs.child(33, 28144, 6, 56);

		rs.child(34, 28145, 102, 100);
		rs.child(35, 28146, 102, 100);
		rs.child(36, 28148, 143, 108);
		rs.child(37, 28147, 6, 96);

		rs.child(38, 28149, 102, 137);
		rs.child(39, 28150, 102, 137);
		rs.child(40, 28152, 144, 145);
		rs.child(41, 28151, 6, 136);

		rs.child(41, 28153, 102, 174);
		rs.child(42, 28154, 102, 174);
		rs.child(43, 28156, 144, 181);
		rs.child(44, 28155, 6, 176);

		rs.child(45, 28157, 102, 211);
		rs.child(46, 28158, 102, 211);
		rs.child(47, 28160, 143, 211 + 7);
		rs.child(48, 28159, 49, 212);

	}

	public static void Pestpanel(TextDrawingArea[] tda) {
		RSInterface RSinterface = addTab(21119);
		addText(21120, "Next Departure:", 0xCCCBCB, false, true, 52, tda, 1);
		addText(21121, "Players Ready:", 0x5BD230, false, true, 52, tda, 1);
		addText(21122, "(Need 5 to 25 players)", 0xDED36A, false, true, 52, tda, 1);
		addText(21123, "Pest Points:", 0x99FFFF, false, true, 52, tda, 1);
		int last = 4;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21120, 15, 12, 0, RSinterface);
		setBounds(21121, 15, 30, 1, RSinterface);
		setBounds(21122, 15, 48, 2, RSinterface);
		setBounds(21123, 15, 66, 3, RSinterface);

		RSInterface rsi = interfaceCache[6114] = new RSInterface();
		rsi.type = 4;
		rsi.width = 390;
		rsi.centerText = true;
	}

	public static void Pestpanel2(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21100);
		addSprite(21101, 0, "Interfaces/Pest Control/PEST1");
		addSprite(21102, 1, "Interfaces/Pest Control/PEST1");
		addSprite(21103, 2, "Interfaces/Pest Control/PEST1");
		addSprite(21104, 3, "Interfaces/Pest Control/PEST1");
		addSprite(21105, 4, "Interfaces/Pest Control/PEST1");
		addSprite(21106, 5, "Interfaces/Pest Control/PEST1");
		addText(21107, "", 0xCC00CC, false, true, 52, tda, 1);
		addText(21108, "", 0x0000FF, false, true, 52, tda, 1);
		addText(21109, "", 0xFFFF44, false, true, 52, tda, 1);
		addText(21110, "", 0xCC0000, false, true, 52, tda, 1);
		addText(21111, "", 0x99FF33, false, true, 52, tda, 1);// w purp
		addText(21112, "", 0x99FF33, false, true, 52, tda, 1);// e blue
		addText(21113, "", 0x99FF33, false, true, 52, tda, 1);// se yel
		addText(21114, "", 0x99FF33, false, true, 52, tda, 1);// sw red
		addText(21115, "200", 0x99FF33, false, true, 52, tda, 1);// attacks
		addText(21116, "", 0x99FF33, false, true, 52, tda, 1);// knights hp
		addText(21117, "Time Remaining:", 0xFFFFFF, false, true, 52, tda, 0);
		addText(21118, "", 0xFFFFFF, false, true, 52, tda, 0);
		int last = 18;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21101, 361, 26, 0, RSinterface);
		setBounds(21102, 396, 26, 1, RSinterface);
		setBounds(21103, 436, 26, 2, RSinterface);
		setBounds(21104, 474, 26, 3, RSinterface);
		setBounds(21105, 3, 21, 4, RSinterface);
		setBounds(21106, 3, 50, 5, RSinterface);
		setBounds(21107, 371, 60, 6, RSinterface);
		setBounds(21108, 409, 60, 7, RSinterface);
		setBounds(21109, 443, 60, 8, RSinterface);
		setBounds(21110, 479, 60, 9, RSinterface);
		setBounds(21111, 362, 10, 10, RSinterface);
		setBounds(21112, 398, 10, 11, RSinterface);
		setBounds(21113, 436, 10, 12, RSinterface);
		setBounds(21114, 475, 10, 13, RSinterface);
		setBounds(21115, 32, 32, 14, RSinterface);
		setBounds(21116, 32, 62, 15, RSinterface);
		setBounds(21117, 8, 88, 16, RSinterface);
		setBounds(21118, 87, 88, 17, RSinterface);
	}

	public static void barrowText(TextDrawingArea[] tda) {
		RSInterface tab = addScreenInterface(16128);
		addText(16129, "Barrows Brothers", tda, 2, 0xff981f, true, true);
		addText(16130, "Dharoks", tda, 1, 0x86B404, true, true);
		addText(16131, "Veracs", tda, 1, 0x86B404, true, true);
		addText(16132, "Ahrims", tda, 1, 0x86B404, true, true);
		addText(16133, "Torags", tda, 1, 0x86B404, true, true);
		addText(16134, "Guthans", tda, 1, 0x86B404, true, true);
		addText(16135, "Karils", tda, 1, 0x86B404, true, true);
		addText(16136, "Killcount:", tda, 2, 0xff981f, true, true);
		addText(16137, "#", tda, 1, 0x86B404, true, true);
		tab.totalChildren(9);
		tab.child(0, 16129, 452, 220);
		tab.child(1, 16130, 460, 240);
		tab.child(2, 16131, 460, 255);
		tab.child(3, 16132, 460, 270);
		tab.child(4, 16133, 460, 285);
		tab.child(5, 16134, 460, 300);
		tab.child(6, 16135, 460, 315);
		tab.child(7, 16136, 30, 318);
		tab.child(8, 16137, 68, 318);
	}

	public static void runepouch(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(41700);
		addSprite(41701, 0, "runepouch/sprite");
		addSprite(41705, 0, "runepouch/rune");
		addSprite(41706, 1, "runepouch/rune");
		addText(41702, "Rune pouch", tda, 2, 0xFFA500, true, true);
		addText(41703, "Pouch", tda, 2, 0xFFA500, true, true);
		addText(41704, "Inventory", tda, 2, 0xFFA500, true, true);
		addHoverButton(41707, "runepouch/close", 0, 21, 21, "Close window", 0, 41708, 1);
		addHoveredButton(41708, "runepouch/close", 1, 21, 21, 41709);
		RSInterface add = addInterface(41710);
		addToItemGroup(add, 3, 1, 26, 1, false, null, null, null);
		add = addInterface(41711);
		addToItemGroup(add, 7, 4, 16, 4, false, null, null, null);
		tab.totalChildren(10);
		tab.child(0, 41701, 0, 0);
		tab.child(1, 41702, 253, 29);
		tab.child(2, 41703, 253, 62);
		tab.child(3, 41704, 253, 137);
		tab.child(4, 41705, 105, 57);
		tab.child(5, 41706, 342, 57);
		tab.child(6, 41707, 406, 26);
		tab.child(7, 41708, 406, 26);
		tab.child(8, 41710, 186, 86);
		tab.child(9, 41711, 98, 154);
	}

	public static void prayerTab(TextDrawingArea[] tda) {
        RSInterface tab = addTabInterface(5608);
        RSInterface currentPray = interfaceCache[687];
        addSprite(5651, 0, "Prayer/PRAYER");
        currentPray.textColor = 0xFF981F;
        currentPray.textShadow = true;
        currentPray.message = "%1/%2";
 
        int[] ID1 = { 18016, 18017, 18018, 18019, 18020, 18021, 18022, 18023, 18024, 18025, 18026, 18027, 18028, 18029,
                18030, 18031, 18032, 18033, 18034, 18035, 18036, 18037, 18038, 18039, 18040, 18041, 18042, 18043, 18044};
        int[] X = { 8, 44, 80, 114, 150, 8, 44, 80, 116, 152, 8, 42, 78, 116, 152, 8, 44, 80, 116, 150, 6, 44, 80, 116,
                150, 6, 44, 80, 114};
        int[] Y = { 6, 6, 6, 4, 4, 42, 42, 42, 42, 42, 79, 76, 76, 78, 78, 114, 114, 114, 114, 112, 148, 150, 150, 150,
                148, 184, 184, 184, 184};
 
        int[] hoverIDs = { 18050, 18052, 18054, 18056, 18058, 18060, 18062, 18064, 18066, 18068, 18070, 18072, 18074,
                18076, 18078, 18080, 18082, 18084, 18086, 18088, 18090, 18092, 18094, 18096, 18098, 18100, 18102, 18104, 18106};
        int[] hoverX = { 12, 8, 20, 12, 24, 2, 2, 6, 6, 50, 6, 6, 10, 6, 6, 5, 5, 5, 5, 5, 18, 28, 28, 50, 1, 1, 12, 16, 12};
        int[] hoverY = { 42, 42, 42, 42, 42, 80, 80, 80, 80, 80, 118, 118, 118, 118, 118, 150, 150, 150, 150, 150, 105,
                80, 65, 65, 80, 110, 110, 105, 115};
        String[] hoverStrings = { "Level 01\nThick Skin\nIncreases your Defence by 5%",
                "Level 04\nBurst of Strength\nIncreases your Strength by 5%",
                "Level 07\nCharity of Thought\nIncreases your Attack by 5%",
                "Level 08\nSharp Eye\nIncreases your Ranged by 5%", "Level 09\nMystic Will\nIncreases your Magic by 5%",
                "Level 10\nRock Skin\nIncreases your Defence by 10%",
                "Level 13\nSuperhuman Strength\nIncreases your Strength by 10%",
                "Level 16\nImproved Reflexes\nIncreases your Attack by 10%",
                "Level 19\nRapid Restore\n2x restore rate for all stats\nexcept Hitpoints and Prayer",
                "Level 22\nRapid Heal\n2x restore rate for the\nHitpoints stat",
                "Level 25\nProtect Item\nKeep one extra item if you die",
                "Level 26\nHawk Eye\nIncreases your Ranged by 10%",
                "Level 27\nMystic Lore\nIncreases your Magic by 10%",
                "Level 28\nSteel Skin\nIncreases your Defence by 15%",
                "Level 31\nUltimate Strength\nIncreases your Strength by 15%",
                "Level 34\nIncredible Reflexes\nIncreases your Attack by 15%",
                "Level 37\nProtect from Magic\nProtection from magical attacks",
                "Level 40\nProtect from Missiles\nProtection from ranged attacks",
                "Level 43\nProtect from Melee\nProtection from close attacks",
                "Level 44\nEagle Eye\nIncreases your Ranged by 15%",
                "Level 45\nMystic Might\nIncreases your Magic by 15%",
                "Level 46\nRetribution\nInflicts damage to nearby\ntargets if you die",
                "Level 49\nRedemption\nHeals you when damaged\nand Hitpoints falls\nbelow 10%",
                "Level 52\nSmite\n1/4 of damage dealt is\nalso removed from\nopponents Prayer",
                "Level 55\nPreserve\nBoosted stats last 20% longer",
                "Level 60\nChivalry\nIncreases your Defence by 20%,\nStrength by 18% and Attack by\n15%",
                "Level 70\nPiety\nIncreases your Defence by 25%,\nStrength by 23% and Attack by\n20%",
                "Level 74\nRigour\nIncreases your Defence by 25%,\nRanged Strength by 23%\nand Ranged by 20%",
                "Level 77\nAugury\nIncreases your Defence by 25%,\nand Magic by 25%"};
       
        int ID2[] = { 5609, 5610, 5611, 5612, 5613, 5614, 5615, 5616, 5617, 5618, 5619, 5620, 5621, 5622, 5623, 683,
                684, 685, 5632, 5633, 5634, 5635, 5636, 5637, 5638, 5639, 5640, 5641, 5642, 5643, 5644, 686, 5645, 5649,
                5647, 5648, 18000, 18001, 18002, 18003, 18004, 18005, 18006, 18007, 18008, 18009, 18010, 18011, 18012,
                18013, 18014, 18015, 18120, 18121, 18122, 18123, 18124, 18125, 18126};
       
        int X2[] = { 6, 42,
                78, 6,
                42, 78,
                114, 150,
                6, 114,
                150, 6,
                42, 78,
                114, 42,
                78, 114,
                8, 44,
                80, 8,
                44, 80,
                116, 152,
                8, 116,
                152, 8,
                44, 80,
                116, 44,
                80, 116,
                114, 117,
                150, 153,
                42, 45,
                78, 81,
                150, 153,
                6, 9,
                150, 153,
                6, 13,
                42, 44,
                78, 81,
                114, 116};
       
        int Y2[] = { 4, 4,
                4, 40,
                40, 40,
                40, 40,
                76, 76,
                76, 112,
                112, 112,
                112, 148,
                148, 148,
                6, 6,
                6, 42,
                42, 42,
                42, 42,
                79, 78,
                78, 114,
                114, 114,
                114, 150,
                150, 150,
                4, 8,
                4, 7,
                76, 80,
                76, 79,
                112, 116,
                148, 151,
                148, 150,
                184, 187,
                184, 195,
                184, 187,
                184, 187};
 
        String[] oldPrayerNames = { "Thick Skin", "Burst of Strength", "Charity of Thought", "Rock Skin",
                "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal", "Protect Item", "Steel Skin",
                "Ultimate Strength", "Incredible Reflexes", "Protect from Magic", "Protect from Missiles",
                "Protect from Melee", "Retribution", "Redemption", "Smite" };
        String[] newPrayerNames = {"Sharp Eye", "Mystic Will", "Hawk Eye", "Mystic Lore", "Eagle Eye", "Mystic Might", "Preserve", "Chivalry", "Piety", "Rigour", "Augury"};
        addPrayer(18000, 0, 601, 7, 0, "Sharp Eye");
        addPrayer(18002, 0, 602, 8, 1, "Mystic Will");
        addPrayer(18004, 0, 603, 25, 2, "Hawk Eye");
        addPrayer(18006, 0, 604, 26, 3, "Mystic Lore");
        addPrayer(18008, 0, 605, 43, 4, "Eagle Eye");
        addPrayer(18010, 0, 606, 44, 5, "Mystic Might");
        addPrayer(18012, 0, 607, 54, 6, "Preserve");
        addPrayer(18014, 0, 608, 59, 7, "Chivalry");
        addPrayer(18120, 0, 609, 69, 8, "Piety");
        addPrayer(18122, 0, 610, 73, 9, "Rigour");
        addPrayer(18124, 0, 611, 76, 10, "Augury");
 
        for (int i = 0; i < 18; i++) {
            addOldPrayer(ID2[i], oldPrayerNames[i]);
        }
 
        for (int i = 0; i < 29; i++) {
            addPrayerHover(ID1[i], hoverIDs[i], i, hoverStrings[i]);
        }
       
        for (int i = 0; i < 8; i++) {
            addOldPrayer(18000+(2*i), newPrayerNames[i]);
        }
 
        for (int i = 0; i < 3; i++) {
            addOldPrayer(18120+(2*i), newPrayerNames[8+i]);
        }
 
        tab.totalChildren(116); // 58
        tab.child(52, 5651, 70, 242);
        for (int ii = 0; ii < 58; ii++) {
            tab.child(ii, ID2[ii], X2[ii], Y2[ii]);
        }
 
        int frame = 58;
        int frame2 = 0;
        for (int i : ID1) {
            tab.child(frame, i, X[frame2], Y[frame2]);
            frame++;
            frame2++;
        }
 
        int frame3 = 0;
        for (int i : hoverIDs) {
            tab.child(frame, i, hoverX[frame3], hoverY[frame3]);
            frame++;
            frame3++;
        }
    }

}
