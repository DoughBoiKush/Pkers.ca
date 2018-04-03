package org.osps.model.content.clan;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import org.osps.Config;
import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Misc;

/**
 * This class stores all information about the clan. This includes
 * active members, banned members, ranked members and their ranks,
 * clan title, and clan founder. All clan joining, leaving, and
 * moderation/setup is also handled in this class.
 * @author Galkon
 * 
 */
public class Clan {
	
	public Clan[] clans = new Clan[100];

	/**
	 * Adds a member to the clan.
	 * @param player
	 */
	public void addMember(Player player) {
		player.sendMessage("Attempting to join channel...");
		if (isBanned(player.playerName)) {
			player.sendMessage("You are currently banned from this clan chat.</col>");
			return;
		}
		if (whoCanJoin > Rank.ANYONE && !isFounder(player.playerName)) {
			if (getRank(player.playerName) < whoCanJoin) {
				player.sendMessage("Only " + getRankTitle(whoCanJoin)
						+ "s+ may join this chat.");
				return;
			}
		}
		player.clan = this;
		player.lastClanChat = getFounder();
		activeMembers.add(player.playerName);
		player.getPA().sendFrame126("Leave Chat", 18135);
		player.getPA().sendFrame126(
				"Talking in: <col=FFFF75>" + getTitle() + "</col>", 18139);
		player.getPA().sendFrame126(
				"Owner: <col=FFFFFF>" + Misc.formatPlayerName(getFounder())
						+ "</col>", 18140);
		player.sendMessage("Now talking in clan channel @blu@" + getTitle()
				+ "@bla@");
		player.sendMessage("To talk, start each line of chat with the / symbol.");
		updateMembers();
	}
	
	/**
	 * Adds the player to previous clan chat upon login.
	 * @param player
	 */
	
	public void LoginStageClan(Player player) {
		player.sendMessage("Attempting to join channel...");
		if (isBanned(player.playerName)) {
			player.sendMessage("You are currently banned from this clan chat.</col>");
			return;
		}
		if (whoCanJoin > Rank.ANYONE && !isFounder(player.playerName)) {
			if (getRank(player.playerName) < whoCanJoin) {
				player.sendMessage("Only " + getRankTitle(whoCanJoin)
						+ "s+ may join this chat.");
				return;
			}
		}
		player.clan = this;
		player.lastClanChat = getFounder();
		activeMembers.add(player.playerName);
		player.getPA().sendFrame126("Leave Chat", 18135);
		player.getPA().sendFrame126(
				"Talking in: <col=FFFF75>" + getTitle() + "</col>", 18139);
		player.getPA().sendFrame126(
				"Owner: <col=FFFFFF>" + Misc.formatPlayerName(getFounder())
						+ "</col>", 18140);
		player.sendMessage("Now talking in clan channel @blu@" + getTitle()
				+ "@bla@");
		player.sendMessage("To talk, start each line of chat with the / symbol.");
		updateMembers();
	}

	/**
	 * Removes the player from the clan.
	 * @param player
	 */
	public void removeMember(Player player) {
		List<String> remove = new ArrayList<>(1);
		for (String member : activeMembers) {
			if (Objects.isNull(member)) {
				continue;
			}
			if (member.equalsIgnoreCase(player.playerName)) {
				player.clan = null;
				resetInterface(player);
				remove.add(member);
			}
		}
		activeMembers.removeAll(remove);
		player.getPA().refreshSkill(21);
		player.getPA().refreshSkill(22);
		player.getPA().refreshSkill(23);
		updateMembers();
	}

	/**
	 * Removes the player from the clan.
	 * @param player
	 */
	public void removeMember(String name) {
		List<String> remove = new ArrayList<>(1);
		for (String member : activeMembers) {
			if (Objects.isNull(member)) {
				continue;
			}
			if (member.equalsIgnoreCase(name)) {
				Player player = PlayerHandler.getPlayer(name);
				player.clan = null;
				resetInterface((Player) player);
				remove.add(member);
			}
		}
		activeMembers.removeAll(remove);
		updateMembers();
	}

	/**
	 * Updates the members on the interface for the player.
	 * @param player
	 */
	public void updateInterface(Player player) {
		player.getPA().sendFrame126(
				"Talking in: <col=FFFF75>" + getTitle() + "</col>", 18139);
		player.getPA().sendFrame126(
				"Owner: <col=FFFFFF>" + Misc.formatPlayerName(getFounder())
						+ "</col>", 18140);
		Collections.sort(activeMembers);
		for (int index = 0; index < 100; index++) {
			if (index < activeMembers.size()) {
				player.getPA().sendFrame126(
						"<clan="
								+ getRank(activeMembers.get(index))
								+ ">"
								+ Misc.formatPlayerName(activeMembers
										.get(index)), 18144 + index);
			} else {
				player.getPA().sendFrame126("", 18144 + index);
			}
		}
	}

