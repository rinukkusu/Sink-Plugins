package de.static_interface.commandsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class CommandsverCommand implements CommandExecutor
{
    public static String prefix = ChatColor.BLUE + "[CommandsPlugin] " + ChatColor.RESET;

    Plugin plugin;

    public CommandsverCommand(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        String tmp = ( (String) plugin.getDescription().getAuthors().toArray()[0] ).replace("[", "");
        tmp = tmp.replace("]", "");
        String[] authorsTMP = tmp.split(",");
        String authors = "";
        int i = 0;
        for (String s : authorsTMP)
        {
            i++;
            if (authors.equals(""))
            {
                authors = s;
                continue;
            }
            if (i == authorsTMP.length)
            {
                authors = authors + " and " + s;
                continue;
            }
            authors = authors + ", " + s;
        }
        sender.sendMessage(prefix + plugin.getDescription().getName() + " by " + authors);
        sender.sendMessage(prefix + "Version: " + plugin.getDescription().getVersion());
        sender.sendMessage(prefix + "Copyright © 2013 Adventuria");
        return true;
    }
}
