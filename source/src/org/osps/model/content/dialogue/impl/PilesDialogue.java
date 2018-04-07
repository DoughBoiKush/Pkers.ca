package org.osps.model.content.dialogue.impl;

import org.osps.model.content.dialogue.Dialogue;
import org.osps.model.content.dialogue.DialogueConstants;
import org.osps.model.content.dialogue.DialogueManager;
import org.osps.model.content.dialogue.Emotion;
import org.osps.model.players.Player;
import org.osps.util.Misc;

/**
 * Handles the wilderness resource arena
 * @author Daniel
 *
 */
public class PilesDialogue extends Dialogue {

	/**
	 * Piles dialogue
	 * @param player
	 */
	public PilesDialogue(Player player) {
		this.player = player;
	}

	/**
	 * The items that may be converted in the arena
	 */
	public int ITEMS[][] = { { 451, 452 }, { 11934, 11935 }, { 440, 441 }, { 453, 454 }, { 444, 445 }, { 447, 448 }, { 449, 450 }, { 1515, 1516 }, { 1513, 1514 } , { 383, 384 }};

	@Override
	public boolean clickButton(int id) {
		switch (id) {

		case DialogueConstants.OPTIONS_2_1:
			player.getPA().closeAllWindows();

			for (int i = 0; i < ITEMS.length; i++) {
				if (player.getItems().playerHasItem(ITEMS[i][0])) {
					int amount = player.getItems().getItemAmount(ITEMS[i][0]);
					int payment = player.getItems().getItemAmount(ITEMS[i][0]) * 1;
					
					if (player.pkp != payment) {
						DialogueManager.sendStatement(player, Misc.format(payment) + " PK Points is required to do this; which you do not have!"); 
						break;
					}
					player.pkp -= payment;
					player.getItems().deleteItem2(ITEMS[i][0], amount);
					player.getItems().addItem(ITEMS[i][1], amount);
					DialogueManager.sendInformationBox(player, "Piles", "You have noted:", "@blu@" + amount + " </col>items", "You have paid:", "@blu@" + Misc.format(payment) + " </col>PK Points");
					break;
				} else {
					DialogueManager.sendStatement(player, "You do not contain any items that are allowed to be noted!");
				}
			}

			break;

		case DialogueConstants.OPTIONS_2_2:
			player.getPA().closeAllWindows();
			break;

		}
		return false;
	}

	@Override
	public void execute() {
		switch (next) {

		case 0:
			DialogueManager.sendNpcChat(player, 13, Emotion.HAPPY, "I can note any items obtained from the resource", "arena for 1 PK Points an item.");
			player.nextChat = next++;
			break;
		case 1:
			DialogueManager.sendOption(player, "Note items", "Nevermind");
			break;

		}
	}

}
