package org.osps.model.npcs.boss;
 
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
 

import org.osps.model.content.teleport.Position;
import org.osps.model.items.ItemDefinition;
import org.osps.model.npcs.NPCHandler;
import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
/**
 * Represents a random {@lin NPC} spawned within the Wilderness location on a regular basis
 * @author Rene
 *
 */
 
 
 
 
public final class NewWildernessBoss {
	   
    public enum Location {
    	  LEVEL_45(new Position(2983, 3879, 0)),
        //LEVEL_12(new Position(3030, 3609, 0)),
        //LEVEL_23(new Position(2950, 3699, 0)),
        //LEVEL_40(new Position(2952, 3833, 0)),
        //LEVEL_47(new Position(3008, 3888, 0)),
        //LEVEL_48(new Position(3092, 3894, 0)),
        //LEVEL_38(new Position(3215, 3820, 0)),
        //LEVEL_39(new Position(3290, 3829, 0)),
        //LEVEL_30(new Position(3306, 3752, 0)),
        //LEVEL_20(new Position(3316, 3673, 0)),
        //LEVEL_8(new Position(3315, 3583, 0)),
       
        ;
       
        private final Position pos;
       
        Location(Position pos) {
            this.pos = pos;
        }
       
        public static final Set<Location> SET = EnumSet.allOf(Location.class);
       
        public static Location random() {
            int size = SET.size();
            int random = ThreadLocalRandom.current().nextInt(size);
            int index = 0;
            for (Location loc : SET) {
                if (index == random) {
                    return loc;
                }
                index++;
            }
            throw new AssertionError("Won't be reached");
        }
       
        public int getLevel() {
            return Integer.parseInt(getName().replace("LEVEL", ""));
        }
       
        public String getName() {
            return name().replaceAll("_", "");
        }
 
        public int getX() {
            return pos.getX();
        }
       
        public int getY() {
            return pos.getY();
        }
       
        public int getHeight() {
            return pos.getZ();
        }
       
        public Position getPos() {
            return pos;
        }
    }
 
    /** Configuration **/
    public static final int NPC_ID = 3127; //TODO
    private static final int NOTIF_COUNTDOWN =  (int) TimeUnit.MINUTES.toSeconds(3) *3;
    private static final int SECONDARY_NOTIFICATION =  (int) TimeUnit.MINUTES.toSeconds(2) *2;
    private static final int COUNTDOWN = (int) TimeUnit.MINUTES.toSeconds(3) * 3;
 
    /** Tztok-Jad rewards items**/
    private static final int[] ITEMS = new int[] { 12825, 12817, 15001, 12821, 12831, 12829, 11802, 11785, 12924, 12929, 12902, 11791, 13652, 13237, 13239, 13235, 11924, 11926 };
 
    /** Class vars **/
    private static int timer = COUNTDOWN;
    private static Location location;
    public static boolean spawned;
 
    public static final void tick() {
        if (spawned) {
            return;
        }
       
       
        if (timer == NOTIF_COUNTDOWN) {
            location = Location.random();
            int level = location.getLevel();
            msgAll("@bla@[@blu@WILDY BOSSES@bla@] A random wilderness Boss will spawn in @red@5 @bla@minutes! (@red@" + level + " @bla@Wilderness)");
        }
        if (timer == SECONDARY_NOTIFICATION) {
            int level = location.getLevel();
            msgAll("@bla@[@blu@WILDY BOSSES@bla@] A random wilderness Boss will spawn in @red@2 @bla@minutes! (@red@" + level + " @bla@Wilderness)");
        }
        if (timer == 0) {
            spawn();
        }
        if (timer != 0) {
            timer--;
        }
    }
   
    private static void spawn() {
        spawned = true;
        Location loc = location;
        int level = location.getLevel();
        NPCHandler.spawnNpc(NPC_ID, loc.getX(), loc.getY(), loc.getHeight(), 1, 1000, 84, 500, 500);
        msgAll("@bla@[@blu@WILDY BOSSES@bla@]@red@TzTok-Jad @bla@has spawned at (@red@" + level + " @bla@Wilderness)");
    }
 
    public static void restart(Optional<Player> killer) {
        
        timer = COUNTDOWN;
        spawned = false;
        killer.ifPresent(player -> {
        int itemId = ITEMS[ThreadLocalRandom.current().nextInt(ITEMS.length)];
        player.getItems().addItem(itemId, 1);
        msgAll("@bla@[@blu@WILDY BOSSES@bla@]@red@TzTok-Jad @bla@has been defeated by @red@"+player.playerName+"@bla@(@blu@" + ItemDefinition.forId(itemId).getName()  + "@bla@)");
        msgAll("@bla@[@blu@WILDY BOSSES@bla@] A random wilderness Boss will spawn in @red@1 @bla@hour.");
        });
    }
 
    private static void msgAll(String msg) {
        Arrays.stream(PlayerHandler.players).filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(msg));
 
    }
 
}