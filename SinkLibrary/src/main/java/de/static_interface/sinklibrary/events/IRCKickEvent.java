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

public class IRCKickEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private final String channel;
    private final String kickerNick;
    private final String kickerLogin;
    private final String kickerHostname;
    private final String recipientNick;
    private final String reason;

    private boolean cancelled = false;

    public IRCKickEvent(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason)
    {
        this.channel = channel;
        this.kickerNick = kickerNick;
        this.kickerLogin = kickerLogin;
        this.kickerHostname = kickerHostname;
        this.recipientNick = recipientNick;
        this.reason = reason;
    }

    public String getChannel()
    {
        return channel;
    }

    public String getKickerNick()
    {
        return kickerNick;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getKickerLogin()
    {
        return kickerLogin;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getKickerHostname()
    {
        return kickerHostname;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getRecipientNick()
    {
        return recipientNick;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getReason()
    {
        return reason;
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
