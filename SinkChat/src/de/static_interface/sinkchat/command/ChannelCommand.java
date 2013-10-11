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

package de.static_interface.sinkchat.command;

import de.static_interface.sinkchat.channel.ChannelHandler;
import de.static_interface.sinkchat.channel.IChannel;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class ChannelCommand extends JavaPlugin implements CommandExecutor
{
    public static final String PREFIX = _("SinkChat.Prefix.Channel") + " " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        User user = SinkLibrary.getUser(sender);
        if (user.isConsole())
        {
            sender.sendMessage(_("General.ConsoleNotAvailabe"));
            return true;
        }

        Player player = user.getPlayer();

        if (args.length == 0)
        {
            sendHelp(player);
            return true;
        }

        String message;

        switch (args[0])
        {
            case "join":
                if (args.length < 2)
                {
                    player.sendMessage(PREFIX + _("SinkChat.Commands.Channel.NoChannelGiven"));
                    return true;
                }

                try
                {
                    IChannel channel = ChannelHandler.getRegisteredChannel(args[1]);
                    if (! user.hasPermission(channel.getPermission()))
                    {
                        player.sendMessage(_("Permissions.General"));
                    }
                    channel.removeExceptedPlayer(player);
                }
                catch (NullPointerException e)
                {
                    message = PREFIX + _("SinkChat.Commands.Channel.ChannelUnknown").replace("%s", args[1]);
                    player.sendMessage(message);
                    return true;
                }

                message = PREFIX + _("SinkChat.Commands.Channel.PlayerJoins").replace("%s", args[1]);
                ChatColor.translateAlternateColorCodes('&', message);
                player.sendMessage(message);
                return true;

            case "leave":

                if (args.length < 2)
                {
                    player.sendMessage(PREFIX + _("SinkChat.Commands.Channel.NoChannelGiven"));
                    return true;
                }

                try
                {
                    IChannel channel = ChannelHandler.getRegisteredChannel(args[1]);
                    if (! user.hasPermission(channel.getPermission()))
                    {
                        player.sendMessage(_("Permissions.General"));
                    }
                    channel.addExceptedPlayer(player);
                }
                catch (NullPointerException e)
                {
                    message = PREFIX + _("SinkChat.Commands.Channel.ChannelUnknown").replace("%s", args[1]);
                    player.sendMessage(message);
                    return true;
                }

                message = PREFIX + _("SinkChat.Commands.Channel.PlayerLeaves").replace("%s", args[1]);
                player.sendMessage(message);


                return true;
            case "list":
                message = PREFIX + _("SinkChat.Commands.Channel.List").replace("%s", ChannelHandler.getChannelNames());
                player.sendMessage(message);
                return true;

            case "participating":
                player.sendMessage(PREFIX + _("SinkChat.Commands.Channel.Part"));
                for (IChannel target : ChannelHandler.getRegisteredChannels())
                {
                    if (target.contains(player))
                    {
                        continue;
                    }

                    player.sendMessage(PREFIX + target.getChannelName());

                }
                return true;

            default:
                sendHelp(player);
                return true;
        }
    }

    private static void sendHelp(Player player)
    {
        player.sendMessage(PREFIX + _("SinkChat.Commands.Channel.Help"));
        player.sendMessage(PREFIX + "/ch join <channel>");
        player.sendMessage(PREFIX + "/ch leave <channel>");
        player.sendMessage(PREFIX + "/ch list");
        player.sendMessage(PREFIX + "/ch participating");
    }
}
