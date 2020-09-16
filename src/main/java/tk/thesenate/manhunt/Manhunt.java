package tk.thesenate.manhunt;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Manhunt extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info("Durver plugin enabled.");
        getServer().getPluginManager().registerEvents(new ManhuntListener(), this);
        Objects.requireNonNull(getCommand("manhunt")).setExecutor(new ManhuntCmd());
        new tk.thesenate.manhunt.CompassWorker(this);

    }

    @Override
    public void onDisable() {
        getLogger().info("Durver plugin disabled.");
    }

}