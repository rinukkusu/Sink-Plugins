package de.static_interface.chatplugin.listener;

import de.static_interface.chatplugin.ChatPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Used for setting chat format
 */
public class ChatListenerLowest implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        ChatPlugin.refreshDisplayName(event.getPlayer());
        if (event.isCancelled())
        {
            return;
        }

        String groupPrefix = ChatColor.RESET.toString() + ChatColor.GRAY + "[" + ChatPlugin.getGroup(event.getPlayer()) + ChatColor.RESET + ChatColor.GRAY + "]";
        event.setFormat(groupPrefix + " %1$s" + ChatColor.GRAY + ":" + ChatColor.WHITE + " %2$s");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        ChatPlugin.refreshDisplayName(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        ChatPlugin.refreshDisplayName(event.getPlayer());
    }

}
