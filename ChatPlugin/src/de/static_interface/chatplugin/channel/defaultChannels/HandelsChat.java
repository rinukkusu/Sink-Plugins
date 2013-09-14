package de.static_interface.chatplugin.channel.defaultChannels;

import de.static_interface.chatplugin.channel.Channel;
import org.bukkit.entity.Player;
import java.util.Vector;

public class HandelsChat implements Channel {

    String PREFIX = new String();


    public HandelsChat(String prefix)
    {
        PREFIX = prefix;
    }


    @Override
    public void addExceptedPlayer(Player player) {
        exceptedPlayers.add(player);
    }

    @Override
    public void removeExceptedPlayer(Player player) {
        exceptedPlayers.remove(player);
    }

    @Override
    public Vector<Player> getExceptedPlayers() {
        return exceptedPlayers;
    }

    @Override
    public boolean contains(Player player) {
        return ( exceptedPlayers.contains(player) );
    }

    @Override
    public String getChannelName() {
        return "Handel";
    }

    @Override
    public String getPrefix()
    {
        return PREFIX;
    }
}
