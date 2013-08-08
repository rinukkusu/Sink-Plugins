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

    public static String prefix = ChatColor.BLUE + "[Milk] " + ChatColor.WHITE;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (! ( sender instanceof Player ))
        {
            sender.sendMessage(prefix + "Dieser Befehl kann nur von einem Spieler genutzt werden.");
            return true;
        }
        Player player = (Player) sender;
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
                CommandsPlugin.broadcast(prefix + "Der Unsichtbarkeits Trank von den folgenden Spielern wurde durch " + player.getDisplayName() + " entfernt:", "commandsplugin.milk.message");
                CommandsPlugin.broadcast(prefix + s, "commandsplugin.milk.message");
                return true;
            }
            sender.sendMessage(prefix + ChatColor.RED + "Es gibt keine Spieler die einen Unsichtbarkeits Trank haben...");
            return true;
        }
        //Remove from specified player
        Player target = ( Bukkit.getServer().getPlayer(args[0]) );
        if (target == null)
        {
            sender.sendMessage(prefix + args[0] + " ist nicht online!");
            return true;
        }

        if (target.hasPotionEffect(PotionEffectType.INVISIBILITY))
        {
            CommandsPlugin.broadcast(prefix + "Der Unsichtbarkeits Trank von " + target.getDisplayName() + " wurde durch " + player.getDisplayName() + " entfernt.", "commandsplugin.milk.message");
            target.removePotionEffect(PotionEffectType.INVISIBILITY);
            return true;
        }
        sender.sendMessage(prefix + ChatColor.RED + "Spieler \"" + target.getDisplayName() + "\" hat keinen Unsichtbarkeits Trank Effekt!");
        return true;
    }
}
