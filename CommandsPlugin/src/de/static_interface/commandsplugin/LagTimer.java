package de.static_interface.commandsplugin;

import de.static_interface.commandsplugin.commands.LagCommand;
import org.bukkit.ChatColor;

public class LagTimer implements Runnable
{
    String PREFIX = LagCommand.PREFIX;

    @Override
    public void run()
    {
        double tps = CommandsPlugin.getCommandsTimer().getAverageTPS();
        if (tps <= 17)
        {
            CommandsPlugin.broadcastMessage(PREFIX + ChatColor.RED + "Der Server laggt gerade!");
        }
        else if (tps <= 18)
        {
            CommandsPlugin.broadcastMessage(PREFIX + ChatColor.YELLOW + "Der Server könnte gerade etwas laggen!");
        }
    }
}
