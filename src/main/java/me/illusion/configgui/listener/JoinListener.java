package me.illusion.configgui.listener;

import me.illusion.configgui.ConfigurationPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private ConfigurationPlugin main;

    public JoinListener(ConfigurationPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e)
    {
        main.getPlayerManager().register(e.getPlayer().getUniqueId());
    }
}
