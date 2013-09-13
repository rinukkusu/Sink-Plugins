package de.static_interface.commandsplugin.listener;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Author: Trojaner
 * Date: 13.09.13
 */
public class ScoreboardListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        CommandsPlugin.refreshScoreboard(Bukkit.getOnlinePlayers().length + 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        CommandsPlugin.refreshScoreboard(Bukkit.getOnlinePlayers().length - 1);
    }
}
