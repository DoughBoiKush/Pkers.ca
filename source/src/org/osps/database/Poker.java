package org.osps.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.osps.Server;
import org.osps.model.players.Player;

public class Poker {

	private static final String SECRET = "APIPass995"; // YOUR SECRET
																	// KEY!

	public static void deposit(Player player, String amount) {
		player.sendMessage("Attempting to deposit...");
		
		Server.ENGINE.executeAsync(() -> {
			try {
				URL url = new URL("http://51.255.164.137:8087/api?Password=" + SECRET
						+ "&Command=AccountsIncBalance&Player=" + player.pokerName.toLowerCase().replaceAll(" ", "_")
						+ "&Amount=" + amount + "&Log=Give " + amount + " chips to "
						+ player.playerName.toLowerCase().replaceAll(" ", "_"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String string = null;
				loop: while ((string = reader.readLine()) != null) {
					string = string.toLowerCase().trim();
					switch (string) {
					case "balance=":
						Server.ENGINE.execute(() -> player.sendMessage("An error occured."));
						break loop;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				player.sendMessage("Currently not available.");
			}
		});
	}

	public static void withdraw(Player player, int amount) {
		player.sendMessage("Attempting to withdraw...");
		
		Server.ENGINE.executeAsync(() -> {
			boolean withdraw = true;
			try {
				URL url = new URL("http://51.255.164.137:8087/api?Password=" + SECRET
						+ "&Command=AccountsDecBalance&Negative=Skip&Player="
						+ player.pokerName.toLowerCase().replaceAll(" ", "_") + "&Amount=" + amount + "&Log=Decrease "
						+ amount + " chips from " + player.playerName.toLowerCase().replaceAll(" ", "_"));
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String string = null;
				loop: while ((string = reader.readLine()) != null) {
					string = string.toLowerCase().trim();
					switch (string) {
					case "amount=0":
						withdraw = false;
						break loop;
					}
				}
			} catch (Exception e) {
				withdraw = false;
				e.printStackTrace();
			}
			
			final boolean status = withdraw;
			Server.ENGINE.execute(() -> {
				if (status) {
					player.getItems().addItemUnderAnyCircumstance(995, amount);
					player.sendMessage(amount+" has been withdrawn from your poker account.");
					player.sendMessage("You can find the coins, in your inventory, bank or on ground.");
				} else {
					player.sendMessage("You cannot withdraw that amount.");
				}
			});
			
		});
	}

	public static void verifyPassword(Player player, String pokerName, String password) {
		player.sendMessage("Attempting to verify password...");

		Server.ENGINE.executeAsync(() -> {
			boolean verified = false;
			try {
				URL url = new URL(
						"http://51.255.164.137:8087/api?Password=" + SECRET + "&Command=AccountsPassword&Player="
								+ pokerName.toLowerCase().replaceAll(" ", "_") + "&PW=" + password);
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
				String string = null;
				loop: while ((string = reader.readLine()) != null) {
					string = string.toLowerCase().trim();
					switch (string) {
					case "verified=yes":
						verified = true;
						break loop;
					case "verified=no":
						verified = false;
						break loop;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			final boolean status = verified;
			Server.ENGINE.execute(() -> {
				if (status) {
					player.sendMessage("Password verified!");
					player.pokerName = pokerName;
					player.hasPokerName = true;
					player.sendMessage("You have set your poker name to: " + player.pokerName);
				} else {
					player.sendMessage("Wrong password or service offline.");
				}
			});
		});
	}
}
