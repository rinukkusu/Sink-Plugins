package de.static_interface.sinkchat.channel;

import de.static_interface.sinkchat.channel.channels.PrivateChannel;

import java.util.TreeMap;

public class PrivateChannelHandler {

    private static TreeMap<String, PrivateChannel> registeredConversations = new TreeMap<>();

    public static void registerChannel(String channelIdentifier, PrivateChannel channel)
    {
        registeredConversations.put(channelIdentifier, channel);
    }

    public static PrivateChannel getChannel(String channelIdentifier)
    {
        return (registeredConversations.get(channelIdentifier));
    }

}
