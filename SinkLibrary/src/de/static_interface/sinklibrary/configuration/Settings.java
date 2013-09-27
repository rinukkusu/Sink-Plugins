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
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Currently unused
 */

public class Settings implements IConfiguration
{
    YamlConfiguration yamlConfiguration;
    File yamlFile;

    public Settings()
    {
        try
        {
            yamlFile = new File(SinkLibrary.getCustomDataFolder(), "Settings.yml");
        }
        catch (NullPointerException ignored)
        {
            yamlConfiguration = null;
        }
        yamlConfiguration = new YamlConfiguration();
        yamlConfiguration = ( exists() ) ? YamlConfiguration.loadConfiguration(yamlFile) : null;
    }

    public void set(String path, Object value)
    {
        yamlConfiguration.set(path, value);
    }

    public Object get(String path)
    {
        return yamlConfiguration.get(path);
    }

    public YamlConfiguration getYamlConfiguration()
    {
        return yamlConfiguration;
    }

    public boolean exists()
    {
        return yamlFile.exists();
    }

    public boolean create()
    {
        if (! yamlFile.exists())
        {
            try
            {
                yamlFile.createNewFile();
            }
            catch (Exception e)
            {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't create settings file", e);
                return false;
            }
        }
        yamlConfiguration = YamlConfiguration.loadConfiguration(yamlFile);
        return true;
    }

    public void save()
    {
        try
        {
            yamlConfiguration.save(yamlFile);
        }
        catch (IOException e)
        {
            Bukkit.getLogger().log(Level.WARNING, "Couldn't save Settings!", e);
        }
    }
}
