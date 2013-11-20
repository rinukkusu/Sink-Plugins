package de.static_interface.sinkchat.channel;

import org.bukkit.entity.Player;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Vector;

public abstract class IPrivateChannel implements IChannel
{

    /**
     * Add player to existing conversation
     *
     * @param invitor Player who invited target
     * @param target  Player who was invited
     */

    public abstract void addPlayer(Player invitor, Player target);

    /**
     * Kicks a player off a conversation
     *
     * @param player Player to kick
     * @param kicker Player who kicked
     * @param reason Reason for kick.
     */


    public abstract void kickPlayer(Player player, Player kicker, String reason);

    /**
     * Registers conversation at PrivateChannelHandler
     */

    public void registerChannel()
    {
        PrivateChannelHandler.registerChannel(this);
    }

    /**
     * Returns wether a conversation contains a player or not
     *
     * @param player Player to check for
     * @return True if player is in that conversation, else false.
     */

    public abstract boolean contains(Player player);

    /**
     * Returns channel identifier
     *
     * @return channel identifier
     */

    public abstract String getChannelIdentifier();

    /**
     * Returns the player who started that conversation.
     *
     * @return Player who started that conversation
     */
    public abstract Player getStarter();

    @Override
    public void addExceptedPlayer(Player player)
    {
        throw new NotImplementedException();
    }

    @Override
    public void removeExceptedPlayer(Player player)
    {
        throw new NotImplementedException();
    }

    public abstract void setChannelName(String channelName);

    public abstract Vector<Player> getPlayers();
}