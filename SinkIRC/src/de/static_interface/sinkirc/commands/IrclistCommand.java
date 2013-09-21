package de.static_interface.sinkirc.commands;

import de.static_interface.sinkirc.SinkIRC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jibble.pircbot.User;

public class IrclistCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.YELLOW + "[IRC] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        User[] users = SinkIRC.getIRCBot().getUsers(SinkIRC.getChannel());
        String message = "";
        for (User user : users)
        {
            String name = user.getNick();
            if (name.equals(SinkIRC.getIRCBot().getNick()))
            {
                continue;
            }
            if (user.isOp())
            {
                name = ChatColor.RED + name + ChatColor.RESET;
            }
            if (message.equals(""))
            {
                message = name;
            }
            else
            {
                message = message + ", " + name;
            }
        }
        if (users.length <= 1)
        {
            message = "Zur Zeit sind keine Benutzer im IRC.";
        }
        sender.sendMessage(PREFIX + "Online IRC Benutzer: " + message);
        return true;
    }
}
