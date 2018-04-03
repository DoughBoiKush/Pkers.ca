package org.osps.model.shops.playerOwned;

import org.osps.model.players.Player;

public class Shop {
    
    public Shop(Player c) {
        this.c = c;
    }
    private Player c;
    
    public int[] items = new int[30];
    public int[] amounts = new int[30];
    public int[] prices = new int[30];
    
    public Player getOwner() {
        return c;
    }
    
    public int findSlot(int item, int price) {
        for (int i = 0; i < 30; i++) {
            if (items[i] == item && prices[i] == price) {
                return i;
            }
        }
        return -1;
    }
    
    public int freeSlot() {
        for (int i = 0; i < 30; i++) {
            if (items[i] <= 0)
                return i;
        }
        return -1;
    }
    
    public int getTotalValue() {
        int value = 0;
        for (int i = 0; i < 30; i++) {
            if (items[i] > 0) {
                value += amounts[i] * prices[i];
            }
        }
        return value;
    }
    
    public int getTotalItems() {
        int value = 0;
        for (int i = 0; i < 30; i++) {
            if (items[i] > 0) {
                value += amounts[i];
            }
        }
        return value;
    }
    
    
    public int getDisplayedItemSlot() {
        for (int i = 0; i < 30; i++) {
            if (items[i] > 0)
                return i;
        }
        return -1;
    }
}
