package de.static_interface.ircplugin;

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

@SuppressWarnings({ "UnusedDeclaration", "FieldCanBeLocal" })
public class IRCPluginMain extends JavaPlugin implements Listener
{
    private String Host = "irc.lolnein.de";
    private String Channel = "#AdventuriaBot";
    private int Port = 6667;

    IRCBot ircBot;
    Logger log;

    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);

        log = getLogger();
        ircBot = new IRCBot();

        try
        {
            ircBot.connect(Host, 6667);
            ircBot.joinChannel(Channel);
        }
        catch (IOException | IrcException e)
        {
            log.severe("An Exception occurred while trying to connect to " + Host + ":");
            log.severe(e.toString());
        }
    }

    @Override
    public void onDisable()
    {
        ircBot.quitServer("Plugin reload or plugin has been deactivated");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        if (IRCBot.disabled) return;
        ircBot.sendCleanMessage(Channel, event.getJoinMessage().replace(ChatColor.YELLOW.toString(), ChatColor.GRAY.toString()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (IRCBot.disabled) return;
        ircBot.sendCleanMessage(Channel, event.getQuitMessage().replace(ChatColor.YELLOW.toString(), ChatColor.GRAY.toString()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerKick(PlayerKickEvent event)
    {
        if (IRCBot.disabled) return;
        if (! event.getReason().equals(""))
        {
            ircBot.sendCleanMessage(Channel, "Player \"" + event.getPlayer().getDisplayName() + "\" has been kicked for reason: " + event.getReason());
        }
        else
        {
            ircBot.sendCleanMessage(Channel, "Player \"" + event.getPlayer().getDisplayName() + "\" has been kicked!");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if (IRCBot.disabled) return;
        ircBot.sendCleanMessage(Channel, event.getDeathMessage());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        if (IRCBot.disabled) return;
        String message = event.getMessage();
        if (ircBot == null)
        {
            return;
        }
        String prefix;
        if (message.length() < 2) return;
        if (message.startsWith("$"))
        {
            prefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "Handel" + ChatColor.GRAY + "] ";
            message = message.replaceFirst("^" + Pattern.quote("$"),"");
        }
        else if (message.startsWith("!"))
        {
            prefix = ChatColor.GRAY + "[Schrei] ";
            message = message.replaceFirst("!","");
        }
        else if (message.startsWith("?"))
        {
            prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "FRAGE" + ChatColor.GRAY + "] ";
            message = message.replaceFirst("^" + Pattern.quote("?"),"");
        }
        else return; // Local Chat
        String formattedMessage = prefix + event.getPlayer().getDisplayName() + ChatColor.GRAY + ": " + ChatColor.WHITE + message;
        ircBot.sendCleanMessage(Channel, IRCBot.replaceColorCodes(formattedMessage));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        if (IRCBot.disabled) return;
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
                ircBot.sendCleanMessage(Channel, IRCBot.replaceColorCodes(IRCBot.replaceAmpersandColorCodes(ChatColor.DARK_PURPLE + "[Server] " + message)));
            }
        }
    }
}
