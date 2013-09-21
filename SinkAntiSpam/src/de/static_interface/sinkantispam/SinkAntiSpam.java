package de.static_interface.sinkantispam;

import de.static_interface.sinklibrary.BukkitUtil;
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * SinkAntiSpamMain
 * Auhtor: Trojaner
 * Date: 27.07.2013
 * Description: Main Class
 * Copyright © Adventuria 2013
 */

public class SinkAntiSpam extends JavaPlugin
{
    SinkLibrary sinkLibrary;

    public static String prefix = ChatColor.RED + "[SinkAntiSpam] " + ChatColor.WHITE;

    public void onEnable()
    {
        PluginManager pm = Bukkit.getPluginManager();
        sinkLibrary = (SinkLibrary) pm.getPlugin("SinkLibrary");
        if (sinkLibrary == null)
        {
            getLogger().log(Level.WARNING, "This Plugin requires SinkCommands!");
            pm.disablePlugin(this);
            return;
        }
        pm.registerEvents(new SinkAntiSpamListener(), this);
    }

    public static void warnPlayer(Player player, String reason)
    {
        player.sendMessage(prefix + ChatColor.RED + "Du wurdest automatisch für den folgenden Grund verwarnt: " + ChatColor.RESET + reason);
        BukkitUtil.broadcast(prefix + player.getDisplayName() + " wurde automatisch verwarnt. Grund: " + reason, "sinkantispam.message");
    }
}
