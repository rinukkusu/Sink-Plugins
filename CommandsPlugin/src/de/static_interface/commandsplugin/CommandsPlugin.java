package de.static_interface.commandsplugin;

import de.static_interface.commandsplugin.commands.*;
import de.static_interface.commandsplugin.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CommandsPlugin Class
 * <p/>
 * Author: Trojaner
 * Date: 27.07.13
 * Description: Main Class for plugin
 * Copyright © Trojaner 2013
 */

public class CommandsPlugin extends JavaPlugin
{
    private static Logger log;
    public static boolean globalmuteEnabled = false;
    private static CommandsTimer timer;
    private static File dataFolder;

    public void onEnable()
    {
        timer = new CommandsTimer();
        LagTimer lagTimer = new LagTimer();
        dataFolder = getDataFolder();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, timer, 1000, 50);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, lagTimer, 60000, 60000);
        log = getLogger();
        if (! getDataFolder().exists())
        {
            getDataFolder().mkdirs();
        }
        registerEvents();
        registerCommands();
        log.info("Loading freezed players...");
        FreezeCommands.loadFreezedPlayers(log, getDataFolder(), this);
        log.info("Done!");
    }

    public void onDisable()
    {
        for (Player p : SpectateCommands.specedPlayers.keySet())
        {
            Player target = SpectateCommands.specedPlayers.get(p);
            target.eject();
            SpectateCommands.show(p);
            SpectateCommands.specedPlayers.remove(p);
            p.sendMessage(SpectateCommands.prefix + "Du wurdest durch einem Reload gezwungen den Spectate Modus zu verlassen.");
        }
        log.info("Unloading freezed players...");
        FreezeCommands.unloadFreezedPlayers(log, getDataFolder());
        log.info("Disabled.");
    }

    /**
     * Get Data Folder
     *
     * @return Data Folder of this plugin
     */
    public static File getDataFolderStatic()
    {
        return dataFolder;
    }

    /**
     * Use this instead of {@link org.bukkit.Bukkit#broadcast(String message, String permission)}.
     *
     * @param message    Message to send
     * @param permission Send message to players with this permission
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
        log.log(Level.INFO, message);
    }

    /**
     * Get CommandsTimer
     *
     * @return CommandsTimer
     */
    public static CommandsTimer getCommandsTimer()
    {
        return timer;
    }

    private void registerEvents()
    {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new FreezeListener(), this);
        pm.registerEvents(new GlobalmuteListener(), this);
        pm.registerEvents(new TradechatListener(), this);
        pm.registerEvents(new SpectateListener(), this);
        pm.registerEvents(new PlayerConfigurationListener(), this);
    }

    private void registerCommands()
    {
        getCommand("commandsver").setExecutor(new CommandsverCommand(this));
        getCommand("drug").setExecutor(new DrugCommand());
        getCommand("milk").setExecutor(new MilkCommand());
        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("freeze").setExecutor(new FreezeCommands.FreezeCommand());
        getCommand("tmpfreeze").setExecutor(new FreezeCommands.TmpfreezeCommand(this));
        getCommand("freezeall").setExecutor(new FreezeCommands.FreezeallCommand());
        getCommand("freezelist").setExecutor(new FreezeCommands.FreezelistCommand());
        getCommand("globalmute").setExecutor(new GlobalmuteCommand());
        getCommand("teamchat").setExecutor(new TeamchatCommand());
        getCommand("newbiechat").setExecutor(new NewbiechatCommand());
        getCommand("spectate").setExecutor(new SpectateCommands.SpectateCommand());
        getCommand("unspectate").setExecutor(new SpectateCommands.UnspectateCommand());
        getCommand("spectatorlist").setExecutor(new SpectateCommands.SpectatorlistCommand());
        getCommand("lag").setExecutor(new LagCommand());
    }
}
