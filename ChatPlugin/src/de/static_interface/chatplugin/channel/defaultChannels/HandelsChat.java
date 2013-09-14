package de.static_interface.chatplugin.channel.defaultChannels;

import de.static_interface.chatplugin.channel.Channel;
import de.static_interface.chatplugin.channel.registeredChannels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Vector;

public class HandelsChat extends JavaPlugin implements Channel
{

    Vector<Player> exceptedPlayers = new Vector<>();
    private char callByChar = '$';
    String PREFIX = ChatColor.GRAY+"["+ChatColor.GOLD+"§6Handel"+ChatColor.GRAY+"]"+ChatColor.RESET;

    public HandelsChat(char callChar)
    {
        registeredChannels.registerChannel(this, "Handel", PREFIX, callChar);
        callByChar = callChar;

    }

    public HandelsChat(char callChar, String prefix)
    {
        PREFIX = prefix;
        registeredChannels.registerChannel(this, "Handel", prefix, callChar);
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
    public void removeExceptedPlayer(String playername)
    {
        if (getServer().getPlayer(playername) == null)
        {
            return;
        }
        exceptedPlayers.remove(getServer().getPlayer(playername));
    }


    @Override
    public Vector<Player> getExceptedPlayers()
    {
        return exceptedPlayers;
    }

    @Override
    public boolean contains(Player player)
    {
        return (exceptedPlayers.contains(player));
    }

    @Override
    public String getChannelName()
    {
        return "Handel";
    }

    public void onPlayerJoinsChannel(Channel channel, Player player)
    {
        if ( !(channel.equals(this)) )
        {
            return;
        }


        if (player.hasPermission(getPermission()) && channel.equals(this))
        {
            addExceptedPlayer(player);
        }

        for ( Player target : getServer().getOnlinePlayers() )
        {
            if ( !(channel.getExceptedPlayers().contains(target)) )
            {
                target.sendMessage(PREFIX+player.getDisplayName()+" ist dem Channel "+channel.getChannelName()+" beigetreten !");
            }
        }
    }

    @Override
    public void onPlayerLeavesChannel(Channel channel, Player player) {

        if ( !(channel.equals(this)) ) return;

        this.addExceptedPlayer(player);

        for ( Player target : getServer().getOnlinePlayers() )
        {
            if ( !(channel.getExceptedPlayers().contains(target)) )
            {
                target.sendMessage(PREFIX+player.getDisplayName()+" verließ den Channel "+channel.getChannelName()+".");
            }
        }
    }

    @Override
    public String getPrefix()
    {
        return PREFIX;
    }

    public String getPermission()
    {
        return "chatplugin.handel";
    }

    @Override
    public char getCharForCall()
    {
        return callByChar;
    }

}
