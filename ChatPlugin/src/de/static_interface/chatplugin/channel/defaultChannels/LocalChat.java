package de.static_interface.chatplugin.channel.defaultChannels;

import de.static_interface.chatplugin.ChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocalChat {

    public static void chatLocal(Player player, String message)
    {
        String formattedMessage = "[" + ChatPlugin.getGroup(player)+ ChatColor.RESET +"] " + ChatPlugin.getDisplayName(player)+ ": " + message;
        formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();

        for (Player p : Bukkit.getOnlinePlayers())
        {
            Location loc = p.getLocation();
            boolean isInRange = Math.abs(x - loc.getX()) <= 50 && Math.abs(y - loc.getY()) <= 50 && Math.abs(z - loc.getZ()) <= 50;
            if (isInRange)
            {
                p.sendMessage(formattedMessage);
            }
            else if (( ( p.hasPermission("chatplugin.spynewbie") ) && ! player.hasPermission("chatplugin.spynewbie.bypass") ) || p.hasPermission("chatplugin.spy"))
            {
                p.sendMessage(ChatColor.GRAY + "[Spy] " + formattedMessage);
            }
        }
    }
}
