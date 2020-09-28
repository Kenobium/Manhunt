package tk.thesenate.manhunt;

import org.bukkit.entity.Player;

import java.util.UUID;

import static org.bukkit.Bukkit.*;

public class CompassWorker {

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

                    /*trackingDim = manhuntMgr.tracking.getWorld().getEnvironment();
                    hunterDim = hunter.getWorld().getEnvironment();

                    if (trackingDim.equals(World.Environment.NORMAL) && hunterDim.equals(World.Environment.NORMAL) || trackingDim.equals(World.Environment.NETHER) && hunterDim.equals(World.Environment.NETHER)) {
                        manhuntMgr.trackerMeta.setLodestone(manhuntMgr.tracking.getLocation());
                    } else { //if ((trackingDim.equals(World.Environment.NORMAL) && hunterDim.equals(World.Environment.NETHER)) || (trackingDim.equals(World.Environment.NETHER) && hunterDim.equals(World.Environment.NORMAL)))
                        manhuntMgr.trackerMeta.setDisplayName("No players to track in this dimension");
                    }*/
                    manhuntMgr.metas.get(index).setLodestone(manhuntMgr.tracking.get(index).getLocation());
                    manhuntMgr.compasses.get(index).setItemMeta(manhuntMgr.metas.get(index));
                    hunter.getInventory().setItem(0, manhuntMgr.compasses.get(index));
                    hunter.updateInventory();
                }
            }

        }, 5L, 5L);
    }

}
