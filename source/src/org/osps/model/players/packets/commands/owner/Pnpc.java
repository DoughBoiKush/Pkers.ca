package org.osps.model.players.packets.commands.owner;

import java.util.Optional;

import org.osps.model.npcs.NPCCacheDefinition;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.model.players.packets.commands.Command;

/**
 * Transform a given player into an npc.
 * 
 * @author Emiel
 *
 */
public class Pnpc implements Command {

	@Override
	public void execute(Player c, String input) {
		try {
			String args[] = input.split("-");
			if (args.length != 2) {
				throw new IllegalArgumentException();
			}
			Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayer(args[0]);
			if (optionalPlayer.isPresent()) {
				Player c2 = optionalPlayer.get();
				int npc = Integer.parseInt(args[1]);
				if (npc < 9999) {
					c2.npcId2 = npc;
					c2.playerWalkIndex = NPCCacheDefinition.forID(npc).getWalkIndex();
					c2.playerStandIndex = NPCCacheDefinition.forID(npc).getStandIndex();
					c2.playerRunIndex = NPCCacheDefinition.forID(npc).getWalkIndex();
					c2.playerTurnIndex = NPCCacheDefinition.forID(npc).getWalkIndex();
					c2.playerTurn180Index = NPCCacheDefinition.forID(npc).getWalkIndex();
					c2.playerTurn90CCWIndex = NPCCacheDefinition.forID(npc).getWalkIndex();
					c2.playerTurn90CWIndex = NPCCacheDefinition.forID(npc).getWalkIndex();
					c2.isNpc = true;
					c2.updateRequired = true;
					c2.appearanceUpdateRequired = true;
				}
			} else {
				throw new IllegalStateException();
			}
		} catch (IllegalArgumentException e) {
			c.sendMessage("Error. Correct syntax: ::pnpc-player-npcid");
		} catch (IllegalStateException e) {
			c.sendMessage("You can only use the command on online players.");
		}
	}
}
