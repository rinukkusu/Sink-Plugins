package de.static_interface.commandsplugin;

public class Util
{
    public static String ReplaceFormattingAndColorCodes(String input)
    {
        input = input.replace("&0", "§0");
        input = input.replace("&1", "§1");
        input = input.replace("&2", "§2");
        input = input.replace("&3", "§3");
        input = input.replace("&4", "§4");
        input = input.replace("&5", "§5");
        input = input.replace("&6", "§6");
        input = input.replace("&7", "§7");
        input = input.replace("&8", "§8");
        input = input.replace("&9", "§9");
        input = input.replace("&a", "§a");
        input = input.replace("&b", "§b");
        input = input.replace("&c", "§c");
        input = input.replace("&d", "§d");
        input = input.replace("&e", "§e");
        input = input.replace("&f", "§f");
        input = input.replace("&k", "§k");
        input = input.replace("&l", "§l");
        input = input.replace("&m", "§m");
        input = input.replace("&n", "§n");
        input = input.replace("&o", "§o");
        input = input.replace("&r", "§r");
        return input;
    }

    public static boolean isNumber(String input)
    {
        try
        {
            Integer.parseInt(input);
        }
        catch (NumberFormatException ex)
        {
            return false;
        }
        return true;
    }
}
