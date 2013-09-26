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
import org.bukkit.potion.PotionEffectType;

public class MilkCommand implements CommandExecutor
{

    public static final String PREFIX = ChatColor.BLUE + "[Milk] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0) //Remove all
        {
            String s = "";
            int i = 0;
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.hasPotionEffect(PotionEffectType.INVISIBILITY))
                {
                    i++;
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    if (s.equals(""))
                    {
                        s = p.getDisplayName();
                    }
                    else
                    {
                        s = s + ", " + p.getDisplayName();
                    }
                }
            }
            if (i > 0)
            {
                BukkitUtil.broadcast(PREFIX + "Der Unsichtbarkeits Trank von den folgenden Spielern wurde durch "
                        + BukkitUtil.getSenderName(sender) + " entfernt:", "sinkcommands.milk.message");
                BukkitUtil.broadcast(PREFIX + s, "sinkcommands.milk.message");
                return true;
            }
            sender.sendMessage(PREFIX + ChatColor.RED + "Es gibt zur Zeit keine Spieler die einen Unsichtbarkeits Trank haben...");
            return true;
        }
        //Remove from specified player
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null)
        {
            sender.sendMessage(PREFIX + args[0] + " ist nicht online!");
            return true;
        }

        if (target.hasPotionEffect(PotionEffectType.INVISIBILITY))
        {
            BukkitUtil.broadcast(PREFIX + "Der Unsichtbarkeits Trank von " + target.getDisplayName()
                    + " wurde durch " + BukkitUtil.getSenderName(sender) + " entfernt.", "sinkcommands.milk.message");
            target.removePotionEffect(PotionEffectType.INVISIBILITY);
            return true;
        }
        sender.sendMessage(PREFIX + ChatColor.RED + "Spieler \"" + target.getDisplayName()
                + "\" hat keinen Unsichtbarkeits Trank Effekt!");
        return true;
    }
}
