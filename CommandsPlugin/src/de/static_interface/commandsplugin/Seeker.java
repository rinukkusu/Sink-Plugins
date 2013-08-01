package de.static_interface.commandsplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Seeker implements Listener
{

    public int random = new Random().nextInt(Bukkit.getOnlinePlayers().length);
    public Player[] players = Bukkit.getOnlinePlayers();
    public Player randomPlayer = players[random];
    public Player[] theNotChosen = new Player[23];
    public Player choosenPlayer;

    public Seeker(CommandSender sender)
    {
        int lenght = 0;
        for (Player player : players)
        {
            if (! player.equals(randomPlayer))
            {
                theNotChosen[lenght] = player;
                player.setHealth(1.0);
                lenght++;
            }
            else
            {
                player.sendMessage("Your It!");
                player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                player.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
                player.getInventory().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                choosenPlayer = player;
            }
        }
    }

    public Seeker()
    {

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (choosenPlayer == null)
        {
            return;
        }
        if (event.getPlayer() != choosenPlayer)
        {
            return;
        }

        double x = choosenPlayer.getLocation().getX();
        double y = choosenPlayer.getLocation().getY();
        double z = choosenPlayer.getLocation().getZ();

        Location loc1 = new Location(choosenPlayer.getWorld(), x, y, z);
        Block block1 = loc1.getBlock();
        block1.setType(Material.STONE);

        Location loc2 = new Location(choosenPlayer.getWorld(), x, y + 1, z);
        Block block2 = loc2.getBlock();
        block2.setType(Material.STONE);


        //Replace old Block with AIR
        Location oldLoc1 = event.getFrom();
        Block oldBlock1 = oldLoc1.getBlock();
        if (oldBlock1.getType() == Material.STONE)
        {
            oldBlock1.setType(Material.AIR);
        }

        Location oldLoc2 = new Location(choosenPlayer.getWorld(), oldLoc1.getX(), oldLoc1.getY() + 1, oldLoc1.getZ());
        Block oldBlock2 = oldLoc2.getBlock();
        if (oldBlock2.getType() == Material.STONE)
        {
            oldBlock2.setType(Material.AIR);
        }

    }
}

