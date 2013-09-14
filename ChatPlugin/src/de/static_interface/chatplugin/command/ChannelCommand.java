package de.static_interface.chatplugin.command;

import de.static_interface.chatplugin.channel.Channel;
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
    public static final String permissionsError = ChatColor.RED+"Keine ausreichenden Permissions. Wende dich an einen Moderator wenn du glaubst, dass dies ein Fehler ist.";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0]) {
            case "join":

                if ( args.length < 2 )
                {
                    sender.sendMessage(PREFIX+"Du musst einen Channelnamen angeben !");
                    return true;
                }


                if (registeredChannels.getRegisteredChannel(args[1]) == null) {
                    sender.sendMessage(PREFIX + " Channel existiert nicht !");
                    return true;
                }


                if ((args.length == 3) && !(getServer().getPlayer(args[2]) == null)) {
                    registeredChannels.getRegisteredChannel(args[1]).removeExceptedPlayer(args[2]);
                    return true;
                }

                registeredChannels.getRegisteredChannel(args[1]).removeExceptedPlayer((Player) sender);

                sender.sendMessage(PREFIX+"Du bist dem Channel "+args[1]+" beigetreten !");

                return true;
            case "leave":

                if ( args.length < 2 )
                {
                    sender.sendMessage(PREFIX+"Du musst einen Channelnamen angeben !");
                    return true;
                }


                if (registeredChannels.getRegisteredChannel(args[1]) == null) sender.sendMessage(PREFIX + " Channel existiert nicht !");
                registeredChannels.getRegisteredChannel(args[1]).addExceptedPlayer((Player) sender);

                sender.sendMessage(PREFIX+"Du hast den Channel "+args[1]+" verlassen.");

                return true;
            case "list":
                sender.sendMessage(PREFIX + " Bekannte Kanäle:");
                sender.sendMessage(PREFIX+registeredChannels.getChannelNames());
                return true;
            case "participating":
                sender.sendMessage(PREFIX+"Du bist in folgenden Channels:");
                for ( Channel target : registeredChannels.getRegisteredChannels() )
                {
                    if ( !(target.contains((Player) sender)) )
                    {
                        sender.sendMessage(PREFIX+target.getChannelName());
                    }
                }
                return true;
            default:
                sendHelp(sender);
                return true;
        }
    }

    private static void sendHelp(CommandSender sender) {
        sender.sendMessage(PREFIX + "Das Channelsystem hat folgende Commands:");
        sender.sendMessage(PREFIX + "/ch join <channel>");
        sender.sendMessage(PREFIX + "/ch leave <channel>");
        sender.sendMessage(PREFIX + "/ch list");
        sender.sendMessage(PREFIX + "/ch participating");
    }
}
