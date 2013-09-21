package de.static_interface.sinklibrary;

import de.static_interface.sinkirc.SinkIRC;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * Author: Trojaner
 * Date: 21.09.13
 */
public class SinkLibrary extends JavaPlugin
{

    private static Economy econ;
    private static SinkIRC irc;
    private static File dataFolder;

    public void onEnable()
    {
        if (! setupEcononmy())
        {
            Bukkit.getLogger().log(Level.SEVERE, "Sink plugins needs an economy plugin and Vault to work correctly.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        irc = (SinkIRC) Bukkit.getPluginManager().getPlugin("SinkIRC");
        dataFolder = getDataFolder();
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
     * Get Economy instance
     * @return Economy instace
     */
    public static Economy getEconomy()
    {
        return econ;
    }


    /**
     * Get SinkIRC Instance
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
}
