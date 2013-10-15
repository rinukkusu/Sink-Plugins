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

package de.static_interface.sinkcommands.listener;

import de.static_interface.sinkcommands.commands.FreezeCommands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FreezeListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if ( FreezeCommands.isFrozen(event.getPlayer()) )
        {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if ( !event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()) )
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if ( !event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()) )
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockIgnite(BlockIgniteEvent event)
    {
        if ( !event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()) )
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if ( !event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()) )
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if ( FreezeCommands.isFrozen(event.getPlayer()) )
        {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if ( !event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()) )
        {
            if ( event.getMessage().startsWith("/login") || event.getMessage().startsWith("/register") )
            {
                return;
            }
            event.getPlayer().sendMessage(FreezeCommands.PREFIX + "Du bist eingefroren und darfst keine Commands nutzen.");
            event.setCancelled(true);
        }
    }
}