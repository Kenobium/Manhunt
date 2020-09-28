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

import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;

public class ManhuntListener implements Listener {

    private final ManhuntMgr manhuntMgr = ManhuntMgr.getInstance();
    private int currentTargetIndex = -1;

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        int playerIndex = manhuntMgr.hunters.indexOf(event.getPlayer().getUniqueId());
        if (manhuntMgr.manhuntOngoing && event.getItemDrop().getItemStack().equals(manhuntMgr.compasses.get(playerIndex)) && manhuntMgr.hunters.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatColor.BLUE + "You dropped this");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        int playerIndex = manhuntMgr.hunters.indexOf(event.getPlayer().getUniqueId());
        if (manhuntMgr.manhuntOngoing && event.hasItem() && Objects.equals(event.getItem(), manhuntMgr.compasses.get(playerIndex)) && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (currentTargetIndex + 1 < manhuntMgr.runners.size()) {
                manhuntMgr.trackingNearestPlayer.set(playerIndex, false);
                currentTargetIndex++;
                manhuntMgr.tracking.set(playerIndex, getPlayer(manhuntMgr.runners.get(currentTargetIndex)));
            } else {
                manhuntMgr.trackingNearestPlayer.set(playerIndex, true);
                currentTargetIndex = -1;
            }

            if (manhuntMgr.getNearestPlayer(event.getPlayer()) == null) {
                manhuntMgr.trackingNearestPlayer.set(playerIndex, false);
                manhuntMgr.metas.get(playerIndex).setDisplayName(ChatColor.RED + "No players to track in this dimension!");
            } else {
                if (!manhuntMgr.trackingNearestPlayer.get(playerIndex)) {
                    manhuntMgr.metas.get(playerIndex).setDisplayName(ChatColor.GREEN + "Tracking " + manhuntMgr.tracking.get(playerIndex).getName());
                } else {
                    manhuntMgr.metas.get(playerIndex).setDisplayName(ChatColor.GREEN + "Tracking nearest player");
                }
            }

            /*if (Objects.equals(event.getHand(), EquipmentSlot.HAND)) {
                event.getPlayer().getInventory().getItemInMainHand().setItemMeta(manhuntMgr.trackerMeta);
            } else if (Objects.equals(event.getHand(), EquipmentSlot.OFF_HAND)) {
                event.getPlayer().getInventory().getItemInOffHand().setItemMeta(manhuntMgr.trackerMeta);
            }*/

            manhuntMgr.compasses.get(playerIndex).setItemMeta(manhuntMgr.metas.get(playerIndex));
            event.getPlayer().updateInventory();
        }

    }

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        int playerIndex = manhuntMgr.hunters.indexOf(event.getPlayer().getUniqueId());
        if (manhuntMgr.manhuntOngoing && manhuntMgr.hunters.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().addItem(manhuntMgr.compasses.get(playerIndex));

        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        int playerIndex = manhuntMgr.hunters.indexOf(event.getEntity().getPlayer().getUniqueId());
        if (manhuntMgr.manhuntOngoing && manhuntMgr.hunters.contains(event.getEntity().getUniqueId())) {
            for (Entity e : event.getEntity().getWorld().getEntities()) {
                if (e instanceof Item && ((Item) e).getItemStack().equals(manhuntMgr.compasses.get(playerIndex))) {
                    e.remove();
                }
            }
        }
    }

}

    
