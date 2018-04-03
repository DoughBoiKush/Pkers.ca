package org.osps.model.content.dialogue.impl;

import org.osps.Config;
import org.osps.model.content.decanting.Decanting;
import org.osps.model.content.dialogue.Dialogue;
import org.osps.model.content.dialogue.DialogueManager;
import org.osps.model.content.dialogue.Emotion;
import org.osps.model.content.dialogue.OptionDialogue;

public class DecantingDialogue extends Dialogue {

	@Override
	public boolean clickButton(int id) {
		return false;
	}

	@Override
	public void execute() {
		switch (getNext()) {
		case 0:
			DialogueManager.sendNpcChat(getPlayer(), NPC_ID, Emotion.EVIL_LAUGH_SHORT,
					"I will decant all your potions for", " coins.");
			setNext(1);
			break;
		case 1:
			DialogueManager.sendNpcChat(getPlayer(), NPC_ID, Emotion.LAUGHING, "50,000 coins please!");
			setNext(2);
			break;
		case 2:
			player.start(new OptionDialogue("Decant potions (Costs 50,000 coins)", p -> {
				if (player.getItems().getItemAmount(995) >= 50000) {
					player.getItems().deleteItem2(995, 50000);
					Decanting.decantInventory(player);
				} else {
					player.sendMessage("You don't have enough coins.");
				}
				player.getPA().removeAllWindows();
			}, "Don't decant potions", p -> {
				player.getPA().removeAllWindows();
			}));
			end();
			break;
		}
	}

	public static final int NPC_ID = 3562;

}