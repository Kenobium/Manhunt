package tk.thesenate.durverplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class DurverPlugin extends JavaPlugin implements Listener {

    //private final CompassWorker compassWorker = new CompassWorker();
    //private final Thread pointCompass = new Thread(compassWorker);
    ManhuntCmd manhuntCmd = new ManhuntCmd();

    @Override
    public void onEnable() {
        getLogger().info("Durver plugin enabled.");
        getCommand("manhunt").setExecutor(manhuntCmd);
        getServer().getPluginManager().registerEvents(this, this);

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (manhuntCmd.manhuntOngoing && getServer().getOnlinePlayers().size() > 1) {
                for (UUID l : manhuntCmd.hunters) {
                    Player hunter = getPlayer(l);
                    if (hunter == null) {
                        manhuntCmd.hunters.remove(l);
                        continue;
                    }
                    if (!manhuntCmd.hunters.contains(manhuntCmd.getNearestPlayer(hunter).getUniqueId())) {
                        hunter.setCompassTarget(manhuntCmd.getNearestPlayer(hunter).getLocation());
                    }
                }
            }
        }, 1L, 1L);
        //pointCompass.start();
    }

    @Override
    public void onDisable() {
        getLogger().info("Durver plugin disabled.");
    }

    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().equals(new ItemStack(Material.COMPASS)) && manhuntCmd.hunters.contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().sendMessage(ChatColor.BLUE + "You dropped this");
            event.setCancelled(true);
        }
    }

}
