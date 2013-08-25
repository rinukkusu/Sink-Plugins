package de.static_interface.commandsplugin;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("UnusedDeclaration")
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

    public static String RemoveFormattingAndColorCodes(String input)
    {
        input = input.replace("§0", "");
        input = input.replace("§1", "");
        input = input.replace("§2", "");
        input = input.replace("§3", "");
        input = input.replace("§4", "");
        input = input.replace("§5", "");
        input = input.replace("§6", "");
        input = input.replace("§7", "");
        input = input.replace("§8", "");
        input = input.replace("§9", "");
        input = input.replace("§a", "");
        input = input.replace("§b", "");
        input = input.replace("§c", "");
        input = input.replace("§d", "");
        input = input.replace("§e", "");
        input = input.replace("§f", "");
        input = input.replace("§k", "");
        input = input.replace("§l", "");
        input = input.replace("§m", "");
        input = input.replace("§n", "");
        input = input.replace("§o", "");
        input = input.replace("§r", "");
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

    final static Charset ENCODING = StandardCharsets.UTF_8;

    /**
     * Read small text files. Doesn't use buffering
     *
     * @param file Path to file
     * @return Lines Lines of file
     * @throws IOException
     */
    public static List<String> readSmallTextFile(String file) throws IOException
    {
        Path path = Paths.get(file);
        return Files.readAllLines(path, ENCODING);
    }

    /**
     * Read large text files. Uses buffering
     *
     * @param file Path to file
     * @return Lines Lines of file
     * @throws IOException
     */
    public static List<String> readLargerTextFile(String file) throws IOException
    {
        Path path = Paths.get(file);
        List<String> tmp = new ArrayList<>();
        try (Scanner scanner = new Scanner(path, ENCODING.name()))
        {
            while (scanner.hasNextLine())
            {
                tmp.add(scanner.nextLine());
            }
        }
        return tmp;
    }

    /**
     * Write small text files. Doesn't use buffering
     *
     * @param lines Lines to write
     * @param file  Path to file
     * @throws IOException
     */
    public static void writeSmallTextFile(String file, List<String> lines) throws IOException
    {
        Path path = Paths.get(file);
        Files.write(path, lines, ENCODING);
    }

    /**
     * Write large text files. Uses buffering
     * @param lines Lines to write
     * @param file Path to file
     * @throws IOException
     */
    public static void writeLargerTextFile(String file, List<String> lines) throws IOException
    {
        Path path = Paths.get(file);
        try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING))
        {
            for (String line : lines)
            {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
