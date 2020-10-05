package tk.thesenate.manhunt;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class CompassWorker {

    private final ManhuntMgr manhuntMgr = ManhuntMgr.getInstance();

    public CompassWorker(Manhunt manhunt) {

        manhunt.getServer().getScheduler().scheduleSyncRepeatingTask(manhunt, () -> {
            if (manhuntMgr.isManhuntOngoing() && getServer().getOnlinePlayers().size() > 1) {
                for (UUID l : manhuntMgr.getHunters()) {
                    Player hunter = getPlayer(l);
                    int index = manhuntMgr.getHunters().indexOf(l);
                    if (hunter == null) {
                        manhuntMgr.getHunters().remove(l);
                        continue;
                    }

                    if (manhuntMgr.isTrackingNearestPlayer().get(index)) {
                        manhuntMgr.getTracking().set(index, manhuntMgr.getNearestPlayer(hunter));
                    }

                    if (hunter.getWorld().getEnvironment().equals(World.Environment.NORMAL) && hunter.getWorld().getEnvironment().equals(manhuntMgr.getTracking().get(index).getWorld().getEnvironment())) {
                        hunter.setCompassTarget(manhuntMgr.getTracking().get(index).getLocation());
                    } else if (hunter.getWorld().getEnvironment().equals(World.Environment.NETHER) && hunter.getWorld().getEnvironment().equals(manhuntMgr.getTracking().get(index).getWorld().getEnvironment())) {
                        for (ItemStack i : hunter.getInventory().getContents()) {
                            if (i != null && i.getType().equals(Material.COMPASS)) {
                                CompassMeta im = (CompassMeta) i.getItemMeta();
                                im.setLodestone(manhuntMgr.getTracking().get(index).getLocation());
                                i.setItemMeta(im);
                            }
                        }
                    }
                }
            }

        }, 0L, 1L);
    }

}
