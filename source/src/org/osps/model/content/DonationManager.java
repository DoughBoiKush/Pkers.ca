
package org.osps.model.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.osps.model.players.Player;
public class DonationManager {
	public static void gpay(Player c, String username) {
		 try {
		  username = username.replaceAll(" ", "_");
		  String secret = "8ace881b1f32ce53519b9d2b741f7c1c"; //YOUR SECRET KEY!
		  URL url = new URL("http://app.gpay.io/api/runescape/" + username + "/" + secret);
		  BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		  String results = reader.readLine();
		  if (results.toLowerCase().contains("!error:")) {
		   //Logger.log(this, "[GPAY]"+results);
		  } else {
		   String[] ary = results.split(",");
		   for (int i = 0; i < ary.length; i++) {
		    switch (ary[i]) {
		     case "0":
		      c.sendMessage("No Donations Found:");
		      break;
		     case "28265": //product ids can be found on the webstore page
		    	 c.getItems().addItemToBank(15098, 1);
		    	 c.sendMessage("You've received a donator Item (Dice)x1");
		    	 break;
		     case "SECONDPRODUCTID": //product ids can be found on the webstore page
		      //add items for the second product here!
		      break;
		    }
		   }
		  }
		 } catch (IOException e) {}
		}
}
