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

import de.static_interface.sinklibrary.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class CommandsVerCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.BLUE + "[SinkCommands] " + ChatColor.RESET;

    Plugin plugin;

    public CommandsVerCommand(Plugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> authorsList = plugin.getDescription().getAuthors();
        String authors = Util.formatPlayerListToString(authorsList);
        sender.sendMessage(PREFIX + plugin.getDescription().getName() + " by " + authors);
        sender.sendMessage(PREFIX + "Version: " + plugin.getDescription().getVersion());
        sender.sendMessage(PREFIX + "Copyright © 2013 Adventuria");
        return true;
    }
}
