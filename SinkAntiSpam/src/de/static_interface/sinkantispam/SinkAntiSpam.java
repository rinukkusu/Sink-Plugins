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

package de.static_interface.sinkantispam;

import de.static_interface.sinklibrary.BukkitUtil;
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class SinkAntiSpam extends JavaPlugin
{
    public static String prefix = ChatColor.RED + "[SinkAntiSpam] " + ChatColor.WHITE;

    public void onEnable()
    {
        if (! checkDependencies())
        {
            return;
        }
        SinkLibrary.registerPlugin(this);
        Bukkit.getPluginManager().registerEvents(new SinkAntiSpamListener(), this);
    }

    public static void warnPlayer(Player player, String reason)
    {
        player.sendMessage(prefix + ChatColor.RED + "Du wurdest automatisch für den folgenden Grund verwarnt: " + ChatColor.RESET + reason);
        BukkitUtil.broadcast(prefix + player.getDisplayName() + " wurde automatisch verwarnt. Grund: " + reason, "sinkantispam.message");
    }


    private boolean checkDependencies()
    {
        if (Bukkit.getPluginManager().getPlugin("SinkLibrary") == null)
        {
            getLogger().log(Level.WARNING, "This Plugin requires SinkLibrary!");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }
}
