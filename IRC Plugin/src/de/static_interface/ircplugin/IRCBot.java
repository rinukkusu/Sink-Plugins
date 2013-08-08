package de.static_interface.ircplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

/**
 * IRCBot
 * Description: Bot for IRC
 * Date: 21.07.13
 * Author: Trojaner
 * Copyright © 2013 Trojaner
 */

public class IRCBot extends PircBot
{
    private boolean disabled = false;
    String BotName = "AdventuriaBot";

    public IRCBot()
    {
        this.setName(BotName);
        this.setLogin(BotName);
        this.setVersion("Bukkit IRC Plugin, (c) Adventuria 2013");
    }

    public void sendCleanMessage(String target, String message)
    {
        if (disabled)
        {
            return;
        }
        message = RemoveColorCodes(message);
        sendMessage(target, message);
    }

    String[] Whitelist = { "Trojaner", "Trojaner_" }; //Very, very poorly written

    private static String RemoveColorCodes(String input)
    {
        input = input.replace("§0", "");
        input = input.replace("§1", "");
        input = input.replace("§2", "");
        input = input.replace("§3", "");
        input = input.replace("§4", "");
        input = input.replace("§5", "");
        input = input.replace("§6", "");
        input = input.replace("§7", "");
        input = input.replace("§8", "");
        input = input.replace("§9", "");
        input = input.replace("§a", "");
        input = input.replace("§b", "");
        input = input.replace("§c", "");
        input = input.replace("§d", "");
        input = input.replace("§e", "");
        input = input.replace("§f", "");
        input = input.replace("§k", "");
        input = input.replace("§l", "");
        input = input.replace("§m", "");
        input = input.replace("§n", "");
        input = input.replace("§o", "");
        input = input.replace("§r", "");
        return input;
    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname)
    {
        //Todo: Verschiedene Nachrichten
        if (sender.equals(getNick()))
        {
            return;
        }
        sendMessage(channel, "Willkommen, " + sender + "!");
        super.onJoin(channel, sender, login, hostname);
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message)
    {
        try
        {
            if (( message.toLowerCase().contains("hello") || message.toLowerCase().contains("hi")
                    || message.toLowerCase().contains("huhu") || message.toLowerCase().contains("hallo")
                    || message.toLowerCase().contains("moin") || message.toLowerCase().contains("morgen") )
                    && ( message.toLowerCase().contains("minecraftbot") || message.toLowerCase().contains("bot") ))
            {
                sendMessage(channel, "Hallo, " + sender); //Todo: Verschiedene Nachrichten
                return;
            }
            if (! message.toLowerCase().startsWith("!"))
            {
                return;
            }
            message = message.replaceFirst("!", "");
            String[] args = message.split(" ");
            String cmd = args[0].toLowerCase();

            boolean isOp = isOp(channel, sender);
            boolean isWhitelist = Contains(Whitelist, sender);

            if (cmd.equals("toggle"))
            {
                if (! isOp && ! isWhitelist)
                {
                    throw new UnauthorizedAccessException();
                }
                disabled = ! disabled;
                if (disabled)
                {
                    sendMessage(channel, "Disabled " + getName());
                }
                else
                {
                    sendMessage(channel, "Enabled " + getName());
                }
            }

            if (disabled)
            {
                return;
            }

            if (cmd.equals("say"))
            {
                Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "[IRC] [" + sender + "] " + ChatColor.WHITE + message.replaceFirst("say", ""));
                sendCleanMessage(channel, "[IRC] [" + sender + "] " + message.replaceFirst("say", ""));
            }

            if (cmd.equals("kick"))
            {
                if (! isOp && ! isWhitelist)
                {
                    throw new UnauthorizedAccessException();
                }
                String targetPlayerName;
                try
                {
                    targetPlayerName = args[1];
                }
                catch (Exception e)
                {
                    sendCleanMessage(channel, "Usage: !kick <player> <reason>");
                    return;
                }
                Player targetPlayer = Bukkit.getServer().getPlayer(targetPlayerName);
                if (targetPlayer == null)
                {
                    sendCleanMessage(channel, "Player \"" + targetPlayerName + "\" is not online!");
                    return;
                }
                String reason = message.replace(targetPlayerName + " ", "");
                reason = reason.replace("kick ", "");
                if (args.length >= 3)
                {
                    targetPlayer.kickPlayer("Kicked by " + sender + " (" + reason + ") !");
                }
                else
                {
                    targetPlayer.kickPlayer("Kicked by " + sender + "!");
                }
            }

            if (cmd.equals("list"))
            {
                String players = "";
                if (Bukkit.getServer().getOnlinePlayers().length == 0)
                {
                    sendCleanMessage(channel, "There are currently no online players");
                    return;
                }
                for (Player p : Bukkit.getServer().getOnlinePlayers())
                {
                    if (players.equals(""))
                    {
                        players = p.getDisplayName();
                    }
                    else
                    {
                        players = players + ", " + p.getDisplayName();
                    }
                }
                sendCleanMessage(channel, "Online Players: " + players);
            }

            if (cmd.equals("permissions") | cmd.equals("perms"))
            {
                if (isOp | isWhitelist)
                {
                    sendCleanMessage(channel, "You are allowed to use administrator commands");
                }
                else
                {
                    sendCleanMessage(channel, "You aren't allowed to use administrator commands");
                }
            }

            if (cmd.equals("whoami"))
            {
                sendCleanMessage(channel, "You are " + sender + " (" + login + "@" + hostname + ")");
            }
            super.onMessage(channel, sender, login, hostname, message);
        }
        catch (UnauthorizedAccessException e)
        {
            sendMessage(channel, "You may not use that command");
        }
    }

    private boolean isOp(String channel, String user)
    {
        for (User u : getUsers(channel))
        {
            if (u.isOp() && u.getNick().equals(user))
            {
                return true;
            }
        }
        return false;
    }

    public boolean Contains(String[] array, String input)
    {
        for (String s : array)
        {
            if (s.equals(input))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue)
    {
        sendMessage(getChannels()[0], sourceNick + " hat mich mit dem Wert \"" + pingValue + "\" angepingt.");
    }
}
