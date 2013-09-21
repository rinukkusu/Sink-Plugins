package de.static_interface.sinkchat.channel.channels;

import de.static_interface.sinkchat.SinkChat;
import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.IChannel;
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Vector;

public class TradeChannel extends JavaPlugin implements IChannel
{
    Vector<Player> exceptedPlayers = new Vector<>();
    private char callByChar = '$';
    String PREFIX = ChatColor.GRAY + "[" + ChatColor.GOLD + "Handel" + ChatColor.GRAY + "] " + ChatColor.RESET;

    public TradeChannel(char callChar)
    {
        callByChar = callChar;
    }

    @Override
    public void addExceptedPlayer(Player player)
    {
        exceptedPlayers.add(player);
    }

    @Override
    public void removeExceptedPlayer(Player player)
    {
        exceptedPlayers.remove(player);
    }


    @Override
    public boolean contains(Player player)
    {
        return ( exceptedPlayers.contains(player) );
    }

    @Override
    public String getChannelName()
    {
        return "Handel";
    }

    @Override
    public boolean sendMessage(Player player, String message)
    {
        if (contains(player))
        {
            return false;
        }
        if (message.toCharArray().length == 1 && message.toCharArray()[0] == callByChar)
        {
            return false;
        }
        String formattedMessage = message.substring(1);
        formattedMessage = PREFIX + ChatColor.GRAY + "[" + SinkChat.getGroup(player) + ChatColor.GRAY + "] " + SinkChat.getDisplayName(player) + ChatColor.GRAY + ": " + ChatColor.RESET + formattedMessage;
        if (player.hasPermission("sinkchat.color"))
        {
            formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);
        }

        for (Player target : Bukkit.getOnlinePlayers())
        {
            if (! ( contains(target) ))
            {
                target.sendMessage(formattedMessage);
            }
        }
        SinkLibrary.sendIRCMessage(formattedMessage);
        return true;
    }

    @Override
    public void registerChannel()
    {
        ChannelHandler.registerChannel(this, "Handel", callByChar);
    }
}
