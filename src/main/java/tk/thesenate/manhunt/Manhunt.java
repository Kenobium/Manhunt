package tk.thesenate.manhunt;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;


public class Manhunt extends JavaPlugin {

    ManhuntMgr manhuntMgr;
    ArrayList<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());

    @Override
    public void onEnable() {

        getLogger().info("Manhunt plugin enabled.");
        manhuntMgr = new ManhuntMgr();
        getServer().getPluginManager().registerEvents(new ManhuntListener(manhuntMgr), this);
        getCommand("manhunt").setExecutor(new ManhuntCmd(manhuntMgr));
        //new CompassWorker(this, manhuntMgr);

    }

    @Override
    public void onDisable() {
        getLogger().info("Manhunt plugin disabled.");
    }

}