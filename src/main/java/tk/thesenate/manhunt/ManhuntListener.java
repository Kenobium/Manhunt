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
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

import static org.bukkit.Bukkit.getPlayer;

public class ManhuntListener implements Listener {

    private final ManhuntMgr manhuntMgr = ManhuntMgr.getInstance();
    private int targetIndex = -1;

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        int playerIndex = manhuntMgr.getHunters().indexOf(event.getPlayer().getUniqueId());
        if (manhuntMgr.isManhuntOngoing() && event.getItemDrop().getItemStack().getType().equals(Material.COMPASS) && manhuntMgr.getHunters().contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatColor.BLUE + "You dropped this");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {

        int playerIndex = manhuntMgr.getHunters().indexOf(event.getPlayer().getUniqueId());

        if (manhuntMgr.isManhuntOngoing() && event.hasItem() && Objects.equals(event.getItem().getType(), Material.COMPASS/*manhuntMgr.compasses.get(playerIndex)*/) && playerIndex != -1 && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            CompassMeta im = (CompassMeta) event.getItem().getItemMeta();
            if (targetIndex + 1 < manhuntMgr.getRunners().size()) {
                manhuntMgr.isTrackingNearestPlayer().set(playerIndex, false);
                targetIndex++;
                manhuntMgr.getTracking().set(playerIndex, getPlayer(manhuntMgr.getRunners().get(targetIndex)));
            } else {
                manhuntMgr.isTrackingNearestPlayer().set(playerIndex, true);
                targetIndex = -1;
            }

            if (manhuntMgr.getNearestPlayer(event.getPlayer()) == null) {
                manhuntMgr.isTrackingNearestPlayer().set(playerIndex, false);
                //manhuntMgr.metas.get(playerIndex).setDisplayName(ChatColor.RED + "No players to track in this dimension!");
                im.setDisplayName(ChatColor.RED + "No players to track in this dimension!");
            } else {
                if (!manhuntMgr.isTrackingNearestPlayer().get(playerIndex)) {
                    World.Environment hunterDim = event.getPlayer().getWorld().getEnvironment();
                    World.Environment trackingDim = manhuntMgr.getTracking().get(playerIndex).getWorld().getEnvironment();
                    if (hunterDim.equals(trackingDim)) {
                        //manhuntMgr.metas.get(playerIndex).setDisplayName(ChatColor.GREEN + "Tracking " + manhuntMgr.tracking.get(playerIndex).getName());
                        im.setDisplayName(ChatColor.GREEN + "Tracking " + manhuntMgr.getTracking().get(playerIndex).getName());
                    } else {
                        //manhuntMgr.metas.get(playerIndex).setDisplayName(ChatColor.GOLD + manhuntMgr.tracking.get(playerIndex).getName() + " is not in this dimension");
                        im.setDisplayName(ChatColor.GOLD + manhuntMgr.getTracking().get(playerIndex).getName() + " is not in this dimension");
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
        int playerIndex = manhuntMgr.getHunters().indexOf(event.getPlayer().getUniqueId());
        if (manhuntMgr.isManhuntOngoing() && manhuntMgr.getHunters().contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().getInventory().addItem(new ItemStack(Material.COMPASS));

        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        int playerIndex = manhuntMgr.getHunters().indexOf(event.getEntity().getPlayer().getUniqueId());
        if (manhuntMgr.isManhuntOngoing() && manhuntMgr.getHunters().contains(event.getEntity().getUniqueId())) {
            for (Entity e : event.getEntity().getWorld().getEntities()) {
                if (e instanceof Item && ((Item) e).getItemStack().getType().equals(Material.COMPASS)) {
                    e.remove();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        manhuntMgr.getHunters().remove(event.getPlayer().getUniqueId());
        manhuntMgr.getRunners().remove(event.getPlayer().getUniqueId());
        if (manhuntMgr.getHunters().contains(event.getPlayer().getUniqueId())) {
            int playerIndex = manhuntMgr.getHunters().indexOf(event.getPlayer().getUniqueId());
            manhuntMgr.isTrackingNearestPlayer().remove(manhuntMgr.isTrackingNearestPlayer().size() - 1);
            manhuntMgr.getTracking().remove(playerIndex);
        }
    }

    @EventHandler
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        if (manhuntMgr.isManhuntOngoing() && event.getFrom().getEnvironment().equals(World.Environment.NETHER)) {

            if (manhuntMgr.getHunters().contains(event.getPlayer().getUniqueId())) {
                manhuntMgr.isTrackingNearestPlayer().set(manhuntMgr.getHunters().indexOf(event.getPlayer().getUniqueId()), false);
                PlayerInventory inv = event.getPlayer().getInventory();
                for (int i = 0; i < inv.getSize(); i++) {
                    if (inv.getItem(i) != null && inv.getItem(i).getType().equals(Material.COMPASS)) {
                        ItemStack c = new ItemStack(Material.COMPASS);
                        ItemMeta im = c.getItemMeta();
                        im.setDisplayName(ChatColor.DARK_PURPLE + "Right click to set");
                        c.setItemMeta(im);
                        inv.setItem(i, c);
                    }
                }

            } else if (manhuntMgr.getRunners().contains(event.getPlayer().getUniqueId())) {
                for (Player p : event.getFrom().getPlayers()) {
                    if (manhuntMgr.getHunters().contains(p.getUniqueId())) {
                        manhuntMgr.isTrackingNearestPlayer().set(manhuntMgr.getHunters().indexOf(p.getUniqueId()), false);
                    }
                }
            }

        } else if (manhuntMgr.isManhuntOngoing() && event.getFrom().getEnvironment().equals(World.Environment.NORMAL)) {
            if (manhuntMgr.getHunters().contains(event.getPlayer().getUniqueId())) {
                manhuntMgr.isTrackingNearestPlayer().set(manhuntMgr.getHunters().indexOf(event.getPlayer().getUniqueId()), false);
            }
        }
    }

    @EventHandler
    public void onCraftItemEvent(CraftItemEvent event) {
        if (manhuntMgr.isManhuntOngoing() && manhuntMgr.getHunters().contains(event.getWhoClicked().getUniqueId()) && event.getCurrentItem().getType().equals(Material.COMPASS)) {
            event.getWhoClicked().sendMessage(ChatColor.GOLD + "You already have a compass!");
            event.setCancelled(true);
        }
    }

}