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

import de.static_interface.sinkcommands.SinkCommands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LagCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.DARK_PURPLE + "[Lag] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        double realTPS = SinkCommands.getCommandsTimer().getAverageTPS();
        double shownTPS = Math.round(realTPS);
        if ( realTPS >= 18.5 )
        {
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Der Server läuft ohne Probleme!");
        }
        else if ( realTPS >= 17 )
        {
            sender.sendMessage(PREFIX + ChatColor.YELLOW + "Der Server könnte gerade etwas laggen!");
        }
        else
        {
            sender.sendMessage(PREFIX + ChatColor.RED + "Der Server laggt gerade!");
        }
        sender.sendMessage(PREFIX + "(TPS: " + shownTPS + ")");
        return true;
    }
}

