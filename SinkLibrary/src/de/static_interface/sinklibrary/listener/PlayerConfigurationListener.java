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

package de.static_interface.sinklibrary.listener;

import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.PlayerConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConfigurationListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        User user = new User(event.getPlayer());
        PlayerConfiguration config = user.getPlayerConfiguration();

        if (! config.exists())
        {
            config.create();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        User user = new User(event.getPlayer());
        PlayerConfiguration config = user.getPlayerConfiguration();
        config.save();
    }
}
