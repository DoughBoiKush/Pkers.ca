package org.osps.model.players;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;


public class PKHighscoresHandler implements Runnable {
	/**
	 * Testing:
	 * 158.69.208.31
	 */
	/**
	 * Original:
	 * 192.99.148.171
	 */
	private final String HOST = "158.69.127.63";
	private final String DATABASE = "ospsps_highscores";
	private final String PORT = "3306";
	private final String USERNAME = "ospsps_high";
	private final String PASSWORD = "OXyTrc(](T!o";
	
	private Player c;
	private Connection con;
	private Statement stmt;
	
	public PKHighscoresHandler(Player c) {
		this.c = c;
	}

	@Override
	public void run() {
		try {
			DecimalFormat df = new DecimalFormat("#.##");
			double kdr = c.KC;
			if(c.DC > 0)
				kdr /= c.DC;
			if(!makeConnection() || stmt == null) {
				destroyConnection();
				return;
			}

			ResultSet rs = query("SELECT * FROM `osps_pk` WHERE `name`='"+c.playerName+"'");
			if(rs.next()) {
				query("UPDATE `osps_pk` " +
						"SET " +
						"`totalkills`='" + c.KC + "'," +
						"`totaldeaths`='" + c.DC + "'," +
						"`rogue`='" + c.getBH().getTotalRogueKills() + "'," +
						"`hunter`='" + c.getBH().getTotalHunterKills() + "'," +
						"`kdr`='" + df.format(kdr) + "'," +
						"`killstreak`='" + c.killStreak + "'," +
						"`rank`='" + c.getRights().getValue() + "' " +
						"WHERE `name`='" + c.playerName + "'");
			} else {
				query("INSERT INTO `osps_pk`(`name`, `totalkills`, `totaldeaths`, `rogue`, `hunter`, `kdr`, `killstreak`, `rank`) " +
						"VALUES ('" + c.playerName + "', '"+c.KC+"', '"+c.DC+"', '"+c.getBH().getTotalRogueKills()+"'," +
							" '"+c.getBH().getTotalHunterKills()+"', '"+df.format(kdr)+"', '"+c.killStreak+"', '"+c.getRights().getValue()+"')");
			}
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
}
