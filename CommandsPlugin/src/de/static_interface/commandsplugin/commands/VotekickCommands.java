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
    public static String prefix = ChatColor.BOLD + ChatColor.DARK_GREEN.toString() + "[VoteKick] " + ChatColor.RESET;

    public static int votesYes = 0;
    public static int votesNo = 0;
    public static boolean voteEnabled;
    public static Player targetPlayer;
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
            if (args.length < 2)
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
            if (voteEnabled)
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
            final String target = targetPlayer.getDisplayName();

            if (target == null)
            {
                sender.sendMessage(prefix + args[0] + " ist nicht online!");
                return true;
            }

            CommandsPlugin.broadcast(prefix + CommandsPlugin.getSenderName(sender) + " hat einen Votekick für " + target + " gestartet. Nutze /voteyes oder /voteno um zu voten!", "commandsplugin.votekick.vote");
            voteEnabled = true;
            long time = 20 * 180; // 20 Ticks * 180 = 3 Minutes

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    double percent = ( votesYes / ( votesYes + votesNo ) ) * 100;
                    if (percent > 50)
                    {
                        CommandsPlugin.broadcast(prefix + target + " wurde gekickt, da die Mehrheit der Spieler dafür waren. (Ja: " + votesYes + ", Nein: " + votesNo + ")", "commandsplugin.votekick.vote");
                        targetPlayer.kickPlayer("Du wurdest durch einen Votekick gekickt.");
                        //ToDo: Temp Ban Player for 5 Minutes
                    }
                    else
                    {
                        CommandsPlugin.broadcast(prefix + target + " wurde nicht gekickt, da die Mehrheit der Spieler dagegen waren. (Ja: " + votesYes + ", Nein: " + votesNo + ")", "commandsplugin.votekick.vote");
                    }
                    votesYes = 0;
                    votesNo = 0;
                    targetPlayer = null;
                    voteEnabled = false;
                    votedPlayers.clear();
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
            if (! VotekickCommands.voteEnabled)
            {
                sender.sendMessage(prefix + "Derzeit läuft kein Votekick...");
                return true;
            }
            if (votedPlayers.contains(sender))
            {
                sender.sendMessage(prefix + "Du kannst nur einmal voten!");
                return true;
            }
            sender.sendMessage(prefix + "Du hast für einen Kick gestimmt!");
            votedPlayers.add(sender);
            votesYes = votesYes + 1;
            return true;
        }
    }

    public static class VotenoCommand implements CommandExecutor
    {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (! VotekickCommands.voteEnabled)
            {
                sender.sendMessage(prefix + "Derzeit läuft kein Votekick...");
                return true;
            }
            if (votedPlayers.contains(sender))
            {
                sender.sendMessage(prefix + "Du kannst nur einmal voten!");
                return true;
            }
            sender.sendMessage(prefix + "Du hast gegen einen Kick gestimmt!");
            votedPlayers.add(sender);
            votesNo = votesNo + 1;
            return true;
        }
    }
}
