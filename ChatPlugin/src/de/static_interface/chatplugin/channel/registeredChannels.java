package de.static_interface.chatplugin.channel;

import java.util.Collection;
import java.util.TreeMap;

public class registeredChannels {

    private static String registeredChannelNames = "";

    private static TreeMap<String, Channel> registeredChannelsMap = new TreeMap<>();
    private static TreeMap<Character, Channel> callStrings = new TreeMap<>();
    private static TreeMap<Character, String> prefixes = new TreeMap<>();

    /**
     * @param channel
     * Instance of Channel to be registered.
     * @param name
     * Name of given instance to be registered.
     * @param prefix
     * Prefix of given instance to be registered.
     * @param callChar
     * Char you use to call the channel in game.
     */
    public static void registerChannel(Channel channel, String name, String prefix, char callChar)
    {
        if (registeredChannelsMap.containsValue(name)) return;

        registeredChannelsMap.put(name, channel);
        callStrings.put(callChar, channel);
        prefixes.put(callChar, prefix);



    }

    /**
     * @param name
     * Channel name you get the channel with.<br>
     * Important: Case sensitive !
     * @return
     * Returns the instance of Channel with the given name.
     */

    public static Channel getRegisteredChannel(String name)
    {
        return registeredChannelsMap.get(name);
    }

    /**
     * @return
     * Returns regiteredChannelNames, a String containing the names of all registered instances.
     */

    public static String getChannelNames()
    {
        return registeredChannelNames;
    }

    /**
     * @param callByChar
     * Char you use to call the channel inGame.
     * @return
     * Returns the instance of Channel with given callByChar
     */

    public static Channel getRegisteredChannel(char callByChar)
    {
        return callStrings.get(callByChar);
    }

    /**
     * @return
     * Returns a collection containing all registered channels.
     */

    public static Collection<Channel> getRegisteredChannels()
    {
        return registeredChannelsMap.values();
    }

    /**
     * @param callChar
     * Char you use in game to call the channel.
     * @return
     * Returns the prefix of the instance using the given callChar
     */

    public static String getChannelPrefix(char callChar)
    {
        return prefixes.get(callChar);
    }

}
