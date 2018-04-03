package org.osps.model.players.skills.agility.impl;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class DraynorRoof {

	public void marks(Player client, int x, int y, int height) {
		int grace = Misc.random(4);
		if (client == null || client.disconnected || client.teleporting || client.isDead) {
			return;
		}
		if (grace == 0) {
			Server.itemHandler.createGroundItem(client, 11849, x, y, client.heightLevel, 1, client.index);
			client.sendMessage("You have received a mark of grace.");
		}
	}

	private void SET_ANIMATION(final Player c, final int walkAnimation, final int x, final int y) {
		c.isRunning2 = false;
		c.getPA().sendFrame36(173, 0);
		c.playerWalkIndex = walkAnimation;
		c.getPA().requestUpdates();
		c.getPA().walkTo5(x, y);
	}

	private static void RESET_ANIMATION(final Player c) {
		c.isRunning2 = true;
		c.getPA().sendFrame36(173, 1);
		c.playerWalkIndex = 0x333;
		c.getPA().requestUpdates();
	}

	public void initialize(Player client, int objectType) {
		switch (objectType) {
		case 10073:
			if(client.hasInteracted >= 1) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			client.stopPlayerPacket = true;
			client.usingObstacle = true;
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected || client.teleporting || client.isDead) {
						container.stop();
						return;
					}

					if (container.getTotalTicks() == 1) {
						client.animation(828);
					}

					if (container.getTotalTicks() == 3) {
						client.getPA().movePlayer(3102, 3279, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					marks(client, 3101, 3280, client.getHeightLevel());
					client.getPA().addSkillXP(600, client.playerAgility);
				}
			}, 1);
			break;

		case 10074:
			if(client.hasInteracted >= 2) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			} else if (client.absX == 3099 && client.absY == 3277) {
				client.stopPlayerPacket = true;
				client.usingObstacle = true;
				client.setForceMovement(client.localX(), client.localY(), client.localX() - 9, client.localY(), 1, 300);
				CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client == null || client.disconnected || client.teleporting || client.isDead) {
							container.stop();
							return;
						}
						client.animation(762);
						if (container.getTotalTicks() == 1) {
							// client.usingObstacle = true;
							client.face(3092, 3277);
						}

						if (container.getTotalTicks() == 10) {
							client.face(3092, 3277);
							container.stop();
						}
					}

					@Override
					public void stop() {
						client.hasInteracted++;
						client.stopPlayerPacket = false;
						client.usingObstacle = false;
						client.getPA().addSkillXP(1000, client.playerAgility);
						client.getPA().movePlayer(3090, 3277, client.heightLevel);
						RESET_ANIMATION(client);
						marks(client, 3089, 3274, client.getHeightLevel());
					}
				}, 1);
			} else {
				client.getPA().walkTo5(3098 - client.absX, 3277 - client.absY);
				client.sendMessage("You need to be infront of the wire to perform this action!");
			}
			break;

		case 10075:
			if(client.hasInteracted >= 3) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			} else if (client.absX == 3091 && client.absY == 3276) {
				client.usingObstacle = true;
				client.stopPlayerPacket = true;
				client.animation(819);
				client.setForceMovement(client.localX(), client.localY(), client.localX() + 1, client.localY(), 1, 50);
				CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client == null || client.disconnected || client.teleporting || client.isDead) {
							container.stop();
							return;
						}
						if (container.getTotalTicks() == 1) {
							client.absX = 3092;
							client.absY = 3276;
							client.heightLevel = 3;
						}

						client.animation(762);
						if (container.getTotalTicks() == 2) {
							client.face(3092, 3266);
							client.getPA().movePlayer(3092, 3276, 3);
						}

						if (container.getTotalTicks() == 3) {
							client.setForceMovement(client.localX(), client.localY(), client.localX(),
									client.localY() - 10, 1, 200);
						}

						if (container.getTotalTicks() == 4) {
							client.absX = 3092;
							client.absY = 3266;
							client.heightLevel = 3;
							;
						}

						if (container.getTotalTicks() == 10) {
							client.face(3092, 3266);
							client.getPA().movePlayer(3092, 3266, 3);
							client.animation(65535);
							container.stop();
						}
					}

					@Override
					public void stop() {
						client.hasInteracted++;
						client.stopPlayerPacket = false;
						client.usingObstacle = false;
						client.getPA().addSkillXP(1000, client.playerAgility);
						RESET_ANIMATION(client);
						marks(client, 3094, 3267, client.getHeightLevel());
					}
				}, 1);
			} else {
				client.getPA().walkTo5(3091 - client.absX, 3276 - client.absY);
				client.sendMessage("You need to be infront of the wire to perform this action!");
			}
			break;

		case 10077:
			if(client.hasInteracted >= 4) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			} else if (client.absX == 3089 && client.absY == 3265) {
				client.stopPlayerPacket = true;
				client.usingObstacle = true;
				CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client == null || client.disconnected || client.teleporting || client.isDead) {
							container.stop();
							return;
						}

						if (container.getTotalTicks() == 1) {
							client.animation(756);
							client.setForceMovement(client.localX(), client.localY(), client.localX(),
									client.localY() - 3, 1, 32, 4);
							client.absX = 3089;
							client.absY = 3262;
							client.heightLevel = 3;
						}

						if (container.getTotalTicks() == 2) {
							client.animation(753);
							client.setForceMovement(client.localX(), client.localY(), client.localX() - 1,
									client.localY() - 1, 1, 32, 4);
							client.absX = 3088;
							client.absY = 3261;
							client.heightLevel = 3;
						}

						if (container.getTotalTicks() == 5) {
							client.face(3088, 3260);
							client.getPA().movePlayer(3088, 3261, 3);
							container.stop();
						}
					}

					@Override
					public void stop() {
						client.hasInteracted++;
						client.stopPlayerPacket = false;
						client.usingObstacle = false;
						client.getPA().addSkillXP(1050, client.playerAgility);
						RESET_ANIMATION(client);
						marks(client, 3094, 3267, client.getHeightLevel());
					}
				}, 1);
			} else {
				client.sendMessage("You need to be infront of the wire to perform this action!");
			}
			break;

		case 10084:
			if(client.hasInteracted >= 5) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absY <= 3255) {
				return;
			}
			client.stopPlayerPacket = true;
			client.usingObstacle = true;
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected || client.teleporting || client.isDead) {
						container.stop();
						return;
					}

					if (container.getTotalTicks() == 1) {
						client.getPA().movePlayer(3088, 3256, 3);
						client.animation(2585);
					}

					if (container.getTotalTicks() == 3) {
						client.getPA().movePlayer(3088, 3255, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					marks(client, 3101, 3280, client.getHeightLevel());
					client.getPA().addSkillXP(900, client.playerAgility);
				}
			}, 1);
			break;

		case 10085:
			if(client.hasInteracted >= 6) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absX >= 3096) {
				return;
			}
			client.stopPlayerPacket = true;
			client.usingObstacle = true;
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected || client.teleporting || client.isDead) {
						container.stop();
						return;
					}

					if (container.getTotalTicks() == 1) {
						client.face(3096, 3256);
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(3096, 3256, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					marks(client, 3101, 3280, client.getHeightLevel());
					client.getPA().addSkillXP(900, client.playerAgility);
				}
			}, 1);
			break;

		case 10086:
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			client.usingObstacle = true;
			client.stopPlayerPacket = true;
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected || client.teleporting || client.isDead) {
						container.stop();
						return;
					}

					if (container.getTotalTicks() == 1) {
						client.face(3102, 3261);
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(3102, 3261, 1);
					}

					if (container.getTotalTicks() == 3) {
						client.face(3103, 3261);
						client.animation(2586);
					}

					if (container.getTotalTicks() == 4) {
						client.animation(2588);
						client.getPA().movePlayer(3103, 3261, 0);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted = 0;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					marks(client, 3101, 3280, client.getHeightLevel());
					client.getPA().addSkillXP(950, client.playerAgility);
				}
			}, 1);
			break;
		}
	}
}
