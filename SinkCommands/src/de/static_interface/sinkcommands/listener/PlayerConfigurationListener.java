package de.static_interface.sinkcommands.listener;

import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConfigurationListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        User user = new User(event.getPlayer());
        PlayerConfiguration config = user.getPlayerConfiguration();

        if (! config.exists())
        {
            config.create();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        User user = new User(event.getPlayer());
        PlayerConfiguration config = user.getPlayerConfiguration();
        config.save();
    }
}
