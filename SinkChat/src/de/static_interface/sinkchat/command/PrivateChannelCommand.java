package de.static_interface.sinkchat.command;

import de.static_interface.sinkchat.channel.IPrivateChannel;
import de.static_interface.sinkchat.channel.PrivateChannelHandler;
import de.static_interface.sinkchat.channel.channels.PrivateChannel;
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class PrivateChannelCommand implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args)
    {
        IPrivateChannel ch = PrivateChannelHandler.getChannel(args[1]);
        switch ( args[0].toLowerCase() )
        {
            case "invite":
                if ( ch == null )
                {
                    if ( args.length < 4 )
                    {
                        sendHelp(sender, cmd);
                        return true;
                    }
                    Player target = Bukkit.getPlayer(args[2]);
                    if ( target == null )
                    {
                        sender.sendMessage(String.format(_("SinkChat.Channels.Private.HasInvitedToChat.ErrorNotOnline"), args[2]));
                        return true;
                    }
                    PrivateChannelHandler.registerChannel(new PrivateChannel(args[1], (Player) (sender), Bukkit.getPlayer(args[2]), args[3]));
                    ch = (PrivateChannelHandler.getChannel(args[1]));
                    sender.sendMessage("Invited " + SinkLibrary.getUser(target).getDisplayName() + " to chat. Use " + args[1] + " <Text> to chat.");
                }
                for ( String s : args )
                {
                    if ( s.equals("invite") || s.equals(args[1]) || (ch.contains(Bukkit.getPlayer(s))) ) continue;
                    if ( Bukkit.getPlayer(s) == null )
                    {
                        sender.sendMessage(String.format(_("SinkChat.Channels.Private.HasInvitedToChat.ErrorNotOnline"), s));
                    }
                    ch.addPlayer((Player) (sender), Bukkit.getPlayer(s));
                }

                return true;

            case "leave":
                if ( args.length < 2 )
                {
                    sendHelp(sender, cmd);
                    return true;
                }
                if ( !(ch.contains((Player) (sender))) || (ch == null) )
                {
                    sender.sendMessage(String.format(_("SinkChat.Commands.Channel.ChannelUnknown"), args[1]));
                    return true;
                }
                ch.kickPlayer((Player) sender, (Player) sender, "");
                return true;

            case "kick":
                if ( args.length < 3 )
                {
                    sendHelp(sender, cmd);
                    return true;
                }
                if ( !(ch.contains((Player) sender)) || (ch == null) )
                {
                    sender.sendMessage(String.format(_("SinkChat.Commands.Channel.ChannelUnknown"), args[1]));
                    return true;
                }
                if ( !(ch.getStarter().equals(sender)) )
                {
                    sender.sendMessage(_("Permissions.General"));
                    return true;
                }
                if ( Bukkit.getPlayer(args[2]) == null )
                {
                    sender.sendMessage(String.format(_("SinkChat.Channels.Private.HasInvitedToChat.ErrorNotOnline"), args[2]));
                }
                if ( !(ch.contains(Bukkit.getPlayer(args[2]))) )
                {
                    sender.sendMessage(String.format(_("SinkChat.Channels.Private.PlayerKicked"), args[2]));
                    return true;
                }

                String reason = "Kicked!";//= args[3];

                for ( int x = 4; x < args.length; x++ )
                {
                    if ( reason.equals("Kicked!") )
                    {
                        reason = args[x];
                        continue;
                    }
                    reason = reason + " " + args[x];
                }

                ch.kickPlayer(Bukkit.getPlayer(args[2]), (Player) sender, reason);
                return true;
        }
        return true;
    }

    private void sendHelp(CommandSender sender, Command cmd)
    {
        sender.sendMessage(ChatColor.RED + "Wrong usage!");
        sender.sendMessage("/" + cmd.getLabel() + " <channel identifier> <player> [players...]");
        sender.sendMessage("/" + cmd.getLabel() + " leave <channel identifier>");
        sender.sendMessage("/" + cmd.getLabel() + " kick <channel identifier> <player> [reason]");
    }
}
