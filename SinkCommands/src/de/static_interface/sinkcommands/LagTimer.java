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

package de.static_interface.sinkcommands;

import de.static_interface.sinkcommands.commands.LagCommand;
import de.static_interface.sinklibrary.BukkitUtil;
import org.bukkit.ChatColor;

public class LagTimer implements Runnable
{
    String PREFIX = LagCommand.PREFIX;

    @Override
    public void run()
    {
        double tps = SinkCommands.getCommandsTimer().getAverageTPS();
        if ( tps <= 17 )
        {
            BukkitUtil.broadcastMessage(PREFIX + ChatColor.RED + "Der Server laggt gerade!");
        }
        else if ( tps <= 18 )
        {
            BukkitUtil.broadcastMessage(PREFIX + ChatColor.YELLOW + "Der Server könnte gerade etwas laggen!");
        }
    }
}
