package de.static_interface.commandsplugin;

import de.static_interface.commandsplugin.commands.*;
import de.static_interface.commandsplugin.listener.*;
import de.static_interface.ircplugin.IRCPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * CommandsPlugin Class
 * Author: Trojaner
 * Date: 27.07.13
 * Description: Main Class for plugin
 * Copyright © Adventuria 2013
 */

public class CommandsPlugin extends JavaPlugin
{
    public static boolean globalmuteEnabled;
    public static List<String> tmpBannedPlayers;

    private static Logger log;
    private static CommandsTimer timer;
    private static File dataFolder;

    static IRCPlugin ircPlugin;

    public void onEnable()
    {
        timer = new CommandsTimer();
        LagTimer lagTimer = new LagTimer();
        PluginManager pm = Bukkit.getPluginManager();
        ircPlugin = (IRCPlugin) pm.getPlugin("IRCPlugin");
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
        log.info("Loading frozen players...");
        FreezeCommands.loadFreezedPlayers(log, getDataFolder(), this);
        log.info("Done!");
        tmpBannedPlayers = new ArrayList<>();
    }

    public void onDisable()
    {
        for (Player p : SpectateCommands.specedPlayers.keySet())
        {
            Player target = SpectateCommands.specedPlayers.get(p);
            target.eject();
            SpectateCommands.show(p);
            SpectateCommands.specedPlayers.remove(p);
            p.sendMessage(SpectateCommands.PREFIX + "Du wurdest durch einen Reload gezwungen den Spectate Modus zu verlassen.");
        }
        log.info("Unloading frozen players...");
        FreezeCommands.unloadFreezedPlayers(log, getDataFolder());
        log.info("Saving player configurations...");
        for (Player p : Bukkit.getOnlinePlayers())
        {
            PlayerConfiguration config = new PlayerConfiguration(p.getName());
            config.save();
        }
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
     * Send message to all players with specified permission.
     *
     * @param message Message to send
     */

    public static void broadcastMessage(String message)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            p.sendMessage(message);
        }
        Bukkit.getConsoleSender().sendMessage(message);

        if (ircPlugin != null)
        {
            IRCPlugin.getIRCBot().sendCleanMessage(IRCPlugin.getChannel(), message);
        }
    }

    /**
     * Use this instead of {@link org.bukkit.Bukkit#broadcast(String message, String permission)}.
     * Send message to all players with specified permission.
     *
     * @param message    Message to send
     * @param permission Permission needed to receive the message
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
        Bukkit.getConsoleSender().sendMessage(message);
        Permission perm = new Permission(permission);
        if (perm.getDefault() == PermissionDefault.TRUE && ircPlugin != null)
        {
            IRCPlugin.getIRCBot().sendCleanMessage(IRCPlugin.getChannel(), message);
        }
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
        pm.registerEvents(new PlayerconfigurationListener(), this);
        pm.registerEvents(new VotekickListener(), this);
        pm.registerEvents(new DrugDeadListener(), this);
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
        getCommand("votekick").setExecutor(new VotekickCommands.VotekickCommand(this));
        getCommand("voteyes").setExecutor(new VotekickCommands.VoteyesCommand(this));
        getCommand("voteno").setExecutor(new VotekickCommands.VotenoCommand(this));
        getCommand("votestatus").setExecutor(new VotekickCommands.VotestatusCommand());
        getCommand("endvote").setExecutor(new VotekickCommands.EndvoteCommand(this));
        getCommand("votekickunban").setExecutor(new VotekickCommands.VotekickunbanCommand());
        getCommand("commandsdebug").setExecutor(new CommandsdebugCommand());
    }

    public static String getSenderName(CommandSender sender)
    {
        String senderName;
        if (sender instanceof Player)
        {
            senderName = ( (Player) sender ).getDisplayName();
        }
        else
        {
            senderName = ChatColor.RED + "Console";
        }
        return senderName;
    }

    /**
     * Add Temp Ban
     *
     * @param username Player to ban
     */
    public static void addTempBan(String username)
    {
        tmpBannedPlayers.add(username);
    }

    /**
     * Remove Temp Ban
     *
     * @param username Player to unban
     */
    public static void removeTempBan(String username)
    {
        tmpBannedPlayers.remove(username);
    }
}
