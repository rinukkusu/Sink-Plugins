package de.static_interface.sinkirc;

import de.static_interface.sinkirc.commands.IrclistCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jibble.pircbot.IrcException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SinkIRC
 * Description: Main Class
 * Date: 21.07.2013
 * Author: Trojaner
 * Copyright © 2013 Trojaner
 */

@SuppressWarnings({ "FieldCanBeLocal" })
public class SinkIRC extends JavaPlugin implements Listener
{
    private String host = "irc.adventuria.eu";
    private static String channel = "#AdventuriaBot";
    private int port = 6667;

    static IRCBot ircBot;
    Logger log;

    @Override
    public void onEnable()
    {
        ircBot = new IRCBot();

        if (Bukkit.getPluginManager().getPlugin("SinkChat") == null)
        {
            Bukkit.getLogger().log(Level.WARNING, "This plugin will not work without SinkChat.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);

        log = getLogger();

        try
        {
            ircBot.connect(host, port);
            ircBot.sendMessage("Trojaner", "register passwordpassword password@password.com"); //dis is testi!
            ircBot.sendMessage("Trojaner", "identify passwordpassword"); //dis is also testi!
            ircBot.joinChannel(channel);
        }
        catch (IOException | IrcException e)
        {
            log.severe("An Exception occurred while trying to connect to " + host + ":");
            log.severe(e.toString());
        }
        getCommand("irclist").setExecutor(new IrclistCommand());
    }

    @Override
    public void onDisable()
    {
        ircBot.quitServer("Plugin reload or plugin has been deactivated");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(channel, event.getJoinMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(channel, event.getQuitMessage());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        if (! event.getReason().equals(""))
        {
            ircBot.sendCleanMessage(channel, "Player \"" + event.getPlayer().getDisplayName() + "\" has been kicked for reason: " + event.getReason());
        }
        else
        {
            ircBot.sendCleanMessage(channel, "Player \"" + event.getPlayer().getDisplayName() + "\" has been kicked!");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(channel, event.getDeathMessage());
    }

    /*

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        String tmp = event.getMessage().replaceFirst("/", "");
        String[] cmdwithargs = tmp.split(" ");
        List<String> args = Arrays.asList(cmdwithargs);
        String cmd = cmdwithargs[0];
        switch (cmd.toLowerCase())
        {
            case "say":
            {
                String message = "";
                boolean commandSkip = true;
                for (String s : args)
                {
                    if (commandSkip)
                    {
                        commandSkip = false;
                        continue;
                    }
                    if (message.equals(""))
                    {
                        message = s;
                    }
                    else
                    {
                        message = message + " " + s;
                    }
                }
                ircBot.sendCleanMessage(channel, IRCBot.replaceColorCodes(IRCBot.replaceAmpersandColorCodes(ChatColor.DARK_PURPLE + "[Server] " + message)));
            }
        }

    }

    */
    public static IRCBot getIRCBot()
    {
        return ircBot;
    }

    public static String getChannel()
    {
        return channel;
    }
}
