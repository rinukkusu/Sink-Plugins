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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class ChatListenerNormal implements Listener
{
    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }

        for (String callChar : ChannelHandler.getRegisteredCallChars().keySet())
        {
            if (event.getMessage().startsWith(callChar)
                    && ChannelHandler.getRegisteredChannel(callChar).sendMessage(event.getPlayer(), event.getMessage()))
            {
                event.setCancelled(true);
                return;
            }
        }

        User eventPlayer = SinkLibrary.getUser(event.getPlayer());

        String message = event.getMessage();
        int range = 50;
        String formattedMessage = event.getFormat().replace("%1$s", eventPlayer.getDisplayName());
        formattedMessage = formattedMessage.replace("%2$s", message);

        if (eventPlayer.hasPermission("sinkchat.color"))
        {
            formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);
        }

        if (! SinkLibrary.permissionsAvailable())
        {
            formattedMessage = ChatColor.GRAY + _("SinkChat.Prefix.ChatLocal") + ChatColor.RESET + " " + formattedMessage;
        }

        String spyPrefix = ChatColor.GRAY + _("SinkChat.Prefix.Spy") + " " + ChatColor.RESET;

        double x = event.getPlayer().getLocation().getX();
        double y = event.getPlayer().getLocation().getY();
        double z = event.getPlayer().getLocation().getZ();

        for (Player p : Bukkit.getOnlinePlayers())
        {
            Location loc = p.getLocation();
            boolean isInRange = Math.abs(x - loc.getX()) <= range && Math.abs(y - loc.getY()) <= range && Math.abs(z - loc.getZ()) <= range;

            User onlineUser = SinkLibrary.getUser(p);

            boolean newbieSpy = ( onlineUser.hasPermission("sinkchat.spynewbie") ) && ! onlineUser.hasPermission("sinkchat.spynewbie.bypass");
            boolean maySpy = onlineUser.hasPermission("sinkchat.spy");

            PlayerConfiguration config = onlineUser.getPlayerConfiguration();

            if (isInRange)
            {
                p.sendMessage(formattedMessage);
            }
            else if (( newbieSpy || maySpy ) && config.getSpyEnabled())
            {
                p.sendMessage(spyPrefix + formattedMessage);
            }
        }
        Bukkit.getConsoleSender().sendMessage(spyPrefix + formattedMessage);
        event.setCancelled(true);
    }
}