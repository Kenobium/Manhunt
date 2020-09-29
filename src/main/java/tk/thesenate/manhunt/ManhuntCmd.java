package tk.thesenate.manhunt;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.broadcastMessage;
import static org.bukkit.Bukkit.getPlayer;

public class ManhuntCmd implements CommandExecutor {

    private final ManhuntMgr manhuntMgr = ManhuntMgr.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // "Manhunt" command
        if (command.getName().equalsIgnoreCase("manhunt")) {
            if (args.length == 0) {
                sendUsage(sender);
            } else {
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
                            ItemStack i = new ItemStack(Material.COMPASS, 1);
                            CompassMeta im = (CompassMeta) i.getItemMeta();
                            im.setDisplayName(ChatColor.DARK_PURPLE + "Right click to set");
                            im.setLodestoneTracked(false);
                            im.setLodestone(new Location(hunter.getWorld(), 0.0, 0.0, 0.0));
                            i.setItemMeta(im);
                            hunter.getInventory().addItem(i);
                            manhuntMgr.compasses.add(i);
                            manhuntMgr.metas.add(im);
                            manhuntMgr.trackingNearestPlayer.add(true);

                        }
                        manhuntMgr.tracking = new ArrayList<>(Arrays.asList(new Player[manhuntMgr.runners.size()]));
                        broadcastMessage(ChatColor.GREEN + "Manhunt game started!");

                    } else {
                        sender.sendMessage(ChatColor.RED + "There is already a game in progress!");
                    }
                    return true;

                    // End manhunt
                } else if (args[0].equalsIgnoreCase("stop")) {
                    if (manhuntMgr.manhuntOngoing) {
                        manhuntMgr.manhuntOngoing = false;
                        for (int k = 0; k < manhuntMgr.hunters.size(); k++) {
                            Player hunter = getPlayer(manhuntMgr.hunters.get(k));
                            if (hunter == null) {
                                manhuntMgr.hunters.remove(k);
                                continue;
                            }
                            hunter.getInventory().remove(manhuntMgr.compasses.get(k));
                            manhuntMgr.compasses.clear();
                            manhuntMgr.metas.clear();
                            manhuntMgr.trackingNearestPlayer.clear();
                            manhuntMgr.tracking.clear();
                        }
                        broadcastMessage(ChatColor.GREEN + "Manhunt game ended.");
                    } else {
                        sender.sendMessage(ChatColor.RED + "There is no game in progress!");
                    }
                    return true;

                } else if (args[0].equalsIgnoreCase("list")) {
                    List<String> huntersList = new ArrayList<>();
                    List<String> runnersList = new ArrayList<>();
                    for (UUID i : manhuntMgr.hunters) {
                        huntersList.add(getPlayer(i).getName());
                    }

                    for (UUID j : manhuntMgr.runners) {
                        runnersList.add(getPlayer(j).getName());
                    }

                    sender.sendMessage(ChatColor.RED + "Hunters: " + String.join(", ", huntersList));
                    sender.sendMessage(ChatColor.GREEN + "Speedrunners: " + String.join(", ", runnersList));
                    return true;

                } else {
                    sendUsage(sender);
                }
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

    private void sendUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "Usage: /manhunt <start/stop/(add/remove)(hunter/runner)> <players>");
    }
}
