package gg.steve.mc.saphirus.discord;

import gg.steve.mc.saphirus.discord.bot.DiscordBotUtil;
import gg.steve.mc.saphirus.discord.framework.SetupManager;
import gg.steve.mc.saphirus.discord.framework.utils.LogUtil;
import gg.steve.mc.saphirus.discord.framework.yml.utils.FileManagerUtil;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

public final class SaphirusDiscord extends JavaPlugin {
    private static SaphirusDiscord instance;
    private static DiscordBotUtil botUtil;
    private static DecimalFormat numberFormat = new DecimalFormat("#,###.##");

    @Override
    public void onLoad() {
        instance = this;
        LogUtil.setInstance(instance, true);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        SetupManager.setupFiles(new FileManagerUtil(instance));
        SetupManager.registerCommands(instance);
        SetupManager.registerEvents(instance);
        SetupManager.loadPluginCache();
        DiscordBotUtil.enableBot();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SetupManager.shutdownPluginCache();
    }

    public static SaphirusDiscord getInstance() {
        return instance;
    }

    public static String format(double number) {
        return numberFormat.format(number);
    }
}
