package de.static_interface.commandsplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SpectateCommands
{
    public static HashMap<Player, Player> specedPlayers = new HashMap<Player, Player>();
    public static String prefix = ChatColor.DARK_PURPLE + "[Spec] " + ChatColor.WHITE;

    public static class SpectateCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (! ( sender instanceof Player ))
            {
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }
            Player player = (Player) sender;
            if (specedPlayers.containsKey(player))
            {
                player.sendMessage(prefix + ChatColor.RED + "Du befindest dich bereits im Spectate Modus! Verlasse ihn erst mit /unspec bevor du einen anderen Spieler beobachtest..");
                return true;
            }
            if (args.length < 1)
            {
                return false;
            }

            Player target = ( Bukkit.getServer().getPlayer(args[0]) );
            if (target == null)
            {
                player.sendMessage(prefix + args[0] + " ist nicht online!");
                return true;
            }

            if (target.hasPermission("commandsplugin.spectate.bypass"))
            {
                player.sendMessage(prefix + "Der Spectate Modus kann nicht für den gewählten Spieler aktiviert werden!");
                return true;
            }

            player.sendMessage(prefix + "Zum verlassen des spectate Modus, /unspec nutzen.");


            specedPlayers.put(player, target);

            target.setPassenger(player);
            hide(player, "commandsplugin.spectate.bypass");

            return true;
        }
    }

    public static class UnspectateCommand implements CommandExecutor
    {
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            if (! ( sender instanceof Player ))
            {
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }

            Player player = (Player) sender;
            if (! specedPlayers.containsKey(player))
            {
                player.sendMessage(prefix + ChatColor.RED + "Du befindest dich nicht im Spectate Modus!");
                return true;
            }
            Player target = specedPlayers.get(player);

            target.eject();
            show(player);
            specedPlayers.remove(player);
            sender.sendMessage(prefix + "Du hast den Spectate Modus verlassen.");
            return true;
        }
    }

    public static class SpectatorlistCommand implements CommandExecutor
    {

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
        {
            sender.sendMessage(prefix + "Spieler im Spectate Modus:");
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
            sender.sendMessage(prefix + message);
            return true;
        }
    }

    public static void hide(Player player, String permission)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (p.hasPermission(permission))
            {
                continue;
            }
            p.hidePlayer(player);
        }
    }

    public static void show(Player player)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            p.showPlayer(player);
        }
    }
}
