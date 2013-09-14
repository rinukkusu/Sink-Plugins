package de.static_interface.chatplugin.channel.defaultChannels;

import de.static_interface.chatplugin.channel.Channel;
import de.static_interface.chatplugin.channel.registeredChannels;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Vector;

public class ShoutChat extends JavaPlugin implements Channel, Listener
{

    Vector<Player> exceptedPlayers = new Vector<>();
    String PREFIX = "§7[Shout]§r";
    private char callByChar = '!';

    public ShoutChat(char callChar)
    {
        registeredChannels.registerChannel(this, "Shout", PREFIX, callChar);
        callByChar = callChar;
    }

    public ShoutChat(char callChar, String prefix)
    {
        PREFIX = prefix;
        registeredChannels.registerChannel(this, "Shout", prefix, callChar);
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
        if (getServer().getPlayer(playername) == null) return;

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
        return exceptedPlayers.contains(player);
    }

    @Override
    public String getChannelName()
    {
        return "Shout";
    }

    @Override
    public String getPrefix()
    {
        return PREFIX;
    }
    public void onPlayerJoinsChannel(Channel channel, Player player)
    {
        if ( !(channel.equals(this)) ) return;


        if ( player.hasPermission(getPermission()) ) removeExceptedPlayer(player);

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

    public String getPermission()
    {
        return "chatplugin.shout";
    }

    @Override
    public char getCharForCall()
    {
        return callByChar;
    }
}
