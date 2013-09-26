package de.static_interface.sinkcommands.commands;

import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.Util;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandsdebugCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.BLUE + "[Debug] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        String option = args[0];
        switch (option.toLowerCase())
        {
            case "getvalue":
            {
                if (args.length != 3)
                {
                    sender.sendMessage(PREFIX + "Falsche Benutzung! Korrekte Benutzung: /cdebug getvalue <player> <path.to.key>");
                    break;
                }
                String player = args[1];
                String path = player + "." + args[2];
                User user = new User(player);
                PlayerConfiguration config = user.getPlayerConfiguration();
                sender.sendMessage(PREFIX + "Output: " + config.getYamlConfiguration().getString(path));
                break;
            }

            case "setvalue":
            {
                if (args.length != 4)
                {
                    sender.sendMessage(PREFIX + "Falsche Benutzung! Korrekte Benutzung: /cdebug setvalue <player> <path.to.key> <value>");
                    break;
                }
                String player = args[1];
                String path = player + "." + args[2];
                Object value = replaceValue(args[3]);

                User user = new User(player);
                PlayerConfiguration config = user.getPlayerConfiguration();
                config.set(path, value);
                sender.sendMessage(PREFIX + "Done");
                break;
            }

            case "haspermission":
            {
                if (args.length != 3)
                {
                    sender.sendMessage(PREFIX + "Falsche Benutzung! Korrekte Benutzung: /cdebug haspermission <player> <permission>");
                    break;
                }
                String player = args[1];
                String permission = args[2];
                User user = new User(player);
                sender.sendMessage(PREFIX + "Output: " + user.hasPermission(permission));
                break;

            }
            default:
            {
                sender.sendMessage(PREFIX + "Unknown option! Valid options are: getvalue, setvalue");
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
