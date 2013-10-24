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

package de.static_interface.sinkantispam;


import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SinkAntiSpamListener implements Listener
{
    String[] blacklist = {"schlampe", "anus", "nigger", "nigga", "suck", "motherfucker", "bastard", "hure", "asshole", "arschloch", "fotze", "bitch", "hurensohn"};

    String[] whiteListDomains = {"youtube.de", "youtube.com", "google.de", "adventuria.eu"};

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        checkMessage(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
        checkMessage(event);
    }

    public void checkMessage(Event event)
    {
        String message;
        Player player;

        if ( event instanceof AsyncPlayerChatEvent ) //ToDo: make this more clean...
        {
            message = ((AsyncPlayerChatEvent) event).getMessage();
            player = ((AsyncPlayerChatEvent) event).getPlayer();
        }
        else if ( event instanceof PlayerCommandPreprocessEvent )
        {
            message = ((PlayerCommandPreprocessEvent) event).getMessage();
            player = ((PlayerCommandPreprocessEvent) event).getPlayer();
        }
        else
        {
            throw new IllegalArgumentException("Event muss be an instance of AsyncPlayerChatEvent or PlayerCommandPreprocessEvent!");
        }

        User user = SinkLibrary.getUser(player);

        if ( user.hasPermission("sinkcommands.bypass") )
        {
            return;
        }
        String word = ContainsArrayItem(message, blacklist);
        if ( word != null )
        {
            message = message.replace(word, ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + word + ChatColor.RESET.toString());
            SinkAntiSpam.warnPlayer(player, "Schreiben eines verbotenen Wortes: " + message);
            if ( event instanceof AsyncPlayerChatEvent )
            {
                ((AsyncPlayerChatEvent) event).setCancelled(true);
            }
            else
            {
                ((PlayerCommandPreprocessEvent) event).setCancelled(true);
            }
        }
        Pattern pattern = Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
        Matcher matcher = pattern.matcher(message);
        if ( matcher.find() )
        {
            String match = matcher.group(0);
            SinkAntiSpam.warnPlayer(player, "Fremdwerbung für folgende IP: " + match + " !");
            if ( event instanceof AsyncPlayerChatEvent )
            {
                ((AsyncPlayerChatEvent) event).setMessage(message.replace(match, "127.0.0.1"));
            }
            else
            {
                ((PlayerCommandPreprocessEvent) event).setMessage(message.replace(match, " 127.0.0.1"));
            }
        }
        pattern = Pattern.compile(" [a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(/\\S)? ");
        matcher = pattern.matcher(message);
        if ( matcher.find() )
        {
            String match = matcher.group(0);
            if ( match.contains("..") )
            {
                return;
            }
            if ( ContainsArrayItem(match, whiteListDomains) != null )
            {
                return;
            }
            SinkAntiSpam.warnPlayer(player, "Fremdwerbung für folgende Domain: " + match + " !");
            if ( event instanceof AsyncPlayerChatEvent )
            {
                ((AsyncPlayerChatEvent) event).setMessage(message.replace(match, "adventuria.eu"));
            }
            else
            {
                ((PlayerCommandPreprocessEvent) event).setMessage(message.replace(match, " adventuria.eu "));
            }
        }
    }

    private String ContainsArrayItem(String input, String[] stringArray)
    {
        int i = 0;
        for ( String s : stringArray )
        {
            if ( input.toLowerCase().contains(" " + s.toLowerCase() + " ") || input.toLowerCase().contains(" " + s.toLowerCase()) || input.toLowerCase().contains(s.toLowerCase() + " ") || (stringArray.length == 0 && input.toLowerCase().contains(s.toLowerCase())) )
            {
                return stringArray[i];
            }
            i++;
        }
        return null;
    }
}
