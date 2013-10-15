package de.static_interface.sinkchat.channel.channels;

import de.static_interface.sinkchat.channel.IPrivateChannel;
import de.static_interface.sinkchat.channel.PrivateChannelHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

import java.util.Vector;

public class PrivateChannel implements IPrivateChannel{

    String channelIdent = new String();
    Vector<Player> participants = new Vector<>();
    Player starter;

    public PrivateChannel(String channelIdentifier, Player starter, Player target)
    {
        if ( PrivateChannelHandler.channelIdentIsTaken(channelIdentifier) )
        {
            channelIdent = channelIdentifier+"_";
            while ( PrivateChannelHandler.channelIdentIsTaken(channelIdentifier) ) channelIdent = channelIdent+"_";
        }
        else
        {
            channelIdent = channelIdentifier;
        }
        this.starter = starter;
        participants.add(starter);
        participants.add(target);

        target.sendMessage(_("SinkChat.Channels.Private.InvitedToChat").replace("%i", starter.getDisplayName()).replace("%c", channelIdent));

    }

    @Override
    public void addPlayer(Player invitor, Player target) {

        if ( participants.contains(target) )
        {
            invitor.sendMessage(_("SinkChat.Channels.Private.HasInvitedToChat.ErrorAlreadyInChat").replace("%t", target.getDisplayName()));
            return;
        }

        participants.add(target);
        target.sendMessage(_("SinkChat.Channels.Private.InvitedToChat").replace("%i", invitor.getDisplayName()).replace("%c", channelIdent));

    }

    @Override
    public void kickPlayer(Player player, Player kicker, String reason) {
        if ( player.equals(kicker) )
        {
            participants.remove(player);
            sendMessage(_("SinkChat.Channels.Private.PlayerLeftCon").replace("%t",player.getDisplayName()).replace("%c",channelIdent));
            return;
        }

        participants.remove(player);
        sendMessage(_("SinkChat.Channels.Private.PlayerKicked").replace("%t",player.getDisplayName()).replace("%c", reason));
    }

    @Override
    public void sendMessage(String message) {
        for ( Player p : participants )
        {
            p.sendMessage(message);
        }
    }

    @Override
    public void registerConversation() {
        PrivateChannelHandler.registerChannel(this);
    }

    @Override
    public boolean contains(Player player) {
        return ( participants.contains(player) );
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