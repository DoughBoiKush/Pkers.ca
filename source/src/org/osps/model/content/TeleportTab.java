package org.osps.model.content;

import org.osps.model.content.teleport.Position;
import org.osps.model.content.teleport.TeleportExecutor;
import org.osps.model.players.Player;


public enum TeleportTab {
    
    
        ARDOUGNE("Rock Crabs @red@ (PVP)", 0, new Position(2672, 3712, 0), 0),
        EAST_DRAGONS("East Dragons @red@(Wild 20)", 0, new Position(3347, 3672, 0), 1),
        EDGEVILLE("Barrows @red@ (PVP)", 0, new Position(3565, 3314, 0), 2),
        LAVADRAGON("Lava Dragons @red@(Wild 43)", 0, new Position(3202, 3859, 0), 3),
        MAGEBANK("Mage Bank @red@(PVP)", 0, new Position(2540, 4715, 0), 5),
        FOURFOURPORTALS("44 Portals @red@(Wild 44)", 0, new Position(2980, 3871, 0), 7),

        
        //BARRELCHEST("Barrelchest", 1, new Position(3808, 2844, 0), 0),
        KBD("KBD Entrance @red@(Wild 42)", 1, new Position(3005, 3850, 0), 10),
  
        LUMBRIDGE("Lumbridge", 2, new Position(3234, 3219, 0), 0),
        ALKHARID("Alkharid", 2, new Position(3293, 3183, 0), 1),
        VARROCK("Varrock", 2, new Position(3212, 3428, 0), 2),
        EDGEVILLECITY("Edgeville", 2, new Position(3087, 3490, 0), 3),
        CANIFIS("Canifis", 2, new Position(3496, 3489, 0), 4),
        DRAYNOR("Draynor", 2, new Position(3094, 3248, 0), 5),
        FALADOR("Falador", 2, new Position(2965, 3380, 0), 6),
        TAVERLY("Taverly", 2, new Position(2893, 3466, 0), 7),
        CAMELOT("Camelot", 2, new Position(2727, 3484, 0), 8),
        YANILLE("Yanille", 2, new Position(2605, 3091, 0), 9),
        RELLEKKA("Rellekka", 2, new Position(2658, 3659, 0), 10),
        
        
        HOME2("Home2", 4, new Position(3351, 3345, 0), 0),
        EAST("East @red@(Wild 20)", 4, new Position(3361, 3676, 0), 1),
        GORILLAS("Gorillas @red@(Wild 26)", 4, new Position(3159, 3727, 0), 2),
        PARTY("Party", 4, new Position(2096, 3206, 0), 3),
        FUNPK("FunPk", 4, new Position(3179, 9758, 0), 4),
        HELL("Hell", 4, new Position(3035, 5229, 0), 5),
        CREATIONS("Creations", 4, new Position(2794, 3321, 0), 6),
        HEAVEN("Heaven", 4, new Position(2804, 3184, 0), 7),
        DARKCASTLE("Dark Castle @red@(Wild 14)", 4, new Position(3010, 3631, 0), 8),
        PKER099("Pker099", 4, new Position(3374, 2971, 0), 9),
        KINGDARK("KingDark", 4, new Position(3233, 9315, 0), 10),
        GOLDS("Golds", 4, new Position(3291, 3027, 0), 11),
        METH("Meth", 4, new Position(3087, 3500, 0), 12),
        RISK("Risk", 4, new Position(3087, 3500, 0), 13),
        HYBRID("Hybrid", 4, new Position(3087, 3500, 0), 13),
        
        
        DUELARENA("Duel Arena", 3, new Position(3365, 3266, 0), 0),
        
    ;
        
    private final String name;
    private final int page, slot;
    private final Position location;
    private TeleportTab(String teleportTitle, int pageId, Position loc, int slotId) {
            this.name = teleportTitle;
            this.page = pageId;
            this.location = loc;
            this.slot = slotId;
    }
            
    
    
    public static TeleportTab forSlot(int button, int page) {
        button -= 182020;
        for (TeleportTab tt : TeleportTab.values()) {
            if (tt.getSlot() == button && tt.getPage() == page) {
                return tt;
            }
        }
        return null;
    }
    
    public static boolean clickButton(Player c, int button) {
        if (button == 182013) {
            nextPage(c);
            return true;
        } else if (button == 182016) {
            previousPage(c);
            return true;
        }
        TeleportTab tele = forSlot(button, c.teleTabPage);
        if (tele != null) {
            teleport(c, tele);
            return true;
        }
        return false;
    }
    
    
    
    
    private static void teleport(Player c, TeleportTab t) {
        TeleportExecutor.teleport(c, t.getLocation());
    }
    
    
    
    private static void nextPage(Player c) {
        if (c.teleTabPage < 3) {
            c.teleTabPage++;
        } else {
            c.teleTabPage = 0;
        }
        loadPage(c);
    }
    
    
    
    private static void previousPage(Player c) {
         if (c.teleTabPage > 0) {
            c.teleTabPage --;
        } else {
            c.teleTabPage = 3;
        }
         loadPage(c);
    }
    
    
    
    public static void loadPage(Player c) {
        c.getPA().sendFrame126(PAGE_NAMES[c.teleTabPage], 46604);
        for (int i = 0; i < 20; i++) {
            c.getPA().sendFrame126(" ", 46612 + i);
        }
        for (TeleportTab tt : TeleportTab.values()) {
            if (tt.getPage() == c.teleTabPage) {
                c.getPA().sendFrame126(tt.getName(), 46612 + tt.getSlot());
            }
        }
    }
    
    
    private static final String[] PAGE_NAMES = {"PKing Spots", "", "Cities", "Minigames"};
    
    
    
    public String getName() {
        return this.name;
    }
    
    public Position getLocation() {
        return this.location;
    }
    
    public int getPage() {
        return this.page;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
}
