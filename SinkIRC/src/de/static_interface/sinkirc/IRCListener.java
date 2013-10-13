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

package de.static_interface.sinkirc;

import de.static_interface.sinklibrary.events.IRCMessageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class IRCListener implements Listener
{
    private IRCBot ircBot;

    public IRCListener(IRCBot ircBot)
    {
        this.ircBot = ircBot;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), event.getJoinMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), event.getQuitMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        String reason = " for reason: " + event.getReason();
        if (event.getReason().equals("")) reason = "";
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), "Player \"" + event.getPlayer().getDisplayName() + "\" has been kicked" + reason + "!");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), event.getDeathMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCMessage(IRCMessageEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), event.getMessage());
    }

}
