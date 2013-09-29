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

import de.static_interface.sinkirc.commands.IrclistCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jibble.pircbot.IrcException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SinkIRC extends JavaPlugin implements Listener
{
    private static String channel = "#AdventuriaBot";

    static IRCBot ircBot;
    Logger log;

    @Override
    public void onEnable()
    {
        ircBot = new IRCBot(this);

        if (Bukkit.getPluginManager().getPlugin("SinkChat") == null)
        {
            Bukkit.getLogger().log(Level.WARNING, "This plugin will not work without SinkChat.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);

        log = getLogger();

        try
        {
            String host = "irc.adventuria.eu";
            int port = 6667;
            ircBot.connect(host, port);
            //Todo: Add NickServ identification support
            ircBot.joinChannel(channel);
        }
        catch (IOException | IrcException e)
        {
            String host = "irc.adventuria.eu";
            log.severe("An Exception occurred while trying to connect to " + host + ":");
            log.severe(e.toString());
        }
        getCommand("irclist").setExecutor(new IrclistCommand());
    }

    @Override
    public void onDisable()
    {
        ircBot.quitServer("Plugin reload or plugin has been deactivated");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(channel, event.getJoinMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(channel, event.getQuitMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        if (! event.getReason().equals(""))
        {
            ircBot.sendCleanMessage(channel, "Player \"" + event.getPlayer().getDisplayName() + "\" has been kicked for reason: " + event.getReason());
        }
        else
        {
            ircBot.sendCleanMessage(channel, "Player \"" + event.getPlayer().getDisplayName() + "\" has been kicked!");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(channel, event.getDeathMessage());
    }

    public static IRCBot getIRCBot()
    {
        return ircBot;
    }

    public static String getChannel()
    {
        return channel;
    }
}
