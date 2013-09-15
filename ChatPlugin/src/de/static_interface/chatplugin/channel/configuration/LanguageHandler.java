package de.static_interface.chatplugin.channel.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LanguageHandler {

    private static YamlConfiguration language = new YamlConfiguration();
    private static final File languageFilesPath = new File("plugins/ChatPlugin/lang.yml");

    public static boolean init()
    {
        try {
            language.load(languageFilesPath);
        } catch (IOException e) {

            language.set("messages.playerJoined","$PLAYER$ joined channel $CHANNEL$");
            language.set("messages.playerJoins","You have joined the channel $CHANNEL$");
            language.set("messages.playerLeft", "$PLAYER$ left channel $CHANNEL");
            language.set("messages.playerLeaves","You have left the channel $CHANNEL$");
            language.set("messages.noChannelGiven","You have to put a channel !");
            language.set("messages.channelUnknown","$CHANNEL$ is not a known channel.");
            language.set("messages.list", "The following channels are known: $CHANNELS$");
            language.set("messages.part", "You take part in the following channels:");
            language.set("messages.help","There are the following commands for the channel plugin:");

            language.set("messages.permissions.general","You don't have the permission to do that. Ask a moderator for help if you think that this is a mistake !");
            language.set("messages.permissions.shout", "You don't have the permission to shout !");
            language.set("messages.permissions.ask", "You don't have the permission to use the questions chat !");
            language.set("messages.permissions.trade", "You don't have the permission to use the trade chat !");

            try {
                language.save("plugins/ChatPlugin/lang.yml");
            } catch (IOException e1) {
                return false;
            }


        } catch (InvalidConfigurationException e) {
            languageFilesPath.delete();
            Bukkit.getLogger().severe("Invalid configuration detected ! Restoring default configuration ...");
            return init();
        }
        return true;
    }

    public static String getString(String key)
    {
        return ( language.getRoot().getString(key) );
    }

}
