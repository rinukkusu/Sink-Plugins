package de.static_interface.commandsplugin;

import de.static_interface.commandsplugin.commands.*;
import de.static_interface.commandsplugin.listener.*;
import de.static_interface.ircplugin.IRCPlugin;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * CommandsPlugin Class
 * Author: Trojaner
 * Date: 27.07.2013
 * Description: Main Class for plugin
 * Copyright © Adventuria 2013
 */

public class CommandsPlugin extends JavaPlugin
{
    public static boolean globalmuteEnabled;
    public static List<String> tmpBannedPlayers;

    private static CommandsTimer timer;
    private static IRCPlugin ircPlugin;
    private static Economy econ;
    private static File dataFolder;
    public void onEnable()
    {
        PluginManager pm = Bukkit.getPluginManager();
        ircPlugin = (IRCPlugin) pm.getPlugin("IRCPlugin");
        Vault vault = (Vault) pm.getPlugin("Vault");
        if (vault == null || ircPlugin == null)
        {
            getLogger().log(Level.WARNING, "This plugin needs IRCPlugin and Vault to work correctly, disabling.");
            pm.disablePlugin(this);
            return;
        }
        setupEcononmy();
        timer = new CommandsTimer();
        LagTimer lagTimer = new LagTimer();
        dataFolder = getDataFolder();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, timer, 1000, 50);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, lagTimer, 60000, 60000);
        if (! getDataFolder().exists())
        {
            getDataFolder().mkdirs();
        }
        registerEvents();
        registerCommands();
        getLogger().info("Loading frozen players...");
        FreezeCommands.loadFreezedPlayers(getLogger(), getDataFolder(), this);
        getLogger().info("Done!");
        tmpBannedPlayers = new ArrayList<>();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {

            @Override
            public void run()
            {
                refreshScoreboard();
            }
        }, 0, 20 * 30); //Update every 30 seconds

    }

    public boolean setupEcononmy()
    {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private static int getMoney(Player p)
    {
        EconomyResponse response = econ.bankBalance(p.getName());
        return (int) response.balance;
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
        getLogger().info("Unloading frozen players...");
        FreezeCommands.unloadFreezedPlayers(getLogger(), getDataFolder());
        getLogger().info("Saving player configurations...");
        for (Player p : Bukkit.getOnlinePlayers())
        {
            PlayerConfiguration config = new PlayerConfiguration(p.getName());
            config.save();
        }
        getLogger().info("Disabled.");
    }

    /**
     * Refresh Scoarboard for all players
     */
    public static void refreshScoreboard()
    {
        refreshScoreboard(- 1);
    }

    /**
     * Refresh Scoreboard for all players
     * @param players Online Players
     */
    public static void refreshScoreboard(int players)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (! p.hasPermission("commandsplugin.stats"))
            {
                continue;
            }
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();
            Team team = board.registerNewTeam(p.getName());
            Objective objective = board.registerNewObjective(ChatColor.DARK_GREEN + "Statistiken", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            Score money = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Geld: "));
            money.setScore(getMoney(p));
            Score onlinePlayers = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Online: "));
            if (players >= 0)
            {
                onlinePlayers.setScore(players);
            }
            else
            {
                onlinePlayers.setScore(Bukkit.getOnlinePlayers().length);
            }

            Score date = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Leben: "));
            date.setScore((int) p.getHealth());
            team.setAllowFriendlyFire(true);
            team.setCanSeeFriendlyInvisibles(false);
            p.setScoreboard(board);
        }
    }


    /**
     * Refresh scoreboard for specified player
     *
     * @param player Refresh scoreboard for this player
     */
    public static void refreshScoreboard(Player player)
    {
        if (! player.hasPermission("commandsplugin.stats"))
        {
            return;
        }
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Team team = board.registerNewTeam(player.getName());
        Objective objective = board.registerNewObjective(ChatColor.DARK_GREEN + "Statistiken", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score money = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Geld: "));
        money.setScore(getMoney(player));
        Score onlinePlayers = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Online: "));
        onlinePlayers.setScore(Bukkit.getOnlinePlayers().length);
        Score date = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Leben: "));
        date.setScore((int) player.getHealth());
        team.setAllowFriendlyFire(true);
        team.setCanSeeFriendlyInvisibles(false);
        player.setScoreboard(board);
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
        pm.registerEvents(new GlobalMuteListener(), this);
        pm.registerEvents(new SpectateListener(), this);
        pm.registerEvents(new PlayerConfigurationListener(), this);
        pm.registerEvents(new VotekickListener(), this);
        pm.registerEvents(new DrugDeadListener(), this);
        pm.registerEvents(new ScoreboardListener(), this);
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
        getCommand("rename").setExecutor(new RenameCommand());
        getCommand("clear").setExecutor(new ClearCommand());
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
