package org.osps.api.store.routing;

import org.osps.model.players.PlayerHandler;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import me.apachenick.host.routing.RouteHttpHandler;
import me.apachenick.host.routing.RouteManifest;
import me.apachenick.preprocessor.type.JadePreprocessor;

/**
 * 
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 */
@RouteManifest(template="/players", method="GET")
public class PlayersRoute extends RouteHttpHandler<JadePreprocessor> {	
	
	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		
		int count = 0;
		
		for(int i = 0; i < PlayerHandler.players.length; i++) { 
			if (PlayerHandler.players[i] != null && PlayerHandler.players[i].isActive) {
				count++;
			}
		}
		
		exchange.getResponseHeaders().put(HttpString.tryFromString("Access-Control-Allow-Origin"), "*");
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
		exchange.getResponseSender().send(""+count);
		
	}
	
}