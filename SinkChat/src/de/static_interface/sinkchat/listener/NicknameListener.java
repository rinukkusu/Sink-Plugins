package de.static_interface.sinkchat.listener;

import de.static_interface.sinkchat.SinkChat;
import de.static_interface.sinkcommands.PlayerConfiguration;
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
        PlayerConfiguration config = new PlayerConfiguration(event.getPlayer().getName());
        String nick = config.getString(NICKNAME_PATH);
        if (nick == null || nick.equals("null") || nick.equals(""))
        {
            config.getPlayerConfiguration().addDefault(event.getPlayer().getName() + HAS_NICKNAME_PATH, false);
            config.getPlayerConfiguration().addDefault(event.getPlayer().getName() + NICKNAME_PATH, "");
            SinkChat.setDisplayName(event.getPlayer(), SinkChat.getDefaultDisplayName(event.getPlayer()));
            SinkChat.setHasDisplayName(event.getPlayer(), false);
        }
    }
}
