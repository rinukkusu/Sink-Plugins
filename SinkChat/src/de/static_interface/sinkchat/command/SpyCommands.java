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
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class SpyCommands
{
    public static final String PREFIX = _("SinkChat.Prefix.Spy") + " " + ChatColor.RESET;

    public static class EnableSpyCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            User user = SinkLibrary.getUser(sender);

            if (user.isConsole())
            {
                sender.sendMessage(_("General.ConsoleNotAvailabe"));
                return true;
            }
            Player player = user.getPlayer();

            PlayerConfiguration config = user.getPlayerConfiguration();

            if (config.getSpyEnabled())
            {
                player.sendMessage(PREFIX + _("SinkChat.Commands.Spy.YlreadyEnabled"));
                return true;
            }

            config.setSpyEnabled(true);
            sender.sendMessage(PREFIX + _("SinkChat.Commands.Spy.Enabled"));
            return true;
        }
    }

    public static class DisablSpyCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            User user = SinkLibrary.getUser(sender);
            if (user.isConsole())
            {
                sender.sendMessage(_("General.ConsoleNotAvailabe"));
                return true;
            }
            Player player = user.getPlayer();

            PlayerConfiguration config = user.getPlayerConfiguration();

            if (! config.getSpyEnabled())
            {
                player.sendMessage(PREFIX + _("SinkChat.Commands.Spy.AlreadyDisabled"));
                return true;
            }

            config.setSpyEnabled(false);
            player.sendMessage(PREFIX + _("SinkChat.Commands.Spy.Disabled"));
            return true;
        }
    }
}
