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

    /**
     * Get current money of player
     *
     * @return Money of player
     * @throws de.static_interface.sinklibrary.exceptions.EconomyNotAvailableException if economy is not available.
     */
    public int getMoney()
    {
        if (! SinkLibrary.economyAvailable())
        {
            throw new EconomyNotAvailableException();
        }

        EconomyResponse response = econ.bankBalance(base.getName());
        return (int) response.balance;
    }

    /**
     * @return The PlayerConfiguration of the Player
     */
    public PlayerConfiguration getPlayerConfiguration()
    {
        return new PlayerConfiguration(this);
    }

    public Player getPlayer()
    {
        return base;
    }

    /**
     * @param permission Permission required
     * @return True if the player has the permission specified by parameter.
     */
    public boolean hasPermission(String permission)
    {
        //if (SinkLibrary.permissionsAvailable())
        //{
        //    return SinkLibrary.getPermissions().has(base, permission);
        //}
        //else
        //{
        return base.hasPermission(permission);
        //}
    }

    /**
     * Get user's primary group.
     *
     * @return Primary Group
     * @throws de.static_interface.sinklibrary.exceptions.PermissionsNotAvailableException if permissions are not available
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
