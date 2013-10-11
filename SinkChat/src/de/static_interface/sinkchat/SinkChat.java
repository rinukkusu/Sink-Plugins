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

package de.static_interface.sinkchat;

import de.static_interface.sinkchat.channel.IChannel;
import de.static_interface.sinkchat.channel.channels.HelpChannel;
import de.static_interface.sinkchat.channel.channels.ShoutChannel;
import de.static_interface.sinkchat.channel.channels.TradeChannel;
import de.static_interface.sinkchat.command.ChannelCommand;
import de.static_interface.sinkchat.command.NickCommand;
import de.static_interface.sinkchat.command.SpyCommands;
import de.static_interface.sinkchat.listener.ChatListenerLowest;
import de.static_interface.sinkchat.listener.ChatListenerNormal;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

import static de.static_interface.sinklibrary.configuration.LanguageConfiguration._;

public class SinkChat extends JavaPlugin
{
    public void onEnable()
    {
        if (! checkDependencies())
        {
            return;
        }

        SinkLibrary.registerPlugin(this);

        for (Player p : Bukkit.getOnlinePlayers())
        {
            refreshDisplayName(p);
        }

        IChannel fc = new HelpChannel(_("SinkChat.Channel.Help.Prefix"));
        IChannel sc = new ShoutChannel(_("SinkChat.Channel.Shout.Prefix"));
        IChannel hc = new TradeChannel(_("SinkChat.Channel.Trade.Prefix"));

        sc.registerChannel();
        hc.registerChannel();
        fc.registerChannel();

        registerEvents(Bukkit.getPluginManager());
        registerCommands();
    }

    private boolean checkDependencies()
    {

        PluginManager pm = Bukkit.getPluginManager();
        SinkLibrary sinkLibrary;

        try
        {
            sinkLibrary = (SinkLibrary) pm.getPlugin("SinkLibrary");
        }
        catch (NoClassDefFoundError ignored)
        {
            sinkLibrary = null;
        }
        if (sinkLibrary == null)
        {
            getLogger().log(Level.WARNING, "This Plugin requires SinkLibrary!");
            pm.disablePlugin(this);
            return false;
        }

        return true;
    }

    /**
     * Refresh Player DisplayName
     */
    public static void refreshDisplayName(Player player)
    {
        String nickname;
        User user = SinkLibrary.getUser(player);
        PlayerConfiguration config = user.getPlayerConfiguration();

        if (! config.exists())
        {
            return;
        }

        if (config.getHasDisplayName())
        {
            nickname = config.getDisplayName();
            if (nickname.equals(user.getDefaultDisplayName()))
            {
                config.setHasDisplayName(false);
            }
        }
        else
        {
            nickname = user.getDefaultDisplayName();
        }
        player.setDisplayName(nickname);
        player.setCustomName(nickname);
    }

    private void registerEvents(PluginManager pm)
    {
        pm.registerEvents(new ChatListenerLowest(), this);
        pm.registerEvents(new ChatListenerNormal(), this);
    }

    private void registerCommands()
    {
        getCommand("nick").setExecutor(new NickCommand());
        getCommand("channel").setExecutor(new ChannelCommand());
        getCommand("enablespy").setExecutor(new SpyCommands.EnableSpyCommand());
        getCommand("disablespy").setExecutor(new SpyCommands.DisablSpyCommand());
    }
}
