package tk.thesenate.durverplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;

public class ManhuntListener implements Listener {

    private final ManhuntCmd manhuntCmd = new ManhuntCmd();
    private int currentTargetIndex = -1;
    static Player tracking;
    static boolean trackingNearestPlayer = true;

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (manhuntCmd.manhuntOngoing && event.getItemDrop().getItemStack().equals(manhuntCmd.trackerCompass) && manhuntCmd.hunters.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatColor.BLUE + "You dropped this");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (manhuntCmd.manhuntOngoing && event.hasItem() && Objects.equals(event.getItem(), manhuntCmd.trackerCompass) && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {

            //compassManuallySet = true;

            if (currentTargetIndex + 1 < manhuntCmd.runners.size()) {
                trackingNearestPlayer = false;
                tracking = getPlayer(manhuntCmd.runners.get(currentTargetIndex + 1));
                currentTargetIndex++;
            } else {
                trackingNearestPlayer = true;
                currentTargetIndex = -1;
                //compassManuallySet = false;
                //tracking = manhuntCmd.getNearestPlayer(event.getPlayer());
            }

            ItemMeta trackerMeta = manhuntCmd.trackerCompass.getItemMeta();

            if (!trackingNearestPlayer) {
                trackerMeta.setDisplayName("Tracking " + tracking.getName());
            } else {
                trackerMeta.setDisplayName("Tracking nearest player");
            }

            if (Objects.equals(event.getHand(), EquipmentSlot.HAND)) {
                event.getPlayer().getInventory().getItemInMainHand().setItemMeta(trackerMeta);
            } else if (Objects.equals(event.getHand(), EquipmentSlot.OFF_HAND)) {
                event.getPlayer().getInventory().getItemInOffHand().setItemMeta(trackerMeta);
            }

            manhuntCmd.trackerCompass.setItemMeta(trackerMeta);

        }
    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        if (manhuntCmd.manhuntOngoing && manhuntCmd.hunters.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().addItem(manhuntCmd.trackerCompass);

        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        if (manhuntCmd.manhuntOngoing && manhuntCmd.hunters.contains(event.getEntity().getUniqueId())) {
            for (Entity e: event.getEntity().getWorld().getEntities()) {
                if (e instanceof Item && ((Item) e).getItemStack().equals(manhuntCmd.trackerCompass)) {
                    e.remove();
                }
            }
        }
    }

}

    