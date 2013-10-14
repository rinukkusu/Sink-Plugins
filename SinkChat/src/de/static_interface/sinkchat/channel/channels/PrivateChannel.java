package de.static_interface.sinkchat.channel.channels;

import de.static_interface.sinkchat.channel.IPrivateChannel;
import org.bukkit.entity.Player;

import de.static_interface.sinklibrary.configuration.LanguageConfiguration;

import java.util.Vector;

public class PrivateChannel implements IPrivateChannel{

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

    @Override
    public void addPlayer(Player target) {

        if ( participants.contains(target) ) return;




    }

    @Override
    public void kickPlayer(Player player, Player kicker, String reason) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void sendMessage(String message) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerConversation() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}