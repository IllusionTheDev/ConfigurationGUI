package me.illusion.configgui;

import lombok.Getter;
import me.illusion.configgui.commands.SettingsCommand;
import me.illusion.configgui.data.PlayerManager;
import me.illusion.configgui.gui.MainGUI;
import me.illusion.configgui.gui.menu.MenuListener;
import me.illusion.configgui.listener.ChatListener;
import me.illusion.configgui.listener.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationPlugin extends JavaPlugin {

    @Getter
    private MainGUI mainGUI;

    @Getter
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);

        getCommand("settings").setExecutor(new SettingsCommand(this));

        playerManager = new PlayerManager();

        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            getLogger().info("Loading settings...");
            mainGUI = new MainGUI(this);
            getLogger().info("Settings loaded.");
        }, 10);

    }

}
