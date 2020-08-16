package tk.thesenate.durverplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
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
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServerIcon;

public class DurverPlugin extends JavaPlugin {

    ManhuntCmd manhuntCmd = new ManhuntCmd();
    int currentTargetIndex = -1;


    @Override
    public void onEnable() {

        getLogger().info("Durver plugin enabled.");
        getCommand("manhunt").setExecutor(manhuntCmd);
        new CompassWorker(this);

        //getServer().getPluginManager().registerEvents(this, this);



        /*Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.getLogger().info(String.valueOf(currentTargetIndex));
                Bukkit.getLogger().info(String.valueOf(trackingNearestPlayer));
            }

        }, 250L, 250L);*/
    }

    @Override
    public void onDisable() {
        getLogger().info("Durver plugin disabled.");
    }

}