package de.static_interface.chatplugin.channel;

import de.static_interface.chatplugin.channel.defaultChannels.LocalChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListenerLOW implements Listener {

    @EventHandler(priority= EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {

        String formattedMessage = event.getMessage();
        String PREFIX;

        for ( char callChar : registeredChannels.getRegisteredCallChars().keySet() )
        {
            if ( callChar == (event.getMessage().toCharArray()[0]) )
            {
                registeredChannels.getRegisteredChannel(callChar).sendMessage(event.getPlayer(), event.getMessage());
                event.setCancelled(true);
                return;
            }
        }

        LocalChat.chatLocal(event.getPlayer(), event.getMessage());
        event.setCancelled(true);
    }
}