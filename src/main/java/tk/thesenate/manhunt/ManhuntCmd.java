package tk.thesenate.manhunt;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getPlayer;

public class ManhuntCmd implements CommandExecutor {

    private final ManhuntMgr manhuntMgr;

    public ManhuntCmd(ManhuntMgr manhuntMgr) {
        this.manhuntMgr = manhuntMgr;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // "Manhunt" command
        if (command.getName().equalsIgnoreCase("manhunt")) {
            // Add hunters
            if (args[0].equalsIgnoreCase("addHunter")) {

                addPlayers(sender, args, manhuntMgr.hunters);
                return true;

                // Remove hunters
            } else if (args[0].equalsIgnoreCase("removeHunter")) {

                removePlayers(sender, args, manhuntMgr.hunters);
                return true;

                // Add speedrunners
            } else if (args[0].equalsIgnoreCase("addRunner")) {

                addPlayers(sender, args, manhuntMgr.runners);
                return true;

                // Remove speedrunners
            } else if (args[0].equalsIgnoreCase("removeRunner")) {

                removePlayers(sender, args, manhuntMgr.runners);
                return true;

                // Begin manhunt
            } else if (args[0].equalsIgnoreCase("start")) {
                if (!manhuntMgr.manhuntOngoing) {
                    manhuntMgr.manhuntOngoing = true;
                    for (UUID j : manhuntMgr.hunters) {
                        Player hunter = getPlayer(j);
                        if (hunter == null) {
                            manhuntMgr.hunters.remove(j);
                            continue;
                        }
                        hunter.getInventory().addItem(manhuntMgr.trackerCompass);
                        manhuntMgr.trackerMeta.setLodestone(new Location(hunter.getWorld(), 0.0, 0.0, 0.0));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "There is already a game in progress!");
                }
                return true;

                // End manhunt
            } else if (args[0].equalsIgnoreCase("stop")) {
                if (manhuntMgr.manhuntOngoing) {
                    manhuntMgr.manhuntOngoing = false;
                    for (UUID k : manhuntMgr.hunters) {
                        Player hunter = getPlayer(k);
                        if (hunter == null) {
                            manhuntMgr.hunters.remove(k);
                            continue;
                        }
                        hunter.getInventory().remove(manhuntMgr.trackerCompass);
                        hunter.setCompassTarget(hunter.getWorld().getSpawnLocation());
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "There is no game in progress!");
                }
                return true;

            } else if (args[0].equalsIgnoreCase("list")) {
                ArrayList<String> huntersList = new ArrayList<>();
                ArrayList<String> runnersList = new ArrayList<>();
                for (UUID i : manhuntMgr.hunters) {
                    huntersList.add(Objects.requireNonNull(getPlayer(i)).getName());
                }

                for (UUID j : manhuntMgr.runners) {
                    runnersList.add(Objects.requireNonNull(getPlayer(j)).getName());
                }

                sender.sendMessage(huntersList.toString());
                sender.sendMessage(runnersList.toString());
                return true;

            }
        }
        return false;
    }

    private void addPlayers(CommandSender sender, String[] args, ArrayList<UUID> team) {
        String teamName = " ";

        if (team.equals(manhuntMgr.hunters)) {
            teamName = "'hunters'";
        } else if (team.equals(manhuntMgr.runners)) {
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

        if (team.equals(manhuntMgr.hunters)) {
            teamName = "'hunters'";
        } else if (team.equals(manhuntMgr.runners)) {
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
