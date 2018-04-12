package org.osps.model.players.combat.effects;

import org.osps.model.npcs.NPC;
import org.osps.model.players.Player;
import org.osps.model.players.combat.Damage;
import org.osps.model.players.combat.DamageEffect;
import org.osps.util.Misc;

public class SnakelingEffect implements DamageEffect {

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		if (Misc.random(2) == 0 && defender.isSusceptibleToVenom()) {
			defender.setVenomDamage((byte) damage.getAmount()); 
			attacker.sendMessage("Your pet snakeling infected your opponent with venom.");
		}
		
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {
		
	}

	@Override
	public boolean isExecutable(Player operator) {
		// TODO Auto-generated method stub
		return false;
	}

}
