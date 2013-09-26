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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class ClearCommand implements CommandExecutor
{

    public static final String PREFIX = ChatColor.RED + "[Clear] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] argsArr)
    {
        List<String> args = Arrays.asList(argsArr);

        Player player = null;

        int i = 0;
        boolean clearInvetory = false;
        boolean clearEffects = false;
        boolean clearArmor = false;
        if (args.contains("-i") || args.contains("-inventory"))
        {
            clearInvetory = true;
            i++;
        }
        if (args.contains("-e") || args.contains("-effects"))
        {
            clearEffects = true;
            i++;
        }
        if (args.contains("-ar") || args.contains("-armor"))
        {
            clearArmor = true;
            i++;
        }
        if (args.contains("-a") || args.contains("-all"))
        {
            clearInvetory = true;
            clearEffects = true;
            clearArmor = true;
            i++;
        }
        if (! clearInvetory && ! clearEffects && ! clearArmor)
        {
            clearInvetory = true;
        }

        if (argsArr.length == i + 1)
        {
            String name = argsArr[0];
            if (! sender.hasPermission("sinkcommands.clear.others"))
            {
                sender.sendMessage(PREFIX + "Du hast nicht genügend Rechte um das Inventar von anderen Spielern zu leeren!");
            }
            player = Bukkit.getPlayer(name);
            if (player == null)
            {
                sender.sendMessage(PREFIX + "Spieler wurde nicht gefunden.");
                return true;
            }
        }
        else if (! ( sender instanceof Player ))
        {
            sender.sendMessage(PREFIX + "Dieser Befehl ist nur ingame verfügbar.");
        }
        if (player == null)
        {
            player = (Player) sender;
        }

        if (clearInvetory)
        {
            player.getInventory().clear();
        }

        if (clearEffects)
        {
            player.removePotionEffect(PotionEffectType.ABSORPTION);
            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.removePotionEffect(PotionEffectType.CONFUSION);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
            player.removePotionEffect(PotionEffectType.HARM);
            player.removePotionEffect(PotionEffectType.HEAL);
            player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
            player.removePotionEffect(PotionEffectType.HUNGER);
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.removePotionEffect(PotionEffectType.POISON);
            player.removePotionEffect(PotionEffectType.REGENERATION);
            player.removePotionEffect(PotionEffectType.SATURATION);
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.WATER_BREATHING);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.removePotionEffect(PotionEffectType.WITHER);
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        }
        if (clearArmor)
        {
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
        }
        if (player != sender)
        {
            sender.sendMessage(PREFIX + player.getDisplayName() + " wurde gecleart.");
        }
        player.sendMessage(PREFIX + "Du wurdest gecleart.");
        return true;
    }

}