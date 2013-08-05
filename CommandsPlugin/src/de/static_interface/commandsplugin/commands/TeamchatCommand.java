package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamchatCommand implements CommandExecutor
{
    public static String prefix = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "T" + ChatColor.RED + "eam" + ChatColor.DARK_RED + "C" + ChatColor.RED + "hat" + ChatColor.GRAY + "] " + ChatColor.WHITE;

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
        if (sender.hasPermission("commandsplugin.teamchat.color"))
        {
            message = ReplaceFormattingAndColorCodes(message);
        }
        CommandsPlugin.broadcast(prefix + name + ChatColor.WHITE + ": " + message, "commandsplugin.teamchat");
        return true;
    }

    private static String ReplaceFormattingAndColorCodes(String input)
    {
        input = input.replace("&0", "§0");
        input = input.replace("&1", "§1");
        input = input.replace("&2", "§2");
        input = input.replace("&3", "§3");
        input = input.replace("&4", "§4");
        input = input.replace("&5", "§5");
        input = input.replace("&6", "§6");
        input = input.replace("&7", "§7");
        input = input.replace("&8", "§8");
        input = input.replace("&9", "§9");
        input = input.replace("&a", "§a");
        input = input.replace("&b", "§b");
        input = input.replace("&c", "§c");
        input = input.replace("&d", "§d");
        input = input.replace("&e", "§e");
        input = input.replace("&f", "§f");
        input = input.replace("&k", "§k");
        input = input.replace("&l", "§l");
        input = input.replace("&m", "§m");
        input = input.replace("&n", "§n");
        input = input.replace("&o", "§o");
        input = input.replace("&r", "§r");
        return input;
    }
}
