package gg.steve.mc.saphirus.discord.framework;

import gg.steve.mc.saphirus.discord.framework.utils.LogUtil;
import gg.steve.mc.saphirus.discord.framework.yml.Files;
import gg.steve.mc.saphirus.discord.framework.yml.utils.FileManagerUtil;
import gg.steve.mc.saphirus.discord.listener.AuctionListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles setting up the plugin on start
 */
public class SetupManager {
    private static FileManagerUtil fileManager;

    private SetupManager() throws IllegalAccessException {
        throw new IllegalAccessException("Manager class cannot be instantiated.");
    }

    /**
     * Loads the files into the file manager
     */
    public static void setupFiles(FileManagerUtil fm) {
        fileManager = fm;
        Files.CONFIG.load(fm);
        Files.DATA.load(fm);
    }

    public static void registerCommands(JavaPlugin instance) {
    }

    /**
     * Register all of the events for the plugin
     *
     * @param instance Plugin, the main plugin instance
     */
    public static void registerEvents(JavaPlugin instance) {
        PluginManager pm = instance.getServer().getPluginManager();
        pm.registerEvents(new AuctionListener(), instance);
    }

    public static void registerEvent(JavaPlugin instance, Listener listener) {
        instance.getServer().getPluginManager().registerEvents(listener, instance);
    }

    public static void loadPluginCache() {

    }

    public static void shutdownPluginCache() {
    }

    public static FileManagerUtil getFileManagerUtil() {
        return fileManager;
    }
}
