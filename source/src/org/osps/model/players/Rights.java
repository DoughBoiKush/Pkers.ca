package org.osps.model.players;

import java.util.Arrays;

import org.osps.util.Misc;

/**
 * The rights of a player determines their authority. Every right can be viewed
 * with a name and a value. The value is used to separate each right from one another.
 * 
 * @author Jason
 * @date January 22, 2015, 5:23:49 PM
 */
public enum Rights {
	PLAYER(0),
	MODERATOR(1),
	ADMINISTRATOR(2, MODERATOR),
	OWNER(3, ADMINISTRATOR, MODERATOR),
	UNKNOWN(4),
	DONATOR(5), //  contributor
	HONORED_DONATOR(6), // sponsor
	RESPECTED_DONATOR(7), // supporter
	LEGENDARY_DONATOR(8), //vip
	DEVELOPER(9),
	YOUTUBER(10),	
	HELPER(11),
	IRONMAN(12),
	STREAMER(13),
	HIDDEN(15, ADMINISTRATOR, MODERATOR);
	
	/**
	 * The level of rights that define this
	 */
	private final int right;
	
	/**
	 * The right or rights inherited by this right
	 */
	private final Rights[] inherited;
	
	/**
	 * Creates a new right with a value to differentiate it between the others
	 * @param right			the right required
	 * @param inherited		the right or rights inherited with this level of right
	 */
	private Rights(int right, Rights... inherited) {
		this.right = right;
		this.inherited = inherited;
	}
	
	/**
	 * The rights of this enumeration
	 * @return	the rights
	 */
	public int getValue() {
		return right;
	}
	
	/**
	 * Determines if this level of rights inherited another level of rights
	 * @param rights	the level of rights we're looking to determine is inherited
	 * @return			{@code true} if the rights are inherited, otherwise {@code false}
	 */
	public boolean inherits(Rights rights) {
		return equals(rights) || Arrays.asList(inherited).contains(rights);
	}
	
	/**
	 * Determines if the player has rights equal to that of {@linkplain PLAYER}.
	 * @return	true if they are of type player
	 */
	public boolean isPlayer() {
		return equals(PLAYER);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain MODERATOR}
	 * @return	true if they are of type moderator
	 */
	public boolean isModerator() {
		return equals(MODERATOR);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain ADMINISTRATOR}
	 * @return	true if they are of type administrator
	 */
	public boolean isAdministrator() {
		return equals(ADMINISTRATOR);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain OWNER}
	 * @return	true if they are of type owner
	 */
	public boolean isOwner() {
		return equals(OWNER);
	}
	
	public boolean isHidden() {
		return equals(HIDDEN);
	}
	/**
	 * Determines if the players rights equal that of {@linkplain OWNER}
	 * @return	true if they are of type owner
	 */
	public boolean isDeveloper() {
		return equals(DEVELOPER);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain CONTRIBUTOR}
	 * @return	true if they are of type contributor
	 */	
	public boolean isContributor() {
		return equals(DONATOR);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain SPONSOR}
	 * @return	true if they are of type sponsor
	 */	
	public boolean isSponsor() {
		return equals(HONORED_DONATOR);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain SUPPORTER}
	 * @return	true if they are of type supporter
	 */	
	public boolean isSupporter() {
		return equals(RESPECTED_DONATOR);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain V_I_P}
	 * @return	true if they are of type v.i.p
	 */	
	public boolean isVIP() {
		return equals(LEGENDARY_DONATOR);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain SUPER_V_I_P}
	 * @return	true if they are of type super v.i.p
	 */	
	public boolean isYoutuber() {
		return equals(YOUTUBER);
	}
	
	
	/**
	 * Determines if the players rights equal that of {@linkplain HELPER}
	 * @return	true if they are of type helper
	 */	
	public boolean isHelper() {
		return equals(HELPER);
	}
	
	/**
	 * Determines if the players rights equal that of {@linkplain STREAMER}
	 * @return	true if they are of type Streamer
	 */	
	public boolean isStreamer() {
		return equals(STREAMER);
	}
	
	/**
	 * Determines if the Staff team are online
	 * @return	true if they are online
	 */
	public boolean OnlineStaff() {
		return isModerator() || isHelper() || isAdministrator() || isOwner() || isDeveloper();
	}
	
	/**
	 * Determines if the you got a Dollar Mark on your name.
	 */
	public boolean GotDollarMark() {
		return isModerator() || isHelper() || isAdministrator() || isOwner() || isContributor() || isSponsor() || isSupporter() || isVIP() || isDeveloper()
			   || isYoutuber();
	}
	
	/**
	 * Need to be Higher than Donator to open bank up.
	 */
	public boolean HigherDonator() {
		return  isPlayer() || isContributor();
	}
	
	/**
	 * Determines if the players rights equal that of {@link CONTRIBUTOR},
	 * {@link SPONSOR}, {@link SUPPORTER}, {@link V_I_P}, and {@link SUPER_V_I_P}
	 * @return	true if they are any of the predefined types
	 */	
	public boolean isDonator() {
		return isContributor() || isSponsor() || isSupporter() || isVIP();
	}
	
	/**
	 * Determines if the players right equal that of {@link MODERATOR}, {@link ADMINISTRATOR},
	 * and {@link OWNER}
	 * @return	true if they are any of the predefined types
	 */
	public boolean isStaff() {
		return isModerator() || isAdministrator() || isOwner() || isDeveloper() ;
	}
	
	/**
	 * Determines if the players rights are in-between two values.
	 * @param start	the lowest range
	 * @param end	the highest range
	 * @return		true if the rights are greater than the start and lower
	 * than the end value.
	 */
	public boolean isBetween(int start, int end) {
		if (start < 0 || end < 0 || start > end || start == end) {
			throw new IllegalStateException();
		}
		return right >= start && right <= end;
	}
	
	/**
	 * Returns a {@link Rights} object for the value.
	 * @param value	the right level
	 * @return	the rights object
	 */
	public static Rights get(int value) {
		return Arrays.asList(values()).stream().filter(element -> element.right == value).findFirst().orElse(PLAYER);
	}
	
	@Override
	public String toString() {
		return Misc.capitalize(name().toLowerCase());
	}
}
