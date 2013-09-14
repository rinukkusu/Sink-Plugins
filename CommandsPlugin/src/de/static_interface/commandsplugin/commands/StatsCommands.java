package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import de.static_interface.commandsplugin.PlayerConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class StatsCommands
{
    public static String PREFIX = ChatColor.DARK_GREEN + "[Statistiken] ";

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
                player.sendMessage(PREFIX + "Die Statistiken sind schon aktiviert!");
                return true;
            }

            config.setStatsEnabled(true);
            sender.sendMessage(PREFIX + "Du hast die Statistiken aktiviert!");
            sender.sendMessage(PREFIX + "Falls sie immer noch nicht aktiviert sind, muss du einen Relog machen.");
            CommandsPlugin.refreshScoreboard(player);
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
                player.sendMessage(PREFIX + "Die Statistiken sind schon deaktiviert!");
                return true;
            }

            config.setStatsEnabled(false);
            sender.sendMessage(PREFIX + "Du hast die Statistiken deaktiviert!");
            sender.sendMessage(PREFIX + "Falls sie immer noch deaktiviert sind, muss du einen Relog machen.");
            CommandsPlugin.refreshScoreboard(player);
            return true;
        }
    }
}
