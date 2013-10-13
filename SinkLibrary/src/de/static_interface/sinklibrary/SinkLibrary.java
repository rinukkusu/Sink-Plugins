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

import de.static_interface.sinkirc.SinkIRC;
import de.static_interface.sinklibrary.configuration.LanguageConfiguration;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import de.static_interface.sinklibrary.configuration.Settings;
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
import java.util.List;
import java.util.logging.Level;

public class SinkLibrary extends JavaPlugin
{

    public static List<String> tmpBannedPlayers;

    private static Economy econ;
    private static Permission perm;
    private static Chat chat;
    private static SinkIRC irc;
    private static File dataFolder;

    private static boolean economyAvailable = true;
    private static boolean permissionsAvailable = true;
    private static boolean chatAvailable = true;

    private static String pluginName;
    private static Settings settings;
    private static List<JavaPlugin> registeredPlugins;
    private static String version;

    public void onEnable()
    {
        if (! vaultAvailable())
        {
            Bukkit.getLogger().warning("Vault Plugin not found. Disabling economy and some permission features.");
            permissionsAvailable = false;
            economyAvailable = false;
            chatAvailable = false;
        }
        else
        {
            if (! setupChat())
            {
                chatAvailable = false;
            }

            if (! setupEcononmy())
            {
                Bukkit.getLogger().warning("Economy Plugin not found. Disabling economy features.");
                economyAvailable = false;
            }
            if (! setupPermissions())
            {
                Bukkit.getLogger().warning("Permissions Plugin not found. Disabling permissions features.");
                permissionsAvailable = false;
            }
        }

        pluginName = this.getDescription().getName();
        dataFolder = getDataFolder();

        if (! getCustomDataFolder().exists())
        {
            try
            {
                getCustomDataFolder().mkdirs();
            }
            catch (Exception e)
            {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't create Data Folder!", e);
            }
        }

        if (chatAvailable && economyAvailable && permissionsAvailable)
        {
            Bukkit.getLogger().info("Successfully hooked into permissions, economy and chat.");
        }

        LanguageConfiguration.init();
        settings = new Settings();

        irc = (SinkIRC) Bukkit.getPluginManager().getPlugin("SinkIRC");
        version = getDescription().getVersion();
        tmpBannedPlayers = new ArrayList<>();
        registeredPlugins = new ArrayList<>();

        Bukkit.getPluginManager().registerEvents(new PlayerConfigurationListener(), this);
        Bukkit.getPluginManager().registerEvents(new DisplayNameListener(), this);
        getCommand("sinkdebug").setExecutor(new SinkDebugCommand());

        for (Player p : Bukkit.getOnlinePlayers())
        {
            refreshDisplayName(p);
        }

        update();
    }

    /**
     * Update SinkPlugins
     */
    private void update()
    {
        Updater updater = new Updater(getSettings().getUpdateType());
        String permission = "sinklibrary.updatenotification";
        String versionType = " " + updater.getLatestGameVersion() + " ";
        if (versionType.equalsIgnoreCase("release")) versionType = " ";
        if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE)
        {
            BukkitUtil.broadcast(Updater.PREFIX + "A new" + versionType + "update is available: " + updater.getLatestName(), permission);
        }
        else if (updater.getResult() == Updater.UpdateResult.NO_UPDATE)
        {
            Bukkit.getLogger().info(Updater.PREFIX + "No new updates found...");
        }
    }

    public void onDisable()
    {
        Bukkit.getLogger().info("Saving players...");
        for (Player p : Bukkit.getOnlinePlayers())
        {
            User user = new User(p);
            if (user.getPlayerConfiguration().exists())
            {
                user.getPlayerConfiguration().save();
            }
        }
        Bukkit.getLogger().info("Disabled.");
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null)
        {
            chat = chatProvider.getProvider();
        }
        return ( chat != null );
    }

    private boolean setupEcononmy()
    {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null)
        {
            perm = permissionProvider.getProvider();
        }
        return ( perm != null );
    }

    /**
     * @return True if chat is available
     */
    public static boolean chatAvailable()
    {
        return chatAvailable;
    }

    /**
     * @return True if economy is available
     */
    public static boolean economyAvailable()
    {
        return economyAvailable;
    }

    /**
     * @return True if SinkIRC is available
     */
    public static boolean sinkIRCAvailable()
    {
        return getSinkIRC() != null;
    }

    /**
     * @return True if permissions are available
     */
    public static boolean permissionsAvailable()
    {
        return permissionsAvailable;
    }

    /**
     * @return True if Vault available
     */
    public static boolean vaultAvailable()
    {
        return Bukkit.getPluginManager().getPlugin("Vault") != null;
    }

    /**
     * Get Chat instance
     *
     * @return Chat instance
     * @see Chat
     */
    public static Chat getChat()
    {
        return chat;
    }

    /**
     * Get Economy instance from Vault
     *
     * @return Economy instace
     * @see Economy
     */
    public static Economy getEconomy()
    {
        return econ;
    }

    /**
     * Get Permissions instance
     *
     * @return Permissions
     * @see Permission
     */
    public static Permission getPermissions()
    {
        return perm;
    }

    /**
     * Get SinkIRC Instance
     *
     * @return SinkIRC Instance
     * @see SinkIRC
     */
    public static SinkIRC getSinkIRC()
    {
        return irc;
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
     */
    public static void sendIRCMessage(String message)
    {
        if (irc != null)
        {
            SinkIRC.getIRCBot().sendCleanMessage(SinkIRC.getChannel(), message);
        }
    }

    /**
     * Add Temp Ban to Player. Will be cleared on next server restart or plugin reload.
     *
     * @param username Player to ban
     */
    public static void addTempBan(String username)
    {
        tmpBannedPlayers.add(username);
    }

    /**
     * Remove Temp Ban
     *
     * @param username Player to unban
     */
    public static void removeTempBan(String username)
    {
        tmpBannedPlayers.remove(username);
    }

    /**
     * Get SinkPlugins Settings.
     *
     * @return Settings
     */
    @SuppressWarnings("UnusedDeclaration")
    public static Settings getSettings()
    {
        return settings;
    }

    /**
     * Register a plugin
     *
     * @param plugin Class which extends {@link org.bukkit.plugin.java.JavaPlugin}
     */
    public static void registerPlugin(JavaPlugin plugin)
    {
        registeredPlugins.add(plugin);
    }

    /**
     * @param player Player
     * @return User instance of player
     */
    public static User getUser(Player player)
    {
        return new User(player);
    }

    /**
     * @param playerName Name of Player
     * @return User instance
     */
    public static User getUser(String playerName)
    {
        return new User(playerName);
    }

    /**
     * @param sender Command Sender
     * @return User instance
     */
    public static User getUser(CommandSender sender)
    {
        return new User(sender);
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
     */
    public static void refreshDisplayName(Player player)
    {
        if (! getSettings().getDisplayNamesEnabled()) return;
        String nickname;
        User user = SinkLibrary.getUser(player);
        PlayerConfiguration config = user.getPlayerConfiguration();

        if (! config.exists())
        {
            return;
        }

        nickname = config.getDisplayName();

        if (nickname == null || nickname.equals("null") || nickname.equals(""))
        {
            config.setDisplayName(user.getDefaultDisplayName());
            config.setHasDisplayName(false);
        }
        else if (config.getHasDisplayName())
        {
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
}
