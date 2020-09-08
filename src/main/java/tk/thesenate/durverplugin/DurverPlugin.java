package tk.thesenate.durverplugin;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DurverPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info("Durver plugin enabled.");
        getServer().getPluginManager().registerEvents(new ManhuntListener(), this);
        Objects.requireNonNull(getCommand("manhunt")).setExecutor(new ManhuntCmd());
        new CompassWorker(this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Durver plugin disabled.");
    }

}