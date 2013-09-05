package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GlobalmuteCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.DARK_RED + "[GlobalMute] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        CommandsPlugin.globalmuteEnabled = ! CommandsPlugin.globalmuteEnabled;

        if (CommandsPlugin.globalmuteEnabled)
        {
            if (args.length > 0)
            {
                String reason = "";
                for (String arg : args)
                {
                    if (reason.equals(""))
                    {
                        reason = arg;
                        continue;
                    }
                    reason = reason + " " + arg;
                }
                CommandsPlugin.broadcastMessage(PREFIX + "Der globale Mute wurde von " + CommandsPlugin.getSenderName(sender) + " aktiviert. Grund: " + reason + ". Alle Spieler sind jetzt stumm.");
            }
            else
            {
                CommandsPlugin.broadcastMessage(PREFIX + "Der global Mute wurde von " + CommandsPlugin.getSenderName(sender) + " aktiviert. Alle Spieler sind jetzt stumm.");
            }
            CommandsPlugin.globalmuteEnabled = true;
            return true;
        }
        else
        {
            CommandsPlugin.broadcastMessage(PREFIX + "Der global Mute wurde von " + CommandsPlugin.getSenderName(sender) + " deaktiviert.");
        }
        return true;
    }
}
