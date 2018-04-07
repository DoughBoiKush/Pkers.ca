package org.osps.world;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import org.json.simple.ItemList;
import org.osps.Config;
import org.osps.model.items.GroundItem;
import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;

/**
 * Handles ground items
 **/

public class ItemHandler {

	public List<GroundItem> items = new LinkedList<>();
	public boolean[] spawnables;
	public static final int HIDE_TICKS = 100;



	/**
	 * Adds item to list
	 **/
	public void addItem(GroundItem item) {
		items.add(item);
	}
	
	public static ItemList ItemList[] = new ItemList[Config.ITEM_LIMIT];
	
	 public static ItemList getItemList(int i) {
	        if (i > 0 && i < Config.ITEM_LIMIT && ItemList[i] != null) {
	            return ItemList[i];
	        }
	        return ItemList[0];
	    }

	/**
	 * Removes item from list
	 **/
	public void removeItem(GroundItem item) {
		items.remove(item);
	}

	/**
	 * Item amount
	 **/
	public int itemAmount(String player, int itemId, int itemX, int itemY, int height) {
		for (GroundItem i : items) {
			if (i.hideTicks >= 1 && player.equalsIgnoreCase(i.getController()) || i.hideTicks < 1) {
				if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY && i.getHeight() == height) {
					return i.getAmount();
				}
			}
		}
		return 0;
	}

	/**
	 * Item exists
	 **/
	public boolean itemExists(int itemId, int itemX, int itemY, int height) {
		for (GroundItem i : items) {
			if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY
					&& i.getHeight() == height) {
				return true;
			}
		}
		return false;
	}
	
	public void reloadItems(Player player) {
		Predicate<GroundItem> visible = item -> (((player.getItems().isTradeable(item.getId())
				|| item.getController().equalsIgnoreCase(player.playerName)) && player
				.distanceToPoint(item.getX(), item.getY()) <= 60)
				&& (item.hideTicks > 0 && item.getController().equalsIgnoreCase(player.playerName) || item.hideTicks == 0) && player.heightLevel == item.getHeight());
		items.stream().filter(visible).forEach(item -> player.getItems().removeGroundItem(item));
		items.stream().filter(visible).forEach(item -> player.getItems().createGroundItem(item));
	}

	public void process() {
		try {
			Iterator<GroundItem> it = items.iterator();
			while (it.hasNext()) {
				GroundItem i = it.next();
				if (i == null)
					continue;
				if(i.globalItem) {
					if(!i.initialized) {
						createGlobalItem(i);
						i.initialized = true;
					}
					continue;
				}
				
				if (i.hideTicks > 0) {
					i.hideTicks--;
				}
				if (i.hideTicks == 1) {
					i.hideTicks = 0;
					createGlobalItem(i);
					i.removeTicks = HIDE_TICKS;
				}
				if (i.removeTicks > 0) {
					i.removeTicks--;
				}
				if (i.removeTicks == 1) {
					i.removeTicks = 0;
					PlayerHandler.stream().filter(Objects::nonNull).filter(p -> p.distanceToPoint(i.getX(), i.getY()) <= 60)
							.forEach(p -> p.getItems().removeGroundItem(i.getId(), i.getX(), i.getY(), i.getAmount()));
					it.remove();
				}
			}
		} catch (Exception e) {
			System.out.println("ItemHandler" + e);
			e.printStackTrace();
		}
	}

	/**
	 * Creates the ground item
	 **/
	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 }, { 4720, 4896 }, { 4718, 4890 },
			{ 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 }, { 4734, 4938 }, { 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 },
			{ 4728, 4920 }, { 4730, 4926 }, { 4745, 4956 }, { 4747, 4926 }, { 4749, 4968 }, { 4751, 4994 }, { 4753, 4980 }, { 4755, 4986 },
			{ 4757, 4992 }, { 4759, 4998 } };
	
	public void createGroundItem(Player c, int itemId, int itemX, int itemY, int height, int itemAmount, int playerId) {
		try {
		if (itemId > 0) {
			if (itemId >= 2412 && itemId <= 2414) {
				c.sendMessage("The cape vanishes as it touches the ground.");
				return;
			}
			if (!ItemDefinition.forId(itemId).isStackable() && itemAmount > 0) {
				if (itemAmount > 28)
					itemAmount = 28;
				for (int j = 0; j < itemAmount; j++) {
					c.getItems().createGroundItem(itemId, itemX, itemY, 1);
					GroundItem item = new GroundItem(itemId, itemX, itemY, height, 1, HIDE_TICKS, PlayerHandler.players[playerId].playerName);
					addItem(item);
				}
			} else {
				c.getItems().createGroundItem(itemId, itemX, itemY, itemAmount);
				GroundItem item = new GroundItem(itemId, itemX, itemY, height, itemAmount, HIDE_TICKS, PlayerHandler.players[playerId].playerName);
				addItem(item);
			}
		}
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	/**
	 * Shows items for everyone who is within 60 squares
	 **/
	public void createGlobalItem(GroundItem i) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (!person.playerName.equalsIgnoreCase(i.getController())) {
					if (!person.getItems().isTradeable(i.getId())) {
						continue;
					}
					if (person.distanceToPoint(i.getX(), i.getY()) <= 60 && person.heightLevel == i.getHeight()) {
						person.getItems().createGroundItem(i.getId(), i.getX(), i.getY(), i.getAmount());
					}
				}
			}
		}
	}

	/**
	 * Removing the ground item
	 **/

	public void removeGroundItem(Player c, int itemId, int itemX, int itemY, int height, boolean add) {
		for (GroundItem i : items) {
			if (i.getId() == itemId && i.getX() == itemX && i.getY() == itemY && i.getHeight() == height) {
				if (i.hideTicks > 0 && i.getController().equalsIgnoreCase(c.playerName)) {
					if (add) {
						if (c.getItems().addItem(i.getId(), i.getAmount())) {
							removeControllersItem(i, c, i.getId(), i.getX(), i.getY(), i.getAmount());
							break;
						}
					} else {
						removeControllersItem(i, c, i.getId(), i.getX(), i.getY(), i.getAmount());
						break;
					}
				} else if (i.hideTicks <= 0) {
					if (c.ironman && !i.getController().equalsIgnoreCase(c.playerName)) {
						c.sendMessage("You can only pick up your own drops and items as a Ironman.");
						return;
					}
					if (add) {
						if (c.getItems().addItem(i.getId(), i.getAmount())) {
							removeGlobalItem(i, i.getId(), i.getX(), i.getY(), i.getHeight(), i.getAmount());
							break;
						}
					} else {
						removeGlobalItem(i, i.getId(), i.getX(), i.getY(), i.getHeight(), i.getAmount());
						break;
					}
				}
			}
		}
	}

	/**
	 * Remove item for just the item controller (item not global yet)
	 **/

	public void removeControllersItem(GroundItem i, Player c, int itemId, int itemX, int itemY, int itemAmount) {
		c.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
		removeItem(i);
	}

	/**
	 * Remove item for everyone within 60 squares
	 **/

	public void removeGlobalItem(GroundItem i, int itemId, int itemX, int itemY, int height, int itemAmount) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = p;
				if (person.distanceToPoint(itemX, itemY) <= 60 && person.heightLevel == height) {
					person.getItems().removeGroundItem(itemId, itemX, itemY, itemAmount);
				}
			}
		}
		removeItem(i);
	}

	/**
	 * Item List
	 **/


}
