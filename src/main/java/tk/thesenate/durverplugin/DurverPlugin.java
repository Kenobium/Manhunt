package tk.thesenate.durverplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class DurverPlugin extends JavaPlugin {

    ManhuntCmd manhuntCmd = new ManhuntCmd();
    int currentTargetIndex = -1;


    @Override
    public void onEnable() {

        getLogger().info("Durver plugin enabled.");
        getServer().getPluginManager().registerEvents(new ManhuntListener(), this);
        getCommand("manhunt").setExecutor(manhuntCmd);
        new CompassWorker(this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Durver plugin disabled.");
    }

}