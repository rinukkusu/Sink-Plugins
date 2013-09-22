package de.static_interface.sinklibrary;

import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class User
{
    private static Player base;
    private static Economy econ;

    private static PlayerConfiguration playerConfig;

    public User(String name)
    {
        base = Bukkit.getPlayer(name);
        econ = SinkLibrary.getEconomy();
        playerConfig = new PlayerConfiguration(name);
    }

    public User(Player player)
    {
        base = player;
        econ = SinkLibrary.getEconomy();
        playerConfig = new PlayerConfiguration(player.getName());
    }

    public int getMoney()
    {
        if (! SinkLibrary.economyAvailable())
        {
            return 0;
        }
        EconomyResponse response = econ.bankBalance(base.getName());
        return (int) response.balance;
    }

    public PlayerConfiguration getPlayerConfiguration()
    {
        return playerConfig;
    }

    public Player getPlayer()
    {
        return base;
    }
}
