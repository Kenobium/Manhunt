package tk.thesenate.durverplugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getPlayer;

public class ManhuntCmd implements CommandExecutor {

    protected static final HashSet<UUID> hunters = new HashSet<>();
    // private final HashSet<UUID> runners = new HashSet<>();
    protected static volatile boolean manhuntOngoing = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // "Manhunt" command
        if (command.getName().equalsIgnoreCase("manhunt")) {
            ItemStack trackerCompass = new ItemStack(Material.COMPASS, 1);

            // Manage hunters
            if (args[0].equalsIgnoreCase("hunter")) {

                // Add hunters
                if (args[1].equalsIgnoreCase("add")) {
                    for (String i : Arrays.copyOfRange(args, 2, args.length)) {
                        Player player = getPlayer(i);
                        if (player == null) {
                            sender.sendMessage(ChatColor.RED + "One or more players were not found.");
                            return true;
                        } else if (hunters.contains(player.getUniqueId())) { // HashSet.add() will return false if contains element. maybe refactor l8r
                            sender.sendMessage(ChatColor.RED + "Player(s) is already on team 'hunters'.");
                            return true;
                        } else {
                            hunters.add(player.getUniqueId());
                            sender.sendMessage(hunters.toString());
                            sender.sendMessage(ChatColor.GREEN + "Player(s) added to team 'hunters'.");
                            return true;
                        }
                    }

                    // Remove hunters
                } else if (args[1].equalsIgnoreCase("remove")) {
                    for (String i : Arrays.copyOfRange(args, 2, args.length)) {
                        Player player = getPlayer(i);
                        if (player == null) {
                            sender.sendMessage(ChatColor.RED + "One or more players were not found.");
                            return true;
                        } else if (!hunters.contains(player.getUniqueId())) {
                            sender.sendMessage(ChatColor.RED + "Player(s) not found on team 'hunters'.");
                            return true;
                        } else {
                            hunters.remove(player.getUniqueId());
                            sender.sendMessage(ChatColor.GREEN + "Player(s) removed from team 'hunters'.");
                            return true;
                        }
                    }
                }

                // Begin manhunt
            } else if (args[0].equalsIgnoreCase("start")) {
                manhuntOngoing = true;
                getLogger().info(String.valueOf(manhuntOngoing));
                for (UUID j : hunters) {
                    Player hunter = getPlayer(j);
                    if (hunter == null) {
                        hunters.remove(j);
                        continue;
                    }
                    hunter.getInventory().setItem(0, trackerCompass);
                }
                return true;

                // End manhunt
            } else if (args[0].equalsIgnoreCase("stop")) {
                manhuntOngoing = false;
                for (UUID k : hunters) {
                    Player hunter = getPlayer(k);
                    if (hunter == null) {
                        hunters.remove(k);
                        continue;
                    }
                    hunter.getInventory().removeItem(trackerCompass);
                }
                return true;

            } else if (args[0].equalsIgnoreCase("test")) {
                //sender.sendMessage(getNearestPlayer((Player) sender).toString());
                getLogger().info(String.valueOf(manhuntOngoing));
                return true;
            }
        }
        return false;
    }

    protected Player getNearestPlayer(Player player) {
        Player nearest = null;
        double lastDistance = Double.MAX_VALUE;
        for(Player p : player.getWorld().getPlayers()) {
            if(player == p)
                continue;

            double distance = player.getLocation().distance(p.getLocation());
            if(distance < lastDistance) {
                lastDistance = distance;
                nearest = p;
            }
        }
        return nearest;
    }

//    protected boolean getManhuntStatus() {
//        return manhuntOngoing;
//    }
//
//    protected HashSet<UUID> getHunters() {
//        return hunters;
//    }
//
//    private void pointCompass(HashSet<UUID> h) {
//
//    }

}
