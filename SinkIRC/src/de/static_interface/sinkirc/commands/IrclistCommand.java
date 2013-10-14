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

package de.static_interface.sinkirc.commands;

import de.static_interface.sinkirc.SinkIRC;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jibble.pircbot.User;

public class IrclistCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.YELLOW + "[IRC] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        User[] users = SinkIRC.getIRCBot().getUsers(SinkIRC.getMainChannel());
        String message = "";
        for ( User user : users )
        {
            String name = user.getNick();
            if ( name.equals(SinkIRC.getIRCBot().getNick()) )
            {
                continue;
            }
            if ( user.isOp() )
            {
                name = ChatColor.RED + name + ChatColor.RESET;
            }
            if ( message.equals("") )
            {
                message = name;
            }
            else
            {
                message = message + ", " + name;
            }
        }
        if ( users.length <= 1 )
        {
            message = "Zur Zeit sind keine Benutzer im IRC.";
        }
        sender.sendMessage(PREFIX + "Online IRC Benutzer: " + message);
        return true;
    }
}
