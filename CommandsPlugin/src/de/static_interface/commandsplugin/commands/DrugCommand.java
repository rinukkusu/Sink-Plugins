package de.static_interface.commandsplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DrugCommand implements CommandExecutor
{
    public static String prefix = ChatColor.AQUA + "[Drogen] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (! ( sender instanceof Player ))
        {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler genutzt werden.");
            return true;
        }

        Player player = (Player) sender;

        if (! player.hasPotionEffect(PotionEffectType.BLINDNESS))
        {
            player.sendMessage(prefix + ChatColor.BLUE + "Du hast Drogen genommen...");
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 20, 1), true);
        }
        else
        {
            for (PotionEffect pe : player.getActivePotionEffects())
            {
                if (! pe.getType().equals(PotionEffectType.BLINDNESS))
                {
                    continue;
                }
                int duration = pe.getDuration();
                int amplifier = pe.getAmplifier();
                if (amplifier == 1)
                {
                    player.sendMessage(prefix + ChatColor.BLUE + "Du hast mehr Drogen genommen...");
                }
                else if (amplifier == 2)
                {
                    player.sendMessage(prefix + ChatColor.BLUE + "Vielleicht solltest du nicht so viele Drogen nehmen.");
                }
                else if (amplifier == 3)
                {
                    player.sendMessage(prefix + ChatColor.BLUE + "Noch mehr Drogen...");
                }
                else if (amplifier == 4)
                {
                    player.sendMessage(prefix + ChatColor.BLUE + "Wow, du hast so viele Drogen genommen... und lebst immer noch!");
                }
                else if (amplifier >= 5)
                {
                    Bukkit.getServer().broadcastMessage(prefix + ChatColor.RED + player.getDisplayName() + ChatColor.WHITE + " nahm zu viele Drogen und ist deswegen gestorben.");
                    player.setHealth(0.0); // Bug: Will send message "%player% died", needs to be fixed
                    return true;
                }
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration + ( 20 * 30 ), amplifier + 1), true);
            }
        }
        return true;
    }
}
