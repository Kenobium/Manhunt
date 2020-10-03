package tk.thesenate.manhunt;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class ManhuntMgr {

    final ArrayList<UUID> hunters = new ArrayList<>();
    final ArrayList<UUID> runners = new ArrayList<>();
    final ArrayList<Boolean> trackingNearestPlayer = new ArrayList<>();
    ArrayList<Player> tracking = new ArrayList<>();
    boolean manhuntOngoing = false;
    private static final ManhuntMgr instance = new ManhuntMgr();

    private ManhuntMgr() {

    }

    public static ManhuntMgr getInstance() {
        return instance;
    }

    Player getNearestPlayer(Player player) {
        Player nearest = null;
        double lastDistance = Double.MAX_VALUE;
        for (Player p : player.getWorld().getPlayers()) {
            if (player == p || hunters.contains(p.getUniqueId()))
                continue;

            double distance = player.getLocation().distance(p.getLocation());
            if (distance < lastDistance) {
                lastDistance = distance;
                nearest = p;
            }
        }
        return nearest;
    }


}
