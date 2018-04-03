package org.osps.model.players.skills.agility.impl;

import org.osps.Server;
import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;
import org.osps.util.Misc;

public class CanifisRoof {

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
		case 10819:
			if(client.hasInteracted >= 1) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.playerLevel[16] < 40) {
				client.getDH().sendStatement("You need a agility level of 40 to use this course!");
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
						client.face(3508, 3489);
						client.getPA().movePlayer(3507, 3489, 0);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(1765);
					}

					if (container.getTotalTicks() == 3) {
						client.getPA().movePlayer(3507, 3489, 0);
					}

					if (container.getTotalTicks() == 4) {
						client.getPA().movePlayer(3506, 3489, 0);
					}

					if (container.getTotalTicks() == 6) {
						client.animation(65535);
						client.getPA().movePlayer(3506, 3492, 2);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					marks(client, 3507, 3495, 2);
					client.getPA().addSkillXP(4100, client.playerAgility);
				}
			}, 1);
			break;
			
		case 10820:
			if(client.hasInteracted >= 2) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absY >= 3499) {
				return;
			}
			client.usingObstacle = true;
			client.stopPlayerPacket = true;
			SET_ANIMATION(client, 1995, 0, +1);
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected || client.teleporting || client.isDead) {
						container.stop();
						return;
					}

					if (container.getTotalTicks() == 1) {
						client.face(3503, 3504);
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(3503, 3504, 2);
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
					RESET_ANIMATION(client);
					marks(client, 3500, 3505, 2);
					client.getPA().addSkillXP(4200, client.playerAgility);
				}
			}, 1);
			break;
			
		case 10821:
			if(client.hasInteracted >= 3) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			if (client.absX < 3496) {
				return;
			}
			client.stopPlayerPacket = true;
			client.usingObstacle = true;
			SET_ANIMATION(client, 1995, 0, -1);
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected || client.teleporting || client.isDead) {
						container.stop();
						return;
					}

					if (container.getTotalTicks() == 1) {
						client.face(3492, 3503);
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(3492, 3503, 2);
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
					RESET_ANIMATION(client);
					marks(client, 3490, 3501, 2);
					client.getPA().addSkillXP(4250, client.playerAgility);
				}
			}, 1);
			break;
			
		case 10828:
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
						client.getPA().movePlayer(3481, 3499, 2);
						client.face(3479, 3499);
						client.animation(2585);
					}

					if (container.getTotalTicks() == 3) {
						client.getPA().movePlayer(3479, 3499, 3);
					}

					if (container.getTotalTicks() == 4) {
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					client.getPA().addSkillXP(4270, client.playerAgility);
					marks(client, 3477, 3496, 3);
				}
			}, 1);
			break;
			
		case 10822:
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
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(3479, 3486, 2);
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
					marks(client, 3478, 3484, 3);
					client.getPA().addSkillXP(4300, client.playerAgility);
				}
			}, 1);
			break;
			
		case 10831:
			if(client.hasInteracted >= 6) {
				client.sendMessage("You have already used this obstacle.");
				return;
			}
			if (client.stopPlayerPacket || client.usingObstacle) {
				return;
			}
			client.stopPlayerPacket = true;
			client.usingObstacle = true;
			SET_ANIMATION(client, 824, -1, +1);
			CycleEventHandler.getSingleton().addEvent(client, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (client == null || client.disconnected || client.teleporting || client.isDead) {
						container.stop();
						return;
					}
					
					if(container.getTotalTicks() == 2) {
						client.face(3489, 3476);
					}

					if (container.getTotalTicks() == 3) {
						RESET_ANIMATION(client);
						client.animation(824);
						client.setForceMovement1(client.localX(),client.localY(),client.localX() + 12, client.localY() - 8, 1, 250, 3);
						client.absX = 3485;
						client.absY = 3481;
						client.heightLevel = 3;
					}
					
					if (container.getTotalTicks() == 4) {
						client.animation(7132);
					}
					
					if (container.getTotalTicks() == 7) {
						client.getPA().movePlayer(client.absX, client.absY, 3);
					}

					if (container.getTotalTicks() == 12) {
						client.animation(65535);
						client.getPA().movePlayer(3491, 3477, 3);
						container.stop();
					}
				}

				@Override
				public void stop() {
					client.hasInteracted++;
					client.stopPlayerPacket = false;
					client.usingObstacle = false;
					marks(client, 3494, 3474, 3);
					client.getPA().addSkillXP(4900, client.playerAgility);
				}
			}, 1);
			break;
			
		case 10823:
			if(client.hasInteracted >= 7) {
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
						client.usingObstacle = true;
						client.face(3510, 3476);
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(3510, 3476, 2);
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
					marks(client, 3513, 3479, 2);
					client.getPA().addSkillXP(4300, client.playerAgility);
				}
			}, 1);
			break;
			
		case 10832:
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
						client.usingObstacle = true;
						client.face(3510, 3488);
						client.animation(2586);
					}

					if (container.getTotalTicks() == 2) {
						client.animation(2588);
						client.getPA().movePlayer(3510, 3484, 0);
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
					marks(client, 3508, 3486, 0);
					client.getPA().addSkillXP(13400, client.playerAgility);
				}
			}, 1);
			break;
		}
	}
}
