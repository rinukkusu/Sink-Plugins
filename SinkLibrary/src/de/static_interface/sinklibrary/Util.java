package de.static_interface.sinklibrary;

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
    /**
     * Format Array to String
     *
     * @param input Input String
     * @param Char  Chat
     * @return If Array = {"s1", "s2", "s3" } and Char = " & " it will return "s1 & s2 & s3"
     */
    public static String formatArrayToString(String[] input, String Char)
    {
        String tmp = "";
        for (String s : input)
        {
            if (tmp.equals(""))
            {
                tmp = s;
                continue;
            }
            tmp = tmp + Char + s;
        }
        return tmp;
    }

    /**
     * Formats a list with names to String.
     *
     * @param names Names
     * @return If names contains "user1", "user2", "user3", it will return "user1, user2 and user3".
     */
    public static String formatPlayerListToString(List<String> names)
    {
        String tmp = "";
        int i = 0;
        for (String s : names)
        {
            i++;
            if (tmp.equals(""))
            {
                tmp = s;
                continue;
            }
            if (i == names.toArray().length)
            {
                tmp = tmp + " and " + s;
                continue;
            }
            tmp = tmp + ", " + s;
        }
        return tmp;
    }

    /**
     * @param input String Input
     * @return True if input is a number
     */
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
     * Read small text files. Doesn't use buffering.
     * Currently unused.
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
     * Read large text files. Uses buffering.
     * Currently unused.
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
     * Write small text files. Doesn't use buffering.
     * Currently unused.
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
     * Write large text files. Uses buffering.
     * Currently unused.
     *
     * @param lines Lines to write
     * @param file  Path to file
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