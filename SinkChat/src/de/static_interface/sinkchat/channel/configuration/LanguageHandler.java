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

package de.static_interface.sinkchat.channel.configuration;

import de.static_interface.sinkchat.SinkChat;
import de.static_interface.sinklibrary.configuration.IConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LanguageHandler implements IConfiguration
{

    private static YamlConfiguration language = new YamlConfiguration();
    private static final File languageFilesPath = new File(SinkChat.getDataFolderStatic() + File.separator + ( "lang.yml" ));

    public static boolean init()
    {
        try
        {
            language.load(languageFilesPath);
        }
        catch (IOException e)
        {

            language.set("messages.playerJoins", "Du bist dem $CHANNEL$ Channel gejoint.");
            language.set("messages.playerLeaves", "Du hast den $CHANNEL$ Channel verlassen.");
            language.set("messages.noChannelGiven", "Du musst einen Channel angeben!");
            language.set("messages.channelUnknown", "$CHANNEL$ ist ein unbekannter Channel.");
            language.set("messages.list", "Folgende Channels sind bekannt: $CHANNELS$");
            language.set("messages.part", "Du bist in den folgenden Channels:");
            language.set("messages.help", "Folgende Befehle sind verfuegbar:");

            language.set("messages.permissions.general", "Du hast nicht genuegend Rechte um das zu tun.");
            language.set("messages.permissions.shout", "Du hast nicht genuegend Rechte um das zu tun.");
            language.set("messages.permissions.ask", "Du hast nicht genuegend Rechte um das zu tun.");
            language.set("messages.permissions.trade", "Du hast nicht genuegend Rechte um das zu tun.");

            try
            {
                language.save(languageFilesPath);
            }
            catch (IOException e1)
            {
                return false;
            }


        }
        catch (InvalidConfigurationException e)
        {
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

    @Override
    public void set(String path, Object value)
    {
        language.set(path, value);
    }

    @Override
    public Object get(String path)
    {
        return language.get(path);
    }

    @Override
    public YamlConfiguration getYamlConfiguration()
    {
        return language;
    }
}
