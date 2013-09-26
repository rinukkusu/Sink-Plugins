package de.static_interface.sinklibrary;

import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import de.static_interface.sinklibrary.exceptions.EconomyNotAvailableException;
import de.static_interface.sinklibrary.exceptions.PermissionsNotAvailableException;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class User
{
    private static Player base;
    private static Economy econ;

    public User(String name)
    {
        base = Bukkit.getPlayer(name);
        econ = SinkLibrary.getEconomy();
    }

    public User(Player player)
    {
        base = player;
        econ = SinkLibrary.getEconomy();
    }

    public int getMoney()
    {
        if (! SinkLibrary.economyAvailable())
        {
            throw new EconomyNotAvailableException();
        }
        EconomyResponse response = econ.bankBalance(base.getName());
        return (int) response.balance;
    }

    public PlayerConfiguration getPlayerConfiguration()
    {
        return new PlayerConfiguration(this);
    }

    public Player getPlayer()
    {
        return base;
    }

    public Object hasPermission(String permission)
    {
        if (SinkLibrary.permissionsAvailable())
        {
            return SinkLibrary.getPermissions().has(base, permission);
        }
        else
        {
            return base.hasPermission(permission);
        }
    }

    /**
     * Get user's primary group.
     *
     * @return Primary Group
     */
    public String getPrimaryGroup()
    {
        if (! SinkLibrary.permissionsAvailable())
        {
            throw new PermissionsNotAvailableException();
        }
        return SinkLibrary.getPermissions().getPrimaryGroup(base);
    }


    /**
     * @return Display Name with Permission Prefix or Op/non-Op Prefix
     */
    public String getDefaultDisplayName()
    {
        try
        {
            if (SinkLibrary.chatAvailable())
            {
                String playerPrefix = ChatColor.translateAlternateColorCodes('&', SinkLibrary.getChat().getPlayerPrefix(base));
                return playerPrefix + base.getName() + ChatColor.RESET;
            }
        }
        catch (Exception ignored)
        {
        }

        String prefix = base.isOp() ? ChatColor.RED.toString() : ChatColor.WHITE.toString();
        return prefix + base.getName() + ChatColor.RESET;
    }
}
