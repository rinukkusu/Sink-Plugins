package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.PlayerConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandsdebugCommand implements CommandExecutor
{
    public static String prefix = ChatColor.BLUE + "[Debug] " + ChatColor.RESET;
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        String option = args[0];
        switch(option)
        {
            case "getvalue":
            {
                if (args.length != 3)
                {
                    sender.sendMessage(prefix + "Falsche Benutzung! Korrekte Benutzung: /debug getvalue <player> <path.to.key>");
                    return true;
                }
                String player = args[1];
                String path = args[2];
                PlayerConfiguration config = new PlayerConfiguration(player);
                sender.sendMessage(config.getPlayerConfiguration().getString(path));
            }
        }
        return true;
    }
}
