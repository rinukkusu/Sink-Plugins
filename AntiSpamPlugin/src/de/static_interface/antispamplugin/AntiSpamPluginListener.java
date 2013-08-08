package de.static_interface.antispamplugin;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntiSpamPluginListener implements Listener
{
    String[] blacklist = { "fak", "piss", "faggit", "spic", "faggot", "damnmit", "son of a bitch", "ass", "arsch", "screw", "cum", "goddamn", "anus", "nigger", "nigga", "suck", "lessy", "damn", "shit", "cocksucker", "motherfucker", "cunt", "dick", "shitface", "clit", "twat", "bastard", "whore", "slut", "jackass", "hooker", "prostitue", "hure", "asshole", "arschloch", "penis", "fotze", "pussy", "idiot", "dummkopf", "noob", "bitch", "fuck", "hurensohn" };
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
        String word = ContainsArrayItem(message, blacklist);
        if (! word.equals(""))
        {
            message = message.replace(word, ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + word + ChatColor.RESET.toString());
            AntiSpamPlugin.warnPlayer(player, "Schreiben der Nachricht: ");
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
            if (! ContainsArrayItem(match, whiteListDomains).equals(""))
            {
                return;
            }
            AntiSpamPlugin.warnPlayer(event.getPlayer(), "Fremdwerbung für folgende Domain: " + match + " !");
            event.setMessage(message.replace(match, "http://kepler-forum.de/board"));
        }
    }

    private String ContainsArrayItem(String input, String[] stringArray)
    {
        int i = 0;
        for (String s : stringArray)
        {
            i++;
            if (input.contains(s))
            {
                return stringArray[i];
            }
        }
        return "";
    }
}
