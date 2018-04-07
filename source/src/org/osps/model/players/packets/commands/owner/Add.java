package org.osps.model.players.packets.commands.owner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.osps.Server;
import org.osps.model.players.Player;
import org.osps.model.players.packets.commands.Command;

/**
 * Adds npcs to the spawn-config file.
 */
public class Add implements Command {

	@Override
	public void execute(Player c, String input) {
		String[] args = input.split(" ");
		try {
			BufferedWriter spawn = new BufferedWriter(new FileWriter("./Data/cfg/spawn-config.cfg", true));
			int npcId = Integer.parseInt(args[0]);
			if (npcId > 0) {
				Server.npcHandler.spawnNpc(c, npcId, c.absX, c.absY, 0, 0, 120, 7, 70, 70, false, false);
				c.sendMessage("You have spawned: " + npcId + "Name: " + Server.npcHandler.getNpcName(npcId));
			} else {
				c.sendMessage("No Such Npc");
			}
			try {
				spawn.newLine();
				spawn.write("spawn = " + npcId + "		" + c.absX + "	" + c.absY + "	0	1	0	0	0");
			} finally {
				spawn.close();
			}
		} catch (IOException e) {
		}
	}
}