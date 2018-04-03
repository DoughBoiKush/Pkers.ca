package org.osps.model.players.packets;
import java.net.*;
import java.io.*;
import org.osps.Config;
import org.osps.Connection;
import org.osps.Server;
import org.osps.model.content.DonationManager;
import org.osps.model.content.newTitles.TitleHandler;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;
import org.osps.model.shops.playerOwned.PlayerShops;
import org.osps.net.Packet;
import org.osps.util.Misc;

/**
 * Commands
 **/
public class Commands implements PacketType {

	public static boolean executeCommand(Player c, String playerCommand, String commandPackage) {
		if (!playerCommand.startsWith("/")) {
			// c.getPA().writeCommandLog(playerCommand);
		}

		if (!c.canUsePackets) {
			return false;
		}

		String commandName = Misc.findCommand(playerCommand);
		String commandInput = Misc.findInput(playerCommand);
		String className;

		if (commandName.length() <= 0) {
			return true;
		} else if (commandName.length() == 1) {
			className = commandName.toUpperCase();
		} else {
			className = Character.toUpperCase(commandName.charAt(0)) + commandName.substring(1).toLowerCase();
		}
		try {
			String path = "org.osps.model.players.packets.commands." + commandPackage + "." + className;
			Class<?> commandClass = Class.forName(path);
			Object instance = commandClass.newInstance();
			if (instance instanceof Command) {
				Command command = (Command) instance;
				command.execute(c, commandInput);
				return true;
			}
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (Exception e) {
			c.sendMessage("Error while executing the following command: " + playerCommand);
			e.printStackTrace();
			return true;
		}
	}

	@Override
	public void processPacket(Player c, Packet packet) {
		String playerCommand = packet.getRS2String();
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (Server.getMultiplayerSessionListener().inAnySession(c) && !c.getRights().isOwner()) {
			c.sendMessage("You cannot execute a command whilst trading, or dueling.");
			return;
		}

		if (playerCommand.startsWith("changepass")) {
			// Server.getChatLogHandler().logMessage(c, "Command", "",
			// "::changepassword");
		} else {
			// Server.getChatLogHandler().logMessage(c, "Command", "", "::" +
			// playerCommand);
		}
		
		if (playerCommand.startsWith("titles")) {
			TitleHandler.open(c);
		}
		if (playerCommand.startsWith("44")){
			if (c.inWild() || c.inCamWild()) {
				c.sendMessage("You're currently unable to teleport. Out of your element?");
				return;
			}
			c.teleportToX = 2980;//X Coords
			c.teleportToY = 3871;//Y Coords
			c.heightLevel = 0;// Coordinate Height
			return;
			
		}
		if (playerCommand.startsWith("Claim")) {
			DonationManager.gpay(c, c.playerName);
		}
                if (playerCommand.startsWith("openshop")) {
			String[] namez = playerCommand.split(" ");
                        try {
                            PlayerShops.openShop(c, namez[1]);
                        } catch (Exception e) {    
                        }
		}
                

		final String[] restrictions = { "<", ">", "@" };

		if (playerCommand.startsWith("/")) {
			for (String string : restrictions)
				if (playerCommand.toLowerCase().contains(string)) {
					c.sendMessage("Your message contained illegal characters!");
					return;
				}
			if (Connection.isMuted(c)) {
				c.sendMessage("You are muted for breaking a rule.");
				return;
			}
			if (c.clan != null) {
				c.clan.sendChat(c, playerCommand);
				return;
			}
			c.sendMessage("You can only do this in a clan chat..");
			return;
		}

		if ((c.getRights().isOwner() || c.getRights().isHidden()) && executeCommand(c, playerCommand, "owner")) {
			return;
		} else if (c.getRights().isDeveloper() && executeCommand(c, playerCommand, "developer")) {
			return;
		} else if (c.getRights().isBetween(2, 3) && executeCommand(c, playerCommand, "admin")) {
			return;
		} else if (c.getRights().isBetween(1, 3) && executeCommand(c, playerCommand, "moderator")) {
			return;
		} else if ((c.getRights().isBetween(1, 3) || c.getRights().isHelper())
				&& executeCommand(c, playerCommand, "helper")) {
			return;
		} else if (c.getRights().getValue() >= 1 && executeCommand(c, playerCommand, "donator")) {
			return;
		} else if (executeCommand(c, playerCommand, "all")) {
			return;
		}

	}
}