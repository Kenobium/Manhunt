package tk.thesenate.manhunt;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManhuntMgr {

    private final List<UUID> hunters = new ArrayList<>();
    private final List<UUID> runners = new ArrayList<>();
    private final List<Boolean> trackingNearestPlayer = new ArrayList<>();
    private List<Player> tracking = new ArrayList<>();
    private boolean manhuntOngoing = false;
    private static final ManhuntMgr instance = new ManhuntMgr();

    private ManhuntMgr() {

    }

    public static ManhuntMgr getInstance() {return instance;}
    public List<UUID> getHunters() {return hunters;}
    public List<UUID> getRunners() {return runners;}
    public List<Boolean> isTrackingNearestPlayer(){return trackingNearestPlayer;}
    public List<Player> getTracking() {return tracking;}
    public void setTracking(List<Player> newList) {tracking = newList;}
    public boolean isManhuntOngoing() {return manhuntOngoing;}
    public void setManhuntOngoing(boolean newVal) {manhuntOngoing = newVal;}

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
