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

package de.static_interface.sinklibrary;

import de.static_interface.sinklibrary.commands.SinkDebugCommand;
import de.static_interface.sinklibrary.commands.SinkReloadCommand;
import de.static_interface.sinklibrary.configuration.LanguageConfiguration;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import de.static_interface.sinklibrary.configuration.Settings;
import de.static_interface.sinklibrary.events.IRCMessageEvent;
import de.static_interface.sinklibrary.exceptions.NotInitializedException;
import de.static_interface.sinklibrary.listener.DisplayNameListener;
import de.static_interface.sinklibrary.listener.PlayerConfigurationListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class SinkLibrary extends JavaPlugin
{

    public static List<String> tmpBannedPlayers;

    private static Economy econ;
    private static Permission perm;
    private static Chat chat;
    private static File dataFolder;

    private static boolean economyAvailable = true;
    private static boolean permissionsAvailable = true;
    private static boolean chatAvailable = true;

    private static String pluginName;
    private static Settings settings;
    private static List<JavaPlugin> registeredPlugins;
    private static String version;

    private static boolean initialized;

    private static HashMap<String, User> users;

    public void onEnable()
    {
        if ( !isVaultAvailable() )
        {
            Bukkit.getLogger().warning("Vault Plugin not found. Disabling economy and some permission features.");
            permissionsAvailable = false;
            economyAvailable = false;
            chatAvailable = false;
        }
        else
        {
            if ( !setupChat() )
            {
                chatAvailable = false;
            }

            if ( !setupEcononmy() )
            {
                Bukkit.getLogger().warning("Economy Plugin not found. Disabling economy features.");
                economyAvailable = false;
            }
            if ( !setupPermissions() )
            {
                Bukkit.getLogger().warning("Permissions Plugin not found. Disabling permissions features.");
                permissionsAvailable = false;
            }
        }

        pluginName = this.getDescription().getName();
        dataFolder = getDataFolder();
        users = new HashMap();

        if ( !getCustomDataFolder().exists() )
        {
            try
            {
                getCustomDataFolder().mkdirs();
            }
            catch ( Exception e )
            {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't create Data Folder!", e);
            }
        }

        if ( chatAvailable && economyAvailable && permissionsAvailable )
        {
            Bukkit.getLogger().info("Successfully hooked into permissions, economy and chat.");
        }

        LanguageConfiguration.load();
        settings = new Settings();
        version = getDescription().getVersion();
        tmpBannedPlayers = new ArrayList<>();
        registeredPlugins = new ArrayList<>();

        for ( Player p : Bukkit.getOnlinePlayers() )
        {
            refreshDisplayName(p);
        }

        registerPlugin(this);

        registerListeners();
        registerCommands();

        update();

        initialized = true;
    }

    /**
     * Update SinkPlugins
     */
    private void update()
    {
        Updater updater = new Updater(getSettings().getUpdateType());
        String permission = "sinklibrary.updatenotification";
        String versionType = " " + updater.getLatestGameVersion() + " ";
        if ( versionType.equalsIgnoreCase("release") ) versionType = " ";
        if ( updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE )
        {
            BukkitUtil.broadcast(Updater.CONSOLEPREFIX + "A new" + versionType + "update is available: " + updater.getLatestName(), permission);
        }
        else if ( updater.getResult() == Updater.UpdateResult.NO_UPDATE )
        {
            Bukkit.getLogger().info(Updater.CONSOLEPREFIX + "No new updates found...");
        }
        else if ( updater.getResult() == Updater.UpdateResult.SUCCESS )
        {
            Bukkit.getLogger().info(Updater.CONSOLEPREFIX + "Updates downloaded, please restart or reload the server to take effect...");
        }
    }

    public void onDisable()
    {
        Bukkit.getLogger().info("Saving players...");
        for ( Player p : Bukkit.getOnlinePlayers() )
        {
            User user = new User(p);
            if ( user.getPlayerConfiguration().exists() )
            {
                user.getPlayerConfiguration().save();
            }
        }
        Bukkit.getLogger().info("Disabled.");
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if ( chatProvider != null )
        {
            chat = chatProvider.getProvider();
        }
        return (chat != null);
    }

    private boolean setupEcononmy()
    {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if ( rsp == null )
        {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if ( permissionProvider != null )
        {
            perm = permissionProvider.getProvider();
        }
        return (perm != null);
    }

    private void registerListeners()
    {
        Bukkit.getPluginManager().registerEvents(new PlayerConfigurationListener(), this);
        Bukkit.getPluginManager().registerEvents(new DisplayNameListener(), this);
    }

    private void registerCommands()
    {
        getCommand("sinkdebug").setExecutor(new SinkDebugCommand());
        getCommand("sinkreload").setExecutor(new SinkReloadCommand(this));
    }

    /**
     * @return True if chat is available
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static boolean isChatAvailable()
    {
        if ( !isReady() ) throw new NotInitializedException();
        return chatAvailable;
    }

    /**
     * @return True if economy is available
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static boolean isEconomyAvailable()
    {
        if ( !isReady() ) throw new NotInitializedException();
        return economyAvailable;
    }

    /**
     * @return True if permissions are available
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static boolean isPermissionsAvailable()
    {
        if ( !isReady() ) throw new NotInitializedException();
        return permissionsAvailable;
    }

    /**
     * @return True if Vault available
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static boolean isVaultAvailable()
    {
        if ( !isReady() ) throw new NotInitializedException();
        return Bukkit.getPluginManager().getPlugin("Vault") != null;
    }

    /**
     * Get Chat instance
     *
     * @return Chat instance
     * @see Chat
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static Chat getChat()
    {
        if ( !isReady() ) throw new NotInitializedException();
        return chat;
    }

    /**
     * Get Economy instance from Vault
     *
     * @return Economy instace
     * @see Economy
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static Economy getEconomy()
    {
        if ( !isReady() ) throw new NotInitializedException();
        return econ;
    }

    /**
     * Get Permissions instance
     *
     * @return Permissions
     * @see Permission
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static Permission getPermissions()
    {
        if ( !isReady() ) throw new NotInitializedException();
        return perm;
    }

    /**
     * Get custom data folder
     *
     * @return Data Folder of Sink Plugins (.../plugins/SinkPlugins/)
     */
    public static File getCustomDataFolder()
    {
        return new File(dataFolder.getAbsolutePath().replace(pluginName, "SinkPlugins"));
    }

    /**
     * Send Message to IRC via SinkIRC Plugin.
     *
     * @param message Message to send
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static boolean sendIRCMessage(String message)
    {
        if ( !isReady() ) throw new NotInitializedException();
        if ( !getRegisteredPlugins().contains("SinkIRC") ) return false;
        IRCMessageEvent event = new IRCMessageEvent(message);
        Bukkit.getPluginManager().callEvent(event);
        return true;
    }

    /**
     * Add Temp Ban to Player. Will be cleared on next server restart or plugin reload.
     *
     * @param username Player to ban
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static void addTempBan(String username)
    {
        if ( !isReady() ) throw new NotInitializedException();
        tmpBannedPlayers.add(username);
    }

    /**
     * Remove Temp Ban
     *
     * @param username Player to unban
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static void removeTempBan(String username)
    {
        if ( !isReady() ) throw new NotInitializedException();
        tmpBannedPlayers.remove(username);
    }

    /**
     * Get SinkPlugins Settings.
     *
     * @return Settings
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static Settings getSettings()
    {
        if ( !isReady() ) throw new NotInitializedException();
        return settings;
    }

    /**
     * Register a plugin
     *
     * @param plugin Class which extends {@link org.bukkit.plugin.java.JavaPlugin}
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static void registerPlugin(JavaPlugin plugin)
    {
        if ( !isReady() ) throw new NotInitializedException();
        registeredPlugins.add(plugin);
    }

    /**
     * @param player Player
     * @return User instance of player
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static User getUser(Player player)
    {
        if ( !isReady() ) throw new NotInitializedException();
        if ( !users.containsKey(player.getName()) )
        {
            users.put(player.getName(), new User(player));
        }
        return users.get(player.getName()); //new User(player);
    }

    /**
     * @param playerName Name of Player
     * @return User instance
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static User getUser(String playerName)
    {
        if ( !isReady() ) throw new NotInitializedException();
        if ( !users.containsKey(playerName) )
        {
            users.put(playerName, new User(playerName));
        }
        return users.get(playerName);
    }

    /**
     * @param sender Command Sender
     * @return User instance
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static User getUser(CommandSender sender)
    {
        if ( !isReady() ) throw new NotInitializedException();
        if ( !users.containsKey(sender.getName()) )
        {
            users.put(sender.getName(), new User(sender));
        }
        return users.get(sender.getName());
    }

    /**
     * Get Version of SinkLibrary
     *
     * @return Version
     */
    public static String getVersion()
    {
        return version;
    }

    /**
     * Refresh Player DisplayName
     * @throws de.static_interface.sinklibrary.exceptions.NotInitializedException if the plugin hasn't initialized. See {@link SinkLibrary#isReady()}
     */
    public static void refreshDisplayName(Player player)
    {
        if ( !isReady() ) throw new NotInitializedException();
        if ( !getSettings().getDisplayNamesEnabled() ) return;
        User user = SinkLibrary.getUser(player);
        PlayerConfiguration config = user.getPlayerConfiguration();

        if ( !config.exists() )
        {
            return;
        }

        String nickname = user.getDisplayName();

        if ( nickname == null || nickname.equals("null") || nickname.equals("") )
        {
            config.setDisplayName(user.getDefaultDisplayName());
            config.setHasDisplayName(false);
        }
        else
        {
            nickname = user.getDefaultDisplayName();
        }

        if ( nickname.equals(user.getDefaultDisplayName()) )
        {
            config.setHasDisplayName(false);
        }

        player.setDisplayName(nickname);
        player.setCustomName(nickname);
    }

    /**
     * @return Plugins which are registered through {@link SinkLibrary#registerPlugin(org.bukkit.plugin.java.JavaPlugin)}
     */
    public static List<JavaPlugin> getRegisteredPlugins()
    {
        if ( !isReady() ) throw new NotInitializedException();
        return registeredPlugins;
    }

    /**
     * @return True if the onCreate method has been executed
     */
    public static boolean isReady()
    {
        return initialized;
    }

    public static void unloadUser(User user)
    {
        user.getPlayerConfiguration().save();
        String name = user.getPlayer().getName();
        if ( name != null && !name.equals("") )
        {
            users.remove(name);
        }
    }
}
