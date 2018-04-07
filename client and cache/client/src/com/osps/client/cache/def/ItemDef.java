package com.osps.client.cache.def;
import java.io.PrintWriter;
import java.util.Arrays;

import com.osps.client.FileOperations;
import com.osps.client.Signlink;
import com.osps.client.Sprite;
import com.osps.client.Stream;
import com.osps.client.cache.StreamLoader;
import com.osps.client.cache.graphics.RSInterface;
import com.osps.client.draw.DrawingArea;
import com.osps.client.draw.Rasterizer;
import com.osps.client.entity.model.Model;
import com.osps.client.link.MRUNodes;

// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class ItemDef {

	public static void nullLoader() {
		mruNodes2 = null;
		mruNodes1 = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public static int getTexture(int id) {
		switch (id) {
		case 12773:
		case 12371:
			return 40;

		case 12774:
			return 24;
		}
		return -1;
	}
	public boolean method192(int j) {
		int k = anInt175;
		int l = anInt166;
		if (j == 1) {
			k = anInt197;
			l = anInt173;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.method463(k))
			flag = false;
		if (l != -1 && !Model.method463(l))
			flag = false;
		return flag;
	}

	@SuppressWarnings("unused")
	private static void writeOut() {
		try {
			PrintWriter writer = new PrintWriter("item_list.txt");
			for (int i = 0; i < totalItems; i++) {
				ItemDef item = ItemDef.forID(i);
				boolean noteable = item.certID != -1 && item.certTemplateID == -1;
				boolean noted = item.certID != -1 && item.certTemplateID != -1;
				writer.println(i + " " + noteable + " " + noted + " " + item.name);
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void displayItemDisplayVals(ItemDef def) {
		System.out.println("modelID: " + def.modelID);
		System.out.println("modelZoom: " + def.modelZoom);
		System.out.println("modelRotationY: " + def.modelRotationY);
		System.out.println("modelRotationX: " + def.modelRotationX);
		System.out.println("modelOffset1: " + def.modelOffset1);
		System.out.println("modelOffset2: " + def.modelOffset2);

	}

	public static void unpackConfig(StreamLoader archive) {
		stream = new Stream(FileOperations.ReadFile(Signlink.findcachedir() + "obj.dat"));
		Stream stream = new Stream(FileOperations.ReadFile(Signlink.findcachedir() + "obj.idx"));
		// stream = new Stream(archive.getDataForName("obj.dat"));
		// Stream stream = new Stream(archive.getDataForName("obj.idx"));
		totalItems = stream.readUnsignedWord() + 21;
		streamIndices = new int[totalItems + 50000];
		int i = 2;
		for (int j = 0; j < totalItems - 21; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}

		cache = new ItemDef[10];
		for (int k = 0; k < 10; k++)
			cache[k] = new ItemDef();
		//writeOut();
	}

	public Model method194(int j) {
		int k = anInt175;
		int l = anInt166;
		if (j == 1) {
			k = anInt197;
			l = anInt173;
		}
		if (k == -1)
			return null;
		Model model = Model.method462(k);
		if (l != -1) {
			Model model_1 = Model.method462(l);
			Model aclass30_sub2_sub4_sub6s[] = { model, model_1 };
			model = new Model(2, aclass30_sub2_sub4_sub6s);
		}
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < modifiedModelColors.length; i1++)
				model.method476(modifiedModelColors[i1], originalModelColors[i1], getTexture(id));

		}
		return model;
	}

	public static void applyTexture(Model model, int id) {
		switch (id) {
		case 12773:// Volcanic Whip
			model.setTexture(31);
			break;
		case 12774:// Frozen Whip
			model.setTexture(51);
			break;
		case 11992:// Lava Scale
			model.setTexture(47155, 31);
			break;
		case 12371:// Lava Mask
			model.setTexture(38119, 31);
			break;
		case 12769:// Frozen Mix
			model.setTexture(51);
			break;
		case 12771:// Volcanic Mix
			model.setTexture(31);
			break;
		}
	}

	public boolean method195(int j) {
		int k = anInt165;
		int l = anInt188;
		int i1 = anInt185;
		if (j == 1) {
			k = anInt200;
			l = anInt164;
			i1 = anInt162;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.method463(k))
			flag = false;
		if (l != -1 && !Model.method463(l))
			flag = false;
		if (i1 != -1 && !Model.method463(i1))
			flag = false;
		return flag;
	}

	public Model method196(int i) {
		int j = anInt165;
		int k = anInt188;
		int l = anInt185;
		if (i == 1) {
			j = anInt200;
			k = anInt164;
			l = anInt162;
		}
		if (j == -1)
			return null;
		Model model = Model.method462(j);
		if (k != -1)
			if (l != -1) {
				Model model_1 = Model.method462(k);
				Model model_3 = Model.method462(l);
				Model aclass30_sub2_sub4_sub6_1s[] = { model, model_1, model_3 };
				model = new Model(3, aclass30_sub2_sub4_sub6_1s);
			} else {
				Model model_2 = Model.method462(k);
				Model aclass30_sub2_sub4_sub6s[] = { model, model_2 };
				model = new Model(2, aclass30_sub2_sub4_sub6s);
			}
		if (i == 0 && maleEquipOffset != 0)
			model.method475(0, maleEquipOffset, 0);
		if (i == 1 && femaleEquipOffset != 0)
			model.method475(0, femaleEquipOffset, 0);
		applyTexture(model, id);
		if (modifiedModelColors != null) {
			for (int i1 = 0; i1 < modifiedModelColors.length; i1++)
				model.method476(modifiedModelColors[i1], originalModelColors[i1], getTexture(id));

		}
		return model;
	}

	private void setDefaults() {
		modelID = 0;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		modelZoom = 2000;
		modelRotationY = 0;
		modelRotationX = 0;
		anInt204 = 0;
		modelOffset1 = 0;
		modelOffset2 = 0;
		stackable = false;
		value = 1;
		membersObject = false;
		groundActions = null;
		itemActions = null;
		anInt165 = -1;
		anInt188 = -1;
		maleEquipOffset = 0;
		anInt200 = -1;
		anInt164 = -1;
		femaleEquipOffset = 0;
		anInt185 = -1;
		anInt162 = -1;
		anInt175 = -1;
		anInt166 = -1;
		anInt197 = -1;
		anInt173 = -1;
		stackIDs = null;
		stackAmounts = null;
		certID = -1;
		certTemplateID = -1;
		anInt167 = 128;
		anInt192 = 128;
		anInt191 = 128;
		brightness = 0;
		anInt184 = 0;
		team = 0;
	}

	public static int[] getResults(String name, int range, boolean limited) {
		int[] items = new int[range];
		int position = 0;
		if (limited) {
			RSInterface bank = RSInterface.interfaceCache[5382];
			for (int i = 0; i < bank.inv.length; i++) {
				if (bank.inv[i] <= 0) {
					continue;
				}
				ItemDef itemDef = forID(bank.inv[i] - 1);
				if (position >= range) {
					break;
				}
				if (itemDef == null) {
					continue;
				}
				if (itemDef.stackable) {
					if (itemDef.description != null) {
						if (itemDef.description.startsWith("Swap this note")) {
							continue;
						}
					}
				}
				String itemName = itemDef.name;
				if (itemName == null) {
					continue;
				}
				if (itemName.toLowerCase().contains(name.toLowerCase())) {
					if (Arrays.binarySearch(items, bank.inv[i] - 1) >= 0) {
						continue;
					}
					items[position] = bank.inv[i] - 1;
					position++;
				}
			}
		} else {
			for (int i = 0; i < 14485; i++) {
				ItemDef itemDef = forID(i);
				if (position >= range) {
					break;
				}
				if (itemDef == null) {
					continue;
				}
				if (itemDef.stackable) {
					if (itemDef.description != null) {
						if (itemDef.description.startsWith("Swap this note")) {
							continue;
						}
					}
				}
				String itemName = itemDef.name;
				if (itemName == null) {
					continue;
				}
				if (itemName.toLowerCase().contains(name.toLowerCase())) {
					if (Arrays.binarySearch(items, itemDef.id) >= 0) {
						continue;
					}
					items[position] = itemDef.id;
					position++;
				}
			}
		}
		return items;
	}

	public static void applyTexturing(Model model, int id) {
		switch (id) {
		case 11992:
			System.out.println("Lava Scale-------------------------");
			// model.setTexture(47155, 40);
			Model.printModelColours(model);
			System.out.println("Lava Scale-------------------------");
			break;
		case 12769:
			System.out.println("Frozen Mix-------------------------");
			model.setTexture(41);
			Model.printModelColours(model);
			System.out.println("Frozen Mix-------------------------");
			break;
		case 12771:
			System.out.println("Volcanic Mix-------------------------");
			model.setTexture(40);
			Model.printModelColours(model);
			System.out.println("Volcanic Mix-------------------------");
			break;
		case 12773:
			System.out.println("Volcanic whip-------------------------");
			model.setTexture(16, 40);
			Model.printModelColours(model);
			System.out.println("Volcanic whip-------------------------");
			break;
		case 12774:
			System.out.println("Frozen whip-------------------------");
			// model.setTexture(16, 42);
			model.setTexture(16, 44);
			Model.printModelColours(model);
			System.out.println("Frozen whip-------------------------");
			break;
		case 12371:
			System.out.println("Lava mask-------------------------");
			model.setTexture(38119, 40);
			Model.printModelColours(model);
			System.out.println("Lava mask-------------------------");
			break;
		}
	}

	public static ItemDef forID(int i) {
		for (int j = 0; j < 10; j++)
			if (cache[j].id == i && i != 2405) {
				return cache[j];
			}

		cacheIndex = (cacheIndex + 1) % 10;
		ItemDef itemDef = cache[cacheIndex];
		stream.currentOffset = streamIndices[i];
		itemDef.id = i;
		itemDef.setDefaults();
		itemDef.readValues(stream);
		/* Customs added here? */
		if (itemDef.certTemplateID != -1)
			itemDef.toNote();
		if (i == 2405) {
			itemDef = forID(1724);
			itemDef.id = 2405;
			itemDef.itemActions[4] = "Bury";
			itemDef.name = "Mark of Sacrifice";
			itemDef.description = "A necklace made to pay tribute to the great lord.";
			itemDef.originalModelColors = new int[1];
			itemDef.modifiedModelColors = new int[1];
			itemDef.originalModelColors[0] = 11200;
			itemDef.modifiedModelColors[0] = 926;
			itemDef.modelCustomColor = 926;
			itemDef.modelCustomColor2 = 926;
			itemDef.modelCustomColor3 = 926;

		}
		if (i == 2406) {
			itemDef.name = "Mark of Life";
			itemDef.description = "A necklace made to pay tribute to life.";

		}
		if (i == 12877 || i == 12873 || i == 12875 || i == 12879 || i == 12881 || i == 12883) {
			itemDef.itemActions = new String[5];
			itemDef.itemActions[2] = "Open";
			itemDef.description = "Stored set of Barrows.";
		}
		if (i == 13173) {
			itemDef.itemActions = new String[5];
			itemDef.itemActions[2] = "Open";
			itemDef.description = "Partyhat set";
		}
		if (i == 13175) {
			itemDef.itemActions = new String[5];
			itemDef.itemActions[2] = "Open";
			itemDef.description = "Halloween Mask Set";
		}
		if (i == 8901) {
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Wear";
			itemDef.itemActions[2] = "Assemble";
		}
		if (i == 1907) {
			itemDef.name = "Poisoned Drink";
		}
		if (i == 2839) {
			itemDef.name = "Slayer Helmet Recipe";
			itemDef.description = "Read this scroll to learn the slayer helmet recipe.";
			itemDef.modelID = 29261;
		}
		switch (i) {
		case 20770:
			itemDef.name = "Preserve prayer scroll";
			itemDef.description = "Learn the forgotten prayers.";
			break;
		case 20771:
			itemDef.name = "Rigour prayer scroll";
			itemDef.description = "Learn the forgotten prayers.";
			break;
		case 20772:
			itemDef.name = "Augury prayer scroll";
			itemDef.description = "Learn the forgotten prayers.";
			break;
		case 8125:
			itemDef.name = "Valuable Blood";
			itemDef.stackable = true;
			break;
		case 2690:
			itemDef.name = "$10 Scroll";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[0] = "Claim";
			break;
		case 2691:
			itemDef.name = "$50 Scroll";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[0] = "Claim";
			break;

		case 2692:
			itemDef.name = "$150 Scroll";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[0] = "Claim";
			break;


		case 15098:
			itemDef.name = "Dice (up to 100)";
			itemDef.modelZoom = 1104;
			itemDef.modelRotationY = 215;
			itemDef.modelRotationX = 94;
			itemDef.modelOffset1 = 9;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Public-roll";
			itemDef.itemActions[2] = "Switch-dice";
			itemDef.itemActions[4] = "Drop";
			itemDef.modelID = 47852;
			itemDef.brightness = 15;
			// itemDef.contrast = 25;
			break;
            
		case 15001:
			itemDef.name = "Divine spirit shield";
			itemDef.modelZoom = 1616;
			itemDef.modelRotationY = 396;
			itemDef.modelRotationX = 1120;
			//itemDef.modelOffset1 = 396;
			itemDef.modelOffset2 = 27;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.itemActions = new String[] { null, "Wield", null, null, "Drop" };
			itemDef.modelID = 11309;
			itemDef.anInt165 = 11047;
			itemDef.anInt200 = 11047;
			break;
			
		case 14484:
                
                itemDef.modelID = 44590;
                itemDef.anInt165 = 43660;//anInt165
                itemDef.anInt200 = 43660;//anInt200
                itemDef.modelZoom = 789;
                itemDef.groundActions = new String[] { null, null, "Take", null, null };
    			itemDef.itemActions = new String[] { null, "Wield", null, null, "Drop" };
                itemDef.modelRotationY = 240;
                itemDef.modelRotationX = 60;
                itemDef.modelOffset1 = -1;
                itemDef.modelOffset2 = -23;
                itemDef.name = "Dragon claws";
               // itemDef.description = "A set of fighting claws.".getBytes();
			break;
			
		case 13858:
			itemDef.name = "Zuriel's robe top";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Wear";
			itemDef.modelID = 42591;
			itemDef.modelZoom = 1373;
			itemDef.modelRotationY = 373;
			itemDef.modelRotationX = 0;
			itemDef.modelOffset2 = -7;
			itemDef.modelOffset1 = 0;
			itemDef.anInt204 = 0;
			itemDef.anInt165 = 42627;
			itemDef.anInt200 = 42642;
			itemDef.description = "Zuriel's robe top, a reward from PVP.";
			break;
		case 13861:
			itemDef.name = "Zuriel's robe bottom";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Wear";
			itemDef.modelID = 42588;
			itemDef.modelZoom = 1697;
			itemDef.modelRotationY = 512;
			itemDef.modelRotationX = 0;
			itemDef.modelOffset2 = -9;
			itemDef.modelOffset1 = 2;
			itemDef.anInt204 = 0;
			itemDef.anInt165 = 42634;
			itemDef.anInt200 = 42645;
			itemDef.description = "Zuriel's robe bottom, a reward from PVP.";
			break;
		case 13864:
			itemDef.name = "Zuriel's hood";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Wear";
			itemDef.modelID = 42604;
			itemDef.modelZoom = 720;
			itemDef.modelRotationY = 28;
			itemDef.modelRotationX = 0;
			itemDef.modelOffset2 = 1;
			itemDef.modelOffset1 = 1;
			itemDef.anInt204 = 0;
			itemDef.anInt165 = 42638;
			itemDef.anInt200 = 42653;
			itemDef.description = "Zuriel's hood, a reward from PVP.";
			break;
		case 13867:
			itemDef.name = "Zuriel's staff";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Equip";
			itemDef.modelID = 42595;
			itemDef.modelZoom = 2000;
			itemDef.modelRotationY = 366;
			itemDef.modelRotationX = 3;
			itemDef.modelOffset2 = 0;
			itemDef.modelOffset1 = 0;
			itemDef.anInt204 = 0;
			itemDef.anInt165 = 42617;
			itemDef.anInt200 = 42617;
			itemDef.description = "Zuriel's staff, a reward from PVP.";
			break;
		case 13893:
	          itemDef.name = "Vesta's plateskirt";
	          itemDef.itemActions = new String[5];
	          itemDef.itemActions[1] = "Wear";
	          itemDef.modelID = 42581;
	          itemDef.modelZoom = 1753;
	          itemDef.modelRotationY = 562;
	          itemDef.modelRotationX = 1;
	          itemDef.modelOffset2 = 11;
	          itemDef.modelOffset1 = -3;
	          itemDef.anInt204 = 0;
	          itemDef.anInt165 = 42633;
	          itemDef.anInt200 = 42649;
	          itemDef.description = "Vesta's plateskirt, a reward from PVP.";
	        break;
		    case 13899:
				itemDef.name = "Vesta's longsword";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Equip";
				itemDef.modelID = 42597;
				itemDef.modelZoom = 1744;
				itemDef.modelRotationY = 738;
				itemDef.modelRotationX = 1985;
				itemDef.modelOffset2 = 0;
				itemDef.modelOffset1 = 0;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42615;
				itemDef.anInt200 = 42615;
				itemDef.description = "Vesta's longsword, a reward from PVP.";
				break;
			case 13905:
				itemDef.name = "Vesta's spear";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Equip";
				itemDef.modelID = 42599;
				itemDef.modelZoom = 2022;
				itemDef.modelRotationY = 480;
				itemDef.modelRotationX = 15;
				itemDef.modelOffset2 = 5;
				itemDef.modelOffset1 = 0;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42614;
				itemDef.anInt200 = 42614;
				itemDef.description = "Vesta's spear, a reward from PVP.";
				break;
			case 13887:
				itemDef.name = "Vesta's chainbody";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wear";
				itemDef.modelID = 42593;
				itemDef.modelZoom = 1440;
				itemDef.modelRotationY = 545;
				itemDef.modelRotationX = 2;
				itemDef.modelOffset2 = 5;
				itemDef.modelOffset1 = 4;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42624;
				itemDef.anInt200 = 42644;
				itemDef.description = "Vesta's chainbody, a reward from PVP.";
				break;
			case 13884:
				itemDef.name = "Statius's platebody";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wear";
				itemDef.modelID = 42602;
				itemDef.modelZoom = 1312;
				itemDef.modelRotationY = 272;
				itemDef.modelRotationX = 2047;
				itemDef.modelOffset2 = 39;
				itemDef.modelOffset1 = -2;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42625;
				itemDef.anInt200 = 42641;
				itemDef.description = "Statius's platebody, a reward from PVP.";
				break;
			case 13890:
	            itemDef.name = "Statius's platelegs";
	            itemDef.itemActions = new String[5];
	            itemDef.itemActions[1] = "Wear";
	            itemDef.modelID = 42590;
	            itemDef.modelZoom = 1625;
	            itemDef.modelRotationY = 355;
	            itemDef.modelRotationX = 2046;
	            itemDef.modelOffset2 = -11;
	            itemDef.modelOffset1 = 0;
	            itemDef.anInt204 = 0;
	            itemDef.anInt165 = 42632;
	            itemDef.anInt200 = 42647;
	            itemDef.description = "Statius's platelegs, a reward from PVP.";
	            break;

			case 13896:
				itemDef.name = "Statius's full helm";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wear";
				itemDef.modelID = 42596;
				itemDef.modelZoom = 789;
				itemDef.modelRotationY = 96;
				itemDef.modelRotationX = 2039;
				itemDef.modelOffset2 = -7;
				itemDef.modelOffset1 = 2;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42639;
				itemDef.anInt200 = 42655;
				itemDef.description = "Statius's full helm, a reward from PVP.";
				break;
			case 13902:
				itemDef.name = "Statius's warhammer ";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wield";
				itemDef.modelID = 42577;
				itemDef.modelZoom = 1360;
				itemDef.modelRotationY = 507;
				itemDef.modelRotationX = 27;
				itemDef.modelOffset2 = 6;
				itemDef.modelOffset1 = 7;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42623;
				itemDef.anInt200 = 42623;
				itemDef.description = "Statius's warhammer, a reward from PVP.";
				break;
			case 13870:
				itemDef.name = "Morrigan's leather body";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wear";
				itemDef.modelID = 42578;
				itemDef.modelZoom = 1184;
				itemDef.modelRotationY = 545;
				itemDef.modelRotationX = 2;
				itemDef.modelOffset2 = 5;
				itemDef.modelOffset1 = 4;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42626;
				itemDef.anInt200 = 42643;
				itemDef.description = "Morrigan's leather body, a reward from PVP.";
				break;

			case 13873:
				itemDef.name = "Morrigan's leather chaps";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wear";
				itemDef.modelID = 42603;
				itemDef.modelZoom = 1753;
				itemDef.modelRotationY = 482;
				itemDef.modelRotationX = 1;
				itemDef.modelOffset2 = 11;
				itemDef.modelOffset1 = -3;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42631;
				itemDef.anInt200 = 42646;
				itemDef.description = "Morrigan's leather chaps, a reward from PVP.";
				break;

			case 13876:
				itemDef.name = "Morrigan's coif";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wear";
				itemDef.modelID = 42583;
				itemDef.modelZoom = 592;
				itemDef.modelRotationY = 537;
				itemDef.modelRotationX = 5;
				itemDef.modelOffset2 = 6;
				itemDef.modelOffset1 = -3;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42636;
				itemDef.anInt200 = 42652;
				itemDef.description = "Morrigan's coif, a reward from PVP.";
				break;
			case 13879:
				itemDef.name = "Morrigan's javelin";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wield";
				itemDef.modelID = 42592;
				itemDef.modelZoom = 1872;
				itemDef.modelRotationY = 282;
				itemDef.modelRotationX = 2009;
				itemDef.modelOffset2 = 0;
				itemDef.modelOffset1 = 0;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42613;
				itemDef.anInt200 = 42613;
				itemDef.description = "Morrigan's javelin, a reward from PVP.";
				break;
			case 13883:
				itemDef.name = "Morrigan's throwing axe";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wield";
				itemDef.modelID = 42582;
				itemDef.modelZoom = 976;
				itemDef.modelRotationY = 672;
				itemDef.modelRotationX = 2024;
				itemDef.modelOffset2 = 4;
				itemDef.modelOffset1 = -5;
				itemDef.anInt204 = 0;
				itemDef.anInt165 = 42611;
				itemDef.anInt200 = 42611;
				itemDef.description = "Morrigan's throwing axe, a reward from PVP.";
				break;
			
		case 2701:
			itemDef.name = "Gambler Scroll";
			itemDef.description = "Read this scroll to be rewarded with the Gambler rank.";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[0] = "Claim Rank";
			break;

		case 2996:
			itemDef.name = "Pk Points";
			itemDef.modelID = 2430;
			itemDef.modelZoom = 830;
			itemDef.modelRotationY = 404;
			itemDef.modelRotationX = 1644;
			itemDef.modelOffset2 = 0;
			itemDef.modelOffset1 = 5;
			itemDef.anInt204 = 0;
			itemDef.description = "A Pk Point Ticket.";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[0] = "Claim Points";
			break;

		case 10594:
			itemDef.name = "Pkers.Ca Vote Book";
			itemDef.description = "This item is obtained by voting for the server, vote today!";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[0] = "Claim Vote";
			break;

		case 5020:
			itemDef.name = "Boss Ticket";
			itemDef.description = "I can trade this ticket for items.";
			// itemDef.itemActions = new String[5];
			// itemDef.itemActions[2] = "Examine";
			break;

		case 13204:
			itemDef.description = "Talk to the Conventor to exchange it for 2,400 coins per token.";
			break;
		case 15310:
            itemDef.itemActions = new String[5];
            itemDef.itemActions[1] = "Wear";
            itemDef.modelID = 45005;
            itemDef.modelZoom = 2000;
            itemDef.modelRotationY = 500;
            itemDef.modelRotationX = 0;
            itemDef.modelOffset1 = -6;
            itemDef.modelOffset2 = 1;
            itemDef.anInt204 = 14;
            itemDef.anInt165 = 45007;
            itemDef.anInt200 = 45007;
            itemDef.name = "Angel Cape";
            itemDef.description = "Large winged Angel Cape.";
         break;
		case 15311:
            itemDef.itemActions = new String[5];
            itemDef.itemActions[1] = "Wear";
            itemDef.modelID = 45004;
            itemDef.modelZoom = 2000;
            itemDef.modelRotationY = 500;
            itemDef.modelRotationX	 = 0;
            itemDef.modelOffset1 = -6;
            itemDef.modelOffset2 = 1;
            itemDef.anInt204 = 14;
            itemDef.anInt165 = 45006;
            itemDef.anInt200 = 45006;
            itemDef.name = "Deadly Slayer Cape";
            itemDef.description = "Large winged deadly Slayer Cape.";
        break;
		case 15312:
            itemDef.itemActions = new String[5];
            itemDef.itemActions[1] = "Wear";
            itemDef.modelID = 45008;
            itemDef.modelZoom = 2000;
            itemDef.modelRotationY = 500;
            itemDef.modelRotationX = 0;
            itemDef.modelOffset1 = -6;
            itemDef.modelOffset2 = 1;
            itemDef.anInt204 = 14;
            itemDef.anInt165 = 45009;
            itemDef.anInt200 = 45009;
            itemDef.name = "Wings Of Life";
            itemDef.description = "Rosejuice's Wings of Life.";
        break;
		case 15313:
            itemDef.itemActions = new String[5];
            itemDef.itemActions[1] = "Wear";
            itemDef.modelID = 45002;
            itemDef.modelZoom = 2000;
            itemDef.modelRotationY = 500;
            itemDef.modelRotationX = 0;
            itemDef.modelOffset1 = -6;
            itemDef.modelOffset2 = 1;
            itemDef.anInt204 = 14;
            itemDef.anInt165 = 45003;
            itemDef.anInt200 = 45003;
            itemDef.name = "Death Wings Of Anarchy";
            itemDef.description = "Rosejuice's Wings Of Anarchy";
        break;
		case 15314:
		    itemDef.itemActions = new String[5];
            itemDef.itemActions[1] = "Wear";
            itemDef.modelID = 45000;
            itemDef.modelZoom = 2000;
            itemDef.modelRotationY = 500;
            itemDef.modelRotationX = 0;
            itemDef.modelOffset1 = -6;
            itemDef.modelOffset2 = 1;
            itemDef.anInt204 = 14;
            itemDef.anInt165 = 45001;
            itemDef.anInt200 = 45001;
            itemDef.name = "Wings Of Saradomin";
            itemDef.description = "Rosejuice's Wings Of Saradomin";
        break;
		case 15315:
			itemDef.name = "@blu@Water cape";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Wear";
			itemDef.originalModelColors= new int[1];
			itemDef.modifiedModelColors = new int[1];
			itemDef.originalModelColors[0] = 17;
			itemDef.modifiedModelColors[0] = 40;
			itemDef.modelID = 9631;
			itemDef.modelZoom = 2040;
			itemDef.modelRotationY = 545;
			itemDef.modelRotationX = 2;
			itemDef.modelOffset2 = 5;
			itemDef.modelOffset1 = 4;
			itemDef.anInt204 = 0;
			itemDef.anInt165 = 9638;
			itemDef.anInt200 = 9640;
			itemDef.description = "A rare cape of water.";
			break;
		case 15316:
			itemDef.name = "@gre@Earth Cape";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Wear";
			itemDef.originalModelColors= new int[1];
			itemDef.modifiedModelColors = new int[1];
			itemDef.originalModelColors[0] = 48;
			itemDef.modifiedModelColors[0] = 40;
			itemDef.modelID = 9631;
			itemDef.modelZoom = 2040;
			itemDef.modelRotationY = 545;
			itemDef.modelRotationX = 2;
			itemDef.modelOffset2 = 5;
			itemDef.modelOffset1 = 4;
			itemDef.anInt204 = 0;
			itemDef.anInt165 = 9638;
			itemDef.anInt200 = 9640;
			itemDef.description = "A rare cape with liquid dragon flowing down it.";
			break;
		case 15317:
			itemDef.name = "@red@Dragon Cape";
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Wear";
			itemDef.originalModelColors= new int[1];
			itemDef.modifiedModelColors = new int[1];
			itemDef.originalModelColors[0] = 34;
			itemDef.modifiedModelColors[0] = 40;
			itemDef.modelID = 9631;
			itemDef.modelZoom = 2040;
			itemDef.modelRotationY = 545;
			itemDef.modelRotationX = 2;
			itemDef.modelOffset2 = 5;
			itemDef.modelOffset1 = 4;
			itemDef.anInt204 = 0;
			itemDef.anInt165 = 9638;
			itemDef.anInt200 = 9640;
			itemDef.description = "A rare cape with liquid dragon flowing down it.";
			break;
		   case 15318: //item Id
			itemDef.itemActions = new String[5];
			itemDef.itemActions[1] = "Wear";
			itemDef.modelID = 5830;
			itemDef.modelZoom = 2000;
			itemDef.modelRotationY = 500;
			itemDef.modelRotationX = 1020;
			itemDef.modelOffset1 = -6;
			itemDef.modelOffset2 = -1;
			itemDef.anInt165 = 5829;
			itemDef.anInt200 = 5829;
			itemDef.anInt175 = -1;
			itemDef.anInt197 = -1;
			//itemDef.anInt199 = -1;
			itemDef.name = "@bla@PKers cape";
			itemDef.description = "A cape only achieved through the slaughting of many people.";
         break;
		   case 15319: //item Id
   			itemDef.itemActions = new String[5];
   			itemDef.itemActions[1] = "Wear";
   			itemDef.originalModelColors = new int[4];
   			itemDef.modifiedModelColors = new int[4];
   			itemDef.originalModelColors[0] = 57280;
   			itemDef.originalModelColors[1] = 54503;
   			itemDef.originalModelColors[2] = 54183;
   			itemDef.originalModelColors[3] = 11200;
   			itemDef.modifiedModelColors[0] = 9152;
   			itemDef.modifiedModelColors[1] = 7104;
   			itemDef.modifiedModelColors[2] = 7104;
   			itemDef.modifiedModelColors[3] = 796;
   			itemDef.modelID = 42222;
   			itemDef.modelZoom = 2000;
   			itemDef.modelRotationY = 500;
   			itemDef.modelRotationX = 0;
   			itemDef.modelOffset1 = 14;
   			itemDef.modelOffset2 = -6;
   			itemDef.anInt165 = 42223;
   			itemDef.anInt200 = 42223;
   			itemDef.anInt175 = -1;
   			itemDef.anInt197 = -1;
   			//itemDef.anInt199 = -1;
  			itemDef.name = "Donator Cape"; 
   			itemDef.description = "The cape worn by Donators.";
         break;
		   case 15320:
				itemDef.name = "@mag@Purple Santa hat";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wear";
				itemDef.originalModelColors = new int[1];
				itemDef.modifiedModelColors = new int[1];
				itemDef.originalModelColors[0] = 2934671;
				itemDef.modifiedModelColors[0] = 933; 
				itemDef.modelID = 2537;//Item Look
				itemDef.modelZoom = 540;
				itemDef.modelRotationY = 72;
				itemDef.modelRotationX = 136;
				itemDef.modelOffset2 = 0;
				itemDef.modelOffset1 = 1;
				itemDef.anInt165 = 189;
				itemDef.anInt200 = 366;
				itemDef.description = "A Purple Santa hat, the most expensive item in the game";
				break;
			case 15321:
				itemDef.name = "@mag@Purple h'ween mask";
				itemDef.itemActions = new String[5]; 
				itemDef.itemActions[1] = "Wear"; 
				itemDef.originalModelColors = new int [1];
				itemDef.modifiedModelColors = new int[1];
				itemDef.originalModelColors[0] = 2934671; //original color
				itemDef.modifiedModelColors[0] = 926; 
				itemDef.modelID = 2438;
				itemDef.modelZoom = 730;
				itemDef.modelRotationY = 516;
				itemDef.modelRotationX = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffset2 = -10;
				itemDef.anInt165 = 3188;
				itemDef.anInt200 = 3192;
				itemDef.description = "An Purple h'ween mask";
				break;
			case 15322:
				itemDef.name = "@whi@White Santa hat";
				itemDef.itemActions = new String[5];
				itemDef.itemActions[1] = "Wear";
				itemDef.originalModelColors = new int[1];
				itemDef.modifiedModelColors = new int[1];
				itemDef.originalModelColors[0] = 100;
				itemDef.modifiedModelColors[0] = 933; 
				itemDef.modelID = 2537;//Item Look
				itemDef.modelZoom = 540;
				itemDef.modelRotationY = 72;
				itemDef.modelRotationX = 136;
				itemDef.modelOffset2 = 0;
				itemDef.modelOffset1 = 1;
				itemDef.anInt165 = 189;
				itemDef.anInt200 = 366;
				itemDef.description = "A White Santa hat, the most expensive item in the game";
				break;
			    case 15323:
				itemDef.name = "@whi@White h'ween mask";
				itemDef.itemActions = new String[5]; 
				itemDef.itemActions[1] = "Wear"; 
				itemDef.originalModelColors = new int [1];
				itemDef.modifiedModelColors = new int[1];
				itemDef.originalModelColors[0] = 100; //original color
				itemDef.modifiedModelColors[0] = 926; 
				itemDef.modelID = 2438;
				itemDef.modelZoom = 730;
				itemDef.modelRotationY = 516;
				itemDef.modelRotationX = 0;
				itemDef.modelOffset1 = 0;
				itemDef.modelOffset2 = -10;
				itemDef.anInt165 = 3188;
				itemDef.anInt200 = 3192;
				itemDef.description = "An White h'ween mask";
				break;
			    case 15324:
					itemDef.name = "@yel@Golden Santa hat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 7114;
					itemDef.modifiedModelColors[0] = 933; 
					itemDef.modelID = 2537;//Item Look
					itemDef.modelZoom = 540;
					itemDef.modelRotationY = 72;
					itemDef.modelRotationX = 136;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 189;
					itemDef.anInt200 = 366;
					itemDef.description = "A Golden Santa hat, the most expensive item in the game";
					break;
				case 15325:
					itemDef.name = "@yel@Golden h'ween mask";
					itemDef.itemActions = new String[5]; 
					itemDef.itemActions[1] = "Wear"; 
					itemDef.originalModelColors = new int [1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 7114; //original color
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2438;
					itemDef.modelZoom = 730;
					itemDef.modelRotationY = 516;
					itemDef.modelRotationX = 0;
					itemDef.modelOffset1 = 0;
					itemDef.modelOffset2 = -10;
					itemDef.anInt165 = 3188;
					itemDef.anInt200 = 3192;
					itemDef.description = "An Golden h'ween mask";
					break;
				case 15326:
					itemDef.name = "@yel@Golden PartyHat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 7114;
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2635;//Item Look
					itemDef.modelZoom = 440;
					itemDef.modelRotationY = 76;
					itemDef.modelRotationX = 1852;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 187;
					itemDef.anInt200 = 363;
					itemDef.description = "An Golden Partyhat";
					break;
				case 15327:
					itemDef.name = "@gre@Lime Santa hat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 17350;
					itemDef.modifiedModelColors[0] = 933; 
					itemDef.modelID = 2537;//Item Look
					itemDef.modelZoom = 540;
					itemDef.modelRotationY = 72;
					itemDef.modelRotationX = 136;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 189;
					itemDef.anInt200 = 366;
					itemDef.description = "A Lime Santa hat, the most expensive item in the game";
					break;
				case 15328:
					itemDef.name = "@gre@Lime h'ween mask";
					itemDef.itemActions = new String[5]; 
					itemDef.itemActions[1] = "Wear"; 
					itemDef.originalModelColors = new int [1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 17350; //original color
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2438;
					itemDef.modelZoom = 730;
					itemDef.modelRotationY = 516;
					itemDef.modelRotationX = 0;
					itemDef.modelOffset1 = 0;
					itemDef.modelOffset2 = -10;
					itemDef.anInt165 = 3188;
					itemDef.anInt200 = 3192;
					itemDef.description = "An Lime h'ween mask";
					break;
				case 15329:
					itemDef.name = "@gre@Lime PartyHat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 17350;
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2635;//Item Look
					itemDef.modelZoom = 440;
					itemDef.modelRotationY = 76;
					itemDef.modelRotationX = 1852;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 187;
					itemDef.anInt200 = 363;
					itemDef.description = "An Lime Partyhat";
					break;
				case 15330:
					itemDef.name = "Barrows Santa hat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 10388;
					itemDef.modifiedModelColors[0] = 933; 
					itemDef.modelID = 2537;//Item Look
					itemDef.modelZoom = 540;
					itemDef.modelRotationY = 72;
					itemDef.modelRotationX = 136;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 189;
					itemDef.anInt200 = 366;
					itemDef.description = "A Barrows Santa hat, the most expensive item in the game";
					break;
				case 15331:
					itemDef.name = "Barrows h'ween mask";
					itemDef.itemActions = new String[5]; 
					itemDef.itemActions[1] = "Wear"; 
					itemDef.originalModelColors = new int [1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 10388; //original color
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2438;
					itemDef.modelZoom = 730;
					itemDef.modelRotationY = 516;
					itemDef.modelRotationX = 0;
					itemDef.modelOffset1 = 0;
					itemDef.modelOffset2 = -10;
					itemDef.anInt165 = 3188;
					itemDef.anInt200 = 3192;
					itemDef.description = "An Barrows h'ween mask";
					break;
				case 15332:
					itemDef.name = "Barrows PartyHat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 10388;
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2635;//Item Look
					itemDef.modelZoom = 440;
					itemDef.modelRotationY = 76;
					itemDef.modelRotationX = 1852;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 187;
					itemDef.anInt200 = 363;
					itemDef.description = "An Barrows Partyhat";
					break;
				case 15333:
					itemDef.name = "@mag@Pink Santa hat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 57300;
					itemDef.modifiedModelColors[0] = 933; 
					itemDef.modelID = 2537;//Item Look
					itemDef.modelZoom = 540;
					itemDef.modelRotationY = 72;
					itemDef.modelRotationX = 136;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 189;
					itemDef.anInt200 = 366;
					itemDef.description = "A Pink Santa hat, the most expensive item in the game";
					break;
				case 15334:
					itemDef.name = "@mag@Pink h'ween mask";
					itemDef.itemActions = new String[5]; 
					itemDef.itemActions[1] = "Wear"; 
					itemDef.originalModelColors = new int [1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 57300; //original color
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2438;
					itemDef.modelZoom = 730;
					itemDef.modelRotationY = 516;
					itemDef.modelRotationX = 0;
					itemDef.modelOffset1 = 0;
					itemDef.modelOffset2 = -10;
					itemDef.anInt165 = 3188;
					itemDef.anInt200 = 3192;
					itemDef.description = "An Pink h'ween mask";
					break;
				case 15335:
					itemDef.name = "@mag@Pink PartyHat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 57300;;
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2635;//Item Look
					itemDef.modelZoom = 440;
					itemDef.modelRotationY = 76;
					itemDef.modelRotationX = 1852;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 187;
					itemDef.anInt200 = 363;
					itemDef.description = "An Pink Partyhat";
					break;
					
				case 15336:
					itemDef.name = "Voting Santa hat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 61;
					itemDef.modifiedModelColors[0] = 933; 
					itemDef.modelID = 2537;//Item Look
					itemDef.modelZoom = 540;
					itemDef.modelRotationY = 72;
					itemDef.modelRotationX = 136;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 189;
					itemDef.anInt200 = 366;
					itemDef.description = "A Voting Santa hat, the most expensive item in the game";
					break;
				case 15337:
					itemDef.name = "Voting h'ween mask";
					itemDef.itemActions = new String[5]; 
					itemDef.itemActions[1] = "Wear"; 
					itemDef.originalModelColors = new int [1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 61; //original color
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2438;
					itemDef.modelZoom = 730;
					itemDef.modelRotationY = 516;
					itemDef.modelRotationX = 0;
					itemDef.modelOffset1 = 0;
					itemDef.modelOffset2 = -10;
					itemDef.anInt165 = 3188;
					itemDef.anInt200 = 3192;
					itemDef.description = "An Voting h'ween mask";
					break;
				case 15338:
					itemDef.name = "Voting PartyHat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 61;
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2635;//Item Look
					itemDef.modelZoom = 440;
					itemDef.modelRotationY = 76;
					itemDef.modelRotationX = 1852;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 187;
					itemDef.anInt200 = 363;
					itemDef.description = "An Voting Partyhat";
					break;
					
				case 15339:
					itemDef.name = "Orange Santa hat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 461770;
					itemDef.modifiedModelColors[0] = 933; 
					itemDef.modelID = 2537;//Item Look
					itemDef.modelZoom = 540;
					itemDef.modelRotationY = 72;
					itemDef.modelRotationX = 136;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 189;
					itemDef.anInt200 = 366;
					itemDef.description = "A Orange Santa hat, the most expensive item in the game";
					break;
				case 15340:
					itemDef.name = "Orange h'ween mask";
					itemDef.itemActions = new String[5]; 
					itemDef.itemActions[1] = "Wear"; 
					itemDef.originalModelColors = new int [1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 461770; //original color
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2438;
					itemDef.modelZoom = 730;
					itemDef.modelRotationY = 516;
					itemDef.modelRotationX = 0;
					itemDef.modelOffset1 = 0;
					itemDef.modelOffset2 = -10;
					itemDef.anInt165 = 3188;
					itemDef.anInt200 = 3192;
					itemDef.description = "An Orange h'ween mask";
					break;
				case 15341:
					itemDef.name = "Orange PartyHat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 461770;
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2635;//Item Look
					itemDef.modelZoom = 440;
					itemDef.modelRotationY = 76;
					itemDef.modelRotationX = 1852;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 187;
					itemDef.anInt200 = 363;
					itemDef.description = "An Orange Partyhat";
					break;
				case 15342:
					itemDef.name = "@cya@Cyan Santa hat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 34770;
					itemDef.modifiedModelColors[0] = 933; 
					itemDef.modelID = 2537;//Item Look
					itemDef.modelZoom = 540;
					itemDef.modelRotationY = 72;
					itemDef.modelRotationX = 136;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 189;
					itemDef.anInt200 = 366;
					itemDef.description = "A Cyan Santa hat, the most expensive item in the game";
					break;
				case 15343:
					itemDef.name = "@cya@Cyan PartyHat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 34770;
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2635;//Item Look
					itemDef.modelZoom = 440;
					itemDef.modelRotationY = 76;
					itemDef.modelRotationX = 1852;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 187;
					itemDef.anInt200 = 363;
					itemDef.description = "An Cyan Partyhat";
					break;
				case 15344:
					itemDef.name = "@cya@Cyan h'ween mask";
					itemDef.itemActions = new String[5]; 
					itemDef.itemActions[1] = "Wear"; 
					itemDef.originalModelColors = new int [1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 34770; //original color
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2438;
					itemDef.modelZoom = 730;
					itemDef.modelRotationY = 516;
					itemDef.modelRotationX = 0;
					itemDef.modelOffset1 = 0;
					itemDef.modelOffset2 = -10;
					itemDef.anInt165 = 3188;
					itemDef.anInt200 = 3192;
					itemDef.description = "An Cyan h'ween mask";
					break;
				case 15345:
					itemDef.name = "@bla@Black Santa hat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 0;
					itemDef.modifiedModelColors[0] = 933; 
					itemDef.modelID = 2537;//Item Look
					itemDef.modelZoom = 540;
					itemDef.modelRotationY = 72;
					itemDef.modelRotationX = 136;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 189;
					itemDef.anInt200 = 366;
					itemDef.description = "A Black SantaHat, the most expensive item in the game";
					break;	
				case 15346:
					itemDef.name = "@bla@Black PartyHat";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 0;
					itemDef.modifiedModelColors[0] = 926; 
					itemDef.modelID = 2635;//Item Look
					itemDef.modelZoom = 440;
					itemDef.modelRotationY = 76;
					itemDef.modelRotationX = 1852;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 1;
					itemDef.anInt165 = 187;
					itemDef.anInt200 = 363;
					itemDef.description = "A Black Partyhat, the most expensive item in the game";
					break;
				case 15347:
					itemDef.name = "@bla@Black h'ween Mask";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 0;  //original color
					itemDef.modifiedModelColors[0] = 926; //changed color
					itemDef.modelID = 2438;
					itemDef.modelZoom = 730;
					itemDef.modelRotationY = 516;
					itemDef.modelRotationX = 0;
					itemDef.modelOffset1 = 0;
					itemDef.modelOffset2 = -10;
					itemDef.anInt165 = 3188;
					itemDef.anInt200 = 3192;
					itemDef.description = "Aaaarrrghhh... I'm a monster.";
					break;
				case 15348:
					itemDef.itemActions = new String[5];
					itemDef.itemActions[0] = "Claim Prize";
					itemDef.name = "Party Package"; //Name
					itemDef.description = "A Party package obtained through donating"; //Description
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 22464;
					itemDef.modifiedModelColors[0] = 22410;
					itemDef.modelID = 2426;
					itemDef.modelZoom = 1180;
					itemDef.modelRotationY = 160;
					itemDef.modelRotationX = 172;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 0;
					break;
				case 15349:
					itemDef.itemActions = new String[5];
					itemDef.itemActions[0] = "Claim Prize";
					itemDef.name = "H'ween Package"; //Name
					itemDef.description = "A H'ween package obtained through donating"; //Description
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 1010;
					itemDef.modifiedModelColors[0] = 22410;
					itemDef.modelID = 2426;
					itemDef.modelZoom = 1180;
					itemDef.modelRotationY = 160;
					itemDef.modelRotationX = 172;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 0;
					break;
				case 15350:
					itemDef.itemActions = new String[5];
					itemDef.itemActions[0] = "Claim Prize";
					itemDef.name = "Huge Package"; //Name
					itemDef.description = "A Huge package obtained through donating"; //Description
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 23009;
					itemDef.modifiedModelColors[0] = 22410;
					itemDef.modelID = 2426;
					itemDef.modelZoom = 1180;
					itemDef.modelRotationY = 160;
					itemDef.modelRotationX = 172;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 0;
					break;
				case 15351:
					itemDef.itemActions = new String[5];
					itemDef.itemActions[0] = "Claim Prize";
					itemDef.name = "Rare Package"; //Name
					itemDef.description = "A Rare package obtained through donating"; //Description
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 8464;
					itemDef.modifiedModelColors[0] = 22410;
					itemDef.modelID = 2426;
					itemDef.modelZoom = 1180;
					itemDef.modelRotationY = 160;
					itemDef.modelRotationX = 172;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 0;
					break;
				case 15352:
					itemDef.itemActions = new String[5];
					itemDef.itemActions[0] = "Claim Prize";
					itemDef.name = "Pking Package"; //Name
					itemDef.description = "A Pking package obtained through donating"; //Description
					itemDef.originalModelColors = new int[1];
					itemDef.modifiedModelColors = new int[1];
					itemDef.originalModelColors[0] = 0;
					itemDef.modifiedModelColors[0] = 22410;
					itemDef.modelID = 2426;
					itemDef.modelZoom = 1180;
					itemDef.modelRotationY = 160;
					itemDef.modelRotationX = 172;
					itemDef.modelOffset2 = 0;
					itemDef.modelOffset1 = 0;
					break;
				case 15353:
					itemDef.itemActions = new String[5];
					itemDef.itemActions[1] = "Wear";
					itemDef.name = "@yel@Ring of Flying"; //Name
					itemDef.description = "A ring that makes you fly!"; //Description
					itemDef.modelID = 2784;
					itemDef.modelZoom = 830;
					itemDef.modelRotationY = 268;
					itemDef.modelRotationX = 180;
					itemDef.anInt204 = 2024;
					itemDef.modelOffset1 = 3;
					itemDef.modelOffset2 = -35;
					break;
				case 15354:
					itemDef.name = "Rocktail";
					itemDef.modelZoom = 1332;
					itemDef.modelRotationY = 499;
					itemDef.modelRotationX = 1926;
					itemDef.modelOffset1 = 3;
					itemDef.modelOffset2 = 0;
					itemDef.groundActions = new String[5];
					itemDef.groundActions[2] = "Take";
					itemDef.itemActions = new String[5];
					itemDef.itemActions[0] = "Eat";
					itemDef.itemActions[4] = "Drop";
					itemDef.modelID = 48728;
					itemDef.anInt165 = -1;
					itemDef.anInt200 = -1;
					itemDef.value = 600;
				break;
				

		}
		return itemDef;
	}

	private void toNote() {
		ItemDef itemDef = forID(certTemplateID);
		modelID = itemDef.modelID;
		modelZoom = itemDef.modelZoom;
		modelRotationY = itemDef.modelRotationY;
		modelRotationX = itemDef.modelRotationX;

		anInt204 = itemDef.anInt204;
		modelOffset1 = itemDef.modelOffset1;
		modelOffset2 = itemDef.modelOffset2;
		modifiedModelColors = itemDef.modifiedModelColors;
		originalModelColors = itemDef.originalModelColors;
		ItemDef itemDef_1 = forID(certID);
		name = itemDef_1.name;
		membersObject = itemDef_1.membersObject;
		value = itemDef_1.value;
		String s = "a";
		char c = itemDef_1.name.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
			s = "an";
		stackable = true;
	}

	public static Sprite getSprite(int id, int size, int color, int zoom) {
		ItemDef item = forID(id);
		if (item.stackIDs == null) {
			size = -1;
		}
		if (size > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++) {
				if (size >= item.stackAmounts[j1] && item.stackAmounts[j1] != 0) {
					i1 = item.stackIDs[j1];
				}
			}
			if (i1 != -1) {
				item = forID(i1);
			}
		}
		Model model = item.method201(1);
		if (model == null)
			return null;
		Sprite image = new Sprite(32, 32);
		int k1 = Rasterizer.centerX;
		int l1 = Rasterizer.centerY;
		int ai[] = Rasterizer.lineOffsets;
		int ai1[] = DrawingArea.pixels;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Rasterizer.aBoolean1464 = false;
		DrawingArea.initDrawingArea(32, 32, image.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Rasterizer.method364();
		int itemZoom = item.modelZoom * zoom - 500;
		int l3 = Rasterizer.anIntArray1470[item.modelRotationY] * itemZoom >> 16;
		int i4 = Rasterizer.anIntArray1471[item.modelRotationY] * itemZoom >> 16;
		model.method482(item.modelRotationX, item.anInt204, item.modelRotationY, item.modelOffset1,
				l3 + model.modelHeight / 2 + item.modelOffset2, i4 + item.modelOffset2);
		if (color == 0) {
			for (int index = 31; index >= 0; index--) {
				for (int index2 = 31; index2 >= 0; index2--)
					if (image.myPixels[index + index2 * 32] == 0 && index > 0 && index2 > 0
							&& image.myPixels[(index - 1) + (index2 - 1) * 32] > 0)
						image.myPixels[index + index2 * 32] = 0x302020;
			}
		}
		DrawingArea.initDrawingArea(j2, i2, ai1);
		DrawingArea.setDrawingArea(j3, k2, l2, i3);
		Rasterizer.centerX = k1;
		Rasterizer.centerY = l1;
		Rasterizer.lineOffsets = ai;
		Rasterizer.aBoolean1464 = true;
		if (item.stackable) {
			image.maxWidth = 33;
		} else {
			image.maxWidth = 32;
		}
		image.maxHeight = size;
		return image;
	}

	public static Sprite getSprite(int i, int j, int k) {
		if (k == 0) {
			Sprite sprite = (Sprite) mruNodes1.insertFromCache(i);
			if (sprite != null && sprite.maxHeight != j && sprite.maxHeight != -1) {

				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		ItemDef itemDef = forID(i);
		if (itemDef.stackIDs == null)
			j = -1;
		if (j > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++)
				if (j >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
					i1 = itemDef.stackIDs[j1];

			if (i1 != -1)
				itemDef = forID(i1);
		}
		Model model = itemDef.method201(1);
		if (model == null)
			return null;
		Sprite sprite = null;
		if (itemDef.certTemplateID != -1) {
			sprite = getSprite(itemDef.certID, 10, -1);
			if (sprite == null)
				return null;
		}
		Sprite enabledSprite = new Sprite(32, 32);
		int k1 = Rasterizer.centerX;
		int l1 = Rasterizer.centerY;
		int ai[] = Rasterizer.lineOffsets;
		int ai1[] = DrawingArea.pixels;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Rasterizer.aBoolean1464 = false;
		DrawingArea.initDrawingArea(32, 32, enabledSprite.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Rasterizer.method364();
		int k3 = itemDef.modelZoom;
		if (k == -1)
			k3 = (int) (k3 * 1.5D);
		if (k > 0)
			k3 = (int) (k3 * 1.04D);
		int l3 = Rasterizer.anIntArray1470[itemDef.modelRotationY] * k3 >> 16;
		int i4 = Rasterizer.anIntArray1471[itemDef.modelRotationY] * k3 >> 16;
		model.method482(itemDef.modelRotationX, itemDef.anInt204, itemDef.modelRotationY, itemDef.modelOffset1,
				l3 + model.modelHeight / 2 + itemDef.modelOffset2, i4 + itemDef.modelOffset2);
		for (int i5 = 31; i5 >= 0; i5--) {
			for (int j4 = 31; j4 >= 0; j4--)
				if (enabledSprite.myPixels[i5 + j4 * 32] == 0)
					if (i5 > 0 && enabledSprite.myPixels[(i5 - 1) + j4 * 32] > 1)
						enabledSprite.myPixels[i5 + j4 * 32] = 1;
					else if (j4 > 0 && enabledSprite.myPixels[i5 + (j4 - 1) * 32] > 1)
						enabledSprite.myPixels[i5 + j4 * 32] = 1;
					else if (i5 < 31 && enabledSprite.myPixels[i5 + 1 + j4 * 32] > 1)
						enabledSprite.myPixels[i5 + j4 * 32] = 1;
					else if (j4 < 31 && enabledSprite.myPixels[i5 + (j4 + 1) * 32] > 1)
						enabledSprite.myPixels[i5 + j4 * 32] = 1;

		}

		if (k > 0) {
			for (int j5 = 31; j5 >= 0; j5--) {
				for (int k4 = 31; k4 >= 0; k4--)
					if (enabledSprite.myPixels[j5 + k4 * 32] == 0)
						if (j5 > 0 && enabledSprite.myPixels[(j5 - 1) + k4 * 32] == 1)
							enabledSprite.myPixels[j5 + k4 * 32] = k;
						else if (k4 > 0 && enabledSprite.myPixels[j5 + (k4 - 1) * 32] == 1)
							enabledSprite.myPixels[j5 + k4 * 32] = k;
						else if (j5 < 31 && enabledSprite.myPixels[j5 + 1 + k4 * 32] == 1)
							enabledSprite.myPixels[j5 + k4 * 32] = k;
						else if (k4 < 31 && enabledSprite.myPixels[j5 + (k4 + 1) * 32] == 1)
							enabledSprite.myPixels[j5 + k4 * 32] = k;

			}

		} else if (k == 0) {
			for (int k5 = 31; k5 >= 0; k5--) {
				for (int l4 = 31; l4 >= 0; l4--)
					if (enabledSprite.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0
							&& enabledSprite.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0)
						enabledSprite.myPixels[k5 + l4 * 32] = 0x302020;

			}

		}
		if (itemDef.certTemplateID != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (k == 0)
			mruNodes1.removeFromCache(enabledSprite, i);
		DrawingArea.initDrawingArea(j2, i2, ai1);
		DrawingArea.setDrawingArea(j3, k2, l2, i3);
		Rasterizer.centerX = k1;
		Rasterizer.centerY = l1;
		Rasterizer.lineOffsets = ai;
		Rasterizer.aBoolean1464 = true;
		if (itemDef.stackable)
			enabledSprite.maxWidth = 33;
		else
			enabledSprite.maxWidth = 32;
		enabledSprite.maxHeight = j;
		return enabledSprite;
	}

	public Model method201(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];

			if (j != -1)
				return forID(j).method201(1);
		}
		Model model = (Model) mruNodes2.insertFromCache(id);
		if (model != null)
			return model;
		model = Model.method462(modelID);
		if (model == null)
			return null;
		applyTexture(model, id);
		if (anInt167 != 128 || anInt192 != 128 || anInt191 != 128)
			model.method478(anInt167, anInt191, anInt192);
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				model.method476(modifiedModelColors[l], originalModelColors[l], getTexture(id));

		}
		model.method479(64 + brightness, 768 + anInt184, -50, -10, -50, true);
		model.aBoolean1659 = true;
		mruNodes2.removeFromCache(model, id);
		return model;
	}

	public int modelCustomColor = 0;
	public int modelCustomColor2 = 0;
	public int modelCustomColor3 = 0;
	public int modelCustomColor4 = 0;
	public int modelSetColor = 0;

	public Model method202(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];

			if (j != -1)
				return forID(j).method202(1);
		}
		Model model = Model.method462(modelID);
		if (model == null)
			return null;
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				model.method476(modifiedModelColors[l], originalModelColors[l], getTexture(id));

		}
		return model;
	}

	private void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1)
				modelID = stream.readUnsignedWord();
			else if (i == 2)
				name = stream.readString();
			else if (i == 3)
				description = stream.readString();
			else if (i == 4)
				modelZoom = stream.readUnsignedWord();
			else if (i == 5)
				modelRotationY = stream.readUnsignedWord();
			else if (i == 6)
				modelRotationX = stream.readUnsignedWord();
			else if (i == 7) {
				modelOffset1 = stream.readUnsignedWord();
				if (modelOffset1 > 32767)
					modelOffset1 -= 0x10000;
			} else if (i == 8) {
				modelOffset2 = stream.readUnsignedWord();
				if (modelOffset2 > 32767)
					modelOffset2 -= 0x10000;
			} else if (i == 10)
				stream.readUnsignedWord();
			else if (i == 11)
				stackable = true;
			else if (i == 12)
				value = stream.readDWord();
			else if (i == 16)
				membersObject = true;
			else if (i == 23) {
				anInt165 = stream.readUnsignedWord();
				maleEquipOffset = stream.readSignedByte();
			} else if (i == 24)
				anInt188 = stream.readUnsignedWord();
			else if (i == 25) {
				anInt200 = stream.readUnsignedWord();
				femaleEquipOffset = stream.readSignedByte();
			} else if (i == 26)
				anInt164 = stream.readUnsignedWord();
			else if (i >= 30 && i < 35) {
				if (groundActions == null)
					groundActions = new String[5];
				groundActions[i - 30] = stream.readString();
			} else if (i >= 35 && i < 40) {
				if (itemActions == null)
					itemActions = new String[5];
				itemActions[i - 35] = stream.readString();
			} else if (i == 40) {
				int j = stream.readUnsignedByte();
				originalModelColors = new int[j];
				modifiedModelColors = new int[j];
				for (int k = 0; k < j; k++) {
					originalModelColors[k] = stream.readUnsignedWord();
					modifiedModelColors[k] = stream.readUnsignedWord();
				}
			} else if (i == 78)
				anInt185 = stream.readUnsignedWord();
			else if (i == 79)
				anInt162 = stream.readUnsignedWord();
			else if (i == 90)
				anInt175 = stream.readUnsignedWord();
			else if (i == 91)
				anInt197 = stream.readUnsignedWord();
			else if (i == 92)
				anInt166 = stream.readUnsignedWord();
			else if (i == 93)
				anInt173 = stream.readUnsignedWord();
			else if (i == 95)
				anInt204 = stream.readUnsignedWord();
			else if (i == 97)
				certID = stream.readUnsignedWord();
			else if (i == 98)
				certTemplateID = stream.readUnsignedWord();
			else if (i >= 100 && i < 110) {
				if (stackIDs == null) {
					stackIDs = new int[10];
					stackAmounts = new int[10];
				}
				stackIDs[i - 100] = stream.readUnsignedWord();
				stackAmounts[i - 100] = stream.readUnsignedWord();
			} else if (i == 110)
				anInt167 = stream.readUnsignedWord();
			else if (i == 111)
				anInt192 = stream.readUnsignedWord();
			else if (i == 112)
				anInt191 = stream.readUnsignedWord();
			else if (i == 113)
				brightness = stream.readSignedByte();
			else if (i == 114)
				anInt184 = stream.readSignedByte() * 5;
			else if (i == 115)
				team = stream.readUnsignedByte();
		} while (true);
	}

	public Model getModelForAmount(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];
			if (j != -1)
				return forID(j).method201(1);
		}
		Model model = (Model) mruNodes2.insertFromCache(id);
		if (model != null)
			return model;
		model = Model.method462(modelID);
		if (model == null)
			return null;
		if (anInt167 != 128 || anInt192 != 128 || anInt191 != 128)
			model.method478(anInt167, anInt191, anInt192);
		if (modifiedModelColors != null) {
			for (int l = 0; l < modifiedModelColors.length; l++)
				model.method476(modifiedModelColors[l], originalModelColors[l], getTexture(id));
		}
		model.method479(64 + brightness, 768 + anInt184, -50, -10, -50, true);
		model.aBoolean1659 = true;
		mruNodes2.removeFromCache(model, id);
		return model;
	}

	private ItemDef() {
		id = -1;
	}

	private byte femaleEquipOffset;
	public int value;// anInt155
	public int[] modifiedModelColors;// newModelColor
	public int id;// anInt157
	public static MRUNodes mruNodes1 = new MRUNodes(100);
	public static MRUNodes mruNodes2 = new MRUNodes(50);
	public int[] originalModelColors;
	public boolean membersObject;// aBoolean161
	private int anInt162;
	public int certTemplateID;
	public int anInt164;// femArmModel
	public int anInt165;// maleWieldModel
	private int anInt166;
	private int anInt167;
	public String groundActions[];
	public int modelOffset1;
	public String name;// itemName
	public static ItemDef[] cache;
	private int anInt173;
	public int modelID;// dropModel
	public int anInt175;
	public boolean stackable;// itemStackable
	public String description;// itemExamine
	public int certID;
	private static int cacheIndex;
	public int modelZoom;
	public static boolean isMembers = true;
	private static Stream stream;
	private int anInt184;
	private int anInt185;
	public int anInt188;// maleArmModel
	public String itemActions[];// itemMenuOption
	public int modelRotationY;// modelRotateUp
	private int anInt191;
	private int anInt192;
	public int[] stackIDs;// modelStack
	public int modelOffset2;//
	private static int[] streamIndices;
	private int brightness;
	public int anInt197;
	public int modelRotationX;// modelRotateRight
	public int anInt200;// femWieldModel
	public int[] stackAmounts;// itemAmount
	public int team;
	public static int totalItems;
	public int anInt204;// modelPositionUp
	private byte maleEquipOffset;

}
