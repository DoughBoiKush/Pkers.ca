package org.osps.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.osps.model.players.Player;

/**
 * MySQL Class
 * 
 * @author Ryan / Lmctruck30
 * 
 */

public class MysqlManager {

	/** MySQL Connection */
	public static Connection conn = null;
	public static Statement statement = null;
	public static ResultSet results = null;

/*	public static String MySQLDataBase = "ospsps_score";
	public static String MySQLURL = "158.69.208.31";
	public static String MySQLUser = "guthixps_high";
	public static String MySQLPassword = "4a}t;}?S5-ze";*/
	public static String MySQLDataBase = "ospsps_score";
	public static String MySQLURL = "localhost";
	public static String MySQLUser = "root";//guthixps_high
	public static String MySQLPassword = "";//4a}t;}?S5-ze
	/**
	 * Creates a Connection to the MySQL Database
	 */
	public synchronized static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			 conn =
			 DriverManager.getConnection("jdbc:mysql://" + MySQLURL + "/" + MySQLDataBase,
			 MySQLUser, MySQLPassword);
			 Misc.println("MySQL Connected");
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public synchronized static void destroyConnection() {
		try {
			statement.close();
			conn.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public synchronized static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = statement.executeQuery(s);
				return rs;
			} else {
				statement.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			destroyConnection();
			createConnection();
			// e.printStackTrace();
		}
		return null;
	}

