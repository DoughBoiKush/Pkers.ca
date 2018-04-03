package org.osps.model.players.packets.commands.moderator;

import org.osps.Connection;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;

/**
 * Send a global message.
 * 
 * @author Emiel
 */
public class Yell implements Command {
	
	static final String[] ILLEGAL_ARGUMENTS = {
		":tradereq:", "<img", "@cr", "<", "<tran", "#url#", ":duelreq:", ":chalreq:"
	};

	@Override
	public void execute(Player c, String input) {
		String rank = "";
		String message = input;
		if (Connection.isMuted(c)) {
			c.sendMessage("You are muted and can therefore not yell.");
			return;
		}
		if (System.currentTimeMillis() < c.muteEnd) {
			c.sendMessage("You are muted and can therefore not yell.");
			return;
		}
		if (!c.lastYell.elapsed(5000) && c.getRights().isContributor()) {
			c.sendMessage("You must wait 5 seconds between each yell.");
			return;
		}
		if (c.getRights().isContributor()) {

			rank = "[<img=4>@red@Donator@bla@]" + c.playerName + ":@dre@";
		}
		if (c.getRights().isSponsor()) {

			rank = "[<img=5>@yel@Sponsor@bla@]" + c.playerName + ":@dre@";
		}
		if (c.getRights().isSupporter()) {

			rank = "[<img=6><col=380770>Legendary Donator@bla@]" + c.playerName + ":@dre@";
		}
		if (c.getRights().isVIP()) {

			rank = "[<img=7><col=FF00CD>Respected Donator</col>@bla@]" + c.playerName + ":@dre@";
		}			
		if (c.getRights().isHelper()) {

			rank = "[<img=10>@blu@Server-Support@bla@]" + c.playerName + ":@dre@";
		}
		/* Staff */
		if (c.getRights().isModerator()) {
			rank = "[<img=0>@blu@Moderator@bla@]" + c.playerName + ":@dre@";
		}
		if (c.getRights().isAdministrator()) {

			rank = "[<img=2>@yel@Administrator@bla@]" + Misc.ucFirst(c.playerName) + ":@dre@";
		}
		if (c.getRights().isOwner() && !c.playerName.equalsIgnoreCase("Notepad5")) {
			rank = "[<col=A67711><img=2>Owner</col>]" + Misc.ucFirst(c.playerName) + ":<col=0000FF>";
		}
		if (c.getRights().isDeveloper()) {
			rank = "[<col=5E14A7><img=8>Developer</col>]" + Misc.ucFirst(c.playerName) + ":<col=0000FF>";
		} 
		if(c.getRights().isYoutuber()) {
			rank = "[<col=B0171F><img=9>Youtuber]</col>" + Misc.ucFirst(c.playerName) + ":<col=0000FF>";
		}
		if(c.getRights().isStreamer()) {
			rank = "[<col=B0171F><img=13>Streamer]</col>" + Misc.ucFirst(c.playerName) + ":<col=0000FF>";
		}
		 if (c.inDuelArena()) {
		        rank = "[@or2@Duel-Arena@bla@]" + Misc.ucFirst(c.playerName) + ":@dre@";
		}
		if (c.inMasterZone()) {
	            rank = "[@blu@Master-Zone@bla@]" + Misc.ucFirst(c.playerName) + ":@dre@";
	    }
		message = message.toLowerCase();
		for (String argument : ILLEGAL_ARGUMENTS) {
			if (message.contains(argument)) {
				c.sendMessage("Your message contains an illegal set of characters, you cannot yell this.");
				return;
			}
		}
		message = Misc.ucFirst(message);
		PlayerHandler.executeGlobalMessage(rank + message, true);
	}
}

