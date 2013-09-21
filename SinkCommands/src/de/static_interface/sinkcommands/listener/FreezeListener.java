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
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (FreezeCommands.isFrozen(event.getPlayer()))
        {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (! event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (! event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgnite(BlockIgniteEvent event)
    {
        if (! event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (! event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        if (FreezeCommands.isFrozen(event.getPlayer()))
        {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (! event.isCancelled() && event.getPlayer() != null && FreezeCommands.isFrozen(event.getPlayer()))
        {
            if (event.getMessage().startsWith("/login") || event.getMessage().startsWith("/register"))
            {
                return;
            }
            event.getPlayer().sendMessage(FreezeCommands.PREFIX + "Du bist eingefroren und darfst keine Commands nutzen.");
            event.setCancelled(true);
        }
    }
}