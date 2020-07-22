package me.illusion.configgui;

import lombok.Getter;
import me.illusion.configgui.commands.SettingsCommand;
import me.illusion.configgui.gui.MainGUI;
import me.illusion.configgui.gui.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationPlugin extends JavaPlugin {

    @Getter
    private MainGUI mainGUI;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        getCommand("settings").setExecutor(new SettingsCommand(this));
        getLogger().info("Enabled, attempting to load settings soon...");
    }

    @Override
    public void onLoad() {
        getLogger().info("Loading settings...");
        mainGUI = new MainGUI(this);
        getLogger().info("Settings loaded.");
    }
}
