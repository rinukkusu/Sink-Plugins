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
    String freezeTimePath = freezePath + ".freezetime";

    /**
     * Stores Player Informations and Settings in PlayerConfiguration YAML Files.
     *
     * @param playerName Player name. Do not use Displayname or Customname!
     */
    public PlayerConfiguration(String playerName)
    {
        this.playerName = playerName;
        playerConfigFile = new File(( CommandsPlugin.getDataFolderStatic() + File.pathSeparator + "Players" ), playerName + ".yml");
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
            if (! CommandsPlugin.getDataFolderStatic().exists() && ! CommandsPlugin.getDataFolderStatic().mkdirs())
            {
                throw new IOException("Couldn't create \"Players\" folder!");
            }
            if (! playerConfigFile.exists() && ! playerConfigFile.createNewFile())
            {
                throw new IOException("Couldn't create player config: " + playerConfigFile);
            }
            setFreezed(false);
            setFreezeTime(0);
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
        return new File(( CommandsPlugin.getDataFolderStatic() + File.pathSeparator + "Players" ), playerName + ".yml").exists();
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
        return playerYamlConfig.getInt(freezeTimePath);
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
        playerYamlConfig.set(freezeTimePath, time);
    }
}
