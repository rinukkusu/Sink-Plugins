package de.static_interface.commandsplugin.listener;

import de.static_interface.commandsplugin.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConfigurationListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        PlayerConfiguration config = new PlayerConfiguration(event.getPlayer().getName());
        if (! config.exists())
        {
            config.create();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        PlayerConfiguration config = new PlayerConfiguration(event.getPlayer().getName());
        config.save();
    }
}
