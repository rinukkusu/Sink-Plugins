package de.static_interface.commandsplugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;


@SuppressWarnings("UnusedDeclaration")
public class PlayerConfiguration
{
    private static String playerName;
    File playerConfigFile;
    YamlConfiguration playerYamlConfig;
    File playersPath;

    /**
     * Stores Player Informations and Settings in PlayerConfiguration YAML Files.
     *
     * @param playerName Player name. Do not use Displayname or Customname!
     */
    public PlayerConfiguration(String playerName)
    {
        PlayerConfiguration.playerName = playerName;
        playersPath = new File(CommandsPlugin.getDataFolderStatic() + File.separator + "Players");
        playerConfigFile = new File(playersPath, playerName + ".yml");
        playerYamlConfig = ( exists() ) ? YamlConfiguration.loadConfiguration(playerConfigFile) : null;
    }

    /**
     * Get Player YAML Configuration
     *
     * @return playerYAMLConfiguration
     */
    public YamlConfiguration getPlayerConfiguration()
    {
        playerYamlConfig = YamlConfiguration.loadConfiguration(playerConfigFile);
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
            playerYamlConfig.addDefault(playerName + ".Freeze.freezed", false);
            playerYamlConfig.addDefault(playerName + ".Freeze.freezedtime", 0);
            playerYamlConfig.addDefault(playerName + ".General.StatsEnabled", true);
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

    public void set(String path, Object value)
    {
        playerYamlConfig.set(playerName + "." + path, value);
        save();
    }

    public String getString(String path)
    {
        try
        {
            return playerYamlConfig.getString(playerName + "." + path);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }

    }

    public boolean getBoolean(String path)
    {
        try
        {
            return playerYamlConfig.getBoolean(playerName + "." + path);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Get freeze value
     *
     * @return true if player is freezed
     */
    public boolean getFreezed()
    {
        return getBoolean(playerName + "Freeze.freezed");
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
        return playerYamlConfig.getInt(playerName + ".Freeze.freezedtime");
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

    public boolean getStatsEnabled()
    {
        return getBoolean("General.StatsEnabled");
    }

    public void setStatsEnabled(boolean value)
    {
        set("General.StatsEnabled", value);
    }
}
