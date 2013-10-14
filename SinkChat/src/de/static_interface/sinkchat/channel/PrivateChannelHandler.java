package de.static_interface.sinkchat.channel;

import de.static_interface.sinkchat.channel.channels.PrivateChannel;

import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class PrivateChannelHandler {

    private static TreeMap<String, PrivateChannel> registeredConversations = new TreeMap<>();

    public static void registerChannel(PrivateChannel channel)
    {
        registeredConversations.put(channel.getChannelIdentifier(), channel);
    }

    public static PrivateChannel getChannel(String channelIdentifier)
    {
        return (registeredConversations.get(channelIdentifier));
    }

    public static boolean channelIdentIsTaken(String channelIdentifier)
    {
        Set<String> keys = registeredConversations.keySet();
        for ( String s : keys )
        {
            if ( s.equals(channelIdentifier) ) return true;
        }

        return false;
    }

}
