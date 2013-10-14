/*
 * Copyright (c) 2013 adventuria.eu / static-interface.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.static_interface.sinkirc;

import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jibble.pircbot.Colors;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IRCBot extends PircBot
{
    public static String IRC_PREFIX = ChatColor.GRAY + "[IRC] " + ChatColor.RESET;
    public static boolean disabled = false;
    String botName = "AdventuriaBot";
    private Plugin plugin;

    public IRCBot(Plugin plugin)
    {
        this.setName(botName);
        this.setLogin(botName);
        this.setVersion("SinkIRC for Bukkit, visit http://dev.bukkit.org/bukkit-plugins/sink-plugins/");
        this.plugin = plugin;
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
    public void onPrivateMessage(String sender, String login, String hostname, String message)
    {
        String[] args = message.split(" ");
        String cmd = args[0];
        List<String> tmp = new ArrayList<>(Arrays.asList(args));
        tmp.remove(0);
        args = tmp.toArray(args);
        executeCommand(cmd, args, sender, sender, message);
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message)
    {
        if (( message.toLowerCase().contains("hello") || message.toLowerCase().contains("hi")
                || message.toLowerCase().contains("huhu") || message.toLowerCase().contains("hallo")
                || message.toLowerCase().contains("moin") || message.toLowerCase().contains("morgen") )
                && ( message.toLowerCase().contains(" " + getName() + " ") || message.toLowerCase().contains(" bot ") ))
        {
            sendMessage(channel, "Hallo, " + sender);
            return;
        }

        if (! message.toLowerCase().startsWith("~"))
        {
            return;
        }
        message = message.replaceFirst("~", "");
        String[] args = message.split(" ");
        String cmd = args[0];
        List<String> tmp = new ArrayList<>(Arrays.asList(args));
        tmp.remove(0);
        args = tmp.toArray(args);
        executeCommand(cmd, args, channel, sender, message);
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

    @Override
    public void onPing(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue)
    {
        sendMessage(SinkIRC.getMainChannel(), sourceNick + " hat mich mit dem Wert \"" + pingValue + "\" angepingt.");
    }

    public void executeCommand(String command, String[] args, String source, String sender, String label)
    {
        try
        {
            boolean isOp = isOp(SinkIRC.getMainChannel(), sender);

            if (command.equals("toggle"))
            {
                if (! isOp)
                {
                    throw new UnauthorizedAccessException();
                }
                disabled = ! disabled;
                if (disabled)
                {
                    sendMessage(source, "Disabled " + getName());
                }
                else
                {
                    sendMessage(source, "Enabled " + getName());
                }
            }

            if (disabled)
            {
                return;
            }

            if (command.equals("exec")) //Execute command as console
            {
                if (! isOp) throw new UnauthorizedAccessException();
                String commandWithArgs = "";
                int i = 0;
                for (String arg : args)
                {
                    if (i == args.length - 1)
                    {
                        break;
                    }
                    i++;
                    if (commandWithArgs.equals(""))
                    {
                        commandWithArgs = arg;
                        continue;
                    }
                    commandWithArgs = commandWithArgs + " " + arg;
                }


                final String finalCommandWithArgs = commandWithArgs;

                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommandWithArgs);
                    }
                });

                sendMessage(source, "Executed command: \"" + commandWithArgs + "\"");
            }

            if (command.equals("say")) //Speak to ingame players
            {
                boolean privateMessageCommand = ! source.startsWith("#");

                if (args.length < 2)
                {
                    sendCleanMessage(source, "Usage: !say <text>");
                    return;
                }

                if (privateMessageCommand)
                {
                    source = "Query";
                }

                String messageWithPrefix = IRC_PREFIX + ChatColor.GRAY + "[" + source + "] " + ChatColor.DARK_AQUA + sender + ChatColor.GRAY
                        + ": " + ChatColor.WHITE + label.replaceFirst("say", "");
                Bukkit.getServer().broadcastMessage(messageWithPrefix);
                sendCleanMessage(SinkIRC.getMainChannel(), replaceColorCodes(messageWithPrefix));
            }

            if (command.equals("kick"))  //Kick players from IRC
            {
                if (! isOp)
                {
                    throw new UnauthorizedAccessException();
                }
                String targetPlayerName;
                try
                {
                    targetPlayerName = args[0];
                }
                catch (Exception e)
                {
                    sendCleanMessage(source, "Usage: !kick <player> <reason>");
                    return;
                }
                final Player targetPlayer = Bukkit.getServer().getPlayer(targetPlayerName);
                if (targetPlayer == null)
                {
                    sendCleanMessage(source, "Player \"" + targetPlayerName + "\" is not online!");
                    return;
                }
                String reason = label.replace(targetPlayerName + " ", "");
                reason = reason.replace("kick ", "");
                String formattedReason = "";
                if (args.length > 1)
                {
                    formattedReason = " (Reason: " + reason + ")";
                }
                reason = targetPlayer.getName() + ": Kicked by " + sender + " from IRC" + formattedReason + "!";
                final String finalReason = reason;
                Bukkit.getScheduler().runTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        targetPlayer.kickPlayer(finalReason);
                    }
                });
                Bukkit.broadcastMessage(reason);

            }

            if (command.equals("list")) //List Players
            {
                String players = "";
                if (Bukkit.getServer().getOnlinePlayers().length == 0)
                {
                    sendCleanMessage(source, "There are currently no online players");
                    return;
                }

                Player[] onlinePlayers = Bukkit.getOnlinePlayers();
                Arrays.sort(onlinePlayers);

                for ( Player p : onlinePlayers )
                {
                    de.static_interface.sinklibrary.User user = SinkLibrary.getUser(p);
                    if (players.equals(""))
                    {
                        players = user.getDisplayName();
                    }
                    else
                    {
                        players = players + ", " + user.getDisplayName();
                    }
                }
                sendCleanMessage(source, "Online Players (" + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers() + "): " + players);
            }
        }
        catch (UnauthorizedAccessException e)
        {
            sendMessage(source, "You may not use that command");
        }
        catch (Exception e)
        {
            sendMessage(source, "Uncaught Exception occured while trying to execute command: " + command);
            sendMessage(source, e.getMessage());
        }
    }
}
