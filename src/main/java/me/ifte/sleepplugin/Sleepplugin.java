package me.ifte.sleepplugin;

import me.ifte.sleepplugin.events.playerBedListen;
import me.ifte.sleepplugin.commands.sleepCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Sleepplugin extends JavaPlugin {


    private static Sleepplugin plugin;
    boolean hasUpdate;


    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;
        hasUpdate = UpdateChecker.hasSpigotUpdate("98976");

        saveDefaultConfig();


        getCommand("sleep").setExecutor(new sleepCommand());
        getServer().getPluginManager().registerEvents(new playerBedListen(), this);
        
        
        
        if(hasUpdate){
            Bukkit.getLogger().warning("Sleepplugin new version is available on Spigot!");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Sleepplugin getPlugin() {
        return plugin;
    }
}
