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
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Currently unused
 */

public class Settings extends ConfigurationBase
{
    YamlConfiguration yamlConfiguration;
    File yamlFile;
    HashMap<String, Object> defaultValues;

    public Settings()
    {
        /**  Dont initialize because its not done... */
        try
        {
            yamlFile = new File(SinkLibrary.getCustomDataFolder(), "Settings.yml");
        }
        catch (NullPointerException ignored)
        {
            yamlConfiguration = null;
        }
        yamlConfiguration = new YamlConfiguration();
        defaultValues = new HashMap<>();
        load();
    }

    @Override
    public void set(String path, Object value)
    {
        if (! exists())
        {
            create();
        }
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
        if (! exists())
        {
            create();
        }
        try
        {
            return yamlConfiguration.get(path);
        }
        catch (Exception ignored)
        {
            Bukkit.getLogger().log(Level.WARNING, yamlFile.getName() + ": Couldn't load value from path: " + path);
            return getDefault(path);
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

    @Override
    public HashMap<String, Object> getDefaults()
    {
        return defaultValues;
    }

    @Override
    public boolean create()
    {
        try
        {
            if (! yamlFile.exists() && ! yamlFile.createNewFile())
            {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't create settings file");
                return false;
            }
            yamlConfiguration = YamlConfiguration.loadConfiguration(yamlFile);
            addDefault("SinkIRC.Enabled", false);
            addDefault("SinkIRC.Username", "SinkIRCBot");
            addDefault("SinkIRC.Server.Address", "irc.example.com");
            addDefault("SinkIRC.Server.Password", "");
            addDefault("SinkIRC.Server.Port", 6667);
            addDefault("SinkIRC.Channel", "#ChatBot");
            addDefault("SinkIRC.Authentification.Enabled", false); //ToDo: http://www.deaded.com/staticpages/index.php/pircbotdemos
            addDefault("SinkIRC.Authentification.AuthBot", "NickServ");
            addDefault("SinkIRC.Authentification.AuthMessage", "indentify <password>");
            addDefault("SinkIRC.Authentification.Password", "");

            addDefault("SinkChat.channel.help.prefix", "?");
            addDefault("SinkChat.channel.shout.prefix", "!");
            addDefault("SinkChat.channel.trade.prefix", "$");

            yamlConfiguration.options().copyDefaults(true);

            save();
            Bukkit.getLogger().log(Level.INFO, "Succesfully created new configuration file: " + yamlFile.getName());
            return true;
        }
        catch (Exception e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't create configuration file: " + yamlFile.getName());
            Bukkit.getLogger().log(Level.SEVERE, "Exception occured: ", e);
            return false;
        }
    }

    @Override
    public void save()
    {
        try
        {
            yamlConfiguration.save(yamlFile);
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.WARNING, "Couldn't save Settings!", e);
        }
    }
}
