package org.osps.model.players.packets;

import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;
import org.osps.util.Misc;

/**
 * Chat
 **/
public class ClanChat implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		String textSent = Misc.longToPlayerName2(packet.getLong());
		textSent = textSent.replaceAll("_", " ");
	}
}