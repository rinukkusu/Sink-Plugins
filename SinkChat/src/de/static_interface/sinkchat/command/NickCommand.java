package de.static_interface.sinkchat.command;

import de.static_interface.sinkchat.SinkChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class NickCommand implements CommandExecutor
{
    public static final String PREFIX = ChatColor.DARK_GREEN + "[Nick] " + ChatColor.RESET;
    public static final String NICKNAME_PATH = "General.Nickname";
    public static final String HAS_NICKNAME_PATH = "General.HasNickname";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        String newDisplayName;
        if (args.length < 1)
        {
            return false;
        }
        if (args.length > 2)
        {
            if (! sender.hasPermission("sinkchat.nick.others"))
            {
                sender.sendMessage(ChatColor.RED + "Du hast nicht genügend Rechte um die Namen anderer zu ändern!");
                return true;
            }
            String playerName = args[0];
            Player target = Bukkit.getServer().getPlayer(playerName);
            newDisplayName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args[1]) + ChatColor.RESET;
            if (target == null)
            {
                sender.sendMessage(PREFIX + args[0] + " ist nicht online!");
                return true;
            }

            if (setDisplayName(target, newDisplayName))
            {
                sender.sendMessage(PREFIX + playerName + " heisst nun " + SinkChat.getDisplayName(target) + ".");
            }
            return true;
        }
        if (sender instanceof ConsoleCommandSender)
        {
            sender.sendMessage(ChatColor.RED + "Dieser Befehl funktioniert nur Ingame.");
            return true;
        }
        newDisplayName = ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', args[0]) + ChatColor.RESET;
        Player player = (Player) sender;
        if (setDisplayName(player, newDisplayName))
        {
            sender.sendMessage(PREFIX + "Du heisst nun " + SinkChat.getDisplayName(player) + ".");
        }
        return true;
    }

    private boolean setDisplayName(Player player, String newDisplayName)
    {
        String cleanDisplayName = ChatColor.stripColor(newDisplayName);
        if (! cleanDisplayName.matches("^[a-zA-Z_0-9\u00a7]+$"))
        {
            player.sendMessage(PREFIX + "Ungültiger Nickname!");
            return false;
        }
        if (cleanDisplayName.length() > 16)
        {
            player.sendMessage(PREFIX + "Nickname ist zu lang!");
            return false;
        }
        if (cleanDisplayName.equals("off"))
        {
            newDisplayName = SinkChat.getDefaultDisplayName(player);
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
        {
            if (player == onlinePlayer)
            {
                continue;
            }
            String displayName = onlinePlayer.getDisplayName().toLowerCase();
            String name = onlinePlayer.getName().toLowerCase();
            String lowerNick = newDisplayName.toLowerCase();
            if (lowerNick.equals(displayName) || lowerNick.equals(name))
            {
                player.sendMessage(PREFIX + "Der Nickname wird bereits verwendet");
                return false;
            }
        }
        SinkChat.setDisplayName(player, newDisplayName);
        return true;
    }
}
