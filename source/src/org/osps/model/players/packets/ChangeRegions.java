package org.osps.model.players.packets;

import org.osps.Server;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		Server.itemHandler.reloadItems(c);
		Server.getGlobalObjects().updateRegionObjects(c);
		if (c.getPA().viewingOtherBank) {
			c.getPA().resetOtherBank();
		}
		c.getPA().castleWarsObjects();
		c.saveFile = true;

		if (c.skullTimer > 0) {
			c.isSkulled = true;
			c.headIconPk = 0;
			c.getPA().requestUpdates();
		}

	}

}
