package de.static_interface.chatplugin.listener;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListenerNormal implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        String message = event.getMessage();
        String shoutPrefix = "";
        boolean isShout = false;
        int range = 50;
        if (message.startsWith("!"))
        {
            if (! event.getPlayer().hasPermission("chatplugin.shout"))
            {
                event.getPlayer().sendMessage(ChatColor.RED + "Du hast nicht genügend Rechte um zu schreien!");
                return;
            }
            message = message.replaceFirst("!", "");
            event.setMessage(message);
            shoutPrefix = ChatColor.RESET.toString() + ChatColor.GRAY + "[Schrei] ";
            isShout = true;
        }
        event.setFormat(shoutPrefix + event.getFormat());
        String formattedMessage = event.getFormat().replace("%1$s", event.getPlayer().getDisplayName());
        formattedMessage = formattedMessage.replace("%2$s", message);
        if (event.getPlayer().hasPermission("chatplugin.color"))
        {
            formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);
        }
        String spyPrefix = ChatColor.GRAY + "[Spy] ";
        if (isShout)
        {
            CommandsPlugin.broadcastMessage(formattedMessage);
        }
        else
        {
            double x = event.getPlayer().getLocation().getX();
            double y = event.getPlayer().getLocation().getY();
            double z = event.getPlayer().getLocation().getZ();
            for (Player p : Bukkit.getOnlinePlayers())
            {
                Location loc = p.getLocation();
                boolean isInRange = Math.abs(x - loc.getX()) <= range && Math.abs(y - loc.getY()) <= range && Math.abs(z - loc.getZ()) <= range;

                if (isInRange)
                {
                    p.sendMessage(formattedMessage);
                }
                else if (( ( p.hasPermission("chatplugin.spynewbie") ) && ! event.getPlayer().hasPermission("chatplugin.spynewbie.bypass") ) || p.hasPermission("chatplugin.spy"))
                {
                    p.sendMessage(spyPrefix + formattedMessage);
                }

            }
        }
        event.setCancelled(true);
    }
}