	/**
	 * Updates the interface for all members.
	 */
/*	public void updateMembers() {
		// gonna try something
		
		for (String s : activeMembers) {
			Player p = PlayerHandler.getPlayer(s);
			//if (p != null) {
				updateInterface(p);
				updateNewInterface(p);
			//}
		}	
	}*/
	public void updateMembers() {
		for (Player player : PlayerHandler.getPlayers()) {
			if (Objects.nonNull(activeMembers) && Objects.nonNull(player)) {
				if (activeMembers.contains(player.playerName)) {
					updateInterface((Player) player);
					updateNewInterface((Player) player);
				}
			}
		}
	}
	
	public void updateNewInterface(Player player) {
		if(getTeleport()){
			player.getPA().sendFrame126("@gre@Yes", 18530);
		} else if(!getTeleport()){
			player.getPA().sendFrame126("@red@No", 18530);
		}
		if(getCanCopy()){
			player.getPA().sendFrame126("@gre@Yes", 18533);
		} else if(!getCanCopy()){
			player.getPA().sendFrame126("@red@No", 18533);
		}
	}

	/**
	 * Resets the clan interface.
	 * @param player
	 */
	public void resetInterface(Player player) {
		player.getPA().sendFrame126("Join chat", 18135);
		player.getPA().sendFrame126("Talking in: Not in chat", 18139);
		player.getPA().sendFrame126("Owner: None", 18140);
		for (int index = 0; index < 100; index++) {
			player.getPA().sendFrame126("", 18144 + index);
		}
	}

	/**
	 * Sends a message to the clan.
	 * @param player
	 * @param message
	 */
	
