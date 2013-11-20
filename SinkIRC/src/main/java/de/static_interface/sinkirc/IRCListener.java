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

import de.static_interface.sinklibrary.BukkitUtil;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.events.*;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.static_interface.sinkirc.IRCBot.IRC_PREFIX;

public class IRCListener implements Listener
{
    private final IRCBot ircBot;

    public IRCListener(IRCBot ircBot)
    {
        this.ircBot = ircBot;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if ( IRCBot.isDisabled() )
        {
            return;
        }
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), event.getJoinMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if ( IRCBot.isDisabled() )
        {
            return;
        }
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), event.getQuitMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event)
    {
        if ( IRCBot.isDisabled() )
        {
            return;
        }
        String reason = ": " + event.getReason();
        if ( event.getReason().equals("") ) reason = "!";
        User user = SinkLibrary.getUser(event.getPlayer());
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), "" + user.getDisplayName() + ChatColor.RESET + " has been kicked" + reason);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if ( IRCBot.isDisabled() )
        {
            return;
        }
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), event.getDeathMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCMessage(IRCSendMessageEvent event)
    {
        if ( IRCBot.isDisabled() )
        {
            return;
        }
        ircBot.sendCleanMessage(SinkIRC.getMainChannel(), event.getMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCJoin(IRCJoinEvent event)
    {
        if ( event.getSender().equals(ircBot.getNick()) )
        {
            return;
        }
        ircBot.sendMessage(event.getChannel(), "Willkommen, " + event.getSender() + "!");
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + event.getChannel() + "] " + ChatColor.DARK_AQUA + event.getSender() + ChatColor.WHITE + " ist dem Kanal beigetreten.", false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCKick(IRCKickEvent event)
    {
        String reason = event.getReason();
        if ( reason.equals(event.getKickerNick()) )
        {
            reason = "";
        }
        String formattedReason = "Grund: " + reason + ".";
        if ( reason.equals("") || reason.equals("\"\"") )
        {
            formattedReason = "";
        }
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + event.getChannel() + "] " + ChatColor.DARK_AQUA + event.getRecipientNick() + ChatColor.WHITE + " wurde von " + event.getKickerNick() + " aus dem Kanal geworfen. " + formattedReason, false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCNickChange(IRCNickChangeEvent event)
    {
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + "Server" + "] " + ChatColor.DARK_AQUA + event.getOldNick() + ChatColor.WHITE + " ist jetzt als " + ChatColor.DARK_AQUA + event.getNewNick() + ChatColor.WHITE + " bekannt.", false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCPart(IRCPartEvent event)
    {
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + event.getChannel() + "] " + ChatColor.DARK_AQUA + event.getSender() + ChatColor.WHITE + " hat den Kanal verlassen.");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCPing(IRCPingEvent event)
    {
        ircBot.sendMessage(SinkIRC.getMainChannel(), event.getSourceNick() + " hat mich mit dem Wert \"" + event.getPingValue() + "\" angepingt.");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onIRCPrivateMessage(IRCPrivateMessageEvent event)
    {
        String[] args = event.getMessage().split(" ");
        String cmd = args[0];
        List<String> tmp = new ArrayList<>(Arrays.asList(args));
        tmp.remove(0);
        args = tmp.toArray(args);
        IRCBot.executeCommand(cmd, args, event.getSender(), event.getSender(), event.getMessage(), ircBot);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCQuit(IRCQuitEvent event)
    {
        String formattedReason = " (" + event.getReason() + ")";
        if ( event.getReason().equals("") || event.getReason().equals("\"\"") )
        {
            formattedReason = "";
        }
        BukkitUtil.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + "Server" + "] " + ChatColor.DARK_AQUA + event.getSourceNick() + ChatColor.WHITE + " hat den IRC Server verlassen." + formattedReason, false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onIRCReceiveMessage(IRCReceiveMessageEvent event)
    {
        String message = event.getMessage();
        String channel = event.getChannel();
        String sender = event.getSender();

        if ( (message.toLowerCase().contains("hello") || message.toLowerCase().contains("hi") || message.toLowerCase().contains("huhu") || message.toLowerCase().contains("hallo") || message.toLowerCase().contains("moin") || message.toLowerCase().contains("morgen")) && (message.toLowerCase().contains(" " + ircBot.getName() + " ") || message.toLowerCase().contains(" bot ")) )
        {
            ircBot.sendMessage(channel, "Hallo, " + sender);
            return;
        }

        if ( !message.toLowerCase().startsWith("~") )
        {
            return;
        }
        message = message.replaceFirst("~", "");
        String[] args = message.split(" ");
        String cmd = args[0];
        List<String> tmp = new ArrayList<>(Arrays.asList(args));
        tmp.remove(0);
        args = tmp.toArray(args);
        IRCBot.executeCommand(cmd, args, channel, sender, message, ircBot);
    }
}
