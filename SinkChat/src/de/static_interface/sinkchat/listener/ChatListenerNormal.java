package de.static_interface.sinkchat.listener;

import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
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

        for (char callChar : ChannelHandler.getRegisteredCallChars().keySet())
        {
            if (callChar == ( event.getMessage().toCharArray()[0] )
                    && ChannelHandler.getRegisteredChannel(callChar).sendMessage(event.getPlayer(), event.getMessage()))
            {
                event.setCancelled(true);
                return;
            }
        }

        String message = event.getMessage();
        int range = 50;
        String formattedMessage = event.getFormat().replace("%1$s", event.getPlayer().getDisplayName());
        formattedMessage = formattedMessage.replace("%2$s", message);
        if (event.getPlayer().hasPermission("sinkchat.color"))
        {
            formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);
        }
        String spyPrefix = ChatColor.GRAY + "[Spy] ";
        double x = event.getPlayer().getLocation().getX();
        double y = event.getPlayer().getLocation().getY();
        double z = event.getPlayer().getLocation().getZ();
        for (Player p : Bukkit.getOnlinePlayers())
        {
            Location loc = p.getLocation();
            boolean isInRange = Math.abs(x - loc.getX()) <= range && Math.abs(y - loc.getY()) <= range && Math.abs(z - loc.getZ()) <= range;

            User user = new User(p);
            PlayerConfiguration config = user.getPlayerConfiguration();
            if (isInRange)
            {
                p.sendMessage(formattedMessage);
            }
            else if (( ( ( p.hasPermission("sinkchat.spynewbie") ) && ! event.getPlayer().hasPermission("sinkchat.spynewbie.bypass") )
                    || p.hasPermission("sinkchat.spy") ) && config.getSpyEnabled())
            {
                p.sendMessage(spyPrefix + formattedMessage);
            }

        }
        event.setCancelled(true);
    }
}
