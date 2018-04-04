package org.osps.model.minigames.lottery;

import java.util.ArrayList;
import java.util.List;

public class Lottery {
	
	private static int jackpot;	
	static List<Entry> entries = new ArrayList<>();

	@SuppressWarnings("static-access")
	public Lottery(List<Entry> entries) {
		if (entries != null) {
			for (Entry entry : entries) {
				this.entries.add(entry);
			}
		}
		for (Entry entry : entries) {
			this.jackpot += entry.getPkpEntered();
		}
	}
	
	public void print() {
		System.out.println("****Jackpot: " + jackpot + "****");
		for (Entry entry : entries) {
			System.out.println("**");
			System.out.println("Name:          " + entry.getName());
			System.out.println("PKP Entered:   " + entry.getPkpEntered());
		}
	}
	
	public int getJackpot() {
		return jackpot;
	}
	
	public List<Entry> getEntries() {
		return entries;
	}
	
	public void addEntry(Entry e) {
		entries.add(e);
		jackpot += e.getPkpEntered();
	}
}
