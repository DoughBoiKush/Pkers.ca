package org.osps.model.shops;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.content.dialogue.impl.ConverterDialogue;
import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Misc;
import org.osps.world.ShopHandler;


public class ShopAssistant {

	private Player c;

	public ShopAssistant(Player client) {
		this.c = client;
	}

	public boolean shopSellsItem(int itemID) {
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
				return true;
			}
		} 
		return false;
	}

	public static final int[][] PKP_DATA = { { 1050, 100000000 } };

	/**
	 * Shops
	 **/

	public void openShop(int ShopID) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.getItems().resetItems(3823);
		resetShop(ShopID);
		c.isShopping = true;
		c.myShopId = ShopID;
		c.getPA().sendFrame248(3824, 3822);
		if (ShopID == 68)
			c.getPA().sendFrame126("Fishing Tourney Shop - Points: " + c.fishingTourneyPoints, 3901);
		if (ShopID == 81)
			c.getPA().sendFrame126("Donator Shop - Points: @or2@" + c.donatorPoints, 3901);
		else if (ShopID == 72)
			c.getPA().sendFrame126("Blast Mine Shop - Points: " + c.blastPoints, 3901);
		else if (ShopID == 74)
			c.getPA().sendFrame126("@or1@Sacrifice Shop - @or2@(SFP: @or1@" + c.hungerPoints + "@or2@)", 3901);
		else if (ShopID == 29)
			c.getPA().sendFrame126("@or1@Pk Point Shop - @or2@(PKP: @or1@" + c.pkp + "@or2@)", 3901);
		else if (ShopID == 38)
			c.getPA().sendFrame126("@or1@Pk Point Shop 2 - @or2@(PKP: @or1@" + c.pkp + "@or2@)", 3901);
		else if (ShopID == 111)
			c.getPA().sendFrame126("@or1@PKP Sell & Exchange - @or2@(PKP: @or1@" + c.pkp + "@or2@)", 3901);
		else if (ShopID == 77)
		c.getPA().sendFrame126("@or1@Vote Shop - @or2@(VP: @or1@" + c.votePoints + "@or2@)", 3901);
		else
			c.getPA().sendFrame126(ShopHandler.ShopName[ShopID], 3901);
	}

	public void updatePlayerShop() {
		for (int i = 1; i < Config.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].isShopping == true && PlayerHandler.players[i].myShopId == c.myShopId
						&& i != c.index) {
					PlayerHandler.players[i].updateShop = true;
				}
			}
		}
	}

	public void updateshop(int i) {
		resetShop(i);
	}

	public void resetShop(int ShopID) {
		// synchronized (c) {
		int TotalItems = 0;
		for (int i = 0; i < ShopHandler.MaxShopItems; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0) {
				TotalItems++;
			}
		}
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		if (ShopID == 80) {
			c.getPA().sendFrame171(0, 28050);
			c.getPA().sendFrame126("Bounties: " + Misc.insertCommas(Integer.toString(c.getBH().getBounties())), 28052);
		} else {
			c.getPA().sendFrame171(1, 28050);
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		int TotalCount = 0;
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if (ShopHandler.ShopItems[ShopID][i] > 0 || i <= ShopHandler.ShopItemsStandard[ShopID]) {
				if (ShopHandler.ShopItemsN[ShopID][i] > 254) {
					c.getOutStream().writeByte(255);
					c.getOutStream().writeDWord_v2(ShopHandler.ShopItemsN[ShopID][i]);
				} else {
					c.getOutStream().writeByte(ShopHandler.ShopItemsN[ShopID][i]);
				}
				if (ShopHandler.ShopItems[ShopID][i] > Config.ITEM_LIMIT || ShopHandler.ShopItems[ShopID][i] < 0) {
					ShopHandler.ShopItems[ShopID][i] = Config.ITEM_LIMIT;
				}
				c.getOutStream().writeWordBigEndianA(ShopHandler.ShopItems[ShopID][i]);
				TotalCount++;
			}
			if (TotalCount > TotalItems) {
				break;
			}
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public double getItemShopValue(int itemID, int Type, int fromSlot) {
		double ShopValue = 1;
		double TotPrice = 0;
		//int price = getSpecialItemValue(ItemID);
		int price = getItemShopValue(itemID);
		//int price = ItemDefinition.forId(ItemID).getGeneralPrice();
		ShopValue = price;

		TotPrice = ShopValue;

		if (ShopHandler.ShopBModifier[c.myShopId] == 1) {
			TotPrice *= 1;
			TotPrice *= 1;
			if (Type == 1) {
				TotPrice *= 1;
			}
		} else if (Type == 1) {
			TotPrice *= 1;
		}
		return TotPrice;
	}

	/*public int donatorValue(int id) {
		switch (id) {
		case 13265:
			return 4000;
		case 12765:
		case 12766:
		case 12767:
		case 12768:
		case 11235:
			return 275;
		case 13243:
		case 13241:
			return 5000;
		case 20368:
			return 5500;
		case 20370:
			return 4000;
		case 20372:
			return 5000;
		case 20374:
			return 3000;
		case 20000:
			return 3000;
		case 20050:
			return 15000;
		case 19722:
			return 2000;
		case 20053:
			return 1000;
		case 13652:
		case 14484:
			return 4000;
		case 12904:
			return 3000;
		case 12785:
		return 5000;
		case 6199:
			return 200;
		case 4067:
			return 1;
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			return 3000;
		case 12006:
			return 5000;
		case 11824:
		case 11889:
		case 10334:
		case 10330:
		case 10332:
		case 10336:
			return 7000;
		case 12422:
		case 12424:
		case 12426:
		case 11791:
		case 11907:
		case 2690:
		case 2697:
		case 10350:
		case 10348:
		case 10346:
		case 10352:
		case 10342:
		case 10344:
		case 10338:
		case 10340:
			return 10000;
		case 12929:
		case 12931:
			return 20000;
		case 11804:
		case 11806:
		case 11808:
			return 15000;
		case 1038:
		case 1040:
			return 4000;
		case 1044:
		case 1046:
			return 5000;
		case 1042:
		case 1048:
			return 6000;
		case 11283:
			return 11000;
		case 962:
			return 5000;
		case 12399:
			return 10000;
		case 11862:
			return 25000;
		case 11926:
		case 11924:
		case 12821:
			return 15000;
		case 11863:
			return 20000;
		case 13175:
			return 10000;
		case 1053:
		case 11834:
			return 2500;
		case 1055:
			return 3500;
		case 1057:
			return 4500;
		case 11847:
			return 15000;
		case 1037:
			return 1000;
		case 1419:
			return 1500;
		case 4084:
			return 10000;
		case 12817:
			return 10000;
		case 13576:
		case 11828:
		case 11830:
		case 11832:
			return 2000;
		case 12437:
			return 10000;
		case 12924:
			return 5000;
		case 13211:
		case 13213:
		case 13215:
		case 12902:
		case 2691:
		case 2698:
			return 20000;
		case 12825:
			return 25000;
		case 2692:
		case 2699:
			return 30000;
		case 11802:
			return 45000;
		case 13173:
			return 29000;
		case 11826:
			return 500;
		case 11785:
			return 1500;
		case 15098:
			return 5000;
		default:
			return Integer.MAX_VALUE;
		}
	}*/
	public int getVoteItemValue(int id) {
		switch (id) {
		case 1037:
			return 200;
		case 15337:
			return 100;
		case 15336:
			return 100;
		case 15338:
			return 100;
		case 12424:
			return 75;
		case 12422:
			return 75;
		case 12426:
			return 75;
		case 13124:
			return 75;
		case 12006:
			return 35;
		case 12002:
			return 35;
		case 13072:
			return 20;
		case 13073:
			return 20;
		case 10350:
			return 25;
		case 10348:
			return 50;
		case 10346:
			return 50;
		case 10352:
			return 25;
		case 10334:
			return 25;
		case 10330:
			return 50;
		case 10332:
			return 50;
		case 10336:
			return 25;
		case 10342:
			return 25;
		case 20576:
			return 50;
		case 20577:
			return 50;
		case 10344:
			return 25;
		case 12399:
			return 250;
		case 12436:
			return 25;
		case 13200:
			return 50;
		case 13201:
			return 50;
		case 12802:
			return 20;
		}
		return 0;
	}
	
	public int getDonatorValue(int id) {
		switch (id) {
		case 15317:
		case 15315:
		case 15316:
		break;
		}
			return 0;
		}
	public int donatorValue(int id) {
		switch (id) {
		case 15315:
		case 15316:
		case 15317:
			return 0;
		case 6199:
			return 10;
		case 7630:
			return 50;
		default:
			return Integer.MAX_VALUE;
		}
	}

	public static int getItemShopValue(int itemId) { // TODO
		switch (itemId) {
		case 4745:
		case 4746:
		case 4747:
		case 4748:
		case 4749:
		case 4750:
		case 4751:
		case 4752:
		case 4753:
		case 4754:
		case 4755:
		case 4756:
		case 4757:
		case 4758:
		case 4759:
		case 4760:
		case 4708:
		case 4709:
		case 4710:
		case 4711:
		case 4712:
		case 4713:
		case 4714:
		case 4715:
		case 4716:
		case 4717:
		case 4718:
		case 4719:
		case 4720:
		case 4721:
		case 4722:
		case 4723:
		case 4724:
		case 4725:
		case 4726:
		case 4727:
		case 4728:
		case 4729:
		case 4730:
		case 4731:
		case 4732:
		case 4733:
		case 4734:
		case 4735:
		case 4736:
		case 4737:
		case 4738:
		case 4739:
			return 300;
		case 4151:
		case 12773:
			return 250;
		case 12006:
			return 500;
		case 6585:
		case 11840:
			return 200;
		case 10334:
		case 10330:
		case 10332:
		case 10336:
			return 250;
		case 12357:
			return 200;
		case 12422:
		case 12424:
		case 12426:
		case 12437:
		case 10350:
		case 10348:
		case 10346:
		case 10352:
		case 10342:
		case 10344:
		case 10338:
		case 10340:
			return 350;
		case 13173:
			return 250;
		case 13174:
			return 200;
		case 11863:
			return 345;
		case 11862:
			return 340;
		case 11847:
			return 300;
		case 962:
			return 150;
		case 1419:
			return 110;
		case 4084:
		case 12848:
			return 110;
		case 6199:
			return 50;
		case 13239:
		case 13237:
		case 13235:
			return 1000;
		}
		/** Donator items **/
		if (itemId == 11889 || itemId == 11824) {
			return 80;
		}
		if (itemId == 11905 || itemId == 11906 || itemId == 11907 || itemId == 11908) {
			return 250;
		}
		if (itemId == 12817 || itemId == 12818 || itemId == 12819 || itemId == 12820 || itemId == 12821
				|| itemId == 12822 || itemId == 12824 || itemId == 12825) {
			return 800;
		}

		if (itemId == 1038 || itemId == 1038 || itemId == 1039 || itemId == 1040 || itemId == 1041 || itemId == 1042
				|| itemId == 1043 || itemId == 1044 || itemId == 1045 || itemId == 1046 || itemId == 1047
				|| itemId == 1048 || itemId == 1049 || itemId == 1050 || itemId == 1051 || itemId == 1053
				|| itemId == 1054 || itemId == 1055 || itemId == 1056 || itemId == 1057 || itemId == 1058) {
			return 150;
		}

		if (itemId == 961 || itemId == 963) {
			return 150;
		}

		if (itemId == 1037) {
			return 70;
		}

		/** PK ITEMS **/
		if (itemId == 11802) {
			return 1200;
		}
		if (itemId == 13652 || itemId == 14484) {
			return 1400;
		}
		if (itemId == 13265) {
			return 600;
		}
		if (itemId == 13263) {
			return 1350;
		}
		if (itemId == 13576) {
			return 1300;
		}
		if (itemId == 11804 || itemId == 11806 || itemId == 11808) {
			return 300;
		}
		if (itemId == 11785) {
			return 750;
		}
		if (itemId == 11926) {
			return 500;
		}
		if (itemId == 2577 || itemId == 2581) {
			return 40;
		}
		if (itemId == 6733) {
			return 3;
		}
		if (itemId == 12006 || itemId == 11838) {
			return 100;
		}
		if (itemId == 11283 || itemId == 11284 || itemId == 11285) {
			return 600;
		}
		if (itemId == 13215) {
			return 600;
		}
		if (itemId == 11771 || itemId == 11773 || itemId == 11772 || itemId == 11770) {
			return 50;
		}
		if (itemId == 6918 || itemId == 6916 || itemId == 6917 || itemId == 6918 || itemId == 6919 || itemId == 6920
				|| itemId == 6921 || itemId == 6922 || itemId == 6923 || itemId == 6924 || itemId == 6925) {
			return 25;
		}
		if (itemId == 13213) {
			return 600;
		}
		if (itemId == 12929 || itemId == 12927 || itemId == 12928 || itemId == 12930 || itemId == 12931) {
			return 500;
		}
		if (itemId == 6737) {
			return 3;
		}
		if (itemId == 11924) {
			return 500;
		}
		if (itemId == 6914 || itemId == 6914 || itemId == 6889 || itemId == 6890) {
			return 80;
		}
		if (itemId == 12002) {
			return 250;
		}
		if (itemId == 6735 || itemId == 6731) {
			return 3;
		}
		if (itemId == 12904 || itemId == 12902 || itemId == 12900) {
			return 650;
		}
		if (itemId == 11791) {
			return 600;
		}
		if (itemId == 13211) {
			return 600;
		}
		if (itemId == 11826 || itemId == 11828 || itemId == 11830) {
			return 400;
		}
		if (itemId == 12596) {
			return 300;
		}
		if (itemId == 11834 || itemId == 11832) {
			return 800;
		}
		if (itemId == 12924 || itemId == 12926) {
			return 2200;
		}
		return 1;
	}

	/**
	 * buy item from shop (Shop Price)
	 **/

	public void buyFromShopPrice(int removeId, int removeSlot) {
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 0, removeSlot));
		ShopValue *= 1.00;
		String ShopAdd = "";
		if (c.myShopId == 7
			 || c.myShopId == 26 || c.myShopId == 57
				|| c.myShopId == 60 || c.myShopId == 61 || c.myShopId == 62 || c.myShopId == 63 || c.myShopId == 65) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " Coins.");
			return;
		}
		
		if (c.myShopId == 22 || c.myShopId == 20 || c.myShopId == 2 || c.myShopId == 88 || c.myShopId == 113 || c.myShopId == 3
				|| c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 16) {
			c.sendMessage("This item is free.");
			return;
		}
		
		if(c.myShopId == 29 || c.myShopId == 38 || c.myShopId == 111 || c.myShopId == 40) {
                    if (removeId == 12934) {
                        c.sendMessage("@blu@Zulrah Scales@bla@ currently costs @red@0.25 @bla@Pk Points each. (Min: 4)");
                        return;
                    }
                    if (removeId == 15318) {
                        c.sendMessage("@blu@Pker's Cape@bla@ is currently free.");
                        return;
                    }
                    
			            c.sendMessage("@blu@" + c.getItems().getItemName(removeId) +"@bla@ currently costs " + "@red@" + getPKPCost(removeId)+ " @bla@Pk Points.");
			            return;
		            }
