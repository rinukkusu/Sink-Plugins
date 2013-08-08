package de.static_interface.antispamplugin;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntiSpamPluginListener implements Listener
{
    String[] blacklist = { "phöse" };
    String[] whiteListDomains = { "kepler-forum.de", "youtube.de", "youtube.com", "google.de" };

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if (player.hasPermission("commandsplugin.bypass"))
        {
            return;
        }
        if (ContainsArrayItem(message, blacklist))
        {
            AntiSpamPlugin.warnPlayer(player, "Posting von einem Blacklist - Wort.");
            event.setCancelled(true);
        }
        Pattern pattern = Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find())
        {
            String match = matcher.group(0);
            AntiSpamPlugin.warnPlayer(event.getPlayer(), "Fremdwerbung für folgende IP: " + match + " !");
            event.setMessage(message.replace(match, "127.0.0.1"));
        }
        pattern = Pattern.compile("[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(/\\S)?");
        matcher = pattern.matcher(message);
        if (matcher.find())
        {
            String match = matcher.group(0);
            if (match.contains(".."))
            {
                return;
            }
            if (ContainsArrayItem(match, whiteListDomains))
            {
                return;
            }
            AntiSpamPlugin.warnPlayer(event.getPlayer(), "Fremdwerbung für folgende Domain: " + match + " !");
            event.setMessage(message.replace(match, "http://kepler-forum.de/board"));
        }
    }

    private boolean ContainsArrayItem(String input, String[] stringArray)
    {
        for (String s : stringArray)
        {
            if (input.contains(s))
            {
                return true;
            }
        }
        return false;
    }
}
