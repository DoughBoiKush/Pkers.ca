package org.osps.model.players.packets;

import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;

public class ChangeRegion implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		c.getPA().removeObjects();
		// Server.objectManager.loadObjects(c);
	}

}
