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
    public static final int CURRENT_VERSION = 1;
    private YamlConfiguration yamlConfiguration;
    private File yamlFile;
    private HashMap<String, Object> defaultValues;

    public Settings()
    {
        load();
    }

    @Override
    public YamlConfiguration getYamlConfiguration()
    {
        return yamlConfiguration;
    }

    @Override
    public void load()
    {
        defaultValues = new HashMap<>();
        create();
    }

    @Override
    public HashMap<String, Object> getDefaults()
    {
        return defaultValues;
    }

    @Override
    public File getFile()
    {
        return yamlFile;
    }

    @Override
    public void create()
    {
        try
        {
            yamlFile = new File(SinkLibrary.getCustomDataFolder(), "Settings.yml");

            boolean createNewConfiguration = ! exists();

            if (createNewConfiguration)
            {
                Bukkit.getLogger().log(Level.INFO, "Creating new configuration: " + yamlFile);
            }

            if (createNewConfiguration && ! yamlFile.createNewFile())
            {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't create configuration: " + yamlFile);
                return;
            }

            yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(yamlFile);

            if (! createNewConfiguration)
            {
                int version = (int) get("Main.ConfigVersion");
                if (version < CURRENT_VERSION)
                {
                    Bukkit.getLogger().log(Level.WARNING, "***************");
                    Bukkit.getLogger().log(Level.WARNING, "Configuration: " + yamlFile + " is too old! Current Version: " + version + ", required Version: " + CURRENT_VERSION);
                    recreate();
                    Bukkit.getLogger().log(Level.WARNING, "***************");
                    return;
                }
            }

            getYamlConfiguration().options().header(String.format("You can customize the SinkPlugins with this configuration."));

            addDefault("Main.ConfigVersion", CURRENT_VERSION);

            addDefault("Updater.Enabled", true);
            addDefault("Updater.UpdateType", "default");

            /*
            addDefault("SinkIRC.BotEnabled", false);
            addDefault("SinkIRC.Username", "SinkIRCBot");
            addDefault("SinkIRC.Server.Address", "irc.example.com");
            addDefault("SinkIRC.Server.Password", "");
            addDefault("SinkIRC.Server.Port", 6667);
            addDefault("SinkIRC.Channel", "#ChatBot");
            addDefault("SinkIRC.Authentification.Enabled", false); //ToDo: http://www.deaded.com/staticpages/index.php/pircbotdemos
            addDefault("SinkIRC.Authentification.AuthBot", "NickServ");
            addDefault("SinkIRC.Authentification.AuthMessage", "indentify <password>");
            addDefault("SinkIRC.Authentification.Password", "");
            */

            addDefault("SinkChat.Channel.Help.Prefix", "?");
            addDefault("SinkChat.Channel.Shout.Prefix", "!");
            addDefault("SinkChat.Channel.Trade.Prefix", "$");

            save();
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't create configuration file: " + yamlFile.getName());
            Bukkit.getLogger().log(Level.SEVERE, "Exception occured: ", e);
        }
        catch (InvalidConfigurationException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "***************");
            Bukkit.getLogger().log(Level.SEVERE, "Invalid configuration file detected: " + yamlFile);
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage());
            Bukkit.getLogger().log(Level.SEVERE, "***************");
            recreate();
        }
    }

    public boolean getUpdaterEnabled()
    {
        return (boolean) get("Updater.Enabled");
    }
}
