package tk.thesenate.durverplugin;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class CompassWorker {

    private final ManhuntCmd manhuntCmd = new ManhuntCmd();

    public CompassWorker(DurverPlugin durverPlugin) {
        durverPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(durverPlugin, () -> {

            if (manhuntCmd.manhuntOngoing && getServer().getOnlinePlayers().size() > 1) {
                for (UUID l : manhuntCmd.hunters) {
                    Player hunter = getPlayer(l);
                    if (hunter == null) {
                        manhuntCmd.hunters.remove(l);
                        continue;
                    }

                    if (ManhuntListener.trackingNearestPlayer) { //!compassManuallySet
                        ManhuntListener.tracking = manhuntCmd.getNearestPlayer(hunter);
                    }

                    World.Environment trackingDim = ManhuntListener.tracking.getWorld().getEnvironment();
                    World.Environment hunterDim = hunter.getWorld().getEnvironment();
                    CompassMeta lodestoneTracker = (CompassMeta) manhuntCmd.trackerCompass.getItemMeta();
                    ItemMeta noPlayers = manhuntCmd.trackerCompass.getItemMeta();
                    noPlayers.setDisplayName("No players to track in this dimension");

                    if (trackingDim.equals(World.Environment.NORMAL) && hunterDim.equals(World.Environment.NORMAL)) {
                        hunter.setCompassTarget(ManhuntListener.tracking.getLocation()); //manhuntCmd.getNearestPlayer(hunter).getLocation()
                    } else if (trackingDim.equals(World.Environment.NETHER) && hunterDim.equals(World.Environment.NETHER)) {
                        lodestoneTracker.setLodestoneTracked(false);
                        lodestoneTracker.setLodestone(ManhuntListener.tracking.getLocation());
                        manhuntCmd.trackerCompass.setItemMeta(lodestoneTracker);
                    } else {
                        manhuntCmd.trackerCompass.setItemMeta(noPlayers);
                    }

                }
            }

        }, 5L, 5L);
    }

}
