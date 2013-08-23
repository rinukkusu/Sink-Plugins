package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
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
    public static String prefix = ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "[VoteKick] " + ChatColor.RESET;

    private static double votesYes = 0;
    private static double votesNo = 0;
    private static boolean voteStarted;
    private static Player targetPlayer;

    private static String target;

    public static List<CommandSender> votedPlayers = new ArrayList<>();

    //ToDo: /voteadmin enable and disable with boolean: voteEnabled
    //ToDo: Add Timer to show remaining seconds...

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
                sender.sendMessage(prefix + "Du kannst nicht einen Votekick starten während ein anderer Votekick läuft!");
                return true;
            }
            boolean voteable = false;
            if (! sender.hasPermission("commandsplugin.votekick.staff"))
            {
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    if (p.hasPermission("commandsplugin.votekick.staff"))
                    {
                        voteable = true;
                        break;
                    }
                }
            }
            else
            {
                voteable = true;
            }
            if (! voteable)
            {
                sender.sendMessage(prefix + "Du kannst nicht einen Votekick starten wenn ein Teammitglied online ist!");
                return true;
            }
            targetPlayer = ( Bukkit.getServer().getPlayer(args[0]) );
            target = targetPlayer.getDisplayName();
            if (targetPlayer == sender)
            {
                sender.sendMessage(prefix + "Du kannst nicht einen Votekick gegen dich selbst starten!");
                return true;
            }
            if (targetPlayer.hasPermission("commandsplugin.votekick.bypass") && ! sender.hasPermission("commandsplugin.votekick.bypass"))
            {
                sender.sendMessage(prefix + "Du kannst nicht einen Votekick gegen diese Person starten!");
                return true;
            }
            if (target == null)
            {
                sender.sendMessage(prefix + args[0] + " ist nicht online!");
                return true;
            }
            if (reason.equals(""))
            {
                CommandsPlugin.broadcast(prefix + CommandsPlugin.getSenderName(sender) + " hat einen Votekick für " + target + " gestartet. Nutze /voteyes oder /voteno um zu voten und /votestatus für den Vote Status!", "commandsplugin.votekick.vote");
            }
            else
            {
                CommandsPlugin.broadcast(prefix + CommandsPlugin.getSenderName(sender) + " hat einen Votekick für " + target + " gestartet. Grund: " + reason + ". Nutze /voteyes oder /voteno um zu voten und /votestatus für den Vote Status!", "commandsplugin.votekick.vote");
            }
            voteStarted = true;
            long time = 20 * 180; // 20 Ticks * 180 = 3 Minutes

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    VotekickCommands.endVoteKick();
                }
            }, time);
            return true;
        }
    }

    public static class VoteyesCommand implements CommandExecutor
    {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            return vote(sender, true);
        }
    }

    public static class VotenoCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            return vote(sender, false);
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

        @Override
        public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
        {
            CommandsPlugin.broadcast(prefix + "Der Votekick gegen " + target + " wurde durch " + CommandsPlugin.getSenderName(commandSender) + " beendet.", "commandsplugin.votekick.vote");
            voteStarted = false;
            endVoteKick();
            return true;
        }
    }

    private static void endVoteKick()
    {
        if (voteStarted)
        {
            double percentYes = Math.round(( votesYes / ( votesYes + votesNo ) ) * 100);
            double percentNo = Math.round(( votesNo / ( votesYes + votesNo ) ) * 100);
            if (percentYes > 50)
            {
                CommandsPlugin.broadcast(prefix + target + " wurde gekickt, da die Mehrheit der Spieler dafür war. (Ja: " + percentYes + "%, Nein: " + percentNo + "%)", "commandsplugin.votekick.vote");
                targetPlayer.kickPlayer("Du wurdest durch einen Votekick gekickt.");
                //ToDo: Temp Ban Player for 5 Minutes
            }
            else
            {
                CommandsPlugin.broadcast(prefix + target + " wurde nicht gekickt, da die Mehrheit der Spieler dagegen war. (Ja: " + percentYes + "%, Nein: " + percentNo + "%)", "commandsplugin.votekick.vote");
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
            sender.sendMessage(prefix + "Derzeit läuft kein Votekick...");
            return true;
        }
        double percentYes = Math.round(( votesYes / ( votesYes + votesNo ) ) * 100);
        double percentNo = Math.round(( votesNo / ( votesYes + votesNo ) ) * 100);
        sender.sendMessage(prefix + "Status: Ja: " + percentYes + "%, Nein: " + percentNo + "%.");
        return true;
    }

    private static boolean vote(CommandSender sender, boolean yes)
    {
        if (! VotekickCommands.voteStarted)
        {
            sender.sendMessage(prefix + "Derzeit läuft kein Votekick...");
            return true;
        }
        if (votedPlayers.contains(sender))
        {
            sender.sendMessage(prefix + "Du kannst nur einmal voten!");
            return true;
        }
        if (yes)
        {
            sender.sendMessage(prefix + "Du hast für einen Kick gestimmt!");
            votesYes = votesYes + 1;
        }
        else
        {
            sender.sendMessage(prefix + "Du hast gegen einen Kick gestimmt!");
            votesNo = votesNo + 1;
        }
        votedPlayers.add(sender);
        sendStatus(sender);

        if (( votesYes + votesNo ) == Bukkit.getOnlinePlayers().length)
        {
            VotekickCommands.endVoteKick();
        }
        return true;
    }
}