	/**
	 * Save Sessions HighScores
	 * 
	 * @param clientToSave
	 *            The session that saves their stats
	 * @return The flag true if successful
	 */
	public synchronized static boolean saveHighScore(Player clientToSave) {
		try {
			query("DELETE FROM `hs_users` WHERE playerName = '"
					+ clientToSave.playerName + "';");
			query("DELETE FROM `hs_users` WHERE playerName = '"
					+ clientToSave.playerName + "';");
			query("INSERT INTO `hs_users` (`playerName`, `rights`, `difficulty`, `Attack_lvl`,`Attack_xp`,`Defence_lvl`,`Defence_xp`,`Strength_lvl`,`Strength_xp`,`Hitpoints_lvl`,`Hitpoints_xp`,`Range_lvl`,`Range_xp`,`Prayer_lvl`,`Prayer_xp`,`Magic_lvl`,`Magic_xp`,`Cooking_lvl`,`Cooking_xp`,`Woodcutting_lvl`,`Woodcutting_xp`,`Fletching_lvl`,`Fletching_xp`,`Fishing_lvl`,`Fishing_xp`,`Firemaking_lvl`,`Firemaking_xp`,`Crafting_lvl`,`Crafting_xp`,`Smithing_lvl`,`Smithing_xp`,`Mining_lvl`,`Mining_xp`,`Herblore_lvl`,`Herblore_xp`,`Agility_lvl`,`Agility_xp`,`Thieving_lvl`,`Thieving_xp`,`Slayer_lvl`,`Slayer_xp`,`Farming_lvl`,`Farming_xp`,`Runecraft_lvl`,`Runecraft_xp`) VALUES ('"
					+ clientToSave.playerName
					+ "',"
					+ clientToSave.getRights().getValue()
					+ "',"
					+ clientToSave.getRights().getValue()
					+ "',"
					+ clientToSave.playerLevel[0]
					+ ","
					+ clientToSave.playerXP[0]
					+ ","
					+ clientToSave.playerLevel[1]
					+ ","
					+ clientToSave.playerXP[1]
					+ ","
					+ clientToSave.playerLevel[2]
					+ ","
					+ clientToSave.playerXP[2]
					+ ","
					+ clientToSave.playerLevel[3]
					+ ","
					+ clientToSave.playerXP[3]
					+ ","
					+ clientToSave.playerLevel[4]
					+ ","
					+ clientToSave.playerXP[4]
					+ ","
					+ clientToSave.playerLevel[5]
					+ ","
					+ clientToSave.playerXP[5]
					+ ","
					+ clientToSave.playerLevel[6]
					+ ","
					+ clientToSave.playerXP[6]
					+ ","
					+ clientToSave.playerLevel[7]
					+ ","
					+ clientToSave.playerXP[7]
					+ ","
					+ clientToSave.playerLevel[8]
					+ ","
					+ clientToSave.playerXP[8]
					+ ","
					+ clientToSave.playerLevel[9]
					+ ","
					+ clientToSave.playerXP[9]
					+ ","
					+ clientToSave.playerLevel[10]
					+ ","
					+ clientToSave.playerXP[10]
					+ ","
					+ clientToSave.playerLevel[11]
					+ ","
					+ clientToSave.playerXP[11]
					+ ","
					+ clientToSave.playerLevel[12]
					+ ","
					+ clientToSave.playerXP[12]
					+ ","
					+ clientToSave.playerLevel[13]
					+ ","
					+ clientToSave.playerXP[13]
					+ ","
					+ clientToSave.playerLevel[14]
					+ ","
					+ clientToSave.playerXP[14]
					+ ","
					+ clientToSave.playerLevel[15]
					+ ","
					+ clientToSave.playerXP[15]
					+ ","
					+ clientToSave.playerLevel[16]
					+ ","
					+ clientToSave.playerXP[16]
					+ ","
					+ clientToSave.playerLevel[17]
					+ ","
					+ clientToSave.playerXP[17]
					+ ","
					+ clientToSave.playerLevel[18]
					+ ","
					+ clientToSave.playerXP[18]
					+ ","
					+ clientToSave.playerLevel[19]
					+ ","
					+ clientToSave.playerXP[19]
					+ ","
					+ clientToSave.playerLevel[20]
					+ ","
					+ clientToSave.playerXP[20] + ");");
			query("INSERT INTO `skillsoverall` (`playerName`,`lvl`,`xp`) VALUES ('"
					+ clientToSave.playerName
					+ "',"
					+ (Player.getLevelForXP(clientToSave.playerXP[0])
							+ Player
									.getLevelForXP(clientToSave.playerXP[1])
							+ Player
									.getLevelForXP(clientToSave.playerXP[2])
							+ Player
									.getLevelForXP(clientToSave.playerXP[3])
							+ Player
									.getLevelForXP(clientToSave.playerXP[4])
							+ Player
									.getLevelForXP(clientToSave.playerXP[5])
							+ Player
									.getLevelForXP(clientToSave.playerXP[6])
							+ Player
									.getLevelForXP(clientToSave.playerXP[7])
							+ Player
									.getLevelForXP(clientToSave.playerXP[8])
							+ Player
									.getLevelForXP(clientToSave.playerXP[9])
							+ Player
									.getLevelForXP(clientToSave.playerXP[10])
							+ Player
									.getLevelForXP(clientToSave.playerXP[11])
							+ Player
									.getLevelForXP(clientToSave.playerXP[12])
							+ Player
									.getLevelForXP(clientToSave.playerXP[13])
							+ Player
									.getLevelForXP(clientToSave.playerXP[14])
							+ Player
									.getLevelForXP(clientToSave.playerXP[15])
							+ Player
									.getLevelForXP(clientToSave.playerXP[16])
							+ Player
									.getLevelForXP(clientToSave.playerXP[17])
							+ Player
									.getLevelForXP(clientToSave.playerXP[18])
							+ Player
									.getLevelForXP(clientToSave.playerXP[19]) + Player
								.getLevelForXP(clientToSave.playerXP[20]))
					+ ","
					+ ((clientToSave.playerXP[0]) + (clientToSave.playerXP[1])
							+ (clientToSave.playerXP[2])
							+ (clientToSave.playerXP[3])
							+ (clientToSave.playerXP[4])
							+ (clientToSave.playerXP[5])
							+ (clientToSave.playerXP[6])
							+ (clientToSave.playerXP[7])
							+ (clientToSave.playerXP[8])
							+ (clientToSave.playerXP[9])
							+ (clientToSave.playerXP[10])
							+ (clientToSave.playerXP[11])
							+ (clientToSave.playerXP[12])
							+ (clientToSave.playerXP[13])
							+ (clientToSave.playerXP[14])
							+ (clientToSave.playerXP[15])
							+ (clientToSave.playerXP[16])
							+ (clientToSave.playerXP[17])
							+ (clientToSave.playerXP[18])
							+ (clientToSave.playerXP[19]) + (clientToSave.playerXP[20]))
					+ ");");
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Save Voting Point Info
	 * 
	 * @param c
	 *            The session's client
	 * @return The flag if true was successful
	 */
	public static boolean saveVotingInfo(Player c) { // this class is not being used
		try {
			query("INSERT INTO `skills` (`playerName`,`playerPass') VALUES ('"
					+ c.playerName + "'," + c.playerPass + ");");
			//////////////////////////////////// READ THIS ///////////////////////////////////////
			//																					//
			// 				WHOEVER DID THIS, DON'T EVER DO THIS AGAIN, EVER				 	//
			// INSERTING PASSWORDS IN A DATABASE IS ONE OF THE MOST STUPID THINGS YOU CAN DO 	//
			// 		UNLESS YOU WANT YOUR ENTIRE SERVER TO GET FUCKED OVER ROYALLY, STOP IT	 	//
			//																					//
			//////////////////////////////////////////////////////////////////////////////////////
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
		return true;
	}

	public static int loadVotingPoints(Player c) {
		try {
			ResultSet group = statement
					.executeQuery("SELECT * FROM user WHERE username = '"
							+ c.playerName + "'");
			while (group.next()) {
				String groupp = group.getString("usergroupid");
				int mgroup = Integer.parseInt(groupp);
				if (mgroup > 0) {
					return mgroup;
				}
				return 0;
			}
		} catch (Exception e) {
			return -1;
		}
		return -1;
	}

	public static int loadDonationPoints(Player c) {
		try {
			ResultSet group = statement
					.executeQuery("SELECT * FROM user WHERE username = '"
							+ c.playerName + "'");
			while (group.next()) {
				String groupp = group.getString("usergroupid");
				int mgroup = Integer.parseInt(groupp);
				if (mgroup > 0) {
					return mgroup;
				}
				return 0;
			}
		} catch (Exception e) {
			return -1;
		}
		return -1;
	}

}
