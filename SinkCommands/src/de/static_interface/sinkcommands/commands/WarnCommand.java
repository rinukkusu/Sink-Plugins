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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.RED + "[Warn] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(PREFIX + ChatColor.RED + "Zu wenige Argumente!");
            sender.sendMessage(PREFIX + ChatColor.RED + "Benutzung: /warn [Spieler] (Grund)");
            return false;
        }
        Player target = ( Bukkit.getServer().getPlayer(args[0]) );
        if (target == null)
        {
            sender.sendMessage(PREFIX + args[0] + " ist nicht online!");
            return true;
        }
        if (target.getDisplayName().equals(BukkitUtil.getSenderName(sender)))
        {
            sender.sendMessage(PREFIX + "Du kannst dich nicht selbst verwarnen!");
            return true;
        }
        if (args.length == 1)
        {
            sender.sendMessage(PREFIX + "Du musst einen Grund angeben!");
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

        target.sendMessage(PREFIX + ChatColor.RED + "Du wurdest von " + BukkitUtil.getSenderName(sender) + ChatColor.RED + " verwarnt. Grund: " + reason);
        BukkitUtil.broadcast(PREFIX + target.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " verwarnt. Grund: " + reason, "sinkcommands.warn.message");
        return true;
    }
}