	public void sendChat(Player paramClient, String paramString) {
		if (getRank(paramClient.playerName) < this.whoCanTalk) {
			paramClient.sendMessage("Only " + getRankTitle(this.whoCanTalk) + "s+ may talk in this chat.");
			return;
		}
		if (System.currentTimeMillis() < paramClient.muteEnd) {
			paramClient.sendMessage("You are muted, you cannot talk in this chat.");
			return;
		}
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				Player c = PlayerHandler.players[j];
				if ((c != null) && (this.activeMembers.contains(c.playerName)))
					//paramString.replace("/", "")
					c.sendMessage("@bla@[@blu@" + getTitle() + "@bla@] <img=" + (paramClient.getRights().getValue() - 1) + ">@bla@"
							+ Misc.optimizeText(paramClient.playerName) + ": @dre@" + paramString.replace("/", ""));
			}
		}
	}

	/**
	 * Sends a message to the clan.
	 * 
	 * @param player
	 * @param message
	 */
	public void sendMessage(String message) {
		for (int index = 0; index < Config.MAX_PLAYERS; index++) {
			Player p = PlayerHandler.players[index];
			if (p != null) {
				if (activeMembers.contains(p.playerName)) {
					p.sendMessage(message);
				}
			}
		}
	}

	/**
	 * Sends a message to the clan.
	 * @param player
	 * @param message
	 */
	/*public void sendMessage(String message) {
		for (int index = 0; index < Config.MAX_PLAYERS; index++) {
			Player p = (Player) PlayerHandler.players[index];
			if (p != null) {
				if (activeMembers.contains(p.playerName)) {
					p.sendMessage(message);
				}
			}
		}
	}*/

	/**
	 * Sets the rank for the specified name.
	 * @param name
	 * @param rank
	 */
	public void setRank(String name, int rank) {
		if (rankedMembers.contains(name)) {
			ranks.set(rankedMembers.indexOf(name), rank);
		} else {
			rankedMembers.add(name);
			ranks.add(rank);
		}
		save();
	}

	/**
	 * Demotes the specified name.
	 * @param name
	 */
	public void demote(String name) {
		if (!rankedMembers.contains(name)) {
			return;
		}
		int index = rankedMembers.indexOf(name);
		rankedMembers.remove(index);
		ranks.remove(index);
		save();
	}

	/**
	 * Gets the rank of the specified name.
	 * @param name
	 * @return
	 */
	public int getRank(String name) {
		name = Misc.formatPlayerName(name);
		if (rankedMembers.contains(name)) {
			return ranks.get(rankedMembers.indexOf(name));
		}
		if (isFounder(name)) {
			return Rank.OWNER;
		}
		//XXX Problem was here for fixing project folder
		return -1;
	}

	/**
	 * Can they kick?
	 * @param name
	 * @return
	 */
	public boolean canKick(String name) {
		if (isFounder(name)) {
			return true;
		}
		if (getRank(name) >= whoCanKick) {
			return true;
		}
		return false;
	}
	public boolean canTeleport;
	public boolean canCopy;

	/**
	 * Can they ban?
	 * @param name
	 * @return
	 */
	public boolean canBan(String name) {
		if (isFounder(name)) {
			return true;
		}
		if (getRank(name) >= whoCanBan) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is the founder.
	 * @param name
	 * @return
	 */
	public boolean isFounder(String name) {
		if (getFounder().equalsIgnoreCase(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is a ranked user.
	 * @param name
	 * @return
	 */
	public boolean isRanked(String name) {
		name = Misc.formatPlayerName(name);
		if (rankedMembers.contains(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether or not the specified name is banned.
	 * @param name
	 * @return
	 */
	public boolean isBanned(String name) {
		name = Misc.formatPlayerName(name);
		if (bannedMembers.contains(name)) {
			return true;
		}
		return false;
	}

	/**
	 * Kicks the name from the clan chat.
	 * @param name
	 */
	public void kickMember(String name) {
		if (!activeMembers.contains(name)) {
			return;
		}
		if (name.equalsIgnoreCase(getFounder())) {
			return;
		}
		removeMember(name);
		Player player = (Player) PlayerHandler.getPlayer(name);
		if (player != null) {
			player.sendMessage("You have been kicked from the clan chat.");
		}
		sendMessage(Misc.formatPlayerName(name) + " has been kicked from the clan chat.");
	}

	/**
	 * Bans the name from entering the clan chat.
	 * @param name
	 */
	public void banMember(String name) {
		name = Misc.formatPlayerName(name);
		if (bannedMembers.contains(name)) {
			return;
		}
		if (name.equalsIgnoreCase(getFounder())) {
			return;
		}
		if (isRanked(name)) {
			return;
		}
		removeMember(name);
		bannedMembers.add(name);
		save();
		Player player = (Player) PlayerHandler.getPlayer(name);
		if (player != null && player.clan == this) {
			player.sendMessage("You have been banned from the clan chat.");
		}
		sendMessage(Misc.formatPlayerName(name) + " has been banned from the clan chat.");
	}

	/**
	 * Unbans the name from the clan chat.
	 * @param name
	 */
	public void unbanMember(String name) {
		name = Misc.formatPlayerName(name);
		if (bannedMembers.contains(name)) {
			bannedMembers.remove(name);
			save();
		}
	}

	/**
	 * Saves the clan.
	 */
	public void save() {
		Server.clanManager.save(this);
		updateMembers();
	}

	/**
	 * Deletes the clan.
	 */
	public void delete() {
		for (String name : activeMembers) {
			removeMember(name);
			Player player = (Player) PlayerHandler.getPlayer(name);
			player.sendMessage("The clan you were in has been deleted.");
		}
		Server.clanManager.delete(this);
	}

	/**
	 * Creates a new clan for the specified player.
	 * @param player
	 */
	public Clan(Player player) {
		setTitle(player.playerName + "'s Clan");
		setFounder(player.playerName.toLowerCase());
	}

	/**
	 * Creates a new clan for the specified title and founder.
	 * @param title
	 * @param founder
	 */
	public Clan(String title, String founder) {
		setTitle(title);
		setFounder(founder);
	}

	/**
	 * Gets the founder of the clan.
	 * @return
	 */
	public String getFounder() {
		return founder;
	}

	/**
	 * Sets the founder.
	 * @param founder
	 */
	public void setFounder(String founder) {
		this.founder = founder;
	}
	
	public void allowTeleport(Player player) {
		if(isFounder(player.playerName) && !getTeleport()) {
			setTeleport(true);
			player.getPA().sendFrame126("@gre@Yes", 18530);
			player.sendMessage("You have @blu@enabled@bla@ the ability for clan members to teleport to other clan members!");
			updateMembers();
		} else if(player.clan.isFounder(player.playerName) && getTeleport()) {
			//canTeleport = false;
			setTeleport(false);
			player.getPA().sendFrame126("@red@No", 18530);
			player.sendMessage("You have @blu@disabled@bla@ the ability for clan members to teleport to other clan members!");
			updateMembers();
		}
	}
	
	public void allowCopyKit(Player player) {
		if(player.clan.isFounder(player.playerName) && !getCanCopy()) {
			setCanCopy(true);
			player.getPA().sendFrame126("@gre@Yes", 18533);
			player.sendMessage("You have @blu@enabled@bla@ the ability for clan members to copy other members armour kits!");
			updateMembers();
		} else if(player.clan.isFounder(player.playerName) && getCanCopy()) {
			setCanCopy(false);
			player.getPA().sendFrame126("@red@No", 18533);
			player.sendMessage("You have @blu@disabled@bla@ the ability for clan members to copy other members armour kits!");
			updateMembers();
		}
	}

	/**
	 * Gets the title of the clan.
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Gets the allowTeleport check
	 * @return
	 */
	
	public Boolean getTeleport() {
		return canTeleport;
	}
	
	/**
	 * Sets the teleport to true or false.
	 * @param title
	 * @return
	 */
	public void setTeleport(Boolean teleport) {
		canTeleport = teleport;
	}
	
	/**
	 * Gets the allowTeleport check
	 * @return
	 */
	
	public Boolean getCanCopy() {
		return canCopy;
	}
	
	/**
	 * Sets the teleport to true or false.
	 * @param title
	 * @return
	 */
	public void setCanCopy(Boolean copy) {
		canCopy = copy;
	}


	/**
	 * Sets the title.
	 * @param title
	 * @return
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * The title of the clan.
	 */
	public String title;
	
	public Boolean teleport;

	/**
	 * The founder of the clan.
	 */
	public String founder;

	/**
	 * The active clan members.
	 */
	public LinkedList<String> activeMembers = new LinkedList<>();

	/**
	 * The banned members.
	 */
	public LinkedList<String> bannedMembers = new LinkedList<>();

	/**
	 * The ranked clan members.
	 */
	public LinkedList<String> rankedMembers = new LinkedList<>();

	/**
	 * The clan member ranks.
	 */
	public LinkedList<Integer> ranks = new LinkedList<>();

	/**
	 * The clan ranks.
	 * @author Galkon
	 *
	 */
	public static class Rank {
		public final static int ANYONE = -1;
		public final static int FRIEND = 0;
		public final static int RECRUIT = 1;
		public final static int CORPORAL = 2;
		public final static int SERGEANT = 3;
		public final static int LIEUTENANT = 4;
		public final static int CAPTAIN = 5;
		public final static int GENERAL = 6;
		public final static int OWNER = 7;
	}

	/**
	 * Gets the rank title as a string.
	 * @param rank
	 * @return
	 */
	public String getRankTitle(int rank) {
		switch (rank) {
			case -1:
				return "Anyone";
			case 0:
				return "Friend";
			case 1:
				return "Recruit";
			case 2:
				return "Corporal";
			case 3:
				return "Sergeant";
			case 4:
				return "Lieutenant";
			case 5:
				return "Captain";
			case 6:
				return "General";
			case 7:
				return "Only Me";
		}
		return "";
	}

	/**
	 * Sets the minimum rank that can join.
	 * @param rank
	 */
	public void setRankCanJoin(int rank) {
		whoCanJoin = rank;
	}

	/**
	 * Sets the minimum rank that can talk.
	 * @param rank
	 */
	public void setRankCanTalk(int rank) {
		whoCanTalk = rank;
	}

	/**
	 * Sets the minimum rank that can kick.
	 * @param rank
	 */
	public void setRankCanKick(int rank) {
		whoCanKick = rank;
	}

	/**
	 * Sets the minimum rank that can ban.
	 * @param rank
	 */
	public void setRankCanBan(int rank) {
		whoCanBan = rank;
	}
	
	/**
	 * Clan Wars variables.
	 */
	public Clan belligerent;
	private static boolean isWarHost;
	
	private static boolean isAtWar;
	
	/**
	 * Represents whether the clan has challenged another clan.
	 * @return	true if the clan is the host of a Clan War
	 */
	public boolean isWarHost() {
		return isWarHost;
	}
	
	/**
	 * Gets the clan that this <code>Clan</code> is currently at war with.
	 * @return
	 */
	public Clan getBelligerent() {
		return belligerent;
	}
	
	/**
	 * Gets whether this <code>Clan</clan> is at war.
	 * @return
	 */
	public boolean atWar() {
		return isAtWar;
	}

	/**
	 * The ranks privileges require (joining, talking, kicking, banning).
	 */
	public int whoCanJoin = Rank.ANYONE;
	public int whoCanTalk = Rank.ANYONE;
	public int whoCanKick = Rank.GENERAL;
	public int whoCanBan = Rank.OWNER;
	

}