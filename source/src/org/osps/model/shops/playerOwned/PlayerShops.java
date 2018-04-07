package org.osps.model.shops.playerOwned;

import java.math.BigInteger;

import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;


public class PlayerShops {
    
    public static void buy(Player c, int slot, int amount) {
        Shop s = c.shopOpened;
        if (ownerOnline(s)) {
            if (c.getItems().freeSlots() < 1) {
                c.sendMessage("No free inventory space.");
                return;
            }
            if (amount > 0) {
                if (amount >= s.amounts[slot]) {
                    amount = s.amounts[slot];
                }
                int totalCost = amount * s.prices[slot];
                if (s == c.shop())
                    totalCost = 0;
                if (totalCost > c.pkp) {
                    amount = c.pkp / s.prices[slot];
                }
                if (amount < 1)
                    return;
                if (!ItemDefinition.forId(s.items[slot]).isStackable()) {
                    if (amount > c.getItems().freeSlots()) {
                        amount = c.getItems().freeSlots();
                    }
                }
                totalCost = amount * s.prices[slot];
                c.pkp -= totalCost;
                s.getOwner().pkp += totalCost;
                s.amounts[slot] -= amount;
                if (c.shop() == s) {
                    c.sendMessage("You reclaim some items from your shop.");
                } else {
                    s.getOwner().sendMessage(""+c.playerName+" has purchased items from your store. You now have: "+s.getOwner().pkp+" Pk Points.");
                }
                c.getItems().addItem(s.items[slot], amount);
                if (s.amounts[slot] <= 0) {
                    s.amounts[slot] = 0;
                    s.items[slot] = 0;
                    s.prices[slot] = 0;
                }
                openShop(c, s.getOwner());
            }
        } else {
            c.sendMessage("The shop owner is now offline.");
        }
    }
    
    public static void value(Player c, int slot) {
        Shop s = c.shopOpened;
        if (ownerOnline(s)) {
            String item = ItemDefinition.forId(s.items[slot]).getName();
            c.sendMessage("@blu@"+s.getOwner().playerName+"@bla@ is selling @blu@"+s.amounts[slot]+"@bla@ X @blu@"+item+" @bla@for @blu@"+getConvertedValueBlue(s.prices[slot])+"@bla@ Pk points each.");
        } else {
            c.sendMessage("The shop owner is now offline.");
        }
    }
    
    public static void openShop(Player c, Player o) {
        openShop(c, o.playerName);
    }
    
    public static void openShop(Player c, String playerName) {
        Player target = PlayerHandler.getPlayer(playerName);
        if (target != null) {
            c.getPA().showInterface(44800);
            c.getPA().requestUpdates();
            c.getPA().loadQuests();
            c.getPA().sendFrame126("@lre@Viewing Shop of: @gre@"+playerName, 44802);
            c.getPA().sendFrame126("You currently have : "+c.pkp+" Pk Points", 44840);
            c.shopOpened = target.shop();
            for (int i = 0; i < 30; i++) {
                if (target.shop().items[i] > 0) {
                    c.getPA().sendFrame34a(44803, target.shop().items[i], i, target.shop().amounts[i]);
                    c.getPA().sendFrame126(""+getConvertedValue(target.shop().prices[i])+" PkP", 44805 + i);
                } else {
                    c.getPA().sendFrame34a(44803, -1, i, 1);
                    c.getPA().sendFrame126(" ", 44805 + i);
                }
            }
        } else {
            c.sendMessage("This player is offline or doesn't exist, shop could not be found.");
        }
    }
    
