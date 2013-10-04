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

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

public abstract class ConfigurationBase
{
    /**
     * Create Configuration File
     *
     * @return True if it has been successfully created
     */
    public abstract boolean create();

    /**
     * Save config file
     */
    public abstract void save();

    /**
     * @param path  Path to value
     * @param value Value of path
     */
    public abstract void set(String path, Object value);

    /**
     * Get value from config
     *
     * @param path Path to value
     * @return Value of path
     */
    public abstract Object get(String path);

    /**
     * Get YAML Configuration
     */
    public abstract YamlConfiguration getYamlConfiguration();

    /**
     * @return True if the config file exists
     */
    public abstract boolean exists();

    /**
     * Load Configuration
     *
     * @return True if successfully loaded, false if file doesnt exists.
     */
    public abstract boolean load();

    /**
     * Get default value for path
     *
     * @param path Path to value
     */
    public Object getDefault(String path)
    {
        if (getDefaults() == null)
        {
            throw new RuntimeException("defaultValues are null! Couldn't read value from path: " + path);
        }
        return getDefaults().get(path);
    }

    /**
     * Add a default value
     *
     * @param path  Path to value
     * @param value Value of path
     */
    public void addDefault(String path, Object value)
    {
        if (getDefaults() == null)
        {
            throw new RuntimeException("defaultValues are null! Couldn't add " + value + " to path: " + path);
        }
        getDefaults().put(path, value);
        getYamlConfiguration().addDefault(path, value);
    }

    /**
     * Get Defaults
     *
     * @return Default values
     */
    public abstract HashMap<String, Object> getDefaults();
}
