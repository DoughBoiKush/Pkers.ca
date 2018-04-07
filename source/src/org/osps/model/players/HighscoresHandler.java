package org.osps.model.players;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class HighscoresHandler implements Runnable {
	private final String HOST = "158.69.127.63"; //Testing: 158.69.208.31
	private final String DATABASE = "ospsps_highscores";
	private final String PASSWORD = "OXyTrc(](T!o";
	private final String USERNAME = "ospsps_high";
	private final String PORT = "3306";
	
	private Player c;
	private Connection con;
	private Statement stmt;
	long totalLevel, totalXP;
	final int NORMAL = 0, IRON = 1;
	final int[] skillOrder = {0, 2, 1, 4, 5, 6, 3, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 21}; //21 unused, construction placeholder
	
	public HighscoresHandler(Player c) {
		this.c = c;

	}
	private long getTotalLevelIron() {
		long totalLevel = 0L;
		for(int i = 0; i <= 22; i++) {
			if(Player.getLevelForXP(c.playerXP[i]) >= 99) 
				totalLevel += 99;
			else
				totalLevel += (double) Player.getLevelForXP(c.playerXP[i]);
		}
		return totalLevel;
	}
	private long getTotalXpIron() {
		long totalXP = 0L;
		for(int i = 0; i <= 22; i++) {
			totalXP += (double) c.playerXP[i];
		}
		return totalXP;
	}
	
	private long getTotalLevelNormal() { 
		long totalLevel = 0L;
		for(int i = 7; i <= 22; i++) {
			totalLevel += (double) Player.getLevelForXP(c.playerXP[i]);
		}
		return totalLevel;
	}
	private long getTotalXpNormal() {
		long totalXP = 0L;
		for(int i = 7; i <= 22; i++) {
			totalXP += (double) c.playerXP[i];
		}
		return totalXP;
	}
	
	@Override
	public void run() {
		try {
			if(c.ironman) {
				totalLevel = getTotalLevelIron();
				totalXP = getTotalXpIron();
			} else {
				totalLevel = getTotalLevelNormal();
				totalXP = getTotalXpNormal();
			}
			if (!makeConnection() || stmt == null) {
				destroyConnection();
				return;
			}
			String name = c.playerName;

			if (!makeConnection() || stmt == null) {
				System.err.println("Failing to update "+name+" highscores. Database could not connect.");
				return;
			}

			PreparedStatement stmt1 = con.prepareStatement("DELETE FROM `osps_hs` WHERE `name`=?");
			stmt1.setString(1, name);
			stmt1.execute();

			PreparedStatement stmt2 = con.prepareStatement(generateQuery());
			stmt2.setString(1, name);
			stmt2.setInt(2, c.getRights().getValue());
			stmt2.setLong(3, c.ironman?getTotalLevelIron():getTotalLevelNormal());
			stmt2.setInt(4, c.ironman?IRON:NORMAL);
			stmt2.setLong(5, c.ironman?getTotalXpIron():getTotalXpNormal());
			for(int i = 0; i < skillOrder.length; i++) {
				if(skillOrder[i] < 7) {
					if (c.ironman) stmt2.setInt(6 + i, c.playerXP[skillOrder[i]]);
					else stmt2.setInt(6 + i, 0);
				} else
					stmt2.setInt(6 + i, c.playerXP[skillOrder[i]]);
			}
			stmt2.execute();

			destroyConnection();
		} catch(SQLException e) {
				e.printStackTrace();
		}
	}
	
	public ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stmt.executeQuery(s);
				return rs;
			}
			stmt.executeUpdate(s);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private void destroyConnection() {
		try {
			this.con = null;
			this.stmt = null;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	private boolean makeConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":"+PORT+"/"+ DATABASE, USERNAME, PASSWORD);
			stmt = con.createStatement();
			if(stmt == null)
				return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static String generateQuery() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO `osps_hs` (");
		sb.append("name, ");
		sb.append("rank, ");
		sb.append("total_level, ");
		sb.append("ironman, ");
		sb.append("overall, ");
		sb.append("attack, ");
		sb.append("strength, ");
		sb.append("defence, ");
		sb.append("ranged, ");
		sb.append("prayer, ");
		sb.append("magic, ");
		sb.append("hitpoints, ");
		sb.append("cooking, ");
		sb.append("woodcutting, ");
		sb.append("fletching, ");
		sb.append("fishing, ");
		sb.append("firemaking, ");
		sb.append("crafting, ");
		sb.append("smithing, ");
		sb.append("mining, ");
		sb.append("herblore, ");
		sb.append("agility, ");
		sb.append("thieving, ");
		sb.append("slayer, ");
		sb.append("farming, ");
		sb.append("runecrafting, ");
		sb.append("hunter, ");
		sb.append("construction) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		return sb.toString();
	}
}
