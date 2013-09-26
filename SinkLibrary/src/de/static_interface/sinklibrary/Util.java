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

import java.util.List;

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
}