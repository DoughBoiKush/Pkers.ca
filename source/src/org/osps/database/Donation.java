package org.osps.database;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

import org.osps.Config;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Misc;

import com.mysql.jdbc.Statement;
import com.sun.security.ntlm.Client;

public class Donation {

  public static void checkDonation(String username, Player c) throws SQLException {
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("Where is your MySQL JDBC Driver?");
		e.printStackTrace();
		return;
	}
	Connection connection = null;
	Statement stmt = null;

	try {
		connection = DriverManager
		.getConnection("jdbc:mysql://164.132.233.45/osps_payment", "osps_pay", "qflT-ELftx#g");

	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return;
	}

	if (connection != null) {
		 stmt = (Statement) connection.createStatement();
	     String sql;
	     sql = "SELECT * FROM fx_payments WHERE claimed = 0 AND username = '"+username+"'";
	     ResultSet rs = stmt.executeQuery(sql);
	     while(rs.next()) {
	    	 int prod1 = Integer.parseInt(rs.getString("name1"));
				int prod2 = Integer.parseInt(rs.getString("name2"));
				int prod3 = Integer.parseInt(rs.getString("name3"));
				
				if (prod1 == 1) {
				c.sendMessage("Thanks for your donation, @blu@"+c.playerName+"@bla@!");
				c.getItems().addItem(995, 1000000);
					for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Player c2 = (Player)PlayerHandler.players[j];
					c2.sendMessage("<img=8> <col=255>"+Misc.capitalize(c.playerName)+" <col=0>has just donated to help support the server! <col=255>::store");							}
					}
					
				} else if (prod2 == 1) {
					c.sendMessage("Thanks for your donation, @blu@"+c.playerName+"@bla@!");
					c.getItems().addItem(995, 2500000);
								for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Player c2 = (Player)PlayerHandler.players[j];
					c2.sendMessage("<img=8> <col=255>"+Misc.capitalize(c.playerName)+" <col=0>has just donated to help support the server! <col=255>::store");								}
					}
					
				} else if (prod3 == 1) {
					c.sendMessage("Thanks for your donation, @blu@"+c.playerName+"@bla@!");
					c.getItems().addItem(995, 5000000);
								for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					Player c2 = (Player)PlayerHandler.players[j];
					c2.sendMessage("<img=8> <col=255>"+Misc.capitalize(c.playerName)+" <col=0>has just donated to help support the server! <col=255>::store");								}
					}
					
				}
			}
	     sql = "UPDATE fx_payments SET claimed = 1 WHERE `username` = '" +username+ "'";
	     stmt.execute(sql);
	     rs.close();
	     stmt.close();
	     connection.close();

	} else {
		System.out.println("Failed to make connection!");
	}
  }
}