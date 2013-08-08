package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * GlobalMuteCommand Class
 * <p/>
 * Author: Trojaner
 * Date: 28.07.13
 * Description: Class for global mute command
 * Copyright © Trojaner 2013
 */
public class GlobalmuteCommand implements CommandExecutor
{
    public static String prefix = ChatColor.DARK_RED + "[GlobalMute] " + ChatColor.WHITE;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        CommandsPlugin.globalmuteEnabled = ! CommandsPlugin.globalmuteEnabled;

        if (CommandsPlugin.globalmuteEnabled)
        {
            if (strings.length > 0)
            {
                String reason = "";
                for (String arg : strings)
                {
                    if (reason.equals(""))
                    {
                        reason = arg;
                        continue;
                    }
                    reason = reason + " " + arg;
                }
                if (commandSender instanceof Player)
                {
                    Bukkit.broadcastMessage(prefix + "Der globale Mute wurde von " + ( (Player) commandSender ).getDisplayName() + " aktiviert. Grund: " + reason + ". Alle Spieler sind jetzt stumm.");
                }
                else
                {
                    Bukkit.broadcastMessage(prefix + "Der globale Mute von der Console aus aktiviert. Grund: " + reason + ". Alle Spieler sind jetzt stumm.");
                }
            }
            else
            {
                if (commandSender instanceof Player)
                {
                    Bukkit.broadcastMessage(prefix + "Der global Mute wurde von " + ( (Player) commandSender ).getDisplayName() + " aktiviert. Alle Spieler sind jetzt stumm.");
                }
                else
                {
                    Bukkit.broadcastMessage(prefix + "Der global Mute wurde von der Console aus aktiviert. Alle Spieler sind jetzt stumm.");
                }
            }
            CommandsPlugin.globalmuteEnabled = true;
            return true;
        }
        else
        {
            if (commandSender instanceof Player)
            {
                Bukkit.broadcastMessage(prefix + "Der global Mute wurde von " + ( (Player) commandSender ).getDisplayName() + " deaktiviert.");
            }
            else
            {
                Bukkit.broadcastMessage(prefix + "Der global Mute wude von der Console aus deaktiviert.");
            }
        }
        return true;
    }
}
