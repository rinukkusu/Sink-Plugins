package de.static_interface.commandsplugin;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;


/**
 * Currenty unused
 */

@SuppressWarnings("UnusedDeclaration")
public class PlayerConfiguration
{
    String playerName;
    File playerConfigFile;
    YamlConfiguration playerYamlConfig;
    String freezePath = playerName + ".Freeze";
    String freezedPath = freezePath + ".freezed";
    String freezedTimePath = freezePath + ".freezetime";
    File playersPath;

    /**
     * Stores Player Informations and Settings in PlayerConfiguration YAML Files.
     *
     * @param playerName Player name. Do not use Displayname or Customname!
     */
    public PlayerConfiguration(String playerName)
    {
        this.playerName = playerName;
        playersPath = new File(CommandsPlugin.getDataFolderStatic() + File.separator + "Players");
        playerConfigFile = new File(playersPath, playerName + ".yml");
        playerYamlConfig = ( exists() ) ? YamlConfiguration.loadConfiguration(playerConfigFile) : null;
    }

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
            playerYamlConfig.createSection(freezePath);
            playerYamlConfig.addDefault(freezedPath, false);
            playerYamlConfig.addDefault(freezedTimePath, 0);
            playerYamlConfig.options().copyDefaults(true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
     * Get freeze value
     *
     * @return true if player is freezed
     */

    public boolean getFreezed()
    {
        return playerYamlConfig.getBoolean(freezedPath);
    }

    /**
     * Get freeze time
     *
     * @return Freeze time, if it is equal or below than 0, it will freeze for ever, anything else is tempfreeze (seconds).
     */
    public int getFreezeTime()
    {
        return playerYamlConfig.getInt(freezedTimePath);
    }

    /**
     * Set freeze value
     *
     * @param value Set value, true will freeze player
     */
    public void setFreezed(boolean value)
    {
        playerYamlConfig.set(freezedPath, value);
    }

    /**
     * Set Freeze Time
     *
     * @param time If it is equal or below than 0, it will freeze for ever, anything else is tempfreeze (seconds).
     */
    public void setFreezeTime(int time)
    {
        playerYamlConfig.set(freezedTimePath, time);
    }
}
