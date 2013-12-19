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

package de.static_interface.sinklibrary.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IRCNickChangeEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private final String oldNick;
    private final String login;
    private final String hostname;
    private final String newNick;

    private boolean cancelled = false;

    public IRCNickChangeEvent(String oldNick, String login, String hostname, String newNick)
    {
        this.oldNick = oldNick;
        this.login = login;
        this.hostname = hostname;
        this.newNick = newNick;
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean value)
    {
        cancelled = value;
    }

    public String getOldNick()
    {
        return oldNick;
    }

    public String getNewNick()
    {
        return newNick;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getLogin()
    {
        return login;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getHostname()
    {
        return hostname;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    @SuppressWarnings("UnusedDeclaration")
    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
