package de.static_interface.commandsplugin.listener;

import de.static_interface.commandsplugin.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConfigurationListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        PlayerConfiguration config = new PlayerConfiguration(event.getPlayer().getName());
        if (! config.exists())
        {
            config.create();
        }
    }
}
