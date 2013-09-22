package de.static_interface.sinkchat.listener;

import de.static_interface.sinkchat.SinkChat;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static de.static_interface.sinkchat.command.NickCommand.HAS_NICKNAME_PATH;
import static de.static_interface.sinkchat.command.NickCommand.NICKNAME_PATH;

public class NicknameListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        User user = new User(event.getPlayer());
        PlayerConfiguration config = user.getPlayerConfiguration();
        String nick = (String) config.get(NICKNAME_PATH);
        if (nick == null || nick.equals("null") || nick.equals(""))
        {
            config.getYamlConfiguration().addDefault(event.getPlayer().getName() + HAS_NICKNAME_PATH, false);
            config.getYamlConfiguration().addDefault(event.getPlayer().getName() + NICKNAME_PATH, "");
            SinkChat.setDisplayName(event.getPlayer(), SinkChat.getDefaultDisplayName(event.getPlayer()));
            SinkChat.setHasDisplayName(event.getPlayer(), false);
        }
    }
}
