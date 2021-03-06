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

import com.google.common.io.Files;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class PlayerConfiguration extends ConfigurationBase
{
    public static final int REQUIRED_VERSION = 1;

    private Player player;
    private File yamlFile = null;
    private YamlConfiguration yamlConfiguration = null;
    private User user;

    HashMap<String, Object> defaultValues = null;

    /**
     * Stores Player Informations and Settings in PlayerConfiguration YAML Files.
     * Should be accessed via {@link de.static_interface.sinklibrary.User#getPlayerConfiguration()}
     *
     * @param user User
     */
    public PlayerConfiguration(User user)
    {
        if ( user.isConsole() )
        {
            throw new RuntimeException("User is Console, cannot create PlayerConfiguration.");
        }

        this.user = user;
        player = user.getPlayer();

        load();
    }

    @Override
    public YamlConfiguration getYamlConfiguration()
    {
        return yamlConfiguration;
    }

    @Override
    public void create()
    {
        try
        {
            File playersPath = new File(SinkLibrary.getCustomDataFolder() + File.separator + "Players");
            yamlFile = new File(playersPath, player.getName() + ".yml");

            boolean createNewConfiguration = !exists();

            if ( createNewConfiguration )
            {
                SinkLibrary.getCustomLogger().log(Level.INFO, "Creating new player configuration: " + yamlFile);
            }

            Files.createParentDirs(yamlFile);

            if ( createNewConfiguration && !yamlFile.createNewFile() )
            {
                SinkLibrary.getCustomLogger().log(Level.SEVERE, "Couldn't create player configuration: " + yamlFile);
                return;
            }

            yamlConfiguration = new YamlConfiguration();
            yamlConfiguration.load(yamlFile);

            /*
            if ( !createNewConfiguration )
            {
                int version;
                try
                {
                    version = (int) get("Main.ConfigVersion");
                }
                catch(NullPointerException e)
                {
                    version = 0;
                }
                if ( version < REQUIRED_VERSION )
                {
                    SinkLibrary.getCustomLogger().log(Level.WARNING, "***************");
                    SinkLibrary.getCustomLogger().log(Level.WARNING, "Configuration: " + yamlFile + " is too old! Current Version: " + version + ", required Version: " + REQUIRED_VERSION);
                    recreate();
                    SinkLibrary.getCustomLogger().log(Level.WARNING, "***************");
                    return;
                }
            }
            */

            yamlConfiguration.options().header(String.format("This configuration saves and loads variables to players.%nDon't edit it."));

            addDefault("Main.ConfigVersion", REQUIRED_VERSION);
            addDefault("General.StatsEnabled", true);
            addDefault("General.ExceptionTrackingEnabled", false);
            addDefault("Spy.Enabled", true);
            addDefault("Nick.HasDisplayName", false);
            addDefault("Nick.DisplayName", user.getDefaultDisplayName());

            save();
        }
        catch ( IOException e )
        {
            SinkLibrary.getCustomLogger().log(Level.SEVERE, "Couldn't create player config file: " + yamlFile.getAbsolutePath());
            SinkLibrary.getCustomLogger().log(Level.SEVERE, "Exception occurred: ", e);
        }
        catch ( InvalidConfigurationException e )
        {
            SinkLibrary.getCustomLogger().log(Level.SEVERE, "***************");
            SinkLibrary.getCustomLogger().log(Level.SEVERE, "Invalid configuration file detected: " + yamlFile);
            SinkLibrary.getCustomLogger().log(Level.SEVERE, e.getMessage());
            SinkLibrary.getCustomLogger().log(Level.SEVERE, "***************");
            recreate();
        }
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

    /**
     * @return True if spy is enabled
     */
    public boolean isSpyEnabled()
    {
        return (boolean) get("Spy.Enabled");
    }

    /**
     * Enable or disable Spy
     *
     * @param value True will enable it, false disable
     */
    public void setSpyEnabled(boolean value)
    {
        set("Spy.Enabled", value);
    }

    /**
     * @return True if stats are enabled
     */
    public boolean isStatsEnabled()
    {
        return (boolean) get("General.StatsEnabled");
    }

    /**
     * Set stats Enabled
     *
     * @param value Value
     */
    public void setStatsEnabled(boolean value)
    {
        set("General.StatsEnabled", value);
    }

    /**
     * Set DisplayName for player
     *
     * @param displayName New Display Name
     */
    public void setDisplayName(String displayName)
    {
        player.setDisplayName(displayName);
        player.setCustomName(displayName);
        set("Nick.DisplayName", displayName);
        if ( ChatColor.stripColor(displayName).equals(ChatColor.stripColor(user.getDefaultDisplayName())) )
        {
            setHasDisplayName(false);
        }
    }

    /**
     * @return Custom Display Name of Player
     */
    public String getDisplayName()
    {
        if ( !getHasDisplayName() )
        {
            return user.getDefaultDisplayName();
        }
        return (String) get("Nick.DisplayName");
    }

    /**
     * @return True if player has an custom Display Name
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean getHasDisplayName()
    {
        return (boolean) get("Nick.HasDisplayName");
    }

    /**
     * @param value If set to true, player will use custom displayname instead of default
     */
    public void setHasDisplayName(boolean value)
    {
        set("Nick.HasDisplayName", value);
    }

    /**
     * @return
     * True if exception tracking is enabled, false if not.
     */
    public boolean getHasExceptionTrackingEnabled()
    {
        return (boolean) get("General.ExceptionTrackingEnabled");
    }

    public void setHasExceptionTrackingEnabled(boolean value)
    {
        set("General.ExceptionTrackingEnabled", value);
    }

}
