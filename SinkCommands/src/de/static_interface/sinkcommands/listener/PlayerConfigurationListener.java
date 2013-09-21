package de.static_interface.sinkcommands.listener;

import de.static_interface.sinkcommands.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConfigurationListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        PlayerConfiguration config = new PlayerConfiguration(event.getPlayer().getName());
        if (! config.exists())
        {
            config.create();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        PlayerConfiguration config = new PlayerConfiguration(event.getPlayer().getName());
        config.save();
    }
}
