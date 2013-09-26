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
import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class VotekickCommands
{
    public static final String PREFIX = ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "[VoteKick] " + ChatColor.RESET;

    private static double votesYes = 0;
    private static double votesNo = 0;
    private static boolean voteStarted;
    private static Player targetPlayer;

    private static String target;

    public static List<CommandSender> votedPlayers = new ArrayList<>();

    public static class VotekickCommand implements CommandExecutor
    {
        private Plugin plugin;

        public VotekickCommand(Plugin plugin)
        {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (args.length < 1)
            {
                return false;
            }

            String reason = "";
            if (Bukkit.getOnlinePlayers().length < 5)
            {
                sender.sendMessage(PREFIX + "Es sind zu wenige Leute für einen Votekick online!");
                return true;
            }
            if (args.length > 2)
            {
                int i = 0;
                for (String arg : args)
                {
                    i++;
                    if (i < 2)
                    {
                        continue;
                    }
                    if (reason.equals(""))
                    {
                        reason = arg;
                        continue;
                    }
                    reason = reason + " " + arg;
                }
            }
            if (voteStarted)
            {
                sender.sendMessage(PREFIX + "Du kannst nicht einen Votekick starten während ein anderer Votekick läuft!");
                return true;
            }
            boolean voteable = false;
            if (! sender.hasPermission("sinkcommands.votekick.staff"))
            {
                int i = 0;
                Player[] onlinePlayers = Bukkit.getOnlinePlayers();
                for (Player p : onlinePlayers)
                {
                    if (! p.hasPermission("sinkcommands.votekick.staff"))
                    {
                        i++;
                        break;
                    }
                }
                if (i == onlinePlayers.length)
                {
                    voteable = true;
                }
            }
            else
            {
                voteable = true;
            }
            if (! voteable)
            {
                sender.sendMessage(PREFIX + "Du kannst nicht einen Votekick starten wenn ein Teammitglied online ist!");
                return true;
            }
            targetPlayer = ( Bukkit.getServer().getPlayer(args[0]) );
            target = targetPlayer.getDisplayName();
            if (targetPlayer == sender)
            {
                sender.sendMessage(PREFIX + "Du kannst nicht einen Votekick gegen dich selbst starten!");
                return true;
            }
            if (targetPlayer.hasPermission("sinkcommands.votekick.bypass") && ! sender.hasPermission("sinkcommands.votekick.bypass"))
            {
                sender.sendMessage(PREFIX + "Du kannst nicht einen Votekick gegen diese Person starten!");
                return true;
            }
            if (target == null)
            {
                sender.sendMessage(PREFIX + args[0] + " ist nicht online!");
                return true;
            }
            if (reason.equals(""))
            {
                BukkitUtil.broadcast(PREFIX + BukkitUtil.getSenderName(sender) + " hat einen Votekick gegen " + target + " gestartet. Nutze /voteyes oder /voteno um zu voten und /votestatus für den Vote Status!", "sinkcommands.votekick.vote");
            }
            else
            {
                BukkitUtil.broadcast(PREFIX + BukkitUtil.getSenderName(sender) + " hat einen Votekick gegen " + target + " gestartet. Grund: " + reason + ". Nutze /voteyes oder /voteno um zu voten und /votestatus für den Vote Status!", "sinkcommands.votekick.vote");
            }
            voteStarted = true;
            long time = 20 * 180; // 20 Ticks (= 1 second) * 180 = 3 Minutes

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    VotekickCommands.endVoteKick(plugin);
                }
            }, time);
            return true;
        }
    }

    public static class VoteyesCommand implements CommandExecutor
    {
        Plugin plugin;

        public VoteyesCommand(Plugin plugin)
        {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            return vote(sender, true, plugin);
        }
    }

    public static class VotenoCommand implements CommandExecutor
    {
        Plugin plugin;

        public VotenoCommand(Plugin plugin)
        {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            return vote(sender, false, plugin);
        }
    }

    public static class VotestatusCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            return sendStatus(sender);
        }
    }

    public static class EndvoteCommand implements CommandExecutor
    {
        Plugin plugin;

        public EndvoteCommand(Plugin plugin)
        {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
        {
            BukkitUtil.broadcast(PREFIX + "Der Votekick gegen " + target + " wurde durch " + BukkitUtil.getSenderName(commandSender) + " beendet.", "sinkcommands.votekick.vote");
            voteStarted = false;
            endVoteKick(plugin);
            return true;
        }
    }

    public static class VotekickunbanCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (args.length != 1)
            {
                return false;
            }
            String username = args[0];
            if (! SinkLibrary.tmpBannedPlayers.contains(username))
            {
                sender.sendMessage(PREFIX + ChatColor.BLUE + username + ChatColor.RESET + " wurde nicht durch einen Votekick gebannt!");
                return true;
            }
            SinkLibrary.removeTempBan(username);
            sender.sendMessage(PREFIX + ChatColor.BLUE + username + ChatColor.RESET + " wurde entbannt.");
            return true;
        }
    }

    private static void endVoteKick(Plugin plugin)
    {
        if (voteStarted)
        {
            int percentYes = (int) Math.round(( votesYes / ( votesYes + votesNo ) ) * 100);
            int percentNo = (int) Math.round(( votesNo / ( votesYes + votesNo ) ) * 100);
            if (( percentYes + percentNo ) <= 1)
            {
                BukkitUtil.broadcast(PREFIX + target + " wurde nicht gekickt, da zu wenige Spieler gevotet haben.", "sinkcommands.votekick.vote");
            }
            if (percentYes > 50)
            {
                BukkitUtil.broadcast(PREFIX + target + " wurde gekickt, weil die Mehrheit der Spieler dafür war (Ja: " + percentYes + "%, Nein: " + percentNo + "%).", "sinkcommands.votekick.vote");
                final String username = targetPlayer.getName();
                targetPlayer.kickPlayer("Du wurdest durch einen Votekick gekickt.");
                SinkLibrary.addTempBan(username);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
                {
                    @Override
                    public void run()
                    {
                        SinkLibrary.removeTempBan(username);
                    }
                }, 20 * 300); //Unban player after 5 minutes
            }
            else
            {
                BukkitUtil.broadcast(PREFIX + target + " wurde nicht gekickt, da die Mehrheit der Spieler dagegen war (Ja: " + percentYes + "%, Nein: " + percentNo + "%).", "sinkcommands.votekick.vote");
            }
        }
        votesYes = 0;
        votesNo = 0;
        targetPlayer = null;
        voteStarted = false;
        votedPlayers.clear();
    }

    private static boolean sendStatus(CommandSender sender)
    {
        if (! VotekickCommands.voteStarted)
        {
            sender.sendMessage(PREFIX + "Derzeit läuft kein Votekick...");
            return true;
        }
        int percentYes = (int) Math.round(( votesYes / ( votesYes + votesNo ) ) * 100);
        int percentNo = (int) Math.round(( votesNo / ( votesYes + votesNo ) ) * 100);
        sender.sendMessage(PREFIX + "Status: Ja: " + percentYes + "%, Nein: " + percentNo + "%.");
        return true;
    }

    private static boolean vote(CommandSender sender, boolean yes, Plugin plugin)
    {
        if (! VotekickCommands.voteStarted)
        {
            sender.sendMessage(PREFIX + "Derzeit läuft kein Votekick...");
            return true;
        }
        if (votedPlayers.contains(sender))
        {
            sender.sendMessage(PREFIX + "Du kannst nur einmal voten!");
            return true;
        }
        if (yes)
        {
            sender.sendMessage(PREFIX + "Du hast für einen Kick gestimmt!");
            votesYes = votesYes + 1;
        }
        else
        {
            sender.sendMessage(PREFIX + "Du hast gegen einen Kick gestimmt!");
            votesNo = votesNo + 1;
        }
        votedPlayers.add(sender);
        sendStatus(sender);

        if (( votesYes + votesNo ) == Bukkit.getOnlinePlayers().length)
        {
            VotekickCommands.endVoteKick(plugin);
        }
        return true;
    }
}
