package de.static_interface.commandsplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;


public class CommandsTimer implements Runnable
{
    private final transient Set<String> onlineUsers = new HashSet<>();
    private transient long lastPoll = System.nanoTime();
    private final LinkedList<Double> history = new LinkedList<>();
    private int skip1 = 0;
    private int skip2 = 0;

    CommandsTimer()
    {
        history.add(20d);
    }

    @Override
    public void run()
    {
        onTick();
        final long startTime = System.nanoTime();
        long timeSpent = ( startTime - lastPoll ) / 1000;
        if (timeSpent == 0)
        {
            timeSpent = 1;
        }
        if (history.size() > 10)
        {
            history.remove();
        }
        long tickInterval = 50;
        double tps = tickInterval * 1000000.0 / timeSpent;
        if (tps <= 21)
        {
            history.add(tps);
        }
        lastPoll = startTime;
        int count = 0;
        for (Player player : Bukkit.getServer().getOnlinePlayers())
        {
            count++;
            if (skip1 > 0)
            {
                skip1--;
                continue;
            }
            if (count % 10 == 0)
            {
                long maxTime = 10 * 1000000;
                if (System.nanoTime() - startTime > maxTime / 2)
                {
                    skip1 = count - 1;
                    break;
                }
            }
            onlineUsers.add(player.getName());
        }

        count = 0;
        final Iterator<String> iterator = onlineUsers.iterator();
        while (iterator.hasNext())
        {
            count++;
            if (skip2 > 0)
            {
                skip2--;
                continue;
            }
            if (count % 10 == 0)
            {
                long maxTime = 10 * 1000000;
                if (System.nanoTime() - startTime > maxTime)
                {
                    skip2 = count - 1;
                    break;
                }
            }
        }
    }

    private void onTick()
    {
        //ToDo
    }

    public double getAverageTPS()
    {
        double avg = 0;
        for (Double f : history)
        {
            if (f != null)
            {
                avg += f;
            }
        }
        return avg / history.size();
    }
}