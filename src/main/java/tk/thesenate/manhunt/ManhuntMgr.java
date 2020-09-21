package tk.thesenate.manhunt;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.ArrayList;
import java.util.UUID;

public class ManhuntMgr {
    final ArrayList<UUID> hunters = new ArrayList<>();
    final ArrayList<UUID> runners = new ArrayList<>();
    final ItemStack trackerCompass = new ItemStack(Material.COMPASS, 1);
    final CompassMeta trackerMeta = (CompassMeta) trackerCompass.getItemMeta();
    Player tracking;
    boolean trackingNearestPlayer = true;
    volatile boolean manhuntOngoing = false;

    public ManhuntMgr() {
        trackerMeta.setDisplayName(ChatColor.DARK_PURPLE + "Right click to set");
        trackerMeta.setLodestoneTracked(false);
        trackerCompass.setItemMeta(trackerMeta);
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
