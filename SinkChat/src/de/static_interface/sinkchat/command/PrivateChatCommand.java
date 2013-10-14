package de.static_interface.sinkchat.command;

import de.static_interface.sinkchat.channel.PrivateChannelHandler;
import de.static_interface.sinkchat.channel.channels.PrivateChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class PrivateChatCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        switch ( args.length )
        {
            case 0:
                sender.sendMessage("/privatechat invite <channel identifier> <player> [players...]");
                sender.sendMessage("/privatechat kick <channel identifier> <player> [reason]");
                sender.sendMessage("/privatechat leave <channel identifier>");
                return true;
            case 2:
                if ( !(args[0].equals("leave")) )
                {
                    sender.sendMessage("/privatechat invite <channel identifier> <player> [players...]");
                    sender.sendMessage("/privatechat kick <channel identifier> <player> [reason]");
                    sender.sendMessage("/privatechat leave <channel identifier>");
                    return true;
                }

                PrivateChannel channel = PrivateChannelHandler.getChannel(args[1]);
                if ( channel == null ) sender.sendMessage((_("SinkChat.Commands.Channel.ChannelUnknown").replace("%s",args[1])));


        }

        return false;
    }
}
