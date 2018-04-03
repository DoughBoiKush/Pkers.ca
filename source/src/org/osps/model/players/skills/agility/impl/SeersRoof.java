package org.osps.model.players.skills.agility.impl;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class SeersRoof {

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
		case 11373:
			if(client.hasInteracted >= 1) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.playerLevel[16] < 60) {
				client.getDH().sendStatement("You need a agility level of 60 to use this course!");
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
						client.animation(737);
					}
					if (container.getTotalTicks() == 2) {
						client.getPA().movePlayer(2729, 3488, 1);
						client.animation(1118);
					}

					if (container.getTotalTicks() == 4) {
						client.animation(65535);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().movePlayer(2729, 3491, 3);
					marks(client, 2725, 3492, 3);
					client.getPA().addSkillXP(5100, client.playerAgility);
				}
			}, 1);
			break;

		case 11374:
			if(client.hasInteracted >= 2) {
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
						client.face(2713, 3494);
						client.animation(2586);
					}
					if (container.getTotalTicks() == 3) {
						client.animation(2588);
						client.getPA().movePlayer(2719, 3495, 2);
					}
					if (container.getTotalTicks() == 5) {
						client.animation(2588);
						client.getPA().movePlayer(2713, 3494, 2);
					}

					if (container.getTotalTicks() == 6) {
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(5500, client.playerAgility);
					marks(client, 2709, 3493, 2);
				}
			}, 1);
			break;

		case 11378:
			if(client.hasInteracted >= 3) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			} else if (client.absX == 2710 && client.absY == 3490) {
				if (client.absX != 2710 && client.absY != 3490) {
					client.getPA().walkTo5(2710 - client.absX, 3490 - client.absY);
				}
				client.stopPlayerPacket = true;
				client.usingObstacle = true;
				SET_ANIMATION(client, 762, 0, -10);
				CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (client == null || client.disconnected || client.teleporting || client.isDead) {
							container.stop();
							return;
						}

						if (container.getTotalTicks() == 10) {
							container.stop();
						}
					}

					@Override
					public void stop() {
						client.hasInteracted++;
						client.stopPlayerPacket = false;
						client.usingObstacle = false;
						client.getPA().addSkillXP(6100, client.playerAgility);
						RESET_ANIMATION(client);
						marks(client, 2713, 3479, client.getHeightLevel());
					}
				}, 1);
			} else {
				client.sendMessage("You need to be infront of the wire to perform this action!");
			}
			break;

		case 11375:
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
						client.face(2711, client.absY - 3);
						client.animation(2585);
						client.getPA().movePlayer(2711, client.absY - 3, 2);

					}
					if (container.getTotalTicks() == 3) {
						client.animation(2588);
						client.getPA().movePlayer(2711, 3472, 3);
					}

					if (container.getTotalTicks() == 6) {
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(5300, client.playerAgility);
					marks(client, 2713, 3472, 3);
				}
			}, 1);
			break;

		case 11376:
			if(client.hasInteracted >= 5) {
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
						client.face(client.absX, client.absY - 5);
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(client.absX, client.absY - 5, 2);
					}

					if (container.getTotalTicks() == 3) {
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(5600, client.playerAgility);
					marks(client, 2699, 3463, 2);
				}
			}, 1);
			break;

		case 11377:
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
						client.face(client.absX + 3, client.absY + 1);
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(client.absX + 3, client.absY, 0);
					}

					if (container.getTotalTicks() == 3) {
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted = 0;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(15800, client.playerAgility);
					marks(client, 2706, 3462, 0);
				}
			}, 1);
			break;
		}
	}
}
