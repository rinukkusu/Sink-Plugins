package de.static_interface.chatplugin.channel.defaultChannels;

import de.static_interface.chatplugin.channel.Channel;
import de.static_interface.chatplugin.channel.registeredChannels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Vector;

public class FrageChat extends JavaPlugin implements Channel
{

    Vector<Player> exceptedPlayers = new Vector<Player>();
    String PREFIX = ChatColor.GRAY+"["+ChatColor.RED+"Hilfe"+ChatColor.GRAY+"]"+ChatColor.RESET;
    private char callByChar = '?';

    public FrageChat(char callChar) {
        registeredChannels.registerChannel(this, "Frage", PREFIX, callChar);
        callByChar = callChar;
    }

    public FrageChat(char callChar, String prefix, String channelname) {
        PREFIX = prefix;
        registeredChannels.registerChannel(this, channelname, prefix, callChar);
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
    public void removeExceptedPlayer(String name)
    {
        if (getServer().getPlayer(name) == null)
        {
            return;
        }

        exceptedPlayers.remove(getServer().getPlayer(name));

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
        return "Frage";
    }

    @Override
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
        return "chatplugin.frage";
    }

    @Override
    public char getCharForCall()
    {
        return callByChar;
    }

}
