package org.osps.model.minigames.lottery;

public class Entry {
	
	private String NAME;
	private int PKP_ENTERED;
	
	public Entry(String name, int pkpEntered) {
		NAME = name;
		PKP_ENTERED = pkpEntered;
	}
	
	public String getName() {
		return NAME;
	}
	
	public void setName(String name) {
		NAME = name;
	}
	
	public int getPkpEntered() {
		return PKP_ENTERED;
	}
	
	public void setPkpEntered(int pkp) {
		PKP_ENTERED = pkp;
	}
	
	public void increasePkpEnteredBy(int pkp) {
		PKP_ENTERED += pkp;
	}
	
	@Override
	public String toString() {
		return NAME + " - " + PKP_ENTERED;
	}

}
