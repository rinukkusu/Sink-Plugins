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

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class NickCommand implements CommandExecutor
{
    public static final String PREFIX = _("SinkChat.Prefix.Nick") + " " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        User user = SinkLibrary.getUser(sender);
        String newDisplayName;

        if (args.length < 1)
        {
            return false;
        }

        if (args.length > 1)
        {
            if (! user.hasPermission("sinkchat.nick.others"))
            {
                sender.sendMessage(_("Permissions.SinkChat.Nick.Other"));
                return true;
            }

            String playerName = args[0];
            Player target = Bukkit.getServer().getPlayer(playerName);

            newDisplayName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args[1]) + ChatColor.RESET;
            if (target == null)
            {
                sender.sendMessage(PREFIX + _("General.NotOnline").replace("%s", args[0]));
                return true;
            }

            if (setDisplayName(target, newDisplayName, sender))
            {
                user = SinkLibrary.getUser(target);
                PlayerConfiguration config = user.getPlayerConfiguration();
                sender.sendMessage(PREFIX + _("SinkChat.Commands.Nick.OtherChanged").replaceFirst("%s", playerName).replaceFirst("%s", config.getDisplayName()));
            }
            return true;
        }
        if (user.isConsole())
        {
            sender.sendMessage(_("General.ConsoleNotAvailabe"));
            return true;
        }
        newDisplayName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args[0]) + ChatColor.RESET;
        Player player = user.getPlayer();
        if (setDisplayName(player, newDisplayName, sender))
        {
            sender.sendMessage(PREFIX + _("SinkChat.Commands.Nick.SelfChanged").replaceFirst("%s", newDisplayName));
        }
        return true;
    }

    private boolean setDisplayName(Player target, String newDisplayName, CommandSender sender)
    {
        User user = SinkLibrary.getUser(target);
        String cleanDisplayName = ChatColor.stripColor(newDisplayName);
        if (! cleanDisplayName.matches("^[a-zA-Z_0-9" + ChatColor.COLOR_CHAR + "]+$"))
        {
            sender.sendMessage(PREFIX + _("SinkChat.Commands.Nick.IllegalNickname"));
            return false;
        }

        if (cleanDisplayName.length() > 16)
        {
            sender.sendMessage(PREFIX + _("SinkChat.Commands.Nick.TooLong"));
            return false;
        }

        if (cleanDisplayName.equals("off"))
        {
            newDisplayName = user.getDefaultDisplayName();
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if (target == onlinePlayer)
            {
                continue;
            }
            String onlinePlayerDisplayName = ChatColor.stripColor(onlinePlayer.getDisplayName().toLowerCase());
            String onlinePlayerName = ChatColor.stripColor(onlinePlayer.getName().toLowerCase());
            String displayName = ChatColor.stripColor(newDisplayName.toLowerCase());
            if (displayName.equals(onlinePlayerDisplayName) || displayName.equals(onlinePlayerName))
            {
                target.sendMessage(PREFIX + _("SinkChat.Commands.Nick.Used"));
                return false;
            }
        }

        if (SinkLibrary.chatAvailable())
        {
            newDisplayName = user.getPrefix() + newDisplayName;
        }

        PlayerConfiguration config = user.getPlayerConfiguration();
        config.setDisplayName(newDisplayName);
        config.setHasDisplayName(true);
        return true;
    }
}
