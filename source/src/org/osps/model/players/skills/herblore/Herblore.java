package org.osps.model.players.skills.herblore;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import org.osps.Config;
import org.osps.model.items.GameItem;
import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;
import org.osps.model.players.skills.Skill;
import org.osps.util.Misc;

public class Herblore {
	
	/**
	 * A {@link Set} of all {@link Herb} elements from it's respective enumeration.
	 */
	private static final Set<Herb> HERBS = Collections.unmodifiableSet(EnumSet.allOf(Herb.class));
	
	/**
	 * A {@link Set} of all {@link Potion} elements from it's respective enumeration.
	 */
	private static final Set<Potion> POTIONS = Collections.unmodifiableSet(EnumSet.allOf(Potion.class));
	
	/**
	 * The player that will be operating this skill
	 */
	private final Player player;
	
	/**
	 * A class for managing herblore operation
	 * @param player	the player
	 */
	public Herblore(Player player) {
		this.player = player;
	}
	
	/**
	 * Cleans a single her
	 * @param itemId	the herb attempting to be cleaned
	 */
	public void clean(int itemId) {
		Optional<Herb> herb = HERBS.stream().filter(h -> h.getGrimy() == itemId).findFirst();
		herb.ifPresent(h -> {
			player.getSkilling().stop();
			player.getSkilling().setSkill(Skill.HERBLORE);
			if (!player.getItems().playerHasItem(h.getGrimy())) {
				player.sendMessage("You need the grimy herb to do this.");
				return;
			}
			if (player.playerLevel[Skill.HERBLORE.getId()] < h.getLevel()) {
				player.sendMessage("You need a herblore level of " + h.getLevel() + " to clean this grimy herb.");
				return;
			}
			ItemDefinition definition = ItemDefinition.forId(h.getClean());
		//	if (player.isExtreme) {
			//	player.getPA().addSkillXP(h.getExperience() * Config.HERBLORE_EXPERIENCE, Skill.HERBLORE.getId()/2);
		//	} else if (!player.isExtreme)  {
				player.getPA().addSkillXP(h.getExperience() * Config.HERBLORE_EXPERIENCE, Skill.HERBLORE.getId());
		//	}
			player.getItems().deleteItem2(h.getGrimy(), 1);
			player.getItems().addItem(h.getClean(), 1);
			player.sendMessage("You identify the herb as " + definition.getName() + ".");
		});
	}
	
	public void mix(int primary) {
		Optional<Potion> potion = POTIONS.stream().filter(p -> p.getPrimary().getId() == primary && containsSecondaries(p)).findFirst();
		potion.ifPresent(p -> {
			player.getSkilling().stop();
			player.getSkilling().setSkill(Skill.HERBLORE);
			ItemDefinition definition = ItemDefinition.forId(p.getResult().getId());
			if (player.playerLevel[Skill.HERBLORE.getId()] < p.getLevel()) {
				player.sendMessage("You need a herblore level of " + p.getLevel() + " to make " + definition.getName() + ".");
				return;
			}
			if (!player.getItems().playerHasItem(227)) {
				player.sendMessage("You need a regular vial of water to do this.");
				return;
			}
			Arrays.asList(p.getIngredients()).stream().forEach(ing -> player.getItems().deleteItem2(ing.getId(), ing.getAmount()));
			player.getItems().deleteItem2(227, 1);
			player.getItems().deleteItem2(p.getPrimary().getId(), p.getPrimary().getAmount());
			player.getItems().addItem(p.getResult().getId(), p.getResult().getAmount());
			//if (player.isExtreme) {
			//	player.getPA().addSkillXP(p.getExperience() * Config.HERBLORE_EXPERIENCE, Skill.HERBLORE.getId()/2);
			//	System.out.println("EXTREME");
			//} else if (!player.isExtreme)  {
				player.getPA().addSkillXP(p.getExperience() * Config.HERBLORE_EXPERIENCE, Skill.HERBLORE.getId());
			//}
			player.sendMessage("You combine all of the ingredients and make a " + definition.getName() + ".");
			int chance = Misc.random(500);
			//player.sendMessage("Your chance to get 100 platinum tokens from skilling was " + chance + " you needed 0.");
			if (chance == 0) {
				//player.getPA().rewardPoints(2, "Congrats, You randomly get 2 PK Points from herblore!");
			}
		});
	}
	
	/**
	 * Determines if the player has all of the ingredients required for the potion.
	 * @param potion	the potion we're determining this for
	 * @return			{@code true} if we have all of the ingredients, otherwise {@code false}
	 */
	private boolean containsSecondaries(Potion potion) {
		int required = potion.getIngredients().length;
		for (GameItem ingredient : potion.getIngredients()) {
			if (player.getItems().playerHasItem(ingredient.getId(), ingredient.getAmount())) {
				required--;
			}
		}
		return required == 0;
	}
}
