package de.static_interface.sinklibrary.configuration;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Author: Trojaner
 * Date: 21.09.13
 */
public interface IConfiguration
{
    public abstract void set(String path, Object value);

    public abstract Object get(String path);

    public abstract YamlConfiguration getYamlConfiguration();
}
