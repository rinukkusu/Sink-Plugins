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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class LanguageConfiguration extends ConfigurationBase
{
    private static YamlConfiguration yamlConfiguration = new YamlConfiguration();
    private static final File yamlFile = new File(SinkLibrary.getCustomDataFolder(), "language.yml");

    @Override
    public boolean create()
    {
        try
        {
            if (! yamlFile.exists() && ! yamlFile.createNewFile())
            {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't create player config: " + yamlFile);
                return false;
            }
            yamlConfiguration = YamlConfiguration.loadConfiguration(yamlFile);
            yamlConfiguration.addDefault("messages.general.notOnline", "&c%s is not online!");
            yamlConfiguration.addDefault("messages.general.consoleNotAvailabe", "&cThis command is only ingame available");

            yamlConfiguration.addDefault("messages.commands.nick.otherChanged", "%s's name is now %s!");
            yamlConfiguration.addDefault("messages.commands.nick.selfChanged", "Your name is now %s!");
            yamlConfiguration.addDefault("messages.commands.nick.illegalNickname", "Illegal nickname!");
            yamlConfiguration.addDefault("messages.commands.nick.tooLong", "Nickname is too long!");
            yamlConfiguration.addDefault("messages.commands.nick.used", "Nickname is already used by someone other!");

            yamlConfiguration.addDefault("messages.commands.channel.playerJoins", "You joined the %s channel.");
            yamlConfiguration.addDefault("messages.commands.channel.playerLeaves", "You left the %s channel.");
            yamlConfiguration.addDefault("messages.commands.channel.noChannelGiven", "You must write the name of the channel!");
            yamlConfiguration.addDefault("messages.commands.channel.channelUnknown", "%s is an unknown channel.");
            yamlConfiguration.addDefault("messages.commands.channel.list", "These channels are available: %s");
            yamlConfiguration.addDefault("messages.commands.channel.part", "You have the following channels enabled:");
            yamlConfiguration.addDefault("messages.commands.channel.help", "These commands are available:");

            yamlConfiguration.addDefault("messages.commands.spy.enabled", "&aSpy chat has been enabled!");
            yamlConfiguration.addDefault("messages.commands.spy.alreadyEnabled", "&cSpy chat has been already enabled!");

            yamlConfiguration.addDefault("messages.commands.spy.disabled", "&cSpy chat has been disabled!");
            yamlConfiguration.addDefault("messages.commands.spy.alreadyDisabled", "&cSpy chat has been already disabled!");

            yamlConfiguration.addDefault("messages.channels.help", "Help");
            yamlConfiguration.addDefault("messages.channels.shout", "Shout");
            yamlConfiguration.addDefault("messages.channels.trade", "Trade");

            yamlConfiguration.addDefault("messages.channel.help.prefix", "?"); //Todo: move these to Settings
            yamlConfiguration.addDefault("messages.channel.shout.prefix", "!");
            yamlConfiguration.addDefault("messages.channel.trade.prefix", "$");

            yamlConfiguration.addDefault("messages.permissions.general", "&4You dont have permissions to do that.");
            yamlConfiguration.addDefault("messages.permissions.channels.shout", "&4You may not use the shout channel.");
            yamlConfiguration.addDefault("messages.permissions.channels.help", "&4You may not use the help channel.");
            yamlConfiguration.addDefault("messages.permissions.channels.trade", "&4You may not use the trade channel.");
            yamlConfiguration.addDefault("messages.permissions.nick.other", "&4You may not change the nickname of other players!");

            yamlConfiguration.addDefault("messages.prefix.channel", "&a[Channel]");
            yamlConfiguration.addDefault("messages.prefix.nick", "&2[Nick]");
            yamlConfiguration.addDefault("messages.prefix.spy", "&7[Spy]");
            yamlConfiguration.addDefault("messages.prefix.chatLocal", "&7[Local]");
            save();
            Bukkit.getLogger().log(Level.INFO, "Succesfully created new configuration file: " + yamlFile.getName());
            return true;
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't create configuration file: " + yamlFile.getName());
            Bukkit.getLogger().log(Level.SEVERE, "Exception occured: ", e);
            return false;
        }

    }

    @Override
    public void save()
    {
        if (yamlFile == null)
        {
            return;
        }

        try
        {
            yamlConfiguration.save(yamlFile);
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't save configuration file: " + yamlFile + "!");
        }
    }

    @Override
    public void set(String path, Object value)
    {
        try
        {
            yamlConfiguration.set(path, value);
        }
        catch (Exception e)
        {
            Bukkit.getLogger().log(Level.WARNING, yamlFile.getName() + ": Couldn't save " + value + " to path " + path, e);
        }
    }

    @Override
    public Object get(String path)
    {
        try
        {
            return yamlConfiguration.get(path);
        }
        catch (Exception ignored)
        {
            Bukkit.getLogger().log(Level.WARNING, yamlFile.getName() + ": Couldn't load value from path: " + path);
            return yamlConfiguration.getDefaults().get(path);
        }
    }

    @Override
    public YamlConfiguration getYamlConfiguration()
    {
        return yamlConfiguration;
    }

    @Override
    public boolean exists()
    {
        return yamlFile.exists();
    }

    @Override
    public boolean load()
    {
        try
        {
            yamlConfiguration.load(yamlFile);
        }
        catch (InvalidConfigurationException ignored)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Invalid player YAML: " + yamlFile.getName() + ", recreating...");
            yamlFile.delete();
            return create();
        }
        catch (Exception ignored)
        {
            return false;
        }
        return true;
    }

    /**
     * Get language as String from key
     *
     * @param key Key of language without "message" root
     *
     * @return Language String
     */
    public static String _(String key)
    {
        String path = "messages." + key;
        try
        {
            String value = yamlConfiguration.getRoot().getString(path);
            if (value == null || value.equals("null"))
            {
                throw new NullPointerException("key returned null. (path: " + path + ")");
            }
            return ChatColor.translateAlternateColorCodes('&', value);
        }
        catch (Exception e)
        {
            Bukkit.getLogger().log(Level.WARNING, yamlFile.getName() + ": Couldn't load value from path: " + path, e);
            return (String) yamlConfiguration.getDefaults().get(path);
        }
    }
}