//		if(c.myShopId == 111) {
//	            c.sendMessage("@blu@" + c.getItems().getItemName(removeId) +"@bla@ currently costs " + "@red@" + getPKPCost(removeId)+ " @bla@Pk Points.");
//	            return;
//            }
		// if (c.myShopId == 3 || c.myShopId == 4 || c.myShopId == 5 ||
		// c.myShopId == 6 || c.myShopId == 8 || c.myShopId == 47 || c.myShopId
		// == 48
		// || c.myShopId == 111 || c.myShopId == 113) {
		// c.sendMessage("This item is for free!");
		// return;
		// }
		if (c.myShopId == 68) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " fishing tourney points.");
			return;
		}
		if (c.myShopId == 74) {
			c.sendMessage("@blu@" + c.getItems().getItemName(removeId) +"@bla@ currently costs " + "@red@" + getItemValueHungerGames(removeId)+ " @bla@Sacrifice Points.");
			return;
		}
		if (c.myShopId == 72) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " blast points.");
			return;
		}
		if (c.myShopId == 44) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " slayer points.");
			return;
		}
		if (c.myShopId == 80) {
			c.sendMessage("@blu@" + c.getItems().getItemName(removeId) + " @bla@currently costs @red@"
					+ Misc.insertCommas(Integer.toString(getBountyHunterItemCost(removeId))) + " @bla@Bounties.");
			return;
		}
		if (c.myShopId == 10) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " slayer points.");
			return;
		}
		if (c.myShopId == 77) {
			c.sendMessage("@blu@" + c.getItems().getItemName(removeId) +"@bla@ currently costs " + "@red@" + getVoteItemValue(removeId)+ " @bla@Vote Points.");
			return;
		}
		if (c.myShopId == 115) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " vote points.");
			return;
		}
		if (c.myShopId == 78) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " achievement points.");
			return;
		}
		if (c.myShopId == 75) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + getSpecialItemValue(removeId)
					+ " PC points.");
			return;
		}
		if (c.myShopId == 9) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + donatorValue(removeId)
					+ " donator points.");
			return;
		}
		if (c.myShopId == 116 || c.myShopId == 117) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + donatorValue(removeId)
					+ " donator points.");
			return;
		}
		if (c.myShopId == 71) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + donatorValue(removeId)
					+ " donator points.");
			return;
		}
		if (c.myShopId == 99) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + ShopValue + " Valuable blood.");
			return;
		}
		if (c.myShopId == 200) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + GracePrice(removeId) + " Marks of Grace.");
			return;
		}
		if (c.myShopId == 210) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + Redwood(removeId) + " Redwood Logs.");
			return;
		}
		if (c.myShopId == 15) {
			c.sendMessage("This item current costs " + c.getItems().getUntradePrice(removeId) + " coins.");
			return;
		}
		if (c.myShopId == 81) {
			c.sendMessage(c.getItems().getItemName(removeId) + ": currently costs " + donatorValue(removeId) + " donator points.");
			return;
		}
	    c.sendMessage("@blu@" + c.getItems().getItemName(removeId)+"@bla@ is currenlty free. ");
    }
	public int getBountyHunterItemCost(int itemId) {
		switch (itemId) {
		case 12783:
			return 500_000;
		case 12804:
			return 25_000_000;
		case 12851:
			return 10_000_000;
		case 12855:
		case 12856:
			return 2_500_000;
		case 12833:
			return 50_000_000;
		case 12831:
			return 35_000_000;
		case 12829:
			return 25_000_000;
		case 14484:
			return 125_000_000;

		case 12800:
		case 12802:
			return 350_000;
		case 12786:
			return 100_000;
		case 10926:
			return 2_500;
		case 12846:
			return 8_000_000;
		case 12420:
		case 12421:
		case 12419:
		case 12457:
		case 12458:
		case 12459:
			return 10_000_000;
		case 12757:
		case 12759:
		case 12761:
		case 12763:
		case 12788:
			return 500_000;
		case 12526:
			return 1_500_000;
		case 12773:
			return 5_000_000;
		case 12774:
			return 5_000_000;
		case 21003:
			return 30_000_000;
		case 21006:
			return 30_000_000;
		case 21015:
			return 30_000_000;
		case 19481:
			return 25_000_000;
		case 19478:
			return 20_000_000;
		case 12924:
			return 25_000_000;
		case 12024:
			return 20_000_000;
		case 13331:
			return 15_000_000;
		case 13335:
			return 15_000_000;
		case 13333:
			return 15_000_000;
		case 12849:
		case 12798:
			return 250_000;
		case 12608:
		case 12610:
		case 12612:
			return 350_000;
		case 12775:
		case 12776:
		case 12777:
		case 12778:
		case 12779:
		case 12780:
		case 12781:
		case 12782:
			return 5_000;

		default:
			return Integer.MAX_VALUE;
		}
	}
