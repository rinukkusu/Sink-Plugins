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

package de.static_interface.sinklibrary;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

public class BukkitUtil
{

    /**
     * Get online Player by name.
     *
     * @param name Name of the
     * @return If more than one matches, it will return the player with exact name.
     */
    public static Player getPlayer(String name)
    {
        List<Player> matchedPlayers = new ArrayList<>();
        for ( Player player : Bukkit.getOnlinePlayers() )
        {
            if ( player.getName().contains(name) ) matchedPlayers.add(player);
        }

        Player exactPlayer = Bukkit.getPlayerExact(name);
        if ( matchedPlayers.toArray().length > 1 && exactPlayer != null )
        {
            return exactPlayer;
        }
        else
        {
            return matchedPlayers.get(0);
        }
    }

    /**
     * @param sender Command Sender
     * @return If {@link org.bukkit.command.CommandSender CommandSnder} is instance of {@link org.bukkit.command.ConsoleCommandSender ConsoleCommandSender},
     * it will return "Console" in {@link org.bukkit.ChatColor#RED RED}, if sender is instance of
     * {@link org.bukkit.entity.Player Player}, it will return player's {@link org.bukkit.entity.Player#getDisplayName() DisplayName}
     */
    public static String getSenderName(CommandSender sender)
    {
        User user = SinkLibrary.getUser(sender);
        return user.getDisplayName();
    }

    /**
     * Use this instead of {@link org.bukkit.Bukkit#broadcast(String, String).}.
     * Send message to all players with specified permission.
     * It will also send the message also to IRC
     *
     * @param message Message to send
     */
    public static void broadcastMessage(String message)
    {
        for ( Player p : Bukkit.getOnlinePlayers() )
        {
            p.sendMessage(message);
        }
        Bukkit.getConsoleSender().sendMessage(message);
        SinkLibrary.sendIRCMessage(message);
    }

    /**
     * Use this instead of {@link org.bukkit.Bukkit#broadcast(String message, String permission).
     * Send message to all players with specified permission.
     * It will also send the message to IRC if the default permission is true}.
     *
     * @param message    Message to send
     * @param permission Permission needed to receive the message
     */
    public static void broadcast(String message, String permission)
    {
        for ( Player p : Bukkit.getOnlinePlayers() )
        {
            User user = SinkLibrary.getUser(p);
            if ( !user.hasPermission(permission) )
            {
                continue;
            }
            p.sendMessage(message);
        }
        Bukkit.getConsoleSender().sendMessage(message);
        Permission perm = new Permission(permission);
        if ( perm.getDefault() == PermissionDefault.TRUE )
        {
            SinkLibrary.sendIRCMessage(message);
        }
    }
}
