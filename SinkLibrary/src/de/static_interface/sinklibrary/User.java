package de.static_interface.sinklibrary;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Author: Trojaner
 * Date: 21.09.13
 */
public class User
{
    Player base;
    private static Economy econ;

    public User(String name)
    {
        base = Bukkit.getPlayer(name);
        econ = SinkLibrary.getEconomy();
    }

    public int getMoney()
    {
        EconomyResponse response = econ.bankBalance(base.getName());
        return (int) response.balance;
    }

}
