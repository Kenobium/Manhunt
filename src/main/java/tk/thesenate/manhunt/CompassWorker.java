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

    static int compassPos;
    private final ManhuntMgr manhuntMgr = ManhuntMgr.getInstance();

    public CompassWorker(Manhunt manhunt) {

        manhunt.getServer().getScheduler().scheduleSyncRepeatingTask(manhunt, () -> {
            if (manhuntMgr.manhuntOngoing && getServer().getOnlinePlayers().size() > 1) {
                for (UUID l : manhuntMgr.hunters) {
                    Player hunter = getPlayer(l);
                    int index = manhuntMgr.hunters.indexOf(l);
                    if (hunter == null) {
                        manhuntMgr.hunters.remove(l);
                        continue;
                    }

                    if (manhuntMgr.trackingNearestPlayer.get(index)) {
                        manhuntMgr.tracking.set(index, manhuntMgr.getNearestPlayer(hunter));
                    }

                    if (hunter.getWorld().getEnvironment().equals(World.Environment.NORMAL) && hunter.getWorld().getEnvironment().equals(manhuntMgr.tracking.get(index).getWorld().getEnvironment())) {
                        hunter.setCompassTarget(manhuntMgr.tracking.get(index).getLocation());
                    } else if (hunter.getWorld().getEnvironment().equals(World.Environment.NETHER) && hunter.getWorld().getEnvironment().equals(manhuntMgr.tracking.get(index).getWorld().getEnvironment())) {
                        for (ItemStack i : hunter.getInventory().getContents()) {
                            if (i != null && i.getType().equals(Material.COMPASS)) {
                                CompassMeta im = (CompassMeta) i.getItemMeta();
                                im.setLodestone(manhuntMgr.tracking.get(index).getLocation());
                                i.setItemMeta(im);
                            }
                        }
                    }
                }
            }

        }, 0L, 1L);
    }

}
