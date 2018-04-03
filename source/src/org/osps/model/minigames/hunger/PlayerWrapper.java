package org.osps.model.minigames.hunger;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.osps.event.CycleEvent;
import org.osps.event.CycleEventContainer;
import org.osps.event.CycleEventHandler;
import org.osps.model.players.Player;

public class PlayerWrapper {
	
	public PlayerWrapper(Player p, Type t) {
		this.p = p;
		this.t = t;
		hasDiamonds = new boolean[DIAMONDS.length];
		for(int i = 0; i < hasDiamonds.length; i++)
			hasDiamonds[i] = false;
		CycleEventHandler.getSingleton().addEvent(p, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				setup();
				container.stop();
			}
		}, 2);
	}
	
	public void setup() {
		betrayed = false;
		p.getItems().wearItem(2405, 1, p.playerAmulet);
		p.getPA().walkableInterface(11479);
		p.canUsePackets = true;
		switch(t) {
			case PLAYER:
				originalSkills = Arrays.copyOfRange(p.playerXP, 0, 7);
				for(int i = 0; i <= 6; i++) {
					if(i == 3) {
						p.playerXP[i] = 1300;
						p.playerLevel[i] = 10;
					} else {
						p.playerXP[i] = 0;
						p.playerLevel[i] = 1;
					}
					p.getPA().refreshSkill(i);
				}
				break;
			case SPECTATOR:
				break;
		}
		p.hungerGames = true;
	}
	
	public void moveToSpawnPoint(Long l) {
		p.canUsePackets = false;
		p.getItems().addItem(10025, 1);
		if(p.hunger1)
			p.getItems().addItem(9008, 1);
		if(p.hunger2)
			p.getItems().addItem(9007, 1);
		p.getPA().movePlayer((int)(l>>>32), (int)(l&(1L<<32)-1), 0);
	}
	
	public void destroy() {
		p.getItems().deleteAllItems();
		p.setPoisonDamage((byte) 0);
		if(t == Type.PLAYER) {
			for(int i = 0; i <= 6; i++) {
				p.playerXP[i] = originalSkills[i];
				p.playerLevel[i] = p.getPA().getLevelForXP(p.playerXP[i]);
				p.getPA().refreshSkill(i);
			}
		} else {
			
		}
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public boolean betrayedLanthus() {
		return betrayed;
	}
	
	
	
	Player p;
	public boolean betrayed, usedRope, freeDiamond;
	Type t;
	int[] originalSkills;
	
	public int getRandomDiamond() {
		Random r = new Random();
		return r.ints(0, DIAMONDS.length).filter(i -> !hasDiamonds[i]).mapToObj(i-> DIAMONDS[i]).findAny().orElse(-1);
	}
	
	public boolean hasAllDiamonds() {
		for(boolean b : hasDiamonds) {
			if(!b)
				return false;
		}
		return true;
	}
	
	public static final int[] DIAMONDS = { 4670, 4671, 4672, 4673 };
	public boolean[] hasDiamonds;
	
	enum Type {
		PLAYER,
		SPECTATOR;
	}

	public void applyPoison() {
		int hp = p.getPA().getLevelForXP(p.playerXP[p.playerHitpoints]);
		if(p.getPoisonDamage() < ((int) hp / 6))
			p.setPoisonDamage((byte) (hp / 6));
	}
	
}
