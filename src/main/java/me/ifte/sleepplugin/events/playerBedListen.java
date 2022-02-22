package me.ifte.sleepplugin.events;

import me.ifte.sleepplugin.Sleepplugin;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class playerBedListen implements Listener {

    @EventHandler
    public static void onBedRightClick(PlayerBedEnterEvent e){
        Player p = e.getPlayer();
        if (Sleepplugin.getPlugin().getConfig().getBoolean("if-player-bed-enter-use-command-sleep")){

            p.chat("/sleep");
            e.setCancelled(true);
        }
    }


}
