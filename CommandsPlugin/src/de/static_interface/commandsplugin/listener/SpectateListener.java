package de.static_interface.commandsplugin.listener;


import de.static_interface.commandsplugin.commands.SpectateCommands;
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
