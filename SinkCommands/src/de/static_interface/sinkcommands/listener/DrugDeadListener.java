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

package de.static_interface.sinkcommands.listener;

import de.static_interface.sinkcommands.commands.DrugCommand;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffectType;

public class DrugDeadListener implements Listener
{

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if ( event.getEntity().equals(DrugCommand.killedByDrugs) && event.getEntityType() == EntityType.PLAYER )
        {
            User user = SinkLibrary.getUser(event.getEntity());
            event.setDeathMessage(DrugCommand.PREFIX + ChatColor.RED + user.getDisplayName() + ChatColor.WHITE + " nahm zu viele Drogen und ist deswegen gestorben.");
            DrugCommand.killedByDrugs.removePotionEffect(PotionEffectType.BLINDNESS);
            DrugCommand.killedByDrugs.removePotionEffect(PotionEffectType.CONFUSION);
            DrugCommand.killedByDrugs = null;
        }
    }
}
