package de.static_interface.chatplugin.command;

import de.static_interface.chatplugin.ChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class NickCommand implements CommandExecutor
{
    public static String PREFIX = ChatColor.DARK_GREEN + "[Nick] " + ChatColor.RESET;
    public static String NICKNAME_PATH = "General.Nickname";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        String newNickname;
        if (args.length < 1)
        {
            return false;
        }
        if (args.length > 2)
        {
            if (! sender.hasPermission("chatplugin.nick.others"))
            {
                sender.sendMessage(ChatColor.RED + "Du hast nicht genügend Rechte um die Namen anderer zu ändern!");
                return true;
            }
            String playerName = args[0];
            newNickname = ChatColor.translateAlternateColorCodes('&', args[1]) + ChatColor.RESET;
            Player target = Bukkit.getServer().getPlayer(playerName);
            if (target == null)
            {
                sender.sendMessage(PREFIX + args[0] + " ist nicht online!");
                return true;
            }

            if (setNickname(target, newNickname))
            {
                sender.sendMessage(PREFIX + playerName + " heisst nun " + newNickname + ".");
            }
            return true;
        }
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(ChatColor.RED + "Dieser Befehl funktioniert nur Ingame.");
            return true;
        }
        newNickname = args[0];
        if (setNickname((Player) sender, newNickname))
        {
            sender.sendMessage(PREFIX + "Du heisst nun " + newNickname + ".");
        }
        return true;
    }

    private boolean setNickname(Player player, String newNickname)
    {
        if (! newNickname.matches("^[a-zA-Z_0-9\u00a7]+$"))
        {
            player.sendMessage(PREFIX + "Ungültiger Nickname!");
            return false;
        }
        if (newNickname.length() > 16)
        {
            player.sendMessage(PREFIX + "Nickname ist zu lang!");
            return false;
        }
        if (newNickname.equals("off"))
        {
            newNickname = ChatPlugin.getDefaultNickName(player);
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if (player == onlinePlayer)
            {
                continue;
            }
            String displayName = onlinePlayer.getDisplayName().toLowerCase(Locale.ENGLISH);
            String name = onlinePlayer.getName().toLowerCase(Locale.ENGLISH);
            String lowerNick = newNickname.toLowerCase(Locale.ENGLISH);
            if (lowerNick.equals(displayName) || lowerNick.equals(name))
            {
                player.sendMessage(PREFIX + "Nickname wird bereits verwendet");
                return false;
            }
        }
        ChatPlugin.setDisplayName(player, newNickname);
        return true;
    }
}
