package me.ifte.sleepplugin.commands;

import me.ifte.sleepplugin.Sleepplugin;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import me.ifte.sleepplugin.messages.sleepMessage;

import java.util.ArrayList;
import java.util.List;

public class sleepCommand implements CommandExecutor {



    public static final List<String> allowedPlayerList = new ArrayList<>();
    public static final List<String> denyedPlayerList = new ArrayList<>();
    public static final List<String> didVote = new ArrayList<>();
    public static final List<Player> oncevoteInitiated = new ArrayList<>();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            Bukkit.getLogger().warning("you cant use sleep command from console");
            return true;
        }
        Player p = (Player) sender;
        if (Sleepplugin.getPlugin().getConfig().getBoolean("vote-sleep")){
            if (p.hasPermission(Sleepplugin.getPlugin().getConfig().getString("player-perms-to-sleep"))){
                if (args.length > 0){
                    if (night(p.getWorld())){
                        if (!oncevoteInitiated.contains(p)){
                            for (Player sendmsg : Bukkit.getOnlinePlayers()){
                                sleepMessage.message(sendmsg);
                                oncevoteInitiated.add(sendmsg);
                            }
                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    if (allowedPlayerList.size() >= denyedPlayerList.size()){
                                        for (Player madedaymsg : Bukkit.getOnlinePlayers()){
                                            madedaymsg.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("making-day-message")));
                                            if (Sleepplugin.getPlugin().getConfig().getBoolean("show-how-many-players-voted-on-successfull") == true){
                                                madedaymsg.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("show-how-many-players-voted-on-successfull-message")) + ": " +allowedPlayerList.size());
                                                madedaymsg.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("show-how-many-players-voted-on-unsuccessfull-message")) + ": " + denyedPlayerList.size());
                                            }
                                            if (Sleepplugin.getPlugin().getConfig().getBoolean("Sounds.night-skip-sound")){
                                                //Sound skipSound = Sound.UI_TOAST_CHALLENGE_COMPLETE;
                                                Sound skipSound = Sound.valueOf(Sleepplugin.getPlugin().getConfig().getString("Sounds.night-skip-sound-name"));
                                                madedaymsg.playSound(madedaymsg.getLocation(), skipSound, 0.4F, 1F);
                                            }
                                        }

                                        if (!Sleepplugin.getPlugin().getConfig().getBoolean("night-skip-animation")){
                                            p.getWorld().setTime(23461L);
                                            p.getWorld().setStorm(false);
                                            p.getWorld().setThundering(false);
                                        }else {
                                            new BukkitRunnable(){

                                                @Override
                                                public void run() {
                                                    World world = p.getWorld();
                                                    long timeWorld = world.getTime();
                                                    double timeRate = 70;

                                                    if(timeWorld >= (1200 - timeRate * 1.5) && timeWorld <= 1200) {
                                                        if(world.hasStorm()){
                                                            world.setStorm(false);
                                                        }
                                                        for (Player player : world.getPlayers()){
                                                            player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                                                        }
                                                        this.cancel();
                                                    } else {
                                                        world.setTime(timeWorld + (int) timeRate);
                                                    }
                                                }
                                            }.runTaskTimer(Sleepplugin.getPlugin(), 1, 1);

                                        }

                                        cleanPlayerName();
                                        oncevoteInitiated.clear();
                                    }else {
                                        for (Player ntmadedaymsg : Bukkit.getOnlinePlayers()){
                                            ntmadedaymsg.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("voting-unsuccessful-message")));
                                            if (Sleepplugin.getPlugin().getConfig().getBoolean("show-how-many-players-voted-on-unsuccessfull") == true){
                                                ntmadedaymsg.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("show-how-many-players-voted-on-successfull-message")) + ": " +allowedPlayerList.size());
                                                ntmadedaymsg.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("show-how-many-players-voted-on-unsuccessfull-message")) + ": " + denyedPlayerList.size());
                                            }
                                        }
                                        cleanPlayerName();
                                        oncevoteInitiated.clear();
                                    }

                                }

                            }.runTaskLater(Sleepplugin.getPlugin(), 460L);
                        }else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("vote-sleep-repeat-denied-message")));
                        }


                    }else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("night-wait-message")));
                    }

                }
                else if (args.length > 1){
                    if (args[0].equalsIgnoreCase("accept") && night(p.getWorld())){
                        if (!didVote.contains(p.getName())){
                            allowedPlayerList.add(p.getName());
                            didVote.add(p.getName());
                            Bukkit.broadcastMessage(ChatColor.GREEN + p.getDisplayName() + " " + ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("if-voted-for-skip-message")));
                        }else {
                            p.sendMessage(ChatColor.DARK_RED + ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("already-voted-message")));
                        }
                    }
                    else if (args[0].equalsIgnoreCase("deny") && night(p.getWorld())){
                        if (!didVote.contains(p.getName())){
                            didVote.add(p.getName());
                            denyedPlayerList.add(p.getName());
                            Bukkit.broadcastMessage(ChatColor.GREEN + p.getDisplayName() + " " + ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("if-denied-for-skip-message")));
                        }else {
                            p.sendMessage(ChatColor.DARK_RED + ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("already-voted-message")));
                        }
                    }
                    else if (args[0].equalsIgnoreCase("reload")){
                        if (p.hasPermission(Sleepplugin.getPlugin().getConfig().getString("admin-perms-to-reload"))){
                            Sleepplugin.getPlugin().reloadConfig();
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("config-reload-message")));
                        }else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("no-permissions-message")));
                        }
                    }
                }
            }else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("no-permissions-message")));
            }
        }else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Sleepplugin.getPlugin().getConfig().getString("vote-sleep-false-message")));
        }
        return true;
    }

    public static boolean night(World world) {
        short time = (short) world.getTime();
        return time < 23460 && time > 12542;
    }

    public static void cleanPlayerName() {
        allowedPlayerList.clear();
        denyedPlayerList.clear();
        didVote.clear();
    }


}
