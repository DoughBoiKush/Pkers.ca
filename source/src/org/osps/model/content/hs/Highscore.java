package org.osps.model.content.hs;

import org.osps.model.players.Player;

public interface Highscore {

	/**
	 * This method will process our information to get a highscore and store it
	 * in a list.
	 */
	public void process();

	/**
	 * This method will return the type of highscore of choice (e.g. PKP, Total
	 * Level,...)
	 *
	 * @return String object
	 */
	public String getType();

	/**
	 * Method for generating our highscore list.
	 * 
	 * @param client
	 *            Client for whom this is to be done.
	 */
	public void generateList(Player client);

	/**
	 * resetting our highscore interface
	 */
	public void resetList(Player client);
}
