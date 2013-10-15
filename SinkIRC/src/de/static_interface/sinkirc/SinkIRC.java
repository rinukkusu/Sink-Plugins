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

package de.static_interface.sinkirc;

import de.static_interface.sinkirc.commands.IrclistCommand;
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jibble.pircbot.IrcException;

import java.io.IOException;
import java.util.logging.Level;

public class SinkIRC extends JavaPlugin
{

    static IRCBot ircBot;

    @Override
    public void onEnable()
    {
        if ( !checkDependencies() ) return;

        ircBot = new IRCBot(this);

        SinkLibrary.registerPlugin(this);
        String host = "irc.adventuria.eu";

        try
        {
            int port = 6667;
            ircBot.connect(host, port);
            //Todo: Add NickServ identification support
            ircBot.joinChannel(getMainChannel());
        }
        catch ( IOException | IrcException e )
        {
            Bukkit.getLogger().severe("An Exception occurred while trying to connect to " + host + ":");
            Bukkit.getLogger().severe(e.getMessage());
        }
        getCommand("irclist").setExecutor(new IrclistCommand());
        Bukkit.getPluginManager().registerEvents(new IRCListener(ircBot), this);
    }

    private boolean checkDependencies()
    {
        if ( Bukkit.getPluginManager().getPlugin("SinkLibrary") == null )
        {
            getLogger().log(Level.WARNING, "This Plugin requires SinkLibrary!");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }

        if ( Bukkit.getPluginManager().getPlugin("SinkChat") == null )
        {
            Bukkit.getLogger().log(Level.WARNING, "This plugin will not work without SinkChat.");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }

        return true;
    }

    @Override
    public void onDisable()
    {
        ircBot.quitServer("Plugin reload or plugin has been deactivated");
    }

    public static IRCBot getIRCBot()
    {
        return ircBot;
    }

    public static String getMainChannel()
    {
        return "#AdventuriaBot";
    }
}
