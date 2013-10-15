package de.static_interface.sinkchat.channel;

import java.util.Set;
import java.util.TreeMap;

public class PrivateChannelHandler
{

    private static TreeMap<String, IPrivateChannel> registeredConversations = new TreeMap<>();

    public static void registerChannel(IPrivateChannel channel)
    {
        registeredConversations.put(channel.getChannelIdentifier(), channel);
    }

    public static IPrivateChannel getChannel(String channelIdentifier)
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
