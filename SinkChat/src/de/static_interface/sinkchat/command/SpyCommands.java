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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SpyCommands
{
    public static String PREFIX = ChatColor.DARK_GRAY + "[Spy] " + ChatColor.RESET;

    public static class EnableSpyCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage("This command is only ingame available.");
                return true;
            }
            Player player = (Player) sender;

            User user = new User(player);
            PlayerConfiguration config = user.getPlayerConfiguration();

            if (config.getSpyEnabled())
            {
                player.sendMessage(PREFIX + ChatColor.RED + "Der Spy ist schon aktiviert!");
                return true;
            }

            config.setSpyEnabled(true);
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Der Spy wurde aktiviert!");
            return true;
        }
    }

    public static class DisablSpyCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage("This command is only ingame available.");
                return true;
            }
            Player player = (Player) sender;

            User user = new User(player);
            PlayerConfiguration config = user.getPlayerConfiguration();

            if (! config.getSpyEnabled())
            {
                player.sendMessage(PREFIX + ChatColor.RED + "Der Spy ist schon deaktiviert!");
                return true;
            }

            config.setSpyEnabled(false);
            sender.sendMessage(PREFIX + ChatColor.RED + "Der Spy wurde deaktiviert.");
            return true;
        }
    }
}
