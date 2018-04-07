package org.osps.model.content.hs;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.osps.model.players.Player;
import org.osps.model.players.PlayerHandler;
import org.osps.util.Misc;

/**
 * Created by Satanizer666 on 11/19/2014.
 * A class that implements our Highscore interface. This class will handle the total level highscore.
 */
public class PKHighscore implements Highscore {

    private String type = "";
    private ArrayList<Player> playerList = new ArrayList<Player>(10);

    @Override
    public void process() {
        playerList = (ArrayList<Player>) Arrays.asList(PlayerHandler.players).stream().filter(p -> p != null).collect(Collectors.toList());

        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                Player client1 = (Player) player1;
                Player client2 = (Player) player2;

                if (client1.KC == client2.KC) {
                    return 0;
                } else if (client2.KC > client1.KC) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void generateList(Player client) {
        resetList(client);
        int amount = 0;
        for (int i = 0; i < playerList.size(); i++) {
        	Player rankedClient = (Player) playerList.get(i);
        	DecimalFormat df = new DecimalFormat("#.##");
    		Double KDR = ((double)rankedClient.KC)/((double)rankedClient.DC);
        	client.getPA().sendFrame126(""+(i + 1)+"): @gre@"+Misc.optimizeText(rankedClient.playerName2)+"", 42013 + i);
        	client.getPA().sendFrame126(""+rankedClient.KC+"", 42023 + i);
        	client.getPA().sendFrame126(""+rankedClient.DC+"", 42033 + i);
        	client.getPA().sendFrame126(""+df.format(KDR)+"", 42043 + i);
        	client.getPA().sendFrame126(String.format(""+rankedClient.killStreak+""), 42053 + i);
                amount++;
                if (amount == 10)
                    break;
        }
        client.getPA().showInterface(42000);
        client.flushOutStream();
        playerList.clear();
    }

    @Override
    public void resetList(Player client) {
        client.getPA().sendFrame126("Pkers.Ca Hiscores:" + getType(), 42002);
        for (int i = 42013; i < 42063; i++) {
            client.getPA().sendFrame126("", i);
            client.flushOutStream();
        }
    }
}
