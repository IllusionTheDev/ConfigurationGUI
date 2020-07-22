package me.illusion.configgui.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public static FileConfiguration configurationFromFile(File file)
    {
        FileConfiguration cfg = new YamlConfiguration();

        if (!file.exists())
            return null;

        try {
            cfg.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return cfg;

    }
}
