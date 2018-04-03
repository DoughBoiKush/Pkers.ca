package org.osps.model.players.packets;

import org.osps.Connection;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;

/**
 * Chat
 **/
public class Chat implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		c.setChatTextEffects(packet.getByteS());
		c.setChatTextColor(packet.getByteS());
		c.setChatTextSize((byte) (packet.getLength() - 2));
		packet.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0);
		if (!c.canUsePackets) {
			return;
		}
		if (System.currentTimeMillis() < c.muteEnd) {
            c.sendMessage("You have been muted due to breaking a rule.");
            c.sendMessage("To prevent further mutes please read the rules.");
			return;
		}
		c.muteEnd = 0;
		//Server.getChatLogHandler().logMessage(c, "Chat", "", Misc.decodeMessage(c.getChatText(), c.getChatTextSize()));
		//System.out.println(c.playerName + " chat: " + Misc.decodeMessage(c.getChatText(), c.getChatTextSize()));
		//ReportHandler.addText(c.playerName, c.getChatText(), packetSize - 2);
        if (!Connection.isMuted(c)) {
            c.setChatTextUpdateRequired(true);
        } else {
        	c.sendMessage("You have been muted due to breaking a rule.");
            c.sendMessage("To prevent further mutes please read the rules.");
            return;
        }
	}
}
