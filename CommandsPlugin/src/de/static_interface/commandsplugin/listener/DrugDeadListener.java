package de.static_interface.commandsplugin.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import de.static_interface.commandsplugin.commands.DrugCommand;
import org.bukkit.event.Listener;

public class DrugDeadListener implements Listener{

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDeath(PlayerDeathEvent event)
    {
        if ( event.getEntity().equals(DrugCommand.killedByDrugs) )
        {
            event.setDeathMessage(DrugCommand.PREFIX + ChatColor.RED + event.getEntity().getDisplayName() + ChatColor.WHITE + " nahm zu viele Drogen und ist deswegen gestorben.");
        }
    }
}
