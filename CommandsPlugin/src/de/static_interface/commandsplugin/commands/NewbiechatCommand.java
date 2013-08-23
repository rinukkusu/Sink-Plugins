package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import de.static_interface.commandsplugin.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NewbiechatCommand implements CommandExecutor
{
    public static String prefix = ChatColor.YELLOW + "[SupportChat] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        String message = "";
        for (String s : args)
        {
            if (message.equals(""))
            {
                message = s;
                continue;
            }
            message = message + " " + s;
        }
        String name;
        if (sender instanceof Player)
        {
            name = ChatColor.WHITE + ( (Player) sender ).getDisplayName();
        }
        else
        {
            name = ChatColor.RED + "Console";
        }
        if (sender.hasPermission("commandsplugin.newbiechat.color"))
        {
            message = Util.ReplaceFormattingAndColorCodes(message);
        }
        CommandsPlugin.broadcast(prefix + name + ChatColor.WHITE + ": " + message, "commandsplugin.newbiechat");
        return true;
    }
}
