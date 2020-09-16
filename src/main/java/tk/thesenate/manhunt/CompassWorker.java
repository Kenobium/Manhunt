package tk.thesenate.manhunt;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class CompassWorker {

    private final ManhuntCmd manhuntCmd = new ManhuntCmd();
    static Player tracking;
    static boolean trackingNearestPlayer = true;
    World.Environment trackingDim;
    World.Environment hunterDim;

    public CompassWorker(Manhunt manhunt) {

        ItemMeta noPlayersMeta = ManhuntCmd.trackerCompass.getItemMeta();
        assert noPlayersMeta != null;
        noPlayersMeta.setDisplayName("No players to track in this dimension");

        manhunt.getServer().getScheduler().scheduleSyncRepeatingTask(manhunt, () -> {

            if (ManhuntCmd.manhuntOngoing && getServer().getOnlinePlayers().size() > 1) {
                for (UUID l : ManhuntCmd.hunters) {
                    Player hunter = getPlayer(l);
                    if (hunter == null) {
                        ManhuntCmd.hunters.remove(l);
                        continue;
                    }

                    if (trackingNearestPlayer) {
                        try {
                            tracking = manhuntCmd.getNearestPlayer(hunter);
                        } catch (NullPointerException e) {
                            ManhuntCmd.trackerCompass.setItemMeta(noPlayersMeta);
                        }

                    }

                    trackingDim = tracking.getWorld().getEnvironment();
                    hunterDim = hunter.getWorld().getEnvironment();
                    CompassMeta lodestoneTracker = (CompassMeta) ManhuntCmd.trackerCompass.getItemMeta();
                    lodestoneTracker.setLodestoneTracked(false);

                    if (trackingDim.equals(World.Environment.NORMAL) && hunterDim.equals(World.Environment.NORMAL)) {
                        hunter.setCompassTarget(tracking.getLocation()); 
                    } else if (trackingDim.equals(World.Environment.NETHER) && hunterDim.equals(World.Environment.NETHER)) {
                        lodestoneTracker.setLodestone(tracking.getLocation());
                        ManhuntCmd.trackerCompass.setItemMeta(lodestoneTracker);
                    }
    //                    else if ((trackingDim.equals(World.Environment.NORMAL) && hunterDim.equals(World.Environment.NETHER)) || (trackingDim.equals(World.Environment.NETHER) && hunterDim.equals(World.Environment.NORMAL))) {
    //                        ManhuntCmd.trackerCompass.setItemMeta(noPlayers);
    //                    }

                }
            }

        }, 5L, 5L);
    }

}
