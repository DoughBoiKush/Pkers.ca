package org.osps.model.players;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OnlinePlayerConnection {
	public static Connection con;
	public static Statement stm;
        public static boolean connected;
	
	public static String Host = "jdbc:mysql://192.99.148.171/osps_pcount";
	public static String User = "osps_pcountr";
	public static String Pass = "lol1235";
	
    public static void process() {
        try
        {
            Class.forName(Driver).newInstance();
	    Connection con = DriverManager.getConnection(Host, User, Pass);
	    stm = con.createStatement();
            connected = true;
        }
        catch(Exception e)
        {
            connected = false;
            System.out.println(e + " | PROCESSING");
        }
    }

    public static ResultSet query(String s)
        throws SQLException
    {
        if(s.toLowerCase().startsWith("select"))
        {
            ResultSet resultset = stm.executeQuery(s);
            return resultset;
        }
        try
        {
            stm.executeUpdate(s);
            return null;
        }
        catch(Exception e)
        {
            destroy();
        }
        process();
        return null;
    }

    public static void destroy() {
        try
        {
            stm.close();
            con.close();
            connected = false;
        }
        catch(Exception e)
        {
            System.out.println(e + " | ERROR ON DESTROY");
        }
    }
    
    public static boolean push(int PlayerCount) {
        try
        {
	    query("UPDATE `PlayerCount` SET `OnlineNumber`=" + PlayerCount + " WHERE 1");
		}
        catch(Exception e)
        {
           	System.out.println(e + " | SAVING");
            return false;
        }
        return true;
    }
	public static String Driver = "com.mysql.jdbc.Driver";
}