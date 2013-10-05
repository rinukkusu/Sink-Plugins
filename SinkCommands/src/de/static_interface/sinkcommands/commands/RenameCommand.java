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

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand implements CommandExecutor
{

    public static final String PREFIX = ChatColor.AQUA + "[Rename] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        User user = SinkLibrary.getUser(sender);
        if (user.isConsole())
        {
            sender.sendMessage(PREFIX + "Dieser Befehl ist nur Ingame ausführbar.");
            return true;
        }
        Player p = user.getPlayer();

        if (args.length < 2)
        {
            return false;
        }
        if (p.getItemInHand().getType() == Material.AIR)
        {
            sender.sendMessage(PREFIX + "Nimm ein Item in die Hand bevor du diesen Befehl ausführst.");
            return true;
        }
        String text = Util.formatArrayToString(args, "");

        text = ChatColor.translateAlternateColorCodes('&', text);
        ItemStack item = p.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(text);
        sender.sendMessage(PREFIX + ChatColor.GRAY + "Name von Item wurde zu: " + ChatColor.GREEN + text + ChatColor.GRAY + " umbenannt.");
        item.setItemMeta(meta);
        return true;
    }
}