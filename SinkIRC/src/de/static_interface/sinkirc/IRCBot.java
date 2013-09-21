package de.static_interface.sinkirc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jibble.pircbot.Colors;
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
    public static String IRC_PREFIX = ChatColor.GRAY + "[IRC] " + ChatColor.RESET;
    public static boolean disabled = false;

    String botName = "AdventuriaBot";

    public IRCBot()
    {
        this.setName(botName);
        this.setLogin(botName);
        this.setVersion("Bukkit IRC Plugin, (c) 2013 Adventuria");
    }

    public void sendCleanMessage(String target, String message)
    {
        if (disabled)
        {
            return;
        }
        message = replaceColorCodes(message);
        sendMessage(target, message);
    }

    String[] Whitelist = { "Trojaner", "Trojaner_" }; //Very, very poorly written

    public static String replaceColorCodes(String input)
    {
        input = input.replace(ChatColor.BLACK.toString(), Colors.BLACK);
        input = input.replace(ChatColor.DARK_BLUE.toString(), Colors.DARK_BLUE);
        input = input.replace(ChatColor.DARK_GREEN.toString(), Colors.DARK_GREEN);
        input = input.replace(ChatColor.DARK_AQUA.toString(), Colors.TEAL);
        input = input.replace(ChatColor.DARK_RED.toString(), Colors.RED);
        input = input.replace(ChatColor.DARK_PURPLE.toString(), Colors.PURPLE);
        input = input.replace(ChatColor.GOLD.toString(), Colors.OLIVE);
        input = input.replace(ChatColor.GRAY.toString(), Colors.LIGHT_GRAY);
        input = input.replace(ChatColor.DARK_GRAY.toString(), Colors.DARK_GRAY);
        input = input.replace(ChatColor.BLUE.toString(), Colors.BLUE);
        input = input.replace(ChatColor.GREEN.toString(), Colors.GREEN);
        input = input.replace(ChatColor.AQUA.toString(), Colors.CYAN);
        input = input.replace(ChatColor.RED.toString(), Colors.RED);
        input = input.replace(ChatColor.LIGHT_PURPLE.toString(), Colors.PURPLE);
        input = input.replace(ChatColor.YELLOW.toString(), Colors.YELLOW);
        input = input.replace(ChatColor.WHITE.toString(), Colors.NORMAL);
        input = input.replace("§k", "");
        input = input.replace(ChatColor.BOLD.toString(), Colors.BOLD);
        input = input.replace(ChatColor.STRIKETHROUGH.toString(), "");
        input = input.replace(ChatColor.UNDERLINE.toString(), Colors.UNDERLINE);
        input = input.replace(ChatColor.ITALIC.toString(), "");
        input = input.replace(ChatColor.RESET.toString(), Colors.NORMAL);
        return input;
    }

    @Override
    public void onJoin(String channel, String sender, String login, String hostname)
    {
        //Todo: Diffrent Welcomemessages
        if (sender.equals(getNick()))
        {
            return;
        }
        sendMessage(channel, "Willkommen, " + sender + "!");
        Bukkit.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + channel + "] " + ChatColor.DARK_AQUA + sender + ChatColor.WHITE + " ist dem Kanal beigetreten.");
        super.onJoin(channel, sender, login, hostname);
    }

    @Override
    public void onPart(String channel, String sender, String login, String hostname)
    {
        Bukkit.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + channel + "] " + ChatColor.DARK_AQUA + sender + ChatColor.WHITE + " hat den Kanal verlassen.");
    }

    @Override
    public void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason)
    {
        String formattedReason = "Grund: " + reason + ".";
        if (reason.equals("") || reason.equals("\"\""))
        {
            formattedReason = "";
        }
        Bukkit.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + channel + "] " + ChatColor.DARK_AQUA + recipientNick + ChatColor.WHITE + " wurde von " + kickerNick + " aus dem Kanal geworfen. " + formattedReason);
    }

    @Override
    public void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason)
    {
        String formattedReason = " (" + reason + ")";
        if (reason.equals("") || reason.equals("\"\""))
        {
            formattedReason = "";
        }
        Bukkit.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + "Server" + "] " + ChatColor.DARK_AQUA + sourceNick + ChatColor.WHITE + " hat den IRC Server verlassen." + formattedReason);
    }

    @Override
    public void onNickChange(String oldNick, String login, String hostname, String newNick)
    {
        Bukkit.broadcastMessage(IRC_PREFIX + ChatColor.GRAY + "[" + "Server" + "] " + ChatColor.DARK_AQUA + oldNick + ChatColor.WHITE + " ist jetzt als " + ChatColor.DARK_AQUA + newNick + ChatColor.WHITE + " bekannt.");
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message)
    {
        try
        {
            if (( message.toLowerCase().contains("hello") || message.toLowerCase().contains("hi")
                    || message.toLowerCase().contains("huhu") || message.toLowerCase().contains("hallo")
                    || message.toLowerCase().contains("moin") || message.toLowerCase().contains("morgen") )
                    && ( message.toLowerCase().contains("adventuriabot") || message.toLowerCase().contains("bot") ))
            {
                sendMessage(channel, "Hallo, " + sender); //Todo: Diffrent Welcome messages
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
            boolean isWhitelist = EqualsArrayItem(sender, Whitelist);

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
                if (args.length < 2)
                {
                    sendCleanMessage(channel, "Usage: !say <text>");
                }
                String messageWithPrefix = IRC_PREFIX + ChatColor.GRAY + "[" + channel + "] " + ChatColor.DARK_AQUA + sender + ChatColor.GRAY + ": " + ChatColor.WHITE + message.replaceFirst("say", "");
                Bukkit.getServer().broadcastMessage(messageWithPrefix);
                sendCleanMessage(channel, replaceColorCodes(messageWithPrefix));
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
                sendCleanMessage(channel, "Online Players (" + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers() + "): " + players);
            }

            if (cmd.equals("permissions") || cmd.equals("perms"))
            {
                if (isOp || isWhitelist)
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

    public boolean EqualsArrayItem(String input, String[] array)
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
