package org.osps.model.content;

import org.osps.model.items.Item;
import org.osps.model.items.ItemDefinition;
import org.osps.model.players.Player;

public class Unspawnable {
	
	public final static String[] names = {

			"ahrim", "reindeer", "token", "lamp", "warrior guild", "manta", "sea turtle", "tuna potato", "star bauble",
			"bauble", "tokkul", "grimy", "herb", "torag", "dharok", "overload", "tenderiser", "woolly", "bobble",
			"jester", "gilded", "legend", "hell", "dragon spear", "odium", "malediction", "callisto", "gods",
			"yrannical", "treasonous", "granite maul", "ancient mace", "super combat", "dragon halberd", "torstol",
			"d hally", "dragon hally", "karil", "defender icon", "attacker icon", "picture", "collector icon",
			"collecter icon", "healer icon", "crystal key", "essence", "3rd", "third", "bomb", "karamb", "guthan",
			"verac", "dark crab", "void", "uncut", "Restrict", "onyx amulet", "onyx ring", "spirit", "chisel",
			"statius", "vesta", "morrigan", "zuriel", "occult", "trident", "mystic smoke", "mystic steam", "tentacle",
			"dark bow", "ranger boots", "robin hood hat", "attack cape", "defence cape", "strength cape", "prayer cape",
			"constitution", "range cape", "ranged cape", "ranging cape", "magic cape", "herblore", "agility",
			"fletching", "crafting", "runecrafting", "runecraft", "farming", "hunter", "slayer", "summoning",
			"construction", "woodcutting", "firemaking", "fishing", "cooking", "smithing", "mining", "thieving",
			"arcane", "divine", "spectral", "wealth", "elysian", "spirit", "status", "hand cannon", "visage", "raw",
			"logs", "bar", "ore", "uncut", "dragon leather", "scroll", "hatchet", "iron axe", "steel axe", "bronze axe",
			"rune axe", "adamant axe", "mithril axe", "black axe", "dragon axe", "vesta", "pumpkin", "statius's",
			"zuriel", "morrigan", "dwarven", "statius", "fancy", "rubber", "sled", "flippers", "camo", "lederhosen",
			"mime", "lantern", "santa", "scythe", "bunny", "h'ween", "hween", "clue", "casket", "cash", "box",
			"cracker", "zuriel's", "Statius's", "torso", "fighter", "Statius", "skeleton", "chicken",
			"zamorak platebody", "guthix platebody", "saradomin plate", "grim reaper hood", "armadyl", "bandos",
			"armadyl cross", "graardor", "zilyana", "kree", "tsut", "mole", "kraken", "dagannoth", "king black dragon",
			"chaos ele", "staff of the dead", "staff of dead", "(i)", "ticket", "PK Point", "jester", "dragon defender",
			"fury", "mithril defender", "adamant defender", "rune defender", "pet box", "crate", "elysian", "mystery box", "arcane",
			"chaotic", "Chaotic", "stream", "broken", "deg", "corrupt", "fire cape", "sigil", "godsword", "void seal",
			"lunar", "hilt", "(g)", "mage's book", "berserker ring", "warriors ring", "warrior ring", "warrior's ring",
			"archer", "archer's ring", "archer ring", "archers' ring", "seers' ring", "seer's ring", "seers ring",
			"mages' book", "wand", "(t)", "guthix", "zamorak", "saradomin", "scythe", "bunny ears", "zaryte bow",
			"armadyl battlestaff", "(i)", "infinity", "slayer", "korasi", "staff of light", "dice", "ardougne",
			"unarmed", "gloves", "dragon claws", "party", "santa", "completionist", "null", "coins", "tokhaar-kal",
			"tokhaar", "sanfew", "dragon defender", "zaryte", "coupon", "flippers", "dragonfire shield", "sled",
			"tzrek", "holiday tool", "ironman", "slayer helmet recipe", "ring of charos", "slayer gloves",
			"salve amulet", "nose peg", "earmuffs", "spiny helmet", "facemask", "rotten potato", "notes",
			"preserve prayer scroll", "rigour prayer scroll", "augury prayer scroll", "Package", "PartyHat", "Partyhat",
			"Santa", "spirit shield", "Elite void", "3rd", "Dice bag", "150$ scroll", "50$ scroll", "10$ scroll",
			"Primordial", "Eternal", "Pegasian", "Anguish", "Tome", "Third", "Berserker ring (i)", "Seers ring (i)",
			"Archers ring (i)", "Warrior ring (i)", "Armadyl godsword", "Staff of the dead", "Smouldering", "Fighter",
			"tentacle", "Fire cape", "Super combat", "partyhat", "h'ween", "halloween", "blowpipe", "Toxic blowpipe",
			"Bounty teleport scroll", "Halloween", "Odium", "Dark infinity", "Light infinity", "dark bow paint",
			"Volcanic", "Malediction", "Magic shortbow (i)", "Max cape", "max cape", "hilt", "Occult", "Graceful",
			"Pet", "granite clamp", "sigil", "Bunny", "Overload", "Crystal hal", "Magma", "Antisanta",
			"Rogue's revenge", "Saradomin's tear", "Cow", "Black mask", "Slayer helm", "slayer helm", "Baby",
			"Kalphite", "Vet'ion", "Sagacious spectacles", "musketeer", "ballista", "Ballista", "crystal hal",
			"Mysterious", "Looting", "Toxic", "Trident", "trident", "toxic", "Big pirate hat", "deerstalker",
			"Kodai wand", "fury", "Dragonfire", "Sled", "Dragon warhammer", "of anguish", "Ring of suffering",
			"Tormented bracelet", "afro", "tophat", "Hunter's honour", "crate", "torture", "Graceful", "Super combat",
			"fury (or)", "Draconic", "Serp", "Zenyte", "Onyx", "Royal", "briefcase", "halo", "Master wand",
			"Morrigan's", "Pk Points", "Zuriel", "infinity", "bulwrak", "Ring of wealth", "Dark crab", "Raw dark crab",
			"Angel Cape", "Deadly Slayer Cape", "PKers cape", "Donator Cape", "Elder maul", "Dinh's bulwark", "notes",
			"preserve prayer scroll", "rigour prayer scroll", "augury prayer scroll", "dragon bones",
			"lava dragon bones", "green dragonhide", "black dragonhide", "bandos", "armadyl", "bandos chestplate",
			"armadyl chestplate", "bandos tassets", "armadyl plateskirt", "ahrim", "arhim's", "karil", "karil's",
			"torag", "torag's", "guthan", "guthan's", "dharok", "dharok's", "verac", "verac's"

	};

public final static int[] ids = {
			13280, 13281, 13282, 13329, 13330, 13331, 13332, 13333, 13334, 13335, 13336, 13337, 13338, 13342, 20770,
			20771, 20772, 7630
};

public static void spawnItem(Player player,int id, int amount){
	String message = canSpawn(id);
	if(message.length() > 0 &&(!player.getRights().isDeveloper() && !player.getRights().isOwner())){
		player.sendMessage(message);
		return;
	}
	player.getItems().addItem(id, amount);
}

