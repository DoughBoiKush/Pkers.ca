package org.osps.model.content.dialogue.impl;

import org.osps.model.content.ItemCheck;
import org.osps.model.content.dialogue.Dialogue;
import org.osps.model.content.dialogue.DialogueConstants;
import org.osps.model.content.dialogue.DialogueManager;
import org.osps.model.content.dialogue.Emotion;
import org.osps.model.content.dialogue.OptionDialogue;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerSave;
import org.osps.util.Misc;

public class AdamDialogue extends Dialogue {
	
	public AdamDialogue(Player player) {
		this.player = player;
	}
	
	private final int[] IRON_ARMOUR = { 
		12810, 12811, 12812, 12813, 12814, 12815
	};

	@Override
	public boolean clickButton(int id) {
		switch (id) {
		
		case DialogueConstants.OPTIONS_2_1:
			for (int i = 0; i < IRON_ARMOUR.length; i++) {
				if (player.getItems().isWearingItem(IRON_ARMOUR[i])) {
					DialogueManager.sendNpcChat(player, 311, Emotion.ANNOYED, "Please remove your Iron Man armour first!");
					return false;
				}
			}
			player.ironman = true;
			PlayerSave.save(player);
			player.getPA().removeAllWindows();
			player.sendMessage("You are an ironman forever.");
			player.updateRequired = true;
			break;
			
		case DialogueConstants.OPTIONS_2_2:
			player.getPA().removeAllWindows();
			break;
		
		case DialogueConstants.OPTIONS_4_1:
			if (player.ironman) {
				if (ItemCheck.hasIronArmour(player)) {
					DialogueManager.sendNpcChat(player, 311, Emotion.ANNOYED, "It appears you already have some armour!");
					return false;
				}
				player.getItems().addItem(12810, 1);
				player.getItems().addItem(12811, 1);
				player.getItems().addItem(12812, 1);
				DialogueManager.sendItem1(player, "Adam has given you some armour.", 12810);
			}
			break;
			
		case DialogueConstants.OPTIONS_4_2:
			player.start(new OptionDialogue("General", p -> {
				player.getShops().openShop(38);
			} , "Armours", p -> {
				player.getShops().openShop(39);
			} , "Miscellaneous", p -> {
				player.getShops().openShop(41);
			} , "Herblore", p -> {
				player.getShops().openShop(33);
			} , "Farming", p -> {
				player.getShops().openShop(32);
			}));
			break;
			
		case DialogueConstants.OPTIONS_4_3:
			setNext(5);
			execute();
			break;
			
		case DialogueConstants.OPTIONS_4_4:
			player.getPA().removeAllWindows();
			break;

		}
		return false;
	}

	@Override
	public void execute() {
		switch (next) {
		
		case 0:
			if (!player.ironman && !player.getRights().isOwner()) {
				DialogueManager.sendNpcChat(player, 311, Emotion.SLIGHTLY_SAD, "Sorry, since you are not an Iron Man I cannot help you.");
				end();
				return;
			}
			DialogueManager.sendNpcChat(player, 311, Emotion.HAPPY_TALK, "Welcome " + Misc.ucFirst(player.playerName) + "!", "My name is Adam,",  "I can help you with your Iron Man account.");
			next++;
			break;
			
		case 1:
			DialogueManager.sendOption(player, "Obtain some armour", "View Iron Man stores", "Remove Iron Man restrictions", "Nevermind");
			break;
			
		case 5:
			DialogueManager.sendNpcChat(player, 311, Emotion.HAPPY_TALK, "I can remove your Iron Man restrictions.", "However, you will not be able to gain it back.");
			next++;
			break;
			
		case 6:
			DialogueManager.sendOption(player, "Do it", "Nevermind");
			break;
		
		}
	}
	

}
