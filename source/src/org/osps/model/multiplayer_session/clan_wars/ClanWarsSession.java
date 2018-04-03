/**
 * 
 */
package org.osps.model.multiplayer_session.clan_wars;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.osps.model.content.clan.Clan;
import org.osps.model.items.GameItem;
import org.osps.model.multiplayer_session.MultiplayerSession;
import org.osps.model.multiplayer_session.MultiplayerSessionFinalizeType;
import org.osps.model.multiplayer_session.MultiplayerSessionType;
import org.osps.model.players.Player;

/**
 * Handles a session of {@link ClanWars}.
 * @author Chris
 * @date Aug 14, 2015 7:39:52 PM
 *
 */
public class ClanWarsSession extends MultiplayerSession {
	
	//TODO: Session logs.
	
	/**
	 * Constructs a {@code ClanWarsSession}.
	 * @param players
	 * @param type
	 */
	public ClanWarsSession(List<Player> players, MultiplayerSessionType type) {
		super(players, type);
	}
	
	/**
	 * A {@link ArrayList} of all clans currently in the minigame.
	 */
	private static ArrayList<Clan> clans = new ArrayList<>();
	
	private ClanWars war;
	
	ClanWarsRules rules = new ClanWarsRules();
	
	/**
	 * Gets the list of clans actively participating in {@link ClanWars}.
	 * @return
	 */
	public static ArrayList<Clan> getClans() {
		return clans;
	}
	
	/**
	 * Adds the <code>Clan</code> to the <code>ArrayList</code> of active clans.
	 * @param clan	the clan to add
	 */
	public static void addClan(Clan clan) {
		clans.add(clan);
	}
	
	/**
	 * Removes the specified <code>Clan</code> from {@link ClanWars}.
	 * @param clan	the clan to remove
	 * @note <p>Uses <code>Optional</code>.
	 */
	public static void removeClan(Optional<Clan> clan) {
		boolean isWar = clans.stream().filter(c -> c.atWar()).anyMatch(c -> c.getTitle() == clan.get().getTitle());
		if (clan.isPresent() && isWar) {
			clans.remove(clan);
		}
	}
	
	/**
	 * Returns whether the specified clan is currently at war.
	 * @param clan	the clan to check
	 * @return	true if the clan is in {@link ClanWars}.
	 */
	public static boolean isBelligerent(Clan clan) {
		return clans.contains(clan);
	}
	
	@Override
	public void accept(Player player, Player recipient, int stageId) {
		switch(stageId) {
		
		}
	}
	
	@Override
	public void updateOfferComponents() {
		
	}
	
	@Override
	public boolean itemAddable(Player player, GameItem item) {
		return false;
	}
	
	@Override
	public boolean itemRemovable(Player player, GameItem item) {
		return false;
	}
	
	@Override
	public void updateMainComponent() {
		//TODO: This.
	}
	
	@Override
	public void give() {
		//Should we give winners a reward?
	}
	
	@Override
	public void dispose() {
		
	}
	
	@Override
	public void withdraw() {
		
	}
	
	/**
	 * Toggles the current {@link ClanWarsRule}.
	 * @param player	the player
	 * @param rule		the rule being toggled
	 */
	public void toggleRule(Player player, ClanWarsRules rule) {
		//TODO: This as well.
	}
	
	/**
	 * Clears any attributes given to the player during the clan war and moves them back.
	 * @param player
	 */
	public void moveAndClearAttributes(Player player) {
		player.getItems().addSpecialBar(player.playerEquipment[player.playerWeapon]);
		player.getPA().showOption(3, 0, "Challenge", 3);
		player.getPA().createPlayerHints(10, -1);
		player.getPA().movePlayerDuel(PortalType.EXIT.getX(), PortalType.EXIT.getY(), 0);
		player.freezeTimer = 1;
		player.getPA().resetFollow();
		player.getCombat().resetPlayerAttack();
		player.setPoisonDamage((byte) 0);
		player.isSkulled = false;
		player.attackedPlayers.clear();
		player.headIconPk = -1;
		player.skullTimer = -1;
		player.getPA().requestUpdates();
		clearPlayerAttributes(player);
	}
	
	/**
	 * Clears player attributes.
	 * @param player
	 */
	private void clearPlayerAttributes(Player player) {
		for (int i = 0; i < player.playerLevel.length; i++) {
			player.playerLevel[i] = player.getPA().getLevelForXP(player.playerXP[i]);
			player.getPA().refreshSkill(i);
		}
		player.specAmount = 10.0;
		player.resetDamageReceived();
		player.getCombat().resetPrayers();
		player.getPotions().resetOverload();
		player.vengOn = false;
		player.usingSpecial = false;
		player.setPoisonDamage((byte) 0);
		player.getItems().updateSpecialBar();
		player.doubleHit = false;
	}

	/* (non-Javadoc)
	 * @see ab.model.multiplayer_session.MultiplayerSessionLog#logSession(ab.model.multiplayer_session.MultiplayerSessionFinalizeType)
	 */
	@Override
	public void logSession(MultiplayerSessionFinalizeType type) {
		// TODO Auto-generated method stub
		
	}

}
