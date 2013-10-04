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

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class SpyCommands
{
    public static final String PREFIX = _("prefix.spy") + " " + ChatColor.RESET;

    public static class EnableSpyCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (sender instanceof ConsoleCommandSender)
            {
                sender.sendMessage(_("general.consoleNotAvailabe"));
                return true;
            }
            Player player = (Player) sender;

            User user = new User(player);
            PlayerConfiguration config = user.getPlayerConfiguration();

            if (config.getSpyEnabled())
            {
                player.sendMessage(PREFIX + _("commands.spy.alreadyEnabled"));
                return true;
            }

            config.setSpyEnabled(true);
            sender.sendMessage(PREFIX + _("commands.spy.enabled"));
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
                sender.sendMessage(_("general.consoleNotAvailabe"));
                return true;
            }
            Player player = (Player) sender;

            User user = new User(player);
            PlayerConfiguration config = user.getPlayerConfiguration();

            if (! config.getSpyEnabled())
            {
                player.sendMessage(PREFIX + _("commands.spy.alreadyDisabled"));
                return true;
            }

            config.setSpyEnabled(false);
            player.sendMessage(PREFIX + _("commands.spy.disabled"));
            return true;
        }
    }
}
