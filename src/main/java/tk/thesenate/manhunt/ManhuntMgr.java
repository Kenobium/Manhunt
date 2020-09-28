package tk.thesenate.manhunt;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.ArrayList;
import java.util.UUID;

public class ManhuntMgr {

    final ArrayList<UUID> hunters = new ArrayList<>();
    final ArrayList<UUID> runners = new ArrayList<>();
    final ArrayList<ItemStack> compasses = new ArrayList<>();
    final ArrayList<CompassMeta> metas = new ArrayList<>();
    ArrayList<Player> tracking = new ArrayList<>();
    final ArrayList<Boolean> trackingNearestPlayer = new ArrayList<>();
    boolean manhuntOngoing = false;
    private static final ManhuntMgr instance = new ManhuntMgr();

    private ManhuntMgr() {
        /*for (CompassMeta c : trackerMeta) {
            c.setDisplayName(ChatColor.DARK_PURPLE + "Right click to set");
            c.setLodestoneTracked(false);
        }
        for (int i = 0; i < trackerCompass.size(); i++) {
            trackerCompass.get(i).setItemMeta(trackerMeta.get(i));
        }*/

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
