package de.static_interface.sinkcommands.commands;

import de.static_interface.sinkcommands.SinkCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.RED + "[Warn] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(PREFIX + ChatColor.RED + "Zu wenige Argumente!");
            sender.sendMessage(PREFIX + ChatColor.RED + "Benutzung: /warn [Spieler] (Grund)");
            return false;
        }
        Player target = ( Bukkit.getServer().getPlayer(args[0]) );
        if (target == null)
        {
            sender.sendMessage(PREFIX + args[0] + " ist nicht online!");
            return true;
        }
        if (target.getDisplayName().equals(SinkCommands.getSenderName(sender)))
        {
            sender.sendMessage(PREFIX + "Du kannst dich nicht selbst verwarnen!");
            return true;
        }
        if (args.length == 1)
        {
            sender.sendMessage(PREFIX + "Du musst einen Grund angeben!");
            return false;
        }

        String reason = "";

        for (int i = 1; i < args.length; i++)
        {
            if (reason.equals(""))
            {
                reason = args[i];
                continue;
            }
            reason = reason + " " + args[i];
        }

        target.sendMessage(PREFIX + ChatColor.RED + "Du wurdest von " + SinkCommands.getSenderName(sender) + ChatColor.RED + " verwarnt. Grund: " + reason);
        SinkCommands.broadcast(PREFIX + target.getDisplayName() + " wurde von " + SinkCommands.getSenderName(sender) + " verwarnt. Grund: " + reason, "sinkcommands.warn.message");
        return true;
    }
}
