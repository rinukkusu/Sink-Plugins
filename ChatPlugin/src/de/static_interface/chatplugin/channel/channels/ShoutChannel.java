package de.static_interface.chatplugin.channel.channels;

import de.static_interface.chatplugin.ChatPlugin;
import de.static_interface.chatplugin.channel.ChannelHandler;
import de.static_interface.chatplugin.channel.IChannel;
import de.static_interface.chatplugin.channel.configuration.LanguageHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Vector;

public class ShoutChannel extends JavaPlugin implements IChannel, Listener {

    Vector<Player> exceptedPlayers = new Vector<>();
    String PREFIX = ChatColor.GRAY + "[Shout] " + ChatColor.RESET;
    private char callByChar = '!';
    String permission = "chatplugin.channels.shout";

    public ShoutChannel(char callChar) {
        callByChar = callChar;
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
    public boolean contains(Player player) {
        return exceptedPlayers.contains(player);
    }

    @Override
    public String getChannelName() {
        return "Shout";
    }

    @Override
    public void sendMessage(Player player, String message) {
        if (!(player.hasPermission(this.getPermission()))) {
            player.sendMessage(PREFIX + LanguageHandler.getString("messages.permissions.shout"));
            return;
        }

        String formattedMessage = message.substring(1);
        formattedMessage = this.PREFIX + " [" + ChatPlugin.getGroup(player) + ChatColor.RESET + "] " + ChatPlugin.getDisplayName(player) + ": " + formattedMessage;
        if (player.hasPermission("chatplugin.color")) {
            formattedMessage = this.PREFIX + " [" + ChatPlugin.getGroup(player) + ChatColor.RESET + "] " + ChatPlugin.getDisplayName(player) + ": " + formattedMessage;
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!(this.contains(target))) {
                target.sendMessage(formattedMessage);
            }
        }
    }

    @Override
    public void registerChannel() {
        ChannelHandler.registerChannel(this, "Shout", PREFIX, callByChar);
    }

    public String getPermission() {
        return permission;
    }

}