public int getItemValueHungerGames(int id) {
	switch(id) {
	case 12397:
		return 150;
	case 12393:
		return 100;
	case 12395:
		return 100;
	case 12439:
		return 100;
	case 12432:
		return 100;
	case 12430:
		return 100;
	case 12355:
		return 100;
	case 12337:
		return 100;
	case 12351:
		return 100;
	case 12441:
		return 50;
	case 12443:
		return 50;
	case 12335:
		return 50;
	case 12637:
		return 200;
	case 12639:
		return 200;
	case 12638:
		return 200;
	case 12540:
		return 30;
	}
	return 0;
}
	public int getSpecialItemValue(int id) {
		switch (id) {

		case 9244:
			return 0;
		case 9245:
			return 0;
		case 9243:
			return 0;
		case 890:
			return 0;
		case 892:
			return 0;
		case 11212:
			return 0;
		case 868:
			return 0;
		case 6522:
			return 0;
		case 2503:
			return 0;
		}
		return 0;
	}

	/**
	 * Sell item to shop (Shop Price)
	 **/
	public void sellToShopPrice(int removeId, int removeSlot) {
		if (c.myShopId == 22 || c.myShopId == 116 || c.myShopId == 9 || c.myShopId == 2 || c.myShopId == 88 || c.myShopId == 113 || c.myShopId == 3
				|| c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 16) {
				//c.sendMessage("@red@You can't sell items to this shop!");
			return;
		}
		for (int i : Config.ITEMS_KEPT_ON_DEATH) {
			if (i == removeId) {
				c.sendMessage("You can't sell " + c.getItems().getItemName(removeId).toLowerCase() + ".");
				return;
			}
		}

		boolean IsIn = false;
		if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
			for (int j = 0; j <= ShopHandler.ShopItemsStandard[c.myShopId]; j++) {
				if (removeId == (ShopHandler.ShopItems[c.myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		if (IsIn == false) {
			c.sendMessage("You can't sell " + c.getItems().getItemName(removeId).toLowerCase() + " to this store.");
		} else {
			int ShopValue = (int) Math.floor(getItemShopValue(removeId, 1, removeSlot));
			String ShopAdd = "";
			if (ShopValue >= 1000 && ShopValue < 1000000) {
				ShopAdd = " (" + (ShopValue / 1000) + "K)";
			} else if (ShopValue >= 1000000) {
				ShopAdd = " (" + (ShopValue / 1000000) + " million)";
			}
			if (c.myShopId == ConverterDialogue.SHOP_ID) {
				ItemDefinition def = ItemDefinition.forId(removeId);
				int specialValue = (int) (getSpecialItemValue(removeId) * 0.50);
				if (specialValue > 0) {
					c.sendMessage(c.getItems().getItemName(removeId) + ": shop will buy for " + specialValue
							+ " Coins.");
				} else {
					c.sendMessage(c.getItems().getItemName(removeId) + ": shop will buy for " + def.getHighAlchValue()
							+ " Coins");
				}
		
			} else if (c.myShopId == 99) {
				c.sendMessage(
						c.getItems().getItemName(removeId) + ": shop will buy for " + ShopValue + " Valuable blood.");
			} else if (c.myShopId == 26) {
				c.sendMessage(c.getItems().getItemName(removeId) + ": shop will buy for "
						+ ((int) Math.ceil((getSpecialItemValue(removeId)))) + " Coins.");
			} else if (c.myShopId == 29 || c.myShopId == 38 || c.myShopId == 40) {
			    c.sendMessage("@blu@"+c.getItems().getItemName(removeId) + ": @bla@shop will buy for @red@"
						+ ((int) Math.ceil((getPKPCost(removeId) * 0.50))) + " @bla@Pk Points.");
			} else if (c.myShopId == 111) { //pkp exchange
			    c.sendMessage("@blu@"+c.getItems().getItemName(removeId) + ": @bla@shop will buy for @red@"
						+ ((int) Math.ceil((getPKPCost(removeId) * 1))) + " @bla@Pk Points.");
                        } else {
				c.sendMessage(c.getItems().getItemName(removeId) + ": shop will buy for "
						+ ((int) Math.ceil((getSpecialItemValue(removeId) * 0.65))) + " Coins.");
			}
		}
	}

	int items[] = { 661, 662 };

	@SuppressWarnings("unused")
	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return false;
		}
		if (c.myShopId == 22 || c.myShopId == 116 || c.myShopId == 9 || c.myShopId == 2 || c.myShopId == 88 || c.myShopId == 113 || c.myShopId == 3
				|| c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 16) {
				c.sendMessage("@red@You can't sell items to this shop!");
			return false;
		}
		for (int i : Config.ITEMS_KEPT_ON_DEATH) {
			if (i == itemID) {
				c.sendMessage("You can't sell " + c.getItems().getItemName(itemID).toLowerCase() + ".");
				return false;
			}
		}
		if (c.myShopId == 115 || c.myShopId == 77 || c.myShopId == 14|| c.myShopId == 116 || c.myShopId == 117 || c.myShopId == 71 || c.myShopId == 78
				|| c.myShopId == 80 || c.myShopId == 44 || c.myShopId == 22 || c.myShopId == 66 || c.myShopId == 67
				|| c.myShopId == 56 || c.myShopId == 20 || c.myShopId == 210 || c.myShopId == 200 || c.myShopId == 68 || c.myShopId == 74 || c.myShopId == 72
				|| c.myShopId == 70 || c.myShopId == 69 || c.myShopId == 88) {
			c.sendMessage("@red@You can't sell items to this shop!");
			return false;
		}
		if (c.getRights().isAdministrator() && !Config.ADMIN_CAN_SELL_ITEMS) {
			c.sendMessage("Selling items as an admin has been disabled.");
			return false;
		}
		if (amount > 0 && itemID == (c.playerItems[fromSlot] - 1)) {
			if (ShopHandler.ShopSModifier[c.myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= ShopHandler.ShopItemsStandard[c.myShopId]; i++) {
					if (itemID == (ShopHandler.ShopItems[c.myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					c.sendMessage(
							"You can't sell " + c.getItems().getItemName(itemID).toLowerCase() + " to this store.");
					return false;
				}
			}
			if (amount > c.playerItemsN[fromSlot] && (ItemDefinition.forId((c.playerItems[fromSlot] - 1)).isNoted()
					|| ItemDefinition.forId((c.playerItems[fromSlot] - 1)).isStackable())) {
				amount = c.playerItemsN[fromSlot];
			} else if (amount > c.getItems().getItemAmount(itemID)
					&& !ItemDefinition.forId((c.playerItems[fromSlot] - 1)).isNoted()
					&& !ItemDefinition.forId((c.playerItems[fromSlot] - 1)).isStackable()) {
				amount = c.getItems().getItemAmount(itemID);
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			int TotPrice3 = 0;
			int TotPrice4 = 0;
			// int Overstock;
			if (c.myShopId == ConverterDialogue.SHOP_ID) {
				ItemDefinition def = ItemDefinition.forId(itemID);
				int rewardAmount = (int) (getSpecialItemValue(itemID) * 0.50);
				int rewardId = rewardAmount > 0 ? 995 : 995;
				if (rewardAmount <= 0) {
					rewardAmount = def.getHighAlchValue();
				}
				rewardAmount *= amount;
				if (!c.getItems().canAdd(rewardId, rewardAmount)) {
					c.sendMessage("You don't have enough space in your inventory.");
					return false;
				}
				c.getItems().deleteItem2(itemID, amount);
				c.getItems().addItem(rewardId, rewardAmount);
			} else {
				for (int i = amount; i > 0; i--) {
					TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 1, fromSlot));
					TotPrice3 = (int) Math.floor(getSpecialItemValue(itemID));
					TotPrice4 = (int) Math.floor(getSpecialItemValue(itemID));

					if (c.myShopId != 26) {
						TotPrice2 *= 1;
					}
					if (c.myShopId != 50 || c.myShopId != 12 || c.myShopId != 49 || c.myShopId != 26) {
						TotPrice2 *= 0.50;
					}
                                        if (c.myShopId == 29 || c.myShopId == 38 || c.myShopId == 111 || c.myShopId == 40) {
                                            TotPrice3 = ((int) Math.floor(getPKPCost(itemID)) / 2);
                                        }
//                                        if (c.myShopId == 111) {
//                                            TotPrice3 = ((int) Math.floor(getPKPCost(itemID)) / 1);
//                                        }
					if (TotPrice3 == 0 && c.myShopId != ConverterDialogue.SHOP_ID) {
						return false;
					}
//					if (TotPrice4 == 0 && c.myShopId != ConverterDialogue.SHOP_ID) {
//						return false;
//					}
					if (c.getItems().freeSlots() > 0 || c.getItems().playerHasItem(995)) {
						if (!ItemDefinition.forId(itemID).isNoted()) {
							c.getItems().deleteItem(itemID, c.getItems().getItemSlot(itemID), 1);
						} else {
							c.getItems().deleteItem(itemID, fromSlot, 1);
						}
						if (c.myShopId == 2 || c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 16 || c.myShopId == 20 || c.myShopId == 60 || c.myShopId == 61 || c.myShopId == 65) {
							c.getItems().addItem(995, (int) Math.ceil(TotPrice3 *= 0.50));
						} else if (c.myShopId == 26 || c.myShopId == 62 || c.myShopId == 63) {
							c.getItems().addItem(995, TotPrice3);
						} else if (c.myShopId == 29 || c.myShopId == 38 || c.myShopId == 111 || c.myShopId == 40) {
                                                        c.pkp += TotPrice3;
                                                        c.sendMessage("Sold for @red@"+TotPrice3+" @bla@Pk Points.");
                                                        c.getPA().sendFrame126("@or1@Pk Point Shop - @or2@(PKP: @or1@" + c.pkp + "@or2@)", 3901);
//						} else if (c.myShopId == 111) {
//							                            c.pkp += TotPrice3;
//							                            c.sendMessage("Sold for @red@"+TotPrice3+" @bla@Pk Points.");
//							                            c.getPA().sendFrame126("@or1@Pk Point Shop - @or2@(PKP: @or1@" + c.pkp + "@or2@)", 3901);
                                                } else {
							break;
						}
					}
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return true;
	}

	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}
		if (ItemDefinition.forId(itemID).isNoted()) {
			itemID = c.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
			if ((ShopHandler.ShopItems[c.myShopId][i] - 1) == itemID) {
				ShopHandler.ShopItemsN[c.myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < ShopHandler.ShopItems.length; i++) {
				if (ShopHandler.ShopItems[c.myShopId][i] == 0) {
					ShopHandler.ShopItems[c.myShopId][i] = (itemID + 1);
					ShopHandler.ShopItemsN[c.myShopId][i] = amount;
					ShopHandler.ShopItemsDelay[c.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}

	public boolean buyItem(int itemID, int fromSlot, int amount) {
		if (Server.getMultiplayerSessionListener().inAnySession(c))
			return false;
		for (int i : Config.INFINITY) {
			if (i == itemID) {
				c.getItems().addItem(itemID, amount);
				c.getItems().resetItems(3823);
				resetShop(c.myShopId);
				updatePlayerShop();
				return false;
			} 
		}
		if (!shopSellsItem(itemID)) {
			c.sendMessage("Stop trying to cheat!");
			return false;
		}
		if (c.myShopId == 14) {
			skillBuy(itemID);
			return false;
			
		} else if (c.myShopId == 15) {
			buyVoid(itemID);
			return false;
		}
		
		if (!shopSellsItem(itemID)) {
			c.sendMessage("Stop trying to cheat!");
			return false;
		}
		if (amount > 0) {
			if (amount > ShopHandler.ShopItemsN[c.myShopId][fromSlot] && c.myShopId != 29) {
				amount = ShopHandler.ShopItemsN[c.myShopId][fromSlot];
			}
			if (c.myShopId == 22 || c.myShopId == 20 || c.myShopId == 2 || c.myShopId == 88 || c.myShopId == 113 || c.myShopId == 3
					|| c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 16) {
					if (c.getItems().freeSlots() > 0) {
						c.getPA().loadQuests();
						c.getItems().addItem(itemID, amount);
						c.getItems().resetItems(3823);
				}
				return false;
			}
			// double ShopValue;
			// double TotPrice;
			int TotPrice2 = 0;
			// int Overstock;
			int Slot = 0;
			int Slot1 = 0;// Tokkul
			int Slot2 = 0; // blood
			int Slot3 = 0; // grace
			int Slot4 = 0; // redwood
			if (c.myShopId == 80) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 29 || c.myShopId == 38 || c.myShopId == 81 || c.myShopId == 56 || c.myShopId == 111 || c.myShopId == 40) {
				handleOtherShop(itemID, amount);
				return false;
			}
		    if (c.myShopId == 81) {
			handleOtherShop(itemID, amount);
			return false;
		    }  
			if (c.myShopId == 3 || c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 6 || c.myShopId == 8
					|| c.myShopId == 47 || c.myShopId == 48 || c.myShopId == 113) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 44 || c.myShopId == 68 || c.myShopId == 74 || c.myShopId == 72) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 79) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 9) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 116 || c.myShopId == 117) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 71) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 10) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 77) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 115) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 78) {
				handleOtherShop(itemID, amount);
				return false;
			}
			if (c.myShopId == 75) {
				handleOtherShop(itemID, amount);
				return false;
			}
			for (int i = amount; i > 0; i--) {
				Slot1 = c.getItems().getItemSlot(6529);
				Slot2 = c.getItems().getItemSlot(8125);
				Slot3 = c.getItems().getItemSlot(11849);
				Slot4 = c.getItems().getItemSlot(19670);
				if (Slot == -1 && c.myShopId != 29 && c.myShopId != 75 && c.myShopId != 99 && c.myShopId != 200 && c.myShopId != 210 && c.myShopId != 111 && c.myShopId != 40) {
					c.sendMessage("You don't have enough Coins.");
					break;
				}
				if (Slot2 == -1 && (c.myShopId == 99)) {
					c.sendMessage("You don't have enough Valuable blood.");
					break;
				}
				if (Slot3 == -1 && (c.myShopId == 200)) {
					c.sendMessage("You don't have enough Marks of Grace.");
					break;
				}
				if (Slot4 == -1 && (c.myShopId == 210)) {
					c.sendMessage("You don't have enough Redwood Logs.");
					break;
				}
				if (TotPrice2 <= 1) {
					TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 0, fromSlot));
					TotPrice2 *= 1.66;
				}
				if (c.myShopId != 29 & c.myShopId != 75 && c.myShopId != 99 && c.myShopId != 200 && c.myShopId != 210 && c.myShopId != 111 && c.myShopId != 40) {
					if (c.playerItemsN[Slot] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					}
				}
				/*
				 * else if (c.myShopId == 68) {
			if (c.fishingTourneyPoints >= getSpecialItemValue(itemID) * amount) {
				if (c.getItems().freeSlots() > 0) {
					c.fishingTourneyPoints -= getSpecialItemValue(itemID) * amount;
					c.getPA().loadQuests();
					c.getItems().addItem(itemID, amount);
					c.getItems().resetItems(3823);
					c.getPA().sendFrame126("Fishing Tourney Shop - Points: " + c.fishingTourneyPoints, 3901);
				}
				 */
				if (c.myShopId == 200) {
					if (c.playerItemsN[Slot3] >= GracePrice(itemID)) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(11849, c.getItems().getItemSlot(11849), GracePrice(itemID));
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough Marks of Grace.");
						break;
					}
				}
				if (c.myShopId == 210) {
					if (c.playerItemsN[Slot4] >= Redwood(itemID)) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(19670, c.getItems().getItemSlot(19670), Redwood(itemID));
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough Marks of Grace.");
						break;
					}
				}
				if (c.myShopId == 99) {
					if (c.playerItemsN[Slot2] >= TotPrice2) {
						if (c.getItems().freeSlots() > 0) {
							c.getItems().deleteItem(8125, c.getItems().getItemSlot(8125), TotPrice2);
							c.getItems().addItem(itemID, 1);
							ShopHandler.ShopItemsN[c.myShopId][fromSlot] -= 1;
							ShopHandler.ShopItemsDelay[c.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > ShopHandler.ShopItemsStandard[c.myShopId]) {
								ShopHandler.ShopItems[c.myShopId][fromSlot] = 0;
							}
						} else {
							c.sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						c.sendMessage("You don't have enough Valuable blood.");
						break;
					}
				}
			}
			c.getItems().resetItems(3823);
			resetShop(c.myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}
	
	/**
	 * Prices for Marks of Grace Shop
	 * @param itemID
	 * @return
	 */

	private int GracePrice(int itemID) {
		switch(itemID){
		case 11850:
		case 11854:
		case 11856:
		case 11858:
		case 11860:
		case 11852:
			return 25;
		case 11738:
			return 15;
		}
		return 0;
	}
	
	/**
	 * Prices for Redwood Logs Shop
	 * @param itemID
	 * @return
	 */
	
	private int Redwood(int itemID) {
		switch(itemID){
		case 6739:
			return 500;
		case 20011:
			return 1000;
		}
		return 0;
	}
	
	public int getPKPCost(int itemID) {
		switch(itemID){
                    case 12934:
                        return 1;
		//Pk Point Shop 1
		case 19547:
			return 8000;
		case 19550:
			return 8000;
		case 21015:
			return 80000;
		case 19478:
			return 25000;
		case 19481:
			return 150000;
		case 20770:
			return 125000;
		case 20771:
			return 200000;
		case 20772:
			return 200000;
		case 12825:
			return 130000;
		case 12817:
			return 180000;
		case 11802:
			return 12000;
		case 11785:
			return 10000;
		case 12929:
			return 8000;
		case 11791:
			return 5000;
		case 13652:
			return 30000;
		case 12954:
			return 2500;
		case 10548:
			return 250;
		case 10551:
			return 750;
		case 11770:
			return 40;
		case 11771:
			return 40;
		case 11772:
			return 40;
		case 11773:
			return 80;
		case 12831:
			return 100;
		case 12829:
			return 60;
		case 11283:
			return 80;
		case 6889:
			return 500;
		case 6914:
			return 1500;
		case 13858:
			return 100;
		case 13861:
			return 100;
		case 13864:
			return 100;
		case 13896:
			return 250;
		case 13890:
			return 500;
		case 13884:
			return 500;
		case 6570:
			return 1000;
		case 13876:
			return 100;
		case 13870:
			return 100;
		case 13873:
			return 100;
		case 13867:
			return 100;
		case 13899:
			return 100;
		case 13905:
			return 80;
		case 3144:
			return 3;
		case 12695:
			return 4;
		case 13237:
			return 3000;
		case 13239:
			return 3000;
		case 13235:
			return 3000;
		case 12791:
			return 2500;
		case 10887:
			return 500;
		case 4153:
			return 3000;
		case 2996:
			return 1;
		case 11826:
			return 600;
		case 6916:
		case 6924:
		return 300;
		case 6918:
			return 150;
		case 6920:
			return 150;
		case 6922:
			return 50;
		case 11828:
		case 11830:
			return 2000;
		case 11832:
		case 11834:
			return 1000;
		case 11836:
			return 500;		
		case 11235:
			return 3000;
		case 12924:
			return 100000;
		case 11924:
			return 1000;
		case 11926:
			return 3000;
		case 15318:
            return 0;
		case 536:
			return 25;
		case 11943:
			return 70;
		case 1753:
			return 25;
		case 1747:
			return 75;
		case 13441:
			return 6;
		case 4745:
		case 4746:
		case 4747:
		case 4748:
		case 4749:
		case 4750:
		case 4751:
		case 4752:
		case 4753:
		case 4754:
		case 4755:
		case 4756:
		case 4757:
		case 4758:
		case 4759:
		case 4760:
		case 4708:
		case 4709:
		case 4710:
		case 4711:
		case 4712:
		case 4713:
		case 4714:
		case 4715:
		case 4716:
		case 4717:
		case 4718:
		case 4719:
		case 4720:
		case 4721:
		case 4722:
		case 4723:
		case 4724:
		case 4725:
		case 4726:
		case 4727:
		case 4728:
		case 4729:
		case 4730:
		case 4731:
		case 4732:
		case 4733:
		case 4734:
		case 4735:
		case 4736:
		case 4737:
		case 4738:
		case 4739:
			return 350;
		}
		return 0;
	}

	public void handleOtherShop(int itemID, int amount) {
		if (c.myShopId == 80) {
			if (c.getItems().freeSlots() < 1) {
				c.sendMessage("You need atleast one free slot to buy this.");
				return;
			}
			int itemValue = getBountyHunterItemCost(itemID);
			if (c.getBH().getBounties() < itemValue) {
				c.sendMessage("You do not have enough bounties to buy this from the shop.");
				return;
			}
			c.getBH().setBounties(c.getBH().getBounties() - itemValue);
			c.getItems().addItem(itemID,1);	
			c.getItems().resetItems(3823);
			c.getPA().sendFrame126("Bounties: " + Misc.insertCommas(Integer.toString(c.getBH().getBounties())), 28052);
			return;
		}
		if (c.myShopId == 12 && itemID == 2437 || itemID == 2441 || itemID == 2443) {
			int itemValue = getSpecialItemValue(itemID);
			if (c.getItems().getItemCount(995) < itemValue) {
				c.sendMessage("You do not have enough coins to buy this item.");
				return;
			}
			c.getItems().deleteItem(995, itemValue);
			c.getItems().addItem(itemID,1);	
			c.getItems().resetItems(3823);
			return;
		}
                
                
                
                
                
                
		if (c.myShopId == 29 || c.myShopId == 38 || c.myShopId == 111 || c.myShopId == 40) {
                    if (c.getItems().freeSlots() == 0) {
                        c.sendMessage("No free inventory space.");
                        return;
                    }
			if (amount > c.getItems().freeSlots()) {
                            if (!ItemDefinition.forId(itemID).isStackable()) {
                                amount = c.getItems().freeSlots();
                                c.sendMessage("Not enough free slots, buying "+amount+" instead.");
                            }
			}
                        if (amount < 1) {
                            return;
                        }
                        long l = (long)(getPKPCost(itemID) * amount);
                        if (l >= Integer.MAX_VALUE) {
                            amount = Integer.MAX_VALUE / getPKPCost(itemID);
                        }
                        if (c.pkp < (getPKPCost(itemID) * amount)) {
                            amount = c.pkp / getPKPCost(itemID);
                        }
                        if (amount < 1) {
                            c.sendMessage("You do not have enough PK Points to buy this item.");
                            return;
                        }
			c.pkp -= (getPKPCost(itemID) * amount);
			c.getPA().loadQuests();
                        if (itemID == 12934)
                            c.getItems().addItem(itemID, amount * 4);	
                        else
                            c.getItems().addItem(itemID, amount);
			c.getItems().resetItems(3823);
                        c.getPA().sendFrame126("@or1@Pk Point Shop - @or2@(PKP: @or1@" + c.pkp + "@or2@)", 3901);
		} 
                
                
                
                
                
                else if (c.myShopId == 3 || c.myShopId == 4 || c.myShopId == 5 || c.myShopId == 6 || c.myShopId == 8
				|| c.myShopId == 47 || c.myShopId == 48 || c.myShopId == 113) {
			if (c.getItems().getItemCount(995) >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.getItems().deleteItem(995, getSpecialItemValue(itemID));
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("Purchase failed.");
			}
			
		} else if (c.myShopId == 68) {
			if (c.fishingTourneyPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.fishingTourneyPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
					c.getPA().sendFrame126("Fishing Tourney Shop - Points: " + c.fishingTourneyPoints, 3901);
				}
			} else {
				c.sendMessage("You do not have enough fishing tourney points to buy this item.");
			}
		} else if (c.myShopId == 74) {
			if (c.hungerPoints >= getItemValueHungerGames(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.hungerPoints -= getItemValueHungerGames(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
					c.getPA().sendFrame126("@or1@Sacrifice Shop - @or2@(SFP: @or1@" + c.hungerPoints + "@or2@)", 3901);
				}
			} else {
				c.sendMessage("You do not have enough Sacrifice Points to buy this item.");
			}
		} else if (c.myShopId == 72) {
			if (c.blastPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.blastPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
					c.getPA().sendFrame126("Blast Mine Shop - Points: " + c.blastPoints, 3901);
				}
			} else {
				c.sendMessage("You do not have enough blast points to buy this item.");
			}
		} else if (c.myShopId == 44) {
			if (c.slayerPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.slayerPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
					c.getPA().sendFrame126("@whi@Slayer Points: @gre@" + c.slayerPoints + " ", 7333);
				}
			} else {
				c.sendMessage("You do not have enough slayer points to buy this item.");
			}
		} else if (c.myShopId == 9) {
			if (c.donatorPoints >= donatorValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.donatorPoints -= donatorValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough donator points to buy this item.");
			}
		} else if (c.myShopId == 116 || c.myShopId == 117) {
			if (c.donatorPoints >= donatorValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.donatorPoints -= donatorValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough donator points to buy this item.");
			}
		} else if (c.myShopId == 71) {
			if (c.donatorPoints >= donatorValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.donatorPoints -= donatorValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough donator points to buy this item.");
			}
		} else if (c.myShopId == 81) {
			if (c.donatorPoints >= getDonatorValue(itemID)) {
				if (c.getItems().freeSlots() > 0){
					c.donatorPoints -= getDonatorValue(itemID);
					c.getItems().addItem(itemID,1);
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough Donator points to buy this item.");			
			}
		} else if (c.myShopId == 10) {
			if (c.slayerPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.slayerPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough vote points to buy this item.");
			}
		} else if (c.myShopId == 75) {
			if (c.pcPoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.pcPoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough PC Points to buy this item.");
			}
		} else if (c.myShopId == 77) {
			if (c.votePoints >= getVoteItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.votePoints -= getVoteItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
					c.getPA().sendFrame126("@or1@Vote Shop - @or2@(VP: @or1@" + c.votePoints + "@or2@)", 3901);
				}
			} else {
				c.sendMessage("You do not have enough vote points to buy this item.");
			}
		} else if (c.myShopId == 115) {
			if (c.votePoints >= getSpecialItemValue(itemID)) {
				if (c.getItems().freeSlots() > 0) {
					c.votePoints -= getSpecialItemValue(itemID);
					c.getPA().loadQuests();
					c.getItems().addItem(itemID,1);	
					c.getItems().resetItems(3823);
				}
			} else {
				c.sendMessage("You do not have enough vote points to buy this item.");
			}
		}
	}

	public void openSkillCape() {
		int capes = get99Count();
		if (capes > 1)
			capes = 1;
		else
			capes = 0;
		c.myShopId = 14;
		setupSkillCapes(capes, get99Count());
	}

	/*
	 * public int[][] skillCapes =
	 * {{0,9747,4319,2679},{1,2683,4329,2685},{2,2680
	 * ,4359,2682},{3,2701,4341,2703
	 * },{4,2686,4351,2688},{5,2689,4347,2691},{6,2692,4343,2691},
	 * {7,2737,4325,2733
	 * },{8,2734,4353,2736},{9,2716,4337,2718},{10,2728,4335,2730
	 * },{11,2695,4321,2697},{12,2713,4327,2715},{13,2725,4357,2727},
	 * {14,2722,4345
	 * ,2724},{15,2707,4339,2709},{16,2704,4317,2706},{17,2710,4361,
	 * 2712},{18,2719,4355,2721},{19,2737,4331,2739},{20,2698,4333,2700}};
	 */
	public int[] skillCapes = { 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795,
			9792, 9774, 9771, 9777, 9786, 9810, 9765, 9789, 9948 };

	public int get99Count() {
		int count = 0;
		for (int j = 0; j < c.playerLevel.length; j++) {
			if (Player.getLevelForXP(c.playerXP[j]) >= 99) {
				count++;
			}
		}
		return count;
	}

	public void setupSkillCapes(int capes, int capes2) {
		c.getPA().sendFrame171(1, 28050);
		c.getItems().resetItems(3823);
		c.isShopping = true;
		c.myShopId = 14;
		c.getPA().sendFrame248(3824, 3822);
		c.getPA().sendFrame126("Skillcape Shop", 3901);

		int TotalItems = 0;
		TotalItems = capes2;
		if (TotalItems > ShopHandler.MaxShopItems) {
			TotalItems = ShopHandler.MaxShopItems;
		}
		c.getOutStream().createFrameVarSizeWord(53);
		c.getOutStream().writeWord(3900);
		c.getOutStream().writeWord(TotalItems);
		for (int i = 0; i <= 22; i++) {
			if (Player.getLevelForXP(c.playerXP[i]) < 99)
				continue;
			if (skillCapes[i] == -1)
				continue;
			c.getOutStream().writeByte(1);
			c.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
		}
		c.getOutStream().endFrameVarSizeWord();
		c.flushOutStream();
		// }
	}

	public void skillBuy(int item) {
		int nn = get99Count();
		if (nn > 1)
			nn = 1;
		else
			nn = 0;
		for (int j = 0; j < skillCapes.length; j++) {
			if (skillCapes[j] == item || skillCapes[j] + 1 == item) {
				if (c.getItems().freeSlots() > 1) {
					if (c.getItems().playerHasItem(995, 99000)) {
						if (Player.getLevelForXP(c.playerXP[j]) >= 99) {
							c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 99000);
							c.getItems().addItem(skillCapes[j] + nn, 1);
							c.getItems().addItem(skillCapes[j] + 2, 1);
						} else {
							c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
						}
					} else {
						c.sendMessage("You need 99,000 coins to buy this item.");
					}
				} else {
					c.sendMessage("You must have at least 1 inventory spaces to buy this item.");
				}
			}
		}
		c.getItems().resetItems(3823);
	}

	public void openVoid() {
	}

	public void buyVoid(int item) {
	}

}
