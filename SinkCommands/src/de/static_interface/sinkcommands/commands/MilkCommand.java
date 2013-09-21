package de.static_interface.sinkcommands.commands;

import de.static_interface.sinkcommands.SinkCommands;
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
                SinkCommands.broadcast(PREFIX + "Der Unsichtbarkeits Trank von den folgenden Spielern wurde durch " + SinkCommands.getSenderName(sender) + " entfernt:", "sinkcommands.milk.message");
                SinkCommands.broadcast(PREFIX + s, "sinkcommands.milk.message");
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
            SinkCommands.broadcast(PREFIX + "Der Unsichtbarkeits Trank von " + target.getDisplayName() + " wurde durch " + SinkCommands.getSenderName(sender) + " entfernt.", "sinkcommands.milk.message");
            target.removePotionEffect(PotionEffectType.INVISIBILITY);
            return true;
        }
        sender.sendMessage(PREFIX + ChatColor.RED + "Spieler \"" + target.getDisplayName() + "\" hat keinen Unsichtbarkeits Trank Effekt!");
        return true;
    }
}
