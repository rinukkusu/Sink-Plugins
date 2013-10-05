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

package de.static_interface.sinkcommands.commands;

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SpectateCommands
{
    public static HashMap<Player, Player> specedPlayers = new HashMap<>();
    public static final String PREFIX = ChatColor.DARK_PURPLE + "[Spec] " + ChatColor.RESET;

    public static class SpectateCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            User user = SinkLibrary.getUser(sender);
            if (user.isConsole())
            {
                sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden!");
                return true;
            }
            Player player = user.getPlayer();
            if (specedPlayers.containsKey(player))
            {
                player.sendMessage(PREFIX + ChatColor.RED + "Du befindest dich bereits im Spectate Modus! Verlasse ihn erst mit /unspec bevor du einen anderen Spieler beobachtest..");
                return true;
            }
            if (args.length < 1)
            {
                return false;
            }

            Player target = ( Bukkit.getServer().getPlayer(args[0]) );
            if (target == null)
            {
                player.sendMessage(PREFIX + args[0] + " ist nicht online!");
                return true;
            }

            if (target.hasPermission("sinkcommands.spectate.bypass"))
            {
                player.sendMessage(PREFIX + "Der Spectate Modus kann nicht für den gewählten Spieler aktiviert werden!");
                return true;
            }

            player.sendMessage(PREFIX + "Zum verlassen des spectate Modus, /unspec nutzen.");


            specedPlayers.put(player, target);

            target.setPassenger(player);
            hide(player, "sinkcommands.spectate.bypass");

            return true;
        }
    }

    public static class UnspectateCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            User user = SinkLibrary.getUser(sender);
            if (user.isConsole())
            {
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }

            Player player = user.getPlayer();
            if (! specedPlayers.containsKey(player))
            {
                player.sendMessage(PREFIX + ChatColor.RED + "Du befindest dich nicht im Spectate Modus!");
                return true;
            }
            Player target = specedPlayers.get(player);

            target.eject();
            show(player);
            specedPlayers.remove(player);
            sender.sendMessage(PREFIX + "Du hast den Spectate Modus verlassen.");
            return true;
        }
    }

    public static class SpectatorlistCommand implements CommandExecutor
    {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            sender.sendMessage(PREFIX + "Spieler im Spectate Modus:");
            int i = 0;
            String message = "";
            for (Player player : specedPlayers.keySet())
            {
                Player target = specedPlayers.get(player);
                if (message.equals(""))
                {
                    message = player.getDisplayName() + " beobachtet: " + target.getDisplayName();
                }
                else
                {
                    message = message + ", " + player.getDisplayName() + " beobachtet: " + target.getDisplayName();
                }
                i++;
            }
            if (i == 0 || message.equals(""))
            {
                message = "Es gibt keine Spieler im Spectate Modus.";
            }
            sender.sendMessage(PREFIX + message);
            return true;
        }
    }

    /**
     * Hide a player for all players except with this permission
     *
     * @param player     Player to hide
     * @param permission Players with this permission will be able to see that player
     */
    public static void hide(Player player, String permission)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            User user = SinkLibrary.getUser(p);
            if (user.hasPermission(permission))
            {
                continue;
            }
            p.hidePlayer(player);
        }
    }

    /**
     * Show player again to all
     *
     * @param player Player who was hidden
     */
    public static void show(Player player)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            p.showPlayer(player);
        }
    }
}
