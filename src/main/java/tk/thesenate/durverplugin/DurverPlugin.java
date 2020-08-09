package tk.thesenate.durverplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class DurverPlugin extends JavaPlugin {

    private final CompassWorker compassWorker = new CompassWorker();
    private final Thread pointCompass = new Thread(compassWorker);

    @Override
    public void onEnable() {
        this.getLogger().info("Durver plugin enabled.");
        this.getCommand("manhunt").setExecutor(new ManhuntCmd());
        pointCompass.start();
    }

    @Override
    public void onDisable() {
        getLogger().info("Durver plugin disabled.");
        // compassWorker.stopRunning();
    }



}
