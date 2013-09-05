package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class MilkCommand implements CommandExecutor
{

    public static final String PREFIX = ChatColor.BLUE + "[Milk] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0) //Remove all
        {
            String s = "";
            int i = 0;
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.hasPotionEffect(PotionEffectType.INVISIBILITY))
                {
                    i++;
                    p.removePotionEffect(PotionEffectType.INVISIBILITY);
                    if (s.equals(""))
                    {
                        s = p.getDisplayName();
                    }
                    else
                    {
                        s = s + ", " + p.getDisplayName();
                    }
                }
            }
            if (i > 0)
            {
                CommandsPlugin.broadcast(PREFIX + "Der Unsichtbarkeits Trank von den folgenden Spielern wurde durch " + CommandsPlugin.getSenderName(sender) + " entfernt:", "commandsplugin.milk.message");
                CommandsPlugin.broadcast(PREFIX + s, "commandsplugin.milk.message");
                return true;
            }
            sender.sendMessage(PREFIX + ChatColor.RED + "Es gibt zur Zeit keine Spieler die einen Unsichtbarkeits Trank haben...");
            return true;
        }
        //Remove from specified player
        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null)
        {
            sender.sendMessage(PREFIX + args[0] + " ist nicht online!");
            return true;
        }

        if (target.hasPotionEffect(PotionEffectType.INVISIBILITY))
        {
            CommandsPlugin.broadcast(PREFIX + "Der Unsichtbarkeits Trank von " + target.getDisplayName() + " wurde durch " + CommandsPlugin.getSenderName(sender) + " entfernt.", "commandsplugin.milk.message");
            target.removePotionEffect(PotionEffectType.INVISIBILITY);
            return true;
        }
        sender.sendMessage(PREFIX + ChatColor.RED + "Spieler \"" + target.getDisplayName() + "\" hat keinen Unsichtbarkeits Trank Effekt!");
        return true;
    }
}
