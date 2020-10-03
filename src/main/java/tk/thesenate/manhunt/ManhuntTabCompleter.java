package tk.thesenate.manhunt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManhuntTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        List<String> tabs = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("manhunt")) {
            List<String> tabs0 = Arrays.asList("addhunter", "removehunter", "addrunner", "removerunner");
            List<String> tabs0_2 = Arrays.asList("list", "start", "stop");

            if (args.length == 1) {
                tabs.addAll(tabs0);
                tabs.addAll(tabs0_2);
                return tabs;
            } else if (args.length >= 2) {
                if (tabs0.contains(args[0].toLowerCase())) {
                    return null;
                } else if (tabs0_2.contains(args[0].toLowerCase())) {
                    return new ArrayList<>();
                }
            }
        }

        return tabs;
    }
}
