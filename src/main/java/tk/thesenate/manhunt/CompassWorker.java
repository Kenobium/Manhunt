package tk.thesenate.manhunt;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

public class CompassWorker {

    private final ManhuntMgr manhuntMgr = ManhuntMgr.getInstance();
    static int compassPos;

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

                    for (ItemStack i: hunter.getInventory().getContents()) {
                        if (i != null && i.getType().equals(Material.COMPASS)) {
                            compassPos = Arrays.asList(hunter.getInventory().getContents()).indexOf(i);
                        }
                    }
                    //compassPos = Arrays.asList(hunter.getInventory().getContents()).indexOf(Material.COMPASS);
                    if (manhuntMgr.tracking.get(index) != null) {
                        manhuntMgr.metas.get(index).setLodestone(manhuntMgr.tracking.get(index).getLocation());
                        manhuntMgr.compasses.get(index).setItemMeta(manhuntMgr.metas.get(index));
                    }
                    if (compassPos != -1) {
                        hunter.getInventory().setItem(compassPos, manhuntMgr.compasses.get(index));
                        //hunter.getInventory().getItem(compassPos).setItemMeta(manhuntMgr.metas.get(index));
                    }
                }
            }

        }, 0L, 1L);
    }

}
