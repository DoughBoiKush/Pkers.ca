package org.osps.model.players.packets;

import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.net.Packet;

/**
 * Bank X Items
 **/
public class BankX1 implements PacketType {

	public static final int PART1 = 135;
	public static final int PART2 = 208;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;

	@Override
	public void processPacket(Player c, Packet packet) {
		if (packet.getOpcode() == 135) {
			c.xRemoveSlot = packet.getLEShort();
			c.xInterfaceId = packet.getUnsignedShortA();
			c.xRemoveId = packet.getLEShort();
		}
		if (c.xInterfaceId == 3900) {
			if (c.myShopId == 81 && c.getRights().getValue() == 0) {
				c.sendMessage("You must be a donator to buy from this shop.");
				return;
			}
			if (c.buyingX) {
				c.getShops().buyItem(c.xRemoveId, c.xRemoveSlot, Xamount);
				c.xRemoveSlot = 0;
				c.xInterfaceId = 0;
				c.xRemoveId = 0;
				c.buyingX = false;
				}
				if(c.storing) {

				return;
			}
                }
		if (packet.getOpcode() == PART1) {
			//synchronized (c) {
				c.getOutStream().createFrame(27);
		  		
		}
                c.sendMessage("lol "+c.xInterfaceId);
    }
}
