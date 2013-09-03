package de.static_interface.commandsplugin.listener;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TradeChatListener implements Listener
{
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (event.getMessage().startsWith("$"))
        {
            String message = event.getMessage().replaceFirst("\\$", ""); //Nun öffnest du github und füllst
            String formattedMessage = event.getFormat().replace("%1$s", event.getPlayer().getDisplayName());
            formattedMessage = formattedMessage.replace("%2$s", message);
            CommandsPlugin.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GOLD + "Handel" + ChatColor.GRAY + "] " + formattedMessage);
            event.setCancelled(true);
        }
    }
}
