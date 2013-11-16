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

package de.static_interface.sinkchat.listener;

import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import de.static_interface.sinklibrary.configuration.Settings;
import de.static_interface.sinklibrary.SinkLibrary;
import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListenerHighest implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        if ( event.isCancelled() )
        {
            return;
        }

        for ( String callChar : ChannelHandler.getRegisteredCallChars().keySet() )
        {
            if ( event.getMessage().startsWith(callChar) && ChannelHandler.getRegisteredChannel(callChar).sendMessage(event.getPlayer(), event.getMessage()) )
            {
                event.setCancelled(true);
                return;
            }
        }

        User eventPlayer = SinkLibrary.getUser(event.getPlayer());

        String message = event.getMessage();
        int range = SinkLibrary.getSettings().getLocalChatRange();
        String formattedMessage = String.format(event.getFormat(), eventPlayer.getDisplayName(), message);

        if ( !SinkLibrary.isPermissionsAvailable() )
        {
            formattedMessage = ChatColor.GRAY + _("SinkChat.Prefix.Chat.Local") + ChatColor.RESET + " " + formattedMessage;
        }

        String spyPrefix = ChatColor.GRAY + _("SinkChat.Prefix.Spy") + " " + ChatColor.RESET;

        double x = event.getPlayer().getLocation().getX();
        double y = event.getPlayer().getLocation().getY();
        double z = event.getPlayer().getLocation().getZ();

        for ( Player p : Bukkit.getOnlinePlayers() )
        {
            Location loc = p.getLocation();
            boolean isInRange = Math.abs(x - loc.getX()) <= range && Math.abs(y - loc.getY()) <= range && Math.abs(z - loc.getZ()) <= range;

            User onlineUser = SinkLibrary.getUser(p);

            boolean newbieSpy = (onlineUser.hasPermission("sinkchat.spynewbie")) && !eventPlayer.hasPermission("sinkchat.spynewbie.bypass");
            boolean maySpy = onlineUser.hasPermission("sinkchat.spy");

            PlayerConfiguration config = onlineUser.getPlayerConfiguration();

            if ( isInRange )
            {
                p.sendMessage(formattedMessage);
            }
            else if ( (newbieSpy || maySpy) && config.getSpyEnabled() )
            {
                p.sendMessage(spyPrefix + formattedMessage);
            }
        }
        Bukkit.getConsoleSender().sendMessage(spyPrefix + formattedMessage);
        event.setCancelled(true);
    }
}