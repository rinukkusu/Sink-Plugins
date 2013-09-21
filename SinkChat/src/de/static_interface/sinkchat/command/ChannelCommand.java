package de.static_interface.sinkchat.command;

import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.IChannel;
import de.static_interface.sinkchat.channel.configuration.LanguageHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/*
Permission nodes:
- sinkchat.channel.member:
-- Erlaubt /ch join/leave/fix/list

- sinkchat.channel.kick:
-- Member += /ch kick

- sinkchat.channel.admin;
-- Kick += /ch ban


 */


public class ChannelCommand extends JavaPlugin implements CommandExecutor
{
    public static final String PREFIX = ChatColor.GREEN + "[Channel] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if (args.length == 0)
        {
            sendHelp(sender);
            return true;
        }

        String message;


        switch (args[0])
        {
            case "join":

                if (args.length < 2)
                {
                    sender.sendMessage(PREFIX + LanguageHandler.getString("messages.noChannelGiven"));
                    return true;
                }

                try
                {
                    ChannelHandler.getRegisteredChannel(args[1]).removeExceptedPlayer((Player) sender);
                }
                catch (NullPointerException e)
                {
                    message = PREFIX + LanguageHandler.getString("messages.channelUnknown").replace("$CHANNEL$", args[1]);
                    sender.sendMessage(message);
                    return true;
                }

                message = PREFIX + LanguageHandler.getString("messages.playerJoins").replace("$CHANNEL$", args[1]);
                ChatColor.translateAlternateColorCodes('&', message);
                sender.sendMessage(message);


                return true;
            case "leave":

                if (args.length < 2)
                {
                    sender.sendMessage(PREFIX + LanguageHandler.getString("messages.noChannelGiven"));
                    return true;
                }

                try
                {
                    ChannelHandler.getRegisteredChannel(args[1]).addExceptedPlayer((Player) sender);
                }
                catch (NullPointerException e)
                {   //Note: Do this more clean...
                    message = PREFIX + LanguageHandler.getString("messages.channelUnknown").replace("$CHANNEL$", args[1]);
                    sender.sendMessage(message);
                    return true;
                }

                message = PREFIX + LanguageHandler.getString("messages.playerLeaves").replace("$CHANNEL$", args[1]);
                sender.sendMessage(message);


                return true;
            case "list":
                message = PREFIX + LanguageHandler.getString("messages.list").replace("$CHANNELS$", ChannelHandler.getChannelNames());
                sender.sendMessage(message);
                return true;

            case "participating":
                sender.sendMessage(PREFIX + LanguageHandler.getString("messages.part"));
                for (IChannel target : ChannelHandler.getRegisteredChannels())
                {
                    if (target.contains((Player) sender))
                    {
                        continue;
                    }

                    sender.sendMessage(PREFIX + target.getChannelName());

                }
                return true;

            default:
                sendHelp(sender);
                return true;
        }
    }

    private static void sendHelp(CommandSender sender)
    {
        sender.sendMessage(PREFIX + LanguageHandler.getString("messages.help"));
        sender.sendMessage(PREFIX + "/ch join <channel>");
        sender.sendMessage(PREFIX + "/ch leave <channel>");
        sender.sendMessage(PREFIX + "/ch list");
        sender.sendMessage(PREFIX + "/ch participating");
    }
}
