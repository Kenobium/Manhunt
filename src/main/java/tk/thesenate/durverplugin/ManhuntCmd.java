package tk.thesenate.durverplugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class ManhuntCmd implements CommandExecutor {

    final ArrayList<UUID> hunters = new ArrayList<>();
    final ArrayList<UUID> runners = new ArrayList<>();
    final ItemStack trackerCompass = new ItemStack(Material.COMPASS, 1);
    volatile boolean manhuntOngoing = false;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // "Manhunt" command
        if (command.getName().equalsIgnoreCase("manhunt")) {
            // Add hunters
            if (args[0].equalsIgnoreCase("addHunter")) {

                addPlayers(sender, args, hunters);
                return true;

                // Remove hunters
            } else if (args[0].equalsIgnoreCase("removeHunter")) {

                removePlayers(sender, args, hunters);
                return true;

                // Add speedrunners
            } else if (args[0].equalsIgnoreCase("addRunner")) {

                addPlayers(sender, args, runners);
                return true;

                // Remove speedrunners
            } else if (args[0].equalsIgnoreCase("removeRunner")) {

                removePlayers(sender, args, runners);
                return true;

                // Begin manhunt
            } else if (args[0].equalsIgnoreCase("start")) {
                manhuntOngoing = true;
                ItemMeta compassName = trackerCompass.getItemMeta();
                compassName.setDisplayName(ChatColor.DARK_PURPLE + "Tracker Compass");
                trackerCompass.setItemMeta(compassName);
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
                    hunter.getInventory().setItem(0, new ItemStack(Material.AIR));
                }
                return true;

            } else if (args[0].equalsIgnoreCase("list")) {
                ArrayList<String> huntersList = new ArrayList<>();
                ArrayList<String> runnersList = new ArrayList<>();
                for (UUID i: hunters) {
                    huntersList.add(getPlayer(i).getName());
                }

                for (UUID j: runners) {
                    runnersList.add(getPlayer(j).getName());
                }

                sender.sendMessage(huntersList.toString());
                sender.sendMessage(runnersList.toString());
                return true;

            }
        }
        return false;
    }

    Player getNearestPlayer(Player player) {
        Player nearest = null;
        double lastDistance = Double.MAX_VALUE;
        for (Player p : player.getWorld().getPlayers()) {
            if (player == p)
                continue;

            double distance = player.getLocation().distance(p.getLocation());
            if (distance < lastDistance) {
                lastDistance = distance;
                nearest = p;
            }
        }
        return nearest;
    }

    private void addPlayers(CommandSender sender, String[] args, ArrayList<UUID> team) {
        String teamName = " ";

        if (team.equals(hunters)) {
            teamName = "'hunters'";
        } else if (team.equals(runners)) {
            teamName = "'speedrunners'";
        }

        for (String i : Arrays.copyOfRange(args, 1, args.length)) {
            Player player = getPlayer(i);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
            } else if (team.contains(player.getUniqueId())) { // HashSet.add() will return false if contains element. maybe refactor l8r
                sender.sendMessage(ChatColor.RED + "Player is already on team " + teamName + ".");
            } else {
                team.add(player.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Player added to team " + teamName + ".");
            }
        }
    }

    private void removePlayers(CommandSender sender, String[] args, ArrayList<UUID> team) {
        String teamName = " ";

        if (team.equals(hunters)) {
            teamName = "'hunters'";
        } else if (team.equals(runners)) {
            teamName = "'speedrunners'";
        }

        for (String i : Arrays.copyOfRange(args, 1, args.length)) {
            Player player = getPlayer(i);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player not found.");
            } else if (!team.contains(player.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "Player not found on team " + teamName + ".");
            } else {
                team.remove(player.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Player removed from team " + teamName + ".");
            }
        }
    }
}
