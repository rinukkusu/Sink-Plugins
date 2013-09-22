package de.static_interface.sinkchat.listener;

import de.static_interface.sinkchat.SinkChat;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class NicknameListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        User user = new User(event.getPlayer());
        PlayerConfiguration config = user.getPlayerConfiguration();
        String nick = config.getDisplayName();
        if (nick == null || nick.equals("null") || nick.equals(""))
        {
            config.setDisplayName(SinkChat.getDefaultDisplayName(event.getPlayer()));
            config.setHasDisplayName(false);
        }
    }
}
