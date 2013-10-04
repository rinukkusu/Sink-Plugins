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
import de.static_interface.sinklibrary.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class PlayerConfiguration extends ConfigurationBase
{
    private String playerName;
    private Player player;
    private File yamlFile;
    private YamlConfiguration yamlConfiguration;
    private File playersPath;
    private User user;

    HashMap<String, Object> defaultValues;

    /**
     * Stores Player Informations and Settings in PlayerConfiguration YAML Files.
     * Should be accessed via {@link de.static_interface.sinklibrary.User#getPlayerConfiguration()}
     *
     * @param user User
     */
    public PlayerConfiguration(User user)
    {
        this.user = user;

        player = user.getPlayer();
        playerName = user.getName();

        playersPath = new File(SinkLibrary.getCustomDataFolder() + File.separator + "Players");
        try
        {
            yamlFile = new File(playersPath, playerName + ".yml");
        }
        catch (NullPointerException ignored)
        {
            yamlConfiguration = null;
        }
        yamlConfiguration = new YamlConfiguration();
        load();
    }

    @Override
    public YamlConfiguration getYamlConfiguration()
    {
        return yamlConfiguration;
    }

    @Override
    public boolean create()
    {
        try
        {
            if (! playersPath.exists() && ! playersPath.mkdirs())
            {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't create \"" + playersPath.getAbsolutePath() + "\" folder!");
                return false;
            }

            if (! yamlFile.exists() && ! yamlFile.createNewFile())
            {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't create player config: " + yamlFile);
                return false;
            }
            yamlConfiguration = YamlConfiguration.loadConfiguration(yamlFile);

            defaultValues = new HashMap<>();

            addDefault("General.StatsEnabled", true);
            addDefault("Spy.Enabled", true);
            addDefault("Nick.HasDisplayName", false);
            addDefault("Nick.DisplayName", user.getDefaultDisplayName());
            addDefault("Freeze.Frozen", false);

            yamlConfiguration.options().copyDefaults(true);

            save();
            Bukkit.getLogger().log(Level.INFO, "Succesfully created new configuration file: " + yamlFile.getName());
            return true;
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't create player config file: " + yamlFile.getName());
            Bukkit.getLogger().log(Level.SEVERE, "Exception occured: ", e);
            return false;
        }
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
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't save player configuration file: " + yamlFile + "!");
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
            Bukkit.getLogger().log(Level.WARNING, playerName + "'s configuration file: Couldn't save " + value + " to path " + path, e);
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
            Bukkit.getLogger().log(Level.WARNING, playerName + "'s configuration file: Couldn't load value from path: " + path);
            Object def = getDefault(path);
            if (def == null)
            {
                throw new NullPointerException("Default value is null!");
            }
            return def;
        }
    }

    /**
     * @return True if spy is enabled
     */
    public boolean getSpyEnabled()
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
     * Get freeze value
     *
     * @return true if player is freezed
     */
    public boolean getFrozen()
    {
        return (boolean) get("Freeze.Frozen");
    }

    /**
     * Set freeze value
     *
     * @param value Set value, true will freeze player
     */
    public void setFrozen(boolean value)
    {
        set("Freeze.Frozen", value);
    }

    /**
     * @return True if stats are enabled
     */
    public boolean getStatsEnabled()
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
        if (displayName.equals(user.getDefaultDisplayName()))
        {
            setHasDisplayName(false);
        }
        else
        {
            setHasDisplayName(true);
        }
    }

    /**
     * @return Custom Display Name of Player
     */
    public String getDisplayName()
    {
        return (String) get("Nick.DisplayName");
    }

    /**
     * @return True if player has an custom Display Name
     */
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
}
