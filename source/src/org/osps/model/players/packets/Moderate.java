package org.osps.model.players.packets;

import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.net.Packet;

public class Moderate implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		int playerId = packet.getInt();
		if (!c.getRights().isStaff()) {
			PlayerHandler.getPlayers().stream().filter(p -> p.getRights().isStaff()).forEach(p -> 
				p.sendMessage("WARNING: " + c.playerName +" just attempted to operate the moderate option."));
			return;
		}
		if (!c.canUsePackets) {
			return;
		}
		if (playerId < 0 || playerId > PlayerHandler.players.length - 1) {
			return;
		}
		Player target = PlayerHandler.players[playerId];
		if (target == null) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.sendMessage("Please finish what you're doing.");
			return;
		}
		if (target.getRights().isStaff() && !c.getRights().isDeveloper()) {
			target.sendMessage(c.playerName + " just attempted to use the punishment panel on you.");
			c.sendMessage("You cannot punish " + target.playerName + ", they are staff.");
			return;
		}
		c.getPunishmentPanel().open(target);
	}

}
