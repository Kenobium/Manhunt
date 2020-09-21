package tk.thesenate.manhunt;

import org.bukkit.entity.Player;

import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class CompassWorker {


    public CompassWorker(Manhunt manhunt, ManhuntMgr manhuntMgr) {

        manhunt.getServer().getScheduler().scheduleSyncRepeatingTask(manhunt, () -> {

            if (manhuntMgr.manhuntOngoing && getServer().getOnlinePlayers().size() > 1) {
                for (UUID l : manhuntMgr.hunters) {
                    Player hunter = getPlayer(l);
                    if (hunter == null) {
                        manhuntMgr.hunters.remove(l);
                        continue;
                    }

                    if (manhuntMgr.trackingNearestPlayer) {
                        manhuntMgr.tracking = manhuntMgr.getNearestPlayer(hunter);
                    }

                    /*trackingDim = manhuntMgr.tracking.getWorld().getEnvironment();
                    hunterDim = hunter.getWorld().getEnvironment();

                    if (trackingDim.equals(World.Environment.NORMAL) && hunterDim.equals(World.Environment.NORMAL) || trackingDim.equals(World.Environment.NETHER) && hunterDim.equals(World.Environment.NETHER)) {
                        manhuntMgr.trackerMeta.setLodestone(manhuntMgr.tracking.getLocation());
                    } else { //if ((trackingDim.equals(World.Environment.NORMAL) && hunterDim.equals(World.Environment.NETHER)) || (trackingDim.equals(World.Environment.NETHER) && hunterDim.equals(World.Environment.NORMAL)))
                        manhuntMgr.trackerMeta.setDisplayName("No players to track in this dimension");
                    }*/
                    manhuntMgr.trackerMeta.setLodestone(manhuntMgr.tracking.getLocation());
                    manhuntMgr.trackerCompass.setItemMeta(manhuntMgr.trackerMeta);
                    hunter.updateInventory();
                }
            }

        }, 5L, 5L);
    }

}
