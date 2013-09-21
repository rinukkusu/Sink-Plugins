package de.static_interface.sinkcommands;

import de.static_interface.sinkcommands.commands.LagCommand;
import org.bukkit.ChatColor;

public class LagTimer implements Runnable
{
    String PREFIX = LagCommand.PREFIX;

    @Override
    public void run()
    {
        double tps = SinkCommands.getCommandsTimer().getAverageTPS();
        if (tps <= 17)
        {
            SinkCommands.broadcastMessage(PREFIX + ChatColor.RED + "Der Server laggt gerade!");
        }
        else if (tps <= 18)
        {
            SinkCommands.broadcastMessage(PREFIX + ChatColor.YELLOW + "Der Server könnte gerade etwas laggen!");
        }
    }
}
