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
    private YamlConfiguration yamlConfiguration = null;
    private File yamlFile = null;
    private HashMap<String, Object> defaultValues = null;

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
                SinkLibrary.getCustomLogger().log(Level.INFO, "Creating new configuration: " + yamlFile);
            }

            if ( createNewConfiguration && !yamlFile.createNewFile() )
            {
                SinkLibrary.getCustomLogger().log(Level.SEVERE, "Couldn't create configuration: " + yamlFile);
                return;
            }

            yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(yamlFile);

            yamlConfiguration.options().header("You can customize the SinkPlugins with this configuration.");

            addDefault("Main.ConfigVersion", REQUIRED_VERSION);

            addDefault("General.DisplayNamesEnabled", true);
            addDefault("General.EnableLog", true);
            addDefault("General.EnableDebug", false);

            addDefault("Updater.Enabled", true);
            addDefault("Updater.UpdateType", "default");

            addDefault("SinkIRC.BotEnabled", false);
            addDefault("SinkIRC.Username", "SinkIRCBot");
            addDefault("SinkIRC.Server.Address", "irc.example.com");
            addDefault("SinkIRC.Server.PasswordEnabled", false);
            addDefault("SinkIRC.Server.Password", "ServerPasswordHere");
            addDefault("SinkIRC.Server.Port", 6667);
            addDefault("SinkIRC.Channel", "#ChatBot");
            addDefault("SinkIRC.Authentification.Enabled", false);
            addDefault("SinkIRC.Authentification.AuthBot", "NickServ");
            addDefault("SinkIRC.Authentification.AuthMessage", "indentify NickServPasswordHere");

            addDefault("SinkAntiSpam.BlacklistedWordsCheck.Enabled", true);

            List<String> defaultBlackList = new ArrayList<>();
            defaultBlackList.add("BlacklistedWord");
            defaultBlackList.add("BlackListedWord2");
            addDefault("SinkAntiSpam.BlacklistedWordsCheck.Words", defaultBlackList);

            addDefault("SinkAntiSpam.WhitelistedDomainsCheck.Enabled", true);

            List<String> defaultDomainWiteList = new ArrayList<>();
            defaultDomainWiteList.add("google.com");
            defaultDomainWiteList.add("youtube.com");
            defaultDomainWiteList.add("yourhomepagehere.com");
            addDefault("SinkAntiSpam.WhitelistedDomainsCheck.Domains", defaultDomainWiteList);

            addDefault("SinkAntiSpam.IPCheck.Enabled", true);

            List<String> defaultExcludedCommands = new ArrayList<>();
            defaultExcludedCommands.add("msg");
            defaultExcludedCommands.add("tell");
            defaultExcludedCommands.add("m");
            defaultExcludedCommands.add("whisper");
            defaultExcludedCommands.add("t");
            addDefault("SinkAntiSpam.ExcludedCommands.Commands", defaultExcludedCommands);


            addDefault("SinkChat.LocalChatRange", 50);
            addDefault("SinkChat.Channels.Help.Prefix", "?");
            addDefault("SinkChat.Channels.Shout.Prefix", "!");
            addDefault("SinkChat.Channels.Trade.Prefix", "$");

            save();
        }
        catch ( IOException e )
        {
            SinkLibrary.getCustomLogger().log(Level.SEVERE, "Couldn't create configuration file: " + yamlFile.getName());
            SinkLibrary.getCustomLogger().log(Level.SEVERE, "Exception occurred: ", e);
        }
        catch ( InvalidConfigurationException e )
        {
            SinkLibrary.getCustomLogger().log(Level.SEVERE, "Invalid configuration file detected: " + yamlFile);
            SinkLibrary.getCustomLogger().log(Level.SEVERE, e.getMessage());
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

    public boolean isUpdaterEnabled()
    {
        return (boolean) get("Updater.Enabled");
    }

    public boolean isDisplayNamesEnabled()
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
            List<String> value = yamlConfiguration.getStringList(path);
            if ( value == null )
            {
                throw new NullPointerException("Path returned null!");
            }
            return value;
        }
        catch ( Exception e )
        {
            SinkLibrary.getCustomLogger().log(Level.WARNING, yamlFile + ": Couldn't load value from path: " + path + ". Reason: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean isBlacklistedWordsEnabled()
    {
        return (boolean) get("SinkAntiSpam.BlacklistedWordsCheck.Enabled");
    }

    public boolean isIPCheckEnabled()
    {
        return (boolean) get("SinkAntiSpam.IPCheck.Enabled");
    }

    public boolean isWhitelistedDomainCheckEnabled()
    {
        return (boolean) get("SinkAntiSpam.WhitelistedDomainsCheck.Enabled");
    }

    public int getLocalChatRange()
    {
        return (int) get("SinkChat.LocalChatRange");
    }

    public boolean isIRCBotEnabled()
    {
        return (boolean) get("SinkIRC.BotEnabled");
    }

    public String getIRCBotUsername()
    {
        return (String) get("SinkIRC.Username");
    }

    public String getIRCAddress()
    {
        return (String) get("SinkIRC.Server.Address");
    }

    public boolean isIRCPasswordEnabled()
    {
        return (boolean) get("SinkIRC.Server.PasswordEnabled");
    }


    public String getIRCPassword()
    {
        return (String) get("SinkIRC.Server.Password");
    }

    public int getIRCPort()
    {
        return (int) get("SinkIRC.Server.Port");
    }

    public String getIRCChannel()
    {
        return (String) get("SinkIRC.Channel");
    }

    public boolean isIRCAuthentificationEnabled()
    {
        return (boolean) get("SinkIRC.Authentification.Enabled");
    }

    public String getIRCAuthBot()
    {
        return (String) get("SinkIRC.Authentification.AuthBot");
    }

    public String getIRCAuthMessage()
    {
        return (String) get("SinkIRC.Authentification.AuthMessage");
    }

    public boolean isLogEnabled()
    {
        return (boolean) get("General.EnableLog");
    }

    public boolean isDebugEnabled()
    {
        return (boolean) get("General.EnableDebug");
    }
}
