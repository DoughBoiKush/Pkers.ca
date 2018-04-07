package org.osps.model.players.packets;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.osps.Server;
import org.osps.cache.RSObjectDefinition;
import org.osps.clip.Region;
import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionStage;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.multiplayer_session.duel.DuelSession;
import org.osps.model.players.Boundary;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerCannon;
import org.osps.model.players.skills.SkillHandler;
import org.osps.model.players.skills.Runecrafting.Portals;
import org.osps.model.players.skills.Runecrafting.Rifts;
import org.osps.model.players.skills.Runecrafting.Runecrafting;
import org.osps.model.players.skills.farming.FarmingConstants;
import org.osps.model.players.skills.fletching.Fletching;
import org.osps.net.Packet;
import org.osps.world.WorldObject;

/**
 * Click Object
 */
public class ClickObject implements PacketType {

	private int clicked = 0;

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70;

	@SuppressWarnings("unused")
	@Override
	public void processPacket(final Player c, Packet packet) {
		c.clickObjectType = c.objectX = c.objectId = c.objectY = 0;
		c.objectYOffset = c.objectXOffset = 0;
		c.getPA().resetFollow();

		if (!c.canUsePackets) {
			return;
		}

		if (c.doingTutorial) {
			// System.out.println("Clicking objects is disabled whilst in
			// tutorial phase.");
			return;
		}

		if (c.usingObstacle) {
			return;
		}

		switch (packet.getOpcode()) {
		case FIRST_CLICK:
			c.objectX = packet.getLEShortA();
			c.objectId = packet.getUnsignedShort();
			c.objectY = packet.getUnsignedShortA();
			c.objectDistance = 1;
			//c.sendDebugMessage("packetType= " + packet.getOpcode() + " id: " + c.objectId + " loc[" + c.objectX + ","
			//		+ c.objectY + " offX: " + (c.getX() - c.objectX) + " offY: " + (c.getY() - c.objectY) + "]");

			if (c.objectId == 9357) {
				if (Boundary.isIn(c, Boundary.FIGHT_CAVE)) {
					c.getFightCave().leaveGame();
				}
				return;
			}
			
			if(c.objectId == 4308) {
				c.objectDistance = 5;
			}

			if (c.objectId == 10583) {
				c.stopMovement();
				c.getPA().walkTo(3314, 3186);
			}

			if (c.getInterfaceEvent().isActive()) {
				c.sendMessage("Please finish what you're doing.");
				return;
			}

			if (!goodPath(c)) {
				return;
			}

			DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
					&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
				c.sendMessage("Your actions have declined the duel.");
				duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			if (Math.abs(c.getX() - c.objectX) > 25 || Math.abs(c.getY() - c.objectY) > 25) {
				c.resetWalkingQueue();
				break;
			}
			Portals.teleport(c, c.objectId);
			Runecrafting.Craft(c, c.objectId);
			Rifts.teleport(c, c.objectId);
			if (SkillHandler.isSkilling[9]) {
				Fletching.resetFletching(c);
			}

			c.setWalkInteractionTask(() -> c.getActions().firstClickObject(c.objectId, c.objectX, c.objectY));
			if (true) {
				break;
			}

			switch (c.objectId) {
			case 16529:
				c.getPA().movePlayer(3142, 3513, 0);
				break;
			case 26709:
				c.objectDistance = 6;
				break;
			case 11756:
			case 11764:
			case 16469:
			case 28498:
				c.objectDistance = 3;
				break;
			case 2114:
				c.objectXOffset = 4;
				break;
			case 677:
				c.objectXOffset = 4;
				break;
			case 2120:
				c.objectXOffset = 2;
				break;

			/**
			 * Rooftop Agility
			 */
			case 11374:
			case 11376:
			case 10819:
			case 10828:
			case 10822:
				c.objectDistance = 2;
				break;
			case 11377:
			case 10357:
				c.objectDistance = 3;
				break;
			case 10777:
				c.objectDistance = 3;
				break;

			case 16530:
				c.getPA().movePlayer(3137, 3516, 0);
				break;
			case 9398:// deposit
				c.getPA().sendFrame126("The Bank of Pkers.Ca - Deposit Box", 7421);
				c.getPA().sendFrame248(4465, 197);// 197 just because you can't
													// see it =\
				c.getItems().resetItems(7423);
				break;
			case 11758:
				c.objectDistance = 5;
				break;
			/*
			 * case 23145: if (c.getGnomeAgility().clicked == 0 && c.absY ==
			 * 3436) { c.getGnomeAgility().clicked = 1;
			 * c.sendMessage("I've clicked" +c.getGnomeAgility().clicked);
			 * return; } else if (c.getGnomeAgility().clicked == 1 && c.absY ==
			 * 3436) { c.objectDistance = 1; }
			 */
			// break;

			case 20772:
			case 20771:
			case 20720:
			case 20722:
			case 20770:
			case 20721:
			//case 20672:
			case 20667:
			case 20668:
			case 20670:
			//case 20671:
			case 20699:
				c.objectYOffset = 0;
				c.objectDistance = 3;
				break;
				
			case 20672:
				c.objectYOffset = 0;
				c.objectDistance = 3;
				c.getPA().movePlayer(3557, 3297, 0);
				break;
				
			case 20671:
				c.objectYOffset = 0;
				c.objectDistance = 5;
				c.getPA().movePlayer(3554, 3282, 0);
				break;
				
			case 21725:
			case 21726:
			case 21722:
			case 21724:
			case 23566:
			case 26760:
				c.objectDistance = 5;
				break;
			case 16671:
				c.getPA().movePlayer(2840, 3539, 2);
				break;

			case 1733:
				c.objectYOffset = 2;
				break;
			case 24600:
				TeleportExecutor.teleport(c, new Position(1469, 3863, 0), true);
				c.sendMessage("<col=006600>Welcome to the Blast Mine, You will need 42 Mining, 30 Crafting!");
				c.sendMessage("<col=006600>Buy a Dynamite pot for 25,000 coins from the operator .");
				c.sendMessage("<col=006600>Talk to Toothy to get up to the mine, there is a bank up there!");
				c.sendMessage("<col=006600>Once Mined enough sulphur, Use it on the dynamite pot to get dynamite");
				c.sendMessage("<col=006600>Talk to smoggy to get back down to the Blast Mines");
				c.sendMessage("<col=006600>Use the dynamite on the walls to get blasted ore");
				c.sendMessage("<col=006600>Finally, Use that ore on ore sack beside the operator to get a reward.");
				break;
			case 11744:
				c.getPA().openUpBank();
				break;
			case 3044:
			case 21764:
			case 17010:
			case 2561:
			case 2562:
			case 2563:
			case 2564:
			case 2565:
			case FarmingConstants.GRASS_OBJECT:
			case FarmingConstants.HERB_OBJECT:
			case FarmingConstants.HERB_PATCH_DEPLETED:
				c.objectDistance = 6;
				break;
			case 11833:
			case 11834:
				c.objectDistance = 2;
				break;
			case 23271:
				if (c.getPosition().getY() == 3523)
					c.objectDistance = 2;
				break;
			case 5094:
			case 5096:
			case 5097:
			case 5098:
				c.objectDistance = 7;
				break;

			case 245:
				c.objectYOffset = -1;
				c.objectDistance = 0;
				break;

			case 272:
				c.objectYOffset = 1;
				c.objectDistance = 0;
				break;

			case 273:
				c.objectYOffset = 1;
				c.objectDistance = 0;
				break;

			case 246:
				c.objectYOffset = 1;
				c.objectDistance = 0;
				break;

			case 4493:
			case 4494:
			case 4496:
			case 4495:
				c.objectDistance = 5;
				break;
			case 10229:
			case 6522:
				c.objectDistance = 2;
				break;
			case 8959:
				c.objectYOffset = 1;
				break;
			case 4417:
				if (c.objectX == 2425 && c.objectY == 3074)
					c.objectYOffset = 2;
				break;
			case 4420:
				if (c.getX() >= 2383 && c.getX() <= 2385) {
					c.objectYOffset = 1;
				} else {
					c.objectYOffset = -2;
				}
				break;
			case 6552:
			case 409:
				c.objectDistance = 2;
				break;
			case 2879:
			case 2878:
				c.objectDistance = 3;
				break;
			case 2558:
				c.objectDistance = 0;
				if (c.absX > c.objectX && c.objectX == 3044)
					c.objectXOffset = 1;
				if (c.absY > c.objectY)
					c.objectYOffset = 1;
				if (c.absX < c.objectX && c.objectX == 3038)
					c.objectXOffset = -1;
				break;
			case 9356:
				c.objectDistance = 2;
				break;
			case 5959:
			case 1815:
			case 5960:
			case 1816:
				c.objectDistance = 0;
				break;

			case 9293:

				c.objectDistance = 2;
				break;
			case 4418:
				if (c.objectX == 2374 && c.objectY == 3131)
					c.objectYOffset = -2;
				else if (c.objectX == 2369 && c.objectY == 3126)
					c.objectXOffset = 2;
				else if (c.objectX == 2380 && c.objectY == 3127)
					c.objectYOffset = 2;
				else if (c.objectX == 2369 && c.objectY == 3126)
					c.objectXOffset = 2;
				else if (c.objectX == 2374 && c.objectY == 3131)
					c.objectYOffset = -2;
				break;
			case 9706:
				c.objectDistance = 0;
				c.objectXOffset = 1;
				break;
			case 9707:
				c.objectDistance = 0;
				c.objectYOffset = -1;
				break;
			case 4419:
				if (c.absX > 2414 && c.absX < 2420 && c.absY > 3070 && c.absY < 3080) {
					c.objectDistance = 3;
					c.objectYOffset = 1;
					break;
				}
			case 6707: // verac
				c.objectYOffset = 3;
				break;
			case 6823:
				c.objectDistance = 2;
				c.objectYOffset = 1;
				break;

			case 6706: // torag
				c.objectXOffset = 2;
				break;
			case 6772:
				c.objectDistance = 2;
				c.objectYOffset = 1;
				break;

			case 6705: // karils
				c.objectYOffset = -1;
				break;
			case 6822:
				c.objectDistance = 2;
				c.objectYOffset = 1;
				break;

			case 6704: // guthan stairs
				c.objectYOffset = -1;
				break;
			case 6773:
				c.objectDistance = 2;
				c.objectXOffset = 1;
				c.objectYOffset = 1;
				break;

			case 6703: // dharok stairs
				c.objectXOffset = -1;
				break;
			case 6771:
				c.objectDistance = 2;
				c.objectXOffset = 1;
				c.objectYOffset = 1;
				break;

			case 6702: // ahrim stairs
				c.objectXOffset = -1;
				break;
			case 6821:
			case 4389:
			case 4408:
			case 4483:
			case 4387:
			case 4388:
				c.objectDistance = 2;
				c.objectXOffset = 1;
				c.objectYOffset = 1;
				break;
			case 3192:
				c.objectDistance = 7;
				break;
			case 1761:
			case 1753:
			case 1759:
			case 1750:
			case 1751:
			case 1276:
			case 1278:// trees
			case 1281: // oak
			case 1308: // willow
			case 1307: // maple
			case 1309: // yew
			case 1306: // yew
				c.objectDistance = 5;
				break;
			default:
				c.objectDistance = 1;
				c.objectXOffset = 0;
				c.objectYOffset = 0;
				break;
			}
			break;

		case SECOND_CLICK:
			c.objectId = packet.getLEShortA(); // getUnsignedShortA
			c.objectY = packet.getLEShort();
			c.objectX = packet.getUnsignedShortA();
			c.objectDistance = 1;
			c.sendDebugMessage("packetType= " + packet.getOpcode() + " id: " + c.objectId + " loc[" + c.objectX + ","
					+ c.objectY + " offX: " + (c.getX() - c.objectX) + " offY: " + (c.getY() - c.objectY) + "]");
			if (c.getInterfaceEvent().isActive()) {
				c.sendMessage("Please finish what you're doing.");
				return;
			}
			if (!goodPath(c)) {
				return;
			}
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
					&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
				c.sendMessage("Your actions have declined the duel.");
				duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}
			if (SkillHandler.isSkilling[9]) {
				Fletching.resetFletching(c);
			}

			c.setWalkInteractionTask(() -> c.getActions().secondClickObject(c.objectId, c.objectX, c.objectY));
			if (true) {
				break;
			}

			if (PlayerCannon.CannonPart.isObjCannon(c.objectId))
				c.objectDistance = 3;
			c.sendDebugMessage("packetType= " + packet.getOpcode() + " id: " + c.objectId + " loc[" + c.objectX + ","
					+ c.objectY + " offX: " + (c.getX() - c.objectX) + " offY: " + (c.getY() - c.objectY) + "]");

			switch (c.objectId) {
			case 6189:
				c.objectDistance = 3;
				break;
			case 409:
				c.objectDistance = 2;
			case 2561:
			case 2562:
			case 2563:
			case 2564:
			case 2565:
			case 2478:
			case 2483:
			case 2484:
				c.objectDistance = 3;
				break;
			case 6163:
			case 6165:
			case 6166:
			case 6164:
			case 6162:
				c.objectDistance = 2;
				break;
			case 3192:
				c.objectDistance = 7;
				break;
			default:
				c.objectDistance = 1;
				c.objectXOffset = 0;
				c.objectYOffset = 0;
				break;

			}
			break;

		case THIRD_CLICK:
			c.objectX = packet.getLEShort();
			c.objectY = packet.getUnsignedShort();
			c.objectId = packet.getLEShortA();
			c.objectDistance = 1;

			if (c.getInterfaceEvent().isActive()) {
				c.sendMessage("Please finish what you're doing.");
				return;
			}
			if (!goodPath(c)) {
				return;
			}
			duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
					MultiplayerSessionType.DUEL);
			if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
					&& duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERACTION) {
				c.sendMessage("Your actions have declined the duel.");
				duelSession.getOther(c).sendMessage("The challenger has declined the duel.");
				duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
				return;
			}

			c.sendDebugMessage("packetType= " + packet.getOpcode() + " id: " + c.objectId + " loc[" + c.objectX + ","
					+ c.objectY + " offX: " + (c.getX() - c.objectX) + " offY: " + (c.getY() - c.objectY) + "]");

			c.setWalkInteractionTask(() -> c.getActions().thirdClickObject(c.objectId, c.objectX, c.objectY));
			if (true) {
				break;
			}

			switch (c.objectId) {
			default:
				c.objectDistance = 1;
				c.objectXOffset = 0;
				c.objectYOffset = 0;
				break;
			}
			break;
		}

	}

	public static boolean goodPath(Player player) {
		if (ignorePath(player.objectId)) {
			return true;
		}

		Position destination = player.getWalkingDestination();
		int x = destination.getX();
		int y = destination.getY();

		if (x > player.objectX) {
			x--;
		} else if (x < player.objectX) {
			x++;
		}

		if (y > player.objectY) {
			y--;
		} else if (y < player.objectY) {
			y++;
		}
		
		boolean notBlocked = true;
		
		Optional<WorldObject> optional = Region.getWorldObject(player.objectId, player.objectX, player.objectY,
				player.heightLevel);
		
		if (optional.isPresent()) {
			WorldObject object = optional.get();
			if (object.type == 0) {
				return Math.max(Math.abs(player.objectX - x), Math.abs(player.objectY - y)) < 2;
			}

			RSObjectDefinition def = RSObjectDefinition.forId(object.getId());
			if (def == null) {
				return false;
			}

			int farX = player.objectX;
			int farY = player.objectY;
			if (object.getFace() != 1 && object.getFace() != 3) {
				farX += def.ysize;
				farY += def.xsize;
			} else {
				farX += def.xsize;
				farY += def.ysize;
			}
			
			notBlocked = x >= player.objectX && x <= farX && y >= player.objectY && y <= farY; 
		}

		return notBlocked && !Region.isBlockedPath(destination.getX(), destination.getY(), x, y, destination.getZ());
	}

	private static final int[] ignorePathObjects = { 10777 };

	public static boolean ignorePath(int objectId) {
		if (Arrays.binarySearch(ignorePathObjects, objectId) < 0) {
			return false;
		}
		return true;
	}

	public void handleSpecialCase(Player c, int id, int x, int y) {

	}

}