    public static void openMainInterface(Player c) {
        c.getPA().showInterface(44000);
        c.getPA().requestUpdates();
        c.getPA().loadQuests();
        int b = 0;
        for (int i = 0; i < PlayerHandler.players.length; i++) {
            Player d = PlayerHandler.players[i];
            if (d != null) {
                c.getPA().sendFrame126(d.playerName, 44700 + b);
                b++;
            } else {
                if (i < 100)
                c.getPA().sendFrame126("", 44700 + i);
            }
        }
        b = 0;
        for (int i = 0; i < PlayerHandler.players.length; i++) {
            Player d = PlayerHandler.players[i];
            if (d != null) {
                if (d.shop().getDisplayedItemSlot() != -1) {
                    c.getPA().sendFrame34a(44300 + b, d.shop().items[d.shop().getDisplayedItemSlot()], 0, d.shop().amounts[d.shop().getDisplayedItemSlot()]);
                    c.getPA().sendFrame126(d.playerName, 44400 + b);
                    c.getPA().sendFrame126(""+d.shop().getTotalItems(), 44500 + b);
                    c.getPA().sendFrame126(""+getConvertedValue(d.shop().getTotalValue()), 44600 + b);
                    b++;
                } else if (d.shop().getDisplayedItemSlot() == -1 || !ownerOnline(d.shop())) {
                    c.getPA().sendFrame34a(44300 + b, -1, 0, 1);
                    c.getPA().sendFrame126(" ", 44400 + b);
                    c.getPA().sendFrame126(" ", 44500 + b);
                    c.getPA().sendFrame126(" ", 44600 + b);
                }
            }
        }
    }
    
    public static void selectForSale(Player c, int slot) {
        int item = c.playerItems[slot] - 1;
        c.shopSellItem = item;
        c.getPA().sendFrame34a(44853, item, 0, 1);
    }
    
    public static void confirmSale(Player c) {
        if (c.shop().freeSlot() != -1) {
            int slot = c.shop().freeSlot();
            boolean in = false;
            if (c.shop().findSlot(c.shopSellItem, c.shopSellPrice) > 0) {
                slot = c.shop().findSlot(c.shopSellItem, c.shopSellPrice);
                in = true;
            }
            if (c.shopSellAmount > 0 && c.shopSellPrice > 0 && c.shopSellItem > 0) {
                int recPkp = getRecommendedPkpValue(c.shopSellItem);
                if (recPkp < 0) {
                    c.sendMessage("This item does not have any value in Pk points, and can't be sold.");
                    return;
                }
                if (c.shop().getTotalValue() + (c.shopSellPrice * c.shopSellAmount) >= 10000000) {
                    c.sendMessage("Your total shop value must be under 10,000,000 Pk Points");
                    return;
                }
                if (!c.getItems().playerHasItem(c.shopSellItem, c.shopSellAmount)) {
                    c.shopSellAmount = c.getItems().getItemAmount(c.shopSellItem);
                }
                if (ItemDefinition.forId(c.shopSellItem).isStackable() || ItemDefinition.forId(c.shopSellItem).isNoted()) {
                    c.getItems().deleteItem(c.shopSellItem, c.shopSellAmount);
                } else {
                    c.getItems().deleteItemS(c.shopSellItem, c.shopSellAmount);
                }
                if (in) {
                    c.shop().amounts[slot] += c.shopSellAmount;
                } else {
                    c.shop().amounts[slot] = c.shopSellAmount;
                    c.shop().items[slot] = c.shopSellItem;
                    c.shop().prices[slot] = c.shopSellPrice;
                }
                    openShop(c, c.playerName);
            }
        }
    }
    
    public static void openSellInterface(Player c) {
        c.getPA().sendFrame34a(44853, -1, 0, 1);
        c.getPA().showInterface(44850);
        c.getPA().requestUpdates();
        c.getPA().loadQuests();
    }
    
