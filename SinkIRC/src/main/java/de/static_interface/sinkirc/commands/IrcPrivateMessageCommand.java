package de.static_interface.sinkirc.commands;

import de.static_interface.sinkirc.SinkIRC;
import de.static_interface.sinklibrary.SinkLibrary;
import de.static_interface.sinklibrary.configuration.LanguageConfiguration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jibble.pircbot.User;

public class IrcPrivateMessageCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if ( args.length < 2 )
        {
            sender.sendMessage( ChatColor.translateAlternateColorCodes( '&', LanguageConfiguration._("General.CommandMisused.Arguments.TooFew")));
            return true;
        }

        User target = null;

        for ( User u : SinkIRC.getIRCBot().getUsers(SinkIRC.getMainChannel()) )
        {
            if ( u.getNick().equalsIgnoreCase(args[0]) )
            {
                target = u;
                break;
            }
        }

        if ( target == null )
        {
            sender.sendMessage( LanguageConfiguration._("General.NotOnline").replace("%s", args[0]) );
            return true;
        }

        String message = SinkLibrary.getUser( sender ).getDefaultDisplayName()+": ";

        for ( int x = 1; x < args.length; x++ ) message = message + " " + args[x];

        SinkIRC.getIRCBot().sendMessage( target.getNick(), message );

        return true;
    }
}
