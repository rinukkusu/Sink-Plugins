package de.static_interface.sinklibrary.commands;

import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.User;
import de.static_interface.sinklibrary.configuration.LanguageConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SinkReloadCommand implements CommandExecutor
{
    private static String pluginName = SinkLibrary.getPluginName();
    public static final String PREFIX = ChatColor.DARK_GREEN + "[" + pluginName + "] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        String name;
        LanguageConfiguration.reload();
        name = LanguageConfiguration.getFile().getName();

        sender.sendMessage(PREFIX + "Reloaded " + name);

        SinkLibrary.getSettings().reload();
        name = SinkLibrary.getSettings().getFile().getName();
        sender.sendMessage(PREFIX + "Reloaded " + name);

        sender.sendMessage(PREFIX + "Reloading PlayerConfigurations...");
        for ( Player p : Bukkit.getOnlinePlayers() )
        {
            User user = SinkLibrary.getUser(p);
            user.getPlayerConfiguration().reload();
        }
        sender.sendMessage(PREFIX + ChatColor.GREEN + "Done");
        return true;
    }
}
