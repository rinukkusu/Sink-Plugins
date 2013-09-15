package de.static_interface.chatplugin.channel.defaultChannels;

import de.static_interface.chatplugin.ChatPlugin;
import de.static_interface.chatplugin.channel.Channel;
import de.static_interface.chatplugin.channel.configuration.LanguageHandler;
import de.static_interface.chatplugin.channel.registeredChannels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Vector;

public class FrageChat extends JavaPlugin implements Channel
{

    Vector<Player> exceptedPlayers = new Vector<Player>();
    String PREFIX = ChatColor.GRAY+"["+ChatColor.RED+"Frage"+ChatColor.GRAY+"]"+ChatColor.RESET;
    private char callByChar = '?';
    String permission = "chatplugin.frage";

    public FrageChat(char callChar)
    {
        registeredChannels.registerChannel(this, "Handel", PREFIX, callChar);
        callByChar = callChar;

    }

    public FrageChat(char callChar, String prefix)
    {
        PREFIX = prefix;
        registeredChannels.registerChannel(this, "Handel", prefix, callChar);
        callByChar = callChar;
    }

    public FrageChat(char callChar, String prefix, String permissionNode)
    {
        PREFIX = prefix;
        registeredChannels.registerChannel(this, "Handel", prefix, callChar);
        permission = permissionNode;

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
        if ( !(channel.equals(this)) ) return;

        if (player.hasPermission(getPermission()) && channel.equals(this))
        {
            removeExceptedPlayer(player);
        }

        String message = PREFIX+LanguageHandler.getString("message.playerJoined");
        message = message.replace("$PLAYER$", player.getDisplayName());
        message = message.replace("$CHANNEL$", channel.getChannelName());
        message = ChatColor.translateAlternateColorCodes('$', message);

        for ( Player target : getServer().getOnlinePlayers() )
        {
            if ( !(channel.getExceptedPlayers().contains(target)) )
            {
                target.sendMessage(message);
            }
        }
    }

    @Override
    public void onPlayerLeavesChannel(Channel channel, Player player) {

        if ( !(channel.equals(this)) ) return;

        this.addExceptedPlayer(player);

        String message = PREFIX+LanguageHandler.getString("message.playerLeft");
        message = message.replace("$PLAYER$", player.getName());
        message = message.replace("$CHANNEL$", channel.getChannelName());
        message = ChatColor.translateAlternateColorCodes('$', message);


        for ( Player target : getServer().getOnlinePlayers() )
        {
            if ( !(channel.getExceptedPlayers().contains(target)) )
            {
                target.sendMessage(message);
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

    @Override
    public void sendMessage(Player player, String message)
    {
        String formattedMessage = message.substring(1);
        formattedMessage = this.PREFIX+" ["+ChatPlugin.getGroup(player)+ ChatColor.RESET +"] "+ChatPlugin.getDisplayName(player)+": "+formattedMessage;
        formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);

        for ( Player target : Bukkit.getOnlinePlayers() )
        {
            if ( !(this.contains(target)) )
            {
                target.sendMessage(formattedMessage);
            }
        }
    }
}