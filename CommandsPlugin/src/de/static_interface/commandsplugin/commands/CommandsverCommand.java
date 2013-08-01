package de.static_interface.commandsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandsverCommand implements CommandExecutor
{
    public static String prefix = ChatColor.BLUE + "[CommandsPlugin] " + ChatColor.WHITE;

    Plugin plugin;

    public CommandsverCommand(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        sender.sendMessage(prefix + "CommandsPlugin by Trojaner");
        sender.sendMessage(prefix + "Version: " + plugin.getDescription().getVersion());
        return true;
    }
}
