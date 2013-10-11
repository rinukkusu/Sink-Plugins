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
import de.static_interface.sinklibrary.configuration.LanguageConfiguration;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class CommandsdebugCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.BLUE + "[Debug] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
        {
            return false;
        }
        String option = args[0];
        switch (option.toLowerCase())
        {
            case "getplayervalue":
            {
                if (args.length != 3)
                {
                    sender.sendMessage(PREFIX + "Falsche Benutzung! Korrekte Benutzung: /cdebug getplayervalue <player> <path.to.key>");
                    break;
                }
                String player = args[1];
                String path = args[2];
                User user = SinkLibrary.getUser(player);
                PlayerConfiguration config = user.getPlayerConfiguration();
                sender.sendMessage(PREFIX + "Output: " + config.getYamlConfiguration().getString(path));
                break;
            }

            case "setplayervalue":
            {
                if (args.length != 4)
                {
                    sender.sendMessage(PREFIX + "Falsche Benutzung! Korrekte Benutzung: /cdebug setplayervalue <player> <path.to.key> <value>");
                    break;
                }
                String player = args[1];
                String path = args[2];
                Object value = replaceValue(args[3]);

                User user = SinkLibrary.getUser(player);
                PlayerConfiguration config = user.getPlayerConfiguration();
                config.set(path, value);
                sender.sendMessage(PREFIX + "Done");
                break;
            }

            case "haspermission":
            {
                if (args.length != 3)
                {
                    sender.sendMessage(PREFIX + "Falsche Benutzung! Korrekte Benutzung: /cdebug haspermission <player> <permission>");
                    break;
                }
                String player = args[1];
                String permission = args[2];
                User user = SinkLibrary.getUser(player);
                sender.sendMessage(PREFIX + "Output: " + user.hasPermission(permission));
                break;

            }

            case "backuplanguage":
            {
                try
                {
                    LanguageConfiguration.backup();
                    sender.sendMessage(PREFIX + ChatColor.GREEN + "Done");
                }
                catch (IOException e)
                {
                    sender.sendMessage(PREFIX + ChatColor.RED + "Failed: ");
                    sender.sendMessage(PREFIX + ChatColor.RED + e.getMessage());
                }
                break;
            }

            default:
            {
                sender.sendMessage(PREFIX + "Unknown option! Valid options are: getplayervalue, setplayervalue, backuplanguage");
            }
        }
        return true;
    }

    private Object replaceValue(String value)
    {
        if (value.equals("true"))
        {
            return true;
        }
        if (value.equals("false"))
        {
            return false;
        }
        if (Util.isNumber(value))
        {
            return Integer.parseInt(value);
        }
        return value;
    }
}
