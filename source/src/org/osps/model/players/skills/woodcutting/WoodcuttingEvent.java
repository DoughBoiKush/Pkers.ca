package org.osps.model.players.skills.woodcutting;

import java.util.Optional;
import java.util.Random;

import org.osps.Config;
import org.osps.Server;
import org.osps.clip.Region;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.skills.Firemaking;
import org.osps.model.players.skills.Skill;
import org.osps.util.Misc;
import org.osps.world.WorldObject;
import org.osps.world.objects.GlobalObject;

public class WoodcuttingEvent extends CycleEvent {
	
	public static final Random random25 = new Random();
	private Player player;
	private Tree tree;
	private Hatchet hatchet;
	private int objectId, x, y, chops;
	
	public WoodcuttingEvent(Player player, Tree tree, Hatchet hatchet, int objectId, int x, int y) {
		this.player = player;
		this.tree = tree;
		this.hatchet = hatchet;
		this.objectId = objectId;
		this.x = x;
		this.y = y;
	}

	@Override
	public void execute(CycleEventContainer container) {
		if(player == null || player.disconnected || player.teleporting || player.isDead || container.getOwner() == null) {
			container.stop();
			return;
		}
		if (!player.getItems().playerHasItem(hatchet.getItemId()) && !player.getItems().isWearingItem(hatchet.getItemId())) {
			player.sendMessage("Your axe has dissapeared.");
			container.stop();
			return;
		}
		if (player.playerLevel[player.playerWoodcutting] < hatchet.getLevelRequired()) {
			player.sendMessage("You no longer have the level required to operate this hatchet.");
			container.stop();
			return;
		}
		if (player.getItems().freeSlots() == 0) {
			player.sendMessage("You have run out of free inventory space.");
			container.stop();
			return;
		}
		if (Misc.random(300) == 0 && player.getInterfaceEvent().isExecutable()) {
			player.getInterfaceEvent().execute();
			container.stop();
			return;
		}
		chops++;
		int chopChance = 1 + (int) (tree.getChopsRequired() * hatchet.getChopSpeed());
		if (Misc.random(tree.getChopdownChance()) == 0 || tree.equals(Tree.NORMAL) && Misc.random(chopChance) == 0) {
			int face = 0;
			Optional<WorldObject> worldObject = Region.getWorldObject(objectId, x, y, 0);
			if (worldObject.isPresent()) {
				face = worldObject.get().getFace();
			}
			if(tree.equals(Tree.RED_WOOD) && player.absX == 1574) {
				Server.getGlobalObjects().add(new GlobalObject(tree.getStumpId(), x, y, player.heightLevel, 3, 10, tree.getRespawnTime(), objectId));
			} else if(tree.equals(Tree.RED_WOOD) && player.absX == 1567) {
				Server.getGlobalObjects().add(new GlobalObject(tree.getStumpId(), x, y, player.heightLevel, 5, 10, tree.getRespawnTime(), objectId));
			} else if(!tree.equals(Tree.RED_WOOD)) {
				Server.getGlobalObjects().add(new GlobalObject(tree.getStumpId(), x, y, player.heightLevel, face, 10, tree.getRespawnTime(), objectId));
			}
			player.getItems().addItem(tree.getWood(), 1);
			if (player.playerEquipment[player.playerHat] == 10941 && player.playerEquipment[player.playerChest] == 10939 && player.playerEquipment[player.playerLegs] == 10940 && player.playerEquipment[player.playerFeet] == 10933) {
				player.getPA().addSkillXP(tree.getExperience() * Config.WOODCUTTING_EXPERIENCE * 1.2, Skill.WOODCUTTING.getId());
			} else {
				player.getPA().addSkillXP(tree.getExperience() * Config.WOODCUTTING_EXPERIENCE, Skill.WOODCUTTING.getId());
			}
			container.stop();
			return;
		}

			if (Misc.random(chopChance) == 0 || chops >= tree.getChopsRequired()) {
				int chance20 = Misc.random(3);
				chops = 0;
				player.getItems().addItem(tree.getWood(), 1);
				if (chance20 == 1 && player.lightWood < 1 && player.getItems().playerHasItem(13241) || player.playerEquipment[player.playerWeapon] == 13241) {
					int r = random25.nextInt(Firemaking.logsdata.length);
					player.getPA().addSkillXP(Firemaking.logsdata[r][2] * Config.FIREMAKING_EXPERIENCE, 11);
					player.getItems().deleteItem(tree.getWood(), 1);
					player.gfx0(1180);
					player.lightWood++;
				}
				if (player.playerEquipment[player.playerHat] == 10941 && player.playerEquipment[player.playerChest] == 10939 && player.playerEquipment[player.playerLegs] == 10940 && player.playerEquipment[player.playerFeet] == 10933) {
					player.getPA().addSkillXP(tree.getExperience() * Config.WOODCUTTING_EXPERIENCE * 1.2, Skill.WOODCUTTING.getId());
				} else {
					player.getPA().addSkillXP(tree.getExperience() * Config.WOODCUTTING_EXPERIENCE, Skill.WOODCUTTING.getId());
				}
				player.lightWood = 0;
				if (tree.equals(Tree.NORMAL)) {
					int random = Misc.random(6000);
					if (random == 6000) {
					player.sendMessage("@red@You receive a skilling pet. It has been added to your bank. Congratulations!");
					player.getItems().addItemToBank(13322, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Player c2 = PlayerHandler.players[j];
								c2.sendMessage("<col=006600>" + player.playerName + " received a skilling pet: 1 x Beaver.");
							}
						}
					}
				} else if (tree.equals(Tree.OAK)) {
					int random = Misc.random(5500);
					if (random == 5500) {
					player.sendMessage("@red@You receive a skilling pet. It has been added to your bank. Congratulations!");
					player.getItems().addItemToBank(13322, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Player c2 = PlayerHandler.players[j];
								c2.sendMessage("<col=006600>" + player.playerName + " received a skilling pet: 1 x Beaver.");
							}
						}
					}
				} else if (tree.equals(Tree.WILLOW)) {
					int random = Misc.random(5000);
					if (random == 5000) {
					player.sendMessage("@red@You receive a skilling pet. It has been added to your bank. Congratulations!");
					player.getItems().addItemToBank(13322, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Player c2 = PlayerHandler.players[j];
								c2.sendMessage("<col=006600>" + player.playerName + " received a skilling pet: 1 x Beaver.");
							}
						}
					}
				} else if (tree.equals(Tree.MAPLE)) {
					int random = Misc.random(4500);
					if (random == 4500) {
					player.sendMessage("@red@You receive a skilling pet. It has been added to your bank. Congratulations!");
					player.getItems().addItemToBank(13322, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Player c2 = PlayerHandler.players[j];
								c2.sendMessage("<col=006600>" + player.playerName + " received a skilling pet: 1 x Beaver.");
							}
						}
					}
				} else if (tree.equals(Tree.YEW)) {
					int random = Misc.random(4000);
					if (random == 4000) {
					player.sendMessage("@red@You receive a skilling pet. It has been added to your bank. Congratulations!");
					player.getItems().addItemToBank(13322, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Player c2 = PlayerHandler.players[j];
								c2.sendMessage("<col=006600>" + player.playerName + " received a skilling pet: 1 x Beaver.");
							}
						}
					}
				} else if (tree.equals(Tree.MAGIC) || tree.equals(Tree.RED_WOOD)) {
					int random = Misc.random(3500);
					if (random == 3500) {
					player.sendMessage("@red@You receive a skilling pet. It has been added to your bank. Congratulations!");
					player.getItems().addItemToBank(13322, 1);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
							if (PlayerHandler.players[j] != null) {
								Player c2 = PlayerHandler.players[j];
								c2.sendMessage("<col=006600>" + player.playerName + " received a skilling pet: 1 x Beaver.");
							}
						}
					}
				}
				int chance = Misc.random(50);
				//player.sendMessage("Your chance to get 100 platinum tokens from skilling was " + chance + " you needed 0.");
				if (chance == 0) {
					player.getPA().rewardPoints(2, "Congrats, You randomly got 2 PK Points from woodcutting!");
				}
			}
		if (container.getTotalTicks() % 4 == 0) {
			player.animation(hatchet.getAnimation());
		}
	}
	
	@Override
	public void stop() {
		if (player != null) {
			player.animation(65535);
		}
	}
}
