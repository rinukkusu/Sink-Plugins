package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor
{
    public static String prefix = ChatColor.RED + "[Warn] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(prefix + ChatColor.RED + "Zu wenige Argumente!");
            sender.sendMessage(prefix + ChatColor.RED + "Benutzung: /warn [Spieler] (Grund)");
            return false;
        }
        Player target = ( Bukkit.getServer().getPlayer(args[0]) );
        if (target == null)
        {
            sender.sendMessage(prefix + args[0] + " ist nicht online!");
            return true;
        }
        if (target.getDisplayName().equals(CommandsPlugin.getSenderName(sender)))
        {
            sender.sendMessage(prefix + "Du kannst dich nicht selbst verwarnen!");
            return true;
        }
        if (args.length == 1)
        {
            sender.sendMessage(prefix + "Du musst einen Grund angeben!");
            return false;
        }
        String reason = "";
        int i = - 1;
        for (String s : args)
        {
            i++;
            if (i == 0)
            {
                continue;
            }
            if (reason.equals(""))
            {
                reason = s;
                continue;
            }
            reason = reason + " " + s;
        }
        target.sendMessage(prefix + ChatColor.RED + "Du wurdest von " + CommandsPlugin.getSenderName(sender) + " verwarnt. Grund: " + reason);
        CommandsPlugin.broadcast(prefix + target.getDisplayName() + " wurde von " + CommandsPlugin.getSenderName(sender) + " verwarnt. Grund: " + reason, "commandsplugin.warn.message");
        return true;
    }
}
