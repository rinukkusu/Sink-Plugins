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


import de.static_interface.sinkcommands.commands.SpectateCommands;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class SpectateListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (SpectateCommands.specedPlayers.containsValue(event.getPlayer()))
        {
            Player player = getHashMapKey(SpectateCommands.specedPlayers, event.getPlayer());
            SpectateCommands.specedPlayers.remove(player);
            SpectateCommands.show(player);
            player.sendMessage(SpectateCommands.PREFIX + "Spieler hat das Spiel verlassen, Spectate Modus wurde beendet.");
        }
        if (SpectateCommands.specedPlayers.containsKey(event.getPlayer()))
        {
            SpectateCommands.specedPlayers.remove(event.getPlayer());
        }
    }

    private Player getHashMapKey(HashMap<Player, Player> hashmap, Player value)
    {
        for (Player p : hashmap.keySet())
        {
            if (hashmap.get(p) == value)
            {
                return p;
            }
        }
        return null;
    }
}
