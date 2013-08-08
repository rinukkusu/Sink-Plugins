package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LagCommand implements CommandExecutor
{
    public static String prefix = ChatColor.DARK_PURPLE + "[Lag] " + ChatColor.WHITE;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        double realTPS = CommandsPlugin.getCommandsTimer().getAverageTPS();
        double shownTPS = Math.round(realTPS);
        if (realTPS >= 18.5)
        {
            commandSender.sendMessage(prefix + ChatColor.GREEN + "Der Server laggt gerade nicht!");
        }
        else if (realTPS >= 16)
        {
            commandSender.sendMessage(prefix + ChatColor.GOLD + "Der Server könnte gerade etwas laggen!");
        }
        else
        {
            commandSender.sendMessage(prefix + ChatColor.RED + "Der Server laggt gerade!");
        }
        commandSender.sendMessage(prefix + "(TPS: " + shownTPS + ")");
        return true;
    }
}

