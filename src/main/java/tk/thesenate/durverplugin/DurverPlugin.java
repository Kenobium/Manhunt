package tk.thesenate.durverplugin;

import org.bukkit.plugin.java.JavaPlugin;

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