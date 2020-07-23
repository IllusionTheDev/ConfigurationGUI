package me.illusion.configgui.listener;

import me.illusion.configgui.ConfigurationPlugin;
import me.illusion.configgui.data.ConfiguratingPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private ConfigurationPlugin main;

    public ChatListener(ConfigurationPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent e)
    {
        ConfiguratingPlayer cp = main.getPlayerManager().get(e.getPlayer());

        if(cp == null || cp.getCurrentlyConfigurating() == null)
            return;

        e.setCancelled(true);

        if(e.getMessage().equalsIgnoreCase("cancel"))
        {
            cp.getCurrentlyConfigurating().getMenu().open(e.getPlayer());
            return;
        }

        boolean success = cp.getCurrentlyConfigurating().setValue(e.getMessage());

        if(!success)
        {
            e.getPlayer().sendMessage(ChatColor.RED + "Invalid value. Type \"cancel\" to stop.");
            return;
        }

        cp.getCurrentlyConfigurating().getMenu().open(e.getPlayer());
    }
}
