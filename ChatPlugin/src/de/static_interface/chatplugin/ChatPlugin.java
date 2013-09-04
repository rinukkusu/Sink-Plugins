package de.static_interface.chatplugin;

import de.static_interface.chatplugin.command.NickCommand;
import de.static_interface.chatplugin.listener.ChatListenerLowest;
import de.static_interface.chatplugin.listener.ChatListenerNormal;
import de.static_interface.chatplugin.listener.NicknameListener;
import de.static_interface.commandsplugin.CommandsPlugin;
import de.static_interface.commandsplugin.PlayerConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.logging.Level;

import static de.static_interface.chatplugin.command.NickCommand.NICKNAME_PATH;

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
        for (Player p : Bukkit.getOnlinePlayers())
        {
            refreshDisplayName(p);
        }
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
        registerEvents(pm);
        registerCommands();
    }

    private void registerCommands()
    {
        getCommand("nick").setExecutor(new NickCommand());
    }

    public void onDisable()
    {
        Bukkit.getLogger().log(Level.INFO, "Disabled.");
    }

    public static String getGroup(Player player)
    {
        PermissionUser user = PermissionsEx.getUser(player);
        return user.getGroupsNames()[0];
    }


    public static void setDisplayName(Player player, String newNickname)
    {
        player.setDisplayName(newNickname);
        player.setCustomName(newNickname);
        PlayerConfiguration config = new PlayerConfiguration(player.getName());
        config.set(NICKNAME_PATH, newNickname);
    }

    public static String getNickname(Player player)
    {
        PlayerConfiguration config = new PlayerConfiguration(player.getName());
        return config.get(NICKNAME_PATH).toString(); //NoSuchMethodException
    }

    public static String getDefaultNickName(Player player)
    {
        PermissionUser user = PermissionsEx.getUser(player);
        String playerPrefix = ChatColor.translateAlternateColorCodes('&', user.getPrefix());
        return playerPrefix + player.getPlayerListName() + ChatColor.RESET;
    }

    public static void refreshDisplayName(Player player)
    {
        player.setDisplayName(getNickname(player));
        player.setCustomName(getNickname(player));
    }

    private void registerEvents(PluginManager pm)
    {
        pm.registerEvents(new ChatListenerLowest(), this);
        pm.registerEvents(new ChatListenerNormal(), this);
        pm.registerEvents(new NicknameListener(), this);
    }
}
