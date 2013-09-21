package de.static_interface.sinkchat.channel;

import java.util.Collection;
import java.util.TreeMap;

public class ChannelHandler
{

    private static String registeredChannelNames = "";

    private static TreeMap<String, IChannel> registeredChannelsMap = new TreeMap<>();
    private static TreeMap<Character, IChannel> callChars = new TreeMap<>();
    private static TreeMap<Character, String> prefixes = new TreeMap<>();

    /**
     * @param channel  Instance of Channel to be registered.
     * @param name     Name of given instance to be registered.
     * @param prefix   Prefix of given instance to be registered.
     * @param callChar Char you use to call the channel in game.
     */
    public static void registerChannel(IChannel channel, String name, String prefix, char callChar)
    {
        if (registeredChannelsMap.containsValue(name))
        {
            return;
        }

        registeredChannelsMap.put(name, channel);
        callChars.put(callChar, channel);
        prefixes.put(callChar, prefix);
        registeredChannelNames = registeredChannelNames + name + " ";


    }

    /**
     * @param name Channel name you get the channel with.<br>
     *             Important: Case sensitive !
     * @return Returns the instance of Channel with the given name.
     */

    public static IChannel getRegisteredChannel(String name)
    {
        return registeredChannelsMap.get(name);
    }

    /**
     * @return Returns regiteredChannelNames, a String containing the names of all registered instances.
     */

    public static String getChannelNames()
    {
        return registeredChannelNames;
    }

    /**
     * @param callByChar Char you use to call the channel inGame.
     * @return Returns the instance of Channel with given callByChar
     */

    public static IChannel getRegisteredChannel(char callByChar)
    {
        return callChars.get(callByChar);
    }

    /**
     * @return Returns a collection containing all registered channels.
     */

    public static Collection<IChannel> getRegisteredChannels()
    {
        return registeredChannelsMap.values();
    }

    /**
     * @return Returns the registered callChars.
     */

    public static TreeMap<Character, IChannel> getRegisteredCallChars()
    {
        return callChars;
    }

}
