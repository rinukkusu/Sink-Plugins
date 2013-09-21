package de.static_interface.sinklibrary;

import de.static_interface.sinkirc.SinkIRC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class BukkitUtil
{

    /**
     * @param sender Command Sender
     * @return Sender Name
     */
    public static String getSenderName(CommandSender sender)
    {
        String senderName;
        if (sender instanceof Player)
        {
            senderName = ( (Player) sender ).getDisplayName();
        }
        else
        {
            senderName = ChatColor.RED + "Console" + ChatColor.RESET;
        }
        return senderName;
    }

    /**
     * Use this instead of {@link org.bukkit.Bukkit#broadcast(String message, String permission)}.
     * Send message to all players with specified permission.
     *
     * @param message Message to send
     */

    public static void broadcastMessage(String message)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            p.sendMessage(message);
        }
        Bukkit.getConsoleSender().sendMessage(message);
        if (SinkLibrary.getSinkIRC() != null)
        {
            SinkIRC.getIRCBot().sendCleanMessage(SinkIRC.getChannel(), message);
        }
    }

    /**
     * Use this instead of {@link org.bukkit.Bukkit#broadcast(String message, String permission)}.
     * Send message to all players with specified permission.
     *
     * @param message    Message to send
     * @param permission Permission needed to receive the message
     */
    public static void broadcast(String message, String permission)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (! p.hasPermission(permission))
            {
                continue;
            }
            p.sendMessage(message);
        }
        Bukkit.getConsoleSender().sendMessage(message);
        Permission perm = new Permission(permission);
        if (perm.getDefault() == PermissionDefault.TRUE && SinkLibrary.getSinkIRC() != null)
        {
            SinkIRC.getIRCBot().sendCleanMessage(SinkIRC.getChannel(), message);
        }
    }
}
