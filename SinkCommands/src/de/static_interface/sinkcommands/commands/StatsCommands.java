package de.static_interface.sinkcommands.commands;

import de.static_interface.sinkcommands.SinkCommands;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class StatsCommands
{
    public static String PREFIX = ChatColor.DARK_GREEN + "[Statistiken] " + ChatColor.RESET;

    public static class EnableStatsCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage("This command is only ingame available.");
                return true;
            }
            Player player = (Player) sender;

            PlayerConfiguration config = new PlayerConfiguration(player.getName());

            if (config.getStatsEnabled())
            {
                player.sendMessage(PREFIX + ChatColor.GREEN + "Die Statistiken sind schon aktiviert!");
                return true;
            }

            config.setStatsEnabled(true);
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Du hast die Statistiken aktiviert!");
            SinkCommands.refreshScoreboard(player);
            return true;
        }
    }

    public static class DisableStatsCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage("This command is only ingame available.");
                return true;
            }
            Player player = (Player) sender;

            PlayerConfiguration config = new PlayerConfiguration(player.getName());

            if (! config.getStatsEnabled())
            {
                player.sendMessage(PREFIX + ChatColor.RED + "Die Statistiken sind schon deaktiviert!");
                return true;
            }

            config.setStatsEnabled(false);
            sender.sendMessage(PREFIX + ChatColor.RED + "Du hast die Statistiken deaktiviert!");
            SinkCommands.refreshScoreboard(player);
            return true;
        }
    }
}
