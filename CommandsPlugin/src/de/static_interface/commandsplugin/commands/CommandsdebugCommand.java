package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.PlayerConfiguration;
import de.static_interface.commandsplugin.Util;
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
                    break;
                }
                String player = args[1];
                String path = player + "." + args[2];
                PlayerConfiguration config = new PlayerConfiguration(player);
                sender.sendMessage(prefix + "Output: " + config.getPlayerConfiguration().getString(path));
                break;
            }
            case "setvalue":
            {
                if (args.length != 4)
                {
                    sender.sendMessage(prefix + "Falsche Benutzung! Korrekte Benutzung: /debug setvalue <player> <path.to.key> <value>");
                    break;
                }
                String player = args[1];
                String path = player + "." + args[2];
                Object value = replaceValue(args[3]);

                PlayerConfiguration config = new PlayerConfiguration(player);
                config.set(path, value);
                sender.sendMessage(prefix + "Done");
                break;
            }
            default:
            {
                sender.sendMessage(prefix + "Unknown option! Valid options are: getvalue, setvalue");
            }
        }
        return true;
    }

    private Object replaceValue(String value)
    {
        if (value.equals("true"))
        {
            return true;
        }
        if (value.equals("false"))
        {
            return false;
        }
        if (Util.isNumber(value))
        {
            return Integer.parseInt(value);
        }
        return value;
    }
}
