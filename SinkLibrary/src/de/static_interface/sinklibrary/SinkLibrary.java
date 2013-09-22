package de.static_interface.sinklibrary;

import de.static_interface.sinkirc.SinkIRC;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SinkLibrary extends JavaPlugin
{

    public static List<String> tmpBannedPlayers;

    private static Economy econ;
    private static SinkIRC irc;
    private static File dataFolder;

    private static boolean economyAvailable = true;
    private static boolean permissionsAvailable = true;

    public void onEnable()
    {
        if (! setupEcononmy())
        {
            Bukkit.getLogger().log(Level.WARNING, "Economy Plugin not found. Disabling economy features.");
            economyAvailable = false;
        }
        if (Bukkit.getPluginManager().getPlugin("PermissionsEx") == null)
        {
            Bukkit.getLogger().log(Level.WARNING, "Permissions Plugin not found. Disabling permissions and group features.");
            permissionsAvailable = false;
        }

        irc = (SinkIRC) Bukkit.getPluginManager().getPlugin("SinkIRC");
        dataFolder = getDataFolder();
        tmpBannedPlayers = new ArrayList<>();
    }

    public void onDisable()
    {

    }

    private boolean setupEcononmy()
    {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null)
        {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    /**
     * @return True if economy is available
     */
    public static boolean economyAvailable()
    {
        return economyAvailable;
    }

    /**
     * @return True if permissions are available
     */
    public static boolean permissionsAvailable()
    {
        return permissionsAvailable;
    }

    /**
     * Get Economy instance
     *
     * @return Economy instace
     */
    public static Economy getEconomy()
    {
        return econ;
    }


    /**
     * Get SinkIRC Instance
     *
     * @return SinkIRC Instance
     */
    public static SinkIRC getSinkIRC()
    {
        return irc;
    }

    /**
     * Get Data Folder
     *
     * @return Data Folder of this plugin
     */
    public static File getDataFolderStatic()
    {
        return dataFolder;
    }

    /**
     * Send Message to IRC
     *
     * @param message Message to send
     */
    public static void sendIRCMessage(String message)
    {
        if (irc != null)
        {
            SinkIRC.getIRCBot().sendCleanMessage(SinkIRC.getChannel(), message);
        }
    }


    /**
     * Add Temp Ban
     *
     * @param username Player to ban
     */
    public static void addTempBan(String username)
    {
        tmpBannedPlayers.add(username);
    }

    /**
     * Remove Temp Ban
     *
     * @param username Player to unban
     */
    public static void removeTempBan(String username)
    {
        tmpBannedPlayers.remove(username);
    }
}
