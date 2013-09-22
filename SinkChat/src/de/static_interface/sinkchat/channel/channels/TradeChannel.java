package de.static_interface.sinkchat.channel.channels;

import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.ChannelUtil;
import de.static_interface.sinkchat.channel.IChannel;
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
        return ChannelUtil.sendMessage(player, message, this, PREFIX, callByChar);
    }

    @Override
    public void registerChannel()
    {
        ChannelHandler.registerChannel(this, "Handel", callByChar);
    }
}
