package de.static_interface.sinkchat;

import de.static_interface.sinkchat.channel.IChannel;
import de.static_interface.sinkchat.channel.channels.HelpChannel;
import de.static_interface.sinkchat.channel.channels.ShoutChannel;
import de.static_interface.sinkchat.channel.channels.TradeChannel;
import de.static_interface.sinkchat.channel.configuration.LanguageHandler;
import de.static_interface.sinkchat.command.ChannelCommand;
import de.static_interface.sinkchat.command.NickCommand;
import de.static_interface.sinkchat.listener.ChatListenerLowest;
import de.static_interface.sinkchat.listener.ChatListenerNormal;
import de.static_interface.sinkchat.listener.NicknameListener;
import de.static_interface.sinkcommands.PlayerConfiguration;
import de.static_interface.sinkcommands.SinkCommands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.logging.Level;

import static de.static_interface.sinkchat.command.NickCommand.HAS_NICKNAME_PATH;
import static de.static_interface.sinkchat.command.NickCommand.NICKNAME_PATH;

/**
 * SinkChat Class
 * Author: Trojaner
 * Date: 23.09.2013
 * Description: Main Class for plugin
 * Copyright © Adventuria 2013
 */

public class SinkChat extends JavaPlugin
{
    public void onEnable()
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            refreshDisplayName(p);
        }
        PluginManager pm = Bukkit.getPluginManager();
        PermissionsEx pex = (PermissionsEx) pm.getPlugin("PermissionsEx");

        SinkCommands sinkCommands = (SinkCommands) pm.getPlugin("SinkCommands");
        if (pex == null)
        {
            Bukkit.getLogger().log(Level.WARNING, "This Plugin needs PermissionsEx to work correctly");
            return;
        }
        if (sinkCommands == null)
        {
            Bukkit.getLogger().log(Level.WARNING, "This Plugin needs SinkCommands to work correctly");
            return;
        }

        //Registering channels. Important: argument is a char, not a String !
        IChannel sc = new ShoutChannel('!');
        IChannel hc = new TradeChannel('$');
        IChannel fc = new HelpChannel('?');

        sc.registerChannel();
        hc.registerChannel();
        fc.registerChannel();

        if (! ( LanguageHandler.init() ))
        {
            getLogger().severe("I/O-Exception occured. Could not load language file ");
        }
        else
        {
            getLogger().info("Loading language files succeeded. Proceeding.");
        }


        registerEvents(pm);
        registerCommands();
    }

    public void onDisable()
    {
        Bukkit.getLogger().log(Level.INFO, "Disabled.");
    }

    public static String getGroup(Player player)
    {
        PermissionUser user = PermissionsEx.getUser(player);
        return user.getGroupsNames()[0];
    }


    public static void setDisplayName(Player player, String newNickname)
    {
        player.setDisplayName(newNickname);
        player.setCustomName(newNickname);
        PlayerConfiguration config = new PlayerConfiguration(player.getName());
        config.set(NICKNAME_PATH, newNickname);
        if (newNickname.equals(getDefaultDisplayName(player)))
        {
            setHasDisplayName(player, false);
        }
        else
        {
            setHasDisplayName(player, true);
        }
    }

    public static String getDisplayName(Player player)
    {
        PlayerConfiguration config = new PlayerConfiguration(player.getName());
        return config.getString(NICKNAME_PATH);
    }

    public static String getDefaultDisplayName(Player player)
    {
        PermissionUser user = PermissionsEx.getUser(player);
        String playerPrefix = ChatColor.translateAlternateColorCodes('&', user.getPrefix());
        return playerPrefix + player.getPlayerListName() + ChatColor.RESET;
    }

    public static void refreshDisplayName(Player player)
    {
        String nickname;
        if (getHasDisplayName(player))
        {
            nickname = getDisplayName(player);
            if (nickname.equals(getDefaultDisplayName(player)))
            {
                setHasDisplayName(player, false);
            }
        }
        else
        {
            nickname = getDefaultDisplayName(player);
        }
        player.setDisplayName(nickname);
        player.setCustomName(nickname);
    }

    public static boolean getHasDisplayName(Player player)
    {
        PlayerConfiguration config = new PlayerConfiguration(player.getName());
        return config.getBoolean(HAS_NICKNAME_PATH);
    }

    public static void setHasDisplayName(Player player, boolean value)
    {
        PlayerConfiguration config = new PlayerConfiguration(player.getName());
        config.set(HAS_NICKNAME_PATH, value);
    }

    private void registerEvents(PluginManager pm)
    {
        pm.registerEvents(new NicknameListener(), this);
        pm.registerEvents(new ChatListenerLowest(), this);
        pm.registerEvents(new ChatListenerNormal(), this);
    }

    private void registerCommands()
    {
        getCommand("nick").setExecutor(new NickCommand());
        getCommand("ch").setExecutor(new ChannelCommand());
    }
}
