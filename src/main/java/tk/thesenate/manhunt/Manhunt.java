package tk.thesenate.manhunt;

import org.bukkit.plugin.java.JavaPlugin;



public class Manhunt extends JavaPlugin {


    @Override
    public void onEnable() {

        getLogger().info("Manhunt plugin enabled.");
        getServer().getPluginManager().registerEvents(new ManhuntListener(), this);
        getCommand("manhunt").setExecutor(new ManhuntCmd());
        new CompassWorker(this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Manhunt plugin disabled.");
    }

}