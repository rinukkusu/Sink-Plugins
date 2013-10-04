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

import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class NickCommand implements CommandExecutor
{
    public static final String PREFIX = _("prefix.nick") + " " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        User user;
        String newDisplayName;
        if (args.length < 1)
        {
            return false;
        }
        if (args.length > 1)
        {
            if (! sender.hasPermission("sinkchat.nick.others"))
            {
                sender.sendMessage(_("permissions.nick.other"));
                return true;
            }
            String playerName = args[0];
            Player target = Bukkit.getServer().getPlayer(playerName);
            newDisplayName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args[1]) + ChatColor.RESET;
            if (target == null)
            {
                sender.sendMessage(PREFIX + _("general.notOnline").replace("%s", args[0]));
                return true;
            }

            if (setDisplayName(target, newDisplayName))
            {
                user = new User(target);
                PlayerConfiguration config = user.getPlayerConfiguration();
                sender.sendMessage(PREFIX + _("commands.nick.otherChanged").replaceFirst("%s", playerName).replaceFirst("%s", config.getDisplayName()));
            }
            return true;
        }
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(_("general.consoleNotAvailabe"));
            return true;
        }
        newDisplayName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args[0]) + ChatColor.RESET;
        Player player = (Player) sender;
        if (setDisplayName(player, newDisplayName))
        {
            sender.sendMessage(PREFIX + _("commands.nick.selfChanged").replaceFirst("%s", newDisplayName));
        }
        return true;
    }

    private boolean setDisplayName(Player player, String newDisplayName)
    {
        User user = new User(player);
        String cleanDisplayName = ChatColor.stripColor(newDisplayName);
        if (! cleanDisplayName.matches("^[a-zA-Z_0-9\u00a7]+$"))
        {
            player.sendMessage(PREFIX + _("commands.nick.illegalNickname"));
            return false;
        }

        if (cleanDisplayName.length() > 16)
        {
            player.sendMessage(PREFIX + _("commands.nick.tooLong"));
            return false;
        }

        if (cleanDisplayName.equals("off"))
        {
            newDisplayName = user.getDefaultDisplayName();
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if (player == onlinePlayer)
            {
                continue;
            }
            String displayName = onlinePlayer.getDisplayName().toLowerCase();
            String name = onlinePlayer.getName().toLowerCase();
            String lowerNick = newDisplayName.toLowerCase();
            if (lowerNick.equals(displayName) || lowerNick.equals(name))
            {
                player.sendMessage(PREFIX + _("commands.nick.used"));
                return false;
            }
        }
        PlayerConfiguration config = user.getPlayerConfiguration();
        config.setDisplayName(newDisplayName);
        return true;
    }
}
