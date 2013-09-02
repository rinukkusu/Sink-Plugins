package de.static_interface.ircplugin;

import de.static_interface.ircplugin.commands.IrclistCommand;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jibble.pircbot.IrcException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * IRCPluginMain
 * Description: Main Class
 * Date: 21.07.13
 * Author: Trojaner
 * Copyright © 2013 Trojaner
 */

@SuppressWarnings({ "FieldCanBeLocal" })
public class IRCPlugin extends JavaPlugin implements Listener
{
    private String host = "irc.adventuria.eu";
    private static String channel = "#AdventuriaBot";
    private int port = 6667;

    static IRCBot ircBot;
    Logger log;

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);

        log = getLogger();
        ircBot = new IRCBot();

        try
        {
            ircBot.connect(host, port);
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(channel, event.getJoinMessage().replace(ChatColor.YELLOW.toString(), ChatColor.GRAY.toString()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(channel, event.getQuitMessage().replace(ChatColor.YELLOW.toString(), ChatColor.GRAY.toString()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        ircBot.sendCleanMessage(channel, event.getDeathMessage());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (IRCBot.disabled)
        {
            return;
        }
        String message = event.getMessage();
        if (ircBot == null)
        {
            return;
        }
        String prefix;
        if (message.length() < 2)
        {
            return;
        }
        else if (message.startsWith("!"))
        {
            prefix = ChatColor.GRAY + "[Schrei] ";
            message = message.replaceFirst("!", "");
        }
        else if (message.startsWith("?"))
        {
            prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "FRAGE" + ChatColor.GRAY + "] ";
            message = message.replaceFirst("^" + Pattern.quote("?"), "");
        }
        else
        {
            return; // Local Chat
        }
        String formattedMessage = prefix + event.getPlayer().getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + message;
        ircBot.sendCleanMessage(channel, IRCBot.replaceColorCodes(formattedMessage));
    }

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

    public static IRCBot getIRCBot()
    {
        return ircBot;
    }

    public static String getChannel()
    {
        return channel;
    }
}
