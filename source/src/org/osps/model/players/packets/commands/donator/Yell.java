package org.osps.model.players.packets.commands.donator;

import org.osps.Connection;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;
import org.osps.util.Misc;
/**
 * Tells the player they need to be a donator to use this feature.
 * 
 * @author Emiel
 */
public class Yell implements Command {
	
	static final String[] ILLEGAL_ARGUMENTS = {
		":tradereq:", "<img", "<img=", "<tran", "#url#", ":duelreq:", ":chalreq:"
	};

	@Override
	public void execute(Player c, String input) {
		if (c.getRights().isStaff()) {
			return;
		}
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
		/*if (!c.lastYell.elapsed(5000) && c.getRights().isSuperVIP()) {
			c.sendMessage("You are a @yel@Super VIP@bla@ and must wait 5 seconds between each yell.");
			return;
		}*/
		if (c.getRights().isPlayer()) {

			c.sendMessage("@blu@Normal players do not have the ability to use the yell command (::donate).");
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

			rank = "[<img=1><col=148200>Moderator</col>@bla@]" + c.playerName + ":@dre@";
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
		/*if (c.playerName.equalsIgnoreCase("Mod trent")) {
			rank = "[<col=5E14A7>Server BITCH</col>][" + Misc.ucFirst(c.playerName) + "]:<col=0000FF>";
		}*/
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
		c.lastYell.reset();
		message = Misc.ucFirst(message);
		PlayerHandler.executeGlobalMessage(rank + message, true);
	}
}