    public static boolean ownerOnline(Shop s) {
        for (int i = 0; i < PlayerHandler.players.length; i++) {
            if (PlayerHandler.players[i] != null) {
                if (PlayerHandler.players[i] == s.getOwner())
                    return true;
            }
        }
        return false;
    }
    
    
    
    
    public static String getConvertedValue(int value) {
	int co = -1;
	int t = -1;
	int m = -1;
	int b = -1;
	BigInteger g = new BigInteger(""+value);
	BigInteger f = g;
	String s = "";
	s = ""+f+"";
	try {
		co = Integer.parseInt(s);
	} catch (Exception e) {
		co = -1;
	}
	f = f.divide(new BigInteger("1000"));
	s = ""+f+"";
	try {
		t = Integer.parseInt(s);
	} catch (Exception e) {
		t = -1;
	}
	f = g;
	f = f.divide(new BigInteger("1000000"));
	s = ""+f+"";
	try {
		m = Integer.parseInt(s);
	} catch (Exception e) {
		m = -1;
	}
	f = g;
	f = f.divide(new BigInteger("1000000000"));
	s = ""+f+"";
	try {
		b = Integer.parseInt(s);
	} catch (Exception e) {
		b = -1;
	}
	String e = "";
	if (t != -1 && co != -1 && co < 10000 && t < 10) {
		e = "@yel@"+co+"";
	} else if (t != -1 && co >= 10000 && t < 10000) {
		e = "@whi@"+t+"K";
	} else if (m != -1 && t >= 10000 && m < 10000) {
		e = "@gre@"+m+"M";
	} else {
		e = "@cya@"+b+"B";
	}
	return e;
    }
    
    public static String getConvertedValueBlue(int value) {
    	int co = -1;
    	int t = -1;
    	int m = -1;
    	int b = -1;
    	BigInteger g = new BigInteger(""+value);
    	BigInteger f = g;
    	String s = "";
    	s = ""+f+"";
    	try {
    		co = Integer.parseInt(s);
    	} catch (Exception e) {
    		co = -1;
    	}
    	f = f.divide(new BigInteger("1000"));
    	s = ""+f+"";
    	try {
    		t = Integer.parseInt(s);
    	} catch (Exception e) {
    		t = -1;
    	}
    	f = g;
    	f = f.divide(new BigInteger("1000000"));
    	s = ""+f+"";
    	try {
    		m = Integer.parseInt(s);
    	} catch (Exception e) {
    		m = -1;
    	}
    	f = g;
    	f = f.divide(new BigInteger("1000000000"));
    	s = ""+f+"";
    	try {
    		b = Integer.parseInt(s);
    	} catch (Exception e) {
    		b = -1;
    	}
    	String e = "";
    	if (t != -1 && co != -1 && co < 10000 && t < 10) {
    		e = "@blu@"+co+"";
    	} else if (t != -1 && co >= 10000 && t < 10000) {
    		e = "@blu@"+t+"K";
    	} else if (m != -1 && t >= 10000 && m < 10000) {
    		e = "@blu@"+m+"M";
    	} else {
    		e = "@blu@"+b+"B";
    	}
    	return e;
        }
    
    
    
    
    
