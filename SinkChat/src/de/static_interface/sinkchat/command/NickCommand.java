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

import de.static_interface.sinkchat.SinkChat;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.DARK_GREEN + "[Nick] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        User user;
        String newDisplayName;
        if (args.length < 1)
        {
            return false;
        }
        if (args.length > 2)
        {
            if (! sender.hasPermission("sinkchat.nick.others"))
            {
                sender.sendMessage(ChatColor.RED + "Du hast nicht genügend Rechte um die Namen anderer zu ändern!");
                return true;
            }
            String playerName = args[0];
            Player target = Bukkit.getServer().getPlayer(playerName);
            newDisplayName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args[1]) + ChatColor.RESET;
            if (target == null)
            {
                sender.sendMessage(PREFIX + args[0] + " ist nicht online!");
                return true;
            }

            if (setDisplayName(target, newDisplayName))
            {
                user = new User(target);
                PlayerConfiguration config = user.getPlayerConfiguration();
                sender.sendMessage(PREFIX + playerName + " heisst nun " + config.getDisplayName() + ".");
            }
            return true;
        }
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(ChatColor.RED + "Dieser Befehl funktioniert nur Ingame.");
            return true;
        }
        newDisplayName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args[0]) + ChatColor.RESET;
        Player player = (Player) sender;
        if (setDisplayName(player, newDisplayName))
        {
            user = new User(player);
            PlayerConfiguration config = user.getPlayerConfiguration();
            sender.sendMessage(PREFIX + "Du heisst nun " + config.getDisplayName() + ".");
        }
        return true;
    }

    private boolean setDisplayName(Player player, String newDisplayName)
    {
        String cleanDisplayName = ChatColor.stripColor(newDisplayName);
        if (! cleanDisplayName.matches("^[a-zA-Z_0-9\u00a7]+$"))
        {
            player.sendMessage(PREFIX + "Ungültiger Nickname!");
            return false;
        }

        if (cleanDisplayName.length() > 16)
        {
            player.sendMessage(PREFIX + "Nickname ist zu lang!");
            return false;
        }

        if (cleanDisplayName.equals("off"))
        {
            newDisplayName = SinkChat.getDefaultDisplayName(player);
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
                player.sendMessage(PREFIX + "Der Nickname wird bereits verwendet");
                return false;
            }
        }
        User user = new User(player);
        PlayerConfiguration config = user.getPlayerConfiguration();
        config.setDisplayName(newDisplayName);
        return true;
    }
}
