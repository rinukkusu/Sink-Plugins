package de.static_interface.chatplugin.listener;

import de.static_interface.chatplugin.ChatPlugin;
import de.static_interface.commandsplugin.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static de.static_interface.chatplugin.command.NickCommand.HAS_NICKNAME_PATH;
import static de.static_interface.chatplugin.command.NickCommand.NICKNAME_PATH;

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
            ChatPlugin.setDisplayName(event.getPlayer(), ChatPlugin.getDefaultDisplayName(event.getPlayer()));
            ChatPlugin.setHasDisplayName(event.getPlayer(), false);
            //ToDo: add: "addDefault" to PlayerConfiguration"
        }
    }
}