    /**
     * Add item and a value for it to be able to add to shop
     * Players are allowed to sell the item for up to 10x what this recommended value is
     */
    public static int getRecommendedPkpValue(int item) {
        switch(item) {
           
            //Custom Items
        case 15342:
             return 1;
        case 15320:
        	 return 1;
        case 15322:
       	     return 1;
        case 15324:
       	     return 1;
        case 15327:
       	     return 1;
        case 15330:
       	     return 1;
        case 15333:
       	     return 1;
        case 15336:
       	     return 1;
        case 15339:
        	 return 1;
        case 15345:
       	     return 1;
        case 1050:
        	 return 1;
        case 1038:
        	 return 1;
        case 1040:
        	 return 1;
        case 1042:
        	 return 1;
        case 1044:
        	 return 1;
        case 1046:
        	 return 1;
        case 1048:
        	 return 1;
        case 11863:
        	 return 1;
        case 12399:
        	 return 1;
        case 15326:
        	 return 1;
        case 15329:
        	 return 1;
        case 15332:
        	 return 1;
        case 15335:
        	 return 1;
        case 15538:
        	 return 1;
        case 15341:
        	 return 1;
        case 15343:
        	 return 1;
        case 15346:
        	 return 1;
        case 1053:
        	 return 1;
        case 1055:
        	 return 1;
        case 1057:
        	 return 1;
        case 15321:
        	 return 1;
        case 15323:
        	 return 1;
        case 15325:
        	 return 1;
        case 15328:
        	 return 1;
        case 15331:
        	 return 1;
        case 15334:
        	 return 1;
        case 15337:
        	 return 1;
        case 15340:
        	 return 1;
        case 15344:
        	 return 1;
        case 15347:
        	 return 1;
        case 15348:
        	 return 1;
        case 15349:
        	 return 1;
        case 15350:
        	 return 1;
        case 15351:
        	 return 1;
        case 15352:
        	 return 1;
        case 13887:
        	 return 1;
        case 13893:
        	 return 1;
        case 13510:
        	 return 1;
        case 15311:
        	 return 1;
        case 15312:
        	 return 1;
        case 15313:
        	 return 1;
        case 15314:
        	 return 1;
        case 21003:
        	 return 1;
        case 21015:
        	 return 1;
        case 21006:
        	 return 1;
        case 12924:
        	 return 1;
        case 19481:
        	 return 1;
        case 12825:
             return 1;
        case 12817:
        	 return 1;
        case 15001:
        	 return 1;
        case 128201:
        	 return 1;
            //End of Custom Items

           //PKP Shop Items
        case 12806:
        	 return 1;
        case 12807:
        	 return 1;
        case 12934:
            return 1;
		case 11802:
			return 1;
		case 11785:
			return 1;
		case 12929:
			return 1;
		case 11791:
			return 1;
		case 13652:
			return 1;
		case 12954:
			return 1;
		case 10548:
			return 1;
		case 536:
			return 1;
		case 11943:
			return 1;
		case 1753:
			return 1;
		case 1747:
			return 1;
		case 10551:
			return 1;
		case 11770:
			return 1;
		case 11771:
			return 1;
		case 11772:
			return 1;
		case 11773:
			return 1;
		case 12831:
			return 1;
		case 12829:
			return 1;
		case 11283:
			return 1;
		case 6889:
			return 1;
		case 6914:
			return 1;
		case 13858:
			return 1;
		case 13861:
			return 1;
		case 13864:
			return 1;
		case 13896:
			return 1;
		case 13890:
			return 1;
		case 13884:
			return 1;
		case 6570:
			return 1;
		case 13876:
			return 1;
		case 13870:
			return 1;
		case 13873:
			return 1;
		case 13867:
			return 1;
		case 13899:
			return 1;
		case 13905:
			return 1;
		case 3144:
			return 1;
		case 12695:
			return 1;
		case 13237:
			return 1;
		case 13239:
			return 1;
		case 13235:
			return 1;
		case 12791:
			return 1;
		case 10887:
			return 1;
		case 2996:
			return 1;
		case 11235:
			return 1;
			//Pk Point Shop 2
		case 11924:
			return 1;
		case 11926:
			return 1;
		case 12902:
			 return 1;
            //End of Pkp Items
			
			//Vote Shop
		case 1037:
			return 1;
		case 15338:
			return 1;
		case 12424:
			return 1;
		case 12422:
			return 1;
		case 12426:
			return 1;
		case 13124:
			return 1;
		case 12006:
			return 1;
		case 12002:
			return 1;
		case 13072:
			return 1;
		case 13073:
			return 1;
		case 10350:
			return 1;
		case 10348:
			return 1;
		case 10346:
			return 1;
		case 10352:
			return 1;
		case 10334:
			return 1;
		case 10330:
			return 1;
		case 10332:
			return 1;
		case 10336:
			return 1;
		case 10342:
			return 1;
		case 20576:
			return 1;
		case 20577:
			return 1;
		case 10344:
			return 1;
		case 12436:
			return 1;
		case 13200:
			return 1;
		case 13201:
			return 1;
		case 12802:
			return 1;
			//End Vote shop.
        }
        return -1;
    }
    
    
}
