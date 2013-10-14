package de.static_interface.sinkchat.channel.channels;

import de.static_interface.sinkchat.channel.IPrivateChannel;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.static_interface.sinklibrary.configuration.LanguageConfiguration;

import java.util.Vector;

public class PrivateChannel implements IPrivateChannel{

    String channelIdent = new String();
    Vector<Player> participants = new Vector<>();
    Player starter;

    public PrivateChannel(String channelIdentifier, Player starter, Player... targets)
    {
        channelIdent = channelIdentifier;
        this.starter = starter;
        participants.add(starter);
        for ( Player p : targets ){
            if ( participants.contains(p) ) continue;

            // <starter> has invited you to chat. Chat with <channel identifier>
            p.sendMessage(LanguageConfiguration._("SinkChat.Channels.Private.InvitedToChat").replace("%c", channelIdentifier).replace("%i", starter.getDisplayName()));

        }

    }

    @Override
    public void addPlayer(CommandSender invitor, Player target) {

        if ( participants.contains(target) )
        {
            invitor.sendMessage(LanguageConfiguration._("SinkChat.Channels.Private.HasInvitedToChat.ErrorAlreadyInChat").replace("%t",target.getDisplayName()));
            return;
        }
        if ( Bukkit.getPlayer(target.getName()) == null ){
            invitor.sendMessage(LanguageConfiguration._("SinkChat.Channels.Private.HasInvitedToChat.ErrorNotOnline").replace("%t", target.getDisplayName()));
        }

        // <invitor> invited you to a chat with <conversation starter>
        target.sendMessage(LanguageConfiguration._("SinkChat.Channels.Private.InvitedToChat").replace("%i", ((Player) (invitor)).getDisplayName()).replace("%p", participants.get(0).getDisplayName()));

    }

    @Override
    public void kickPlayer(Player player, Player kicker, String reason) {
        if ( player.equals(kicker) )
        {
            participants.remove(player);
            sendMessage(LanguageConfiguration._("SinkChat.Channels.Private.PlayerLeftCon").replace("%t",player.getDisplayName()).replace("%c",channelIdent));
            return;
        }

        participants.remove(player);
        sendMessage(LanguageConfiguration._("SinkChat.Channels.Private.PlayerKicked").replace("%t",player.getDisplayName()).replace("%c", reason));
    }

    @Override
    public void sendMessage(String message) {
        for ( Player p : participants )
        {
            p.sendMessage(message);
        }
    }

    @Override
    public void registerConversation() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}