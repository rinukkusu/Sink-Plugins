package de.static_interface.sinkchat;

import de.static_interface.sinkchat.channel.IChannel;
import de.static_interface.sinkchat.channel.channels.HelpChannel;
import de.static_interface.sinkchat.channel.channels.ShoutChannel;
import de.static_interface.sinkchat.channel.channels.TradeChannel;
import de.static_interface.sinkchat.channel.configuration.LanguageHandler;
import de.static_interface.sinkchat.command.ChannelCommand;
import de.static_interface.sinkchat.command.NickCommand;
import de.static_interface.sinkchat.command.SpyCommands;
import de.static_interface.sinkchat.listener.ChatListenerLowest;
import de.static_interface.sinkchat.listener.ChatListenerNormal;
import de.static_interface.sinkchat.listener.NicknameListener;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * SinkChat Class
 * Author: Trojaner
 * Date: 23.09.2013
 * Description: Main Class for plugin
 * Copyright © Adventuria 2013
 */

public class SinkChat extends JavaPlugin
{
    public static File dataFolder;

    public void onEnable()
    {
        if (! checkDependencies())
        {
            return;
        }


        dataFolder = getDataFolder();
        for (Player p : Bukkit.getOnlinePlayers())
        {
            refreshDisplayName(p);
        }

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


        registerEvents(Bukkit.getPluginManager());
        registerCommands();
    }

    public void onDisable()
    {
        Bukkit.getLogger().log(Level.INFO, "Disabled.");
    }

    private boolean checkDependencies()
    {

        PluginManager pm = Bukkit.getPluginManager();
        SinkLibrary sinkLibrary;

        try
        {
            sinkLibrary = (SinkLibrary) pm.getPlugin("SinkLibrary");
        }
        catch (NoClassDefFoundError ignored)
        {
            sinkLibrary = null;
        }
        if (sinkLibrary == null)
        {
            getLogger().log(Level.WARNING, "This Plugin requires SinkLibrary!");
            pm.disablePlugin(this);
            return false;
        }

        return true;
    }

    /**
     * Get Data Folder
     *
     * @return Data Folder
     */
    public static String getDataFolderStatic()
    {
        return String.valueOf(dataFolder);
    }

    /**
     * @param player Player
     * @return Player's primary group
     */
    public static String getGroup(Player player)
    {
        User user = new User(player);
        PlayerConfiguration config = user.getPlayerConfiguration();
        return config.getGroups()[0];
    }

    /**
     * Refresh Player DisplayName
     *
     * @param player Player
     */
    public static void refreshDisplayName(Player player)
    {
        String nickname;
        User user = new User(player);
        PlayerConfiguration config = user.getPlayerConfiguration();

        if (config.getHasDisplayName())
        {
            nickname = config.getDisplayName();
            if (nickname.equals(getDefaultDisplayName(player)))
            {
                config.setHasDisplayName(false);
            }
        }
        else
        {
            nickname = getDefaultDisplayName(player);
        }
        player.setDisplayName(nickname);
        player.setCustomName(nickname);
    }

    /**
     * @param player Player
     * @return Default DisplayName of Player
     */
    public static String getDefaultDisplayName(Player player)
    {
        User user = new User(player);
        PlayerConfiguration config = user.getPlayerConfiguration();
        return config.getDefaultDisplayName();
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
        getCommand("channel").setExecutor(new ChannelCommand());
        getCommand("enablespy").setExecutor(new SpyCommands.EnableSpyCommand());
        getCommand("disablespy").setExecutor(new SpyCommands.DisablSpyCommand());
    }
}
