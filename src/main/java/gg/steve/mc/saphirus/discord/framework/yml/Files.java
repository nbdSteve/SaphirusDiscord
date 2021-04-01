package gg.steve.mc.saphirus.discord.framework.yml;

import gg.steve.mc.saphirus.discord.framework.yml.utils.FileManagerUtil;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public enum Files {
    // generic
    CONFIG("conf.yml");

    private final String path;

    Files(String path) {
        this.path = path;
    }

    public void load(FileManagerUtil fileManager) {
        fileManager.add(name(), this.path);
    }

    public YamlConfiguration get() {
        return FileManagerUtil.get(name());
    }

    public void save() {
        FileManagerUtil.save(name());
    }

    public static void reload() {
        FileManagerUtil.reload();
    }
}
