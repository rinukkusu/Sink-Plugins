package de.static_interface.sinkchat.channel.channels;

import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.ChannelUtil;
import de.static_interface.sinkchat.channel.IChannel;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Vector;

public class ShoutChannel extends JavaPlugin implements IChannel, Listener
{

    Vector<Player> exceptedPlayers = new Vector<>();
    String PREFIX = ChatColor.GRAY + "[" + getChannelName() + "] " + ChatColor.RESET;
    private char callByChar = '!';

    public ShoutChannel(char callChar)
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
        return exceptedPlayers.contains(player);
    }

    @Override
    public String getChannelName()
    {
        return "Schrei";
    }

    @Override
    public boolean sendMessage(Player player, String message)
    {
        return ChannelUtil.sendMessage(player, message, this, PREFIX, callByChar);
    }

    @Override
    public void registerChannel()
    {
        ChannelHandler.registerChannel(this, getChannelName(), callByChar);
    }
}
