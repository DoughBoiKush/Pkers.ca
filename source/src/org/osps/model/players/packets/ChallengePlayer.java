package org.osps.model.players.packets;

import java.util.Objects;

import org.osps.Config;
import org.osps.model.players.Boundary;
import org.osps.model.players.PacketType;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.net.Packet;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements PacketType {

	@Override
	public void processPacket(Player c, Packet packet) {
		if (!c.canUsePackets) {
			return;
		}
		switch (packet.getOpcode()) {
		case 128:
			int answerPlayer = packet.getUnsignedShort();
			if(answerPlayer >= PlayerHandler.players.length || answerPlayer < 0) {
				return;
			}
			if (PlayerHandler.players[answerPlayer] == null) {
				return;
			}
			Player requested = PlayerHandler.players[answerPlayer];
			if (Objects.isNull(requested)) {
				return;
			}
			if (c.getInterfaceEvent().isActive()) {
				c.sendMessage("Please finish what you're doing.");
				return;
			}
			if (requested.getInterfaceEvent().isActive()) {
				c.sendMessage("That player is busy right now.");
				return;
			}
			if (Boundary.isIn(c, Boundary.DUEL_ARENAS) ||
					Boundary.isIn(requested, Boundary.DUEL_ARENAS)) {
				c.sendMessage("You cannot do this inside of the duel arena.");
				return;
			}
			if (Config.NEW_DUEL_ARENA_ACTIVE) {
				if (c.getDuel().requestable(requested)) {
					c.getDuel().request(requested);
				}
			} else {
				c.getDH().sendStatement("@red@Dueling Temporarily Disabled",
						"The duel arena minigame is currently being rewritten.",
						"No player has access to this minigame during this time.",
						"", "Thank you for your patience, Developer J.");
				c.nextChat = -1;
				//c.getTradeAndDuel().requestDuel(answerPlayer);
			}
			break;
		}
	}
}
