package de.static_interface.chatplugin.channel;

import de.static_interface.chatplugin.ChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListenerLOW implements Listener {

    @EventHandler(priority= EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {

        String formattedMessage = event.getMessage();
        String PREFIX;

        switch ( event.getMessage().toCharArray()[0] )
        {
            case '?':
            {
                if ( registeredChannels.getRegisteredChannel('?').contains(event.getPlayer()) )
                {
                    return;
                }

                formattedMessage = formattedMessage.substring(1);
                PREFIX = registeredChannels.getChannelPrefix('?');
                formattedMessage = PREFIX+ChatColor.GRAY+"["+ChatColor.RESET+ChatPlugin.getGroup(event.getPlayer())+ChatColor.GRAY+"]"+event.getPlayer().getDisplayName()+ChatColor.RESET+formattedMessage;
                // [PREFIX] [GROUP] PLAYER: formattedMessage

                for ( Player target : Bukkit.getOnlinePlayers() )
                {
                    if ( !(registeredChannels.getRegisteredChannel('?').contains(target)) )
                    {
                        target.sendMessage(formattedMessage);
                    }
                }
            }

            case '!':
            {
                if ( registeredChannels.getRegisteredChannel('!').contains(event.getPlayer()) )
                {
                    return;
                }

                formattedMessage = formattedMessage.substring(1);
                PREFIX = registeredChannels.getChannelPrefix('!');

                formattedMessage = PREFIX+ChatColor.GRAY+"["+ChatColor.RESET+ChatPlugin.getGroup(event.getPlayer())+ChatColor.GRAY+"]"+event.getPlayer().getDisplayName()+ChatColor.RESET+formattedMessage;

                for ( Player target : Bukkit.getOnlinePlayers() )
                {
                    if ( !(registeredChannels.getRegisteredChannel('!').contains(target)) )
                    {
                        target.sendMessage(formattedMessage);
                    }
                }
            }

            case '$':
            {
                if ( registeredChannels.getRegisteredChannel('$').contains(event.getPlayer()) )
                {
                    return;
                }



                formattedMessage = formattedMessage.substring(1);
                PREFIX = registeredChannels.getChannelPrefix('$');

                formattedMessage = PREFIX+ChatColor.GRAY+"["+ChatColor.RESET+ChatPlugin.getGroup(event.getPlayer())+ChatColor.GRAY+"]"+event.getPlayer().getDisplayName()+ChatColor.RESET+formattedMessage;

                for ( Player target : Bukkit.getOnlinePlayers() )
                {
                    if ( !(registeredChannels.getRegisteredChannel('$').contains(target)) )
                    {
                        target.sendMessage(formattedMessage);
                    }
                }
            }
            default:
                double x = event.getPlayer().getLocation().getX();
                double y = event.getPlayer().getLocation().getY();
                double z = event.getPlayer().getLocation().getZ();

                for (Player p : Bukkit.getOnlinePlayers())
                {
                    Location loc = p.getLocation();
                    boolean isInRange = Math.abs(x - loc.getX()) <= 50 && Math.abs(y - loc.getY()) <= 50 && Math.abs(z - loc.getZ()) <= 50;

                    if (isInRange)
                    {
                        p.sendMessage(formattedMessage);
                    }
                    else if (( ( p.hasPermission("chatplugin.spynewbie") ) && ! event.getPlayer().hasPermission("chatplugin.spynewbie.bypass") ) || p.hasPermission("chatplugin.spy"))
                    {
                        p.sendMessage(ChatColor.GRAY + "[Spy] " + formattedMessage);
                    }
            }
        }
        event.setCancelled(true);
    }
}
