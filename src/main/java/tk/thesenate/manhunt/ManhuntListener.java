package tk.thesenate.manhunt;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;

public class ManhuntListener implements Listener {

    private final ManhuntMgr manhuntMgr = ManhuntMgr.getInstance();
    private int targetIndex = -1;

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        int playerIndex = manhuntMgr.hunters.indexOf(event.getPlayer().getUniqueId());
        if (manhuntMgr.manhuntOngoing && event.getItemDrop().getItemStack().getType().equals(Material.COMPASS) && manhuntMgr.hunters.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatColor.BLUE + "You dropped this");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        int playerIndex = manhuntMgr.hunters.indexOf(event.getPlayer().getUniqueId());

        if (manhuntMgr.manhuntOngoing && event.hasItem() && Objects.equals(event.getItem().getType(), Material.COMPASS/*manhuntMgr.compasses.get(playerIndex)*/) && playerIndex != -1 && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            CompassMeta im = (CompassMeta) event.getItem().getItemMeta();
            if (targetIndex + 1 < manhuntMgr.runners.size()) {
                manhuntMgr.trackingNearestPlayer.set(playerIndex, false);
                targetIndex++;
                manhuntMgr.tracking.set(playerIndex, getPlayer(manhuntMgr.runners.get(targetIndex)));
            } else {
                manhuntMgr.trackingNearestPlayer.set(playerIndex, true);
                targetIndex = -1;
            }

            if (manhuntMgr.getNearestPlayer(event.getPlayer()) == null) {
                manhuntMgr.trackingNearestPlayer.set(playerIndex, false);
                //manhuntMgr.metas.get(playerIndex).setDisplayName(ChatColor.RED + "No players to track in this dimension!");
                im.setDisplayName(ChatColor.RED + "No players to track in this dimension!");
            } else {
                if (!manhuntMgr.trackingNearestPlayer.get(playerIndex)) {
                    World.Environment hunterDim = event.getPlayer().getWorld().getEnvironment();
                    World.Environment trackingDim = manhuntMgr.tracking.get(playerIndex).getWorld().getEnvironment();
                    if (hunterDim.equals(trackingDim)) {
                        //manhuntMgr.metas.get(playerIndex).setDisplayName(ChatColor.GREEN + "Tracking " + manhuntMgr.tracking.get(playerIndex).getName());
                        im.setDisplayName(ChatColor.GREEN + "Tracking " + manhuntMgr.tracking.get(playerIndex).getName());
                    } else {
                        //manhuntMgr.metas.get(playerIndex).setDisplayName(ChatColor.GOLD + manhuntMgr.tracking.get(playerIndex).getName() + " is not in this dimension");
                        im.setDisplayName(ChatColor.GOLD + manhuntMgr.tracking.get(playerIndex).getName() + " is not in this dimension");
                    }
                } else {
                    im.setDisplayName(ChatColor.GREEN + "Tracking nearest player");
                }
            }

            //manhuntMgr.compasses.get(playerIndex).setItemMeta(manhuntMgr.metas.get(playerIndex));
            event.getItem().setItemMeta(im);
        }

    }

    /*@EventHandler
    public void onInventoryClickEvent (InventoryClickEvent event) {
        if (event.getCurrentItem().getType().equals(Material.COMPASS)) {
            CompassWorker.compassPos = -1;
        }
    }*/

    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        int playerIndex = manhuntMgr.hunters.indexOf(event.getPlayer().getUniqueId());
        if (manhuntMgr.manhuntOngoing && manhuntMgr.hunters.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));

        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        int playerIndex = manhuntMgr.hunters.indexOf(event.getEntity().getPlayer().getUniqueId());
        if (manhuntMgr.manhuntOngoing && manhuntMgr.hunters.contains(event.getEntity().getUniqueId())) {
            for (Entity e : event.getEntity().getWorld().getEntities()) {
                if (e instanceof Item && ((Item) e).getItemStack().getType().equals(Material.COMPASS)) {
                    e.remove();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        manhuntMgr.hunters.remove(event.getPlayer().getUniqueId());
        manhuntMgr.runners.remove(event.getPlayer().getUniqueId());
        if (manhuntMgr.hunters.contains(event.getPlayer().getUniqueId())) {
            int playerIndex = manhuntMgr.hunters.indexOf(event.getPlayer().getUniqueId());
            manhuntMgr.trackingNearestPlayer.remove(manhuntMgr.trackingNearestPlayer.size() - 1);
            manhuntMgr.tracking.remove(playerIndex);
        }
    }

    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        if (manhuntMgr.manhuntOngoing && event.getFrom().getEnvironment().equals(World.Environment.NETHER)) {

            if (manhuntMgr.hunters.contains(event.getPlayer().getUniqueId())) {
                PlayerInventory inv = event.getPlayer().getInventory();
                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) != null && inv.getItem(i).getType().equals(Material.COMPASS)) {
                        inv.setItem(i, new ItemStack(Material.COMPASS));
                    }
                }
                manhuntMgr.trackingNearestPlayer.set(manhuntMgr.hunters.indexOf(event.getPlayer().getUniqueId()), false);

            } else if (manhuntMgr.runners.contains(event.getPlayer().getUniqueId())) {
                for (Player p : event.getFrom().getPlayers()) {
                    if (manhuntMgr.hunters.contains(p.getUniqueId())) {
                        manhuntMgr.trackingNearestPlayer.set(manhuntMgr.hunters.indexOf(p.getUniqueId()), false);
                    }
                }
            }

        }
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        if (manhuntMgr.manhuntOngoing && manhuntMgr.hunters.contains(event.getWhoClicked().getUniqueId()) && event.getCurrentItem().getType().equals(Material.COMPASS)) {
            event.getWhoClicked().sendMessage(ChatColor.GOLD + "You already have a compass!");
            event.setCancelled(true);
        }
    }

}

    
