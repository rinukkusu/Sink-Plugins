package de.static_interface.ircplugin.commands;

import de.static_interface.ircplugin.IRCPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jibble.pircbot.User;

public class IrclistCommand implements CommandExecutor
{
    public static String PREFIX = ChatColor.YELLOW + "[IRC] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        User[] users = IRCPlugin.getIRCBot().getUsers(IRCPlugin.getChannel());
        String message = "";
        for (User user : users)
        {
            if (message.equals(""))
            {
                message = user.getNick();
            }
            else
            {
                message = message + ", " + user.getNick();
            }
            sender.sendMessage(PREFIX + "Online IRC Users: " + message);
        }

        return true;
    }
}
