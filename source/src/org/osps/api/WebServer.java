package org.osps.api;

import org.osps.api.store.StoreVirtualHost;

import me.apachenick.server.ServerController;

/**
 * 
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 */
public class WebServer {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		initialize();
	}

	/**
	 * 
	 */
	public static void initialize() {
		
		ServerController server = new ServerController("0.0.0.0", 8080, 8443);
		
		server.register(new StoreVirtualHost());
		server.inititialize();
		
	}
	
}
