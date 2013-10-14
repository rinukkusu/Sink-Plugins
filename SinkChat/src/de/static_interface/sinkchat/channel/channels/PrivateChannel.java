package de.static_interface.sinkchat.channel.channels;

import org.bukkit.entity.Player;

import de.static_interface.sinklibrary.configuration.LanguageConfiguration;

import java.util.Vector;

public class PrivateChannel {

    String channelIdent = new String();
    Vector<Player> participants = new Vector<>();

    public PrivateChannel(String channelIdentifier, Player starter, Player... targets)
    {
        channelIdent = channelIdentifier;
        participants.add(starter);
        for ( Player p : targets ){
            if ( participants.contains(p) ) continue;

            p.sendMessage(LanguageConfiguration._("SinkChat.Channels.Private.InvitedToChat").replace("%p",starter.getDisplayName()));

        }

    }


}