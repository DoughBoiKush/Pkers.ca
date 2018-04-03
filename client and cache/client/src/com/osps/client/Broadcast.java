package com.osps.client;


/**
 * Represents an in-game message shown.
 * @author Chris
 * @date Aug 29, 2015 10:09:42 PM
 *
 */
public class Broadcast {
	
	private static String message;
	
	public int lifetime;
	
	@SuppressWarnings("static-access")
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int getLifetime() {
		return lifetime;
	}
	
	public static void pushMessage() {
		Client.instance.pushMessage(message, 0, "");
	}

}
