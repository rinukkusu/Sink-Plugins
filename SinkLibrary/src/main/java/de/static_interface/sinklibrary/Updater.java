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

/*
 * Updater for Bukkit by h31ix. https://github.com/h31ix/Updater
 *
 * This class provides the means to safely and easily update a plugin, or check to see if it is updated using dev.bukkit.org
 */
package de.static_interface.sinklibrary;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Check dev.bukkit.org to find updates for a given plugin, and download the updates if needed.
 * <p/>
 * <b>VERY, VERY IMPORTANT</b>: Because there are no standards for adding auto-update toggles in your plugin's config, this system provides NO CHECK WITH YOUR CONFIG to make sure the user has allowed auto-updating.
 * <br>
 * It is a <b>BUKKIT POLICY</b> that you include a boolean value in your config that prevents the auto-updater from running <b>AT ALL</b>.
 * <br>
 * If you fail to include this option in your config, your plugin will be <b>REJECTED</b> when you attempt to submit it to dev.bukkit.org.
 * <p/>
 * An example of a good configuration option would be something similar to 'auto-update: true' - if this value is set to false you may NOT run the auto-updater.
 * <br>
 * If you are unsure about these rules, please read the plugin submission guidelines: http://goo.gl/8iU5l
 *
 * @author Gravity, modified by Trojaner
 * @version 2.0
 */

@SuppressWarnings({"UnusedDeclaration", "HardcodedFileSeparator"})
public class Updater
{
    public static final String CONSOLEPREFIX = "[SinkPluginsUpdate] ";
    public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "SinkPluginsUpdate" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    private UpdateType type;
    private String versionName = null;
    private String versionLink = null;
    private String versionType = null;
    private String versionGameVersion = null;

    private URL url = null; // Connecting to RSS
    //private File file; // The plugin's file
    private Thread thread; // Updater thread

    private int id = -1; // Project's Curse ID

    private static final String TITLE_VALUE = "name"; // Gets remote file's title
    private static final String LINK_VALUE = "downloadUrl"; // Gets remote file's download link
    private static final String TYPE_VALUE = "releaseType"; // Gets remote file's release type
    private static final String VERSION_VALUE = "gameVersion"; // Gets remote file's build version
    private static final String QUERY = "/servermods/files?projectIds="; // Path to GET
    private static final String HOST = "https://api.curseforge.com"; // Slugs will be appended to this to get to the project's RSS feed

    private static final String[] NO_UPDATE_TAG = {"-DEV", "-PRE", "-SNAPSHOT"}; // If the version number contains one of these, don't update.
    private static final int BYTE_SIZE = 1024; // Used for downloading files
    private static final String VERSION_PATTERN = " v";

    private String updateFolder;// The folder that downloads will be placed in
    private Updater.UpdateResult result = Updater.UpdateResult.SUCCESS; // Used for determining the outcome of the update process

    /**
     * Gives the dev the result of the update process. Can be obtained by called getResult().
     */
    public enum UpdateResult
    {
        /**
         * The updater found an update, and has readied it to be loaded the next time the server restarts/reloads.
         */
        SUCCESS,
        /**
         * The updater did not find an update, and nothing was downloaded.
         */
        NO_UPDATE,
        /**
         * The server administrator has disabled the updating system
         */
        DISABLED,
        /**
         * The updater found an update, but was unable to download it.
         */
        FAIL_DOWNLOAD,
        /**
         * For some reason, the updater was unable to contact dev.bukkit.org to download the file.
         */
        FAIL_DBO,
        /**
         * When running the version check, the file on DBO did not contain the a version in the format 'vVersion' such as 'v1.0'.
         */
        FAIL_NOVERSION,
        /**
         * The id provided by the plugin running the updater was invalid and doesn't exist on DBO.
         */
        FAIL_BADID,
        /**
         * The server administrator has improperly configured their API key in the configuration
         */
        FAIL_APIKEY,
        /**
         * The updater found an update, but because of the UpdateType being set to NO_DOWNLOAD, it wasn't downloaded.
         */
        UPDATE_AVAILABLE
    }

    /**
     * Allows the dev to specify the type of update that will be run.
     */
    public enum UpdateType
    {
        /**
         * Run a version check, and then if the file is out of date, download the newest version.
         */
        DEFAULT,
        /**
         * Don't run a version check, just find the latest update and download it.
         */
        NO_VERSION_CHECK,
        /**
         * Get information about the version and the download size, but don't actually download anything.
         */
        NO_DOWNLOAD
    }

