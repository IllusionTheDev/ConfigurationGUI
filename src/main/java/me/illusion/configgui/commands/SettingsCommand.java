package me.illusion.configgui.commands;

import me.illusion.configgui.ConfigurationPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommand implements CommandExecutor {

    private ConfigurationPlugin main;

    public SettingsCommand(ConfigurationPlugin main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You need to be a player to open the settings gui");
            return true;
        }

        main.getMainGUI().getMainMenu().open((Player) sender);
        return true;
    }
}
