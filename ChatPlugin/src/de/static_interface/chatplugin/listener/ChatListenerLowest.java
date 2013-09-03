package de.static_interface.chatplugin.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Used for setting chat format
 */
public class ChatListenerLowest implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        setDisplayName(event.getPlayer());
        if (event.isCancelled())
        {
            return;
        }
        PermissionUser user = PermissionsEx.getUser(event.getPlayer());
        String group = user.getGroupsNames()[0];
        String groupPrefix = ChatColor.RESET.toString() + ChatColor.GRAY + "[" + group + ChatColor.RESET + ChatColor.GRAY + "]";
        event.setFormat(groupPrefix + " %1$s" + ChatColor.GRAY + ":" + ChatColor.WHITE + " %2$s");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        setDisplayName(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        setDisplayName(event.getPlayer());
    }

    public static void setDisplayName(Player player)
    {
        PermissionUser user = PermissionsEx.getUser(player);
        String playerPrefix = ChatColor.translateAlternateColorCodes('&', user.getPrefix());
        player.setDisplayName(playerPrefix + player.getPlayerListName());
        player.setCustomName(playerPrefix + player.getPlayerListName());
    }
}
