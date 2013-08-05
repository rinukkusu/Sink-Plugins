package de.static_interface.commandsplugin.commands;

import de.static_interface.commandsplugin.CommandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FreezeCommands
{
    public static String prefix = ChatColor.RED + "[Freeze] " + ChatColor.WHITE;

    public static class FreezeCommand implements CommandExecutor
    {

        private final CommandsPlugin plugin;

        public FreezeCommand(CommandsPlugin plugin)
        {
            this.plugin = plugin;
        }

        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            if (args.length < 1)
            {
                return false;
            }
            String reason = "";
            int i = - 1;
            for (String s : args)
            {
                i++;
                if (i <= 0)
                {
                    continue;
                }
                if (reason.equals(""))
                {
                    reason = s;
                }
                else
                {
                    reason = reason + " " + s;
                }
            }

            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (p.getName().toLowerCase().contains(args[0].toLowerCase()) || p.getName().equalsIgnoreCase(args[0]))
                {
                    if (args.length < 2 && ! CommandsPlugin.toFreeze.contains(p.getName()))
                    {
                        return false;
                    }
                    if (! plugin.canBeFrozen(p) && sender instanceof Player)
                    {
                        sender.sendMessage(prefix + ChatColor.DARK_RED + "Dieser Spieler kann nicht eingefroren werden!");
                        return true;
                    }
                    if (plugin.toggleFreeze(p))
                    {
                        if (sender instanceof Player)
                        {
                            p.sendMessage(prefix + ChatColor.RED + "Du wurdest von " + ( (Player) sender ).getDisplayName() + " eingefroren. Grund: " + reason);
                            CommandsPlugin.broadcast(prefix + p.getDisplayName() + " wurde von " + ( (Player) sender ).getDisplayName() + " eingefroren. Grund: " + reason, "commandsplugin.freeze.message");
                        }
                        else
                        {
                            p.sendMessage(prefix + ChatColor.RED + "Du wurdest von der Console aus eingefroren. Grund: " + reason);
                            CommandsPlugin.broadcast(prefix + p.getDisplayName() + " wurde von der Console aus eingefroren. Grund: " + reason, "commandsplugin.freeze.message");
                        }
                        return true;
                    }
                    if (sender instanceof Player)
                    {
                        p.sendMessage(prefix + ChatColor.RED + "Du wurdest von " + ( (Player) sender ).getDisplayName() + " aufgetaut.");
                        CommandsPlugin.broadcast(prefix + p.getDisplayName() + " wurde von " + ( (Player) sender ).getDisplayName() + " aufgetaut.", "commandsplugin.freeze.message");
                    }
                    else
                    {
                        p.sendMessage(prefix + ChatColor.RED + "Du wurdest von der Console aus aufgetaut.");
                        CommandsPlugin.broadcast(prefix + p.getDisplayName() + " wurde von der Console aus aufgetaut.", "commandsplugin.freeze.message");
                    }
                    return true;
                }
            }

            return true;
        }

    }

    public static class TmpfreezeCommand implements CommandExecutor
    {

        private final CommandsPlugin plugin;

        public TmpfreezeCommand(CommandsPlugin plugin)
        {
            this.plugin = plugin;
        }


        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            if (args.length < 3)
            {
                return false;
            }
            String reason = "";
            int i = - 1;
            for (String s : args)
            {
                i++;
                if (i <= 1)
                {
                    continue;
                }
                if (reason.equals(""))
                {
                    reason = s;
                }
                else
                {
                    reason = reason + " " + s;
                }
            }
            int seconds = 0;
            if (! isNumber(args[1]) || Integer.parseInt(args[1]) <= 0)
            {
                Pattern p = Pattern.compile("(\\d)+d");
                Matcher m = p.matcher(args[1]);

                if (m.find())
                {
                    seconds += 86400 * Integer.parseInt(m.group(1).trim());
                }

                p = Pattern.compile("(\\d)+h");
                m = p.matcher(args[1]);

                if (m.find())
                {
                    seconds += 3600 * Integer.parseInt(m.group(1).trim());
                }

                p = Pattern.compile("(\\d)+m");
                m = p.matcher(args[1]);

                if (m.find())
                {
                    seconds += 60 * Integer.parseInt(m.group(1).trim());
                }

                p = Pattern.compile("(\\d)+s");
                m = p.matcher(args[1]);

                if (m.find())
                {
                    seconds += Integer.parseInt(m.group(1).trim());
                }

                if (seconds <= 0)
                {
                    sender.sendMessage(prefix + ChatColor.RED + "Ungültige Zeit (muss größer als 0 sein)!");
                    return true;
                }
            }
            else
            {
                seconds = Integer.parseInt(args[1]);
            }

            for (final Player p : Bukkit.getOnlinePlayers())
            {
                if (p.getName().toLowerCase().contains(args[0].toLowerCase()) || p.getName().equalsIgnoreCase(args[0]))
                {
                    if (! plugin.temporarilyFreeze(p, seconds))
                    {
                        sender.sendMessage(prefix + "Dieser Spieler wurde schon eingefroren");
                        return true;
                    }
                    if (sender instanceof Player)
                    {
                        p.sendMessage(prefix + ChatColor.RED + "Du wurdest von " + ( (Player) sender ).getDisplayName() + " für " + seconds + " Sekunden eingefroren. Grund: " + reason);
                        CommandsPlugin.broadcast(prefix + p.getDisplayName() + " wurde von " + ( (Player) sender ).getDisplayName() + " für " + seconds + " Sekunden eingefroren. Grund: " + reason, "commandsplugin.freeze.message");
                    }
                    else
                    {
                        p.sendMessage(prefix + ChatColor.RED + "Du wurdest von der Console aus für " + seconds + " Sekunden eingefroren. Grund: " + reason);
                        CommandsPlugin.broadcast(prefix + p.getDisplayName() + " wurde von der Console aus für " + seconds + " Sekunden eingefroren. Grund: " + reason, "commandsplugin.freeze.message");
                    }
                }
            }
            return true;
        }
    }

    public static class FreezeallCommand implements CommandExecutor
    {
        private final CommandsPlugin plugin;

        public FreezeallCommand(CommandsPlugin plugin)
        {
            this.plugin = plugin;
        }

        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            if (args.length < 1 && CommandsPlugin.freezeAll.size() != Bukkit.getWorlds().size())
            {
                return false;
            }

            String reason = "";
            for (String s : args)
            {
                if (reason.equals(""))
                {
                    reason = s;
                }
                else
                {
                    reason = reason + " " + s;
                }
            }

            if (plugin.toggleFreezeAll())
            {
                if (sender instanceof ConsoleCommandSender)
                {
                    Bukkit.broadcastMessage(prefix + ChatColor.RED + "Alle Spieler wurden von der Console aus eingefroren. Grund: " + reason);
                }
                else
                {
                    Bukkit.broadcastMessage(prefix + ChatColor.RED + "Alle Spieler wurden von " + ( (Player) sender ).getDisplayName() + " eingefroren. Grund: " + reason);
                }
            }
            else
            {
                if (sender instanceof ConsoleCommandSender)
                {
                    Bukkit.broadcastMessage(prefix + ChatColor.RED + "Alle Spieler wurden von der Console aus aufgetaut.");
                }
                else
                {
                    Bukkit.broadcastMessage(prefix + ChatColor.RED + "Alle Spieler wurden von " + ( (Player) sender ).getDisplayName() + " aufgetaut.");
                }
            }
            return true;
        }
    }

    public static class FreezelistCommand implements CommandExecutor
    {

        private final CommandsPlugin plugin;

        public FreezelistCommand(CommandsPlugin plugin)
        {
            this.plugin = plugin;
        }

        public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
        {
            String frozenList = "";
            String tmpfrozenList = "";
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (plugin.isFrozen(p))
                {
                    if (frozenList.length() > 0)
                    {
                        frozenList = frozenList + ", " + p.getDisplayName();
                    }
                    else
                    {
                        frozenList = p.getDisplayName();
                    }
                }

                if (plugin.isTmpFrozen(p) > 0)
                {
                    if (tmpfrozenList.length() > 0)
                    {
                        tmpfrozenList = tmpfrozenList + ", " + p.getDisplayName() + " für " + plugin.isTmpFrozen(p) + " Sekunden";
                    }
                    else
                    {
                        tmpfrozenList = p.getDisplayName() + " für " + plugin.isTmpFrozen(p) + " Sekunden";
                    }
                }
            }

            if (frozenList.length() > 0)
            {
                sender.sendMessage(prefix + "Eingefrorene Spieler: " + frozenList);
            }
            else
            {
                sender.sendMessage(prefix + "Es gibt keine eingefrorenen Spieler.");
            }

            if (tmpfrozenList.length() > 0)
            {
                sender.sendMessage(prefix + "Temporär eingefrorene Spieler: " + tmpfrozenList);
            }
            else
            {
                sender.sendMessage(prefix + "Es gibt keine temporär eingefrorenen Spieler.");
            }
            return true;
        }
    }

    private static boolean isNumber(String in)
    {
        try
        {
            Integer.parseInt(in);
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
        return true;
    }
}