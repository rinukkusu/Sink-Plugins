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

public interface IChannel
{
    /**
     * Joining a Channel will remove you from the list<br>
     * of excepted players.
     */

    /**
     * @param player Given player will be added to the excepted players list.
     *               Will add the player to the excepted players list   <br>
     *               so he will be excepted from the messages sent.     <br>
     */
    public void addExceptedPlayer(Player player);

    /**
     * @param player Player to remove from the excepted players list.
     *               Removes player from the excepted players list.
     */
    public void removeExceptedPlayer(Player player);

    /**
     * Send message to all players in channel
     *
     * @param player  Sender
     * @param message Message
     * @return True if message was send successfully
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean sendMessage(Player player, String message);


    /**
     * Register Channel
     */
    public void registerChannel();

    /**
     * @param player Player to check for.
     * @return True if player was added to exceptedPlayers, else false.
     */
    public boolean contains(Player player);

    public String getChannelName();

    /**
     * @return Permission required to join channel
     */
    public String getPermission();
}
