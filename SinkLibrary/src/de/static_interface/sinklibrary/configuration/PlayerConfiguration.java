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
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class PlayerConfiguration implements IConfiguration
{
    private String playerName;
    private Player player;
    private File yamlFile;
    private YamlConfiguration yamlConfiguration;
    private File playersPath;
    private User user;

    /**
     * Stores Player Informations and Settings in PlayerConfiguration YAML Files.
     *
     * @param user User
     */
    public PlayerConfiguration(User user)
    {
        this.user = user;

        player = user.getPlayer();
        playerName = player.getName();

        playersPath = new File(SinkLibrary.getCustomDataFolder() + File.separator + "Players");
        try
        {
            yamlFile = new File(playersPath, playerName + ".yml");
        }
        catch (NullPointerException ignored)
        {
            yamlConfiguration = null;
        }
        yamlConfiguration = ( exists() ) ? YamlConfiguration.loadConfiguration(yamlFile) : null;
    }

    public YamlConfiguration getYamlConfiguration()
    {
        return yamlConfiguration;
    }

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
            yamlConfiguration.addDefault(playerName + ".General.StatsEnabled", true);
            yamlConfiguration.addDefault(playerName + ".Spy.Enabled", true);
            yamlConfiguration.addDefault(playerName + ".Nick.HasNickname", false);
            yamlConfiguration.addDefault(playerName + ".Nick.Nickname", user.getDefaultDisplayName());
            yamlConfiguration.addDefault(playerName + ".Freeze.freezed", false);
            yamlConfiguration.options().copyDefaults(true);
            save();
            return true;
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't create player config file: " + yamlFile);
            Bukkit.getLogger().log(Level.SEVERE, "Exception occured: ", e);
            return false;
        }
    }

    public boolean exists()
    {
        return yamlFile.exists();
    }

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

    public void set(String path, Object value)
    {
        try
        {
            yamlConfiguration.set(playerName + "." + path, value);
            save();
        }
        catch (Exception e)
        {
            Bukkit.getLogger().log(Level.WARNING, "WARNING: " + playerName + ": Couldn't save " + value + " to path " + path, e);
        }
    }

    public Object get(String path)
    {
        try
        {
            return yamlConfiguration.get(playerName + "." + path);
        }
        catch (Exception e)
        {
            return null;
        }

    }

    /**
     * @return True if spy is enabled
     */
    public boolean getSpyEnabled()
    {
        try
        {
            return (boolean) get("Spy.Enabled");
        }
        catch (Exception ignored)
        {
            return false;
        }
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
    public boolean getFreezed()
    {
        try
        {
            return (boolean) get(playerName + "Freeze.freezed");
        }
        catch (Exception ignored)
        {
            return false;
        }
    }

    /**
     * Set freeze value
     *
     * @param value Set value, true will freeze player
     */
    public void setFreezed(boolean value)
    {
        set("Freeze.freezed", value);
    }

    /**
     * @return True if stats are enabled
     */
    public boolean getStatsEnabled()
    {
        try
        {
            return (boolean) get("General.StatsEnabled");
        }
        catch (Exception e)
        {
            return false;
        }
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
        PlayerConfiguration config = user.getPlayerConfiguration();
        config.set("Nick.Nickname", displayName);
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
        try
        {
            return (String) get("Nick.Nickname");
        }
        catch (Exception e)
        {
            return user.getDefaultDisplayName();
        }
    }

    /**
     * @return True if player has an custom Display Name
     */
    public boolean getHasDisplayName()
    {
        try
        {
            return (boolean) get("Nick.HasNickname");
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * @param value If set to true, player will use custom displayname
     */
    public void setHasDisplayName(boolean value)
    {
        set("Nick.HasNickname", value);
    }
}