    /**
     * Initialize the updater
     *
     * @param type Specify the type of update this will be. See {@link UpdateType}
     */
    public Updater(UpdateType type)
    {
        this.type = type;
        id = 66370;
        updateFolder = Bukkit.getUpdateFolder();

        //this.config.addDefault("api-key", "PUT_API_KEY_HERE");

        if ( !SinkLibrary.getSettings().isUpdaterEnabled() )
        {
            result = UpdateResult.DISABLED;
            return;
        }

        try
        {
            url = new URL(Updater.HOST + Updater.QUERY + id);
        }
        catch ( MalformedURLException e )
        {
            SinkLibrary.getCustomLogger().severe(CONSOLEPREFIX + "The project ID provided for updating, " + id + " is invalid.");
            result = UpdateResult.FAIL_BADID;
            e.printStackTrace();
        }

        thread = new Thread(new UpdateRunnable());
        thread.start();
    }

    /**
     * Get the result of the update process.
     */
    public Updater.UpdateResult getResult()
    {
        waitForThread();
        return result;
    }

    /**
     * Get the latest version's release type (release, beta, or alpha).
     */
    public String getLatestType()
    {
        waitForThread();
        return versionType;
    }

    /**
     * Get the latest version's game version.
     */
    public String getLatestGameVersion()
    {
        waitForThread();
        return versionGameVersion;
    }

    /**
     * Get the latest version's name.
     */
    public String getLatestName()
    {
        waitForThread();
        return versionName;
    }

    /**
     * Get the latest version's file link.
     */
    public String getLatestFileLink()
    {
        waitForThread();
        return versionLink;
    }

