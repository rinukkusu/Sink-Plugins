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
import de.static_interface.sinklibrary.BukkitUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GlobalmuteCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.DARK_RED + "[GlobalMute] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        SinkCommands.globalmuteEnabled = !SinkCommands.globalmuteEnabled;

        if ( SinkCommands.globalmuteEnabled )
        {
            if ( args.length > 0 )
            {
                String reason = "";
                for ( String arg : args )
                {
                    if ( reason.equals("") )
                    {
                        reason = arg;
                        continue;
                    }
                    reason = reason + " " + arg;
                }
                BukkitUtil.broadcastMessage(PREFIX + "Der globale Mute wurde von " + BukkitUtil.getSenderName(sender) + " aktiviert. Grund: " + reason + ". Alle Spieler sind jetzt stumm.");
            }
            else
            {
                BukkitUtil.broadcastMessage(PREFIX + "Der global Mute wurde von " + BukkitUtil.getSenderName(sender) + " aktiviert. Alle Spieler sind jetzt stumm.");
            }
            SinkCommands.globalmuteEnabled = true;
            return true;
        }
        else
        {
            BukkitUtil.broadcastMessage(PREFIX + "Der global Mute wurde von " + BukkitUtil.getSenderName(sender) + " deaktiviert.");
        }
        return true;
    }
}
