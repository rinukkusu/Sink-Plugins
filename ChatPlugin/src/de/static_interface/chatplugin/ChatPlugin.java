package de.static_interface.chatplugin;

import de.static_interface.chatplugin.listener.ChatListenerLowest;
import de.static_interface.chatplugin.listener.ChatListenerNormal;
import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.logging.Level;

/**
 * ChatPlugin Class
 * Author: Trojaner
 * Date: 23.09.2013
 * Description: Main Class for plugin
 * Copyright © Adventuria 2013
 */

public class ChatPlugin extends JavaPlugin
{

    public void onEnable()
    {
        PluginManager pm = Bukkit.getPluginManager();
        PermissionsEx pex = (PermissionsEx) pm.getPlugin("PermissionsEx");

        CommandsPlugin commandsPlugin = (CommandsPlugin) pm.getPlugin("CommandsPlugin");
        if (pex == null)
        {
            Bukkit.getLogger().log(Level.WARNING, "This Plugin needs PermissionsEx to work correctly");
            return;
        }
        if (commandsPlugin == null)
        {
            Bukkit.getLogger().log(Level.WARNING, "This Plugin needs CommandsPlugin to work correctly");
            return;
        }
        pm.registerEvents(new ChatListenerLowest(), this);
        pm.registerEvents(new ChatListenerNormal(), this);
    }

    public void onDisable()
    {
        Bukkit.getLogger().log(Level.INFO, "Disabled.");
    }
}
