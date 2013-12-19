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

import org.bukkit.entity.Player;

import java.util.Vector;

public abstract class IPrivateChannel implements IChannel
{

    /**
     * Add player to existing conversation
     *
     * @param invitor Player who invited target
     * @param target  Player who was invited
     */

    public abstract void addPlayer(Player invitor, Player target);

    /**
     * Kicks a player off a conversation
     *
     * @param player Player to kick
     * @param kicker Player who kicked
     * @param reason Reason for kick.
     */


    public abstract void kickPlayer(Player player, Player kicker, String reason);

    /**
     * Registers conversation at PrivateChannelHandler
     */

    public void registerChannel()
    {
        PrivateChannelHandler.registerChannel(this);
    }

    /**
     * Returns wether a conversation contains a player or not
     *
     * @param player Player to check for
     * @return True if player is in that conversation, else false.
     */

    public abstract boolean contains(Player player);

    /**
     * Returns channel identifier
     *
     * @return channel identifier
     */

    public abstract String getChannelIdentifier();

    /**
     * Returns the player who started that conversation.
     *
     * @return Player who started that conversation
     */
    public abstract Player getStarter();

    @Override
    public void addExceptedPlayer(Player player)
    {
        throw new RuntimeException("This method is not supported with this class");
    }

    @Override
    public void removeExceptedPlayer(Player player)
    {
        throw new RuntimeException("This method is not supported with this class");
    }

    public abstract void setChannelName(String channelName);

    public abstract Vector<Player> getPlayers();
}