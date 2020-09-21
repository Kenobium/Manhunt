package tk.thesenate.manhunt;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
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

    private int currentTargetIndex = -1;

    private final ManhuntMgr manhuntMgr;

    public ManhuntListener(ManhuntMgr manhuntMgr) {
        this.manhuntMgr = manhuntMgr;
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (manhuntMgr.manhuntOngoing && event.getItemDrop().getItemStack().equals(manhuntMgr.trackerCompass) && manhuntMgr.hunters.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatColor.BLUE + "You dropped this");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        event.getPlayer().sendMessage(String.valueOf(Objects.equals(event.getItem(), manhuntMgr.trackerCompass)));

        if (manhuntMgr.manhuntOngoing && event.hasItem() && Objects.equals(event.getItem(), manhuntMgr.trackerCompass) && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (currentTargetIndex + 1 < manhuntMgr.runners.size()) {
                manhuntMgr.trackingNearestPlayer = false;
                currentTargetIndex++;
                manhuntMgr.tracking = getPlayer(manhuntMgr.runners.get(currentTargetIndex));
            } else {
                manhuntMgr.trackingNearestPlayer = true;
                currentTargetIndex = -1;
            }

            if (manhuntMgr.getNearestPlayer(event.getPlayer()) == null) {
                manhuntMgr.trackingNearestPlayer = false;
                manhuntMgr.trackerMeta.setDisplayName(ChatColor.RED + "No players to track in this dimension!");
            } else if (!manhuntMgr.trackingNearestPlayer) {
                manhuntMgr.trackerMeta.setDisplayName(ChatColor.GREEN + "Tracking " + manhuntMgr.tracking.getName());
            } else {    
                manhuntMgr.trackerMeta.setDisplayName(ChatColor.GREEN + "Tracking nearest player");
            }

            /*if (Objects.equals(event.getHand(), EquipmentSlot.HAND)) {
                event.getPlayer().getInventory().getItemInMainHand().setItemMeta(manhuntMgr.trackerMeta);
            } else if (Objects.equals(event.getHand(), EquipmentSlot.OFF_HAND)) {
                event.getPlayer().getInventory().getItemInOffHand().setItemMeta(manhuntMgr.trackerMeta);
            }*/

            manhuntMgr.trackerCompass.setItemMeta(manhuntMgr.trackerMeta);
            event.getPlayer().updateInventory();
        }

    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        if (manhuntMgr.manhuntOngoing && manhuntMgr.hunters.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().addItem(manhuntMgr.trackerCompass);

        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        if (manhuntMgr.manhuntOngoing && manhuntMgr.hunters.contains(event.getEntity().getUniqueId())) {
            for (Entity e : event.getEntity().getWorld().getEntities()) {
                if (e instanceof Item && ((Item) e).getItemStack().equals(manhuntMgr.trackerCompass)) {
                    e.remove();
                }
            }
        }
    }

}

    
