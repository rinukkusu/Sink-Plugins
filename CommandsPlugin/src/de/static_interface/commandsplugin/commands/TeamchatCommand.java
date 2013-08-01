package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * TeamChatCommand Class
 * <p/>
 * Author: Trojaner
 * Date: 29.07.13
 * Description: Class for teamchat command
 * Copyright © Trojaner 2013
 */
public class TeamchatCommand implements CommandExecutor
{
    public static String prefix = ChatColor.BLUE + "[TeamChat] " + ChatColor.WHITE;

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
        CommandsPlugin.broadcast(prefix + name + ChatColor.WHITE + ": " + message, "commandsplugin.teamchat");
        return true;
    }
}
