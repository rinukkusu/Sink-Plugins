package de.static_interface.chatplugin.listener;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventPriority;

public class QuestionMarkListener implements Listener {

    public String PREFIX = "["+ChatColor.RED+"Hilfe"+ChatColor.RESET+"] ";

    @EventHandler(priority=EventPriority.LOW)
    public void onChatMessage(AsyncPlayerChatEvent event)
    {
        if ( event.getMessage().toCharArray()[0] == '?' )
        {
            String message = event.getMessage().substring(1);
            event.setMessage("");
            if ( event.getPlayer().hasPermission("chatplugin.color") )
            {
                message = ChatColor.translateAlternateColorCodes('&', message);
            }
            CommandsPlugin.broadcastMessage(PREFIX+event.getPlayer().getDisplayName()+": "+message);
            if ( !event.isCancelled() )
            {
                event.setCancelled(true);
            }
        }else
        {
            return;
        }
        event.setCancelled(true);
    }
}