package de.static_interface.commandsplugin.listener;


import de.static_interface.commandsplugin.commands.SpecCommands;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class SpecListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        if (SpecCommands.specedPlayers.containsValue(event.getPlayer()))
        {
            Player player = getHashMapKey(SpecCommands.specedPlayers, event.getPlayer());
            SpecCommands.specedPlayers.remove(player);
            SpecCommands.show(player);
            player.sendMessage(SpecCommands.prefix + "Spieler hat das Spiel verlassen, Spectate Modus wurde beendet.");
        }
        if (SpecCommands.specedPlayers.containsKey(event.getPlayer()))
        {
            SpecCommands.specedPlayers.remove(event.getPlayer());
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
