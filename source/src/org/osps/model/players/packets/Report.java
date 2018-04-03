package org.osps.model.players.packets;

import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;

public class Report implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		if (!c.canUsePackets) {
			return;
		}
		try {
			ReportHandler.handleReport(c, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}