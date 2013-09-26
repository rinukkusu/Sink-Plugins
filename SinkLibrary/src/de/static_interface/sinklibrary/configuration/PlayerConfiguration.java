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


@SuppressWarnings("UnusedDeclaration")
public class PlayerConfiguration implements IConfiguration
{
    private String playerName;
    private Player player;
    private File playerConfigFile;
    private YamlConfiguration playerYamlConfig;
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

        playersPath = new File(SinkLibrary.getDataFolderStatic() + File.separator + "Players");
        try
        {
            playerConfigFile = new File(playersPath, playerName + ".yml");
        }
        catch (NullPointerException ignored)
        {
            playerYamlConfig = null;
        }
        playerYamlConfig = ( exists() ) ? YamlConfiguration.loadConfiguration(playerConfigFile) : null;
    }

    /**
     * Get Player YAML Configuration
     *
     * @return playerYAMLConfiguration
     */
    public YamlConfiguration getYamlConfiguration()
    {
        return playerYamlConfig;
    }

    /**
     * Create Configuration File
     */
    public void create()
    {
        try
        {
            if (! playersPath.exists() && ! playersPath.mkdirs())
            {
                throw new IOException("Couldn't create \"" + playersPath.getAbsolutePath() + "\" folder!");
            }
            if (! playerConfigFile.exists() && ! playerConfigFile.createNewFile())
            {
                throw new IOException("Couldn't create player config: " + playerConfigFile);
            }
            playerYamlConfig = YamlConfiguration.loadConfiguration(playerConfigFile);
            //playerYamlConfig.createSection(FREEZEPATH);
            playerYamlConfig.addDefault(playerName + ".General.StatsEnabled", true);
            playerYamlConfig.addDefault(playerName + ".Spy.Enabled", true);
            playerYamlConfig.addDefault(playerName + ".Nick.HasNickname", false);
            playerYamlConfig.addDefault(playerName + ".Nick.Nickname", user.getDefaultDisplayName());
            playerYamlConfig.addDefault(playerName + ".Freeze.freezed", false);
            playerYamlConfig.addDefault(playerName + ".Freeze.freezedtime", 0);
            playerYamlConfig.options().copyDefaults(true);
            save();
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't create player config file: " + playerConfigFile);
            Bukkit.getLogger().log(Level.SEVERE, "Exception occured: ", e);
        }
    }

    /**
     * @return True if the config file exists
     */
    public boolean exists()
    {
        return playerConfigFile.exists();
    }

    /**
     * Save config file
     */
    public void save()
    {
        if (playerConfigFile == null)
        {
            return;
        }
        try
        {
            playerYamlConfig.save(playerConfigFile);
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't save player config file: " + playerConfigFile + "!");
        }
    }

    /**
     * @param path  Path to value
     * @param value Value of path
     */
    public void set(String path, Object value)
    {
        try
        {
            playerYamlConfig.set(playerName + "." + path, value);
            save();
        }
        catch (Exception e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "WARNING: " + playerName + ": Couldn't save " + value + " to path " + path);
            throw e;
        }
    }

    /**
     * Get value from config
     *
     * @param path Path to value
     * @return Value of path
     */
    public Object get(String path)
    {
        try
        {
            return playerYamlConfig.get(playerName + "." + path);
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
            return (boolean) get(playerName + "Spy.Enabled");
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
     * Get freeze time
     *
     * @return Freeze time, if it is equal or below than 0, it will freeze for ever, anything else is tempfreeze (seconds).
     */
    public int getFreezeTime()
    {
        try
        {
            return playerYamlConfig.getInt(playerName + ".Freeze.freezedtime");
        }
        catch (Exception ignored)
        {
            return 0;
        }
    }

    /**
     * Set Freeze Time
     *
     * @param time If it is equal or below than 0, it will freeze for ever, anything else is tempfreeze (seconds).
     */
    public void setFreezeTime(int time)
    {
        set("Freeze.freezedtime", time);
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
