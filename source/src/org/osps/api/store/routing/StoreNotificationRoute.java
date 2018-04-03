package org.osps.api.store.routing;

import java.util.Map;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import me.apachenick.host.routing.RouteHttpHandler;
import me.apachenick.host.routing.RouteManifest;
import me.apachenick.preprocessor.type.JadePreprocessor;

/**
 * 
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 *
 */
@RouteManifest(template="/", method="GET")
public class StoreNotificationRoute extends RouteHttpHandler<JadePreprocessor> {	
	
	@Override
	public Map<String, Object> model(HttpServerExchange exchange) {
		
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/html");
		exchange.getResponseSender().send("test");
		
		return null;
		
	}
	
}