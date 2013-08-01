package de.static_interface.commandsplugin;

import de.static_interface.commandsplugin.commands.*;
import de.static_interface.commandsplugin.listener.FreezeListener;
import de.static_interface.commandsplugin.listener.GlobalMuteListener;
import de.static_interface.commandsplugin.listener.SpecListener;
import de.static_interface.commandsplugin.listener.TradeChatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CommandsPlugin Class
 *
 * Author: Trojaner
 * Date: 27.07.13
 * Description: Main Class
 * Copyright © Trojaner 2013
 */

public class CommandsPlugin extends JavaPlugin
{
    Logger log;
    public static boolean globalmuteEnabled = false;
    public static Set<String> toFreeze = new HashSet<String>();
    private ArrayList<String[]> toTmpFreeze = new ArrayList<String[]>();
    public static Set<String> freezeAll = new HashSet<String>();

    public void onEnable()
    {
        log = getLogger();
        if (!getDataFolder().exists())
        {
            getDataFolder().mkdirs();
        }
        File saveFile = new File(getDataFolder(), "freezedPlayers.txt");
        if (saveFile.exists())
        {
            try
            {
                InputStream ips = new FileInputStream(saveFile);
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);
                String line;
                while (( line = br.readLine() ) != null)
                {
                    toFreeze.add(line.trim());
                }
            }
            catch (Exception exc)
            {
                log.log(Level.SEVERE, "Fehler beim laden der freezedPlayers.txt ", exc);
            }
        }
        saveFile = new File(getDataFolder(), "freezedTmpPlayers.txt");
        if (saveFile.exists())
        {
            try
            {
                InputStream ips = new FileInputStream(saveFile);
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);
                String line;
                while (( line = br.readLine() ) != null)
                {
                    Pattern p = Pattern.compile("(.*):(.*);");
                    Matcher m = p.matcher(line);
                    while (m.find())
                    {
                        String array[] = { m.group(1).trim(), m.group(2).trim() };
                        final String playerName = m.group(1).trim();
                        final long time = Long.parseLong(m.group(2).trim());
                        final int IDListArray = toTmpFreeze.size();
                        if (Bukkit.getOfflinePlayer(playerName) != null)
                        {
                            toTmpFreeze.add(IDListArray, array);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);
                                    if (player != null)
                                    {
                                        unfreeze(player);
                                        toTmpFreeze.remove(IDListArray);
                                        Bukkit.getPlayer(player.getName()).sendMessage("Du wurdest nach " + time + " Sekunden ungefreezt.");
                                    }
                                }
                            }, time * 20);
                            if (! toFreeze.contains(playerName))
                            {
                                toFreeze.add(playerName);
                            }
                        }
                    }
                }
            }
            catch (Exception exc)
            {
                log.log(Level.SEVERE, "Fehler beim laden der freezedTmpPlayers.txt ", exc);
            }
            registerEvents();
            registerCommands();

        }

    }

    private void registerEvents()
    {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new FreezeListener(this), this);
        pm.registerEvents(new GlobalMuteListener(), this);
        pm.registerEvents(new TradeChatListener(), this);
        pm.registerEvents(new SpecListener(), this);
    }

    private void registerCommands()
    {
        getCommand("commandsver").setExecutor(new CommandsverCommand(this));
        getCommand("drug").setExecutor(new DrugCommand());
        getCommand("milk").setExecutor(new MilkCommand());
        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("freeze").setExecutor(new FreezeCommands.FreezeCommand(this));
        getCommand("tmpfreeze").setExecutor(new FreezeCommands.TmpFreezeCommand(this));
        getCommand("freezeall").setExecutor(new FreezeCommands.FreezeAllCommand(this));
        getCommand("freezelist").setExecutor(new FreezeCommands.FreezeListCommand(this));
        getCommand("globalmute").setExecutor(new GlobalmuteCommand());
        getCommand("teamchat").setExecutor(new TeamchatCommand());
        getCommand("newbiechat").setExecutor(new NewbiechatCommand());
        getCommand("spec").setExecutor(new SpecCommands.SpecCommand());
        getCommand("unspec").setExecutor(new SpecCommands.UnspecCommand());
        getCommand("speclist").setExecutor(new SpecCommands.SpeclistCommand());
    }

    public static void broadcast(String message, String permission)
    {
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if (!p.hasPermission(permission)) continue;
            p.sendMessage(message);
        }
        Bukkit.getLogger().log(Level.INFO, message);
    }

    public boolean canBeFrozen(Player player)
    {
        return ! player.hasPermission("commandsplugin.freeze.never");
    }

    public boolean isFrozen(Player player)
    {
        if (player == null)
        {
            return false;
        }

        if (freezeAll.contains(player.getWorld().getName()))
        {
            return canBeFrozen(player);
        }

        return toFreeze.contains(player.getName());
    }

    public int isTmpFrozen(Player player)
    {
        if (player == null)
        {
            return - 1;
        }

        for (String[] string : toTmpFreeze)
        {
            if (player.getName().equalsIgnoreCase(string[0]))
            {
                return Integer.parseInt(string[1]);
            }
        }

        return 0;
    }

    public boolean toggleFreezeAll()
    {
        if (freezeAll.size() == Bukkit.getWorlds().size())
        {
            freezeAll.clear();
            return false;
        }

        for (World w : Bukkit.getWorlds())
        {
            if (! freezeAll.contains(w.getName()))
            {
                freezeAll.add(w.getName());
            }
        }

        return true;
    }

    public boolean toggleFreeze(Player player)
    {
        if (toTmpFreeze.contains(player.getName()))
        {
            toTmpFreeze.remove(player.getName());
        }
        if (toFreeze.contains(player.getName()))
        {
            toFreeze.remove(player.getName());

            return false;
        }
        else
        {
            toFreeze.add(player.getName());
            return true;
        }
    }

    public void unfreeze(Player player)
    {
        if (toFreeze.contains(player.getName()))
        {
            toFreeze.remove(player.getName());
        }
        if (toTmpFreeze.contains(player.getName()))
        {
            toTmpFreeze.remove(player.getName());
        }
    }

    public void unfreeze(OfflinePlayer player)
    {
        if (toFreeze.contains(player.getName()))
        {
            toFreeze.remove(player.getName());
        }
        if (toTmpFreeze.contains(player.getName()))
        {
            toTmpFreeze.remove(player.getName());
        }
    }

    public boolean temporarilyFreeze(final Player player, final int seconds)
    {
        if (! toFreeze.contains(player.getName()) && ! toTmpFreeze.contains(player.getName()))
        {
            toFreeze.add(player.getName());
            String[] array = { player.getName(), String.valueOf(seconds) };
            final int IDListArray = toTmpFreeze.size();
            toTmpFreeze.add(IDListArray, array);
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
            {
                @Override
                public void run()
                {
                    if(!toTmpFreeze.contains(player.getName()))
                    {
                        return;
                    }
                    unfreeze(player);
                    toTmpFreeze.remove(IDListArray);
                    player.sendMessage(ChatColor.RED + "[Freeze]" + ChatColor.WHITE + " Du wurdest nach " + seconds + " Sekunden ungefreezt.");
                }
            }, seconds * 20);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void onDisable()
    {
        if (!getDataFolder().exists())
        {
            getDataFolder().mkdirs();
        }
        File saveFile = new File(getDataFolder(), "freezedPlayers.txt");
        if (saveFile.exists())
        {
            saveFile.delete();
        }

        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(saveFile));

            for (String freezedPlayer : toFreeze)
            {
                if (! toTmpFreeze.contains(freezedPlayer))
                {
                    out.write(freezedPlayer + "\n");
                }
            }

            out.close();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "Fehler beim speichern der freezedPlayers.txt ", e);
        }

        saveFile = new File(getDataFolder(), "freezedTmpPlayers.txt");
        if (saveFile.exists())
        {
            saveFile.delete();
        }

        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(saveFile));

            for (String[] array : toTmpFreeze)
            {
                out.write(array[0] + ":" + array[1] + ";\n");
            }

            out.close();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "Fehler beim speichern der freezedPlayers.txt ", e);
        }

        for (Player p : SpecCommands.specedPlayers.keySet())
        {
            Player target = SpecCommands.specedPlayers.get(p);
            target.eject();
            SpecCommands.show(p);
            SpecCommands.specedPlayers.remove(p);
            p.sendMessage(SpecCommands.prefix + "Du wurdest durch einem Reload gezwungen den Spectate Modus zu verlassen.");
        }

        log.info("Disabled.");
    }
}
