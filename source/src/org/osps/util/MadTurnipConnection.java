package org.osps.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.osps.Config;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;

@SuppressWarnings("unused")
public class MadTurnipConnection extends Thread {

	public static Connection con = null;
	public static Statement stm;
	public static String Host = "jdbc:mysql://158.69.208.31/osps_payment";
	public static String User = "osps_pay";
	public static String Pass = "qflT-ELftx#g";
    
	public static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(Host, User, Pass);
			stm = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
			stm = null;
		}
	}
	
	public MadTurnipConnection(){
		
	}
	
	public void run() {
		while(true) {		
			try {
				if(con == null)
					createConnection(); 
				else
					ping();
				Thread.sleep(10000);//10 seconds
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void ping(){
		try {
			String query = "SELECT * FROM donation WHERE username = 'null'";
			query(query);
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
			stm = null;
		}
	}
	
	public static void addDonateItems(final Player c,final String name){
		if(con == null){
			if(stm != null){
				try {
					stm = con.createStatement();
				} catch(Exception e){
					con = null;
					stm = null;
					//put a sendmessage here telling them to relog in 30 seconds
					return;
				}
			} else {
				//put a sendmessage here telling them to relog in 30 seconds
				return;
			}
		}
		new Thread(){
			@Override
			public void run()
			{
				try {
					String name2 = name.replaceAll(" ", "_");
					String query = "SELECT * FROM fx_payments WHERE claimed = 0 AND username = '"
							+ name2 + "'";
					ResultSet rs = query(query);
					boolean b = false;
					while (rs.next()) {
						int prod1 = Integer.parseInt(rs.getString("name1"));
						int prod2 = Integer.parseInt(rs.getString("name2"));
						int prod3 = Integer.parseInt(rs.getString("name3"));
						if (prod1 == 1) {
						c.sendMessage("Thanks for your donation, @blu@"+c.playerName+"@bla@!");
						c.getItems().addItem(995, 1);
							for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							Player c2 = (Player)PlayerHandler.players[j];
							c2.sendMessage("<img=8> <col=255>"+Misc.capitalize(c.playerName)+" <col=0>has just donated to help support the server! <col=255>::store");							}
							}
							b = true;
						} else if (prod2 == 1) {
							c.sendMessage("Thanks for your donation, @blu@"+c.playerName+"@bla@!");
							c.getItems().addItem(995, 1);
										for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							Player c2 = (Player)PlayerHandler.players[j];
							c2.sendMessage("<img=8> <col=255>"+Misc.capitalize(c.playerName)+" <col=0>has just donated to help support the server! <col=255>::store");								}
							}
							b = true;
						} else if (prod3 == 1) {
							c.sendMessage("Thanks for your donation, @blu@"+c.playerName+"@bla@!");
							c.getItems().addItem(995, 1);
										for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							Player c2 = (Player)PlayerHandler.players[j];
							c2.sendMessage("<img=8> <col=255>"+Misc.capitalize(c.playerName)+" <col=0>has just donated to help support the server! <col=255>::store");								}
							}
							b = true;
						}
					}
					if (b) {
						query("UPDATE fx_payments SET claimed = 1 WHERE `username` = '" + name2 + "';");
					}

				} catch (Exception e) {
					e.printStackTrace();
					con = null;
					stm = null;
				}
			}
		}.start();
	}
	
	public static ResultSet query(String s) throws SQLException {
		try {
			if (s.toLowerCase().startsWith("select")) {
				ResultSet rs = stm.executeQuery(s);
				return rs;
			} else {
				stm.executeUpdate(s);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
			stm = null;
		}
		return null;
	}
}