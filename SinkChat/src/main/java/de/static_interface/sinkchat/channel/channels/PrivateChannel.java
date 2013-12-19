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

package de.static_interface.sinkchat.channel.channels;

import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.IPrivateChannel;
import de.static_interface.sinkchat.channel.PrivateChannelHandler;
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Vector;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class PrivateChannel extends IPrivateChannel
{
    String channelIdent;
    Vector<Player> participants = new Vector<>();
    Player starter;
    String channelName;

    public PrivateChannel(String channelIdentifier, Player starter, String name)
    {
        channelName = name;
        if ( PrivateChannelHandler.isChannelIdentTaken(channelIdentifier) )
        {
            channelIdent = channelIdentifier + '_';
            while ( PrivateChannelHandler.isChannelIdentTaken(channelIdentifier) ) channelIdent = channelIdent + '_';
        }
        else
        {
            channelIdent = channelIdentifier;
        }
        this.starter = starter;
        participants.add(starter);
        ChannelHandler.registerChannel(this, ChatColor.GRAY + "[" + ChatColor.translateAlternateColorCodes('&', name) + ChatColor.GRAY + ']', channelIdentifier);

    }

    @Override
    public void addPlayer(Player invitor, Player target)
    {

        if ( participants.contains(target) )
        {
            invitor.sendMessage(String.format(_("SinkChat.Channels.Private.HasInvitedToChat.ErrorAlreadyInChat"), target.getDisplayName()));
            return;
        }

        participants.add(target);
        target.sendMessage(String.format(_("SinkChat.Channels.Private.InvitedToChat"), invitor.getDisplayName(), channelIdent));
    }

    @Override
    public void kickPlayer(Player player, Player kicker, String reason)
    {
        if ( player.equals(kicker) )
        {
            participants.remove(player);
            sendMessage(player, String.format(_("SinkChat.Channels.Private.PlayerLeftCon"), channelIdent));
            return;
        }

        participants.remove(player);
        sendMessage(player, String.format(_("SinkChat.Channels.Private.PlayerKicked"), reason));
    }

    @Override
    public boolean sendMessage(Player player, String message)
    {
        message = String.format(message, player.getDisplayName());
        message = message.replaceFirst(channelIdent, "");
        String group = "";
        if ( SinkLibrary.isChatAvailable() )
        {
            group = '[' + SinkLibrary.getUser(player).getPrimaryGroup() + ChatColor.GRAY + "] ";
        }
        message = ChatColor.GRAY + "[" + ChatColor.translateAlternateColorCodes('&', channelName) + ChatColor.GRAY + "] " + ChatColor.GRAY + group + SinkLibrary.getUser(player).getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', message);
        for ( Player p : participants )
        {
            p.sendMessage(message);
        }
        return true;
    }

    @Override
    public boolean contains(Player player)
    {
        return (participants.contains(player));
    }

    @Override
    public String getChannelName()
    {
        return channelName;
    }

    @Override
    public String getPermission()
    {
        return "sinkchat.privatechannel.use";
    }

    @Override
    public String getChannelIdentifier()
    {
        return channelIdent;
    }

    public Player getStarter()
    {
        return starter;
    }

    @Override
    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    @Override
    public Vector<Player> getPlayers()
    {
        return participants;
    }

}