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

public class IRCPingEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private final String sourceNick;
    private final String sourceLogin;
    private final String sourceHostname;
    private final String target;
    private final String pingValue;

    private boolean cancelled = false;

    public IRCPingEvent(String sourceNick, String sourceLogin, String sourceHostname, String target, String pingValue)
    {
        this.sourceNick = sourceNick;
        this.sourceLogin = sourceLogin;
        this.sourceHostname = sourceHostname;
        this.target = target;
        this.pingValue = pingValue;
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

    public String getSourceNick()
    {
        return sourceNick;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getSourceLogin()
    {
        return sourceLogin;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getSourceHostname()
    {
        return sourceHostname;
    }

    public String getTarget()
    {
        return target;
    }

    public String getPingValue()
    {
        return pingValue;
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
