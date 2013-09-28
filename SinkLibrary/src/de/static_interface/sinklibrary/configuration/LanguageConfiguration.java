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

package de.static_interface.sinklibrary.configuration;

import de.static_interface.sinklibrary.SinkLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class LanguageConfiguration implements IConfiguration
{
    private static YamlConfiguration language = new YamlConfiguration();
    private static final File languageFilesPath = new File(SinkLibrary.getCustomDataFolder(), "language.yml");

    public boolean create()
    {
        try
        {
            language.load(languageFilesPath);
        }
        catch (IOException e)
        {

            language.set("messages.general.notOnline", "§c%s is not online!");
            language.set("messages.general.consoleNotAvailabe", "§cThis command is only ingame available");

            language.set("messages.commands.nick.otherChanged", "%s's name is now %s!");
            language.set("messages.commands.nick.selfChanged", "Your name is now %s!");
            language.set("messages.commands.nick.illegalNickname", "Illegal Nickname!");
            language.set("messages.commands.nick.tooLong", "Nickname is too long!");
            language.set("messages.commands.nick.used", "Nickname is already used by someone other!");

            language.set("messages.commands.channel.playerJoins", "You joined the %s channel.");
            language.set("messages.commands.channel.playerLeaves", "You left the %s channel.");
            language.set("messages.commands.channel.noChannelGiven", "You must write the name of the channel!");
            language.set("messages.commands.channel.channelUnknown", "%s is an unknown channel.");
            language.set("messages.commands.channel.list", "These channels are available: %s");
            language.set("messages.commands.channel.part", "You have the following channels enabled:");
            language.set("messages.commands.channel.help", "These commands are: available");

            language.set("messages.commands.spy.enabled", "§aSpy chat has been enabled!");
            language.set("messages.commands.spy.alreadyEnabled", "§cSpy chat has been already enabled!");

            language.set("messages.commands.spy.disabled", "§cSpy chat has been disabled!");
            language.set("messages.commands.spy.alreadyDisabled", "§cSpy chat has been already disabled!");

            language.set("messages.channels.help", "Help");
            language.set("messages.channels.shout", "Shout");
            language.set("messages.channels.trade", "Trade");

            language.set("messages.channel.help.prefix", "?"); //Todo: move these to Settings?
            language.set("messages.channel.shout.prefix", "!");
            language.set("messages.channel.trade.prefix", "$");

            language.set("messages.permissions.general", "§4You dont have permissions to do that.");
            language.set("messages.permissions.channels.shout", "§4You may not use the shout channel.");
            language.set("messages.permissions.channels.help", "§4You may not use the help channel.");
            language.set("messages.permissions.channels.trade", "§4You may not use the trade channel.");
            language.set("messages.permissions.nick.other", "§4You may not change the nickname of other players!");

            language.set("message.prefix.channel", ChatColor.GREEN + "[Channel]");
            language.set("message.prefix.nick", ChatColor.DARK_GREEN + "[Nick]");
            language.set("message.prefix.spy", ChatColor.DARK_GRAY + "[Spy]");
            language.set("message.prefix.chatLocal", ChatColor.GRAY + "[Local]");

            save();
        }
        catch (InvalidConfigurationException e)
        {
            languageFilesPath.delete();
            Bukkit.getLogger().severe("Invalid configuration detected ! Restoring default configuration ...");
            return create();
        }
        return true;
    }

    public void save()
    {
        try
        {
            language.save(languageFilesPath);
        }
        catch (Exception e)
        {
            Bukkit.getLogger().log(Level.WARNING, "WARNING: Couldn't save language configuration!", e);
        }
    }

    public static String _(String key)
    {
        return language.getRoot().getString("messages." + key);
    }

    public void set(String path, Object value)
    {
        language.set(path, value);
    }

    public Object get(String path)
    {
        return language.get(path);
    }

    public YamlConfiguration getYamlConfiguration()
    {
        return language;
    }

    public boolean exists()
    {
        return languageFilesPath.exists();
    }
}
