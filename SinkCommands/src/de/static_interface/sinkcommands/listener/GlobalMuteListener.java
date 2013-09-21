package de.static_interface.sinkcommands.listener;

import de.static_interface.sinkcommands.SinkCommands;
import de.static_interface.sinkcommands.commands.GlobalmuteCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GlobalMuteListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (SinkCommands.globalmuteEnabled && ! event.getPlayer().hasPermission("sinkcommands.globalmute.bypass"))
        {
            event.getPlayer().sendMessage(GlobalmuteCommand.PREFIX + "Du kannst nicht schreiben wenn der globale Mute aktiviert ist.");
            event.setCancelled(true);
        }
    }
}
