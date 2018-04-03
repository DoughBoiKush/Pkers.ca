package org.osps.model.players;

public class UpdateRank {
	Player c;
public void updateRank() {
	if (c.amDonated >= 10 && c.amDonated < 50 && c.getRights().getValue() == 0) {
		c.setRights(Rights.DONATOR);
	}
	if (c.amDonated >= 50 && c.amDonated < 150 && c.getRights().getValue() <= 5 && !c.getRights().isStaff()) {
		c.setRights(Rights.HONORED_DONATOR);
	}
	if (c.amDonated >= 150 && c.amDonated < 500 && c.getRights().getValue() <= 6 && !c.getRights().isStaff()) {
		c.setRights(Rights.RESPECTED_DONATOR);
	}
	if (c.amDonated >= 500 && c.amDonated < 1000 && c.getRights().getValue() <= 7 && !c.getRights().isStaff()) {
		c.setRights(Rights.LEGENDARY_DONATOR);
	}
	/*if (c.amDonated >= 300 && !c.getRights().isStaff()) {
		c.setRights(Rights.SUPER_V_I_P);
	}*/
 }

}
