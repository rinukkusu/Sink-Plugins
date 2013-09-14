package de.static_interface.chatplugin.channel;

import org.bukkit.entity.Player;

import java.util.Vector;

public interface Channel
{
    /**
     * Joining a Channel will remove you from the list<br>
     * of excepted players.
     */

    /**
     * @param player Given player will be added to the excepted players
     *               list.
     * @method addExceptedPlayer
     * Will add the player to the excepted players list   <br>
     * so he will be excepted from the messages sent.     <br>
     */
    public void addExceptedPlayer(Player player);

    /**
     * @param player Player to remove from the excepted players list.
     * @method removeExceptedPlayer
     * Removes player from the excepted players list.
     */
    public void removeExceptedPlayer(Player player);

    public void removeExceptedPlayer(String playername);

    /**
     * @return Returns a Vector of players not getting messages   <br>
     *         by the plugin.
     */
    public Vector<Player> getExceptedPlayers();


    /**
     * @param player Player to check for.
     * @return True if player was added to exceptedPlayers, else false.
     */
    public boolean contains(Player player);

    public String getChannelName();

    public void onPlayerLeavesChannel(Channel channel, Player player);
    public void onPlayerJoinsChannel(Channel channel, Player player);
    public String getPrefix();
    public char getCharForCall();

}
