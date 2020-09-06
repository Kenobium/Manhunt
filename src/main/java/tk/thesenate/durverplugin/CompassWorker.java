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

            if (ManhuntCmd.manhuntOngoing && getServer().getOnlinePlayers().size() > 1) {
                for (UUID l : ManhuntCmd.hunters) {
                    Player hunter = getPlayer(l);
                    if (hunter == null) {
                        ManhuntCmd.hunters.remove(l);
                        continue;
                    }

                    if (ManhuntListener.trackingNearestPlayer) { //!compassManuallySet
                        ManhuntListener.tracking = manhuntCmd.getNearestPlayer(hunter);
                    }

                    World.Environment trackingDim = ManhuntListener.tracking.getWorld().getEnvironment();
                    World.Environment hunterDim = hunter.getWorld().getEnvironment();
                    CompassMeta lodestoneTracker = (CompassMeta) ManhuntCmd.trackerCompass.getItemMeta();
                    ItemMeta noPlayers = ManhuntCmd.trackerCompass.getItemMeta();
                    noPlayers.setDisplayName("No players to track in this dimension");

                    if (trackingDim.equals(World.Environment.NORMAL) && hunterDim.equals(World.Environment.NORMAL)) {
                        hunter.setCompassTarget(ManhuntListener.tracking.getLocation()); //manhuntCmd.getNearestPlayer(hunter).getLocation()
                    } else if (trackingDim.equals(World.Environment.NETHER) && hunterDim.equals(World.Environment.NETHER)) {
                        lodestoneTracker.setLodestoneTracked(false);
                        lodestoneTracker.setLodestone(ManhuntListener.tracking.getLocation());
                        ManhuntCmd.trackerCompass.setItemMeta(lodestoneTracker);
                    } else {
                        ManhuntCmd.trackerCompass.setItemMeta(noPlayers);
                    }

                }
            }

        }, 5L, 5L);
    }

}
