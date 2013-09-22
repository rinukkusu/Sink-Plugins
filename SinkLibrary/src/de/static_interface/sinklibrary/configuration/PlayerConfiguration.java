package de.static_interface.sinklibrary.configuration;

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

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

    /**
     * Stores Player Informations and Settings in PlayerConfiguration YAML Files.
     *
     * @param playerName Player name. Do not use Displayname or Customname!
     */
    public PlayerConfiguration(String playerName)
    {
        this.playerName = playerName;
        player = Bukkit.getPlayer(playerName);
        playersPath = new File(SinkLibrary.getDataFolderStatic() + File.separator + "Players");
        playerConfigFile = new File(playersPath, playerName + ".yml");
        playerYamlConfig = ( exists() ) ? YamlConfiguration.loadConfiguration(playerConfigFile) : null;
    }

    /**
     * Get Player YAML Configuration
     *
     * @return playerYAMLConfiguration
     */
    public YamlConfiguration getYamlConfiguration()
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
            playerYamlConfig.addDefault(playerName + ".General.StatsEnabled", true);
            playerYamlConfig.addDefault(playerName + ".Spy.Enabled", true);
            playerYamlConfig.addDefault(playerName + ".Nick.HasNickname", false);
            playerYamlConfig.addDefault(playerName + ".Nick.Nickname", getDefaultDisplayName());
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
        playerYamlConfig.set(playerName + "." + path, value);
        save();
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
            e.printStackTrace();
            return "";
        }

    }

    public String[] getGroups()
    {
        PermissionUser user = PermissionsEx.getUser(player);
        return user.getGroupsNames();
    }

    public boolean getSpyEnabled()
    {
        return (boolean) get(playerName + "Spy.Enabled");
    }


    /**
     *
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
        return (boolean) get(playerName + "Freeze.freezed");
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
        User user = new User(player);
        PlayerConfiguration config = user.getPlayerConfiguration();
        config.set("Nick.Nickname", displayName);
        if (displayName.equals(getDefaultDisplayName()))
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
            User user = new User(player);
            PlayerConfiguration config = user.getPlayerConfiguration();
            return (String) config.get("Nick.Nickname");
        }
        catch (Exception e)
        {
            return getDefaultDisplayName();
        }

    }

    /**
     * @return Display Name with Permission Prefix or Op/non-Op Prefix
     */
    public String getDefaultDisplayName()
    {
        if (SinkLibrary.permissionsAvailable())
        {
            PermissionUser permsUser = PermissionsEx.getUser(player);
            String playerPrefix = ChatColor.translateAlternateColorCodes('&', permsUser.getPrefix());
            return playerPrefix + player.getName() + ChatColor.RESET;
        }
        String prefix = player.isOp() ? ChatColor.RED.toString() : ChatColor.WHITE.toString();
        return prefix + player.getName() + ChatColor.RESET;
    }

    /**
     * @return True if player has an custom Display Name
     */
    public boolean getHasDisplayName()
    {
        try
        {
            User user = new User(player);
            PlayerConfiguration config = user.getPlayerConfiguration();
            return (boolean) config.get("Nick.HasNickname");
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
        User user = new User(player);
        PlayerConfiguration config = user.getPlayerConfiguration();
        config.set("Nick.HasNickname", value);
    }
}
