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

package de.static_interface.sinklibrary.configuration;

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class LanguageConfiguration
{
    public static final int REQUIRED_VERSION = 1;

    private static YamlConfiguration yamlConfiguration = new YamlConfiguration();
    private static HashMap<String, Object> defaultValues;
    private static File yamlFile;
    private static boolean busy;

    /**
     * Initialize
     */
    public static void init()
    {
        defaultValues = new HashMap<>();
        create();
    }

    /**
     * Create Configuration
     */
    public static void create()
    {
        try
        {
            yamlFile = new File(SinkLibrary.getCustomDataFolder(), "Language.yml");

            boolean createNewConfiguration = !exists();

            if ( createNewConfiguration )
            {
                Bukkit.getLogger().log(Level.INFO, "Creating new configuration: " + yamlFile);
            }

            if ( createNewConfiguration && !yamlFile.createNewFile() )
            {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't create configuration: " + yamlFile);
                return;
            }

            yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(yamlFile);

            if ( !createNewConfiguration )
            {
                int version = (int) get("Main.ConfigVersion");
                if ( version < REQUIRED_VERSION )
                {
                    Bukkit.getLogger().log(Level.WARNING, "***************");
                    Bukkit.getLogger().log(Level.WARNING, "Configuration: " + yamlFile + " is too old! Current Version: " + version + ", required Version: " + REQUIRED_VERSION);
                    recreate();
                    Bukkit.getLogger().log(Level.WARNING, "***************");
                    return;
                }
            }

            getYamlConfiguration().options().header(String.format("This configuration saves and loads variables for language.%n%%s will be replaced with the value by the plugin."));

            addDefault("Main.ConfigVersion", REQUIRED_VERSION);
            addDefault("General.NotOnline", "&c%s is not online!");
            addDefault("General.ConsoleNotAvailabe", "&cThis command is only ingame available");

            addDefault("SinkChat.Commands.Nick.OtherChanged", "%s's name is now %s!");
            addDefault("SinkChat.Commands.Nick.SelfChanged", "Your name is now %s!");
            addDefault("SinkChat.Commands.Nick.IllegalNickname", "Illegal nickname!");
            addDefault("SinkChat.Commands.Nick.TooLong", "Nickname is too long!");
            addDefault("SinkChat.Commands.Nick.Used", "Nickname is already used by someone other!");

            addDefault("SinkChat.Commands.Channel.PlayerJoins", "You joined the %s channel.");
            addDefault("SinkChat.Commands.Channel.PlayerLeaves", "You left the %s channel.");
            addDefault("SinkChat.Commands.Channel.NoChannelGiven", "You must write the name of the channel!");
            addDefault("SinkChat.Commands.Channel.ChannelUnknown", "%s is an unknown channel.");
            addDefault("SinkChat.Commands.Channel.List", "These channels are available: %s");
            addDefault("SinkChat.Commands.Channel.Part", "You have the following channels enabled:");
            addDefault("SinkChat.Commands.Channel.Help", "These commands are available:");

            addDefault("SinkChat.Commands.Spy.Enabled", "&aSpy chat has been enabled!");
            addDefault("SinkChat.Commands.Spy.AlreadyEnabled", "&cSpy chat has been already enabled!");

            addDefault("SinkChat.Commands.Spy.Disabled", "&cSpy chat has been disabled!");
            addDefault("SinkChat.Commands.Spy.AlreadyDisabled", "&cSpy chat has been already disabled!");

            addDefault("SinkChat.Channels.Help", "Help");
            addDefault("SinkChat.Channels.Shout", "Shout");
            addDefault("SinkChat.Channels.Trade", "Trade");

            addDefault("SinkChat.Channels.Private.InvitedToChat", "%s invited you to a chat. Chat with %s");
            addDefault("SinkChat.Channels.Private.HasInvitedToChat", "You have invited %s to chat.");
            addDefault("SinkChat.Channels.Private.HasInvitedToChat.ErrorAlreadyInChat", "%s already takes part in that conversation !");
            addDefault("SinkChat.Channels.Private.HasInvitedToChat.ErrorNotOnline", "%s is not online !");
            addDefault("SinkChat.Channels.Private.LeftChat", "You have left the private conversation %s!");
            addDefault("SinkChat.Channels.Private.PlayerLeftCon", "%s has left conversation %s");
            addDefault("SinkChat.Channels.Private.PlayerKicked", "%s has been kicked: %s");
            addDefault("SinkChat.Channels.Private.PlayerKicked.ErrorNotInChannel", "%s is not in that conversation!");

            addDefault("Permissions.General", "&4You dont have permissions to do that.");
            addDefault("Permissions.SinkChat.Channels.Shout", "&4You may not use the shout channel.");
            addDefault("Permissions.SinkChat.Channels.Help", "&4You may not use the help channel.");
            addDefault("Permissions.SinkChat.Channels.Trade", "&4You may not use the trade channel.");
            addDefault("Permissions.SinkChat.Nick.Other", "&4You may not change the nickname of other players!");

            addDefault("SinkChat.Prefix.Channel", "&a[Channel]");
            addDefault("SinkChat.Prefix.Nick", "&2[Nick]");
            addDefault("SinkChat.Prefix.Spy", "&7[Spy]");
            addDefault("SinkChat.Prefix.Local", "&7[Local]");

            save();
        }
        catch ( IOException e )
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't create configuration file: " + yamlFile.getName());
            Bukkit.getLogger().log(Level.SEVERE, "Exception occured: ", e);
        }
        catch ( InvalidConfigurationException e )
        {
            Bukkit.getLogger().log(Level.SEVERE, "***************");
            Bukkit.getLogger().log(Level.SEVERE, "Invalid configuration file detected: " + yamlFile);
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage());
            Bukkit.getLogger().log(Level.SEVERE, "***************");
            recreate();
        }
    }

    /**
     * Save configuration
     */
    public static void save()
    {
        if ( yamlFile == null )
        {
            return;
        }
        if ( !exists() )
        {
            return;
        }

        try
        {
            yamlConfiguration.save(yamlFile);
        }
        catch ( IOException e )
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't save configuration file: " + yamlFile + "!");
        }
    }

    /**
     * Get Value from path
     *
     * @param path Path to value
     * @return Value of Path
     */
    public static Object get(String path)
    {
        try
        {
            Object value = yamlConfiguration.get(path);
            if ( value == null || value == "" )
            {
                throw new NullPointerException("Path returned null!");
            }
            return value;
        }
        catch ( Exception e )
        {
            Bukkit.getLogger().log(Level.WARNING, getFile() + ": Couldn't load value from path: " + path + ". Reason: " + e.getMessage() + " Using default value.");
            return getDefault(path);
        }
    }

    /**
     * @return True if file exists
     */
    public static boolean exists()
    {
        return yamlFile.exists();
    }

    /**
     * Delete configuration
     */
    public static void delete()
    {
        yamlFile.delete();
    }

    /**
     * Backup configuration
     */
    public static void backup() throws IOException
    {
        Util.backupFile(getFile(), true);
    }

    /**
     * Get default values
     *
     * @return Default Values
     */
    public static HashMap<String, Object> getDefaults()
    {
        return defaultValues;
    }

    /**
     * Get language as String from key
     *
     * @param path Path to language variable
     * @return Language String
     */
    public static String _(String path)
    {
        String value;
        try
        {
            String rawValue = yamlConfiguration.getRoot().getString(path);
            if ( rawValue == null || rawValue.equals("null") )
            {
                throw new NullPointerException("Path returned null.");
            }
            value = ChatColor.translateAlternateColorCodes('&', rawValue);
        }
        catch ( Exception e )
        {
            Bukkit.getLogger().log(Level.WARNING, yamlFile.getName() + ": Couldn't load value from path: " + path + ". Reason: " + e.getMessage() + " Using default value.");
            value = (String) getDefault(path);
        }
        return value;
    }

    /**
     * Get Default
     *
     * @param path Path to default value
     * @return Default value
     */
    public static Object getDefault(String path)
    {
        return getDefaults().get(path);
    }

    /**
     * Add default value
     *
     * @param path  Path to default value
     * @param value Value of Path
     */
    public static void addDefault(String path, Object value)
    {
        if ( getDefaults() == null )
        {
            throw new RuntimeException("defaultValues are null! Couldn't add " + value + " to path: " + path);
        }

        if ( !getYamlConfiguration().isSet(path) || getYamlConfiguration().get(path) == null )
        {
            getYamlConfiguration().set(path, value);
            save();
        }
        getDefaults().put(path, value);
    }

    /**
     * Get YAML Configuration
     *
     * @return YamlConfiguration
     */
    public static YamlConfiguration getYamlConfiguration()
    {
        return yamlConfiguration;
    }

    /**
     * Get File
     *
     * @return Configuration File
     */
    public static File getFile()
    {
        return yamlFile;
    }

    /**
     * Recreate configuration
     */
    public static void recreate()
    {
        if ( busy ) return;

        busy = true;

        Bukkit.getLogger().log(Level.WARNING, "Recreating Configuration: " + getFile());
        try
        {
            backup();
        }
        catch ( IOException e )
        {
            Bukkit.getLogger().log(Level.SEVERE, "Coudln't backup configuration: " + getFile(), e);
            return;
        }
        delete();
        create();

        busy = false;
    }
}
