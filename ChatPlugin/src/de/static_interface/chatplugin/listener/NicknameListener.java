package de.static_interface.chatplugin.listener;

import de.static_interface.chatplugin.ChatPlugin;
import de.static_interface.commandsplugin.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static de.static_interface.chatplugin.command.NickCommand.NICKNAME_PATH;

public class NicknameListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        PlayerConfiguration config = new PlayerConfiguration(event.getPlayer().getName());
        String nick = (String) config.get(NICKNAME_PATH);
        if (nick == null || nick.equals("null") || nick.equals(""))
        {
            config.set(NICKNAME_PATH, ChatPlugin.getDefaultNickName(event.getPlayer()));
        }
    }
}
