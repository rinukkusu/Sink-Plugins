package de.static_interface.commandsplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RenameCommand implements CommandExecutor
{

    public static String PREFIX = ChatColor.AQUA + "[Rename] " + ChatColor.RESET;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (! ( sender instanceof Player ))
        {
            sender.sendMessage(PREFIX + "Dieser Befehl ist nur Ingame ausführbar.");
            return true;
        }
        Player p = (Player) sender;

        if (args.length < 2)
        {
            return false;
        }
        if (p.getItemInHand().getTypeId() == 0)
        {
            sender.sendMessage(PREFIX + "Nimm ein Item in die Hand bevor du diesen Befehl ausführst.");
            return true;
        }
        String text = "";
        for (int x = 1; x < args.length; x++)
        {
            text += args[x];
            if (x + 1 < args.length)
            {
                text += " ";
            }
        }
        text = ChatColor.translateAlternateColorCodes('&', text);
        ItemStack item = p.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        switch (args[0].toLowerCase())
        {
            case "item":
                meta.setDisplayName(text);
                sender.sendMessage(PREFIX + ChatColor.GRAY + "Name von Item wurde zu: " + ChatColor.GREEN + text + ChatColor.GRAY + " umbennant.");
                break;
            case "lore":
                List<String> lore = new ArrayList<>();
                lore.add(text);
                meta.setLore(lore);
                sender.sendMessage(PREFIX + ChatColor.GRAY + "Die Lore von Item wurde zu: " + ChatColor.GREEN + text + ChatColor.GRAY + " umbennant.");
                break;

            default:
                return false;
        }
        item.setItemMeta(meta);
        return true;
    }
}