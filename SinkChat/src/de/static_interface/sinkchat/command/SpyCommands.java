package de.static_interface.sinkchat.command;

import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SpyCommands
{
    public static String PREFIX = ChatColor.DARK_GRAY + "[Spy] " + ChatColor.RESET;

    public static class EnableSpyCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage("This command is only ingame available.");
                return true;
            }
            Player player = (Player) sender;

            User user = new User(player);
            PlayerConfiguration config = user.getPlayerConfiguration();

            if (config.getSpyEnabled())
            {
                player.sendMessage(PREFIX + ChatColor.RED + "Der Spy ist schon aktiviert!");
                return true;
            }

            config.setSpyEnabled(true);
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Der Spy wurde aktiviert!");
            return true;
        }
    }

    public static class DisablSpyCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage("This command is only ingame available.");
                return true;
            }
            Player player = (Player) sender;

            User user = new User(player);
            PlayerConfiguration config = user.getPlayerConfiguration();

            if (! config.getSpyEnabled())
            {
                player.sendMessage(PREFIX + ChatColor.RED + "Der Spy ist schon deaktiviert!");
                return true;
            }

            config.setSpyEnabled(false);
            sender.sendMessage(PREFIX + ChatColor.RED + "Der Spy wurde deaktiviert.");
            return true;
        }
    }
}
