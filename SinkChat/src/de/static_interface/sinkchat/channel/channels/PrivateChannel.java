package de.static_interface.sinkchat.channel.channels;

import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.IPrivateChannel;
import de.static_interface.sinkchat.channel.PrivateChannelHandler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Vector;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class PrivateChannel extends IPrivateChannel
{
    String channelIdent;
    Vector<Player> participants = new Vector<>();
    Player starter;

    public PrivateChannel(String channelIdentifier, Player starter, Player target, String name)
    {
        if ( PrivateChannelHandler.channelIdentIsTaken(channelIdentifier) )
        {
            channelIdent = channelIdentifier + "_";
            while ( PrivateChannelHandler.channelIdentIsTaken(channelIdentifier) ) channelIdent = channelIdent + "_";
        }
        else
        {
            channelIdent = channelIdentifier;
        }
        this.starter = starter;
        participants.add(starter);
        participants.add(target);
        ChannelHandler.registerChannel(this, ChatColor.translateAlternateColorCodes('&', name), channelIdentifier);
        target.sendMessage(String.format(_("SinkChat.Channels.Private.InvitedToChat"), starter.getDisplayName(), channelIdent));

    }

    @Override
    public void addPlayer(Player invitor, Player target)
    {

        if ( participants.contains(target) )
        {
            invitor.sendMessage(String.format(_("SinkChat.Channels.Private.HasInvitedToChat.ErrorAlreadyInChat"), target.getDisplayName()));
            return;
        }

        participants.add(target);
        target.sendMessage(String.format(_("SinkChat.Channels.Private.InvitedToChat"), invitor.getDisplayName(), channelIdent));
    }

    @Override
    public void kickPlayer(Player player, Player kicker, String reason)
    {
        if ( player.equals(kicker) )
        {
            participants.remove(player);
            sendMessage(player, String.format(_("SinkChat.Channels.Private.PlayerLeftCon"), channelIdent));
            return;
        }

        participants.remove(player);
        sendMessage(player, String.format(_("SinkChat.Channels.Private.PlayerKicked"), reason));
    }

    @Override
    public boolean sendMessage(Player player, String message)
    {
        message = String.format(message, player.getDisplayName());

        for ( Player p : participants )
        {
            p.sendMessage(message);
        }
        return true;
    }

    @Override
    public boolean contains(Player player)
    {
        return (participants.contains(player));
    }

    @Override
    public String getChannelName()
    {
        return channelIdent;
    }

    @Override
    public String getPermission()
    {
        return "sinkchat.privatechannel.use";
    }

    @Override
    public String getChannelIdentifier()
    {
        return channelIdent;
    }

    public Player getStarter()
    {
        return starter;
    }

}