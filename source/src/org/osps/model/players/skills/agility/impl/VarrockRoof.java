package org.osps.model.players.skills.agility.impl;

import java.util.Random;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class VarrockRoof {

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
	
	public static final Random random = new Random();
	
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

	public void initialize(Player client, int objectType) {
		switch (objectType) {
		case 10586:
			if(client.hasInteracted >= 1) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.playerLevel[16] < 30) {
				client.getDH().sendStatement("You need a agility level of 30 to use this course!");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absX != 3221 && client.absY != 3414) {
				client.getPA().walkTo5(3221, 3414);
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
						client.face(3220, 3414);
						client.animation(828);
					}

					if (container.getTotalTicks() == 2) {
						client.getPA().movePlayer(3220, 3414, 3);
						client.animation(2585);
					}

					if (container.getTotalTicks() == 4) {
						client.getPA().movePlayer(3219, 3414, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					marks(client, 3219, 3416, 3);
					client.getPA().addSkillXP(2700, client.playerAgility);
				}
			}, 1);
			break;

		case 10587:
			if(client.hasInteracted >= 2) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absX != 3214 && client.absY != 3414) {
				client.getPA().walkTo5(3214, 3414);
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
						client.face(3208, 3414);
						client.animation(741);
					}

					if (container.getTotalTicks() == 2) {
						client.getPA().movePlayer(3212, 3414, 3);
						client.animation(741);
					}

					if (container.getTotalTicks() == 3) {
						client.getPA().movePlayer(3210, 3414, 3);
						client.animation(741);
					}

					if (container.getTotalTicks() == 4) {
						client.getPA().movePlayer(3208, 3414, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					marks(client, 3206, 3414, 3);
					client.getPA().addSkillXP(2900, client.playerAgility);
				}
			}, 1);
			break;

		case 10642:
			if(client.hasInteracted >= 3) {
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
						client.face(3197, 3416);
						client.animation(2586);
					}
					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(3197, 3416, 1);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(2800, client.playerAgility);
					marks(client, 3195, 3416, 1);
				}
			}, 1);
			break;

		case 10777:
			if(client.hasInteracted >= 4) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absX != 3194 && client.absY != 3416) {
				client.getPA().walkTo5(3194, 3416);
				return;
			}
			client.usingObstacle = true;
			client.stopPlayerPacket = true;
			SET_ANIMATION(client, 1995, -1, 0);
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected || client.teleporting || client.isDead) {
						container.stop();
						return;
					}
					if (container.getTotalTicks() == 2) {
						client.face(3190, 3414);
						client.animation(741);
					}

					if (container.getTotalTicks() == 3) {
						client.getPA().movePlayer(3190, 3414, 1);
						client.face(3189, 3414);
						client.animation(1122);
					}

					if (container.getTotalTicks() == 5) {
						RESET_ANIMATION(client);
						client.getPA().movePlayer(3190, 3413, 1);
						client.face(3189, 3413);
						client.animation(1122);
					}

					if (container.getTotalTicks() == 7) {
						client.getPA().movePlayer(3190, 3412, 1);
						client.face(3189, 3412);
						client.animation(1122);
					}

					if (container.getTotalTicks() == 9) {
						client.getPA().movePlayer(3190, 3411, 1);
						client.face(3189, 3411);
						client.animation(1122);
					}

					if (container.getTotalTicks() == 11) {
						client.getPA().movePlayer(3190, 3410, 1);
						client.face(3189, 3410);
						client.animation(1124);
					}

					if (container.getTotalTicks() == 13) {
						client.animation(65535);
						client.getPA().movePlayer(3190, 3409, 1);
						client.animation(756);
						client.setForceMovement(client.localX(), client.localY(), client.localX(), client.localY() - 3,
								1, 30, 2);
						client.face(3192, 3406);
					}

					if (container.getTotalTicks() == 15) {
						client.face(3192, 3406);
						client.animation(741);
					}

					if (container.getTotalTicks() == 16) {
						client.getPA().movePlayer(3192, 3406, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.face(3192, 3405);
					client.getPA().addSkillXP(3800, client.playerAgility);
				}
			}, 1);
			break;

		case 10778:
			if(client.hasInteracted >= 5) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absX != 3192 && client.absY != 3402) {
				client.getPA().walkTo5(3192, 3402);
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
						client.face(client.absX, client.absY - 3);
						client.animation(741);
					}

					if (container.getTotalTicks() == 2) {
						client.getPA().movePlayer(client.absX, client.absY - 3, 2);
						client.animation(2585);
					}

					if (container.getTotalTicks() == 4) {
						client.getPA().movePlayer(client.absX, client.absY - 2, 3);
					}

					if (container.getTotalTicks() == 5) {
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(3100, client.playerAgility);
					marks(client, 3196, 3394, 3);
				}
			}, 1);
			break;

		case 10779:
			if(client.hasInteracted >= 6) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absX != 3208 && client.absY != 3398) {
				client.getPA().walkTo5(3208, 3398);
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
						client.face(3218, 3399);
						client.animation(1995);
					}

					if (container.getTotalTicks() == 3) {
						client.animation(4789);
					}

					if (container.getTotalTicks() == 4 && client.absY == 3398) {
						client.face(3215, 3399);
						client.setForceMovement(client.localX(), client.localY(), client.localX() + 7,
								client.localY() + 1, 1, 40, 4);
					}

					if (container.getTotalTicks() == 4 && client.absY == 3397) {
						client.face(3215, 3399);
						client.setForceMovement(client.localX(), client.localY(), client.localX() + 7,
								client.localY() + 2, 1, 40, 4);
					}

					if (container.getTotalTicks() == 5) {
						client.animation(2588);
					}

					if (container.getTotalTicks() == 7) {
						client.getPA().movePlayer(3217, 3399, 3);
						client.animation(2585);
					}

					if (container.getTotalTicks() == 9) {
						client.getPA().movePlayer(3218, 3399, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(3900, client.playerAgility);
					marks(client, 3218, 3401, 3);
				}
			}, 1);
			break;

		case 10780:
			if(client.hasInteracted >= 7) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			client.usingObstacle = true;
			client.stopPlayerPacket = true;
			if (client.absX != 3232 && client.absY != 3402) {
				client.getPA().walkTo5(3232, 3402);
				return;
			}
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected || client.teleporting || client.isDead) {
						container.stop();
						return;
					}
					if (container.getTotalTicks() == 1) {
						client.face(3236, 3403);
						client.animation(2586);
					}
					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(3236, 3403, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(3100, client.playerAgility);
					marks(client, 3236, 3405, 3);
				}
			}, 1);
			break;

		case 10781:
			if(client.hasInteracted >= 8) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.absY >= 3410) {
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
						client.face(3236, 3410);
						client.animation(1603);
					}
					if (container.getTotalTicks() == 2) {
						client.getPA().movePlayer(3236, 3410, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(3050, client.playerAgility);
					marks(client, 3236, 3412, 3);
				}
			}, 1);
			break;

		case 10817:
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absX != 3237 && client.absY != 3415) {
				client.getPA().walkTo5(3237, 3415);
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
						client.face(3237, 3417);
						client.animation(741);
					}

					if (container.getTotalTicks() == 2) {
						client.getPA().movePlayer(3237, 3416, 2);
					}

					if (container.getTotalTicks() == 3) {
						client.animation(2586);
					}

					if (container.getTotalTicks() == 3) {
						client.animation(2588);
						client.getPA().movePlayer(3238, 3417, 0);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted = 0;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(3200, client.playerAgility);
					marks(client, 3237, 3418, 0);
				}
			}, 1);
			break;
		}
	}
}

