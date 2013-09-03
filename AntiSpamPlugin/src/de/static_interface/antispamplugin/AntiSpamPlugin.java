package de.static_interface.antispamplugin;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * AntiSpamPluginMain
 * Auhtor: Trojaner
 * Date: 27.07.2013
 * Description: Main Class
 * Copyright © Adventuria 2013
 */

public class AntiSpamPlugin extends JavaPlugin
{
    CommandsPlugin commandsPlugin;

    public static String prefix = ChatColor.RED + "[AntiSpamPlugin] " + ChatColor.WHITE;

    public void onEnable()
    {
        PluginManager pm = Bukkit.getPluginManager();
        commandsPlugin = (CommandsPlugin) pm.getPlugin("CommandsPlugin");
        if (commandsPlugin == null)
        {
            getLogger().log(Level.WARNING, "This Plugin requires CommandsPlugin!");
            pm.disablePlugin(this);
            return;
        }
        pm.registerEvents(new AntiSpamPluginListener(), this);
    }

    public static void warnPlayer(Player player, String reason)
    {
        player.sendMessage(prefix + ChatColor.RED + "Du wurdest automatisch für den folgenden Grund verwarnt: " + ChatColor.RESET + reason);
        CommandsPlugin.broadcast(prefix + player.getDisplayName() + " wurde automatisch verwarnt. Grund: " + reason, "antispamplugin.message");
    }
}
