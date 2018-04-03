package org.osps.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class ConnectionPool {
	
	private static ComboPooledDataSource cpds;
	
	/**
	 * Database credentials.
	 */
	private static String host = "osps.com", db = "osps_payments", pass = "qflT-ELftx#g", user = "ospsps_pay";
	
	/**
	 * Create a new connection pool.
	 */
	public static void createPool() {
		cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
			cpds.setJdbcUrl("jdbc:mysql://" + host + "/" + db);
			cpds.setUser(user);
			cpds.setPassword(pass);
			cpds.setMinPoolSize(10);
			cpds.setAcquireIncrement(10);
			cpds.setMaxPoolSize(100);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Return a free Connection to the database.
	 * @return A free connection.
	 * @throws SQLException
	 */
	
	public static Connection getConnection() throws SQLException {
		if (cpds == null) {
			createPool();
			return cpds.getConnection();
		}
		return cpds.getConnection();
	}
	
    public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
