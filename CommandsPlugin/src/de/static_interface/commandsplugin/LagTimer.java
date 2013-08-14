package de.static_interface.commandsplugin;

import de.static_interface.commandsplugin.commands.LagCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class LagTimer implements Runnable
{
    String prefix = LagCommand.prefix;

    @Override
    public void run()
    {
        double tps = CommandsPlugin.getCommandsTimer().getAverageTPS();
        if (tps <= 17)
        {
            Bukkit.broadcastMessage(prefix + ChatColor.RED + "Der Server laggt gerade!");
        }
        else if (tps <= 18)
        {
            Bukkit.broadcastMessage(prefix + ChatColor.YELLOW + "Der Server könnte gerade etwas laggen!");
        }
    }
}
