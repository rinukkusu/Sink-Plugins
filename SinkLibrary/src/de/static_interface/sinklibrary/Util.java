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

package de.static_interface.sinklibrary;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Util
{
    /**
     * Backup a file
     *
     * @param file   File to backup
     * @param notify Send message to Console?
     * @throws java.lang.RuntimeException When backup fails
     */
    public static void backupFile(File file, boolean notify) throws IOException
    {
        if ( notify ) Bukkit.getLogger().log(Level.INFO, "Creating backup of " + file + "...");

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY-hh.mm");
        String date = format.format(new Date());

        Path sourcePath = Paths.get(file.getAbsolutePath());
        Path targetPath = Paths.get(file.getPath() + "." + date + ".backup");
        try
        {
            Files.copy(sourcePath, targetPath, REPLACE_EXISTING);
        }
        catch ( IOException e )
        {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't backup file: " + file.getAbsolutePath());
            throw e;
        }
    }

    /**
     * Format Array to String
     *
     * @param input Input String
     * @param Char  Chat
     * @return If Array = {"s1", "s2", "s3" } and Char = " & " it will return "s1 & s2 & s3"
     */
    public static String formatArrayToString(Object[] input, String Char)
    {
        String tmp = "";
        for ( Object s : input )
        {
            if ( tmp.equals("") )
            {
                tmp = (String) s;
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
        for ( String s : names )
        {
            i++;
            if ( tmp.equals("") )
            {
                tmp = s;
                continue;
            }
            if ( i == names.toArray().length )
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
        catch ( NumberFormatException ex )
        {
            return false;
        }
        return true;
    }
}
