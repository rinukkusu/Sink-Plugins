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

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DrugCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.AQUA + "[Drogen] " + ChatColor.RESET;

    public static Player killedByDrugs;


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
            player.sendMessage(PREFIX + ChatColor.BLUE + "Du hast Drogen genommen...");
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 20, 1), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 20, 1), true);
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
                    player.sendMessage(PREFIX + ChatColor.BLUE + "Du hast mehr Drogen genommen...");
                }
                else if (amplifier == 2)
                {
                    player.sendMessage(PREFIX + ChatColor.BLUE + "Vielleicht solltest du nicht so viele Drogen nehmen.");
                }
                else if (amplifier == 3)
                {
                    player.sendMessage(PREFIX + ChatColor.BLUE + "Noch mehr Drogen...");
                }
                else if (amplifier == 4)
                {
                    player.sendMessage(PREFIX + ChatColor.BLUE + "Wow, du hast so viele Drogen genommen... und lebst immer noch!");
                }
                else if (amplifier >= 5)
                {
                    killedByDrugs = player;
                    player.setHealth(0.0);
                    return true;
                }
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.removePotionEffect(PotionEffectType.CONFUSION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration + ( 20 * 30 ), amplifier + 1), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, duration + ( 20 * 30 ), amplifier + 1), true);
            }
        }
        return true;
    }
}
