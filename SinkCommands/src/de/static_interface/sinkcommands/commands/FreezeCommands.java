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

package de.static_interface.sinkcommands.commands;

import de.static_interface.sinklibrary.BukkitUtil;
import de.static_interface.sinklibrary.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommands
{
    public static final String PREFIX = ChatColor.RED + "[Freeze] " + ChatColor.RESET;

    public static class FreezeCommand implements CommandExecutor
    {
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (args.length < 1)
            {
                return false;
            }

            String reason = "";

            for (int i = 1; i < args.length; i++)
            {
                if (reason.equals(""))
                {
                    reason = args[i];
                    continue;
                }
                reason = reason + " " + args[i];
            }

            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.getName().toLowerCase().contains(args[0].toLowerCase()) || p.getName().equalsIgnoreCase(args[0]))
                {
                    if (! canBeFrozen(p) && sender instanceof Player)
                    {
                        sender.sendMessage(PREFIX + ChatColor.DARK_RED + "Dieser Spieler kann nicht eingefroren werden!");
                        return true;
                    }

                    if (toggleFreeze(p))
                    {
                        if (args.length < 2)
                        {
                            BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " eingefroren.", "sinkcommands.freeze.message");
                        }
                        else
                        {
                            BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " eingefroren. Grund: " + reason, "sinkcommands.freeze.message");
                        }
                        return true;
                    }
                    p.sendMessage(PREFIX + ChatColor.RED + "Du wurdest von " + BukkitUtil.getSenderName(sender) + " aufgetaut.");
                    BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " wieder aufgetaut.", "sinkcommands.freeze.message");
                    return true;
                }
            }
            return true;
        }
    }

    public static class FreezelistCommand implements CommandExecutor
    {
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            String frozenList = "";
            String tmpfrozenList = "";

            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (isFrozen(p))
                {
                    if (frozenList.length() > 0)
                    {
                        frozenList = frozenList + ", " + p.getDisplayName();
                    }
                    else
                    {
                        frozenList = p.getDisplayName();
                    }
                }
            }

            if (frozenList.length() > 0)
            {
                sender.sendMessage(PREFIX + "Eingefrorene Spieler: " + frozenList);
            }
            else
            {
                sender.sendMessage(PREFIX + "Es gibt keine eingefrorenen Spieler.");
            }

            if (tmpfrozenList.length() > 0)
            {
                sender.sendMessage(PREFIX + "Temporär eingefrorene Spieler: " + tmpfrozenList);
            }
            else
            {
                sender.sendMessage(PREFIX + "Es gibt keine temporär eingefrorenen Spieler.");
            }
            return true;
        }
    }

    public static boolean isFrozen(Player player)
    {
        User user = new User(player);
        return user.getPlayerConfiguration().getFreezed();
    }

    public static boolean canBeFrozen(Player player)
    {
        User user = new User(player);
        return ! user.hasPermission("sinkcommands.freeze.never");
    }


    public static boolean toggleFreeze(Player player)
    {
        User user = new User(player);
        if (user.getPlayerConfiguration().getFreezed())
        {
            user.getPlayerConfiguration().setFreezed(false);
            return false;
        }
        else
        {
            user.getPlayerConfiguration().setFreezed(true);
            return true;
        }
    }

    public static void unfreeze(Player player)
    {
        User user = new User(player);
        user.getPlayerConfiguration().setFreezed(false);
    }
}