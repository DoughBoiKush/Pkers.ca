package org.osps.model.players.skills.agility.impl;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class AlkharidRoof {

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
		case 10093:
			if (client.playerLevel[16] < 20) {
				client.getDH().sendStatement("You need a agility level of 20 to use this course!");
				return;
			}
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

					if (container.getTotalTicks() == 2) {
						client.getPA().movePlayer(3273, 3192, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(1200, client.playerAgility);
					marks(client, 3273, 3189, client.getHeightLevel());
				}
			}, 1);
			break;

		case 10284:
			if(client.hasInteracted >= 2) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			} else if (client.absX == 3272 && client.absY == 3182) {
				if (client.absX != 3272 && client.absY != 3182) {
					client.getPA().walkTo5(3272 - client.absX, 3182 - client.absY);
				}
				client.usingObstacle = true;
				client.stopPlayerPacket = true;
				client.setForceMovement(client.localX(), client.localY(), client.localX(), client.localY() - 10, 1, 300);
				CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client == null || client.disconnected || client.teleporting || client.isDead) {
							container.stop();
							return;
						}
						client.animation(762);
						if (container.getTotalTicks() == 1) {
							client.face(3272, 3172);
						}

						if (container.getTotalTicks() == 11) {
							client.face(3272, 3171);
							container.stop();
						}
					}

					@Override
					public void stop() {
						client.hasInteracted++;
						client.stopPlayerPacket = false;
						client.usingObstacle = false;
						client.getPA().addSkillXP(2000, client.playerAgility);
						client.getPA().movePlayer(3272, 3172, client.heightLevel);
						marks(client, 3267, 3170, client.getHeightLevel());
					}
				}, 1);
			} else {
				client.getPA().walkTo5(3272 - client.absX, 3182 - client.absY);
				client.sendMessage("You need to be infront of the wire to perform this action!");
			}
			break;

		case 10355:
			if(client.hasInteracted >= 3) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			} else if (client.absX == 3268 && client.absY == 3166) {
				if (client.absX != 3268 && client.absY != 3166) {
					client.getPA().walkTo5(3268 - client.absX, 3166 - client.absY);
				}
				client.usingObstacle = true;
				client.stopPlayerPacket = true;
				SET_ANIMATION(client, 824, -2, 0);
				CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client == null || client.disconnected || client.teleporting || client.isDead) {
							container.stop();
							return;
						}

						if (container.getTotalTicks() == 5) {
							client.animation(824);
							client.setForceMovement(client.localX(), client.localY(), client.localX() + 15,
									client.localY(), 1, 100);
						}

						if (container.getTotalTicks() == 6) {
							client.animation(751);
						}

						if (container.getTotalTicks() == 10) {
							client.face(3284, 3166);
							client.getPA().movePlayer(3283, 3166, 3);
							container.stop();
						}
					}

					@Override
					public void stop() {
						client.hasInteracted++;
						client.stopPlayerPacket = false;
						client.usingObstacle = false;
						client.getPA().addSkillXP(2400, client.playerAgility);
						RESET_ANIMATION(client);
						marks(client, 3286, 3163, client.getHeightLevel());
					}
				}, 1);
			} else {
				client.getPA().walkTo5(3268 - client.absX, 3166 - client.absY);
			}
			break;

		case 10356:
			if(client.hasInteracted >= 4) {
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
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(1601);
						client.getPA().movePlayer(3303, 3163, 1);
					}

					if (container.getTotalTicks() == 3) {
						client.animation(1602);
						client.setForceMovement(client.localX(), client.localY(), client.localX() + 12, client.localY(),
								1, 250);
					}

					if (container.getTotalTicks() == 12) {
						client.face(3316, 3163);
						client.getPA().movePlayer(3315, 3163, 1);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(2600, client.playerAgility);
					client.animation(65535);
					RESET_ANIMATION(client);
					marks(client, 3318, 3163, client.getHeightLevel());
				}
			}, 1);
			break;

		case 10357:
			if(client.hasInteracted >= 5) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			} else if (client.absX == 3317 && client.absY == 3165) {
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
							client.getPA().movePlayer(3317, 3169, 1);
							client.face(3320, 3169);
						}

						if (container.getTotalTicks() == 2) {
							client.animation(1122);
						}

						if (container.getTotalTicks() == 3) {
							client.animation(1122);
						}

						if (container.getTotalTicks() == 4) {
							client.face(3317, 3174);
						}

						if (container.getTotalTicks() == 5) {
							client.animation(2586);
						}

						if (container.getTotalTicks() == 6) {
							client.getPA().movePlayer(3317, 3174, 2);
							container.stop();
						}
					}

					@Override
					public void stop() {
						client.hasInteracted++;
						client.stopPlayerPacket = false;
						client.usingObstacle = false;
						client.getPA().addSkillXP(1800, client.playerAgility);
						client.animation(65535);
						RESET_ANIMATION(client);
						marks(client, 3315, 3176, 2);
					}
				}, 1);
			} else {
				client.getPA().walkTo(3317, 3165);
			}
			break;

		case 10094:
			if(client.hasInteracted >= 6) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
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
						client.animation(828);
					}

					if (container.getTotalTicks() == 2) {
						client.getPA().movePlayer(3316, 3180, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(1700, client.playerAgility);
					marks(client, 3314, 3182, 3);
				}
			}, 1);
			break;

		case 10583:
			if(client.hasInteracted >= 7) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			} else if (client.absX == 3314 && client.absY == 3186) {
				client.stopPlayerPacket = true;
				client.usingObstacle = true;
				client.setForceMovement(client.localX(), client.localY(), client.localX() - 12, client.localY(), 1, 300);
				CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client == null || client.disconnected || client.teleporting || client.isDead) {
							container.stop();
							return;
						}
						client.animation(762);
						if (container.getTotalTicks() == 1) {
							client.face(3302, 3186);
						}

						if (container.getTotalTicks() == 11) {
							client.face(3302, 3186);
							container.stop();
						}
					}

					@Override
					public void stop() {
						client.hasInteracted++;
						client.stopPlayerPacket = false;
						client.usingObstacle = false;
						client.getPA().addSkillXP(2200, client.playerAgility);
						client.getPA().movePlayer(3302, 3186, client.heightLevel);
						marks(client, 3303, 3190, client.getHeightLevel());
					}
				}, 1);
			} else {
				
				
			}
			break;

		case 10352:
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

					if (container.getTotalTicks() == 1 && client.absX == 3300) {
						client.animation(819);
						client.setForceMovement(client.localX(), client.localY(), client.localX() - 1,
								client.localY() + 1, 1, 60);
						client.face(3299, 3194);
					}

					if (container.getTotalTicks() == 1 && client.absX == 3301) {
						client.usingObstacle = true;
						client.stopPlayerPacket = true;
						client.animation(824);
						client.setForceMovement(client.localX(), client.localY(), client.localX() - 1, client.localY(),
								1, 60);
						client.face(3299, 3194);
					}

					if (container.getTotalTicks() == 3) {
						client.animation(2586);
					}

					if (container.getTotalTicks() == 4) {
						client.getPA().movePlayer(3299, 3194, 0);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted = 0;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(1400, client.playerAgility);
					marks(client, 3301, 3197, client.getHeightLevel());
				}
			}, 1);
			break;
		}
	}
}
