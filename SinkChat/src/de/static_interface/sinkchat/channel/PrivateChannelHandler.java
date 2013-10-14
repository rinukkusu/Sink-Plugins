package de.static_interface.sinkchat.channel;

import java.util.TreeMap;

public class PrivateChannelHandler {

    private static TreeMap<String, IPrivateChannel> registeredConversations = new TreeMap<>();

    public void registerChannel(String channelIdentifier, IPrivateChannel channel)
    {
        registeredConversations.put(channelIdentifier, channel);
    }

    public IPrivateChannel getChannel(String channelIdentifier)
    {
        return (registeredConversations.get(channelIdentifier));
    }

}
