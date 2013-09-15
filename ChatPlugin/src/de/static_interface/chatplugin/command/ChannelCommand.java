package de.static_interface.chatplugin.command;

import de.static_interface.chatplugin.channel.Channel;
import de.static_interface.chatplugin.channel.configuration.LanguageHandler;
import de.static_interface.chatplugin.channel.registeredChannels;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/*
Permission nodes:
- chatplugin.channel.member:
-- Erlaubt /ch join/leave/fix/list

- chatplugin.channel.kick:
-- Member += /ch kick

- chatplugin.channel.admin;
-- Kick += /ch ban


 */


public class ChannelCommand extends JavaPlugin implements CommandExecutor {
    public static final String PREFIX = ChatColor.GREEN + "[ChanMan] " + ChatColor.RESET;
    public static final String permissionsError = ChatColor.translateAlternateColorCodes('&',LanguageHandler.getString("messages.permissions.general"));

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        String message;


        switch (args[0]) {
            case "join":

                if ( args.length < 2 )
                {
                    sender.sendMessage(PREFIX+LanguageHandler.getString("messages.noChannelGiven"));
                    return true;
                }

                try{
                    registeredChannels.getRegisteredChannel(args[1]).removeExceptedPlayer((Player) sender);
                }catch ( NullPointerException e )
                {   //Note: Do this more clean...
                    message = PREFIX+LanguageHandler.getString("messages.channelUnknown").replace("$CHANNEL$",args[1]);
                    sender.sendMessage(message);
                    return true;
                }

                message = PREFIX+LanguageHandler.getString("messages.playerJoins").replace("$CHANNEL$", args[1]);
                ChatColor.translateAlternateColorCodes('&', message);
                sender.sendMessage(message);


                return true;
            case "leave":

                if ( args.length < 2 )
                {
                    sender.sendMessage(PREFIX+LanguageHandler.getString("messages.noChannelGiven"));
                    return true;
                }

                try{
                registeredChannels.getRegisteredChannel(args[1]).addExceptedPlayer((Player) sender);
                }catch ( NullPointerException e )
                {   //Note: Do this more clean...
                    message = PREFIX+LanguageHandler.getString("messages.channelUnknown").replace("$CHANNEL$",args[1]);
                    sender.sendMessage(message);
                    return true;
                }

                message = PREFIX+LanguageHandler.getString("messages.playerLeaves").replace("$CHANNEL$", args[1]);
                sender.sendMessage(message);


                return true;
            case "list":
                message = PREFIX+LanguageHandler.getString("messages.list").replace("$CHANNELS$", registeredChannels.getChannelNames());
                sender.sendMessage(message);
                return true;
            case "participating":
                sender.sendMessage(PREFIX+LanguageHandler.getString("messages.part"));
                for ( Channel target : registeredChannels.getRegisteredChannels() )
                {
                    if (target.contains((Player) sender)) continue;

                    sender.sendMessage(PREFIX+target.getChannelName());

                }
                return true;
            default:
                sendHelp(sender);
                return true;
        }
    }

    private static void sendHelp(CommandSender sender) {
        sender.sendMessage(PREFIX + LanguageHandler.getString("messages.help"));
        sender.sendMessage(PREFIX + "/ch join <channel>");
        sender.sendMessage(PREFIX + "/ch leave <channel>");
        sender.sendMessage(PREFIX + "/ch list");
        sender.sendMessage(PREFIX + "/ch participating");
    }
}
