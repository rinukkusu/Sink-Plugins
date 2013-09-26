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

import de.static_interface.sinklibrary.BukkitUtil;
import de.static_interface.sinklibrary.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FreezeCommands
{
    public static final String PREFIX = ChatColor.RED + "[Freeze] " + ChatColor.RESET;

    public static Set<String> toFreeze = new HashSet<>();
    public static Set<String> freezeAll = new HashSet<>();

    public static class FreezeCommand implements CommandExecutor
    {
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (args.length < 1)
            {
                return false;
            }

            String reason = "";

            for (int i = 1; i < args.length; i++)
            {
                if (reason.equals(""))
                {
                    reason = args[i];
                    continue;
                }
                reason = reason + " " + args[i];
            }

            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.getName().toLowerCase().contains(args[0].toLowerCase()) || p.getName().equalsIgnoreCase(args[0]))
                {
                    if (! canBeFrozen(p) && sender instanceof Player)
                    {
                        sender.sendMessage(PREFIX + ChatColor.DARK_RED + "Dieser Spieler kann nicht eingefroren werden!");
                        return true;
                    }
                    if (toggleFreeze(p))
                    {
                        if (args.length < 2)
                        {
                            BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " eingefroren.", "sinkcommands.freeze.message");
                        }
                        else
                        {
                            BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " eingefroren. Grund: " + reason, "sinkcommands.freeze.message");
                        }
                        return true;
                    }
                    p.sendMessage(PREFIX + ChatColor.RED + "Du wurdest von " + BukkitUtil.getSenderName(sender) + " aufgetaut.");
                    BukkitUtil.broadcast(PREFIX + p.getDisplayName() + " wurde von " + BukkitUtil.getSenderName(sender) + " wieder aufgetaut.", "sinkcommands.freeze.message");
                    return true;
                }
            }
            return true;
        }
    }

    public static class FreezeallCommand implements CommandExecutor
    {
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (args.length < 1 && freezeAll.size() != Bukkit.getWorlds().size())
            {
                return false;
            }

            String reason = Util.formatArrayToString(args, " ");

            if (toggleFreezeAll())
            {
                BukkitUtil.broadcastMessage(PREFIX + ChatColor.RED + "Alle Spieler wurden von " + BukkitUtil.getSenderName(sender) + " eingefroren. Grund: " + reason);
            }
            else
            {
                BukkitUtil.broadcastMessage(PREFIX + ChatColor.RED + "Alle Spieler wurden von " + BukkitUtil.getSenderName(sender) + " aufgetaut.");
            }
            return true;
        }
    }

    public static class FreezelistCommand implements CommandExecutor
    {
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            String frozenList = "";
            String tmpfrozenList = "";
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (isFrozen(p))
                {
                    if (frozenList.length() > 0)
                    {
                        frozenList = frozenList + ", " + p.getDisplayName();
                    }
                    else
                    {
                        frozenList = p.getDisplayName();
                    }
                }
            }


            if (frozenList.length() > 0)
            {
                sender.sendMessage(PREFIX + "Eingefrorene Spieler: " + frozenList);
            }
            else
            {
                sender.sendMessage(PREFIX + "Es gibt keine eingefrorenen Spieler.");
            }

            if (tmpfrozenList.length() > 0)
            {
                sender.sendMessage(PREFIX + "Temporär eingefrorene Spieler: " + tmpfrozenList);
            }
            else
            {
                sender.sendMessage(PREFIX + "Es gibt keine temporär eingefrorenen Spieler.");
            }
            return true;
        }
    }

    public static boolean isFrozen(Player player)
    {
        if (player == null)
        {
            return false;
        }

        if (freezeAll.contains(player.getWorld().getName()))
        {
            return canBeFrozen(player);
        }

        return toFreeze.contains(player.getName());
    }

    public static boolean canBeFrozen(Player player)
    {
        return ! player.hasPermission("sinkcommands.freeze.never");
    }

    public static boolean toggleFreezeAll()
    {
        if (freezeAll.size() == Bukkit.getWorlds().size())
        {
            freezeAll.clear();
            return false;
        }

        for (World w : Bukkit.getWorlds())
        {
            if (! freezeAll.contains(w.getName()))
            {
                freezeAll.add(w.getName());
            }
        }
        return true;
    }

    public static boolean toggleFreeze(Player player)
    {
        if (toFreeze.contains(player.getName()))
        {
            toFreeze.remove(player.getName());
            return false;
        }
        else
        {
            toFreeze.add(player.getName());
            return true;
        }
    }

    public static void unfreeze(Player player)
    {
        if (toFreeze.contains(player.getName()))
        {
            toFreeze.remove(player.getName());
        }
    }

    public static void unfreeze(OfflinePlayer player)
    {
        if (toFreeze.contains(player.getName()))
        {
            toFreeze.remove(player.getName());
        }
    }

    public static void loadFreezedPlayers(Logger log, File dataFolder)
    {
        File saveFile = new File(dataFolder, "freezedPlayers.txt");
        if (saveFile.exists())
        {
            try
            {
                InputStream ips = new FileInputStream(saveFile);
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);
                String line;
                while (( line = br.readLine() ) != null)
                {
                    toFreeze.add(line.trim());
                }
            }
            catch (Exception exc)
            {
                log.log(Level.SEVERE, "Fehler beim laden der freezedPlayers.txt ", exc);
            }
        }
    }

    public static void unloadFreezedPlayers(Logger log, File dataFolder)
    {
        if (! dataFolder.exists())
        {
            dataFolder.mkdirs();
        }
        File saveFile = new File(dataFolder, "freezedPlayers.txt");
        if (saveFile.exists())
        {
            saveFile.delete();
        }

        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter(saveFile));
            out.close();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "Fehler beim speichern der freezedPlayers.txt ", e);
        }
    }
}