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

import de.static_interface.sinkcommands.SinkCommands;
import de.static_interface.sinkcommands.commands.GlobalmuteCommand;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GlobalMuteListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        User user = SinkLibrary.getUser(event.getPlayer());
        if ( SinkCommands.globalmuteEnabled && !user.hasPermission("sinkcommands.globalmute.bypass") )
        {
            event.getPlayer().sendMessage(GlobalmuteCommand.PREFIX + "Du kannst nicht schreiben wenn der globale Mute aktiviert ist.");
            event.setCancelled(true);
        }
    }
}
