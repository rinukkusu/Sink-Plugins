package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LagCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.DARK_PURPLE + "[Lag] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        double realTPS = CommandsPlugin.getCommandsTimer().getAverageTPS();
        double shownTPS = Math.round(realTPS);
        if (realTPS >= 18.5)
        {
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Der Server läuft ohne Probleme!");
        }
        else if (realTPS >= 17)
        {
            sender.sendMessage(PREFIX + ChatColor.YELLOW + "Der Server könnte gerade etwas laggen!");
        }
        else
        {
            sender.sendMessage(PREFIX + ChatColor.RED + "Der Server laggt gerade!");
        }
        sender.sendMessage(PREFIX + "(TPS: " + shownTPS + ")");
        return true;
    }
}

