package org.osps.model.players;

import org.osps.net.Packet;

public interface PacketType {
	public void processPacket(Player c, Packet packet);
}
