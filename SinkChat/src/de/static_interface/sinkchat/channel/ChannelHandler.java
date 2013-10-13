/*
 * Copyright (c) 2013 adventuria.eu / static-interface.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.static_interface.sinkchat.channel;

import java.util.Collection;
import java.util.TreeMap;

public class ChannelHandler
{

    private static String registeredChannelNames = "";

    private static TreeMap<String, IChannel> registeredChannelsMap = new TreeMap<>();
    private static TreeMap<String, IChannel> callChars = new TreeMap<>();

    /**
     * @param channel  Instance of Channel to be registered.
     * @param name     Name of given instance to be registered.
     * @param callChar Char you use to call the channel in game.
     */
    public static void registerChannel(IChannel channel, String name, String callChar)
    {
        if (registeredChannelsMap.containsValue(name))
        {
            return;
        }

        registeredChannelsMap.put(name, channel);
        callChars.put(callChar, channel);
        registeredChannelNames = registeredChannelNames + name + " ";
    }

    /**
     * @param callChar Char you use to call the channel in game.
     * @return Returns the instance of Channel with the given name.
     */

    public static IChannel getRegisteredChannel(String callChar)
    {
        return callChars.get(callChar);
    }

    /**
     * @param name Name of the channel
     * @return IChannel instance
     */
    public static IChannel getChannelByName(String name)
    {
        for (String channel : registeredChannelsMap.keySet())
        {
            if (channel.equalsIgnoreCase(name)) return registeredChannelsMap.get(channel);
        }
        return null;
    }

    /**
     * @return Returns regiteredChannelNames, a String containing the names of all registered instances.
     */

    public static String getChannelNames()
    {
        return registeredChannelNames;
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

    public static TreeMap<String, IChannel> getRegisteredCallChars()
    {
        return callChars;
    }

}
