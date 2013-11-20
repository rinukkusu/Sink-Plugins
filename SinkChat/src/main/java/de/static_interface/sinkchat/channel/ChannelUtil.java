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

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChannelUtil
{
    public static boolean sendMessage(Player player, String message, IChannel channel, String prefix, String callByChar)
    {
        if ( channel.contains(player) )
        {
            return false;
        }

        if ( message.equals(callByChar) )
        {
            return false;
        }

        String formattedMessage = message.substring(1);
        User user = SinkLibrary.getUser(player);

        if ( SinkLibrary.isPermissionsAvailable() )
        {
            formattedMessage = prefix + ChatColor.GRAY + "[" + user.getPrimaryGroup() + ChatColor.GRAY + "] " + user.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.RESET + formattedMessage;
        }
        else
        {
            formattedMessage = prefix + user.getDisplayName() + ChatColor.GRAY + ": " + ChatColor.RESET + formattedMessage;
        }

        for ( Player target : Bukkit.getOnlinePlayers() )
        {
            if ( !(channel.contains(target)) && target.hasPermission(channel.getPermission()) )
            {
                target.sendMessage(formattedMessage);
            }
        }

        Bukkit.getConsoleSender().sendMessage(formattedMessage);
        SinkLibrary.sendIRCMessage(formattedMessage);
        return true;
    }
}
