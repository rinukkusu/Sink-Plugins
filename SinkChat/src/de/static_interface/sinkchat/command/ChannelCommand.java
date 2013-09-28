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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class ChannelCommand extends JavaPlugin implements CommandExecutor
{
    public static final String PREFIX = _("prefix.channel") + " " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(_("general.consoleNotAvailabe"));
            return true;
        }

        if (! sender.hasPermission("sinkchat.channel.use"))
        {
            sender.sendMessage(_("permissions.general"));
            return true;
        }

        if (args.length == 0)
        {
            sendHelp(sender);
            return true;
        }

        String message;


        switch (args[0])
        {
            case "join":
                if (args.length < 2)
                {
                    sender.sendMessage(PREFIX + _("commands.channel.noChannelGiven"));
                    return true;
                }

                try
                {
                    IChannel channel = ChannelHandler.getRegisteredChannel(args[1]);
                    if (! sender.hasPermission(channel.getPermission()))
                    {
                        sender.sendMessage(_("permissions.general"));
                    }
                    channel.removeExceptedPlayer((Player) sender);
                }
                catch (NullPointerException e)
                {
                    message = PREFIX + _("commands.channel.channelUnknown").replace("%s", args[1]);
                    sender.sendMessage(message);
                    return true;
                }

                message = PREFIX + _("commands.channel.playerJoins").replace("%s", args[1]);
                ChatColor.translateAlternateColorCodes('&', message);
                sender.sendMessage(message);


                return true;

            case "leave":

                if (args.length < 2)
                {
                    sender.sendMessage(PREFIX + _("commands.channel.noChannelGiven"));
                    return true;
                }

                try
                {
                    IChannel channel = ChannelHandler.getRegisteredChannel(args[1]);
                    if (! sender.hasPermission(channel.getPermission()))
                    {
                        sender.sendMessage(_("permissions.general"));
                    }
                    channel.addExceptedPlayer((Player) sender);
                }
                catch (NullPointerException e)
                {   //Note: Do this more clean...
                    message = PREFIX + _("commands.channel.channelUnknown").replace("%s", args[1]);
                    sender.sendMessage(message);
                    return true;
                }

                message = PREFIX + _("commands.channel.playerLeaves").replace("%s", args[1]);
                sender.sendMessage(message);


                return true;
            case "list":
                message = PREFIX + _("commands.channel.list").replace("%s", ChannelHandler.getChannelNames());
                sender.sendMessage(message);
                return true;

            case "participating":
                sender.sendMessage(PREFIX + _("commands.channel.part"));
                for (IChannel target : ChannelHandler.getRegisteredChannels())
                {
                    if (target.contains((Player) sender))
                    {
                        continue;
                    }

                    sender.sendMessage(PREFIX + target.getChannelName());

                }
                return true;

            default:
                sendHelp(sender);
                return true;
        }
    }

    private static void sendHelp(CommandSender sender)
    {
        sender.sendMessage(PREFIX + _("commands.channel.help"));
        sender.sendMessage(PREFIX + "/ch join <channel>");
        sender.sendMessage(PREFIX + "/ch leave <channel>");
        sender.sendMessage(PREFIX + "/ch list");
        sender.sendMessage(PREFIX + "/ch participating");
    }
}
