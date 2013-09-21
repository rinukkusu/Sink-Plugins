package de.static_interface.sinkcommands.commands;

import de.static_interface.sinkcommands.SinkCommands;
import de.static_interface.sinklibrary.BukkitUtil;
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
        SinkCommands.globalmuteEnabled = ! SinkCommands.globalmuteEnabled;

        if (SinkCommands.globalmuteEnabled)
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
                BukkitUtil.broadcastMessage(PREFIX + "Der globale Mute wurde von " + BukkitUtil.getSenderName(sender) + " aktiviert. Grund: " + reason + ". Alle Spieler sind jetzt stumm.");
            }
            else
            {
                BukkitUtil.broadcastMessage(PREFIX + "Der global Mute wurde von " + BukkitUtil.getSenderName(sender) + " aktiviert. Alle Spieler sind jetzt stumm.");
            }
            SinkCommands.globalmuteEnabled = true;
            return true;
        }
        else
        {
            BukkitUtil.broadcastMessage(PREFIX + "Der global Mute wurde von " + BukkitUtil.getSenderName(sender) + " deaktiviert.");
        }
        return true;
    }
}