	public static String canSpawn(int itemId) {
		switch (itemId) {
		case 11785:
		case 12929:
		case 11791:
		case 10551:
		case 11770:
		case 11771:
		case 11772:
		case 11773:
		case 12831:
		case 12829:
		case 11283:
		case 6570:
		case 12695:
		case 13237:
		case 13239:
		case 13235:
		case 11924:
		case 11926:
		case 11826:
		case 11828:
		case 11830:
		case 11832:
		case 11834:
		case 11836:
		case 15318:
		case 12924:
		case 4716:
		case 4718:
		case 4720:
		case 4722:
		case 4732:
		case 4734:
		case 4736:
		case 4738:
		case 4724:
		case 4726:
		case 4728:
		case 4730:
		case 4745:
		case 4747:
		case 4749:
		case 4751:
		case 4708:
		case 4710:
		case 4712:
		case 4714:
		case 4753:
		case 4755:
		case 4757:
		case 4759:
		case 962:
		case 963:
		case 12691:
		case 12692:
		case 10887:
		case 10888:
		case 11798:
		case 11799:
		case 12653:
		case 2572:
		case 11849:
		case 13073:
		case 13072:
		case 13241:
		case 13243:
		case 12774:
		case 12773:
		case 13652:
		case 19722:
		case 10594:
		case 5020:
		case 13204:
		case 13307:
		case 8125:
		case 2701:
		case 2691:
		case 2690:
		case 2692:
		case 11936:
		case 13441:
		case 13439:
		case 11934:
		case 2996:
		case 10548:
		case 12954:
		case 8851:
		case 11235:
		case 11236:
		case 15098:
		case 299:
		case 6199:
		case 12601:
		case 13344:
		case 13343:
		case 12602:
		case 12791:
		case 995:
		case 12603:
		case 13320:
		case 13225:
		case 13321:
		case 13322:
		case 13247:
		case 12604:
		case 12605:
		case 12606:
		case 13263:
		case 4149:
		case 13262:
		case 13178:
		case 13177:
		case 13181:
		case 10476:
		case 12851:
		case 12853:
		case 14484:
		case 12904:
		case 12610:
		case 12608:
		case 3272:
		case 12612:
		case 6889:
		case 6914:
		case 12596:
		case 19994:
		case 12804:
		case 12526:
		case 12932:
		case 12934:
		case 12938:
		case 20408:
		case 19970:
		case 12765:
		case 12766:
		case 12767:
		case 12768:
		case 21015:
		case 13884:
		case 13890:
		case 13896:
		case 13902:
		case 13887:
		case 13893:
		case 13899:
		case 13905:
		case 13870:
		case 13873:
		case 13876:
		case 13879:
		case 13883:
		case 13858:
		case 13861:
		case 13864:
		case 13867:
		case 3144:
		case 13124:
		case 13200:
		case 13201:
		case 12802:
		case 12397:
		case 12393:
		case 12395:
		case 12439:
		case 12432:
		case 12430:
		case 12355:
		case 12337:
		case 12351:
		case 12441:
		case 12443:
		case 12335:
		case 12637:
		case 12639:
		case 12638:
		case 12540:
		case 3145:
		case 20997:
		case 21018:
		case 21021:
		case 21024:
		case 21012:
		case 21000:
		case 13265:
		case 13267:
		case 13269:
		case 13271:
		case 20784:
		case 13272:
		case 13270:
		case 13268:
		case 13266:
		case 20764:
		case 13264:
		case 11802:
		case 11803:
		case 20770:
		case 20771:
		case 20772:
		case 536:
		case 11943:
		case 1753:
		case 1747:
			return "This item is not spawnable.";
		}
		String itemName = ItemDefinition.forId(itemId).getName();
		for (String unspawnables : names) {
			if (itemName.contains(unspawnables))
				return "This item is not spawnable.";
		}

		for (int i : ids) {
			if (itemId == i) {
				return "This item is not spawnable.";
			}
		}
		return "";
	}

public static boolean spawnable(int itemId) {
    
    switch (itemId) {
	case 962:
	case 963:
	case 12691:
	case 12692:
	case 10887:
	case 10888:
	case 11798:
	case 11799:
	case 4551:
	case 4552:
	case 4164:
	case 4165:
	case 4166:
	case 4167:
	case 4168:
	case 4169:
	case 12653:
	case 2572:
	case 11849:
	case 13073:
	case 13072:
	case 13241:
	case 13243:
	case 6739:
	case 6740:
	case 12774:
	case 12773:
	case 13652:
	case 19722:
	case 10594:
	case 5020:
	case 13204:
	case 13307:
	case 8125:
	case 2701:
	case 2691:
	case 2690:
	case 2692:
	case 11936:
	case 13441:
	case 13439:
	case 11934:
	case 2996:
	case 10548:
	case 12954:
	case 8851:
	case 11235:
	case 11236:
	case 15098:
	case 299:
	case 6199:
	case 12601: 
	case 13344:
	case 13343:
	case 12602: 
	case 12791:
	case 995:
	case 12603: 
	case 13320: 
	case 13225: 
	case 13321: 
	case 13322: 
	case 13247:
	case 12604: 
	case 12605: 
	case 12606:
	case 13263:
	case 4149:
	case 13262:
	case 13178:
	case 13177:
	case 13181:
	case 10476:
	case 12851:
	case 12853:
	case 14484:
	case 12904:
	case 12610:
	case 12608:
	case 3272:
	case 12612:
	case 6889:
	case 6914:
	case 12596:
	case 19994:
	case 12804:
	case 12526:
	case 12932:
	case 12934:
	case 12938:
	case 20408:
	case 19970:
	case 12765:
	case 12766:
	case 12767:
	case 12768:
	case 21015:
	case 13884:
	case 13890:
	case 13896:
	case 13902:
	case 13887:
	case 13893:
	case 13899:
	case 13905:
	case 13870:
	case 13873:
	case 13876:
	case 13879:
	case 13883:
	case 13858:
	case 13861:
	case 13864:
	case 13867:
	case 3144:
	case 13124:
	case 13200:
	case 13201:
	case 12802:
	case 12397:
	case 12393:	
	case 12395:
	case 12439:
	case 12432:
	case 12430:
	case 12355:
	case 12337:
	case 12351:
	case 12441:
	case 12443:
	case 12335:
	case 12637:
	case 12639:
	case 12638:
	case 12540:
	case 3145:
	case 20997:
	case 21018:
	case 21021:
	case 21024:
	case 21012:
	case 21000:
	case 13265:
	case 13267:
	case 13269:
	case 13271:
	case 20784:
	case 13272:
	case 13270:
	case 13268:
	case 13266:
	case 20764:
	case 13264:
	case 11802:
	case 11803:
		return false;
	}
	String itemName = ItemDefinition.forId(itemId).getName();
	for (String unspawnables : names) {
		if (itemName.contains(unspawnables))
			return false;
	}

	for (int i : ids) {
		if (itemId == i) {
			return false;
		}
	}
    
    return true;
}




}
