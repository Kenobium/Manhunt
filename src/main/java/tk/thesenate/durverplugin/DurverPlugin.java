package tk.thesenate.durverplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class DurverPlugin extends JavaPlugin {

    //private final CompassWorker compassWorker = new CompassWorker();
    //private final Thread pointCompass = new Thread(compassWorker);
    ManhuntCmd manhuntCmd = new ManhuntCmd();

    @Override
    public void onEnable() {
        this.getLogger().info("Durver plugin enabled.");
        this.getCommand("manhunt").setExecutor(new ManhuntCmd());
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            if (ManhuntCmd.manhuntOngoing) {
                //getLogger().info("despacito");
                for (UUID l : ManhuntCmd.hunters) {
                    Player hunter = getPlayer(l);
                    if (hunter == null) {
                        ManhuntCmd.hunters.remove(l);
                        continue;
                    }
                    //getLogger().info("trying to set compass");
                    if (!ManhuntCmd.hunters.contains(manhuntCmd.getNearestPlayer(hunter).getUniqueId())) {
                        //getLogger().info("setting compass");
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

}
