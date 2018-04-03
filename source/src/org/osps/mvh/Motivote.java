package org.osps.mvh;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.GetRequest;

public final class Motivote extends AuthService {
	public static final String MV_API_URL = "http://motivoters.com/api/auth/{action}/{key}/{code}/";
	
	private final String apikey;
	
	public Motivote(String apikey) {
		this.apikey = apikey;
	}
	
	private Future<Vote> action(String name, String authcode) {
		FutureTask<Vote> ft = new FutureTask<Vote>(new Callable<Vote>() {
			@Override
			public Vote call() throws Exception {
				try {
					GetRequest r = Unirest.get(MV_API_URL)
							.routeParam("action", name)
							.routeParam("key", apikey)
							.routeParam("code", authcode);
					//System.out.println(r.getUrl());
					HttpResponse<JsonNode> response = r.asJson();
					JSONObject js = response.getBody().getObject();
					
					if (js.getString("error").isEmpty()) {
						return new Vote(js.getString("auth"), js.getString("ip"), js.getLong("timestamp"), js.getBoolean("ready"), js.getBoolean("fulfilled"), js.getBoolean("redeemed"));
					}
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				
				return null;
			}
		});
		executor.execute(ft);
		return ft;
	}

	@Override
	public Future<Vote> info(String authcode) {
		return action("info", authcode);
	}

	@Override
	public Future<Boolean> redeem(final String authcode) {
		FutureTask<Boolean> ft = new FutureTask<Boolean>(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				Vote vote = action("redeem", authcode).get();
				
				if (vote == null) {
					return false;
				}
				
				return vote.redeemed();
			}
		});
		
		executor.execute(ft);
		return ft;
	}

	@Override
	public void shutdown() {
		try {
			Unirest.shutdown();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("You probably got here by accident.");
	}
}