    /**
     * As the result of Updater output depends on the thread's completion, it is necessary to wait for the thread to finish
     * before allowing anyone to check the result.
     */
    private void waitForThread()
    {
        if ( (thread != null) && thread.isAlive() )
        {
            try
            {
                thread.join();
            }
            catch ( final InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save an update from dev.bukkit.org into the server's update folder.
     */
    private void saveFile(File folder, String u)
    {
        if ( !folder.exists() )
        {
            folder.mkdir();
        }
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        String tempFile = "temp.zip";
        File downloadFile = new File(folder.getAbsolutePath() + File.separator + tempFile);
        try
        {
            // Download the file
            final URL url = new URL(u);
            final int fileLength = url.openConnection().getContentLength();
            in = new BufferedInputStream(url.openStream());
            fout = new FileOutputStream(downloadFile);

            final byte[] data = new byte[Updater.BYTE_SIZE];
            int count;
            SinkLibrary.getCustomLogger().info(CONSOLEPREFIX + "About to download a new update: " + versionName);
            long downloaded = 0;
            while ( (count = in.read(data, 0, Updater.BYTE_SIZE)) != -1 )
            {
                downloaded += count;
                fout.write(data, 0, count);
                final int percent = (int) ((downloaded * 100) / fileLength);
                if ( ((percent % 10) == 0) )
                {
                    SinkLibrary.getCustomLogger().info(CONSOLEPREFIX + "Downloading update: " + percent + "% of " + fileLength + " bytes.");
                }
            }
            //Just a quick check to make sure we didn't leave any files from last time...
            for ( final File xFile : new File(SinkLibrary.getCustomDataFolder().getParent(), updateFolder).listFiles() )
            {
                xFile.delete();
            }
            // Check to see if it's a zip file, if it is, unzip it.
            final File dFile = new File(folder.getAbsolutePath() + File.separator + tempFile);
            unzip(dFile);
        }
        catch ( final Exception ex )
        {
            SinkLibrary.getCustomLogger().log(Level.WARNING, CONSOLEPREFIX + "The auto-updater tried to download a new update, but was unsuccessful.", ex);
            result = Updater.UpdateResult.FAIL_DOWNLOAD;
        }
        finally
        {
            try
            {
                if ( in != null )
                {
                    in.close();
                }
                if ( fout != null )
                {
                    fout.close();
                }
            }
            catch ( final Exception ignored )
            {
            }
        }
    }

    /**
     * Part of Zip-File-Extractor, modified by Gravity for use with Bukkit
     */
    private void unzip(File zipFile)
    {
        byte[] buffer = new byte[1024];

        try
        {
            File outputFolder = zipFile.getParentFile();
            if ( !outputFolder.exists() )
            {
                outputFolder.mkdir();
            }

            ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry ze = zis.getNextEntry();

            while ( ze != null )
            {

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                if ( !newFile.exists() && newFile.getName().toLowerCase().endsWith(".jar") )
                {
                    ze = zis.getNextEntry();
                    continue;
                }

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ( (len = zis.read(buffer)) > 0 )
                {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            zipFile.delete();

        }
        catch ( IOException ex )
        {
            SinkLibrary.getCustomLogger().warning("The auto-updater tried to unzip a new update file, but was unsuccessful.");
            result = Updater.UpdateResult.FAIL_DOWNLOAD;
            ex.printStackTrace();
        }
    }

    /**
     * Check to see if the program should continue by evaluation whether the plugin is already updated, or shouldn't be updated
     */
    @SuppressWarnings("DynamicRegexReplaceableByCompiledPattern")
    private boolean checkVersion(String title)
    {
        if ( type != UpdateType.NO_VERSION_CHECK )
        {
            final String version = SinkLibrary.getVersion();
            if ( title.split(VERSION_PATTERN).length == 2 )
            {
                final String remoteVersion = title.split(VERSION_PATTERN)[1].split(" ")[0]; // Get the newest file's version number
                int remVer, curVer = 0;
                try
                {
                    remVer = calVer(remoteVersion);
                    curVer = calVer(version);
                }
                catch ( final NumberFormatException ignored )
                {
                    remVer = -1;
                }
                if ( hasTag(version) || version.equalsIgnoreCase(remoteVersion) || (curVer >= remVer) )
                {
                    // We already have the latest version, or this build is tagged for no-update
                    result = Updater.UpdateResult.NO_UPDATE;
                    return false;
                }
            }
            else
            {
                // The file's name did not contain the string 'vVersion'
                SinkLibrary.getCustomLogger().warning("Couldn't get latest version of plugin.");
                SinkLibrary.getCustomLogger().warning("Please notify the author of this error.");
                result = Updater.UpdateResult.FAIL_NOVERSION;
                return false;
            }
        }
        return true;
    }

    /**
     * Used to calculate the version string as an Integer
     *
     * @throws NumberFormatException
     */
    private Integer calVer(String s)
    {
        if ( s.contains(".") )
        {
            s.replace(".", "");
            return Integer.parseInt(s);
        }
        return Integer.parseInt(s) * 10;
    }

    /**
     * Evaluate whether the version number is marked showing that it should not be updated by this program
     */
    private boolean hasTag(String version)
    {
        for ( final String s : Updater.NO_UPDATE_TAG )
        {
            if ( version.contains(s) )
            {
                return true;
            }
        }
        return false;
    }

    private boolean isUpdateAvailable()
    {
        try
        {
            final URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);

            String version = SinkLibrary.getVersion();

            conn.addRequestProperty("User-Agent", "SinkLibrary/v" + version + " (modified by Trojaner)");

            conn.setDoOutput(true);

            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String response = reader.readLine();

            final JSONArray array = (JSONArray) JSONValue.parse(response);

            if ( array.size() == 0 )
            {
                SinkLibrary.getCustomLogger().warning("The updater could not find any files for the project id " + id);
                result = UpdateResult.FAIL_BADID;
                return false;
            }

            versionName = (String) ((JSONObject) array.get(array.size() - 1)).get(Updater.TITLE_VALUE);
            versionLink = (String) ((JSONObject) array.get(array.size() - 1)).get(Updater.LINK_VALUE);
            versionType = (String) ((JSONObject) array.get(array.size() - 1)).get(Updater.TYPE_VALUE);
            versionGameVersion = (String) ((JSONObject) array.get(array.size() - 1)).get(Updater.VERSION_VALUE);

            return true;
        }
        catch ( final IOException e )
        {
            if ( e.getMessage().contains("HTTP response code: 403") )
            {
                SinkLibrary.getCustomLogger().warning("dev.bukkit.org rejected the API key provided in plugins/Updater/config.yml");
                SinkLibrary.getCustomLogger().warning("Please double-check your configuration to ensure it is correct.");
                result = UpdateResult.FAIL_APIKEY;
            }
            else
            {
                SinkLibrary.getCustomLogger().warning("The updater could not contact dev.bukkit.org for updating.");
                SinkLibrary.getCustomLogger().warning("If you have not recently modified your configuration and this is the first time you are seeing this message, the site may be experiencing temporary downtime.");
                result = UpdateResult.FAIL_DBO;
            }
            e.printStackTrace();
            return false;
        }
    }

    private class UpdateRunnable implements Runnable
    {
        UpdateRunnable() {}

        @Override
        public void run()
        {
            if ( url != null )
            {
                // Obtain the results of the project's file feed
                if ( isUpdateAvailable() )
                {
                    if ( checkVersion(versionName) )
                    {
                        if ( (versionLink != null) && (type != UpdateType.NO_DOWNLOAD) )
                        {
                            // If it's a zip file, it shouldn't be downloaded as the plugin's name
                            //final String[] split = Updater.this.versionLink.split("/");
                            //name = split[split.length - 1];
                            saveFile(new File(SinkLibrary.getCustomDataFolder().getParent(), updateFolder), versionLink);
                        }
                        else
                        {
                            result = UpdateResult.UPDATE_AVAILABLE;
                        }
                    }
                }
            }
        }
    }
}