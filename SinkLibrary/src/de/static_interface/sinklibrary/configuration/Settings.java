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
import de.static_interface.sinklibrary.Updater;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class Settings extends ConfigurationBase
{
    public static final int REQUIRED_VERSION = 1;
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

            getYamlConfiguration().options().header(String.format("You can customize the SinkPlugins with this configuration."));

            addDefault("Main.ConfigVersion", REQUIRED_VERSION);
            addDefault("General.DisplayNamesEnabled", true);


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

            addDefault("SinkAntiSpam.BlacklistedWordsCheck.Enabled", true);

            List<String> defaultBlackList = new ArrayList<>();
            defaultBlackList.add("BlacklistedWord");
            defaultBlackList.add("BlackListedWord2");
            addDefault("SinkAntiSpam.BlacklistedWordsCheck.Words", defaultBlackList);

            addDefault("SinkAntiSpam.WhitelistedDomainsCheck.Enabled", true);

            List<String> defaultDomainWiteList = new ArrayList<>();
            defaultDomainWiteList.add("google.com");
            defaultDomainWiteList.add("examlple.com");
            addDefault("SinkAntiSpam.WhitelistedDomainsCheck.Domains", defaultDomainWiteList);

            addDefault("SinkAntiSpam.IPCheck.Enabled", true);

            List<String> defaultExcludedCommands = new ArrayList<>();
            defaultExcludedCommands.add("msg");
            defaultExcludedCommands.add("tell");
            defaultExcludedCommands.add("m");
            defaultExcludedCommands.add("whisper");
            defaultExcludedCommands.add("t");
            addDefault("SinkAntiSpam.ExcludedCommands.Commands", defaultExcludedCommands);


            addDefault("SinkChat.Channels.Help.Prefix", "?");
            addDefault("SinkChat.Channels.Shout.Prefix", "!");
            addDefault("SinkChat.Channels.Trade.Prefix", "$");

            save();
        }
        catch ( IOException e )
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't create configuration file: " + yamlFile.getName());
            Bukkit.getLogger().log(Level.SEVERE, "Exception occured: ", e);
        }
        catch ( InvalidConfigurationException e )
        {
            Bukkit.getLogger().log(Level.SEVERE, "Invalid configuration file detected: " + yamlFile);
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage());
            recreate();
        }
    }

    public Updater.UpdateType getUpdateType()
    {
        switch ( ((String) get("Updater.UpdateType")).toLowerCase() )
        {
            case "default":
                return Updater.UpdateType.DEFAULT;
            case "no_download":
                return Updater.UpdateType.NO_DOWNLOAD;
            case "no_version_check":
                return Updater.UpdateType.NO_VERSION_CHECK;
            default:
                return Updater.UpdateType.DEFAULT;
        }
    }

    public boolean getUpdaterEnabled()
    {
        return (boolean) get("Updater.Enabled");
    }

    public boolean getDisplayNamesEnabled()
    {
        return (boolean) get("General.DisplayNamesEnabled");
    }

    public List<String> getBlackListedWords()
    {
        return getStringList("SinkAntiSpam.BlacklistedWordsCheck.Words");
    }

    public List<String> getWhitelistedWords()
    {
        return getStringList("SinkAntiSpam.WhitelistedDomainsCheck.Domains");
    }

    public List<String> getExcludedCommands()
    {
        return getStringList("SinkAntiSpam.ExcludedCommands.Commands");
    }

    public List<String> getStringList(String path)
    {
        try
        {
            List<String> value = getYamlConfiguration().getStringList(path);
            if ( value == null )
            {
                throw new NullPointerException("Path returned null!");
            }
            return value;
        }
        catch ( Exception e )
        {
            Bukkit.getLogger().log(Level.WARNING, getFile() + ": Couldn't load value from path: " + path + ". Reason: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean getBlacklistedWordsEnabled()
    {
        return (boolean) get("SinkAntiSpam.BlacklistedWordsCheck.Enabled");
    }

    public boolean getIPCheckEnabled()
    {
        return (boolean) get("SinkAntiSpam.IPCheck.Enabled");
    }

    public boolean getWhitelistedDomainCheckEnabled()
    {
        return (boolean) get("SinkAntiSpam.WhitelistedDomainsCheck.Enabled");
    }
}
